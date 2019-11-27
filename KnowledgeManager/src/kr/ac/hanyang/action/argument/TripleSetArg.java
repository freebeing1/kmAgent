package kr.ac.hanyang.action.argument;

import java.util.ArrayList;

import org.json.simple.JSONObject;

public class TripleSetArg extends InferenceArgument{
	private ArrayList<String> subject;
	private ArrayList<String> predicate;
	private ArrayList<String> object;
	
	public TripleSetArg(ArrayList<String> s, ArrayList<String> p, ArrayList<String> o) {
		this.subject = s;
		this.predicate = p;
		this.object = o;
	}
	
	
	public void setSubject(ArrayList<String> subject) {
		this.subject = subject;
	}
	public void setPredicate(ArrayList<String> predicate) {
		this.predicate = predicate;
	}
	public void setObject(ArrayList<String> object) {
		this.object = object;
	}

	
	public ArrayList<String> getSubject() {
		return subject;
	}
	public ArrayList<String> getPredicate() {
		return predicate;
	}
	public ArrayList<String> getObject() {
		return object;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		JSONObject log = new JSONObject();
		log.put("subject list", subject);
		log.put("predicate list", predicate);
		log.put("object list", object);
		
		return log.toJSONString();
	}
}
