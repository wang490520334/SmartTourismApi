package iii.org.tw.auth.controller;
 
import iii.org.tw.auth.Google2Api;
import iii.org.tw.auth.OAuth2Conf;
import iii.org.tw.auth.OAuth2Template2;
import iii.org.tw.auth.SOCIAL;
import iii.org.tw.model.OAuth2Model;
import iii.org.tw.model.OAuth2ModelIn;
import iii.org.tw.util.SmartTourismErrorMsg;
import iii.org.tw.util.SmartTourismException;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
 
@Path("/google")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/google", description = "Smart Tourism Google OAuth2 Api")
public class GoogleController2 extends OAuth2Template2 {
	
	
	Logger logger = LoggerFactory.getLogger(GoogleController2.class);

	
	//http://wess.com.tw:8081/SmartTourismApi/api/v1/google
	@Override
	@POST
    @ApiOperation(value = "google",notes="google" , response = OAuth2Model.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
	public String callback(@ApiParam(required=true,value="Code Json")OAuth2ModelIn body, @Context HttpServletRequest httpRequest) throws SmartTourismException {
		


		
		OAuth2Conf oAuth2Conf = new OAuth2Conf(Google2Api.class,"832401982614.apps.googleusercontent.com","GDUonUR8wVtsYIYB3Iiu0Xbe","http://www.vztaiwan.com/auth/signIn","openid email");
		//OAuth2Conf oAuth2Conf = new OAuth2Conf(Google2Api.class,"906813512989.apps.googleusercontent.com","Zv2r-gphEWb9gglJ80k44WvE","http://wess.com.tw:8081/SmartTourismApi/api/v1/google","openid email");
		oAuth2Conf.setUserInfoApi("https://www.googleapis.com/oauth2/v1/userinfo");
		
		OAuth2Model oAuth2Model = null;
		
		if (!StringUtils.isEmpty(body.getAccessToken())) {//手機
			oAuth2Model = getUserTokenByOAuth2(SOCIAL.GOOGLE, body.getAccessToken(), oAuth2Conf, true);
		} else {//web
			oAuth2Model = getUserTokenByOAuth2(SOCIAL.GOOGLE, body.getCode(), oAuth2Conf, false);
		}
		
		
    	Gson gson = new Gson();
    	String json = gson.toJson(oAuth2Model);

        return json;
	}
	
	
	
    

}