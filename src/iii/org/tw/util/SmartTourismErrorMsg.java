package iii.org.tw.util;

public class SmartTourismErrorMsg {
	private String errorCode;
	private String errorMsg;

	public SmartTourismErrorMsg(String errorMsg) {
		super();
		this.errorMsg = errorMsg;
	}
	public SmartTourismErrorMsg(String errorCode, String errorMsg) {
		super();
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
	}
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getErrorMsg() {
		return errorMsg;
	}
	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}
	
	
	
}
