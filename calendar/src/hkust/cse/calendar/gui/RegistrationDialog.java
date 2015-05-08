package hkust.cse.calendar.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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


public class RegistrationDialog extends JFrame {

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
	private ButtonGroup bgUserType;
	private JRadioButton adminType;
	private JRadioButton normalType;
	private JButton btnConfirm;
	private JButton btnCancel;

	public RegistrationDialog(final CalGrid calGrid)
	{
		this.setLayout(null);
		this.setLocationByPlatform(true);
		this.setSize(355,305);
		this.setTitle("Account Registration");

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

		bgUserType = new ButtonGroup();
		normalType = new JRadioButton("Normal");
		normalType.setBounds(15, 215, 100, 20);
		bgUserType.add(normalType);
		normalType.setSelected(true);
		this.add(normalType);
		adminType = new JRadioButton("Admin");
		adminType.setBounds(115, 215, 100, 20);
		bgUserType.add(adminType);
		this.add(adminType);

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
					if( adminType.isSelected() )
						userType = User.ADMIN;
					else
						userType = User.NORMAL;
					if( !password.equals(confirmPassword) )
					{
						JOptionPane.showMessageDialog(null, "Password fail!");
						return;
					}
					if( !userID.isEmpty() && !password.isEmpty() && !email.isEmpty() )
					{
						if( calGrid.isUserExist(userID) )
							JOptionPane.showMessageDialog(null, "Duplicate User!");
						else
						{
							calGrid.addUser( new User(-1, firstName, lastName, userID, password, tfEmail.getText().trim(), userType) );
							closeWindow();
						}
					}
					else
						JOptionPane.showMessageDialog(null, "Missing Information!");
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
