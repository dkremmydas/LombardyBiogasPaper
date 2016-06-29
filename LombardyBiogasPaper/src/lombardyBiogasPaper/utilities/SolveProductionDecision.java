package lombardyBiogasPaper.utilities;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.Farm;
import lombardyBiogasPaper.agents.Municipality;
import lombardyBiogasPaper.crops.ArableCrop;

import org.apache.log4j.Level;

import com.gams.api.GAMSDatabase;
import com.gams.api.GAMSGlobals;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;

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
	
	
	private GAMSDatabase db, dbResults; private GAMSJob job;

	public SolveProductionDecision() {
		this.db = ws.addDatabase("farmData");
		SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Building Parameters from Farms");
//		this.buildPriceExpecationsParameter();
//		this.buildYieldParameter();
		this.createFarmData();
		this.totalLandParameter();
//		this.buildVarCostParameter();
		this.writeFarmData();
		
//		this.job = ws.addJobFromFile(modelFile);
//        GAMSOptions opt = ws.addOptions();
//        opt.defines("incname", farmDataFilename);
//        SimulationContext.logMessage(this.getClass(), Level.DEBUG, "finished... Running Job");
//        this.job.run(opt, db);
//        this.dbResults = this.job.OutDB();
//        SimulationContext.logMessage(this.getClass(), Level.DEBUG, "finished...");
	}
	

	public GAMSDatabase retrieveResults() {
		return this.dbResults;
	}


	public void updateFarmLandUse() {
		  
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
	
//	private  void buildPriceExpecationsParameter() {
//		Iterable<ArableCrop> cs =  SimulationContext.getInstance().getCrops().getAll();
//		this.farmData += "\ntable eprices(f,c)\n";
//		
//		this.farmData += String.format("%-13s"," ");
//		for(ArableCrop c: cs) {
//			this.farmData += String.format("%-8s",c.getName()) ;
//		};
//		for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
//			 for(Farm f: m.getAgentLayer(Farm.class)) {
//				 this.farmData += "\n" + String.format("%-13s",f.getName());
//				 for(ArableCrop c: cs) {
//					 double v = (f.getPriceExpectationSnapshot().get(c)==null)?0:f.getPriceExpectationSnapshot().get(c);
//					 this.farmData += String.format("#####.##",v);
//				 }				
//			 }
//		}
//		this.farmData += "\n;";
//	}
//	
//	private  void buildYieldParameter() {
//		Iterable<ArableCrop> cs =  SimulationContext.getInstance().getCrops().getAll();
//		this.farmData += "\ntable yield(f,c)\n";
//		
//		this.farmData += String.format("%-13s"," ");
//		for(ArableCrop c: cs) {
//			this.farmData += String.format("%-8s",c.getName()) ;
//		};
//		
//		//GAMSParameter r = this.db.addParameter("yield", 2, "the yield of each crop for a farm (the old rendt) (tn/ha)");
//		for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
//			 for(Farm f: m.getAgentLayer(Farm.class)) {
//				 this.farmData += "\n" + String.format("%-13s",f.getName());
//				 for(ArableCrop c: cs) {
//					 double v = (f.getYield().get(c)==null)?0:f.getYield().get(c);
//					 this.farmData += String.format("#####.##",v);
//				 }		
//			 }
//		}
//		this.farmData += "\n;";
//	}
//	
//	private  void buildVarCostParameter() {
//		Iterable<ArableCrop> cs =  SimulationContext.getInstance().getCrops().getAll();
//		this.farmData += "\ntable varcost(f,c)\n";
//		
//		this.farmData += String.format("%-13s"," ");
//		for(ArableCrop c: cs) {
//			this.farmData += String.format("%-8s",c.getName()) ;
//		};
//		
//		//GAMSParameter r = this.db.addParameter("varcost", 2, "the variable cost of each crop per farm per hectare (euros/ha)");
//		for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
//			 for(Farm f: m.getAgentLayer(Farm.class)) {
//				 this.farmData += "\n" + String.format("%-13s",f.getName());
//				 for(ArableCrop c: cs) {
//					 double v = (f.getVarCost().get(c)==null)?0:f.getVarCost().get(c);
//					 this.farmData += String.format("%-8s",v);
//				 }
//			 }
//		}
//		this.farmData += "\n;";
//	}
	
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
	
	
	
}
