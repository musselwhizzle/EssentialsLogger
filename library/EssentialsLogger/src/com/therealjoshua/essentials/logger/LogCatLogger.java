package com.therealjoshua.essentials.logger;

/**
 * This is an adapter class. It simply wraps the standard android.util.Log calls 
 * into the interface Logger. 
 * 
 */
public class LogCatLogger extends AbstractLogger implements Logger {
	
	public LogCatLogger() {
		this(Log.VERBOSE);
	}
	
	public LogCatLogger(int logLevel) {
		super(logLevel);
	}
	
	@Override
	protected void handleV(String tag, String message, Throwable tr) {
		android.util.Log.v(tag, message, tr);
	}
	
	@Override
	protected void handleD(String tag, String message, Throwable tr) {
		android.util.Log.d(tag, message, tr);
	}
	
	@Override
	protected void handleI(String tag, String message, Throwable tr) {
		android.util.Log.i(tag, message, tr);
	}
	
	@Override
	protected void handleW(String tag, String message, Throwable tr) {
		android.util.Log.w(tag, message, tr);
	}
	
	@Override
	protected void handleE(String tag, String message, Throwable tr) {
		android.util.Log.e(tag, message, tr);
	}
	
	@Override
	protected void handleLogEvent(LogEvent event) {
		switch (event.getLevel()) {
		case Log.VERBOSE: handleI(event.getTag(), event.getMessage(), event.getThrowable()); break;
		case Log.DEBUG: handleD(event.getTag(), event.getMessage(), event.getThrowable()); break;
		case Log.INFO: handleI(event.getTag(), event.getMessage(), event.getThrowable()); break;
		case Log.WARN: handleW(event.getTag(), event.getMessage(), event.getThrowable()); break;
		case Log.ERROR: handleE(event.getTag(), event.getMessage(), event.getThrowable()); break;
		}
	}
}
