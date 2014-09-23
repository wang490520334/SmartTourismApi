package iii.org.tw.controller;

import iii.org.tw.entity.FoodAndDrink;
import iii.org.tw.hydrator.FoodAndDrinkHydrator;
import iii.org.tw.util.ApiException;
import iii.org.tw.util.DbUtil;
import iii.org.tw.util.DistanceCalculator;
import iii.org.tw.util.LanguageNotSupportException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import javax.ws.rs.*;
import net.hydromatic.linq4j.Linq4j;
import net.hydromatic.linq4j.function.Predicate1;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/foodanddrink")
@Api(value = "/foodanddrink", description = "Operations about foodanddrinks")
@Produces({ "application/json" })
public class FoodAndDrinkController {

	private String getTableName(String language) throws LanguageNotSupportException
	{
		if(language == null)
			language = "zh_tw";
		String tablename = "food_drink_zh_tw";
		switch(language)
		{
		case "en_us":
			tablename = "food_drink_en_us";
			break;
		case "zh_tw":
			tablename = "food_drink_zh_tw";
			break;
		case "ja_jp":
			tablename = "food_drink_ja_jp";
			break;
		default:
			throw new LanguageNotSupportException();
		}
		return tablename;
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(value = "Get the foodanddrink infomation by ID", response = FoodAndDrink.class)
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "FoodAndDrink not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public FoodAndDrink GETFoodAndDrinkByID(@PathParam("id") String id,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception{
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Place_Id` = `item_theme`.`Item_Id` and `"+ TN +"`.`Place_Id`=? GROUP BY `"+ TN +"`.`Place_Id`");
		pst.setString(1, id);
		ResultSet result = pst.executeQuery();
		FoodAndDrink r = FoodAndDrinkHydrator.fromResultSet(result);
		pst.close();
		return r;
	}

	@GET
	@Path("/findByCounty")
	@ApiOperation(value = "Get the foodanddrinks infomation by county id", response = FoodAndDrink.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "FoodAndDrink not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public List<FoodAndDrink> GETFoodAndDrinksByCounty(
			@ApiParam(required = true)@QueryParam("countyid") String countyid,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Place_Id` = `item_theme`.`Item_Id` and `"+ TN +"`.`County_Id`=? GROUP BY `"+ TN +"`.`Place_Id`");
		pst.setString(1, countyid);
		ResultSet result = pst.executeQuery();
		List<FoodAndDrink> r = Linq4j.asEnumerable(FoodAndDrinkHydrator.fromResultSettoList(result)).where(new Predicate1<FoodAndDrink>() {
			
			@Override
			public boolean apply(FoodAndDrink arg0) {
				// TODO Auto-generated method stub
				if(arg0.getPictures().size()==0)
					return false;
				else
					return true;
			}
		}).toList();
		pst.close();
		return r;
	}

	@GET
	@Path("/findTopTen")
	@ApiOperation(value = "Get Top 10 foodanddrinks infomation", response = FoodAndDrink.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "FoodAndDrink not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public List<FoodAndDrink> GETFoodAndDrinksByTopTen(
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Place_Id` = `item_theme`.`Item_Id` and `"+ TN +"`.`Top_10`=1 GROUP BY `"+ TN +"`.`Place_Id`");
		ResultSet result = pst.executeQuery();
		return FoodAndDrinkHydrator.fromResultSettoList(result);
	}

	@GET
	@Path("/findByTheme")
	@ApiOperation(value = "Get the foodanddrinks infomation by theme class id", response = FoodAndDrink.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "FoodAndDrink not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public List<FoodAndDrink> GETFoodAndDrinksByTheme(
			@ApiParam(required = true)@QueryParam("themeid") String themeid,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Place_Id` = `item_theme`.`Item_Id` and `"+ TN +"`.`Place_Id` IN ( SELECT DISTINCT `Item_Id` from `item_theme` where `Theme`=?) GROUP BY `"+ TN +"`.`Place_Id`");
		pst.setString(1, themeid);
		ResultSet result = pst.executeQuery();
		List<FoodAndDrink> r = Linq4j.asEnumerable(FoodAndDrinkHydrator.fromResultSettoList(result)).where(new Predicate1<FoodAndDrink>() {
			
			@Override
			public boolean apply(FoodAndDrink arg0) {
				// TODO Auto-generated method stub
				if(arg0.getPictures().size()==0)
					return false;
				else
					return true;
			}
		}).toList();
		pst.close();
		return r;
	}

	@GET
	@Path("/findByCriteria")
	@ApiOperation(value = "Get the foodanddrinks infomation by some criterias", response = FoodAndDrink.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "FoodAndDrink not found"),
			  @ApiResponse(code = 400, message = "Invalid Parameter") 
			})
	public List<FoodAndDrink> GETFoodAndDrinksByCriteria(
			@QueryParam("themeid") String themeid,
			@QueryParam("countyid") String countyid,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		
		if(themeid == null && countyid == null)
			throw new ApiException(400, "Invalid Parameter");
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		String str = "SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Place_Id` = `item_theme`.`Item_Id`";
		if(themeid != null)
			str += " and `"+ TN +"`.`Place_Id` IN ( SELECT DISTINCT `Item_Id` from `item_theme` where `Theme`=?)";
		if(countyid != null)
			str += " and `"+ TN +"`.`County_Id`=?";
		str += " GROUP BY `"+ TN +"`.`Place_Id`";
		PreparedStatement pst = con.prepareStatement(str);
		int i = 1;
		if(themeid != null)
			pst.setString(i++, themeid);
		if(countyid != null)
			pst.setString(i++, countyid);
		ResultSet result = pst.executeQuery();
		List<FoodAndDrink> r = Linq4j.asEnumerable(FoodAndDrinkHydrator.fromResultSettoList(result)).where(new Predicate1<FoodAndDrink>() {
			
			@Override
			public boolean apply(FoodAndDrink arg0) {
				// TODO Auto-generated method stub
				if(arg0.getPictures().size()==0)
					return false;
				else
					return true;
			}
		}).toList();
		pst.close();
		return r;
	}

	@GET
	@Path("/findByNearBy")
	@ApiOperation(value = "Get the foodanddrinks infomation by nearby location", response = FoodAndDrink.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "FoodAndDrink not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public List<FoodAndDrink> GETFoodAndDrinksByNearBy(
			@ApiParam(required = true)@QueryParam("latitude") final double latitude,
			@ApiParam(required = true)@QueryParam("longitude") final double longitude,
			@ApiParam(value = "in meter", required = true)@QueryParam("distance") final Integer distance,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		String TN = getTableName(language);
		double delta = distance / 1000.0 * 0.02;
		double px_min = longitude - delta, px_max = longitude + delta, py_min = latitude - delta, py_max = latitude + delta;
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Place_Id` = `item_theme`.`Item_Id` and `"+ TN +"`.`Px`>? and `" + TN + "`.`Px`<? and `" + TN + "`.`Py`>? and `" + TN + "`.`Py`<? GROUP BY `"+ TN +"`.`Place_Id`");
		pst.setDouble(1, px_min);
		pst.setDouble(2, px_max);
		pst.setDouble(3, py_min);
		pst.setDouble(4, py_max);
		ResultSet result = pst.executeQuery();
		List<FoodAndDrink> r = Linq4j.asEnumerable(FoodAndDrinkHydrator.fromResultSettoList(result)).where(new Predicate1<FoodAndDrink>() {		
			@Override
			public boolean apply(FoodAndDrink arg0) {
				if(arg0.getPictures().size()==0)
					return false;
				if(DistanceCalculator.GetDistance(arg0.getLocation().getLongitude(), 
						arg0.getLocation().getLatitude(), longitude, latitude) > distance)
					return false;
				else
					return true;
			}
		}).toList();
		pst.close();
		return r;
	}
	
	@GET
	@Path("/findByRank")
	@ApiOperation(value = "Get the foodanddrinks infomation by rank", response = FoodAndDrink.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "FoodAndDrink not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public List<FoodAndDrink> GETFoodAndDrinksByRank(
			@ApiParam(value = "[0-5]", required = true)@QueryParam("rank") Integer rank,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Place_Id` = `item_theme`.`Item_Id` and `"+ TN +"`.`Avg_Rank` >= ? GROUP BY `"+ TN +"`.`Place_Id`");
		pst.setInt(1, rank);
		ResultSet result = pst.executeQuery();
		List<FoodAndDrink> r =  Linq4j.asEnumerable(FoodAndDrinkHydrator.fromResultSettoList(result)).where(new Predicate1<FoodAndDrink>() {
			
			@Override
			public boolean apply(FoodAndDrink arg0) {
				// TODO Auto-generated method stub
				if(arg0.getPictures().size()==0)
					return false;
				else
					return true;
			}
		}).toList();
		pst.close();
		return r;
	}

}
