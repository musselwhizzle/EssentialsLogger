package com.therealjoshua.essentials.logger;

import java.util.HashMap;

/**
 * The AbstractLogger is a convenience class. It has some convenience methods such as 
 * checking if a log call should actually log. Loggers may subclass this class to 
 * take advantages of such methods. 
 * 
 */
abstract public class AbstractLogger implements Logger {
	
	private int logLevel = Log.VERBOSE;
	private HashMap<String, Integer> levels;
	
	public AbstractLogger() {
		this(Log.VERBOSE);
	}
	
	public AbstractLogger(int logLevel) {
		this.logLevel = logLevel;
		levels = new HashMap<String, Integer>();
	}
	
	@Override
	public int getLogLevel() {
		return logLevel;
	}
	
	@Override
	public int getLogLevel(String tag) {
		return (!levels.containsKey(tag)) ? logLevel : levels.get(tag);
	}
	
	@Override
	public void setLogLevel(int logLevel) {
		this.logLevel = logLevel;
	}

	@Override
	public void setLogLevel(String tag, int logLevel) {
		levels.put(tag, logLevel);
	}
	
	@Override
	public boolean isLoggable(int level) {
		return (level >= logLevel);
	}
	
	@Override
	public boolean isLoggable(String tag, int level) {
		if (!isLoggable(level)) return false;
		int limit = getLogLevel(tag);
		return (level >= limit);
	}
	
	@Override
	public void v(String tag, String message) {
		if (isLoggable(tag, Log.VERBOSE)) {
			handleV(tag, message, null);
		}
	}

	@Override
	public void v(String tag, String message, Throwable tr) {
		if (isLoggable(tag, Log.VERBOSE)) {
			handleV(tag, message, tr);
		}
	}
	
	@Override
	public void d(String tag, String message) {
		if (isLoggable(tag, Log.DEBUG)) {
			handleD(tag, message, null);
		}
	}

	@Override
	public void d(String tag, String message, Throwable tr) {
		if (isLoggable(tag, Log.DEBUG)) {
			handleD(tag, message, tr);
		}
	}

	@Override
	public void i(String tag, String message) {
		if (isLoggable(tag, Log.INFO)) {
			handleI(tag, message, null);
		}
	}

	@Override
	public void i(String tag, String message, Throwable tr) {
		if (isLoggable(tag, Log.INFO)) {
			handleI(tag, message, tr);
		}
	}

	@Override
	public void w(String tag, String message) {
		if (isLoggable(tag, Log.WARN)) {
			handleW(tag, message, null);
		}
	}

	@Override
	public void w(String tag, String message, Throwable tr) {
		if (isLoggable(tag, Log.WARN)) {
			handleW(tag, message, tr);
		}
	}

	@Override
	public void e(String tag, String message) {
		if (isLoggable(tag, Log.ERROR)) {
			handleE(tag, message, null);
		}
	}

	@Override
	public void e(String tag, String message, Throwable tr) {
		if (isLoggable(tag, Log.ERROR)) {
			handleE(tag, message, tr);
		}
	}
	
	@Override
	public void log(LogEvent event) {
		if (isLoggable(event.getTag(), event.getLevel())) {
			handleLogEvent(event);
		}
	}
	
	/**
	 * A log call when the checks to isLoggable has passed
	 */
	protected void handleV(String tag, String message, Throwable tr) {
		
	}
	
	/**
	 * A log call when the checks to isLoggable has passed
	 */
	protected void handleD(String tag, String message, Throwable tr) {
		
	}
	
	/**
	 * A log call when the checks to isLoggable has passed
	 */
	protected void handleI(String tag, String message, Throwable tr) {
		
	}
	
	/**
	 * A log call when the checks to isLoggable has passed
	 */
	protected void handleW(String tag, String message, Throwable tr) {
		
	}
	
	/**
	 * A log call when the checks to isLoggable has passed
	 */
	protected void handleE(String tag, String message, Throwable tr) {
		
	}
	
	/**
	 * A log call when the checks to isLoggable has passed
	 */
	protected void handleLogEvent(LogEvent event) {
		
	}
	
	/**
	 * Creates a new LogEvent.
	 */
	protected LogEvent createLogEvent(int level, String tag, String message, Throwable tr) {
		return new LogEvent(level, tag, message, tr);
	}
	
}
