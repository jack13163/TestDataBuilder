package com.testDataBuilder.util.pkg;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;


public class JarBuilder {

    public String workspaceDir = System.getProperty("user.dir");    
    public static final String SEP = System.getProperty("file.separator");
    public final String META_INF = "META-INF";    
    public final String MANIFEST_FILE_NAME = "MANIFEST.MF";
    public final String LINE_SEP = System.getProperty("line.separator");
    
    public static final String TEMP_CONFIG = "JarBuilder.config.xml";
    
    
    public void saveConfig(Properties properties, File configFile){
        if(properties != null && properties.size() > 0){
            try {
                properties.storeToXML(new FileOutputStream(configFile), "");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    public Properties loadFromConfig(File file){
        Properties properties = new Properties();
        try {
            properties.loadFromXML(new FileInputStream(file));
        } catch (InvalidPropertiesFormatException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
    
    /**
     * <p><code>main</code></p>
     * @param args
     * @author LiuXiaojie 2007-10-12
     * @throws IOException 
     */
    public static void main(String[] args) throws IOException {        
        JarBuilder jarBuilder =  new JarBuilder();        
        jarBuilder.build();
    }
    
    String baseDir = null;
    String destDir = null;
    String version = null;
    String mainClass = null;       
    String jarName = null;
    
    public static final String BASE_DIR = "baseDir";
    public static final String VERSION = "version";
    public static final String MAIN_CLASS = "mainClass";
    public static final String JAR_NAME = "jarName";
    public static final String DEST_DIR = "destDir";
    
    private static List<String> excludeDirs = null;
    public static List<String> getExcludeDirs(){
    	if(excludeDirs == null){
    		excludeDirs = new ArrayList<String>();
    		excludeDirs.add("helpdoc");  
            excludeDirs.add("language");
    	}
    	return excludeDirs;
    }
    
    public static void makeJar(File directory, File zipFile, Manifest manifest) throws FileNotFoundException {
      
    	JarOutputStream  jos = null;
        try{
            jos = new JarOutputStream(new FileOutputStream(zipFile), manifest);
            String fileNames[] = directory.list();
        if (fileNames != null) {
            for (int i = 0; i < fileNames.length; i++){
                recurseFiles(new File(directory, fileNames[i]),jos, "");
            }
        }
        }catch(IOException ex){
            ex.printStackTrace();
        }finally{
            if(jos != null){
                try {
                    jos.flush();
                    jos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        
    }
    
    private static void recurseFiles(File file,JarOutputStream jos, String pathName) throws IOException, FileNotFoundException {
        if (file.isDirectory()) {
        	if(!getExcludeDirs().contains(file.getName())){
	            pathName = pathName + file.getName() + "/";
	            jos.putNextEntry(new JarEntry(pathName));
	            String fileNames[] = file.list();
	            if (fileNames != null) {
	                for (int i = 0; i < fileNames.length; i++)
	                    recurseFiles(new File(file, fileNames[i]),jos, pathName);
	
	            }
	            jos.closeEntry();
        	}
        } else if(isNotJarFile(file)){
            JarEntry jarEntry = new JarEntry(pathName + file.getName());
            System.out.println(pathName + file.getName());
            FileInputStream fin = new FileInputStream(file);
            BufferedInputStream in = new BufferedInputStream(fin);
            jos.putNextEntry(jarEntry);
            int len = -1;
            byte buf[] = new byte[1024];
            while ((len = in.read(buf)) >= 0){
                jos.write(buf, 0, len);
            }
            in.close();
            jos.closeEntry();
        }
    }

    
    public void build() throws IOException{
        //JarBuilder builder = new JarBuilder();
        File tempConfig = new File(TEMP_CONFIG);
        boolean useConfig = false;
        if(tempConfig.exists()){
            Properties properties =  this.loadFromConfig(tempConfig);
            baseDir = properties.getProperty(BASE_DIR);
            destDir = properties.getProperty(DEST_DIR);
            version = properties.getProperty(VERSION);
            mainClass = properties.getProperty(MAIN_CLASS);
            jarName = properties.getProperty(JAR_NAME);
            System.out.println("the last configure is:");
            System.out.println("    baseDir: " + baseDir);
            System.out.println("    destDir: " + destDir);            
            System.out.println("    version: " + version);
            System.out.println("  MainClass: " + mainClass);
            System.out.println("    jarName: " + jarName);
            System.out.println("=======================================");
            String result = getInput("use the last configure file to run？(Y/N)");
            if(result != null && result.equalsIgnoreCase("Y")){
                useConfig = true;
            }
        }
        
        if(!useConfig){
            String curDir = System.getProperty("user.dir");
            baseDir =  getInput("please input the basic directory(default is :" + curDir + ")" + LINE_SEP 
                            + " :");
            if(baseDir == null || baseDir.equals("")){
                baseDir = curDir;
            }
            destDir = getInput("please input the dest directory(default is :" +baseDir + ")" + LINE_SEP);
            if(destDir == null || destDir.equals("")){
                destDir = baseDir;
            }
            
            version = getInput("please input the jar version(default is 1.0):");
            if(version == null || version.equals("")){
                version = "1.0";
            }
            mainClass = getInput("please input the main class(full name):");
            String className = mainClass.substring(mainClass.lastIndexOf(".") + 1);        
            jarName = getInput("please input the jar package name(default is " + className + ".jar):");        
            
            if(jarName == null || jarName.equals("")){
                jarName = className + ".jar";
            }
            if(!jarName.endsWith(".jar")){
                jarName = jarName + ".jar";
            }
            Properties pros = new Properties();
            pros.setProperty(BASE_DIR, baseDir);
            pros.setProperty(DEST_DIR, destDir);
            pros.setProperty(VERSION, version);
            pros.setProperty(MAIN_CLASS, mainClass);
            pros.setProperty(JAR_NAME, jarName);
            this.saveConfig(pros, tempConfig);
        }
        
        autoBuildByConfigFile(tempConfig, false);
        
        
    }
    

    public void autoBuildByConfigFile(File configFile, boolean showLog) throws FileNotFoundException, IOException{
    	Properties properties =  this.loadFromConfig(configFile);
        baseDir = properties.getProperty(BASE_DIR);
        destDir = properties.getProperty(DEST_DIR);
        version = properties.getProperty(VERSION);
        mainClass = properties.getProperty(MAIN_CLASS);
        jarName = properties.getProperty(JAR_NAME);
        autoBuildByParam(baseDir,destDir,version, mainClass,jarName,showLog);
    }
    
    public void autoBuildByParam(
					    		String baseDir, 
                                String destDir,
					    		String version, 
					    		String mainClass, 
					    		String jarName, 
					    		boolean showLog) throws FileNotFoundException, IOException{
    	if(showLog){
	        System.out.println("========================= config=========================");
            System.out.println("    baseDir: " + baseDir);
            System.out.println("    destDir: " + destDir);
            System.out.println("    version: " + version);
	        System.out.println("  MainClass: " + mainClass);
	        System.out.println("    jarName: " + jarName);
	        System.out.println("========================= end =========================");
        }
        String fileName = getManifestFile(baseDir, version, mainClass, jarName);
                
        Manifest manifest = new Manifest(new FileInputStream(fileName)); 
          
        File jarFile = new File(destDir,jarName);
        
        this.makeJar(new File(baseDir), jarFile, manifest);
        
        System.out.println("jar file store in directory:" + jarFile.getAbsolutePath());
        
    }
    
    @Override
    protected void finalize() throws Throwable {
        this.deleteDirectory(new File(baseDir, META_INF));
        super.finalize();
    }

    public String getManifestFile(String baseDir, String version, String mainClass,String excludeJar){
        //version info        
        if(version == null || version.equals("")){
            version = "1.0";
        }
        StringBuffer buffer = new StringBuffer("Manifest-Version: " + version + LINE_SEP);
        
        List<String> jarFiles = new LinkedList<String>();
        getJarFiles(new File(baseDir), jarFiles, baseDir);
        
        if(jarFiles.size() > 0){
            buffer.append("Class-Path: " + LINE_SEP);
            for(String jarName : jarFiles){
                if(!jarName.equalsIgnoreCase(excludeJar)){
                    buffer.append(" " + jarName + "  " +  LINE_SEP);
                }
            }
            //buffer.append(LINE_SEP);
        }
        
        
        buffer.append("Main-Class: " + mainClass + LINE_SEP);
       
        try {
            File parentDir = new File(baseDir + SEP + META_INF);
            if(!parentDir.exists()){
                parentDir.mkdir();
            }
            return saveFile(new File(parentDir, MANIFEST_FILE_NAME), buffer.toString());            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    
    public List<String> getJarFiles(File baseDir, List<String> jarFiles, String strBaseDir){ 
        if(baseDir.isDirectory()){
            File[] files = baseDir.listFiles();
            for(File file : files){
                if(file.isDirectory()){
                    getJarFiles(file, jarFiles, strBaseDir);
                }else if (isJarFile(file)){
                    System.out.println("jar:" + file.getAbsolutePath());
                    jarFiles.add(file.getAbsolutePath().replace(strBaseDir + SEP, ""));
                }
            }            
        }
        return jarFiles;
    }
    
    static BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
    
    private static String getInput(String prompt){
        System.out.print(prompt);
        String retValue = null;
        try {
            retValue = read.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }       
        
        return retValue;
    }
    
    //[start]文件操作。
    /**
     * 保存的文件全名。
     * <p><code>saveFile</code></p>
     * @param fileName
     * @param content
     * @return
     * @throws Exception
     * @author LiuXiaojie 2007-10-12
     */
    public static String saveFile(File file, String content) throws Exception {
        BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        bw.write(content);
        bw.flush();
        bw.close();
        return file.getAbsolutePath();
    }
    
    /**
     * 删除一个目录。
     * <p><code>deleteDirectory</code></p>
     * @param file
     * @author LiuXiaojie 2007-12-27
     */
    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        cleanDirectory(directory);
        if (!directory.delete()) {
            String message =
                "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }

    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            if (!file.exists()) {
                throw new FileNotFoundException("File does not exist: " + file);
            }
            if (!file.delete()) {
                String message =
                    "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }
    //[end]
    
    
    public static boolean isNotJarFile(File file){
        String fileName = file.getName();
        return !fileName.endsWith(".jar") && !fileName.endsWith("MANIFEST.MF") && !fileName.endsWith("META-INF");
    }
    
    public static boolean isJarFile(File file){
        String fileName = file.getName();
        return fileName.endsWith("jar") || fileName.endsWith("zip");
    }
    
    //[end]
}
