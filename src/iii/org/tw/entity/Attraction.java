package iii.org.tw.entity;

import java.util.*;

import javax.validation.constraints.Null;

import iii.org.tw.entity.Location;
import iii.org.tw.entity.base.IBaseType;

public class Attraction extends IBaseType{
	/* unique identifier for the attraction */
	private String id = null;
//	private String name = null;
	private List<String> themes = new ArrayList<String>();	
	private String description = null;
	private List<Picture> pictures = new ArrayList<Picture>();
	private List<String> videos = new ArrayList<String>();
	private Location location = null;
	private List<String> tels = new ArrayList<String>();
	private String address = null;
	private String opentime = null;
	private float avgrank = 0;
	private String parking = null;
	private Integer price = null;
	private String note = null;
	private String website = null;
	private String transport = null;
	private String county = null;
	private Integer collection = null;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getThemes() {
		return themes;
	}

	public void setThemes(List<String> themes) {
		this.themes = themes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Picture> getPictures() {
		return pictures;
	}

	public void setPictures(List<Picture> pictures) {
		this.pictures = pictures;
	}

	public List<String> getVideos() {
		return videos;
	}

	public void setVideos(List<String> videos) {
		this.videos = videos;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public List<String> getTels() {
		return tels;
	}

	public void setTels(List<String> tels) {
		this.tels = tels;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getOpentime() {
		return opentime;
	}

	public void setOpentime(String opentime) {
		this.opentime = opentime;
	}

	public float getAvgrank() {
		return avgrank;
	}

	public void setAvgrank(float avgrank) {
		this.avgrank = avgrank;
	}

	public String getParking() {
		return parking;
	}

	public void setParking(String parking) {
		this.parking = parking;
	}

	public Integer getPrice() {
		return price;
	}

	public void setPrice(Integer price) {
		this.price = price;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getTransport() {
		return transport;
	}

	public void setTransport(String transport) {
		this.transport = transport;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public Integer getCollection() {
		return collection;
	}

	public void setCollection(Integer collection) {
		this.collection = collection;
	}



	
	
}
