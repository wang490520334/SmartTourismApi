package iii.org.tw.model;

public class SuggestionTimeModel {
	private long start_time;
	private long end_time;
	
	public void setStartTime(long startTime){
		this.start_time = startTime;
	}
	
	public void setEndTime(long endTime){
		this.end_time = endTime;
	}
	
	public long getStartTime(){
		return this.start_time;
	}
	
	public long getEndTime(){
		return this.end_time;
	}
}
