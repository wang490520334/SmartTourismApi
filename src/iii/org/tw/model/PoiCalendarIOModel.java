package iii.org.tw.model;

import iii.org.tw.util.SmartTourismException;

import javax.xml.bind.annotation.XmlElement;

import com.wordnik.swagger.annotations.ApiModelProperty;


public class PoiCalendarIOModel {

	private String id ; 
	private String type ;	
	private PoiCustomDetail detail;
		
	public PoiCalendarIOModel(String id, String type) throws NumberFormatException, SmartTourismException {
		super();
		this.id = id;
		this.type = iii.org.tw.definition.Type.getTypeStringByInt(Integer.valueOf(type));
	}


	public PoiCalendarIOModel(String id, String type, PoiCustomDetail detail) throws NumberFormatException, SmartTourismException {
		super();
		this.id = id;
		this.type = iii.org.tw.definition.Type.getTypeStringByInt(Integer.valueOf(type));
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
	public String getType() throws NumberFormatException, SmartTourismException {
		return iii.org.tw.definition.Type.getTypeStringByInt(Integer.parseInt(type));
	}

	public void setType(String type) throws SmartTourismException { 
		this.type = Integer.toString(iii.org.tw.definition.Type.getTypeIntByString(type));
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
