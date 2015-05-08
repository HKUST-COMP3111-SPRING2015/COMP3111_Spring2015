package hkust.cse.calendar.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import hkust.cse.calendar.unit.Location;
import hkust.cse.calendar.unit.User;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;


public class AdminUserDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	private CalGrid calGrid;
	private User curUser;
	private DefaultListModel<User> listModel;
	private JScrollPane scrollPane;
	private JList<User> list;
	private JButton btnEdit;
	private JButton btnRemove;
	private JButton btnView;

	public AdminUserDialog(final CalGrid calGrid, User user)
	{
		this.calGrid = calGrid;
		this.curUser = user;
		this.setLayout(null);
		this.setLocationByPlatform(true);
		this.setTitle("User Management");
		this.setSize(355,215);

		listModel = new DefaultListModel<User>();
		ArrayList<User> userList = calGrid.getUserList();
		for(User u : userList)
			listModel.addElement(u);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 5, 330, 120);
		this.add(scrollPane);

		list = new JList<User>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setModel(listModel);
		//list.setBounds(5, 5, 330, 120);
		//this.add(list);
		scrollPane.setViewportView(list);
		list.setCellRenderer(new DefaultListCellRenderer() {
		    @Override
		    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        if (value instanceof User) {
		        	User user = (User)value;
		            setText(user.getLoginID());
		        }
		        return this;
		    }
		});

		btnEdit = new JButton( "Edit" );
		btnEdit.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					NormalUserDialog nud = new NormalUserDialog( calGrid, list.getSelectedValue() );
				}
			}
		);
		btnEdit.setBounds(5, 130, 160, 20);
		this.add(btnEdit);

		btnRemove = new JButton( "Remove" );
		btnRemove.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if(list.getSelectedValue() != null)
					{
						if(list.getSelectedValue().getLoginID().equals(curUser.getLoginID()))
						{
							JOptionPane.showMessageDialog(null, "You cannot remove yourself!", "FAIL", JOptionPane.WARNING_MESSAGE);
							return;
						}
						int n = JOptionPane.showConfirmDialog(null, "Confirm Delete?", "CONFIRM", JOptionPane.YES_NO_OPTION);
						if (n == JOptionPane.YES_OPTION)
						{
							calGrid.removeUser(list.getSelectedValue().getID());
							listModel.removeElement(list.getSelectedValue());
						}
					}
				}
			}
		);
		btnRemove.setBounds(175, 130, 160, 20);
		this.add(btnRemove);

		btnView = new JButton( "View" );
		btnView.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					NormalUserDialog nud = new NormalUserDialog( ("View User: " + list.getSelectedValue().getLoginID()), calGrid, list.getSelectedValue() );
				}
			}
		);
		btnView.setBounds(5, 155, 330, 20);
		this.add(btnView);

		this.setResizable(false);
		this.setVisible(true);
	}
}
