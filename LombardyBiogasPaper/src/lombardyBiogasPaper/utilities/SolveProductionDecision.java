package lombardyBiogasPaper.utilities;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.DecimalFormat;
import java.util.Map;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.farms.Farm;
import lombardyBiogasPaper.agents.municipalities.Municipality;
import lombardyBiogasPaper.crops.ArableCrop;

import org.apache.log4j.Level;

import com.gams.api.GAMSDatabase;
import com.gams.api.GAMSGlobals;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSOptions;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.io.Files;

/**
 * Collects data from agents, creates matrix and calls gams software to solve the LP
 * //TODO Create a new tmp working directory
 * 
 * @author Dimitris Kremmydas
 *
 */
public class SolveProductionDecision {

	private GAMSWorkspaceInfo ginfo = new GAMSWorkspaceInfo("C:\\Users\\jkr\\Dropbox\\CurrentProjects\\Phd Proposal\\03. Work on progress\\Lombardy Biogas ABM\\model\\arableFarm", 
			"C:\\GAMS\\win64\\24.0",
			false);

	private GAMSWorkspace ws = new GAMSWorkspace(ginfo);
	
	private String modelFile="arableFarmModel.Java.gms";
	private String farmDataFilename = "farmData.inc";
	private String farmData = "";
	
	/**
	 * <Farm,ArableCrop>=Float
	 */
	private Table<String, String, Float> solutionResults = HashBasedTable.create();
	
	
	private GAMSDatabase db, dbResults; private GAMSJob job;

	private File workingDir;

	private Boolean usedWorkingDir;

	/**
	 *  //TODO Documentation
	 */
	public SolveProductionDecision() {
		
	}
	

	public GAMSDatabase retrieveGamsDBResults() {
		return this.dbResults;
	}

	public Table<String, String, Float> retrieveResults() {
		return this.solutionResults;
	}
	
	public void solve() {
		this.db = ws.addDatabase("farmData");
		SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Building Parameters from Farms");
		this.createFarmData();
		this.totalLandParameter();
		this.writeFarmData();
		
		this.job = ws.addJobFromFile(modelFile);
        GAMSOptions opt = ws.addOptions();
        opt.defines("incname", farmDataFilename);
        SimulationContext.logMessage(this.getClass(), Level.DEBUG, "finished... Running Job");
        this.job.run(opt, db);
        this.dbResults = this.job.OutDB();
        this.collectResults();
        this.updateFarmLandUse();
        SimulationContext.logMessage(this.getClass(), Level.DEBUG, "finished...");
	}
	
	private void updateFarmLandUse() {
		Iterable<ArableCrop> cs =  SimulationContext.getInstance().getCrops().getAll();
		for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
			 for(Farm f: m.getAllFarms()) {
				 Map<String,Float> mp = this.solutionResults.row(f.getName());
				 for(ArableCrop c: cs) {
					 f.getCropPlan().put(c, (float)mp.get(c.getName()));
				 }
			 }
		}
	}
	
	/**
	 * Tranform results from GASM.db format to the guava Table
	 */
	private void collectResults() {
		GAMSVariable x = this.dbResults.getVariable("mcult");
		this.solutionResults.clear();
		for (GAMSVariableRecord rec : x) {
		  	this.solutionResults.put(rec.getKeys()[0] , rec.getKeys()[1], (float)rec.getLevel());
		}
	}
	
	private void writeFarmData() {
		try {
		      BufferedWriter file = new BufferedWriter(new FileWriter(
		                                ws.workingDirectory() + GAMSGlobals.FILE_SEPARATOR + farmDataFilename
		                            ));
		      file.write(this.farmData);
		      file.close();
		  } catch(IOException e) {
		      e.printStackTrace();
		      System.exit(-1);
		  }
	}
	
	private void createFarmData() {
		DecimalFormat format = new DecimalFormat("0.00");
		Iterable<ArableCrop> cs =  SimulationContext.getInstance().getCrops().getAll();
		String eprices ="\ntable eprices(f,c)\n", yields = "\ntable yield(f,c)\n", varcosts = "\ntable varcost(f,c)\n";

		
		eprices += String.format("%-13s"," ");yields += String.format("%-13s"," ");varcosts += String.format("%-13s"," ");
		for(ArableCrop c: cs) {
			eprices += String.format("%-12s",c.getName()) ;
			yields += String.format("%-12s",c.getName()) ;
			varcosts += String.format("%-12s",c.getName()) ;
		};
		for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
			 for(Farm f: m.getAgentLayer(Farm.class)) {
				 eprices += "\n" + String.format("%-13s",f.getName());
				 yields += "\n" + String.format("%-13s",f.getName());
				 varcosts += "\n" + String.format("%-13s",f.getName());
				 for(ArableCrop c: cs) {
					 double p1 = (f.getPriceExpectationSnapshot().get(c)==null)?0:f.getPriceExpectationSnapshot().get(c);
					 double y2 = (f.getYield().get(c)==null)?0:f.getYield().get(c);
					 double v3 = (f.getVarCost().get(c)==null)?0:f.getVarCost().get(c);
					 eprices += String.format("%-12s",format.format(p1));
					 yields += String.format("%-12s",format.format(y2));
					 varcosts += String.format("%-12s",format.format(v3));
				 }				
			 }
		}
		this.farmData += eprices + ";\n" + yields + ";\n" + varcosts + ";\n";
		
	}

	private  void totalLandParameter() {
		this.farmData += "\nparameter totalLand(f) /";
		 //GAMSParameter r = this.db.addParameter("totalLand", 1, "the total available land of farm (ha)");
		String sep="";
		 for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
			 for(Farm f: m.getAgentLayer(Farm.class)) {
				 //r.addRecord(f.getName()).setValue(Double.valueOf(f.getTotalLand()));
				 this.farmData += sep + f.getName() + " " + f.getTotalLand(); sep=",";
			 }	
		 }
		 this.farmData += "/;";
	}
	
	
	
	private void getNewWorkingDir() {
		try {
			if(! (this.workingDir==null)) {
				this.deleteExistingWorkingDir();
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.workingDir = Files.createTempDir();
		usedWorkingDir=Boolean.FALSE;
	}
	
	private void deleteExistingWorkingDir() throws IOException {
		if(! this.workingDir.exists()) return;
		java.nio.file.Files.walkFileTree(this.workingDir.toPath(), new SimpleFileVisitor<Path>() {
			   @Override
			   public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				   java.nio.file.Files.delete(file);
			       return FileVisitResult.CONTINUE;
			   }

			   @Override
			   public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				   java.nio.file.Files.delete(dir);
			       return FileVisitResult.CONTINUE;
			   }
			});
	}
	
	
	
} //end class
