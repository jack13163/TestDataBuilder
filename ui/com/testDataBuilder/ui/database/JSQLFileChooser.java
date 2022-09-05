package com.testDataBuilder.ui.database;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;

import com.testDataBuilder.resources.RM;


public class JSQLFileChooser extends JFileChooser {

    FileFilter filter = 
        new FileFilter(){
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getAbsolutePath().endsWith(".sql"); //$NON-NLS-1$
            }
            @Override
            public String getDescription() {                    
                return RM.R("label.JSQLFileChooser.SQLScriptFile"); //$NON-NLS-1$
            }            
    }; 
    
    /**
     * Comment for <code>serialVersionUID</code>
     */
    private static final long serialVersionUID = 3L;

    public JSQLFileChooser() {
        super();
		initialize();
    }

    public JSQLFileChooser(File currentFile, FileSystemView fsv) {
        super(currentFile, fsv);
		initialize();
    }

    public JSQLFileChooser(File currentDirectory) {
        super(currentDirectory);
		initialize();
    }

    public JSQLFileChooser(FileSystemView fsv) {
        super(fsv);
		initialize();
    }

    public JSQLFileChooser(String currentDirectoryPath, FileSystemView fsv) {
        super(currentDirectoryPath, fsv);
		initialize();
    }

    public JSQLFileChooser(String currentDirectoryPath) {
        super(currentDirectoryPath);
		initialize();
    }


    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setDialogTitle(RM.R("label.JSQLFileChooser.title")); //$NON-NLS-1$
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);   
        this.setFileFilter(filter);
    }
}
