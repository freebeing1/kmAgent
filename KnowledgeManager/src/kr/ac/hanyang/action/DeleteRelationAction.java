package kr.ac.hanyang.action;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.json.simple.JSONObject;

import kr.ac.hanyang.action.argument.TripleArg;

/**
 * 
 * @author freebeing1
 *
 */
public class DeleteRelationAction extends KnowledgeProcessAction {

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {
		// TODO Auto-generated method stub
		TripleArg arg = (TripleArg) o;
		String subject = arg.getSubject();
		String predicate = arg.getPredicate();
		String object = arg.getObject();
		String result = "";
		
		Individual si = service_OntModel.getIndividual(subject);
		String propertyType = propertyTypeOf(predicate);
		
		if(si == null) {
			System.out.println("<DELETE> : Individual named \"" + predicate + "\" NOT defined.\n");
			result = "DENIED";
		}
		else if(propertyType.contains("ObjectProperty")) {
			
			ObjectProperty op = service_OntModel.getObjectProperty(predicate);
			if(op == null) {
				System.out.println("<DELETE> : Property named \"" + predicate + "\" NOT defined.\n");
				result = "DENIED";
			}
			else {
				Individual oi = service_OntModel.getIndividual(object);
				si.removeProperty(op, oi);
				result = "SUCCESS";
			}
			
		} 
		else if(propertyType.contains("DatatypeProperty")) {
			
			DatatypeProperty dp = service_OntModel.getDatatypeProperty(predicate);
			if(dp == null) {
				System.out.println("<DELETE> : Property named \"" + predicate + "\" NOT defined.\n");
				result = "DENIED";	
			}
			else {
				Literal od = service_OntModel.createLiteral(object);
				si.removeProperty(dp, od);
				result = "SUCCESS";
			}
			
			
		} 
		else {
			System.out.println("<DELETE> : Property named \"" + predicate + "\" NOT defined.\n");
			result = "DENIED";	
		}
		
		Writer fw;
		try {
			fw = new FileWriter(service_OwlFile);
			service_OntModel.write(fw, "RDF/XML");
		} catch (IOException e) {
			e.printStackTrace();
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

	private String propertyTypeOf(String predicate) {
		String type = "";
		String queryString = ""
				+ PREFIX
				+ ""
				+ "SELECT ?type "
				+ "WHERE {"
				+ "<"+predicate+"> rdf:type ?type . "
				+ "}"
				+ "\n\n";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, service_OntModel); // 통합용
		ResultSet results = qe.execSelect();
		
		while(results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			type = soln.getResource("type").getURI();
		}
		
		return type;
	}

}
