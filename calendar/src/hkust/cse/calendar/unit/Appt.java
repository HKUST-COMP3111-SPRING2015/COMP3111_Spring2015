package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Appt implements Serializable {

	private static final long serialVersionUID = 1L;
	private TimeSpan mTimeSpan;					// Include day, start time and end time of the appointments
	private String mTitle;						// The Title of the appointments
	private String mInfo;						// Store the content of the appointments description
	private Location mLocation;					// The location where the event will be hold
	private int mApptID;						// The appointment id
	private int joinApptID;						// The join appointment id
	private boolean isjoint;					// The appointment is a joint appointment
	private boolean isPublic;
	private LinkedList<String> attend;			// The Attendant list
	private LinkedList<String> reject;			// The reject list
	private LinkedList<String> waiting;			// The waiting list
	private int rid;							// The reminder id
	private int freqGroupID;					// The appointment IDs of the same frequency group
	private int initUserID;					// The initiator's user ID
	//private ArrayList<String> inviteUserIDList;	// The list of users being invited
	//private ArrayList<Integer> userResponseList;	// The list of reponses from the users
	private String groupName;					// The group this event belongs to
	private boolean isScheduled;				//

	public Appt() {								// A default constructor used to set all the attribute to default values
		mApptID = 0;
		mTimeSpan = null;
		mTitle = "Untitled";
		mInfo = "";
		isjoint = false;
		isPublic = false;
		attend = new LinkedList<String>();
		reject = new LinkedList<String>();
		waiting = new LinkedList<String>();
		joinApptID = -1;
		rid = -1;
		freqGroupID = -1;
		initUserID = -1;
		groupName = "";
		//inviteUserIDList = new ArrayList<String>();
		//userResponseList = new ArrayList<Integer>();
	}

	// Getter of the mTimeSpan
	public TimeSpan TimeSpan() { return mTimeSpan; }
	
	// Getter of the appointment title
	public String getTitle() { return mTitle; }

	// Getter of appointment description
	public String getInfo() { return mInfo; }

	// Getter of appointment location
	public Location getLocation() { return mLocation; }

	// Getter of the appointment id
	public int getID() { return mApptID; }
	
	// Getter of the join appointment id
	public int getJoinID(){ return joinApptID; }

	public void setJoinID(int joinID){ this.joinApptID = joinID; }
	// Getter of the attend LinkedList<String>
	public LinkedList<String> getAttendList(){ return attend; }
	
	// Getter of the reject LinkedList<String>
	public LinkedList<String> getRejectList(){ return reject; }
	
	// Getter of the waiting LinkedList<String>
	public LinkedList<String> getWaitingList(){ return waiting; }
	
	public LinkedList<String> getAllPeople(){
		LinkedList<String> allList = new LinkedList<String>();
		allList.addAll(attend);
		allList.addAll(reject);
		allList.addAll(waiting);
		return allList;
	}
	
	public void addAttendant(String addID){
		if (attend == null)
			attend = new LinkedList<String>();
		attend.add(addID);
	}
	
	public void addReject(String addID){
		if (reject == null)
			reject = new LinkedList<String>();
		reject.add(addID);
	}
	
	public void addWaiting(String addID){
		if (waiting == null)
			waiting = new LinkedList<String>();
		waiting.add(addID);
	}
	
	public void setWaitingList(LinkedList<String> waitingList){ waiting = waitingList; }
	
	public void setWaitingList(String[] waitingList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (waitingList !=null){
			for (int a=0; a<waitingList.length; a++){
				tempLinkedList.add(waitingList[a].trim());
			}
		}
		waiting = tempLinkedList;
	}
	
	public void setRejectList(LinkedList<String> rejectLinkedList) { reject = rejectLinkedList; }
	
	public void setRejectList(String[] rejectList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (rejectList !=null){
			for (int a=0; a<rejectList.length; a++){
				tempLinkedList.add(rejectList[a].trim());
			}
		}
		reject = tempLinkedList;
	}
	
	public void setAttendList(LinkedList<String> attendLinkedList) { attend = attendLinkedList; }
	
	public void setAttendList(String[] attendList){
		LinkedList<String> tempLinkedList = new LinkedList<String>();
		if (attendList !=null){
			for (int a=0; a<attendList.length; a++){
				tempLinkedList.add(attendList[a].trim());
			}
		}
		attend = tempLinkedList;
	}
	// Getter of the appointment title
	public String toString() { return mTitle; }

	// Setter of the appointment title
	public void setTitle(String t) { mTitle = t; }

	// Setter of the appointment description
	public void setInfo(String in) { mInfo = in; }

	// Setter of the mTimeSpan
	public void setTimeSpan(TimeSpan d) { mTimeSpan = d; }

	// Setter of the mLocation
	public void setLocation(Location l) { mLocation = l; }

	public void setApptID(int id) { mApptID = id; }
	public boolean isJoint(){ return isjoint; }
	public void setJoint(boolean isjoint){ this.isjoint = isjoint; }
	public boolean isPublic(){ return isPublic; }
	public void setPublic(boolean isPublic){ this.isPublic = isPublic; }
	public boolean isOverlap(TimeSpan d) { return this.mTimeSpan.Overlap(d); }
	public void setReminderID(int rid) { this.rid = rid; }
	public int getReminderID() { return rid; }
	public void setFreqGroupID( int id ) { freqGroupID = id; }
	public int getFreqGroupID() { return freqGroupID; }
	public void setInitUserID(int id) { this.initUserID = id; }
	public int getInitUserID() { return initUserID; }
	public void setGroupName(String s) { this.groupName = s; }
	public String getGroupName() { return groupName; }
	/*public void inviteUser(String id) { this.inviteUserIDList.add(id); this.userResponseList.add(Appt.NEW); }
	public void editResponse(String id, int res)
	{
		for( int i = 0; i < inviteUserIDList.size(); i++ )
			if( inviteUserIDList.get(i).equals(id) )
			{
				userResponseList.set(i, res);
				break;
			}
		for( int i = 0; i < userResponseList.size(); i++ )
			if( !userResponseList.get(i).equals(Appt.ACCEPT) )
			{
				isScheduled = false;
				return;
			}
		isScheduled = true;
	}*/
	public void setIsScheduled(boolean b) { this.isScheduled = b; }
	public boolean isScheduled() { return isScheduled; }
}
