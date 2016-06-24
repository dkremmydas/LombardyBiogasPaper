package LombardyBiogasPaper.tests;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Test;

import com.gams.api.GAMSJob;
import com.gams.api.GAMSVariable;
import com.gams.api.GAMSVariableRecord;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;


public class gamsInterface {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void simpleGAMS() {
		//System.out.println(System.getProperty("java.io.tmpdir"));
		GAMSWorkspaceInfo ginfo = new GAMSWorkspaceInfo(System.getProperty("java.io.tmpdir"), 
												"C:\\GAMS\\win64\\24.0",
												false);
		GAMSWorkspace ws = new GAMSWorkspace(ginfo);
		GAMSJob t1 = ws.addJobFromGamsLib("trnsport");    
        t1.run(); 
        
        System.out.println("Ran with Default:");
        GAMSVariable x = t1.OutDB().getVariable("x");
        for (GAMSVariableRecord rec :  x) {
            System.out.print("x(" + rec.getKeys()[0] + ", " + rec.getKeys()[1] + "):");
            System.out.print(", level    = " + rec.getLevel());
            System.out.println(", marginal = " + rec.getMarginal());
        }
		
	}

}
