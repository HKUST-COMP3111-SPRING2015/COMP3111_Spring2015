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


public class AvailableScheduleDialog extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel headingLabel;
	private JScrollPane scheduleScrollPane;
	private JTable scheduleTable;
	private TableModel scheduleModel;
	private JButton btnSelect;
	DefaultTableModel model;
	private AppScheduler scheduler;
	private ArrayList<TimeSpan> availableList;

	public AvailableScheduleDialog(AppScheduler scheduler, ArrayList<TimeSpan> availableList)
	{
		this.scheduler = scheduler;
		this.availableList = availableList;
		this.setLayout(null);
		this.setLocationByPlatform(true);
		this.setSize(800,635);
		this.setTitle("Available Schedule");
		this.setResizable(false);
		this.setVisible(true);

		headingLabel = new JLabel("Please select the schedule:");
		headingLabel.setBounds(10, 10, 680, 20);
		this.add(headingLabel);
		btnSelect = new JButton("Select");
		btnSelect.setBounds(690, 10, 100, 20);
		btnSelect.addActionListener(this);
		this.add(btnSelect);
		Object pendingColumnNames[] = {"Start Time", "End Time"};
		scheduleModel = new DefaultTableModel(null, pendingColumnNames);
		scheduleTable = new JTable(scheduleModel);
		scheduleTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		scheduleScrollPane = new JScrollPane(scheduleTable);
		scheduleScrollPane.setBounds(10, 35, 780, 115);
		this.add(scheduleScrollPane);

		setTableData();
		
	}

	private void setTableData()
	{
		if( availableList != null )
		{
			model = (DefaultTableModel) scheduleTable.getModel();
			for( int i = 0; i < availableList.size(); i++ )
			{
				TimeSpan t = availableList.get(i);
				System.out.println(t.getTimeString());
				model.addRow(new Object[]{t.StartTime(), t.EndTime()});
			}
		}

	}

	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() == btnSelect)
		{
			model = (DefaultTableModel) scheduleTable.getModel();
			TimeSpan t = availableList.get( scheduleTable.getSelectedRow() );
			scheduler.setSchedule(t);
			closeWindow();
		}
		
	}

	private void closeWindow()
	{
		this.dispose();
	}
}
