package com.therealjoshua.essentialsloggersample1;

import com.therealjoshua.essentials.logger.AbstractBinderServiceLogger;
import com.therealjoshua.essentials.logger.ErrorReportNotificationBarLogger;

public class Example7ServiceLogger extends AbstractBinderServiceLogger {
	
	public Example7ServiceLogger() {
		super();
		setLogger(new ErrorReportNotificationBarLogger(this));
	}
	
}