package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Reminder;
import hkust.cse.calendar.unit.TimeSpan;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;


public class AppScheduler extends JDialog implements ActionListener,
		ComponentListener {

	private JLabel yearL;
	private JTextField yearF;
	private JLabel monthL;
	private JTextField monthF;
	private JLabel dayL;
	private JTextField dayF;
	private JLabel sTimeHL;
	private JTextField sTimeH;
	private JLabel sTimeML;
	private JTextField sTimeM;
	private JLabel eTimeHL;
	private JTextField eTimeH;
	private JLabel eTimeML;
	private JTextField eTimeM;

	private JLabel isRemindL;
	private JCheckBox isRemind;
	private JLabel rTimeHL;
	private JTextField rTimeH;
	private JLabel rTimeML;
	private JTextField rTimeM;

	private ButtonGroup frequencyGroup;
	private JRadioButton rbOneTime;
	private JRadioButton rbDaily;
	private JRadioButton rbWeekly;
	private JRadioButton rbMonthly;
	private JLabel numOfFreqL;
	private JTextField numOfFreqF;

	private DefaultListModel model;
	private JTextField titleField;
	private JComboBox<Location> cbLocation;

	private JButton saveBut;
	private JButton CancelBut;
	private JButton inviteBut;
	private JButton rejectBut;
	
	private Appt NewAppt;
	private CalGrid parent;
	private boolean isNew = true;
	private boolean isChanged = true;
	private boolean isJoint = false;

	private JTextArea detailArea;

	private JSplitPane pDes;
	JPanel detailPanel;

//	private JTextField attendField;
//	private JTextField rejectField;
//	private JTextField waitingField;
	private int selectedApptId = -1;
	private int selectedReminderId = -1;

	private void commonConstructor(String title, CalGrid cal) {
		parent = cal;
		this.setAlwaysOnTop(true);
		setTitle(title);
		setModal(false);

		Container contentPane;
		contentPane = getContentPane();

		JPanel pApptDateTime = new JPanel();
		pApptDateTime.setLayout(new BorderLayout());

		JPanel pDate = new JPanel();
		Border dateBorder = new TitledBorder(null, "DATE");
		pDate.setBorder(dateBorder);

		yearL = new JLabel("YEAR: ");
		pDate.add(yearL);
		yearF = new JTextField(6);
		pDate.add(yearF);
		monthL = new JLabel("MONTH: ");
		pDate.add(monthL);
		monthF = new JTextField(4);
		pDate.add(monthF);
		dayL = new JLabel("DAY: ");
		pDate.add(dayL);
		dayF = new JTextField(4);
		pDate.add(dayF);
		pApptDateTime.add(pDate, BorderLayout.NORTH);

		JPanel psTime = new JPanel();
		Border stimeBorder = new TitledBorder(null, "START TIME");
		psTime.setBorder(stimeBorder);
		sTimeHL = new JLabel("Hour");
		psTime.add(sTimeHL);
		sTimeH = new JTextField(4);
		psTime.add(sTimeH);
		sTimeML = new JLabel("Minute");
		psTime.add(sTimeML);
		sTimeM = new JTextField(4);
		psTime.add(sTimeM);
		pApptDateTime.add(psTime, BorderLayout.WEST);

		JPanel peTime = new JPanel();
		Border etimeBorder = new TitledBorder(null, "END TIME");
		peTime.setBorder(etimeBorder);
		eTimeHL = new JLabel("Hour");
		peTime.add(eTimeHL);
		eTimeH = new JTextField(4);
		peTime.add(eTimeH);
		eTimeML = new JLabel("Minute");
		peTime.add(eTimeML);
		eTimeM = new JTextField(4);
		peTime.add(eTimeM);
		pApptDateTime.add(peTime, BorderLayout.EAST);

		JPanel pApptOthers = new JPanel();
		pApptOthers.setLayout(new BorderLayout());

		JPanel pReminder = new JPanel();
		Border reminderBorder = new TitledBorder(null, "REMINDER");
		pReminder.setBorder(reminderBorder);
		isRemindL = new JLabel("Need Reminder?");
		pReminder.add(isRemindL);
		isRemind = new JCheckBox();
		pReminder.add(isRemind);
		isRemind.setSelected(false);
		rTimeHL = new JLabel("Hour");
		pReminder.add(rTimeHL);
		rTimeH = new JTextField(4);
		rTimeH.getDocument().addDocumentListener
		(
			new DocumentListener()
			{
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					if(!rTimeH.getText().isEmpty())
						isRemind.setSelected(true);
					else if(rTimeH.getText().isEmpty() && rTimeM.getText().isEmpty())
						isRemind.setSelected(false);
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					changedUpdate(arg0);
				}

				@Override
				public void removeUpdate(DocumentEvent arg0) {
					changedUpdate(arg0);
				}
			}
		);
		pReminder.add(rTimeH);
		rTimeML = new JLabel("Minute");
		pReminder.add(rTimeML);
		rTimeM = new JTextField(4);
		rTimeM.getDocument().addDocumentListener
		(
			new DocumentListener()
			{
				@Override
				public void changedUpdate(DocumentEvent arg0) {
					if(!rTimeM.getText().isEmpty())
						isRemind.setSelected(true);
					else if(rTimeH.getText().isEmpty() && rTimeM.getText().isEmpty())
						isRemind.setSelected(false);
				}

				@Override
				public void insertUpdate(DocumentEvent arg0) {
					changedUpdate(arg0);
				}

				@Override
				public void removeUpdate(DocumentEvent arg0) {
					changedUpdate(arg0);
				}
			}
		);
		pReminder.add(rTimeM);
		pApptOthers.add(pReminder, BorderLayout.NORTH);

		JPanel pFrequency = new JPanel();
		Border frequencyBorder = new TitledBorder(null, "FREQUENCY");
		pFrequency.setBorder(frequencyBorder);
		rbOneTime = new JRadioButton("One-Time");
		rbDaily = new JRadioButton("Daily");
		rbWeekly = new JRadioButton("Weekly");
		rbMonthly = new JRadioButton("Monthly");
		frequencyGroup = new ButtonGroup();
		frequencyGroup.add(rbOneTime);
		frequencyGroup.add(rbDaily);
		frequencyGroup.add(rbWeekly);
		frequencyGroup.add(rbMonthly);
		rbOneTime.setSelected(true);
		numOfFreqL = new JLabel("No. of Freq:");
		numOfFreqF = new JTextField(2); 
		pFrequency.add(rbOneTime);
		pFrequency.add(rbDaily);
		pFrequency.add(rbWeekly);
		pFrequency.add(rbMonthly);
		pFrequency.add(numOfFreqL);
		pFrequency.add(numOfFreqF);
		pApptOthers.add(pFrequency, BorderLayout.SOUTH);
		
		JPanel top = new JPanel();
		top.setLayout(new BorderLayout());
		top.setBorder(new BevelBorder(BevelBorder.RAISED));
		top.add(pApptDateTime, BorderLayout.WEST);
		top.add(pApptOthers, BorderLayout.EAST);

		contentPane.add("North", top);

		JPanel titleAndTextPanel = new JPanel();
		JLabel titleL = new JLabel("TITLE");
		titleField = new JTextField(15);
		titleAndTextPanel.add(titleL);
		titleAndTextPanel.add(titleField);
		titleAndTextPanel.add( new JLabel("   ") );
		JLabel locationLabel = new JLabel("LOCATION");
		cbLocation = new JComboBox<Location>();
		Location[] locations = cal.getLocationList();
		if( locations != null )
			for( Location l : locations )
				cbLocation.addItem( l );
		cbLocation.setRenderer(new DefaultListCellRenderer() {
		    @Override
		    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        if (value instanceof Location) {
		        	Location location = (Location)value;
		            setText(location.getName());
		        }
		        return this;
		    }
		});
		titleAndTextPanel.add(locationLabel);
		titleAndTextPanel.add(cbLocation);

		detailPanel = new JPanel();
		detailPanel.setLayout(new BorderLayout());
		Border detailBorder = new TitledBorder(null, "Appointment Description");
		detailPanel.setBorder(detailBorder);
		detailArea = new JTextArea(20, 30);

		detailArea.setEditable(true);
		JScrollPane detailScroll = new JScrollPane(detailArea);
		detailPanel.add(detailScroll);

		pDes = new JSplitPane(JSplitPane.VERTICAL_SPLIT, titleAndTextPanel,
				detailPanel);

		top.add(pDes, BorderLayout.SOUTH);

		if (NewAppt != null) {
			detailArea.setText(NewAppt.getInfo());

		}
		
		JPanel panel2 = new JPanel();
		panel2.setLayout(new FlowLayout(FlowLayout.RIGHT));

//		inviteBut = new JButton("Invite");
//		inviteBut.addActionListener(this);
//		panel2.add(inviteBut);
		
		saveBut = new JButton("Save");
		saveBut.addActionListener(this);
		panel2.add(saveBut);

		rejectBut = new JButton("Reject");
		rejectBut.addActionListener(this);
		panel2.add(rejectBut);
		rejectBut.show(false);

		CancelBut = new JButton("Cancel");
		CancelBut.addActionListener(this);
		panel2.add(CancelBut);

		contentPane.add("South", panel2);
		NewAppt = new Appt();

		if (this.getTitle().equals("Join Appointment Content Change") || this.getTitle().equals("Join Appointment Invitation")){
			inviteBut.show(false);
			rejectBut.show(true);
			CancelBut.setText("Consider Later");
			saveBut.setText("Accept");
		}
		if (this.getTitle().equals("Someone has responded to your Joint Appointment invitation") ){
			inviteBut.show(false);
			rejectBut.show(false);
			CancelBut.show(false);
			saveBut.setText("confirmed");
		}
		if (this.getTitle().equals("Join Appointment Invitation") || this.getTitle().equals("Someone has responded to your Joint Appointment invitation") || this.getTitle().equals("Join Appointment Content Change")){
			allDisableEdit();
		}
		pack();

	}
	
	AppScheduler(String title, CalGrid cal, int selectedApptId) {
		this.selectedApptId = selectedApptId;
		commonConstructor(title, cal);
	}

	AppScheduler(String title, CalGrid cal) {
		commonConstructor(title, cal);
	}
	
	public void actionPerformed(ActionEvent e) {

		// distinguish which button is clicked and continue with require function
		if (e.getSource() == CancelBut) {

			setVisible(false);
			dispose();
		} else if (e.getSource() == saveBut) {
			saveButtonResponse();

		} else if (e.getSource() == rejectBut){
			if (JOptionPane.showConfirmDialog(this, "Reject this joint appointment?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0){
				NewAppt.addReject(getCurrentUser());
				NewAppt.getAttendList().remove(getCurrentUser());
				NewAppt.getWaitingList().remove(getCurrentUser());
				this.setVisible(false);
				dispose();
			}
		}
		parent.getAppList().clear();
		parent.getAppList().setTodayAppt(parent.GetTodayAppt());
		parent.repaint();
	}

	private JPanel createPartOperaPane() {
		JPanel POperaPane = new JPanel();
		JPanel browsePane = new JPanel();
		JPanel controPane = new JPanel();

		POperaPane.setLayout(new BorderLayout());
		TitledBorder titledBorder1 = new TitledBorder(BorderFactory
				.createEtchedBorder(Color.white, new Color(178, 178, 178)),
				"Add Participant:");
		browsePane.setBorder(titledBorder1);

		POperaPane.add(controPane, BorderLayout.SOUTH);
		POperaPane.add(browsePane, BorderLayout.CENTER);
		POperaPane.setBorder(new BevelBorder(BevelBorder.LOWERED));
		return POperaPane;

	}

	private int[] getValidDate() {

		int[] date = new int[3];
		date[0] = Utility.getNumber(yearF.getText());
		date[1] = Utility.getNumber(monthF.getText());
		if (date[0] < 1980 || date[0] > 2100) {
			JOptionPane.showMessageDialog(this, "Please input proper year",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (date[1] <= 0 || date[1] > 12) {
			JOptionPane.showMessageDialog(this, "Please input proper month",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		date[2] = Utility.getNumber(dayF.getText());
		int monthDay = CalGrid.monthDays[date[1] - 1];
		if (date[1] == 2) {
			GregorianCalendar c = new GregorianCalendar();
			if (c.isLeapYear(date[0]))
				monthDay = 29;
		}
		if (date[2] <= 0 || date[2] > monthDay) {
			JOptionPane.showMessageDialog(this,
			"Please input proper month day", "Input Error",
			JOptionPane.ERROR_MESSAGE);
			return null;
		}
		return date;
	}

	private int getTime(JTextField h, JTextField min) {

		int hour = Utility.getNumber(h.getText());
		if (hour == -1)
			return -1;
		int minute = Utility.getNumber(min.getText());
		if (minute == -1)
			return -1;

		return (hour * 60 + minute);

	}

	private int[] getValidTimeInterval() {

		int[] result = new int[2];
		result[0] = getTime(sTimeH, sTimeM);
		result[1] = getTime(eTimeH, eTimeM);
		if ((result[0] % 15) != 0 || (result[1] % 15) != 0) {
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if (!sTimeM.getText().equals("0") && !sTimeM.getText().equals("15") && !sTimeM.getText().equals("30") && !sTimeM.getText().equals("45") 
			|| !eTimeM.getText().equals("0") && !eTimeM.getText().equals("15") && !eTimeM.getText().equals("30") && !eTimeM.getText().equals("45")){
			JOptionPane.showMessageDialog(this,
					"Minute Must be 0, 15, 30, or 45 !", "Input Error",
					JOptionPane.ERROR_MESSAGE);
			return null;
		}
		
		if (result[1] == -1 || result[0] == -1) {
			JOptionPane.showMessageDialog(this, "Please check time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if (result[1] <= result[0]) {
			JOptionPane.showMessageDialog(this,
					"End time should be bigger than \nstart time",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}
		if ((result[0] < AppList.OFFSET * 60)
				|| (result[1] > (AppList.OFFSET * 60 + AppList.ROWNUM * 2 * 15))) {
			JOptionPane.showMessageDialog(this, "Out of Appointment Range !",
					"Input Error", JOptionPane.ERROR_MESSAGE);
			return null;
		}

		return result;
	}

	private void saveButtonResponse() {//TODO: saveButtonResponse()
		// Fix Me!
		// Save the appointment to the hard disk
		if( yearF.getText().trim().isEmpty() || monthF.getText().trim().isEmpty() || dayF.getText().trim().isEmpty()
				|| sTimeH.getText().trim().isEmpty() || sTimeM.getText().trim().isEmpty() || eTimeH.getText().trim().isEmpty()
				|| eTimeM.getText().trim().isEmpty() || titleField.getText().trim().isEmpty() )
		{
			JOptionPane.showMessageDialog( this, "Missing information!", "ERROR", JOptionPane.ERROR_MESSAGE );
			return;
		}
		if( !rbOneTime.isSelected() && numOfFreqF.getText().trim().isEmpty() )
		{
			JOptionPane.showMessageDialog( this, "Please fill the no. of frequency!", "ERROR", JOptionPane.ERROR_MESSAGE );
			return;
		}
		if( isRemind.isSelected() && rTimeH.getText().trim().isEmpty() && rTimeM.getText().trim().isEmpty() )
		{
			JOptionPane.showMessageDialog( this, "Please fill the time for reminder!", "ERROR", JOptionPane.ERROR_MESSAGE );
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat( "dd/MM/yyyy HH:mm" );
		Calendar now = Calendar.getInstance();
		Calendar start = Calendar.getInstance();
		Calendar end = Calendar.getInstance();
		int year, month, day, startTimeHour, startTimeMin, endTimeHour, endTimeMin;
		try
		{
			year = Integer.parseInt( yearF.getText() );
			month = Integer.parseInt( monthF.getText() );
			day = Integer.parseInt( dayF.getText() );
			if( !isDateValid( year, month, day ) )
			{
				JOptionPane.showMessageDialog( this, "Invalid date information!", "ERROR", JOptionPane.ERROR_MESSAGE );
				return;
			}
			start.set( year, month-1, day );//-1 because the month attribute of Calendar.class is from 0 - 11
			if( start.before( now ) )
			{
				JOptionPane.showMessageDialog( this, "Cannot add event before today!", "ERROR", JOptionPane.ERROR_MESSAGE );
				return;
			}
		}
		catch( NumberFormatException nfe )
		{
			nfe.printStackTrace();
			JOptionPane.showMessageDialog( this, "Only integers are allowed for date fields!", "ERROR", JOptionPane.ERROR_MESSAGE );
			return;
		}
		try
		{
			startTimeHour = Integer.parseInt( sTimeH.getText() );
			startTimeMin = Integer.parseInt( sTimeM.getText() );
			endTimeHour = Integer.parseInt( eTimeH.getText() );
			endTimeMin = Integer.parseInt( eTimeM.getText() );
			if( !isTimeValid( startTimeHour, startTimeMin ) || !isTimeValid( endTimeHour, endTimeMin ) )
			{
				JOptionPane.showMessageDialog( this, "Invalid time information!", "ERROR", JOptionPane.ERROR_MESSAGE );
				return;
			}
			start.set( year, month-1, day, startTimeHour, startTimeMin, 0 );//-1 because the month attribute of Calendar.class is from 0 - 11
			end.set( year, month-1, day, endTimeHour, endTimeMin, 0 );
			if( end.before( start ) )
			{
				JOptionPane.showMessageDialog( this, "End time cannot be earlier than start time!", "ERROR", JOptionPane.ERROR_MESSAGE );
				return;
			}
		}
		catch( NumberFormatException nfe )
		{
			nfe.printStackTrace();
			JOptionPane.showMessageDialog( this, "Only integers are allowed for time fields!", "ERROR", JOptionPane.ERROR_MESSAGE );
			return;
		}

		int calIncType, calInc;
		if( rbDaily.isSelected() )
		{
			calIncType = Calendar.DAY_OF_MONTH;
			calInc = 1;
		}
		else if( rbWeekly.isSelected() )
		{
			calIncType = Calendar.DAY_OF_MONTH;
			calInc = 7;
		}
		else
		{
			calIncType = Calendar.MONTH;
			calInc = 1;
		}
		int freq;
		if( rbOneTime.isSelected() )
			freq = 1;
		else
			freq = Integer.parseInt( numOfFreqF.getText().trim() );
		for( int i = 0; i < freq; i++ )
		{
			NewAppt = new Appt();
			NewAppt.setTitle( titleField.getText().trim() );
			NewAppt.setInfo( detailArea.getText().trim() );
			Calendar s = (Calendar) start.clone();
			s.add( calIncType, calInc*i );
			Calendar e = (Calendar) end.clone();
			e.add( calIncType, calInc*i );
			NewAppt.setTimeSpan( new TimeSpan( new Timestamp( s.getTimeInMillis() ), new Timestamp( e.getTimeInMillis() ) ) );
			NewAppt.setLocation( (Location)cbLocation.getSelectedItem() );
			Reminder r = null;
			if( isRemind.isSelected() )
			{
				r = new Reminder();
				int reminderHour, reminderMinute;
				if( rTimeH.getText().trim().isEmpty() )
					reminderHour = 0;
				else
					reminderHour = Integer.parseInt( rTimeH.getText().trim() );
				if( rTimeM.getText().trim().isEmpty() )
					reminderMinute = 0;
				else
					reminderMinute = Integer.parseInt( rTimeM.getText().trim() );
				Calendar reminderS = (Calendar) s.clone();
				reminderS.add( Calendar.HOUR, -reminderHour );
				reminderS.add( Calendar.MINUTE, -reminderMinute );
				r.setTimeSpan( new TimeSpan( new Timestamp( reminderS.getTimeInMillis() ), new Timestamp( s.getTimeInMillis() ) ) );
				r.setHourInterval( reminderHour );
				r.setMinuteInterval( reminderMinute );
			}
			//Make new appointment
			if( selectedApptId == -1 )
			{
				NewAppt.setID( parent.getNextApptID() );
				if( isRemind.isSelected() )
				{
					r.setID( parent.getNextReminderID() );
					r.setApptID( NewAppt.getID() );
					NewAppt.setReminderID( r.getID() );
					parent.addNewReminder( r );
				}
				if( !parent.addNewAppt( NewAppt ) )
				{
					JOptionPane.showMessageDialog( this, "Overlapping appointments", "OVERLAP", JOptionPane.ERROR_MESSAGE );
					return;
				}
			}
			//Modify appointment
			else
			{
				NewAppt.setID( selectedApptId );
				if( selectedReminderId == -1 )//No reminder originally
				{
					if( isRemind.isSelected() )
					{
						selectedReminderId = parent.getNextReminderID();
						r.setID( selectedReminderId );
						r.setApptID( NewAppt.getID() );
						NewAppt.setReminderID( selectedReminderId );
						parent.addNewReminder( r );
					}
				}
				else
				{
					if( isRemind.isSelected() )
					{
						r.setID( selectedReminderId );
						r.setApptID( NewAppt.getID() );
						parent.updateReminder( r );
					}
					else
					{
						r = parent.getReminder(selectedReminderId);
						parent.removeReminder( r );
					}
				}
				if( !parent.modifyAppt( NewAppt ) )
				{
					JOptionPane.showMessageDialog( this, "Overlapping appointments", "OVERLAP", JOptionPane.ERROR_MESSAGE );
					return;
				}
			}
		}
		this.dispose();
	}

	private boolean isDateValid( int year, int month, int day )
	{
		if( month < 1 || month > 12 )
			return false;
		else if( day < 1 )
			return false;
		else if( month == 2 )
		{
			if( ( year % 4 == 0 && year % 100 != 0 ) || year % 400 == 0 )
			{
				if( day > 29 )
					return false;
			}
			else
			{
				if( day > 28 )
					return false;
			}
		}
		else if( month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12 )
		{
			if( day > 31 )
				return false;
		}
		else
		{
			if( day > 30 )
				return false;
		}
		
		return true;
	}

	private boolean isTimeValid( int hour, int min )
	{
		if( hour >= 8 && hour <= 17 && min % 15 == 0 )
			return true;
		return false;
	}

	private Timestamp CreateTimeStamp(int[] date, int time) {
		Timestamp stamp = new Timestamp(0);
		stamp.setYear(date[0]);
		stamp.setMonth(date[1] - 1);
		stamp.setDate(date[2]);
		stamp.setHours(time / 60);
		stamp.setMinutes(time % 60);
		return stamp;
	}

	public void updateSetApp(Appt appt) {
		//TODO: updateSetApp(Appt appt)
		// Fix Me!
		TimeSpan ts = appt.TimeSpan();
		Timestamp start = ts.StartTime();
		Timestamp end = ts.EndTime();
		yearF.setText(""+(start.getYear()+1900));
		monthF.setText(""+(start.getMonth()+1));
		dayF.setText(""+start.getDate());
		sTimeH.setText(""+start.getHours());
		sTimeM.setText(""+start.getMinutes());
		eTimeH.setText(""+end.getHours());
		eTimeM.setText(""+end.getMinutes());
		titleField.setText(appt.getTitle());
		detailArea.setText(appt.getInfo());
		selectedReminderId = appt.getReminderID();
		if( selectedReminderId != -1 )
		{
			isRemind.setSelected(true);
			Reminder r = parent.getReminder(appt.getReminderID());
			if( r != null )
			{
				rTimeH.setText(""+r.getHourInterval());
				rTimeM.setText(""+r.getMinuteInterval());
			}
		}
		if( appt.getLocation() != null )
			cbLocation.setSelectedItem(appt.getLocation());
		else
			cbLocation.setSelectedIndex(0);
	}

	public void componentHidden(ComponentEvent e) {

	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentResized(ComponentEvent e) {

		Dimension dm = pDes.getSize();
		double width = dm.width * 0.93;
		double height = dm.getHeight() * 0.6;
		detailPanel.setSize((int) width, (int) height);

	}

	public void componentShown(ComponentEvent e) {

	}
	
	public String getCurrentUser()		// get the id of the current user
	{
		return this.parent.mCurrUser.ID();
	}
	
	private void allDisableEdit(){
		yearF.setEditable(false);
		monthF.setEditable(false);
		dayF.setEditable(false);
		sTimeH.setEditable(false);
		sTimeM.setEditable(false);
		eTimeH.setEditable(false);
		eTimeM.setEditable(false);
		titleField.setEditable(false);
		detailArea.setEditable(false);
	}
}
