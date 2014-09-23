package iii.org.tw.model;

import javax.xml.bind.annotation.XmlElement;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class RankCmpModel {

	private String placeId ;
	private String type ;
	private float avg_rank ;
	
	public RankCmpModel(String placeId, String type, float avg_rank) {
		super();
		this.placeId = placeId;
		this.type = type;
		this.avg_rank = avg_rank;
	}
	
	@XmlElement(name = "placeId")
	@ApiModelProperty(value = "評分物件id", required=true)	
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	@XmlElement(name = "type")
	@ApiModelProperty(value = "'Attraction' or 'FoodAndDrink' or 'Shopping' or 'Activity' or 'Tour'", required=true)		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	@XmlElement(name = "avg_rank")
	@ApiModelProperty(value = "此物件的平均rank", required=true)	
	public float getAvg_rank() {
		return avg_rank;
	}
	public void setAvg_rank(float avg_rank) {
		this.avg_rank = avg_rank;
	}
	
}
