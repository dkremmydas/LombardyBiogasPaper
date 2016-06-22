package lombardyBiogasPaper;



import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.log4j.Level;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import repast.simphony.context.Context;
import repast.simphony.context.DefaultContext;
import repast.simphony.dataLoader.ContextBuilder;
import repast.simphony.engine.environment.RunEnvironment;
import simphony.util.messages.MessageCenter;

import com.google.common.collect.ArrayListMultimap;


public class SimulationContext extends DefaultContext<Municipality> implements ContextBuilder<Object> {
	
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
	public Context<Municipality> build(Context<Object> context)  {
		SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Start building SimulationContet");
			
		//1. Load Data
		String dataFile = RunEnvironment.getInstance().getParameters().getString("initializationFile");
		try {
			ExcelDataLoader edl = new ExcelDataLoader(dataFile);
			this.addAll(edl.getMunicipalities());
			
			
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//2. Initialize municipalities
		
		
		//3. create farmers
		
		//debug
		SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Start building SimulationContet");
		
		
		//4. Return the created SimulationContext
		return this;
	}
	
	
	
	
	public class ExcelDataLoader  {

		private Workbook excelWB; 
		
		/**
		 * Constructor
		 * @param excel_location
		 * @throws InvalidFormatException
		 * @throws IOException
		 */
		public ExcelDataLoader(String excelLocation) throws InvalidFormatException, IOException {
			super();
			Workbook wb = WorkbookFactory.create(new File(excelLocation));
			this.excelWB=wb;
		}
		
		public ArrayList<Municipality> getMunicipalities() {
			ArrayList<Municipality> r = new ArrayList<Municipality>();
			Sheet sh = this.excelWB.getSheet("municipalities");
			Iterator<Row> rowItr = sh.iterator(); 
			rowItr.next(); //skip first row
			while(rowItr.hasNext()) {
				Row row = rowItr.next();
				String name = row.getCell(0).getStringCellValue();
				int id = (int)row.getCell(1).getNumericCellValue();			
				r.add(new Municipality(id, name));
			}
			
			
			return r;
		}
		
		
		public void addFarms(SimulationContext sc) {
			ArrayListMultimap<Integer,Farm> r = ArrayListMultimap.create();
			
			//1. get farms per municipality
			Sheet sh = this.excelWB.getSheet("farms");
			Iterator<Row> rowItr = sh.iterator(); 
			rowItr.next(); //skip first row
			while(rowItr.hasNext()) {
				Row row = rowItr.next();
				int farm_id = (int)row.getCell(0).getNumericCellValue();
				int mun_id = (int)row.getCell(1).getNumericCellValue();			
				Farm f = new Farm(farm_id);
				r.put(mun_id, f);
			}
			
			//2. foreach municipality, add them to mun.context
			for (int mun : r.keySet()) { 
				Municipality m = (Municipality) sc.findContext(mun);
				m.addAll(r.get(mun));
				
				//debug
				SimulationContext.logMessage(this.getClass(), Level.DEBUG, "[added]" + m.toString());
			}
			
			
			
		}
		
	}

	
}
