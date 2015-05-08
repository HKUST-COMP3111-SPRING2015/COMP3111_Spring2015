package hkust.cse.calendar.apptstorage;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import hkust.cse.calendar.gui.CalGrid;
import hkust.cse.calendar.unit.*;

public class Controller
{
	private CalGrid calGrid;
	private Model model;

	public Controller()
	{
		
	}

	public void setCalGrid( CalGrid calGrid )
	{
		this.calGrid = calGrid;
	}

	public void setModel( Model model )
	{
		this.model = model;
	}

	public boolean isApptCrash(int userID, TimeSpan t, int apptID)
	{
		ArrayList<Appt> apptList = model.getApptList(userID);
		for( Appt a : apptList )
			if( apptID != a.getID() && a.isOverlap(t) )
				return true;
		return false;
	}

	public boolean isLocationCrash(Location l, TimeSpan t, int apptID)
	{
		ArrayList<Appt> apptList = model.getLocationApptList(l.getID());
		for( Appt a : apptList )
			if( apptID != a.getID() && a.isOverlap(t) )
				return true;
		return false;
	}

	public boolean isApptExistInPeriod(User user, TimeSpan period)
	{
		ArrayList<Appt> apptList = model.getApptList(user.getID());
		for( Appt a : apptList )
			if( a.isOverlap(period) )
				return true;
		return false;
	}

	public ArrayList<Appt> getApptList(int userID, TimeSpan period)
	{
		ArrayList<Appt> apptList = model.getApptList(userID);
		ArrayList<Appt> returnList = new ArrayList<Appt>();
		for( Appt a : apptList )
			if( a.isOverlap(period) )
				returnList.add(a);
		return returnList;
	}

	public ArrayList<Appt> getInitiateApptList(int userID, TimeSpan period)
	{
		ArrayList<Appt> apptList = model.getInitiateApptList(userID);
		ArrayList<Appt> returnList = new ArrayList<Appt>();
		for( Appt a : apptList )
		{
			if( a.isOverlap(period) )
				returnList.add(a);
		}
		return returnList;
	}

	public ArrayList<Appt> getApptResponseList(int userID, String response, TimeSpan period)
	{
		ArrayList<Appt> apptList = model.getApptResponseList(userID, response);
		ArrayList<Appt> returnList = new ArrayList<Appt>();
		for( Appt a : apptList )
		{
			if( a.isOverlap(period) )
				returnList.add(a);
		}
		return returnList;
	}

	public ArrayList<Appt> getPublicApptList(TimeSpan period)
	{
		ArrayList<Appt> apptList = model.getPublicApptList();
		ArrayList<Appt> returnList = new ArrayList<Appt>();
		for( Appt a : apptList )
		{
			if( a.isOverlap(period) )
				returnList.add(a);
		}
		return returnList;
	}

	public TimeSpan getEarliestSchedule(GregorianCalendar today, int userID)
	{
		Calendar now = Calendar.getInstance();
		now.setTime( ( today.getTime() ));
		int currentY = today.get(Calendar.YEAR);
		int currentD = today.get(today.DAY_OF_MONTH);
		int currentM = today.get(today.MONTH);
		int currentH = today.get(today.HOUR_OF_DAY);
		int currentMin = today.get(today.MINUTE);
		if( currentMin >= 45 )
		{
			currentH++;
			currentMin = 0;
		}
		else
			currentMin = currentMin - (currentMin%15) + 15;
		
		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM);
		start.setDate(currentD);
		start.setHours(currentH);
		start.setMinutes(currentMin);
		start.setSeconds(0);
		
		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM);
		end.setDate(currentD);
		end.setHours(currentH);
		end.setMinutes(currentMin+15);
		end.setSeconds(0);
		TimeSpan ts = new TimeSpan(start, end);
		ArrayList<Appt> apptList = model.getApptList(userID);
		currentMin+=15;
		while(true)
		{
			System.out.println(ts.getTimeString());
			boolean isAvailable = true;
			for( Appt a : apptList )
				if( a.isOverlap(ts) )
				{
					isAvailable = false;
					break;
				}
			if( isAvailable )
				return ts;
			start.setMinutes(currentMin);
			end.setMinutes(currentMin+15);
			currentMin+=15;
			ts = new TimeSpan(start, end);
		}
	}

	public ArrayList<TimeSpan> getAvailableSchedule(Calendar today, ArrayList<String> userLoginIDList, int numOfDays)
	{
		/*int currentY = today.get(Calendar.YEAR);
		int currentD = today.get(Calendar.DAY_OF_MONTH);
		int currentM = today.get(Calendar.MONTH);
		int currentH = today.get(Calendar.HOUR_OF_DAY);
		int currentMin = today.get(Calendar.MINUTE);
		if( currentMin >= 45 )
		{
			currentH++;
			currentMin = 0;
		}
		else
			currentMin = currentMin - (currentMin%15) + 15;*/

		Timestamp start = new Timestamp(today.getTimeInMillis());
		start.setSeconds(0);
		/*start.setYear(currentY);
		start.setMonth(currentM);
		start.setDate(currentD);
		start.setHours(currentH);
		start.setMinutes(currentMin);
		start.setSeconds(0);*/

		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(today.getTimeInMillis());
		c.add(Calendar.MINUTE, 15);
		Timestamp end = new Timestamp(c.getTimeInMillis());
		end.setSeconds(0);
		/*end.setYear(currentY);
		end.setMonth(currentM);
		end.setDate(currentD);
		end.setHours(currentH);
		end.setMinutes(currentMin+15);
		end.setSeconds(0);*/
		TimeSpan ts = new TimeSpan(start, end);

		Timestamp until = new Timestamp(start.getTime());
		
		c.setTimeInMillis(until.getTime());
		c.add(Calendar.DAY_OF_MONTH, numOfDays);
		until.setTime(c.getTimeInMillis());

		ArrayList<TimeSpan> availableList = new ArrayList<TimeSpan>();
		ArrayList<Appt> apptList = model.getApptList(userLoginIDList);
		boolean isFirstSlot = true;
		while(true)
		{
			if( ts.EndTime().after(until) )
			{
				c = Calendar.getInstance();
				c.setTimeInMillis(end.getTime());
				c.add(Calendar.MINUTE, -15);
				end.setTime(c.getTimeInMillis());
				Timestamp t1 = new Timestamp(start.getTime());
				Timestamp t2 = new Timestamp(end.getTime());
				availableList.add( new TimeSpan(t1, t2) );
				break;
			}
			//System.out.println(ts.getTimeString());
			boolean isAvailable = true;
			for( Appt a : apptList )
				if( a.isOverlap(ts) )
				{
					isAvailable = false;
					break;
				}
			if( isAvailable )
			{
				isFirstSlot = false;
				c = Calendar.getInstance();
				c.setTimeInMillis(end.getTime());
				c.add(Calendar.MINUTE, 15);
				end.setTime(c.getTimeInMillis());
				ts = new TimeSpan(start, end);
			}
			else
			{
				//Do nothing if it is the 1st trial
				if( isFirstSlot )
				{
					c = Calendar.getInstance();
					c.setTimeInMillis(start.getTime());
					c.add(Calendar.MINUTE, 15);
					start.setTime(c.getTimeInMillis());
					c = Calendar.getInstance();
					c.setTimeInMillis(end.getTime());
					c.add(Calendar.MINUTE, 15);
					end.setTime(c.getTimeInMillis());
				}
				else
				{
					c = Calendar.getInstance();
					c.setTimeInMillis(end.getTime());
					c.add(Calendar.MINUTE, -15);
					end.setTime(c.getTimeInMillis());
					Timestamp t1 = new Timestamp(start.getTime());
					Timestamp t2 = new Timestamp(end.getTime());
					availableList.add( new TimeSpan(t1, t2) );
					start.setTime(end.getTime()+15*60*1000);
					c = Calendar.getInstance();
					c.setTimeInMillis(end.getTime());
					c.add(Calendar.MINUTE, 30);
					end.setTime(c.getTimeInMillis());
					ts = new TimeSpan(start, end);
				}
				isFirstSlot = true;
			}
		}
		return availableList;
	}

	public ArrayList<Reminder> getReminderList(ArrayList<Appt> apptList, TimeSpan period)
	{
		ArrayList<Reminder> reminderList = model.getReminderList(apptList);
		ArrayList<Reminder> returnList = new ArrayList<Reminder>();
		for( Reminder r : reminderList )
			if( r.getTimeSpan().Overlap(period) )
				returnList.add(r);
		return returnList;
	}

	public void addUserToRemoveTable(int userID, TimeSpan period)
	{
		ArrayList<Integer> informList = model.getInitUserByUserID(userID);
		for( int i = 0; i < informList.size(); i++ )
			model.addUserToRemoveTable(userID, informList.get(i));
	}

	public void addLocationToRemoveTable(int locationID, TimeSpan period)
	{
		ArrayList<Integer> informList = model.getInitUserByLocationID(locationID);
		for( int i = 0; i < informList.size(); i++ )
			model.addLocationToRemoveTable(locationID, informList.get(i));
	}
}
