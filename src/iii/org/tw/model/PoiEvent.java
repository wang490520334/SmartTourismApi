package iii.org.tw.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import com.wordnik.swagger.annotations.ApiModelProperty;


public class PoiEvent {

	private List<PoiCalendar> morning = new ArrayList<PoiCalendar>() ;
	private List<PoiCalendar> afternoon = new ArrayList<PoiCalendar>() ;
	private List<PoiCalendar> night = new ArrayList<PoiCalendar>() ;
	private List<PoiCalendar> accommodation = new ArrayList<PoiCalendar>() ;
	
	public PoiEvent(List<PoiCalendar> morning, List<PoiCalendar> afternoon,
			List<PoiCalendar> night, List<PoiCalendar> accommodation) {
		super();
		this.morning = morning;
		this.afternoon = afternoon;
		this.night = night;
		this.accommodation = accommodation;
	}

	@XmlElement(name = "morning")
	@ApiModelProperty(value = "早上的Poi Event", required=true)
	public List<PoiCalendar> getMorning() {
		return morning;
	}

	public void setMorning(List<PoiCalendar> morning) {
		this.morning = morning;
	}

	@XmlElement(name = "afternoon")
	@ApiModelProperty(value = "下午的Poi Event", required=true)
	public List<PoiCalendar> getAfternoon() {
		return afternoon;
	}

	public void setAfternoon(List<PoiCalendar> afternoon) {
		this.afternoon = afternoon;
	}

	@XmlElement(name = "night")
	@ApiModelProperty(value = "晚上的Poi Event", required=true)
	public List<PoiCalendar> getNight() {
		return night;
	}

	public void setNight(List<PoiCalendar> night) {
		this.night = night;
	}

	@XmlElement(name = "accommodation")
	@ApiModelProperty(value = "住宿的Poi Event", required=true)
	public List<PoiCalendar> getAccommodation() {
		return accommodation;
	}

	public void setAccommodation(List<PoiCalendar> accommodation) {
		this.accommodation = accommodation;
	}
	
	
}
