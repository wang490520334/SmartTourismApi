package iii.org.tw.model;

import java.util.ArrayList;
import java.util.List;
import iii.org.tw.model.PoiCalendarIOModel;

import javax.xml.bind.annotation.XmlElement;
import com.wordnik.swagger.annotations.ApiModelProperty;


public class PoiEventIOModel {

	private List<PoiCalendarIOModel> morning = new ArrayList<PoiCalendarIOModel>() ;
	private List<PoiCalendarIOModel> afternoon = new ArrayList<PoiCalendarIOModel>() ;
	private List<PoiCalendarIOModel> night = new ArrayList<PoiCalendarIOModel>() ;
	private List<PoiCalendarIOModel> accommodation = new ArrayList<PoiCalendarIOModel>() ;
	
	public PoiEventIOModel(List<PoiCalendarIOModel> morning, List<PoiCalendarIOModel> afternoon,
			List<PoiCalendarIOModel> night, List<PoiCalendarIOModel> accommodation) {
		super();
		this.morning = morning;
		this.afternoon = afternoon;
		this.night = night;
		this.accommodation = accommodation;
	}

	@XmlElement(name = "morning")
	@ApiModelProperty(value = "早上的Poi Event", required=true)
	public List<PoiCalendarIOModel> getMorning() {
		return morning;
	}

	public void setMorning(List<PoiCalendarIOModel> morning) {
		this.morning = morning;
	}

	@XmlElement(name = "afternoon")
	@ApiModelProperty(value = "下午的Poi Event", required=true)
	public List<PoiCalendarIOModel> getAfternoon() {
		return afternoon;
	}

	public void setAfternoon(List<PoiCalendarIOModel> afternoon) {
		this.afternoon = afternoon;
	}

	@XmlElement(name = "night")
	@ApiModelProperty(value = "晚上的Poi Event", required=true)
	public List<PoiCalendarIOModel> getNight() {
		return night;
	}

	public void setNight(List<PoiCalendarIOModel> night) {
		this.night = night;
	}

	@XmlElement(name = "accommodation")
	@ApiModelProperty(value = "住宿的Poi Event", required=true)
	public List<PoiCalendarIOModel> getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(List<PoiCalendarIOModel> accommodation) {
		this.accommodation = accommodation;
	}
	
	
}
