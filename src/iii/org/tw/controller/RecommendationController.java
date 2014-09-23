package iii.org.tw.controller;
import iii.org.tw.model.POIListModel;
import iii.org.tw.model.RecommendationInput;
import iii.org.tw.service.MySqlConnection;
import iii.org.tw.util.NotFoundException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;


@Path("/recommendation")
@Api(value = "/recommendation", description = "Operations about recommendation")
@Produces({ "application/json" })
public class RecommendationController 
{
	Logger logger = LoggerFactory.getLogger(CalendarController.class);
	private ArrayList<String> poiCandidate;
	private MySqlConnection connection;
	public Hashtable<String, Double> score;
	
	public RecommendationController()
	{
		connection = new MySqlConnection();
	}

    @ApiOperation(value = "產生個人化頁面的POI List", response = String.class, responseContainer="List")
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Attraction not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
    @POST
    public String personalPage( @ApiParam(value = "[本平台使用者token(登入時取得)] 或 [第三方APP userKey]", required = true) @HeaderParam("userToken") String userToken,
    		@ApiParam(value = "POI ID組成的list", required = true) RecommendationInput poiList )
    {
    	String request;
		ResultSet result;
		double sum;

		ArrayList<String> personalPageResult = new ArrayList<String>();    	
		List<String> list = poiList.getPoiList();
		
		try
		{
			//取得rating table中該user rated的poi list
			request = "SELECT Place_ID, Rating FROM rating WHERE User_ID = '" + userToken + "'";
			result = connection.searchQuery(request);
			Hashtable<String, Integer> userRatedPOIList = new Hashtable<String, Integer>();
			while(result.next())
			{
				userRatedPOIList.put(result.getString("Place_ID"), result.getInt("Rating"));
			}
			score = new Hashtable<String, Double>();
			for(String targetPOI : list)
			{	
				sum = 0.0;
				for(Map.Entry<String, Integer> ratedPOI : userRatedPOIList.entrySet())
				{
					//找出target poi與rated poi的similarity
					request = "SELECT Similarity FROM similarity WHERE Place_ID_1 = '" + targetPOI + "' AND Place_ID_2 = '" + ratedPOI.getKey() + "'"
							+ " OR Place_ID_2 = '" + targetPOI + "' AND Place_ID_1 = '" + ratedPOI.getKey() + "'";
					result = connection.searchQuery(request);
					if(result.next())
					{
						sum = sum + userRatedPOIList.get(ratedPOI.getKey()) * result.getDouble("Similarity");
					}
				}
				score.put(targetPOI, sum);
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		personalPageResult = ranking(score);
		Gson gson = new Gson();
		String json = gson.toJson(personalPageResult);
		return json;
    }
	
	@ApiOperation(value = "利用當前的userToken及poiID取得poi推薦結果", response = POIListModel.class, responseContainer="List")
	@Path("/related")
	@GET
	public String relatedPOIRecommend(@QueryParam("userToken") String userToken, @QueryParam("poiID") String poiID) throws NotFoundException
	{	
		getPOICandidate(poiID);
		
		String request;
		ResultSet result;
		double sum;
		
		ArrayList<String> poiRecommendResult = new ArrayList<String>();
		try
		{
			//取得rating table中該user rated的poi list
			request = "SELECT Place_ID, Rating FROM rating WHERE User_ID = '" + userToken + "'";
			result = connection.searchQuery(request);
			Hashtable<String, Integer> userRatedPOIList = new Hashtable<String, Integer>();
			while(result.next())
			{
				userRatedPOIList.put(result.getString("Place_ID"), result.getInt("Rating"));
			}
			score = new Hashtable<String, Double>();
			for(String targetPOI : poiCandidate)
			{	
				sum = 0.0;
				for(Map.Entry<String, Integer> ratedPOI : userRatedPOIList.entrySet())
				{
					//找出target poi與rated poi的similarity
					request = "SELECT Similarity FROM similarity WHERE Place_ID_1 = '" + targetPOI + "' AND Place_ID_2 = '" + ratedPOI.getKey() + "'"
							+ " OR Place_ID_2 = '" + targetPOI + "' AND Place_ID_1 = '" + ratedPOI.getKey() + "'";
					result = connection.searchQuery(request);
					if(result.next())
					{
						sum = sum + userRatedPOIList.get(ratedPOI.getKey()) * result.getDouble("Similarity");
					}
				}
				score.put(targetPOI, sum);
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		poiCandidate.clear();
		poiRecommendResult = ranking(score);
		List<POIListModel> poiResult = new ArrayList<POIListModel>() ;  
		for(String poi : poiRecommendResult)
		{
			String[] tempSpilt = poi.split(":");
			POIListModel tempPoi = new POIListModel(tempSpilt[0], tempSpilt[1]) ;
			poiResult.add(tempPoi) ;
			
		}
		Gson gson = new Gson();
		String json = gson.toJson(poiResult);
		return json;
	}
	/*
	@ApiOperation(value = "利用當前的poiID取得poi推薦結果", response = String.class, responseContainer="List")
	@Path("/nearby")
	@GET
	public String nearbyPOIRecommend(@QueryParam("poiID") String poiID) throws NotFoundException
	{			
		String request;
		ResultSet result;
		String[] temp;
		
		ArrayList<String> poiRecommendResult = new ArrayList<String>();
		
		try
		{
			//取得rating table中該user rated的poi list
			request = "SELECT Class FROM poi_distance WHERE Place_ID = '" + poiID + "'";
			result = connection.searchQuery(request);
			result.next();
			temp = result.getString("Class").split(" ");
			for(String poi : temp)
			{
				poiRecommendResult.add(poi);
			}
		}
		catch(SQLException e) {e.printStackTrace();}
		Gson gson = new Gson();
		String json = gson.toJson(poiRecommendResult);
		return json;
	}
	*/
	
	//取得poi candidates (欲推薦的target)
	private void getPOICandidate(String poiID)
	{
		String request;
		String[] data;
		ResultSet result;
		poiCandidate = new ArrayList<String>();
		
		try
		{
			request = "SELECT Class FROM all_group WHERE Place_ID = '" + poiID + "'";
			result = connection.searchQuery(request);
			if(result.next())
			{
				data = result.getString("Class").split(" ");
				for(String s : data)
				{
					poiCandidate.add(s);
				}
			}
		}
		catch(SQLException e) {e.printStackTrace();}
	}
	
	//ranking
	private ArrayList<String> ranking(Hashtable<String, Double> poi)
	{
		 ArrayList<Map.Entry<String, Double>> temp = new ArrayList<Map.Entry<String, Double>>(poi.entrySet());
	     Collections.sort(temp, new Comparator<Map.Entry<String, Double>>()
	     {
	          public int compare(Map.Entry<String, Double> a, Map.Entry<String, Double> b)
	          {
	               return b.getValue().compareTo(a.getValue());
	          }
	     });
	     ArrayList<String> result = new ArrayList<String>();
	     for(Map.Entry<String, Double> s : temp)
	     {
	    	 result.add(s.getKey());
	     }
	     return result;
	}
}

