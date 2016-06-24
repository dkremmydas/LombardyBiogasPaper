package LombardyBiogasPaper.tests;

import static org.junit.Assert.fail;
import lombardyBiogasPaper.SimulationContext;

import org.junit.Before;
import org.junit.Test;

import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;

public class ArableModelTest {
	
	public SimulationContext sc;

	@Before
	public void setUp() throws Exception {
		Schedule schedule = new Schedule ();
		RunEnvironment . init ( schedule , null , null , true );
		sc = new SimulationContext();
		sc = (SimulationContext) sc.build(new DefaultContext<Object>());
	}

	@Test
	public void simulationContextInit() {
		System.out.println(sc);
	}
	
	@Test
	public void gamsExecution() {
		fail("Not yet implemented");
	}

}
