package com.therealjoshua.essentials.logger;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Process;

/**
 * A LogEvent is a set of properties that represent a log action. 
 */
public class LogEvent implements Parcelable {

	private int level = -1;
	private long time;
	private int pid;
	private int tid;
	private String tag, message;
	private Throwable throwable;

	public LogEvent() {
		this(-1, null, null);
	}
	
	public LogEvent(int level, String tag, String message) {
		this(level, tag, message, null);
	}

	public LogEvent(int level, String tag, String message, Throwable tr) {
		this.level = level;
		this.tag = tag;
		this.message = message;
		this.throwable = tr;
		this.time = System.currentTimeMillis();
		this.pid = Process.myPid();
		this.tid = Process.myTid();
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getThrowable() {
		return throwable;
	}

	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public int getTid() {
		return tid;
	}

	public void setTid(int tid) {
		this.tid = tid;
	}
	
	@Override
	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append("{");
		str.append("level: ");
		str.append(level);
		str.append(", time: ");
		str.append(time);
		str.append(", pid: ");
		str.append(pid);
		str.append(", tid: ");
		str.append(tid);
		str.append(", tag: ");
		str.append(tag);
		str.append(", message: ");
		str.append(message);
		str.append(", throwable: ");
		str.append(throwable);
		str.append("}");
		return str.toString();
	}
	
	public LogEvent copy() {
		LogEvent event = new LogEvent();
		event.setLevel(level);
		event.setTag(tag);
		event.setMessage(message);
		event.setThrowable(throwable);
		event.setTime(time);
		event.setPid(pid);
		event.setTid(tid);
		return event;
	}
	
	
	// FOR PARCELABLE 
	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(level);
		dest.writeLong(time);
		dest.writeInt(pid);
		dest.writeInt(tid);
		dest.writeString(tag);
		dest.writeString(message);
		dest.writeSerializable(throwable);
	}
	
	private void readFromParcel(Parcel in) {
		level = in.readInt();
		time = in.readLong();
		pid = in.readInt();
		tid = in.readInt();
		tag = in.readString();
		message = in.readString();
		throwable = (Throwable)in.readSerializable();
	}
	
	public static final Parcelable.Creator<LogEvent> CREATOR = 
		new Parcelable.Creator<LogEvent>() {
			@Override
			public LogEvent createFromParcel(Parcel source) {
				LogEvent e = new LogEvent();
				e.readFromParcel(source);
				return e;
			}

			@Override
			public LogEvent[] newArray(int size) {
				return new LogEvent[size];
			}
		};

}
