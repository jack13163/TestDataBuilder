package com.testDataBuilder.util.pkg;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;


public class TDBPackager {

	public String workspaceDir = System.getProperty("user.dir");   
	
	String baseDir = workspaceDir +FILE_SEP +  "bin";
    String version = "1.0";
    String mainClass = "TestDataBuilder";       
    String jarName = "TestDataBuilder.jar";
    
    public static final String FILE_SEP = System.getProperty("file.separator");

    
    /**
     * <p><code>main</code></p>
     * @param args
     * @author LiuXiaojie 2008-1-18
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {
        
       
//        String fromDir = getInput("input the project bin dir(default is " +curDir + "):");
//        if(fromDir == null || fromDir.equals("")){
//            fromDir = curDir;
//        }
//        
//        String toDir = getInput("input the dest dir(default is " +curDir + "):");
//        if(toDir == null || toDir.equals("")){
//            toDir = curDir;
//        }
    	TDBPackager tdbPackager = new TDBPackager();
    	tdbPackager.build();
        
    }


    public void build() throws FileNotFoundException, IOException{
        JarBuilder jarBuilder =new JarBuilder();
        
        jarBuilder.autoBuildByParam(baseDir,baseDir,version,mainClass,jarName,true);
        
        String curDir = System.getProperty("user.dir"); 
        String fromDir = curDir + "/bin";
        String toDir = curDir;
        
        String releaseDir = toDir + FILE_SEP + "TestDataBuilder";
        
        List<String> copyDirectorys = new LinkedList<String>();
        List<String> copyFiles = new LinkedList<String>();
        
        copyDirectorys.add("lib");
        copyDirectorys.add("res");
        copyDirectorys.add("projects");
        
        copyFiles.add("run.bat");
        copyFiles.add("TestDataBuilder.jar");
        
        File relDir = new File(releaseDir);
        if(relDir.exists()){
            JarBuilder.forceDelete(relDir);
        }
        for(String dir: copyDirectorys){
            FileUtils.copyDirectory(new File(fromDir, dir), new File(releaseDir, dir));
        }
        for(String file : copyFiles){
            FileUtils.copyFile(new File(fromDir, file), new File(releaseDir, file));
        }
        
        System.out.println("package all file to " + toDir);
    }
    
    static BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
    
    private static String getInput(String prompt){
        System.out.print(prompt);
        try {
            return read.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
