package iii.org.tw.util;

public class ApiException extends Exception{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7820265449521595648L;
	private int code;
	public ApiException (int code, String msg) {
		super(msg);
		this.code = code;
	}
	public int getCode () {
		return code;
	}
}

