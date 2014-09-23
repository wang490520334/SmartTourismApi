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

/*
 * InputWords->ResultClustering->SolrMlt
 */

  public class Search {


      private static String str = null;

		public static String main(String keyword, String langtype) throws JSONException {
 			String result = new Search().go(keyword,langtype);
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
      	
      	
      	
      	
      	
      	
      	
      	String url = "http://140.92.88.113:8983";
      	
	   	WebTarget target = ClientBuilder.newClient().target(url).path("solr/"+ langtype +"/select")
	   			.queryParam("q", keyword).queryParam("start", "0").queryParam("rows", "100")
	            .queryParam("wt", "json").queryParam("indent", "true");
          
          str = target.request().get(String.class);

          JSONObject js;

 	      js = JSONObject.fromObject(str);
 	     
 	      JSONArray jsonarry = (JSONArray) js.getJSONObject("response").get("docs");
 	      
	      JSONArray jsonAray0 = new JSONArray();

	      JSONArray jsonAray1 = new JSONArray();
	      JSONArray jsonAray2 = new JSONArray();

    	  String finallist = "";
    	  JSONObject jsonObj0  = new JSONObject();

    	  JSONObject jsonObj1  = new JSONObject();

    	  
    	  try {
    		  
    		if (keyword.length()>2){  
    			
    			String SegWord = InputSeg.main(keyword);
    			
    			
    			String[] e1 = SegWord.split(",");

    			for (int i = 0; i < e1.length; i++){
    		    	JSONObject jsonObj2  = new JSONObject();

    				String words = e1[i];
        		   	logger.info("words={}", words);        	

    				jsonObj2.put("keywords",words);
        			jsonAray2.add(jsonObj2);

    				
    			}
    			
    			
    			

    		}else{
		    	JSONObject jsonObj2  = new JSONObject();

    			jsonObj2.put("keywords",keyword);
    			jsonAray2.add(jsonObj2);

    		}
    		  
		
    	  
    	  
    	  } catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	  
    	  

	      jsonObj0.put("SearchResult", jsonAray2);
	      jsonAray0.add(jsonObj0);
	      
 	    	 for (int i = 0; i < jsonarry.size(); i++){
 	    		 JSONObject objectInArray = jsonarry.getJSONObject(i);
			     JSONObject jsonObj  = new JSONObject();

			    
	             String Id = (String) objectInArray.get("Id");
	             String Poitype = (String) objectInArray.get("Poitype");

	             //String Tid = Id.substring(0, 1);
	             int type = Integer.parseInt(Poitype);
	             String deftype = "";
	             
	            switch (type){
	        	   
	           	case 0:
	           		deftype = "Attraction";
	           		break;
	           		
	           	case 1:
	           		deftype = "FoodAndDrink";
	           		break;

	           	case 2:
	           		deftype = "Shopping";
	           		break;
	           		
	        	case 3:
	           		deftype = "Tour";
	           		break;
	           		
	        	case 4:
	           		deftype = "Activity";
	           		break;
	           	
	           	}
	             
	     	     jsonObj.put("type", deftype);
	             jsonObj.put("id", Id);
	            // jsonObj.put("Description", Desc);

	             jsonAray1.add(jsonObj);
      	 
 	    	 }

      
 	    	 jsonObj1.put("Result", jsonAray1);
 		     jsonAray0.add(jsonObj1);

	            	 
 	      //   jsonAray2.put(jsonObj1);      
	  	           
		       
	             
	             
 	      
 
 	    
 	     
 	      
 	 	//     jsonObj1.put("cluster2", jsonAray2);

 	 	      finallist = jsonAray0.toString(); 
 	      
 	      
 		   
       
		  return finallist;

          
       
          
          
      }
      final static Logger logger = LoggerFactory.getLogger(Search.class);

  }
