package kr.ac.hanyang.agent;

public class DmLauncher {
	// 에이전트 주소
	public static final String JMS_BROKER_URL = "tcp://127.0.0.1:61616"; // 내부테스트용
//	public static final String JMS_BROKER_URL = "tcp://172.16.0.67:61616"; // 통합용
	public static final String TASKMANAGER_ADDRESS = "agent://www.arbi.com/taskManager";
	public static final String CONTEXTMANAGER_ADDRESS = "agent://www.arbi.com/contextManager";
	public static final String KNOWLEDGEMANAGER_ADDRESS = "agent://www.arbi.com/knowledgeManager";
	public static final String PERCEPTION_ADDRESS = "agent://www.arbi.com/perception";
	public static final String ACTION_ADDRESS = "agent://www.arbi.com/action";
	public static final String INITIATOR_ADDRESS = "agent://www.arbi.com/initiator";
	public static final String CLOUD_ADDRESS = "agent://www.arbi.com/cloudInterfaceManager";
	public static final String INTERTACTION_MANAGER_ADDRESS = "http://www.arbi.com/interactionManager";
	public static final String DIALOGUEMANAGER_ADDRESS = "agent://www.arbi.com/dialogueManager";
	
	public static void main(String[] args) {
	
		new DmAgent();
		
	}

}
