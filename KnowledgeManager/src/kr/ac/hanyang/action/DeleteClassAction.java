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

public class DeleteClassAction extends KnowledgeProcessAction{

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {
		ClassArg arg = (ClassArg) o;
		String scID = arg.getSuperClass();
		String tcID = arg.getTargetClass();
		String result = "";

		OntClass superClass = null;
		OntClass targetClass = null;

		superClass = service_OntModel.getOntClass(scID);
		targetClass = service_OntModel.getOntClass(tcID);

		if(superClass == null) {
			System.out.println("<DELETE> : Class named \""+scID+"\" is NOT defined.\n");
			result = "DENIED";
		}
		else if(targetClass == null) {
			System.out.println("<DELETE> : Class named \""+tcID+"\" is NOT defined.\n");
			result = "DENIED";
		}
		else if(!tcID.contains(PREFIX_service)) {
			System.out.println("<DELETE> : Only the classes defined in SERVICE ONTOLOGY can be deleted.\n");
			result = "DENIED";
		}
		else if(targetClass.hasSubClass()) {
			System.out.println("<DELETE> : Only the classes which do NOT have SUBCLASS can be deleted.\n");
			return "DENIED";
		}
		else {
			// 클래스 삭제
			targetClass.remove();

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
		log.put("superClassID", scID);
		log.put("targetClassID", tcID);
		log.put("Ontology Scale", ontologyMonitor_Scale());
		log.put("result", result);
		
		return log.toJSONString();


	}

}
