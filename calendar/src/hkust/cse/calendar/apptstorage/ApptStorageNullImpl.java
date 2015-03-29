package hkust.cse.calendar.apptstorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Reminder;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

public class ApptStorageNullImpl extends ApptStorage {

	private User defaultUser = null;
	private Location[] locations;
	
	public ApptStorageNullImpl( User user )
	{
		defaultUser = user;
		mAppts = new HashMap<Integer, Appt>();
		locations = new Location[1];
		locations[0] = new Location("Test");
		mReminders = new HashMap<Integer, Reminder>();
		mAssignedApptID = 1;
		mAssignedReminderID = 1;
	}

	@Override
	public boolean SaveAppt(Appt appt) {
		Iterator<Integer> it = mAppts.keySet().iterator();
		TimeSpan ts = appt.TimeSpan();
		while ( it.hasNext() )
		{
			Appt a = (Appt) mAppts.get( it.next() );
			if( a.isOverlap(ts) )
				return false;
		}
		mAppts.put(appt.getID(), appt);
		mAssignedApptID++;
		return true;
	}

	@Override
	public Appt getAppt(int apptID)
	{
		return (Appt) mAppts.get(apptID);
	}

	@Override
	public Appt[] RetrieveAppts(TimeSpan d) {
		ArrayList<Appt> apptList = new ArrayList<Appt>();
		Iterator<Integer> it = mAppts.keySet().iterator();
		while ( it.hasNext() )
		{
			Appt a = (Appt) mAppts.get( it.next() );
			if( a.isOverlap( d ) )
				apptList.add( a );
		}
		Appt[] returnApptList = new Appt[apptList.size()];
		for( int i = 0; i < apptList.size(); i++ )
			returnApptList[i] = apptList.get(i);
		return returnApptList;
	}

	@Override
	public Appt[] RetrieveAppts(User entity, TimeSpan time) {
		if( !this.defaultUser.equals( entity ) )
			return null;
		ArrayList<Appt> apptList = new ArrayList<Appt>();
		Iterator<Integer> it = mAppts.keySet().iterator();
		while ( it.hasNext() )
		{
			Appt a = (Appt) mAppts.get( it.next() );
			if( a.isOverlap( time ) )
				apptList.add( a );
		}
		Appt[] returnApptList = new Appt[apptList.size()];
		for( int i = 0; i < apptList.size(); i++ )
			returnApptList[i] = apptList.get(i);
		return returnApptList;
	}

	@Override
	public Appt RetrieveAppts(int joinApptID) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean UpdateAppt(Appt appt) {
		Iterator<Integer> it = mAppts.keySet().iterator();
		TimeSpan ts = appt.TimeSpan();
		while ( it.hasNext() )
		{
			Appt a = (Appt) mAppts.get( it.next() );
			if( a.getID() != appt.getID() && a.isOverlap(ts) )
				return false;
		}
		mAppts.replace(appt.getID(), appt);
		return true;
	}

	@Override
	public boolean RemoveAppt(Appt appt) {
		mAppts.remove(appt.getID());
		return true;
	}

	@Override
	public User getDefaultUser() {
		// TODO Auto-generated method stub
		return defaultUser;
	}

	@Override
	public void LoadApptFromXml() {
		// TODO Auto-generated method stub

	}

	@Override
	public Location[] getLocationList() {
		return locations;
	}

	@Override
	public void setLocationList(Location[] locations) {
		this.locations = locations;
	}

	@Override
	public int getNextApptID()
	{
		return mAssignedApptID;
	}

	@Override
	public void addReminder(Reminder r)
	{
		mReminders.put(r.getID(), r);
		mAssignedReminderID++;
	}

	@Override
	public void updateReminder(Reminder r)
	{
		mReminders.replace(r.getID(), r);
	}

	@Override
	public void removeReminder(Reminder r)
	{
		mReminders.remove(r.getID());
	}

	@Override
	public HashMap<Integer, Reminder> getReminders()
	{
		return mReminders;
	}

	@Override
	public Reminder getReminder(int rid)
	{
		return mReminders.get(rid);
	}

	@Override
	public int getNextReminderID()
	{
		return mAssignedReminderID;
	}
}
