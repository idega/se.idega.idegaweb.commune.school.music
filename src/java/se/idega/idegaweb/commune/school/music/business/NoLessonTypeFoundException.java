/*
 * Created on 5.5.2004
 */
package se.idega.idegaweb.commune.school.music.business;

import com.idega.data.IDOEntity;

/**
 * @author laddi
 */
public class NoLessonTypeFoundException extends Exception {

	public NoLessonTypeFoundException() {
		super();
	}

	public NoLessonTypeFoundException(String message) {
		super(message);
	}

	public NoLessonTypeFoundException(Exception exeption) {
		this("NoInstrumentsFoundException - Originally: " + exeption.getClass().getName() + " : " + exeption.getMessage());
	}

	public NoLessonTypeFoundException(Exception exeption, IDOEntity thrower) {
		this("NoInstrumentsFoundException - Originally: " + exeption.getClass().getName() + " : " + exeption.getMessage() + " : from : " + thrower.getClass().getName());
	}
}