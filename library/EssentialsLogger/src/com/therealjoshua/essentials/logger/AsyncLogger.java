package com.therealjoshua.essentials.logger;

import com.therealjoshua.essentials.utils.ObjectFactory;
import com.therealjoshua.essentials.utils.ObjectPool;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;

/**
 * AsyncLogger is a convenient way to make a Logger asynchronous. Simple pass the logger
 * (such as FileLogger or DatabaseLogger) into the constructor to make it asyn.
 */
public class AsyncLogger extends AbstractLogger {
	private static final String TAG = AsyncLogger.class.getSimpleName();
	private Logger logger;
	private Handler handler;
	private HandlerThread handlerThread;
	private ObjectPool<LogEvent> eventPool;
	
	public AsyncLogger() {
		this(null);
	}
	
	public AsyncLogger(Logger logger) {
		this.logger = logger;
		handlerThread = LoggingThread.getInstance();
		handlerThread.start();
		handler = new Handler(handlerThread.getLooper());
		eventPool = new ObjectPool<LogEvent>(eventFactory, 10);
	}
	
	public void quit() {
		try {
			handlerThread.quit();
		} catch (Exception e) {
			android.util.Log.e(TAG, "Cound not stop HandlerThread properly", e);
		}
	}
	
	public void setLogger(Logger logger) {
		this.logger = logger;
	}
	
	public Logger getLogger() {
		return logger;
	}
	
	@Override
	protected void handleV(final String tag, final String message, final Throwable tr) {
		handleLogEvent(createLogEvent(Log.VERBOSE, tag, message, tr));
	}
	
	@Override
	protected void handleD(final String tag, final String message, final Throwable tr) {
		handleLogEvent(createLogEvent(Log.DEBUG, tag, message, tr));
	}
	
	@Override
	protected void handleI(final String tag, final String message, final Throwable tr) {
		handleLogEvent(createLogEvent(Log.INFO, tag, message, tr));
	}
	
	@Override
	protected void handleW(final String tag, final String message, final Throwable tr) {
		handleLogEvent(createLogEvent(Log.WARN, tag, message, tr));
	}
	
	@Override
	protected void handleE(final String tag, final String message, final Throwable tr) {
		handleLogEvent(createLogEvent(Log.ERROR, tag, message, tr));
	}
	
	@Override
	protected void handleLogEvent(final LogEvent event) {
		handler.post(new Runnable() {
			@Override
			public void run() {
				logger.log(event);
				synchronized (eventPool) {
					eventPool.recycle(event);
				}
			}
		});
	}
	
	@Override
	protected LogEvent createLogEvent(int level, String tag, String message, Throwable tr) {
		synchronized (eventPool) {
			LogEvent event = eventPool.getObject();
			event.setLevel(level);
			event.setTag(tag);
			event.setMessage(message);
			event.setThrowable(tr);
			event.setTime(System.currentTimeMillis());
			event.setPid(Process.myPid());
			event.setTid(Process.myTid());
			return event;
		}
	}
	
	private ObjectFactory<LogEvent> eventFactory = new ObjectFactory<LogEvent>() {
		@Override
		public LogEvent create() {
			return new LogEvent();
		}

		@Override
		public void recycle(LogEvent object) { }
	};
}
