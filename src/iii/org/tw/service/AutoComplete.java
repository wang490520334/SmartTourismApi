package iii.org.tw.service;




import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.JsonArray;

/*
 * InputWords->ResultClustering->SolrMlt
 */

  public class AutoComplete {


      private static String str = null;

		public static String main(String keyword, String langtype) throws JSONException {
 			String result = new AutoComplete().go(keyword,langtype);
			return result;
      }

      public String go(String keyword, String langtype) throws JSONException {
      	
      /*
      	Properties properties = new Properties();
      	try {
      	    properties.load(getClass().getResourceAsStream("/config.properties"));

      	} catch (FileNotFoundException ex) {
      	    ex.printStackTrace();
      	} catch (IOException ex) {
      	    ex.printStackTrace();
      	   
      	}
      	*/
      	
      	
      	
      	
      	
      	
      	
      	
     //	String url = "http://140.92.2.183:8983/solr/"+ langtype +"/terms?terms.fl=Name&terms.fl=Description&terms.prefix=" + keyword + "&terms.limit=10&wt=json&indent=true";
        String url = "http://140.92.88.113:8983";
      	
	   	WebTarget target = ClientBuilder.newClient().target(url).path("solr/"+ langtype +"/terms")
	   			.queryParam("terms.prefix", keyword).queryParam("terms.fl", "Name")
	   			.queryParam("terms.limit", "10").queryParam("wt", "json").queryParam("indent", "true");
	   	
	  // 	logger.info("url={}", url);        	

	   	
          /*
      	target.queryParam("terms.prefix", keyword)
          .queryParam("terms.fl", "Name")
          .queryParam("terms.fl", "Description")
          .queryParam("terms.limit", "10")
          .queryParam("wt", "json")
          .queryParam("indent", "true");
          */

          str = target.request().get(String.class);
    
          JSONObject js;

 	      js = JSONObject.fromObject(str);
 	     
 	      JSONArray jsonarry = (JSONArray) js.getJSONObject("terms").get("Name");
 	      
	      JSONArray jsonAray1 = new JSONArray();
	      JSONArray jsonAray2 = new JSONArray();

    	  JSONObject jsonObj1  = new JSONObject();
    	  String finallist = "";
 	      

 	    	  
    	  JSONArray nameResult = new JSONArray();
 	    	 for (int i = 0; i < jsonarry.size(); i+=2){
 	    		 String name = jsonarry.getString(i);
 	    		 nameResult.add(name);
 	    		 //System.out.println(objectInArray);
			//     String word1 = words.toString();
			//     String word2 = word1.split(",")[0];


	             

	            

	      //      	 jsonObj.put("STATEID", word2);
	            	
	            	 
	            	
		        
	            	 
		            
	  	           
		       
	             }
 	    	js.getJSONObject("terms").put("Name", nameResult);
 	      
 
 	    
 	     
 	     
       
		  return js.toString();

          
       
          
          
      }
      final static Logger logger = LoggerFactory.getLogger(AutoComplete.class);

  }
