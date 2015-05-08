package hkust.cse.calendar.unit;

import java.io.Serializable;

public class Location implements Serializable{
	private static final long serialVersionUID = 1L;
	private int locationID;
	private String name;
	private String group;
	private int capacity;
	private boolean isGroup;

	public Location(String name, String group, int capacity)
	{
		this.name = name;
		this.group = group;
		if(group.isEmpty())
			isGroup = false;
		else
			isGroup = true;
		this.capacity = capacity;
	}

	public int getID()
	{
		return locationID;
	}

	public void setID( int id )
	{
		this.locationID = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
	}

	public String getGroup()
	{
		return group;
	}

	public void setGroup( String group )
	{
		this.group = group;
		if(group.isEmpty())
			isGroup = false;
		else
			isGroup = true;
	}

	public int getCapacity()
	{
		return capacity;
	}

	public void setCapacity( int capacity )
	{
		this.capacity = capacity;
	}

	public boolean isGroup()
	{
		return isGroup;
	}

	public void setIsGroup(boolean b)
	{
		this.isGroup = b;
	}

	@Override
	public boolean equals( Object o )
	{
		if( o == null )
			return false;
		if( !o.getClass().equals( this.getClass() ) )
			return false;
		if( o == this )
			return true;
		if ( o instanceof Location )
		{
			Location l = (Location) o;
			if ( this.name.equals( l.getName() ) )
				return true;
		}
	    return false;
	}
}
