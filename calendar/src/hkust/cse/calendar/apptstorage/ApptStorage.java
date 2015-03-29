package hkust.cse.calendar.apptstorage;//

import hkust.cse.calendar.unit.*;

import java.util.ArrayList;
import java.util.HashMap;


public abstract class ApptStorage {

	protected HashMap<Integer, Appt> mAppts;		//a hashmap to save every thing to it, write to memory by the memory based storage implementation	
	protected User defaultUser;	//a user object, now is single user mode without login
	protected int mAssignedApptID;	//a global appointment ID for each appointment record
	protected HashMap<Integer, Reminder> mReminders;
	protected int mAssignedReminderID;

	public ApptStorage() {	//default constructor
	}

	public abstract Location[] getLocationList();

	public abstract void setLocationList( Location[] locations );

	public abstract boolean SaveAppt(Appt appt);	//abstract method to save an appointment record

	public abstract Appt getAppt(int apptID);

	public abstract Appt[] RetrieveAppts(TimeSpan d);	//abstract method to retrieve an appointment record by a given timespan

	public abstract Appt[] RetrieveAppts(User entity, TimeSpan time);	//overloading abstract method to retrieve an appointment record by a given user object and timespan
	
	public abstract Appt RetrieveAppts(int joinApptID);					// overload method to retrieve appointment with the given joint appointment id

	public abstract boolean UpdateAppt(Appt appt);	//abstract method to update an appointment record

	public abstract boolean RemoveAppt(Appt appt);	//abstract method to remove an appointment record
	
	public abstract User getDefaultUser();		//abstract method to return the current user object
	
	public abstract void LoadApptFromXml();		//abstract method to load appointment from xml reocrd into hash map

	public abstract int getNextApptID();		//abstract method to return the next appointment id

	public abstract void addReminder(Reminder r);	//abstract method to add a reminder

	public abstract void updateReminder(Reminder r);	// abstract method to update a reminder

	public abstract void removeReminder(Reminder r);	//abstract method to remove a reminder

	public abstract ArrayList<Reminder> getReminders(User entity, TimeSpan time); // abstract method to return the list of reminders within the period

	public abstract Reminder getReminder(int rid);	//abstract method to return a reminder of the parameter id

	public abstract int getNextReminderID();		//abstract method to return the next reminder id
	/*
	 * Add other methods if necessary
	 */

}
