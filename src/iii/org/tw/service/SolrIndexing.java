package iii.org.tw.service;

  import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

    public class SolrIndexing {


        private static String str = null;

		public static String main(int servicetype) {
            new SolrIndexing().go_match(servicetype);
			return str;
        }

        public void go_match(int servicetype) {
        	 String tablename = "";
        	try {
			    Thread.sleep(10000);
			    /*
	        	 * Load ip config file
	        	 */
	        	Properties properties = new Properties();
	        	try {
	        	    properties.load(getClass().getResourceAsStream("/config.properties"));

	        	} catch (FileNotFoundException ex) {
	        	    ex.printStackTrace();
	        	} catch (IOException ex) {
	        	    ex.printStackTrace();
	        	   
	        	}
	        	String url = properties.getProperty("ip");
	        	
			    
			    if (servicetype == 1){
					tablename = "ASSET";
					url = url + ":8983/solr/smartasset/dataimport?";
			   // 	logger.info("SolrIndex url2={}", url);
					Client client = ClientBuilder.newClient();
			      	WebTarget target = client.target(url);
		         //   logger.info("time={},comname={}", time,comname);
		            // �]�w�n�a���Ѽ�
			      	target.queryParam("command", "delta-import").queryParam("clean", "false")
		            .queryParam("entity", tablename).queryParam("commit", "true").queryParam("optimize", "true");

		          //  System.out.println("1");

		            str = target.request().get(String.class);
		           
		            logger.info("str_service={}", str);
		        }
        	}catch (InterruptedException e) {
				    e.printStackTrace();
				    // handle the exception...        
				    // For example consider calling Thread.currentThread().interrupt(); here.
				}
        

            
            
        }
       
        
        final static Logger logger = LoggerFactory.getLogger(SolrIndexing.class);

    }
 