package LombardyBiogasPaper.tests;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.productionDecisionCollectors.ProductionDecisionCollector;
import lombardyBiogasPaper.realityGenerators.RealityGenerator;

import org.junit.Before;
import org.junit.Test;

import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.DefaultScheduleRunner;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.parameter.DefaultParameters;

public class ArableModelTest {
	
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
	
	@Test
	public void gamsExecution() {
		final long startTime = System.currentTimeMillis(); System.out.println(startTime);
		ProductionDecisionCollector solve = new ProductionDecisionCollector();				
		System.out.println(solve.toString());
		final long elapsedTimeMillis = System.currentTimeMillis() - startTime;
		System.out.println(elapsedTimeMillis/1000);
	}
	
	@Test
	public void ProductionCycle() {
		final long startTime = System.currentTimeMillis(); System.out.println(startTime);
		
		ProductionDecisionCollector solve = new ProductionDecisionCollector();				
		//solve.updateFarmLandUse();
		
		RealityGenerator pr = sc.getRealityGenerator();
		//pr.realizeProduction();
		
		
		
		
		final long elapsedTimeMillis = System.currentTimeMillis() - startTime;
		System.out.println(elapsedTimeMillis/1000);
	}

}
