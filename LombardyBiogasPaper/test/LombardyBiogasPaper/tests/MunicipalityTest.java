package LombardyBiogasPaper.tests;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.municipalities.Municipality;

import org.junit.Before;
import org.junit.Test;

import cern.jet.random.Normal;
import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.DefaultScheduleRunner;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.parameter.DefaultParameters;
import repast.simphony.random.RandomHelper;


public class MunicipalityTest {
	
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
	}

	@Test
	public void simulationContextInit() {
		System.out.println(sc);
	}
	
	/**
	 * Does the {@link MunicipalityTest#yearTickCount()} works well ?
	 */
	@Test
	public void initialPrices() {
		Municipality m = (Municipality) sc.getSubContexts().iterator().next();
		System.out.println(m.getPriceHistory());
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
		Municipality m = (Municipality) sc.getSubContexts().iterator().next();
		
		System.out.println("Year count: " + sc.getCurrentYear());
		System.out.println(m.getPriceHistory());
		
		RunEnvironment.getInstance().getCurrentSchedule().execute();
		System.out.println("Year count: " + sc.getCurrentYear());
		m.insertToPriceHistory(sc.getCurrentYear(), m.getPriceRealizationRule().getPrices());
		System.out.println(m.getPriceHistory());
		
		
		RunEnvironment.getInstance().getCurrentSchedule().execute();
		m.insertToPriceHistory(sc.getCurrentYear(), m.getPriceRealizationRule().getPrices());
		System.out.println("Year count: " + sc.getCurrentYear());
		System.out.println(m.getPriceHistory());		
	}
	
	
}
