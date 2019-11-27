package kr.ac.hanyang.action;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import kr.ac.hanyang.action.argument.TripleArg;

/**
 * 
 * @author freebeing1
 *
 */
public class QueryCloudRelationAction extends KnowledgeProcessAction {
	
	private TripleArg triple;
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {
		long start = System.currentTimeMillis();
		String resultGL = "";
		triple = (TripleArg) o;
		String subject = triple.getSubject();
		String predicate = triple.getPredicate();
		String object = triple.getObject();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		JSONObject log = new JSONObject();
		JSONObject queryLog = new JSONObject();
		JSONObject ontologyLog = new JSONObject();
		JSONArray monitorLog = new JSONArray();
		
		resultGL = "(queryCloudRelation \""+subject+"\" \""+predicate+"\" $o (result (triple \""+subject+"\" \""+predicate+"\" \""+object+"\")))";
		
		long time = System.currentTimeMillis()-start;
		
		queryLog.put("InferenceTime", time+"ms");
		
		log.put("KnowledgeBase", "Cloud");
		log.put("msg", queryLog);
		
		ontologyLog.put("SubjectClass", "UNKNOWN");
		ontologyLog.put("SubjectIndividual", subject);
		ontologyLog.put("ObjectClass", "UNKNOWN");
		ontologyLog.put("ObjectIndividual", object);
		ontologyLog.put("Predicate", predicate);
		monitorLog.add(ontologyLog);
		log.put("Ontology Monitor", monitorLog);
		log.put("Ontology Scale", ontologyMonitor_Scale());
		log.put("GL", resultGL.toString());
		
		System.out.println("<TRIPLE> : " + triple);
		System.out.println();
		
		
		
		return log.toJSONString();
	}
	
}
