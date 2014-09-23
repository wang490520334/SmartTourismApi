package iii.org.tw.controller;
 
import iii.org.tw.entity.CalendarEvent;
import iii.org.tw.model.BooleanModel;
import iii.org.tw.model.CalendarEventInput;
import iii.org.tw.model.CalendarEventOutput;
import iii.org.tw.model.ContentModel;
import iii.org.tw.model.PoiEvent;
import iii.org.tw.model.PoiEventIOModel;
import iii.org.tw.service.UserTokenService;
import iii.org.tw.util.DbUtil;
import iii.org.tw.util.SmartTourismException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import java.lang.reflect.Type;
import java.sql.*;
 
@Path("/calendar")
@Api(value = "/calendar", description = "Operations about calendar")
@Produces(MediaType.APPLICATION_JSON)
public class CalendarController {
	
	
	Logger logger = LoggerFactory.getLogger(CalendarController.class);
	private Statement stat = null ; //執行, 傳入需為完整sql字串
	private ResultSet rs = null ; //sql result 
	private PreparedStatement pst = null; //執行,傳入之sql為預儲之字申,需要傳入變數之位置 
										   //先利用?來做標示 
	
    /**
     * Calendar getEvent
     * 
     * http://localhost:8080/SmartTourismApi/api/v1/calendar/{eventId}
     * @throws java.lang.Exception 
     */
    @GET
    @ApiOperation(value = "利用已知事件id取得事件資料", response = CalendarEventOutput.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Attraction not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
    @Path("/{eventId}")
    public CalendarEventOutput getEvent( @ApiParam(value = "要查詢的事件id", required = true) @PathParam("eventId") String eventId, 
    		@ApiParam(value = "[本平台使用者token(登入時取得)] 或 [第三方APP userKey]", required = true) @HeaderParam("userToken") String userToken,
    		@ApiParam(value = "[第三方APP apiKey] (暫為任意字串)", required = true) @HeaderParam("apiKey") String apiKey ) throws Exception {
		
    	logger.info("getEvent :"+eventId);    	

    	CalendarEventOutput result = null;	
   		String userId = UserTokenService.getUserId(userToken) ;
    		
   		Class.forName("com.mysql.jdbc.Driver"); 
   		//註冊driver
    		
   		Connection con = DbUtil.getConnection() ;    	
    		
   		String sqlCmd = "select * from calendar_events where Event_Id= '" + eventId + "' and User_Id = '" + userId + "'"; // 連結Object
   		stat = con.createStatement(); // 執行,傳入之sql為完整字串 
   		rs = stat.executeQuery(sqlCmd) ; // result
    		
   		if ( !rs.next() ) {	//判斷有無回傳結果
   			throw new SmartTourismException("cant find this Event") ;
   		} else { 	    		
   			
   			Gson input = new Gson(); 
   			Type PoiEventList = new TypeToken<List<PoiEventIOModel>>() {
				private static final long serialVersionUID = 1L;
			}.getType();
						
   			List<PoiEventIOModel> dayEvent = input.fromJson(rs.getString("Event_Poi"), PoiEventList) ;
   			
   			
	   		result = new CalendarEventOutput( rs.getString("Event_Id"), rs.getString("Event_Type"),
	   				rs.getString("Event_Title"), rs.getString("Start_Time"), rs.getString("End_Time"),
	   				dayEvent, rs.getString("Notice"), rs.getInt("Remind") ) ;    		
    	} // end else 

    	return result ;    	
    } // getEvent()
    
    /**
     * Calendar createEvent
     * 
     * http://localhost:8080/SmartTourismApi/api/v1/calendar/
     * @throws Exception 
     */
    @ApiOperation(value = "增加一個新的事件到行事曆中", response = CalendarEvent.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Attraction not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
    @POST
    public String createEvent( @ApiParam(value = "[本平台使用者token(登入時取得)] 或 [第三方APP userKey]", required = true) @HeaderParam("userToken") String userToken,
    		@ApiParam(value = "[第三方APP apiKey] (暫為任意字串)", required = true) @HeaderParam("apiKey") String apiKey,
    		@ApiParam(value = "新增事件所需之詳細資料(CalendarEventInput Object)", required = true) CalendarEventInput httpBody ) throws Exception {
    	
    	
    	logger.info( "createEvent :" + httpBody ) ;
    	
    	Gson gsonInput = new Gson();	    	
    	List<PoiEvent> dayEvent = httpBody.getEvent_Poi() ;
    	

    	Class.forName("com.mysql.jdbc.Driver"); 
    	//註冊driver
    	Connection con = DbUtil.getConnection() ;
    		
    	String sqlCmd = "INSERT INTO calendar_events ( Event_Id, Event_Title, Event_Type, Api_Key, User_Id, Start_Time, End_Time, Event_Poi, Notice, Remind )"
    			+ " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );" ;
    	
    	String uuid = UUID.randomUUID().toString() ;
    	
    	pst = con.prepareStatement(sqlCmd) ;
    	pst.setString(1, uuid);
    	pst.setString(2, httpBody.getEvent_Title());
    	pst.setString(3, httpBody.getEvent_Type());
    	pst.setString(4, apiKey);
    	pst.setString(5, UserTokenService.getUserId(userToken));
    	pst.setString(6, httpBody.getStart_Time());
    	pst.setString(7, httpBody.getEnd_Time());
    	pst.setString(8, gsonInput.toJson(dayEvent));
    	pst.setString(9, httpBody.getNotice());
    	pst.setInt(10, httpBody.getRemind());
    		
    	pst.executeUpdate() ;
    	
    	CalendarEvent result = new CalendarEvent(uuid, httpBody.getEvent_Type(), UserTokenService.getUserId(userToken), httpBody.getEvent_Title(), httpBody.getStart_Time(), httpBody.getEnd_Time(), dayEvent, httpBody.getNotice(), httpBody.getRemind()) ;
    	
    	
    	Gson gsonOutput = new Gson();
    	String json = gsonOutput.toJson(result);    	
    	return json ;
    	
    } //createEvent()
    
    /**
     * Calendar modifyEvent
     * 
     * http://localhost:8080/SmartTourismApi/api/v1/calendar/{eventId}
     */
    @ApiOperation(value = "更新一行事曆中的事件", response = CalendarEvent.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Attraction not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
    @PUT
    @Path("/{eventId}")
    public String modifyEvent( @ApiParam(value = "要修改的事件id", required = true) @PathParam("eventId") String eventId,
    		@ApiParam(value = "[本平台使用者token(登入時取得)] 或 [第三方APP userKey]", required = true) @HeaderParam("userToken") String userToken,
    		@ApiParam(value = "[第三方APP apiKey] (暫為任意字串)", required = true) @HeaderParam("apiKey") String apiKey,
    		@ApiParam(value = "更新事件所需之詳細資料(CalendarEventInput Object)", required = true) CalendarEventInput httpBody) throws Exception {
    	
    	logger.info("modifyEvent :" + eventId + "body: " + httpBody );
    	
    	Gson gsonInput = new Gson();    	
    	Gson gson = new Gson(); // output json object 
    	String json = null ;  
    	
    	
   		Class.forName("com.mysql.jdbc.Driver") ;	//註冊driver
   		Connection con = DbUtil.getConnection() ;
   		stat = con.createStatement() ; // 執行,傳入之sql為完整字串
    		 
    		
   		String sqlCmd = "select * from calendar_events where Event_Id= '" + eventId + "' and User_Id = '" + UserTokenService.getUserId(userToken) + "'"; // 連結Object
   		rs = stat.executeQuery(sqlCmd) ;
   		if ( !rs.next() ) //判斷有無回傳結果
   			throw new Exception("cant find this Event") ;
   		else { 
   			
   			List<PoiEvent> dayEvent = httpBody.getEvent_Poi() ;
   			
    		sqlCmd = "UPDATE calendar_events SET ";
    		if ( httpBody.getEvent_Title() != null )
    			sqlCmd = sqlCmd + "Event_Title = '" + httpBody.getEvent_Title() + "'," ;
    		if ( httpBody.getEvent_Type() != null )
    			sqlCmd = sqlCmd + "Event_Type = '" + httpBody.getEvent_Type() + "'," ; 
    		if ( httpBody.getStart_Time() != null )
    			sqlCmd = sqlCmd + "Start_Time = '" + httpBody.getStart_Time() + "'," ; 
    		if ( httpBody.getEnd_Time() != null )
    			sqlCmd = sqlCmd + "End_Time = '" + httpBody.getEnd_Time() + "'," ;
	    	if ( httpBody.getEvent_Poi() != null )
    			sqlCmd = sqlCmd + "Event_Poi = '" + gsonInput.toJson(dayEvent) + "'," ; 
    		if ( httpBody.getNotice() != null )
    			sqlCmd = sqlCmd + "Notice = '" + httpBody.getNotice() + "'," ; 
    		if ( httpBody.getRemind() != null ) 
    			sqlCmd = sqlCmd + "Remind = '" + httpBody.getRemind() + "'," ;
    		
    		sqlCmd = sqlCmd.substring(0,sqlCmd.length()-1) ; // 移除最後一個字元 ','    			
    		sqlCmd = sqlCmd + " WHERE Event_Id = '" + eventId + "' and User_Id = '" + UserTokenService.getUserId(userToken) + "'";
    		stat.executeUpdate(sqlCmd) ;
    		
       		sqlCmd = "select * from calendar_events where Event_Id= '" + eventId + "' and User_Id = '" + UserTokenService.getUserId(userToken) + "'"; // 連結Object
       		rs = stat.executeQuery(sqlCmd) ;       		
       		rs.next() ;       		
       		
       		json = gson.toJson( new CalendarEvent( rs.getString("Event_Id"), rs.getString("Event_Type"), rs.getString("User_Id"),
	   				rs.getString("Event_Title"), rs.getString("Start_Time"), rs.getString("End_Time"),
	   				dayEvent, rs.getString("Notice"), rs.getInt("Remind") ) ) ;
       		
    		
   		} // end else
   	
    	
    	return json ;
    } // modifyEvent()
    
    /**
     * Calendar deleteEvent
     * 
     * http://localhost:8080/SmartTourismApi/api/v1/calendar/{eventId}
     */
    @ApiOperation(value = "刪除一行事曆中的事件", response = String.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Attraction not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
    @DELETE
    @Path("/{eventId}")
    public String deleteEvent( @ApiParam(value = "要修改的事件id", required = true) @PathParam("eventId") String eventId,
    		@ApiParam(value = "[本平台使用者token(登入時取得)] 或 [第三方APP userKey]", required = true) @HeaderParam("userToken") String userToken,
    		@ApiParam(value = "[第三方APP apiKey] (暫為任意字串)", required = true) @HeaderParam("apiKey") String apiKey ) throws Exception {
    	
    	logger.info("deleteEvent :" + eventId );
    	Gson gsonOutput = new Gson() ;
    	String json = null ;
    	
    	int affectRows = 99 ;
		
   		Class.forName("com.mysql.jdbc.Driver") ;	//註冊driver
   		Connection con = DbUtil.getConnection() ;
   		String sqlCmd = "DELETE FROM calendar_events WHERE Event_Id = '" + eventId + "' and User_Id = '" + UserTokenService.getUserId(userToken) + "'" ;
   		
   		stat = con.createStatement() ; // 執行,傳入之sql為完整字串
   		affectRows = stat.executeUpdate(sqlCmd) ;
   		if ( affectRows == 0 )
   			throw new Exception("delete nothing, please check the eventId") ;
   		else
   			json = gsonOutput.toJson(new ContentModel("delete successfully") ) ;    
   		
   		
   		return json ;
    	
    } // modifyEvent
    
    /**
     * get user's calendar event list
     * 
     * http://localhost:8080/SmartTourismApi/api/v1/calendar/getMyCalendar
     */
    @ApiOperation(value = "取得使用者的行事曆事件列表", response = CalendarEventOutput.class, responseContainer = "List")
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Attraction not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
    @GET
    @Path("/getMyCalendar")
    public List<CalendarEventOutput> getMyCalendar( @ApiParam( value = "取得Poi object的語言", allowableValues = "zh_tw,en_us,ja_jp") @HeaderParam("language") String language, 
    		@ApiParam(value = "[本平台使用者token(登入時取得)] 或 [第三方APP userKey]", required = true) @HeaderParam("userToken") String userToken,
    		@ApiParam(value = "[第三方APP apiKey] (暫為任意字串)", required = true) @HeaderParam("apiKey") String apiKey ) throws java.lang.Exception {
    	
    	logger.info( "getMyCalendar :" + userToken ) ; // output json object
    	
    	ArrayList<CalendarEventOutput> eventlist = new ArrayList<CalendarEventOutput>() ;

   		Class.forName("com.mysql.jdbc.Driver") ;	//註冊driver
   		Connection con = DbUtil.getConnection() ;
   		String sqlCmd = "SELECT * FROM calendar_events WHERE User_Id = '" + UserTokenService.getUserId(userToken) + "';";

   		stat = con.createStatement() ; // 執行,傳入之sql為完整字串
   		rs = stat.executeQuery(sqlCmd) ;
    		
   		if ( !rs.next() ) {	//判斷有無回傳結果
   			throw new SmartTourismException("this user have no calendar event!") ;
   		} else {
   			
    		do {    			 		        		   
    			Gson gsonInput = new Gson();
       			Type PoiEventList = new TypeToken<List<PoiEventIOModel>>() {
					private static final long serialVersionUID = 1L;
    			}.getType();
       			
    			List<PoiEventIOModel> dayEvent = gsonInput.fromJson(rs.getString("Event_Poi"), PoiEventList) ;
    			
    			CalendarEventOutput tempCalendarEvent = new CalendarEventOutput( rs.getString("Event_Id"), rs.getString("Event_Type"),
        				rs.getString("Event_Title"), rs.getString("Start_Time"), rs.getString("End_Time"),
        				dayEvent, rs.getString("Notice"), rs.getInt("Remind") ) ;    						
    			
    			eventlist.add(tempCalendarEvent);    			
    		} while ( rs.next() ); // end while
    		
   		} // end else

   		return eventlist ;
    } // getMyCalendar()
/*
	private List getPoiListWithDetail(String language, List<Poi> poiList) throws Exception {
		
		if(poiList == null)
			return Collections.EMPTY_LIST;
		
		for(Poi temp : poiList){
			
			temp.getDetail(); // {detail}
			
			IBaseType detail = null;
			switch(temp.getType()){
			case "Attraction":    					
				detail = attractionController.GETAttractionByID(temp.getId(), language) ;
				break;
			case "FoodAndDrink":
				detail = foodAndDrinkController.GETFoodAndDrinkByID(temp.getId(), language) ;
				break;
			case "Shopping": 				
				detail = shoppingController.GETShoppingByID(temp.getId(), language) ;
				break ;
			case "Tour":
				detail = tourController.gettourbyid(temp.getId(), language) ;
				break;
			case "Activity":
				detail = activityController.GETActivityByID(temp.getId(), language) ;
				break ;
			default:	
				logger.info(temp.getId());
				
			} // switch()
			
			temp.setDetail(detail);						
		} // end for
		
		return poiList;
	} // getPoiListWithDetail()
*/    
    /**
     * get event location from POI
     * 
     * http://localhost:8080/SmartTourismApi/api/v1/calendar/getEventLocation
     */
    @ApiOperation(value = "取得一事件內的所有活動地點", response = String.class, responseContainer = "Array")
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Attraction not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
    @GET
    @Path("/getEventLocation")
    public String getEventLocation ( @ApiParam(value = "要查詢的事件id", required = true) @HeaderParam("eventId") String eventId,
    		@ApiParam(value = "[本平台使用者token(登入時取得)] 或 [第三方APP userKey]", required = true) @HeaderParam("userToken") String userToken,
    		@ApiParam(value = "[第三方APP apiKey] (暫為任意字串)", required = true) @HeaderParam("apiKey") String apiKey ) throws Exception {
    	
    	logger.info("getEventLocation :" + eventId);
    	Gson gsonInput = new Gson() ;
    	Gson gsonOutput = new Gson() ;
    	String json = null ;
    	
    	ArrayList<String> poiLocation = new ArrayList<String>() ;
    	
    	Class.forName("com.mysql.jdbc.Driver") ;	//註冊driver
    	Connection con = DbUtil.getConnection() ;
    	String sqlCmd = "SELECT Event_Poi FROM calendar_events WHERE Event_Id = '" + eventId + "';";
    	stat = con.createStatement() ; // 執行,傳入之sql為完整字串
    	rs = stat.executeQuery(sqlCmd) ;
    	
    	if ( !rs.next() ) {	//判斷有無回傳結果
   			throw new SmartTourismException("Cant find this event!") ;
   		} else {
   			
   			Type PoiEventList = new TypeToken<List<PoiEvent>>() {
				private static final long serialVersionUID = 1L;
			}.getType();
						
   			List<PoiEvent> dayEvent = gsonInput.fromJson(rs.getString("Event_Poi"), PoiEventList) ;
   			
   			for ( int x = 0 ; x < dayEvent.size() ; x++ ) {
   				logger.info("dayEvent size:" + dayEvent.size());
	   			if ( dayEvent.get(x).getMorning() != null ) {
		   			for ( int i = 0 ; i < dayEvent.get(x).getMorning().size() ; i++ ) {
		   				if ( dayEvent.get(x).getMorning().get(i).getType() == "0" ) {
			   				sqlCmd = "SELECT * FROM place_part_zh_tw WHERE Place_Id = '" + dayEvent.get(x).getMorning().get(i).getId() + "' ;" ;  
			        		stat = con.createStatement() ; // 執行,傳入之sql為完整字串
			        		ResultSet poiResultSet = stat.executeQuery(sqlCmd) ;
			        		poiResultSet.next() ; // default 指標是指向第一筆之前的 null 位址
			        		
			        		poiLocation.add(poiResultSet.getString("Name")) ;
		   				} else {
		   					poiLocation.add(dayEvent.get(x).getMorning().get(i).getId()) ;
		   				} // end else					        		
		   			} // end for
	   			} // end if 
	   			
	   			if ( dayEvent.get(x).getAfternoon() != null ) {
		   			for ( int i = 0 ; i < dayEvent.get(x).getAfternoon().size() ; i++ ) {
		   				if ( dayEvent.get(x).getAfternoon().get(i).getType() == "0" ) {
			   				sqlCmd = "SELECT * FROM place_part_zh_tw WHERE Place_Id = '" + dayEvent.get(x).getAfternoon().get(i).getId() + "' ;" ;  
			        		stat = con.createStatement() ; // 執行,傳入之sql為完整字串
			        		ResultSet poiResultSet = stat.executeQuery(sqlCmd) ;
			        		poiResultSet.next() ; // default 指標是指向第一筆之前的 null 位址
			        		
			        		poiLocation.add(poiResultSet.getString("Name")) ;
		   				} else {
		   					poiLocation.add(dayEvent.get(x).getAfternoon().get(i).getId()) ;
		   				} // end else					        		
		   			} // end for
	   			} // end if
	   			
	   			if ( dayEvent.get(x).getNight() != null ) {
		   			for ( int i = 0 ; i < dayEvent.get(x).getNight().size() ; i++ ) {
		   				if ( dayEvent.get(x).getNight().get(i).getType() == "0" ) {
			   				sqlCmd = "SELECT * FROM place_part_zh_tw WHERE Place_Id = '" + dayEvent.get(x).getNight().get(i).getId() + "' ;" ;  
			        		stat = con.createStatement() ; // 執行,傳入之sql為完整字串
			        		ResultSet poiResultSet = stat.executeQuery(sqlCmd) ;
			        		poiResultSet.next() ; // default 指標是指向第一筆之前的 null 位址
			        		
			        		poiLocation.add(poiResultSet.getString("Name")) ;
		   				} else {
		   					poiLocation.add(dayEvent.get(x).getNight().get(i).getId()) ;
		   				} // end else					        		
		   			} // end for
	   			} // end if
	   			
	   			if ( dayEvent.get(x).getAccommodation() != null ) {
		   			for ( int i = 0 ; i < dayEvent.get(x).getAccommodation().size() ; i++ ) {
		   				if ( dayEvent.get(x).getAccommodation().get(i).getType() == "0" ) {
			   				sqlCmd = "SELECT * FROM place_part_zh_tw WHERE Place_Id = '" + dayEvent.get(x).getAccommodation().get(i).getId() + "' ;" ;  
			        		stat = con.createStatement() ; // 執行,傳入之sql為完整字串
			        		ResultSet poiResultSet = stat.executeQuery(sqlCmd) ;
			        		poiResultSet.next() ; // default 指標是指向第一筆之前的 null 位址
			        		
			        		poiLocation.add(poiResultSet.getString("Name")) ;
		   				} else {
		   					poiLocation.add(dayEvent.get(x).getAccommodation().get(i).getId()) ;
		   				} // end else					        		
		   			} // end for
	   			} // end if
   			} // end for
   			/*
        	StringTokenIterator tokens = new StringTokenIterator(rs.getString("Event_Poi"), ",");
        	while( tokens.hasNext() ) {
        		sqlCmd = "SELECT * FROM place_part_zh_tw WHERE Place_Id = '" + tokens.next() + "' ;" ;  
        		stat = con.createStatement() ; // 執行,傳入之sql為完整字串
        		ResultSet poiResultSet = stat.executeQuery(sqlCmd) ;
        		poiResultSet.next() ; // default 指標是指向第一筆之前的 null 位址 
        		
        		poiLocation.add(poiResultSet.getString("Name")) ;
        	} // end while
        	*/
		}
    	 
    	json = gsonOutput.toJson(poiLocation) ;    	
    	return json ;
    	
    } // getEventLocation()
    
    /**
     * get event location from POI
     * 
     * http://localhost:8080/SmartTourismApi/api/v1/calendar/isPlaceInCalendar
     */
    @ApiOperation(value = "判斷此使用者行事曆中事件是否含指定Poi", response = String.class, responseContainer="Array")
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Attraction not found"),
			  @ApiResponse(code = 400, message = "Invalid Language String") 
			})
    @GET
    @Path("/isPlaceInCalendar")
    public String isPlaceInCalendar( @ApiParam(value = "Poi Id", required = true) @HeaderParam("PoiId") String poiId, 
    		@ApiParam(value = "[本平台使用者token(登入時取得)] 或 [第三方APP userKey]", required = true) @HeaderParam("userToken") String userToken,
    		@ApiParam(value = "[第三方APP apiKey] (暫為任意字串)", required = true) @HeaderParam("apiKey") String apiKey ) throws Exception {
    	
    	logger.info( "isPlaceInCalendar :" + poiId);

    	boolean result = true ;
    	
    	Class.forName("com.mysql.jdbc.Driver") ;	//註冊driver
    	Connection con = DbUtil.getConnection() ;
    	String sqlCmd = "SELECT * FROM calendar_events WHERE Event_Poi LIKE '%" + poiId + "%' AND User_Id = '" + UserTokenService.getUserId(userToken) + "' ;" ;    	
    	stat = con.createStatement() ; // 執行,傳入之sql為完整字串
    	rs = stat.executeQuery(sqlCmd) ;

    	if ( !rs.next() )	//判斷有無回傳結果
    		result = false ; 
    	else 
    		result = true ;    		
    
    	Gson gsonOutput = new Gson() ;    	
    	String json = gsonOutput.toJson(new BooleanModel(result)) ;
    	
    	return json;
    } // isPlaceInCalendar()

    
}