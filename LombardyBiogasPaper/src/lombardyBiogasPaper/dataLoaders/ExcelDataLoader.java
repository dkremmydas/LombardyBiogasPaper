package lombardyBiogasPaper.dataLoaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.farms.Farm;
import lombardyBiogasPaper.agents.farms.PriceExpectations;
import lombardyBiogasPaper.agents.municipalities.Municipality;
import lombardyBiogasPaper.crops.ArableCrop;
import lombardyBiogasPaper.crops.AvailableArableCrops;
import lombardyBiogasPaper.productionDecisionCollectors.ProductionDecisionCollector;
import lombardyBiogasPaper.realityGenerators.RealityGenerator;
import lombardyBiogasPaper.utilities.Utility;

import org.apache.log4j.Level;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Table;

public class ExcelDataLoader implements DataLoader {
	
	private Workbook excelWB; 
	
	private HashMap<ArableCrop,Long> initPrices = new HashMap<>();
	private HashMap<ArableCrop,Float> initYields = new HashMap<>();
	
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

	@Override
	public ArrayList<Municipality> getMunicipalities() {
		SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Loading Municipalities");
		ArrayList<Municipality> r = new ArrayList<>();
		Sheet sh = this.excelWB.getSheet("municipalities");
		Iterator<Row> rowItr = sh.iterator(); 
		rowItr.next(); //skip first row
		while(rowItr.hasNext()) {
			Row row = rowItr.next();
			String name = row.getCell(0).getStringCellValue();
			int id = (int)row.getCell(1).getNumericCellValue();		
			r.add( new Municipality(id, name) );
		}
		return r;
	}

	@Override
	public AvailableArableCrops getAvailableCrops() {
		SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Loading Crops");
		AvailableArableCrops r = new AvailableArableCrops();
		Sheet sh = this.excelWB.getSheet("crops");
		SimulationContext.logMessage(this.getClass(), Level.DEBUG, "sheets crops:" + sh);
		Iterator<Row> rowItr = sh.iterator(); 
		rowItr.next(); //skip first row
		while(rowItr.hasNext()) {
			Row row = rowItr.next();
			int id = (int)row.getCell(0).getNumericCellValue();	
			String name = row.getCell(1).getStringCellValue();
			String originalName = row.getCell(2).getStringCellValue();	
			Long price = (long)(row.getCell(3).getNumericCellValue());
			Float yield = (float)(row.getCell(4).getNumericCellValue());
			ArableCrop c = new ArableCrop(id, name, originalName);
			SimulationContext.logMessage(this.getClass(), Level.DEBUG, "created crop:" + c);
			r.add(c);
			this.initPrices.put(c,price);
			this.initYields.put(c,yield);
		}
		return r;
	}

	@Override
	public ArrayListMultimap<Integer, Farm> getFarms() {
		SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Loading Farms");
		ArrayListMultimap<Integer,Farm> r = ArrayListMultimap.create();
		
		//load farm names/ids
		Sheet sh = this.excelWB.getSheet("farms");
		Iterator<Row> rowItr = sh.iterator(); 
		rowItr.next(); //skip first row
		while(rowItr.hasNext()) {
			Row row = rowItr.next();
			int farm_id = (int)row.getCell(0).getNumericCellValue();
			int mun_id = (int)row.getCell(1).getNumericCellValue();	
			String name = row.getCell(2).getStringCellValue();	
			//String ismixed = row.getCell(3).getStringCellValue();
			Long cash = (long)row.getCell(4).getNumericCellValue();
			Farm f = new Farm(farm_id,name);
			f.getAccount().addCash(cash);
			f.setPriceExp(new PriceExpectations(this.initPrices));
			r.put(mun_id, f);
		}
		
		//load farm data
		this.loadFarmData(r);
		
		SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Sample Farm:\n"+r.values().iterator().next().toString());
		
		
		//return
		return r;
	}
	
	private void loadFarmData(ArrayListMultimap<Integer,Farm> fs) {
		
		//load surface
		Sheet sh_surf = this.excelWB.getSheet("initialSurface");
		Sheet sh_varcost = this.excelWB.getSheet("varcost");
		Sheet sh_yield = this.excelWB.getSheet("yield");
		
		Table<String,String,String> t_surf = Utility.convertExcelToTable(sh_surf);
		Table<String,String,String> t_marg = Utility.convertExcelToTable(sh_varcost);
		Table<String,String,String> t_yield = Utility.convertExcelToTable(sh_yield);
		//SimulationContext.logMessage(this.getClass(), Level.DEBUG, t_surf.toString());
		HashMap<Integer,Float> tl = this.loadTotalLand(this.excelWB.getSheet("totalLand"));
		
		//load 
		for(Farm f: fs.values()) {
			Map<String,String> row_surf =  t_surf.row(String.valueOf(f.getId()));
			Map<String,String> row_varcost =  t_marg.row(String.valueOf(f.getId()));
			Map<String,String> row_yield =  t_yield.row(String.valueOf(f.getId()));
			//SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Row: " + row.toString());
			if(! row_surf.isEmpty()) {
				for(ArableCrop ac: SimulationContext.getInstance().getCrops().getAll()) {
					Float v_surf,v_varcost,v_yield;
					v_surf=cleanFloat(row_surf.get(ac.getName()));
					v_varcost=cleanFloat(row_varcost.get(ac.getName()));
					v_yield=cleanFloat(row_yield.get(ac.getName()));
					
					f.getCropPlan().put(ac, v_surf);
					f.getVarCost().put(ac, v_varcost);
					f.getYield().put(ac, v_yield);
					f.setTotalLand(tl.get(f.getId()));
				}
			}
			
			
		}
		
	}
	
	private HashMap<Integer,Float> loadTotalLand(Sheet sh) {
		HashMap<Integer,Float> r = new HashMap<>();
		Iterator<Row> rowItr = sh.iterator(); 
		rowItr.next(); //skip first row
		while(rowItr.hasNext()) {
			Row row = rowItr.next();
			int farm_id = (int)row.getCell(0).getNumericCellValue();
			float land = (float)row.getCell(1).getNumericCellValue();	
			r.put(farm_id,land);
		}
		return r;
	}
	

	private Float cleanFloat(String s) {
		Float f;
		try {
			f = Float.parseFloat(s);
		}
		catch(NumberFormatException e) {
			SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Could not convert'" + s + "' to float");
			f = 0f;
		}
		catch(Exception e) {
			SimulationContext.logMessage(this.getClass(), Level.DEBUG, "An Exception was raised for '" + s + "': " + e.toString());
			f = 0f;
		}
		
		return f;
	}

	@Override
	public void setupSimulationContext(SimulationContext sc) {
		//set Reality generator
		sc.setRealityGenerator( new RealityGenerator(this.initPrices,this.initYields) );
		
		//set production solver
		sc.setSolveProductionDecision(new ProductionDecisionCollector());
		
	}

	
} //end  class


