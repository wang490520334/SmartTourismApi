package iii.org.tw.entity;

import java.util.ArrayList;
import java.util.List;

public class Picture {
	private String url;
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	private List<PictureMeta> metas = new ArrayList<PictureMeta>();
	private String description;
	public List<PictureMeta> getMetas() {
		return metas;
	}
	public void setMetas(List<PictureMeta> metas) {
		this.metas = metas;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
