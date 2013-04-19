package com.therealjoshua.essentials.logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

/**
 * The AbstractLoggerServiceProxy is combines some logic for creating a maintain either a 
 * binder service or a messenger service. The subclasses LoggerServiceBinderProxy and LoggerServiceRemoteProxy
 * provide the details for handling each specific type. 
 */
abstract public class AbstractLoggerServiceProxy extends AbstractLogger {
	
	private ServiceConnection serviceConnection;
	private List<LogEvent> events;
	
	private boolean isConnected = false;
	public boolean isConnected() {
		return isConnected;
	}
	public AbstractLoggerServiceProxy() {
		this(Log.VERBOSE);
	}
	
	public AbstractLoggerServiceProxy(int logLevel) {
		super(logLevel);
		events = Collections.synchronizedList(new ArrayList<LogEvent>());
		
		serviceConnection = new ServiceConnection() {
			// this is called when the service crashes or frees. not when the client unbinds. 
			@Override
			public void onServiceDisconnected(ComponentName name) {
				isConnected = false;
				AbstractLoggerServiceProxy.this.onServiceDisconnected(name);
			}
			
			@Override
			public void onServiceConnected(ComponentName name, IBinder service) {
				isConnected = true;
				AbstractLoggerServiceProxy.this.onServiceConnected(name, service);
				
				// send all the queued events to the service
				for (LogEvent event : events) {
					sendToService(event);
				}
				events.clear(); // Umm...there's problem more effecient way to do this. 
			}
		};
	}
	
	protected void onServiceDisconnected(ComponentName name) {
		
	}
	
	protected void onServiceConnected(ComponentName name, IBinder service) {
		
	}
	
	protected List<LogEvent> getEvents() {
		return events;
	}
	
	public boolean bindService(Context context, Intent intent) {
		return context.getApplicationContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
	}
	
	/**
	 * Proxys the unbindService because if someone calls unbind, there is no call to onServiceDisconnected, 
	 * and it's unknown that the service stopped. 
	 * @param context
	 */
	public void unbindService(Context context) {
		context.getApplicationContext().unbindService(serviceConnection);
		isConnected = false;
	}
	
	@Override
	protected void handleV(String tag, String message, Throwable tr) {
		LogEvent event = createLogEvent(Log.VERBOSE, tag, message, tr);
		handleLogEvent(event);
	}
	
	@Override
	protected void handleD(String tag, String message, Throwable tr) {
		LogEvent event = createLogEvent(Log.DEBUG, tag, message, tr);
		handleLogEvent(event);
	}
	
	@Override
	protected void handleI(String tag, String message, Throwable tr) {
		LogEvent event = createLogEvent(Log.INFO, tag, message, tr);
		handleLogEvent(event);
	}
	
	@Override
	protected void handleW(String tag, String message, Throwable tr) {
		LogEvent event = createLogEvent(Log.WARN, tag, message, tr);
		handleLogEvent(event);
	}
	
	@Override
	protected void handleE(String tag, String message, Throwable tr) {
		LogEvent event = createLogEvent(Log.ERROR, tag, message, tr);
		handleLogEvent(event);
	}
	
	@Override
	protected void handleLogEvent(LogEvent event) {
		if (isConnected) {
			sendToService(event);
		} else {
			events.add(event);
		}
	}
	
	abstract protected void sendToService(LogEvent event);
	
}
