package iii.org.tw.model;

import iii.org.tw.model.SuggestionTimeModel;

public class SuggestionResultModel {
	private SuggestionTimeModel time = new SuggestionTimeModel();
	private java.util.ArrayList<String> attraction_order = new java.util.ArrayList<String>();
	
	public void setSuggestionTime(SuggestionTimeModel suggsetionTime){
		this.time.setEndTime(suggsetionTime.getEndTime());
		this.time.setStartTime(suggsetionTime.getStartTime());
	}
	
	public void setAttractionOrder(java.util.ArrayList<String> attractionOrder){
		for(int i=0; i<attractionOrder.size(); i++){
			this.attraction_order.add(attractionOrder.get(i));
		}
	}
	
	public SuggestionTimeModel getSuggestionTime(){
		return this.time;
	}
	
	public java.util.ArrayList<String> getAttractionOrder(){
		return this.attraction_order;
	}
}
