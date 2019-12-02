package kr.ac.hanyang.action;

import java.util.HashMap;

import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.ModelFactory;
import org.json.simple.JSONObject;

import kr.ac.uos.ai.arbi.agent.logger.ActionBody;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedListBuilder;

/**
 * 
 * @author freebeing1
 * @description 모든 액션이 공유하는 객체 & 모든 액션이 상속받아 사용하는 공용 메서드 정의
 */
public abstract class KnowledgeProcessAction implements ActionBody{
	
	private static boolean KMTEST = true;
	
	// ------------------- //
	// SPARQL query prefix //
	// ------------------- //
	
	protected static String PREFIX = ""
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n"
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
			+ "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"

            + "PREFIX knowrob: <http://knowrob.org/kb/knowrob.owl#> \n"
            + "PREFIX ourk: <http://kb.OntologyUnifiedRobotKnowledge.org#> \n"

            + "PREFIX isro: <http://robot-arbi.kr/ontologies/isro.owl#> \n"
            + "PREFIX isro_social: <http://robot-arbi.kr/ontologies/isro_social.owl#> \n"
            + "PREFIX isro_medical: <http://robot-arbi.kr/ontologies/isro_medical.owl#> \n"
            + "PREFIX isro_SD: <http://robot-arbi.kr/ontologies/isro_SD.owl#> \n"
//          + "PREFIX isro_cap: <http://robot-arbi.kr/ontologies/isro_cap.owl#> \n"
//          + "PREFIX isro_map: <http://robot-arbi.kr/ontologies/isro_map.owl#> \n"
//          + "PREFIX isro_cloud: <http://robot-arbi.kr/ontologies/isro_cloud.owl#> \n"
//			+ "PREFIX service: <http://robot-arbi.kr/ontologies/complexService.owl#> \n"
            + "PREFIX DemoKM: <http://robot-arbi.kr/ontologies/DemoKM.owl#> \n\n";
	
	
	
	// ------------- //
	// Ontology List //
	// ------------- //
	
	protected static String owlLocation = "owl/";
	protected static String owlCoreLocation = "core/";
	protected static String owlServicePackageLocation = "ServicePackage/";
	
	protected static final String ISRO_OwlFile = owlLocation + owlCoreLocation + "isro.owl"; // 지식베이스 owl 파일
	protected static final String ISRO_social_OwlFile = owlLocation + owlCoreLocation + "isro_social.owl"; // 소셜 지식 owl 파일
	protected static final String ISRO_medical_OwlFile = owlLocation + owlCoreLocation + "isro_medical.owl"; // 의학관련 지식 owl 파일
	protected static final String ISRO_SD_OwlFile = owlLocation + owlCoreLocation + "isro_SD.owl"; // 서비스 디스크립션 관련 owl 파일
	protected static final String ISRO_VS_OwlFile = owlLocation + owlCoreLocation + "isro_vs.owl"; // virtual sensor 관련 owl 파일
	
//	protected static final String DemoKM_OwlFile = owlLocation + "DemoKM.owl"; // 연구실 데모용 owl 파일
//	protected static final String Rec_OwlFile = owlLocation + "Rec.owl"; // 연구실 데모 인식데이터 저장용 owl 파일
	
	// Service Initialize 과정에서 Service Package Dispatcher 로부터 배포받는 owl 파일  
	protected static String ISRO_cap_OwlFile; // 로봇 capability 관련 owl 파일
	protected static String ISRO_map_OwlFile; // 맵 지식 owl 파일
	protected static String service_OwlFile; // 서비스 owl 파일
	protected static String ISRO_cloud_OwlFile; // 클라우드 인터페이스 스키마 owl 파일

	
	
	// -------------- //
	// Ontology Model //
	// -------------- //
	
	protected static final OntModel ISRO_OntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	protected static final OntModel ISRO_social_OntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	protected static final OntModel ISRO_medical_OntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	protected static final OntModel ISRO_map_OntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	protected static final OntModel ISRO_cap_OntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	protected static final OntModel ISRO_SD_OntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	protected static final OntModel ISRO_VS_OntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	protected static final OntModel DemoKM_OntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	protected static final OntModel Rec_OntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
	protected static final OntModel service_OntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM_TRANS_INF);
	protected static final OntModel ISRO_cloud_OntModel = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);

	
	
	// -------- //
	// IRI List //
	// -------- //
	
	protected static final String PREFIX_xsd = "http://www.w3.org/2001/XMLSchema#";
	protected static final String PREFIX_owl = "http://www.w3.org/2002/07/owl#";
	protected static final String PREFIX_rdf = "http://www.w3.org/1999/02/22-rdf-syntax-ns#";
	protected static final String PREFIX_rdfs = "http://www.w3.org/2000/01/rdf-schema#";
	
	protected static final String PREFIX_knowrob = "http://knowrob.org/kb/knowrob.owl#";
	
	protected static final String PREFIX_isro = "http://robot-arbi.kr/ontologies/isro.owl#";
	protected static final String PREFIX_isro_social = "http://robot-arbi.kr/ontologies/isro_social.owl#";
	protected static final String PREFIX_isro_medical = "http://robot-arbi.kr/ontologies/isro_medical.owl#";
	protected static final String PREFIX_isro_SD = "http://robot-arbi.kr/ontologies/isro_SD.owl#";
	protected static final String PREFIX_isro_VS = "http://robot-arbi.kr/ontologies/isro_vs.owl#";
	
	// Service Initialize 과정에서 Service Package Dispatcher 로부터 배포받는 owl 파일 확인 후 초기화
	protected static String PREFIX_isro_cap; // = "http://robot-arbi.kr/ontologies/isro_cap.owl#";
	protected static String PREFIX_isro_map; // = "http://robot-arbi.kr/ontologies/isro_map.owl#";
	protected static String PREFIX_isro_cloud; // = "http://robot-arbi.kr/ontologies/isro_cloud.owl#";
	protected static String PREFIX_service; // = "http://robot-arbi.kr/ontologies/complexService.owl#";
	
	// 연구실 데모용
	protected static final String PREFIX_DemoKM = "http://robot-arbi.kr/ontologies/DemoKM.owl#";
	
	
	public KnowledgeProcessAction(){
		
	}
	
	/**
	 * 
	 * A method to obtain the semantic map information
	 * 
	 * @return (semanticMap (triple S P O) (triple S P O) ... )
	 * 
	 */
	protected static String mapIndividual() {
		String queryString_1 = "";
		String queryString_2 = "";
		

		queryString_1 = ""
				+ PREFIX
				+ "SELECT ?x ?y ?z "
				+ "WHERE {"
				+ "?x ?y ?z . "
				+ "?x rdf:type owl:NamedIndividual . "
				+ "?y rdf:type owl:ObjectProperty . "
				+ "}";
		Query query_1 = QueryFactory.create(queryString_1);
		QueryExecution qe_1 = QueryExecutionFactory.create(query_1, ISRO_map_OntModel);
		ResultSet results_1 = qe_1.execSelect();
		

		queryString_2 = ""
				+ PREFIX
				+ "SELECT ?x ?y ?z "
				+ "WHERE {"
				+ "?x ?y ?z . "
				+ "?x rdf:type owl:NamedIndividual . "
				+ "?y rdf:type owl:DatatypeProperty . "
				+ "}";
		Query query_2 = QueryFactory.create(queryString_2);
		QueryExecution qe_2 = QueryExecutionFactory.create(query_2, ISRO_map_OntModel);
		ResultSet results_2 = qe_2.execSelect();
		
		GeneralizedListBuilder resultBuilder = new GeneralizedListBuilder("semanticMap");
		
		while(results_1.hasNext()) {
			QuerySolution solution = results_1.nextSolution();
			String nameSpace = solution.getResource("x").getNameSpace();
			if(nameSpace.equals(PREFIX_isro_map)) {
				GeneralizedListBuilder builder = new GeneralizedListBuilder();
				builder.setName("triple");
				Expression exp1 = GLFactory.newValueExpression(solution.getResource("x").getURI());
				Expression exp2 = GLFactory.newValueExpression(solution.getResource("y").getURI());
				Expression exp3 = GLFactory.newValueExpression(solution.getResource("z").getURI());
				builder.addExpression(exp1);
				builder.addExpression(exp2);
				builder.addExpression(exp3);
				Expression exp0 = GLFactory.newExpression(builder.toGeneralizedList());
				resultBuilder.addExpression(exp0);
			}
		}
		
		while(results_2.hasNext()) {
			QuerySolution solution = results_2.nextSolution();
			String nameSpace = solution.getResource("x").getNameSpace();
			if(nameSpace.equals(PREFIX_isro_map)) {
				GeneralizedListBuilder builder = new GeneralizedListBuilder();
				builder.setName("triple");
				Expression exp1 = GLFactory.newValueExpression(solution.getResource("x").getURI());
				Expression exp2 = GLFactory.newValueExpression(solution.getResource("y").getURI());
				Expression exp3 = GLFactory.newValueExpression(solution.getLiteral("z").getValue().toString());
				builder.addExpression(exp1);
				builder.addExpression(exp2);
				builder.addExpression(exp3);
				Expression exp0 = GLFactory.newExpression(builder.toGeneralizedList());
				resultBuilder.addExpression(exp0);
			}
		}
		
		return resultBuilder.toGeneralizedList().toString();
	}
	
	/**
	 * 
	 * A method to obtain the knowledge base schema information
	 * 
	 * @return (ontologySchema (triple S P O) (triple S P O) ... )
	 * 
	 */
	protected static String ontologySchema() {
		String queryString = ""
				+ PREFIX
				+ "SELECT ?x ?y ?z "
				+ "WHERE {"
				+ "?x rdf:type owl:Class . "
				+ "FILTER regex(str(?y), \"subClassOf\") . "
				+ "}";
		
		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, ISRO_OntModel);
		ResultSet results = qe.execSelect();
		
		GeneralizedListBuilder resultBuilder = new GeneralizedListBuilder("ontologySchema");
		
		while(results.hasNext()) {
			QuerySolution solution = results.nextSolution();
			GeneralizedListBuilder builder = new GeneralizedListBuilder();
			builder.setName("triple");
			Expression exp1 = GLFactory.newValueExpression(solution.getResource("x").getURI());
			Expression exp2 = GLFactory.newValueExpression(solution.getResource("y").getURI());
			Expression exp3 = GLFactory.newValueExpression(solution.getResource("z").getURI());
			builder.addExpression(exp1);
			builder.addExpression(exp2);
			builder.addExpression(exp3);
			Expression exp0 = GLFactory.newExpression(builder.toGeneralizedList());
			resultBuilder.addExpression(exp0);
		}
		
		return resultBuilder.toGeneralizedList().toString();
	}
	
	
	/**
	 * 
	 * @param uri : full IRI of an entity
	 * @return Shortened IRI
	 */
	protected static String toShortenedIRI(String uri) {
		String iri = null;
		
		if(uri.contains(PREFIX_xsd)) {
			iri = "xsd:" + uri.split("#")[1];
		} else if(uri.contains(PREFIX_owl)) {
			iri = "owl:" + uri.split("#")[1];
		} else if(uri.contains(PREFIX_rdf)) {
			iri = "rdf:" + uri.split("#")[1];
		} else if(uri.contains(PREFIX_rdfs)) {
			iri = "rdfs:" + uri.split("#")[1];
		} else if(uri.contains(PREFIX_knowrob)) {
			iri = "knowrob:" + uri.split("#")[1];
		} else if(uri.contains(PREFIX_isro)) {
			iri = "isro:" + uri.split("#")[1];
		} else if(uri.contains(PREFIX_isro_map)) {
			iri = "isro_map:" + uri.split("#")[1];
		} else if(uri.contains(PREFIX_isro_cap)) {
			iri = "isro_cap:" + uri.split("#")[1];
		} else if(uri.contains(PREFIX_isro_cloud)) {
			iri = "isro_cloud:" + uri.split("#")[1];
		} else if(uri.contains(PREFIX_isro_social)) {
			iri = "isro_social:" + uri.split("#")[1];
		} else if(uri.contains(PREFIX_isro_medical)) {
			iri = "isro_medical:" + uri.split("#")[1];
		} else if(uri.contains(PREFIX_service)) {
			iri = "complexService:" + uri.split("#")[1];
		} else {
			iri = uri;
		}
		
		return iri;
	}
	
	protected static String toFullIRI(String uri) {
		String iri = null;
		if(uri.contains("xsd:")) {
			iri = PREFIX_xsd + uri.split(":")[1];
		}
		else if(uri.contains("owl:")) {
			iri = PREFIX_owl + uri.split(":")[1];
		}
		else if(uri.contains("rdf:")) {
			iri = PREFIX_rdf + uri.split(":")[1];
		}
		else if(uri.contains("rdfs:")) {
			iri = PREFIX_rdfs + uri.split(":")[1];
		}
		else if(uri.contains("knowrob:")) {
			iri = PREFIX_knowrob + uri.split(":")[1];
		}
		else if(uri.contains("isro:")) {
			iri = PREFIX_isro + uri.split(":")[1];
		}
		else if(uri.contains("isro_map:")) {
			iri = PREFIX_isro_map + uri.split(":")[1];
		}
		else if(uri.contains("isro_cap:")) {
			iri = PREFIX_isro_cap + uri.split(":")[1];
		}
		else if(uri.contains("isro_cloud:")) {
			iri = PREFIX_isro_cloud + uri.split(":")[1];
		}
		else if(uri.contains("isro_social:")) {
			iri = PREFIX_isro_social + uri.split(":")[1];
		}
		else if(uri.contains("isro_medical:")) {
			iri = PREFIX_isro_medical + uri.split(":")[1];
		}
		else if(uri.contains("complexService:")) {
			iri = PREFIX_service + uri.split(":")[1];
		}
		
		
		else {
			iri = uri;
		}
		
		return iri;
	}
	
	
	/**
	 * class count
	 * object property count
	 * data property count
	 * individual count
	 * 
	 * @return a JSONObject of the ontology scale information
	 */
	@SuppressWarnings("unchecked")
	protected static JSONObject ontologyMonitor_Scale() {
		JSONObject scale = new JSONObject();
		
		String queryString = ""
				+ PREFIX
				+ "SELECT "
				+ "(COUNT(?c) AS ?Class) (COUNT(?op) AS ?ObjectProperty) (COUNT(?dp) AS ?DatatypeProperty) (COUNT(?ni) AS ?NamedIndividual) "
				+ "WHERE {"
				+ "{" 
				+ "?c rdf:type owl:Class . " 
				+ "} UNION {"
				+ "?op rdf:type owl:ObjectProperty . "
				+ "} UNION {"
				+ "?dp rdf:type owl:DatatypeProperty . "
				+ "} UNION {"
				+ "?ni rdf:type owl:NamedIndividual . "
				+ "}"
				+ "}";

		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, service_OntModel);
		ResultSet results = qe.execSelect();
		
		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			scale.put("ClassCount", soln.getLiteral("Class").getInt());
			scale.put("ObjectPropertyCount", soln.getLiteral("ObjectProperty").getInt());
			scale.put("DataPropertyCount", soln.getLiteral("DatatypeProperty").getInt());
			scale.put("IndividualCount", soln.getLiteral("NamedIndividual").getInt());
		}
		
		return scale;
	}
	
	public static HashMap<String, String> ontologyMonitor_Individual(String individual) {

		HashMap<String, String> map = new HashMap<String, String>();
		
		individual = toFullIRI(individual);
		
		String queryString = "" 
				+ PREFIX 
				+ "SELECT " 
				+ "?typeClass "
//				+ "?superClass " 
				+ "WHERE {" 
				+ "<"+individual+"> rdf:type ?typeClass . "
				+ "FILTER (?typeClass != owl:NamedIndividual) . "
//				+ "?typeClass rdfs:subClassOf ?superClass . "
				+ "}"
				+ "";

		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, service_OntModel);

		ResultSet results = qe.execSelect();
		
		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			String str1 =  soln.getResource("typeClass").getURI();
//			String str2 = soln.getResource("superClass").getURI();
			
			map.put("typeClass", str1);
//			try {
//				map.put("superClass", str2);
//			} catch (Exception e) {
//				map.put("superClass", "");
//			}
		}
		
		return map;
	}
	
	public static void load() {
		ISRO_OntModel.read(ISRO_OwlFile);
		ISRO_social_OntModel.read(ISRO_social_OwlFile);
		ISRO_medical_OntModel.read(ISRO_medical_OwlFile);
		ISRO_SD_OntModel.read(ISRO_SD_OwlFile);
		ISRO_VS_OntModel.read(ISRO_VS_OwlFile);

		if(KMTEST) {
			String owlServicePackageLocation_forTest = "ServicePackage_KMtest/";

			PREFIX_isro_cap = "http://robot-arbi.kr/ontologies/isro_cap.owl#";
			PREFIX_isro_map = "http://robot-arbi.kr/ontologies/isro_map.owl#";
			PREFIX_isro_cloud = "http://robot-arbi.kr/ontologies/isro_cloud.owl#"; // 통합용
			PREFIX_service = "http://robot-arbi.kr/ontologies/complexService.owl#"; // 통합용

			ISRO_cap_OwlFile = owlLocation + owlServicePackageLocation_forTest + "isro_cap.owl";
			ISRO_map_OwlFile = owlLocation + owlServicePackageLocation_forTest + "isro_map.owl";
			ISRO_cloud_OwlFile = owlLocation + owlServicePackageLocation_forTest + "isro_cloud.owl";
			service_OwlFile = owlLocation + owlServicePackageLocation_forTest + "complexService.owl";

			ISRO_map_OntModel.read(ISRO_map_OwlFile);
			ISRO_map_OntModel.addSubModel(ISRO_OntModel);
			System.out.println("<INIT>: MAP ontology model is loaded.");
			
			ISRO_cap_OntModel.read(ISRO_cap_OwlFile);
			ISRO_cap_OntModel.addSubModel(ISRO_OntModel);
			System.out.println("<INIT>: CAPABILITY ontology model is loaded.");
			
			ISRO_cloud_OntModel.read(ISRO_cloud_OwlFile);
			ISRO_cloud_OntModel.addSubModel(ISRO_OntModel);
			System.out.println("<INIT>: CLOUD ontology model is loaded.");
			
			service_OntModel.read(service_OwlFile);
			
			service_OntModel.addSubModel(ISRO_OntModel);
			service_OntModel.addSubModel(ISRO_social_OntModel);
			service_OntModel.addSubModel(ISRO_medical_OntModel);
			service_OntModel.addSubModel(ISRO_SD_OntModel);
			service_OntModel.addSubModel(ISRO_VS_OntModel);
			
			service_OntModel.addSubModel(ISRO_map_OntModel);
			service_OntModel.addSubModel(ISRO_cap_OntModel);
			service_OntModel.addSubModel(ISRO_cloud_OntModel);
			System.out.println("<INIT>: SERVICE ontology model is Loaded.\n");
			
			System.out.println("<TEST> : ready to start!\n");
			
		} else {
			System.out.println("Waiting for service package be dispatched...\n");
		}
	}
	
	
}
