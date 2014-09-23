package iii.org.tw.entity;

import java.util.ArrayList;

public class PlanInfo 
{
	String fromid;
	String toid;
	String time;
	String direction;
	public String getFromId() {
		return fromid;
	}
	public void setFromId(String id) {
		this.fromid = id;
	}
	public String getToId() {
		return toid;
	}
	public void setToId(String id) {
		this.toid = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String t) {
		this.time = t;
	}
	public String getDirection() {
		return direction;
	}
	public void setDirection(String d) {
		this.direction = d;
	}
		
}
