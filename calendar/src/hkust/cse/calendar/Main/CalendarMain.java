package hkust.cse.calendar.Main;

import hkust.cse.calendar.apptstorage.Controller;
import hkust.cse.calendar.apptstorage.Model;
import hkust.cse.calendar.gui.CalGrid;


public class CalendarMain
{
	private CalGrid cd;
	private Controller ctrl;
	private Model model;
	
	public static void main(String[] args)
	{
		CalendarMain main = new CalendarMain();
	}

	public CalendarMain()
	{
		cd = new CalGrid(this);
		ctrl = new Controller();
		model = new Model();
		cd.setController(ctrl);
		cd.setModel(model);
		ctrl.setCalGrid(cd);
		ctrl.setModel(model);
		cd.login();
	}

	public void restart()
	{
		cd.dispose();
		cd = new CalGrid(this);
		cd.setController(ctrl);
		cd.setModel(model);
		cd.login();
	}
}
		