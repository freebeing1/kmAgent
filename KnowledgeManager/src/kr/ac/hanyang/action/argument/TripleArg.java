package kr.ac.hanyang.action.argument;

import org.json.simple.JSONObject;

public class TripleArg extends InferenceArgument {
	
	private String subject;
	private String predicate;
	private String object;
	
	public TripleArg(String subject, String predicate, String object) {
		
		this.subject = subject;
		this.predicate = predicate;
		this.object = object;
		
	}
	
	
	public String getSubject() {
		return subject;
	}
	public String getPredicate() {
		return predicate;
	}
	public String getObject() {
		return object;
	}
	
	
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public void setPredicate(String predicate) {
		this.predicate = predicate;
	}
	public void setObject(String object) {
		this.object = object;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		JSONObject log = new JSONObject();
		log.put("subject", this.subject);
		log.put("predicate", this.predicate);
		log.put("object", this.object);
		
		return log.toJSONString();
	}
	
}
