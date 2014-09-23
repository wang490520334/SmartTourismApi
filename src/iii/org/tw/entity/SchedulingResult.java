package iii.org.tw.entity;

import iii.org.tw.model.Poi;

import java.util.*;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class SchedulingResult {
		
	@XmlElement(name = "dayplan")
	@ApiModelProperty(value = "行程規劃清單", required=true)	
	private List<SchedulingInfo> dayplan = new ArrayList<SchedulingInfo>();
	
	
	
//	
	public void addDayplan(SchedulingInfo i)
	{
		dayplan.add(i);
	}
//	public SchedulingInfo getDayplan(int index)
//	{
//		return dayplan.get(index);
//	}
	
	public List<SchedulingInfo> getDayplan() {
		return dayplan;
	}



	public void setDayplan(List<SchedulingInfo> dayplan) {
		this.dayplan = dayplan;
	}



	public List<Poi> getRecommendList() {
		return recommendList;
	}



	public void setRecommendList(List<Poi> recommendList) {
		this.recommendList = recommendList;
	}



	@XmlElement(name = "recommendList")
	@ApiModelProperty(value = "其他可能為user感興趣的景點清單", required=true)	
	private List<Poi> recommendList;
	
//	
//	public void setList(ArrayList<Poi> r)
//	{
//		recommendList = r;
//	}
}

	


