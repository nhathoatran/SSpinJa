package sspinja.scheduling ;
import spinja.util.*;
import spinja.exceptions.ValidationException;
import java.util.ArrayList;

//Automatic generation
public class SchedulerProcess_priority  extends SchedulerProcessBase {	
	public int processID ;
	public int refID ;
	
	public int get_processID() {
		return processID ;
	}				
	public void print(){
		Util.println(this.toString()) ;
	}
	
	public byte priority  ;
	public byte get_priority() {
		return this.priority ;
	}
					
	public void inc_time() {}
	public void dec_time() {}
	public void add_time(int time) {}
	public void sub_time(int time) {}
	public int getSize() {
		int size = 0;				
		size += 1 ; //priority	
		return 4 + 4 + size ; //processID, refID, process properties				
	}
	
	public SchedulerProcess_priority(int processID) {
		//this.processID = (byte) processID ;
		this.processID = processID ;
	}			
	public SchedulerProcess_priority() {}
	
	public String getName() {
		return SchedulerObject.getStaticPropertyObject(this.refID).pName + "_" + this.processID; 
	}
					
	public String toString() {
		String res = "    + Process ID=" + this.processID ; 
		res += ", Name: " + SchedulerObject.getStaticPropertyObject(this.refID).pName + ", Ref ID=" + this.refID ;
		res +=  ", priority = " + this.priority ;
		return res ;
	}
	
	public void encode(DataWriter _writer) {
		//_writer.writeByte(processID);
		//_writer.writeByte(refID);
		_writer.writeInt(processID);
		_writer.writeInt(refID);
		_writer.writeByte(priority) ;	
	}		
	public boolean decode(DataReader _reader) {		
		//processID = (byte) _reader.readByte();
		//refID = (byte) _reader.readByte();
		processID = _reader.readInt();
		SchedulerObject.processInScheduler[processID] = true ;
		refID = _reader.readInt();
		priority = (byte) _reader.readByte() ; 
		return true;
	}
	
	//default value
	public void t1() {
		this.priority = 2 ;
	}					
	//init process with string parameter
	public void t1(String paraList) throws ValidationException {
		String[] parameters = paraList.split(",");							
		ArrayList<String> para = new ArrayList<String>() ;
		for (int i = 0; i < parameters.length; i++)
			if (! parameters[i].trim().isEmpty())
				para.add(parameters[i]) ;
		if (para.size() == 0) {
			t1() ;							
		} else {
			//check number of parameters
			if (para.size() == 0) 								
				t1() ;
			else //raise exception
				throw new ValidationException("Error init the process for scheduling: t1(" + paraList + ")") ;
		}
	}
	//default value
	public void t2() {
		this.priority = 2 ;
	}					
	//init process with string parameter
	public void t2(String paraList) throws ValidationException {
		String[] parameters = paraList.split(",");							
		ArrayList<String> para = new ArrayList<String>() ;
		for (int i = 0; i < parameters.length; i++)
			if (! parameters[i].trim().isEmpty())
				para.add(parameters[i]) ;
		if (para.size() == 0) {
			t2() ;							
		} else {
			//check number of parameters
			if (para.size() == 0) 								
				t2() ;
			else //raise exception
				throw new ValidationException("Error init the process for scheduling: t2(" + paraList + ")") ;
		}
	}
	//default value
	public void t3() {
		this.priority = 2 ;
	}					
	//init process with string parameter
	public void t3(String paraList) throws ValidationException {
		String[] parameters = paraList.split(",");							
		ArrayList<String> para = new ArrayList<String>() ;
		for (int i = 0; i < parameters.length; i++)
			if (! parameters[i].trim().isEmpty())
				para.add(parameters[i]) ;
		if (para.size() == 0) {
			t3() ;							
		} else {
			//check number of parameters
			if (para.size() == 0) 								
				t3() ;
			else //raise exception
				throw new ValidationException("Error init the process for scheduling: t3(" + paraList + ")") ;
		}
	}
									
	public void initProcess(String pName, ArrayList<String> para){
		//call only with execute function				
		switch (pName) {
			case "t1" : 
				getRefID(pName) ;
				if (para == null) {
					t1() ;								
				} else {
					if (para.isEmpty()) {
						t1() ;
					} else {	
						//check number of parameters
						if (para.size() == 0) 								
							t1() ;
						else
							System.out.println("Error init the process for scheduling") ;
					}
				}
				break ;
			case "t2" : 
				getRefID(pName) ;
				if (para == null) {
					t2() ;								
				} else {
					if (para.isEmpty()) {
						t2() ;
					} else {	
						//check number of parameters
						if (para.size() == 0) 								
							t2() ;
						else
							System.out.println("Error init the process for scheduling") ;
					}
				}
				break ;
			case "t3" : 
				getRefID(pName) ;
				if (para == null) {
					t3() ;								
				} else {
					if (para.isEmpty()) {
						t3() ;
					} else {	
						//check number of parameters
						if (para.size() == 0) 								
							t3() ;
						else
							System.out.println("Error init the process for scheduling") ;
					}
				}
				break ;
			default : break ;
		}
	}
	public void getRefID(String pName){
		if (processList.size() == 0) initProcessList() ;
		this.refID = processList.indexOf(pName);
	}			
	protected void initProcessList() {
		if (!processList.contains("t1"))
			processList.add("t1") ;
		if (!processList.contains("t2"))
			processList.add("t2") ;
		if (!processList.contains("t3"))
			processList.add("t3") ;
	}		
}		
