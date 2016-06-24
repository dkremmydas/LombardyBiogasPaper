package lombardyBiogasPaper.utilities;

import com.gams.api.GAMSDatabase;
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
	
	private String modelFile="arableFarmModel.gms";
	
	private GAMSDatabase db;

	public SolveProductionDecision() {
		this.db = ws.addDatabase();
	}
	
	
	
	
	private buildYields
	
	
}
