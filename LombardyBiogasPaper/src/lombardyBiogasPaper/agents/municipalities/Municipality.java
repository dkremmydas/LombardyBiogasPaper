package lombardyBiogasPaper.agents.municipalities;

import lombardyBiogasPaper.agents.farms.Farm;
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
	
	public Iterable<Farm> getAllFarms() {
		return this.getAgentLayer(Farm.class);
	}
	
	public Long getAverageCash() {
		Long r = 0l;
		boolean first=true;
		for(Farm f: this.getAllFarms()) {
			if(first) {r=(long)f.getAccount().getCash();first=false;}
			else {
				r = ((long)r + (long)f.getAccount().getCash())/2;
			}
		}
		return r;
	}
	

}
