package lombardyBiogasPaper.dataLoaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import lombardyBiogasPaper.Farm;
import lombardyBiogasPaper.Municipality;
import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.utilities.Utility;

import org.apache.log4j.Level;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Table;

import crops.ArableCrop;

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
		
		//return
		return r;
	}
	
	private void loadFarmData(ArrayListMultimap<Integer,Farm> fs) {
		
		//load surface
		Sheet sh = this.excelWB.getSheet("initialSurface");
		
		Table<String,String,String> t = Utility.convertExcelToTable(sh);
		//SimulationContext.logMessage(this.getClass(), Level.DEBUG, t.toString());
		
		for(Farm f: fs.values()) {
			Map<String,String> row =  t.row(String.valueOf(f.getId()));
			for(ArableCrop ac: SimulationContext.getInstance().getCrops().getAll()) {
				Float v;
				v = Float.parseFloat(row.get(ac.getName()));
				f.getCurSurface().put(ac, v);
			}
		}
		
	}
	


	
} //end inner class


