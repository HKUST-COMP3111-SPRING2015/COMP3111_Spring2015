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


public class PublicAppointmentDialog extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel publicLabel;
	private JScrollPane publicScrollPane;
	private JTable publicTable;
	private TableModel publicModel;
	private JButton btnView;
	DefaultTableModel model;
	private CalGrid calGrid;
	private User curUser;
	private GregorianCalendar today;

	public PublicAppointmentDialog(GregorianCalendar today, CalGrid calGrid, User curUser)
	{
		this.today = today;
		this.calGrid = calGrid;
		this.curUser = curUser;
		this.setLayout(null);
		this.setLocationByPlatform(true);
		this.setSize(800,635);
		this.setTitle("Public Appointment");
		this.setResizable(false);
		this.setVisible(true);

		publicLabel = new JLabel("Public  Appoinments:");
		publicLabel.setBounds(10, 10, 680, 20);
		this.add(publicLabel);
		btnView = new JButton("View");
		btnView.setBounds(690, 10, 100, 20);
		btnView.addActionListener(this);
		this.add(btnView);
		Object pendingColumnNames[] = {"Appt ID", "User", "Appt", "Time", "Venue", "Status"};
		publicModel = new DefaultTableModel(null, pendingColumnNames);
		publicTable = new JTable(publicModel);
		publicTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		publicScrollPane = new JScrollPane(publicTable);
		publicScrollPane.setBounds(10, 35, 780, 115);
		this.add(publicScrollPane);

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

		ArrayList<Appt> apptList = calGrid.getPublicApptList(period);
		if( apptList != null )
		{
			model = (DefaultTableModel) publicTable.getModel();
			for( int i = 0; i < apptList.size(); i++ )
			{
				Appt a = apptList.get(i);
				User u = calGrid.getUser(a.getInitUserID());
				model.addRow(new Object[]{a.getID()+"", u.getLoginID(), a.getTitle(), a.TimeSpan().getTimeString(), a.getLocation().getName(), "Pending"});
			}
		}
	}

	public void refreshTable()
	{
		model = (DefaultTableModel) publicTable.getModel();
		while( model.getRowCount() > 0 )
			model.removeRow(0);
		setTableData();
	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == btnView)
		{
			model = (DefaultTableModel) publicTable.getModel();
			Vector<String> data = (Vector<String>) model.getDataVector().get(publicTable.getSelectedRow());
			int apptID = Integer.parseInt( data.get(0) );
			AppScheduler apptScheduler = new AppScheduler("Public Appointment", calGrid, apptID);
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
