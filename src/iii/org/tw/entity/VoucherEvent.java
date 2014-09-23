package iii.org.tw.entity;

import javax.xml.bind.annotation.XmlElement;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class VoucherEvent {

	private String Uid;
	private String Voucher_Name;
	private String Voucher_Discount;
	private int Number;
	private int Quota_Each;
	private String Vendor;
	private String StartTime;
	private String EndTime;
	private String Create_Date;
	private String Modify_Date;
	public VoucherEvent(String uid, String voucher_Name,
			String voucher_Discount, int number, int quota_Each, String vendor,
			String startTime, String endTime, String create_Date,
			String modify_Date) {
		super();
		Uid = uid;
		Voucher_Name = voucher_Name;
		Voucher_Discount = voucher_Discount;
		Number = number;
		Quota_Each = quota_Each;
		Vendor = vendor;
		StartTime = startTime;
		EndTime = endTime;
		Create_Date = create_Date;
		Modify_Date = modify_Date;
	}
	
	@XmlElement(name = "VoucherUId")
	@ApiModelProperty(value = "折價券", required=true)	
	public String getUid() {
		return Uid;
	}
	public void setUid(String uid) {
		Uid = uid;
	}
	
	@XmlElement(name = "VoucherName")
	@ApiModelProperty(value = "折價券名稱", required=true)
	public String getVoucher_Name() {
		return Voucher_Name;
	}
	public void setVoucher_Name(String voucher_Name) {
		Voucher_Name = voucher_Name;
	}
	
	@XmlElement(name = "VoucherDiscount")
	@ApiModelProperty(value = "折價策略", required=true)
	public String getVoucher_Discount() {
		return Voucher_Discount;
	}
	public void setVoucher_Discount(String voucher_Discount) {
		Voucher_Discount = voucher_Discount;
	}
	
	@XmlElement(name = "Number")
	@ApiModelProperty(value = "折價券發放數量", required=true)
	public int getNumber() {
		return Number;
	}
	public void setNumber(int number) {
		Number = number;
	}
	@XmlElement(name = "QuotaEach")
	@ApiModelProperty(value = "每人可使用折價券之數量", required=true)
	public int getQuota_Each() {
		return Quota_Each;
	}
	public void setQuota_Each(int quota_Each) {
		Quota_Each = quota_Each;
	}
	
	@XmlElement(name = "Vendor")
	@ApiModelProperty(value = "Vendor名", required=true)
	public String getVendor() {
		return Vendor;
	}
	public void setVendor(String vendor) {
		Vendor = vendor;
	}
	
	@XmlElement(name = "StartTime")
	@ApiModelProperty(value = "折價券起始時間", required=true)
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	
	@XmlElement(name = "EndTime")
	@ApiModelProperty(value = "折價券結束時間", required=true)
	public String getEndTime() {
		return EndTime;
	}
	public void setEndTime(String endTime) {
		EndTime = endTime;
	}
	
	@XmlElement(name = "CreateDate")
	@ApiModelProperty(value = "折價券申請創建日期", required=true)
	public String getCreate_Date() {
		return Create_Date;
	}
	public void setCreate_Date(String create_Date) {
		Create_Date = create_Date;
	}
	
	@XmlElement(name = "ModifyDate")
	@ApiModelProperty(value = "折價券最後修改日期", required=true)
	public String getModify_Date() {
		return Modify_Date;
	}
	public void setModify_Date(String modify_Date) {
		Modify_Date = modify_Date;
	}
	
}
