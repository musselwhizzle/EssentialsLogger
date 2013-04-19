package com.therealjoshua.essentials.logger;

/**
 * Interface for Binder Services to use to get a reference to the Logger. 
 */
public interface LoggerBinder {
	
	/**
	 * Returns a reference to the Logger.
	 * 
	 * @return Logger
	 */
	public Logger getLogger();
}