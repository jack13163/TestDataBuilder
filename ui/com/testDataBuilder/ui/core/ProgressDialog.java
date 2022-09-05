package com.testDataBuilder.ui.core;

import java.awt.Dialog;
import java.awt.BorderLayout;

import javax.swing.JDialog;

import com.testDataBuilder.core.IProcess;


/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class ProgressDialog extends CenterDialog implements IProcess{

	private static final long serialVersionUID = 1L;
	private ProgressPanel progressPanel;

	/**
	 * @param owner
	 */
	public ProgressDialog(Dialog owner) {
		super(owner);
		initialize();
		this.center();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(494, 155);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		getContentPane().add(getProgressPanel(), BorderLayout.CENTER);
		this.setModal(true);
	}
	
	public static void main(String[] args) {
		ProgressDialog progressDialog = new ProgressDialog(null);
		progressDialog.setVisible(true);
	}
	
	public ProgressPanel getProgressPanel() {
		if(progressPanel == null) {
			progressPanel = new ProgressPanel();
			progressPanel.setPreferredSize(new java.awt.Dimension(404, 173));
		}
		return progressPanel;
	}

	public void setInfo(String info) {
		this.getProgressPanel().getTxtInfo().setText(info);
	}

	public void setProcessBarPos(int pos) {
		this.getProgressPanel().getProcessBar().setValue(pos);
	}
	
}

