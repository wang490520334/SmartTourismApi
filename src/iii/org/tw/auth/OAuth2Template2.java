package iii.org.tw.auth;

import iii.org.tw.model.FacebookAnsonPayload;
import iii.org.tw.model.OAuth2Model;
import iii.org.tw.model.OAuth2ModelIn;
import iii.org.tw.service.LdapService;
import iii.org.tw.util.DbUtil;
import iii.org.tw.util.SmartTourismException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.scribe.builder.ServiceBuilder;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public abstract class OAuth2Template2 {
	
	public abstract String callback (OAuth2ModelIn body, HttpServletRequest httpRequest) throws SmartTourismException;
	
	
	
	protected OAuth2Model getUserTokenByOAuth2 (SOCIAL social, String code, OAuth2Conf oAuth2Conf, boolean mobile) throws SmartTourismException {
		
		String userToken = null;
		String ansontoken = null;
		
		if (mobile) {
			
			Token token = new Token(code, oAuth2Conf.getApiSecret());
			OAuth2UserInfo oAuth2UserInfo = getUserInfo(oAuth2Conf, token, oAuth2Conf.getUserInfoApi());	
			userToken = getUserToken(social, oAuth2UserInfo.getUserId(), oAuth2UserInfo.getEmail());	
			
		} else {
			checkOAuth2Conf(oAuth2Conf);
			Token token = getTokenByCode(oAuth2Conf, code);
			OAuth2UserInfo oAuth2UserInfo = getUserInfo(oAuth2Conf, token, oAuth2Conf.getUserInfoApi());	
			userToken = getUserToken(social, oAuth2UserInfo.getUserId(), oAuth2UserInfo.getEmail());	

			switch (social) {
		        case GOOGLE:
		        	JsonElement jelement = new JsonParser().parse(token.getRawResponse());
		        	JsonObject jsonObject = jelement.getAsJsonObject();
		        	ansontoken = jsonObject.get("id_token").getAsString();
		            break;
		        case FACEBOOK:
		        	
		        	FacebookAnsonPayload facebookAnsonPayload = new FacebookAnsonPayload();
		        	System.out.println(oAuth2Conf.getHostname());
		        	facebookAnsonPayload.setIss(oAuth2Conf.getHostname());
		        	facebookAnsonPayload.setSub(oAuth2UserInfo.getUserId());
		        	facebookAnsonPayload.setIat(String.valueOf((int) (System.currentTimeMillis() / 1000L)));
		        	facebookAnsonPayload.setExp(String.valueOf((int) (System.currentTimeMillis() / 1000L + 14 * 24 * 60 * 60)));

		        	
		        	Gson gson = new Gson();
		        	String json = gson.toJson(facebookAnsonPayload);
		        	
		        	ansontoken = "abc." + new String(Base64.encodeBase64(json.getBytes())) + ".abc";
		        	
		        	//FacebookAnsonPayload
		            break;
			}	
		}
		
		
		return new OAuth2Model(userToken, ansontoken);
		
	}
	
	
	private final void checkOAuth2Conf (OAuth2Conf oAuth2Conf) throws SmartTourismException {
		if (StringUtils.isEmpty(oAuth2Conf.getApiKey()) || 
				StringUtils.isEmpty(oAuth2Conf.getApiSecret()) || 
				StringUtils.isEmpty(oAuth2Conf.getCallback()) || 
				StringUtils.isEmpty(oAuth2Conf.getScope()) || 
				StringUtils.isEmpty(oAuth2Conf.getUserInfoApi()) ) {
			throw new SmartTourismException("OAuth2Conf未完整設定，請洽系統管理員");
		}
	}
	
	
	private final Token getTokenByCode (OAuth2Conf oAuth2Conf, String code) {
		OAuthService service = new ServiceBuilder()
        .provider(oAuth2Conf.getApi())
	        .apiKey(oAuth2Conf.getApiKey())
	        .apiSecret(oAuth2Conf.getApiSecret())
	        .callback(oAuth2Conf.getCallback())
	        .scope(oAuth2Conf.getScope())
	        .build();
	    Token token = service.getAccessToken(null, new Verifier(code));	
	    System.out.println(token.getToken());
	    System.out.println(token.getSecret());
	    System.out.println(token.getRawResponse());
	    return token;
	}
	
	
	private final OAuth2UserInfo getUserInfo (OAuth2Conf oAuth2Conf, Token token, String userInfoApi) {
		
		OAuthService service = new ServiceBuilder()
        .provider(oAuth2Conf.getApi())
	        .apiKey(oAuth2Conf.getApiKey())
	        .apiSecret(oAuth2Conf.getApiSecret())
	        .callback(oAuth2Conf.getCallback())
	        .scope(oAuth2Conf.getScope())
	        .build();
		
		OAuthRequest request = new OAuthRequest(Verb.GET, userInfoApi);
	    service.signRequest(token, request);
	    Response response = request.send();
	    System.out.println(response.getCode());
	    System.out.println(response.getBody());
	    
	    JsonElement jelement = new JsonParser().parse(response.getBody());
	    JsonObject  jobject = jelement.getAsJsonObject();
	    String email = jobject.get("email").getAsString();
		String userId = jobject.get("id").getAsString();
		
		return new OAuth2UserInfo(userId, email);
		
	}
	
	
	
    
	protected String getUserToken (SOCIAL social, String userId, String email) throws SmartTourismException {
    	
    	SearchResult account = null;
    	
    	System.out.println("!!!!!" + social + "!!!!!");
    	SearchResult searchResult3 = LdapService.findOneBySocialUserId(social, userId);
	    if (searchResult3 == null) {//帳號不存在
	    	System.out.println(email);
	    	if (LdapService.findOneByEmail(email) == null) { //email也未重複
	    		String result = LdapService.createLdapSocialUser(social, userId, email);
	    	} else { //綁定帳號
	    		Map<String, String> attributes = new HashMap<String, String>();
	    	    attributes.put(LdapService.facebook, userId);    
	    	    LdapService.modifyAttribute(email, attributes);	    
	    	}
	    }
	    account = LdapService.findOneBySocialUserId(social, userId);

    	
    	

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
	


	
	

}
