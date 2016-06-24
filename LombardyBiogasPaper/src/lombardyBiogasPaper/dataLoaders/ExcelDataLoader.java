package lombardyBiogasPaper.dataLoaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.Farm;
import lombardyBiogasPaper.agents.Municipality;
import lombardyBiogasPaper.crops.ArableCrop;
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
	public ArrayList<ArableCrop> getAvailableCrops() {
		SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Loading Crops");
		ArrayList<ArableCrop> r = new ArrayList<>();
		Sheet sh = this.excelWB.getSheet("crops");
		Iterator<Row> rowItr = sh.iterator(); 
		rowItr.next(); //skip first row
		while(rowItr.hasNext()) {
			Row row = rowItr.next();
			int id = (int)row.getCell(0).getNumericCellValue();	
			String name = row.getCell(1).getStringCellValue();
			String originalName = row.getCell(2).getStringCellValue();	
			ArableCrop c = new ArableCrop(id, name, originalName);
			r.add(c);
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
			Farm f = new Farm(farm_id,name);
			r.put(mun_id, f);
		}
		
		//load farm data
		this.loadFarmData(r);
		
		SimulationContext.logMessage(this.getClass(), Level.INFO, "Sample Farm:\n"+r.values().iterator().next().toString());
		
		
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
					
					f.getCurSurface().put(ac, v_surf);
					f.getVarCost().put(ac, v_varcost);
					f.getYield().put(ac, v_yield);
				}
			}
			
			
		}
		
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

	
} //end  class


