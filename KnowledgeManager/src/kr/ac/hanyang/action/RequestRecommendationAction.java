package kr.ac.hanyang.action;

import java.util.HashSet;
import java.util.Iterator;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import kr.ac.hanyang.action.argument.RecommendationArg;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedListBuilder;

public class RequestRecommendationAction extends KnowledgeProcessAction {

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {
		
		/*
		 * [example]
		 * (responseRecommendation "http://robot-arbi.kr/ontologies/complexService.owl#Person001" "http://knowrob.org/kb/knowrob.owl#Drink" (result (recommendation "http://robot-arbi.kr/ontologies/complexService.owl#_Water") (reason (disease "http://robot-arbi.kr/ontologies/isro_medical.owl#_Diabetes"))))
		 * 
		 */
		
		RecommendationArg arg = (RecommendationArg) o;
		
		String userID = toFullIRI(arg.getUserID());
		String targetAction = toFullIRI(arg.getTargetAction());
		String history = toFullIRI(arg.getHistory());

		HashSet<String> objSet = new HashSet<>();
		HashSet<String> exclusionSet = new HashSet<>();
		HashSet<String> hSet = new HashSet<>();
		HashSet<String> dSet = new HashSet<>();
		HashSet<String> candidateSet = new HashSet<>();
		
		String queryString;
		Query query;
		QueryExecution qe;
		ResultSet results;
		QuerySolution soln;
		
		GeneralizedListBuilder responseGL = new GeneralizedListBuilder("responseRecommendation");
		
		GeneralizedListBuilder resultGL = new GeneralizedListBuilder("result");
		GeneralizedListBuilder recommendationGL = new GeneralizedListBuilder("recommendation");
		GeneralizedListBuilder reasonGL = new GeneralizedListBuilder("reason");
		GeneralizedListBuilder diseaseGL = new GeneralizedListBuilder("disease");
		GeneralizedListBuilder historyGL = new GeneralizedListBuilder("history");
		
		if(targetAction.equals("http://knowrob.org/kb/knowrob.owl#Drink")) {
			queryString = ""
					+ PREFIX
					+ "SELECT ?obj ?exceptionD ?exceptionH ?d "
					+ "WHERE {"
					+ "{"
					+ "<"+userID+"> isro_social:prefer ?obj . "
					+ "?obj rdf:type ?objType . "
					+ "?objType rdfs:subClassOf knowrob:Drink . "
					+ "} UNION {"
					+ "<"+userID+"> isro_social:prefer ?exceptionD . "
					+ "<"+userID+"> isro_medical:getDisease ?d . "
					+ "?d rdf:type ?dType . "
					+ "?dType rdfs:subClassOf [a owl:Restriction; owl:onProperty isro_medical:shouldAvoid; owl:someValuesFrom knowrob:Sugar] . "
					+ "?exceptionD rdf:type ?exceptionDType . "
					+ "?exceptionDType rdfs:subClassOf [a owl:Restriction; owl:onProperty knowrob:hasIngredient; owl:someValuesFrom knowrob:Sugar] . "
					+ "} UNION {"
					+ "<"+history+"> rdf:type ?exceptionHType . "
					+ "?exceptionHType rdfs:subClassOf knowrob:Drink . "
					+ "?exceptionH rdf:type owl:NamedIndividual . "
					+ "?exceptionH rdf:type ?exceptionHType . "
					+ "FILTER regex(str(?exceptionH), \"#_\") . "
					+ "}"
					+ "}";
			query = QueryFactory.create(queryString);
			qe = QueryExecutionFactory.create(query, service_OntModel);

			results = qe.execSelect();
			while (results.hasNext()) {
				soln = results.nextSolution();
				try {
					objSet.add(soln.getResource("obj").getURI().toString());
				} catch (Exception e) {
					
				}
				try {
					exclusionSet.add(soln.getResource("exceptionD").getURI().toString());
				} catch (Exception e) {
					
				}
				try {
					hSet.add(soln.getResource("exceptionH").getURI().toString());
				} catch (Exception e) {
					
				}
				
				try {
					dSet.add(soln.getResource("d").getURI().toString());
				} catch (Exception e) {
					
				}

			}
			candidateSet.addAll(objSet);
			objSet.removeAll(exclusionSet);
			objSet.removeAll(hSet);
//			System.out.println(objSet);
			
			Iterator<String> iter1 = objSet.iterator();
			while(iter1.hasNext()) {
				recommendationGL.addExpression(GLFactory.newExpression(GLFactory.newValue(iter1.next().toString())));
			}
			
			resultGL.addExpression(GLFactory.newExpression(recommendationGL.toGeneralizedList()));
			
			if(!dSet.isEmpty()) {
				Iterator<String> iter = dSet.iterator();
				while(iter.hasNext()) {
					diseaseGL.addExpression(GLFactory.newExpression(GLFactory.newValue(iter.next().toString())));
				}
				reasonGL.addExpression(GLFactory.newExpression(diseaseGL.toGeneralizedList()));
			} else {
				diseaseGL.addExpression(GLFactory.newExpression(GLFactory.newValue("null")));
				reasonGL.addExpression(GLFactory.newExpression(diseaseGL.toGeneralizedList()));
			}
			if(!hSet.isEmpty()) {
				Iterator<String> iter = hSet.iterator();
				while(iter.hasNext()) {
					historyGL.addExpression(GLFactory.newExpression(GLFactory.newValue(iter.next().toString())));
				}
				reasonGL.addExpression(GLFactory.newExpression(historyGL.toGeneralizedList()));
			} else {
				historyGL.addExpression(GLFactory.newExpression(GLFactory.newValue("null")));
				reasonGL.addExpression(GLFactory.newExpression(historyGL.toGeneralizedList()));
			}
			resultGL.addExpression(GLFactory.newExpression(reasonGL.toGeneralizedList()));
			
		}
		Expression exp1 = GLFactory.newExpression(GLFactory.newValue(userID));
		Expression exp2 = GLFactory.newExpression(GLFactory.newValue(targetAction));
		
		responseGL.addExpression(exp1);
		responseGL.addExpression(exp2);
		responseGL.addExpression(GLFactory.newExpression(resultGL.toGeneralizedList()));
		
		JSONObject log = new JSONObject();

		log.put("KnowledgeBase", service_OwlFile);
		log.put("UserID", userID);
		log.put("TargetAction", targetAction);
		JSONArray candidateArray = new JSONArray();
		candidateArray.addAll(candidateSet);
		log.put("Candidate", candidateArray);
		
		JSONObject dLog = new JSONObject();
		JSONArray dArray = new JSONArray();
		dArray.addAll(dSet);
		dLog.put("Disease", dArray);
		JSONArray eArray = new JSONArray();
		eArray.addAll(exclusionSet);
		dLog.put("Exclusion", eArray);
		
		log.put("ExcludedByDisease", dLog);
		JSONArray hArray = new JSONArray();
		hArray.addAll(hSet);
		log.put("ExcludedByHistory", hArray);
		JSONArray oArray = new JSONArray();
		oArray.addAll(objSet);
		log.put("Recommendation", oArray);
		log.put("Ontology Scale", ontologyMonitor_Scale());
		log.put("GL", responseGL.toGeneralizedList().toString());
//		System.out.println(log.toJSONString());
		return log.toJSONString();
	}

}
