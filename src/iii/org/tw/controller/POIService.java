package iii.org.tw.controller;

import java.util.ArrayList;
import java.util.List;

import iii.org.tw.definition.Type;
import iii.org.tw.entity.Activity;
import iii.org.tw.entity.Attraction;
import iii.org.tw.entity.FoodAndDrink;
import iii.org.tw.entity.Shopping;
import iii.org.tw.entity.Tour;
import iii.org.tw.model.Poi;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/poiService")
@Api(value = "/poiService", description = "Operations about poiList")
@Produces(MediaType.APPLICATION_JSON)
public class POIService {
	
	private static AttractionController attractionController = new AttractionController() ;
	private static FoodAndDrinkController foodAndDrinkController = new FoodAndDrinkController() ;
	private static ShoppingController shoppingController = new ShoppingController() ;
	private static TourController tourController = new TourController() ;
	private static ActivityController activityController = new ActivityController() ;
	
    /**
     * poiService showDetailOfPoiList
     * 
     * http://localhost:8080/SmartTourismApi/api/v1/poiService/
     * @throws Exception 
     */
    @ApiOperation(value = "顯示poiList的各個detail", response = Poi.class, responseContainer="List")
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Attraction not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
    @POST
    public List<Poi>showDetailOfPoiList ( @ApiParam(value = "要查詢的PoiList", required = true) List<Poi> httpBody,
    		@ApiParam( value = "取得Poi object的語言", allowableValues = "zh_tw,en_us,ja_jp") @HeaderParam("language") String language,
    		@ApiParam(value = "[第三方APP apiKey] (暫為任意字串)", required = true) @HeaderParam("apiKey") String apiKey ) throws Exception {
    	
    	List<Poi> result = new ArrayList<Poi>() ;
    	for ( Poi tmpPoi : httpBody ) {
    		switch (tmpPoi.getType()) {
			case "Attraction" :
				Attraction tmpAttraction = attractionController.GETAttractionByID(tmpPoi.getId(), language) ;
				Poi newAttractionPoi = new Poi(tmpAttraction.getId(), Type.getTypeStringByInt(0), tmpAttraction) ;
				result.add(newAttractionPoi) ;
				break;
			case "FoodAndDrink" :
				FoodAndDrink tmpFoodAndDrink = foodAndDrinkController.GETFoodAndDrinkByID(tmpPoi.getId(), language) ;
				Poi newFoodAndDrinkPoi = new Poi(tmpFoodAndDrink.getId(), Type.getTypeStringByInt(1), tmpFoodAndDrink) ;
				result.add(newFoodAndDrinkPoi) ;
				break;
			case "Shopping" : 
				Shopping tmpShopping = shoppingController.GETShoppingByID(tmpPoi.getId(), language) ;
				Poi newShoppingPoi = new Poi(tmpShopping.getId(), Type.getTypeStringByInt(2), tmpShopping) ;
				result.add(newShoppingPoi) ;
				break;
			case "Tour" :
				Tour tmpTour = tourController.gettourbyid(tmpPoi.getId(), language) ;
				Poi newTourPoi = new Poi(tmpTour.getId(), Type.getTypeStringByInt(3), tmpTour) ;
				result.add(newTourPoi) ;
				break;
			case "Activity" : 
				Activity tmpActivity = activityController.GETActivityByID(tmpPoi.getId(), language) ;
				Poi newActivityPoi = new Poi(tmpActivity.getId(), Type.getTypeStringByInt(4), tmpActivity) ;
				result.add(newActivityPoi) ;
				break;				
			default:
				throw new Exception("undefined POI type!") ;
			}
    	} // end for    	    
    	
		return result;    	
    } // showDetailOfPoiList
	
}
