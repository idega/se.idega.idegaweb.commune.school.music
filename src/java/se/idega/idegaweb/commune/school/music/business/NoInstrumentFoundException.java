/*
 * Created on 5.5.2004
 */
package se.idega.idegaweb.commune.school.music.business;

import com.idega.data.IDOEntity;

/**
 * @author laddi
 */
public class NoInstrumentFoundException extends Exception {

	public NoInstrumentFoundException() {
		super();
	}

	public NoInstrumentFoundException(String message) {
		super(message);
	}

	public NoInstrumentFoundException(Exception exeption) {
		this("NoInstrumentsFoundException - Originally: " + exeption.getClass().getName() + " : " + exeption.getMessage());
	}

	public NoInstrumentFoundException(Exception exeption, IDOEntity thrower) {
		this("NoInstrumentsFoundException - Originally: " + exeption.getClass().getName() + " : " + exeption.getMessage() + " : from : " + thrower.getClass().getName());
	}
}