package iii.org.tw.controller;
import iii.org.tw.entity.Attraction;
import iii.org.tw.entity.SentimentResult;
import iii.org.tw.service.SQLcommand;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;


import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
@Path("/SentimentAnalysis")
@Api(value = "/SentimentAnalysis", description = "Operations about SentimentAnalysis")
@Produces({ "application/json" })
public class SentimentAnalysisController {

	public SentimentAnalysisController()
	{
		try
		{
			sc.connect_DB("sentiment_analysis");
		}
		catch (Exception e)
		{}
	}

	private int type=2; //2為以月為單位, 4為以週為單位
	
	private HashMap<String,Integer> dic = new HashMap<String,Integer>();
	private ArrayList<String> negation = new ArrayList<String>();
	private ArrayList<String> stopword = new ArrayList<String>();
	
	private SQLcommand sc = new SQLcommand();
	private SentimentResult src = new SentimentResult();
	
	@GET
	@Path("/Analysis")
	@ApiOperation(value = "Get sentiment scores and hot words by query", response = SentimentResult.class)
	public String main(@QueryParam("keyword") String keyword,
			@QueryParam("startDate") String startDate,
			@QueryParam("endDate") String endDate) throws SQLException
	{
//			src.startDate = "2013-05-01";
//			src.endDate = "2014-06-01";
//			src.keyword = "Acer";
		src.setStartDate(startDate);
		src.setEndDate(endDate);
		src.setKeyword(keyword);
		
		readOpinionWords();
		readNegationWords();
		readStopWords();
		int total = readData();
		findTags(total);
		
		Gson gson   = new Gson();
    	String json = gson.toJson(src);
    	return json;
	}
	private void readOpinionWords()
	{
		try
		{
			ResultSet rs = sc.select_table("SELECT * FROM OpinionWords");
			while (rs.next())
			{
				if (rs.getString("opinion").equals("+"))
					dic.put(rs.getString("term"), 1);
				else
					dic.put(rs.getString("term"), -1);
			}
			rs.close();
		}
		catch (Exception e)
		{}
		//System.out.print(dic);
		
	}
	private void readNegationWords()
	{
		try
		{
			try
			{
				ResultSet rs = sc.select_table("SELECT * FROM negations");
				while (rs.next())
				{
					negation.add(rs.getString("term"));
				}
				rs.close();
			}
			catch (Exception e)
			{}
		}
		catch (Exception e)
		{}
		
	}
//		
	private int readData()
	{
		String query="SELECT pos,neg,tags FROM sentiment_tourism WHERE name LIKE '%"+src.getKeyword()+"%'";
		
		Calendar c1 = Calendar.getInstance(),c2= Calendar.getInstance(),c3= Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String startT,endT;
		ResultSet rs;
		int pos=0,neg=0;
		try
		{			
			Date dt =sdf.parse(src.getStartDate());
			c1.setTime(dt);
			
			dt = sdf.parse(src.getEndDate());
			c2.setTime(dt);
		}
		catch (Exception e)
		{}
		int total=1;
		while (c1.before(c2))
		{
			c3.setTime(c1.getTime());
			c1.add(type,1);
			startT = sdf.format(c3.getTime());
			endT = sdf.format(c1.getTime());
			//System.out.println("Date : FROM " + startT + " To " + endT );
			pos=0;
			neg=0;
			try
			{
				rs = sc.select_table(query + "and (postTime >= '"+startT+"' and postTime < '"+endT+"')");
				while (rs.next())
				{
					pos+=rs.getInt("pos");
					neg+=rs.getInt("neg");
					getTags(rs.getString("tags"));
					total++;
				}
				rs.close();
				pair.remove("");
			}
			catch (Exception e)
			{e.printStackTrace();}
			
			
			src.getPositive().add(pos+"");
			src.getNegative().add(neg + "");
//				if (flag)
//				{
//					if (pos_before!=0)
//						src.positive.add((double)(pos-pos_before)/(double)pos_before+"");
//					else
//						src.positive.add("0");
//					if (neg_before!=0)
//						src.negative.add((double)(neg-neg_before)/(double)neg_before + "");
//					else
//						src.negative.add("0");
//				}
//				else
//				{
//					pos_before = pos;
//					neg_before = neg;
//					flag = true;
//				}
			
		}
		return total;
	}
	private void getTags(String content)
	{
		String spl[];
		ArrayList<String> repeat = new ArrayList<String>();
		spl = content.split(", ");
		info t;
		for (String s : spl)
		{
			if (!pair.containsKey(s))
			{
				t = new info();
				pair.put(s, t);
				repeat.add(s);
			}
			else
			{
				if (!repeat.contains(s))
				{
					pair.get(s).tf++;
					pair.get(s).df++;
					repeat.add(s);
				}
				else
				{
					pair.get(s).tf++;
				}
			}
		}
		pair.remove("");

	}
	private void readStopWords()
	{
		try
		{
			ResultSet rs = sc.select_table("SELECT term FROM stopword");
			
			while (rs.next())
			{
				stopword.add(rs.getString("term"));
			}
			rs.close();
		}
		catch (Exception e)
		{}
		
	}
	
	
	private HashMap<String,info> pair = new HashMap<String,info>();
	//private Pattern pattern = Pattern.compile("　.{0,5}\\([NV][AJBHCKab].?\\)　.{0,5}\\([NV][AJBHCKab].?\\)　?");
	private class info
	{
		double tf=1;
		double df=1;
	}

	private void findTags(int total)
	{
		HashMap<String,Integer> tmp = new HashMap<String,Integer>();
		for (String s : pair.keySet())
		{
			tmp.put(s, (int)(pair.get(s).tf * Math.log10(total/pair.get(s).df)*100000));
			//System.out.println(s + "=" + pair.get(s).tf + "=" + pair.get(s).df);
		}
		int count=1;
		List<Map.Entry<String, Integer>> rank = sort(tmp);
		for (Map.Entry<String, Integer> entry:rank) 
		{
			if (check(entry.getKey()))
				continue;
			if (entry.getValue()<2)
				break;
			if (count==6)
				break;
			src.getTags().add(entry.getKey());
			count++;
		}
	}
	private boolean check(String term)
	{
		for (String s : stopword)
		{
			if (term.contains(s))
				return true;
		}
		return false;
	}
	private List<Map.Entry<String, Integer>> sort(HashMap<String,Integer> tmp)
	{
		List<Map.Entry<String, Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(tmp.entrySet());
		Collections.sort(list_Data, new Comparator<Map.Entry<String, Integer>>()
				{
					public int compare(Map.Entry<String, Integer> entry1,Map.Entry<String, Integer> entry2)
					{
						return (entry2.getValue().compareTo(entry1.getValue()));
					}
			    }
				);
		return list_Data;
	}

}

	

