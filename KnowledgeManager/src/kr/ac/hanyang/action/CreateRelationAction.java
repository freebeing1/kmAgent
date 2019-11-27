package kr.ac.hanyang.action;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import org.json.simple.JSONObject;

import kr.ac.hanyang.action.argument.TripleArg;


/**
 * 
 * @author freebeing1
 *
 */
public class CreateRelationAction extends KnowledgeProcessAction {
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {

		TripleArg arg = (TripleArg) o;
		String subject = arg.getSubject();
		String predicate = arg.getPredicate();
		String object = arg.getObject();
		
		String propertyType = "";
		String result = "";
		
		String sType = "";
		
		OntClass subjectCls;
		@SuppressWarnings("unused")
		Individual subjectI;

		String[] subjectName = subject.split("#");
		String strIndividualID = "";
		
		// subject가 "클래스"인 경우와 "인디비주얼"인 경우를 구분해서 처리.
		String queryString = ""
				+ PREFIX
				+ ""
				+ "SELECT ?type "
				+ "WHERE {"
				+ "<"+subject+"> rdf:type ?type . "
				+ "}"
				+ "\n\n";
		Query query = QueryFactory.create(queryString);
//		QueryExecution qe = QueryExecutionFactory.create(query, DemoKM_OntModel); // 연구실 데모용
		QueryExecution qe = QueryExecutionFactory.create(query, service_OntModel); // 통합용
		
		ResultSet results = qe.execSelect();
		while(results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			sType = soln.getResource("type").getURI();
		}
		
		// subject가 "클래스"인 경우.
		if(sType.contains("Class")) {
			// 다음 인덱스 찾기
			String nextIndex = getNextIndex(subject);

			// 인디비주얼 ID
			strIndividualID = PREFIX_service + subjectName[1] + nextIndex;

			// 인디비주얼 생성
//			subjectCls = DemoKM_OntModel.getOntClass(subject); // 연구실 데모용
//			subjectI = DemoKM_OntModel.createIndividual(strIndividualID, subjectCls); // 연구실 데모용
			subjectCls = service_OntModel.getOntClass(subject); // 통합용
			subjectI = service_OntModel.createIndividual(strIndividualID, subjectCls); // 통합용
			
			// 온톨로지 반영
			Writer fw;
			try {
//				fw = new FileWriter(DemoKM_OwlFile); // 연구실 데모용
//				DemoKM_OntModel.write(fw, "RDF/XML"); // 연구실 데모용
				fw = new FileWriter(service_OwlFile); // 통합용
				service_OntModel.write(fw, "RDF/XML"); // 통합용
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// subject가 "인디비주얼"인 경우.
		else {
			strIndividualID = subject;
		}

		
		/*
		 *  (predicate $p $o)의 $p가 "knowrob:startTime"인 경우,
		 *  $o 는 "yyyy-MM-dd'T'HH:mm:ss" 의 형식으로 이루어져있어야 하며
		 *  그것을 unix time으로 변환하여 TimePoint individual을 만들어 붙이도록 예외처리
		 *  
		 *  [Example]
		 *  (predicate "http://knowrob.org/kb/knowrob.owl#startTime" "2018-06-01T17:54:24") 
		 */
		if(predicate.equals("http://knowrob.org/kb/knowrob.owl#startTime")) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
			Date date = null;
			try {
				date = sdf.parse(object);
			} catch (java.text.ParseException e) {
				e.printStackTrace();
			}
			long uTime = date.getTime() / 1000;
//			object = DemoKM_IRI + "TimePoint_" + uTime; // 연구실 데모용
			object = PREFIX_service + "TimePoint_" + uTime; // 통합용
			
			// TimePoint 인디비주얼 생성
//			OntClass timePointCls = DemoKM_OntModel.getOntClass("http://knowrob.org/kb/knowrob.owl#TimePoint"); // 연구실 메도용
//			Individual timePointI = DemoKM_OntModel.createIndividual(object, timePointCls); // 연구실 데모용
			OntClass timePointCls = service_OntModel.getOntClass("http://knowrob.org/kb/knowrob.owl#TimePoint"); // 통합용
			@SuppressWarnings("unused")
			Individual timePointI = service_OntModel.createIndividual(object, timePointCls); // 통합용
			
			// 온톨로지 반영
			Writer fw;
			try {
//				fw = new FileWriter(DemoKM_OwlFile); // 연구실 데모용
//				DemoKM_OntModel.write(fw, "RDF/XML"); // 연구실 데모용
				fw = new FileWriter(service_OwlFile); // 통합용
				service_OntModel.write(fw, "RDF/XML"); // 통합용
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		// predicate이 "오브젝트 프로퍼티"인 경우와 "데이터 프로퍼티"인 경우를 구분해서 처리.
		queryString = ""
				+ PREFIX
				+ ""
				+ "SELECT ?type "
				+ "WHERE {"
				+ "<"+predicate+"> rdf:type ?type . "
				+ "}"
				+ "\n\n";
		
		query = QueryFactory.create(queryString);
//		qe = QueryExecutionFactory.create(query, DemoKM_OntModel); // 연구실 데모용
		qe = QueryExecutionFactory.create(query, service_OntModel); // 통합용
		results = qe.execSelect();
		
		while(results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			propertyType = soln.getResource("type").getURI();
		}
		
		if(propertyType.contains("ObjectProperty")) {
			result = createObjectRelation(subject, predicate, object);			
		} 
		else if (propertyType.contains("DatatypeProperty")){
			result = createDatatypeRelation(subject, predicate, object);
		} 
		else {
			System.out.println("<CREATE> : PROPERTY Type [" + propertyType + "] is NOT defined.");
			result = "DENIED";
		}
		
		JSONObject log = new JSONObject();
		JSONObject tripleLog = new JSONObject();
		
		tripleLog.put("subject", subject);
		tripleLog.put("predicate", predicate);
		tripleLog.put("object", object);
		
		log.put("KnowledgeBase", service_OwlFile);
		log.put("triple", tripleLog);
		log.put("Ontology Scale", ontologyMonitor_Scale());
		log.put("result", result);
		
		return log.toJSONString();
	}
	
	
	/**
	 * 
	 * @param subject
	 * @return next index of the individual
	 */
	private String getNextIndex(String subject) {
		
		String queryString = ""
				+ PREFIX
				+ "SELECT ?target "
				+ "WHERE {"
				+ "?target rdf:type <"+subject+"> . "
				+ "FILTER regex(str(?target), \""+PREFIX_service+"\") . "
				+ "}";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, service_OntModel);
		
		ResultSet results = qe.execSelect();
		int maxIndex = 0;
		int thisIndex;
		while(results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			String targetIndividual = soln.getResource("target").getURI();
			thisIndex = Integer.parseInt(targetIndividual.split((String)PREFIX_service)[1]);
			if(thisIndex > maxIndex) {
				maxIndex = thisIndex;
			}
		}
		int nextIndex = maxIndex + 1;
		
		String nextIndexStr = Integer.toString(nextIndex);
		for(int i=0;i<3-nextIndexStr.length();i++) {
			nextIndexStr = "0" + nextIndexStr;
		}
		
		return nextIndexStr;
	}


	/**
	 * 
	 * @param s subject
	 * @param p predicate(datatype property)
	 * @param o object
	 * @return "COMPLETE"
	 */
	private String createDatatypeRelation(String s, String p, String o) {

		Individual si = service_OntModel.getIndividual(s);
		DatatypeProperty dp = service_OntModel.getDatatypeProperty(p);
		
		si.addProperty(dp, o);
		
		Writer fw;
		try {
			fw = new FileWriter(service_OwlFile);
			service_OntModel.write(fw, "RDF/XML");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "SUCCESS";
	}

	
	/**
	 * 
	 * @param s subject
	 * @param p predicate(object property)
	 * @param o object
	 * @return "COMPLETE"
	 */
	private String createObjectRelation(String s, String p, String o) {

		Individual si = service_OntModel.getIndividual(s);
		ObjectProperty op = service_OntModel.getObjectProperty(p);
		Individual oi = service_OntModel.getIndividual(o);
		
		si.addProperty(op, oi);
		
		Writer fw;
		try {
			fw = new FileWriter(service_OwlFile);
			service_OntModel.write(fw, "RDF/XML");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "SUCCESS";
		
	}
		
}


