package sspinja;
			
import java.util.ArrayList;
import spinja.model.Transition;	
import sspinja.scheduler.promela.model.SchedulerPromelaModel;	
import sspinja.GenerateCode;
			
//Scheduler converter: Automatic generation
//Automatic generation
public class SchedulerState {
	public ArrayList<SchedulerState> father = new ArrayList<SchedulerState>();
	public ArrayList<Transition> apptrans = new ArrayList<Transition>();
	public ArrayList<GenerateCode> generatecodes = new ArrayList<GenerateCode>() ;
	public boolean ndpoint = SchedulerPromelaModel.scheduler.checkNDpoint();
	
	public int identifier = -1;
	public int prunid = -1;
	public int execid = -1;
	public int runinstance = 0;
	
	public boolean results[];
	public boolean checked[];
	public boolean duplicate = false ;
	
	public ArrayList<SchedulerState> next = new ArrayList<SchedulerState>();
	public ArrayList<Transition> trans = new ArrayList<Transition>();
	public ArrayList<Integer> newID = new ArrayList<Integer>();
	public ArrayList<Integer> endID = new ArrayList<Integer>();
	//public ArrayList<Integer> pID = new ArrayList<Integer>();
	
	public GenerateCode getGenerateCode(int id) {
		return null ;
	}
	public void update(int depth, boolean isCycle, boolean isCurrentState) {}	
	public void print(ArrayList<SchedulerState> schStateList, String pref, boolean fulltrans) {
		if (!schStateList.contains(this)) schStateList.add(this) ;
		System.out.println(pref + "[" + prunid+ "," + identifier + "]");
		for (SchedulerState child : next) System.out.print("," + child.identifier);
		System.out.println("");
		int index = 0 ;
		for (SchedulerState s : next) {
			if (!schStateList.contains(s)) {
				if (fulltrans) {
					s.print(schStateList, pref + trans.get(index++) + "-->", fulltrans);
				} else {
					s.print(schStateList, pref + "-->", fulltrans);
				}
			}
		}
	}				
	public void print(String pref) {
		System.out.println(pref + "[" + prunid+ "," + identifier + "]");
		int index = 0 ;		
		for (SchedulerState s : next) {						
			s.print(pref + trans.get(index++) + "-->");
		}
	}
	public void print() {}
}
