package iii.org.tw.entity;

public class UserToken {
	
    private String uuid;

    private String userToken;

    private String userId;

    private String loginTime;

	public UserToken() {
		super();
	}
    
	public UserToken(String uuid, String userToken, String userId, String loginTime) {
		super();
		this.uuid = uuid;
		this.userToken = userToken;
		this.userId = userId;
		this.loginTime = loginTime;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUserToken() {
		return userToken;
	}

	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}
	
	
    
    
}
