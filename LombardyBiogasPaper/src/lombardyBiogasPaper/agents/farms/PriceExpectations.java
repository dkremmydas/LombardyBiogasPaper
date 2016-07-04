package lombardyBiogasPaper.agents.farms;

import java.util.HashMap;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.crops.ArableCrop;
import lombardyBiogasPaper.crops.AvailableArableCrops;

public class PriceExpectations {

	private HashMap<ArableCrop, Long> expectations = new HashMap<>();
	
	
	
	public PriceExpectations(HashMap<ArableCrop, Long> initExpectations) {
		this.expectations=initExpectations;
	}



	public HashMap<ArableCrop, Long> getExpectations() {
		return this.expectations;
	}
	
	
}
