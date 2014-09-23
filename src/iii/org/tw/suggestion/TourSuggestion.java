package iii.org.tw.suggestion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Collections;
import java.util.TimeZone;

import iii.org.tw.util.DbUtil;

public class TourSuggestion {

	/**
	 * @param args
	 */
	//參數設定
	//--------------------------------------------------------------
	static private final int    themeFactor              = 15;                // theme參數，可調
	static private final int    distanceFactor           = 12;                // 距離參數，景點集中就調大，景點分散就調小。Ex.台南 20 因為景點近，台北12 因為景點較分散
	static private final int    maxSuggestedNumber       = 7;                 // 最多推薦景點數
	static private final String defaultAttraction        = "1_02_2775_5977";  // 沒景點時，就推薦的地方。台南兌悅門"E05" 台北誠品書店"1_02_2775_5977"" 
	//--------------------------------------------------------------
	
	static private int          totalPlaces = 50000;              // 景點數目，50000只是初步任意選擇
	static private short[][]    duration;                         // 景點與景點的距離
	static private int[]        numOfClassifiedPlaces;            // 景點類別數目
	static private String[]     placesids;                        // 景點id
	static private String[]     placeThemeSet;                    // 景點Theme集合             
	static private PlaceInfo[]  places;                           // 景點資訊
	static private java.util.ArrayList<OpenData>[] open_times;    // 景點開放時間
																
	static private boolean initedTotalPlaces = false;             // 是否初始化
	static private boolean initedPlaces      = false;
	static private boolean initedOpentime    = false;
	static private boolean initDuration      = false;
	
	static private float maxDuration;                             // 兩景點間最長距離
	static private float minDuration;                             // 兩景點間最短距離
	
	// 輸入所有初始化資料
	//--------------------------------------------------------------
	static public void initAllData(){         // 初始化資料
		setNumOfTotalPlaces();
		setPlaces();
		setOpentime();
		setDuration();
	}
	
	// 初始化點到點時間
	//--------------------------------------------------------------
	static private void setDuration(){
		String [] placeId = placesids;
		int maxDuration = Integer.MIN_VALUE, minDuration = Integer.MAX_VALUE;
		int [] duration = new int [placeId.length * placeId.length];
		
		try{		
			Connection dbConnection = DbUtil.getConnection();	
			for(int i=0; i<placeId.length; i++){
				for(int j=0; j<placeId.length; j++){
					if(i == j)
						duration[i * placeId.length + j] = 0;
					else{
						String selectSQL_1 = "SELECT `Duration` FROM `place_route` WHERE `Place_Id_S` = '" + placeId[i] + "' AND `Place_Id_D` = '" + placeId[j] + "'";
						Statement statement_1 = dbConnection.createStatement();
						ResultSet dbResults_1 = statement_1.executeQuery(selectSQL_1);
						dbResults_1.next();
						duration[i * placeId.length + j] = dbResults_1.getInt("Duration");
						if(duration[i * placeId.length + j] > maxDuration)
							maxDuration = duration[i * placeId.length + j];
						if(duration[i * placeId.length + j] < minDuration)
							minDuration = duration[i * placeId.length + j];
					}
					
				}
			}
			initDuration(duration, maxDuration, minDuration);
		}
		catch ( Exception e) {
			// TODO Auto-generated catch block
			
			e.printStackTrace();
		}	
	}
	
	// 初始化開放時間
	//--------------------------------------------------------------
	static private void setOpentime(){
		int numOfOpentime = 0;
		String selectSQL_1 = "SELECT COUNT(*) FROM `place_open_time`";
		String [] placeId = placesids;
		
		try{		
			Connection dbConnection = DbUtil.getConnection();	
			
			Statement statement_1 = dbConnection.createStatement();
			ResultSet dbResults_1 = statement_1.executeQuery(selectSQL_1);
			dbResults_1.next();
			numOfOpentime = dbResults_1.getInt(1);
			
			int[] index        = new int    [numOfOpentime];
			int[] day          = new int    [numOfOpentime];
			String[] time1o    = new String [numOfOpentime];
			String[] time1c    = new String [numOfOpentime];
			String[] time2o    = new String [numOfOpentime];
			String[] time2c    = new String [numOfOpentime];
			
			int c = 0;
			for(int i=0;i < placeId.length; i++){
				String selectSQL_2 = "SELECT `Open_Time_AM`, `Close_Time_AM`, `Open_Time_PM`, `Close_Time_PM` , `Exception` "
						+ "FROM `place_open_time`"
						+ "WHERE Place_Id = '" + placeId[i] + "'"; 
				Statement statement_2 = dbConnection.createStatement();
				ResultSet dbResults_2 = statement_2.executeQuery(selectSQL_2);
				while(dbResults_2.next()){
					index[c]  = i;
					day[c]    = dbResults_2.getInt("Exception");
					time1o[c] = dbResults_2.getString("Open_Time_AM");
					time1c[c] = dbResults_2.getString("Close_Time_AM");
					time2o[c] = dbResults_2.getString("Open_Time_PM");
					time2c[c] = dbResults_2.getString("Close_Time_PM");
					c++;
				}
				dbResults_2.close();
				statement_2.close();
			}
//				System.out.println(placeId[i]);
			dbResults_1.close();
			statement_1.close();
			dbConnection.close();
			
			initOpentime(index, day, time1o, time1c, time2o, time2c);
		}
		catch ( Exception e) {
				// TODO Auto-generated catch block
				
			e.printStackTrace();
		}		
		
	}
	
	// 初始化景點資訊
	//--------------------------------------------------------------
	static private void setPlaces(){
		int numOfTypes = 0;
		int numberOfPlace = getNumOfTotalPlaces();
		String selectSQL_1 = "SELECT `Theme` FROM `theme_type` WHERE `Type` = 0 ORDER BY `Theme`";
		String selectSQL_2 = "SELECT `Place_Id`, `Stay_Time`, `Px`, `Py` FROM `place_part_general` ORDER BY `Place_Id`";
		String [] themeSet = null;
		
		try{		
			Connection dbConnection = DbUtil.getConnection();	
			
			Statement statement_1 = dbConnection.createStatement();
			ResultSet dbResults_1 = statement_1.executeQuery(selectSQL_1);
			dbResults_1.last();
			numOfTypes = dbResults_1.getRow();  //景點類別數目
			dbResults_1.beforeFirst();
			themeSet = new String [numOfTypes];
			for(int i=0; dbResults_1.next(); i++)
				themeSet[i] = dbResults_1.getString("Theme");
			dbResults_1.close();
			statement_1.close();
			
			byte  [] types    = new byte   [numberOfPlace * numOfTypes];
			String[] placeId  = new String [numberOfPlace];
			String[] stayTime = new String [numberOfPlace];
			double[] px       = new double [numberOfPlace];  
			double[] py       = new double [numberOfPlace];
			
			Statement statement_2 = dbConnection.createStatement();
			ResultSet dbResults_2 = statement_2.executeQuery(selectSQL_2);
			
			for(int i = 0; dbResults_2.next(); i++){
				placeId[i]   = dbResults_2.getString("Place_Id");		
				stayTime[i]  = dbResults_2.getString("Stay_Time");
				px[i]        = dbResults_2.getDouble("Px");
				py[i]        = dbResults_2.getDouble("Py");
				for(int j=0; j<numOfTypes; j++){
					types[i * numOfTypes + j] = '0';
				}	
				String selectSQL_3 = "SELECT `Theme_Id` FROM `item_theme_en_us` WHERE `Theme_Type` = 0 AND `Item_Id` = '" + placeId[i] + "'";
				Statement statement_3 = dbConnection.createStatement();
				ResultSet dbResults_3 = statement_3.executeQuery(selectSQL_3);
				while(dbResults_3.next()){
					String themeId = dbResults_3.getString("Theme_Id");
					for(int j=0; j<themeSet.length; j++){
						if(themeId.equals(themeSet[j]))
							types[i * numOfTypes + j] = '1';
					}
				}
				dbResults_3.close();
				statement_3.close();
			}
			dbResults_2.close();
			statement_2.close();
			dbConnection.close();
//			for(int i=0; i< themeSet.length;i++)
//				System.out.println(themeSet[i]);
			initPlaces(types, numOfTypes, placeId, themeSet, stayTime, px, py);
		}
		catch ( Exception e) {
				// TODO Auto-generated catch block
				
			e.printStackTrace();
		}	
	}
	
	// 初始化景點數目
	//--------------------------------------------------------------
	static private void setNumOfTotalPlaces(){	
		
		String selectSQL_1 = "SELECT Count(*) FROM `place_part_general`";
		
		try{		
			Connection dbConnection = DbUtil.getConnection();

			Statement statement_1 = dbConnection.createStatement();
			ResultSet dbResults_1= statement_1.executeQuery(selectSQL_1);
			
			dbResults_1.next();
			initNumOfTotalPlaces(dbResults_1.getInt(1));  // 呼叫API，寫入景點數目

			dbResults_1.close();
			statement_1.close();
			dbConnection.close();
		}
		catch ( Exception e) {
				// TODO Auto-generated catch block
				
			e.printStackTrace();
		}	
	}	
	//--------------------------------------------------------------
		
	
	
	// 開放取得相關資料
	//--------------------------------------------------------------
	static public int getNumOfTotalPlaces() {
		return totalPlaces;
	}

	static public String [] getPlacesids () {
		return placesids.clone();
	}
	
	static public PlaceInfo [] getPlaces () {	
		return places.clone();
	}
	
	static public String [] getPlaceThemeSet () {
		return placeThemeSet.clone();
	}
	
	static public int [] getDuration () {
		int [] tempDuration = new int [totalPlaces*totalPlaces];
		
		for (int i = 0; i < totalPlaces; i++) {
			for (int j = 0; j < totalPlaces; j++) {	
				tempDuration[i*totalPlaces+j] = (int) duration[i][j];
			}
		}				
		return tempDuration;
	}
	
	static public boolean isNotInitial () {
		if (!initedTotalPlaces || !initedPlaces || !initedOpentime || !initDuration)
			return true;
		else 
			return false;
	}
	
	static public java.util.ArrayList<OpenData> [] getOpentimes () {
		return open_times;
	}
	// 開放取得相關資料
	//--------------------------------------------------------------
	
	
	//初始化所有資料API
	//--------------------------------------------------------------
	static public void initPlaces(byte[] inTypes, int inNumOfTypes,
			String[] inPlacesIds, String[] inNameC, 
			String[] inNameE, String[] inPlaceThemeSet, String[] inStay_time,
			double[] inPx, double[] inPy) {
		
		initPlaces(inTypes, inNumOfTypes, inPlacesIds, null, null, inStay_time, inPx, inPy);
		initPlaceThemeSet(inPlaceThemeSet);	
	
	}
	
	static public void initPlaces(byte[] inTypes, int inNumOfTypes,
			String[] inPlacesIds, String[] inPlaceThemeSet, String[] inStay_time,
			double[] inPx, double[] inPy) {
		
		initPlaces(inTypes, inNumOfTypes, inPlacesIds, null, null, inStay_time, inPx, inPy);
		initPlaceThemeSet(inPlaceThemeSet);
	
	}
	
	static public void initPlaceThemeSet(String [] inPlaceThemeSet){
		placeThemeSet = inPlaceThemeSet.clone();
	}
	
	static public void initNumOfTotalPlaces(int inTotalPlaces) {
		totalPlaces       = inTotalPlaces;
		initedTotalPlaces = true;
	}
	
	static public void initPlaces(byte[] inTypes, int inNumOfTypes,
			String[] inPlacesIds, String[] inNameC, 
			String[] inNameE, String[] inStay_time,
			double[] inPx, double[] inPy) {

		if (!initedTotalPlaces)
			return;

		places    = new PlaceInfo[totalPlaces];
		placesids = new String[totalPlaces];
			
		for (int i = 0; i < totalPlaces; i++) {
			places[i] = new PlaceInfo(inNumOfTypes);

			for (int j = 0; j < PlaceInfo.numOfTypes; j++) {
				places[i].types[j] = inTypes[i * PlaceInfo.numOfTypes + j];
			}
			
			placesids[i]        = inPlacesIds[i];
			places[i].stay_time = inStay_time[i];
			places[i].px        = inPx[i];
			places[i].py        = inPy[i];
		}

		// -----initNumOfClassifiedPlaces

		numOfClassifiedPlaces = new int[PlaceInfo.numOfTypes + 1];

		for (int i = 0; i <= PlaceInfo.numOfTypes; i++)
			numOfClassifiedPlaces[i] = 0;                        // 多一個零類別在最前面

		for (int i = 0; i < totalPlaces; i++) {
			for (int j = 0; j < PlaceInfo.numOfTypes; j++) {
				if (places[i].types[j] == '1')
					numOfClassifiedPlaces[j + 1]++;
			}
		}
		initedPlaces = true;
	}
	
	static public void initDuration(int[] inDuration, int inMaxDuration, int inMinDuration) {
		
		if (!initedTotalPlaces)
			return;
		
		duration = new short[totalPlaces][totalPlaces];
		
		maxDuration = (float) inMaxDuration;
		minDuration = (float) inMinDuration;
		for (int i = 0; i < totalPlaces; i++) {
			for (int j = 0; j < totalPlaces; j++) {	
				duration[i][j] = (short) Math.round(((float) inDuration[i*totalPlaces+j] - minDuration) 
								/ (maxDuration - minDuration) * 255.0f);
			}
		}
		initDuration =true;
	}
		
	@SuppressWarnings("unchecked")
	static public void initOpentime(int[] idx, int[] inDay, 
			String[] inTime1o, String[] inTime1c, 
			String[] inTime2o, String[] inTime2c) {

		if (!initedTotalPlaces)
			return;

		int numOfOpentime = idx.length;

		// ---------------------
		open_times = new java.util.ArrayList[totalPlaces];
		for (int i = 0; i < totalPlaces; i++)
			open_times[i] = new java.util.ArrayList<OpenData>();

		for (int i = 0; i < numOfOpentime; i++) {
			OpenData times = new OpenData();
			times.day    = inDay[i];
			times.time1o = inTime1o[i];
			times.time1c = inTime1c[i];
			times.time2o = inTime2o[i];
			times.time2c = inTime2c[i];
			open_times[idx[i]].add(times);
		}

		initedOpentime = true;
	}
	// 初始化所有資料
	//--------------------------------------------------------------
	
	
	static private void StringExplode(String str, String separator, // 分割字串
			java.util.ArrayList<String> results) {
		int found;
		found = str.indexOf(separator);
		while (found != -1) {
			if (found > 0) {
				results.add(str.substring(0, found));
			}
			str   = str.substring(found + 1);
			found = str.indexOf(separator);
		}
		if (str.length() > 0) {
			results.add(str); // 最後一個substring
		}
	}

	static private java.util.ArrayList<Store> sci(OpenData od) {
		java.util.ArrayList<Store> bk = new java.util.ArrayList<Store>();
		java.util.ArrayList<String> ts = new java.util.ArrayList<String>();

		/* StringExplode("12:00:00",":",&ts); */
		if (od.time1o.compareTo(od.time1c) == 0) {             // 相同代表不開門 00:00:00
			Store s = new Store();
			StringExplode(od.time2o, ":", ts);
			s.start = Integer.parseInt(ts.get(0) + ts.get(1)); // 轉整數 比大小用
			ts.clear();
			StringExplode(od.time2c, ":", ts);
			s.end   = Integer.parseInt(ts.get(0) + ts.get(1));
			ts.clear();
			s.stt   = od.time2o;
			s.endt  = od.time2c;
			bk.add(s);
		} else if (od.time2o.compareTo(od.time2c) == 0) {
			Store s = new Store();
			StringExplode(od.time1o, ":", ts);
			s.start = Integer.parseInt(ts.get(0) + ts.get(1));
			ts.clear();
			StringExplode(od.time1c, ":", ts);
			s.end   = Integer.parseInt(ts.get(0) + ts.get(1));
			ts.clear();
			s.stt   = od.time1o;
			s.endt  = od.time1c;
			bk.add(s);
		} else if (od.time2o.compareTo(od.time1c) == 0) {
			Store s = new Store();
			StringExplode(od.time1o, ":", ts);
			s.start = Integer.parseInt(ts.get(0) + ts.get(1));
			ts.clear();
			StringExplode(od.time2c, ":", ts);
			s.end   = Integer.parseInt(ts.get(0) + ts.get(1));
			ts.clear();
			s.stt   = od.time1o;
			s.endt  = od.time2c;
			bk.add(s);
		} else {
			Store s = new Store();
			StringExplode(od.time1o, ":", ts);
			s.start = Integer.parseInt(ts.get(0) + ts.get(1));
			ts.clear();
			StringExplode(od.time1c, ":", ts);
			s.end   = Integer.parseInt(ts.get(0) + ts.get(1));
			ts.clear();
			s.stt   = od.time1o;
			s.endt  = od.time1c;
			bk.add(s);
			Store s1 = new Store();
			StringExplode(od.time2o, ":", ts);
			s1.start = Integer.parseInt(ts.get(0) + ts.get(1));
			ts.clear();
			StringExplode(od.time2c, ":", ts);
			s1.end   = Integer.parseInt(ts.get(0) + ts.get(1));
			ts.clear();
			s1.stt   = od.time2o;
			s1.endt  = od.time2c;
			bk.add(s1);
		}
		return bk;
	}

	static private int minusf(String eh, String em, Store swt) {
		java.util.ArrayList<String> usrt = new java.util.ArrayList<String>();
		StringExplode(swt.endt, ":", usrt);

		int frone = (Integer.parseInt(usrt.get(0)) - Integer.parseInt(eh)) * 60; // 轉為分鐘

		int rte   = frone + (Integer.parseInt(usrt.get(1)) - Integer.parseInt(em));

		return rte;
	}

	static private String trimp(String st) {               // 拿掉秒12:23:45 => 12:23
		java.util.ArrayList<String> ste = new java.util.ArrayList<String>();
		StringExplode(st, ":", ste);

		return ste.get(0) + ":" + ste.get(1);

	}

	static private int testifopened(long stime, long endtime, // 測試 stime 到 endtime 是否開門 
			java.util.ArrayList<OpenData> all) {
		java.util.ArrayList<OpenData> holiday = new java.util.ArrayList<OpenData>();
		java.util.ArrayList<OpenData> openday = new java.util.ArrayList<OpenData>();
		java.util.ArrayList<OpenData> genday  = new java.util.ArrayList<OpenData>();

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		// opendata pd = new opendata();
		int uday;
		String ush = new String();
		String ueh = new String();
		String usm = new String();
		String uem = new String();
		int ust;
		int uend;

		for (int ai = 0; ai < all.size(); ai++) {
			all.get(ai).time1o = trimp(all.get(ai).time1o); // 拿掉秒
			all.get(ai).time1c = trimp(all.get(ai).time1c);
			all.get(ai).time2o = trimp(all.get(ai).time2o);
			all.get(ai).time2c = trimp(all.get(ai).time2c);
		}

		stime   += 28800;         // +8小時(台灣時區)
		endtime += 28800;

		calendar.setTimeInMillis(stime * 1000); //
		uday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
		ush  = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
		usm  = String.format("%02d", calendar.get(Calendar.MINUTE));

		calendar.setTimeInMillis(endtime * 1000);
		if (calendar.get(Calendar.DAY_OF_WEEK) - 1 != uday)
			ueh = String
					.format("%02d", 24 + calendar.get(Calendar.HOUR_OF_DAY)); // �j��
		else
			ueh = String.format("%02d", calendar.get(Calendar.HOUR_OF_DAY));
		uem = String.format("%02d", calendar.get(Calendar.MINUTE));

		String combs = new String();
		combs = ush + usm;
		ust   = Integer.parseInt(combs);     // 18:00 -> 1800

		String combe = new String();
		combe = ueh + uem;
		uend  = Integer.parseInt(combe);

		if (uday == 0)
			uday = 7; // 星期天

		for (int i = 0; i < all.size(); i++) {
			if (all.get(i).day < 8 && all.get(i).time1o.compareTo("00:00") == 0
					&& all.get(i).time1c.compareTo("00:00") == 0
					&& all.get(i).time2o.compareTo("00:00") == 0
					&& all.get(i).time2c.compareTo("00:00") == 0)
				holiday.add(all.get(i));     // 沒開
			else if (all.get(i).day > 8) {   // 開放式空間
				return 10000;
			} 
			else if (all.get(i).day == 0) {
				genday.add(all.get(i));
			} 
			else {
				openday.add(all.get(i));
			}
		}
		if (holiday.size() != 0) {
			for (int i = 0; i < holiday.size(); i++) {
				if (holiday.get(i).day == uday) {
					return -1;
				}
			}
		}

		if (openday.size() != 0) {
			for (int i = 0; i < openday.size(); i++) {
				if (openday.get(i).day == uday) {
					java.util.ArrayList<Store> ao = new java.util.ArrayList<Store>();
					ao = sci(openday.get(i));
					for (int n = 0; n < ao.size(); n++) {
						int f1 = ao.get(n).start;
						int f2 = ao.get(n).end;
						if ((ust - f1) >= 0 && (f2 - uend) >= 0) {

							return minusf(ueh, uem, ao.get(n)); // 回傳有
						}
					}
				}
			}
		}
		java.util.ArrayList<Store> aoi = new java.util.ArrayList<Store>();
		if (genday.size() != 0) {
			aoi = sci(genday.get(0));
			for (int n = 0; n < aoi.size(); n++) {
				int f1 = aoi.get(n).start;
				int f2 = aoi.get(n).end;
				if ((ust - f1) >= 0 && (f2 - uend) >= 0) {
					return minusf(ueh, uem, aoi.get(n)); // 回傳有
				}
			}
		}
		
		return -1;
		// if no return value
	}



	static private Result plan(java.util.ArrayList<Integer> ids_old,         // 順序推薦;
			long starttime, long endtime, int startplace)		             // starttime & endtime (秒)
	// must put encoded place index
	{	
		java.util.ArrayList<Integer> ids = new java.util.ArrayList<Integer>(); // 所有被選上的景點
		for (int i = 0; i < ids_old.size(); i++)
			ids.add(ids_old.get(i));
		Result r = new Result();
		if ((starttime % 1800) != 0) {                                   // half hour 檢查是不是半小時丟進來
			starttime -= (starttime % 1800);
			starttime += 1800;                                           // 無條件進入
		}
		
		
		r.start_time = starttime;
		java.util.ArrayList<String> re = new java.util.ArrayList<String>();
		long realend = 0;
		while (ids.size() != 0) {
			int numofplaces      = ids.size();
			int minidx           = Integer.MAX_VALUE;
			int minduration      = Integer.MAX_VALUE;
			int trueMaxDuration  = (int) maxDuration;
			int tureMinDuration  = (int) minDuration;

			for (int i = 0; i < numofplaces; i++) {
				int true_duration = (int) duration[startplace][ids.get(i)] 
						* (trueMaxDuration - tureMinDuration) / 255 + tureMinDuration;		
				int remain = testifopened(
						starttime + true_duration,
						starttime
								+ true_duration
								+ (int) (Double.parseDouble(places[ids.get(i)].stay_time) * 3600.0),
						open_times[ids.get(i)]) * 60; // 有沒有開

				if (remain >= 0) {
					if (remain < 2 * 3600)            // 2 hours 
					{
						minidx      = i;
						minduration = true_duration;
						break;
					} else if (true_duration < minduration) {
						minidx      = i;
						minduration = true_duration;
					}
				}
			}
			if (minidx == Integer.MAX_VALUE) { // 沒找到合適的
				starttime += 1800;
				if (ids.size() == ids_old.size())
					r.start_time = starttime - (starttime % 1800);

				if (starttime > endtime)
					break;
				else
					continue;
			}
			startplace = ids.get(minidx);
			int visittime = (int) (Double
					.parseDouble(places[ids.get(minidx)].stay_time) * 3600.0); // 換成秒
			if (endtime > starttime + minduration + visittime) {
				re.add(placesids[startplace]);
				realend = starttime = starttime + minduration + visittime;
				if (re.size() == maxSuggestedNumber) // 超過maxSuggestedNumber數目就不要再推薦
					break;
			}
			// starttime = starttime + minduration + visittime;
			ids.remove(minidx);
		}

		if ((realend % 1800) != 0) { // half hour
			realend -= (realend % 1800);
			realend += 1800;
		}

		r.attraction_order = re;
		r.end_time         = realend;
		return r;
	}

	static private int nearest(double x, double y) {
		int placeidx       = 0;
		double minDistance = Math.abs(x - places[0].px)
				+ Math.abs(y - places[0].py);

		for (int i = 0; i < places.length; i++) {
			double currentDistance = Math.abs(x - places[i].px)
					+ Math.abs(y - places[i].py);
			if (currentDistance < minDistance) {         // ---------------------------
				placeidx    = i;
				minDistance = currentDistance;           // Math.abs(x - places[i].px) +
												         // Math.abs(y - places[i].py);
			}

		}
		return placeidx;
	}

	@SuppressWarnings("unchecked")
	static private java.util.ArrayList<java.util.ArrayList<PlaceScore>> gensuggestion(
			// 取得推薦景點
			double X, double Y, java.util.ArrayList<Integer> Themes,
			java.util.ArrayList<String> Pockets,
			java.util.ArrayList<String> Whitelist,
			java.util.ArrayList<String> Blacklist) {

		int consideredPlaces = totalPlaces; 

		int startplace = nearest(X, Y);

		float[] placescore1 = new float[consideredPlaces]; 
		float[] placescore2 = new float[consideredPlaces]; 

		float[] themescore  = new float[PlaceInfo.numOfTypes + 1];
		float[] themestep   = new float[PlaceInfo.numOfTypes + 1];

		for (int i = 0; i <= PlaceInfo.numOfTypes; i++) {

			themescore[i] = 0.0f;

			if (numOfClassifiedPlaces[i] == 0)
				themestep[i] = 0.0f;
			else
				themestep[i] = 1.0f / numOfClassifiedPlaces[i];
		}
		if(Pockets != null) {
			for (int i = 0; i < Pockets.size(); i++) {
			int idx = java.util.Arrays.binarySearch(placesids, Pockets.get(i)); 
				for (int j = 0; j < PlaceInfo.numOfTypes; j++) {
					if (places[idx].types[j] == '1')
						themescore[j + 1] += themestep[j + 1];
				}
			}
		}
		
		float max = 0.0F;
		for (int i = 1; i <= PlaceInfo.numOfTypes; i++)
			if (max < themescore[i])
				max = themescore[i];
		if (max >= 0.00001)
			for (int i = 1; i <= PlaceInfo.numOfTypes; i++)
				themescore[i] /= max;
		else
			// No Pocket
			for (int i = 1; i <= PlaceInfo.numOfTypes; i++)
				themescore[i] = 0.5F;
		if (Themes != null && Themes.size() != PlaceInfo.numOfTypes) { // 自己設定的偏好
			for (int i = 0; i < Themes.size(); i++)
				themescore[Themes.get(i)] = 1.0F; 
		}

		float minduration = 10000000.0F;            // FLT_MAX ??
		for (int i = 0; i < consideredPlaces; i++) {
			if (startplace == i)
				placescore2[i] = 10000000.0F;
			else
				placescore2[i] = (float) duration[startplace][i];
			if (minduration > placescore2[i])
				minduration = placescore2[i];      // 最短距離
			float maxscore = 0F;
			for (int j = 0; j < PlaceInfo.numOfTypes; j++) {
				if (places[i].types[j] == '1') {
					if (maxscore < themescore[j + 1])
						maxscore = themescore[j + 1];
				}
			}
			placescore1[i] = maxscore;           // 初步景點推薦分
		}
		java.util.ArrayList<PlaceScore> suggestion1 = new java.util.ArrayList<PlaceScore>( 
				consideredPlaces);
		java.util.ArrayList<PlaceScore> suggestion2 = new java.util.ArrayList<PlaceScore>(
				consideredPlaces);
		for (int i = 0; i < consideredPlaces; i++) { 
			suggestion1.add(new PlaceScore());
			suggestion2.add(new PlaceScore());
		}
		for (int i = 0; i < consideredPlaces; i++) {
			suggestion1.get(i).index = suggestion2.get(i).index = i;
			placescore2[i] = (float) Math.exp((minduration-placescore2[i])/20.0);   //台南 30 台北20
			suggestion1.get(i).totalscore = 0.9f * placescore1[i] + 0.1f
					* placescore2[i];
			suggestion2.get(i).totalscore = 0.1f * placescore1[i] + 0.9f
					* placescore2[i];
		}
		if (Whitelist != null) {                             // 特別白名單
			for (int i = 0; i < Whitelist.size(); i++) {
				int idx = java.util.Arrays.binarySearch(placesids,
						Whitelist.get(i));
				suggestion1.get(idx).totalscore += 0.5f;
				suggestion2.get(idx).totalscore += 0.5f;
			}
		}
		if (Blacklist != null) {                            // 特別黑名單
			for (int i = 0; i < Blacklist.size(); i++)      
			{
				int idx = java.util.Arrays.binarySearch(placesids,
						Blacklist.get(i));
				suggestion1.get(idx).totalscore = 0f;
				suggestion2.get(idx).totalscore = 0f;
			}
		}
		Collections.sort(suggestion1);
		Collections.sort(suggestion2);
		java.util.ArrayList<java.util.ArrayList<PlaceScore>> re = new java.util.ArrayList<java.util.ArrayList<PlaceScore>>();
		re.add(suggestion1);
		re.add(suggestion2);
		return re;
	}

	static public java.util.ArrayList<Result> SmartTour(double X, double Y,
			long StartTime, long EndTime, java.util.ArrayList<Integer> Themes,
			java.util.ArrayList<String> Pockets) {
		return SmartTour(X, Y, StartTime, EndTime, Themes, Pockets, null, null);
	}
	
	static public java.util.ArrayList<Result> SmartTour(
			double X,
			double Y,                                                      // 指定開始時間的推薦景點
			long StartTime, long EndTime, java.util.ArrayList<Integer> Themes,
			java.util.ArrayList<String> Pockets,
			java.util.ArrayList<String> Whitelist,
			java.util.ArrayList<String> Blacklist) {

		if (isNotInitial ()) {
			java.util.ArrayList<Result> err = new java.util.ArrayList<Result>();
			err = null;
			return err;	
		}

		
		java.util.ArrayList<java.util.ArrayList<PlaceScore>> sugs = gensuggestion(
				X, Y, Themes, Pockets, Whitelist, Blacklist);
		java.util.ArrayList<Integer> sug1 = new java.util.ArrayList<Integer>();
		java.util.ArrayList<Integer> sug2 = new java.util.ArrayList<Integer>();
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 12; i++) {
			int rand1 = (int) ((cal.getTimeInMillis() + Math.round(Math.random() * 12 * i)) % themeFactor);    //theme參數，可以調
			sug1.add(new Integer((int) sugs.get(0).get(rand1).index));
			sugs.get(0).remove(rand1);
			int rand2 = (int) ((cal.getTimeInMillis() + Math.round(Math.random() * 12 * i)) % distanceFactor); // 距離參數
			sug2.add(new Integer((int) sugs.get(1).get(rand2).index));
			sugs.get(1).remove(rand2);
		}
 
		java.util.ArrayList<Result> r = new java.util.ArrayList<Result>();
		Result r1 = plan(sug1, StartTime, EndTime, nearest(X, Y));
		Result r2 = plan(sug2, StartTime, EndTime, nearest(X, Y));

		boolean isthesame = false;
		if (r1.attraction_order.size() == r2.attraction_order.size()) {
			if (r1.attraction_order.size() == 0)
				isthesame = true;
			for (int i = 0; i < r1.attraction_order.size(); i++)
				// 檢查r1和r2是否不同
				if (!r1.attraction_order.get(i).equals(
						r2.attraction_order.get(i)))
					break;
				else if (i == r1.attraction_order.size() - 1)
					isthesame = true;
		}
		if (r1.attraction_order.size() == 0) {
			r1.start_time = StartTime;
			r1.end_time = EndTime;
			r1.attraction_order.add(defaultAttraction); // 沒景點時，就推薦的地方
		}

		r.add(r1);
		if (!isthesame && r2.attraction_order.size() != 0)
			r.add(r2);
		return r;
	}

	static public java.util.ArrayList<Result> GoNow(double X,
			double Y,                                                  // 由目前時間地點推薦行程 2個
			long EndTime, java.util.ArrayList<Integer> Themes,
			java.util.ArrayList<String> Pockets) {

		if (isNotInitial ()) {
			java.util.ArrayList<Result> err = new java.util.ArrayList<Result>();
			err = null;
			return err;
		}

		Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		long StartTime = cal1.getTimeInMillis() / 1000;
		java.util.ArrayList<java.util.ArrayList<PlaceScore>> sugs = gensuggestion(
				X, Y, Themes, Pockets, null, null);
		java.util.ArrayList<Integer> sug1 = new java.util.ArrayList<Integer>();
		java.util.ArrayList<Integer> sug2 = new java.util.ArrayList<Integer>();
		Calendar cal = Calendar.getInstance();
		for (int i = 0; i < 8; i++) {
			int rand = (int) ((cal.getTimeInMillis() + i) % 10);
			sug1.add(new Integer((int) sugs.get(0).get(rand).index));
			sugs.get(0).remove(rand);
			sug2.add(new Integer((int) sugs.get(1).get(rand).index));
			sugs.get(1).remove(rand);
		}
		java.util.ArrayList<Result> r = new java.util.ArrayList<Result>();
		Result r1 = plan(sug1, StartTime, EndTime, nearest(X, Y));
		Result r2 = plan(sug2, StartTime, EndTime, nearest(X, Y));
		r.add(r1);
		r.add(r2);
		return r;
	}

	static private java.util.ArrayList<EventData> getfreetime(
			java.util.ArrayList<EventData> va) {
		double x = 0, y = 0;
		long bigger = 0;
		java.util.ArrayList<EventData> free = new java.util.ArrayList<EventData>();
		for (int i = 0; i < va.size() - 1; i++) {
			if (va.get(i).EndTime > bigger) {
				bigger = va.get(i).EndTime;
				x = va.get(i).X;
				y = va.get(i).Y;
			}
			if (va.get(i + 1).StartTime - bigger >= 3600) {            // 一小時
				EventData one = new EventData();
				one.StartTime = bigger;
				one.X = x;
				one.Y = y;
				one.EndTime = va.get(i + 1).StartTime;
				free.add(one);
			}
		}
		return free;
	}

	@SuppressWarnings("unchecked")
	static public java.util.ArrayList<Result> FromMyPocket(
			// 使用者選定景點，我們推薦時間
			java.util.ArrayList<String> PlacesIDs,
			java.util.ArrayList<Long> EventsStartTime,
			java.util.ArrayList<Long> EventsEndTime,
			java.util.ArrayList<Double> EventsX,
			java.util.ArrayList<Double> EventsY) {

		if (isNotInitial ()) {
			java.util.ArrayList<Result> err = new java.util.ArrayList<Result>();
			err = null;
			return err;
		}
		
		java.util.ArrayList<Integer> myplaces = new java.util.ArrayList<Integer>();
		for (int i = 0; i < PlacesIDs.size(); i++) {
			int b = (int) java.util.Arrays.binarySearch(placesids, // 找到該景點的index
					PlacesIDs.get(i));
			if (!myplaces.contains(b))                               // 防止重覆選
				myplaces.add(b);
		}
		
		Calendar cal1 = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
		long input    = cal1.getTimeInMillis() / 1000;
		
		if (input < 1370304000) // 2013/6/4 8:00 in Taiwan
			input = 1370304000;
		
		java.util.ArrayList<EventData> Events = new java.util.ArrayList<EventData>();
		for (int y = 0; y < EventsStartTime.size(); y++) {
			EventData ed = new EventData();
			ed.StartTime = EventsStartTime.get(y);
			ed.EndTime   = EventsEndTime.get(y);
			if (EventsX.size() > y)
				ed.X = EventsX.get(y);
			if (EventsY.size() > y)
				ed.Y = EventsY.get(y);
			Events.add(ed);
		}


		java.util.ArrayList<EventData> Free = new java.util.ArrayList<EventData>();
		EventData e1 = new EventData();

		e1.StartTime = 0;
		e1.EndTime   = input;
		Events.add(e1);

		input = input - (input % (24 * 3600));

		for (int i = 0; i < 20; i++) { 
			EventData e = new EventData();
			e.StartTime = input + (i * 24 + 16) * 3600;
			e.EndTime   = input + (i + 1) * 24 * 3600;
			Events.add(e);
		}

		Collections.sort(Events);
		Free = getfreetime(Events);

		java.util.ArrayList<Result> r    = new java.util.ArrayList<Result>();
		java.util.ArrayList<Result> back = new java.util.ArrayList<Result>();
		for (int i = 0; i < Free.size(); i++) {
			Result aresult = plan(myplaces, Free.get(i).StartTime,
					Free.get(i).EndTime, nearest(Free.get(i).X, Free.get(i).Y));
			if (aresult.attraction_order.size() == myplaces.size()
					&& r.size() < 2)
				r.add(aresult);
			else {
				int residual = myplaces.size()
						- aresult.attraction_order.size();
				aresult.end_time = Free.get(i).EndTime + residual * 7200;
				back.add(aresult);
			}
		}
		while (r.size() < 2 && back.size() != 0) {
			r.add(back.get(0));
			back.remove(0);
		}
		return r;
	}
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// init();


		initAllData();
		System.out.print(totalPlaces);
		System.out.print('\n');
		/*
		System.out.print(isNotInitial());
		initNumOfTotalPlaces(106);
		String connectionString = "jdbc:mysql://140.92.2.182/Tainan_DB?useUnicode=true&characterEncoding=utf8&user=root&password=123qwe&PlaceTable=place_for_computex&OpentimeTable=place_open_time&DurationTable=place_route&numofTypes=7";
		
		init (connectionString);
		
		*/
		System.out.println("OK");
		/*
		int [] test2 = new int [totalPlaces*7]; 
		for (int i = 0; i < totalPlaces; i++) {
			for (int j = 0; j < 7; j++) {
				test2[i*7+j] =  (i*j);
			}
		}
		
		
		System.out.println(test1(test2));*/
		//System.out.println(test1(test2));

		/*
		
		//System.out.println(test1());
		
		*/	
//		for (int i = 0; i < totalPlaces; i++) {
//			for (int j = 0; j < totalPlaces; j++) {
//				//System.out.println(String.valueOf(c));
//				System.out.print(String.valueOf(i) + "--" + String.valueOf(j) + "�G");
//				System.out.println(String.valueOf(duration[i][j]));
//			}
//		}
		

//		for (int i = 0; i < totalPlaces; i++) {
//			System.out.println(String.valueOf(i));
//			System.out.println(placesids[i]);
//			System.out.println(places[i].stay_time);
//			System.out.println(String.valueOf(places[i].px));
//			System.out.println(String.valueOf(places[i].py));			
//			for(int j=0; j<7; j++) {
//				 System.out.println(String.valueOf(places[i].types[j]));
//			}
//			System.out.print('\n');
//		}

	/*

		for (int i = 0; i < totalPlaces; i++) {
			for (int j = 0; j < open_times[i].size(); j++) {
				System.out.println(String.valueOf(i));
				System.out.println(String.valueOf(open_times[i].get(j).day));
				System.out.println(open_times[i].get(j).time1o);
				System.out.println(open_times[i].get(j).time1c);
				System.out.println(open_times[i].get(j).time2o);
				System.out.println(open_times[i].get(j).time2c);
			}
		}
		*/
/*		
		int numOfOpentime = 167;

		int[] idx = new int[numOfOpentime];
		int[] inDay = new int[numOfOpentime];
		String[] inTime1o = new String[numOfOpentime];
		String[] inTime1c = new String[numOfOpentime];
		String[] inTime2o = new String[numOfOpentime];
		String[] inTime2c = new String[numOfOpentime];

		// InitOpentime (topen_times);
		int c = 0;
		for (int i = 0; i < totalPlaces; i++) {
			for (int j = 0; j < topen_times[i].size(); j++) {
				idx[c] = i;
				inDay[c] = topen_times[i].get(j).day;
				inTime1o[c] = topen_times[i].get(j).time1o;
				inTime1c[c] = topen_times[i].get(j).time1c;
				inTime2o[c] = topen_times[i].get(j).time2o;
				inTime2c[c] = topen_times[i].get(j).time2c;
				c++;
			}
		}

		initOpentime(idx, inDay, inTime1o, inTime1c, inTime2o, inTime2c);
*/
//		for (int i = 0; i < totalPlaces; i++) {
//			for (int j = 0; j < open_times[i].size(); j++) {
//				System.out.println(String.valueOf(i));
//				System.out.println(String.valueOf(open_times[i].get(j).day));
//				System.out.println(open_times[i].get(j).time1o);
//				System.out.println(open_times[i].get(j).time1c);
//				System.out.println(open_times[i].get(j).time2o);
//				System.out.println(open_times[i].get(j).time2c);
//			}
//		}

/*
		 * for (int i=0;i<totalPlaces;i++){ for(int j=0; j<7; j++) {
		 * System.out.println(String.valueOf(places[i].types[j])); }
		 * System.out.println(places[i].nameC);
		 * System.out.println(places[i].nameE);
		 * System.out.println(places[i].stay_time);
		 * System.out.println(String.valueOf(places[i].px));
		 * System.out.println(String.valueOf(places[i].py)); }
		 * 
		 * 
		 * 
		 * for(int i = 0; i < totalPlaces; i++){ for (int j = 0; j <
		 * totalPlaces; j++){ System.out.print(duration[i][j]);
		 * System.out.print(" "); } System.out.print('\n'); } for (int i=0; i <
		 * 8; i++){
		 * System.out.println(String.valueOf(numOfClassifiedPlaces[i])); }
		 * //static private
		 * java.util.ArrayList<java.util.ArrayList<place_score>> gensuggestion(
		 * // ��o���˴��I // double X, double Y, java.util.ArrayList<Integer> Themes,
		 * // java.util.ArrayList<String> Pockets, java.util.ArrayList<String>
		 * Whitelist, // java.util.ArrayList<String> Blacklist)
		 * java.util.ArrayList<Integer> tThemes = new
		 * java.util.ArrayList<Integer>(); java.util.ArrayList<String> tPockets
		 * = new java.util.ArrayList<String>(); java.util.ArrayList<String>
		 * tWhitelist = new java.util.ArrayList<String>();
		 * java.util.ArrayList<String> tBlacklist = new
		 * java.util.ArrayList<String>();
		 * 
		 * for(int i = 0; i < 20; i++) { if (i < 5) tThemes.add(i);
		 * tPockets.add(placesids[i]); tWhitelist.add(placesids[i+20]);
		 * tBlacklist.add(placesids[i+40]); } gensuggestion( places[92].px ,
		 * places[92].py, tThemes, tPockets, tWhitelist, tBlacklist);
		 * 
		 * place_info [] places2 = new place_info [totalPlaces]; for(int i = 0;
		 * i < totalPlaces; i++) { places2[i] = new place_info();
		 * place_info.numOfTypes = 9; places2[i].InitTypes();
		 * 
		 * places2[i].types[0] = tplaces[i].type1; places2[i].types[1] =
		 * tplaces[i].type2; places2[i].types[2] = tplaces[i].type3;
		 * places2[i].types[3] = tplaces[i].type4; places2[i].types[4] =
		 * tplaces[i].type5; places2[i].types[5] = tplaces[i].type6;
		 * places2[i].types[6] = tplaces[i].type7; places2[i].types[7] = 100;
		 * places2[i].types[8] = 10; places2[i].nameC = tplaces[i].nameC;
		 * places2[i].nameE = tplaces[i].nameE; places2[i].stay_time =
		 * tplaces[i].stay_time; places2[i].px = tplaces[i].px; places2[i].py =
		 * tplaces[i].py; }
		 * 
		 * for(int i = 0; i < totalPlaces; i++) { for(int j = 0; j <
		 * place_info.numOfTypes; j++) { System.out.print(places2[i].types[j]);
		 * System.out.print('\n'); } System.out.println(places2[i].nameC);
		 * System.out.println(places2[i].nameE);
		 * System.out.println(places2[i].stay_time);
		 * System.out.println(String.valueOf(places2[i].px));
		 * System.out.println(String.valueOf(places2[i].py)); }
		 */
	}
}
