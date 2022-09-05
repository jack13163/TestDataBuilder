package com.testDataBuilder.ui.project;

public class ProjectInfo{
    
    private String projectName;
    
    private String projectPath;

    public ProjectInfo(String projectName, String projectPath) {
        super();
        this.projectName = projectName;
        this.projectPath = projectPath;
    }

    public ProjectInfo() {
        super();        
    }

    @Override
    public int hashCode() {
        final int PRIME = 31;
        int result = 1;
        result = PRIME * result + ((projectName == null) ? 0 : projectName.hashCode());
        result = PRIME * result + ((projectPath == null) ? 0 : projectPath.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final ProjectInfo other = (ProjectInfo) obj;
        if (projectName == null) {
            if (other.projectName != null) return false;
        } else if (!projectName.equalsIgnoreCase(other.projectName)) return false;
        if (projectPath == null) {
            if (other.projectPath != null) return false;
        } else if (!projectPath.equalsIgnoreCase(other.projectPath)) return false;
        return true;
    }
    
//    public String toString(){
//        return BeanXMLMapping.toXML(this);
//    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectPath() {
        return projectPath;
    }

    public void setProjectPath(String projectPath) {
        this.projectPath = projectPath;
    }
    

    
}
