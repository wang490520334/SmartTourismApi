package iii.org.tw.model;

import com.wordnik.swagger.annotations.ApiModelProperty;


public class CommentInModel {
	
	@ApiModelProperty(required=true, value="Attraction,FoodAndDrink,Shopping,Activity,Tour等的ID")
	private String placeId;

	@ApiModelProperty(required=true, value="評論")
	private String comment;

	private String picture;
	
	@ApiModelProperty(required=true,allowableValues = "Attraction,FoodAndDrink,Shopping,Activity,Tour")
	private String type;


	public String getPlaceId() {
		return placeId;
	}

	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}


	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "CommentInModel [placeId=" + placeId + ", comment=" + comment
				+ ", picture=" + picture + ", type=" + type + "]";
	}



	
	

}
