package kr.ac.hanyang.action;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.jena.ontology.Individual;
import org.apache.jena.ontology.OntClass;
import org.json.simple.JSONObject;

import kr.ac.hanyang.action.argument.IndividualArg;
/**
 * 
 * @author freebeing1
 *
 */
public class CreateIndividualAction extends KnowledgeProcessAction {
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {
		
		IndividualArg arg = (IndividualArg) o;
		String typeClassID = arg.getTypeClass();
		String individualID = arg.getIndividual();
		String result = "";
		
		OntClass typeClass = service_OntModel.getOntClass(typeClassID);
		
		if(typeClass == null) {
			
			System.out.println("<CREATE> : CLASS named \""+typeClassID+"\" is NOT defined.");
			result = "DENIED";
			
		} 
		else if(service_OntModel.getIndividual(individualID) != null) {
			
			System.out.println("<CREATE> : There is already an INDIVIDUAL named \""+individualID+"\" in SERVICE ONTOLOGY.");
			result = "DENIED";
			
		} 
		else if(!individualID.contains(PREFIX_service)) {
			
			System.out.println("<CREATE> : The definition of a new INDIVIDUAL is only possible within a SERVICE ONTOLOGY.");
			result = "DENIED";
			
		} 
		else {
			@SuppressWarnings("unused")
			Individual individual = service_OntModel.createIndividual(individualID, typeClass);
			// 온톨로지 반영
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

		log.put("Knowledge Base", service_OwlFile);
		log.put("typeClassID", typeClassID);
		log.put("individualID", individualID);
		log.put("Ontology Scale", ontologyMonitor_Scale());
		log.put("result", result);
		
		return log.toJSONString();
	}
}
