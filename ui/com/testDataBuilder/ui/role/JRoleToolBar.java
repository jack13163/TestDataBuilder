package com.testDataBuilder.ui.role;

import javax.swing.JToolBar;
import java.awt.Dimension;
import com.testDataBuilder.ui.core.JTDButton;

public class JRoleToolBar extends JToolBar {

    RoleDialog parent = null;
    private JTDButton btnNewRole = null;
    private JTDButton btnDelSelRole = null;
    
    public JRoleToolBar(RoleDialog parent){
        super();
		initialize();
        this.parent = parent;
    }

    /**
     * This method initializes this
     * 
     */
    private void initialize() {
        this.setSize(new Dimension(427, 28));
        this.add(getBtnNewRole());
        this.add(getBtnDelSelRole());
    		
    }

    /**
     * This method initializes btnNewRole	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnNewRole() {
        if (btnNewRole == null) {
            btnNewRole = new JTDButton();
            btnNewRole.setText("添加规则");
        }
        return btnNewRole;
    }

    /**
     * This method initializes btnDelSelRole	
     * 	
     * @return com.testDataBuilder.ui.core.JTDButton	
     */
    private JTDButton getBtnDelSelRole() {
        if (btnDelSelRole == null) {
            btnDelSelRole = new JTDButton();
            btnDelSelRole.setText("删除所选规则");
        }
        return btnDelSelRole;
    }
    
    
}  //  @jve:decl-index=0:visual-constraint="10,10"
