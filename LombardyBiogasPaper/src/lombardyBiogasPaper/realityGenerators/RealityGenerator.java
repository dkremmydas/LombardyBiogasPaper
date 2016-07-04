package lombardyBiogasPaper.realityGenerators;

import java.util.HashMap;
import java.util.Map;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.farms.Farm;
import lombardyBiogasPaper.agents.municipalities.Municipality;
import lombardyBiogasPaper.crops.ArableCrop;
import lombardyBiogasPaper.crops.AvailableArableCrops;
import repast.simphony.random.RandomHelper;

public class RealityGenerator {

	private HashMap<ArableCrop, Long> lastPrices = new HashMap<>();
	private HashMap<ArableCrop, Float> lastYields = new HashMap<>();
	

	public RealityGenerator() {
		//this.realizeProduction();
	}

	/**
	 * Get prices and yields and update Farm
	 */
	public void realizePrices() {
	}
	
	public void realizeYields() {
		
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
					 f.getAccount().addCash((long)(f.getCropPlan().get(c)*this.lastPrices.get(c)));
					 f.getAccount().removeCash((long)(f.getCropPlan().get(c)*f.getVarCost().get(c)));
				};
			 }
		}
	}
	

	
	
	
}
