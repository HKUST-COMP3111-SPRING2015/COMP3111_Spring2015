package hkust.cse.calendar.gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import hkust.cse.calendar.unit.Location;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;


public class AdminLocationsDialog extends JFrame {

	private static final long serialVersionUID = 1L;
	private CalGrid calGrid;
	private DefaultListModel<Location> listModel;
	private JScrollPane scrollPane;
	private JList<Location> list;
	private JLabel locationNameLabel;
	private JTextField tfLocationName;
	private JLabel groupNameLabel;
	private JTextField tfGroupName;
	private JLabel capacityLabel;
	private JComboBox<Integer> cbCapacity;
	private JButton btnView;
	private JButton btnEdit;
	private JButton btnAdd;
	private JButton btnRemove;

	public AdminLocationsDialog(final CalGrid calGrid)
	{
		this.calGrid = calGrid;
		this.setLayout(null);
		this.setLocationByPlatform(true);
		this.setTitle("Location Management");
		this.setSize(345,310);

		listModel = new DefaultListModel<Location>();
		ArrayList<Location> locations = calGrid.getLocationList();
		if( locations != null )
			for(Location l : locations)
				listModel.addElement(l);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(5, 5, 330, 120);
		this.add(scrollPane);

		list = new JList<Location>();
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
		        if (value instanceof Location) {
		        	Location location = (Location)value;
		        	String s = location.getName();
		        	if( location.isGroup() )
		        		s += (" of "+location.getGroup()+"("+location.getCapacity()+")");
		            setText(s);
		            //TODO: Need description for location?
		            //setToolTipText(location.getDescription());
		            //setIcon(location.getIcon());
		        }
		        return this;
		    }
		});

		locationNameLabel = new JLabel("Name:");
		locationNameLabel.setBounds(15, 130, 80, 20);
		this.add(locationNameLabel);
		tfLocationName = new JTextField();
		tfLocationName.setBounds(95, 130, 220, 20);
		this.add(tfLocationName);

		groupNameLabel = new JLabel("Group:");
		groupNameLabel.setBounds(15, 155, 80, 20);
		this.add(groupNameLabel);
		tfGroupName = new JTextField();
		tfGroupName.setBounds(95, 155, 220, 20);
		this.add(tfGroupName);

		capacityLabel = new JLabel("Capacity:");
		capacityLabel.setBounds(15, 180, 80, 20);
		this.add(capacityLabel);
		cbCapacity = new JComboBox<Integer>();
		for( int i = 0; i < 100; i+=5 )
			cbCapacity.addItem(i);
		cbCapacity.setEditable(false);
		cbCapacity.setBounds(95, 180, 220, 20);
		this.add(cbCapacity);

		btnView = new JButton( "View" );
		btnView.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					Location l = list.getSelectedValue();
					tfLocationName.setText(l.getName());
					if( l.isGroup() )
					{
						tfGroupName.setText(l.getGroup());
						cbCapacity.setSelectedItem(l.getCapacity());
					}
				}
			}
		);
		btnView.setBounds(5, 205, 160, 20);
		this.add(btnView);

		btnEdit = new JButton( "Edit" );
		btnEdit.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String locationName = tfLocationName.getText().trim();
					String groupName = tfGroupName.getText().trim();
					if(!locationName.isEmpty())
					{
						if( (groupName.isEmpty() && cbCapacity.getSelectedIndex() != 0) || (!groupName.isEmpty() && cbCapacity.getSelectedIndex() == 0) )
						{
							JOptionPane.showMessageDialog(null, "Unmatch Information!", "ERROR", JOptionPane.ERROR_MESSAGE);
							return;
						}
						Location l = new Location(locationName, groupName, (Integer)cbCapacity.getSelectedItem());
						l.setID(list.getSelectedValue().getID());
						calGrid.updateLocation(l);
						if(list.getSelectedValue() != null)
							listModel.removeElement(list.getSelectedValue());
						listModel.addElement(l);
						tfLocationName.setText("");
						tfGroupName.setText("");
						cbCapacity.setSelectedIndex(0);
						list.setSelectedIndex(-1);
					}
				}
			}
		);
		btnEdit.setBounds(175, 205, 160, 20);
		this.add(btnEdit);

		btnAdd = new JButton( "Add" );
		btnAdd.addActionListener
		(
			new ActionListener()
			{
				@Override
				public void actionPerformed(ActionEvent e)
				{
					String locationName = tfLocationName.getText().trim();
					String groupName = tfGroupName.getText().trim();
					if(!locationName.isEmpty())
					{
						if( (groupName.isEmpty() && cbCapacity.getSelectedIndex() != 0) || (!groupName.isEmpty() && cbCapacity.getSelectedIndex() == 0) )
						{
							JOptionPane.showMessageDialog(null, "Unmatch Information!", "ERROR", JOptionPane.ERROR_MESSAGE);
							return;
						}
						Location l = new Location(locationName, groupName, (Integer)cbCapacity.getSelectedItem());
						l.setID(calGrid.getNextLocationID());
						
						if(listModel.contains(l))
						{
							JOptionPane.showMessageDialog(null, "Duplicate location!", "ERROR", JOptionPane.ERROR_MESSAGE);
							return;
						}
						else
						{
							calGrid.addNewLocation(l);
							listModel.addElement(l);
							tfLocationName.setText("");
							tfGroupName.setText("");
							cbCapacity.setSelectedIndex(0);
						}
					}
				}
			}
		);
		btnAdd.setBounds(5, 230, 160, 20);
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
					{
						calGrid.removeLocation(list.getSelectedValue().getID());
						listModel.removeElement(list.getSelectedValue());
					}
				}
			}
		);
		btnRemove.setBounds(175, 230, 160, 20);
		this.add(btnRemove);

		this.setResizable(false);
		this.setVisible(true);
	}

	private void closeWindow()
	{
		this.dispose();
	}
}
