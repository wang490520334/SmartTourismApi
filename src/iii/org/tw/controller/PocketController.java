package iii.org.tw.controller;
 
import iii.org.tw.dao.PocketDao;
import iii.org.tw.entity.Pocket;
import iii.org.tw.model.BooleanModel;
import iii.org.tw.model.MyPocket;
import iii.org.tw.model.MyPocketIn;
import iii.org.tw.service.UserTokenService;
import iii.org.tw.util.SmartTourismErrorMsg;
import iii.org.tw.util.SmartTourismException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
 
@Path("/pocket")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/pocket", description = "Smart Tourism Pocket Api")
public class PocketController {
	
	
	Logger logger = LoggerFactory.getLogger(PocketController.class);
	
    /**
     * 根據 type 取得使用者的Pockets
     * 
     * http://localhost:8081/SmartTourismApi/api/v1/pocket
     */
/*	
    @GET
    @ApiOperation(value = "根據 type 取得使用者的Pockets",notes="根據 type 取得使用者的Pockets" , response = MyPocket.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String getPocketsByType(@ApiParam(required=true,value="userToken")@HeaderParam("userToken") String userToken, @ApiParam(required=true,value="Attraction, FoodAndDrink, Shopping.....等",allowableValues = "Attraction,FoodAndDrink,Shopping")@QueryParam("type") String type) throws SmartTourismException {
    	
    	logger.info("-----getPocketsByType----");
    	logger.info(userToken);
    	logger.info(type);

    	PocketDao pocketDao = PocketDao.getInstance();
    	List<Map<String, Object>> list = pocketDao.getPocketsByType(UserTokenService.getUserId(userToken), iii.org.tw.definition.Type.getTypeIntByString(type));
    	
    	List<MyPocket> pockets = new ArrayList<MyPocket>();
    	for (int i=0; i<list.size(); i++) {
    		Map<String, Object> map = list.get(i);
    		MyPocket pocket = new MyPocket();
    		pocket.setId(map.get("UUID").toString());
    		pocket.setUserKey(map.get("USER_ID").toString());	
    		pocket.setPocketType(iii.org.tw.definition.Type.getTypeStringByInt(Integer.valueOf(map.get("POCKET_TYPE").toString())));
    		pocket.setRelatedId(map.get("RELATED_ID").toString());
    		pockets.add(pocket);
    	}
    	Gson gson = new Gson();
    	String json = gson.toJson(pockets);
    	return json;

    }   
*/
    @GET
    @ApiOperation(value = "取得使用者的Pockets",notes="取得使用者的Pockets" , response = MyPocket.class, responseContainer = "List")
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String getPocketsByType(@ApiParam(required=true,value="userToken")@HeaderParam("userToken") String userToken) throws SmartTourismException {
    	
    	logger.info("-----getPocketsByType----");
    	logger.info(userToken);

    	PocketDao pocketDao = PocketDao.getInstance();
    	List<Map<String, Object>> list = pocketDao.getPockets(UserTokenService.getUserId(userToken));
    	
    	List<MyPocket> pockets = new ArrayList<MyPocket>();
    	for (int i=0; i<list.size(); i++) {
    		Map<String, Object> map = list.get(i);
    		MyPocket pocket = new MyPocket();
    		pocket.setId(map.get("UUID").toString());
    		pocket.setUserKey(map.get("USER_ID").toString());	
    		pocket.setPocketType(iii.org.tw.definition.Type.getTypeStringByInt(Integer.valueOf(map.get("POCKET_TYPE").toString())));
    		pocket.setRelatedId(map.get("RELATED_ID").toString());
    		pockets.add(pocket);
    	}
    	Gson gson = new Gson();
    	String json = gson.toJson(pockets);
    	return json;

    }  
	
	
	
	
    /**
     * 從Pocket內取得單一Item
     */
    @GET
    @Path("/{id}")
    @ApiOperation(value = "從Pocket內取得單一Item",notes="從Pocket內取得單一Item" , response = MyPocket.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public MyPocket getPocket(@ApiParam(required=true,value="要查詢的id")@PathParam("id") String id) throws SmartTourismException {
    	
	    logger.info("-----getPocket-----");
	    logger.info(id);


    	PocketDao pocketDao = PocketDao.getInstance();
    	List<Map<String, Object>> list = pocketDao.getPocketById(id);
    		
    	MyPocket pocket = null;
    	if (list.size() > 0) {
    		Map<String, Object> map = list.get(0);
    		pocket = new MyPocket();
    		pocket.setId(map.get("UUID").toString());
    		pocket.setUserKey(map.get("USER_ID").toString());
    		pocket.setPocketType(iii.org.tw.definition.Type.getTypeStringByInt(Integer.valueOf(map.get("POCKET_TYPE").toString())));
    		pocket.setRelatedId(map.get("RELATED_ID").toString());
    	} else {
    		throw new SmartTourismException("資料不存在");
    	}

    	return pocket;

    }    	
	
    /**
     * 加到我的最愛 Pocket
     * @throws Exception 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "加到我的最愛 Pocket",notes="加到我的最愛 Pocket" , response = MyPocket.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public MyPocket savePocket(@ApiParam(required=true,value="MyPocket")MyPocketIn body, @ApiParam(required=true,value="userToken")@HeaderParam("userToken") String userToken) throws Exception {
    	
	    logger.info("-----savePocket-----");
	    logger.info(body.toString());
    	logger.info(userToken);
    	

    	PocketDao pocketDao = PocketDao.getInstance();
    	List<Map<String, Object>> list = pocketDao.getPocketByRelatedId(UserTokenService.getUserId(userToken), iii.org.tw.definition.Type.getTypeIntByString(body.getPocketType()), body.getRelatedId());   	
    	if (list.size() > 0) {
    		throw new SmartTourismException("已存在");
    	}
    	
    	if (StringUtils.isEmpty(body.getRelatedId())) {
    		throw new SmartTourismException("relatedId不得為空");
    	}

    	Pocket pocket = new Pocket();
    	pocket.setUserId(UserTokenService.getUserId(userToken));    
    	pocket.setPocketType(iii.org.tw.definition.Type.getTypeIntByString(body.getPocketType()));
    	pocket.setRelateId(body.getRelatedId());
    	String uuid = pocketDao.savePocket(pocket);
    	
    	MyPocket myPocket = getPocket(uuid);

        return myPocket;
    } 
    

    
    /**
     * 要從Pocket刪除 id
     */
    @DELETE
    @Path("/{id}")
    @ApiOperation(value = "要從Pocket刪除 id",notes="要從Pocket刪除 id" , response = BooleanModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String deletePocket(@ApiParam(required=true,value="加從Pocket刪除的id")@PathParam("id") String id) throws SmartTourismException {
    	
	    logger.info("-----deletePocket----");
	    logger.info(id);
    	
    	
    	PocketDao pocketDao = PocketDao.getInstance();
    	
    	int result = pocketDao.deletePocketById(id);
    	if (result == 0) {
    		throw new SmartTourismException("資料不存在或已經刪除");
    	}


	    Gson gson = new Gson();
    	String json = gson.toJson(new BooleanModel(true));
    	
        return json;
    } 
	
 

    

    
    

}