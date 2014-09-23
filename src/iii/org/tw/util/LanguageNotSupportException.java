package iii.org.tw.util;

public class LanguageNotSupportException extends ApiException{

	public LanguageNotSupportException(int code, String msg) {
		super(code, msg);
	}
	
	public LanguageNotSupportException() {
		super(400, "Invalid language string");
	}
}
