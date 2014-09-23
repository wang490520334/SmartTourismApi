package iii.org.tw.model;

import javax.xml.bind.annotation.XmlElement;

import com.wordnik.swagger.annotations.ApiModelProperty;


public class PoiCalendar {

	private String id ; 
	private String type ;	
	private PoiCustomDetail detail;
		
	public PoiCalendar(String id, String type) {
		super();
		this.id = id;
		this.type = type;
	}


	public PoiCalendar(String id, String type, PoiCustomDetail detail) {
		super();
		this.id = id;
		this.type = type;
		this.detail = detail;
	}
	
	
	@XmlElement(name = "id")
	@ApiModelProperty(value = "POI id", required=true)
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@XmlElement(name = "type")
	@ApiModelProperty(value = "'Attraction' or 'FoodAndDrink' or 'Shopping' or 'Activity' or 'Tour' or 'Custom'", required=true)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlElement(name = "detail")
	@ApiModelProperty(value = "假如type為custom，則需填寫此自定義的poi detail object", required=false)
	public PoiCustomDetail getDetail() {
		return detail;
	}

	public void setDetail(PoiCustomDetail detail) {
		this.detail = detail;
	}
}
