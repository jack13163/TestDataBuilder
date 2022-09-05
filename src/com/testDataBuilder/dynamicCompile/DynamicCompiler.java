package com.testDataBuilder.dynamicCompile;

import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import com.testDataBuilder.config.TableConfig;
import com.testDataBuilder.core.role.IJavaRole;
import com.testDataBuilder.core.role.JavaRole;
import com.testDataBuilder.exception.BaseException;


public class DynamicCompiler {
	
	public static final String DEF_CLASS_NAME = "JavaRoleEx";
	
	static{
		File tempDir = new File("temp");
		tempDir.mkdirs();
		System.out.println("======== Dynamic Compile Temp DIR:" + tempDir.getAbsolutePath() + "======");
		setBaseDir(tempDir.getAbsolutePath());
	}

    public DynamicCompiler(){
    	this.className = DEF_CLASS_NAME + System.currentTimeMillis();
    }
    
    private String className = null;

    public static String pkgName = "com.testDataBuilder.core.role";
    
    private DynaCode dynaCode = null;
    
    public DynaCode getDynaCode(){
        if(dynaCode == null){
            dynaCode = new DynaCode();
            dynaCode.addSourceDir(new File(getBaseDir()));            
        }
        return dynaCode;
    }
    
    /**
     * 动态编译。
     * @param javaSource
     * @param tableConfig
     * @return
     * @throws BaseException
     */
    public IJavaRole getJavaRoleImpl(String javaSource, TableConfig tableConfig) throws BaseException {
        IJavaRole javaRoleImpl = getJavaRoleImpl(javaSource,false);
        javaRoleImpl.setTableConfig(tableConfig);
        return javaRoleImpl;
    }

    /**
     * 编译javaSource.
     * @param javaSource
     * @param deleteFiles 完成后是否删除.java.class文件。
     * @return
     * @throws BaseException
     */
    private IJavaRole getJavaRoleImpl(String javaSource, boolean deleteFiles) throws BaseException  {
        IJavaRole javaRoleImpl =  null;
        javaSource = javaSource.replaceFirst(DEF_CLASS_NAME, this.className);

        File javaSourceFile = new File(getClassDir(),className+".java");
        File javaClassFile = new File(getClassDir(), className + ".class");
        
		try {
			FileUtils.writeStringToFile(javaSourceFile, javaSource);
			javaRoleImpl =  (IJavaRole) getDynaCode().newProxyInstance(IJavaRole.class,pkgName + "." + className);
		} catch (RuntimeException e) {
			System.out.println("动态编译TableConvert.java的时候出错！可能是tools.jar版本同当前项目不匹配!");
			e.printStackTrace();
		}catch(Throwable ex){
            System.out.println("动态编译时出错。");
            throw new BaseException(ex);
        } finally{
        	if(deleteFiles){
        	   javaSourceFile.deleteOnExit();
        	   javaClassFile.deleteOnExit();
        	}
        }
        return javaRoleImpl;
    }
    
    public IJavaRole validateJavaSource(String javaSource) throws BaseException{
    	return getJavaRoleImpl(javaSource, true);
    }
    
    //[end]
    
  
    public static void main(String[] args) throws Exception {
    	DynamicCompiler dynamic = new DynamicCompiler();
    	dynamic.getJavaRoleImpl(JavaRole.getJavaSourceTemplate(), null);
    	
    }

    public static String getBaseDir() {
        return DynaCode.getBaseDir();
    }

    private static String classDir = null;
    
    public static String getClassDir(){
    	if(classDir == null){
    		classDir = getBaseDir() + "/" + (pkgName.replace('.', '/'));
    	}
    	return classDir;
    }
    public static void setBaseDir(String baseDir) {
        DynaCode.setBaseDir(baseDir);
    }
    
    
    public static void clearAll(String strDir){
        File dir = new File(strDir);        
        if(dir.isDirectory()){
            String[] subFiles = dir.list();
            for(int i=0;i < subFiles.length;i++){
                clearAll(dir.getAbsolutePath() + System.getProperty("file.separator") + subFiles[i]);
            }
        }else{
            if(dir.exists()){
                dir.delete();
            }
        }        
    }
    
    public static void clearTempFiles(){
        clearAll(DynamicCompiler.getBaseDir());
    }
}
