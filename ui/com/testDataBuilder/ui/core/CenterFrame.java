package com.testDataBuilder.ui.core;
import java.awt.Dimension;
import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import javax.swing.JFrame;

public class CenterFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	public CenterFrame(GraphicsConfiguration gc) {
		super(gc);
	}

	public CenterFrame(String title, GraphicsConfiguration gc) {
		super(title, gc);
	}

	public CenterFrame(String title) throws HeadlessException {
		super(title);
	}

	/**
	 * This is the default constructor
	 */
	public CenterFrame() {
		super();
		center();
	}

	public void center(){
		Dimension screenSize = this.getToolkit().getScreenSize();
		int x = (screenSize.width - this.getWidth()) / 2;
		int y = (screenSize.height - this.getHeight()) /2;
		this.setLocation(x, y);
	}
}
