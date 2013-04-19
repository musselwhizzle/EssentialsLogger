package com.therealjoshua.essentials.logger;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;

/**
 * This class is used as a Proxy to allow communication with a Remote Messenger Service 
 * In other words, a Service running on another process (AIDL)
 *
 */
public class LoggerServiceRemoteProxy extends AbstractLoggerServiceProxy {
	
	private static final String TAG = LoggerServiceRemoteProxy.class.getSimpleName();
	private Messenger messenger;
	
	public LoggerServiceRemoteProxy() {
		this(Log.VERBOSE);
	}
	
	public LoggerServiceRemoteProxy(int logLevel) {
		super(logLevel);
	}
	
	@Override
	public void unbindService(Context context) {
		super.unbindService(context);
		messenger = null;
	}
	
	@Override
	protected void onServiceDisconnected(ComponentName name) {
		super.onServiceDisconnected(name);
		messenger = null;
	}
	
	@Override
	protected void onServiceConnected(ComponentName name, IBinder service) {
		super.onServiceConnected(name, service);
		messenger = new Messenger(service);
	}
	
	@Override
	protected void sendToService(LogEvent event) {
		Message msg = Message.obtain(null, AbstractMessengerServiceLogger.LOG);
		msg.getData().putParcelable(AbstractMessengerServiceLogger.DATA_EVENT, event);
		try {
			messenger.send(msg);
		} catch (RemoteException e) {
			getEvents().add(event);
			android.util.Log.w(TAG, "Error sending log", e);
		}
	}
	
}