package com.therealjoshua.essentials.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.NotificationCompat;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.StyleSpan;

/**
 * A rather unique loggers, this logger only logs Log.ERROR and does so by sending it to the 
 * NotificationBar which allows users to email the error report. If you'd like to customize
 * the look and actions of the notification, implement your own version of 
 * ErrorReportNotificationBarLogger.NotificationBuilder and set it to the logger via the
 * <method>setNotificationBuilder</method>.
 */
public class ErrorReportNotificationBarLogger extends AbstractLogger implements Logger {
	
	public static interface NotificationBuilder {
		public Notification createNotification(LogEvent event);
		public int getNotificationId();
	}
	
	@SuppressWarnings("unused")
	private static final String TAG = ErrorReportNotificationBarLogger.class.getSimpleName();
	private Context context;
	private NotificationBuilder notificationBuilder;
	
	public ErrorReportNotificationBarLogger(Context context) {
		super(Log.ERROR);
		this.context = (context instanceof Service) ? context : context.getApplicationContext();
		notificationBuilder = new DefaultNotificationBuilder(context);
	}
	
	public void setNotificationBuilder(NotificationBuilder notificationBuilder) {
		this.notificationBuilder = notificationBuilder;
	}
	
	@Override
	public void setLogLevel(int logLevel) {
		if (logLevel < Log.ERROR) throw new IllegalArgumentException("logLevel must be at least Log.ERROR");
		super.setLogLevel(logLevel);
	}
	
	// null out some methods
	@Override protected void handleV(String tag, String message, Throwable tr) { }
	@Override protected void handleD(String tag, String message, Throwable tr) { }	
	@Override protected void handleI(String tag, String message, Throwable tr) { }
	@Override protected void handleW(String tag, String message, Throwable tr) { }
	
	@Override
	protected void handleE(String tag, String message, Throwable tr) {
		addNotification(createLogEvent(Log.ERROR, tag, message, tr));
	}
	
	@Override
	protected void handleLogEvent(LogEvent event) {
		if (event.getLevel() == Log.ERROR) {
			addNotification(event);
		}
	}
	
	private void addNotification(LogEvent event) {
		Notification noti = notificationBuilder.createNotification(event);
		int notiId = notificationBuilder.getNotificationId();
		
		// add it to the notification bar
		NotificationManager manager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(notiId, noti);
	}
	
	public static class DefaultNotificationBuilder implements NotificationBuilder {
		private int notificationId = 0;
		private ArrayList<LogEvent> events;
		private DateFormat format = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.SHORT, SimpleDateFormat.SHORT);
		private Context context;
		private int notificationIconId = android.R.drawable.stat_notify_error;
		private String emailAddress;
		
		public DefaultNotificationBuilder(Context context) {
			this.context = context;
			events = new ArrayList<LogEvent>();
		}
		
		public void clearEvents() {
			events.clear();
		}
		
		public String getEmailAddress() {
			return emailAddress;
		}
		
		public void setEmailAddress(String emailAddress) {
			this.emailAddress = emailAddress;
		}
		
		/*
		 * While keeping the same notification and just updating it would be nice, it's seems to be 
		 * rather awkward to clear out the events on a notification click or dismiss. We'd have to 
		 * 1) make a BroadcastReceiver or an action in our App 2) on that action, get a reference to
		 * this builder which may be in a process 3) call clear notifications 4) launch the intent to
		 * email the errors. Specific implementation may do so, but it's too concrete of a use case for 
		 * this logger frame work to implement. 
		 */
		@Override
		public int getNotificationId() {
			return notificationId++;
		}
		
		@Override
		public Notification createNotification(LogEvent event) {
			events.clear(); // dirty hack to not have to rewrite the code
			events.add(event);
			NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
			
			NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
			for (int i=0; i<Math.min(events.size(), 6); i++) {
				inboxStyle.addLine(createLine(events.get(i)));
			}
			if (events.size() > 5) inboxStyle.setSummaryText("+" + (events.size()-5) + " more");
			
			builder.setStyle(inboxStyle);
			builder.setSmallIcon(notificationIconId);
			builder.setContentTitle("Error in App");
			builder.setContentText("Report this error!");
			builder.setAutoCancel(true);
			builder.setDefaults(Notification.DEFAULT_LIGHTS | 
					Notification.FLAG_AUTO_CANCEL | Notification.DEFAULT_SOUND | 
					Notification.FLAG_SHOW_LIGHTS);
			
			// add the action button
			Intent email = createEmailIntent("Crash Report", events);
			email = Intent.createChooser(email, "Send via");
			PendingIntent intent = PendingIntent.getActivity(context, 0, email, PendingIntent.FLAG_UPDATE_CURRENT);
			builder.addAction(notificationIconId, "Send Report", intent);
			builder.setContentIntent(intent);
			Notification noti = builder.build();
			
			return noti;
		}
		
		private Intent createEmailIntent(String subject, List<LogEvent> events) {
			StringBuilder sb = new StringBuilder();
			for (int i=0; i<events.size(); i++) {
				sb.append("Error ");
				sb.append((i+1));
				sb.append("\n");
				sb.append(convertEventToString(events.get(i)));
				sb.append("\n\n");
			}
			Intent email = new Intent(Intent.ACTION_SEND);
			email.setType("text/plain");
			if (!TextUtils.isEmpty(emailAddress)) email.putExtra(Intent.EXTRA_EMAIL, emailAddress);
			if (!TextUtils.isEmpty(subject)) email.putExtra(Intent.EXTRA_SUBJECT, subject);
			email.putExtra(Intent.EXTRA_TEXT, sb.toString());
			return email;
		}
		
		private String convertEventToString(LogEvent event) {
			StringBuilder str = new StringBuilder();
			str.append("{");
			str.append("level: ");
			str.append(getLogLevel(event.getLevel()));
			str.append(", time: ");
			str.append(format.format( new Date( event.getTime() )) );
			str.append(", pid: ");
			str.append(event.getPid());
			str.append(", tid: ");
			str.append(event.getTid());
			str.append(", tag: ");
			str.append(event.getTag());
			str.append(", message: ");
			str.append(event.getMessage());
			str.append(", throwable: ");
			str.append(Log.getStackTraceString(event.getThrowable()));
			str.append("}");
			return str.toString();
		}
		
		private String getLogLevel(int level) {
			switch (level) {
				case Log.VERBOSE: return "Verbose";
				case Log.DEBUG: return "Debug";
				case Log.INFO: return "Debug";
				case Log.WARN: return "Warn";
				case Log.ERROR: return "Error";
			}
			return Integer.toString(level);
		}
		
		private CharSequence createLine(LogEvent event) {
			SpannableStringBuilder ssb = new SpannableStringBuilder();
			if (!TextUtils.isEmpty(event.getTag())) {
				ssb.append(event.getTag());
				ssb.setSpan(new StyleSpan(Typeface.BOLD), 0, ssb.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				ssb.append(": ");
			}
			ssb.append( event.getMessage() );
			
			return ssb;
		}
	}
	
}