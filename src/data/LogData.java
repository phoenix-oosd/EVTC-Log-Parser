package data;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class LogData {

	// Fields
	private String build_version;
	private String log_start = "yyyy-MM-dd HH:mm:ss z";
	private String log_end = "yyyy-MM-dd HH:mm:ss z";
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");

	// Constructors
	public LogData(String build_version) {
		this.build_version = build_version;
		this.sdf.setTimeZone(TimeZone.getDefault());
	}

	// Public Methods
	public String[] toStringArray() {
		String[] array = new String[3];
		array[0] = String.valueOf(build_version);
		array[1] = String.valueOf(log_start);
		array[2] = String.valueOf(log_end);
		return array;
	}

	// Getters
	public String getBuildVersion() {
		return build_version;
	}

	public String getLogStart() {
		return log_start;
	}

	public String getLogEnd() {
		return log_end;
	}

	// Setters
	public void setLogStart(int unix_seconds) {
		this.log_start = sdf.format(new Date(unix_seconds * 1000L));
	}

	public void setLogEnd(int unix_seconds) {
		this.log_end = sdf.format(new Date(unix_seconds * 1000L));
	}

}
