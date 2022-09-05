package com.testDataBuilder.ui.core;

import javax.swing.JLabel;

import com.testDataBuilder.core.IProcess;

public class ProgressLable extends JLabel implements IProcess{

	public ProgressLable(String info){
		this.setText(info);
	}
	
	private String info = null;
	private int pos = 0;
	
	public void setInfo(String info) {
		this.info = info;
		refresh();
	}

	private void refresh() {
		String str = String.format("已经生成%d条记录  %s",new Object[]{pos,info});
		this.setText(str);
		
	}

	public void setProcessBarPos(int pos) {
		this.pos = pos;
	}
	
	public void clearText(){
		this.setText(" ");
	}

}
