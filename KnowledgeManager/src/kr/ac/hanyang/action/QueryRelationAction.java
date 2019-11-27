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

import kr.ac.hanyang.action.argument.TripleArg;
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
public class QueryRelationAction extends KnowledgeProcessAction {
	
	private TripleArg tripleArg;
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {
		
		long start = System.currentTimeMillis();
		
		tripleArg = (TripleArg) o;
		String subject = tripleArg.getSubject();
		String predicate = tripleArg.getPredicate();
		String object = tripleArg.getObject();
		
		ArrayList<List<String>> result = null;
		
		GeneralizedList res = null;
		
		ExpressionList expList = new ExpressionList();
		ExpressionList subExpList = null;
		
		result = queryRelation(subject, predicate, object);
		
		
		for(List<String> item : result) {
			subExpList = new ExpressionList();
			for(String spo : item) {
				Expression exp = GLFactory.newValueExpression(spo);
				subExpList.add(exp);
			}
			expList.add(GLFactory.newGLExpression("triple", subExpList));
		}
		res = GLFactory.newGL("result", expList);
		
		
		String expS = subject;
		String expP = predicate;
		String expO = object;
		
		if(!subject.contains("$")) {
			expS = "\"" + subject + "\"";
		}
		if(!predicate.contains("$")) {
			expP = "\"" + predicate + "\"";
		}
		if(!object.contains("$")) {
			expO = "\"" + object + "\"";
		}
		
		
		String glString = "(queryRelation " + expS + " " + expP + " " + expO + " " + res.toString()+")";
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
		
		queryLog.put("Result", result);
		queryLog.put("InferenceTime", time+"ms");
		log.put("KnowledgeBase", "Ontology");
		log.put("msg", queryLog);
		
		
		for(int i=0;i<result.size();i++) {
			JSONObject ontologyLog = new JSONObject(); 
			String res_sbj = result.get(i).get(0);
			String res_pre = result.get(i).get(1);
			String res_obj = result.get(i).get(2);
			
			HashMap<String, String> monitor_s = ontologyMonitor_Individual(res_sbj);
			HashMap<String, String> monitor_o = null;
			if(res_obj.contains("#")) {
				monitor_o = ontologyMonitor_Individual(res_obj);
				try {
					ontologyLog.put("ObjectClass", shortenedIRI(monitor_o.get("typeClass")));
				} catch (Exception e) {
					ontologyLog.put("ObjectClass",null);
				}
				
			}else {
				ontologyLog.put("ObjectClass",null);
			}
			
			ontologyLog.put("SubjectClass", shortenedIRI(monitor_s.get("typeClass")));
			ontologyLog.put("SubjectIndividual", shortenedIRI(res_sbj));
			ontologyLog.put("Predicate", shortenedIRI(res_pre));
			ontologyLog.put("ObjectIndividual", shortenedIRI(res_obj));
			
			monitorLog.add(ontologyLog);
		}
		log.put("Ontology Monitor", monitorLog);
		log.put("Ontology Scale", ontologyMonitor_Scale());
		log.put("GL", resultGL.toString());
		
		System.out.println("<TRIPLE> : " + tripleArg);
		System.out.println();
		
		
		return log.toJSONString(); 
			
	}

	


	private ArrayList<List<String>> queryRelation(String subject, String predicate, String object) {
		
		ArrayList<List<String>> result = new ArrayList<List<String>>();
		
		String s_query;
		String p_query;
		String o_query;
		
		if(subject.contains("$")) {
			s_query = "?s";
		} else {
			s_query = "<"+subject+">"; 
		}
		
		if(predicate.contains("$")) {
			p_query = "?p";
		} else {
			p_query = "<"+predicate+">"; 
		}
		
		if(object.contains("$")) {
			o_query = "?o";
		} else if(object.contains("http://")) {
			o_query = "<" + object +">"; 
		} else {
			o_query = "\"" + object +"\"";
		}
		
		
		
		String queryString = ""
				+ PREFIX
				
				+ "SELECT \n"
				
				+ "?s ?p ?o \n"
				
				+ "WHERE {\n"
				
				+ s_query + " " + p_query + " " + o_query + " . \n"
				
				+ "}"
				+ "\n";
		
		Query sQuery = QueryFactory.create(queryString);
//		QueryExecution qe = QueryExecutionFactory.create(sQuery, DemoKM_OntModel);
		QueryExecution qe = QueryExecutionFactory.create(sQuery, service_OntModel);
		ResultSet results = qe.execSelect();
		
		
		while(results.hasNext()) {
			
			String s = null;
			String p = null;
			String o = null;
			
			QuerySolution soln = results.nextSolution();
			
			try {
				s = soln.getResource("s").getURI();
			} catch (Exception e) {
				try {
					s = soln.getLiteral("s").getString();
				} catch (Exception e2) {
					s = subject;
				}
			}
			
			try {
				p = soln.getResource("p").getURI();
			} catch (Exception e) {
				try {
					p = soln.getLiteral("p").getString();
				} catch (Exception e2) {
					p = predicate;
				}
				
			}
			
			try {
				o = soln.getResource("o").getURI();
			} catch (Exception e) {
				try {
					o = soln.getLiteral("o").getString();
				} catch (Exception e2) {
					o = object;
				}
				
			}
			
			List<String> resultTriple = new ArrayList<String>();
			resultTriple.add(s);
			resultTriple.add(p);
			resultTriple.add(o);
			result.add(resultTriple);
			
		}
		
		
		
		return result;
	}
	
}
