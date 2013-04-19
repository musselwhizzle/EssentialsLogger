package com.therealjoshua.essentials.logger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;

//TODO: This is rather chatting on the GC. Why? 
/**
 * A logger to log all calls to a file.  
 */
public class FileLogger extends AbstractLogger implements Logger {
	
	private static final String TAG = "FileLogger";
	private Context context;
	private String logFileName = "logs.txt";
	private DateFormat dateFormat;
	private LogEvent recycledEvent;
	private boolean firstRun = true;
	private boolean clearPreviousLogs = true;
	
	
	public FileLogger(Context context) {
		this(context, Log.VERBOSE, true);
	}

	public FileLogger(Context context, int logLevel, boolean clearPreviousLogs) {
		super(logLevel);
		this.context = (context instanceof Service) ? context : context.getApplicationContext();
		this.clearPreviousLogs = clearPreviousLogs;
		// FIXME: I don't like the formatting
		dateFormat = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT);
		
		if (!(context instanceof Service)) {
			PackageManager pm = context.getPackageManager();
			int hasPerm = pm.checkPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, 
					context.getPackageName());
			if (hasPerm != PackageManager.PERMISSION_GRANTED) {
				throw new IllegalAccessError("Manifest file does not have permission: " +
						"android.permission.WRITE_EXTERNAL_STORAGE");
			}
		}
	}
	
	public String getLogFileName() {
		return logFileName;
	}
	
	public void setLogFileName(String fileName) {
		this.logFileName = fileName;
	}
	
	
	
	@Override
	protected void handleV(String tag, String message, Throwable tr) {
		writeLogEvent(createLogEvent(Log.VERBOSE, tag, message, tr));
	}
	
	@Override
	protected void handleD(String tag, String message, Throwable tr) {
		writeLogEvent(createLogEvent(Log.DEBUG, tag, message, tr));
	}
	
	@Override
	protected void handleI(String tag, String message, Throwable tr) {
		writeLogEvent(createLogEvent(Log.INFO, tag, message, tr));
	}
	
	@Override
	protected void handleW(String tag, String message, Throwable tr) {
		writeLogEvent(createLogEvent(Log.WARN, tag, message, tr));
	}
	
	@Override
	protected void handleE(String tag, String message, Throwable tr) {
		writeLogEvent(createLogEvent(Log.ERROR, tag, message, tr));
	}
	
	@Override
	protected void handleLogEvent(LogEvent event) {
		writeLogEvent(event);
	}
	
	/**
	 * This class is synchonrous so I can just keep reusing the same object
	 * as i don't have to worry about another call before this one is used. 
	 */
	@Override
	protected LogEvent createLogEvent(int level, String tag, String message, Throwable tr) {
		if (recycledEvent == null) recycledEvent = new LogEvent();
		recycledEvent.setLevel(level);
		recycledEvent.setTag(tag);
		recycledEvent.setMessage(message);
		recycledEvent.setThrowable(tr);
		recycledEvent.setTime(System.currentTimeMillis());
		recycledEvent.setPid(Process.myPid());
		recycledEvent.setTid(Process.myTid());
		return recycledEvent;
	}
	
	/**
	 * Determines where the log file gets written to. Subclasses can override
	 * this method to provide alternative locations
	 * 
	 * @return File of where to write the logs
	 */
	protected File getLogFile() {
		File path = getFilePath();
		if (path == null) return null;
		File file = new File(path, logFileName);
		return file;
	}
	
	/**
	 * Controls the formatting of how the event is written to the file. Subclasses
	 * can override this to provide their own formatting. 
	 * 
	 * @param event The Logged event
	 * @return A CharSequence representing the info to write to the file
	 */
	protected CharSequence formatLogEvent(LogEvent event) {
		StringBuilder sb = new StringBuilder();
		sb.append(getLevelString(event.getLevel()));
		sb.append(" | ");
		sb.append( dateFormat.format(new Date(event.getTime())) );
		sb.append(" | ");
		sb.append(event.getPid());
		sb.append(" | ");
		sb.append(event.getTid());
		sb.append(" | ");
		sb.append(event.getTag());
		sb.append(" | ");
		sb.append(event.getMessage());
		sb.append(" | ");
		if (event.getThrowable() != null) {
			sb.append(android.util.Log.getStackTraceString(event.getThrowable()));
		}
		return sb;
	}
	
	private File getFilePath() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
				return context.getExternalFilesDir(null);
			} else {
				return Environment.getExternalStorageDirectory();
			}
		} else {
			android.util.Log.w(TAG, "Could not get file to write to. " +
					"Perhaps it's not mounted or being shared?");
			return null;
		}
	}
	
	private BufferedWriter getWriter(File file) throws IOException {
		return new BufferedWriter(new FileWriter(file, (clearPreviousLogs && firstRun)));
	}
	
	private String getLevelString(int level) {
		switch (level) {
			case Log.VERBOSE: return "V";
			case Log.DEBUG: return "D";
			case Log.INFO: return "V";
			case Log.WARN: return "W";
			case Log.ERROR: return "E";
		}
		return "";
	}
	
	private void writeLogEvent(LogEvent event) {
		File file = getLogFile();
		if (file == null) return;
		
		BufferedWriter writer = null;
		try {
			writer = getWriter(file);
			writer.append(formatLogEvent(event));
			writer.newLine();
			writer.flush();
			firstRun = false;
		} catch (FileNotFoundException e) {
			android.util.Log.e(TAG, "Couldn't open file to write logs", e);
		} catch (IOException e) {
			android.util.Log.e(TAG, "File not found", e);
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					// don't need to handle this
				}
			}
		}
	}
}
