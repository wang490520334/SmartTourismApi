package iii.org.tw.entity;

import java.util.*;

import javax.validation.constraints.Null;

import iii.org.tw.entity.Location;
import iii.org.tw.entity.base.IBaseType;
public class Shopping extends IBaseType {
  /* unique identifier for the attraction */
  private String id = null;
//  private String name = null;
  private List<String> themes = new ArrayList<String>();
  private String description = null;
  private List<Picture> pictures = new ArrayList<Picture>();
  private List<String> videos = new ArrayList<String>();
  private Location location = null;
  private List<String> tels = new ArrayList<String>();
  private String address = null;
  private String opentime = null;
  private float avgrank = 0 ;
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
@Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Shopping {\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  themes: ").append(themes).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  pictures: ").append(pictures).append("\n");
    sb.append("  videos: ").append(videos).append("\n");
    sb.append("  location: ").append(location).append("\n");
    sb.append("  tels: ").append(tels).append("\n");
    sb.append("  address: ").append(address).append("\n");
    sb.append("  opentime: ").append(opentime).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

