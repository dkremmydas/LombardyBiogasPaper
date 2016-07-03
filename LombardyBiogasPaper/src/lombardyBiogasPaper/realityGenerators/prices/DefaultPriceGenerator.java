package lombardyBiogasPaper.realityGenerators.prices;

import java.util.HashMap;
import java.util.Map;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.crops.ArableCrop;
import repast.simphony.random.RandomHelper;
import cern.jet.random.Normal;

public class DefaultPriceGenerator implements PriceGenerator {

	private Map<ArableCrop,Long> previousPrices = new HashMap<>();
	
	private float sigma ; //it is initialized to 0.3f in the constructor
	
	private Normal normalDistr;
	
	public DefaultPriceGenerator(Map<ArableCrop, Long> previousPrices) {
		this(previousPrices,30);
	}
	
	public DefaultPriceGenerator(Map<ArableCrop, Long> previousPrices,
			float sigma) {
		super();
		this.previousPrices = previousPrices;
		this.normalDistr = new Normal(100, 50,RandomHelper.getGenerator());
	}
	

	@Override
	public HashMap<ArableCrop,Long> getPrices() {
		HashMap<ArableCrop,Long> r = new HashMap<>();
		Iterable<ArableCrop> cs = SimulationContext.getInstance().getCrops().getAll();
		for(ArableCrop c: cs) {
			Double factor = this.normalDistr.nextDouble();
			Long newVal = (long) (this.previousPrices.get(c) *( 1 + factor));
			r.put(c, newVal);
		}
		return r;
	}
	
}
