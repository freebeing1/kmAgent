package kr.ac.hanyang.action.argument;

import org.json.simple.JSONObject;

public class PropertyArg extends KnowledgeManipulateArgument {
	
	private String propertyType;
	private String superProperty;
	private String propertyID;
	private String domain;
	private String range;
	
	public PropertyArg(String propertyType, String superProperty, String propertyID, String domain, String range) {
		this.propertyType = propertyType;
		this.superProperty = superProperty;
		this.propertyID = propertyID;
		this.domain = domain;
		this.range = range;
	}
	
	public String getPropertyType() {
		return propertyType;
	}
	public String getSuperProperty() {
		return superProperty;
	}
	public String getPropertyID() {
		return propertyID;
	}
	public String getDomain() {
		return domain;
	}
	public String getRange() {
		return range;
	}
	
	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}
	public void setSuperProperty(String superProperty) {
		this.superProperty = superProperty;
	}
	public void setPropertyID(String propertyID) {
		this.propertyID = propertyID;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public void setRange(String range) {
		this.range = range;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		JSONObject log = new JSONObject();
		log.put("propertyType", this.propertyType);
		log.put("superProperty", this.superProperty);
		log.put("propertyID", this.propertyID);
		log.put("domain", this.domain);
		log.put("range", this.range);
		
		return log.toJSONString();
	}
}
