package iii.org.tw.entity;

import iii.org.tw.entity.base.IBaseType;
import iii.org.tw.model.Poi;

import java.util.ArrayList;
import java.util.List;

public class Tour extends IBaseType {

	String id=null;
//	String name=null;
	List<String> themes=new ArrayList<String>();
	String description=null ;
	private int collection=0;
	List<Picture>pictures=new ArrayList<Picture>();
	private List<Poi> relatedid = new ArrayList<Poi>();
	
	
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

	public List<Poi> getRelatedid() {
		return relatedid;
	}

	public void setRelatedid(List<Poi> relatedid) {
		this.relatedid = relatedid;
	}

	public int getCollection() {
		return collection;
	}

	public void setCollection(int collection) {
		this.collection = collection;
	}

	
}
