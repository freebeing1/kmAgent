package kr.ac.hanyang.dummy;

public class DummyLauncher {
	
	public static final String JMS_BROKER_URL = "tcp://127.0.0.1:61616"; // 내부테스트용
//	public static final String JMS_BROKER_URL = "tcp://172.16.0.127:61616"; // 통합용
	public static final String KNOWLEDGEMANAGER_ADDRESS = "agent://www.arbi.com/knowledgeManager";
	public static final String DIALOGUEMANAGER_ADDRESS = "agent://www.arbi.com/dialogueManager";
	
	public static void main(String[] args) {
		new DummyAgent();
	}

}
