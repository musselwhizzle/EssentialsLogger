package com.therealjoshua.essentials.logger;

public interface Logger {
	
	/**
	 * The log level that all log call must exceed in order to be logged.
	 * 
	 * @param logLevel the the minimum log level this instance will log
	 */
	public int getLogLevel();
	
	/**
	 * The level that the specific tag is set to log.
	 * 
	 * @param logLevel the the minimum log level this instance will log
	 */
	public int getLogLevel(String tag);
	
	/**
	 * Sets the log level that all subsequent log calls must be equal to or greater for the 
	 * supplied tag. Note, the level must all be greater than {@link getLogLevel()}.
	 * 
	 * @param logLevel the the minimum log level this instance will log
	 */
	public void setLogLevel(String tag, int logLevel);
	
	/**
	 * Sets the log level that all subsequent log calls must be equal to or greater. For instance, 
	 * setting this level to {@link #WARN} causes all log call to VERBOSE, DEBUG, and INFO to be ignored. 
	 * Only WARN and ERROR will log. If you do not want all calls to log set the level to OFF
	 * 
	 * @param logLevel the the minimum log level this instance will log
	 */
	public void setLogLevel(int logLevel);
	
	/**
	 * Checks to see whether or not a log for the specified tag is loggable at the specified level. 
	 * 
	 * @param tag The tag to check.
     * @return Whether or not that this is allowed to be logged.
	 */
	public boolean isLoggable (int level);
	
	/**
	 * Checks to see whether or not a log for the specified tag is loggable at the specified level. 
	 * The level may not be less than the {@link #getLogLevel()}.
	 * 
	 * @param tag The tag to check.
     * @param level The level to check.
     * @return Whether or not that this is allowed to be logged.
	 */
	public boolean isLoggable (String tag, int level);
	
	/**
     * Send a {@link #VERBOSE} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public void v(String tag, String message);
	
	/**
     * Send a {@link #VERBOSE} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public void v(String tag, String message, Throwable tr);
	
	/**
     * Send a {@link #DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public void d(String tag, String message);
	
	/**
     * Send a {@link #DEBUG} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public void d(String tag, String message, Throwable tr);
	
	/**
     * Send an {@link #INFO} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public void i(String tag, String message);
	
	/**
     * Send a {@link #INFO} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public void i(String tag, String message, Throwable tr);
	
	/**
     * Send a {@link #WARN} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public void w(String tag, String message);
	
	/**
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public void w(String tag, String message, Throwable tr);
	
	/**
     * Send an {@link #ERROR} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public void e(String tag, String message);
	
	/**
     * Send a {@link #ERROR} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public void e(String tag, String message, Throwable tr);
	
	/**
     * Send a {@link #LogEvent} log message which has all the event information already populated
     * This is useful for when queuing log but wanting the original time/pid/etc of the log event
     * and not when it finally got logged from the queue. 
     * 
     * If the logger does something with the 
     * event object asynchronously in this method, it should make it's own copy as the event object
     * may be pooled and it's value could change. 
     * 
     * @param event The LogEvent containing the information to log
     */
	public void log(LogEvent event);
	
}