package hkust.cse.calendar.unit;

public class Reminder {

	private int rid;							// ID of the reminder
	private int apptID;							// ID of the corresponding appointment
	private TimeSpan timeSpan;					// Include day, start time and end time of the reminder
	private int hourInterval;					// Interval of reminder from the appointment in hour
	private int minuteInterval;					// Interval of reminder from the appointment in minute
	private boolean isReminded;					// Store if the reminder is done

	public Reminder()
	{
		rid = -1;
		apptID = -1;
		timeSpan = null;
		isReminded = false;
	}
	public void setID(int rid) {this.rid = rid;}
	public void setApptID(int apptID) {this.apptID = apptID;}
	public void setTimeSpan(TimeSpan timeSpan) {this.timeSpan = timeSpan;}
	public void setHourInterval(int hourInterval) {this.hourInterval = hourInterval;}
	public void setMinuteInterval(int minuteInterval) {this.minuteInterval = minuteInterval;}
	public void setIsReminded(boolean isReminded) {this.isReminded = isReminded;}

	public int getID() {return rid;}
	public int getApptID() {return apptID;}
	public TimeSpan getTimeSpan() {return timeSpan;}
	public int getHourInterval() {return hourInterval;}
	public int getMinuteInterval() {return minuteInterval;}
	public boolean isReminded() {return isReminded;}
}
