package iii.org.tw.controller;
 
import iii.org.tw.dao.CommentDao;
import iii.org.tw.entity.Comment;
import iii.org.tw.model.BooleanModel;
import iii.org.tw.model.CommentInModel;
import iii.org.tw.model.CommentModel;
import iii.org.tw.model.UserModel;
import iii.org.tw.service.LdapService;
import iii.org.tw.service.UserTokenService;
import iii.org.tw.util.SmartTourismErrorMsg;
import iii.org.tw.util.SmartTourismException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.DELETE;
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
 
@Path("/comment")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/comment", description = "Smart Comment Pocket Api")
public class CommentController {
	
	
	Logger logger = LoggerFactory.getLogger(CommentController.class);
	
    /**
     * 取得特定APP下，某種type下某個景點的評論
     * 
     * http://localhost:8081/SmartTourismApi/api/v1/comment
     */
    @GET
    @ApiOperation(value = "取得特定APP下，某種type下某個景點的評論",notes="取得特定APP下，某種type下某個景點的評論" , response = CommentModel.class, responseContainer = "List")
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String getCommentsByPlaceId(@ApiParam(required=true,value="Attraction, FoodAndDrink, Shopping.....等",allowableValues = "Attraction,FoodAndDrink,Shopping,Activity,Tour")@QueryParam("type") String type, @ApiParam(required=true,value="特定景點、美食或購物Id")@QueryParam("placeId") String placeId) throws SmartTourismException {
    	
    	logger.info("-----getCommentsByPlaceId----");
    	logger.info(type);
    	logger.info(placeId);
    	
    	CommentDao commentDao = CommentDao.getInstance();
    	List<Map<String, Object>> list = commentDao.getCommentsByPlaceId(iii.org.tw.definition.Type.getTypeIntByString(type), placeId);
    	
    	List<CommentModel> comments = new ArrayList<CommentModel>();
    	for (int i=0; i<list.size(); i++) {
    		Map<String, Object> map = list.get(i);
    			
    		CommentModel commentModel = new CommentModel();
    		commentModel.setCommentId(map.get("COMMENT_ID").toString());
    		commentModel.setPlaceId(map.get("PLACE_ID").toString());
    		commentModel.setType(iii.org.tw.definition.Type.getTypeStringByInt(Integer.valueOf(map.get("TYPE").toString())));
    		
    		try {
    			UserModel user = LdapService.getUserDataByUserId(map.get("USER_ID").toString());
    			commentModel.setName(user.getName());
    		} catch (SmartTourismException e) { // user資料遺失
    			commentModel.setName("guest");
    		}
    		    		
    		
    		commentModel.setComment(map.get("COMMENT").toString());
    		commentModel.setPicture(map.get("PICTURE") == null ? null : map.get("PICTURE").toString());
    		commentModel.setTime(String.valueOf(((Timestamp)map.get("TIME")).getTime()));
    		comments.add(commentModel);
		
    	}
    	Gson gson = new Gson();
    	String json = gson.toJson(comments);
    	return json;

    }   
    
    /**
     * 取得單一評論
     */
    @GET
    @Path("/{commentId}")
    @ApiOperation(value = "取得單一評論",notes="取得單一評論" , response = CommentModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public CommentModel getComment(@ApiParam(required=true,value="要查詢的commentId")@PathParam("commentId") String commentId) throws SmartTourismException {
    	
	    logger.info("-----getComment-----");
	    logger.info(commentId);

	    CommentDao commentDao = CommentDao.getInstance();
    	List<Map<String, Object>> list = commentDao.getCommentByCommentId(commentId);
    	
    	
    	CommentModel commentModel = null;
    	if (list.size() > 0) {
    		Map<String, Object> map = list.get(0);
    		commentModel = new CommentModel();
    		commentModel.setCommentId(map.get("COMMENT_ID").toString());
    		commentModel.setPlaceId(map.get("PLACE_ID").toString());
    		commentModel.setType(iii.org.tw.definition.Type.getTypeStringByInt(Integer.valueOf(map.get("TYPE").toString())));
    		UserModel user = LdapService.getUserDataByUserId(map.get("USER_ID").toString());
    		commentModel.setName(user.getName());
    		commentModel.setComment(map.get("COMMENT").toString());
    		commentModel.setPicture(map.get("PICTURE") == null ? null : map.get("PICTURE").toString());
    		commentModel.setTime(String.valueOf(((Timestamp)map.get("TIME")).getTime()));

    	} else {
    		throw new SmartTourismException("查無資料");
    	}

    	return commentModel;

    }    	
	
    /**
     * 加入comment
     */
    @POST
    @ApiOperation(value = "加入comment",notes="加入comment" , response = CommentModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String saveComment(@ApiParam(required=true,value="Comment")CommentInModel body, @ApiParam(required=true,value="userToken")@HeaderParam("userToken") String userToken) throws SmartTourismException {
    	
	    logger.info("-----saveComment-----");
	    logger.info(body.toString());
    	logger.info(userToken);
    	
    	CommentDao commentDao = CommentDao.getInstance();

    	Comment comment = new Comment();
    	comment.setUserId(UserTokenService.getUserId(userToken));
    	comment.setPlaceId(body.getPlaceId());
    	comment.setComment(body.getComment());
    	comment.setPicture(body.getPicture());
    	long time = System.currentTimeMillis();
    	java.sql.Timestamp timestamp = new java.sql.Timestamp(time);
    	comment.setTimestamp(timestamp);
    	comment.setType(iii.org.tw.definition.Type.getTypeIntByString(body.getType()));
    	String uuid = commentDao.saveComment(comment);
    	
    	
    	CommentModel commentModel = getComment(uuid);

    	Gson gson = new Gson();
    	String json = gson.toJson(commentModel);

    	
        return json;
    } 
    

    
    /**
     * 刪除特定comment
     */
    @DELETE
    @Path("/{commentId}")
    @ApiOperation(value = "刪除特定comment",notes="刪除特定comment" , response = BooleanModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String deleteComment(@ApiParam(required=true,value="要刪除的commentId")@PathParam("commentId") String commentId) throws SmartTourismException {
    	
	    logger.info("-----deleteComment----");
	    logger.info(commentId);
	    
	    CommentDao commentDao = CommentDao.getInstance();
	    int result = commentDao.deleteCommentByCommentId(commentId);
	    if (result == 0) {
    		throw new SmartTourismException("資料不存在或已刪除");
    	}

	    Gson gson = new Gson();
    	String json = gson.toJson(new BooleanModel(true));
    	
        return json;
    } 
	
 

    

    
    

}