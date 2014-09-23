package iii.org.tw.controller;
 
import iii.org.tw.dao.CheckinDao;
import iii.org.tw.dao.PocketDao;
import iii.org.tw.entity.Checkin;
import iii.org.tw.model.BooleanModel;
import iii.org.tw.model.MyCheckin;
import iii.org.tw.model.MyCheckinIn;
import iii.org.tw.model.MyPocket;
import iii.org.tw.service.UserTokenService;
import iii.org.tw.util.SmartTourismErrorMsg;
import iii.org.tw.util.SmartTourismException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
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
 
@Path("/checkin")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/checkin", description = "Smart Tourism Checkin Api")
public class CheckInController {
	
	
	Logger logger = LoggerFactory.getLogger(CheckInController.class);
	

    @GET
    @ApiOperation(value = "取得使用者的打卡資訊",notes="取得使用者的打卡資訊" , response = MyCheckin.class, responseContainer = "List")
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String getChickins(@ApiParam(required=true,value="userToken")@HeaderParam("userToken") String userToken) throws SmartTourismException {
    	
    	logger.info("-----getChickins----");
    	logger.info(userToken);

    	CheckinDao checkinDao = CheckinDao.getInstance();
    	List<Map<String, Object>> list = checkinDao.getCheckins(UserTokenService.getUserId(userToken));
    	
    	List<MyCheckin> checkins = new ArrayList<MyCheckin>();
    	for (int i=0; i<list.size(); i++) {
    		Map<String, Object> map = list.get(i);
    		MyCheckin checkin = new MyCheckin();
    		checkin.setId(map.get("UUID").toString());
    		checkin.setUserKey(map.get("USER_ID").toString());	
    		checkin.setPocketType(iii.org.tw.definition.Type.getTypeStringByInt(Integer.valueOf(map.get("POCKET_TYPE").toString())));
    		checkin.setRelatedId(map.get("RELATED_ID").toString());
    		checkins.add(checkin);
    	}
    	Gson gson = new Gson();
    	String json = gson.toJson(checkins);
    	return json;

    }  
	
	

	
    /**
     * 打卡
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "打卡",notes="打卡" , response = BooleanModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String saveCheckin(@ApiParam(required=true,value="MyCheckinIn")MyCheckinIn body, @ApiParam(required=true,value="userToken")@HeaderParam("userToken") String userToken) throws SmartTourismException {
    	
	    logger.info("-----saveCheckin-----");
	    logger.info(body.toString());
    	logger.info(userToken);
    	

    	CheckinDao checkinDao = CheckinDao.getInstance();
    	List<Map<String, Object>> list = checkinDao.getCheckinByRelatedId(UserTokenService.getUserId(userToken), iii.org.tw.definition.Type.getTypeIntByString(body.getPocketType()), body.getRelatedId());   	
    	if (list.size() > 0) {
    		throw new SmartTourismException("已存在");
    	}

    	Checkin checkin = new Checkin();
    	checkin.setUserId(UserTokenService.getUserId(userToken));    
    	checkin.setPocketType(iii.org.tw.definition.Type.getTypeIntByString(body.getPocketType()));
    	checkin.setRelateId(body.getRelatedId());
    	checkinDao.saveCheckin(checkin);

    	Gson gson = new Gson();
    	String json = gson.toJson(new BooleanModel(true));

    	
        return json;
    } 
    

    

    
    

}