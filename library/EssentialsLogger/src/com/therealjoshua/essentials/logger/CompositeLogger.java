package com.therealjoshua.essentials.logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The CompositeLogger is a collection of other loggers. For each log call it loops through
 * it's loggers and calls their log method. 
 */
public class CompositeLogger extends AbstractLogger {
	
	private List<Logger> loggers;
	
	public CompositeLogger() {
		this(null);
	}
	
	public CompositeLogger(ArrayList<Logger> loggers) {
		if (loggers != null) this.loggers = Collections.synchronizedList(new ArrayList<Logger>(loggers));
		else this.loggers = Collections.synchronizedList(new ArrayList<Logger>());
	}
	
	public void removeLogger(Logger logger) {
		loggers.remove(logger);
	}
	
	public void addLogger(Logger logger) {
		loggers.add(logger);
	}
	
	
	/**
	 * Checks to see if any loggers will actually handle the log request
	 * 
	 * @param level
	 * @return
	 */
	public boolean willLog(int level) {
		for (Logger l : loggers) {
			if (l.isLoggable(level)) return true;
		}
		return false;
	}
	
	/**
	 * Checks to see if any loggers will actually handle the log request
	 * 
	 * @param level
	 * @return
	 */
	public boolean willLog(String tag, int level) {
		for (Logger l : loggers) {
			if (l.isLoggable(tag, level)) return true;
		}
		return false;
	}
	
	@Override
	protected void handleV(String tag, String message, Throwable tr) {
		for (Logger logger : loggers) {
			logger.v(tag, message, tr);
		}
	}
	
	@Override
	protected void handleD(String tag, String message, Throwable tr) {
		for (Logger logger : loggers) {
			logger.d(tag, message, tr);
		}
	}
	
	@Override
	protected void handleI(String tag, String message, Throwable tr) {
		for (Logger logger : loggers) {
			logger.i(tag, message, tr);
		}
	}
	
	@Override
	protected void handleW(String tag, String message, Throwable tr) {
		for (Logger logger : loggers) {
			logger.w(tag, message, tr);
		}
	}
	
	@Override
	protected void handleE(String tag, String message, Throwable tr) {
		for (Logger logger : loggers) {
			logger.e(tag, message, tr);
		}
	}
	
	@Override
	protected void handleLogEvent(LogEvent event) {
		super.handleLogEvent(event);
		for (Logger logger : loggers) {
			logger.log(event);
		}
	}
	
}