package iii.org.tw.controller;
 


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wordnik.swagger.annotations.Api;
 
@Path("/fbLogin")
@Api(value = "/fbLogin", description = "Operations about facebook login")
@Produces(MediaType.APPLICATION_JSON)
public class FbLoginController {
	
	Logger logger = LoggerFactory.getLogger(FbLoginController.class);
	private static final String NETWORK_NAME = "Facebook";
	private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
	private static final Token EMPTY_TOKEN = null;
	
	/**
    * get facebook user's login
    * 
    * http://localhost:8080/SmartTourismApi/api/v1/fbLogin/
    */
	@GET
	public String getLogin( @QueryParam("code") String fbCode ) {
	
		
		String fbApiKey = "274868366049849" ;
		String fbApiSecret = "cfaa422e90e79ee9372e4ca55f7dcbbb" ;	
		
		OAuthService service = new ServiceBuilder()
							        .provider(FacebookApi.class)
							        .apiKey(fbApiKey)
							        .apiSecret(fbApiSecret)
							        .callback("http://gene.com.tw:8080/SmartTourismApi/api/v1/fbLogin/")
							        .build();
		
		System.out.println("=== " + NETWORK_NAME + "'s OAuth Workflow ===");
		System.out.println();
		
		// Obtain the Authorization URL
		System.out.println("Fetching the Authorization URL...");
		String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
		System.out.println("Got the Authorization URL!");
		System.out.println("Now go and authorize Scribe here:");
		System.out.println(authorizationUrl);
		System.out.println("And paste the authorization code here");
		System.out.print(">>" + fbCode);
		Verifier verifier = new Verifier(fbCode);
		System.out.println();
		
		// Trade the Request Token and Verfier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		Token accessToken = service.getAccessToken(null, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this: " + accessToken + " )");
		System.out.println();
		
		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		System.out.println("response code :" + response.getCode());
		System.out.println(response.getBody());
		
		System.out.println();
		System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
		
		
		return response.getBody() ;
	} // getLogin()
	
} // FbLoginController