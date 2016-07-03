package lombardyBiogasPaper;



import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import lombardyBiogasPaper.agents.farms.Farm;
import lombardyBiogasPaper.agents.municipalities.Municipality;
import lombardyBiogasPaper.crops.AvailableArableCrops;
import lombardyBiogasPaper.dataLoaders.ExcelDataLoader;

import org.apache.log4j.Level;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import repast.simphony.engine.schedule.ScheduledMethod;
import simphony.util.messages.MessageCenter;

import com.google.common.collect.ArrayListMultimap;


public class SimulationContext extends DefaultContext<Object> implements ContextBuilder<Object> {
	
	private static SimulationContext instance=null;
	
	private AvailableArableCrops crops = new AvailableArableCrops();
	
	private int yearCount = 0;

	
	/**
	 * Only one instance of MainContext exists. 
	 * <br />Singleton Design Pattern (?)
	 * @return
	 */
	public static SimulationContext getInstance() {
		if (SimulationContext.instance==null) {SimulationContext.instance=new SimulationContext();}
		return SimulationContext.instance;
	}
	
	/**
	 * Private Constructor, so the existence of a unique instance of MainContext is enforced. 
	 */
	public SimulationContext() {
		super("SimulationContext","SimulationContext");
		SimulationContext.instance = this;
	}



	public static void logMessage(Class<?> clazz, Level level,Object message) {
		MessageCenter mc = MessageCenter.getMessageCenter(clazz);
		mc.fireMessageEvent(level, message, null);
	}
	
	
	public AvailableArableCrops getCrops() {
		return crops;
	}

	public void setCrops(AvailableArableCrops crops) {
		this.crops = crops;
	}
	
	public int getCurrentYear() {
		return this.yearCount;
	}

	/**
	 * It builds the Contexts of Agroscape. <br />
	 * The steps that this method does, are: <br />
	 * 1. //TODO complete documentation
	 * 
	 */
	@Override
	public Context<Object> build(Context<Object> context)  {
		SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Start building SimulationContext");
			
		//1. Load Data
		String dataFile = RunEnvironment.getInstance().getParameters().getString("initializationFile");
		try {
			ExcelDataLoader edl = new ExcelDataLoader(dataFile);
			SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Loading from Excel file"+dataFile);
			
			//load crops			
			SimulationContext.getInstance().setCrops(edl.getAvailableCrops());
			SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Crops Loaded. \nSimulationContext contains:\n"+this.getCrops().toString());
			
			//load municipalities
			ArrayList<Municipality> ms = edl.getMunicipalities();
			for(Municipality m: ms) {SimulationContext.getInstance().addSubContext(m);}
			SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Municipalities Loaded. \nSimulationContext contains:\n"+this.toString());
			
			//load farms
			ArrayListMultimap<Integer,Farm> fs = edl.getFarms();
			for (int mun : fs.keySet()) { 
				Municipality m = (Municipality) SimulationContext.getInstance().findContext(mun);
				for(Farm f: fs.get(mun)) {
					m.add(f);
				}
			}
			SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Farms Loaded. \nSimulationContext contains:\n"+this.toString());
			
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//2. Initialize municipalities
		
		
		//3. create farmers
		
		
		
		//4. Return the created SimulationContext
		return this;
	}
	
	
	public Iterable<Municipality> getMunicipalities() {
		ArrayList<Municipality> ms = new ArrayList<Municipality>();
		Map<Object, Context<? extends Object>> objs = this.subContexts;
		
		for(Object o: objs.keySet()) {
			Municipality m = (Municipality)objs.get(o);
			ms.add(m);
		}
		
		return ms;
	}

	
	@Override
	public String toString() {
		String r = "SimulationContext [ Municipalities: ";
		for (Municipality m: this.getMunicipalities())  {
			r += "\n" + m.toString();
		}
		r +="] ]";
		
		return r;
	}
	
	
	@ScheduledMethod(start=0,interval=1)
	public void step() {
		//advance year count
		this.yearCount++;
		
		
	}

	
} //end outer class
