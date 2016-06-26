package lombardyBiogasPaper.utilities;

import java.util.Vector;

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
	
	
	private GAMSDatabase db; private GAMSJob job;

	public SolveProductionDecision() {
		this.db = ws.addDatabase("farmData");
		System.out.println("Building Parameters");
		this.buildYieldParameter();
		this.totalLandParameter();
		this.job = ws.addJobFromFile(modelFile);
        GAMSOptions opt = ws.addOptions();
        opt.defines("gdxincname", db.getName());
        System.out.println("Running Job");
        this.job.run(opt, db);
        System.out.println("Finished Job");

        GAMSVariable var = this.job.OutDB().getVariable("mcult");
        for(GAMSVariableRecord rec : var) 
        	System.out.println("mcult(" + rec.getKeys()[0] + ", " + rec.getKeys()[1] + "): level=" + rec.getLevel() + " marginal=" + rec.getMarginal());
        System.out.println();
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
	
	private  void totalLandParameter() {
		 GAMSParameter r = this.db.addParameter("totalLand", 1, "the total available land of farm (ha)");
		 for(Municipality m: SimulationContext.getInstance().getMunicipalities()) {
			 for(Farm f: m.getAgentLayer(Farm.class)) {
				 r.addRecord(f.getName()).setValue(Double.valueOf(f.getTotalLand()));
			 }	
		 }
	}
	
	
	
}
