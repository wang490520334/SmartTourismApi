package iii.org.tw.auth;

import org.scribe.builder.api.DefaultApi20;

public class OAuth2Conf {

	private Class<? extends DefaultApi20> api;
	private String apiKey;
	private String apiSecret;
	private String callback;
	private String scope;
	private String userInfoApi;
	private String hostname;
	
	


	public OAuth2Conf(Class<? extends DefaultApi20> api, String apiKey, String apiSecret,
			String callback, String scope) {
		super();
		this.api = api;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
		this.callback = callback;
		this.scope = scope;
	}
	
	


	public Class<? extends DefaultApi20> getApi() {
		return api;
	}
	

	public void setApi(Class<? extends DefaultApi20> api) {
		this.api = api;
	}
	

	public String getApiKey() {
		return apiKey;
	}


	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}


	public String getApiSecret() {
		return apiSecret;
	}


	public void setApiSecret(String apiSecret) {
		this.apiSecret = apiSecret;
	}


	public String getCallback() {
		return callback;
	}


	public void setCallback(String callback) {
		this.callback = callback;
	}


	public String getScope() {
		return scope;
	}


	public void setScope(String scope) {
		this.scope = scope;
	}


	public String getUserInfoApi() {
		return userInfoApi;
	}


	public void setUserInfoApi(String userInfoApi) {
		this.userInfoApi = userInfoApi;
	}




	public String getHostname() {
		return hostname;
	}




	public void setHostname(String hostname) {
		this.hostname = hostname;
	}
	
	
	
	
	
	
	
	
}
