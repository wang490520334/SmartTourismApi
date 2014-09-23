package iii.org.tw.controller;
import iii.org.tw.entity.SchedulingInfo;
import iii.org.tw.entity.SchedulingResult;
import iii.org.tw.model.Poi;
import iii.org.tw.model.SchedulingIn;
import iii.org.tw.service.SQLcommand;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Path("/scheduling")
@Api(value = "/scheduling", description = "Operations about scheduling")
@Produces({ "application/json" })
public class SchedulingController {

	private SQLcommand sc = new SQLcommand();
	public ArrayList<String> repeat = new ArrayList<String>(); //紀錄不重複景點
	private HashMap<String,Integer> citySort;
	
	private SchedulingResult src = new SchedulingResult();
	private HashMap<Integer,String> period = new HashMap<Integer,String>();
	
	private HashMap<String,String> catgory_list = new HashMap<String,String>(); //紀錄景點類別
	
	private String wherePrefence="";
	
	
	private void readCategory() throws SQLException
	{
		ResultSet rs = sc.select_table("SELECT * FROM category_list");
		while (rs.next())
			catgory_list.put(rs.getString("id"), rs.getString("category"));
		rs.close();
	}
	public SchedulingController() throws IOException, SQLException, ParseException
	{
		period.put(0, "morning");
		period.put(1, "afternoon");
		period.put(2, "night");
		
		String city[] = {"TW18","TW3","TW2","TW4","TW5","TW8","TW15"};
		citySort = new HashMap<String,Integer>();
		for (int i=0;i<city.length;i++)
			citySort.put(city[i],i);
		
		/*資料庫連線*/
		try
		{
			sc.connect_DB("smart_tourism");
		}
		catch (Exception e)
		{}
		
		readCategory();
	}

	@POST
	@ApiOperation(value = "Get the trip planning by query" , response = SchedulingResult.class)
	@ApiResponses(value = {
			  @ApiResponse(code = 500, message = "Internal Error"),
			  @ApiResponse(code = 404, message = "Not found"), 
			})
	public String startPlan(@ApiParam(required=true, value="Plan")SchedulingIn body, @ApiParam(value="[本平台使用者token(登入取得)]")@HeaderParam("userToken") String userToken) throws IOException, SQLException, ParseException 
	{

		/*
		 * 參數傳入處理
		 * */
		List<String> cityname = body.getCityList();
		//List<String> prefence = body.getPrefence();
		
		Date beginDate = sdf.parse(body.getStart());
		Date endDate = sdf.parse(body.getEnd());
	
//		for (int i=0;i<prefence.size()-1;i++)
//			wherePrefence+="theme = '" + prefence.get(i) + "' or ";
//		wherePrefence+="theme = '" + prefence.get(prefence.size()-1) + "'";
	
		//取得遊玩天數
		int days = (int)TimeUnit.MILLISECONDS.toDays(endDate.getTime() - beginDate.getTime())+1;     

		//檢查是否每天都有縣市
		ArrayList<String> tourCity = new ArrayList<String>();
		if (cityname.size()==0)
			tourCity = AutoRecommendCity(days);
		else
			tourCity = checkTour(days,cityname);
		
		//設定起始日期
		Calendar cal = Calendar.getInstance();
		cal.setTime(beginDate);

		//讀取score最高的景點
		String id[] = readTopPOI(tourCity,days); //OK
		String poiID[];
		
		System.out.println(tourCity);
		ArrayList<Poi[]> period;
		SchedulingInfo si;
		for (int i=0;i<id.length;i++) //top poi
		{
			si = new SchedulingInfo();
			si.setDate(sdf.format(cal.getTime()));
			cal.add(Calendar.DATE, 1);
			si.setCity(tourCity.get(i));
			
			poiID = getSimilar(id[i]); //找到user rating最相似的topN景點ID
			period = output_poi(poiID); //找到距離最相近的景點
			si.setMorning(Arrays.asList(period.get(0)));
			si.setAfternoon(Arrays.asList(period.get(1)));
			si.setNight(Arrays.asList(period.get(2)));
			
			src.addDayplan(si);
			
			System.out.println(id[i] + "=" + poiID[0] + "," + poiID[1] + "," + poiID[2]);
		}
		src.setRecommendList(getRecommendPOI(tourCity)); //側邊欄推薦景點清單

		Gson gson = new Gson();
		String json = gson.toJson(src);

		return json;
		
	}
	
	private ArrayList<String> AutoRecommendCity(int days)
	{
		//新北市,台北市,桃園縣,新竹縣,台中市,高雄市,花蓮縣
		ArrayList<String> newResult = new ArrayList<String>();
		String city[] = {"TW18","TW3","TW2","TW4","TW5","TW8","TW15"};
		
		int ran;
		String c;
		if (days<=2)
			newResult.add(city[(int)(Math.random()*7)]);
		else
		{
			//(int)(Math.random() * (Y-X+1)) + X;
			ran = (int)(Math.random()*(days-(days-2)))+(days-2);
			for (int i=0;i<ran;i++)
			{
				while (true)
				{
					c = city[(int)(Math.random()*7)];
					if (!newResult.contains(c))
					{
						newResult.add(c);
						break;
					}	
					else
						continue;
				}	
			}
		}
		return checkTour(days,newResult);
	}
	private ArrayList<String> checkTour(int days,List<String> city)
	{
		/*補足使用者未填滿的行程清單
		 * */
		city = sort(city);
		ArrayList<String> newResult = new ArrayList<String>();
		int avg = days / city.size();
		int loss = days % city.size();
		int ran;
		for (String c : city)
		{
			for (int i=0;i<avg;i++)
				newResult.add(c);
			//亂數決定是否可新增一天
			if (loss!=0)
			{
				ran = (int)(Math.random()*2);
				if (ran==1)
				{
					newResult.add(c);
					loss--;
				}
			}		
		}
			
		for (int j=0;j<loss;j++)
			newResult.add(newResult.get(newResult.size()-1));
		
		return newResult;
	}
	private ArrayList<String> sort(List<String> s)
	{
		ArrayList<String> newResult = new ArrayList<String>();
		HashMap<String,Integer> tmp = new HashMap<String,Integer>();
		for (String ss : s)
			tmp.put(ss, citySort.get(ss));
		List<Map.Entry<String, Integer>> list_Data = new ArrayList<Map.Entry<String, Integer>>(tmp.entrySet());
		
		Collections.sort(list_Data, new Comparator<Map.Entry<String, Integer>>(){
	            public int compare(Map.Entry<String, Integer> entry1,
	                               Map.Entry<String, Integer> entry2){
	                return (entry1.getValue() - entry2.getValue());
	            }
	        });
		for (Map.Entry<String, Integer> entry:list_Data) 
			newResult.add(entry.getKey());
		return newResult;
	}
	
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private ArrayList<Poi> getRecommendPOI(ArrayList<String> tour) throws SQLException
	{
		String where="";
//		for (int i=0;i<tour.size()-1;i++)
//			where += "city = '" + tour.get(i) + "' or ";
//		where += "city = '" + tour.get(tour.size()-1) + "'";
		String t;
		int count=1;
		ArrayList<Poi> recommend = new ArrayList<Poi>();
		Poi d[] = new Poi[5];
		int index=0;
		
		//ResultSet rs = sc.select_table("SELECT place_id FROM ipeen_gmap WHERE "+where+" ORDER BY score DESC");
		ResultSet rs = sc.select_table("SELECT place_id FROM ipeen_gmap ORDER BY score DESC");
		while (rs.next())
		{
			t = rs.getString("place_id");
			if (!repeat.contains(t))
			{
				d[index++] = new Poi(t,catgory_list.get(t));
//				d[index].setId(t);
//				d[index++].setType(catgory_list.get(t));
				count++;
			}
			if (count==6)
				break;
		}
		if (count<6)
		{
			//rs = sc.select_table("SELECT id FROM nearest WHERE "+where+" ORDER BY RAND()");
			rs = sc.select_table("SELECT id FROM nearest ORDER BY RAND()");
			while (rs.next())
			{
				t = rs.getString("id");
				if (!repeat.contains(t))
				{
					d[index++] = new Poi(t,catgory_list.get(t));
//					d[index].setId(t);
//					d[index++].setType(catgory_list.get(t));
					count++;
				}
				if (count==6)
					break;
			}
		}
		Collections.addAll(recommend, d);
		return recommend;
	}

	private ArrayList<Poi[]> output_poi(String poi[]) throws SQLException
	{
		ArrayList<Poi[]> period = new ArrayList<Poi[]>();
		for (String s : poi)
			period.add(getNearestPOI(s));		
		return period;
	}
	private Poi[] getNearestPOI(String id) throws SQLException
	{
		//找出最近的POI
		String tmp;
		Poi result[] = new Poi[3];
		
		result[0] = new Poi(id,catgory_list.get(id));
		tmp = findNearest(id);
		result[1] = new Poi(tmp,catgory_list.get(tmp));
		tmp = findNearest(result[1].getId());
		result[2] = new Poi(tmp,catgory_list.get(tmp));
		
		return result;
	}
	private String findNearest(String id) throws SQLException
	{
		boolean flag = true;
		String spl[];
		String result = "";
		
		ResultSet rs = sc.select_table("SELECT nearest_id FROM nearest WHERE id = '"+id+"'");
		
		if (rs.next())
		{
			spl = rs.getString("nearest_id").split(";");
			for (String s : spl)
			{
				if (!repeat.contains(s))
				{
					result = s;
					repeat.add(s);
					flag = false;
					break;
				}
			}
		}
		
		
		String tmp;
		//假如都遇到重複的
		if (flag)
		{
			rs = sc.select_table("SELECT arrival_id FROM googledirection_new WHERE id = '"+id+"' and (time <> -1 and time <> 1) and distance <> '1 公尺' ORDER BY time");
			while (rs.next())
			{
				tmp = rs.getString("arrival_id");
				if (!repeat.contains(tmp))
				{
					result = tmp;
					repeat.add(tmp);
					break;
				}
			}
		}
		
		return result;
		
	}
	private String[] getSimilar(String id) throws SQLException
	{
		/*
		 * 取得指定ID的user rating最相似的三個景點(包含指定ID), 若不足的則用距離指定ID最近的景點補充
		 * */
		
		String result[] = new String[3]; //三個時段
		int count=1;
		result[0] = id;
		
		try
		{
			ResultSet rs = sc.select_table("SELECT recommend_id FROM itembased WHERE id = '"+id+"'");
			String spl[];
			rs.next();
			spl = rs.getString("recommend_id").split(";");
			for (String s : spl)
			{
				if (count==3)
					break;
				if (!repeat.contains(s))
				{
					result[count++]=s;
					repeat.add(s);
				}		
			}
			rs.close();
		}
		catch (Exception e)
		{}
	
		if (count<3)
		{
			ResultSet rs = sc.select_table("SELECT arrival_id FROM googledirection_new WHERE id = '"+id+"' and time <> -1 ORDER BY time");
			while (rs.next())
			{
				if (!repeat.contains(rs.getString("arrival_id")))
				{
					result[count++] = rs.getString("arrival_id");
					repeat.add(rs.getString("arrival_id"));
				}
				if (count==3)
					break;
			}
			rs.close();
		}
		
		return result;
		
	}
	private class cityScore
	{
		ArrayList<String> top = new ArrayList<String>();
		int index=0;
		private void setIndex()
		{
			index++;
		}
	}
	private String[] readTopPOI(ArrayList<String> city,int days) throws SQLException
	{
		//讀取各縣市score景點(分數由高到低)
		
		HashMap<String,cityScore> cityRecord = new HashMap<String,cityScore>();
		String name;
		String id[] = new String[days];
		cityScore cs;
		ResultSet rs = sc.select_table("SELECT place_id,city FROM ipeen_gmap ORDER BY score DESC");
		while (rs.next())
		{
			name = rs.getString("city");
			if (!cityRecord.containsKey(name))
			{
				cs = new cityScore();
				cs.top.add(rs.getString("place_id"));
				cityRecord.put(name, cs);
			}
			else
				cityRecord.get(name).top.add(rs.getString("place_id"));
		}
		rs.close();
		
		int i=0;
		String pid;
		for (String c : city)
		{
			try
			{
				pid = cityRecord.get(c).top.get(cityRecord.get(c).index);
				id[i++] = pid;
				repeat.add(pid);
				cityRecord.get(c).setIndex();
			}
			catch (Exception e)
			{
				rs = sc.select_table("SELECT * FROM place_part_general WHERE county = '"+c+"' and category = 'Attraction' ORDER BY rand()");
				while (rs.next())
				{
					pid = rs.getString("place_id");
					if (!repeat.contains(pid))
					{
						id[i++] = pid;
						repeat.add(pid);
						break;
					}
						
				}
			}
			
		
		
		}
		//Collections.addAll(repeat, id); //存入不重複
		
		
		return id;
	}
}
