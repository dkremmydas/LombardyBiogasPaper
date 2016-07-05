package lombardyBiogasPaper.agents.farms.priceExpectations;

import java.util.HashMap;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.municipalities.Municipality;
import lombardyBiogasPaper.crops.ArableCrop;

public class FixedPriceExpectation implements PriceExpectation {

	private HashMap<ArableCrop, Long> expectations = new HashMap<>();
	
	
	
	public FixedPriceExpectation() {}

	@Override
	public HashMap<ArableCrop, Long> getExpectations() {
		return this.expectations;
	}

	@Override
	public void initExpectations() {
		Municipality m = SimulationContext.getInstance().getMunicipalities().iterator().next();
		this.expectations.putAll(SimulationContext.getInstance().getRealityGenerator().getMunicipalityLastPrices(m));	
	}
	
	
}
