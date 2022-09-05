package com.testDataBuilder.ui.main;

import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JOptionPane;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;

import com.testDataBuilder.resources.RM;

public class OpenWorkspaceUtil {

    static Logger logger = Logger.getLogger(OpenWorkspaceUtil.class);
    
    public static String COMMAND_FILE = "osExplorerCmd.xml";
    public static String WORKSPACE_MASK = "${workspace}";
    public static void openWorkspaceInExplorer(String workspace, Component parent){
        
        Properties p = new Properties();
        FileInputStream is = null;
        File file = new File(COMMAND_FILE);
        try {
            is = FileUtils.openInputStream(file);
            p.loadFromXML(is);
        } catch (IOException ex) {
            String info = String.format(RM.R("label.OpenWorkspaceUtil.error.openCommandFile"),
                            new Object[]{file.getAbsolutePath()});
            JOptionPane.showMessageDialog(parent, info);
            logger.info(info, ex);
            return;
        }
       
        String OSName = System.getProperty("os.name").toLowerCase(); //$NON-NLS-1$
        String command = (String) p.get(OSName);
        if(command != null){
            command = command.replace(WORKSPACE_MASK, workspace);
            try {
                Runtime.getRuntime().exec(command); //$NON-NLS-1$
            } catch (IOException ex) {
                String info = String.format(RM.R("label.OpenWorkspaceUtil.error.openProject"),
                                new Object[]{workspace, ex.getMessage()});
               JOptionPane.showMessageDialog(parent, info);
               logger.info(info, ex);
            }
        }else{
            String info = String.format(RM.R("label.OpenWorkspaceUtil.error.openProject.noCmd"),
                            new Object[]{OSName, file.getAbsolutePath()});
            logger.info(info);
            JOptionPane.showMessageDialog(parent, info);
        }
    }
}
