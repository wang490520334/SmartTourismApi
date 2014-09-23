package iii.org.tw.controller;
 
import iii.org.tw.dao.UserTokenDao;
import iii.org.tw.entity.UserToken;
import iii.org.tw.model.UserInModel;
import iii.org.tw.model.UserModel;
import iii.org.tw.service.LdapService;
import iii.org.tw.util.SmartTourismErrorMsg;
import iii.org.tw.util.SmartTourismException;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
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
 
@Path("/user")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/user", description = "Smart Tourism User Api")
public class UserController {
	
	
	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	
	
    /**
     * http://localhost:8081/SmartTourismApi/api/v1/userToken
     */
    @GET
    @ApiOperation(value = "取得使用者資訊",notes="取得使用者資訊" , response = UserModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String getUser(@ApiParam(required=true,value="userToken")@HeaderParam("userToken") String userToken) throws SmartTourismException {
    	
	    logger.info("-----getUser-----");
	    logger.info(userToken);
	    
	    UserTokenDao userTokenDao = UserTokenDao.getInstance();
	    UserToken userTokenEntity = userTokenDao.findOneByTokenId(userToken);    
	    if (userTokenEntity == null) {
	    	throw new SmartTourismException("usertoken錯誤");
	    }
	    
	    //UserModel user = LdapService.getUserData(userTokenEntity.getUserId());
	    UserModel user = LdapService.getUserDataByUserId(userTokenEntity.getUserId());

    	Gson gson = new Gson();
    	String json = gson.toJson(user);

        return json;

    }    
        
    
    /**
     * 
     * http://localhost:8081/SmartTourismApi/api/v1/user
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "更新使用者資訊",notes="更新使用者資訊" , response = UserModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String updateUser(@ApiParam(required=true,value="userToken")@HeaderParam("userToken") String userToken, @ApiParam(required=true,value="User Model")UserInModel body) throws SmartTourismException {
    	
	    logger.info("----updateUser----");
	    logger.info(userToken);
	    logger.info(body.toString());
	    
	    
	    UserTokenDao userTokenDao = UserTokenDao.getInstance();
	    UserToken userTokenEntity = userTokenDao.findOneByTokenId(userToken);    
	    if (userTokenEntity == null) {
	    	throw new SmartTourismException("userToken錯誤");
	    }


    	UserModel user = new UserModel();
   	
    	user.setName(body.getName());
    	user.setMail(body.getMail());
    	user.setTelephone(body.getTelephone());
    	user.setMobile(body.getMobile());
    	
    	
    	user.setGender(body.getGender());
		user.setAge(body.getAge());
		user.setNationality(body.getNationality());
		user.setEducation(body.getEducation());
		user.setIndustry(body.getIndustry());
		user.setMarriage(body.getMarriage());
		user.setMonthlyIncome(body.getMonthlyIncome());
		user.setTimesToTaiwan(body.getTimesToTaiwan());
	
    		
    	LdapService.updateUserDataByUserId(userTokenEntity.getUserId(), user);

    	Gson gson = new Gson();
    	String json = gson.toJson(user);
    	
        return json;
    } 
    
    
    /**
     * 
     * http://localhost:8081/SmartTourismApi/api/v1/user
     */
    @POST
    @ApiOperation(value = "更新使用者照片欄位",notes="更新使用者照片欄位" , response = UserModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String updateUserImg(@ApiParam(required=true,value="userToken")@HeaderParam("userToken") String userToken, @ApiParam(required=true,value="fileToken")@FormParam("url") String url) throws SmartTourismException {
    	
	    logger.info("----updateUser----");
	    logger.info(userToken);
	    
	    
	    UserTokenDao userTokenDao = UserTokenDao.getInstance();
	    UserToken userTokenEntity = userTokenDao.findOneByTokenId(userToken);    
	    if (userTokenEntity == null) {
	    	throw new SmartTourismException("userToken錯誤");
	    }

    	LdapService.updateUserImgByUserId(userTokenEntity.getUserId(), url);
    	UserModel user = LdapService.getUserDataByUserId(userTokenEntity.getUserId());

    	Gson gson = new Gson();
    	String json = gson.toJson(user);
    	
        return json;
    } 
    

 


    
    

    

}