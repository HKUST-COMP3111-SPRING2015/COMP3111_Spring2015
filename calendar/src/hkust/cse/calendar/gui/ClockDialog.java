package hkust.cse.calendar.gui;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Location;

import javax.swing.*;


public class ClockDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	private CalGrid calGrid;
	private Thread clockThread;
	private JLabel curTimeLabel;
	private JSpinner spCurDateTime;
	private JButton btnConfirm;
	private JButton btnCancel;
	private boolean isRunning = true;

	public ClockDialog( final CalGrid calGrid, Date now )
	{
		this.calGrid = calGrid;
		//this.setLayout(new BorderLayout());
		this.setLayout(null);
		this.setLocationByPlatform(true);
		this.setSize(500,300);
		this.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
		this.setResizable(false);
		this.setVisible(true);

		//final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		curTimeLabel = new JLabel("Current Time:");
		curTimeLabel.setBounds(10, 10, 170, 30);
		curTimeLabel.setFont(new Font("TimesRoman", Font.BOLD, 20));
		this.add(curTimeLabel);

		Calendar c = Calendar.getInstance();
		c.setTime(now);
		Date initialDate = c.getTime();
		c.add(Calendar.YEAR, -100);
		Date earliestDate = c.getTime();
		c.add(Calendar.YEAR, 200);
		Date latestDate = c.getTime();
		spCurDateTime = new JSpinner(new SpinnerDateModel(initialDate, earliestDate, latestDate, Calendar.YEAR));
		spCurDateTime.setEditor(new JSpinner.DateEditor(spCurDateTime, "dd / MMM / yyyy   HH : mm : ss"));
		spCurDateTime.setBounds(180, 10, 310, 30);
		spCurDateTime.setFont(new Font("TimesRoman", Font.BOLD, 20));
		this.add(spCurDateTime);
		clockThread = new Thread()
		{
			@Override
			public void run()
			{
				while(isRunning)
				{
					//System.out.println((Date)spCurDateTime.getModel().getValue());
					Calendar cur = Calendar.getInstance();
					cur.setTime((Date)spCurDateTime.getModel().getValue());
					cur.add(Calendar.SECOND, 1);
					int pos = ((JSpinner.DefaultEditor)spCurDateTime.getEditor()).getTextField().getCaretPosition();
					spCurDateTime.getModel().setValue(cur.getTime());
					((JSpinner.DefaultEditor)spCurDateTime.getEditor()).getTextField().setCaretPosition(pos);
					try
					{
						sleep(1000);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			}
		};
		clockThread.start();

		btnConfirm = new JButton("Confirm");
		btnConfirm.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int n = JOptionPane.showConfirmDialog(null, "Confirm changes?", "CONFIRM", JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION)
					{
						calGrid.updateTime((Date)spCurDateTime.getModel().getValue());
						dispose();
					}
				}
			}
		);
		btnConfirm.setBounds(140, 100, 100, 20);
		this.add(btnConfirm);

		btnCancel = new JButton("Cancel");
		btnCancel.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int n = JOptionPane.showConfirmDialog(null, "Cancel changes?", "CANCEL", JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION)
						dispose();
				}
			}
		);
		btnCancel.setBounds(260, 100, 100, 20);
		this.add(btnCancel);
	}

	@Override
	public void dispose()
	{
		isRunning = false;
		if( !clockThread.isInterrupted() )
			clockThread.interrupt();
		super.dispose();
	}
}
