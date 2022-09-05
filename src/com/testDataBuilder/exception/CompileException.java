package com.testDataBuilder.exception;

import javax.swing.JComponent;

/**
 * 编译时出错.
 * <p>Title：CompileException.java</p>
 * <p>Description：TestDataBuilder</p>
 * <p>Copyright：Copyright (c)2008 frontier,Inc</p>
 * <p>Company：company-name,Inc</p> 
 * @author LiuXiaojie 2008-9-10
 * @version 1.0
 */
public class CompileException extends ValidataException {

	public CompileException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CompileException(JComponent control, String cause) {
		super(control, cause);
		// TODO Auto-generated constructor stub
	}

	public CompileException(String message, Throwable cause) {
		super(message, cause);
		// TODO Auto-generated constructor stub
	}

	public CompileException(String message) {
		super(message);
		// TODO Auto-generated constructor stub
	}

	public CompileException(Throwable cause) {
		super(cause);
		// TODO Auto-generated constructor stub
	}

}
