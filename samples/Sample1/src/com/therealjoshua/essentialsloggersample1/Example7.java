package com.therealjoshua.essentialsloggersample1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.therealjoshua.essentials.logger.Log;
import com.therealjoshua.essentials.logger.LoggerServiceBinderProxy;

/**
 * This example shows logging via a service in a the same service as your app. To do so we need 2 things: 
 * 1) a proxy to the service which we add to the Log. If your service is a remote service using
 * a messenger you can use LoggerServiceRemoteProxy. No need to subclass or anything. If it is a service within
 * the same process using an IBinder you can use LoggerServiceProxy
 * and 
 * 2) the service itself. This service should be your own subclass of AbstractMessengerServiceLogger
 * which specifies what types of loggers to use. 
 * 
 * In this example the remote logger only "logs" errors and does so by putting them in the notification tray
 * so that either QA or perhaps your users can send you a bug report. 
 */
public class Example7 extends Activity {
	
	private static final String TAG = "MainActivity";
	private LoggerServiceBinderProxy proxy;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		proxy = new LoggerServiceBinderProxy();
		Log.addLogger(proxy);
		boolean success = proxy.bindService(Example7.this, new Intent(Example7.this, Example7ServiceLogger.class));
		Log.d(TAG, "Did service start? success = " + success);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		setContentView(ll);
		
		Button btn = new Button(this);
		btn.setText("Generate Logs");
		btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Throwable tr = null;
				try {
					throw new IllegalArgumentException("Testing");
				} catch (Exception e) {
					tr = e;
				}
				Log.v(TAG, "VERBOSE");
				Log.d(TAG, "DEBUG");
				Log.i(TAG, "INFO");
				Log.w(TAG, "WARN", tr);
				Log.e(TAG, "ERROR. This is a longer line of text to see if the text will wrap lines", tr);
			}
		});
		ll.addView(btn);
	}
}
