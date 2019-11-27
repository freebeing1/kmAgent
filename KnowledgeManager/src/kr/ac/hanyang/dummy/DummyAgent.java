package kr.ac.hanyang.dummy;

import java.util.Scanner;

import kr.ac.hanyang.dummy.DummyLauncher;
import kr.ac.uos.ai.arbi.agent.ArbiAgent;
import kr.ac.uos.ai.arbi.agent.ArbiAgentExecutor;

public class DummyAgent extends ArbiAgent {
	
	Scanner sc = new Scanner(System.in); 
	
	public DummyAgent() {
		ArbiAgentExecutor.execute("configuration/DummyConfiguration.xml", this);
		
		
	}
	
	public void onStart() {
		
		super.onStart();
		
		while(true) {
			System.out.println("input msg");
			String msg = sc.nextLine();
			String res = request(DummyLauncher.KNOWLEDGEMANAGER_ADDRESS, msg);
//			String res = request(DummyLauncher.DIALOGUEMANAGER_ADDRESS, msg);
			
			System.out.println(res);
		}
		
		
	}
	
	
}
