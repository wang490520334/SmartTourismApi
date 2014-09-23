package iii.org.tw.definition;

import iii.org.tw.util.SmartTourismException;

public class Type {
	public static final int Attraction = 0;
	public static final int FoodAndDrink = 1;
	public static final int Shopping = 2;
	public static final int Tour = 3;
	public static final int Activity = 4;
	
	
	public static int getTypeIntByString (String str) throws SmartTourismException {
		
		
		
		switch (str) {
	        case "Attraction" :
	        	return Type.Attraction;
	        case "FoodAndDrink":
	        	return Type.FoodAndDrink;
	        case "Shopping":
	        	return Type.Shopping;
	        case "Activity":
	        	return Type.Activity;
	        case "Tour":
	        	return Type.Tour;	
	        default :
	        	throw new SmartTourismException("不支援的Type");

		}
		
		
	}
	
	
	public static String getTypeStringByInt (int i) throws SmartTourismException {
		
		
		
		switch (i) {
	        case Type.Attraction :
	        	return "Attraction";
	        case Type.FoodAndDrink :
	        	return "FoodAndDrink";
	        case Type.Shopping :
	        	return "Shopping";
	        case Type.Activity :
	        	return "Activity";	
	        case Type.Tour :
	        	return "Tour";	
	        default :
	        	throw new SmartTourismException("不支援的Type");

		}
		
		
	}
	
	
	
	
}
