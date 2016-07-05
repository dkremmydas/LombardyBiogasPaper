package lombardyBiogasPaper.utilities.gamsInterface;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import com.gams.api.GAMSDatabase;
import com.gams.api.GAMSJob;
import com.gams.api.GAMSWorkspace;
import com.gams.api.GAMSWorkspaceInfo;
import com.google.common.io.Files;


/*
 * Responsible for solving GAMS model with several files.
 * It takes a list of files or strings and
 * A gams model file and an input.gdx have to be provided. 

 */
public class GamsModelSolver {
	
	private GAMSWorkspace ws;
	
	private File workingDir = null;	
	
	private boolean debug = false;
	
	private String systemDir = "C:\\GAMS\\win64\\24.0";
	
	private File modelFile = null;
	
	private ArrayList<File> dataFile = new ArrayList<>();
	
	
	public GamsModelSolver() {	
		this.getNewWorkingDir();
		initWorkspace();
	}
	
	public GamsModelSolver(String systemDir) {	
		this.getNewWorkingDir();
		this.systemDir=systemDir;
		initWorkspace();
	}
	
	public GamsModelSolver(String systemDir, boolean debug) {	
		this.getNewWorkingDir();
		this.systemDir=systemDir;
		this.debug = debug;
		initWorkspace();
	}
	
	private void initWorkspace() {
		ws = new GAMSWorkspace(new GAMSWorkspaceInfo(this.workingDir.getAbsolutePath(),this.systemDir,this.debug));
		System.out.println(this.workingDir);
	}
	

	public GAMSDatabase solve(String modelFile) throws IOException {
		
		this.modelFile = new File(modelFile);
		return this.doSolve();
	}

	public GAMSDatabase solve(String modelFile, String dataFile) throws IOException {
		this.modelFile = new File(modelFile);
		this.dataFile.add(new File(dataFile));		
		return this.doSolve();
	}
	
	public GAMSDatabase solve(String modelFile, ArrayList<String> dataFiles) throws IOException {
		this.modelFile = new File(modelFile);
		for (String string : dataFiles) {
			this.dataFile.add(new File(string));		
		}
		return this.doSolve();
	}


	private GAMSDatabase doSolve() throws IOException {
				
		//copy data file to working dir
		for (File file : dataFile) {
			if(file.exists()) {
				FileUtils.copyFile(file, new File(this.workingDir+File.separator+file.getName()));
			}
			else {
				throw new IOException("Gams Data File does not exist ! ["+file.getAbsolutePath()+"]");
			}
		}
		
		//copy data file to working dir
		if(this.modelFile.exists()) {
			FileUtils.copyFile(this.modelFile, new File(this.workingDir+File.separator+this.modelFile.getName()));
		}
		else {
			throw new IOException("Gams Model File does not exist ! ["+this.modelFile.getAbsolutePath()+"]");
		}
		
		
		GAMSJob job = ws.addJobFromFile(modelFile.getName());
		job.run();
        return job.OutDB();
		
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


	@Override
	public String toString() {
		return "GamsModelSolver [workingDir=" + workingDir + ", systemDir="
				+ systemDir + "]";
	}
	
	
	public String getWorkingDir() {
		return this.workingDir.getAbsolutePath();
	}
	
	
}
