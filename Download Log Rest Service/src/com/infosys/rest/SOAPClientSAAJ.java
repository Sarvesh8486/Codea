package com.infosys.rest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Iterator;

import javax.xml.soap.*;

import org.apache.commons.codec.binary.Base64;

public class SOAPClientSAAJ {


    String username = "";
    String password = "";
    String soapEndpointUrl = "https://adc-fap1556-fa-ext.oracledemos.com/publicFinancialCommonErpIntegration/ErpIntegrationService?wsdl";
    String soapAction = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/downloadExportOutput";
    
    public SOAPClientSAAJ() {
        callSoapWebService(soapEndpointUrl, soapAction);
	}
    
    private void createSoapEnvelope(SOAPMessage soapMessage) throws SOAPException {
        SOAPPart soapPart = soapMessage.getSOAPPart();

        String myNamespace = "types";
        String myNamespaceURI = "http://xmlns.oracle.com/apps/financials/commonModules/shared/model/erpIntegrationService/types/";

        SOAPEnvelope envelope = soapPart.getEnvelope();
        envelope.addNamespaceDeclaration(myNamespace, myNamespaceURI);
        SOAPBody soapBody = envelope.getBody();
        SOAPElement downloadExportOutput = soapBody.addChildElement("downloadExportOutput", myNamespace);
        SOAPElement requestId = downloadExportOutput.addChildElement("requestId", myNamespace);
        
        requestId.addTextNode("123456");
        
    }

    private void callSoapWebService(String soapEndpointUrl, String soapAction) {
        try {
            // Create SOAP Connection
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();

            // Send SOAP Message to SOAP Server
            SOAPMessage soapResponse = soapConnection.call(createSOAPRequest(soapAction), soapEndpointUrl);
            Iterator<AttachmentPart> it = soapResponse.getAttachments();
            while(it.hasNext()){
            	AttachmentPart part = it.next(); 
            	InputStream stream = part.getBase64Content();
            	FileOutputStream out = new FileOutputStream(new File("D://s.zip"));
            	byte[] buffer = new byte[1024];
            	int len;
            	while ((len = stream.read(buffer)) != -1) {
            	    out.write(buffer, 0, len);
            	}
            }
            
            // Print the SOAP Response
            System.out.println("Response SOAP Message:");
            //soapResponse.writeTo(System.out);
            //System.out.println();

            soapConnection.close();
        } catch (Exception e) {
            System.err.println("\nError occurred while sending SOAP Request to Server!\nMake sure you have the correct endpoint URL and SOAPAction!\n");
            e.printStackTrace();
        }
    }

    private SOAPMessage createSOAPRequest(String soapAction) throws Exception {
        MessageFactory messageFactory = MessageFactory.newInstance();
        SOAPMessage soapMessage = messageFactory.createMessage();

        createSoapEnvelope(soapMessage);

        MimeHeaders headers = soapMessage.getMimeHeaders();
        headers.addHeader("SOAPAction", soapAction);
        headers.addHeader("Content-Type", "text/xml;charset=UTF-8");
        byte[] encodedPwd = (username+":"+password).getBytes();
        String pwd = Base64.encodeBase64String(encodedPwd);
        headers.addHeader("Authorization", "Basic "+pwd);

        soapMessage.saveChanges();

        /* Print the request message, just for debugging purposes */
        System.out.println("Request SOAP Message:");
        soapMessage.writeTo(System.out);
        System.out.println("\n");

        return soapMessage;
    }

}
