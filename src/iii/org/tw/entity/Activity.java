package iii.org.tw.entity;

import iii.org.tw.entity.base.IBaseType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Activity extends IBaseType {
	private String id=null;
	private String address=null;
	private float avgrank=0;
	private int collection=0;
	private String county=null;
	private String currency=null;
	private int price=0;
	private String description=null;
	private String starttime=null;
	private String endtime=null;
//	private String name=null;
	private String note=null;
	private String organizer=null;
	private String parking=null;
	private List<Picture> pictures = new ArrayList<Picture>();
	private String presenter;
	private Location location = null;
	private String registration;
	private String target;
	private List<String> tels = new ArrayList<String>();
	private String transport;
	private List<String> videos = new ArrayList<String>();
	private String website;
	private List<String> themes = new ArrayList<String>();
	private String shareUrl = null;

	public String getShareUrl() {
		return shareUrl;
	}

	public void setShareUrl(String shareUrl) {
		this.shareUrl = shareUrl;
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public float getAvgrank() {
		return avgrank;
	}
	public void setAvgrank(float avgrank) {
		this.avgrank = avgrank;
	}
	public int getCollection() {
		return collection;
	}
	public void setCollection(int collection) {
		this.collection = collection;
	}
	public String getCounty() {
		return county;
	}
	public void setCounty(String county) {
		this.county = county;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public int getPrice() {
		return price;
	}
	public void setPrice(int price) {
		this.price = price;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getEndtime() {
		return endtime;
	}

	public void setEndtime(String endtime) {
		this.endtime = endtime;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	public String getOrganizer() {
		return organizer;
	}
	public void setOrganizer(String organizer) {
		this.organizer = organizer;
	}
	public String getParking() {
		return parking;
	}
	public void setParking(String parking) {
		this.parking = parking;
	}
	public List<Picture> getPictures() {
		return pictures;
	}
	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}
	public String getPresenter() {
		return presenter;
	}
	public void setPresenter(String presenter) {
		this.presenter = presenter;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getRegistration() {
		return registration;
	}
	public void setRegistration(String registration) {
		this.registration = registration;
	}
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public List<String> getTels() {
		return tels;
	}
	public void setTels(List<String> tels) {
		this.tels = tels;
	}
	public String getTransport() {
		return transport;
	}
	public void setTransport(String transport) {
		this.transport = transport;
	}
	public List<String> getVideos() {
		return videos;
	}
	public void setVideos(List<String> videos) {
		this.videos = videos;
	}
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public List<String> getThemes() {
		return themes;
	}
	public void setThemes(List<String> themes) {
		this.themes = themes;
	}
}
