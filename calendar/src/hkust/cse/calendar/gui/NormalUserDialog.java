package hkust.cse.calendar.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.User;

import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;


public class NormalUserDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	private JLabel instructionLabel;
	private JLabel firstNameLabel;
	private JTextField tfFirstName;
	private JLabel lastNameLabel;
	private JTextField tfLastName;
	private JLabel userIDLabel;
	private JTextField tfUserID;
	private JLabel passwordLabel;
	private JPasswordField pfPassword;
	private JLabel confirmPasswordLabel;
	private JPasswordField pfConfirmPassword;
	private JLabel emailLabel;
	private JTextField tfEmail;
	private JLabel userTypeLabel;
	private JTextField tfUserType;
	private JButton btnConfirm;
	private JButton btnCancel;
	private JButton btnOK;
	private CalGrid calGrid;
	private User curUser;

	public NormalUserDialog(String title, final CalGrid calGrid, User u)
	{
		this(calGrid, u);
		tfFirstName.setEditable(false);
		tfLastName.setEditable(false);
		tfUserID.setEditable(false);
		pfPassword.setEditable(false);
		pfConfirmPassword.setEditable(false);
		tfEmail.setEditable(false);
		btnConfirm.setVisible(false);
		btnCancel.setVisible(false);
		btnOK = new JButton("OK");
		btnOK.setBounds(5, 245, 330, 20);
		btnOK.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					closeWindow();
				}
			}
		);
		this.add(btnOK);
	}

	public NormalUserDialog(final CalGrid calGrid, User u)
	{
		this.calGrid = calGrid;
		this.curUser = u;
		this.setLayout(null);
		this.setLocationByPlatform(true);
		this.setSize(355,305);
		this.setTitle("Account Settings");

		instructionLabel = new JLabel("Please fill the below information for registration.");
		instructionLabel.setBounds(15, 5, 325, 20);
		this.add(instructionLabel);

		firstNameLabel = new JLabel("First Name:");
		firstNameLabel.setBounds(15, 35, 130, 20);
		this.add(firstNameLabel);
		tfFirstName = new JTextField();
		tfFirstName.setBounds(145, 35, 190, 20);
		this.add(tfFirstName);

		lastNameLabel = new JLabel("Last Name:");
		lastNameLabel.setBounds(15, 65, 130, 20);
		this.add(lastNameLabel);
		tfLastName = new JTextField();
		tfLastName.setBounds(145, 65, 190, 20);
		this.add(tfLastName);
	
		userIDLabel = new JLabel("User ID:");
		userIDLabel.setBounds(15, 95, 130, 20);
		this.add(userIDLabel);
		tfUserID = new JTextField();
		tfUserID.setBounds(145, 95, 190, 20);
		this.add(tfUserID);

		passwordLabel = new JLabel("Password:");
		passwordLabel.setBounds(15, 125, 130, 20);
		this.add(passwordLabel);
		pfPassword = new JPasswordField();
		pfPassword.setBounds(145, 125, 190, 20);
		this.add(pfPassword);

		confirmPasswordLabel = new JLabel("Confrim Password:");
		confirmPasswordLabel.setBounds(15, 155, 130, 20);
		this.add(confirmPasswordLabel);
		pfConfirmPassword = new JPasswordField();
		pfConfirmPassword.setBounds(145, 155, 190, 20);
		this.add(pfConfirmPassword);

		emailLabel = new JLabel("Email:");
		emailLabel.setBounds(15, 185, 130, 20);
		this.add(emailLabel);
		tfEmail = new JTextField();
		tfEmail.setBounds(145, 185, 190, 20);
		this.add(tfEmail);

		userTypeLabel = new JLabel("User Type:");
		userTypeLabel.setBounds(15, 215, 130, 20);
		this.add(userTypeLabel);
		tfUserType = new JTextField();
		tfUserType.setBounds(145, 215, 190, 20);
		this.add(tfUserType);
		tfUserType.setEditable(false);

		tfFirstName.setText(curUser.getFirstName());
		tfLastName.setText(curUser.getLastName());
		tfUserID.setText(curUser.getLoginID());
		tfEmail.setText(curUser.getEmail());
		if(curUser.getUserType() == User.ADMIN)
			tfUserType.setText("Admin");
		else
			tfUserType.setText("Normal");

		btnConfirm = new JButton( "Confirm" );
		btnConfirm.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String firstName = tfFirstName.getText().trim();
					String lastName = tfLastName.getText().trim();
					String userID = tfUserID.getText();
					String password = pfPassword.getText();
					String confirmPassword = pfConfirmPassword.getText();
					String email = tfEmail.getText().trim();
					int userType;
					if( tfUserType.equals("Admin") )
						userType = User.ADMIN;
					else
						userType = User.NORMAL;
					if( !password.equals(confirmPassword) )
					{
						JOptionPane.showMessageDialog(null, "Password fail!", "WARNING", JOptionPane.WARNING_MESSAGE);
						return;
					}
					if( !userID.isEmpty() && !password.isEmpty() && !email.isEmpty() )
					{
						calGrid.updateUser( new User(curUser.getID(), firstName, lastName, userID, password, tfEmail.getText().trim(), userType) );
						closeWindow();
					}
					else
						JOptionPane.showMessageDialog(null, "Missing Information!", "WARNING", JOptionPane.WARNING_MESSAGE);
				}
			}
		);
		btnConfirm.setBounds(5, 245, 160, 20);
		this.add(btnConfirm);

		btnCancel = new JButton( "Cancel" );
		btnCancel.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					closeWindow();
				}
			}
		);
		btnCancel.setBounds(175, 245, 160, 20);
		this.add(btnCancel);

		this.setResizable(false);
		this.setVisible(true);
	}

	private void closeWindow()
	{
		this.dispose();
	}
}
