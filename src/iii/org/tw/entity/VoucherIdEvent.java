package iii.org.tw.entity;

import javax.xml.bind.annotation.XmlElement;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class VoucherIdEvent {
	
	private String Customer_Id;
	private String Voucher_Uid;
	private String Voucher_Name;
	private String Voucher_Discount;
	private String Vendor;
	private String Voucher_Code;	
	private String Date;
	private String StartTime;
	private String EndTime;
	private String State;
	public VoucherIdEvent(String customer_Id, String voucher_Uid,
			String voucher_Name, String voucher_Discount, String vendor,
			String voucher_Code, String date, String startTime, String endTime,
			String state) 
	{
		super();
		Customer_Id = customer_Id;
		Voucher_Uid = voucher_Uid;
		Voucher_Name = voucher_Name;
		Voucher_Discount = voucher_Discount;
		Vendor = vendor;
		Voucher_Code = voucher_Code;
		Date = date;
		StartTime = startTime;
		EndTime = endTime;
		State = state;
	}
	public VoucherIdEvent()
	{}
	@XmlElement(name = "CustomerId")
	@ApiModelProperty(value = "顧客ID", required=true)
	public String getCustomer_Id() {
		return Customer_Id;
	}
	public void setCustomer_Id(String customer_Id) {
		Customer_Id = customer_Id;
	}
	
	@XmlElement(name = "VoucherUId")
	@ApiModelProperty(value = "折價券", required=true)
	public String getVoucher_Uid() {
		return Voucher_Uid;
	}
	public void setVoucher_Uid(String voucher_Uid) {
		Voucher_Uid = voucher_Uid;
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
	
	@XmlElement(name = "Vendor")
	@ApiModelProperty(value = "Vendor名", required=true)
	public String getVendor() {
		return Vendor;
	}
	public void setVendor(String vendor) {
		Vendor = vendor;
	}
	
	@XmlElement(name = "VoucherCode")
	@ApiModelProperty(value = "折價券兌換編號", required=true)
	public String getVoucher_Code() {
		return Voucher_Code;
	}
	public void setVoucher_Code(String voucher_Code) {
		Voucher_Code = voucher_Code;
	}
	
	@XmlElement(name = "Date")
	@ApiModelProperty(value = "折價券獲取日期", required=true)
	public String getDate() {
		return Date;
	}
	public void setDate(String date) {
		Date = date;
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
	
	@XmlElement(name = "State")
	@ApiModelProperty(value = "折價券使用狀態(有日期表示已使用)", required=true)
	public String getState() {
		return State;
	}
	public void setState(String state) {
		State = state;
	}
	
	
}
