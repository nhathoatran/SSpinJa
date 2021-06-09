package sspinja.scheduling ;
		
import java.util.Iterator;
import java.util.ArrayList;	
import spinja.util.*;

//Automatic generation
public class RunningSet implements ProcessSet {
	//using for running set! 
	public ArrayList<SchedulerProcess> dataSet = new ArrayList<SchedulerProcess>();
	
	public void clear() {
		dataSet.clear();
	}
	public void add_time(int time) {
		for (SchedulerProcess p : dataSet) 
			p.add_time(time) ;
	}
	
	public void sub_time(int time) {
		for (SchedulerProcess p : dataSet) 
			p.sub_time(time) ;
	}
				
	public void encode(DataWriter _writer) {		
		int pCount = dataSet.size() ;
		_writer.writeByte(pCount);
		
		for(int _i = 0; _i < pCount; _i++) {
			dataSet.get(_i).encode(_writer);
		}		
	}

	public boolean decode(DataReader _reader) {		
		int pCount = _reader.readByte();
		dataSet.clear();
		for(int _i = 0; _i < pCount; _i++) {
			SchedulerProcess proc = new SchedulerProcess();
			proc.decode(_reader);
			dataSet.add(proc) ;
		}
		
		return true;
	}
	
	public void replace(SchedulerProcess running_process, SchedulerProcess _previous_run) {
	    Iterator<SchedulerProcess> iterator = dataSet.iterator();
	
	    int index = 0 ;         
	    while(iterator.hasNext()){
	        SchedulerProcess proc = iterator.next();
	        if (proc.processID == running_process.processID) { 
	           	dataSet.set(index,_previous_run) ;
	           	return ;
	        }         	            
	        index ++ ;
	    }
	}
	    
	public ArrayList<SchedulerProcess> getProcessSet() {
		return dataSet ;
	}
	
	public int getSize() {
		int pCount = dataSet.size() ;
		if (pCount == 0)
			return 1 ;
		else
			return 1 + pCount * dataSet.get(0).getSize() ;
	}
				
	public SchedulerProcess getFirstProcess(int lastProcessID) {
		if (dataSet.isEmpty())
			return null ;
		else {			
			if (lastProcessID >= 0) {
				for (int i = 0 ; i < dataSet.size() ; i++) {
					if (dataSet.get(i).processID == lastProcessID) {
						if (i < dataSet.size() - 1)
							return dataSet.get(i + 1);
						else
							return null ; 
					}
				}
				return null ;
			} else {
				return dataSet.get(0) ;
			}
		}	
	}
	
	public int size(){
		return dataSet.size() ;
	}
	
	public int isEmpty() {
		return dataSet.size() == 0 ? 1 : 0 ;
	}
				
	public void put(SchedulerProcess p){
        dataSet.add(p) ;
    }
    
    public void put(ArrayList<SchedulerProcess> aL) {
    	for (SchedulerProcess p : aL)
    		put(p) ;
    }
    
    public void print(){
    	if (dataSet == null) 
    		Util.println("Empty!") ;
    	else
	    	if (dataSet.isEmpty())
	    		Util.println("Empty!") ;
			else
				Util.println(dataSet.toString()) ;
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		if (dataSet == null) 
    		sb.append("Empty!") ;
    	else
	    	if (dataSet.isEmpty())
	    		sb.append("Empty!") ;
			else
				sb.append(dataSet.toString()) ;
		return (sb.toString());
	}
	
	public int hasProcess(SchedulerProcess proc){
        return hasProcess(proc.processID);
    }
			    
    public int hasProcess(int processID){
        boolean result = false ;
        Iterator<SchedulerProcess> proc_iterator = dataSet.iterator();
        while(proc_iterator.hasNext()) {
        	result = (proc_iterator.next().processID == processID) ;
        	if (result) return 1 ; 
        }
        return 0 ;
    }
    
    public int hasProcess(String pName){
    	boolean result = false ;
        Iterator<SchedulerProcess> proc_iterator = dataSet.iterator();
        while(proc_iterator.hasNext()) {
        	result = SchedulerObject.getStaticPropertyObject(proc_iterator.next().refID).pName.equals(pName) ;
        	if (result) return 1 ; 
        }
        return 0 ;
    }
    
    public SchedulerProcess getProcess(){
    	if (dataSet.isEmpty()) return null ;
    	else {
    		//Util.println(dataSet) ;	
    		SchedulerProcess result = (SchedulerProcess) dataSet.toArray()[0] ;
    		dataSet.remove(result) ;
    		return result ;
    	}
    }
    			
	public SchedulerProcess getProcess(int processID) {				
        Iterator<SchedulerProcess> proc_iterator = dataSet.iterator();
        while(proc_iterator.hasNext()) {
        	SchedulerProcess p = proc_iterator.next() ;
        	if (p.processID == processID) 
        		return p;
        }
        	
        return null ;
	}
				 
	public SchedulerProcess removeProcess(SchedulerProcess proc) {
		return removeProcess(proc.processID) ;
	}
				
    public SchedulerProcess removeProcess(int processID){
    	if (processID < 0 ) return null;
        Iterator<SchedulerProcess> iterator = dataSet.iterator();		        
        while(iterator.hasNext()){
        	SchedulerProcess proc = iterator.next() ;
        	if (proc.processID == processID) {		        		
        		dataSet.remove(proc) ;
        		return proc ;
        	}
        }
        return null ;
    }
    
	public SchedulerProcess get() {
		// TODO Auto-generated method stub
		return getProcess();
	}
	
	public boolean equals(ProcessCollection obj){
		//if (obj == null) return false ;
		if (this.dataSet.size() == obj.dataSet.size()) {
			for (SchedulerProcess p : this.dataSet)
				if (! obj.dataSet.contains(p))
					return false ;
			return true ;
		}
		else return false ;
	}
}
