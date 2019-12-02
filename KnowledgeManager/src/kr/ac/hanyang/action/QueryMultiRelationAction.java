package kr.ac.hanyang.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import kr.ac.hanyang.action.argument.TripleSetArg;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.ExpressionList;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

/**
 * 
 * @author freebeing1
 *
 */
public class QueryMultiRelationAction extends KnowledgeProcessAction{

	private TripleSetArg tripleSet;
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {
		
		long start = System.currentTimeMillis();
		
		tripleSet = (TripleSetArg) o;
		
		ArrayList<String> subject = tripleSet.getSubject();
		ArrayList<String> predicate = tripleSet.getPredicate();
		ArrayList<String> object = tripleSet.getObject();
		
		ArrayList<List<List<String>>> result = null;
		
		result = queryMultiRelation(subject, predicate, object);
		
		ExpressionList expTripleSet = new ExpressionList();
		for(List<List<String>> set : result) {
			Expression expTripleGL = null;
			ExpressionList expTripleList = new ExpressionList();
			for(int i=0; i<set.get(0).size();i++) {
				ExpressionList expTriple = new ExpressionList();
				for(int j=0; j<3; j++) {
					Expression exp = GLFactory.newValueExpression(set.get(j).get(i));
					expTriple.add(exp);
				}
				expTripleGL = GLFactory.newGLExpression("triple", expTriple);
				expTripleList.add(expTripleGL);
			}
			expTripleSet.add(GLFactory.newGLExpression("tripleSet", expTripleList));
		}
		
		Expression expResult = GLFactory.newGLExpression("result", expTripleSet);
		
		String glString = "(queryMultiRelation (tripleSet ";
		
		for(int i=0; i<subject.size(); i++) {
			String sbj = subject.get(i);
			String pre = predicate.get(i);
			String obj = object.get(i);
			if(!sbj.contains("$")) {
				sbj = "\"" + sbj + "\"";
			}
			if(!pre.contains("$")) {
				pre = "\"" + pre + "\"";
			}
			if(!obj.contains("$")) {
				obj = "\"" + obj + "\"";
			}
			String triple = "(triple "+sbj+" "+pre+" "+obj+") ";
			glString = glString + triple;
		}
		glString = glString + ") "+expResult.toString() + ")";
		System.out.println(glString);
		
		GeneralizedList resultGL = null;
		try {
			resultGL = GLFactory.newGLFromGLString(glString);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		long time = System.currentTimeMillis() - start;
		JSONObject log = new JSONObject();
		JSONObject queryLog = new JSONObject();
		JSONArray monitorLog = new JSONArray();
		
		queryLog.put("InferenceTime", time+"ms");
		log.put("QueryType", "KnowledgeBase");
		log.put("msg", queryLog);
		
		for(int i=0;i<result.get(0).get(0).size();i++) {
			JSONObject ontologyLog = new JSONObject();
			String res_sbj = result.get(0).get(0).get(i);
			String res_pre = result.get(0).get(1).get(i);
			String res_obj = result.get(0).get(2).get(i);
			
			HashMap<String, String> monitor_s = ontologyMonitor_Individual(res_sbj);
			HashMap<String, String> monitor_o = null;
			if(res_obj.contains("#")) {
				monitor_o = ontologyMonitor_Individual(res_obj);
				try {
					ontologyLog.put("ObjectClass", toShortenedIRI(monitor_o.get("typeClass")));
				} catch (Exception e) {
					ontologyLog.put("ObjectClass", null);
				}
				
			}else {
				ontologyLog.put("ObjectClass", null);
			}
			
			ontologyLog.put("SubjectClass", toShortenedIRI(monitor_s.get("typeClass")));
			ontologyLog.put("SubjectIndividual", toShortenedIRI(res_sbj));
			ontologyLog.put("Predicate", toShortenedIRI(res_pre));
			ontologyLog.put("ObjectIndividual", toShortenedIRI(res_obj));
			
			monitorLog.add(ontologyLog);
		}
		log.put("Ontology Monitor", monitorLog);
		log.put("Ontology Scale", ontologyMonitor_Scale());
		log.put("GL", resultGL.toString());
		
		return log.toJSONString();
	}

	@SuppressWarnings("unchecked")
	private ArrayList<List<List<String>>> queryMultiRelation(ArrayList<String> subject, ArrayList<String> predicate, ArrayList<String> object) {
		
		ArrayList<List<List<String>>> result = new ArrayList<List<List<String>>>();
		
		int arraySize = subject.size();
		String selectClause = "";
		String whereClause = "";

		for(int i=0; i<arraySize; i++) {
			String s_query = toFullIRI(subject.get(i));
			String p_query = toFullIRI(predicate.get(i));
			String o_query = toFullIRI(object.get(i));
			
			if(s_query.contains("$")) {
				s_query = "?" + s_query.substring(1);
				selectClause = selectClause + s_query + " "; 
			} else {
				s_query = "<" + s_query + ">";
			}
			
			
			if(p_query.contains("$")) {
				p_query = "?" + p_query.substring(1);
				selectClause = selectClause + p_query + " ";
			} else {
				p_query = "<" + p_query + ">";
			}
			
			
			if(o_query.contains("$")) {
				o_query = "?" + o_query.substring(1);
				selectClause = selectClause + o_query + " ";
			} else if (o_query.contains("http://")) {
				o_query = "<" + o_query + ">";
			} else {
				o_query = "\"" + o_query + "\"";
			}
			
			
			whereClause = whereClause + s_query + " " + p_query + " " + o_query + " . \n";
		}
		
		String queryString = ""
				+ PREFIX
				
				+ "SELECT \n"
				
				+ selectClause 
				+ "\n"
				// ?room ?roomName ?level ?floorNumber  
				
				+ "WHERE {\n"
				
				+ whereClause 
				// <http://www.robot-arbi.kr/ontologies/DemoKM.owl#InternalMedicineDepartment001> <http://knowrob.org/kb/knowrob.owl#locatedAt> ?room . 
				// ?room <http://www.w3.org/2000/01/rdf-schema#label> ?roomName . 
				// ?room <http://knowrob.org/kb/knowrob.owl#hasLevels> ?level . 
				// ?level <http://knowrob.org/kb/knowrob.owl#floorNumber> ?floorNumber
				
				+ "}"
				+ "\n";
		
		Query query = QueryFactory.create(queryString);
//		QueryExecution qe = QueryExecutionFactory.create(query, DemoKM_OntModel); // 연구실 데모용
		QueryExecution qe = QueryExecutionFactory.create(query, service_OntModel); // 통합용
		
		ResultSet results = qe.execSelect();
		selectClause = selectClause.replace("?", "");
		while(results.hasNext()) {
			ArrayList<String> s = (ArrayList<String>) subject.clone();
			ArrayList<String> p = (ArrayList<String>) predicate.clone();
			ArrayList<String> o = (ArrayList<String>) object.clone();
			
			QuerySolution soln = results.nextSolution();
			
			for(String item : selectClause.split(" ")) {
				for(int i=0; i<arraySize; i++) {
					if(item.equals(s.get(i).substring(1))) {
						s.set(i, toShortenedIRI(soln.getResource(item).getURI()));
					}
					if(item.equals(p.get(i).substring(1))){
						p.set(i, toShortenedIRI(soln.getResource(item).getURI()));
					}
					if(item.equals(o.get(i).substring(1))){
						try {
							o.set(i, toShortenedIRI(soln.getResource(item).getURI()));
						} catch (Exception e) {
							o.set(i, toShortenedIRI(soln.getLiteral(item).getString()));
						}
					}
				}
			}
			List<List<String>> tripleSet = new ArrayList<List<String>>(); 
			tripleSet.add(s);
			tripleSet.add(p);
			tripleSet.add(o);
			result.add(tripleSet);
		}
		
		
		return result;
	}
	
}
