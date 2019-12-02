package kr.ac.hanyang.agent;

import java.util.ArrayList;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

/////////////////////////////////////////////////////////////////////////

import kr.ac.hanyang.agent.KmLauncher;

/////////////////////////////////////////////////////////////////////////

import kr.ac.hanyang.Point3D;

/////////////////////////////////////////////////////////////////////////

import kr.ac.hanyang.action.InitializeAction;
import kr.ac.hanyang.action.KnowledgeProcessAction;
import kr.ac.hanyang.action.CreateClassAction;
import kr.ac.hanyang.action.CreateIndividualAction;
import kr.ac.hanyang.action.CreatePropertyAction;
import kr.ac.hanyang.action.CreateRelationAction;
import kr.ac.hanyang.action.DeleteClassAction;
import kr.ac.hanyang.action.DeleteIndividualAction;
import kr.ac.hanyang.action.DeletePropertyAction;
import kr.ac.hanyang.action.DeleteRelationAction;
import kr.ac.hanyang.action.PathFindingAction;
import kr.ac.hanyang.action.QueryRelationAction;
import kr.ac.hanyang.action.RequestRecommendationAction;
import kr.ac.hanyang.action.QueryMultiRelationAction;
import kr.ac.hanyang.action.QueryCloudRelationAction;

///////////////////////////////////////////////////////////////////////////

import kr.ac.hanyang.action.argument.InitializeArg;
import kr.ac.hanyang.action.argument.ClassArg;
import kr.ac.hanyang.action.argument.IndividualArg;
import kr.ac.hanyang.action.argument.PropertyArg;
import kr.ac.hanyang.action.argument.RecommendationArg;
import kr.ac.hanyang.action.argument.PathFindingArg;
import kr.ac.hanyang.action.argument.TripleArg;
import kr.ac.hanyang.action.argument.TripleSetArg;

/////////////////////////////////////////////////////////////////////////

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.agent.logger.AgentAction;
import kr.ac.uos.ai.arbi.agent.logger.LogTiming;
import kr.ac.uos.ai.arbi.agent.logger.LoggerManager;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.GLParser;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

/////////////////////////////////////////////////////////////////////////

public class KmAgent extends ArbiAgent {

	private LoggerManager loggerManager;
	private static boolean initiated = false;
	
	public KmAgent() {
		ArbiAgentExecutor.execute("configuration/KmConfiguration.xml", this);
	}

	
	// --------------- //
	// Framework Space //
	// --------------- //

	@Override
	public void onStart() {
		
		System.out.println("+++++++++++++++++++++++++++++++");
		System.out.println("+   Knowledge Manager Start   +");
		System.out.println("+++++++++++++++++++++++++++++++");
		System.out.println();
		
		System.out.println("LoggerManager Registering");
		System.out.println();
		
		// Action 등록
		loggerManager = LoggerManager.getInstance();
		
		
		// ---------------- //
		//    INITIALIZE    //
		// ---------------- //
		
		System.out.println("Welcome to Initialize!");
		InitializeAction ia = new InitializeAction(); // 여기서 출력됨
		AgentAction actionIA = new AgentAction("InitializeType", ia);
		loggerManager.registerAction(actionIA, LogTiming.Later);
		actionIA.changeAction(true);
		
		
		

		
		// ---------------- //
		// CREATE OPERATION //
		// ---------------- //
		
		System.out.println("Welcome to CreateClassAction!");
		CreateClassAction cca = new CreateClassAction();
		AgentAction actionCCA = new AgentAction("CreateClass", cca);
		loggerManager.registerAction( actionCCA, LogTiming.Later);
		actionCCA.changeAction(true);
		
		System.out.println("Welcome to CreatePropertyAction!");		
		CreatePropertyAction cpa = new CreatePropertyAction();
		AgentAction actionCPA = new AgentAction("CreateProperty", cpa);
		loggerManager.registerAction(actionCPA, LogTiming.Later);
		actionCPA.changeAction(true);
		
		System.out.println("Welcome to CreateIndividualAction!");
		CreateIndividualAction cia = new CreateIndividualAction();
		AgentAction actionCIA = new AgentAction("CreateIndividual", cia);
		loggerManager.registerAction( actionCIA, LogTiming.Later);
		actionCIA.changeAction(true);
		
		System.out.println("Welcome to CreateRelationAction!");
		CreateRelationAction cra = new CreateRelationAction();
		AgentAction actionCRA = new AgentAction("CreateRelation", cra);
		loggerManager.registerAction( actionCRA, LogTiming.Later);
		actionCRA.changeAction(true);
		
		
		
		
		// ---------------- //
		//  READ OPERATION  // 
		// ---------------- // 
		
		System.out.println("Welcome to QueryRelationAction!");
		QueryRelationAction qra = new QueryRelationAction();
		AgentAction actionQRA = new AgentAction("QueryRelation", qra);
		loggerManager.registerAction(actionQRA, LogTiming.Later);
		actionQRA.changeAction(true);
		
		System.out.println("Welcome to QueryMultiRelationAction!");
		QueryMultiRelationAction qmra = new QueryMultiRelationAction();
		AgentAction actionQMRA = new AgentAction("QueryMultiRelation", qmra);
		loggerManager.registerAction(actionQMRA, LogTiming.Later);
		actionQMRA.changeAction(true);
		
		System.out.println("Welcome to QueryCloudRelationAction!");
		QueryCloudRelationAction qcra = new QueryCloudRelationAction();
		AgentAction actionQCRA = new AgentAction("QueryCloudRelation", qcra);
		loggerManager.registerAction(actionQCRA, LogTiming.Later);
		actionQCRA.changeAction(true);
		
		
		
		
		// ---------------- //
		// UPDATE OPERATION //
		// ---------------- //
		
		
		
		
		
		
		// ---------------- //
		// DELETE OPERATION //
		// ---------------- //
		
		System.out.println("Welcome to DeleteClassAction!");
		DeleteClassAction dca = new DeleteClassAction();
		AgentAction actionDCA = new AgentAction("DeleteClass", dca);
		loggerManager.registerAction(actionDCA, LogTiming.Later);
		actionDCA.changeAction(true);
		
		System.out.println("Welcome to DeletePropertyAction!");
		DeletePropertyAction dpa = new DeletePropertyAction();
		AgentAction actionDPA = new AgentAction("DeleteProperty", dpa);
		loggerManager.registerAction( actionDPA, LogTiming.Later);
		actionDPA.changeAction(true);
		
		System.out.println("Welcome to DeleteIndividualAction!");
		DeleteIndividualAction dia = new DeleteIndividualAction();
		AgentAction actionDIA = new AgentAction("DeleteIndividual", dia);
		loggerManager.registerAction(actionDIA, LogTiming.Later);
		actionDIA.changeAction(true);
		
		System.out.println("Welcome to DeleteRelationAction!");
		DeleteRelationAction dra = new DeleteRelationAction();
		AgentAction actionDRA = new AgentAction("DeleteRelation", dra);
		loggerManager.registerAction( actionDRA, LogTiming.Later);
		actionDRA.changeAction(true);
		
		
		
		
		// ---------------- //
		// SPECIAL REQUESTS //
		// ---------------- //
		
		System.out.println("Welcome to PathFindingAction!");
		PathFindingAction pfa = new PathFindingAction();
		AgentAction actionPF = new AgentAction("PathQuery", pfa);
		loggerManager.registerAction( actionPF, LogTiming.Later);
		actionPF.changeAction(true);
		
		
		System.out.println("Welcome to RequestRecommendation!\n");
		RequestRecommendationAction rra = new RequestRecommendationAction();
		AgentAction actionRRA = new AgentAction("RequestRecommendation", rra);
		loggerManager.registerAction( actionRRA, LogTiming.Later);
		actionRRA.changeAction(true);
		
		super.onStart();
		
		initiated = true;
		
		// model load
		KnowledgeProcessAction.load();
	}
	
	
	/**
	 * request 요청에 대한 콜백
	 *  
	 *  쿼리와 같은 방식으로 Request - Response 
	 * 
	 * @param sender
	 * 				Request 요청자
	 * @param request
	 * 				요청 내용 (GL)
	 * @return 요청 반응 (GL, 혹은 OK 등 간단한 메시지)
	 */
	
	@Override
	public String onRequest(String sender, String request) {
		
		JSONParser actionResultParser = new JSONParser();
		JSONObject responseJSON = new JSONObject();
		
		String response = "";
		String actionResult = "";
		
		
		System.out.println("<ON REQUEST> (" + sender + ") : " + request);
		
		GeneralizedList gl = null; // request GL
		try {
			gl = GLFactory.newGLFromGLString(request);
		} catch (ParseException e) {
			System.out.println("<GL Error> : Parse error");
			e.printStackTrace();
		}
		String glName = gl.getName();
		
		// 각 Case에서 무조건 리턴
		switch (glName) {
	
		// ---------------- //
		// CREATE OPERATION //
		// ---------------- //

		case "createClass":
			/* 
			 * 새로운 클래스 생성 요청
			 * 
			 * [GL format]
			 * (createClass $superClassID $newclassID)
			 * 
			 * [Example GL]
			 * (createClass "http://robot-arbi.kr/ontologies/isro_social.owl#MentalDisease" "http://robot-arbi.kr/ontologies/EduService.owl#Schizophrenia")
			 */
			if(true) {
				String superClassID = gl.getExpression(0).asValue().stringValue();
				String newClassID = gl.getExpression(1).asValue().stringValue();
				
				ClassArg arg = new ClassArg(superClassID, newClassID);
				
				actionResult = (String) loggerManager.getAction("CreateClass").execute(arg);
				try {
					responseJSON = (JSONObject) actionResultParser.parse(actionResult);
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				response = "(ok)";
				System.out.println("<REQUEST> : [" + glName + "] " + responseJSON.get("result"));
				System.out.println();
				
				return response;
			}
			
			
		case "createProperty":
			/* 
			 * 새로운 프로퍼티 생성 요청
			 * domain, range 이 정의되지 않았을 경우, GL 포맷의 $domain $range 그대로 유지한 채로 받아야 함. 
			 * 
			 * [GL format]
			 * (createProperty $propertyType $superProperty $propertyID $domain $range)
			 * 
			 * [Example GL]
			 * (createProperty "ObjectProperty" "http://robot-arbi.kr/ontologies/isro_social.owl#getDisease" "http://robot-arbi.kr/ontologies/EduService.owl#getPhysicalDisease" "http://knowrob.org/kb/knowrob.owl#Person" "http://robot-arbi.kr/ontologies/isro_social.owl#PhysicalDisease")
			 * (createProperty "DatatypeProperty" "http://www.w3.org/2002/07/owl#topDataProperty" "http://robot-arbi.kr/ontologies/isro_social.owl#testDatatypeProperty" $domain $range)
			 */
			if(true) {
				
				String propertyType = gl.getExpression(0).asValue().stringValue();
				String superProperty = gl.getExpression(1).asValue().stringValue();
				String propertyID = gl.getExpression(2).asValue().stringValue();
				String domain = "$domain";
				String range = "$range";
				if(gl.getExpression(3).isValue()) {
					domain = gl.getExpression(3).asValue().stringValue();
				}
				if(gl.getExpression(4).isValue()) {
					range = gl.getExpression(4).asValue().stringValue();
				}
				
				PropertyArg arg = new PropertyArg(propertyType, superProperty, propertyID, domain, range);
				
				actionResult = (String) loggerManager.getAction("CreateProperty").execute(arg);
				try {
					responseJSON = (JSONObject) actionResultParser.parse(actionResult);
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				response = "(ok)";
				System.out.println("<REQUEST> : [" + glName + "] " + responseJSON.get("result"));
				System.out.println();
				
				return response;
			}

			
		case "createIndividual":
			/* 
			 * 새로운 인디비주얼 생성 요청
			 * 
			 * [GL format]
			 * (createIndividual $typeClassID $individualID)
			 * 
			 * [Example GL]
			 * (createIndividual "http://robot-arbi.kr/ontologies/isro_social.owl#Schizophrenia" "http://robot-arbi.kr/ontologies/isro_social.owl#_Schizophrenia")
			 */
			if(true) {
				String typeClassID = gl.getExpression(0).asValue().stringValue();
				String individualID = gl.getExpression(1).asValue().stringValue();
				
				IndividualArg arg = new IndividualArg(typeClassID, individualID);
				
				actionResult = (String) loggerManager.getAction("CreateIndividual").execute(arg);
				try {
					responseJSON = (JSONObject) actionResultParser.parse(actionResult);
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				response = "(ok)";
				System.out.println("<REQUEST> : [" + glName + "] " + responseJSON.get("result"));
				System.out.println();
				
				return response;
			}
			
			
		case "createRelation":
			/*
			 * A general purpose method to make new relation triple($s $p $o)s.
			 * 온톨로지에 새로운 triple 관계 생성을 요청하기 위한 request 
			 *  
			 * [GL Format]
			 * (createRelation $subject (predicate $p $o) (predicate $p $o) ... (predicate $p $o))
			 * 
			 * [Example GL]
			 * (createRelation "http://robot-arbi.kr/ontologies/isro.owl#FaceRecognition" (predicate "http://robot-arbi.kr/ontologies/isro_social.owl#faceID" "001"))
			 * (createRelation "http://robot-arbi.kr/ontologies/EduService.owl#Person001" (predicate "http://robot-arbi.kr/ontologies/isro_social.owl#getDisease" "http://robot-arbi.kr/ontologies/EduService.owl#_Schizophrenia"))
			 */
			if(true) {
				String subject = gl.getExpression(0).asValue().stringValue();
				
				int size = gl.getExpressionsSize();
				
				for(int i=1; i<size; i++) {
					String p = gl.getExpression(i).asGeneralizedList().getExpression(0).asValue().stringValue();
					String o = gl.getExpression(i).asGeneralizedList().getExpression(1).asValue().stringValue();
					//System.out.println("p : " + p);
					//System.out.println("o : " + o);
					
					TripleArg args = new TripleArg(subject, p, o);
					actionResult = (String) loggerManager.getAction("CreateRelation").execute(args);
					try {
						responseJSON = (JSONObject) actionResultParser.parse(actionResult);
					} catch (org.json.simple.parser.ParseException e) {
						e.printStackTrace();
					}
					
					System.out.println("<REQUEST> : [" + glName + "] ("+i+"/"+(size-1)+") " + responseJSON.get("result"));
				}
				response = "(ok)";
				System.out.println();
				
				return response;
			}
			
		// ---------------- //
		// DELETE OPERATION //
		// ---------------- //
			
		case "deleteClass":
			/*
			 * 기존 클래스 삭제 요청 
			 *  
			 * [GL Format]
			 * (deleteClass $superClass $targetClass)
			 * 
			 * [Example GL]
			 * (deleteClass "http://robot-arbi.kr/ontologies/isro_social.owl#MentalDisease" "http://robot-arbi.kr/ontologies/EduService.owl#Schizophrenia")
			 */
			if(true) {
				String superClassID =  gl.getExpression(0).asValue().stringValue();
				String targetClassID = gl.getExpression(1).asValue().stringValue();
				
				ClassArg arg = new ClassArg(superClassID, targetClassID);
				actionResult = (String) loggerManager.getAction("DeleteClass").execute(arg);
				try {
					responseJSON = (JSONObject) actionResultParser.parse(actionResult);
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				response = "(ok)";
				System.out.println("<REQUEST> : [" + glName + "] " + responseJSON.get("result"));
				System.out.println();
				
				return response;
			}
			
		case "deleteProperty":
			/*
			 * 기존 프로퍼티 삭제 요청
			 * 
			 * [GL format]
			 * (deleteProperty $propertyType $superProperty $propertyID $domain $range)
			 * 
			 * [Example GL]
			 * (deleteProperty "ObjectProperty" "http://robot-arbi.kr/ontologies/isro_social.owl#getDisease" "http://robot-arbi.kr/ontologies/EduService.owl#getPhysicalDisease" "http://knowrob.org/kb/knowrob.owl#Person" "http://robot-arbi.kr/ontologies/isro_social.owl#PhysicalDisease")
			 * (deleteProperty "DatatypeProperty" "http://www.w3.org/2002/07/owl#topDataProperty" "http://robot-arbi.kr/ontologies/isro_social.owl#testDatatypeProperty" $domain $range)
			 */
			if(true) {
				String propertyType = gl.getExpression(0).asValue().stringValue();
				String superProperty = gl.getExpression(1).asValue().stringValue();
				String propertyID = gl.getExpression(2).asValue().stringValue();
				String domain = "$domain";
				String range = "$range";
				if(gl.getExpression(3).isValue()) {
					domain = gl.getExpression(3).asValue().stringValue();
				}
				if(gl.getExpression(4).isValue()) {
					range = gl.getExpression(4).asValue().stringValue();
				}
				
				PropertyArg arg = new PropertyArg(propertyType, superProperty, propertyID, domain, range);
				actionResult = (String) loggerManager.getAction("DeleteProperty").execute(arg);
				try {
					responseJSON = (JSONObject) actionResultParser.parse(actionResult);
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				response = "(ok)";
				System.out.println("<REQUEST> : [" + glName + "] " + responseJSON.get("result"));
				System.out.println();
				
				return response;
			}
			
		case "deleteIndividual":
			/*
			 * 기존 인디비주얼 삭제 요청
			 * 
			 * [GL format]
			 * (deleteIndividual $typeClassID $individualID)
			 * 
			 * [Example GL]
			 * (deleteIndividual "http://robot-arbi.kr/ontologies/isro_social.owl#Schizophrenia" "http://robot-arbi.kr/ontologies/EduService.owl#_Schizophrenia")
			 */
			if(true) {
				String typeClassID = gl.getExpression(0).asValue().stringValue();
				String individualID = gl.getExpression(1).asValue().stringValue();
				
				IndividualArg arg = new IndividualArg(typeClassID, individualID);
				
				actionResult = (String) loggerManager.getAction("DeleteIndividual").execute(arg);
				try {
					responseJSON = (JSONObject) actionResultParser.parse(actionResult);
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				response = "(ok)";
				System.out.println("<REQUEST> : [" + glName + "] " + responseJSON.get("result"));
				System.out.println();
				
				return response;
			}
		
			
		case "deleteRelation":
			/*
			 * A general purpose method to delete existing relation triple($s $p $o)s.
			 * 온톨로지에 존재하는 기존 triple 관계 삭제를 요청하기 위한 request 
			 *  
			 * [GL Format]
			 * (deleteRelation $subject $predicate $object)
			 * 
			 * [Example GL]
			 * (deleteRelation "http://robot-arbi.kr/ontologies/EduService.owl#Person001" "http://robot-arbi.kr/ontologies/isro_social.owl#getDisease" "http://robot-arbi.kr/ontologies/EduService.owl#_Schizophrenia")
			 */
			if(true) {
				String s = gl.getExpression(0).asValue().stringValue();
				String p = gl.getExpression(1).asValue().stringValue();
				String o = gl.getExpression(2).asValue().stringValue();
				
				TripleArg arg = new TripleArg(s, p, o);
				actionResult = (String) loggerManager.getAction("DeleteRelation").execute(arg);
				try {
					responseJSON = (JSONObject) actionResultParser.parse(actionResult);
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				response = "(ok)";
				System.out.println("<REQUEST> : [" + glName + "] " + responseJSON.get("result"));
				System.out.println();
				
				return response;
			}
		
		// ---------------- //
		// SPECIAL REQUESTS //
		// ---------------- //
			
		case "requestPath":
			/*
			 * 현재위치 좌표와 출발지 목적지로 최단경로 요청
			 * 
			 * [GL format]
			 * (requestPath $type (currentPoint $x $y $z) $departure $destination)
			 * 
			 * [Example GL]
			 * (requestPath "coordinate" (currentPoint 2.0 3.0 0.0) "http://robot-arbi.kr/ontologies/isro_map.owl#OfficeRoom001" "http://robot-arbi.kr/ontologies/isro_map.owl#OfficeRoom002")
			 * (requestPath "name" (currentPoint 2.0 3.0 0.0) "http://robot-arbi.kr/ontologies/isro_map.owl#OfficeRoom001" "http://robot-arbi.kr/ontologies/isro_map.owl#OfficeRoom002")
			 * (requestPath "action" (currentPoint 5.25 0.8056 0.0) "http://robot-arbi.kr/ontologies/isro_map.owl#ReceptionRoom001" "http://robot-arbi.kr/ontologies/inro_map.owl#HospitalRoom001")
			 */
			if(true) {
				String pathType = gl.getExpression(0).asValue().stringValue();
				
				GeneralizedList currentCoord = gl.getExpression(1).asGeneralizedList();
				double xCoord = currentCoord.getExpression(0).asValue().floatValue();
				double yCoord = currentCoord.getExpression(1).asValue().floatValue();
				double zCoord = currentCoord.getExpression(2).asValue().floatValue();
				Point3D coord = new Point3D(xCoord, yCoord, zCoord);
				
				String departure = gl.getExpression(2).asValue().stringValue();
				String destination = gl.getExpression(3).asValue().stringValue();
				
				PathFindingArg args = new PathFindingArg(departure, destination);
				args.setPathType(pathType);
				args.setPosition(coord);
				
				
				actionResult = (String) loggerManager.getAction("Path").execute(args);
				try {
					responseJSON = (JSONObject) actionResultParser.parse(actionResult);
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				response = (String) responseJSON.get("GL");
				
				return response;
			}
			
		case "requestRecommendation":
			/*
			 * 사용자의 행위에 따른 적절한 오브젝트 추천
			 * 
			 * [GL format]
			 * (requestRecommendation $userID $targetAction)
			 * 
			 * [Result GL format]
			 * (requestRecommendation $userID $targetAction (result $result1 $result2 ... ))
			 * 
			 * [Example GL]
			 * (requestRecommendation "http://robot-arbi.kr/ontologies/complexService.owl#person001" "http://knowrob.org/kb/knowrob.owl#Drink" $result)
			 * 
			 * [Example result]
			 * (responseRecommendation "http://robot-arbi.kr/ontologies/complexService.owl#person001" "http://knowrob.org/kb/knowrob.owl#Drink" (result (recommendation "http://robot-arbi.kr/ontologies/complexService.owl#_water") (reason (disease "http://robot-arbi.kr/ontologies/isro_medical.owl#_diabetes") (history "null"))))
			 * 
			 */
			if(true) {
				String userID = gl.getExpression(0).asValue().stringValue();
				String targetAction = gl.getExpression(1).asValue().stringValue();
				
//				boolean kmTest= true; // for kmTest
				boolean kmTest = false; // for integration
				String history = "";
				String queryMsg = "";
				if(targetAction.contains("http://knowrob.org/kb/knowrob.owl#Drink")) {
					String queryResult = "";
					if(kmTest) {
						if(userID.contains("1")) {
							queryResult = "(context (drink \"http://robot-arbi.kr/ontologies/complexService.owl#person001\" \"\" \"TODAY\"))";
						}
						else if (userID.contains("2")) {
							queryResult = "(context (drink \"http://robot-arbi.kr/ontologies/complexService.owl#person002\" \"http://robot-arbi.kr/ontologies/complexService.owl#coffee001\" \"TODAY\"))";
						}
						else if(userID.contains("4")) {
							queryResult = "(context (drink \"http://robot-arbi.kr/ontologies/complexService.owl#person004\" \"http://robot-arbi.kr/ontologies/complexService.owl#coffee003\" \"TODAY\"))";
						}
					} 
					else 
					{
						queryMsg = "(context (drink \"" + userID + "\" $Beverage \"TODAY\"))";
						queryResult = query(KmLauncher.CONTEXTMANAGER_ADDRESS, queryMsg);
						// (context (drink "http://robot-arbi.kr/ontologies/complexService.owl#person001" "" "TODAY"))
						// (context (drink "http://robot-arbi.kr/ontologies/complexService.owl#person002" "http://robot-arbi.kr/ontologies/complexService.owl#Coffee001" "TODAY"))
					}
					
					GeneralizedList queryResultGL = null;
					try {
						queryResultGL = GLFactory.newGLFromGLString(queryResult);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					history = queryResultGL.getExpression(0).asGeneralizedList().getExpression(1).asValue().stringValue();
					// "http://robot-arbi.kr/ontologies/complexService.owl#Coffee001"
				}
				
//				// for test
//				history = "http://robot-arbi.kr/ontologies/complexService.owl#Coffee001";
				
				
				
				RecommendationArg arg = new RecommendationArg(userID, targetAction, history);
				
				actionResult = (String) loggerManager.getAction("RequestRecommendation").execute(arg);
				
				try {
//					System.out.println(actionResult);
					responseJSON = (JSONObject) actionResultParser.parse(actionResult);
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				response = (String) responseJSON.get("GL");
				GeneralizedList resultGL = null;
				try {
					resultGL = GLFactory.newGLFromGLString(response);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
				System.out.println("<REQUEST> : [" + glName + "] " + resultGL.getExpression(2).asGeneralizedList().toString());
				System.out.println();
				
				return response;
			}
		
		default:
			String res = "<GL Error> : GL NAME [" + glName + "] NOT DEFINED";
			System.out.println(res);
			return res;
		}
	}

	
	
	
	/**
	 * 쿼리 요청 받음
	 * 
	 * @param sender
	 *            - 쿼리 요청자
	 * @param query
	 *            - 요청 쿼리━ (GL String)
	 * @return 쿼리 응답 (GL String)
	 */
	@Override
	public String onQuery(String sender, String query) {
		
		JSONParser actionResultParser = new JSONParser();
		JSONObject responseJSON = new JSONObject();
		
		String response = "";
		String actionResult = "";
		
		GLParser parser = new GLParser();
		GeneralizedList gl = null; // GL 쿼리 입력 GL
		System.out.println("<On Query> : " + query);
		
		try {
			gl = parser.parseGL(query);
		} catch (ParseException e) {
			System.out.println("<GL Error> : Parse error");
			return "parse error";
		}
		
		
		String glName = gl.getName();
		
		// 각 Case에서 무조건 리턴
		switch(glName) {
		
		case "initiationCheck":
			/*
			 * [GL format]
			 * (initiationCheck $initiated)
			 * 
			 * [Result GL Example]
			 * (initiationCheck "true")
			 * (initiationCheck "false")
			 * 
			 */
			if(true) {
				if(initiated) {
					response = "(initiationCheck \"true\")";
				} 
				else 
				{
					response = "(initiationCheck \"false\")";
				}
				return response;
			}
		case "queryCloudRelation":
			/*
			 * [GL format]
			 * (queryCloudRelation $s $p $o $result)
			 * 
			 * [Result GL format]
			 * (queryCloudRelation $s $p $o (result (triple $s1 $p1 $o1) (triple $s2 $p2 $o2) ... ))
			 * 
			 * [GL Example]
			 * (queryCloudRelation "추석" "일시" $o $result)
			 * (queryCloudRelation "추석" "일시" $o $result)
			 * 
			 * [Result GL Example]
			 * (queryCloudRelation "추석" "일시" $o (result (triple "추석" "일시" $o)))
			 */
			if(true) {
				// (queryCloudRelation "기상예보" $p $o $result)
				if(gl.getExpression(0).asValue().stringValue().contains("기상")) {
					@SuppressWarnings("unused")
					String res = "";
//					String protocol = "(WeatherReport)";
					String glString = null;
//					glString = this.query("agent://www.arbi.com/cloudAgent#WeatherReport", protocol);
					System.out.println("Forecast record received!\n");
					glString = ""
							+ "(Weather\r\n" + 
							"  (Response\r\n" + 
							"    (Body\r\n" + 
							"      (Items\r\n" + 
							"        (item\r\n" + 
							"          (AnnounceTime 20181018)\r\n" + 
							"          (numEf 0)\r\n" + 
							"          (Temperature 20)\r\n" + 
							"          (Weather \"맑음\")\r\n" + 
							"          (rainFallForm \"0\")\r\n" +
							"        )\r\n" + 
							"      )\r\n" + 
							"    )\r\n" + 
							"  )\r\n" + 
							")";
					GeneralizedList resultGL = null;
					try {
//						System.out.println(glString);
						resultGL = GLFactory.newGLFromGLString(glString);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					String isro_IRI = "http://robot-arbi.kr/ontologies/isro.owl#";
					String service_IRI = "http://robot-arbi.kr/ontologies/EduService.owl#";
					String request = "";
					GeneralizedList Items = resultGL.getExpression(0).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asGeneralizedList();
					int NumOfItem = Items.getExpressionsSize();
					ArrayList<String> individuals = new ArrayList<String>();
					for(int i=0; i<NumOfItem; i++) {
						GeneralizedList item = Items.getExpression(i).asGeneralizedList();
						String announceTime = item.getExpression(0).asGeneralizedList().getExpression(0).asValue().stringValue();
						String forecastingTimeZone = item.getExpression(1).asGeneralizedList().getExpression(0).asValue().stringValue();
						String temperature = item.getExpression(2).asGeneralizedList().getExpression(0).asValue().stringValue();
						String skyStatus = item.getExpression(3).asGeneralizedList().getExpression(0).asValue().stringValue();
						String rainfallForm = item.getExpression(4).asGeneralizedList().getExpression(0).asValue().stringValue();
						String individual = service_IRI+"Foercast_"+announceTime+"_"+forecastingTimeZone;
						individuals.add(individual);
						request = "(createIndividual \""+isro_IRI+"Forecast\" \""+individual+"\")";
						res = this.request(KmLauncher.KNOWLEDGEMANAGER_ADDRESS, request);
						System.out.println();
						request = ""
								+ "(createRelation "
								+ " \""+individual+"\" "
								+ " (predicate \""+isro_IRI+"announceTime"+"\" \""+announceTime+"\") "
								+ " (predicate \""+isro_IRI+"forecastingTimeZone"+"\" \""+forecastingTimeZone+"\") "
								+ " (predicate \""+isro_IRI+"temperature"+"\" \""+temperature+"\") "
								+ " (predicate \""+isro_IRI+"skyStatus"+"\" \""+skyStatus+"\") "
								+ " (predicate \""+isro_IRI+"rainfallForm"+"\" \""+rainfallForm+"\") "
								+ ")"
								+ "";
						res = this.request(KmLauncher.KNOWLEDGEMANAGER_ADDRESS, request);
						System.out.println();
					}
					
					
					response = "(queryCloudRelation \"기상예보\" $p $o (result ";
					for(int i=0; i<individuals.size(); i++) {
						try {
							resultGL = GLFactory.newGLFromGLString(this.query(KmLauncher.KNOWLEDGEMANAGER_ADDRESS, "(queryRelation \""+individuals.get(i)+"\" $p $o $result)"));
						} catch (ParseException e) {
							e.printStackTrace();
						}
						String responsePart = resultGL.getExpression(3).asGeneralizedList().toString();
						responsePart = responsePart.split("result")[1];
						responsePart = "(tripleSet "+ responsePart;
						System.out.println(responsePart);
						response = response + responsePart;
					}
					response = response + "))";
					System.out.println(response);
					return response;
				}
				
				
				
				
				
				// (queryCloudRelation "??" "개요" $o $result)
				else if(gl.getExpression(1).asValue().stringValue().equals("개요")) {
					String queryMsg = "(QueryKoDBPediaAbstract (Keyword \""+gl.getExpression(0).asValue().stringValue()+"\"))";
					
					String glString = this.query("agent://www.arbi.com/cloudAgent#QueryKoDBPediaAbstract", queryMsg);
					GeneralizedList resultGL = null;
					try {
						resultGL = GLFactory.newGLFromGLString(glString);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					if(resultGL.getName().equals("CloudError")) {
						String result = "(queryCloudRelation \""+gl.getExpression(0).asValue().stringValue()+"\" \"개요\" $o (result (triple \""+gl.getExpression(0).asValue().stringValue()+"\" \"개요\" \"알수없음\")))";
						return result;
					} else {
						String value = resultGL.getExpression(0).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asValue().stringValue();
						
						TripleArg args = new TripleArg(gl.getExpression(0).asValue().stringValue(), "개요", value);
						
						actionResult = (String) loggerManager.getAction("QueryCloudRelation").execute(args);
						try {
							responseJSON = (JSONObject) actionResultParser.parse(actionResult);
						} catch (org.json.simple.parser.ParseException e) {
							e.printStackTrace();
						}
						response = (String) responseJSON.get("GL");
						return response;
						
					}					
				}
							
				// (queryCloudRelation "??" "일시" $o $result)
				else if(gl.getExpression(1).asValue().stringValue().equals("일시")) {
					String queryMsg = "(QueryKoDBPediaWhen (Keyword \""+gl.getExpression(0).asValue().stringValue()+"\"))";
					String glString = this.query("agent://www.arbi.com/cloudAgent#QueryKoDBPediaWhen", queryMsg);
					
					glString = GLFactory.unescape(glString);
					GeneralizedList resultGL = null;
					try {
						resultGL = GLFactory.newGLFromGLString(glString);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					if(resultGL.getName().equals("CloudError")) {
						response = "(queryCloudRelation \""+gl.getExpression(0).asValue().stringValue()+"\" \"일시\" $o (result (triple \""+gl.getExpression(0).asValue().stringValue()+"\" \"일시\" \"알수없음\")))";
						return response;
					} else {
						String value = resultGL.getExpression(0).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asValue().stringValue();
						
						TripleArg args = new TripleArg(gl.getExpression(0).asValue().stringValue(), "일시", value);
						
						actionResult = (String) loggerManager.getAction("QueryCloudRelation").execute(args);
						try {
							responseJSON = (JSONObject) actionResultParser.parse(actionResult);
						} catch (org.json.simple.parser.ParseException e) {
							e.printStackTrace();
						}
						response = (String) responseJSON.get("GL");
						return response;
					}
				}
				
				// (queryCloudRelation "??" "시작" $o $result)
				else if(gl.getExpression(1).asValue().stringValue().equals("시작")) {
					String queryMsg = "(QueryKoDBPediaStart (Keyword \""+gl.getExpression(0).asValue().stringValue()+"\"))";
					String glString = this.query("agent://www.arbi.com/cloudAgent#QueryKoDBPediaStart", queryMsg);
					GeneralizedList resultGL = null;
					try {
						resultGL = GLFactory.newGLFromGLString(glString);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					
					if(resultGL.getName().equals("CloudError")) {
						response = "(queryCloudRelation \""+gl.getExpression(0).asValue().stringValue()+"\" \"시작\" $o (result (triple \""+gl.getExpression(0).asValue().stringValue()+"\" \"시작\" \"알수없음\")))";
						return response;
					} else {
						String value = resultGL.getExpression(0).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asGeneralizedList().getExpression(0).asValue().stringValue();
						
						TripleArg args = new TripleArg(gl.getExpression(0).asValue().stringValue(), "시작", value);
						
						actionResult = (String) loggerManager.getAction("QueryCloudRelation").execute(args);
						try {
							responseJSON = (JSONObject) actionResultParser.parse(actionResult);
						} catch (org.json.simple.parser.ParseException e) {
							e.printStackTrace();
						}
						response = (String) responseJSON.get("GL");
						return response;
					}
				}
			}
			
		case "queryRelation":
			if(true) {
				/*
				 * [GL format]
				 * (queryRelation $s $p $o $result)
				 * 
				 * [Result GL format]
				 * (queryRelation $s $p $o (result (triple $s1 $p1 $o1) (triple $s2 $p2 $o2) ... ))
				 * 
				 * [Example]
				 * (queryRelation "http://robot-arbi.kr/ontologies/EduService.owl#Person001" $p $o $result)
				 * 
				 * [ResultGL Example]
				 * (queryRelation "http://robot-arbi.kr/ontologies/DemoKM.owl#Person001" $p $o (result (triple "http://robot-arbi.kr/ontologies/DemoKM.owl#Person001" "http://robot-arbi.kr/ontologies/isro_social.owl#isAged" "http://robot-arbi.kr/ontologies/isro_social.owl#AdultAge") ... ))
				 */

				String s = "$s";
				String p = "$p";
				String o = "$o";

				if(gl.getExpression(0).isValue()) {
					s = gl.getExpression(0).asValue().stringValue();
				}
				
				if(gl.getExpression(1).isValue()) {
					p = gl.getExpression(1).asValue().stringValue();
				}
				
				if(gl.getExpression(2).isValue()) {
					o = gl.getExpression(2).asValue().stringValue();
				} 

				TripleArg args = new TripleArg(s, p, o);
				actionResult = (String) loggerManager.getAction("QueryRelation").execute(args); 
				try {
					responseJSON = (JSONObject) actionResultParser.parse(actionResult);
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				response = (String) responseJSON.get("GL");
				return response;
			}
			
		case "queryMultiRelation":
			if(true) {
				/*
				 * [GL format]
				 * (queryMultiRelation (tripleSet (triple $s $p $o) ... ) $result)
				 * 
				 * [ResultGL format]
				 * (queryMultiRelation (tripleSet (triple $s $p $o) ... ) (result (tripleSet (triple $s $p $o) ... ) ... ))
				 * 
				 * [Example]
				 * (queryMultiRelation (tripleSet (triple $medicalRecord "http://robot-arbi.kr/ontologies/isro.owl#targetPerson" "http://robot-arbi.kr/ontologies/DemoKM.owl#Person001") (triple $medicalRecord "http://robot-arbi.kr/ontologies/isro.owl#targetDisease" $disease) (triple $medicalRecord "http://knowrob.org/kb/knowrob.owl#startTime" "http://robot-arbi.kr/ontologies/DemoKM.owl#TimePoint_123456789")) $result)
				 * (queryMultiRelation (tripleSet (triple $medicalRecord "http://robot-arbi.kr/ontologies/isro.owl#targetPerson" "화자") (triple $medicalRecord "http://robot-arbi.kr/ontologies/isro.owl#targetDisease" $disease) (triple $medicalRecord "http://knowrob.org/kb/knowrob.owl#startTime" $timePoint) ) $result)
				 */
				
				int tripleSize = gl.getExpression(0).asGeneralizedList().getExpressionsSize();
				
				ArrayList<String> s = new ArrayList<String>();
				ArrayList<String> p = new ArrayList<String>();
				ArrayList<String> o = new ArrayList<String>();
				
				for(int i=0;i<tripleSize;i++) {
					try {
						s.add(gl.getExpression(0).asGeneralizedList().getExpression(i).asGeneralizedList().getExpression(0).asValue().stringValue());
					} catch (Exception e) {
						s.add(gl.getExpression(0).asGeneralizedList().getExpression(i).asGeneralizedList().getExpression(0).asVariable().toString());
					}
					
					try {
						p.add(gl.getExpression(0).asGeneralizedList().getExpression(i).asGeneralizedList().getExpression(1).asValue().stringValue());
					} catch (Exception e) {
						p.add(gl.getExpression(0).asGeneralizedList().getExpression(i).asGeneralizedList().getExpression(1).asVariable().toString());
					}
					
					try {
						o.add(gl.getExpression(0).asGeneralizedList().getExpression(i).asGeneralizedList().getExpression(2).asValue().stringValue());
					} catch (Exception e) {
						o.add(gl.getExpression(0).asGeneralizedList().getExpression(i).asGeneralizedList().getExpression(2).asVariable().toString());
					}
				}
				
				TripleSetArg args = new TripleSetArg(s, p, o);
				actionResult = (String) loggerManager.getAction("QueryMultiRelation").execute(args); 
				try {
					responseJSON = (JSONObject) actionResultParser.parse(actionResult);
				} catch (org.json.simple.parser.ParseException e) {
					e.printStackTrace();
				}
				response = (String) responseJSON.get("GL");
				return response;
			}
		
		default: 
			String res = "<GL Error> : GL NAME [" + glName + "] NOT DEFINED";
			System.out.println(res);
			return res;
		}
	}

	
	@Override
	public void onData(String sender, String msg) {
		System.out.println("<On Data> " + msg);
		
		GeneralizedList gl = null;
		
		try {
			gl = GLFactory.newGLFromGLString(msg);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		String glName = gl.getName(); 
		
		switch (glName) {
		
		// ---------------- //
		//    INITIALIZE    //
		// ---------------- //
				
		case "InitiateServicePackage":
			if(true) {
				/*
				 * [GL format]
				 * (InitiateServicePackage $servicePackageNmae)
				 * 
				 * [Example]
				 * (InitiateServicePackage "EduService")
				 */
		
				String servicePackageName = gl.getExpression(0).asValue().stringValue();
				InitializeArg arg = new InitializeArg(servicePackageName);
				loggerManager.getAction("Initialize").execute(arg);
				
				
				// Dummy Cloud Query
				String initMsg = "(CloudSpecification \r\n" + 
						"        (GLName \"QueryKoDBPediaStart\") \r\n" + 
						"        (GLMethod \"QUERY\") \r\n" + 
						"        (URL \"http://143.248.135.47/sparql?default-graph-uri=&query=select+*+where+%7B%3Chttp%3A%2F%2Fko.dbpedia.org%2Fresource%2F$keyword%3E+%3Chttp%3A%2F%2Fko.dbpedia.org%2Fproperty%2F%EC%8B%9C%EC%9E%91%3E+%3FAbstract%7D&format=application%2Fsparql-results%2Bjson&timeout=30000\") \r\n" + 
						"        (Method \"GET\") \r\n" + 
						"        (OpenApi)\r\n" + 
						"        (RequestBodyType \"JSON\")\r\n" + 
						"        (Request\r\n" + 
						"                (Argument (URLMapping \"$keyword\") (DynamicValue \"Keyword\"))\r\n" + 
						"        )\r\n" + 
						"        (Response \"bindings\" \"JSON\"\r\n" + 
						"                (Arguments (Argument\r\n" + 
						"                (BodyMapping \"OBJECT\" \"results\" \r\n" + 
						"                        (Arguments \r\n" + 
						"                                (Argument (BodyMapping \"LIST\" \"bindings\"\r\n" + 
						"                                        (Arguments (Argument (BodyMapping \"OBJECT\" \"Abstract\"\r\n" + 
						"                                                (Arguments (Argument (BodyMapping \"STRING\" \"value\") (DynamicValue \"value\")))\r\n" + 
						"                                        ) (DynamicValue \"Abstract\")))\r\n" + 
						"                                 ) (DynamicValue \"Value\")) \r\n" + 
						"                        )\r\n" + 
						"                ) (DynamicValue \"Bindings\")))\r\n" + 
						"        )\r\n" + 
						")\r\n" + 
						""; 
				
				this.send(KmLauncher.CLOUD_ADDRESS, initMsg);
				
				initMsg = "(CloudSpecification \r\n" + 
						"        (GLName \"QueryKoDBPediaWhen\") \r\n" + 
						"        (GLMethod \"QUERY\") \r\n" + 
						"        (URL \"http://143.248.135.47/sparql?default-graph-uri=&query=select+*+where+%7B%3Chttp%3A%2F%2Fko.dbpedia.org%2Fresource%2F$keyword%3E+%3Chttp%3A%2F%2Fko.dbpedia.org%2Fproperty%2F날짜%3E+%3FAbstract%7D&format=application%2Fsparql-results%2Bjson&timeout=30000\") \r\n" + 
						"        (Method \"GET\") \r\n" + 
						"        (OpenApi)\r\n" + 
						"        (RequestBodyType \"JSON\")\r\n" + 
						"        (Request\r\n" + 
						"                (Argument (URLMapping \"$keyword\") (DynamicValue \"Keyword\"))\r\n" + 
						"        )\r\n" + 
						"        (Response \"bindings\" \"JSON\"\r\n" + 
						"                (Arguments (Argument\r\n" + 
						"                (BodyMapping \"OBJECT\" \"results\" \r\n" + 
						"                        (Arguments \r\n" + 
						"                                (Argument (BodyMapping \"LIST\" \"bindings\"\r\n" + 
						"                                        (Arguments (Argument (BodyMapping \"OBJECT\" \"Abstract\"\r\n" + 
						"                                                (Arguments (Argument (BodyMapping \"STRING\" \"value\") (DynamicValue \"value\")))\r\n" + 
						"                                        ) (DynamicValue \"Abstract\")))\r\n" + 
						"                                 ) (DynamicValue \"Value\")) \r\n" + 
						"                        )\r\n" + 
						"                ) (DynamicValue \"Bindings\")))\r\n" + 
						"        )\r\n" + 
						")\r\n" + 
						""; 
				this.send(KmLauncher.CLOUD_ADDRESS, initMsg);
				
				initMsg = "(CloudSpecification \r\n" + 
						"        (GLName \"QueryKoDBPediaAbstract\") \r\n" + 
						"        (GLMethod \"QUERY\") \r\n" + 
						"        (URL \"http://143.248.135.47/sparql?default-graph-uri=&query=select+*+where+%7B%3Chttp%3A%2F%2Fko.dbpedia.org%2Fresource%2F$keyword%3E+%3Chttp%3A%2F%2Fdbpedia.org/ontology/abstract%3E+%3FAbstract%7D&format=application%2Fsparql-results%2Bjson&timeout=30000\") \r\n" + 
						"        (Method \"GET\") \r\n" + 
						"        (OpenApi)\r\n" + 
						"        (RequestBodyType \"JSON\")\r\n" + 
						"        (Request\r\n" + 
						"                (Argument (URLMapping \"$keyword\") (DynamicValue \"Keyword\"))\r\n" + 
						"        )\r\n" + 
						"        (Response \"bindings\" \"JSON\"\r\n" + 
						"                (Arguments (Argument\r\n" + 
						"                (BodyMapping \"OBJECT\" \"results\" \r\n" + 
						"                        (Arguments \r\n" + 
						"                                (Argument (BodyMapping \"LIST\" \"bindings\"\r\n" + 
						"                                        (Arguments (Argument (BodyMapping \"OBJECT\" \"Abstract\"\r\n" + 
						"                                                (Arguments (Argument (BodyMapping \"STRING\" \"value\") (DynamicValue \"value\")))\r\n" + 
						"                                        ) (DynamicValue \"Abstract\")))\r\n" + 
						"                                 ) (DynamicValue \"Value\")) \r\n" + 
						"                        )\r\n" + 
						"                ) (DynamicValue \"Bindings\")))\r\n" + 
						"        )\r\n" + 
						")\r\n" + 
						""; 
				this.send(KmLauncher.CLOUD_ADDRESS, initMsg);
				
				this.send(KmLauncher.CLOUD_ADDRESS, "(CloudSpecification \r\n" + 
	                    "        (GLName \"QueryCloudSchedule\") \r\n" + 
	                    "        (GLMethod \"QUERY\") \r\n" + 
	                    "        (URL \"https://www.googleapis.com/calendar/v3/calendars/$calendarID/events\") \r\n" + 
	                    "        (Method \"GET\") \r\n" + 
	                    "        (OAuth2 \"Google\" \"375056564837-u2qvl77r9mcup2lbfn4n25miod5uthtc.apps.googleusercontent.com\" \"EjkicNWpeyMinXnPKDtJFBuG\" \"https://www.googleapis.com/auth/calendar\")\r\n" + 
	                    "        (RequestBodyType \"JSON\")\r\n" + 
	                    "        (Request\r\n" + 
	                    "                (Argument (URLMapping \"$calendarID\") (FixedValue \"primary\"))\r\n" + 
	                    "        )\r\n" + 
	                    "        (Response \"Events\" \"JSON\"\r\n" + 
	                    "                (Arguments (Argument\r\n" + 
	                    "                (BodyMapping \"LIST\" \"items\" \r\n" + 
	                    "                        (Arguments \r\n" + 
	                    "                                (Argument (BodyMapping \"OBJECT\" \"start\"\r\n" + 
	                    "                                        (Arguments (Argument (BodyMapping \"STRING\" \"dateTime\") (DynamicValue \"DateTime\")))\r\n" + 
	                    "                                 ) (DynamicValue \"Start\")) \r\n" +
	                    "                                (Argument (BodyMapping \"OBJECT\" \"end\"\r\n" + 
	                    "                                        (Arguments (Argument (BodyMapping \"STRING\" \"dateTime\") (DynamicValue \"DateTime\")))\r\n" + 
	                    "                                 ) (DynamicValue \"End\")) \r\n" +
	                    "                                (Argument (BodyMapping \"STRING\" \"summary\"\r\n" + 
	                    "                                 ) (DynamicValue \"Summary\")) \r\n" +
	                    "                        )\r\n" + 
	                    "                ) (DynamicValue \"Event\")))\r\n" + 
	                    "        )\r\n" + 
	                    ")");
				
				this.send(KmLauncher.CLOUD_ADDRESS, "(CloudSpecification \r\n" + 
	                    "        (GLName \"WeatherAnnouncement\") \r\n" + 
	                    "        (GLMethod \"QUERY\") \r\n" + 
	                    "        (URL \"http://newsky2.kma.go.kr/service/VilageFrcstDspthDocInfoService/WidGeneralWeatherCondition?stnId=109&_type=json\") \r\n" + 
	                    "        (Method \"GET\") \r\n" + 
	                    "        (ApiKey \"FGu4RZnaLFP9RduL%2FhBObl6TU2fCS2fzqIlACh%2FElRZ2vspcQHsEFSXM5ZIrWJIkLZym1MO4AXOk3j00EIXM0w%3D%3D\")\r\n" + 
	                    "        (RequestBodyType \"JSON\")\r\n" + 
	                    "        (Request\r\n" + 
	                    "        )\r\n" + 
	                    "        (Response \"Weather\" \"JSON\"\r\n" + 
	                    "                (Arguments (Argument\r\n" + 
	                    "                (BodyMapping \"OBJECT\" \"response\" \r\n" + 
	                    "                        (Arguments \r\n" + 
	                    "                                (Argument (BodyMapping \"OBJECT\" \"body\"\r\n" + 
	                    "                                        (Arguments (Argument (BodyMapping \"OBJECT\" \"items\" (Arguments (Argument (BodyMapping \"OBJECT\" \"item\" (Arguments (Argument (BodyMapping \"STRING\" \"wfSv1\") (DynamicMapping \"Content\")))) (DynamicMapping \"item\")))) (DynamicValue \"Items\")))\r\n" + 
	                    "                                 ) (DynamicValue \"Body\")) \r\n" +
	                    "                        )\r\n" + 
	                    "                ) (DynamicValue \"Response\")))\r\n" + 
	                    "        )\r\n" + 
	                    ")");
				
				
				this.send(KmLauncher.CLOUD_ADDRESS, "(CloudSpecification\r\n" + 
	                      "(GLName \"WeatherReport\")\r\n" + 
	                      "(GLMethod \"QUERY\")\r\n" + 
	                      "(URL \"http://newsky2.kma.go.kr/service/VilageFrcstDspthDocInfoService/WidOverlandForecast?regId=11B10101&_type=json\")\r\n" + 
	                      "(Method \"GET\")\r\n" + 
	                      "(ApiKey \"FGu4RZnaLFP9RduL%2FhBObl6TU2fCS2fzqIlACh%2FElRZ2vspcQHsEFSXM5ZIrWJIkLZym1MO4AXOk3j00EIXM0w%3D%3D\") \r\n" + 
	                      "(RequestBodyType \"JSON\") \r\n" + 
	                      "(Request) \r\n" + 
	                      "(Response \"Weather\" \"JSON\" (Arguments \r\n" + 
	                      "   (Argument \r\n" + 
	                      "      (BodyMapping \"OBJECT\" \"response\" (Arguments \r\n" + 
	                      "         (Argument (BodyMapping \"OBJECT\" \"body\" (Arguments \r\n" + 
	                      "            (Argument (BodyMapping \"OBJECT\" \"items\" (Arguments \r\n" + 
	                      "               (Argument (BodyMapping \"LIST\" \"item\" (Arguments \r\n" + 
	                      "                  (Argument (BodyMapping \"LONG\" \"announceTime\") (DynamicMapping \"AnnounceTime\")) \r\n" +
	                      "                  (Argument (BodyMapping \"LONG\" \"numEf\") (DynamicMapping \"numEf\")) \r\n" + 
	                      "                  (Argument (BodyMapping \"LONG\" \"ta\") (DynamicMapping \"Temperature\")) \r\n" + 
	                      "                  (Argument (BodyMapping \"STRING\" \"wf\") (DynamicMapping \"Weather\"))\r\n" + 
	                      "                  (Argument (BodyMapping \"LONG\" \"rnYn\") (DynamicMapping \"RainCode\")) \r\n" + 
	                      "               )) (DynamicMapping \"item\"))\r\n" + 
	                      "            )) (DynamicValue \"Items\"))\r\n" + 
	                      "         )) (DynamicValue \"Body\")) \r\n" + 
	                      "      )) (DynamicValue \"Response\"))\r\n" + 
	                      "   ) \r\n" + 
	                      "))");
				
				System.out.println("<INIT> : Cloud Interface Manager Initializing Message Post!");
				
				System.out.println("<INIT> : Knowledge Manager Initialized!");
				
				break;
			}

		
		default:
			break;
		}

	}

	
	@Override
	public String onSubscribe(String sender, String subscribe) {
		return super.onSubscribe(sender, subscribe);
	}

}
