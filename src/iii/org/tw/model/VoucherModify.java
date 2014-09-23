package iii.org.tw.model;

import javax.xml.bind.annotation.XmlElement;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class VoucherModify {
	
	
	private String Voucher_Name;
	private String Voucher_Discount;
	private int Number;
	private int Quota_Each;
	private String StartTime;
	private String EndTime;
	
	public VoucherModify(String voucher_Name, String voucher_Discount,
			int number, int quota_Each, String startTime, String endTime) {
		super();
		Voucher_Name = voucher_Name;
		Voucher_Discount = voucher_Discount;
		Number = number;
		Quota_Each = quota_Each;
		StartTime = startTime;
		EndTime = endTime;
	}
	
	@XmlElement(name = "VoucherName")
	@ApiModelProperty(value = "折價券名稱", required=false)
	public String getVoucher_Name() {
		return Voucher_Name;
	}
	public void setVoucher_Name(String voucher_Name) {
		Voucher_Name = voucher_Name;
	}
	
	@XmlElement(name = "VoucherDiscount")
	@ApiModelProperty(value = "折價策略", required=false)
	public String getVoucher_Discount() {
		return Voucher_Discount;
	}
	public void setVoucher_Discount(String voucher_Discount) {
		Voucher_Discount = voucher_Discount;
	}
	
	@XmlElement(name = "Number")
	@ApiModelProperty(value = "折價券發放數量", required=false)
	public int getNumber() {
		return Number;
	}
	public void setNumber(int number) {
		Number = number;
	}
	
	@XmlElement(name = "QuotaEach")
	@ApiModelProperty(value = "每人可使用折價券之數量", required=false)
	public int getQuota_Each() {
		return Quota_Each;
	}
	public void setQuota_Each(int quota_Each) {
		Quota_Each = quota_Each;
	}
	
	@XmlElement(name = "StartTime")
	@ApiModelProperty(value = "折價券起始時間", required=false)
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	
	@XmlElement(name = "EndTime")
	@ApiModelProperty(value = "折價券結束時間", required=false)
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}	
}
