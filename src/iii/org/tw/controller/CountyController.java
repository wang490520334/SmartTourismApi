package iii.org.tw.controller;

import iii.org.tw.entity.County;
import iii.org.tw.util.DbUtil;
import iii.org.tw.util.LanguageNotSupportException;
import iii.org.tw.util.NotFoundException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/county")
@Api(value = "/county", description = "Operations about counties")
@Produces({ "application/json" })
public class CountyController {

	private String getTableName(String language) throws LanguageNotSupportException
	{
		if(language == null)
			language = "zh_tw";
		String tablename = "county_zh_tw";
		switch(language)
		{
		case "en_us":
			tablename = "county_en_us";
			break;
		case "zh_tw":
			tablename = "county_zh_tw";
			break;
		case "ja_jp":
			tablename = "county_ja_jp";
			break;
		default:
			throw new LanguageNotSupportException();
		}
		return tablename;
	}
	
	@GET
	@Path("/")
	@ApiOperation(value = "Get all counties infomation", response = County.class, responseContainer = "List")
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "County not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
	public List<County> GETAllCounties(
			@ApiParam(allowableValues = "zh_tw,en_us,ja_jp")@HeaderParam("language") String language) throws Exception{
		String TN = getTableName(language);
		Connection con = DbUtil.getConnection();
		PreparedStatement pst = con.prepareStatement(
				"SELECT * FROM `"+ TN + "`" );
		ResultSet result = pst.executeQuery();
		ArrayList<County> results = new ArrayList<County>();
		while (result.next()) {
			County c = new County();
			c.setId(result.getString("County_Id"));
			c.setName(result.getString("Name"));
			c.setRegion(result.getString("Region"));
			results.add(c);
		}
		pst.close();
		if(results.size() == 0)
			throw new NotFoundException("No such data");
		return results;
	}
}