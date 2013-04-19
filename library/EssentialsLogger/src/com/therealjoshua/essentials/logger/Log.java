package com.therealjoshua.essentials.logger;

/**
 * API for sending log output. Use this as a replacement for the android.util.Log class. This 
 * API memicks Android native Log but add extra features such as where to direct the log output
 * and the ability to set log levels are run time. 
 *
 * <p>Generally, use the Log.v() Log.d() Log.i() Log.w() and Log.e()
 * methods.
 *
 * <p>The order in terms of verbosity, from least to most is
 * ERROR, WARN, INFO, DEBUG, VERBOSE.  Verbose should never be compiled
 * into an application except during development.  Debug logs are compiled
 * in but stripped at runtime.  Error, warning and info logs are always kept.
 *
 * <p><b>Tip:</b> A good convention is to declare a <code>TAG</code> constant
 * in your class:
 *
 * <pre>private static final String TAG = "MyActivity";</pre>
 *
 * and use that in subsequent calls to the log methods.
 * </p>
 */
public class Log {

	public static final int VERBOSE = android.util.Log.VERBOSE;
	public static final int DEBUG = android.util.Log.DEBUG;
	public static final int INFO = android.util.Log.INFO;
	public static final int WARN = android.util.Log.WARN;
	public static final int ERROR = android.util.Log.ERROR;
	public static final int ASSERT = android.util.Log.ASSERT;
	public static final int SILENT = Integer.MAX_VALUE;

	@SuppressWarnings("unused")
	private static final String TAG = "Log";
	private static LogCatLogger logCatLogger;
	private static boolean isInit = false;
	private static CompositeLogger loggers;

	static {
		init();
		
		logCatLogger = new LogCatLogger();
		loggers = new CompositeLogger();
		loggers.addLogger(logCatLogger);
	}
	
	private static void init() {
		init(true);
	}
	
	private static void init(boolean useUncaughtErrorHandler) {
		if (true) {
			initCrashHandler();
		}
	}
	
	/**
	 * As a convenience the logcat logger is automatically added so there is no need to init the Log util.
	 * Use this method to get a reference to the logger or to remove it if you don't wish to use it.  
	 * 
	 * @return A LogCatLogger instance
	 */
	public static LogCatLogger getLogCatLogger() {
		return logCatLogger;
	}
	
	/**
	 * Allow you to add additionally Loggers to the static Log. Each subsiquent call will be tracked by all loggers
	 * 
	 * @param logger An additional Logger to add and be used when a Log method is called. 
	 */
	public static void addLogger(Logger logger) {
		loggers.addLogger(logger);
	}
	
	/**
	 * Removes a logger from the Log
	 * 
	 * @param logger The reference to the logger in the collection
	 */
	public static void removeLogger(Logger logger) {
		loggers.removeLogger(logger);
	}
	
	/**
	 * The log level that all log call must exceed in order to be logged.
	 * 
	 * @param logLevel the the minimum log level this instance will log
	 */
	public static int getLogLevel() {
		return loggers.getLogLevel();
	}
	
	/**
	 * The level that the specific tag is set to log.
	 * 
	 * @param logLevel the the minimum log level this instance will log
	 */
	public static int getLogLevel(String tag) {
		return loggers.getLogLevel(tag);
	}
	
	/**
	 * Sets the log level that all subsequent log calls must be equal to or greater. For instance, 
	 * setting this level to {@link #WARN} causes all log call to VERBOSE, DEBUG, and INFO to be ignored. 
	 * Only WARN and ERROR will log. If you do not want all calls to log set the level to OFF
	 * 
	 * @param logLevel the the minimum log level this instance will log
	 */
	public static void setLogLevel(int logLevel) {
		loggers.setLogLevel(logLevel);
	}
	
	/**
	 * Sets the log level that all subsequent log calls must be equal to or greater for the 
	 * supplied tag. Note, the level must all be greater than {@link getLogLevel()}.
	 * 
	 * @param logLevel the the minimum log level this instance will log
	 */
	public static void setLogLevel(String tag, int logLevel) {
		loggers.setLogLevel(tag, logLevel);
	}
	
	/**
	 * The event may be loggable at this level, but the composite loggers may not choose
	 * to handle it. Call willLog() to see if any logger will actually log the call. 
	 * 
	 * @param level
	 * @return
	 */
	public static boolean isLoggable(int level) {
		return loggers.isLoggable(level);
	}
	
	/**
	 * The event may be loggable at this level, but the composite loggers may not choose
	 * to handle it. Call willLog() to see if any logger will actually log the call. 
	 * 
	 * @param level
	 * @return
	 */
	public static boolean isLoggable(String tag, int level) {
		return loggers.isLoggable(tag, level);
	}
	
	/**
	 * Checks to see if any loggers will actually handle the log request
	 * 
	 * @param level
	 * @return
	 */
	public static boolean willLog(int level) {
		return loggers.willLog(level);
	}
	
	/**
	 * Checks to see if any loggers will actually handle the log request
	 * 
	 * @param level
	 * @return
	 */
	public static boolean willLog(String tag, int level) {
		return loggers.willLog(tag, level);
	}
	
	/**
     * Send a {@link #VERBOSE} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static void v(String tag, String message) {
		loggers.v(tag, message);
	}
	
	/**
     * Send a {@link #VERBOSE} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public static void v(String tag, String message, Throwable tr) {
		loggers.v(tag, message, tr);
	}
	
	/**
     * Send a {@link #DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static void d(String tag, String message) {
		loggers.d(tag, message);
	}
	
	/**
     * Send a {@link #DEBUG} log message.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public static void d(String tag, String message, Throwable tr) {
		loggers.d(tag, message, tr);
	}
	
	/**
     * Send a {@link #INFO} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static void i(String tag, String message) {
		loggers.i(tag, message);
	}
	
	/**
     * Send a {@link #INFO} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public static void i(String tag, String message, Throwable tr) {
		loggers.i(tag, message, tr);
	}
	
	/**
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static void w(String tag, String message) {
		loggers.w(tag, message);
	}
	
	/**
     * Send a {@link #WARN} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public static void w(String tag, String message, Throwable tr) {
		loggers.w(tag, message, tr);
	}
	
	/**
     * Send a {@link #ERROR} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
	public static void e(String tag, String message) {
		loggers.e(tag, message);
	}
	
	/**
     * Send a {@link #ERROR} log message and log the exception.
     * @param tag Used to identify the source of a log message.  It usually identifies
     *        the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     * @param tr An exception to log
     */
	public static void e(String tag, String message, Throwable tr) {
		loggers.e(tag, message, tr);
	}
	
	/**
     * Handy function to get a loggable stack trace from a Throwable
     * @param tr An exception to log
     */
	public static String getStackTraceString(Throwable tr) {
		return android.util.Log.getStackTraceString(tr);
	}
	
	private static void initCrashHandler() {
		if (!isInit) {
			isInit = true;
			Thread.UncaughtExceptionHandler defaultUncaughtHandler = Thread.getDefaultUncaughtExceptionHandler();
			Thread.setDefaultUncaughtExceptionHandler(new LogUncaughtExceptionHandler(defaultUncaughtHandler));
		}
	}
}