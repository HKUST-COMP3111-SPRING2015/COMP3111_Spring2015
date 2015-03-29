package hkust.cse.calendar.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hkust.cse.calendar.apptstorage.ApptStorageControllerImpl;
import hkust.cse.calendar.unit.Location;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;


public class LocationsDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	private ApptStorageControllerImpl ctrl;
	private DefaultListModel<Location> listModel;
	private JList<Location> list;
	private JTextField locationNameText;
	private JButton btnAdd;
	private JButton btnRemove;
	private JButton btnConfirm;

	public LocationsDialog( ApptStorageControllerImpl ctrl )
	{
		this.ctrl = ctrl;
		//this.setLayout(new BorderLayout());
		this.setLayout(null);
		this.setLocationByPlatform(true);
		this.setSize(355,215);

		listModel = new DefaultListModel<Location>();
		Location[] locations = ctrl.getLocationList();
		if( locations != null )
			for(Location l : locations)
				listModel.addElement(l);

		list = new JList<Location>();
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		list.setSelectedIndex(0);
		list.setModel(listModel);
		list.setBounds(5, 5, 330, 120);
		this.add(list);
		/*list.addListSelectionListener
		(
			new ListSelectionListener()
		);*/
		list.setCellRenderer(new DefaultListCellRenderer() {
		    @Override
		    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
		        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
		        if (value instanceof Location) {
		        	Location location = (Location)value;
		            setText(location.getName());
		            //TODO: Need description for location?
		            //setToolTipText(location.getDescription());
		            //setIcon(location.getIcon());
		        }
		        return this;
		    }
		});

		locationNameText = new JTextField();
		locationNameText.setBounds(5, 130, 160, 20);
		this.add(locationNameText);

		btnAdd = new JButton( "Add" );
		btnAdd.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if(locationNameText != null && !locationNameText.getText().trim().isEmpty())
					{
						Location l = new Location(locationNameText.getText().trim());
						if(listModel.contains(l))
						{
							JOptionPane.showMessageDialog(null, "Duplicate location!", "ERROR", JOptionPane.ERROR_MESSAGE);
						}
						else
						{
							listModel.addElement(l);
							locationNameText.setText("");
						}
					}
				}
			}
		);
		btnAdd.setBounds(170, 130, 80, 20);
		this.add(btnAdd);

		btnRemove = new JButton( "Remove" );
		btnRemove.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					if(list.getSelectedValue() != null)
						listModel.removeElement(list.getSelectedValue());
				}
			}
		);
		btnRemove.setBounds(255, 130, 80, 20);
		this.add(btnRemove);

		btnConfirm = new JButton( "Confirm" );
		btnConfirm.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					int n = JOptionPane.showConfirmDialog(null, "Confirm Location?", "CONFIRM", JOptionPane.YES_NO_OPTION);
					if (n == JOptionPane.YES_OPTION)
					{
						ListModel<Location> lm = list.getModel();
						Location[] locations = new Location[lm.getSize()];
						for(int i = 0; i < lm.getSize(); i++)
							locations[i] = lm.getElementAt(i);
						//ctrl.setLocationList(locations);
						setLocationList(locations);
						closeWindow();
					}
				}
			}
		);
		btnConfirm.setBounds(5, 155, 330, 20);
		this.add(btnConfirm);

		this.setResizable(false);
		this.setVisible(true);
	}

	private void setLocationList(Location[] locations)
	{
		ctrl.setLocationList(locations);
	}

	private void closeWindow()
	{
		this.dispose();
	}
}
