package iii.org.tw.util;

import java.sql.Connection;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import com.google.gson.Gson;

@Provider
public class ApiExceptionHandler implements ExceptionMapper<ApiException> 
{
    @Override
    public Response toResponse(ApiException exception) 
    {
    	
    	try {
    	
	    	Connection conn = DbUtil.localCon.get();
	    	if (conn != null && !conn.isClosed()) {
	    		conn.close();
	    	}
    	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	
    	
    	Gson gson = new Gson();
    	String json = gson.toJson(new SmartTourismErrorMsg(Integer.toString(exception.getCode()), exception.getMessage()));
    	
        return Response.status(exception.getCode()).entity(json).build();  
    }
    

}
