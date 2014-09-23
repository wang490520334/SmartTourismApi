package iii.org.tw.auth.controller;
 
import iii.org.tw.auth.Google2Api;
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
 
@Path("/googleDeprecated")
@Produces(MediaType.APPLICATION_JSON)
//@Api(value = "/googleDeprecated", description = "Smart Tourism Google OAuth2 Api")
public class GoogleController extends OAuth2Template {
	
	
	Logger logger = LoggerFactory.getLogger(GoogleController.class);

	
	//http://wess.com.tw:8081/SmartTourismApi/api/v1/google
	@Override
	@GET
    @ApiOperation(value = "google",notes="google" , response = ContentModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
	public String callback(@ApiParam(value="error")@QueryParam("error") String error, @ApiParam(value="code")@QueryParam("code") String code) throws SmartTourismException {

		
		OAuth2Conf oAuth2Conf = new OAuth2Conf(Google2Api.class,"440874335687-2dalqmvrfft2kjae02k1g1u8vlh4gm3u.apps.googleusercontent.com","5I2gQGnVLreVEB3xh2yA1daz","http://test.vztaiwan.com.tw:42074/O2OTourism/signIn","openid email");
		//OAuth2Conf oAuth2Conf = new OAuth2Conf(Google2Api.class,"906813512989.apps.googleusercontent.com","Zv2r-gphEWb9gglJ80k44WvE","http://wess.com.tw:8081/SmartTourismApi/api/v1/google","openid email");
		oAuth2Conf.setUserInfoApi("https://www.googleapis.com/oauth2/v1/userinfo");
		String userToken = getUserTokenByOAuth2(SOCIAL.GOOGLE, error, code, oAuth2Conf);
		
		
    	Gson gson = new Gson();
    	String json = gson.toJson(new ContentModel(userToken));

        return json;
	}
	
	
	
    

}