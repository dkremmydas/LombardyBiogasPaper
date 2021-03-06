package lombardyBiogasPaper.utilities;

import java.util.HashMap;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

public class Utility {
	
	/**
	 * 
	 * @param sh
	 * @return
	 */
	public static Table<String,String,String>  convertExcelToTable(Sheet sh) {
		DataFormatter objDefaultFormat = new DataFormatter();
		
		Table<String,String,String> t =  HashBasedTable.create();

		HashMap<Integer,String> colHeadings = new HashMap<>();
		Iterator<Row> rowItr = sh.iterator(); 
		
		//first row: crop headings: we load the various crops
		Row row = rowItr.next(); //first row
		Iterator<Cell> colItr = row.cellIterator();
		colItr.next();
		while(colItr.hasNext()) {
			Cell cell = colItr.next();
			String cr = cell.getStringCellValue();	
			colHeadings.put(cell.getColumnIndex(), cr);
		}
		
		//now we iterate through rows to complete the farms' data
		while(rowItr.hasNext()) {
			row = rowItr.next();
			colItr = row.cellIterator();
			Cell cell = colItr.next(); 
			String rowHeading;
			if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC) {
				rowHeading = objDefaultFormat.formatCellValue(cell);
			}
			else {
				rowHeading = cell.getStringCellValue();
			}	
			while(colItr.hasNext()) {
				cell = colItr.next();
				String value;
				if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC) {
					 value = objDefaultFormat.formatCellValue(cell);
				}
				else {
					 value = cell.getStringCellValue();
				}				
					
				t.put(rowHeading, colHeadings.get(cell.getColumnIndex()), value);
			}
		}
		
		return t;
	}
	
	public static <A,B,C> String convertTabletoCsv(Table<A,B,C> t) {
		String r="";
		
		//print headers
		r += "\n";
		for(B col: t.columnKeySet()) {
			r += "\t" + col;
		}

		
		//print data
		for(A row: t.rowKeySet()) {
			r += "\n" + row;
			for(B col: t.columnKeySet()) {
				r += "\t" + t.get(row, col);
			}
		}
		

		return r;
	}
	

}
