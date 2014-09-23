package iii.org.tw.controller;

import iii.org.tw.entity.Activity;
import iii.org.tw.entity.Attraction;
import iii.org.tw.entity.FoodAndDrink;
import iii.org.tw.hydrator.ActivityHydrator;
import iii.org.tw.hydrator.AttractionHydrator;
import iii.org.tw.hydrator.FoodAndDrinkHydrator;
import iii.org.tw.util.ApiException;
import iii.org.tw.util.DbUtil;
import iii.org.tw.util.DistanceCalculator;
import iii.org.tw.util.LanguageNotSupportException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.ws.rs.*;

import net.hydromatic.linq4j.Linq4j;
import net.hydromatic.linq4j.function.Predicate1;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponses;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/activity")
@Api(value = "/activity", description = "Operations about activities")
@Produces({ "application/json" })
public class ActivityController {

	private String getTableName(String language) throws LanguageNotSupportException
	{
		if(language == null)
			language = "zh_tw";
		String tablename = "activity_zh_tw";
		switch(language)
		{
		case "en_us":
			tablename = "activity_en_us";
			break;
		case "zh_tw":
			tablename = "activity_zh_tw";
			break;
		case "ja_jp":
			tablename = "activity_ja_jp";
			break;
		default:
			throw new LanguageNotSupportException();
		}
		return tablename;
	}
	
	@GET
	@Path("/{id}")
	@ApiOperation(value = "Get the activity infomation by ID", response = Activity.class)
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Activity not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public Activity GETActivityByID(@PathParam("id") String id,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception{
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Activity_Id` = `item_theme`.`Item_Id` and `"+ TN +"`.`Activity_Id`=? GROUP BY `"+ TN +"`.`Activity_Id`");
		pst.setString(1, id);
		ResultSet result = pst.executeQuery();
		Activity r = ActivityHydrator.fromResultSet(result);
		pst.close();
		return r;
	}

	@GET
	@Path("/")
	@ApiOperation(value = "Get all activities infomation", response = Activity.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Activity not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public List<Activity> GETAllActivities(
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Activity_Id` = `item_theme`.`Item_Id` GROUP BY `"+ TN +"`.`Activity_Id`");
		ResultSet result = pst.executeQuery();
		List<Activity> r = ActivityHydrator.fromResultSettoList(result);
		pst.close();
		return r;
	}

	@GET
	@Path("/findTopTen")
	@ApiOperation(value = "Get Top 10 activities infomation", response = Activity.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Activity not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public List<Activity> GETActivitiesByTopTen(
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Activity_Id` = `item_theme`.`Item_Id` GROUP BY `"+ TN +"`.`Activity_Id` ORDER BY `"+ TN +"`.`Avg_Rank`");
		ResultSet result = pst.executeQuery();
		return ActivityHydrator.fromResultSettoList(result);
	}
	
	@GET
	@Path("/findByCounty")
	@ApiOperation(value = "Get the activities infomation by county id", response = Activity.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Activity not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public List<Activity> GETActivitiesByCounty(
			@ApiParam(required = true)@QueryParam("countyid") String countyid,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Activity_Id` = `item_theme`.`Item_Id` and `"+ TN +"`.`County_Id`=? GROUP BY `"+ TN +"`.`Activity_Id`");
		pst.setString(1, countyid);
		ResultSet result = pst.executeQuery();
		List<Activity> r = Linq4j.asEnumerable(ActivityHydrator.fromResultSettoList(result)).where(new Predicate1<Activity>() {
			
			@Override
			public boolean apply(Activity arg0) {
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
	@Path("/findByTheme")
	@ApiOperation(value = "Get the activities infomation by theme class id", response = Activity.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Activity not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public List<Activity> GETActivitiesByTheme(
			@ApiParam(required = true)@QueryParam("themeid") String themeid,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Activity_Id` = `item_theme`.`Item_Id` and `"+ TN +"`.`Activity_Id` IN ( SELECT DISTINCT `Item_Id` from `item_theme` where `Theme`=?) GROUP BY `"+ TN +"`.`Activity_Id`");
		pst.setString(1, themeid);
		ResultSet result = pst.executeQuery();
		List<Activity> r = ActivityHydrator.fromResultSettoList(result);
		pst.close();
		return r;
	}

	@GET
	@Path("/findByCriteria")
	@ApiOperation(value = "Get the activities infomation by some criterias", response = Activity.class, responseContainer = "List", hidden=true)
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Attraction not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public List<Attraction> GETActivitiesByCriteria(String body,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement("select * from place_zh_tw where Top_10=1");
		ResultSet result = pst.executeQuery();
		List<Attraction> r = AttractionHydrator.fromResultSettoList(result);
		pst.close();
		return r;
	}

	@GET
	@Path("/findByNearBy")
	@ApiOperation(value = "Get the activities infomation by nearby location", response = Activity.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Activity not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public List<Activity> GETActivitiesByNearBy(
			@ApiParam(required = true)@QueryParam("latitude") final double latitude,
			@ApiParam(required = true)@QueryParam("longitude") final double longitude,
			@ApiParam(value = "in meter", required = true)@QueryParam("distance") final Integer distance,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		String TN = getTableName(language);
		double delta = distance / 1000.0 * 0.02;
		double px_min = longitude - delta, px_max = longitude + delta, py_min = latitude - delta, py_max = latitude + delta;
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Activity_Id` = `item_theme`.`Item_Id` and `"+ TN +"`.`Px`>? and `" + TN + "`.`Px`<? and `" + TN + "`.`Py`>? and `" + TN + "`.`Py`<? GROUP BY `"+ TN +"`.`Activity_Id`");
		pst.setDouble(1, px_min);
		pst.setDouble(2, px_max);
		pst.setDouble(3, py_min);
		pst.setDouble(4, py_max);
		ResultSet result = pst.executeQuery();
		List<Activity> r = Linq4j.asEnumerable(ActivityHydrator.fromResultSettoList(result)).where(new Predicate1<Activity>() {
			
			@Override
			public boolean apply(Activity arg0) {
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
	@ApiOperation(value = "Get the activities infomation by rank", response = Activity.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Activity not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public List<Activity> GETActivitiesByRank(
			@ApiParam(value = "[0-5]", required = true)@QueryParam("rank") Integer rank,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Activity_Id` = `item_theme`.`Item_Id` and `"+ TN +"`.`Avg_Rank` >= ? GROUP BY `"+ TN +"`.`Activity_Id`");
		pst.setInt(1, rank);
		ResultSet result = pst.executeQuery();
		List<Activity> r = ActivityHydrator.fromResultSettoList(result);
		pst.close();
		return r;
	}
	
	@GET
	@Path("/findByDate")
	@ApiOperation(value = "Get the activities infomation by date", response = Activity.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Activity not found"),
			  @ApiResponse(code = 400, message = "Invalid query parameter format") 
			})
	public List<Activity> GETActivitiesByDate(
			@ApiParam(required = true, value = "format yyyy-MM-dd")@QueryParam("Date") String date,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateObj =null;
		try{
			dateObj = sdf.parse(date);
		}
		catch(ParseException e)
		{
			throw new ApiException(400,"Invalid Date format yyyy-MM-dd");
		}
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT *,GROUP_CONCAT(`item_theme`.`Theme` SEPARATOR ',' ) AS `Theme_Concat` FROM `"+ TN +"` JOIN `item_theme` WHERE `"+ TN +"`.`Activity_Id` = `item_theme`.`Item_Id` and `"+ TN +"`.`Start_Time`<=? and `"+ TN +"`.`End_Time`>=? GROUP BY `"+ TN +"`.`Activity_Id`");
		pst.setDate(1, new java.sql.Date(dateObj.getTime()));
		pst.setDate(2, new java.sql.Date(dateObj.getTime()));
		ResultSet result = pst.executeQuery();
		List<Activity> r = ActivityHydrator.fromResultSettoList(result);
		pst.close();
		return r;
	}

}
