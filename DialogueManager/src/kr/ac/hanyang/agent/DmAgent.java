package kr.ac.hanyang.agent;

import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;
import kr.ac.uos.ai.arbi.ltm.DataSource;
import kr.ac.uos.ai.arbi.model.GLFactory;
import kr.ac.uos.ai.arbi.model.GeneralizedList;
import kr.ac.uos.ai.arbi.model.parser.ParseException;

public class DmAgent extends ArbiAgent {
	
	private DataSource dc; // CDC
	private static boolean initiated = false;
	
	public DmAgent() {
		ArbiAgentExecutor.execute("configuration/DmConfiguration.xml", this);
	}
	
	@Override
	public void onStart() {
		System.out.println("++++++++++++++++++++++++++++");
		System.out.println("+   Dialog Manager Start   +");
		System.out.println("++++++++++++++++++++++++++++");
		System.out.println();
		
		dc = new DataSource();
		dc.connect(DmLauncher.JMS_BROKER_URL, "dc://dataSourceagentKM", 2); // CDC connection
		
		super.onStart();
		
		initiated = true;
	}
	
	@Override
	public String onQuery(String sender, String query) {
		
		System.out.println("<ON QUERY> (" + sender + ") : " + query);
		String result = "";
		String response = "";
		GeneralizedList queryGL = null;
		try {
			queryGL = GLFactory.newGLFromGLString(query);
		} catch (ParseException e) {
			System.out.println("<GL Error> : Parse error");
			e.printStackTrace();
		}
		String glName = queryGL.getName();
		
		switch(glName) {
		case "initiationCheck":
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
		default:
			result = "<GL Error> : GL NAME [" + glName + "] NOT DEFINED";
			System.out.println(result);
			response = "(error)";
			return response;
		}
		
		
	}
	
	@Override
	public String onRequest(String sender, String request) {
		
		String result = "";
		String response = "";
		System.out.println("<ON REQUEST> (" + sender + ") : " + request);
		
		GeneralizedList requestGL = null;
		try {
			requestGL = GLFactory.newGLFromGLString(request);
		} catch (ParseException e) {
			System.out.println("<GL Error> : Parse error");
			e.printStackTrace();
		}
		String glName = requestGL.getName();
		
		switch(glName) {
		
		case "requestConversationContent":
			if(true) {
				/*
				 * [GL format]
				 * (requestConversationContent (intention $intention) (arguments $arg1 $arg2 ...))
				 * 
				 * [Intention list]
				 * - InformationDelivery
				 * - Greeting
				 * - Inquiry
				 * - Request
				 * - Appointment
				 * - Agreement
				 * - Disagreement
				 * 
				 * [Example GL]
				 * 
				 * - argSize 1
				 * (requestConversationContent (intention "InformationDelivery") (arguments "갈증"))
				 * (requestConversationContent (intention "InformationDelivery") (arguments "긴급 호출"))
				 * 
				 * - argSize 2
				 * (requestConversationContent (intention "InformationDelivery") (arguments "테이블" "정리"))
				 * (requestConversationContent (intention "Request") (arguments "병" "적재"))
				 * (requestConversationContent (intention "Request") (arguments "병" "수거"))
				 * (requestConversationContent (intention "InformationDelivery") (arguments "전등" "소등"))
				 * (requestConversationContent (intention "InformationDelivery") (arguments "물" "전달"))
				 * (requestConversationContent (intention "InformationDelivery") (arguments "물" "불가"))
				 * 
				 * - argSize 3
				 * (requestConversationContent (intention "InformationDelivery") (arguments "유명수" "물" "당뇨"))
				 * (requestConversationContent (intention "InformationDelivery") (arguments "최병기" "주스" "중복"))
				 * (requestConversationContent (intention "InformationDelivery") (arguments "유명수" "낙상" "관리자"))
				 * 
				 * - argSize 4
				 * (requestConversationContent (intention "InformationDelivery") (arguments "이기호" (recommendation "물" "홍차") (disease "당뇨") (history "커피")))
				 * (requestConversationContent (intention "InformationDelivery") (arguments "이기호" (recommendation "물" "홍차" "주스" "커피") (disease "null") (history "null")))
				 * (requestConversationContent (intention "InformationDelivery") (arguments "이기호" (recommendation "물" "홍차" "커피") (disease "당뇨") (history "null")))
				 * (requestConversationContent (intention "InformationDelivery") (arguments "이기호" (recommendation "물" "홍차" "주스") (disease "null") (history "커피")))
				 * 
				 * 
				 */
				GeneralizedList intentionGL = requestGL.getExpression(0).asGeneralizedList();
				String intention = intentionGL.getExpression(0).asValue().stringValue();
				
				GeneralizedList argsGL = requestGL.getExpression(1).asGeneralizedList();
				int argSize = argsGL.getExpressionsSize();
				
				if(intention.equals("InformationDelivery")) 
				{
					if(argSize == 1) 
					{
						String arg1 = argsGL.getExpression(0).asValue().stringValue();
						if(arg1.equals("갈증")) {
							response = "(responseConversationContent \"음료를 준비하겠습니다.\")";
						}
						else if(arg1.contains("긴급")) {
							response = "(responseConversationContent \"" + arg1 + "을 시작합니다.\")";
						}
						else {
							System.out.println("<GL Error> : unknown argument error");
							System.out.println("[intention] : " + intention);
							System.out.println("[argSize] : " + argSize);
							System.out.println("[arg1] : " + arg1);
						}
					} 
					else if (argSize == 2)
					{
						String arg1 = argsGL.getExpression(0).asValue().stringValue();
						String arg2 = argsGL.getExpression(1).asValue().stringValue();
						if(arg2.equals("불가")) {
							if(arg1.equals("물")) {
								response = "(responseConversationContent \"죄송하지만, " + arg1 + "은 현재 제공해드릴 수 없습니다.\")";
							}
							else {
								response = "(responseConversationContent \"죄송하지만, " + arg1 + "는 현재 제공해드릴 수 없습니다.\")";
							}
						}
						else {
							if(arg1.equals("전등")) {
								response = "(responseConversationContent \"" + arg1 + "을 " + arg2 + "하겠습니다.\")";
							}
							else if(arg1.equals("물") || arg1.equals("테이블")) {
								response = "(responseConversationContent \"알겠습니다. " + arg1 + "을 " + arg2 + "해드리겠습니다.\")";
							}
							else {
								response = "(responseConversationContent \"알겠습니다. " + arg1 + "를 " + arg2 + "해드리겠습니다.\")";
							}
						}
					}
					else if (argSize == 3)
					{
						String arg1 = argsGL.getExpression(0).asValue().stringValue();
						String arg2 = argsGL.getExpression(1).asValue().stringValue();
						String arg3 = argsGL.getExpression(2).asValue().stringValue();
						
						if(arg1.equals("유명수")) 
						{
							if(arg2.equals("물")) {
								response = "(responseConversationContent \"" + arg1 + "님은 "+arg3+"가 있으시므로 " + arg2 + "을 드리겠습니다.\")";
							} 
							else if (arg2.equals("낙상")) {
								response = "(responseConversationContent \"" + arg1 + "님의 "+arg2+"이 발견되었습니다. " + arg3 + "를 호출합니다.\")";
							}
							else {
								System.out.println("<GL Error> : unknown argument error");
								System.out.println("[arg2] : " + arg2);
							}
						}
						else if(arg1.equals("최병기")) 
						{
							response = "(responseConversationContent \"" + arg1 + "님은 이미 커피를 드셨으므로 " + arg2 + "를 드리겠습니다.\")";
						}
						else {
							System.out.println("<GL Error> : unknown argument error");
							System.out.println("[intention] : " + intention);
							System.out.println("[argSize] : " + argSize);
							System.out.println("[arg1] : " + arg1);
						}
						
					} else if(argSize == 4) {
						String user = argsGL.getExpression(0).asValue().stringValue();
						GeneralizedList rGL = argsGL.getExpression(1).asGeneralizedList();
						int rSize = rGL.getExpressionsSize();
						String r = "";
						for(int i=0;i<rSize;i++) {
							r = r + rGL.getExpression(i).asValue().stringValue() + ", ";
						}
						r = r.substring(0, r.length()-2);
						
						GeneralizedList dGL = argsGL.getExpression(2).asGeneralizedList();
						String d = dGL.getExpression(0).asValue().stringValue();
						GeneralizedList hGL = argsGL.getExpression(3).asGeneralizedList();
						String h = hGL.getExpression(0).asValue().stringValue();
						
						if(d.equals("null") && h.equals("null")) {
							response = "(responseConversationContent \"" + user + " 님이 음료 중 드실 수 있는 것은 " + r + "입니다. 무엇을 드릴까요?\")";
						}
						else if(d.equals("null")) {
							response = "(responseConversationContent \"" + user + " 님은 이미 " + h + "를 드셨으므로, 음료 중 드실 수 있는 것은 " + r + "입니다. 무엇을 드릴까요?\")";
						}
						else if(h.equals("null")) {
							response = "(responseConversationContent \"" + user + " 님은 " + d + "가 있으시므로, 음료 중 드실 수 있는 것은 " + r + "입니다. 무엇을 드릴까요?\")";
						}
						else {
							response = "(responseConversationContent \"" + user + " 님은 " + d + "가 있으시고, 이미 " + h + "를 드셨으므로, 음료 중 드실 수 있는 것은 " + r + "입니다. 무엇을 드릴까요?\")";
						}
					}
					
					else {
						System.out.println("<GL Error> : arguments size error");
						System.out.println("[intention] : " + intention);
						System.out.println("[argSize] : " + argSize);
					}
				}
				else if(intention.equals("Request")) 
				{
					if(argSize == 1) 
					{
						System.out.println("<GL Error> : arguments size error");
						System.out.println("[intention] : " + intention);
						System.out.println("[argSize] : " + argSize);
					}
					else if(argSize == 2) 
					{
						String arg1 = argsGL.getExpression(0).asValue().stringValue();
						String arg2 = argsGL.getExpression(1).asValue().stringValue();
						
						if(arg2.equals("적재")) 
						{
							if(arg1.equals("물")) {
								response = "(responseConversationContent \"" + arg1 + "을 올려주시겠어요?\")";
							}
							else {
								response = "(responseConversationContent \"" + arg1 + "를 올려주시겠어요?\")";
							}
						}
						else if(arg2.equals("수거"))
						{
							if(arg1.equals("물")) {
								response = "(responseConversationContent \"" + arg1 + "을 가져가주세요.\")";
							}
							else {
								response = "(responseConversationContent \"" + arg1 + "를 가져가주세요.\")";
							}
						}
						else {
							System.out.println("<GL Error> : unknown argument error");
							System.out.println("[intention] : " + intention);
							System.out.println("[argSize] : " + argSize);
							System.out.println("[arg2] : " + arg2);
						}
					}
					else {
						System.out.println("<GL Error> : arguments size error");
						System.out.println("[intention] : " + intention);
						System.out.println("[argSize] : " + argSize);
					}
				}
				else {
					System.out.println("<GL Error> : unknown intention error");
					System.out.println("[intention] : " + intention);
					System.out.println("[argSize] : " + argSize);
				}
				result = "SUCCESS";
				
				System.out.println("<REQUEST> : [" + glName + "] " + result);
				System.out.println();
				
				return response;
			}
		case "requestConversationAnalysis":
			if(true) {
				/*
				 * [GL format]
				 * (requestConversationAnalysis $content)
				 * 
				 */
				
				String content = requestGL.getExpression(0).asValue().stringValue();
				
				if(content.contains("목말라") || content.contains("목 말라") || content.contains("목") || content.contains("먹었")) { // (requestConversationAnalysis "로봇아 나 목 말라")
					response = "(responseConversationAnalysis (speechIntention \"InformationDelivery\") (triple \"화자\" \"상태\" \"갈증\")) ";
					dc.assertFact(response);
					System.out.println("<AssertFact> : " + response);
				}
				
				else if(content.contains("정리")) { // (requestConversationAnalysis "로봇아 테이블 정리해줘")
					response = "(responseConversationAnalysis (speechIntention \"Request\") (arguments \"테이블\" \"정리\")) ";
					dc.assertFact(response);
					System.out.println("<AssertFact> : " + response);
				}// (responseConversationAnalysis (speechIntention "Request") (arguments "테이블" "정리"))
				
				else if(content.contains("긴급")) { // (requestConversationAnalysis "로봇아 긴급 호출해줘")
					response = "(responseConversationAnalysis (speechIntention \"Request\") (arguments \"긴급 호출\")) ";
					dc.assertFact(response);
					System.out.println("<AssertFact> : " + response);
				}// (responseConversationAnalysis (speechIntention "Request") (arguments "긴급 호출"))
				
				else if(content.contains("마실") || content.contains("먹을") || content.contains("갖다") || content.contains("가져다")) {
					if(content.contains("물")) {
						response = "(responseConversationAnalysis (speechIntention \"Request\") (arguments \"화자\" \"음료\" \"물\")) ";
					}
					else if(content.contains("커피")) {
						response = "(responseConversationAnalysis (speechIntention \"Request\") (arguments \"화자\" \"음료\" \"커피\")) ";
					}
					else if(content.contains("홍차")) {
						response = "(responseConversationAnalysis (speechIntention \"Request\") (arguments \"화자\" \"음료\" \"홍차\")) ";
					}
					else if(content.contains("주스")) {
						response = "(responseConversationAnalysis (speechIntention \"Request\") (arguments \"화자\" \"음료\" \"주스\")) ";
					}
					else {
						response = "(responseConversationAnalysis (speechIntention \"Request\") (arguments \"화자\" \"음료\" \"null\")) ";
					}
					dc.assertFact(response);
					System.out.println("<AssertFact> : " + response);
				}// (responseConversationAnalysis (speechIntention "Request") (arguments "화자" "후식" "물"))
				
				else {
					response = "(responseConversationAnalysis \"FAIL\")";
				}
				result = "SUCCESS";
				System.out.println("<REQUEST> : [" + glName + "] " + result);
				System.out.println();
				
				return response;
			}
			
		default:
			
			result = "<GL Error> : GL NAME [" + glName + "] NOT DEFINED";
			System.out.println(result);
			response = "(error)";
			return response;
		}
	}
}
