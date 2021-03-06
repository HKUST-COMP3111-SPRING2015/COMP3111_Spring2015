package hkust.cse.calendar.gui;

import hkust.cse.calendar.Main.CalendarMain;
import hkust.cse.calendar.apptstorage.Controller;
import hkust.cse.calendar.apptstorage.Model;
import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Reminder;
import hkust.cse.calendar.unit.RemoveMessage;
import hkust.cse.calendar.unit.Response;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.plaf.basic.BasicArrowButton;
import javax.swing.plaf.metal.MetalBorders.Flush3DBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;


public class CalGrid extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private CalendarMain main;
	private Controller ctrl;
	private Model model;
	public User curUser;
	private String mCurrTitle = "Desktop Calendar - No User - ";
	private GregorianCalendar today;
	public int currentD;
	public int currentM;
	public int currentY;
	public int previousRow;
	public int previousCol;
	public int currentRow;
	public int currentCol;
	private BasicArrowButton eButton;
	private BasicArrowButton wButton;
	private JLabel year;
	private JComboBox month;

	private final Object[][] data = new Object[6][7];
	//private final Vector[][] apptMarker = new Vector[6][7];
	private final String[] names = { "Sunday", "Monday", "Tuesday",
			"Wednesday", "Thursday", "Friday", "Saturday" };
	private final String[] months = { "January", "Feburary", "March", "April",
			"May", "June", "July", "August", "September", "October",
			"November", "December" };
	private JTable tableView;
	private AppList applist;
	public static final int[] monthDays = { 31, 28, 31, 30, 31, 30, 31, 31, 30,
			31, 30, 31 };
	private JTextPane note;

	private JSplitPane upper;
	private JSplitPane whole;
	private JScrollPane scrollpane;
	private StyledDocument mem_doc = null;
	private SimpleAttributeSet sab = null;
	private JMenu Appmenu;

	private final String[] holidays = {
			"New Years Day\nSpring Festival\n",
			"President's Day (US)\n",
			"",
			"Ching Ming Festival\nGood Friday\nThe day following Good Friday\nEaster Monday\n",
			"Labour Day\nThe Buddha���s Birthday\nTuen Ng Festival\n",
			"",
			"Hong Kong Special Administrative Region Establishment Day\n",
			"Civic Holiday(CAN)\n",
			"",
			"National Day\nChinese Mid-Autumn Festival\nChung Yeung Festival\nThanksgiving Day\n",
			"Veterans Day(US)\nThanksgiving Day(US)\n", "Christmas\n" };

	private Thread timeThread;
	private boolean isApptHappening = false;
	private boolean isAdmin = false;

	public CalGrid(CalendarMain main)
	{
		super();
		this.main = main;
		this.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		this.setVisible(false);
	}

	public void startMainProgram(String loginID)
	{
		curUser = model.getUser(loginID);
		ArrayList<RemoveMessage> msgList = model.getRemoveMessage(curUser.getID());
		for( RemoveMessage msg : msgList )
		{
			String s = "";
			if( msg.getUserID() != -1 )
			{
				User u = model.getUser(msg.getUserID());
				s += "User "+ u.getLoginID() +" will be deleted, is it OK?";
			}
			else if( msg.getLocationID() != -1 )
			{
				Location l = model.getLocation(msg.getLocationID());
				s += "Location "+ l.getName() +" will be deleted, is it OK?";
			}
			
			int n = JOptionPane.showConfirmDialog(null, s, "Remove", JOptionPane.YES_NO_OPTION);
			if (n == JOptionPane.YES_OPTION)
			{
				if( msg.getUserID() != -1 )
					model.updateUserToRemoveTable(msg.getUserID(), msg.getInformID(), true);
				else
					model.updateLocationToRemoveTable(msg.getLocationID(), msg.getInformID(), true);
			}
			else
			{
				if( msg.getUserID() != -1 )
					model.updateUserToRemoveTable(msg.getUserID(), msg.getInformID(), false);
				else
					model.updateLocationToRemoveTable(msg.getLocationID(), msg.getInformID(), false);
			}
		}

		if( curUser.getUserType() == User.ADMIN )
			isAdmin = true; 
		previousRow = 0;
		previousCol = 0;
		currentRow = 0;
		currentCol = 0;
		
		applist = new AppList();
		applist.setParent(this);

		this.setJMenuBar(null);
		setJMenuBar(createMenuBar());

		today = new GregorianCalendar();
		currentY = today.get(Calendar.YEAR);
		currentD = today.get(today.DAY_OF_MONTH);
		int temp = today.get(today.MONTH) + 1;
		currentM = 12;

		getDateArray(data);

		JPanel leftP = new JPanel();
		leftP.setLayout(new BorderLayout());
		leftP.setPreferredSize(new Dimension(500, 300));

		JLabel textL = new JLabel("Important Days");
		textL.setForeground(Color.red);

		note = new JTextPane();
		note.setEditable(false);
		note.setBorder(new Flush3DBorder());
		mem_doc = note.getStyledDocument();
		sab = new SimpleAttributeSet();
		StyleConstants.setBold(sab, true);
		StyleConstants.setFontSize(sab, 30);

		JPanel noteP = new JPanel();
		noteP.setLayout(new BorderLayout());
		noteP.add(textL, BorderLayout.NORTH);
		noteP.add(note, BorderLayout.CENTER);

		leftP.add(noteP, BorderLayout.CENTER);

		eButton = new BasicArrowButton(SwingConstants.EAST);
		eButton.setEnabled(true);
		eButton.addActionListener(this);
		wButton = new BasicArrowButton(SwingConstants.WEST);
		wButton.setEnabled(true);
		wButton.addActionListener(this);

		year = new JLabel(new Integer(currentY).toString());
		month = new JComboBox();
		month.addActionListener(this);
		month.setPreferredSize(new Dimension(200, 30));
		for (int cnt = 0; cnt < 12; cnt++)
			month.addItem(months[cnt]);
		month.setSelectedIndex(temp - 1);

		JPanel yearGroup = new JPanel();
		yearGroup.setLayout(new FlowLayout());
		yearGroup.setBorder(new Flush3DBorder());
		yearGroup.add(wButton);
		yearGroup.add(year);
		yearGroup.add(eButton);
		yearGroup.add(month);

		leftP.add(yearGroup, BorderLayout.NORTH);
		
		TableModel dataModel = prepareTableModel();
		tableView = new JTable(dataModel) {
			public TableCellRenderer getCellRenderer(int row, int col) {
				String tem = (String) data[row][col];

				if (tem.equals("") == false) {
					try {
						CalCellRenderer ccr = new CalCellRenderer(null);
						Timestamp start = new Timestamp(0);
						start.setYear(currentY);
						start.setMonth(currentM-1);
						start.setDate(Integer.parseInt(tem));
						start.setHours(0);
						start.setMinutes(0);
						start.setSeconds(0);
						
						Timestamp end = new Timestamp(0);
						end.setYear(currentY);
						end.setMonth(currentM-1);
						end.setDate(Integer.parseInt(tem));
						end.setHours(23);
						end.setMinutes(59);
						end.setSeconds(59);
						
						TimeSpan period = new TimeSpan(start, end);
						//Appt[] isAppt = controller.RetrieveAppts(curUser,period);
						boolean tempB = ctrl.isApptExistInPeriod(curUser, period);
						if( tempB )
							ccr.setBackground(new Color(228, 175, 245));
						if (today.get(Calendar.YEAR) == currentY && today.get(today.MONTH) + 1 == currentM && today.get(today.DAY_OF_MONTH) == Integer.parseInt(tem))
						{
							CalCellRenderer tccr = new CalCellRenderer(today);
							if( isApptHappening )
								tccr.setBackground(new Color(131, 210, 237));
							else if( tempB )
								tccr.setBackground(new Color(228, 175, 245));
							return tccr;
						}
						return ccr;
					} catch (Throwable e) {
						System.exit(1);
					}

				}
				return new CalCellRenderer(null);
			}
		};

		tableView.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		tableView.setRowHeight(40);
		tableView.setRowSelectionAllowed(false);
		tableView.addMouseListener(new MouseAdapter() {

			public void mousePressed(MouseEvent e) {
				mousePressResponse();
			}

			public void mouseReleased(MouseEvent e) {
				mouseResponse();
			}
		});

		JTableHeader head = tableView.getTableHeader();
		head.setReorderingAllowed(false);
		head.setResizingAllowed(true);

		scrollpane = new JScrollPane(tableView);
		scrollpane.setBorder(new BevelBorder(BevelBorder.RAISED));
		scrollpane.setPreferredSize(new Dimension(536, 260));

		upper = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftP, scrollpane);
		whole = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upper, applist);
		getContentPane().add(whole);
		//Appmenu.setEnabled(true);

		startTimeThread();
		UpdateCal();
		pack();				// sized the window to a preferred size
		this.setVisible(true);	//set the window to be visible
	}

	public TableModel prepareTableModel() {

		TableModel dataModel = new AbstractTableModel() {

			public int getColumnCount() {
				return names.length;
			}

			public int getRowCount() {
				return 6;
			}

			public Object getValueAt(int row, int col) {
				return data[row][col];
			}

			public String getColumnName(int column) {
				return names[column];
			}

			public Class getColumnClass(int c) {
				return getValueAt(0, c).getClass();
			}

			public boolean isCellEditable(int row, int col) {
				return false;
			}

			public void setValueAt(Object aValue, int row, int column) {
				System.out.println("Setting value to: " + aValue);
				data[row][column] = aValue;
			}
		};
		return dataModel;
	}

	private void startTimeThread()
	{
		timeThread = new Thread()
		{
			@Override
			public void run()
			{
				while(true)
				{
					Timestamp start = new Timestamp(0);
					start.setYear(currentY);
					start.setMonth(currentM-1);
					start.setDate(currentD);
					start.setHours(0);
					start.setMinutes(0);
					start.setSeconds(0);
					
					Timestamp end = new Timestamp(0);
					end.setYear(currentY);
					end.setMonth(currentM-1);
					end.setDate(currentD);
					end.setHours(23);
					end.setMinutes(59);
					end.setSeconds(59);
					
					TimeSpan period = new TimeSpan(start, end);
					ArrayList<Appt> appts = model.getApptList(curUser.getID());
					ArrayList<Reminder> reminders = model.getReminderList(appts);
					//Appt[] appts = controller.RetrieveAppts(curUser,period);
					//ArrayList<Reminder> reminders = controller.getReminders(curUser,period);
					today.add(Calendar.SECOND, 1);
					//System.out.println(today.get(Calendar.HOUR)+":"+today.get(Calendar.MINUTE)+":"+today.get(Calendar.SECOND)+"  A-Size:"+appts.length+"  R-Size:"+reminders.size());
					boolean tBool = isApptHappening;
					isApptHappening = false;
					for( int i = 0; i < appts.size(); i++ )
					{
						Appt a = appts.get(i);
						if( a.TimeSpan().StartTime().before(today.getTime()) && a.TimeSpan().EndTime().after(today.getTime()) )
						{
							isApptHappening = true;
							break;
						}
					}
					if( today.get(Calendar.HOUR)+today.get(Calendar.MINUTE)+today.get(Calendar.SECOND) == 0 || tBool != isApptHappening )
						tableView.repaint();
					for( int i = 0; i < reminders.size(); i++ )
					{
						Reminder r = reminders.get( i );
						if( r.getTimeSpan().StartTime().before(today.getTime()) && r.getTimeSpan().EndTime().after(today.getTime()) )
							popupReminder(r);
					}
					try
					{
						sleep(1000);
					}
					catch(InterruptedException e)
					{
						//e.printStackTrace();
					}
				}
			}
		};
		timeThread.start();
	}

	public void getDateArray(Object[][] data) {
		GregorianCalendar c = new GregorianCalendar(currentY, currentM - 1, 1);
		int day;
		int date;
		Date d = c.getTime();
		c.setTime(d);
		day = d.getDay();
		date = d.getDate();

		if (c.isLeapYear(currentY)) {

			monthDays[1] = 29;
		} else
			monthDays[1] = 28;

		int temp = day - date % 7;
		if (temp > 0)
			day = temp + 1;
		else if (temp < 0)
			day = temp + 1 + 7;
		else
			day = date % 7;
		day %= 7;
		for (int i = 0; i < 6; i++)
			for (int j = 0; j < 7; j++) {
				int temp1 = i * 7 + j - day + 1;
				if (temp1 > 0 && temp1 <= monthDays[currentM - 1])
					data[i][j] = new Integer(temp1).toString();
				else
					data[i][j] = new String("");
			}

	}

	private JMenuBar createMenuBar() {

		ActionListener listener = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Manual Scheduling")) {
					AppScheduler a = new AppScheduler("New", CalGrid.this);
					a.updateSetApp(hkust.cse.calendar.gui.Utility
							.createDefaultAppt(currentY, currentM, currentD,
									curUser));
					a.setLocationRelativeTo(null);
					a.show();
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
				}

			}
		};
		JMenuBar menuBar = new JMenuBar();
		menuBar.getAccessibleContext().setAccessibleName("Calendar Choices");
		JMenuItem mi;

		JMenu Access = (JMenu) menuBar.add(new JMenu("Access"));
		Access.setMnemonic('A');
		Access.getAccessibleContext().setAccessibleDescription(
				"Account Access Management");

		mi = (JMenuItem) Access.add(new JMenuItem("Logout"));	//adding a Logout menu button for user to logout
		mi.setMnemonic('L');
		mi.getAccessibleContext().setAccessibleDescription("For user logout");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "Logout?",
						"Comfirm", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION){
					logout();
					//dispose();
					//CalendarMain.logOut = true;
					//TODO: Logout
					return;	//return to CalendarMain()
				}
			}
		});
		
		mi = (JMenuItem) Access.add(new JMenuItem("Exit"));
		mi.setMnemonic('E');
		mi.getAccessibleContext().setAccessibleDescription("Exit Program");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int n = JOptionPane.showConfirmDialog(null, "Exit Program ?",
						"Comfirm", JOptionPane.YES_NO_OPTION);
				if (n == JOptionPane.YES_OPTION)
					System.exit(0);

			}
		});

		JMenu Appmenu = (JMenu) menuBar.add(new JMenu("Appointment"));
		//menuBar.add(Appmenu);
		Appmenu.setEnabled(true);
		Appmenu.setMnemonic('p');
		Appmenu.getAccessibleContext().setAccessibleDescription(
				"Appointment Management");
		mi = new JMenuItem("Manual Scheduling");
		mi.addActionListener(listener);
		Appmenu.add(mi);

		mi = new JMenuItem("My Group Appointments");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MyAppointmentManagementDialog amd = new MyAppointmentManagementDialog( today, getCalGrid(), curUser );
			}
		});
		Appmenu.add(mi);
		
		
		if( isAdmin )
		{
			mi = new JMenuItem("Manage Locations");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AdminLocationsDialog ld = new AdminLocationsDialog( getCalGrid() );
				}
			});
			Appmenu.add(mi);

			mi = new JMenuItem("Manage Users");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AdminUserDialog aud = new AdminUserDialog( getCalGrid(), curUser );
				}
			});
			Appmenu.add(mi);

			/*mi = new JMenuItem("Manage All Appointments");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					AdminAppointmentManagementDialog aud = new AdminAppointmentManagementDialog( today, getCalGrid(), curUser );
				}
			});
			Appmenu.add(mi);*/
		}
		else
		{
			mi = new JMenuItem("Account Settings");
			mi.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					NormalUserDialog nud = new NormalUserDialog( getCalGrid(), curUser );
				}
			});
			Appmenu.add(mi);
		}
		
		mi = new JMenuItem("Public Appointments");
		mi.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				PublicAppointmentDialog pad = new PublicAppointmentDialog( today, getCalGrid(), curUser );
			}
		});
		Appmenu.add(mi);
		

		JMenu SystemMenu = (JMenu) menuBar.add(new JMenu("System"));
		SystemMenu.setMnemonic('S');
		SystemMenu.getAccessibleContext().setAccessibleDescription(
				"System Configuration");

		mi = (JMenuItem) SystemMenu.add(new JMenuItem("Time Machine"));	//Time machine to control the time of the system
		mi.setMnemonic('T');
		mi.getAccessibleContext().setAccessibleDescription("Control the time");
		class TimeMachineMenuItemActionListener implements ActionListener
		{
			private CalGrid calGrid;

			public TimeMachineMenuItemActionListener(CalGrid calGrid)
			{
				this.calGrid = calGrid;
			}

			@Override
			public void actionPerformed(ActionEvent e) {
				ClockDialog cd = new ClockDialog( calGrid, today.getTime() );
				//tableView.repaint();
			}
		};
		TimeMachineMenuItemActionListener timeMachineMenuItemListener = new TimeMachineMenuItemActionListener(this);
		mi.addActionListener(timeMachineMenuItemListener);
		return menuBar;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == eButton) {
			if (year == null)
				return;
			currentY = currentY + 1;
			year.setText(new Integer(currentY).toString());
			CalGrid.this.setTitle("Desktop Calendar - No User - (" + currentY
					+ "-" + currentM + "-" + currentD + ")");
			getDateArray(data);
			if (tableView != null) {
				TableModel t = prepareTableModel();
				tableView.setModel(t);
				tableView.repaint();

			}
			UpdateCal();
		} else if (e.getSource() == wButton) {
			if (year == null)
				return;
			currentY = currentY - 1;
			year.setText(new Integer(currentY).toString());
			CalGrid.this.setTitle("Desktop Calendar - No User - (" + currentY
					+ "-" + currentM + "-" + currentD + ")");
			getDateArray(data);
			if (tableView != null) {
				TableModel t = prepareTableModel();
				tableView.setModel(t);
				tableView.repaint();

			}
			UpdateCal();
		} else if (e.getSource() == month) {
			if (month.getSelectedItem() != null) {
				currentM = month.getSelectedIndex() + 1;
				try {
					mem_doc.remove(0, mem_doc.getLength());
					mem_doc.insertString(0, holidays[currentM - 1], sab);
				} catch (BadLocationException e1) {

					e1.printStackTrace();
				}

				CalGrid.this.setTitle("Desktop Calendar - No User - ("
						+ currentY + "-" + currentM + "-" + currentD + ")");
				getDateArray(data);
				if (tableView != null) {
					TableModel t = prepareTableModel();
					tableView.setModel(t);
					tableView.repaint();
					UpdateCal();
				}
				//UpdateCal();TODO: Changed BY Alton
			}
		}
	}

	// update the appointment list on gui
	public void updateAppList() {
		applist.clear();
		applist.repaint();
		applist.setTodayAppt(GetTodayAppt());
	}

	public void UpdateCal() {
		if (curUser != null) {
			mCurrTitle = "Desktop Calendar - " + curUser.getLoginID() + " - ";
			this.setTitle(mCurrTitle + "(" + currentY + "-" + currentM + "-"
					+ currentD + ")");
			Appt[] monthAppts = null;
			GetMonthAppts();

//			for (int i = 0; i < 6; i++)
//				for (int j = 0; j < 7; j++)
//					apptMarker[i][j] = new Vector(10, 1);

			TableModel t = prepareTableModel();
			//buildTable(t);
			this.tableView.setModel(t);
			this.tableView.repaint();
			updateAppList();
		}
	}

//	public void clear() {
//		for (int i = 0; i < 6; i++)
//			for (int j = 0; j < 7; j++)
//				apptMarker[i][j] = new Vector(10, 1);
//		TableModel t = prepareTableModel();
//		tableView.setModel(t);
//		tableView.repaint();
//		applist.clear();
//	}

	private Appt[] GetMonthAppts() {
		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM - 1);
		start.setDate(1);
		start.setHours(0);
		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM - 1);
		GregorianCalendar g = new GregorianCalendar(currentY, currentM - 1, 1);
		end.setDate(g.getActualMaximum(GregorianCalendar.DAY_OF_MONTH));
		end.setHours(23);
		TimeSpan period = new TimeSpan(start, end);
		ArrayList<Appt> appts = ctrl.getApptList(curUser.getID(), period);
		Appt[] array = new Appt[appts.size()];
		for( int i = 0; i < appts.size(); i++ )
			array[i] = appts.get(i);
		return array;
		//return controller.RetrieveAppts(curUser, period);
	}

	private void mousePressResponse() {
		previousRow = tableView.getSelectedRow();
		previousCol = tableView.getSelectedColumn();
	}
	
	private void mouseResponse() {
		int[] selectedRows = tableView.getSelectedRows();
		int[] selectedCols = tableView.getSelectedColumns();
		if(tableView.getSelectedRow() == previousRow && tableView.getSelectedColumn() == previousCol){
			currentRow = selectedRows[selectedRows.length - 1];
			currentCol = selectedCols[selectedCols.length - 1];
		}
		else if(tableView.getSelectedRow() != previousRow && tableView.getSelectedColumn() == previousCol){
			currentRow = tableView.getSelectedRow();
			currentCol = selectedCols[selectedCols.length - 1];
		}
		else if(tableView.getSelectedRow() == previousRow && tableView.getSelectedColumn() != previousCol){
			currentRow = selectedRows[selectedRows.length - 1];
			currentCol = tableView.getSelectedColumn();
		}
		else{
			currentRow = tableView.getSelectedRow();
			currentCol = tableView.getSelectedColumn();
		}
		
		if (currentRow > 5 || currentRow < 0 || currentCol < 0
				|| currentCol > 6)
			return;

		if (tableView.getModel().getValueAt(currentRow, currentCol) != "")
			try {
				currentD = new Integer((String) tableView.getModel()
						.getValueAt(currentRow, currentCol)).intValue();
			} catch (NumberFormatException n) {
				return;
			}
		CalGrid.this.setTitle(mCurrTitle + "(" + currentY + "-" + currentM
				+ "-" + currentD + ")");
		updateAppList();
	}

	public boolean IsTodayAppt(Appt appt) {
		if (appt.TimeSpan().StartTime().getYear() + 1900 != currentY)
			return false;
		if ((appt.TimeSpan().StartTime().getMonth() + 1) != currentM)
			return false;
		if (appt.TimeSpan().StartTime().getDate() != currentD)
			return false;
		return true;
	}

	public boolean IsMonthAppts(Appt appt) {

		if (appt.TimeSpan().StartTime().getYear() + 1900 != currentY)
			return false;

		if ((appt.TimeSpan().StartTime().getMonth() + 1) != currentM)
			return false;
		return true;
	}

	public Appt[] GetTodayAppt() {
		Integer temp;
		temp = new Integer(currentD);
		Timestamp start = new Timestamp(0);
		start.setYear(currentY);
		start.setMonth(currentM-1);
		start.setDate(currentD);
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);
		
		Timestamp end = new Timestamp(0);
		end.setYear(currentY);
		end.setMonth(currentM-1);
		end.setDate(currentD);
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		
		TimeSpan period = new TimeSpan(start, end);
		ArrayList<Appt> appts = ctrl.getApptList(curUser.getID(), period);
		Appt[] array = new Appt[appts.size()];
		for( int i = 0; i < appts.size(); i++ )
			array[i] = appts.get(i);
		return array;
		//return controller.RetrieveAppts(curUser, period);
	}

	public AppList getAppList() {
		return applist;
	}

	public User getCurUser() {
		return curUser;
	}

	public ArrayList<Appt> getInitiateApptList(int userID, TimeSpan period)
	{
		return ctrl.getInitiateApptList(userID, period);
	}

	public ArrayList<Appt> getApptResponseList(int userID, String response, TimeSpan period)
	{
		return ctrl.getApptResponseList(userID, response, period);
	}

	public ArrayList<Integer> getApptGroupIDList(int freGroupID)
	{
		return model.getApptGroupIDList(freGroupID);
	}

	public ArrayList<Appt> getPublicApptList(TimeSpan period)
	{
		return ctrl.getPublicApptList(period);
	}
			
	/*public TimeSpan getEarliestSchedule()
	{
		return ctrl.getEarliestSchedule(today, curUser.getID());
	}*/

	public ArrayList<TimeSpan> getAvailableSchedule(Calendar start, ArrayList<String> userLoginIDList, int numOfDays)
	{
		return ctrl.getAvailableSchedule(start, userLoginIDList, numOfDays);
	}

	public ArrayList<Location> getLocationList()
	{
		return model.getLocationList();
	}

	public int getNextApptID()
	{
		return model.getNextApptID();
	}

	public int getNextReminderID()
	{
		return model.getNextReminderID();
	}

	public int getNextLocationID()
	{
		return model.getNextLocationID();
	}

	public int getNextFreqGroupID()
	{
		return model.getNextFreqGroupID();
	}

	public boolean isApptCrash(int userID, TimeSpan t, int apptID)
	{
		return ctrl.isApptCrash(userID, t, apptID);
	}

	public boolean isLocationCrash(Location l, TimeSpan t, int apptID)
	{
		return ctrl.isLocationCrash(l, t, apptID);
	}
	
	public boolean addNewAppt(Appt a)
	{
		if( !ctrl.isApptCrash(a.getInitUserID(), a.TimeSpan(), a.getID()) )
		{
			boolean isSuccess = model.addAppt(a);
			updateAppList();
			return isSuccess;
		}
		return false;
	}

	public void addNewReminder(Reminder r)
	{
		model.addReminder(r);
	}

	public void addNewLocation(Location l)
	{
		model.addLocation(l);
	}

	public void addNewResponse(int apptID, int userID, String res)
	{
		model.addResponse(apptID, userID, res);
	}

	public Appt getAppt(int apptID)
	{
		return model.getAppt(apptID);
	}

	public Reminder getReminder(int rid)
	{
		return model.getReminder(rid);
	}

	public User getUser(int userID)
	{
		return model.getUser(userID);
	}

	public User getUser(String loginID)
	{
		return model.getUser(loginID);
	}

	public ArrayList<Response> getResponseList(int apptID)
	{
		return model.getResponseList(apptID);
	}


	public boolean modifyAppt(Appt a)
	{
		if( !ctrl.isApptCrash(a.getInitUserID(), a.TimeSpan(), a.getID()) )
		{
			boolean isSuccess = model.updateAppt(a);
			updateAppList();
			return isSuccess;
		}
		return false;
	}

	public void updateReminder(Reminder r)
	{
		model.updateReminder( r );
	}

	public void updateLocation(Location l)
	{
		model.updateLocation(l);
	}

	public void updateResponse(int apptID, int userID, String res)
	{
		model.updateResponse(apptID, userID, res);
	}

	public boolean removeAppt(int apptID)
	{
		boolean isSuccess = model.removeAppt(apptID);
		updateAppList();
		return isSuccess;
	}

	public void removeFreqGroup(int gid)
	{
		model.removeFreqGroup(gid);
	}

	public void removeReminder(int rid)
	{
		model.removeReminder( rid );
	}

	public void removeLocation(int locationID)
	{
		model.removeLocation(locationID);
	}

	public void removeResponse(int apptID)
	{
		model.removeResponse(apptID);
	}

	public void updateTime( Date d )
	{
		today.setTime(d);
		currentY = today.get(Calendar.YEAR);
		currentM = today.get(today.MONTH) + 1;
		currentD = today.get(today.DAY_OF_MONTH);
		year = new JLabel(new Integer(currentY).toString());
		month.setSelectedIndex(currentM-1);
		updateAppList();
		UpdateCal();
	}

	public void popupReminder( Reminder r )
	{
		if( r.isReminded() )
			return;
		SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy");
		SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
		Appt a = model.getAppt( r.getApptID() );
		String msg = "~~~~~Appointment Reminder~~~~~\n";
		msg += "Appointment: " + a.getTitle() + "\n";
		msg += "Date: " + sdfDate.format(a.TimeSpan().StartTime()) + "\n";
		msg += "Start Time: " + sdfTime.format(a.TimeSpan().StartTime()) + "\n";
		msg += "End Time: " + sdfTime.format(a.TimeSpan().EndTime()) + "\n";
		msg += "Location: " + a.getLocation().getName() + "\n";
		JOptionPane.showMessageDialog(null, msg, "APPOINTMENT REMINDER", JOptionPane.INFORMATION_MESSAGE);
		r.setIsReminded(true);
		model.updateReminder(r);
	}

	public GregorianCalendar getToday()
	{
		return today;
	}

	public boolean isUserAuthorized(String loginID, String pw)
	{
		return model.isUserAuthorized(loginID, pw);
	}

	public boolean isUserExist(String loginID)
	{
		if( model.getUser(loginID) == null )
			return false;
		return true;
	}

	public String getCurUserLoginID() { return curUser.getLoginID(); }
	public ArrayList<User> getUserList() { return model.getUserList(); }
	public void addUser(User user) { model.addUser(user); }
	public void updateUser(User user) { model.updateUser(user); }
	//public void removeUser(int userID) { model.removeUser(userID); }
	public void addUserToRemoveTable(int userID)
	{
		Timestamp t1 = new Timestamp( today.getTimeInMillis() );
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(today.getTimeInMillis());
		c.add(Calendar.YEAR, 100);
		Timestamp t2 = new Timestamp( c.getTimeInMillis() );
		TimeSpan ts = new TimeSpan(t1, t2);
		ctrl.addUserToRemoveTable(userID, ts);
	}

	public void addLocationToRemoveTable(int locationID)
	{
		Timestamp t1 = new Timestamp( today.getTimeInMillis() );
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(today.getTimeInMillis());
		c.add(Calendar.YEAR, 100);
		Timestamp t2 = new Timestamp( c.getTimeInMillis() );
		TimeSpan ts = new TimeSpan(t1, t2);
		ctrl.addLocationToRemoveTable(locationID, ts);
	}
	
	public void login()
	{
		LoginDialog loginDialog = new LoginDialog(this);
	}

	public void logout()
	{
		//LoginDialog loginDialog = new LoginDialog(this);
		//this.setVisible(false);
		main.restart();
	}

	public void setController(Controller ctrl) { this.ctrl = ctrl; }
	public void setModel(Model model) { this.model = model; }
	public CalGrid getCalGrid() { return this; }
}
