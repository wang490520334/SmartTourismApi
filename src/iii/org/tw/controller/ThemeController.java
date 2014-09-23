package iii.org.tw.controller;
 
import iii.org.tw.definition.Type;
import iii.org.tw.entity.Activity;
import iii.org.tw.entity.Attraction;
import iii.org.tw.entity.FoodAndDrink;
import iii.org.tw.entity.Picture;
import iii.org.tw.entity.Shopping;
import iii.org.tw.entity.Theme;
import iii.org.tw.entity.Tour;
import iii.org.tw.hydrator.AttractionHydrator;
import iii.org.tw.model.Poi;
import iii.org.tw.util.DbUtil;
import iii.org.tw.util.LanguageNotSupportException;
import iii.org.tw.util.NotFoundException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
 
@Path("/theme")
@Api(value = "/theme", description = "Operations about themes")
@Produces(MediaType.APPLICATION_JSON)
public class ThemeController {

	public static Properties props = new Properties();

	static {
		try {
			props.load(ThemeController.class
					.getResourceAsStream("/db.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	Logger logger = LoggerFactory.getLogger(UserTokenController.class);
	
	private static AttractionController attractionController = new AttractionController();
	private static ActivityController activityController = new ActivityController();
	private static ShoppingController shoppingController = new ShoppingController();
	private static TourController tourController = new TourController();
	private static FoodAndDrinkController foodAndDrinkController = new FoodAndDrinkController();
	
	
	//private String databaseURL = "jdbc:mysql://140.92.2.220:3306/ST_DB?useUnicode=true&characterEncoding=UTF8";
    /**
     * http://localhost:8888/SmartTourismApi/api/v1/Theme
     * @throws Exception 
     */
    @GET
    @ApiOperation(value = "Get all themes", response = Theme.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Theme not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
    public String getTheme(@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception 
    {    	
    	logger.info("Theme called success");
    	List<Theme> list = new ArrayList<>();
    	String LangTable;
    	if(language == null)
    		language = "zh_tw";
  	  	if(language.equals("zh_tw"))
  	  	{
  	  		LangTable = "theme_zh_tw";
  	  	}
  	  	else if(language.equals("en_us"))
  	  	{
  	  		LangTable = "theme_en_us";
  	  	}
  	  	else if(language.equals("ja_jp"))
  	  	{
  	  		LangTable = "theme_ja_jp";
  	  	}
  	  	else
  	  	{
  	  		throw new LanguageNotSupportException();
  	  	}  	  	
  	  	

    		Class.forName("com.mysql.jdbc.Driver");
    		Connection con = DbUtil.getConnection();
    		//Connection con = DriverManager.getConnection(databaseURL, "ari", "ari36993") ;
     		String SQL = "SELECT * FROM "+ LangTable;
    		Statement stmt = con.createStatement();
    		ResultSet rs = stmt.executeQuery(SQL);

    		while (rs.next()) 
    		{
    			Theme result = new Theme();
    			result.setId(rs.getString("Theme"));
    			result.setName(rs.getString("Name"));
    			if(rs.getString("Picture")!=null && !rs.getString("Picture").isEmpty()){
	    			for(String picstr : rs.getString("Picture").split(","))
	    			{
	    				Picture apic = new Picture();
	    				if(picstr.startsWith("http"))
	    					apic.setUrl(picstr);
	    				else
	    					apic.setUrl(props.getProperty("images.hosturl") + "images/" + picstr);
	    				apic.setDescription("");
	    				result.getPictures().add(apic);
	    			}
    			}
    			result.setDescription(rs.getString("Description"));
    			result.setType(rs.getInt("Type"));
    			list.add(result);
    		}
    		rs.close();
    		stmt.close();
    	
    	  
    	Gson gson = new Gson();
      	String json = gson.toJson(list);      	
        return json;
    }       

	/**
     * http://localhost:8888/SmartTourismApi/api/v1/Theme/{id}
	 * @throws Exception 
     */

    @GET
    @Path("/{id}")
    @ApiOperation(value = "Get one theme by themeid", response = Theme.class)
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Theme not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
    public Theme getThemeId(@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language,
    		@PathParam("id") String id) throws Exception 
    {    	    	
    	logger.info("ThemeId called success");
    	
    	Theme result = new Theme();
  	  	String LangTable = null;
  	  	if(language == null)
  	  		language = "zh_tw";
  	  	if(language.equals("zh_tw"))
  	  	{
  	  		LangTable = "theme_zh_tw";
  	  	}
  	  	else if(language.equals("en_us"))
  	  	{
  	  		LangTable = "theme_en_us";
  	  	}
  	  	else if(language.equals("ja_jp"))
  	  	{
  	  		LangTable = "theme_ja_jp";
  	  	}
  	  	else
  	  	{
  	  		throw new LanguageNotSupportException();
  	  	}
  	  	

  	  		Class.forName("com.mysql.jdbc.Driver");
  	  		Connection con = DbUtil.getConnection();
  	  		//Connection con = DriverManager.getConnection(databaseURL, "ari", "ari36993") ;
  	  		String SQL = "SELECT * FROM `" + LangTable + "` WHERE `Theme` = ?";
  	  		PreparedStatement stmt = con.prepareStatement(SQL);
  	  		stmt.setString(1, id);
  	  		ResultSet rs = stmt.executeQuery();
  	     
  	  		if(!rs.first())
  	  			throw new NotFoundException("No such data");
	  	  	
  	  		
  	  		List<Attraction> attractions = new ArrayList<Attraction>();
  	  		List<FoodAndDrink> foodAndDrinks = new ArrayList<FoodAndDrink>();
  	  		List<Shopping> shoppings = new ArrayList<Shopping>();
  	  		List<Tour> tours = new ArrayList<Tour>();
  	  		List<Activity> activities = new ArrayList<Activity>();
  	  		try {	
	  	  		attractions = attractionController.GETAttractionsByTheme(rs.getString("Theme"), language);  	  		
  	  		} catch (NotFoundException e) {
  	  			//not to do anything
  	  		}
  	  		try {
	  			foodAndDrinks = foodAndDrinkController.GETFoodAndDrinksByTheme(rs.getString("Theme"), language);
	  		} catch (NotFoundException e) {
	  			//not to do anything
	  		}
  	  		try {
  	  			shoppings = shoppingController.GETShoppingsByTheme(rs.getString("Theme"), language);
	  		} catch (NotFoundException e) {
	  			//not to do anything
	  		}
	  	  	try {
	  	  		tours = tourController.findbytheme(rs.getString("Theme"), language);
	  		} catch (NotFoundException e) {
	  			//not to do anything
	  		}
  	  		try {
				activities = activityController.GETActivitiesByTheme(rs.getString("Theme"), language);
			} catch (NotFoundException e) {
				//not to do anything
			}
  	  		
  	  		
  	  		List<Poi> poiList = new ArrayList<Poi>();
  	  		for (Attraction attraction : attractions) {
  	  			poiList.add(new Poi(attraction.getId(), Type.getTypeStringByInt(0)));
  	  		}
	  	  	for (FoodAndDrink foodAndDrink : foodAndDrinks) {
	  	  		poiList.add(new Poi(foodAndDrink.getId(), Type.getTypeStringByInt(1)));
	  	  	}
	  	  	for (Shopping shopping : shoppings) {
	  	  		poiList.add(new Poi(shopping.getId(), Type.getTypeStringByInt(2)));
	  	  	}
	  	  	for (Tour tour : tours) {
	  	  		poiList.add(new Poi(tour.getId(), Type.getTypeStringByInt(3)));
	  	  	}
	  	  	for (Activity activity : activities) {
	  	  		poiList.add(new Poi(activity.getId(), Type.getTypeStringByInt(4)));	
		  	}
  	  		
			result.setId(rs.getString("Theme"));
			result.setName(rs.getString("Name"));
			result.setDescription(rs.getString("Description"));
			if(rs.getString("Picture")!=null && !rs.getString("Picture").isEmpty()){
				for(String picstr : rs.getString("Picture").split(","))
				{
					Picture apic = new Picture();				
					if(picstr.startsWith("http"))
						apic.setUrl(picstr);
					else
						apic.setUrl(props.getProperty("images.hosturl") + "images/" + picstr);
					apic.setDescription("");
					result.getPictures().add(apic);
				}
			}
			result.setType(rs.getInt("Type"));
			result.setPoiList(poiList);
  	  		rs.close();
  	  		stmt.close();
  	  	
  	  	if(result.getType() == 7){	//root theme
	  	  	SQL = "SELECT `Item_Id` FROM `item_theme` WHERE `Theme` = ?";
	  		stmt = con.prepareStatement(SQL);
	  		stmt.setString(1, id);
	  		rs = stmt.executeQuery();
	  		List<String> themeids = new ArrayList<String>();
	  		while(rs.next())
	  			themeids.add(rs.getString("Item_Id"));
	  		for(String theid:themeids)
	  		{
	  			System.out.println(theid);
	  			result.getSubthemeList().add(getThemeId(language, theid));
	  		}
	  		stmt.close();
  		}
  	
        return result;
    } 
}