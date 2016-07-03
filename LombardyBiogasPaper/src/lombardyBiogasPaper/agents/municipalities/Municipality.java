package lombardyBiogasPaper.agents.municipalities;

import java.util.HashMap;
import java.util.Map;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.farms.Farm;
import lombardyBiogasPaper.crops.ArableCrop;
import repast.simphony.context.DefaultContext;
import repast.simphony.random.RandomHelper;
import cern.jet.random.Normal;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class Municipality extends DefaultContext<Farm> {
	
	private String originalName;
	
	private Table<Integer,ArableCrop,Long> priceHistory = HashBasedTable.create();
	
	private PriceRealizationRule priceRealizationRule = new PriceRealizationRule(priceHistory.rowMap().get(SimulationContext.getInstance().getCurrentYear()));
		
	public Municipality(int id, String originalName, Map<ArableCrop,Long> initPrices) {
		super(id,id);
		this.originalName = originalName;
		
		//add initial crop prices
		for(ArableCrop c: initPrices.keySet()) {
			priceHistory.put(0, c, initPrices.get(c));
		}
	}
	
	public Table<Integer, ArableCrop, Long> getPriceHistory() {
		return this.priceHistory;
	}
	
	public PriceRealizationRule getPriceRealizationRule() {
		return this.priceRealizationRule;
	}


	@Override
	public String toString() {
		String r = "Municipality [id=" + this.getId() + ", originalName=" + originalName + " Farms: [";
		
		for(Farm f: this.getAgentLayer(Farm.class)) {
			r += "\n" + f.toString();
		}
				
		return r+"\n] ]";
	}
	
	
	
	class PriceRealizationRule  {
		
		private Map<ArableCrop,Long> previousPrices = new HashMap<>();
		
		private float sigma = 0.3f;
		
		public PriceRealizationRule(Map<ArableCrop, Long> previousPrices) {
			this.previousPrices = previousPrices;
		}
		
		public PriceRealizationRule(Map<ArableCrop, Long> previousPrices,
				float sigma) {
			super();
			this.previousPrices = previousPrices;
			this.sigma = sigma;
		}



		public Map<ArableCrop,Long> getPrices() {
			HashMap<ArableCrop,Long> r = new HashMap<>();
			Iterable<ArableCrop> cs = SimulationContext.getInstance().getCrops().getAll();
			for(ArableCrop c: cs) {
				Long newVal = (long) (this.previousPrices.get(c) *( 1 + RandomHelper.getNormal().nextDouble(0, sigma)));
				r.put(c, newVal);
			}
			return r;
		}
	}
	
	

}
