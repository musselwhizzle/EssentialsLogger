package com.therealjoshua.essentials.logger;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

// http://developer.android.com/guide/components/bound-services.html
abstract public class AbstractServiceLogger extends Service implements Logger {
	
	private Logger logger;
	private final LoggingBinder binder = new LoggingBinder();
	
	public AbstractServiceLogger() {
		super();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		return START_REDELIVER_INTENT; // makes the service restart if it was killed??? Does the "connection" get called again? 
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return binder;
	}
	
	public class LoggingBinder extends Binder implements LoggerBinder {
		@Override
		public Logger getLogger() {
			return AbstractServiceLogger.this;
		}
	}
	
	@Override
	public int getLogLevel() {
		return logger.getLogLevel();
	}
	
	@Override
	public int getLogLevel(String tag) {
		return logger.getLogLevel(tag);
	}
	
	@Override
	public void setLogLevel(int logLevel) {
		logger.setLogLevel(logLevel);
	}

	@Override
	public void setLogLevel(String tag, int logLevel) {
		logger.setLogLevel(tag, logLevel);
	}

	@Override
	public boolean isLoggable(int level) {
		return logger.isLoggable(level);
	}

	@Override
	public boolean isLoggable(String tag, int level) {
		return logger.isLoggable(tag, level);
	}
	
	@Override
	public void v(String tag, String message) {
		logger.v(tag, message);
	}

	@Override
	public void v(String tag, String message, Throwable tr) {
		logger.v(tag, message, tr);
	}
	
	@Override
	public void d(String tag, String message) {
		logger.d(tag, message);
	}

	@Override
	public void d(String tag, String message, Throwable tr) {
		logger.d(tag, message, tr);
	}

	@Override
	public void i(String tag, String message) {
		logger.i(tag, message);
	}

	@Override
	public void i(String tag, String message, Throwable tr) {
		logger.i(tag, message, tr);
	}

	@Override
	public void w(String tag, String message) {
		logger.w(tag, message);
	}

	@Override
	public void w(String tag, String message, Throwable tr) {
		logger.w(tag, message, tr);
	}

	@Override
	public void e(String tag, String message) {
		logger.e(tag, message);
	}

	@Override
	public void e(String tag, String message, Throwable tr) {
		logger.e(tag, message, tr);
	}
	
	protected void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	protected Logger getLogger() {
		return logger;
	}
}
