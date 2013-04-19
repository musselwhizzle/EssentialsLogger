package com.therealjoshua.essentials.logger;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

//http://developer.android.com/guide/components/bound-services.html
/**
 * The AbstractMessengerServiceLogger should be used by subclass it and setting a Logger. 
 * That's a bout it. This class takes care of setting up and handling 
 * the incoming messages. 
 */
abstract public class AbstractMessengerServiceLogger extends Service implements Logger {
	
	public static final int LOG = 1;
	public static final String DATA_EVENT = "dataEvent";
	
	private Logger logger;
	private final Messenger messenger = new Messenger(new LoggingHandler());
	
	public AbstractMessengerServiceLogger() {
		this(null);
	}
	
	public AbstractMessengerServiceLogger(Logger logger) {
		super();
		setLogger(logger);
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return messenger.getBinder();
	}
	
	@SuppressLint("HandlerLeak")
	private class LoggingHandler extends Handler {
		@Override
		public void handleMessage(Message msg) {
			int what = msg.what;
			switch (what) {
			case LOG: 
				final Bundle bundle = msg.getData();
				bundle.setClassLoader(getClassLoader());
				LogEvent event = (LogEvent)bundle.getParcelable(DATA_EVENT);
				log(event);
				break;
			}
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
	
	@Override
	public void log(LogEvent event) {
		logger.log(event);
	}
	
	protected void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	protected Logger getLogger() {
		return logger;
	}
	
	
}
