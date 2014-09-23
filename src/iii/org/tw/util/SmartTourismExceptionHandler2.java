package iii.org.tw.util;

import java.sql.Connection;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.stream.MalformedJsonException;

@Provider
public class SmartTourismExceptionHandler2 implements ExceptionMapper<RuntimeException> 
{
    @Override
    public Response toResponse(RuntimeException exception) 
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
    	String json = gson.toJson(new SmartTourismErrorMsg("程式發生 RuntimeException，請洽系統設計師"));
    	
        return Response.status(500).entity(json).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8" ).build();  
    }
    

}
