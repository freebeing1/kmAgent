package kr.ac.hanyang.action;

import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.json.simple.JSONObject;

import kr.ac.hanyang.Point3D;
import kr.ac.hanyang.action.argument.PathFindingArg;
import kr.ac.uos.ai.arbi.model.Expression;
import kr.ac.uos.ai.arbi.model.ExpressionList;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;

/**
 * 경로 추론 알고리즘
 * 목적지 포인트 리스트 리턴
 * 
 * 1. 시작지 부터 목적지 까지 경로를 모두 구해서 리스트에 저장
 * 2. 해당 경로의 경로 거리를 구함
 * 3. 최적 경로 리스트만 골라냄
 * 
 * @author bh9928
 *
 */

public class PathFindingAction extends KnowledgeProcessAction {

	private List<ArrayList<String>> pathList = new ArrayList<ArrayList<String>>();
	// 경로들의 리스트 

	@SuppressWarnings("unchecked")
	@Override
	public Object execute(Object o) {
		long startTime = System.currentTimeMillis();
		PathFindingArg arg = (PathFindingArg) o;
		pathList.clear();
		String start = arg.getStart();
		String end = arg.getEnd();
		/*
		 * ArrayList<String> pathX = new ArrayList<String>(); 
		 * List<Point3D> path = new ArrayList<Point3D>(); 
		 * pathplanningByRelation(start, end, pathX); 
		 * path = optimumPathCoord(arg.getPosition(), pathList);
		 * System.out.println(path);
		 */

		String pathType = arg.getPathType();
		ExpressionList expList = new ExpressionList();
		
		// pathType == coordinate -> 지나는 좌표값들을 리턴
		// pathType == name -> 경로 이름 리턴 (지나는 지점)
		// pathType == action -> 경로를 위해 수행해야할 액션 목록 리턴
		
		Expression resultExp = null;
		
		if (pathType.equals("coordinate")) {
			ArrayList<String> pathX = new ArrayList<String>();
			List<Point3D> path = new ArrayList<Point3D>();
			pathplanningByRelation(start, end, pathX);
			path = optimumPathCoord(arg.getPosition(), pathList);
			path.remove(0);
			for (Point3D point : path) {
				Expression x = GLFactory.newValueExpression((float) point.getX());
				Expression y = GLFactory.newValueExpression((float) point.getY());
				Expression z = GLFactory.newValueExpression((float) point.getZ());

				GeneralizedList gl = GLFactory.newGL("coordinate", x, y, z);

				expList.add(GLFactory.newExpression(gl));
			}
		} else if(pathType.equals("name")) {// 이름 검색
			ArrayList<String> pathX = new ArrayList<String>();
			List<String> path = new ArrayList<String>();
			pathplanningByRelation(start, end, pathX);
			path = optimumPathName(arg.getPosition(), pathList);
			
			for (String str : path) {
				expList.add(GLFactory.newValueExpression(str));
			}
		} else if(pathType.equals("action")) { //action 으로 반환
			
			int startLevel = 0;
			int endLevel = 0;
			
			String queryString = ""
					+ PREFIX
					+ "SELECT ?sln ?eln "
					+ "WHERE {"
					+ "<" + start + "> knowrob:hasLevels ?sl . "
					+ "?sl knowrob:floorNumber ?sln . "
					+ "<" + end + "> knowrob:hasLevels ?el . "
					+ "?el knowrob:floorNumber ?eln . "
					+ "}"
					+ "";
//			System.out.println(queryString);
			
			Query sQuery = QueryFactory.create(queryString);
//			QueryExecution qe = QueryExecutionFactory.create(sQuery, DemoKM_OntModel); // 연구실 데모용
			QueryExecution qe = QueryExecutionFactory.create(sQuery, service_OntModel); // 통합용
			
			ResultSet results = qe.execSelect();
			
			while(results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				startLevel = soln.getLiteral("sln").getInt();
				endLevel = soln.getLiteral("eln").getInt();
			}
			
			if(startLevel == endLevel) {
				ArrayList<String> pathX = new ArrayList<String>();
				List<Point3D> path = new ArrayList<Point3D>();
				pathplanningByRelation(start, end, pathX);
				path = optimumPathCoord(arg.getPosition(), pathList);
				path.remove(0);
				for (Point3D point : path) {
					Expression x = GLFactory.newValueExpression((float) point.getX());
					Expression y = GLFactory.newValueExpression((float) point.getY());
					Expression z = GLFactory.newValueExpression((float) point.getZ());

					GeneralizedList gl = GLFactory.newGL("coordinate", x, y, z);

					expList.add(GLFactory.newExpression(gl));
				}
				ExpressionList tempExpList = new ExpressionList();
				GeneralizedList tempGL = expList.get(1).asGeneralizedList();
				while(tempGL != null) {
					
					double e_x = expList.get(1).asGeneralizedList().getExpression(0).asValue().floatValue();
					double e_y = expList.get(1).asGeneralizedList().getExpression(1).asValue().floatValue();
					double e_z = expList.get(1).asGeneralizedList().getExpression(2).asValue().floatValue();
					
					e_x = Math.round(e_x*1000)/1000.0;
					e_y = Math.round(e_y*1000)/1000.0;
					e_z = Math.round(e_z*1000)/1000.0;
					
					Expression exp1 = GLFactory.newValueExpression("moveTo({"+e_x+" "+e_y+" "+e_z+"})");
					Expression exp2 = GLFactory.newGLExpression("action", exp1);
					
					tempExpList.add(exp2);
					
					expList.remove(0);
					try
					{
						tempGL = expList.get(1).asGeneralizedList();
					}
					catch (Exception e)
					{
						break;
					}
				}
				expList = tempExpList;
				
			} else {
				ArrayList<String> pathX = new ArrayList<String>();
				List<Point3D> path = new ArrayList<Point3D>();
				
				String sElv = "";
				String eElv = "";
				
				String queryString1 = ""
						+ PREFIX
						+ "SELECT ?S_elv ?E_elv "
						+ "WHERE {"
						+ "?S_elv knowrob:floorNumber \"" + startLevel + "\"^^xsd:integer . "
						+ "?S_elv rdf:type knowrob:Elevator . "
						+ "?E_elv knowrob:floorNumber \"" + endLevel + "\"^^xsd:integer . "
						+ "?E_elv rdf:type knowrob:Elevator . "
						+ "}";
//				System.out.println(queryString1);
				Query sQuery1 = QueryFactory.create(queryString1);
//				QueryExecution qe1 = QueryExecutionFactory.create(sQuery1, DemoKM_OntModel); // 연구실 데모용
				QueryExecution qe1 = QueryExecutionFactory.create(sQuery1, service_OntModel); // 통합용
				ResultSet results1 = qe1.execSelect();
				
				while(results1.hasNext()) {
					QuerySolution soln = results1.nextSolution();
					sElv = soln.getResource("S_elv").getURI();
					eElv = soln.getResource("E_elv").getURI();
				}
				
				pathplanningByRelation(start, sElv, pathX); // "http://www.robot-arbi.kr/ontologies/isro_map.owl#Elevator001"
				path = optimumPathCoord(arg.getPosition(), pathList);
				System.out.println(pathList);
				
				for (Point3D point : path) {
					Expression x = GLFactory.newValueExpression((float) point.getX());
					Expression y = GLFactory.newValueExpression((float) point.getY());
					Expression z = GLFactory.newValueExpression((float) point.getZ());

					GeneralizedList gl = GLFactory.newGL("coordinate", x, y, z);

					expList.add(GLFactory.newExpression(gl));
				}
//				System.out.println(GLFactory.newGLExpression("test", expList).toString());
				
				ExpressionList tempExpList = new ExpressionList();
				GeneralizedList tempGL = expList.get(1).asGeneralizedList();
				
				while(tempGL != null) {
					
					double e_x = expList.get(1).asGeneralizedList().getExpression(0).asValue().floatValue();
					double e_y = expList.get(1).asGeneralizedList().getExpression(1).asValue().floatValue();
					double e_z = expList.get(1).asGeneralizedList().getExpression(2).asValue().floatValue();
					
					e_x = Math.round(e_x*1000)/1000.0;
					e_y = Math.round(e_y*1000)/1000.0;
					e_z = Math.round(e_z*1000)/1000.0;
					
					Expression exp1 = GLFactory.newValueExpression("moveTo({"+e_x+" "+e_y+" "+e_z+"})");
					Expression exp2 = GLFactory.newGLExpression("action", exp1);
					
					tempExpList.add(exp2);
					
					expList.remove(0);
//					System.out.println(expList.get(0).asGeneralizedList().toString());
					try
					{
						tempGL = expList.get(1).asGeneralizedList();
					}
					catch (Exception e)
					{
						break;
					}
				}
				
				Expression tempExp1 = GLFactory.newValueExpression("translocateLevel(["+startLevel+"] ["+endLevel+"])");
				Expression tempExp2 = GLFactory.newGLExpression("action", tempExp1);
				tempExpList.add(tempExp2);
				
				pathX = new ArrayList<String>();
				path = new ArrayList<Point3D>();
				pathList.clear();
				pathplanningByRelation(eElv, end, pathX);
				GeneralizedList eGL = expList.get(0).asGeneralizedList();
				
				Point3D ePoint = new Point3D(eGL.getExpression(0).asValue().floatValue(), eGL.getExpression(1).asValue().floatValue(), eGL.getExpression(2).asValue().floatValue());
				path = optimumPathCoord(ePoint, pathList);
				expList = new ExpressionList();
				for (Point3D point : path) {
					Expression x = GLFactory.newValueExpression((float) point.getX());
					Expression y = GLFactory.newValueExpression((float) point.getY());
					Expression z = GLFactory.newValueExpression((float) point.getZ());

					GeneralizedList gl = GLFactory.newGL("coordinate", x, y, z);

					expList.add(GLFactory.newExpression(gl));
				}
				expList.remove(0);
				tempGL = expList.get(1).asGeneralizedList();
				
				while(tempGL != null) {
					double e_x = expList.get(1).asGeneralizedList().getExpression(0).asValue().floatValue();
					double e_y = expList.get(1).asGeneralizedList().getExpression(1).asValue().floatValue();
					double e_z = expList.get(1).asGeneralizedList().getExpression(2).asValue().floatValue();
					
					e_x = Math.round(e_x*1000)/1000.0;
					e_y = Math.round(e_y*1000)/1000.0;
					e_z = Math.round(e_z*1000)/1000.0;
					
					Expression exp1 = GLFactory.newValueExpression("moveTo({"+e_x+" "+e_y+" "+e_z+"})");
					Expression exp2 = GLFactory.newGLExpression("action", exp1);
					
					tempExpList.add(exp2);
					
					expList.remove(0);
					try
					{
						tempGL = expList.get(1).asGeneralizedList();
					}
					catch (Exception e)
					{
						break;
					}
				}
				expList = tempExpList;	
			}
		} else {
			System.out.println("path type error...");
		}
		
		resultExp = GLFactory.newGLExpression("result", expList);
		
		Expression expType = GLFactory.newValueExpression(pathType);
		
		String result =  GLFactory.newGL("requestPath", expType, resultExp).toString();
		long time = System.currentTimeMillis() - startTime;
		
		JSONObject log = new JSONObject();
		JSONObject queryLog = new JSONObject();
		
		queryLog.put("Result",result);
		queryLog.put("InferenceTime", time+"ms");
		log.put("msg", queryLog);
		log.put("start", start);
		log.put("end", end);
		log.put("pathType", pathType);
		log.put("Ontology Scale", ontologyMonitor_Scale());
		log.put("GL", result);
		
		arg.setResult(log.toJSONString());

		return arg.getResult();
	}

	
	/**
	 * 목적지에 도달하는 모든 경로를 찾아주는 함수
	 * 재귀 함수
	 * 현재 장소에서 목적지 나올떄 까지 재귀
	 * 
	 * @param departure	출발지
	 * @param destination 목적지
	 * @param list 저장 공간
	 * 
	 * @author freebeing1
	 */
	@SuppressWarnings("unchecked")
	public void pathplanningByRelation(String departure, String destination, ArrayList<String> list) {

		
		list.add(departure);

		String queryString = ""
				+ PREFIX

				+ "SELECT "
				
				+ "?connectedLocation "
				
				+ "WHERE {"
				
				+ "<"+departure+"> 		isro:architecturallyConnectedTo 		?connectedLocation . "
				
				+ "}";

		Query query = QueryFactory.create(queryString);
		QueryExecution qe = QueryExecutionFactory.create(query, ISRO_map_OntModel);

		ResultSet results = qe.execSelect();

		//결과 - > 현재 위치에서 연결되어있는 위치 
		while (results.hasNext()) {
			QuerySolution soln = results.nextSolution();
			departure = soln.getResource("connectedLocation").getURI();
			if (list.contains(departure)) { //현재 위치가 다시나온경우  리스트 포함 x
				continue;
			} else {
				pathplanningByRelation(departure, destination, (ArrayList<String>) list.clone());
				//그 다음 지점 부터 다시 함수 시작
			}
		}
		
		//최종적으로 경로 리스트에 모든 경로가 저장됨
		if (list.get(list.size() - 1).equals(destination)) {
			pathList.add(list);
		}
	}

	
	/**
	 * 목적지에 도달하는 모든 경로 중 최단 경로를 찾아주는 함수
	 * 
	 * @param startPoint 출발 지점 좌표
	 * @param ListOfPath 목적지에 도달하는 모든 경로가 담긴 리스트
	 * @return 최단경로(좌표값 리스트)
	 * 
	 * @author freebeing1
	 */
	public List<Point3D> optimumPathCoord(Point3D startPoint, List<ArrayList<String>> ListOfPath) {

		List<Point3D> optimumPathCoord = PathToCoord(startPoint, ListOfPath.get(0));
		List<Point3D> comparePathCoord;
		double optimumDistance = pathDistance(optimumPathCoord);
		double compareDistance;
		for (int i = 0; i < ListOfPath.size(); i++) {
			comparePathCoord = PathToCoord(startPoint, ListOfPath.get(i));
			compareDistance = pathDistance(comparePathCoord);
			if (compareDistance <= optimumDistance) {
				optimumDistance = compareDistance;
				optimumPathCoord = comparePathCoord;
			}
		}
		// System.out.println("Optimum Distance = " + optimumDistance);
		return optimumPathCoord;
	}

	
	/**
	 * ID값 리스트로 이루어진 경로를 좌표 경로로 바꿔주는 함수
	 * 
	 * @param startPoint 출발 지점 좌표
	 * @param path 경로 (ID값 리스트)
	 * @return 경로(좌표값 리스트)
	 * 
	 * @author freebeing1
	 */
	public List<Point3D> PathToCoord(Point3D startPoint, ArrayList<String> path) {


		List<Point3D> coordList = new ArrayList<Point3D>();

		coordList.add(startPoint);
		
//		System.out.println(path);
//		System.out.println(path.size());
		for (int i = 0; i < path.size() - 1; i++) {

			String queryString = "" 
					+ PREFIX

					+ "SELECT \n"
					+ "\n"

					+ "?R2H_IP_x  ?R2H_IP_y  ?R2H_IP_z \n"
					+ "?R2H_EP_x  ?R2H_EP_y  ?R2H_EP_z \n"

					+ "?ISP_x     ?ISP_y     ?ISP_z \n"

					+ "?H2R_EP_x  ?H2R_EP_y  ?H2R_EP_z \n"
					+ "?H2R_IP_x  ?H2R_IP_y  ?H2R_IP_z \n"
					
					+ "?R2R_EP_x  ?R2R_EP_y  ?R2R_EP_z \n"
					+ "?R2R_IP_x  ?R2R_IP_y  ?R2R_IP_z \n"

					+ "?H2E_EP_x  ?H2E_EP_y  ?H2E_EP_z \n"
					+ "?H2E_IP_x  ?H2E_IP_y  ?H2E_IP_z \n"
					
//					+ "?E2H_IP_x  ?E2H_IP_y  ?E2H_IP_z \n"
					+ "?E2H_EP_x  ?E2H_EP_y  ?E2H_EP_z \n"
					+ "\n"

					+ "WHERE {\n"
					+ "\n"

					+ "{\n"
					+ "<"+path.get(i)+">              rdf:type                           ?type1 . \n"
					+ "?type1                         rdfs:subClassOf                    knowrob:RoomInAConstruction . \n"
					+ "<"+path.get(i+1)+">            rdf:type                           knowrob:Hallway . \n"
					+ "?doorR2H                       knowrob:betweenContainers          <"+path.get(i)+"> . \n"
					+ "?doorR2H                       knowrob:betweenContainers          <"+path.get(i+1)+"> . \n"
					+ "?R2H_IP                        knowrob:relativeTo                 ?doorR2H . \n"
					+ "?R2H_EP                        knowrob:relativeTo                 ?doorR2H . \n"
					+ "<"+path.get(i)+">              isro:hasInnerPoint                 ?R2H_IP . \n"
					+ "<"+path.get(i)+">              isro:hasEntrancePoint              ?R2H_EP . \n"
					+ "?R2H_IP                        knowrob:xCoord                     ?R2H_IP_x . \n"
					+ "?R2H_IP                        knowrob:yCoord                     ?R2H_IP_y . \n"
					+ "?R2H_IP                        knowrob:zCoord                     ?R2H_IP_z . \n"
					+ "?R2H_EP                        knowrob:xCoord                     ?R2H_EP_x . \n"
					+ "?R2H_EP                        knowrob:yCoord                     ?R2H_EP_y . \n"
					+ "?R2H_EP                        knowrob:zCoord                     ?R2H_EP_z . \n"
					+ "}\n"
					+ "\n"

					+ " UNION \n"
					+ "\n"

					+ "{\n"
					+ "<"+path.get(i)+">              rdf:type                           knowrob:Hallway . \n"
					+ "<"+path.get(i+1)+">            rdf:type                           knowrob:Hallway . \n"
					+ "?IS                            isro:intersectionOf                <"+path.get(i)+"> . \n"
					+ "?IS                            isro:intersectionOf                <"+path.get(i+1)+"> . \n"
					+ "?IS                            knowrob:center                     ?ISP . \n"
					+ "?ISP                           knowrob:xCoord                     ?ISP_x . \n"
					+ "?ISP                           knowrob:yCoord                     ?ISP_y . \n"
					+ "?ISP                           knowrob:zCoord                     ?ISP_z . \n"
					+ "}\n"
					+ "\n"

					+ " UNION \n"
					+ "\n"

					+ "{\n"
					+ "<"+path.get(i)+">              rdf:type                           knowrob:Hallway . \n"
					+ "<"+path.get(i+1)+">            rdf:type                           ?type2 . \n"
					+ "?type2                         rdfs:subClassOf                    knowrob:RoomInAConstruction . \n"
					+ "?doorH2R                       knowrob:betweenContainers          <"+path.get(i)+"> . \n"
					+ "?doorH2R                       knowrob:betweenContainers          <"+path.get(i+1)+"> . \n"
					+ "?H2R_EP                        knowrob:relativeTo                 ?doorH2R . \n"
					+ "?H2R_IP                        knowrob:relativeTo                 ?doorH2R . \n"
					+ "<"+path.get(i+1)+">            isro:hasEntrancePoint              ?H2R_EP . \n"
					+ "<"+path.get(i+1)+">            isro:hasInnerPoint                 ?H2R_IP . \n"
					+ "?H2R_EP                        knowrob:xCoord                     ?H2R_EP_x . \n"
					+ "?H2R_EP                        knowrob:yCoord                     ?H2R_EP_y . \n"
					+ "?H2R_EP                        knowrob:zCoord                     ?H2R_EP_z . \n"
					+ "?H2R_IP                        knowrob:xCoord                     ?H2R_IP_x . \n"
					+ "?H2R_IP                        knowrob:yCoord                     ?H2R_IP_y . \n"
					+ "?H2R_IP                        knowrob:zCoord                     ?H2R_IP_z . \n"
					+ "}\n"
					+ "\n"

					+ " UNION \n"
					+ "\n"

					+ "{\n"
					+ "<"+path.get(i)+">              rdf:type                           ?type3 . \n"
					+ "?type3                         rdfs:subClassOf                    knowrob:RoomInAConstruction . \n"
					+ "<"+path.get(i+1)+">            rdf:type                           ?type4 . \n"
					+ "?type4                         rdfs:subClassOf                    knowrob:RoomInAConstruction . \n"
					+ "?doorR2R                       knowrob:betweenContainers          <"+path.get(i)+"> . \n"
					+ "?doorR2R                       knowrob:betweenContainers          <"+path.get(i+1)+"> . \n"
					+ "?R2R_EP                        knowrob:relativeTo                 ?doorR2R . \n"
					+ "?R2R_IP                        knowrob:relativeTo                 ?doorR2R . \n"
					+ "<"+path.get(i)+">              isro:hasInnerPoint                 ?R2R_EP . \n"
					+ "<"+path.get(i+1)+">            isro:hasInnerPoint                 ?R2R_IP . \n"
					+ "?R2R_EP                        knowrob:xCoord                     ?R2R_EP_x . \n"
					+ "?R2R_EP                        knowrob:yCoord                     ?R2R_EP_y . \n"
					+ "?R2R_EP                        knowrob:zCoord                     ?R2R_EP_z . \n"
					+ "?R2R_IP                        knowrob:xCoord                     ?R2R_IP_x . \n"
					+ "?R2R_IP                        knowrob:yCoord                     ?R2R_IP_y . \n"
					+ "?R2R_IP                        knowrob:zCoord                     ?R2R_IP_z . \n"
					+ "}\n"
					+ "\n"

					+ " UNION \n"
					+ "\n"

					+ "{\n"
					+ "<"+path.get(i)+">              rdf:type                           knowrob:Hallway . \n"
					+ "<"+path.get(i+1)+">            rdf:type                           knowrob:Elevator . \n"
					+ "?doorH2E                       knowrob:betweenContainers          <"+path.get(i)+"> . \n"
					+ "?doorH2E                       knowrob:betweenContainers          <"+path.get(i+1)+"> . \n"
					+ "?H2E_EP                        knowrob:relativeTo                 ?doorH2E . \n"
					+ "?H2E_IP                        knowrob:relativeTo                 ?doorH2E . \n"
					+ "<"+path.get(i+1)+">            isro:hasEntrancePoint              ?H2E_EP . \n"
					+ "<"+path.get(i+1)+">            isro:hasInnerPoint                 ?H2E_IP . \n"
					+ "?H2E_EP                        knowrob:xCoord                     ?H2E_EP_x . \n"
					+ "?H2E_EP                        knowrob:yCoord                     ?H2E_EP_y . \n"
					+ "?H2E_EP                        knowrob:zCoord                     ?H2E_EP_z . \n"
					+ "?H2E_IP                        knowrob:xCoord                     ?H2E_IP_x . \n"
					+ "?H2E_IP                        knowrob:yCoord                     ?H2E_IP_y . \n"
					+ "?H2E_IP                        knowrob:zCoord                     ?H2E_IP_z . \n"
					+ "}\n"
					+ "\n"
					
					+ " UNION \n"
					+ "\n"
					
					+ "{\n"
					+ "<"+path.get(i)+">              rdf:type                           knowrob:Elevator . \n"
					+ "<"+path.get(i+1)+">            rdf:type                           knowrob:Hallway . \n"
					+ "?doorE2H                       knowrob:betweenContainers          <"+path.get(i)+"> . \n"
					+ "?doorE2H                       knowrob:betweenContainers          <"+path.get(i+1)+"> . \n"
					+ "?E2H_EP                        knowrob:relativeTo                 ?doorE2H . \n"
//					+ "?E2H_IP                        knowrob:relativeTo                 ?doorE2H . \n"
					+ "<"+path.get(i)+">              isro:hasEntrancePoint              ?E2H_EP . \n"
//					+ "<"+path.get(i)+">              isro:hasInnerPoint                 ?E2H_IP . \n"
					+ "?E2H_EP                        knowrob:xCoord                     ?E2H_EP_x . \n"
					+ "?E2H_EP                        knowrob:yCoord                     ?E2H_EP_y . \n"
					+ "?E2H_EP                        knowrob:zCoord                     ?E2H_EP_z . \n"
//					+ "?E2H_IP                        knowrob:xCoord                     ?E2H_IP_x . \n"
//					+ "?E2H_IP                        knowrob:yCoord                     ?E2H_IP_y . \n"
//					+ "?E2H_IP                        knowrob:zCoord                     ?E2H_IP_z . \n"
					+ "}\n"
					+ "\n"
					
					+ "}\n";
			
//			System.out.println(queryString);
			
			Query query = QueryFactory.create(queryString);
//			QueryExecution qe = QueryExecutionFactory.create(query, DemoKM_OntModel); // 연구실 데모용
			QueryExecution qe = QueryExecutionFactory.create(query, service_OntModel); // 통합용

			ResultSet results = qe.execSelect();


			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();

				try {
					double R2H_IP_x = soln.getLiteral("R2H_IP_x").getDouble();
					double R2H_IP_y = soln.getLiteral("R2H_IP_y").getDouble();
					double R2H_IP_z = soln.getLiteral("R2H_IP_z").getDouble();
					double R2H_EP_x = soln.getLiteral("R2H_EP_x").getDouble();
					double R2H_EP_y = soln.getLiteral("R2H_EP_y").getDouble();
					double R2H_EP_z = soln.getLiteral("R2H_EP_z").getDouble();

					coordList.add(new Point3D(R2H_IP_x, R2H_IP_y, R2H_IP_z));
					coordList.add(new Point3D(R2H_EP_x, R2H_EP_y, R2H_EP_z));

				} catch (Exception e) {
					try {
						double ISP_x = soln.getLiteral("ISP_x").getDouble();
						double ISP_y = soln.getLiteral("ISP_y").getDouble();
						double ISP_z = soln.getLiteral("ISP_z").getDouble();

						coordList.add(new Point3D(ISP_x, ISP_y, ISP_z));

					} catch (Exception e2) {

						try {
							double H2R_EP_x = soln.getLiteral("H2R_EP_x").getDouble();
							double H2R_EP_y = soln.getLiteral("H2R_EP_y").getDouble();
							double H2R_EP_z = soln.getLiteral("H2R_EP_z").getDouble();
							double H2R_IP_x = soln.getLiteral("H2R_IP_x").getDouble();
							double H2R_IP_y = soln.getLiteral("H2R_IP_y").getDouble();
							double H2R_IP_z = soln.getLiteral("H2R_IP_z").getDouble();

							coordList.add(new Point3D(H2R_EP_x, H2R_EP_y, H2R_EP_z));
							coordList.add(new Point3D(H2R_IP_x, H2R_IP_y, H2R_IP_z));

						} catch (Exception e3) {
							try
							{
								double R2R_EP_x = soln.getLiteral("R2R_EP_x").getDouble();
								double R2R_EP_y = soln.getLiteral("R2R_EP_y").getDouble();
								double R2R_EP_z = soln.getLiteral("R2R_EP_z").getDouble();
								double R2R_IP_x = soln.getLiteral("R2R_IP_x").getDouble();
								double R2R_IP_y = soln.getLiteral("R2R_IP_y").getDouble();
								double R2R_IP_z = soln.getLiteral("R2R_IP_z").getDouble();

								coordList.add(new Point3D(R2R_EP_x, R2R_EP_y, R2R_EP_z));
								coordList.add(new Point3D(R2R_IP_x, R2R_IP_y, R2R_IP_z));
							}
							catch (Exception e4)
							{
								try
								{
									double H2E_EP_x = soln.getLiteral("H2E_EP_x").getDouble();
									double H2E_EP_y = soln.getLiteral("H2E_EP_y").getDouble();
									double H2E_EP_z = soln.getLiteral("H2E_EP_z").getDouble();
//									double H2E_IP_x = soln.getLiteral("H2E_IP_x").getDouble();
//									double H2E_IP_y = soln.getLiteral("H2E_IP_y").getDouble();
//									double H2E_IP_z = soln.getLiteral("H2E_IP_z").getDouble();
									
									coordList.add(new Point3D(H2E_EP_x, H2E_EP_y, H2E_EP_z));
//									coordList.add(new Point3D(H2E_IP_x, H2E_IP_y, H2E_IP_z));
								}
								catch (Exception e5)
								{
//									double E2H_IP_x = soln.getLiteral("E2H_IP_x").getDouble();
//									double E2H_IP_y = soln.getLiteral("E2H_IP_y").getDouble();
//									double E2H_IP_z = soln.getLiteral("E2H_IP_z").getDouble();
									double E2H_EP_x = soln.getLiteral("E2H_EP_x").getDouble();
									double E2H_EP_y = soln.getLiteral("E2H_EP_y").getDouble();
									double E2H_EP_z = soln.getLiteral("E2H_EP_z").getDouble();
									
//									coordList.add(new Point3D(E2H_IP_x, E2H_IP_y, E2H_IP_z));
									coordList.add(new Point3D(E2H_EP_x, E2H_EP_y, E2H_EP_z));
								}
								
								
							}
							

						}

					}

				}

			}

		}

//		System.out.println(coordList);

		return coordList;
	}

	
	/**
	 * 
	 * @param startPoint 출발지점 좌표
	 * @param ListOfPath 목적지에 도달하는 모든 경로가 담긴 리스트
	 * @return 최단경로(장소이름 리스트)
	 * 
	 * @author freebeing1
	 */
	public ArrayList<String> optimumPathName(Point3D startPoint, List<ArrayList<String>> ListOfPath) {
		
		List<Point3D> optimumPathCoord = PathToCoord(startPoint, ListOfPath.get(0));
		List<Point3D> comparePathCoord;
		ArrayList<String> optimumPath = ListOfPath.get(0);
		ArrayList<String> comparePath;
		ArrayList<String> pathName = new ArrayList<String>();
		double optimumDistance = pathDistance(optimumPathCoord);
		double compareDistance;
		for (int i = 0; i < ListOfPath.size(); i++) {
			comparePathCoord = PathToCoord(startPoint, ListOfPath.get(i));
			comparePath = ListOfPath.get(i);
			compareDistance = pathDistance(comparePathCoord);
			if (compareDistance <= optimumDistance) {
				optimumDistance = compareDistance;
				optimumPathCoord = comparePathCoord;
				optimumPath = comparePath;
			}
		}

		

		for (int i = 0; i < optimumPath.size(); i++) {
			String queryString = ""
					+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n"
					+ "PREFIX isro: <http://www.robot-arbi.kr/ontologies/isro.owl#> \n"
					+ "PREFIX knowrob: <http://knowrob.org/kb/knowrob.owl#> \n"
					
					+ "SELECT "
					
					+ "?name "
					
					+ "WHERE {"
					
					+ "<"+optimumPath.get(i)+"> knowrob:fullName ?name . "
					
					+ "}";

			Query query = QueryFactory.create(queryString);
			QueryExecution qe = QueryExecutionFactory.create(query, ISRO_map_OntModel);

			ResultSet results = qe.execSelect();
			while (results.hasNext()) {
				QuerySolution soln = results.nextSolution();
				pathName.add(soln.getLiteral("name").getString());
			}
		}
		return pathName;
	}

	
	/**
	 * 경로의 길이를 계산해주는 함수
	 * 
	 * @param coordOfPath 경로(좌표값 리스트) 
	 * @return 경로 길이
	 */
	public static double pathDistance(List<Point3D> coordOfPath) {

		double pathDistance = 0;
		for (int i = 0; i < coordOfPath.size() - 1; i++) {
			pathDistance += distance(coordOfPath.get(i), coordOfPath.get(i + 1));
		}
		return pathDistance;
	}

	
	/**
	 * 
	 * @param startPoint 출발지점 좌표
	 * @param endPoint 도착지점 좌표
	 * @return 유클리디언 거리
	 */
	public static double distance(Point3D startPoint, Point3D endPoint) {
		double dist, sum = 0;

		sum = Math.pow((startPoint.getX() - endPoint.getX()), 2) + Math.pow((startPoint.getY() - endPoint.getY()), 2)
				+ Math.pow((startPoint.getZ() - endPoint.getZ()), 2);

		dist = Math.sqrt(sum);
		// System.out.println(dist);
		return dist;
	}
}
