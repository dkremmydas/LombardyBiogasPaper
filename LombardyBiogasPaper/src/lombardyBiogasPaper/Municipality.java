package lombardyBiogasPaper;

import repast.simphony.context.DefaultContext;

public class Municipality extends DefaultContext<Farm> {
	
	private int id;
	private String originalName;
	
	
	public Municipality(int id, String originalName) {
		super(originalName,id);
		this.id = id;
		this.originalName = originalName;
	}


	@Override
	public String toString() {
		return "Municipality [id=" + id + ", originalName=" + originalName
				+ ", allObjs=" + allObjs + "]";
	}
	
	
	
	
	

}
