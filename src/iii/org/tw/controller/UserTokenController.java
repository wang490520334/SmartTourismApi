package iii.org.tw.controller;
 
import iii.org.tw.dao.UserTokenDao;
import iii.org.tw.entity.UserToken;
import iii.org.tw.model.BooleanModel;
import iii.org.tw.model.ContentModel;
import iii.org.tw.model.TokenModel;
import iii.org.tw.service.LdapService;
import iii.org.tw.service.MailService;
import iii.org.tw.util.DbUtil;
import iii.org.tw.util.SmartTourismErrorMsg;
import iii.org.tw.util.SmartTourismException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.SearchResult;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
 
@Path("/authentication")
@Produces(MediaType.APPLICATION_JSON)
@Api(value = "/authentication", description = "Smart Tourism Authentication Api")
public class UserTokenController {
	
	
	Logger logger = LoggerFactory.getLogger(UserTokenController.class);
	
	
	
    /**
     * 使用者註冊 並寄送認證email
     * 
     * http://localhost:8081/SmartTourismApi/api/v1/authentication
     */
    @POST
    @ApiOperation(value = "使用者註冊 並寄送認證email",notes="使用者註冊 並寄送認證email" , response = BooleanModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String signup(@ApiParam(required=true,value="mail")@FormParam("mail") String mail, @ApiParam(required=true,value="pass")@FormParam("pass") String pass, @Context HttpServletRequest httpRequest) throws SmartTourismException {
    	
	    logger.info("-----signup-----");
	    logger.info(httpRequest.getServerName());
	    logger.info(String.valueOf(httpRequest.getServerPort()));
	    
	    List<String> list = new ArrayList<String>();
	    
	    String secureToken = UUID.randomUUID().toString();
    	
    	
    	String result = LdapService.createLdapUser(mail, pass, secureToken);
    	
    	if (!"SUCCESS".equals(result)) {
    		logger.error(result);
    		throw new SmartTourismException("帳號創建失敗(請確定帳號是否已註冊)，請洽系統管理員' !!");
    	}
    	
    	MailService.sendMail("驗證確任信", "http://140.92.88.105:8080/SmartTourismApi/api/v1/authentication/activate?mail="+mail+"&secureToken="+secureToken, "vzTaiwan@iii.org.tw", mail);

    	Gson gson = new Gson();
    	String json = gson.toJson(new BooleanModel(true));

    	
        return json;
    } 
    
    
    /**
     * 使用者登入
     * 
     * http://localhost:8081/SmartTourismApi/api/v1/userToken
     */
    @PUT
    @ApiOperation(value = "使用者登入",notes="使用者登入" , response = TokenModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String login(@ApiParam(required=true,value="mail")@FormParam("mail") String mail, @ApiParam(required=true,value="pass")@FormParam("pass") String pass) throws SmartTourismException {
    	
	    logger.info("----login----");
	    String result = LdapService.authenticate(mail, pass);
	    
	    if (!"SUCCESS".equals(result)) {
	    	throw new SmartTourismException("帳號或密碼錯誤!!");
	    }
	    
	    
	    
	    SearchResult searchResult = LdapService.findOneByEmail(mail);
	    if (searchResult == null) {
	    	throw new SmartTourismException("帳號不存在");
	    }    	    
	    boolean activate = false;
	    Attributes attrs = searchResult.getAttributes();
    	try {
    		activate = ((String)attrs.get(LdapService.activate).get()).equals("true"); 
		} catch (NamingException e) {
			e.printStackTrace();
			throw new SmartTourismException(e.getMessage());
		}    
    	if (!activate) {
    		throw new SmartTourismException("帳號尚未啟用!!");
    	}
   
	    
	    String token = UUID.randomUUID().toString();
	    
	    Connection conn = null;
	    PreparedStatement preparedStatement = null;
	    try {
	    	
	    	
			conn = DbUtil.getConnection();
			preparedStatement = conn.prepareStatement("insert into user_token (UUID,USER_TOKEN,USER_ID,LOGIN_TIME) values (?,?,?,?)");
			preparedStatement.setString(1, UUID.randomUUID().toString());
			preparedStatement.setString(2, token);
			preparedStatement.setString(3, (String)attrs.get(LdapService.secureToken).get());
			preparedStatement.setString(4, String.valueOf(System.currentTimeMillis()));

			boolean success = preparedStatement.execute();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
			throw new SmartTourismException("資料庫錯誤，請洽系統管理員");
			
		} finally {		
			try {
				preparedStatement.close();
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

    	Gson gson = new Gson();
    	String json = gson.toJson(new TokenModel(token));
    	
        return json;
    } 
    
    /**
     * 使用者登出
     * 
     * http://localhost:8081/SmartTourismApi/api/v1/userToken
     */
    @DELETE
    @Path("/{userToken}")
    @ApiOperation(value = "使用者登出",notes="使用者登出" , response = BooleanModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String logout(@ApiParam(required=true,value="userToken")@PathParam("userToken") String userToken) throws SmartTourismException {
    	
	    logger.info("-----logout----");

	    UserTokenDao userTokenDao = UserTokenDao.getInstance();
	    UserToken userTokenEntity = userTokenDao.findOneByTokenId(userToken);
	    
	    if (userTokenEntity == null) {
	    	throw new SmartTourismException("使用者未登入");
	    }
	    
	    //userTokenDao.deleteByTokenId(userToken);
	    userTokenDao.deleteByUserId(userTokenEntity.getUserId());

	    Gson gson = new Gson();
    	String json = gson.toJson(new BooleanModel(true));
    	
        return json;
    } 
	
    
    @GET
    @Path("/{userToken}")
    @ApiOperation(value = "查詢使用者是否已登入",notes="查詢使用者是否已登入" , response = BooleanModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String isLogin(@ApiParam(required=true,value="userToken")@PathParam("userToken") String userToken) throws SmartTourismException {
    	
    	logger.info("-----isLogin----");

	    UserTokenDao userTokenDao = UserTokenDao.getInstance();
	    UserToken userTokenEntity = userTokenDao.findOneByTokenId(userToken);
	    
	    if (userTokenEntity == null) {
	    	Gson gson = new Gson();
	    	String json = gson.toJson(new BooleanModel(false));
	    	return json;
	    } else {
	    	Gson gson = new Gson();
	    	String json = gson.toJson(new BooleanModel(true));
	    	return json;
	    }

    } 
    
    
    @POST
    @Path("/changePass")
    @ApiOperation(value = "更改密碼",notes="更改密碼" , response = BooleanModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String changePass(@ApiParam(required=true,value="userToken")@HeaderParam("userToken") String userToken, @ApiParam(required=true,value="舊密碼")@FormParam("oldPass") String oldPass, @ApiParam(required=true,value="新密碼")@FormParam("newPass") String newPass) throws SmartTourismException {
    	
	    logger.info("-----changePass-----");
	    
	    UserTokenDao userTokenDao = UserTokenDao.getInstance();
	    UserToken userTokenEntity = userTokenDao.findOneByTokenId(userToken);    
	    if (userTokenEntity == null) {
	    	throw new SmartTourismException("usertoken錯誤");
	    }
	    

	    String mail = null;
	    SearchResult searchResult = LdapService.findOneByUserId(userTokenEntity.getUserId());
	    Attributes attrs = searchResult.getAttributes();
    	try {
    		mail = (String)attrs.get("cn").get();
		} catch (NamingException e) {
			e.printStackTrace();
			throw new SmartTourismException(e.getMessage());
		}    

	    
	    
	    String result = LdapService.authenticate(mail, oldPass);    
	    if (!"SUCCESS".equals(result)) {
	    	throw new SmartTourismException("密碼錯誤!!");
	    }

	    Map<String, String> attributes = new HashMap<String, String>();
	    attributes.put("userPassword", LdapService.shaByte(newPass));    
	    LdapService.modifyAttribute(mail, attributes);
	    
  
    	Gson gson = new Gson();
    	String json = gson.toJson(new BooleanModel(true));
    	return json;

    } 
    

    /**
     * http://localhost:8081/SmartTourismApi/api/v1/userToken
     */
    @GET
    @Path("/forgetPass")
    @ApiOperation(value = "忘記密碼 並 寄送通知",notes="忘記密碼 並 寄送通知" , response = ContentModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String forgetPass(@ApiParam(required=true,value="mail")@QueryParam("mail") String mail) throws SmartTourismException {
    	
	    logger.info("-----forgetPass-----");
	    
	    String changePassSecurityCode = randomString(5);
	    Map<String, String> attributes = new HashMap<String, String>();
	    attributes.put(LdapService.changeToken, changePassSecurityCode + "," + String.valueOf(Calendar.getInstance().getTimeInMillis()));    
	    LdapService.modifyAttribute(mail, attributes);
	    
	    
	    MailService.sendMail("忘記密碼", "驗證碼 : " + changePassSecurityCode , "vzTaiwan@iii.org.tw", mail);

    	Gson gson = new Gson();
    	String json = gson.toJson(new ContentModel("信件已寄發"));

        return json;

    }
    
    @POST
    @Path("/forgetPassChange")
    @ApiOperation(value = "更改密碼(給忘記密碼用)",notes="更改密碼(給忘記密碼用)" , response = BooleanModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String forgetPassChange(@ApiParam(required=true,value="mail")@FormParam("email") String email, @ApiParam(required=true,value="securityCode")@FormParam("securityCode") String securityCode, @ApiParam(required=true,value="新密碼")@FormParam("newPass") String newPass) throws SmartTourismException {
    	
	    logger.info("-----forgetPassChange-----");
	    
	    
	    SearchResult searchResult = LdapService.findOneByEmail(email);
	    if (searchResult == null) {
	    	throw new SmartTourismException("帳號不存在");
	    } else {
	    	Attributes attrs = searchResult.getAttributes();
	    	try {
				if (!( ((String)attrs.get(LdapService.changeToken).get()).split(",")[0] ).equals(securityCode)) {
		    		throw new SmartTourismException("安全碼有問題");
		    	}
				
				long securityTime = Long.valueOf( ((String)attrs.get(LdapService.changeToken).get()).split(",")[1] );
		    	long nowTime = System.currentTimeMillis();
		    	if ( (nowTime - securityTime) > (1000 * 60 * 10)  ) {
		    		throw new SmartTourismException("安全碼已過期");
		    	}
		    	
			} catch (NamingException e) {
				e.printStackTrace();
			}
	    	
	    	
	    	
	    }

	    Map<String, String> attributes = new HashMap<String, String>();
	    attributes.put("userPassword", LdapService.shaByte(newPass));    
	    LdapService.modifyAttribute(email, attributes);
	    
  
    	Gson gson = new Gson();
    	String json = gson.toJson(new BooleanModel(true));
    	return json;

    } 
    
    
    
    @GET
    @Path("/checkEmail")
    @ApiOperation(value = "檢查email是否可使用",notes="檢查email是否可使用" , response = BooleanModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String checkEmail(@ApiParam(required=true,value="mail")@QueryParam("mail") String mail) throws SmartTourismException {
    	
    	
    	
//	    	Class.forName("com.mysql.jdbc.Driver"); 
//	    	Connection con = DriverManager.getConnection("jdbc:mysql://140.92.2.220:3306/ST_DB", "root", "ari36993");

    	
    	
	    logger.info("-----checkEmail-----");
	    
	    
	    SearchResult searchResult = LdapService.findOneByEmail(mail);
	    if (searchResult == null) {
	    	Gson gson = new Gson();
	    	String json = gson.toJson(new BooleanModel(true));
	    	return json;
	    } else {
	    	Gson gson = new Gson();
	    	String json = gson.toJson(new BooleanModel(false));
	    	return json;
	    }
	    
    }   
    
    @GET
    @Path("/activate")
    @ApiOperation(value = "啟用帳號",notes="啟用帳號" , response = BooleanModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String activate(@ApiParam(required=true,value="mail")@QueryParam("mail") String mail, @ApiParam(required=true,value="安全碼")@QueryParam("secureToken") String secureToken) throws SmartTourismException {
    	
	    logger.info("-----activate-----");
	    
	    
	    SearchResult searchResult = LdapService.findOneByEmail(mail);
	    if (searchResult == null) {
	    	throw new SmartTourismException("帳號不存在");
	    } else {
	    	Attributes attrs = searchResult.getAttributes();
	    	try {
				if (!( (String)attrs.get(LdapService.secureToken).get() ).equals(secureToken)) {
		    		throw new SmartTourismException("安全碼有問題");
		    	}
			} catch (NamingException e) {
				e.printStackTrace();
				throw new SmartTourismException(e.getMessage());
			}
	    }

	    
	    Map<String, String> attributes = new HashMap<String, String>();
	    attributes.put(LdapService.activate, "true");    
	    LdapService.modifyAttribute(mail, attributes);

    	Gson gson = new Gson();
    	String json = gson.toJson(new BooleanModel(true));
    	
        return json;
    }   
    
    @POST
    @Path("/checkActivate")
    @ApiOperation(value = "檢查帳號是否啟用",notes="檢查帳號是否啟用" , response = BooleanModel.class)
    @ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 400, message = "Error Message", response = SmartTourismErrorMsg.class) 
			})
    public String checkActivate(@ApiParam(required=true,value="mail")@FormParam("mail") String mail) throws SmartTourismException {
    	
	    logger.info("-----checkActivate-----");
	    
    	
    	SearchResult searchResult = LdapService.findOneByEmail(mail);
	    if (searchResult == null) {
	    	throw new SmartTourismException("帳號不存在");
	    }    
	    
	    boolean result = false;
	    Attributes attrs = searchResult.getAttributes();
    	try {
    		result = ((String)attrs.get(LdapService.activate).get()).equals("true"); 
		} catch (NamingException e) {
			e.printStackTrace();
			throw new SmartTourismException(e.getMessage());
		}    

    	Gson gson = new Gson();
    	String json = gson.toJson(new BooleanModel(result));
    	
        return json;
    }   
     
    
    
    static final String range = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    static Random rnd = new Random();

    private String randomString(int len) {
       StringBuilder sb = new StringBuilder( len );
       for( int i = 0; i < len; i++ ) {
          sb.append( range.charAt( rnd.nextInt(range.length()) ) );
       }   
       return sb.toString();
    }
    

}