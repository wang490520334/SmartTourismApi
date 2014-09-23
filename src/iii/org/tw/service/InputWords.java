package iii.org.tw.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Properties;

import javax.ws.rs.core.*;

import net.sf.json.JSONException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * InputWords->ResultClustering->SolrMlt
 */

  public class InputWords {


      private static String str = null;

		public static String main(String keyword, int lang) throws JSONException {
			

			String result = new InputWords().go(keyword,lang);
			return result;
      }

      public String go(String keyword, int lang) throws JSONException {
      	
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
      	String results = "";
      	String langtype = "";
      	
      	switch (lang){
      	   
      	case 0:
      		langtype = "STTC";
      		results = Search.main(keyword,langtype);
      		break;
      		
      	case 1:
      		langtype = "STEN";
      		results = Search.main(keyword,langtype);
      		break;

      	case 2:
      		langtype = "STJP";
      		results = Search.main(keyword,langtype);	
      		break;

      	
      	}
		return results;
      	

          
       
          
          
      }
      final static Logger logger = LoggerFactory.getLogger(InputWords.class);

  }
