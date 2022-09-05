package com.testDataBuilder.ui.project;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileSystemView;

import com.testDataBuilder.resources.RM;
import com.testDataBuilder.ui.help.Helper;
import com.testDataBuilder.util.Global;

public class JProjectDirChooser extends JFileChooser {

    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3L;

    public JProjectDirChooser() {
        super();
		initialize();
    }

    public JProjectDirChooser(File currentDirectory, FileSystemView fsv) {
        super(currentDirectory, fsv);
		initialize();
    }

    public JProjectDirChooser(File currentDirectory) {
        super(currentDirectory);
		initialize();
    }

    public JProjectDirChooser(FileSystemView fsv) {
        super(fsv);
		initialize();
    }

    public JProjectDirChooser(String currentDirectoryPath, FileSystemView fsv) {
        super(currentDirectoryPath, fsv);
		initialize();
    }

    public JProjectDirChooser(String currentDirectoryPath) {
        super(currentDirectoryPath);
		initialize();
    }


    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setDialogTitle(RM.R("label.JProjectDirChooser.title")); //$NON-NLS-1$
        
        Helper.getInstance().enableHelpKey(
                this, "detail.project.openProject"); //$NON-NLS-1$
        
    }

    @Override
    public void approveSelection(){
        String selDir = this.getSelectedFile().getAbsolutePath();
        if(Global.isValidWorkspace(selDir)){
            super.approveSelection();
        }else{
            JOptionPane.showMessageDialog(this, RM.R("label.JProjectDirChooser.error.invalidProjectDir")); //$NON-NLS-1$
        }
    }
}
