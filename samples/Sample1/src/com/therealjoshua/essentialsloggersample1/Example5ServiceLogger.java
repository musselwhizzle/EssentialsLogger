package com.therealjoshua.essentialsloggersample1;

import com.therealjoshua.essentials.logger.AbstractMessengerServiceLogger;
import com.therealjoshua.essentials.logger.ErrorReportNotificationBarLogger;

public class Example5ServiceLogger extends AbstractMessengerServiceLogger {
	
	public Example5ServiceLogger() {
		super();
		setLogger(new ErrorReportNotificationBarLogger(this));
	}
	
}