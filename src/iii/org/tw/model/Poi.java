package iii.org.tw.model;

import iii.org.tw.entity.base.IBaseType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "Poi")
@XmlRootElement(name = "Poi")
public class Poi {

	private String id ; 
	private String type ;	
	private IBaseType detail; // 'Attraction' or 'FoodAndDrink' or 'Shopping' or 'Activity' or 'Tour' or 'Custom(PoiCustomDetail)'
	
	public Poi() {
		super();
	}
	
	public Poi(String id, String type, IBaseType detail) {
		super();
		this.id = id;
		this.type = type;
		this.detail = detail;
	}

	public Poi(String id, String type) {
		super();
		this.id = id;
		this.type = type;
	}
	
	public Poi(String type, IBaseType detail) {
		super();
		this.type = type;
		this.detail = detail;
	}

	@XmlElement(name = "id")
	@ApiModelProperty(value = "POI id", required=false)
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
	@ApiModelProperty(value = "自定義的poi object detail", required=false)
	public IBaseType getDetail() {
		return detail;
	}

	public void setDetail(IBaseType detail) {
		this.detail = detail;
	}

}
