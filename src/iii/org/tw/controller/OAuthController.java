package iii.org.tw.controller;
 
import iii.org.tw.auth.SOCIAL;
import iii.org.tw.model.ContentModel;
import iii.org.tw.model.FacebookAnsonPayload;
import iii.org.tw.model.OAuth2Model;
import iii.org.tw.service.LdapService;
import iii.org.tw.util.DbUtil;
import iii.org.tw.util.SmartTourismErrorMsg;
import iii.org.tw.util.SmartTourismException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.TwitterApi;
import org.scribe.exceptions.OAuthException;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
 
@Path("/oauth")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/oauth", description = "Smart Tourism OAuth Api")
public class OAuthController {
	
	
	Logger logger = LoggerFactory.getLogger(OAuthController.class);
	
	
//	
//    /**
//     * http://wess.com.tw:8081/SmartTourismApi/api/v1/oauth2/googleCallBack
//     */
//    @GET
//    @Path("/googleCallBack")
//    @ApiOperation(value = "googleCallBack",notes="googleCallBack" , response = ContentModel.class)
//    @ApiResponses(value = {
//			  @ApiResponse(code = 500, message = "Internal Error"),
//			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
//			})
//    public String googleCallBack(@ApiParam(value="error")@QueryParam("error") String error, @ApiParam(value="code")@QueryParam("code") String code) throws SmartTourismException {
//    	
//	    logger.info("-----googleCallBack-----");
//	    logger.info(error);
//	    logger.info(code);
//
//
//	    if (error != null) {
//	    	throw new SmartTourismException("授權失敗 或 使用者拒絕");
//	    } 
//	    
//	    
//	    OAuthService service = new ServiceBuilder()
//        .provider(Google2Api.class)
//	        .apiKey("906813512989.apps.googleusercontent.com")
//	        .apiSecret("Zv2r-gphEWb9gglJ80k44WvE")
//	        .callback("http://wess.com.tw:8081/SmartTourismApi/api/v1/oauth2/googleCallBack")
//	        .scope("openid email")
//	        .build();
//	    Token token = service.getAccessToken(null, new Verifier(code));	
//
//		
//		OAuthRequest request = new OAuthRequest(Verb.GET, "https://www.googleapis.com/oauth2/v1/userinfo");
//	    service.signRequest(token, request);
//	    Response response = request.send();
//	    System.out.println(response.getCode());
//	    System.out.println(response.getBody());
//	    
//	    JsonElement jelement = new JsonParser().parse(response.getBody());
//	    JsonObject  jobject = jelement.getAsJsonObject();
//	    String email = jobject.get("email").getAsString();
//		String userId = jobject.get("id").getAsString();
//
//	
//		String userToken = OAuth2Controller.getUserToken(SOCIAL.GOOGLE, userId, email);	
//
//    	Gson gson = new Gson();
//    	String json = gson.toJson(new ContentModel(userToken));
//
//        return json;
//
//    }    
//        
//
//
//    
//    /**
//     * http://wess.com.tw:8081/SmartTourismApi/api/v1/oauth2/facebookCallBack
//     */
//    @GET
//    @Path("/facebookCallBack")
//    @ApiOperation(value = "facebookCallBack",notes="facebookCallBack" , response = ContentModel.class)
//    @ApiResponses(value = {
//			  @ApiResponse(code = 500, message = "Internal Error"),
//			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
//			})
//    public String facebookCallBack(@ApiParam(value="error")@QueryParam("error") String error, @ApiParam(value="code")@QueryParam("code") String code) throws SmartTourismException {
//    	
//    	//SUCCESS: http://wess.com.tw:8081/SmartTourismApi/api/v1/oauth2/facebookCallBack?error=access_denied&error_code=200&error_description=Permissions+error&error_reason=user_denied#_=_
//	    //ERROR: http://wess.com.tw:8081/SmartTourismApi/api/v1/oauth2/facebookCallBack?code=AQClvkSTdyb_m0O-Z2lq4cqeyKmdwVFq829jfLLMrOp-skDT9SxLjGSD8n2zgLEO_PJrrEwkp8TJi6Cjs0cYNRR7UNwdrtTfdKaNbIYLCG31csiqAHotFjjPWrkZH_8PSE_EVj9iwxoV2Ah6Z7xZYcWlvR_LjoD-4-cJ4NfavW3LdywvsWl7sSYbWXoxnm1rSAutZlDhicDeL9V8QHTFYgp3cJQdZ7kSTxjYxXSw1_zzGPpKUYV8elhhFw50MgGiIVlsnU5FZyRiB-2inIle6RyZi9HqWKZe8JG5Olh-zx_0pN8ekOmTaV1uO_UAK__-PsQ#_=_
//	    
//    	
//    	
//	    logger.info("-----googleCallBack-----");
//	    logger.info(error);
//	    logger.info(code);
//	    
//	    if (error != null) {
//	    	throw new SmartTourismException("授權失敗 或 使用者拒絕");
//	    } 
//	    
//	    OAuthService service = new ServiceBuilder()
//        .provider(FacebookApi.class)
//	        .apiKey("430696250402812")
//	        .apiSecret("83625ac0fb0677b66f2328c071160d0d")
//	        .callback("http://wess.com.tw:8081/SmartTourismApi/api/v1/oauth2/facebookCallBack")
//	        .scope("email")
//	        .build();
//	    Token token = service.getAccessToken(null, new Verifier(code));	
//		//String accessToken = token.getToken();
//		
//		OAuthRequest request = new OAuthRequest(Verb.GET, "https://graph.facebook.com/me");
//	    service.signRequest(token, request);
//	    Response response = request.send();
//	    System.out.println(response.getCode());
//	    System.out.println(response.getBody());
//	    
//	    
//	    
//	    JsonElement jelement = new JsonParser().parse(response.getBody());
//	    JsonObject  jobject = jelement.getAsJsonObject();
//	    String email = jobject.get("email").getAsString();
//		String userId = jobject.get("id").getAsString();
//
//	
//		String userToken = OAuth2Controller.getUserToken(SOCIAL.FACEBOOK, userId, email);	
//
//    	Gson gson = new Gson();
//    	String json = gson.toJson(new ContentModel(userToken));
//
//        return json;
//
//    }
//    
    
    /**
     * http://wess.com.tw:8081/SmartTourismApi/api/v1/oauth/twitterCallBack
     */
    @GET
    @Path("/twitterCallBack")
    @ApiOperation(value = "twitterCallBack",notes="twitterCallBack" , response = ContentModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String twitterCallBack(@ApiParam(value="oauth_token")@QueryParam("oauth_token") String oauth_token, @ApiParam(value="oauth_verifier")@QueryParam("oauth_verifier") String oauth_verifier, @Context HttpServletRequest httpRequest, @Context HttpServletResponse response) throws SmartTourismException {
    	
	    logger.info("-----googleCallBack-----");
	    logger.info(oauth_token);
	    logger.info(oauth_verifier);
	    
//	    OAuthService service = new ServiceBuilder()
//	        .provider(TwitterApi.SSL.class)
//	        .apiKey("tqaHlYsXybIAz46VpnkSaWKxE")
//	        .apiSecret("7aA33ISrxKKEn0l0PZu0cvr1a5FFFgbT2ThYByBZqyhWxA1ub4")
//	        .callback("http://wess.com.tw:8081/SmartTourismApi/api/v1/oauth2/twitterCallBack")
//	        .build();
	    
		OAuthService service = new ServiceBuilder()
			.provider(TwitterApi.SSL.class)
			.apiKey("duwWpNnPW41q99GzjCVLITVGc")
			.apiSecret("gR6kHN5M8clkk777TvCaIQmoLy4Gs2hcz2c9trMnF4BbqP5ZP7")
			.callback(
					"http://www.vztaiwan.com/auth/signIn")
			.build();
	    
	    
	    if (StringUtils.isEmpty(oauth_token) || StringUtils.isEmpty(oauth_verifier)) {
	    	//Twitter請使用者重導URL如 :http://wess.com.tw:8081/SmartTourismApi/api/v1/oauth2/twitterCallBack?oauth_token=skLcnSqoQlq7txkAtdbinYN3NJ7hgAzd&oauth_verifier=wrTpkdWYTjdMwBzynVwzXNV5sqL9iM0p
	    	try {	    		
	    		Token requestToken = service.getRequestToken();
	    		String authUrl = service.getAuthorizationUrl(requestToken);    		
				response.sendRedirect(authUrl);
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new SmartTourismException(e.getMessage());
			}
	    }	    


	    Token requestToken = new Token(oauth_token, oauth_verifier);
	    Verifier v = new Verifier(oauth_verifier);
	    Token accessToken = null;
	    try {
	    
	    	accessToken = service.getAccessToken(requestToken, v); // the requestToken you
	    
	    } catch (OAuthException e) {
	    	throw new SmartTourismException("授權失敗(驗證碼有問題)");
	    }
	    
	    System.out.println(accessToken.getRawResponse());
	    //twitter無法提供user email
	    //oauth_token=2577122994-GG9Dz7TRaiySHofxPjdu2CPgTPxhkM86RfuPas9&oauth_token_secret=u4tZihuLQj3Jpt6a0sWuno45rMGRFORvMeRYlxlJ2xeyt&user_id=2577122994&screen_name=wesswang0110
	    
	    
	    Map<String, String> map = OAuthController.dealTwitterQueryParams(accessToken.getRawResponse());
	    String userToken = OAuthController.getUserToken(SOCIAL.TWITTER, map.get("user_id"), null);
	    
	    
	    FacebookAnsonPayload facebookAnsonPayload = new FacebookAnsonPayload();
    	facebookAnsonPayload.setIss(httpRequest.getServerName());
    	facebookAnsonPayload.setSub(map.get("user_id"));
    	facebookAnsonPayload.setIat(String.valueOf((int) (System.currentTimeMillis() / 1000L)));
    	facebookAnsonPayload.setExp(String.valueOf((int) (System.currentTimeMillis() / 1000L + 14 * 24 * 60 * 60)));

    	
    	Gson gson = new Gson();
    	String json = gson.toJson(facebookAnsonPayload);
   
    	String ansontoken = "abc." + new String(Base64.encodeBase64(json.getBytes())) + ".abc";

    	
    	Gson gson2 = new Gson();
    	String json2 = gson2.toJson(new OAuth2Model(userToken, ansontoken));
    	
        return json2;

    }

    
    
    private static String getUserToken (SOCIAL social, String userId, String email) throws SmartTourismException {
    	
    	SearchResult account = null;
    	
    	switch (social) {
	        case TWITTER:
	        	System.out.println("!!!!!Twitter!!!!!");
	        	SearchResult searchResult2 = LdapService.findOneBySocialUserId(SOCIAL.TWITTER, userId);
	    	    if (searchResult2 == null) {//帳號不存在
	    	    	String result = LdapService.createLdapSocialUser(SOCIAL.TWITTER, userId, userId);//twitter不提供email
	    	    }
	    	    account = LdapService.findOneByEmail(userId);
	            break;
    	}
    	
    	
    	

	    if (account == null) {
	    	throw new SmartTourismException("帳號不存在");
	    }    	    
	    Attributes attrs = account.getAttributes();
    	String token = UUID.randomUUID().toString();
	    Connection conn = null;
	    PreparedStatement preparedStatement = null;
	    try {
	    		    	
			conn = DbUtil.getConnection();
			preparedStatement = conn.prepareStatement("insert into user_token (UUID,USER_TOKEN,USER_ID,LOGIN_TIME) values (?,?,?,?)");
			preparedStatement.setString(1, UUID.randomUUID().toString());
			preparedStatement.setString(2, token);
			preparedStatement.setString(3, (String)attrs.get(LdapService.secureToken).get());
			preparedStatement.setString(4, String.valueOf(System.currentTimeMillis()));

			boolean success = preparedStatement.execute();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new SmartTourismException("資料庫錯誤，請洽系統管理員");
			
		} finally {		
			try {
				preparedStatement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

    	
    	return token;
    }

    
    public static Map<String, String> dealTwitterQueryParams(String queryParamString) throws SmartTourismException {
        try {
            Map<String, String> params = new HashMap<String, String>();
            for (String param : queryParamString.split("&")) {
                String[] pair = param.split("=");
                String key = URLDecoder.decode(pair[0], "UTF-8");
                String value = URLDecoder.decode(pair[1], "UTF-8");;
                params.put(key, value);
            }

            return params;
        } catch (UnsupportedEncodingException ex) {
        	ex.printStackTrace();
            throw new SmartTourismException("URL query parameter 解析錯誤");
        }
    }

    

}