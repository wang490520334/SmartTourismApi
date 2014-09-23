package iii.org.tw.hydrator;

import iii.org.tw.entity.Activity;
import iii.org.tw.entity.Attraction;
import iii.org.tw.entity.Location;
import iii.org.tw.entity.Picture;
import iii.org.tw.entity.PictureMeta;
import iii.org.tw.util.NotFoundException;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ActivityHydrator {

	public static Properties props = new Properties();

	static {
		try {
			props.load(ActivityHydrator.class
					.getResourceAsStream("/db.properties"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
	
	private static Activity hydrator(ResultSet result) throws SQLException
	{
		Activity att = new Activity();
		att.setId(result.getString("Activity_Id"));
		att.setName(result.getString("Name"));
		att.setAddress(result.getString("Address"));
		att.setDescription(result.getString("Description"));
		att.setAvgrank(result.getFloat("Avg_Rank"));
		att.setTransport(result.getString("Transport"));
		att.setTarget(result.getString("Target"));
		att.setOrganizer(result.getString("Organizer"));
		Location loc = new Location();
		loc.setLatitude(result.getDouble("Py"));
		loc.setLongitude(result.getDouble("Px"));
		att.setLocation(loc);
		att.setStarttime(String.valueOf(result.getDate("Start_Time").getTime()));
		att.setEndtime(String.valueOf(result.getDate("End_Time").getTime()));
		att.setParking(result.getString("Parking").equals("")?null:result.getString("Parking"));
		att.setNote(result.getString("Note").equals("")?null:result.getString("Note"));
		att.setPrice(result.getInt("Price"));
		att.setThemes(Arrays.asList(result.getString("Theme_Concat").split(",")));
		att.setTels(result.getString("Tel") == null || result.getString("Tel").equals("") ? new ArrayList<String>() : Arrays.asList(result.getString("Tel").split(",")));
		att.setVideos(result.getString("Video") == null || result.getString("Video").equals("") ? new ArrayList<String>() : Arrays.asList(result.getString("Video").split(",")));
		att.setCounty(result.getString("County_Id"));
		att.setCollection(result.getInt("Collection"));		
		att.setShareUrl(props.getProperty("share.url.bath") + props.getProperty("share.url.Activity") + result.getString("Activity_Id"));
		ArrayList<Picture> pics = new ArrayList<Picture>();
		if(result.getString("Picture") != null && !result.getString("Picture").equals(""))
		{
			int index = 0;
			List<String> picdecs = new ArrayList<String>();
			if(result.getString("Picture_Description") != null)
				picdecs = Arrays.asList(result.getString("Picture_Description").split("[|]{3}"));
			for(String picurl : result.getString("Picture").split(","))
			{
				Picture pic = new Picture();
				PictureMeta meta = new PictureMeta();
				if(picurl.startsWith("http")){
					meta.setUrl(picurl);
					pic.setUrl(picurl);
				}
				else{
					meta.setUrl(props.getProperty("images.hosturl") + picurl);
					pic.setUrl(props.getProperty("images.hosturl") + picurl);
				}
				meta.setWidth(512);	//FIX ME
				meta.setHeight(300);	//FIX ME
				pic.getMetas().add(meta);
				if(index < picdecs.size())
					pic.setDescription(picdecs.get(index));
				else
					pic.setDescription("");
				index++;
				pics.add(pic);
			}
		}
		att.setPictures(pics);
		return att;
	}
	
	public static Activity fromResultSet(ResultSet result) throws SQLException, NotFoundException
	{
		if(result.first())
			return hydrator(result);
		else
			throw new NotFoundException("No such data");
	}
	
	public static List<Activity> fromResultSettoList(ResultSet result) throws NotFoundException, SQLException
	{
		ArrayList<Activity> results = new ArrayList<Activity>();
		while (result.next()) {
			results.add(hydrator(result));
		}
		if(results.size() == 0)
			throw new NotFoundException("No such data");
		return results;
	}
}
