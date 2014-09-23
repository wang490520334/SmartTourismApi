package iii.org.tw.auth;

public class OAuth2UserInfo {
	
	private String userId;
	
	private String email;
	
	

	public OAuth2UserInfo(String userId, String email) {
		super();
		this.userId = userId;
		this.email = email;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
	
}
