/*
 * Created on 5.5.2004
 */
package se.idega.idegaweb.commune.school.music.business;

import com.idega.data.IDOEntity;

/**
 * @author laddi
 */
public class NoDepartmentFoundException extends Exception {

	public NoDepartmentFoundException() {
		super();
	}

	public NoDepartmentFoundException(String message) {
		super(message);
	}

	public NoDepartmentFoundException(Exception exeption) {
		this("NoInstrumentsFoundException - Originally: " + exeption.getClass().getName() + " : " + exeption.getMessage());
	}

	public NoDepartmentFoundException(Exception exeption, IDOEntity thrower) {
		this("NoInstrumentsFoundException - Originally: " + exeption.getClass().getName() + " : " + exeption.getMessage() + " : from : " + thrower.getClass().getName());
	}
}