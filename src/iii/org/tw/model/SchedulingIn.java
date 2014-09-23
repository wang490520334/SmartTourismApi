package iii.org.tw.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class SchedulingIn {
	private List<String> cityList;
	private List<String> prefenceList;
	private String start;
	private String end;
	
	@XmlElement(name = "cityList")
	@ApiModelProperty(value = "縣市id清單", required=false)
	public List<String> getCityList() {
		return cityList;
	}
	public void setCityname(List<String> city) {
		this.cityList = city;
	}
	@XmlElement(name = "prefenceList")
	@ApiModelProperty(value = "user偏好清單", required=false)
	public List<String> getPrefence() {
		return prefenceList;
	}
	public void setPrefence(List<String> prefence) {
		this.prefenceList = prefence;
	}
	
	@XmlElement(name = "start")
	@ApiModelProperty(value = "出發日期(日期格式yyyy-mm-dd)", required=true)
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	@XmlElement(name = "end")
	@ApiModelProperty(value = "結束日期(日期格式yyyy-mm-dd)", required=true)
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
}
