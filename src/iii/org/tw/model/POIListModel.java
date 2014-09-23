package iii.org.tw.model;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;


@ApiModel(value = "Poi")
@XmlRootElement(name = "Poi")
public class POIListModel {

	private String id ; 
	private String type ;
		
	public POIListModel(String id, String type) {
		super();
		this.id = id;
		this.type = type;
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
	@ApiModelProperty(value = "Attraction：景點, FoodAndDrink：食物, Shopping：購物, Activity：活動, Tour：遊程", required=true)
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
		
	
}

