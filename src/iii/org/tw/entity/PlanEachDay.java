package iii.org.tw.entity;

import java.util.ArrayList;
import java.util.HashMap;

public class PlanEachDay 
{
	String date;
	HashMap<String,ArrayList<PlanInfo>> period = new HashMap<String,ArrayList<PlanInfo>>();
	public String getDate() {
		return date;
	}
	public void setDate(String d) {
		this.date = d;
	}
	public HashMap<String,ArrayList<PlanInfo>> getPeriod() {
		return period;
	}
	public void setPeriod(HashMap<String,ArrayList<PlanInfo>> p) {
		this.period = p;
	}
}
