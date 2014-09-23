package iii.org.tw.controller;

import iii.org.tw.entity.Attraction;
import iii.org.tw.entity.Picture;
import iii.org.tw.entity.PictureMeta;
import iii.org.tw.entity.Tour;
import iii.org.tw.util.DbUtil;
import iii.org.tw.util.LanguageNotSupportException;
import iii.org.tw.util.NotFoundException;
import iii.org.tw.util.SingletonControllerHelper;
import iii.org.tw.util.tourexception;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/tour")
@Api(value = "/tour", description = "Operations about tours")
@Produces(MediaType.APPLICATION_JSON + ";charset=utf-8")
public class TourController {
	

	/**
	 * query all tours
	 * 
	 * http://localhost:8081/SmartTourismApi/api/v1/tour
	 * 
	 * @throws LanguageNotSupportException
	 * @throws SQLException
	 * @throws NotFoundException
	 */
	private String switchlang(String language)
			throws LanguageNotSupportException {
		if (language == null)
			language = "zh_tw";
		String tablename = "_zh_tw";
		switch (language) {
		case "en_us":
			tablename = "_en_us";
			break;
		case "zh_tw":
			tablename = "_zh_tw";
			break;
		case "ja_jp":
			tablename = "_ja_jp";
			break;
		default:
			throw new LanguageNotSupportException();
		}
		return tablename;
	}

	public static Properties props = new Properties();

	static {
		try {
			props.load(TourController.class
					.getResourceAsStream("/db.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	@GET
	@ApiOperation(value = "得到所有的行程", response = Tour.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Internal Error"),
			@ApiResponse(code = 404, message = "tour not found"),
			@ApiResponse(code = 400, message = "Invalid Language String") })
	public List<Tour> gettour(
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp") @HeaderParam("language") String lan)
			throws Exception {
		PreparedStatement resultb;
		String lang = switchlang(lan);
		Connection con = null;
		try {
			con = DbUtil.getConnection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet result = null;
		try {
			resultb = con.prepareStatement("select * from tour" + lang);
			result = resultb.executeQuery();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Tour> multitour = new ArrayList<Tour>();
		result = tourexception.check(result);
		try {
			while (result.next()) {
				ArrayList<String> relateid = new ArrayList<String>();
				// String
				// qrid="select * from tour_place where Tour_Id ='"+result.getString("Tour_Id")+"' ORDER BY 'Place_Order' ASC";
				resultb = con
						.prepareStatement("select * from tour_place where Tour_Id =? ORDER BY 'Place_Order' ASC");
				resultb.setString(1, result.getString("Tour_Id"));
				ResultSet rela = resultb.executeQuery();
				while (rela.next()) {
					relateid.add(rela.getString("Place_Id"));
				}

				ArrayList<Picture> pics = putpic(result.getString("Picture"),
						result.getString("Picture_Description"));
				ArrayList<String> ttheme = new ArrayList<String>();
				String qe = "select * from item_theme where Item_Id =?";
				resultb = con.prepareStatement(qe);
				resultb.setString(1, result.getString("Tour_Id"));
				ResultSet restheme = resultb.executeQuery();
				while (restheme.next()) {
					ttheme.add(restheme.getString("Theme"));
				}
				Tour ntour = new Tour();
				ntour.setId(result.getString("Tour_Id"));
				ntour.setName(result.getString("Name"));
				ntour.setDescription(result.getString("Description"));
				ntour.setCollection(result.getInt("Collection"));
				ntour.setThemes(ttheme);
				ntour.setPictures(pics);
				ntour.setRelatedid(SingletonControllerHelper.getPoiList(relateid));
				ntour.setShareUrl(props.getProperty("share.url.bath")
						+ props.getProperty("share.url.Tour")
						+ result.getString("Tour_Id"));
				multitour.add(ntour);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Gson gson = new Gson();
		// String json = gson.toJson(multitour);
		// return json;
		return multitour;
	}

	/**
	 * query one tour
	 * 
	 * http://localhost:8081/SmartTourismApi/api/v1/tour/{id}
	 * 
	 * @throws Exception
	 * 
	 */
	@GET
	@Path("/{id}")
	@ApiOperation(value = "用tourid找出某一行程", response = Tour.class)
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Internal Error"),
			@ApiResponse(code = 404, message = "tour not found"),
			@ApiResponse(code = 400, message = "Invalid Language String") })
	public Tour gettourbyid(
			@PathParam("id") String id,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp") @HeaderParam("language") String lan)
			throws Exception {

		String lang = switchlang(lan);
		Connection con = DbUtil.getConnection();
		// Statement rst = con.createStatement();
		PreparedStatement resultb = con.prepareStatement("select * from tour"
				+ lang + " where Tour_Id= ?");
		resultb.setString(1, id);
		ResultSet result = resultb.executeQuery();
		result = tourexception.check(result);
		result.next();

		ArrayList<String> relateid = new ArrayList<String>();
		String qrid = "select * from tour_place where Tour_Id =? ORDER BY 'Place_Order' ASC";
		resultb = con.prepareStatement(qrid);
		resultb.setString(1, id);
		ResultSet rela = resultb.executeQuery();
		while (rela.next()) {
			relateid.add(rela.getString("Place_Id"));
		}

		ArrayList<Picture> pics = putpic(result.getString("Picture"),
				result.getString("Picture_Description"));
		ArrayList<String> ttheme = new ArrayList<String>();
		String qe = "select * from item_theme where Item_Id =?";
		resultb = con.prepareStatement(qe);
		resultb.setString(1, result.getString("Tour_Id"));
		ResultSet restheme = resultb.executeQuery();
		while (restheme.next()) {
			ttheme.add(restheme.getString("Theme"));
		}

		Tour ntour = new Tour();
		ntour.setId(result.getString("Tour_Id"));
		ntour.setName(result.getString("Name"));
		ntour.setDescription(result.getString("Description"));
		ntour.setCollection(result.getInt("Collection"));
		ntour.setThemes(ttheme);
		ntour.setPictures(pics);
		ntour.setRelatedid(SingletonControllerHelper.getPoiList(relateid));
		ntour.setShareUrl(props.getProperty("share.url.bath")
				+ props.getProperty("share.url.Tour")
				+ result.getString("Tour_Id"));

		// Gson gson = new Gson();
		// String json = gson.toJson(ntour);
		// return json;
		return ntour;
	}

	/**
	 * query one tour
	 * 
	 * http://localhost:8081/SmartTourismApi/api/v1/tour/findByTheme
	 * 
	 * @throws SQLException
	 * @throws NotFoundException
	 * @throws LanguageNotSupportException
	 * @throws Exception
	 * 
	 */
	@GET
	@Path("/findByTheme")
	@ApiOperation(value = "以themeid來找到符合之行程", response = Tour.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Internal Error"),
			@ApiResponse(code = 404, message = "tour not found"),
			@ApiResponse(code = 400, message = "Invalid Language String") })
	public List<Tour> findbytheme(
			@ApiParam(required = true) @QueryParam("themeid") String themeid,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp") @HeaderParam("language") String lan)
			throws Exception {
		PreparedStatement resultb;
		String lang = switchlang(lan);
		Connection con = null;
		try {
			con = DbUtil.getConnection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet result = null;
		String aqry = "Select * from tour" + lang + " join item_theme on tour"
				+ lang
				+ ".Tour_Id = item_theme.Item_Id and item_theme.Theme =?";
		try {
			resultb = con.prepareStatement(aqry);
			resultb.setString(1, themeid);
			result = resultb.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Tour> multitour = new ArrayList<Tour>();
		result = tourexception.check(result);
		try {
			while (result.next()) {
				ArrayList<String> relateid = new ArrayList<String>();
				String qrid = "select * from tour_place where Tour_Id =? ORDER BY 'Place_Order' ASC";
				resultb = con.prepareStatement(qrid);
				resultb.setString(1, result.getString("Tour_Id"));
				ResultSet rela = resultb.executeQuery();
				while (rela.next()) {
					relateid.add(rela.getString("Place_Id"));
				}

				ArrayList<Picture> pics = putpic(result.getString("Picture"),
						result.getString("Picture_Description"));
				ArrayList<String> ttheme = new ArrayList<String>();
				String qe = "select * from item_theme where Item_Id =?";
				resultb = con.prepareStatement(qe);
				resultb.setString(1, result.getString("Tour_Id"));
				ResultSet restheme = resultb.executeQuery();
				while (restheme.next()) {
					ttheme.add(restheme.getString("Theme"));
				}
				Tour ntour = new Tour();
				ntour.setId(result.getString("Tour_Id"));
				ntour.setName(result.getString("Name"));
				ntour.setDescription(result.getString("Description"));
				ntour.setCollection(result.getInt("Collection"));
				ntour.setThemes(ttheme);
				ntour.setPictures(pics);
				ntour.setRelatedid(SingletonControllerHelper.getPoiList(relateid));
				ntour.setShareUrl(props.getProperty("share.url.bath")
						+ props.getProperty("share.url.Tour")
						+ result.getString("Tour_Id"));
				multitour.add(ntour);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

//		Gson gson = new Gson();
//		String json = gson.toJson(multitour);
		return multitour;

	}

	/**
	 * 
	 * 
	 * http://localhost:8081/SmartTourismApi/api/v1/tour/findByAttraction
	 * 
	 * @throws SQLException
	 * @throws NotFoundException
	 * @throws LanguageNotSupportException
	 * @throws Exception
	 * 
	 */
	@GET
	@Path("/findByAttraction")
	@ApiOperation(value = "以景點id來找到符合之行程", response = Tour.class, responseContainer = "List")
	@ApiResponses(value = {
			@ApiResponse(code = 500, message = "Internal Error"),
			@ApiResponse(code = 404, message = "tour not found"),
			@ApiResponse(code = 400, message = "Invalid Language String") })
	public String findByAttraction(
			@ApiParam(required = true) @QueryParam("attractionid") String attractionid,
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp") @HeaderParam("language") String lan)
			throws Exception {
		PreparedStatement resultb;
		String lang = switchlang(lan);
		Connection con = null;
		try {
			con = DbUtil.getConnection();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		ResultSet result = null;
		ResultSet preresult = null;
		String aqry = "select * from tour_place  where `Place_Id`=?";
		// String
		// aqry="SELECT * ,GROUP_CONCAT(tour_place.Place_Id ORDER BY tour_place.Place_Order ASC SEPARATOR',')as related  FROM `tour_place` join `tour"+lang+"` on tour"+lang+".Tour_Id=tour_place.Tour_Id join "
		// +
		// "(select *,GROUP_CONCAT(item_theme"+lang+".Theme_Id SEPARATOR',')as alltheme FROM `item_theme"+lang+"` group by item_theme"+lang+".Item_Id )stab on stab.Item_Id=tour_place.Tour_Id WHERE tour_place."
		// +
		// "Tour_Id=(select Tour_Id from tour_place where `Place_Id`=?)group by tour_place.Tour_Id";
		// SELECT *,GROUP_CONCAT(tour_place.Place_Id ORDER BY
		// tour_place.Place_Order ASC SEPARATOR',')as related FROM
		// `tour_place`join `tour_part_zh_tw` on
		// tour_part_zh_tw.Tour_Id=tour_place.Tour_Id join `item_theme_zh_tw` on
		// item_theme_zh_tw.Item_Id=tour_place.Tour_Id WHERE
		// tour_place.Tour_Id=(select Tour_Id from tour_place where
		// `Place_Id`='1_379000000A_000145') group by tour_place.Tour_Id
		// SELECT * ,GROUP_CONCAT(tour_place.Place_Id ORDER BY
		// tour_place.Place_Order ASC SEPARATOR',')as related FROM
		// `tour_place`join `tour_zh_tw` on
		// tour_zh_tw.Tour_Id=tour_place.Tour_Id join (select
		// *,GROUP_CONCAT(item_theme_zh_tw.Theme_Id SEPARATOR',')as alltheme
		// FROM `item_theme_zh_tw` group by item_theme_zh_tw.Item_Id )stab on
		// stab.Item_Id=tour_place.Tour_Id WHERE tour_place.Tour_Id=(select
		// Tour_Id from tour_place where `Place_Id`='3_379000000A_000929')group
		// by tour_place.Tour_Id
		try {
			resultb = con.prepareStatement(aqry);
			resultb.setString(1, attractionid);
			preresult = resultb.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Tour> multitour = new ArrayList<Tour>();
		preresult = tourexception.check(preresult);
		try {
			while (preresult.next()) {

				String ak = "SELECT * ,GROUP_CONCAT(tour_place.Place_Id ORDER BY tour_place.Place_Order ASC SEPARATOR',')as related  FROM `tour_place` join `tour"
						+ lang
						+ "` on tour"
						+ lang
						+ ".Tour_Id=tour_place.Tour_Id join (select *,GROUP_CONCAT(item_theme.Theme SEPARATOR',')as alltheme FROM `item_theme` group by item_theme.Item_Id )stab on stab.Item_Id=tour_place.Tour_Id WHERE tour_place.Tour_Id=? group by tour_place.Tour_Id";
				resultb = con.prepareStatement(ak);
				resultb.setString(1, preresult.getString("Tour_Id"));

				result = resultb.executeQuery();

				result = tourexception.check(result);
				result.next();

				ArrayList<String> relateid = putarray(result
						.getString("related"));

				ArrayList<Picture> pics = putpic(result.getString("Picture"),
						result.getString("Picture_Description"));
				ArrayList<String> ttheme = putarray(result
						.getString("alltheme"));
				Tour ntour = new Tour();
				ntour.setId(result.getString("Tour_Id"));
				ntour.setName(result.getString("Name"));
				ntour.setDescription(result.getString("Description"));
				ntour.setCollection(result.getInt("Collection"));
				ntour.setThemes(ttheme);
				ntour.setPictures(pics);
				ntour.setRelatedid(SingletonControllerHelper.getPoiList(relateid));
				ntour.setShareUrl(props.getProperty("share.url.bath")
						+ props.getProperty("share.url.Tour")
						+ result.getString("Tour_Id"));
				multitour.add(ntour);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Gson gson = new Gson();
		String json = gson.toJson(multitour);
		return json;

	}
	
	

	private String[] part(String sb) {
		return sb.split(",");
	}

	private ArrayList<String> putarray(String starray) {
		ArrayList<String> ape = new ArrayList<String>();

		if (starray == null || starray.equals(""))
			;
		else {
			for (String id : part(starray)) {
				ape.add(id);
			}
		}
		return ape;
	}

	private ArrayList<Picture> putpic(String pic, String desc) {
		ArrayList<Picture> ape = new ArrayList<Picture>();

		if (pic != null && !pic.equals("")) {
			int index = 0;
			List<String> picdecs = new ArrayList<String>();
			if (desc != null)
				picdecs = Arrays.asList(desc.split("[|]{3}"));
			for (String picurl : pic.split(",")) {
				Picture apic = new Picture();
				PictureMeta meta = new PictureMeta();
				if (picurl.startsWith("http")){
					meta.setUrl(picurl);
					apic.setUrl(picurl);
				}
				else{
					meta.setUrl(props.getProperty("images.hosturl") + picurl);
					apic.setUrl(props.getProperty("images.hosturl") + picurl);
				}
				meta.setWidth(512);	//FIX ME
				meta.setHeight(300);	//FIX ME
				apic.getMetas().add(meta);
				if (index < picdecs.size())
					apic.setDescription(picdecs.get(index));
				else
					apic.setDescription("");
				index++;
				ape.add(apic);
			}
		}
		return ape;
	}
}