package iii.org.tw.entity.base;

import javax.xml.bind.annotation.XmlElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(subTypes= { iii.org.tw.entity.Attraction.class, iii.org.tw.entity.FoodAndDrink.class}, description=" 可能是 Attraction, FoodAndDrink")

public class IBaseType {
	
	protected String name ; 
	
	public IBaseType() {
		super();
	}
	
	public IBaseType(String name) {
		super();
		this.name = name;
	}

	@XmlElement(name = "name")
	@ApiModelProperty(value = "自定義的Poi object name", required=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
