package hkust.cse.calendar.unit;

import java.io.Serializable;
import java.util.ArrayList;

public class User implements Serializable {

	private static final long serialVersionUID = 1L;
	private int userID;
	private String firstName;
	private String lastName;
	private String password;				// User password
	private String loginID;					// User id
	private String email;					// User email
	private int userType = 1;				// Admin or normal user
	private boolean isRemoved = false;
	//private boolean isInformed = false;
	//private ArrayList<Integer> apptIDList;
	//private ArrayList<Integer> apptTypeList;
	public static final int NEW = 0, CHANGE = 1, ACCEPT = 2, REJECT = 3, INIT = 4, REMOVE = 5;
	private ArrayList<String> groups;	// The groups the user belongs to
	public static final int ADMIN = 0, NORMAL = 1;

	public User(int userID, String firstName, String lastName, String id, String pass, String email, int userType) {
		this.userID = userID;
		this.firstName = firstName;
		this.lastName = lastName;
		loginID = id;
		password = pass;
		this.email = email;
		this.userType = userType;
		groups = new ArrayList<String>();
		//apptIDList = new ArrayList<Integer>();
		//apptTypeList = new ArrayList<Integer>();
	}
	public String toString() { return getLoginID(); }
	public int getID() { return userID; }
	public void setID(int id) { this.userID = id; }
	public String getLoginID() { return loginID; }
	public void setLoginID(String id) { this.loginID = id; }
	public String getPassword() { return password; }
	public void setPassword(String pass) { this.password = pass; }
	public String getEmail() { return email; }
	public void setEmail(String email) { this.email = email; }
	public int getUserType() { return userType; }
	public void setUserType(int userType) { this.userType = userType; }
	public String getFirstName() { return firstName; }
	public void setFirstName(String firstName) { this.firstName = firstName; }
	public String getLastName() { return lastName; }
	public void setLastName(String lastName) { this.lastName = lastName; }
	/*public ArrayList<Integer> getApptIDList() { return apptIDList; }
	public ArrayList<Integer> getApptTypeList() { return apptTypeList; }
	public void addAppt(int apptID, int type) { this.apptIDList.add(apptID); this.apptTypeList.add(type); System.out.println(loginID+"@"+apptID+"@"+type); }
	public void editAppt(int apptID, int type)
	{
		for( int i = 0; i < apptIDList.size(); i++ )
			if( apptIDList.get(i) == apptID )
				apptTypeList.set(i, type);
	}*/
	public boolean isRemoved() { return isRemoved; }
	public void setIsRemoved(boolean isRemoved) { this.isRemoved = isRemoved; }
	//public boolean isInformed() { return isInformed; }
	//public void setIsInformed(boolean isInformed) { this.isInformed = isInformed; }

	public boolean isUserPasswordCorrect( String id, String password )
	{
		if( this.loginID.equals(id) && this.password.equals(password) )
			return true;
		return false;
	}

	public void setUser( User u )
	{
		this.firstName = u.getFirstName();
		this.lastName = u.getLastName();
		this.loginID = u.getLoginID();
		this.password = u.getPassword();
		this.email = u.getEmail();
		this.userType = u.getUserType();
		this.isRemoved = this.isRemoved;
		//this.isInformed = this.isInformed;
		//this.apptIDList = u.apptIDList;
		//this.apptTypeList = u.apptTypeList;
		this.groups = u.groups;
	}
}
