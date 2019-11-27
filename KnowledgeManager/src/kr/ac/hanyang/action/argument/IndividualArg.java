package kr.ac.hanyang.action.argument;

import org.json.simple.JSONObject;

public class IndividualArg extends KnowledgeManipulateArgument {
	private String typeClass;
	private String individual;
	
	public IndividualArg(String tc, String i) {
		this.typeClass = tc;
		this.individual = i;
	}
	
	public String getTypeClass() {
		return typeClass;
	}
	public String getIndividual() {
		return individual;
	}
	public void setTypeClass(String tc) {
		this.typeClass = tc;
	}
	public void setIndividual(String i) {
		this.individual = i;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		JSONObject log = new JSONObject();
		log.put("typeClass", this.typeClass);
		log.put("individual", this.individual);
		
		return log.toJSONString();
	}
}
