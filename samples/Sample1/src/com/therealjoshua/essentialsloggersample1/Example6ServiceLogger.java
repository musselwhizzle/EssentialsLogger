package com.therealjoshua.essentialsloggersample1;

import android.util.Log;

import com.therealjoshua.essentials.logger.AbstractMessengerServiceLogger;
import com.therealjoshua.essentials.logger.CompositeLogger;
import com.therealjoshua.essentials.logger.DatabaseLogger;
import com.therealjoshua.essentials.logger.ErrorReportNotificationBarLogger;
import com.therealjoshua.essentials.logger.FileLogger;
import com.therealjoshua.essentials.logger.LogCatLogger;

public class Example6ServiceLogger extends AbstractMessengerServiceLogger {
	
	public Example6ServiceLogger() {
		super();
		CompositeLogger composite = new CompositeLogger();
		composite.addLogger(new LogCatLogger(Log.DEBUG));
		composite.addLogger(new FileLogger(this, Log.WARN, true));
		composite.addLogger(new DatabaseLogger(this, Log.INFO, true));
		composite.addLogger(new ErrorReportNotificationBarLogger(this));
		setLogger(composite);
	}
	
}