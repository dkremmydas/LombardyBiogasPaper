package LombardyBiogasPaper.tests;

import lombardyBiogasPaper.SimulationContext;

import org.junit.Before;
import org.junit.Test;

import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.DefaultScheduleRunner;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.parameter.DefaultParameters;


public class SimulationContextTest {
	
	public SimulationContext sc;

	@Before
	public void setUp() throws Exception {
		Schedule schedule = new Schedule ();
		
		DefaultParameters p = new DefaultParameters();
		p.addParameter("initializationFile", "initializationFile", String.class,"C:\\Users\\jkr\\Dropbox\\CurrentProjects\\Phd Proposal\\03. Work on progress\\Lombardy Biogas ABM\\model\\data\\initializationDataTest.xlsx" , false);
		
		RunEnvironment . init ( schedule , new DefaultScheduleRunner(), p , true );
		sc = new SimulationContext();
		sc = (SimulationContext) sc.build(new DefaultContext<Object>());
	}

	@Test
	public void simulationContextInit() {
		System.out.println(sc);
	}
	
	/**
	 * Does the {@link SimulationContextTest#yearTickCount()} works well ?
	 */
	@Test
	public void yearTickCount() {
		System.out.println("Year count: " + sc.getCurrentYear());
		System.out.println("advance"); RunEnvironment.getInstance().getCurrentSchedule().execute();
		System.out.println("Year count: " + sc.getCurrentYear());
		System.out.println("advance"); RunEnvironment.getInstance().getCurrentSchedule().execute();
		System.out.println("Year count: " + sc.getCurrentYear());
		System.out.println("advance"); RunEnvironment.getInstance().getCurrentSchedule().execute();
		System.out.println("Year count: " + sc.getCurrentYear());
		System.out.println("advance"); RunEnvironment.getInstance().getCurrentSchedule().execute();
	}
	
	
}
