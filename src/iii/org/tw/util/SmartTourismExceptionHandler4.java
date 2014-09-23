package iii.org.tw.util;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

@Provider
public class SmartTourismExceptionHandler4 implements ExceptionMapper<JsonSyntaxException> 
{
    @Override
    public Response toResponse(JsonSyntaxException exception) 
    {
    	
    	exception.printStackTrace();
    	
    	Gson gson = new Gson();
    	String json = gson.toJson(new SmartTourismErrorMsg("Json資料格式錯誤  : " + exception.getMessage()));
    	
        return Response.status(400).entity(json).header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON+"; charset=UTF-8" ).build();  
    }
    

}
