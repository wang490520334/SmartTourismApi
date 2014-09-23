package iii.org.tw.entity;

import iii.org.tw.model.Poi;

import java.util.ArrayList;
import java.util.List;
public class Theme {
  /* unique identifier for the theme */
  private String id = null;
  private String name = null;
  private String description = null;
  private List<Picture> pictures = new ArrayList<Picture>();
  private List<Poi> poiList = new ArrayList<Poi>();
  private List<Theme> subthemeList = new ArrayList<Theme>();
  private int type = 0;
  
  public List<Theme> getSubthemeList() {
	return subthemeList;
  }
  public void setSubthemeList(List<Theme> subthemeList) {
    this.subthemeList = subthemeList;
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
  
  public int getType() {
	return type;
  }
  public void setType(int type) {
	this.type = type;
  }
  

  public List<Poi> getPoiList() {
	return poiList;
  }
  public void setPoiList(List<Poi> poiList) {
	this.poiList = poiList;
  }
@Override
  public String toString()  {
    StringBuilder sb = new StringBuilder();
    sb.append("class Theme {\n");
    sb.append("  id: ").append(id).append("\n");
    sb.append("  name: ").append(name).append("\n");
    sb.append("  description: ").append(description).append("\n");
    sb.append("  pictures: ").append(pictures).append("\n");
    sb.append("}\n");
    return sb.toString();
  }
}

