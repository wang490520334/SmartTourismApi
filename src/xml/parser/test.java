package xml.parser;

import iii.org.tw.util.DbUtil;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.FacebookApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;
import org.w3c.dom.*;

public class test {

	private static Statement stat = null ; //執行, 傳入需為完整sql字串
	private static ResultSet rs = null ; //sql result 
	private static PreparedStatement pst = null;  //執行,傳入之sql為預儲之字申,需要傳入變數之位置 
										   //先利用?來做標示 

	private static final String NETWORK_NAME = "Facebook";
	private static final String PROTECTED_RESOURCE_URL = "https://graph.facebook.com/me";
	private static final Token EMPTY_TOKEN = null;
	
	
	public static void main( String argv[] ) {
		
		
		FacebookLogin() ;
		
		// XMLParse() ;
		
	} // main()

	public static void FacebookLogin() {

		
		//https://www.facebook.com/dialog/oauth?client_id=274868366049849&scope=email,user_birthday&redirect_uri=http://localhost:8080/SmartTourismApi/api/v1/fbLogin/
		
	    // Replace these with your own api key and secret
	    String apiKey = "274868366049849";
	    String apiSecret = "cfaa422e90e79ee9372e4ca55f7dcbbb";
	    OAuthService service = new ServiceBuilder()
	                                  .provider(FacebookApi.class)
	                                  .apiKey(apiKey)
	                                  .apiSecret(apiSecret)
	                                  .callback("http://gene.com.tw:8080/SmartTourismApi/api/v1/fbLogin/")
	                                  .build();
	   
	    // Obtain the Authorization URL
	    //System.out.println("Fetching the Authorization URL...");
	    String authorizationUrl = service.getAuthorizationUrl(EMPTY_TOKEN);
	   
	    System.out.println(authorizationUrl);
	} // FacebookLogin
	
	public void XMLParse() {
		

		try {
			
			Class.forName("com.mysql.jdbc.Driver"); 
	    	//註冊driver
	    	Connection con = DbUtil.getConnection() ;
	    	
			
			File restrauntXML = new File("C://Users/GeneChu/Documents/restaurant_C.xml");
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(restrauntXML) ;
			
			doc.getDocumentElement().normalize();
			
			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
			
			NodeList nList = doc.getElementsByTagName("Info") ;
			
			System.out.println(" nList.getLength() :" +  nList.getLength());
			for ( int i = 0 ; i < 1 ; i++ ) {
				
				
				Node nNode = nList.item(i) ;
				
				if ( nNode.getNodeType() == Node.ELEMENT_NODE ) {
					
					Element eElement = (Element) nNode;
					
					System.out.print("id:" + eElement.getAttribute("Id") + " ");
					
					String sqlCmd = "SELECT * FROM food_drink_part_general WHERE Place_Id = '" +  eElement.getAttribute("Id") + "'" ;
					stat = con.createStatement(); // 執行,傳入之sql為完整字串 
					rs = stat.executeQuery(sqlCmd) ; // result
					
					
					if ( !rs.next() )  {				
						
						sqlCmd = "INSERT INTO food_drink_part_general ( Place_Id, Tel, County, Price, Currency, Picture, Video, Website, Stay_Time, Px, Py)"
								+ " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" ;
								
						
						pst = con.prepareStatement(sqlCmd) ;
						pst.setString( 1, eElement.getAttribute("Id") );
						pst.setString( 2, eElement.getAttribute("Tel") );
						pst.setString( 3, eElement.getAttribute("Region") );
						pst.setString( 4, null );
						pst.setString( 5, null );
						pst.setString( 6, eElement.getAttribute("Picture1") );
						pst.setString( 7, null );
						pst.setString( 8, eElement.getAttribute("Website") );
						pst.setString( 9, null );
						pst.setDouble( 10, Double.valueOf(eElement.getAttribute("Px")) );
						pst.setDouble( 11, Double.valueOf(eElement.getAttribute("Py")) );
	
						pst.executeUpdate() ;
						
						sqlCmd = "INSERT INTO food_drink_part_zh_tw ( Place_Id, Name, Address, Parking, Description, Picture_Description, Transport, Opentime, Note, Paying)"
								+ " VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)" ;
								
						pst = con.prepareStatement(sqlCmd) ;
						pst.setString( 1, eElement.getAttribute("Id") );
						pst.setString( 2, eElement.getAttribute("Name") );
						pst.setString( 3, eElement.getAttribute("Add") );
						pst.setString( 4, eElement.getAttribute("Parkinginfo") );
						pst.setString( 5, eElement.getAttribute("Description") );
						pst.setString( 6, eElement.getAttribute("Picdescribe1") );
						pst.setString( 7, null );
						pst.setString( 8, eElement.getAttribute("Opentime") );
						pst.setString( 9, null );
						pst.setString( 10, null );
						
						pst.executeUpdate() ;
						
//						System.out.println("id :" + eElement.getAttribute("Id") + ", Name :" + eElement.getAttribute("Name")
//								+ ", Description :" + eElement.getAttribute("Description") + ", Add :" + eElement.getAttribute("Add")
//								+ ", Zipcode :" + eElement.getAttribute("Zipcode") +
//								+ ", Tel :" + eElement.getAttribute("Tel") + ", Opentime :" + eElement.getAttribute("Opentime")
//								+ ", Website :" + eElement.getAttribute("Website") + ", Picture1 :" + eElement.getAttribute("Picture1")
//								+ ", Picdescribe1 :" + eElement.getAttribute("Picdescribe1") + ", Picture2 :" + eElement.getAttribute("Picture2")
//								+ ", Picdescribe2 :" + eElement.getAttribute("Picdescribe2") + ", Picture3 :" + eElement.getAttribute("Picture2")
//								+ ", Picdescribe3 :" + eElement.getAttribute("Picdescribe3") + ", Px :" + eElement.getAttribute("Px")
//								+ ", Py :" + eElement.getAttribute("Py") + ", Class :" + eElement.getAttribute("Class")
//								+ ", Map :" + eElement.getAttribute("Map") + ", Parkinginfo :" + eElement.getAttribute("Parkinginfo") 
//								+ ", Region :" + eElement.getAttribute("Region") + ", Town :" + eElement.getAttribute("Town")
//								);
						
						System.out.println("insert :" + eElement.getAttribute("Id") + " successfully.") ;
						
					} // end if ( !rs.next() )
					else {
						System.out.println("find :" + eElement.getAttribute("Id") + "in database, skipped this item.") ;
					} // end else
					
					
				} // end if ( nNode.getNodeType() == Node.ELEMENT_NODE )
				
				
			} // end for
			
			
			System.out.println("---------------------------------XML PARSING END---------------------------------------");
		} catch (Exception e) {
			e.printStackTrace();
	    }
		
	} // XMLParse()
	
}
