package lombardyBiogasPaper.realityGenerators.prices;

import java.util.HashMap;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.municipalities.Municipality;
import lombardyBiogasPaper.crops.ArableCrop;
import repast.simphony.random.RandomHelper;

/**
 * Generates prices for a set of crops. 
 * A problem is that prices between crops are correlated
 * 
 * @author Dimitris Kremmydas
 *
 */
public class DefaultGlobalPriceGenerator implements PriceGenerator {
	
	private double sigma ; //it is initialized to 0.3f in the constructor
	
	public DefaultGlobalPriceGenerator() {
		this(0.05d);
	}
	
	public DefaultGlobalPriceGenerator(double sigma) {
		super();
		this.sigma = sigma;
	}
	

	@Override
	public HashMap<ArableCrop,Long> getPrices() {
		int currentYear = SimulationContext.getInstance().getCurrentYear();
		Municipality m = SimulationContext.getInstance().getMunicipalities().iterator().next(); //take a random municipality
		HashMap<ArableCrop,Long> r = new HashMap<>();
		Iterable<ArableCrop> cs = SimulationContext.getInstance().getCrops().getAll();
		for(ArableCrop c: cs) {
			//Double factor = this.normalDistr.nextDouble();
			Double factor = RandomHelper.getNormal().nextDouble(0,sigma);
			Long oldVal = SimulationContext.getInstance().getRealityGenerator().getPriceHistory().get(m).get(currentYear-1,c);
			Long newVal = (long) (oldVal *( 1 + factor));
			if(newVal.compareTo(1l)<0) {newVal = 2l;} //TODO It should work with compareTo(0l)
			r.put(c, newVal);
		}
		return r;
	}
	
}
