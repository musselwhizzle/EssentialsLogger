package com.therealjoshua.essentials.logger;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;

/**
 * This class is used as a Proxy to allow communication with Binder Service running in the same 
 * process
 * @hide - this class is incomplete right now.
 */
public class LoggerServiceProxy extends AbstractLoggerServiceProxy {
	
	private Logger serviceLogger;
	
	public LoggerServiceProxy(int logLevel) {
		super(logLevel);
	}
	
	@Override
	protected void onServiceDisconnected(ComponentName name) {
		super.onServiceDisconnected(name);
		serviceLogger = null;
	}
	
	@Override
	protected void onServiceConnected(ComponentName name, IBinder service) {
		super.onServiceConnected(name, service);
		serviceLogger = ((LoggerBinder)service).getLogger();
	}
	
	@Override
	public void unbindService(Context context) {
		super.unbindService(context);
		serviceLogger = null;
	}
	
	
	@Override
	protected void sendToService(LogEvent event) {
		serviceLogger.log(event);
//		int level = event.getLevel(); 
//		String tag = event.getTag();
//		String message = event.getMessage(); 
//		Throwable tr = event.getThrowable();
//		
//		tag += " Proxy";
//		if (!isLoggable(tag, level)) return;
//		switch (level) {
//		case Log.VERBOSE:
//			if (tr == null)
//				serviceLogger.v(tag, message);
//			else
//				serviceLogger.v(tag, message, tr);
//			break;
//		case Log.DEBUG:
//			if (tr == null)
//				serviceLogger.d(tag, message);
//			else
//				serviceLogger.d(tag, message, tr);
//			break;
//		case Log.INFO:
//			if (tr == null)
//				serviceLogger.i(tag, message);
//			else
//				serviceLogger.i(tag, message, tr);
//			break;
//		case Log.WARN:
//			if (tr == null)
//				serviceLogger.w(tag, message);
//			else
//				serviceLogger.w(tag, message, tr);
//			break;
//		case Log.ERROR:
//			if (tr == null)
//				serviceLogger.e(tag, message);
//			else
//				serviceLogger.e(tag, message, tr);
//			break;
//		case Log.ASSERT:
//			break;
//		}
	}
	
}
