package LombardyBiogasPaper.tests;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.farms.Farm;
import lombardyBiogasPaper.agents.municipalities.Municipality;
import lombardyBiogasPaper.utilities.Utility;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import repast.simphony.context.DefaultContext;
import repast.simphony.engine.environment.DefaultScheduleRunner;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.Schedule;
import repast.simphony.parameter.DefaultParameters;
import repast.simphony.random.RandomHelper;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Iterables;
import com.google.common.collect.Table;


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
		RunEnvironment.getInstance().getCurrentSchedule().schedule(sc);
	}

	@Test
	public void simulationContextInit() {
		System.out.println(sc);
	}
	
	@Test
	public void availableCropsLoaded() {
		System.out.println(sc.getCrops());
	}
	
	@Test
	public void testUpdateFarmerSolveProduction() {
		RandomHelper.createNormal(0, 0.3);
		Municipality m = (Municipality) sc.getSubContexts().iterator().next();
		Farm f =  m.getRandomObject();
		
		System.out.println("Year count: " + sc.getCurrentYear());
		System.out.println(f);
		
		RunEnvironment.getInstance().getCurrentSchedule().execute();
		System.out.println("Year count: " + sc.getCurrentYear());
		sc.getSolveProductionDecision().solve();
		System.out.println(f);
		//System.out.println(f.getCropPlan());
		//System.out.println(f.getAccount().getCash());
		
	}
	
	@Test
	public void productionCycle() {
		RandomHelper.createNormal(0, 0.3);
		Municipality m = (Municipality) sc.getSubContexts().iterator().next();
		Farm f =  m.getRandomObject();
		
		System.out.println("Year count: " + sc.getCurrentYear());
		System.out.println(f);
		
		for(int i=0;i<5;i++) {
			RunEnvironment.getInstance().getCurrentSchedule().execute();
			System.out.println("Year count: " + sc.getCurrentYear());
			sc.getSolveProductionDecision().solve();
			sc.getRealityGenerator().realizeProduction();
			System.out.println(f);
		}
		
//		Iterable<ArableCrop> cs =  SimulationContext.getInstance().getCrops().getAll();
//		System.out.println( sc.getRealityGenerator().getMunicipalityLastPrices(m));
//		System.out.println( sc.getRealityGenerator().getMunicipalityLastYields(m));
//		for(ArableCrop c: cs) {
//			 Long pr = sc.getRealityGenerator().getMunicipalityLastPrices(m).get(c); 
//			 Float yi = sc.getRealityGenerator().getMunicipalityLastYields(m).get(c);
//			 Map<ArableCrop,Float> pl = f.getCropPlan(); 
//			 Float la = pl.get(c); 
//			 Long prof = pr*((long)(yi*la));
//			 System.out.println("Arable: " + c + " plan:" + f.getCropPlan().get(c) + " profit:" + prof + " vc:" +f.getCropPlan().get(c)*f.getVarCost().get(c) );
////			 if(la.compareTo(0f)>0) {
////				 f.getAccount().addCash((long)(la*pr*yi));
////				 f.getAccount().removeCash((long)(f.getCropPlan().get(c)*f.getVarCost().get(c)));
////			 }

		System.out.println(Utility.convertTabletoCsv(sc.getRealityGenerator().getMunicipalityPriceHistory(m)));
	}
	
	@Test
	public void simulationCycle() {
		System.out.println("# Scheduled Actions: " + RunEnvironment.getInstance().getCurrentSchedule().getActionCount());
		
		RandomHelper.createNormal(0, 0.3);
		Municipality m = (Municipality) sc.getSubContexts().iterator().next();
		Farm f =  m.getRandomObject();
		
		System.out.println("Year count: " + sc.getCurrentYear());
		System.out.println(f);
		
		for(int i=0;i<5;i++) {
			RunEnvironment.getInstance().getCurrentSchedule().execute();
			System.out.println("Year count: " + sc.getCurrentYear());
			System.out.println(f);
		}

		System.out.println(Utility.convertTabletoCsv(sc.getRealityGenerator().getMunicipalityPriceHistory(m)));
	}
	
	@Test
	public void getAllFarmersFromSimulationContext() {
		Iterable<Object> allFarms = SimulationContext.getInstance().getObjects(Farm.class);
		System.out.println(Iterables.size(allFarms));
		System.out.println(allFarms);
		
	}
	
	@Test
	public void simulationCycleTestCashAverage() {
		System.out.println("# Scheduled Actions: " + RunEnvironment.getInstance().getCurrentSchedule().getActionCount());
		RandomHelper.createNormal(0, 0.3);
		System.out.println("Year count: " + sc.getCurrentYear());
		Table<Municipality,Integer,Long> cashHistory = HashBasedTable.create();
		for(Municipality m: sc.getMunicipalities()) {
			Long avg = m.getAverageCash();
			cashHistory.put(m, sc.getCurrentYear(), avg);
			System.out.println("Mun: " + m.getId() + " Avg Cash:" + avg);
		}
				
		
		for(int i=0;i<2;i++) {
			RunEnvironment.getInstance().getCurrentSchedule().execute();
			System.out.println("Year count: " + sc.getCurrentYear());
			for(Municipality m: sc.getMunicipalities()) {
				Long avg = m.getAverageCash();
				cashHistory.put(m, sc.getCurrentYear(), avg);
				System.out.println("Mun: " + m.getId() + " Avg Cash:" + avg);
			}
		}
		Utility.convertTabletoCsv(cashHistory);
	}
	
	/**
	 * Does the {@link SimulationContextTest#yearTickCount()} works well ?
	 */
	@Test
	public void yearTickCount() {
		System.out.println("# Scheduled Actions: " + RunEnvironment.getInstance().getCurrentSchedule().getActionCount());
		
		System.out.println("Year count: " + sc.getCurrentYear() + " Scheduled tick:" + RunEnvironment.getInstance().getCurrentSchedule().getTickCount());
		System.out.println("advance"); RunEnvironment.getInstance().getCurrentSchedule().execute();
		
		System.out.println("Year count: " + sc.getCurrentYear() + " Scheduled tick:" + RunEnvironment.getInstance().getCurrentSchedule().getTickCount());
		System.out.println("advance"); RunEnvironment.getInstance().getCurrentSchedule().execute();
		
		System.out.println("Year count: " + sc.getCurrentYear() + " Scheduled tick:" + RunEnvironment.getInstance().getCurrentSchedule().getTickCount());
		System.out.println("advance"); RunEnvironment.getInstance().getCurrentSchedule().execute();
		
		System.out.println("Year count: " + sc.getCurrentYear() + " Scheduled tick:" + RunEnvironment.getInstance().getCurrentSchedule().getTickCount());
		System.out.println("advance"); RunEnvironment.getInstance().getCurrentSchedule().execute();
		
		System.out.println("Year count: " + sc.getCurrentYear() + " Scheduled tick:" + RunEnvironment.getInstance().getCurrentSchedule().getTickCount());
		System.out.println("advance"); RunEnvironment.getInstance().getCurrentSchedule().execute();
		
		Assert.assertEquals(sc.getCurrentYear(),4);
	}
	
	
}
