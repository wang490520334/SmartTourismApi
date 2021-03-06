package iii.org.tw.entity;

import iii.org.tw.model.PoiEvent;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "行事曆事件")
@XmlRootElement(name = "CalendarEvent")
public class CalendarEvent {
	
    private String Event_Id ;
    private String Event_Type ;
    private String User_Id ;
    private String Event_Title ;
    private String Start_Time ; 
    private String End_Time ;   
    private List<PoiEvent> Event_Poi ;     
    private String Notice ;    
    private Integer Remind ;
    
	
	// 包含event_id
	public CalendarEvent(String event_Id, String event_Type, String user_Id,
			String event_Title, String start_Time, String end_Time,
			List<PoiEvent> event_Poi, String notice, Integer remind) {
		super();
		Event_Id = event_Id;
		Event_Type = event_Type;
		User_Id = user_Id;
		Event_Title = event_Title;		
		Start_Time = start_Time;
		End_Time = end_Time;
		Event_Poi = event_Poi;
		Notice = notice;
		Remind = remind;
	}
	
	// 不包含event_id
	public CalendarEvent(String event_Type, String user_Id, String event_Title,
			String start_Time, String end_Time, List<PoiEvent> event_Poi,
			String notice, Integer remind) {
		super();
		Event_Type = event_Type;
		User_Id = user_Id;
		Event_Title = event_Title;
		Start_Time = start_Time;
		End_Time = end_Time;
		Event_Poi = event_Poi;
		Notice = notice;
		Remind = remind;
	}

	@XmlElement(name = "Event_Id")
	@ApiModelProperty(value = "事件id", required=true)
	public String getEvent_Id() {
		return Event_Id;
	}
	
	public void setEvent_Id(String event_Id) {
		Event_Id = event_Id;
	}

	@XmlElement(name = "Event_Type")
	@ApiModelProperty(value = "事件型態: 1. 議程 2. 雄獅(套裝行程)3.Taipei Free Tour(自由行) 4.自訂-私人活動 5. 自訂-拜訪廠商 6. 自訂-Google Calendar 7.智慧行程規畫 8. 景點", required=true)
	public String getEvent_Type() {
		return Event_Type;
	}

	public void setEvent_Type(String event_Type) {
		Event_Type = event_Type;
	}

	@XmlElement(name = "User_Id")
	@ApiModelProperty(value = "填登入時所提供的user token", required=true)
	public String getUser_Id() {
		return User_Id;
	}

	public void setUser_Id(String user_Id) {
		User_Id = user_Id;
	}

	@XmlElement(name = "Event_Title")
	@ApiModelProperty(value = "事件名稱", required=true)
	public String getEvent_Title() {
		return Event_Title;
	}

	public void setEvent_Title(String event_Title) {
		Event_Title = event_Title;
	}

	@XmlElement(name = "Start_Time")
	@ApiModelProperty(value = "事件開始時間 ex:2014-10-22 10:30:00.0", required=true)	
	public String getStart_Time() throws Exception {
		return Start_Time ;
	}

	public void setStart_Time(String start_Time) {
		Start_Time = start_Time;
	}

	@XmlElement(name = "End_Time")
	@ApiModelProperty(value = "事件結束時間 ex:2014-10-22 12:30:00.0", required=true)	
	public String getEnd_Time() throws Exception {
		return End_Time ;
	}

	public void setEnd_Time(String end_Time) {
		End_Time = end_Time;
	}
	
	@XmlElement(name = "Event_Poi")
	@ApiModelProperty(value = "事件Poi", required=true)
	public List<PoiEvent> getEvent_Poi() {
		return Event_Poi;
	}

	public void setEvent_Poi(List<PoiEvent> event_Poi) {
		Event_Poi = event_Poi;
	}

	@XmlElement(name = "Notice")
	@ApiModelProperty(value = "注意事項", required=true)	
	public String getNotice() {
		return Notice;
	}

	public void setNotice(String notice) {
		Notice = notice;
	}
	
	@XmlElement(name = "Remind")
	@ApiModelProperty(value = "需要提醒與否(0:不需提醒 ; 1:需要提醒)", required=true)
	public Integer getRemind() {
		return Remind;
	}

	public void setRemind(Integer remind) {
		Remind = remind;
	}
    
}
