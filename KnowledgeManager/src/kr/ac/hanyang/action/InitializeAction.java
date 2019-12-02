package kr.ac.hanyang.action;

import java.util.ArrayList;
import org.json.simple.JSONObject;

import kr.ac.hanyang.Util;
import kr.ac.hanyang.action.argument.InitializeArg;
import kr.ac.hanyang.agent.KmLauncher;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

/**
 * 
 * Service Package Initialize Action
 * 
 * @author freebeing1
 *
 */
public class InitializeAction extends KnowledgeProcessAction {

	private DataSource dc; // CDC
	private ArrayList<String> owlList;
	private static final String ISRO_ontology = "http://robot-arbi.kr/ontologies/"; 
	
	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {
		InitializeArg arg = (InitializeArg) o;
		dc = new DataSource();
		dc.connect(KmLauncher.JMS_BROKER_URL, "dc://dataSourceKMInit", 2); // LTM 연결 --> 이 부분 수정 예정
		String servicePackageName = arg.getServicePackageName();	//현재 서비스패키지 이름
		owlList = new ArrayList<String>();

		String result = initialize(servicePackageName);
		System.out.println(result);
		
		JSONObject log = new JSONObject();

		log.put("On-memory ontologies", owlList);
		
		log.put("Ontology Scale", ontologyMonitor_Scale());

		arg.setResult(log.toJSONString());
		
		return arg.getResult();
	}

	private String initialize(String name) {
		String result = "";
		// Owl file location
		try {
			System.out.println("<INIT> : Service Package Name : [" + name + "]");
			
			owlList.add("isro.owl");
			owlList.add("isro_social.owl");
			owlList.add("isro_medical.owl");
			owlList.add("isro_SD.owl");
			owlList.add("isro_vs.owl");
			
			// LTM으로 부터 owl파일 받음(GL 형태)
			String cap_from_ltm = dc.retrieveFact("(ServicePackage \"" + name + "\" \"isro_cap.owl\" $a)");
			owlList.add("isro_cap.owl");
			PREFIX_isro_cap = ISRO_ontology + "isro_cap.owl#";
			PREFIX = PREFIX + "PREFIX isro_cap: <" + PREFIX_isro_cap + "> \n";
			System.out.println("<RetrieveFact> : " + cap_from_ltm);
			System.out.println();
			
			String map_from_ltm = dc.retrieveFact("(ServicePackage \"" + name + "\" \"isro_map.owl\" $a)");
			owlList.add("isro_map.owl");
			PREFIX_isro_map = ISRO_ontology + "isro_map.owl#";
			PREFIX = PREFIX + "PREFIX isro_map: <" + PREFIX_isro_map + "> \n";
			System.out.println("<RetrieveFact> : " + map_from_ltm);
			System.out.println();
			
			String cloud_from_ltm = dc.retractFact("(ServicePackage \"" + name + "\" \"isro_cloud.owl\" $a)");
			owlList.add("isro_cloud.owl");
			PREFIX_isro_cloud = ISRO_ontology + "isro_cloud.owl#";
			PREFIX = PREFIX + "PREFIX isro_cloud: <" + PREFIX_isro_cloud + "> \n"; 
			System.out.println("<RetrieveFact> : " + cloud_from_ltm);
			System.out.println();
			
			/*
			String serviceOwlString = "";// 여기에 아울파일 바인딩!!!!!!
			service_from_ltm = "(ServicePackage \""+name+"\" \""+name+".owl\" "+serviceOwlString+")";
 			*/
			String service_from_ltm = dc.retrieveFact("(ServicePackage \"" + name + "\" \""+name+".owl\" $a)");
			owlList.add(name+".owl");
			PREFIX_service = ISRO_ontology + name + ".owl#";
			PREFIX = PREFIX + "PREFIX service: <" + PREFIX_service + "> \n";
			System.out.println("<RetrieveFact> : " + service_from_ltm);
			System.out.println();
			
			String cap_OwlString = GLFactory.newGLFromGLString(cap_from_ltm).getExpression(2).asValue().stringValue();
			String map_OwlString = GLFactory.newGLFromGLString(map_from_ltm).getExpression(2).asValue().stringValue();
			String cloud_OwlString = GLFactory.newGLFromGLString(cloud_from_ltm).getExpression(2).asValue().stringValue();
			String service_OwlString = GLFactory.newGLFromGLString(service_from_ltm).getExpression(2).asValue().stringValue();

			owlServicePackageLocation = "ServicePackage/";
			
			ISRO_cap_OwlFile = owlLocation + owlServicePackageLocation + "isro_cap.owl";
			ISRO_map_OwlFile = owlLocation + owlServicePackageLocation + "isro_map.owl";
			ISRO_cloud_OwlFile = owlLocation + owlServicePackageLocation + "isro_cloud.owl";
			service_OwlFile = owlLocation + owlServicePackageLocation + name + ".owl";
			
			try {
				Util.FileOutput(ISRO_cap_OwlFile, cap_OwlString);
				Util.FileOutput(ISRO_map_OwlFile, map_OwlString);
				Util.FileOutput(ISRO_cloud_OwlFile, cloud_OwlString);
				Util.FileOutput(service_OwlFile, service_OwlString);
				System.out.println("<INIT>: All owl files are successfully saved.");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("<INIT>: File Save Error.");
			}
			
			
			
			// 서비스 패키지 디스패쳐로부터 받은 service-specific한 온톨로지를 모델에 올리기.
			ISRO_cap_OntModel.read(ISRO_cap_OwlFile);
			ISRO_cap_OntModel.addSubModel(ISRO_OntModel);
			System.out.println("<INIT>: CAPABILITY ontology model is loaded.");
			
			ISRO_map_OntModel.read(ISRO_map_OwlFile);
			ISRO_map_OntModel.addSubModel(ISRO_OntModel);
			System.out.println("<INIT>: MAP ontology model is loaded.");
			
			ISRO_cloud_OntModel.read(ISRO_cloud_OwlFile);
			ISRO_cloud_OntModel.addSubModel(ISRO_OntModel);
			System.out.println("<INIT>: CLOUD ontology model is loaded.");
			
			service_OntModel.read(service_OwlFile);
			service_OntModel.addSubModel(ISRO_OntModel);
			service_OntModel.addSubModel(ISRO_social_OntModel);
			service_OntModel.addSubModel(ISRO_map_OntModel);
			service_OntModel.addSubModel(ISRO_cap_OntModel);
			service_OntModel.addSubModel(ISRO_cloud_OntModel);
			System.out.println("<INIT>: SERVICE ontology model is Loaded.\n");
			
			
			// 서비스 패키지 이니셜라이즈 수행후 LTM에 필요 데이터 올림
			GeneralizedList mapInstanceGL = GLFactory.newGLFromGLString(mapIndividual());
			dc.assertFact(mapInstanceGL.toString()); // 시맨틱 맵 데이터 CDC에 업로드
			System.out.println("<AssertFact> : " + mapInstanceGL.toString());
			
			GeneralizedList schemeGL = GLFactory.newGLFromGLString(ontologySchema());
			dc.assertFact(schemeGL.toString()); // 온톨로지 스키마 CDC에 업로드
			System.out.println("<AssertFact> : " + schemeGL.toString());
			
//			GeneralizedList robotProfileGL = GLFactory.newGL("robotprofile_Silbot2_001", GLFactory.newValueExpression(QuerySet.robotProfile()));
//			dc.assertFact(robotProfileGL.toString()); // 로봇 프로파일 업로드
//			System.out.println("<AssertFact> : " + robotProfileGL.toString());

//			GeneralizedList actionGL = GLFactory.newGL("actioncapabilities", GLFactory.newValueExpression(QuerySet.action_capability()));
//			dc.assertFact(actionGL.toString()); // 로봇 capability 정보 업로드
//			System.out.println("<AssertFact> : " + actionGL.toString());
			
			// CDC 저장 종료
			result = "<INIT>: Service Package Load SUCCESS";

		} catch (Exception e) {
			result = "<INIT>: Service Package Load FAILED";
			e.printStackTrace();
		}
		return result;
	}

}
