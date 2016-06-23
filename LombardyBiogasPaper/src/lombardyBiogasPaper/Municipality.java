package lombardyBiogasPaper;

import repast.simphony.context.DefaultContext;

public class Municipality extends DefaultContext<Farm> {
	
	private String originalName;
	
	
	public Municipality(int id, String originalName) {
		super(id,id);
		this.originalName = originalName;
	}


	@Override
	public String toString() {
		String r = "Municipality [id=" + this.getId() + ", originalName=" + originalName + " Farms: [";
		
		for(Farm f: this.getAgentLayer(Farm.class)) {
			r += "\n" + f.toString();
		}
				
		return r+"\n] ]";
	}
	
	
	
	
	

}
