package hkust.cse.calendar.unit;

public class Location {
	private String name;

	public Location(String name)
	{
		this.name = name;
	}

	public String getName()
	{
		return name;
	}

	public void setName( String name )
	{
		this.name = name;
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
