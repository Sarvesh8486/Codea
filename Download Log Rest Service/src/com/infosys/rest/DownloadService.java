package com.infosys.rest;

import javax.websocket.server.PathParam;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/download")
public class DownloadService {

	@GET
	@Path("/url/{u}")
	@Produces(MediaType.TEXT_PLAIN)
	public String sayXMLHello(@PathParam("u") String url) {
		System.out.println("absad");
		return url + " Stark";
	}
	
}
