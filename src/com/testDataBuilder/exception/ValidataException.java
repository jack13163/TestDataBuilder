package com.testDataBuilder.exception;

import javax.swing.JComponent;



public class ValidataException extends BaseException {

    JComponent control = null;
    
	public ValidataException() {
	}

	public ValidataException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidataException(String message) {
		super(message);
	}

    public ValidataException(Throwable cause) {
        super(cause);
    }
    public ValidataException(JComponent control, String cause) {
        super(cause);
        this.setControl(control);        
    }

    public JComponent getControl() {
        return control;
    }

    public void setControl(JComponent control) {
        this.control = control;
    }

	
}
