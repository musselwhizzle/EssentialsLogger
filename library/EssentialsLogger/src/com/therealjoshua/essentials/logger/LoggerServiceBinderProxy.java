package com.therealjoshua.essentials.logger;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;

/**
 * This class is used as a Proxy to allow communication with Binder Service running in the same 
 * process
 * @hide - this class is incomplete right now.
 */
public class LoggerServiceBinderProxy extends AbstractLoggerServiceProxy {
	
	private Logger serviceLogger;
	
	public LoggerServiceBinderProxy() {
		this(Log.VERBOSE);
	}
	
	public LoggerServiceBinderProxy(int logLevel) {
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
	}
}
