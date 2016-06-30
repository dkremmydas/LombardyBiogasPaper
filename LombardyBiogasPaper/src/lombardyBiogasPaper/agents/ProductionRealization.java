package lombardyBiogasPaper.agents;

import java.util.HashMap;
import java.util.Map;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.farms.Farm;
import lombardyBiogasPaper.crops.ArableCrop;
import lombardyBiogasPaper.crops.AvailableArableCrops;
import repast.simphony.random.RandomHelper;

public class ProductionRealization {

	private HashMap<ArableCrop, Long> lastPrices = new HashMap<>();
	private HashMap<ArableCrop, Float> lastYields = new HashMap<>();
	

	public ProductionRealization() {
		this.realizeProduction();
	}

	/**
	 * Get prices and yields and update Farm
	 */
	public void realizeProduction() {
		this._realizePrices();
		this._realizeYields();		
	}
	
	public Map<ArableCrop, Float> getYields() {
		return this.lastYields;
	}
	
	public Map<ArableCrop, Long> getPrices() {
		return this.lastPrices;
	}

	
	private void updateFarmAccounts() {
		Iterable<ArableCrop> cs =  SimulationContext.getInstance().getCrops().getAll();
		for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
			 for(Farm f: m.getAgentLayer(Farm.class)) {
				 for(ArableCrop c: cs) {
					 f.getAccount().addCash(f.getCurSurface().get(c)*this.lastPrices.get(c));
					 f.getAccount().removeCash(f.getCurSurface().get(c)*SimulationContext.getInstance().getCrops().g);
				};
			 }
		}
	}
	

	private void _realizeYields() {
		AvailableArableCrops avcs = SimulationContext.getInstance().getCrops();
		HashMap<ArableCrop, Float> baseYields = new HashMap<>();
		baseYields.put(avcs.getByName("ofc"),12f);
		baseYields.put(avcs.getByName("ogl"),3.4965034965035f);
		baseYields.put(avcs.getByName("sbt"),69.9588477366255f);
		baseYields.put(avcs.getByName("sca"),26.1194029850746f);
		baseYields.put(avcs.getByName("wme"),31.9526627218935f);
		baseYields.put(avcs.getByName("alf"),11.734693877551f);
		baseYields.put(avcs.getByName("heg"),11.864406779661f);
		baseYields.put(avcs.getByName("drw"),6.93430656934307f);
		baseYields.put(avcs.getByName("sfw"),7.04761904761905f);
		baseYields.put(avcs.getByName("suf"),2.77392510402219f);
		baseYields.put(avcs.getByName("let"),30f);
		baseYields.put(avcs.getByName("smze"),66.6666666666667f);
		baseYields.put(avcs.getByName("mze"),12.9531051964512f);
		baseYields.put(avcs.getByName("mel"),52.9411764705882f);
		baseYields.put(avcs.getByName("bar"),12f);
		baseYields.put(avcs.getByName("pot"),7.1f);
		baseYields.put(avcs.getByName("tom"),74.2857142857143f);
		baseYields.put(avcs.getByName("gras"),12f);
		baseYields.put(avcs.getByName("soj"),4.97362110311751f);
		baseYields.put(avcs.getByName("sfwII"),6.26102292768959f);
		baseYields.put(avcs.getByName("sojII"),4.53333333333333f);


		Iterable<ArableCrop> cs =  SimulationContext.getInstance().getCrops().getAll();
		for(ArableCrop c: cs) {
			this.lastYields.put(c, baseYields.get(c)* (1 + RandomHelper.nextIntFromTo(-50,50) / 100) );
		}
	}
	
	private void _realizePrices() {
		AvailableArableCrops avcs = SimulationContext.getInstance().getCrops();
		HashMap<ArableCrop, Long> basePrices = new HashMap<>();
		basePrices.put(avcs.getByName("ofc"), 150l);
		basePrices.put(avcs.getByName("ogl"),120l);
		basePrices.put(avcs.getByName("sbt"),30l);
		basePrices.put(avcs.getByName("sca"),500l);
		basePrices.put(avcs.getByName("wme"),220l);
		basePrices.put(avcs.getByName("alf"),180l);
		basePrices.put(avcs.getByName("heg"),120l);
		basePrices.put(avcs.getByName("drw"),250l);
		basePrices.put(avcs.getByName("sfw"),170l);
		basePrices.put(avcs.getByName("bar"),230l);
		basePrices.put(avcs.getByName("suf"),200l);
		basePrices.put(avcs.getByName("let"),720l);
		basePrices.put(avcs.getByName("smze"),30l);
		basePrices.put(avcs.getByName("mze"),150l);
		basePrices.put(avcs.getByName("mel"),480l);
		basePrices.put(avcs.getByName("pot"),300l);
		basePrices.put(avcs.getByName("tom"),90l);
		basePrices.put(avcs.getByName("gras"),100l);
		basePrices.put(avcs.getByName("soj"),300l);
		basePrices.put(avcs.getByName("sfwII"),170l);
		basePrices.put(avcs.getByName("sojII"),300l);

		Iterable<ArableCrop> cs =  SimulationContext.getInstance().getCrops().getAll();
		for(ArableCrop c: cs) {
			this.lastPrices.put(c, basePrices.get(c)* (1 + RandomHelper.nextIntFromTo(-50,50) / 100) );
		}

	}
	
	
}
