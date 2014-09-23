package iii.org.tw.util;

public class NotFoundException extends ApiException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4992829091382185202L;
	public NotFoundException (int code, String msg) {
		super(code, msg);
	}
	public NotFoundException (String msg) {
		super(404, msg);
	}
}

