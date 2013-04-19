package com.therealjoshua.essentials.logger;

import android.os.Handler;

/**
 * This class is used to catch an Exceptions which were not handled and is about the crash the app.
 * We first let our LoggingThread finish and then pass on the error to Android's default error
 * handling so it crashes the app and logs the call. 
 * 
 */
public class LogUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

	private Thread.UncaughtExceptionHandler defaultHandler;
	public Thread.UncaughtExceptionHandler getDefaultHandler() {
		return defaultHandler;
	}

	public LogUncaughtExceptionHandler(Thread.UncaughtExceptionHandler defaultHandler) {
		this.defaultHandler = defaultHandler;
	}

	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		try {
			handlePendingLogs(thread, ex);
		} catch (Exception e) {
			
		} finally {
			// this should be the original Android handler
			bubbleError(thread, ex);
		}
		
		// one way to kill the process.
		// android.os.Process.killProcess(android.os.Process.myPid());
		// System.exit(10);
	}

	private void handlePendingLogs(Thread thread, Throwable ex) {
		// turn off logcat logger as default handler will automatically log it.
		Log.getLogCatLogger().setLogLevel(Log.SILENT);
		Log.getLogCatLogger().setLogLevel("Log", Log.SILENT);
		Log.e("Log", "FATAL EXCEPTION", ex);
		Log.setLogLevel(Log.SILENT); // no more log calls. (some could still get through with a custom tag)
	}

	private void bubbleError(Thread thread, Throwable ex) {
		// If we call the default handler immediately our LoggingThread will immediately end
		// so Let's either make all async log calls a service in a process or 
		// just post the default handler to the end of our logging call. 

		// post the defaultHandler to the end of our
		LoggingThread lt = LoggingThread.getInstance();
		if (lt.isAlive()) {
			Handler h = new Handler(lt.getLooper());
			final Thread errorThread = thread;
			final Throwable errorEx = ex;
			h.post(new Runnable() {
				@Override
				public void run() {
					defaultHandler.uncaughtException(errorThread, errorEx);
				}
			});
		} else {
			defaultHandler.uncaughtException(thread, ex);
		}
	}
}
