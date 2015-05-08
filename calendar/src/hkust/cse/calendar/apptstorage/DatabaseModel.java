package hkust.cse.calendar.apptstorage;

import hkust.cse.calendar.unit.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class DatabaseModel
{
	private static String url = "jdbc:mysql://localhost:3306/COMP3111";
	private static String accountName = "root";
	private static String password = "1234";
	private Connection conn;

	public DatabaseModel()
	{
		try
		{
			Class.forName( "com.mysql.jdbc.Driver" );
			conn = DriverManager.getConnection( url, accountName, password );
		}
		catch( SQLException | ClassNotFoundException e ) { e.printStackTrace(); }
	}

	public int getNextApptID()
	{
		try
		{
			ResultSet rs = executeSQL( "SELECT MAX(ApptID) AS ID FROM Appointment" );
			rs.next();
			int id = (rs.getInt("ID")+1);
			rs.close();
			return id;
		}
		catch( SQLException e ) { e.printStackTrace(); return -1; }
	}

	public int getNextFreqGroupID()
	{
		try
		{
			ResultSet rs = executeSQL( "SELECT MAX(FreqGroupID) AS ID FROM Appointment" );
			rs.next();
			int id = (rs.getInt("ID")+1);
			rs.close();
			return id;
		}
		catch( SQLException e ) { e.printStackTrace(); return -1; }
	}

	public int getNextReminderID()
	{
		try
		{
			ResultSet rs = executeSQL( "SELECT MAX(ReminderID) AS ID FROM Reminder" );
			rs.next();
			int id = (rs.getInt("ID")+1);
			rs.close();
			return id;
		}
		catch( SQLException e ) { e.printStackTrace(); return -1; }
	}

	public int getNextLocationID()
	{
		try
		{
			ResultSet rs = executeSQL( "SELECT MAX(LocationID) AS ID FROM Location" );
			rs.next();
			int id = (rs.getInt("ID")+1);
			rs.close();
			return id;
		}
		catch( SQLException e ) { e.printStackTrace(); return -1; }
	}

	public int addAppointment( Appt a )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "INSERT INTO Appointment (Title, Info, StartTime, EndTime, InitUserID, LocationID, ReminderID, FreqGroupID, IsPublic, IsJoint, IsScheduled) "
					+ "VALUES (?,?,?,?,?,?,?,?,?,?,?)";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setString(1, a.getTitle());
			stat.setString(2, a.getInfo());
			stat.setTimestamp(3, a.TimeSpan().StartTime());
			stat.setTimestamp(4, a.TimeSpan().EndTime());
			stat.setInt(5, a.getInitUserID());
			stat.setInt(6, a.getLocation().getID());
			stat.setInt(7, a.getReminderID());
			stat.setInt(8, a.getFreqGroupID());
			stat.setBoolean(9, a.isPublic());
			stat.setBoolean(10, a.isJoint());
			stat.setBoolean(11, a.isScheduled());
			stat.executeUpdate();
			ResultSet rs = executeSQL( "SELECT MAX(ApptID) AS AID FROM Appointment" );
			rs.next();
			int aid = rs.getInt("AID");
			rs.close();
			conn.close();
			return aid;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return -1;
		}
	}

	public int addReminder( Reminder r )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "INSERT INTO Reminder (StartTime, EndTime, HourInterval, MinuteInterval, IsReminded) "
					+ "VALUES (?,?,?,?,?)";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setTimestamp(1, r.getTimeSpan().StartTime());
			stat.setTimestamp(2, r.getTimeSpan().EndTime());
			stat.setInt(3, r.getHourInterval());
			stat.setInt(4, r.getMinuteInterval());
			stat.setBoolean(5, r.isReminded());
			stat.executeUpdate();
			ResultSet rs = executeSQL( "SELECT MAX(ReminderID) AS RID FROM Reminder" );
			rs.next();
			int rid = rs.getInt("RID");
			rs.close();
			conn.close();
			return rid;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return -1;
		}
	}

	public int addLocation( Location l )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "INSERT INTO Location (Name, GroupName, Capacity, IsGroup) "
					+ "VALUES (?,?,?,?)";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setString(1, l.getName());
			stat.setString(2, l.getGroup());
			stat.setInt(3, l.getCapacity());
			stat.setBoolean(4, l.isGroup());
			stat.executeUpdate();
			ResultSet rs = executeSQL( "SELECT MAX(LocationID) AS LID FROM Location" );
			rs.next();
			int lid = rs.getInt("LID");
			rs.close();
			conn.close();
			return lid;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return -1;
		}
	}

	public int addUser( User u )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "INSERT INTO User (FirstName, LastName, LoginID, Password, Email, UserType, IsRemoved) "
					+ "VALUES (?,?,?,?,?,?,?)";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setString(1, u.getFirstName());
			stat.setString(2, u.getLastName());
			stat.setString(3, u.getLoginID());
			stat.setString(4, u.getPassword());
			stat.setString(5, u.getEmail());
			stat.setInt(6, u.getUserType());
			stat.setBoolean(7, u.isRemoved());
			stat.executeUpdate();
			ResultSet rs = executeSQL( "SELECT MAX(UserID) AS UID FROM User" );
			rs.next();
			int uid = rs.getInt("UID");
			rs.close();
			conn.close();
			return uid;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return -1;
		}
	}

	public void addResponse( int apptID, int userID, String res )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "INSERT INTO Response (ApptID, UserID, Response) "
					+ "VALUES (?,?,?)";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setInt(1, apptID);
			stat.setInt(2, userID);
			stat.setString(3, res);
			stat.executeUpdate();
			conn.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public boolean updateAppt( Appt a )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "UPDATE Appointment SET Title=?, Info=?, StartTime=?, EndTime=?, InitUserID=?, LocationID=?, ReminderID=?, FreqGroupID=?, IsPublic=?, IsJoint=?, IsScheduled=? WHERE ApptID=?";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setString(1, a.getTitle());
			stat.setString(2, a.getInfo());
			stat.setTimestamp(3, a.TimeSpan().StartTime());
			stat.setTimestamp(4, a.TimeSpan().EndTime());
			stat.setInt(5, a.getInitUserID());
			stat.setInt(6, a.getLocation().getID());
			stat.setInt(7, a.getReminderID());
			stat.setInt(8, a.getFreqGroupID());
			stat.setBoolean(9, a.isPublic());
			stat.setBoolean(10, a.isJoint());
			stat.setBoolean(11, a.isScheduled());
			stat.setInt(12, a.getID());
			stat.executeUpdate();
			conn.close();
			return true;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean scheduleAppt( int apptID )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "UPDATE Appointment SET IsScheduled=? WHERE ApptID=?";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setBoolean(1, true);
			stat.setInt(2, apptID);
			stat.executeUpdate();
			conn.close();
			return true;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return false;
		}
	}

	public void updateReminder( Reminder r )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "UPDATE Reminder SET StartTime=?, EndTime=?, HourInterval=?, MinuteInterval=?, IsReminded=? WHERE ReminderID=?";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setTimestamp(1, r.getTimeSpan().StartTime());
			stat.setTimestamp(2, r.getTimeSpan().EndTime());
			stat.setInt(3, r.getHourInterval());
			stat.setInt(4, r.getMinuteInterval());
			stat.setBoolean(5, r.isReminded());
			stat.setInt(6, r.getID());
			stat.executeUpdate();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void updateLocation( Location l )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "UPDATE Location SET Name=?, GroupName=?, Capacity=?, IsGroup=? WHERE LocationID = ?";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setString(1, l.getName());
			stat.setString(2, l.getGroup());
			stat.setInt(3, l.getCapacity());
			stat.setBoolean(4, l.isGroup());
			stat.setInt(5, l.getID());
			stat.executeUpdate();
			conn.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void updateUser( User u )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "UPDATE User SET FirstName=?, LastName=?, LoginID=?, Password=?, Email=?, "
					+ "UserType=?, IsRemoved=? WHERE UserID = ?";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setString(1, u.getFirstName());
			stat.setString(2, u.getLastName());
			stat.setString(3, u.getLoginID());
			stat.setString(4, u.getPassword());
			stat.setString(5, u.getEmail());
			stat.setInt(6, u.getUserType());
			stat.setBoolean(7, u.isRemoved());
			stat.setInt(8, u.getID());
			stat.executeUpdate();
			conn.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void updateResponse( int apptID, int userID, String res )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "UPDATE Response SET Response=? WHERE ApptID=? AND UserID=?";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setString(1, res);
			stat.setInt(2, apptID);
			stat.setInt(3, userID);
			stat.executeUpdate();
			conn.close();

			ResultSet rs = executeSQL( "SELECT Response FROM Response WHERE ApptID="+apptID );
			boolean isScheduled = true;
			while(rs.next())
			{
				if(!rs.getString("Response").equals(Response.ACCEPT))
				{
					isScheduled = false;
					break;
				}
			}
			if( isScheduled )
			{
				this.scheduleAppt(apptID);
			}
			rs.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public boolean removeAppointment( int apptID )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "UPDATE Appointment SET IsRemoved=? WHERE ApptID=?";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setBoolean(1, true);
			stat.setInt(2, apptID);
			stat.executeUpdate();
			conn.close();
			return true;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return false;
		}
	}

	public boolean removeFreqGroup( int groupID )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "UPDATE Appointment SET IsRemoved=? WHERE FreqGroupID=?";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setBoolean(1, true);
			stat.setInt(2, groupID);
			stat.executeUpdate();
			ResultSet rs = executeSQL( "SELECT ReminderID FROM Appointment WHERE FreqGroupID="+groupID );
			while(rs.next())
			{
				String sql2 = "UPDATE Reminder SET IsRemoved=? WHERE ReminderID=?";
				PreparedStatement stat2 = conn.prepareStatement(sql2);
				stat2.setBoolean(1, true);
				stat2.setInt(2, rs.getInt("ReminderID"));
				stat2.executeUpdate();
			}
			rs.close();
			conn.close();
			return true;
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return false;
		}
	}

	public void removeReminder( int rID )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "UPDATE Reminder SET IsRemoved=? WHERE ReminderID=?";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setBoolean(1, true);
			stat.setInt(2, rID);
			stat.executeUpdate();
			conn.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void removeLocation( int id )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "UPDATE Location SET IsRemoved=? WHERE LocationID=?";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setBoolean(1, true);
			stat.setInt(2, id);
			stat.executeUpdate();
			conn.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void removeUser( int id )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "UPDATE User SET IsRemoved=? WHERE UserID=?";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setBoolean(1, true);
			stat.setInt(2, id);
			stat.executeUpdate();
			conn.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public void removeResponse( int apptID )
	{
		try
		{
			Class.forName("com.mysql.jdbc.Driver");
			Connection conn = DriverManager.getConnection( url, accountName, password );
			String sql = "UPDATE Response SET Response=? WHERE ApptID=?";
			PreparedStatement stat = conn.prepareStatement(sql);
			stat.setString(1, Response.REMOVE);
			stat.setInt(2, apptID);
			stat.executeUpdate();
			conn.close();
		}
		catch( Exception e )
		{
			e.printStackTrace();
		}
	}

	public Appt getAppt(int apptID)
	{
		try
		{
			ResultSet rs = executeSQL( "SELECT * FROM Appointment WHERE ApptID = "+apptID );
			rs.next();
			Appt a = new Appt();
			a.setApptID(rs.getInt("ApptID"));
			a.setTitle(rs.getString("Title"));
			a.setInfo(rs.getString("Info"));
			Timestamp start = rs.getTimestamp("StartTime");
			Timestamp end = rs.getTimestamp("EndTime");
			TimeSpan d = new TimeSpan(start, end);
			a.setTimeSpan(d);
			a.setInitUserID(rs.getInt("InitUserID"));
			a.setLocation(this.getLocation(rs.getInt("LocationID")));
			a.setReminderID(rs.getInt("ReminderID"));
			a.setFreqGroupID(rs.getInt("FreqGroupID"));
			a.setPublic(rs.getBoolean("IsPublic"));
			a.setJoint(rs.getBoolean("IsJoint"));
			a.setIsScheduled(rs.getBoolean("IsScheduled"));
			rs.close();
			return a;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public ArrayList<Appt> getApptList(int userID)
	{
		ArrayList<Appt> apptList = new ArrayList<Appt>();
		try
		{
			/*ResultSet rs = executeSQL( "SELECT * FROM Appointment A, Response R WHERE A.ApptID = R.ApptID AND "
					+ "Response='"+Response.INDIV+"' AND A.IsRemoved = FALSE AND R.UserID="+userID );*/
			ResultSet rs = executeSQL( "SELECT * FROM Appointment WHERE IsRemoved = FALSE AND IsJoint = FALSE AND InitUserID="+userID );
			while(rs.next())
			{
				Appt a = new Appt();
				a.setApptID(rs.getInt("ApptID"));
				a.setTitle(rs.getString("Title"));
				a.setInfo(rs.getString("Info"));
				Timestamp start = rs.getTimestamp("StartTime");
				Timestamp end = rs.getTimestamp("EndTime");
				TimeSpan d = new TimeSpan(start, end);
				a.setTimeSpan(d);
				a.setInitUserID(rs.getInt("InitUserID"));
				a.setLocation(this.getLocation(rs.getInt("LocationID")));
				a.setReminderID(rs.getInt("ReminderID"));
				a.setFreqGroupID(rs.getInt("FreqGroupID"));
				a.setPublic(rs.getBoolean("IsPublic"));
				a.setJoint(rs.getBoolean("IsJoint"));
				a.setIsScheduled(rs.getBoolean("IsScheduled"));
				apptList.add( a );
			}
			rs.close();
			rs = executeSQL( "SELECT * FROM Appointment WHERE IsRemoved = FALSE AND IsJoint = TRUE AND IsScheduled = TRUE AND InitUserID="+userID );
			while(rs.next())
			{
				Appt a = new Appt();
				a.setApptID(rs.getInt("ApptID"));
				a.setTitle(rs.getString("Title"));
				a.setInfo(rs.getString("Info"));
				Timestamp start = rs.getTimestamp("StartTime");
				Timestamp end = rs.getTimestamp("EndTime");
				TimeSpan d = new TimeSpan(start, end);
				a.setTimeSpan(d);
				a.setInitUserID(rs.getInt("InitUserID"));
				a.setLocation(this.getLocation(rs.getInt("LocationID")));
				a.setReminderID(rs.getInt("ReminderID"));
				a.setFreqGroupID(rs.getInt("FreqGroupID"));
				a.setPublic(rs.getBoolean("IsPublic"));
				a.setJoint(rs.getBoolean("IsJoint"));
				a.setIsScheduled(rs.getBoolean("IsScheduled"));
				apptList.add( a );
			}
			rs.close();
			rs = executeSQL( "SELECT * FROM Appointment A, Response R WHERE A.ApptID = R.ApptID AND "
					+ "A.IsRemoved = FALSE AND A.IsJoint = TRUE AND A.IsScheduled = TRUE AND R.UserID="+userID );
			while(rs.next())
			{
				Appt a = new Appt();
				a.setApptID(rs.getInt("ApptID"));
				a.setTitle(rs.getString("Title"));
				a.setInfo(rs.getString("Info"));
				Timestamp start = rs.getTimestamp("StartTime");
				Timestamp end = rs.getTimestamp("EndTime");
				TimeSpan d = new TimeSpan(start, end);
				a.setTimeSpan(d);
				a.setInitUserID(rs.getInt("InitUserID"));
				a.setLocation(this.getLocation(rs.getInt("LocationID")));
				a.setReminderID(rs.getInt("ReminderID"));
				a.setFreqGroupID(rs.getInt("FreqGroupID"));
				a.setPublic(rs.getBoolean("IsPublic"));
				a.setJoint(rs.getBoolean("IsJoint"));
				a.setIsScheduled(rs.getBoolean("IsScheduled"));
				apptList.add( a );
			}
			return apptList;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public ArrayList<Appt> getApptList(ArrayList<String> userLoginIDList)
	{
		ArrayList<Integer> userIDList = new ArrayList<Integer>();
		ArrayList<Appt> apptList = new ArrayList<Appt>();
		for( int i = 0; i < userLoginIDList.size(); i++ )
		{
			try {
				ResultSet rs = executeSQL("SELECT UserID FROM User WHERE LoginID='"+userLoginIDList.get(i)+"' AND IsRemoved=FALSE");
				rs.next();
				userIDList.add(rs.getInt("UserID"));
				rs.close();
			} catch (SQLException e) { e.printStackTrace(); }
			
		}
		for( int i = 0; i < userIDList.size(); i++ )
		{
			ArrayList<Appt> tempList = getApptList(userIDList.get(i));
			for( Appt a : tempList )
				if( !isExistInArrayList(apptList, a) )
					apptList.add(a);
		}
		return apptList;
	}

	public ArrayList<Appt> getLocationApptList(int locationID)
	{
		ArrayList<Appt> apptList = new ArrayList<Appt>();
		try
		{
			ResultSet rs = executeSQL("SELECT * FROM Appointment WHERE LocationID="+locationID+" AND IsRemoved=FALSE");
			while(rs.next())
			{
				Appt a = new Appt();
				a.setApptID(rs.getInt("ApptID"));
				a.setTitle(rs.getString("Title"));
				a.setInfo(rs.getString("Info"));
				Timestamp start = rs.getTimestamp("StartTime");
				Timestamp end = rs.getTimestamp("EndTime");
				TimeSpan d = new TimeSpan(start, end);
				a.setTimeSpan(d);
				a.setInitUserID(rs.getInt("InitUserID"));
				a.setLocation(this.getLocation(rs.getInt("LocationID")));
				a.setReminderID(rs.getInt("ReminderID"));
				a.setFreqGroupID(rs.getInt("FreqGroupID"));
				a.setPublic(rs.getBoolean("IsPublic"));
				a.setJoint(rs.getBoolean("IsJoint"));
				a.setIsScheduled(rs.getBoolean("IsScheduled"));
				apptList.add( a );
			}
			rs.close();
			return apptList;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public ArrayList<Appt> getInitiateApptList(int userID)
	{
		ArrayList<Appt> apptList = new ArrayList<Appt>();
		try
		{
			ResultSet rs = executeSQL("SELECT * FROM Appointment WHERE InitUserID="+userID+" AND IsJoint=true AND IsRemoved=FALSE");
			while(rs.next())
			{
				Appt a = new Appt();
				a.setApptID(rs.getInt("ApptID"));
				a.setTitle(rs.getString("Title"));
				a.setInfo(rs.getString("Info"));
				Timestamp start = rs.getTimestamp("StartTime");
				Timestamp end = rs.getTimestamp("EndTime");
				TimeSpan d = new TimeSpan(start, end);
				a.setTimeSpan(d);
				a.setInitUserID(rs.getInt("InitUserID"));
				a.setLocation(this.getLocation(rs.getInt("LocationID")));
				a.setReminderID(rs.getInt("ReminderID"));
				a.setFreqGroupID(rs.getInt("FreqGroupID"));
				a.setPublic(rs.getBoolean("IsPublic"));
				a.setJoint(rs.getBoolean("IsJoint"));
				a.setIsScheduled(rs.getBoolean("IsScheduled"));
				apptList.add( a );
			}
			rs.close();
			return apptList;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public ArrayList<Appt> getApptResponseList(int userID, String response)
	{
		ArrayList<Appt> apptList = new ArrayList<Appt>();
		try
		{
			ResultSet rs = executeSQL( "SELECT * FROM Appointment A, Response R WHERE A.ApptID = R.ApptID AND "
					+ "Response='"+response+"' AND A.IsRemoved = FALSE AND R.UserID="+userID );
			while(rs.next())
			{
				Appt a = new Appt();
				a.setApptID(rs.getInt("ApptID"));
				a.setTitle(rs.getString("Title"));
				a.setInfo(rs.getString("Info"));
				Timestamp start = rs.getTimestamp("StartTime");
				Timestamp end = rs.getTimestamp("EndTime");
				TimeSpan d = new TimeSpan(start, end);
				a.setTimeSpan(d);
				a.setInitUserID(rs.getInt("InitUserID"));
				a.setLocation(this.getLocation(rs.getInt("LocationID")));
				a.setReminderID(rs.getInt("ReminderID"));
				a.setFreqGroupID(rs.getInt("FreqGroupID"));
				a.setPublic(rs.getBoolean("IsPublic"));
				a.setJoint(rs.getBoolean("IsJoint"));
				a.setIsScheduled(rs.getBoolean("IsScheduled"));
				apptList.add( a );
			}
			rs.close();
			return apptList;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public ArrayList<Appt> getPublicApptList()
	{
		ArrayList<Appt> apptList = new ArrayList<Appt>();
		try
		{
			ResultSet rs = executeSQL("SELECT * FROM Appointment WHERE IsPublic=TRUE AND IsRemoved=FALSE");
			while(rs.next())
			{
				Appt a = new Appt();
				a.setApptID(rs.getInt("ApptID"));
				a.setTitle(rs.getString("Title"));
				a.setInfo(rs.getString("Info"));
				Timestamp start = rs.getTimestamp("StartTime");
				Timestamp end = rs.getTimestamp("EndTime");
				TimeSpan d = new TimeSpan(start, end);
				a.setTimeSpan(d);
				a.setInitUserID(rs.getInt("InitUserID"));
				a.setLocation(this.getLocation(rs.getInt("LocationID")));
				a.setReminderID(rs.getInt("ReminderID"));
				a.setFreqGroupID(rs.getInt("FreqGroupID"));
				a.setPublic(rs.getBoolean("IsPublic"));
				a.setJoint(rs.getBoolean("IsJoint"));
				a.setIsScheduled(rs.getBoolean("IsScheduled"));
				apptList.add( a );
			}
			rs.close();
			return apptList;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public ArrayList<Integer> getApptGroupIDList(int freGroupID)
	{
		ArrayList<Integer> idList = new ArrayList<Integer>();
		try
		{
			ResultSet rs = executeSQL("SELECT ApptID FROM Appointment WHERE FreqGroupID="+freGroupID);
			while(rs.next())
			{
				idList.add( rs.getInt("ApptID") );
			}
			rs.close();
			return idList;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}
	
	public Reminder getReminder(int rid)
	{
		try
		{
			ResultSet rs = executeSQL( "SELECT ApptID FROM Appointment WHERE ReminderID = "+rid );
			rs.next();
			int apptID = rs.getInt("ApptID");
			rs = executeSQL( "SELECT * FROM Reminder WHERE ReminderID = "+rid );
			rs.next();
			Reminder r = new Reminder();
			r.setApptID(apptID);
			r.setID(rid);
			Timestamp start = rs.getTimestamp("StartTime");
			Timestamp end = rs.getTimestamp("EndTime");
			TimeSpan d = new TimeSpan(start, end);
			r.setTimeSpan(d);
			r.setHourInterval(rs.getInt("HourInterval"));
			r.setMinuteInterval(rs.getInt("MinuteInterval"));
			r.setIsReminded(rs.getBoolean("IsReminded"));
			rs.close();
			return r;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public ArrayList<Reminder> getReminderList(ArrayList<Appt> apptList)
	{
		ArrayList<Reminder> reminderList = new ArrayList<Reminder>();
		try
		{
			for( Appt a : apptList )
			{
				if( a.getReminderID() == -1 )
					continue;
				ResultSet rs = executeSQL( "SELECT * FROM Reminder WHERE IsRemoved = FALSE AND IsReminded = FALSE AND ReminderID="+a.getReminderID() );
				while(rs.next())
				{
					Reminder r = new Reminder();
					r.setApptID(a.getID());
					r.setID(a.getReminderID());
					Date s = rs.getDate("StartTime");
					Timestamp start = rs.getTimestamp("StartTime");
					Timestamp end = rs.getTimestamp("EndTime");
					TimeSpan d = new TimeSpan(start, end);
					r.setTimeSpan(d);
					r.setHourInterval(rs.getInt("HourInterval"));
					r.setMinuteInterval(rs.getInt("MinuteInterval"));
					r.setIsReminded(rs.getBoolean("IsReminded"));
					reminderList.add( r );
				}
				rs.close();
			}
			return reminderList;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public Location getLocation( int locationID )
	{
		try
		{
			ResultSet rs = executeSQL( "SELECT * FROM Location WHERE IsRemoved = FALSE AND LocationID = " + locationID );
			rs.next();
			Location l = new Location(rs.getString("Name"), rs.getString("GroupName"), rs.getInt("Capacity"));
			l.setIsGroup(rs.getBoolean("IsGroup"));
			rs.close();
			return l;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public ArrayList<Location> getLocationList()
	{
		try
		{
			ArrayList<Location> locationList = new ArrayList<Location>();
			ResultSet rs = executeSQL( "SELECT * FROM Location WHERE IsRemoved = FALSE" );
			while(rs.next())
			{
				Location l = new Location(rs.getString("Name"), rs.getString("GroupName"), rs.getInt("Capacity"));
				l.setID(rs.getInt("LocationID"));
				l.setIsGroup(rs.getBoolean("IsGroup"));
				locationList.add(l);
			}
			rs.close();
			return locationList;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public User getUser( int userID )
	{
		try
		{
			ResultSet rs = executeSQL( "SELECT * FROM User WHERE IsRemoved = FALSE AND UserID = " + userID );
			User user = null;
			if( rs.next() )
				user = new User(userID, rs.getString("FirstName"), rs.getString("LastName"), rs.getString("LoginID"), rs.getString("Password"), rs.getString("Email"), rs.getInt("UserType"));
			rs.close();
			return user;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public User getUser( String loginID )
	{
		try
		{
			ResultSet rs = executeSQL( "SELECT * FROM User WHERE IsRemoved = FALSE AND LoginID = '" + loginID + "'" );
			User user = null;
			if( rs.next() )
				user = new User(rs.getInt("UserID"), rs.getString("FirstName"), rs.getString("LastName"), loginID, rs.getString("Password"), rs.getString("Email"), rs.getInt("UserType"));
			rs.close();
			return user;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public ArrayList<User> getUserList()
	{
		ArrayList<User> userList = new ArrayList<User>();
		try
		{
			ResultSet rs = executeSQL( "SELECT * FROM User" );
			while(rs.next())
			{
				User user = new User(rs.getInt("UserID"), rs.getString("FirstName"), rs.getString("LastName"), rs.getString("LoginID"), rs.getString("Password"), rs.getString("Email"), rs.getInt("UserType"));
				userList.add( user );
			}
			rs.close();
			return userList;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public ArrayList<Response> getResponseList(int apptID)
	{
		ArrayList<Response> resList = new ArrayList<Response>();
		try
		{
			ResultSet rs = executeSQL( "SELECT * FROM Response R, User U WHERE R.UserID = U.UserID AND R.ApptID="+apptID );
			while(rs.next())
			{
				Response res = new Response(rs.getInt("ApptID"), rs.getInt("UserID"), rs.getString("LoginID"), rs.getString("Response"));
				resList.add( res );
			}
			rs.close();
			return resList;
		}
		catch( SQLException e ) { e.printStackTrace(); return null; }
	}

	public ResultSet executeSQL( String sql )
	{
		try
		{
			//Class.forName( "com.mysql.jdbc.Driver" );
			//Connection conn = DriverManager.getConnection( url, accountName, password );
			Statement stat = conn.createStatement();
			return stat.executeQuery( sql );
		}
		catch( Exception e )
		{
			e.printStackTrace();
			return null;
		}
	}

	private boolean isExistInArrayList(ArrayList<Appt> apptList, Appt a)
	{
		for( Appt a1 : apptList )
			if( a1.getID() == a.getID() )
				return true;
		return false;
	}
}