package lombardyBiogasPaper.agents.farms;

import java.util.HashMap;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.crops.ArableCrop;
import lombardyBiogasPaper.crops.AvailableArableCrops;

public class PriceExpectations {

	private HashMap<ArableCrop, Long> expectations = new HashMap<>();
	
	
	
	public PriceExpectations() {
		this.initExpecations();
	}



	public HashMap<ArableCrop, Long> getExpectations() {
		return this.expectations;
	}
	
	
	private void initExpecations() {
		AvailableArableCrops cs = SimulationContext.getInstance().getCrops();
		//expectations = cs.getPrices();
		/**
		 * 	ofc	150
			ogl	120
			sbt	30
			sca	500
			wme	220
			alf	180
			heg	120
			drw	250
			sfw	170
			suf	200
			bar	230
			let	720
			smze	30
			mze	150
			mel	480
			pot	300
			tom	90

		 */
		
		
	}
	
}
