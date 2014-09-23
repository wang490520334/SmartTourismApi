package iii.org.tw.auth.controller;
 
import iii.org.tw.auth.OAuth2Conf;
import iii.org.tw.auth.OAuth2Template;
import iii.org.tw.auth.OAuth2UserInfo;
import iii.org.tw.auth.SOCIAL;
import iii.org.tw.model.ContentModel;
import iii.org.tw.util.SmartTourismErrorMsg;
import iii.org.tw.util.SmartTourismException;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.scribe.builder.api.FacebookApi;
import org.scribe.model.Token;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
 
@Path("/facebookDeprecated")
@Produces(MediaType.APPLICATION_JSON)
//@Api(value = "/facebookDeprecated", description = "Smart Tourism Facebook OAuth2 Api")
public class FacebookController extends OAuth2Template {
	
	
	Logger logger = LoggerFactory.getLogger(FacebookController.class);

	
	//http://wess.com.tw:8081/SmartTourismApi/api/v1/facebook
	@Override
	@GET
    @ApiOperation(value = "facebookCallBack",notes="facebookCallBack" , response = ContentModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
	public String callback(@ApiParam(value="error")@QueryParam("error") String error, @ApiParam(value="code")@QueryParam("code") String code) throws SmartTourismException {
	
		
		OAuth2Conf oAuth2Conf = new OAuth2Conf(FacebookApi.class,"430696250402812","83625ac0fb0677b66f2328c071160d0d","http://wess.com.tw:8081/SmartTourismApi/api/v1/facebook","email");
		oAuth2Conf.setUserInfoApi("https://graph.facebook.com/me");
		String userToken = getUserTokenByOAuth2(SOCIAL.FACEBOOK, error, code, oAuth2Conf);
		


    	Gson gson = new Gson();
    	String json = gson.toJson(new ContentModel(userToken));

        return json;
	}
	
	
	
    

}