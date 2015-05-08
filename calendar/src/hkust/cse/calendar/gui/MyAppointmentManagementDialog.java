package hkust.cse.calendar.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Vector;

import hkust.cse.calendar.unit.Appt;
import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.Response;
import hkust.cse.calendar.unit.TimeSpan;
import hkust.cse.calendar.unit.User;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.BadLocationException;


public class MyAppointmentManagementDialog extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel initLabel;
	private JScrollPane initScrollPane;
	private JTable initTable;
	private TableModel initModel;
	private JLabel pendingLabel;
	private JScrollPane pendingScrollPane;
	private JTable pendingTable;
	private TableModel pendingModel;
	private JButton btnViewPending;
	private JLabel acceptLabel;
	private JScrollPane acceptScrollPane;
	private JTable acceptTable;
	private TableModel acceptModel;
	private JButton btnViewAccept;
	private JLabel rejectLabel;
	private JScrollPane rejectScrollPane;
	private JTable rejectTable;
	private TableModel rejectModel;
	private JButton btnViewReject;
	DefaultTableModel model;
	private CalGrid calGrid;
	private User curUser;
	private GregorianCalendar today;

	public MyAppointmentManagementDialog(GregorianCalendar today, CalGrid calGrid, User curUser)
	{
		this.today = today;
		this.calGrid = calGrid;
		this.curUser = curUser;
		this.setLayout(null);
		this.setLocationByPlatform(true);
		this.setSize(800,635);
		this.setTitle("My Group Appointment Management");
		this.setResizable(false);
		this.setVisible(true);

		initLabel = new JLabel("Appoinments Initiated by Me:");
		initLabel.setBounds(10, 10, 780, 20);
		this.add(initLabel);
		Object initColumnNames[] = {"Appt ID", "Appt", "Time", "Venue", "Status"};
		initModel = new DefaultTableModel(null, initColumnNames);
		initTable = new JTable(initModel);
		initTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		initScrollPane = new JScrollPane(initTable);
		initScrollPane.setBounds(10, 35, 780, 115);
		this.add(initScrollPane);

		pendingLabel = new JLabel("Pending Invitations of Appoinments:");
		pendingLabel.setBounds(10, 160, 680, 20);
		this.add(pendingLabel);
		btnViewPending = new JButton("View");
		btnViewPending.setBounds(690, 160, 100, 20);
		btnViewPending.addActionListener(this);
		this.add(btnViewPending);
		Object pendingColumnNames[] = {"Appt ID", "Appt", "Time", "Venue", "Status"};
		pendingModel = new DefaultTableModel(null, pendingColumnNames);
		pendingTable = new JTable(pendingModel);
		pendingTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pendingScrollPane = new JScrollPane(pendingTable);
		pendingScrollPane.setBounds(10, 185, 780, 115);
		this.add(pendingScrollPane);

		acceptLabel = new JLabel("Accepted Invitations of Appoinments:");
		acceptLabel.setBounds(10, 310, 680, 20);
		this.add(acceptLabel);
		btnViewAccept = new JButton("View");
		btnViewAccept.setBounds(690, 310, 100, 20);
		btnViewAccept.addActionListener(this);
		this.add(btnViewAccept);
		Object acceptColumnNames[] = {"Appt ID", "Appt", "Time", "Venue", "Status"};
		acceptModel = new DefaultTableModel(null, acceptColumnNames);
		acceptTable = new JTable(acceptModel);
		acceptTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		acceptScrollPane = new JScrollPane(acceptTable);
		acceptScrollPane.setBounds(10, 335, 780, 115);
		this.add(acceptScrollPane);

		rejectLabel = new JLabel("Rejected Invitations of Appoinments:");
		rejectLabel.setBounds(10, 460, 680, 20);
		this.add(rejectLabel);
		btnViewReject = new JButton("View");
		btnViewReject.setBounds(690, 460, 100, 20);
		btnViewReject.addActionListener(this);
		this.add(btnViewReject);
		Object rejectColumnNames[] = {"Appt ID", "Appt", "Time", "Venue", "Status"};
		rejectModel = new DefaultTableModel(null, rejectColumnNames);
		rejectTable = new JTable(rejectModel);
		rejectTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		rejectScrollPane = new JScrollPane(rejectTable);
		rejectScrollPane.setBounds(10, 485, 780, 115);
		this.add(rejectScrollPane);

		setTableData();
		
	}

	private void setTableData()
	{
		Timestamp start = new Timestamp(0);
		start.setYear(today.get(Calendar.YEAR));
		start.setMonth(today.get(today.MONTH));
		start.setDate(today.get(today.DAY_OF_MONTH));
		start.setHours(0);
		start.setMinutes(0);
		start.setSeconds(0);
		Timestamp end = new Timestamp(0);
		end.setYear(2100);
		end.setMonth(11);
		end.setDate(31);
		end.setHours(23);
		end.setMinutes(59);
		end.setSeconds(59);
		TimeSpan period = new TimeSpan(start, end);

		ArrayList<Appt> initiateApptList = calGrid.getInitiateApptList(curUser.getID(), period);
		if( initiateApptList != null )
		{
			model = (DefaultTableModel) initTable.getModel();
			for( int i = 0; i < initiateApptList.size(); i++ )
			{
				Appt a = initiateApptList.get(i);
				String status = a.isScheduled() ? "Success" : "Waiting";
				model.addRow(new Object[]{a.getID(), a.getTitle(), a.TimeSpan().getTimeString(), a.getLocation().getName(), status});
			}
		}

		ArrayList<Appt> pendingApptList = calGrid.getApptResponseList(curUser.getID(), Response.NEW, period);
		if( pendingApptList != null )
		{
			model = (DefaultTableModel) pendingTable.getModel();
			for( int i = 0; i < pendingApptList.size(); i++ )
			{
				Appt a = pendingApptList.get(i);
				model.addRow(new Object[]{a.getID()+"", a.getTitle(), a.TimeSpan().getTimeString(), a.getLocation().getName(), "Pending"});
			}
		}

		ArrayList<Appt> acceptApptList = calGrid.getApptResponseList(curUser.getID(), Response.ACCEPT, period);
		if( acceptApptList != null )
		{
			model = (DefaultTableModel) acceptTable.getModel();
			for( int i = 0; i < acceptApptList.size(); i++ )
			{
				Appt a = acceptApptList.get(i);
				model.addRow(new Object[]{a.getID()+"", a.getTitle(), a.TimeSpan().getTimeString(), a.getLocation().getName(), "Accepted"});
			}
		}
		
		ArrayList<Appt> rejectApptList = calGrid.getApptResponseList(curUser.getID(), Response.REJECT, period);
		if( rejectApptList != null )
		{
			model = (DefaultTableModel) rejectTable.getModel();
			for( int i = 0; i < rejectApptList.size(); i++ )
			{
				Appt a = rejectApptList.get(i);
				model.addRow(new Object[]{a.getID()+"", a.getTitle(), a.TimeSpan().getTimeString(), a.getLocation().getName(), "Rejected"});
			}
		}
	}

	public void refreshTable()
	{
		model = (DefaultTableModel) pendingTable.getModel();
		while( model.getRowCount() > 0 )
			model.removeRow(0);
		model = (DefaultTableModel) acceptTable.getModel();
		while( model.getRowCount() > 0 )
			model.removeRow(0);
		model = (DefaultTableModel) rejectTable.getModel();
		while( model.getRowCount() > 0 )
			model.removeRow(0);
		setTableData();
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == btnViewPending)
		{
			model = (DefaultTableModel) pendingTable.getModel();
			Vector<String> data = (Vector<String>) model.getDataVector().get(pendingTable.getSelectedRow());
			int apptID = Integer.parseInt( data.get(0) );
			AppScheduler apptScheduler = new AppScheduler("Confirmation of Appointment", calGrid, apptID);
			apptScheduler.updateSetApp(calGrid.getAppt(apptID));
			apptScheduler.DisableEdit();
			apptScheduler.addAcceptReject();
			apptScheduler.setAppointmentManagement(this);
			apptScheduler.show();
			apptScheduler.setResizable(false);
		}
		else if(e.getSource() == btnViewAccept)
		{
			model = (DefaultTableModel) acceptTable.getModel();
			Vector<String> data = (Vector<String>) model.getDataVector().get(acceptTable.getSelectedRow());
			int apptID = Integer.parseInt( data.get(0) );
			AppScheduler apptScheduler = new AppScheduler("Accepted Appointment", calGrid, apptID);
			apptScheduler.updateSetApp(calGrid.getAppt(apptID));
			apptScheduler.DisableEdit();
			apptScheduler.changeSaveToOK();
			apptScheduler.show();
			apptScheduler.setResizable(false);
		}
		else if(e.getSource() == btnViewReject)
		{
			model = (DefaultTableModel) rejectTable.getModel();
			Vector<String> data = (Vector<String>) model.getDataVector().get(rejectTable.getSelectedRow());
			int apptID = Integer.parseInt( data.get(0) );
			AppScheduler apptScheduler = new AppScheduler("Rejected Appointment", calGrid, apptID);
			apptScheduler.updateSetApp(calGrid.getAppt(apptID));
			apptScheduler.DisableEdit();
			apptScheduler.changeSaveToOK();
			apptScheduler.show();
			apptScheduler.setResizable(false);
		}
	}

	private void closeWindow()
	{
		this.dispose();
	}
}
