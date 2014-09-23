package iii.org.tw.auth.controller;
 
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
import org.scribe.builder.api.FacebookApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
 
@Path("/facebook")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/facebook", description = "Smart Tourism Facebook OAuth2 Api")
public class FacebookController2 extends OAuth2Template2 {
	
	
	Logger logger = LoggerFactory.getLogger(FacebookController2.class);

	
	//http://wess.com.tw:8081/SmartTourismApi/api/v1/facebook
	@Override
	@POST
    @ApiOperation(value = "facebookCallBack",notes="facebookCallBack" , response = OAuth2Model.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
	public String callback(@ApiParam(required=true,value="Code Json")OAuth2ModelIn body, @Context HttpServletRequest httpRequest) throws SmartTourismException {
	

		OAuth2Conf oAuth2Conf = new OAuth2Conf(FacebookApi.class,"584534878292732","846e804711d5c4194afd8aa58c10ab28","http://www.vztaiwan.com/auth/signIn","email");
		//OAuth2Conf oAuth2Conf = new OAuth2Conf(FacebookApi.class,"430696250402812","83625ac0fb0677b66f2328c071160d0d","http://wess.com.tw:8081/SmartTourismApi/api/v1/facebook","email");
		oAuth2Conf.setUserInfoApi("https://graph.facebook.com/me");
		oAuth2Conf.setHostname(httpRequest.getServerName());

		OAuth2Model oAuth2Model = null;
		
		if (!StringUtils.isEmpty(body.getAccessToken())) {//手機
			oAuth2Model = getUserTokenByOAuth2(SOCIAL.FACEBOOK, body.getAccessToken(), oAuth2Conf, true);
		} else {//Web
			oAuth2Model = getUserTokenByOAuth2(SOCIAL.FACEBOOK, body.getCode(), oAuth2Conf, false);
		}
		
		
		
		Gson gson = new Gson();
    	String json = gson.toJson(oAuth2Model);

        return json;
	}
	
	
	
    

}