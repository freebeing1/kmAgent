package kr.ac.hanyang.action;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.jena.ontology.Individual;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.json.simple.JSONObject;

import kr.ac.hanyang.action.argument.IndividualArg;
/**
 * 
 * @author freebeing1
 *
 */
public class DeleteIndividualAction extends KnowledgeProcessAction {
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {
		IndividualArg arg = (IndividualArg) o;
		String individual = arg.getIndividual();
		String result = "";
		
		if(isInUse(individual)) {
			System.out.println("<DELETE> : This individual is still in use. Delete relations BEFORE deleting it.");
			result = "DENIED";
		}
		
		else if(!exists(individual)) {
			System.out.println("<DELETE> : Individual named \""+individual+"\" is NOT defined.\n");
			result = "DENIED";
		}
		
		else if(!individual.contains(PREFIX_service)) {
			System.out.println("<DELETE> : Only the individuals defined in SERVICE ONTOLOGY can be deleted.\n");
			result = "DENIED";
		}
		else {
			Individual targetIndividual = service_OntModel.getIndividual(individual);
			
			targetIndividual.remove();
			
			Writer fw;
			try {
				fw = new FileWriter(service_OwlFile);
				service_OntModel.write(fw, "RDF/XML");
			} catch (IOException e) {
				e.printStackTrace();
			}
			result = "SUCCESS";
		}
		
		
		JSONObject log = new JSONObject();

		log.put("KnowledgeBase", service_OwlFile);
		log.put("individual", individual);
		log.put("Ontology Scale", ontologyMonitor_Scale());
		log.put("result", result);
		
		return log.toJSONString();
	}

	
	private boolean exists(String individual) {
		
		boolean res = false;
		
		Individual checkI = service_OntModel.getIndividual(individual);
		if(checkI != null) {
			res = true;
		}
		
		return res;
	}


	private boolean isInUse(String individual) {
		
		boolean res = false;
		String op1 = null;
		String op2 = null;
		String dp = null;
		String queryString;
		Query query;
		QueryExecution qe;
		ResultSet results;
		
		queryString = ""
				+ PREFIX
				+ ""
				+ "SELECT ?p "
				+ "WHERE {"
				+ "?s ?p <" + individual + "> . "
				+ "?p rdf:type owl:ObjectProperty . "
				+ "}"
				+ "\n";
		query = QueryFactory.create(queryString);
		qe = QueryExecutionFactory.create(query, service_OntModel);
		results = qe.execSelect();
		while(results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			op1 = soln.getResource("p").getURI();
		}
		
		queryString = ""
				+ PREFIX
				+ ""
				+ "SELECT ?p "
				+ "WHERE {"
				+ "<" + individual + "> ?p ?o. "
				+ "?p rdf:type owl:ObjectProperty . "
				+ "}"
				+ "\n";
		query = QueryFactory.create(queryString);
		qe = QueryExecutionFactory.create(query, service_OntModel);
		results = qe.execSelect();
		while(results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			op2 = soln.getResource("p").getURI();
		}
		
		queryString = ""
				+ PREFIX
				+ ""
				+ "SELECT ?p "
				+ "WHERE {"
				+ "<" + individual + "> ?p ?o. "
				+ "?p rdf:type owl:DatatypeProperty . "
				+ "}"
				+ "\n";
		query = QueryFactory.create(queryString);
		qe = QueryExecutionFactory.create(query, service_OntModel);
		results = qe.execSelect();
		while(results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			dp = soln.getResource("p").getURI();
		}
		
		if(op1 != null || op2 != null || dp != null) {
			res = true;
		}
		
		return res;
	}
}
