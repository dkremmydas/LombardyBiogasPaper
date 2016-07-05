package lombardyBiogasPaper.agents.farms.priceExpectations;

import java.util.HashMap;

import lombardyBiogasPaper.crops.ArableCrop;

public interface PriceExpectation {

	/**
	 * Return the calculated expectations
	 * @return HashMap<ArableCrop, Long>
	 */
	public HashMap<ArableCrop, Long> getExpectations();
	
}
