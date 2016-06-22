package lombardyBiogasPaper;

import org.apache.log4j.Level;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.dataLoader.ContextBuilder;
import simphony.util.messages.MessageCenter;


public class SimulationContext extends DefaultContext<Object> implements ContextBuilder<Object> {
	
	private static SimulationContext instance=null;

	
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
	
	/**
	 * It builds the Contexts of Agroscape. <br />
	 * The steps that this method does, are: <br />
	 * 1. //TODO complete documentation
	 * 
	 */

	@Override
	public Context<Object> build(Context<Object> context)  {
		SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Start building SimulationContet");
			
		//1. set dataLoaders
		
		
		//2. Initialize municipalities
		
		
		//3. create farmers
		
		
		//4. Return the created SimulationContext
		return context;
	}
	

	
}
