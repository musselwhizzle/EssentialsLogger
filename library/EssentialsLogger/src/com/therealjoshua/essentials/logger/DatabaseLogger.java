package com.therealjoshua.essentials.logger;

import java.util.List;

import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Process;

/**
 * A logger to log all calls to a sqlite database. 
 */
public class DatabaseLogger extends AbstractLogger implements Logger {
	
	private static final String CLASS_TAG = "DatabaseLogger";
	private String databaseName = "Logs";
	private Context appContext;
	private EventsDao dao;
	private LogEvent recycledEvent;
	
	public DatabaseLogger(Context context) {
		this(context, Log.VERBOSE, true);
	}
	
	/**
	 * 
	 * @param context Regular ol context. Will internally get the app context. 
	 * @param clearPreviousLogs delete the logs previously in the db
	 */
	public DatabaseLogger(Context context, int logLevel, boolean clearPreviousLogs) {
		super(logLevel);
		this.appContext = (context instanceof Service) ? context : context.getApplicationContext();
		dao = new EventsDao(appContext);
		
		if (clearPreviousLogs && !(context instanceof Service)) {
			appContext.deleteDatabase(databaseName);
		}
	}
	
	@Override
	protected void handleV(String tag, String message, Throwable tr) {
		dao.insert(createLogEvent(Log.VERBOSE, tag, message, tr));
	}
	
	@Override
	protected void handleD(String tag, String message, Throwable tr) {
		dao.insert(createLogEvent(Log.DEBUG, tag, message, tr));
	}
	
	@Override
	protected void handleI(String tag, String message, Throwable tr) {
		dao.insert(createLogEvent(Log.INFO, tag, message, tr));
	}
	
	@Override
	protected void handleW(String tag, String message, Throwable tr) {
		dao.insert(createLogEvent(Log.WARN, tag, message, tr));
	}
	
	@Override
	protected void handleE(String tag, String message, Throwable tr) {
		dao.insert(createLogEvent(Log.ERROR, tag, message, tr));
	}
	
	@Override
	protected void handleLogEvent(LogEvent event) {
		dao.insert(event);
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
	
	private class DatabaseHelper extends SQLiteOpenHelper {
		
		private static final int DATABASE_VERSION = 1;

		public DatabaseHelper(Context context) {
			super(context, databaseName, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase database) {
			final String table = "CREATE TABLE " + EventsDao.TABLE_NAME + " (" +
				EventsDao._ID + " integer primary key, " +
				EventsDao.LEVEL + " integer, " + 
				EventsDao.TIME + " text, " +
				EventsDao.PID + " text, " +
				EventsDao.TID + " text, " +
				EventsDao.TAG + " text, " +
				EventsDao.MESSAGE + " text" +
				")";
			database.execSQL(table);
		}

		@Override
		public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

		}
	}
	
	private class EventsDao {
		private static final String TABLE_NAME = "events";
		private static final String _ID = "_id";
		private static final String LEVEL = "level";
		private static final String TIME = "systime";
		private static final String PID = "pid";
		private static final String TID = "tid";
		private static final String TAG = "tag";
		private static final String MESSAGE = "message";
		
		private Context context;
		
		private EventsDao(Context context) {
			this.context = context;
		}
		
		public void insert(LogEvent event) {
			SQLiteDatabase db = null;
			try {
				db = new DatabaseHelper(context).getWritableDatabase();
			} catch (Exception e) {
				android.util.Log.e(CLASS_TAG, "Couldn't open database", e);
				return;
			}
			ContentValues values = new ContentValues();
			try {
				values.put(LEVEL, event.getLevel());
				values.put(TIME, event.getTime());
				values.put(PID, event.getPid());
				values.put(TID, event.getTid());
				values.put(TAG, event.getTag());
				String msg = event.getMessage();
				if (event.getThrowable() != null) {
					msg += "\n" + android.util.Log.getStackTraceString(event.getThrowable());
				}
				values.put(MESSAGE, msg);
				db.insert(TABLE_NAME, null, values);
			}
			catch (Exception e) {
				android.util.Log.e(CLASS_TAG, "Error inserting", e);
			}
			finally {
				db.close();
			}
		}
		
		@SuppressWarnings("unused")
		public void insert(List<LogEvent> events) {
			SQLiteDatabase db = new DatabaseHelper(context).getWritableDatabase();
			ContentValues values = new ContentValues();
			db.beginTransaction();
			try {
				for (int i=0; i<events.size(); i++) {
					LogEvent event = events.get(i);
					values.put(LEVEL, event.getLevel());
					values.put(TIME, event.getTime());
					values.put(PID, event.getPid());
					values.put(TID, event.getTid());
					values.put(TAG, event.getTag());
					values.put(MESSAGE, event.getMessage());
					
					db.insert(TABLE_NAME, null, values);
					db.yieldIfContendedSafely();
				}
				db.setTransactionSuccessful();
			}
			catch (Exception e) {
				android.util.Log.e(CLASS_TAG, "Error inserting", e);
			}
			finally {
				db.endTransaction();
				db.close();
			}
		}
	}
}