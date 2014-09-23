package iii.org.tw.entity;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "評分物件")
@XmlRootElement(name = "Rank")
public class Rank {
	
	private String rankId ;
    private String placeId ;
    private String userId ;
    private int rank ;
    private int type ;
    
    public Rank() {
    	super() ;
    }
    
	public Rank(String rankId, String placeId, String userId, int rank, int type) {
		super();
		this.rankId = rankId ;
		this.placeId = placeId;
		this.userId = userId;
		this.rank = rank;
		this.type = type;
	}
	
	@XmlElement(name = "rankId")
	@ApiModelProperty(value = "評分rank id", required=true)	
	public String getRankId() {
		return rankId ;
	}	
	
	public void setRankId(String rankId) {
		this.rankId = rankId ;
	}	
	
	@XmlElement(name = "placeId")
	@ApiModelProperty(value = "評分物件id", required=true)	
	public String getPlaceId() {
		return placeId;
	}
	public void setPlaceId(String placeId) {
		this.placeId = placeId;
	}
	
	@XmlElement(name = "userId")
	@ApiModelProperty(value = "新增此rank的user id", required=true)	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
    
}
