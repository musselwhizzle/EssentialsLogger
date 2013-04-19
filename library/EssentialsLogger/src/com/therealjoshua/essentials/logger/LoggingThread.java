package com.therealjoshua.essentials.logger;

import java.util.concurrent.atomic.AtomicInteger;

import android.os.HandlerThread;
import android.os.Process;

/**
 * Thread which all async log calls can go into.
 * 
 * @author Joshua
 *
 */
public class LoggingThread extends HandlerThread {
	
	private static LoggingThread instance;
	private static AtomicInteger count;
	
	private LoggingThread(String name) {
		super(name);
	}

	private LoggingThread(String name, int priority) {
		super(name, priority);
	}
	
	public static LoggingThread getInstance() {
		if (instance == null) {
			instance = new LoggingThread("Logging Thread", Process.THREAD_PRIORITY_BACKGROUND);
			count = new AtomicInteger();
		}
		
		return instance;
	};
	
	@Override
	public synchronized void start() {
		count.incrementAndGet();
		if (!isAlive()) super.start();
	}
	
	/**
	 * Only quits once all references has asked for a quit
	 */
	@Override
	public boolean quit() {
		if (count.decrementAndGet() <= 0) {
			return super.quit();
		} else {
			return false;
		}
	}
}
