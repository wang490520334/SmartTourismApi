package iii.org.tw.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class OAuth2ModelIn {

	@ApiModelProperty(value="Oauth2提供廠商回傳之code(code或accessToken為二擇一)")
	private String code;
	
	@ApiModelProperty(value="Oauth2提供廠商回傳之code後，使用code查詢到之accessToken(code或accessToken為二擇一)")
	private String accessToken;
	
	private String clientId;
	private String redirectUri;
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getClientId() {
		return clientId;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public String getRedirectUri() {
		return redirectUri;
	}
	public void setRedirectUri(String redirectUri) {
		this.redirectUri = redirectUri;
	}
	
	
	
	
	
}
