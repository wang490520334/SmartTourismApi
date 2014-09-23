package iii.org.tw.suggestion;

public class PlaceInfo {
	/*
	 * public byte type1; public byte type2; public byte type3; public byte
	 * type4; public byte type5; public byte type6; public byte type7; public
	 * String nameC; public String nameE; public String stay_time; //建亦停留時間
	 * public double px; //GPS經度 public double py; //GPS緯度
	 */

	public static int numOfTypes = 100;
	public byte[] types;
	public String stay_time; // 建議停留時間
	public double px; // GPS經度
	public double py; // GPS緯度

	public PlaceInfo() {
		numOfTypes = 100;
	}

	public PlaceInfo(int inNumOfTypes) {
		InitTypes(inNumOfTypes);
	}

	public void InitTypes() {
		types = new byte[numOfTypes];
	}

	public void InitTypes(int inNumOfTypes) {
		numOfTypes = Math.abs(inNumOfTypes);
		InitTypes();
	}
}