package kr.ac.hanyang.action.argument;

import org.json.simple.JSONObject;

public class ClassArg extends KnowledgeManipulateArgument {
	private String superClass;
	private String targetClass;
	
	public ClassArg(String sc, String tc) {
		this.superClass = sc;
		this.targetClass = tc;
	}

	public String getTargetClass() {
		return targetClass;
	}
	
	public String getSuperClass() {
		return superClass;
	}
	
	public void setTargetClass(String tc) {
		this.targetClass = tc;
	}
	
	public void setSuperClass(String sc) {
		this.superClass = sc;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		JSONObject log = new JSONObject();
		log.put("superClass", this.superClass);
		log.put("targetClass", this.targetClass);
		
		return log.toJSONString();
	}
	
}
