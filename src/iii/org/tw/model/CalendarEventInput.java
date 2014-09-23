package iii.org.tw.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class CalendarEventInput {

    private String Event_Type ;
    private String Event_Title ;
    private String Start_Time ; 
    private String End_Time ;   
    private List<PoiEvent> Event_Poi ;     
    private String Notice ;    
    private Integer Remind ;
    
	public CalendarEventInput(String event_Type, String event_Title,
			String start_Time, String end_Time, List<PoiEvent> event_Poi,
			String notice, Integer remind) {
		super();
		Event_Type = event_Type;
		Event_Title = event_Title;
		Start_Time = start_Time;
		End_Time = end_Time;
		Event_Poi = event_Poi;
		Notice = notice;
		Remind = remind;
	}

	@XmlElement(name = "Event_Type")
	@ApiModelProperty(value = "事件型態: 1. 議程 2. 雄獅(套裝行程)3.Taipei Free Tour(自由行) 4.自訂-私人活動 5. 自訂-拜訪廠商 6. 自訂-Google Calendar 7.智慧行程規畫 8. 景點", required=true)
	public String getEvent_Type() {
		return Event_Type;
	}

	public void setEvent_Type(String event_Type) {
		Event_Type = event_Type;
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
