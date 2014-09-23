package iii.org.tw.util;

import java.sql.Connection;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;

@Provider
public class SmartTourismExceptionHandler3 implements ExceptionMapper<Exception> 
{
    @Override
    public Response toResponse(Exception exception) 
    {
    	
    	try {
        	
	    	Connection conn = DbUtil.localCon.get();
	    	if (conn != null && !conn.isClosed()) {
	    		conn.close();
	    	}
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	exception.printStackTrace();
    	
    	Gson gson = new Gson();
    	String json = gson.toJson(new SmartTourismErrorMsg("500", "程式發生例外  : " + exception.getMessage()));
    	
        return Response.status(500).entity(json).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8" ).build();  
    }
    

}
