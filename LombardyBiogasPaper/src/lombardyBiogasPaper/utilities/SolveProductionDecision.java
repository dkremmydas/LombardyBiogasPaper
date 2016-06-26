package lombardyBiogasPaper.utilities;

import java.util.Vector;

import org.apache.log4j.Level;

import lombardyBiogasPaper.SimulationContext;
import lombardyBiogasPaper.agents.Farm;
import lombardyBiogasPaper.agents.Municipality;
import lombardyBiogasPaper.crops.ArableCrop;

import com.gams.api.GAMSDatabase;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSOptions;
import com.gams.api.GAMSParameter;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;

/**
 * Collects data from agents, creates matrix and calls gams software to solve the LP
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
	
	
	private GAMSDatabase db, dbResults; private GAMSJob job;

	public SolveProductionDecision() {
		this.db = ws.addDatabase("farmData");
		SimulationContext.logMessage(this.getClass(), Level.DEBUG, "Building Parameters from Farms");
		this.buildYieldParameter();
		this.totalLandParameter();
		this.buildVarCostParameter();
		this.job = ws.addJobFromFile(modelFile);
        GAMSOptions opt = ws.addOptions();
        opt.defines("gdxincname", db.getName());
        SimulationContext.logMessage(this.getClass(), Level.DEBUG, "finished... Running Job");
        this.job.run(opt, db);
        this.dbResults = this.job.OutDB();
        SimulationContext.logMessage(this.getClass(), Level.DEBUG, "finished...");
	}
	

	public GAMSDatabase retrieveResults() {
		return this.dbResults;
	}


	public void updateFarmLandUse() {
		
	}
	

	private  void buildYieldParameter() {
		GAMSParameter r = this.db.addParameter("yield", 2, "the yield of each crop for a farm (the old rendt) (tn/ha)");
		for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
			 for(Farm f: m.getAgentLayer(Farm.class)) {
				 for(ArableCrop c: f.getYield().keySet()) {
					 if(f.getYield().get(c).compareTo(0.0f)==1) {
						 Vector<String> v = new Vector<>(2); 
						 v.add(f.getName());v.add(c.getName());
						 r.addRecord(v).setValue(Double.valueOf(f.getYield().get(c)));
					 }
					
				 }
				
			 }
		}
	}
	
	private  void buildVarCostParameter() {
		GAMSParameter r = this.db.addParameter("varcost", 2, "the variable cost of each crop per farm per hectare (euros/ha)");
		for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
			 for(Farm f: m.getAgentLayer(Farm.class)) {
				 for(ArableCrop c: f.getVarCost().keySet()) {
					 if(f.getYield().get(c).compareTo(0.0f)==1) {
						 Vector<String> v = new Vector<>(2); 
						 v.add(f.getName());v.add(c.getName());
						 r.addRecord(v).setValue(Double.valueOf(f.getVarCost().get(c)));
					 }
					
				 }
				
			 }
		}
	}
	
	private  void totalLandParameter() {
		 GAMSParameter r = this.db.addParameter("totalLand", 1, "the total available land of farm (ha)");
		 for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
			 for(Farm f: m.getAgentLayer(Farm.class)) {
				 r.addRecord(f.getName()).setValue(Double.valueOf(f.getTotalLand()));
			 }	
		 }
	}
	
	
	
}
