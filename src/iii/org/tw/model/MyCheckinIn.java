package iii.org.tw.model;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class MyCheckinIn {
	
	@ApiModelProperty(required=true,allowableValues = "Attraction,FoodAndDrink,Shopping,Activity,Tour")
	private String pocketType;
	
	@ApiModelProperty(required=true, value="Attraction,FoodAndDrink,Shopping,Activity,Tour等的ID")
	private String relatedId;
	
	
	public String getPocketType() {
		return pocketType;
	}
	public void setPocketType(String pocketType) {
		this.pocketType = pocketType;
	}
	public String getRelatedId() {
		return relatedId;
	}
	public void setRelatedId(String relatedId) {
		this.relatedId = relatedId;
	}
	@Override
	public String toString() {
		return "MyPocketIn [pocketType=" + pocketType + ", relatedId="
				+ relatedId + "]";
	}
	
	
	
}
