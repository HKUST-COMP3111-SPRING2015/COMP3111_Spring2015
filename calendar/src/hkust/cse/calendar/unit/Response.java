package hkust.cse.calendar.unit;

public class Response
{
	private int apptID;
	private int userID;
	private String userLoginID;
	private String response;
	public static final String NEW = "New", ACCEPT = "Accept", REJECT = "Reject", REMOVE = "Remove", INDIV = "Indiv";

	public Response(int apptID, int userID, String userLoginID, String response)
	{
		this.apptID = apptID;
		this.userID = userID;
		this.userLoginID = userLoginID;
		this.response = response;
	}

	public int getApptID() { return apptID; }
	public int getUserID() { return userID; }
	public String getUserLoginID() { return userLoginID; }
	public String getResponse() { return response; }

	public void setApptID(int apptID) { this.apptID = apptID; }
	public void setUserID(int userID) { this.userID = userID; }
	public void setUserLoginID(String userLoginID) { this.userLoginID = userLoginID; }
	public void setResponse(String response) { this.response = response; }
}
