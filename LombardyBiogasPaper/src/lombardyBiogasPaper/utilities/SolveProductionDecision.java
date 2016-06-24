package lombardyBiogasPaper.utilities;

import java.util.HashMap;

import lombardyBiogasPaper.agents.Farm;

import com.gams.api.GAMSDatabase;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;
import com.google.common.collect.Table;

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
	
	private String modelFile="arableFarmModel.gms";
	
	private Table farmSurfaces, farmYields, farmVarCosts;
	private HashMap<Farm,Float> totalLand;
	
	private GAMSDatabase db; private GAMSJob job;

	public SolveProductionDecision() {
		this.db = ws.addDatabase("farmData");
	}
	
	
	
	
	private void buildYieldParameter() {
		
	}
	
	
}
