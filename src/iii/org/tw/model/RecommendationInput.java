package iii.org.tw.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class RecommendationInput {

	private List<String> poiList ;

	public RecommendationInput(List<String> poiList) {
		super();
		this.poiList = poiList;
	}

	@XmlElement(name = "poiList")
	@ApiModelProperty(value = "POI id組成的列表", required=true)
	public List<String> getPoiList() {
		return poiList;
	}

	public void setPoiList(List<String> poiList) {
		this.poiList = poiList;
	} 
	
	
	
}
