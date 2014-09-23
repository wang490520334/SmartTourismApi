package iii.org.tw.service;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
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

  public class SolrMlt {


      private static String str = null;

		public static JSONArray main(String keyword) throws JSONException {
          JSONArray result = new SolrMlt().go(keyword);
			return result;
      }

      public JSONArray go(String id) throws JSONException {
      	
      
      	Properties properties = new Properties();
      	try {
      	    properties.load(getClass().getResourceAsStream("/config.properties"));

      	} catch (FileNotFoundException ex) {
      	    ex.printStackTrace();
      	} catch (IOException ex) {
      	    ex.printStackTrace();
      	   
      	}
      	String url = properties.getProperty("ip");
      	url = url + ":8983/solr/post/mlt?";
	    //	logger.info("ServiceRegion url={}", url);        	
      	
      	String idinfo = "prod_uid : " + id; 
        //get similar docu. based on the prod_id
      	
      	Client client = ClientBuilder.newClient();
      	WebTarget target = client.target(url);
      	target.queryParam("q", idinfo).queryParam("start", "0").queryParam("rows", "10")
      		.queryParam("wt", "json").queryParam("indent", "true");
         
      	str = target.request().get(String.class);
          
          JSONArray results = SolrMlt.filter(str);
       
		   return results;

          
       
          
          
      }
      
      public static JSONArray filter(String str) throws JSONException {
        	
          
	       JSONObject js;
	       JSONArray jsonAray1 = new JSONArray();
	       js = JSONObject.fromObject(str);

		   Object jsonOb = js.getJSONObject("response").get("numFound");
		   String value = jsonOb.toString();
		   String results = "";
  		//   ArrayList <String> list = new ArrayList();

		   if (value.equals("0")){
			   results = "";

		   }else{
	  	     
	  	       JSONArray jsonarry = (JSONArray) js.getJSONObject("response").get("docs");
	  	        String prod_name = "";
	  	        String prod_uid = "";
	  	        String prod_selling_price = "";


	  	       for (int i = 0; i < jsonarry.size(); i++){
	  	          JSONObject objectInArray = jsonarry.getJSONObject(i);
	  	          prod_name = (String) objectInArray.get("prod_name");
	  	          prod_uid  = (String) objectInArray.get("prod_uid");
	  	          prod_selling_price = (String) objectInArray.get("prod_selling_price");
	  	        //  String reslist = "[\"prod_uid\":\""+ prod_uid + "\"," + "\"prod_name\":\"" + prod_name + "\"," + "\"prod_selling_price\":\"" + prod_selling_price + "\"]";
	  	      //    JSONObject jsonObj  = new JSONObject(reslist);
	  	       //   list.add(jsonObj4);
	  	          JSONObject jsonObj  = new JSONObject();
	  	          jsonObj.put("prod_uid", prod_uid);
	  	          jsonObj.put("prod_name", prod_name);
	  	          jsonObj.put("prod_selling_price", prod_selling_price);
	  	          jsonAray1.add(jsonObj);
	  	       }


	  	      
		   }
		//   logger.info("list={}", list);
		   return jsonAray1;

   
         
         
     }
      
      
      final static Logger logger = LoggerFactory.getLogger(SolrMlt.class);

  }
