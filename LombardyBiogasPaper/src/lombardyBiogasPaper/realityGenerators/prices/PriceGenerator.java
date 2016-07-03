package lombardyBiogasPaper.realityGenerators.prices;

import java.util.Map;

import lombardyBiogasPaper.crops.ArableCrop;

public interface PriceGenerator {
	
	/**
	 * The generated prices for each {@link ArableCrop}
	 * @return Map<ArableCrop,Long>
	 */
	public Map<ArableCrop,Long> getPrices();
}
