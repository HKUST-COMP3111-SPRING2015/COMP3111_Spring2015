package hkust.cse.calendar.gui;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Reminder;
import hkust.cse.calendar.unit.Response;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

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
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;


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

	private JTextField titleField;
	private JCheckBox cbIsPublic;
	private JComboBox<Location> cbLocation;
	private JButton btnAutoSchedule;

	private JTextArea detailArea;
	private JTable inviteListTable;
	private TableModel inviteListModel;
	private JTextField tfInviteUser;
	private JCheckBox cbIsJoint;
	private JButton btnInvite;
	private JLabel getScheduleL;
	private JTextField tfGetSchedule;
	private JButton btnGetSchedule;

	private JButton saveBut;
	private JButton CancelBut;
	private JButton inviteBut;
	private JButton rejectBut;
	
	private Appt NewAppt;
	private CalGrid calGrid;
	private boolean isNew = true;
	private boolean isChanged = true;
	private boolean isJoint = false;

	private JSplitPane pDes;
	private JPanel groupAndDetailPanel;
	private JPanel groupEventPanel; 

//	private JTextField attendField;
//	private JTextField rejectField;
//	private JTextField waitingField;
	private int selectedApptId = -1;
	private int selectedReminderId = -1;
	private int selectedFreqGroupId = -1;
	private MyAppointmentManagementDialog am;

	private void commonConstructor(String title, final CalGrid calGrid)
	{
		this.calGrid = calGrid;
		//this.setAlwaysOnTop(true);
		this.setTitle(title);
		this.setModal(false);

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

		JPanel titleAndTextPanel = new JPanel();
		JLabel titleL = new JLabel("TITLE");
		titleField = new JTextField(15);
		titleAndTextPanel.add(titleL);
		titleAndTextPanel.add(titleField);
		titleAndTextPanel.add( new JLabel("   ") );
		JLabel locationLabel = new JLabel("LOCATION");
		cbLocation = new JComboBox<Location>();
		ArrayList<Location> locations = calGrid.getLocationList();
		if( locations != null )
			for( Location l : locations )
				cbLocation.addItem( l );
		cbLocation.setRenderer(new DefaultListCellRenderer() {
		    @Override
		    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        if (value instanceof Location) {
		        	Location location = (Location)value;
		        	String s = location.getName();
		        	if( location.isGroup() )
		        		s += (" of "+location.getGroup()+"("+location.getCapacity()+")");
		            setText(s);
		        }
		        return this;
		    }
		});
		cbLocation.addActionListener(this);
		titleAndTextPanel.add(locationLabel);
		titleAndTextPanel.add(cbLocation);
		cbIsPublic = new JCheckBox("Public");
		titleAndTextPanel.add(cbIsPublic);

		btnAutoSchedule = new JButton("Auto");
		btnAutoSchedule.addActionListener(this);
		titleAndTextPanel.add(btnAutoSchedule);
		getScheduleL = new JLabel("Days:");
		titleAndTextPanel.add(getScheduleL);
		tfGetSchedule = new JTextField(2);
		titleAndTextPanel.add(tfGetSchedule);
		btnGetSchedule = new JButton("Schedule");
		btnGetSchedule.addActionListener(this);
		btnGetSchedule.setEnabled(false);
		titleAndTextPanel.add(btnGetSchedule);
		
		groupAndDetailPanel = new JPanel();
		groupAndDetailPanel.setLayout(new BorderLayout());
		Border detailBorder = new TitledBorder(null, "Appointment Description");
		groupAndDetailPanel.setBorder(detailBorder);
		//detailArea = new JTextArea(20, 30);
		detailArea = new JTextArea(5, 30);
		detailArea.setEditable(true);
		JScrollPane detailScroll = new JScrollPane(detailArea);
		groupAndDetailPanel.add(detailScroll, BorderLayout.NORTH);

		groupEventPanel = new JPanel();
		groupEventPanel.setLayout(new BorderLayout());
		Border groupEventBorder = new TitledBorder(null, "Group Event");
		groupEventPanel.setBorder(groupEventBorder);
		tfInviteUser = new JTextField(20);
		tfInviteUser.setEditable(true);
		tfInviteUser.setEnabled(false);
		groupEventPanel.add(tfInviteUser, BorderLayout.CENTER);
		btnInvite = new JButton("Invite");
		btnInvite.addActionListener(this);
		btnInvite.setEnabled(false);
		cbIsJoint = new JCheckBox("Group Event");
		cbIsJoint.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent arg0)
				{
					if(cbIsJoint.isSelected())
					{
						tfInviteUser.setEnabled(true);
						btnInvite.setEnabled(true);
						btnGetSchedule.setEnabled(true);
						Location l = (Location) cbLocation.getSelectedItem();
						int capacity = l.getCapacity();
						if( capacity == 0 )
						{
							JOptionPane.showMessageDialog(null, "Please change to a bigger room first!", "ERROR", JOptionPane.WARNING_MESSAGE);
							cbIsJoint.setSelected(false);
							tfInviteUser.setEnabled(false);
							btnInvite.setEnabled(false);
							btnGetSchedule.setEnabled(false);
							return;
						}
						DefaultTableModel model = (DefaultTableModel) inviteListTable.getModel();
						model.addRow(new Object[]{calGrid.getCurUserLoginID(), "Pending"});
					}
					else
					{
						tfInviteUser.setEnabled(false);
						btnInvite.setEnabled(false);
						btnGetSchedule.setEnabled(false);
						DefaultTableModel model = (DefaultTableModel) inviteListTable.getModel();
						while(model.getRowCount()>0)
							model.removeRow(0);
					}
				}
			}
		);
		groupEventPanel.add(cbIsJoint, BorderLayout.NORTH);
		groupEventPanel.add(btnInvite, BorderLayout.EAST);

		Object columnNames[] = {"User ID", "Status"};
		inviteListModel = new DefaultTableModel(null, columnNames);
		inviteListTable = new JTable(inviteListModel);
		JScrollPane scrollPane = new JScrollPane(inviteListTable);
		inviteListTable.setSize(this.getWidth(), 200);
		inviteListTable.setPreferredScrollableViewportSize(inviteListTable.getSize());
		inviteListTable.setEnabled(false);
		//DefaultTableModel model = (DefaultTableModel) inviteListTable.getModel();
		//model.addRow(new Object[]{calGrid.getCurUserLoginID(), "Pending"});
		groupEventPanel.add(scrollPane, BorderLayout.SOUTH);

		groupAndDetailPanel.add(groupEventPanel, BorderLayout.CENTER);

		pDes = new JSplitPane(JSplitPane.VERTICAL_SPLIT, titleAndTextPanel, groupAndDetailPanel);

		top.add(pDes, BorderLayout.SOUTH);

		contentPane.add("North", top);

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
			DisableEdit();
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
			if (JOptionPane.showConfirmDialog(this, "Reject this invitation?", "Confirmation", JOptionPane.YES_NO_OPTION) == 0){
				/*NewAppt.addReject(getCurrentUser());
				NewAppt.getAttendList().remove(getCurrentUser());
				NewAppt.getWaitingList().remove(getCurrentUser());
				this.setVisible(false);*/
				calGrid.updateResponse(selectedApptId, calGrid.getCurUser().getID(), Response.REJECT);
				am.refreshTable();
				dispose();
			}
		}
		else if( e.getSource() == btnInvite )
		{
			DefaultTableModel model = (DefaultTableModel) inviteListTable.getModel();
			String input = tfInviteUser.getText().trim();
			if( calGrid.isUserExist(input) )
			{
				Vector v = model.getDataVector();
				for( int i = 0; i < v.size(); i++ )
					if( ((Vector)v.get(i)).get(0).equals(input) )
					{
						JOptionPane.showMessageDialog(null, input+" is on the list already!", "ERROR", JOptionPane.WARNING_MESSAGE);
						return;
					}
				Location l = (Location) cbLocation.getSelectedItem();
				if( l.getCapacity() > model.getRowCount() )
				{
					model.addRow(new Object[]{input, "Pending"});
					tfInviteUser.setText("");
				}
				else
				{
					JOptionPane.showMessageDialog(null, "The venue is full", "ERROR", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}
			else
				JOptionPane.showMessageDialog(null, "No such user!", "ERROR", JOptionPane.WARNING_MESSAGE);
		}
		else if( e.getSource() == btnAutoSchedule )
		{
			TimeSpan t;
			if( cbIsJoint.isSelected() )
			{
				Vector<Vector<String>> data = ((DefaultTableModel) inviteListTable.getModel()).getDataVector();
				ArrayList<String> userLoginIDList = new ArrayList<String>();
				for( int i = 0; i < data.size(); i++ )
					userLoginIDList.add( data.get(i).get(0) );
				//t = calGrid.getEarliestSchedule(userLoginIDList);
			}
			//else
				//t = calGrid.getEarliestSchedule();
		}
		//TODO: Get Schedule
		else if( e.getSource() == btnGetSchedule )
		{
			
			if( yearF.getText().trim().isEmpty() || monthF.getText().trim().isEmpty() || dayF.getText().trim().isEmpty()
					|| sTimeH.getText().trim().isEmpty() || sTimeM.getText().trim().isEmpty() )
			{
				JOptionPane.showMessageDialog( this, "Missing information!", "ERROR", JOptionPane.ERROR_MESSAGE );
				return;
			}
			//Check if num of days correct
			try{Integer.parseInt(this.tfGetSchedule.getText());}
			catch(Exception e2)
			{
				JOptionPane.showMessageDialog( this, "Number Format Problem!", "ERROR", JOptionPane.ERROR_MESSAGE );
				return;
			}
			
			//Get Time first
			Calendar now = Calendar.getInstance();
			now.setTime( ( calGrid.getToday().getTime() ));
			Calendar start = Calendar.getInstance();
			int year, month, day, startTimeHour, startTimeMin;
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
				startTimeHour = Integer.parseInt( sTimeH.getText() );
				startTimeMin = Integer.parseInt( sTimeM.getText() );
				if( !isTimeValid( startTimeHour, startTimeMin ) )
				{
					JOptionPane.showMessageDialog( this, "Invalid time information!", "ERROR", JOptionPane.ERROR_MESSAGE );
					return;
				}
				start.set( year, month-1, day, startTimeHour, startTimeMin, 0 );//-1 because the month attribute of Calendar.class is from 0 - 11
				if( start.before( now ) )
				{
					JOptionPane.showMessageDialog( this, "Cannot add past event!", "ERROR", JOptionPane.ERROR_MESSAGE );
					return;
				}
			}
			catch( NumberFormatException nfe )
			{
				nfe.printStackTrace();
				JOptionPane.showMessageDialog( this, "Only integers are allowed for date or time fields!", "ERROR", JOptionPane.ERROR_MESSAGE );
				return;
			}
			Vector<Vector<String>> data = ((DefaultTableModel) inviteListTable.getModel()).getDataVector();
			ArrayList<String> userLoginIDList = new ArrayList<String>();
			for( int i = 0; i < data.size(); i++ )
				userLoginIDList.add( data.get(i).get(0) );
			
			ArrayList<TimeSpan> availableList = calGrid.getAvailableSchedule(start, userLoginIDList, Integer.parseInt(this.tfGetSchedule.getText()));
			for( int i = 0; i < availableList.size(); i++ )
				System.out.println("AppScheduler-"+(i+1)+":"+availableList.get(i).getTimeString());

			AvailableScheduleDialog asd = new AvailableScheduleDialog(this, availableList);
		}
		else if( e.getSource() == cbLocation )
		{
			DefaultTableModel model = (DefaultTableModel) inviteListTable.getModel();
			Location l = (Location) cbLocation.getSelectedItem();
			int capacity = l.getCapacity();
			if( capacity < model.getRowCount() )
			{
				JOptionPane.showMessageDialog(null, "The venue is too small!", "Warning", JOptionPane.WARNING_MESSAGE);
				while( model.getRowCount() > 0 )
					model.removeRow(0);
				if( capacity > 0 )
					model.addRow(new Object[]{calGrid.getCurUserLoginID(), "Pending"});
				else
				{
					cbIsJoint.setSelected(false);
					tfInviteUser.setEnabled(false);
					btnInvite.setEnabled(false);
					btnGetSchedule.setEnabled(false);
				}
			}
		}
		calGrid.getAppList().clear();
		calGrid.getAppList().setTodayAppt(calGrid.GetTodayAppt());
		calGrid.repaint();
	}

	private void saveButtonResponse() {//TODO: saveButtonResponse()
		if( saveBut.getText().equals("OK") )
		{
			this.dispose();
			return;
		}
		if( saveBut.getText().equals("Accept") )
		{
			if( calGrid.isApptCrash(calGrid.getCurUser().getID(), calGrid.getAppt(selectedApptId).TimeSpan(), selectedApptId) )
			{
				JOptionPane.showMessageDialog( this, "It crashes with your other appointment!", "ERROR", JOptionPane.ERROR_MESSAGE );
				return;
			}
			calGrid.updateResponse(selectedApptId, calGrid.getCurUser().getID(), Response.ACCEPT);
			am.refreshTable();
			this.dispose();
			return;
		}
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
		now.setTime( ( calGrid.getToday().getTime() ));
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
			if( start.before( now ) )
			{
				JOptionPane.showMessageDialog( this, "Cannot add past event!", "ERROR", JOptionPane.ERROR_MESSAGE );
				return;
			}
			if( end.before( start ) )
			{
				JOptionPane.showMessageDialog( this, "End time cannot be earlier than start time!", "ERROR", JOptionPane.ERROR_MESSAGE );
				return;
			}
		}
		catch( NumberFormatException nfe )
		{
			nfe.printStackTrace();
			JOptionPane.showMessageDialog( this, "Only integers are allowed for date or time fields!", "ERROR", JOptionPane.ERROR_MESSAGE );
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

		//Check if the appointment(s) crash(es) others
		for( int i = 0; i < freq; i++ )
		{
			Calendar s = (Calendar) start.clone();
			s.add( calIncType, calInc*i );
			Calendar e = (Calendar) end.clone();
			e.add( calIncType, calInc*i );
			TimeSpan apptTime = new TimeSpan( new Timestamp( s.getTimeInMillis() ), new Timestamp( e.getTimeInMillis() ) );
			
			if( calGrid.isApptCrash(calGrid.getCurUser().getID(), apptTime, selectedApptId) )
			{
				JOptionPane.showMessageDialog( this, "Overlapping appointments!", "OVERLAP", JOptionPane.ERROR_MESSAGE );
				return;
			}
			if( calGrid.isLocationCrash((Location)cbLocation.getSelectedItem(), apptTime, selectedApptId) )
			{
				JOptionPane.showMessageDialog( this, "Venue Occupied!", "OCCUPIED", JOptionPane.ERROR_MESSAGE );
				return;
			}
			/*if( cbIsJoint.isSelected() )
			{
				DefaultTableModel model = (DefaultTableModel) inviteListTable.getModel();
				Vector<Vector<String>> data = model.getDataVector();
				for( int j = 0; j < data.size(); j++ )
					if( calGrid.isApptCrash(calGrid.getUser(data.get(j).get(0)).getID(), apptTime) )
					{
						JOptionPane.showMessageDialog( this, "Overlapping appointments", "OVERLAP", JOptionPane.ERROR_MESSAGE );
						return;
					}
			}*/
		}

		//Remove old appointments freq group & responses
		if( selectedApptId != -1 )
		{
			Appt ta = calGrid.getAppt(selectedApptId);
			selectedFreqGroupId = ta.getFreqGroupID();
			selectedReminderId = ta.getReminderID(); 
			if( selectedFreqGroupId != -1 )
				calGrid.removeFreqGroup(selectedFreqGroupId);

			//Remove responses
			ArrayList<Integer> idList = calGrid.getApptGroupIDList(selectedFreqGroupId);
			for( int i : idList )
				calGrid.removeResponse(i);
		}

		if( freq > 1 )
			selectedFreqGroupId = calGrid.getNextFreqGroupID();

		//Create new appointment objects
		for( int i = 0; i < freq; i++ )
		{
			Calendar s = (Calendar) start.clone();
			s.add( calIncType, calInc*i );
			Calendar e = (Calendar) end.clone();
			e.add( calIncType, calInc*i );
			TimeSpan apptTime = new TimeSpan( new Timestamp( s.getTimeInMillis() ), new Timestamp( e.getTimeInMillis() ) );

			NewAppt = new Appt();
			NewAppt.setTitle( titleField.getText().trim() );
			NewAppt.setInfo( detailArea.getText().trim() );
			NewAppt.setTimeSpan( apptTime );
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

			//Make new appointment or modify group of appointments
			if( selectedApptId == -1 || freq > 1 )
			{
				NewAppt.setApptID( calGrid.getNextApptID() );
				if( selectedFreqGroupId != -1 )
					NewAppt.setFreqGroupID(selectedFreqGroupId);
				if( isRemind.isSelected() )
				{
					r.setID( calGrid.getNextReminderID() );
					r.setApptID( NewAppt.getID() );
					NewAppt.setReminderID( r.getID() );
					calGrid.addNewReminder( r );
				}
				NewAppt.setInitUserID(calGrid.getCurUser().getID());
				NewAppt.setJoint(false);
				NewAppt.setPublic(cbIsPublic.isSelected());
				NewAppt.setIsScheduled(true);
				if( cbIsJoint.isSelected() )
				{
					NewAppt.setJoint(true);
					NewAppt.setIsScheduled(false);
					DefaultTableModel model = (DefaultTableModel) inviteListTable.getModel();
					Vector<Vector<String>> data = model.getDataVector();
					if( data.size() == 1 )
					{
						JOptionPane.showMessageDialog( this, "Please invite people!", "OVERLAP", JOptionPane.ERROR_MESSAGE );
						return;
					}
				}
				if( !calGrid.addNewAppt( NewAppt ) )
				{
					JOptionPane.showMessageDialog( this, "Error adding appointments!", "OVERLAP", JOptionPane.ERROR_MESSAGE );
					return;
				}
				else
				{
					if( cbIsJoint.isSelected() )
					{
						DefaultTableModel model = (DefaultTableModel) inviteListTable.getModel();
						Vector<Vector<String>> data = model.getDataVector();
						for( int j = 0; j < data.size(); j++ )
						{
							User u = calGrid.getUser(data.get(j).get(0));
							if( calGrid.getCurUserLoginID().equals(u.getLoginID()) )
								calGrid.addNewResponse(NewAppt.getID(), u.getID(), Response.ACCEPT);
							else
								calGrid.addNewResponse(NewAppt.getID(), u.getID(), Response.NEW);
							//NewAppt.inviteUser(data.get(j).get(0));
						}
					}
					else
						calGrid.addNewResponse(NewAppt.getID(), calGrid.getCurUser().getID(), Response.INDIV);
					
					/*DefaultTableModel model = (DefaultTableModel) inviteListTable.getModel();
					Vector<Vector<String>> data = model.getDataVector();
					for( int j = 0; j < data.size(); j++ )
						calGrid.inviteUser(NewAppt.getID(), data.get(j).get(0));
					if( selectedGroupId != -1 )
						calGrid.addToGroup(selectedGroupId, NewAppt.getID());*/
				}
			}
			//Modify to single appointment
			else
			{
				NewAppt.setApptID( selectedApptId );
				NewAppt.setInitUserID(calGrid.getCurUser().getID());
				NewAppt.setJoint(false);
				NewAppt.setPublic(cbIsPublic.isSelected());
				NewAppt.setIsScheduled(true);
				if( cbIsJoint.isSelected() )
				{
					NewAppt.setJoint(true);
					NewAppt.setIsScheduled(false);
					DefaultTableModel model = (DefaultTableModel) inviteListTable.getModel();
					Vector<Vector<String>> data = model.getDataVector();
					if( data.size() == 1 )
					{
						JOptionPane.showMessageDialog( this, "Please invite people!", "OVERLAP", JOptionPane.ERROR_MESSAGE );
						return;
					}
				}
				if( selectedReminderId == -1 )//No reminder originally
				{
					if( isRemind.isSelected() )
					{
						selectedReminderId = calGrid.getNextReminderID();
						r.setID( selectedReminderId );
						r.setApptID( NewAppt.getID() );
						NewAppt.setReminderID( selectedReminderId );
						calGrid.addNewReminder( r );
					}
				}
				else
				{
					if( isRemind.isSelected() )
					{
						r.setID( selectedReminderId );
						r.setApptID( NewAppt.getID() );
						if( selectedFreqGroupId == -1 )
							calGrid.updateReminder( r );
						else
							calGrid.addNewReminder( r );
					}
					else
					{
						//r = calGrid.getReminder(selectedReminderId);
						calGrid.removeReminder( selectedReminderId );
					}
				}
				boolean isSuccess;
				if( selectedFreqGroupId == -1 )
					isSuccess = calGrid.modifyAppt( NewAppt );
				else
				{
					NewAppt.setApptID(calGrid.getNextApptID());
					isSuccess = calGrid.addNewAppt( NewAppt );
				}
				if( !isSuccess )
				{
					JOptionPane.showMessageDialog( this, "Error adding appointments!", "OVERLAP", JOptionPane.ERROR_MESSAGE );
					return;
				}
				else
				{
					if( cbIsJoint.isSelected() )
					{
						DefaultTableModel model = (DefaultTableModel) inviteListTable.getModel();
						Vector<Vector<String>> data = model.getDataVector();
						for( int j = 0; j < data.size(); j++ )
						{
							User u = calGrid.getUser(data.get(j).get(0));
							if( calGrid.getCurUserLoginID().equals(u.getLoginID()) )
								calGrid.addNewResponse(NewAppt.getID(), u.getID(), Response.ACCEPT);
							else
								calGrid.addNewResponse(NewAppt.getID(), u.getID(), Response.NEW);
							//NewAppt.inviteUser(data.get(j).get(0));
						}
					}
					else
					{
						if( selectedFreqGroupId == -1 )
							calGrid.updateResponse(NewAppt.getID(), calGrid.getCurUser().getID(), Response.INDIV);
						else
							calGrid.addNewResponse(NewAppt.getID(), calGrid.getCurUser().getID(), Response.INDIV);
					}
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

	public void updateSetApp(Appt a) {
		//TODO: Update for freq group event!
		// Fix Me!
		TimeSpan ts = a.TimeSpan();
		Timestamp start = ts.StartTime();
		Timestamp end = ts.EndTime();
		yearF.setText(""+(start.getYear()+1900));
		monthF.setText(""+(start.getMonth()+1));
		dayF.setText(""+start.getDate());
		sTimeH.setText(""+start.getHours());
		sTimeM.setText(""+start.getMinutes());
		eTimeH.setText(""+end.getHours());
		eTimeM.setText(""+end.getMinutes());
		titleField.setText(a.getTitle());
		detailArea.setText(a.getInfo());
		selectedReminderId = a.getReminderID();
		if( selectedReminderId != -1 )
		{
			isRemind.setSelected(true);
			Reminder r = calGrid.getReminder(a.getReminderID());
			if( r != null )
			{
				rTimeH.setText(""+r.getHourInterval());
				rTimeM.setText(""+r.getMinuteInterval());
			}
		}
		if( a.getLocation() != null )
			cbLocation.setSelectedItem(a.getLocation());
		else
			cbLocation.setSelectedIndex(0);
		if( a.isPublic() )
			cbIsPublic.setSelected(true);
		if( a.isJoint() )
		{
			cbIsJoint.setSelected(true);
			tfInviteUser.setEnabled(true);
			btnInvite.setEnabled(true);
			ArrayList<Response> resList = calGrid.getResponseList(a.getID());
			DefaultTableModel model = (DefaultTableModel) inviteListTable.getModel();
			for( Response res : resList )
				model.addRow(new Object[]{res.getUserLoginID(), res.getResponse()});
		}
		else
			cbIsJoint.setSelected(false);
	}

	public void componentHidden(ComponentEvent e) {

	}

	public void componentMoved(ComponentEvent e) {

	}

	public void componentResized(ComponentEvent e) {

		Dimension dm = pDes.getSize();
		double width = dm.width * 0.93;
		double height = dm.getHeight() * 0.6;
		groupAndDetailPanel.setSize((int) width, (int) height);

	}

	public void componentShown(ComponentEvent e) {

	}
	
	public String getCurrentUser()		// get the id of the current user
	{
		return this.calGrid.curUser.getLoginID();
	}
	
	public void DisableEdit(){
		yearF.setEditable(false);
		monthF.setEditable(false);
		dayF.setEditable(false);
		sTimeH.setEditable(false);
		sTimeM.setEditable(false);
		eTimeH.setEditable(false);
		eTimeM.setEditable(false);
		titleField.setEditable(false);
		cbLocation.setEnabled(false);
		detailArea.setEditable(false);
		isRemind.setEnabled(false);
		rTimeH.setEditable(false);
		rTimeM.setEditable(false);
		rbOneTime.setEnabled(false);
		rbDaily.setEnabled(false);
		rbWeekly.setEnabled(false);
		rbMonthly.setEnabled(false);
		numOfFreqF.setEditable(false);
		cbIsJoint.setEnabled(false);
	}

	public void addAcceptReject()
	{
		saveBut.setText("Accept");
		rejectBut.show(true);
	}

	public void changeSaveToOK()
	{
		saveBut.setText("OK");
	}

	public void setAppointmentManagement(MyAppointmentManagementDialog am)
	{
		this.am = am;
	}

	public void setSchedule(TimeSpan t)
	{
		Timestamp s = t.StartTime();
		Timestamp e = t.EndTime();
		yearF.setText(s.getYear()+1900+"");
		monthF.setText(s.getMonth()+1+"");
		dayF.setText(s.getDate()+"");
		sTimeH.setText(s.getHours()+"");
		sTimeM.setText(s.getMinutes()+"");
		eTimeH.setText(e.getHours()+"");
		eTimeM.setText(e.getMinutes()+"");
	}
	/*
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
*/
}
