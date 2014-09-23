package iii.org.tw.controller;

import iii.org.tw.entity.VoucherEvent;
import iii.org.tw.entity.VoucherIdEvent;
import iii.org.tw.model.VoucherInput;
import iii.org.tw.model.VoucherModify;
import iii.org.tw.service.UserTokenService;
import iii.org.tw.util.DbUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.Date;

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

@Path("/voucher")
@Api(value = "/voucher", description = "Operations about voucher")
@Produces(MediaType.APPLICATION_JSON)
public class VoucherController {

	Logger logger = LoggerFactory.getLogger(VoucherController.class);
	
	private Statement stmt = null;
	private ResultSet rs = null;
	private String SQL;
	private PreparedStatement pst = null;
	private String exportString = null;
	private Gson gson = new Gson();
	private String json = null;  
	
	
	 @ApiOperation(value = "查詢所有類型折價券", response = VoucherEvent.class, responseContainer = "Array")
	 @ApiResponses(value = {
				  @ApiResponse(code = 500, message = "Internal Error"),
				  @ApiResponse(code = 404, message = "Attraction not found")
				})
	 @GET
	 public String getVoucher(@ApiParam(allowableValues = "system,vendor",value = "Vendor 類型(system或vendor)", required = true) @HeaderParam("vendorType") String vendorType,
			 @ApiParam(value = "Vendor Token", required = true) @HeaderParam("vendorToken") String vendorToken) throws Exception 
	 {	
		logger.info("VendorName : "+vendorToken);  
		 
		 List<VoucherEvent> list = new ArrayList<>();
		 try 
		 {				 
			 Class.forName("com.mysql.jdbc.Driver");
			 Connection con = DbUtil.getConnection();
			 
			 if(vendorType.equals("system") && vendorToken.equals("admin"))
			 {
				 SQL = "SELECT * FROM voucher";
				 stmt = con.createStatement();
				 rs = stmt.executeQuery(SQL);
				 
				 while (rs.next()) 
				 {
	    			VoucherEvent result = new VoucherEvent(rs.getString("Uid"), rs.getString("Voucher_Name"), 
	    					rs.getString("Voucher_Discount"), rs.getInt("Number"), rs.getInt("Quota_Each"), 
	    					rs.getString("Vendor"), rs.getString("StartTime"), rs.getString("EndTime"), 
	    					rs.getString("Create_Date"), rs.getString("Modify_Date"));
	    			list.add(result);
				 }
				 rs.close();
				 stmt.close();
	    	 }
			 else
			 {
				exportString = "Illegal Account";
			    json = gson.toJson(exportString); 
			 }
		}
		catch( ClassNotFoundException e) 
		{
			System.out.println("DriverClassNotFound :"+e.toString());
		} 
		catch (SQLException e) 
	    {
			System.out.println("SQL Exception :" +e.toString());
	    }	     
		 
		 json = gson.toJson(list);		 	 
		 return json;
	 }
	 
	 
	 @ApiOperation(value = "查詢vendor各自擁有折價券類型", response = VoucherEvent.class, responseContainer = "Array")
	 @ApiResponses(value = {
				  @ApiResponse(code = 500, message = "Internal Error"),
				  @ApiResponse(code = 404, message = "Attraction not found")
				})
	 @GET
	 @Path("/vendorname")
	 public String getVoucherId(@ApiParam(allowableValues = "system,vendor", value = "Vendor 類型(system或vendor)", required = true) @HeaderParam("vendorType") String vendorType, 
			 @ApiParam(value = "Vendor Token", required = true) @HeaderParam("vendorToken") String vendorToken) throws Exception 
	 { 		 
		 	logger.info("VendorName : "+vendorToken);
		 	
		 	List<VoucherEvent> list = new ArrayList<>();		 
	    	try 
	    	{	    	
	    		Class.forName("com.mysql.jdbc.Driver");
	    		Connection con = DbUtil.getConnection();	    	
	    		
	    		if(vendorType.equals("vendor"))
	   		 	{	   		 
	    			SQL = "SELECT * FROM voucher where Vendor = " + "'" + vendorToken + "'";
	    			stmt = con.createStatement();
	    			rs = stmt.executeQuery(SQL);

	    			while (rs.next()) 
	    			{
	    				VoucherEvent result = new VoucherEvent(rs.getString("Uid"), rs.getString("Voucher_Name"), 
		    					rs.getString("Voucher_Discount"), rs.getInt("Number"), rs.getInt("Quota_Each"), 
		    					rs.getString("Vendor"), rs.getString("StartTime"), rs.getString("EndTime"), 
		    					rs.getString("Create_Date"), rs.getString("Modify_Date"));
	    				list.add(result);
	    			}
	    			rs.close();
	    			stmt.close();
	   		 	}
	    		else
				{
	    			exportString = "Illegal Account";
	    			json = gson.toJson(exportString); 
				}
	    	}
	    	catch( ClassNotFoundException e) 
	    	{
	    		System.out.println("DriverClassNotFound :"+e.toString());
	      	} 
	    	catch (SQLException e) 
	    	{
	    		System.out.println("SQL Exception :" +e.toString());
	  		}
	    	
	      	json = gson.toJson(list);
	
	        return json;		 
	 }
	 
	 
	 @ApiOperation(value = "查詢user各自所擁有的折價券", response = VoucherIdEvent.class, responseContainer = "Array")
	 @ApiResponses(value = {
				  @ApiResponse(code = 500, message = "Internal Error"),
				  @ApiResponse(code = 404, message = "Attraction not found")
				})
	 @GET
	 @Path("/customerid")
	 public String getVoucherCustomerId(@ApiParam(value = "User Token", required = true) @HeaderParam("userToken") String userToken) throws Exception 
	 {		 		 
		 logger.info("UserName : "+userToken);  
		 
		 String customerId = UserTokenService.getUserId(userToken) ;
		 List<VoucherIdEvent> list = new ArrayList<>();
	    	try 
	    	{	    		
	    		Class.forName("com.mysql.jdbc.Driver");
	    		Connection con = DbUtil.getConnection();
	    		
	     		SQL = "SELECT * FROM voucher_detail where Customer_Id = " + "'" + customerId + "'";
	    		stmt = con.createStatement();
	    		rs = stmt.executeQuery(SQL);
	    		
	    		while (rs.next()) 
	    		{
	    			VoucherIdEvent result = new VoucherIdEvent(rs.getString("Customer_Id"), rs.getString("Voucher_Uid"), 
	    					rs.getString("Voucher_Name"), rs.getString("Voucher_Discount"), rs.getString("Vendor"), 
	    					rs.getString("Voucher_Code"), rs.getString("Date"), rs.getString("StartTime"), 
	    					rs.getString("EndTime"), rs.getString("State"));	    			
	    			list.add(result);
	    		}
	    		rs.close();
	    		stmt.close();
	    	}
	    	catch( ClassNotFoundException e) 
	    	{
	    		
	    		System.out.println("DriverClassNotFound :"+e.toString());
	      	} 
	    	catch (SQLException e) 
	    	{
	    		System.out.println("SQL Exception :" +e.toString());
	  		}

	      	json = gson.toJson(list);      	
	        return json;		 
	 }

	 @ApiOperation(value = "新增一項折價券類別與詳細資訊", response = VoucherEvent.class)
	 @ApiResponses(value = {
				  @ApiResponse(code = 500, message = "Internal Error"),
				  @ApiResponse(code = 404, message = "Attraction not found")
				})
	 @POST
	 public String CeateVoucher (@ApiParam(allowableValues = "system,vendor", value = "Vendor 類型(system或vendor)", required = true) @HeaderParam("vendorType") String vendorType, 
			 @ApiParam(value = "Vendor Token", required = true) @HeaderParam("vendorToken") String vendorToken, 
			 @ApiParam(value = "新增事件所需之詳細資料", required = true) VoucherInput httpBody) throws Exception 
	 {			 
		 logger.info("VendorName : "+vendorToken + "Body : " + httpBody);
		 
		 try 
		 {				 
			 Class.forName("com.mysql.jdbc.Driver");
			 Connection con = DbUtil.getConnection();	
			 if(vendorType.equals("vendor"))
			 {
	    		String sqlCmd = "INSERT INTO voucher (Uid, Voucher_Name, Voucher_Discount, Number, Quota_Each, Vendor, StartTime, EndTime, Create_Date, Modify_Date)"
	    					+ " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ? );"; 
	    		
	    		SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
				Date current = new Date();		
				
	    		String uid = UUID.randomUUID().toString();
	    		String date = sd.format(current);
	
		    	pst = con.prepareStatement(sqlCmd) ;
		    	pst.setString(1, uid);
		    	pst.setString(2, httpBody.getVoucher_Name());
		    	pst.setString(3, httpBody.getVoucher_Discount());
		    	pst.setInt(4, httpBody.getNumber());
		    	pst.setInt(5, httpBody.getQuota_Each());
		    	pst.setString(6, vendorToken);
		    	pst.setString(7, httpBody.getStartTime());
		    	pst.setString(8, httpBody.getEndTime());
		    	pst.setString(9, date);
		    	pst.setString(10, null);	
		    	pst.executeUpdate() ;
		    	VoucherEvent result = new VoucherEvent(uid,httpBody.getVoucher_Name(),httpBody.getVoucher_Discount(),
		    			httpBody.getNumber(),httpBody.getQuota_Each(),vendorToken,httpBody.getStartTime(),
		    			httpBody.getEndTime(),date,null);
		    	json = gson.toJson(result); 
			 }
			 else
			 {
				 exportString = "Illegal Account";
				 json = gson.toJson(exportString); 
			 }
		 }			 
		 catch( ClassNotFoundException e) 
		 {
			 System.out.println("DriverClassNotFound :"+e.toString());
		 } 
		 catch (SQLException e) 
		 {
			 System.out.println("SQL Exception :" +e.toString());
		 }
		 
		 return json;
	 }	 
	 
	 @ApiOperation(value = "user申請產生折價券兌換號碼", response = VoucherIdEvent.class)
	 @ApiResponses(value = {
				  @ApiResponse(code = 500, message = "Internal Error"),
				  @ApiResponse(code = 404, message = "Attraction not found")
				})
	 @POST
	 @Path("/createcode")
	 public String CeateVocherCostomer (@ApiParam(value = "User Token", required = true) @HeaderParam("userToken") String userToken, 
			 @ApiParam(value = "折價券 Uid", required = true) @HeaderParam("voucherUid") String voucherUid) throws Exception 
	 {    		 		 
		 logger.info("UserName : "+userToken + "VoucherUid : " + voucherUid);
		 
		 String customerId = UserTokenService.getUserId(userToken) ;
		 try 
	  	 {
			 Class.forName("com.mysql.jdbc.Driver");
			 Connection con = DbUtil.getConnection();	    		
			    		
			 String sqlCmd = "INSERT INTO voucher_customer (Customer_Id, Voucher_Uid, Voucher_Code, Date, State)"
	    					+ " VALUES( ?, ?, ?, ?, ?);"; 
	    		
			 SQL = "SELECT Number From voucher where Uid ='"+ voucherUid +"';";
			 stmt = con.createStatement();
			 rs =stmt.executeQuery(SQL);	
			 rs.next();
			 int n = rs.getInt("Number");
			 
			 if(n!=0)
			 {
				 SQL = "SELECT Quota_Each From voucher where Uid ='"+ voucherUid +"';";
				 stmt = con.createStatement();
				 rs =stmt.executeQuery(SQL);	
				 rs.next();
				 int q = rs.getInt("Quota_Each");
				 
				 SQL = "SELECT Voucher_Uid From voucher_customer where Voucher_Uid ='"+ voucherUid +"';";
				 stmt = con.createStatement();
				 rs =stmt.executeQuery(SQL);	
				 rs.next();
				 int v = rs.getRow();
				 
				 if(v<q)
				 {
					 String uid = UUID.randomUUID().toString();
					 SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
					 Date current = new Date();	
					 
						
					 pst = con.prepareStatement(sqlCmd) ;
					 pst.setString(1, customerId);
					 pst.setString(2, voucherUid);
					 pst.setString(3, uid);
					 pst.setString(4, sd.format(current));
					 pst.setString(5, null);		    			    		
					 pst.executeUpdate() ;
					 n=n-1;
				 				 
					 String sqlCmd2 = "UPDATE voucher SET Number = '" + n+ "' where Uid ='"+ voucherUid +"';";
					 stmt.executeUpdate(sqlCmd2) ;		
	    			
					 List<VoucherIdEvent> list = new ArrayList<>();	
					 SQL = "SELECT * From voucher_customer where Customer_Id ='"+ customerId+"'and "+ "Voucher_Code ='"+ uid +"';";
					 stmt = con.createStatement();
					 rs = stmt.executeQuery(SQL);
					 while (rs.next()) 
					 {
						 VoucherIdEvent result = new VoucherIdEvent();		    			
						 result.setCustomer_Id(rs.getString("Customer_Id"));
						 result.setVoucher_Uid(rs.getString("Voucher_Uid"));
						 result.setVoucher_Code(rs.getString("Voucher_Code"));
						 result.setDate(rs.getString("Date"));
						 result.setState(rs.getString("State"));
						 list.add(result);
					 }
					 rs.close();
					 stmt.close();		    		
					 json = gson.toJson(list);
				 }
				 else
				 {
					 exportString = "No Quota";
					 json = gson.toJson(exportString); 
				 }
			 }
			 else	    		
			 {
				 exportString = "No enough voucher";
				 json = gson.toJson(exportString); 
			 }
	  	 }
		 catch( ClassNotFoundException e) 
		 {
			 System.out.println("DriverClassNotFound :"+e.toString());
		 } 
		 catch (SQLException e) 
		 {
			 System.out.println("SQL Exception :" +e.toString());
		 }
		 
	     return json;	
	 }
	 
	 @ApiOperation(value = "修改折價券狀態(兌換)", response = VoucherIdEvent.class)
	 @ApiResponses(value = {
				  @ApiResponse(code = 500, message = "Internal Error"),
				  @ApiResponse(code = 404, message = "Attraction not found")
				})
	 @PUT
	 @Path("/exchangecode")
	 public String exchangeVoucher(@ApiParam(allowableValues = "system,vendor", value = "Vendor 類型(system或vendor)", required = true) @HeaderParam("vendorType") String vendorType, 
			 @ApiParam(value = "Vendor Token", required = true) @HeaderParam("vendorToken") String vendorToken, 
			 @ApiParam(value = "折價券兌換號碼", required = true) @HeaderParam("code") String code) throws Exception
	 {
		logger.info("VendorName : "+vendorToken + "Code : " + code);
		 
		try 
	  	{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DbUtil.getConnection();
			
			String tmpSQL = "SELECT Voucher_Uid FROM voucher_customer WHERE  Voucher_Code = " + "'" + code + "';";
			stmt = con.createStatement() ;
			rs =stmt.executeQuery(tmpSQL);
			rs.next();
			String tmpSQL2 = "SELECT Voucher_Name FROM voucher WHERE Uid = " + "'" + rs.getString("Voucher_Uid") + "' and Vendor = " + "'" + vendorToken +"';" ;
			stmt = con.createStatement() ;
			rs =stmt.executeQuery(tmpSQL2);
			rs.next();

			if(vendorType.equals("vendor") && rs.getRow()!=0)
			{
				SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
				Date current = new Date();
				SQL = "SELECT Voucher_Uid, State FROM  voucher_detail WHERE  Voucher_Code = " + "'" + code + "'" +" AND TO_DAYS(StartTime) <= TO_DAYS('"+ sd.format(current) +"') AND TO_DAYS('"+ sd.format(current) +"') <= TO_DAYS(EndTime)";
				stmt = con.createStatement() ;
				rs =stmt.executeQuery(SQL);
				rs.next();			
				
				if(rs.getRow()==0 && rs.getString("State")!=null)
				{					
					exportString= "Illegal code or Used";
					json = gson.toJson(exportString);
				}				
				else
				{	
					String id = rs.getString("Voucher_Uid");				
					String sqlCmd = "UPDATE voucher_customer SET ";						
					current = new Date();
				
					sqlCmd = sqlCmd + "State = '" + sd.format(current) + "' where Voucher_Code = '"+ code + "'" ;	
					stmt.executeUpdate(sqlCmd) ;
								
					String sqlCmd2 = "UPDATE voucher SET ";
					sqlCmd2 = sqlCmd2 + "Modify_Date = '" + sd.format(current)+"' where Uid = '"+ id + "'" ;
					stmt.executeUpdate(sqlCmd2) ;				
							
					List<VoucherIdEvent> list = new ArrayList<>();	
					SQL ="SELECT * FROM  voucher_customer WHERE  Voucher_Code = " + "'" + code + "';";
					stmt = con.createStatement();
					rs = stmt.executeQuery(SQL);
					while (rs.next()) 
					{
						VoucherIdEvent result = new VoucherIdEvent();		    			
						result.setCustomer_Id(rs.getString("Customer_Id"));
						result.setVoucher_Uid(rs.getString("Voucher_Uid"));
						result.setVoucher_Code(rs.getString("Voucher_Code"));
						result.setDate(rs.getString("Date"));
						result.setState(rs.getString("State"));
						list.add(result);
					}
					rs.close();
					stmt.close();		    		
					json = gson.toJson(list);
				}
			}
			else
			{
    			exportString = "Illegal Account";
    			json = gson.toJson(exportString); 
			}
	  	}
		catch( ClassNotFoundException e) 
  	  	{
  	  		System.out.println("DriverClassNotFound :"+e.toString());
  	  	} 
  	  	catch (SQLException e) 
  	  	{
  	  		System.out.println("SQL Exception :" +e.toString());
  	  	}     	
	    return json;
	 }
	
	 @ApiOperation(value = "變更折價券內容", response = VoucherEvent.class)
	 @ApiResponses(value = {
				  @ApiResponse(code = 500, message = "Internal Error"),
				  @ApiResponse(code = 404, message = "Attraction not found")
				})
	 @PUT
	 @Path("/voucheruid")
	 public String modifyVoucher(@ApiParam(allowableValues = "system,vendor", value = "Vendor 類型(system或vendor)", required = true) @HeaderParam("vendorType") String vendorType, 
			 @ApiParam(value = "Vendor Token", required = true) @HeaderParam("vendorToken") String vendorToken, 
			 @ApiParam(value = "折價券 Uid", required = true) @HeaderParam("voucherUid") String voucherUid, 
			 @ApiParam(value = "修改事件所需之詳細資料,亦可只修改其中之一,範例{'Quota_Each':2}", required = true) VoucherModify httpBody) throws Exception
	 {		
		logger.info("VendorName : "+vendorToken + "VoucherUid : " + voucherUid + "Body : " + httpBody); 
		 
		try 
	  	{
			Class.forName("com.mysql.jdbc.Driver");
			Connection con = DbUtil.getConnection();
			
			String tmpSQL = "SELECT Voucher_Name FROM voucher WHERE Uid = " + "'" + voucherUid + "' and Vendor = " + "'" + vendorToken +"';" ;
			stmt = con.createStatement() ;
			rs =stmt.executeQuery(tmpSQL);
			rs.next();
			
			if(vendorType.equals("vendor") && rs.getRow()!=0)
			{
				SQL = "SELECT Uid FROM voucher WHERE  Uid = " + "'" + voucherUid +"'";
				stmt = con.createStatement() ;
				rs =stmt.executeQuery(SQL);
				rs.next();					

				if(rs.getRow()==0)
				{
					exportString= "Invalid UID";				
					json = gson.toJson(exportString);  
				}
				else
				{	
					String sqlCmd = "UPDATE voucher SET ";	
					if ( httpBody.getVoucher_Name() != null )
					{
						sqlCmd = sqlCmd + "Voucher_Name = '" + httpBody.getVoucher_Name() + "'," ;
					}
					if ( httpBody.getVoucher_Discount() != null )
					{
						sqlCmd = sqlCmd + "Voucher_Discount = '" + httpBody.getVoucher_Discount() + "'," ;
					}
					if ( httpBody.getNumber() != 0)
					{
						sqlCmd = sqlCmd + "Number = '" + httpBody.getNumber() + "'," ;
					}
					if ( httpBody.getQuota_Each() != 0)
					{
						sqlCmd = sqlCmd + "Quota_Each = '" + httpBody.getQuota_Each() + "'," ;
					}
					if ( httpBody.getStartTime() != null )
					{
						sqlCmd = sqlCmd + "StartTime = '" + httpBody.getStartTime() + "'," ;
					}
					if ( httpBody.getEndTime() != null )
					{
						sqlCmd = sqlCmd + "EndTime = '" + httpBody.getEndTime() + "'," ;
					}
				
					SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
					Date current = new Date();
					sqlCmd = sqlCmd + "Modify_Date = '" + sd.format(current) + "'," ;
					sqlCmd = sqlCmd.substring(0,sqlCmd.length()-1) ; // 移除最後一個字元 ',' 
					sqlCmd = sqlCmd + " WHERE  Uid = " + "'" + voucherUid +"'";
					stmt.executeUpdate(sqlCmd) ;
				
					List<VoucherEvent> list = new ArrayList<>();	
					SQL = "SELECT * FROM voucher where Uid = '" + voucherUid +"';";
					stmt = con.createStatement();
					rs = stmt.executeQuery(SQL);
					while (rs.next()) 
					{
						VoucherEvent result = new VoucherEvent(rs.getString("Uid"), rs.getString("Voucher_Name"), 
		    					rs.getString("Voucher_Discount"), rs.getInt("Number"), rs.getInt("Quota_Each"), 
		    					rs.getString("Vendor"), rs.getString("StartTime"), rs.getString("EndTime"), 
		    					rs.getString("Create_Date"), rs.getString("Modify_Date"));
						list.add(result);
					}
					rs.close();
					stmt.close();
					json = gson.toJson(list);
				}
			}
			else
			{
    			exportString = "Illegal Account";
    			json = gson.toJson(exportString); 
			}
	  	}
		catch( ClassNotFoundException e) 
  	  	{
  	  		System.out.println("DriverClassNotFound :"+e.toString());
  	  	} 
  	  	catch (SQLException e) 
  	  	{
  	  		System.out.println("SQL Exception :" +e.toString());
  	  	}
    	
	    return json;
	 }
}
