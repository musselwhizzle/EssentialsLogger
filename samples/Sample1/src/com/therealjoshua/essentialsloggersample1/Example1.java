package com.therealjoshua.essentialsloggersample1;

import com.therealjoshua.essentials.logger.Log;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


/**
 * This example shows basic logging. 
 */
public class Example1 extends Activity {
	
	private static final String TAG = "MainActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Button btn = new Button(this);
		btn.setText("Click me!");
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
				Log.e(TAG, "ERROR", tr);
			}
		});
		
		setContentView(btn);
	}
}
