package com.testDataBuilder.exception;

public class InvalidIPValueException extends BaseException {

	public InvalidIPValueException(String value){
		super("invalid ip value [" +value + "]");
	}
}
