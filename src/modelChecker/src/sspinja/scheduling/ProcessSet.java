package sspinja.scheduling ;

import java.util.ArrayList;
import spinja.util.*;

//Automatic generation
public interface ProcessSet {	
	public void put(SchedulerProcess _proc) ;
	public SchedulerProcess get() ;
	public ArrayList<SchedulerProcess> getProcessSet();
	public int hasProcess(String pName) ;
	public int hasProcess(int processID) ;
	public int hasProcess(SchedulerProcess p) ;
	public int isEmpty() ;    
	
	public void add_time(int time) ;
	public void sub_time(int time) ;
	
	public int getSize() ;			
	public void encode(DataWriter _writer) ;
	public boolean decode(DataReader _reader);	
	
	//public void replace(SchedulerProcess running_process, SchedulerProcess _previous_run) ;
}
