package iii.org.tw.model;

import javax.xml.bind.annotation.XmlElement;

import com.wordnik.swagger.annotations.ApiModelProperty;

public class RankOutput {

	private String rankId ;
    private String placeId ;
    private int rank ;
    private String type ;
   
	public RankOutput(String rankId, String placeId, int rank,
			String type) {
		super();
		this.rankId = rankId;
		this.placeId = placeId;
		this.rank = rank;
		this.type = type;
	}

	@XmlElement(name = "rankId")
	@ApiModelProperty(value = "rank id", required=true)	
	public String getRankId() {
		return rankId;
	}

	public void setRankId(String rankId) {
		this.rankId = rankId;
	}

	@XmlElement(name = "placeId")
	@ApiModelProperty(value = "評分物件id", required=true)	
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	
	@XmlElement(name = "rank")
	@ApiModelProperty(value = "評分rank(0~5)", required=true)		
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	@XmlElement(name = "type")
	@ApiModelProperty(value = "'Attraction' or 'FoodAndDrink' or 'Shopping' or 'Activity' or 'Tour'", required=true)		
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
    
	
}
