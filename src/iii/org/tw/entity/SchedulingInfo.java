package iii.org.tw.entity;

import iii.org.tw.model.Poi;



import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "行程資訊")
@XmlRootElement(name = "SchedulingInfo")
public class SchedulingInfo {

	private String city;
	private String date;
	private List<Poi> morning = new ArrayList<Poi>();
	private List<Poi> afternoon = new ArrayList<Poi>();
	private List<Poi> night = new ArrayList<Poi>();
	
	@XmlElement(name = "city")
	@ApiModelProperty(value = "當天所在縣市", required=true)
	public String getCity()
	{
		return this.city;
	}
	public void setCity(String c)
	{
		this.city = c;
	}

	@XmlElement(name = "date")
	@ApiModelProperty(value = "當天日期", required=true)
	public String getDate()
	{
		return this.date;
	}
	public void setDate(String d)
	{
		this.date = d;
	}
	
	@XmlElement(name = "morning")
	@ApiModelProperty(value = "當天上午行程", required=true)
	public List<Poi> getMorning()
	{
		return this.morning;
	}
	public void setMorning(List<Poi> m)
	{
		this.morning = m;
	}
	
	
	@XmlElement(name = "afternoon")
	@ApiModelProperty(value = "當天下午行程", required=true)	
	public List<Poi> getAfternoon()
	{
		return this.afternoon;
	}
	public void setAfternoon(List<Poi> a)
	{
		this.afternoon = a;
	}
	
	@XmlElement(name = "night")
	@ApiModelProperty(value = "當天晚上行程", required=true)	
	public List<Poi> getNight()
	{
		return this.night;
	}
	public void setNight(List<Poi> n)
	{
		this.night = n;
	}

}
