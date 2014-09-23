package iii.org.tw.controller;

import iii.org.tw.service.Preprocess;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
 
@Path("/SolrProcess")
public class API {
	/*
	 * servicetype: 0 = class,shopping -> using product table; 1 = service -> using service table
	 * status: 0 = don��t pre-processing ; 1 = pre-processing
	 */
	
	
	@GET
    @Path("/{servicetype}/{status}")
	@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
	
    public String analysis(@PathParam("servicetype") int servicetype, @PathParam("status") int status) throws Exception 
    { 
		
        logger.info("servicetype={}", servicetype);
        logger.info("status={}", status);
        
        
        new Preprocess(servicetype,status).start();
        
        //results = SolrIndexing.main(servicetype,status,tablename);
        
		//return results;
        return "true";


	//	String results = Classification.
    }
	
	
    final static Logger logger = LoggerFactory.getLogger(API.class);

	
	
}
