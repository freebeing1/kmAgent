package kr.ac.hanyang.action.argument;

import kr.ac.hanyang.Point3D;

public class PathFindingArg extends InferenceArgument {
	
	private Point3D position;
	private String start;
	private String end;
	private String pathType;
	
	
	public PathFindingArg(String start, String end) {
		super();
		this.start = start;
		this.end = end;
	}
	
	

	public String getPathType() {
		return pathType;
	}
	public void setPathType(String pathType) {
		this.pathType = pathType;
	}



	public void setPosition(Point3D position){
		this.position = position;
	}
	public Point3D getPosition(){
		return position;
	}
	
	
	
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	
	
	
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	
	
	@Override
	public String toString() {
		return super.getResult();
	}
	
	
}
