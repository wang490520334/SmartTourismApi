package iii.org.tw.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class OAuth2Model {

	@ApiModelProperty(required=true, value="本平台使用者登入之  userToken")
	private String userToken;
	private String token;
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public OAuth2Model(String userToken, String token) {
		super();
		this.userToken = userToken;
		this.token = token;
	}
	
	
}
