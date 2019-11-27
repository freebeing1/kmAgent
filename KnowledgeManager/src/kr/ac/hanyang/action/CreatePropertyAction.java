package kr.ac.hanyang.action;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.rdf.model.Resource;
import org.json.simple.JSONObject;

import kr.ac.hanyang.action.argument.PropertyArg;

/**
 * 
 * @author freebeing1
 *
 */
public class CreatePropertyAction extends KnowledgeProcessAction{

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {
		
		String result = "";
		
		PropertyArg arg = (PropertyArg) o;
		String propertyType = arg.getPropertyType();
		String superProperty = arg.getSuperProperty();
		String propertyID = arg.getPropertyID();
		String domain = arg.getDomain();
		String range = arg.getRange();
		
		if(propertyType.equals("ObjectProperty")) {
			result = createObjectProperty(superProperty, propertyID, domain, range);
		}
		else if(propertyType.equals("DatatypeProperty")) {
			result = createDatatypeProperty(superProperty, propertyID, domain, range);
		}
		else {
			System.out.println("<CREATE> : Property Type [" + propertyType + "] NOT DEFINED\n");
			result = "DENIED"; 
		}
		
		JSONObject log = new JSONObject();

		log.put("KnowledgeBase", service_OwlFile);
		log.put("propertyType", propertyType);
		log.put("superProperty", superProperty);
		log.put("propertyID", propertyID);
		log.put("domain", domain);
		log.put("range", range);
		log.put("Ontology Scale", ontologyMonitor_Scale());
		log.put("result", result);
		
		return log.toJSONString();
	}

	
	/**
	 * 오브젝트 프로퍼티 생성 메서드
	 * 
	 * @param superProperty : 새로 만들 오브젝트 프로퍼티의 상위 프로퍼티
	 * @param propertyID : 새로 만들 오브젝트 프로퍼티의 IRI
	 * @param domain : 새로 만들 오브젝트 프로퍼티의 도메인
	 * @param range : 새로 만들 오브젝트 프로퍼티의 레인지
	 * @return
	 */
	private String createObjectProperty(String superProperty, String propertyID, String domain, String range) {
		
		if(!propertyID.contains(PREFIX_service)) {
			
			System.out.println("<CREATE> : The definition of a new OBJECT PROPERTY is only possible within a SERVICE ONTOLOGY.");
			return "DENIED";
			
		} else if(service_OntModel.getObjectProperty(propertyID) != null){
			
			System.out.println("<CREATE> : There is already an OBJECT PROPERTY named \""+propertyID+"\" in SERVICE ONTOLOGY.");
			return "DENIED";
			
		} else {
			ObjectProperty sop = service_OntModel.getObjectProperty(superProperty);
			ObjectProperty nop = service_OntModel.createObjectProperty(propertyID);
			sop.addSubProperty(nop);
			nop.addSuperProperty(sop);
			
			OntClass d;
			OntClass r;
			
			if(!domain.equals("$domain")) {
				d = service_OntModel.getOntClass(domain); // 통합용
				nop.addDomain(d);
			}
			if(!range.equals("$range")) {
				r = service_OntModel.getOntClass(range); // 통합용
				nop.addRange(r);
			}
			
			// 온톨로지 반영
			Writer fw;
			try {
				fw = new FileWriter(service_OwlFile);
				service_OntModel.write(fw, "RDF/XML");
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		}
		
		return "SUCCESS";
	}
	
	
	/**
	 * 데이터 프로퍼티 생성 메서드
	 * 
	 * @param superProperty : 새로 만들 데이터 프로퍼티의 상위 프로퍼티
	 * @param propertyID : 새로 만들 데이터 프로퍼티의 IRI
	 * @param domain : 새로 만들 데이터 프로퍼티의 도메인
	 * @param range : 새로 만들 데이터 프로퍼티의 레인지
	 * @return
	 */
	private String createDatatypeProperty(String superProperty, String propertyID, String domain, String range) {
		
		if(!propertyID.contains(PREFIX_service)) {
			System.out.println("<CREATE> : The definition of a new DATA PROPERTY is only possible within a SERVICE ONTOLOGY.");
			return "DENIED";
		} else if(service_OntModel.getDatatypeProperty(propertyID) != null){
			
			System.out.println("<CREATE> : There is already an DATA PROPERTY named \""+propertyID+"\" in SERVICE ONTOLOGY.");
			return "DENIED";
			
		} else {
			DatatypeProperty sdp = service_OntModel.getDatatypeProperty(superProperty);
			DatatypeProperty ndp = service_OntModel.createDatatypeProperty(propertyID);
			sdp.addSubProperty(ndp);
			ndp.addSuperProperty(sdp);
			
			OntClass d;
			Resource r;
			
			if(!domain.equals("$domain")) {
				d = service_OntModel.getOntClass(domain); // 통합용
				ndp.addDomain(d);
			}
			if(!range.equals("$range")) {
				r = service_OntModel.getOntClass(range); // 통합용
				ndp.addRange(r);
			}
			
			// 온톨로지 반영
			Writer fw;
			try {
				fw = new FileWriter(service_OwlFile);
				service_OntModel.write(fw, "RDF/XML");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		return "SUCCESS";
	}
	
}
