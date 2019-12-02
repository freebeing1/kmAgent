package kr.ac.hanyang.action.argument;

public class InitializeArg {

	private String servicePackageName;
	private String result;
	
	public InitializeArg(String servicePackageName) {
		this.servicePackageName = servicePackageName;
	}
	
	public String getServicePackageName() {
		return servicePackageName;
	}
	
	public void setServicePackageName(String servicePackageName) {
		this.servicePackageName = servicePackageName;
	}
	
	public String getResult() {
		return result;
	}
	
	public void setResult(String result) {
		this.result = result;
	}
	
	@Override
	public String toString() {
		
//		System.out.println(result);
		return result;
	}
	
}
