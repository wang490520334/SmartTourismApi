package iii.org.tw.util;

import iii.org.tw.controller.ActivityController;
import iii.org.tw.controller.AttractionController;
import iii.org.tw.controller.FoodAndDrinkController;
import iii.org.tw.controller.ShoppingController;
import iii.org.tw.controller.TourController;
import iii.org.tw.definition.Type;
import iii.org.tw.entity.Activity;
import iii.org.tw.entity.Attraction;
import iii.org.tw.entity.FoodAndDrink;
import iii.org.tw.entity.Shopping;
import iii.org.tw.entity.Tour;
import iii.org.tw.model.Poi;

import java.util.ArrayList;
import java.util.List;

public class SingletonControllerHelper {
	private static AttractionController attractionController = new AttractionController();
	private static ActivityController activityController = new ActivityController();
	private static ShoppingController shoppingController = new ShoppingController();
	private static TourController tourController = new TourController();
	private static FoodAndDrinkController foodAndDrinkController = new FoodAndDrinkController();
	
	
	public static AttractionController getAttractionController() {
		return attractionController;
	}
	public static ActivityController getActivityController() {
		return activityController;
	}
	public static ShoppingController getShoppingController() {
		return shoppingController;
	}
	public static TourController getTourController() {
		return tourController;
	}
	public static FoodAndDrinkController getFoodAndDrinkController() {
		return foodAndDrinkController;
	}
	
	
	public static List<Poi> getPoiList (ArrayList<String> relateid) throws Exception {

  		
  		List<Poi> poiList = new ArrayList<Poi>();
  		
  		
  		for (int i = 0; i < relateid.size(); i++) {
  			String id = relateid.get(i);
  			System.out.println(id);
  			try {	  				
  				Attraction attraction = attractionController.GETAttractionByID(id, null);
  				poiList.add(new Poi(attraction.getId(), Type.getTypeStringByInt(0)));	  		
  	  		} catch (NotFoundException e) {
  	  			//not to do anything
  	  		}
  	  		try {			
  	  			FoodAndDrink foodAndDrink =foodAndDrinkController.GETFoodAndDrinkByID(id, null);
  	  			poiList.add(new Poi(foodAndDrink.getId(), Type.getTypeStringByInt(1)));
  	  		} catch (NotFoundException e) {
  	  			//not to do anything
  	  		}
  		  	try {
  		  		Shopping shopping = shoppingController.GETShoppingByID(id, null);
  		  		poiList.add(new Poi(shopping.getId(), Type.getTypeStringByInt(2)));
  	  		} catch (NotFoundException e) {
  	  			//not to do anything
  	  		}
  	  	  	try {
  	  	  		Tour tour = tourController.gettourbyid(id, null);
  	  	  		poiList.add(new Poi(tour.getId(), Type.getTypeStringByInt(3)));
  	  		} catch (NotFoundException e) {
  	  			//not to do anything
  	  		}
  		  	try {
  		  		Activity activity = activityController.GETActivityByID(id, null);
  				poiList.add(new Poi(activity.getId(), Type.getTypeStringByInt(4)));	
  			} catch (NotFoundException e) {
  				//not to do anything
  			}
  		}

  	  	
  	  	
  	  	return poiList;
  	  	
	}
	

}
