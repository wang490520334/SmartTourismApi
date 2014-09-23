package iii.org.tw.service;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import net.sf.json.JSONException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import com.chenlb.mmseg4j.ComplexSeg;
import com.chenlb.mmseg4j.Dictionary;
import com.chenlb.mmseg4j.MMSeg;
import com.chenlb.mmseg4j.Seg;
import com.chenlb.mmseg4j.Word;

public class InputSeg {
	protected Dictionary dic;
	
	public InputSeg() {
		//System.setProperty("mmseg.dic.path", "C:/workspace/SmartTourism/src/data/");	//�o�̥i�H��w�ۭq��w
			System.setProperty("mmseg.dic.path", "/usr/local/solr-4.4.0/example/solr/dic");	//�o�̥i�H��w�ۭq��w

		dic = Dictionary.getInstance();
	}

	protected Seg getSeg() {
			return new ComplexSeg(dic);
			//return new SimpleSeg(dic);
			//			return new MaxWordSeg(dic);
	}
	
	
	public String segWords(String txt, String wordSpilt) throws IOException {
		Reader input = new StringReader(txt);
		StringBuilder sb = new StringBuilder();
		Seg seg = getSeg();
		MMSeg mmSeg = new MMSeg(input, seg);
		Word word = null;
		boolean first = true;
		while((word=mmSeg.next())!=null) {
			if(!first) {
				sb.append(wordSpilt);
			}
			String w = word.getString();
			sb.append(w);
			first = false;		
		}
		return sb.toString();
	}


protected String run(String content) throws IOException, JSONException {
	
	String SegResults = segWords(content, ",");
   	logger.info("SegResults={}", SegResults);        	

	return SegResults;
}
final static Logger logger = LoggerFactory.getLogger(InputSeg.class);


	public static String main(String content) throws IOException, JSONException {		
		return new InputSeg().run(content);

	}
	
}