package iii.org.tw.model;

import iii.org.tw.entity.base.IBaseType;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "PoiCustomDetail")
@XmlRootElement(name = "PoiCustomDetail")
public class PoiCustomDetail extends IBaseType {

//	private String name ; 
	private String address ;
	
	public PoiCustomDetail() {
		super();
	}
	
	public PoiCustomDetail(String name, String address) {
		super();
		this.name = name;
		this.address = address;
	}

	@XmlElement(name = "name")
	@ApiModelProperty(value = "自定義的Poi object name", required=true)
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlElement(name = "address")
	@ApiModelProperty(value = "自定義的Poi object address", required=true)
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
	
	
	
}
