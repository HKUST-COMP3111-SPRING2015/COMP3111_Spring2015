package hkust.cse.calendar.apptstorage;

import java.util.ArrayList;
import java.util.HashMap;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Reminder;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

/* This class is for managing the Appt Storage according to different actions */
public class ApptStorageControllerImpl {

	/* Remove the Appt from the storage */
	public final static int REMOVE = 1;

	/* Modify the Appt the storage */
	public final static int MODIFY = 2;

	/* Add a new Appt into the storage */
	public final static int NEW = 3;
	
	/*
	 * Add additional flags which you feel necessary
	 */
	
	/* The Appt storage */
	private ApptStorage mApptStorage;

	/* Create a new object of ApptStorageControllerImpl from an existing storage of Appt */
	public ApptStorageControllerImpl(ApptStorage storage) {
		mApptStorage = storage;
	}

	public Appt getAppt(int apptID)
	{
		return mApptStorage.getAppt(apptID);
	}

	/* Retrieve the Appt's in the storage for a specific user within the specific time span */
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		return mApptStorage.RetrieveAppts(entity, time);
	}

	// overload method to retrieve appointment with the given joint appointment id
	public Appt RetrieveAppts(int joinApptID) {
		return mApptStorage.RetrieveAppts(joinApptID);
	}
	
	/* Manage the Appt in the storage
	 * parameters: the Appt involved, the action to take on the Appt */
	public boolean ManageAppt(Appt appt, int action) {

		if (action == NEW) {				// Save the Appt into the storage if it is new and non-null
			if (appt == null)
				return false;
			return mApptStorage.SaveAppt(appt);
		} else if (action == MODIFY) {		// Update the Appt in the storage if it is modified and non-null
			if (appt == null)
				return false;
			return mApptStorage.UpdateAppt(appt);
		} else if (action == REMOVE) {		// Remove the Appt from the storage if it should be removed
			return mApptStorage.RemoveAppt(appt);
		} 
		return false;
	}

	public void addNewReminder(Reminder r)
	{
		mApptStorage.addReminder(r);
	}

	public void updateReminder(Reminder r)
	{
		mApptStorage.updateReminder(r);
	}

	public void removeReminder(Reminder r)
	{
		mApptStorage.removeReminder(r);
	}

	/* Get the defaultUser of mApptStorage */
	public User getDefaultUser() {
		return mApptStorage.getDefaultUser();
	}

	// method used to load appointment from xml record into hash map
	public void LoadApptFromXml(){
		mApptStorage.LoadApptFromXml();
	}

	public Location[] getLocationList() {
		return mApptStorage.getLocationList();
	}

	public void setLocationList(Location[] locations) {
		mApptStorage.setLocationList(locations);
	}

	public HashMap<Integer, Reminder> getReminders()
	{
		return mApptStorage.getReminders();
	}

	public Reminder getReminder(int rid)
	{
		return mApptStorage.getReminder(rid);
	}

	public int getNextApptID()
	{
		return mApptStorage.getNextApptID();
	}

	public int getNextReminderID()
	{
		return mApptStorage.getNextReminderID();
	}
}
