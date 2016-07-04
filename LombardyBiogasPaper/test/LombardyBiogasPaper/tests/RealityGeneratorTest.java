package LombardyBiogasPaper.tests;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.farms.Farm;
import lombardyBiogasPaper.agents.municipalities.Municipality;
import lombardyBiogasPaper.utilities.Utility;

import org.junit.Before;
import org.junit.Test;

import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.DefaultScheduleRunner;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.parameter.DefaultParameters;
import repast.simphony.random.RandomHelper;
import cern.jet.random.Normal;


public class RealityGeneratorTest {
	
	public SimulationContext sc;

	@Before
	public void setUp() throws Exception {
		Schedule schedule = new Schedule ();
		
		DefaultParameters p = new DefaultParameters();
		p.addParameter("initializationFile", "initializationFile", String.class,"C:\\Users\\jkr\\Dropbox\\CurrentProjects\\Phd Proposal\\03. Work on progress\\Lombardy Biogas ABM\\model\\data\\initializationDataTest.xlsx" , false);
		
		RunEnvironment . init ( schedule , new DefaultScheduleRunner(), p , true );
		sc = new SimulationContext();
		sc = (SimulationContext) sc.build(new DefaultContext<Object>());
		RunEnvironment.getInstance().getCurrentSchedule().schedule(sc);
		System.out.println("# of actions scheduled: " + RunEnvironment.getInstance().getCurrentSchedule().getActionCount());
	}

	@Test
	public void simulationContextInit() {
		System.out.println(sc);
	}
	
	/**
	 * Does the {@link RealityGeneratorTest#yearTickCount()} works well ?
	 */
	@Test
	public void initialPrices() {
		Municipality m = (Municipality) sc.getSubContexts().iterator().next();
		System.out.println(sc.getRealityGenerator().getPriceHistory());
		System.out.println(sc.getRealityGenerator().getMunicipalityLastPrices(m));
	}
	
	@Test
	public void testNormalDistrGeneration() {
		 Normal normalDistr= new Normal(10, 5, RandomHelper.getGenerator());
		 
		 System.out.println(normalDistr.nextDouble());
		 System.out.println(normalDistr.nextDouble());
		 System.out.println(normalDistr.nextDouble());
	}
	
	@Test
	public void priceCreation() {
		RandomHelper.createNormal(0, 0.3);
		Municipality m = (Municipality) sc.getSubContexts().iterator().next();
		
		System.out.println("Year count: " + sc.getCurrentYear());
		
		RunEnvironment.getInstance().getCurrentSchedule().execute();
		System.out.println("Year count: " + sc.getCurrentYear());
		sc.getRealityGenerator().realizeProduction();
		System.out.println(sc.getRealityGenerator().getMunicipalityLastPrices(m));
		
		for(int i=0;i<30;i++) {
			RunEnvironment.getInstance().getCurrentSchedule().execute();
			System.out.println("Year count: " + sc.getCurrentYear());
			sc.getRealityGenerator().realizeProduction();
			System.out.println(sc.getRealityGenerator().getMunicipalityLastPrices(m));
		}
		
		//System.out.println(m.getPriceHistory());	
		System.out.println(Utility.convertTabletoCsv(sc.getRealityGenerator().getMunicipalityPriceHistory(m)));
	}
	
	
	
	@Test
	public void longComparison() {
		Long t = -1l;
		System.out.println(t.compareTo(0l));
		if(t.compareTo(1l)<0) {t=2l;}
		System.out.println(t);
	}
	
	
}
