package com.testDataBuilder.ui.project;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.testDataBuilder.util.BeanXMLMapping;
/**
 * 菜单中的最近打开的工程列表。
 * <p>Title：RecentProjectList.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2007 TestDataBuilder,Inc</p>
 * <p>Company：TestDataBuilder,Inc</p> 
 * @author LiuXiaojie 2007-12-26
 * @version 1.0
 */
public class RecentProjects {

	public static final String REC_PROJS_FILE = "recentProjects.xml";
	
    static final Logger logger = Logger.getLogger(RecentProjects.class);
    
    public RecentProjects(){
        
    }
    
    private static RecentProjects instance = null;
    
    public static synchronized RecentProjects getInstance(){
    	if(instance == null){
    		try {
				instance = initFromFile();
			} catch (IOException ex) {
				logger.error("initFromFile", ex);
			}
			if(instance == null){
				instance = new RecentProjects();
			}
    	}
    	return instance;
    }
    
    public static RecentProjects initFromFile() throws IOException{
    	File file = new File(REC_PROJS_FILE);
    	if(file.exists()){
    		String xml = FileUtils.readFileToString(file, "utf8");
    		return fromXml(xml);
    	}
    	return null;
    }
    
    /**
     * list中index越大，表示时间越远
     */
    private List<ProjectInfo> recentProjectList = null;
    
    public static final int MAX_SIZE = 10;
    
    private boolean changed = false;
    
    public List<ProjectInfo> getRecentProjectList() {
        if(recentProjectList == null){
            recentProjectList = new  ArrayList<ProjectInfo>();
        }
        return recentProjectList;
    }

    public void setRecentProjectList(List<ProjectInfo> recentProjectList) {
        this.recentProjectList = recentProjectList;
    }
    
    public void addProject(ProjectInfo projectInfo){
        if(this.getRecentProjectList().contains(projectInfo)){
            this.getRecentProjectList().remove(projectInfo);
        }else{
            if(this.getRecentProjectList().size() >= MAX_SIZE){
                this.getRecentProjectList().remove(MAX_SIZE -1);
            }            
        }
        this.getRecentProjectList().add(0,projectInfo);
        this.setChanged(true);
    }
    
    public boolean removeProjectByName(String projectName){
        for(ProjectInfo projectInfo : this.getRecentProjectList()){
            if(projectInfo.getProjectName().equalsIgnoreCase(projectName)){
                return this.getRecentProjectList().remove(projectInfo);
            }
        }
        return false;
    }
      
    public boolean removeProjectByPath(String projectPath){
        for(ProjectInfo projectInfo : this.getRecentProjectList()){
            if(projectInfo.getProjectPath().equalsIgnoreCase(projectPath)){
                return this.getRecentProjectList().remove(projectInfo);
            }
        }
        return false;
    }
      

    public void addProject2RecentList(String projectName, String projectPath){
        ProjectInfo projectInfo = new ProjectInfo(projectName, projectPath);
        this.addProject(projectInfo);
        try {
            this.storeToXmlFile();
        } catch (IOException e) {
            logger.error("addProject2RecentList", e);
        }
    }
    
    public void storeToXmlFile() throws IOException{
    	String xml = this.toString();
    	FileUtils.writeStringToFile(new File(REC_PROJS_FILE),
    			xml, "utf8");
    }
    
    public void removeProjectByPathAndSave(String projectPath){        
        this.removeProjectByPath(projectPath);
        try {
            this.storeToXmlFile();
        } catch (IOException e) {
            logger.error("addProject2RecentList", e);
        }
    }
    
    public String toString(){
        try {
            return BeanXMLMapping.toXML(this);
        } catch (IOException e) {
            logger.error("toString", e);
        }
        return null;
    }
    

    public static RecentProjects fromXml(String xml) throws IOException{
        return (RecentProjects) BeanXMLMapping.fromXML(xml, RecentProjects.class);
    }
    
    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}

