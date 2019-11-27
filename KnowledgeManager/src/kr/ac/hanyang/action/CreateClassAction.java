package kr.ac.hanyang.action;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.jena.ontology.OntClass;
import org.json.simple.JSONObject;

import kr.ac.hanyang.action.argument.ClassArg;
/**
 * 
 * @author freebeing1
 *
 */
public class CreateClassAction extends KnowledgeProcessAction {
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {
		
		ClassArg arg = (ClassArg) o;
		String superClassID = arg.getSuperClass();
		String newClassID = arg.getTargetClass();
		String result = "";
		
		
		OntClass superClass = null;
		OntClass newClass = null;
		
		superClass = service_OntModel.getOntClass(superClassID);
		
		if(superClass == null) {
			System.out.println("<CREATE> : There is no class named \"" + superClassID +"\".");
			result = "DENIED";
		} 
		else if(!newClassID.contains(PREFIX_service)) {
			System.out.println("<CREATE> : The definition of a new CLASS is only possible within SERVICE ONTOLOGY.");
			result = "DENIED";	
		}
		else if(service_OntModel.getOntClass(newClassID) != null) {
			System.out.println("<CREATE> : There is already a CLASS named \""+newClassID+"\" in SERVICE ONTOLOGY.");
			result = "DENIED";
		}
		else {
			newClass = service_OntModel.createClass(newClassID);
			// rdfs:subClassOf
			// 서브클래스 관계 연결
			superClass.addSubClass(newClass);

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

		log.put("KnowledgeBase", service_OwlFile);
		log.put("superClassID", superClassID);
		log.put("newClassID", newClassID);
		log.put("Ontology Scale", ontologyMonitor_Scale());
		log.put("result", result);
		
		return log.toJSONString();
		
	}
}
