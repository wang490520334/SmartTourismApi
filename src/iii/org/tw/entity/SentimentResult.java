package iii.org.tw.entity;

import java.util.ArrayList;
import java.util.List;

public class SentimentResult {
	String keyword;
	String startDate,endDate;
	List<String> positive = new ArrayList<String>();
	List<String> negative = new ArrayList<String>();
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public List<String> getPositive() {
		return positive;
	}
	public void setPositive(List<String> positive) {
		this.positive = positive;
	}
	public List<String> getNegative() {
		return negative;
	}
	public void setNegative(List<String> negative) {
		this.negative = negative;
	}
	public List<String> getTags() {
		return tags;
	}
	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	List<String> tags = new ArrayList<String>();
}
