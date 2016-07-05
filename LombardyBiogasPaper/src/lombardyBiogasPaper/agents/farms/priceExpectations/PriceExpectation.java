package lombardyBiogasPaper.agents.farms.priceExpectations;

import java.util.HashMap;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.crops.ArableCrop;

public interface PriceExpectation {

	/**
	 * Return the calculated expectations
	 * @return HashMap<ArableCrop, Long>
	 */
	public HashMap<ArableCrop, Long> getExpectations();
	
	/**
	 * This is called at the end of the {@link SimulationContext} initialization,
	 * so that any additional data can be loaded
	 */
	public void initExpectations();
	
}
