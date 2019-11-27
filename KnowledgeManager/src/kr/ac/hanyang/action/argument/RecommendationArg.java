package kr.ac.hanyang.action.argument;

import org.json.simple.JSONObject;

public class RecommendationArg extends InferenceArgument {
	
	private String userID;
	private String targetAction;
	private String history;
	
	public RecommendationArg(String u, String ta, String h) {
		super();
		this.userID = u;
		this.targetAction = ta;
		this.history = h;
	}
	
	public String getUserID() {
		return userID;
	}
	
	public String getTargetAction() {
		return targetAction;
	}
	
	public String getHistory() {
		return history;
	}
	
	public void setUserID(String userID) {
		this.userID = userID;
	}

	public void setTargetAction(String targetAction) {
		this.targetAction = targetAction;
	}
	
	public void setHistory(String history) {
		this.history = history;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString() {
		JSONObject log = new JSONObject();
		log.put("userID", userID);
		log.put("targetAction", targetAction);
		log.put("history", history);
		
		return log.toJSONString();
	}
	
}
