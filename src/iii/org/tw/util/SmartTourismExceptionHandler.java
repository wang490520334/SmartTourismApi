package iii.org.tw.util;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;

@Provider
public class SmartTourismExceptionHandler implements ExceptionMapper<SmartTourismException> 
{
    @Override
    public Response toResponse(SmartTourismException exception) 
    {
    	
    	Gson gson = new Gson();
    	String json = gson.toJson(new SmartTourismErrorMsg(exception.getMessage()));
    	
        return Response.status(400).entity(json).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8" ).build();  
    }
    

}
