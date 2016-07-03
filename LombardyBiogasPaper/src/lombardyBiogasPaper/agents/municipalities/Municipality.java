package lombardyBiogasPaper.agents.municipalities;

import java.util.Map;

import lombardyBiogasPaper.agents.farms.Farm;
import lombardyBiogasPaper.crops.ArableCrop;
import lombardyBiogasPaper.realityGenerators.prices.DefaultPriceGenerator;
import lombardyBiogasPaper.realityGenerators.prices.PriceGenerator;
import repast.simphony.context.DefaultContext;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class Municipality extends DefaultContext<Farm> {
	
	private String originalName;
	
	private Table<Integer,ArableCrop,Long> priceHistory = HashBasedTable.create();
	
	private PriceGenerator priceRealizationRule; 

	public Municipality(int id, String originalName, Map<ArableCrop,Long> initPrices) {
		super(id,id);
		this.originalName = originalName;
		
		//add initial crop prices
		for(ArableCrop c: initPrices.keySet()) {
			priceHistory.put(-1, c, initPrices.get(c));
		}
		
		priceRealizationRule = new DefaultPriceGenerator(priceHistory.rowMap().get(-1));
		
	}
	
	public Table<Integer, ArableCrop, Long> getPriceHistory() {
		return this.priceHistory;
	}
	
	public void insertToPriceHistory(Integer tick,Map<ArableCrop, Long> v) {
		this.priceHistory.row(tick).putAll(v);
	}
	
	public PriceGenerator getPriceRealizationRule() {
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
	

}
