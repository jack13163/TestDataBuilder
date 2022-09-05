package com.testDataBuilder.ui.core;

import java.awt.Color;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;

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
public class ProgressPanel extends JPanel implements IProcess{

	private int gap = 15;
	
	private static final long serialVersionUID = 1L;
	private JProgressBar processBar;
	private JTextField txtInfo;

	/**
	 * This is the default constructor
	 */
	public ProgressPanel() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setLayout(null);
		this.setPreferredSize(new java.awt.Dimension(456, 197));
		this.add(getProcessBar());
		this.add(getTxtInfo());
		this.addComponentListener(new ComponentAdapter(){
            @Override
             public void componentResized(ComponentEvent e) {
                 ProgressPanel.this.PanelResized(e);                    
             } 
         });
	}
	
	protected void PanelResized(ComponentEvent e) {
		int width = this.getWidth();
		int controlWidth = width - (gap * 2);
		this.getProcessBar().setSize(controlWidth, this.getProcessBar().getHeight());
		this.getTxtInfo().setSize(controlWidth, this.getTxtInfo().getHeight());
	}

	public JProgressBar getProcessBar() {
		if(processBar == null) {
			processBar = new JProgressBar();
			processBar.setBounds(15, 10, 429, 22);
            processBar.setForeground(new Color(95,191,95));
		}
		return processBar;
	}
	
	public JTextField getTxtInfo() {
		if(txtInfo == null) {
			txtInfo = new JTextField();
			txtInfo.setEditable(false);
			txtInfo.setBounds(15, 44, 429, 22);
		}
		return txtInfo;
	}

	public int getGap() {
		return gap;
	}

	public void setGap(int gap) {
		this.gap = gap;
	}

	public void setInfo(String info) {
		this.getTxtInfo().setText(info);
	}

	public void setProcessBarPos(int pos) {
		this.getProcessBar().setValue(pos);
	}

}
