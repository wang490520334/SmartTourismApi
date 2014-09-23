package iii.org.tw.entity;

import iii.org.tw.util.SingletonControllerHelper;

public class Pocket {
	
	private String uuid;
	
	private String apiKey;
	
	private String userId;
	
	private String relateId;
	
	private int pocketType;

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getRelateId() {
		return relateId;
	}

	public void setRelateId(String relateId) throws Exception {
		
		
		switch (this.pocketType) {
            case 0:  
            	SingletonControllerHelper.getAttractionController().GETAttractionByID(relateId, null);
            	break;
            case 1:  
            	SingletonControllerHelper.getFoodAndDrinkController().GETFoodAndDrinkByID(relateId, null);
                break;
            case 2:  
            	SingletonControllerHelper.getShoppingController().GETShoppingByID(relateId, null);
                break;
            case 3:  
            	SingletonControllerHelper.getTourController().gettourbyid(relateId, null);
                break;
            case 4:  
            	SingletonControllerHelper.getActivityController().GETActivityByID(relateId, null);
                break;

        }
		
		this.relateId = relateId;
	}

	public int getPocketType() {
		return pocketType;
	}

	public void setPocketType(int pocketType) {
		this.pocketType = pocketType;
	}


	
	
	

}
