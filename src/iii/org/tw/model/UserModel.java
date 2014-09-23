package iii.org.tw.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class UserModel {
	
	private String name;
	
	private String mail;
	
	private String telephone;
	
	private String mobile;
	
	private String imgSrc;
	
	private String createDateLong;
	
	@ApiModelProperty(allowableValues = "Male,Female")
	private String gender;
	
	private int age;
	
	@ApiModelProperty(allowableValues = "None,台灣,日本,United States,中國,香港,한국,新加坡,Europe-Country,Other")
	private String nationality;
	
	@ApiModelProperty(allowableValues = "None,Primary/Secondary Education,High School/Vocational School,Bachelors/Associate,Master/Phd")
	private String education;
	
	@ApiModelProperty(allowableValues = "None,Government/Defence,Police and Fire Departments,Education,Medical Institutions,Banking/Financial Services/Insurance,IT/Electronics,Manufacturing/Construction,Wholesale/Retailing/Import/Export,Customer Service/Catering/Hospital,Media/Advertising/Entertainment,Agriculture/Mining/Religion,Freelance")
	private String industry;
	
	@ApiModelProperty(allowableValues = "None,Married,Single")
	private String marriage;

	@ApiModelProperty(allowableValues = "None,US$ 2500 or less,Between US$ 2500- US$ 4000,Between US$ 4000- US$ 7500,More than US$ 7500")
	private String monthlyIncome;
	
	@ApiModelProperty(allowableValues = "None,Once,2~5 times,5+ times,Permanent")
	private String timesToTaiwan;
	
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMail() {
		return mail;
	}
	public void setMail(String mail) {
		this.mail = mail;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getImgSrc() {
		return imgSrc;
	}
	public void setImgSrc(String imgSrc) {
		this.imgSrc = imgSrc;
	}
	public String getCreateDateLong() {
		return createDateLong;
	}
	public void setCreateDateLong(String createDateLong) {
		this.createDateLong = createDateLong;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getNationality() {
		return nationality;
	}
	public void setNationality(String nationality) {
		this.nationality = nationality;
	}
	public String getEducation() {
		return education;
	}
	public void setEducation(String education) {
		this.education = education;
	}
	public String getIndustry() {
		return industry;
	}
	public void setIndustry(String industry) {
		this.industry = industry;
	}
	public String getMarriage() {
		return marriage;
	}
	public void setMarriage(String marriage) {
		this.marriage = marriage;
	}
	public String getMonthlyIncome() {
		return monthlyIncome;
	}
	public void setMonthlyIncome(String monthlyIncome) {
		this.monthlyIncome = monthlyIncome;
	}
	public String getTimesToTaiwan() {
		return timesToTaiwan;
	}
	public void setTimesToTaiwan(String timesToTaiwan) {
		this.timesToTaiwan = timesToTaiwan;
	}
	
	
	
}
