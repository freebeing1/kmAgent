package kr.ac.hanyang.action;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.json.simple.JSONObject;

import kr.ac.hanyang.action.argument.PropertyArg;

/**
 * 
 * @author freebeing1
 *
 */

public class DeletePropertyAction extends KnowledgeProcessAction{

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {
		PropertyArg arg = (PropertyArg) o;
		String pType = arg.getPropertyType();
		String pID = arg.getPropertyID();
		String result = "";
		
		if(isInUse(pID)) {
			System.out.println("<DELETE> : This property is still in use. Delete relations BEFORE deleting it.");
			result = "DENIED";
		}
		else if(hasSubProperty(pID)) {
			System.out.println("<DELETE> : This property is still in use. Delete relations BEFORE deleting it.");
			result = "DENIED";
		}
		else if(!pID.contains(PREFIX_service)) {
			System.out.println("<DELETE> : Only the properties defined in SERVICE ONTOLOGY can be deleted.");
			result = "DENIED";
		}
		else if(pType.equals("ObjectProperty")) {
			
			ObjectProperty op = service_OntModel.getObjectProperty(pID);
			
			if(op == null) {
				System.out.println("<DELETE> : ObjectProperty named \"" + pID + "\" is NOT defined.");
				return "DENIED";
			}
			
			op.remove();
			result = "SUCCESS";
			
		}
		else if(pType.contains("DatatypeProperty")) {
			DatatypeProperty dp = service_OntModel.getDatatypeProperty(pID);
			
			if(dp == null) {
				System.out.println("<DELETE> : DatatypeProperty named \"" + pID + "\" is NOT defined.");
				return "DENIED";
			}
			
			dp.remove();
			result = "SUCCESS";
		} 
		
		else {
			System.out.println("<DELETE> : Property type \"" + pType + "\" NOT defined.");
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

		log.put("KnowledgeBase", service_OwlFile);
		log.put("propertyType", pType);
		log.put("propertyID", pID);
		log.put("Ontology Scale", ontologyMonitor_Scale());
		log.put("result", result);
		
		return log.toJSONString();
	}
	
	

	private boolean isInUse(String pID) {
		// has relations
		boolean result = false;
		String obj = null;
		String queryString = ""
				+ PREFIX
				+ ""
				+ "SELECT ?o "
				+ "WHERE {"
				+ "?s <" + pID + "> ?o . "
				+ "}"
				+ "\n";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, service_OntModel);
		ResultSet results = qe.execSelect();
		while(results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			obj = soln.getResource("o").getURI();
		}
		if(obj != null) {
			result = true;
		}
		return result;
	}
	
	private boolean hasSubProperty(String pID) {
		// has sub property
		boolean result = false;
		String sp = null;
		String queryString = ""
				+ PREFIX
				+ ""
				+ "SELECT ?sub "
				+ "WHERE {"
				+ "?sub rdfs:subPropertyOf <" + pID + "> . " 
				+ "}"
				+ "\n";
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, service_OntModel);
		ResultSet results = qe.execSelect();
		while(results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			sp = soln.getResource("sub").getURI();
		}
		if(sp != null) {
			result = true;
		}
		return result;
	}

}
