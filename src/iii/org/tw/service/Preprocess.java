package iii.org.tw.service;

import javax.ws.rs.PathParam;

public class Preprocess extends Thread {


	static int type, status ; 
	
	
	public Preprocess( int servicetype, int serviceStatus) throws Exception {
		type = servicetype ; 
		status = serviceStatus ;
	}

	@Override
	public void run() {
		SolrIndexing.main(type) ;
		
		//PreProcessing.main(status);    undo
	}

}
