package lombardyBiogasPaper.agents.farms.priceExpectations;

import java.util.HashMap;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.crops.ArableCrop;
import lombardyBiogasPaper.crops.AvailableArableCrops;

public class FixedPriceExpectation {

	private HashMap<ArableCrop, Long> expectations = new HashMap<>();
	
	
	
	public FixedPriceExpectation(HashMap<ArableCrop, Long> initExpectations) {
		this.expectations=initExpectations;
	}



	public HashMap<ArableCrop, Long> getExpectations() {
		return this.expectations;
	}
	
	
}
