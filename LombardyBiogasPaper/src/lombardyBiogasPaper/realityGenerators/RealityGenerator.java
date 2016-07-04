package lombardyBiogasPaper.realityGenerators;

import java.util.HashMap;
import java.util.Map;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.farms.Farm;
import lombardyBiogasPaper.agents.municipalities.Municipality;
import lombardyBiogasPaper.crops.ArableCrop;
import lombardyBiogasPaper.realityGenerators.prices.DefaultGlobalPriceGenerator;
import lombardyBiogasPaper.realityGenerators.prices.PriceGenerator;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class RealityGenerator {
	
	/**
	 * Historical data on prices
	 */
	private HashMap <Municipality,Table<Integer,ArableCrop,Long>> priceHistory = new HashMap<>();
	
	/**
	 * Historical data on yields
	 */
	private HashMap <Municipality,Table<Integer,ArableCrop,Float>> yieldHistory = new HashMap<>();
	
	private PriceGenerator priceGenerator = new DefaultGlobalPriceGenerator(0.3);
	

	public RealityGenerator(Map<ArableCrop,Long> initPrices, Map<ArableCrop,Float> initYields) {
		
		//initialize historical data
		for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
			Table<Integer,ArableCrop,Long> t1 = HashBasedTable.create();
			priceHistory.put(m, t1);
			priceHistory.get(m).row(-1).putAll(initPrices);
			
			Table<Integer,ArableCrop,Float> t2 = HashBasedTable.create();
			yieldHistory.put(m, t2);
			yieldHistory.get(m).row(-1).putAll(initYields);
		}
		
	}

	/**
	 * Get prices and yields and update Farm
	 */
	public void realizeProduction() {
		this.realizeYields();
		this.realizePrices();
		this.updateFarmAccounts();
	}
	
	private void realizeYields() {
		int cy = SimulationContext.getInstance().getCurrentYear();
		
		//just copy previous year
		for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
			this.yieldHistory.get(m).row(cy).putAll(this.yieldHistory.get(m).row(cy-1));
		}
	}
	
	private void realizePrices() {
		int cy = SimulationContext.getInstance().getCurrentYear();
		Map<ArableCrop,Long> p = this.priceGenerator.getPrices();
		for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
			this.priceHistory.get(m).row(cy).putAll(p);
		}
	}
	

	private void updateFarmAccounts() {
		int cy = SimulationContext.getInstance().getCurrentYear();
		Iterable<ArableCrop> cs =  SimulationContext.getInstance().getCrops().getAll();
		for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
			 for(Farm f: m.getAgentLayer(Farm.class)) {
				 for(ArableCrop c: cs) {
					 Long pr = this.priceHistory.get(m).get(cy, c);
					 Float yi = this.yieldHistory.get(m).get(cy, c); 
					 Map<ArableCrop,Float> pl = f.getCropPlan(); 
					 Float la = pl.get(c); 
					 Long prof = pr*((long)(yi*la));
					 if(la.compareTo(0f)>0) {
						 f.getAccount().addCash(prof);
						 f.getAccount().removeCash((long)(f.getCropPlan().get(c)*f.getVarCost().get(c)));
					 }
					 
				};
			 }
		}
	}

	public HashMap<Municipality, Table<Integer, ArableCrop, Long>> getPriceHistory() {
		return priceHistory;
	}

	public HashMap<Municipality, Table<Integer, ArableCrop, Float>> getYieldHistory() {
		return yieldHistory;
	}
	
	
	public Table<Integer,ArableCrop, Float> getMunicipalityYieldHistory(Municipality m) {
		return this.yieldHistory.get(m);
	}
	
	/**
	 * 
	 * @param m
	 * @return
	 */
	public Table<Integer, ArableCrop, Long> getMunicipalityPriceHistory(Municipality m) {
		return this.priceHistory.get(m);
	}
	
	/**
	 * Get the prices map for a {@link Municipality} fr the last tick
	 * @param m
	 * @return
	 */
	public Map<ArableCrop, Long> getMunicipalityLastPrices(Municipality m) {
		int cy = SimulationContext.getInstance().getCurrentYear(), py;
		if(cy<0) {py=-1;} else {py=cy;}
		Table<Integer, ArableCrop, Long> t = this.priceHistory.get(m);
		return t.row(py);
	}
	
	/**
	 * Get the last entry of the yield table for a {@link Municipality}
	 * @param m
	 * @return
	 */
	public Map<ArableCrop, Float> getMunicipalityLastYields(Municipality m) {
		int cy = SimulationContext.getInstance().getCurrentYear(), py;
		if(cy<0) {py=-1;} else {py=cy;}
		Table<Integer, ArableCrop, Float> t = this.yieldHistory.get(m);
		return t.row(py);
	}

	
	
	
}
