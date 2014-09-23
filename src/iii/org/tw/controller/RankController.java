package iii.org.tw.controller;
 
import iii.org.tw.definition.Type;
import iii.org.tw.entity.Attraction;
import iii.org.tw.entity.Rank;
import iii.org.tw.model.RankCmpModel;
import iii.org.tw.model.RankInput;
import iii.org.tw.model.RankOutput;
import iii.org.tw.service.UserTokenService;
import iii.org.tw.util.DbUtil;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
 
@Path("/rank")
@Api(value = "/rank", description = "Operations about rank")
@Produces(MediaType.APPLICATION_JSON)
public class RankController {
	
	Logger logger = LoggerFactory.getLogger(RankController.class);
	private Statement stat = null ; //執行, 傳入需為完整sql字串
	private ResultSet rs = null ; //sql result 
	private PreparedStatement pst = null;  //執行,傳入之sql為預儲之字申,需要傳入變數之位置 
										    //先利用?來做標示 

    /**
     * create rank 
     * 
     * http://localhost:8080/SmartTourismApi/api/v1/rank
     * @throws Exception 
     */
    @POST    
    @ApiOperation(value = "新增一rank評分(若對一物件重複評分則覆蓋先前評分)", response = Rank.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Attraction not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
    public Rank createRank( @ApiParam(value = "[本平台使用者token(登入時取得)] 或 [第三方APP userKey]", required = true) @HeaderParam("userToken") String userToken, 
    		@ApiParam(value = "[第三方APP apiKey] (暫為任意字串)", required = true) @HeaderParam("apiKey") String apiKey, 
    		@ApiParam(value = "評分所需資料(Rank Object)", required = true) RankInput httpBody ) throws Exception {
		
    	logger.info( "createEvent :" + httpBody ) ;
    	  		
    	Class.forName("com.mysql.jdbc.Driver"); 
    	//註冊driver
    	Connection con = DbUtil.getConnection() ;
    		
    	String sqlCmd = "SELECT * FROM rank WHERE Place_Id = '" + httpBody.getPlaceId() + "' AND User_Id = '" + UserTokenService.getUserId(userToken) + "'" ;    	
    	stat = con.createStatement() ; // 執行,傳入之sql為完整字串
    	rs = stat.executeQuery(sqlCmd) ;
    	
    	String uuid = UUID.randomUUID().toString() ;
    	
    	if ( !rs.next() ) { // 此物件尚未被此使用者評分，新增一評分
    		
    		sqlCmd = "INSERT INTO rank ( Rank_Id, Place_Id, Api_Key, User_Id, Rank, Type )"
        			+ " VALUES( ?, ?, ?, ?, ?, ? );" ;
        		    		
        	pst = con.prepareStatement(sqlCmd) ;
        	pst.setString(1, uuid);
        	pst.setString(2, httpBody.getPlaceId());
        	pst.setString(3, apiKey);
        	pst.setString(4, UserTokenService.getUserId(userToken));
        	pst.setInt(5, httpBody.getRank());
        	pst.setInt(6, Type.getTypeIntByString(httpBody.getType()));

        	pst.executeUpdate() ;    	
    		
    	} else {

    		uuid = rs.getString("Rank_Id") ;
    		
    		sqlCmd = "UPDATE rank SET Api_Key = '" + apiKey + "', Rank = '" + httpBody.getRank() + "' WHERE Rank_Id = '" + rs.getString("Rank_Id") + "'" ;    		
    		stat.executeUpdate(sqlCmd) ;
    		
    	} // end else    	
    	
    	Rank result = new Rank(uuid, httpBody.getPlaceId(),  UserTokenService.getUserId(userToken), httpBody.getRank(), Type.getTypeIntByString(httpBody.getType())) ;
    	
    	return result ;    	
    } // createRank()    

    /**
     * Rank getRank
     * 
     * http://localhost:8080/SmartTourismApi/api/v1/rank/{placeId}
     * @throws Exception 
     */
//    @GET
//    @ApiOperation(value = "取得rank評分資訊", response = Rank.class)
//    @ApiResponses(value = {
//			  @ApiResponse(code = 500, message = "Internal Error"),
//			  @ApiResponse(code = 404, message = "Attraction not found"),
//			  @ApiResponse(code = 400, message = "Invalid Language String") 
//			})    
//    @Path("/{rankId}")
//    public String getRank( @ApiParam(value = "要查詢的rank id", required = true) @PathParam("rankId") String rankId,
//    		@ApiParam(value = "[第三方APP apiKey]", required = true) @HeaderParam("apiKey") String apiKey ) throws Exception {
//		
//    	logger.info("getRank :"+ rankId );    	
//    	
//    	Gson gson = new Gson(); // output json object 
//    	String json = null ;    	
//    	Rank result = null ;
//
//   		Class.forName("com.mysql.jdbc.Driver"); 
//   		//註冊driver
//    		
//   		Connection con = DbUtil.getConnection() ;    	
//    		
//   		String sqlCmd = "select * from rank where Rank_Id= '" + rankId + "';" ; // 連結Object
//   		stat = con.createStatement(); // 執行,傳入之sql為完整字串 
//   		rs = stat.executeQuery(sqlCmd) ; // result
//    		
//   		if ( !rs.next() ) //判斷有無回傳結果
//   			throw new Exception("cant find this Event") ;
//   		else		
//    		result = new Rank( rs.getString("Rank_Id"), rs.getString("Place_Id"), rs.getString("User_Id"), rs.getInt("rank"), rs.getInt("type")) ;    		 
//    		    		
//   		json = gson.toJson(result);
//        return json ;     	
//    } // getRank()
    

    /**
     * Rank getUserAllRank
     * 
     * http://localhost:8080/SmartTourismApi/api/v1/rank/
     * @throws Exception 
     */
    @GET
    @ApiOperation(value = "取得單一使用者的所有rank評分資訊", response = RankOutput.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Attraction not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
    @Path("/getUserAllRank")
    public List<RankOutput> getUserAllRank( @ApiParam(value = "[本平台使用者token(登入時取得)] 或 [第三方APP userKey]", required = true) @HeaderParam("userToken") String userToken,
    		@ApiParam(value = "[第三方APP apiKey] (暫為任意字串)", required = true) @HeaderParam("apiKey") String apiKey ) throws Exception {
    	
    	logger.info("getUserAllRank :"+ UserTokenService.getUserId(userToken) );    	
    	    	     
    	List<RankOutput> result = new ArrayList<RankOutput>() ;
   		Class.forName("com.mysql.jdbc.Driver"); 
   		//註冊driver
    		
   		Connection con = DbUtil.getConnection() ;    	
    		
   		String sqlCmd = "select * from rank where User_Id= '" + UserTokenService.getUserId(userToken) + "';" ; // 連結Object
   		stat = con.createStatement(); // 執行,傳入之sql為完整字串 
   		rs = stat.executeQuery(sqlCmd) ; // result
    		
   		if ( !rs.next() ) //判斷有無回傳結果
   			result = Collections.emptyList() ; // 回傳空陣列
   		else {
   			do {
   				RankOutput tmpRank = new RankOutput( rs.getString("Rank_Id"), rs.getString("Place_Id"), rs.getInt("rank"), Type.getTypeStringByInt( rs.getInt("type") ) ) ;
   				result.add(tmpRank) ;
   			} while ( rs.next() ) ;   			   			   		
   		} // end else
   		
        return result ;
    } // getUserAllRank
    
    
    /**
     * Rank getRankByStandard
     * 
     * http://localhost:8080/SmartTourismApi/api/v1/rank
     * @throws Exception 
     */
    @GET
    @ApiOperation(value = "拿到所有類型且rank分數大於指定值的物件", response = RankCmpModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Attraction not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})      
    public List<RankCmpModel> getObjectMoreThanRankStandard( @ApiParam(value = "rank標準(0~5)", required = true)@QueryParam("rankStd") String rankStd,
    		@ApiParam(value = "[第三方APP apiKey] (暫為任意字串)", required = true) @HeaderParam("apiKey") String apiKey ) throws Exception {
    	
    	logger.info("getRankByStandard :"+ rankStd );    	    	  	
    	List<RankCmpModel> result = new ArrayList<RankCmpModel>() ;
    	    		
   		Class.forName("com.mysql.jdbc.Driver"); 
   		//註冊driver
    		
   		Connection con = DbUtil.getConnection() ;    	
    		
   		String[] tableName = {"place_part", "food_drink_part", "souvenirs_part", "tour_part", "activity_part"} ;
    		
   		for ( int i = 0 ; i < tableName.length ; i++ ) {
   			
   			String idColumnName = "" ; 
   			if ( i < 3 ) // 前三張table, id name為Place_Id,與4, 5張不同
   				idColumnName = "Place_Id" ;
   			else if ( i == 3 )
   				idColumnName = "Tour_Id" ;
   			else if ( i == 4 )
   				idColumnName = "Activity_Id" ;
   			
   			String sqlCmd = "SELECT * "
       				+ "FROM " + tableName[i] + "_general as PG RIGHT JOIN " + tableName[i] + "_zh_tw as PP on PG." + idColumnName + " = PP." + idColumnName
       				+ " WHERE PG.Avg_Rank > " + rankStd ; // 連結Object

   			stat = con.createStatement(); // 執行,傳入之sql為完整字串 
       		rs = stat.executeQuery(sqlCmd) ; // result
        		
       		if ( !rs.next() ) ;	//判斷有無回傳結果       			
       		else { 	    			
   	    		do {   	    			
   	    			RankCmpModel tmpEntity = new RankCmpModel(rs.getString("Place_Id"), Type.getTypeStringByInt(i), rs.getFloat("Avg_Rank")) ;
   		    		result.add(tmpEntity) ;
   	    		} while ( rs.next() ) ;	    		
       		} // end else 
   		} // end for
   		
   		if ( result.size() == 0 )
   			throw new Exception("no object is matched.") ;
   
    	return result ;
    } // getObjectMoreThanRankStandard()
}