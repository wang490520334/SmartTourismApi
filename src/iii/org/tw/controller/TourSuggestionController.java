package iii.org.tw.controller;

import java.util.Arrays;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.ArrayList;

import iii.org.tw.model.SuggestionTimeModel;
import iii.org.tw.model.SuggestionResultModel;
import iii.org.tw.util.SmartTourismErrorMsg;
import iii.org.tw.util.SmartTourismException;
import iii.org.tw.suggestion.*;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonArray;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;




@Path("/TourSuggestion")
@Produces(MediaType.APPLICATION_JSON)
//@Api(value = "/TourSuggestion", description = "Smart Tourism Tour Suggestion Api")
public class TourSuggestionController {
	
	/**
	 * http://localhost:8081/SmartTourismApi/api/v1/TourSuggestion/SmartTour
	 * @throws Exception 
	 */
	@POST
	@Path("/SmartTour")
	@ApiOperation(value = "指定時間之行程景點推薦",notes="使用者指定時間，系統推薦行程景點" , response = SuggestionResultModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String SmartTour(String body) throws SmartTourismException {
		
		//變數宣告
		double px = 0.0, py = 0.0;
		long startTime, endTime;
		String [] placeThemeSet;
		ArrayList<String> pockets                         = new ArrayList<String>();
		ArrayList<Integer> themes                         = new ArrayList<Integer>();
		ArrayList<String> whiteList                       = new ArrayList<String>();
		ArrayList<String> blackList                       = new ArrayList<String>();
		ArrayList<SuggestionResultModel> suggestionResult = new ArrayList<SuggestionResultModel>(); 
		//--------------------------------------------------------------
		
		//檢查推薦系統是否初始化
		if(TourSuggestion.isNotInitial())
			TourSuggestion.initAllData();
		//--------------------------------------------------------------
		
		//取得輸入JSON資料
		JsonElement jelement  = new JsonParser().parse(body);
    	JsonObject jsonObject = jelement.getAsJsonObject();	
    	//--------------------------------------------------------------
    	
    	//輸入資料設定
    	if(jsonObject.getAsJsonObject("location") != null){
    		px = jsonObject.getAsJsonObject("location").get("longitude") == null ? 0.0 : jsonObject.getAsJsonObject("location").get("longitude").getAsDouble();
    		py = jsonObject.getAsJsonObject("location").get("latitude")  == null ? 0.0 : jsonObject.getAsJsonObject("location").get("latitude").getAsDouble();
    	}
    	
    	startTime = jsonObject.getAsJsonObject("time").get("start_time") == null ? Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000             : jsonObject.getAsJsonObject("time").get("start_time").getAsLong();
    	endTime   = jsonObject.getAsJsonObject("time").get("end_time")   == null ? Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000 + 10800 * 2 : jsonObject.getAsJsonObject("time").get("end_time").getAsLong();
    	  
    	  
    	if(jsonObject.getAsJsonArray("pockets") != null){
    		JsonArray inPockets = jsonObject.getAsJsonArray("pockets");
    		for(int i=0; i<inPockets.size(); i++){
    			pockets.add(inPockets.get(i).getAsString());
    		}
    	}
    	
    	if(jsonObject.getAsJsonArray("themes") != null){
    		placeThemeSet = TourSuggestion.getPlaceThemeSet();
    		Arrays.sort(placeThemeSet);
    		JsonArray inThemes = jsonObject.getAsJsonArray("themes");
    		for(int i=0; i<inThemes.size(); i++)
    			themes.add(Arrays.binarySearch(placeThemeSet, inThemes.get(i).getAsString()) + 1);
    	}
    	
    	if(jsonObject.getAsJsonArray("white_list") != null){
    		JsonArray inWhiteList = jsonObject.getAsJsonArray("white_list");
    		for(int i=0; i<inWhiteList.size(); i++){
    			whiteList.add(inWhiteList.get(i).getAsString());
    		}
    	}
    	
    	if(jsonObject.getAsJsonArray("black_list") != null){
    		JsonArray inBlackList = jsonObject.getAsJsonArray("black_list");
    		for(int i=0; i<inBlackList.size(); i++){
    			blackList.add(inBlackList.get(i).getAsString());
    		}
    	}
    	//--------------------------------------------------------------
    	
    	//行程推薦
    	ArrayList<Result> returnData = TourSuggestion.SmartTour(px, py, startTime, endTime, themes, pockets, whiteList, blackList);
    	//--------------------------------------------------------------
		
    	//輸出資料設定
		for(int i=0; i<returnData.size(); i++){
			SuggestionTimeModel tempTime   = new SuggestionTimeModel();
			tempTime.setStartTime(returnData.get(i).start_time);
			tempTime.setEndTime(returnData.get(i).end_time);
			
			SuggestionResultModel tempSugResult = new SuggestionResultModel();
			tempSugResult.setSuggestionTime(tempTime);
			tempSugResult.setAttractionOrder(returnData.get(i).attraction_order);
			suggestionResult.add(tempSugResult); 
		}
		//--------------------------------------------------------------
		
		//輸出JSON
    	Gson gson   = new Gson();
    	String json = gson.toJson(suggestionResult);
    	
        return json; 
      //--------------------------------------------------------------
    }   
	
	
	/**
	 * http://localhost:8081/SmartTourismApi/api/v1/TourSuggestion/GoNow
	 * @throws Exception 
	 */
	@POST
	@Path("/GoNow")
	@ApiOperation(value = "立即行程景點推薦",notes="根據現在的時間及使用者目前的所在位置，系統推薦行程景點" , response = SuggestionResultModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String GoNow(String body) throws SmartTourismException {
		
		//變數宣告
		double px = 0.0, py = 0.0;
		long endTime, currenrtTime;
		String [] placeThemeSet;
		ArrayList<String> pockets                         = new ArrayList<String>();
    	ArrayList<Integer> themes                         = new ArrayList<Integer>();
    	ArrayList<SuggestionResultModel> suggestionResult = new ArrayList<SuggestionResultModel>(); 
    	//--------------------------------------------------------------
    	
    	//檢查推薦系統是否初始化
		if(TourSuggestion.isNotInitial())
			TourSuggestion.initAllData();
		//--------------------------------------------------------------
		
		//取得輸入JSON資料
		JsonElement jelement  = new JsonParser().parse(body);
    	JsonObject jsonObject = jelement.getAsJsonObject();	
    	//--------------------------------------------------------------
    	
    	//輸入資料設定
    	if(jsonObject.getAsJsonObject("location") != null){
    		px = jsonObject.getAsJsonObject("location").get("longitude") == null ? 0.0 : jsonObject.getAsJsonObject("location").get("longitude").getAsDouble();
    		py = jsonObject.getAsJsonObject("location").get("latitude")  == null ? 0.0 : jsonObject.getAsJsonObject("location").get("latitude").getAsDouble();
    	}
    	
    	currenrtTime = Calendar.getInstance(TimeZone.getTimeZone("UTC")).getTimeInMillis() / 1000;
    	if(jsonObject.get("end_time") == null) 
    		endTime = currenrtTime + 3600 * 4;
    	else {
    		long tempEndTime  = jsonObject.get("end_time").getAsLong();
    		if (tempEndTime < currenrtTime)
    			endTime = currenrtTime + 3600 * 4;
    		else
    			endTime = tempEndTime;
    	}
 
    	  
    	if(jsonObject.getAsJsonArray("pockets") != null){
    		JsonArray inPockets = jsonObject.getAsJsonArray("pockets");
    		for(int i=0; i<inPockets.size(); i++){
    			pockets.add(inPockets.get(i).getAsString());
    		}
    	}
    	 
    	if(jsonObject.getAsJsonArray("themes") != null){
    		placeThemeSet = TourSuggestion.getPlaceThemeSet();
    		Arrays.sort(placeThemeSet);
    		JsonArray inThemes = jsonObject.getAsJsonArray("themes");
    		for(int i=0; i<inThemes.size(); i++)
    			themes.add(Arrays.binarySearch(placeThemeSet, inThemes.get(i).getAsString()) + 1);
    	}
    	//--------------------------------------------------------------
    	
    	//行程推薦
    	ArrayList<Result> returnData = TourSuggestion.GoNow(px, py, endTime, themes, pockets);
    	//--------------------------------------------------------------
    	
    	//輸出資料設定
		for(int i=0; i<returnData.size(); i++){
			SuggestionTimeModel tempTime   = new SuggestionTimeModel();
			tempTime.setStartTime(returnData.get(i).start_time);
			tempTime.setEndTime(returnData.get(i).end_time);
			
			SuggestionResultModel tempSugResult = new SuggestionResultModel();
			tempSugResult.setSuggestionTime(tempTime);
			tempSugResult.setAttractionOrder(returnData.get(i).attraction_order);
			suggestionResult.add(tempSugResult); 
		}
		//--------------------------------------------------------------
		
		//輸出JSON
    	Gson gson   = new Gson();
    	String json = gson.toJson(suggestionResult);
    	
        return json;
        //--------------------------------------------------------------
    } 
	
	/**
	 * http://localhost:8081/SmartTourismApi/api/v1/TourSuggestion/FromMyPocket
	 * @throws Exception 
	 */
	@POST
	@Path("/FromMyPocket")
	@ApiOperation(value = "MyPocket行程景點推薦",notes="根據使用者的MyPocket，系統排出適當行程及時間" , response = SuggestionResultModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String FromMyPocket(String body) throws SmartTourismException {
		
		//變數宣告
		ArrayList<String> pockets                         = new ArrayList<String>();
		ArrayList<Long> startTime                         = new ArrayList<Long>();
		ArrayList<Long> endTime                           = new ArrayList<Long>();
		ArrayList<Double> px                              = new ArrayList<Double>();
		ArrayList<Double> py                              = new ArrayList<Double>();   	
		ArrayList<SuggestionResultModel> suggestionResult = new ArrayList<SuggestionResultModel>(); 
		//--------------------------------------------------------------
		
		//檢查推薦系統是否初始化
		if(TourSuggestion.isNotInitial())
			TourSuggestion.initAllData();
		//--------------------------------------------------------------
		
		//取得輸入JSON資料
		JsonElement jelement  = new JsonParser().parse(body);
    	JsonObject jsonObject = jelement.getAsJsonObject();	
    	//--------------------------------------------------------------
    	
    	//輸入資料設定
    	if(jsonObject.getAsJsonArray("free_info") != null){
    		JsonArray inFree = jsonObject.getAsJsonArray("free_info");
    		for(int i=0; i<inFree.size(); i++){
    			JsonObject jsonFreeInfo = inFree.get(i).getAsJsonObject();
    			if(jsonFreeInfo.getAsJsonObject("time") != null){
    				startTime.add(jsonFreeInfo.getAsJsonObject("time").get("start_time").getAsLong());
    				endTime.add(jsonFreeInfo.getAsJsonObject("time").get("end_time").getAsLong());	
    			}
    			if(jsonFreeInfo.getAsJsonObject("location") != null){
    				px.add(jsonFreeInfo.getAsJsonObject("location").get("longitude") == null ? null : jsonFreeInfo.getAsJsonObject("location").get("longitude").getAsDouble());
        			py.add(jsonFreeInfo.getAsJsonObject("location").get("latitude")  == null ? null : jsonFreeInfo.getAsJsonObject("location").get("latitude").getAsDouble());	
    			}
    		}
    	}
  	  
    	if(jsonObject.getAsJsonArray("pockets") != null){
    		JsonArray inPockets = jsonObject.getAsJsonArray("pockets");
    		for(int i=0; i<inPockets.size(); i++){
    			pockets.add(inPockets.get(i).getAsString());
    		}
    	}
    	//--------------------------------------------------------------
    	
    	//行程推薦
    	ArrayList<Result> returnData = TourSuggestion.FromMyPocket(pockets, startTime, endTime, px, py);
    	//--------------------------------------------------------------
    	
    	//輸出資料設定
		for(int i=0; i<returnData.size(); i++){
			SuggestionTimeModel tempTime   = new SuggestionTimeModel();
			tempTime.setStartTime(returnData.get(i).start_time);
			tempTime.setEndTime(returnData.get(i).end_time);
			
			SuggestionResultModel tempSugResult = new SuggestionResultModel();
			tempSugResult.setSuggestionTime(tempTime);
			tempSugResult.setAttractionOrder(returnData.get(i).attraction_order);
			suggestionResult.add(tempSugResult); 
		}
		//--------------------------------------------------------------
		
		//輸出JSON
    	Gson gson   = new Gson();
    	String json = gson.toJson(suggestionResult);
    	
        return json;
      //--------------------------------------------------------------
    }   
}
