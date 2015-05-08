package hkust.cse.calendar.apptstorage;

import java.util.ArrayList;

import hkust.cse.calendar.gui.CalGrid;
import hkust.cse.calendar.unit.*;

public class Model
{
	//private CalGrid calGrid;
	//private Controller ctrl;
	private DatabaseModel db;

	public Model()
	{
		db = new DatabaseModel();
	}

	public int getNextApptID()
	{
		return db.getNextApptID();
	}

	public int getNextReminderID()
	{
		return db.getNextReminderID();
	}

	public int getNextLocationID()
	{
		return db.getNextLocationID();
	}

	public int getNextFreqGroupID()
	{
		return db.getNextFreqGroupID();
	}

	public boolean isUserAuthorized(String id, String pw)
	{
		User u = db.getUser(id);
		if(u != null && u.getPassword().equals(pw))
			return true;
		return false;
	}

	public User getUser(int userID)
	{
		return db.getUser(userID);
	}

	public User getUser(String loginID)
	{
		return db.getUser(loginID);
	}

	public ArrayList<User> getUserList()
	{
		return db.getUserList();
	}

	public void addUser(User user)
	{
		db.addUser(user);
	}

	public void updateUser(User user)
	{
		db.updateUser(user);
	}

	public void removeUser(int userID)
	{
		db.removeUser(userID);
	}

	public Appt getAppt(int apptID)
	{
		return db.getAppt(apptID);
	}

	public ArrayList<Appt> getApptList(int userID)
	{
		return db.getApptList(userID);
	}

	public ArrayList<Appt> getApptList(ArrayList<String> userLoginIDList)
	{
		return db.getApptList(userLoginIDList);
	}

	public ArrayList<Appt> getLocationApptList(int locationID)
	{
		return db.getLocationApptList(locationID);
	}

	public ArrayList<Appt> getInitiateApptList(int userID)
	{
		return db.getInitiateApptList(userID);
	}

	public ArrayList<Appt> getApptResponseList(int userID, String response)
	{
		return db.getApptResponseList(userID, response);
	}

	public ArrayList<Integer> getApptGroupIDList(int freGroupID)
	{
		return db.getApptGroupIDList(freGroupID);
	}

	public ArrayList<Appt> getPublicApptList()
	{
		return db.getPublicApptList();
	}
 
	public boolean addAppt(Appt a)
	{
		int t = db.addAppointment(a);
		if( t == -1 )
			return false;
		return true; 
	}

	public boolean updateAppt(Appt a)
	{
		return db.updateAppt(a);
	}

	public boolean removeAppt(int apptID)
	{
		return db.removeAppointment(apptID);
	}

	public void removeFreqGroup(int gid)
	{
		db.removeFreqGroup(gid);
	}

	public Reminder getReminder(int rid)
	{
		return db.getReminder(rid);
	}

	public ArrayList<Reminder> getReminderList(ArrayList<Appt> apptList)
	{
		return db.getReminderList(apptList);
	}

	public void addReminder(Reminder r)
	{
		db.addReminder(r);
	}

	public void updateReminder(Reminder r)
	{
		db.updateReminder(r);
	}

	public void removeReminder(int rID)
	{
		db.removeReminder(rID);
	}

	public Location getLocation(int locationID)
	{
		return db.getLocation(locationID);
	}

	public ArrayList<Location> getLocationList()
	{
		return db.getLocationList();
	}

	public void addLocation(Location l)
	{
		db.addLocation(l);
	}

	public void updateLocation(Location l)
	{
		db.updateLocation(l);
	}

	public void removeLocation(int locationID)
	{
		db.removeLocation(locationID);
	}

	public ArrayList<Response> getResponseList(int apptID)
	{
		return db.getResponseList(apptID);
	}

	public void addResponse(int apptID, int userID, String res)
	{
		db.addResponse(apptID, userID, res);
	}

	public void updateResponse(int apptID, int userID, String res)
	{
		db.updateResponse(apptID, userID, res);
	}

	public void removeResponse(int apptID)
	{
		db.removeResponse(apptID);
	}

	public void addUserToRemoveTable(int userID, int informID)
	{
		db.addToRemoveTable(userID, -1, informID);
	}

	public void addLocationToRemoveTable(int locationID, int informID)
	{
		db.addToRemoveTable(-1, locationID, informID);
	}

	public void updateUserToRemoveTable(int userID, int informID, boolean isConfirm)
	{
		db.updateRemoveTable(userID, -1, informID, isConfirm);
	}

	public void updateLocationToRemoveTable(int locationID, int informID, boolean isConfirm)
	{
		db.updateRemoveTable(-1, locationID, informID, isConfirm);
	}

	public ArrayList<Integer> getInitUserByUserID(int userID)
	{
		return db.getInitUserByUserID(userID);
	}

	public ArrayList<Integer> getInitUserByLocationID(int locationID)
	{
		return db.getInitUserByLocationID(locationID);
	}

	public ArrayList<RemoveMessage> getRemoveMessage(int userID)
	{
		return db.getRemoveMessage(userID);
	}
}
