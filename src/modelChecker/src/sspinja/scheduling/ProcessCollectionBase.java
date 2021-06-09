package sspinja.scheduling ;
	
import java.util.Iterator;
import java.util.ArrayList;
import spinja.util.*;

//Automatic generation
public abstract class ProcessCollectionBase implements ProcessSet {	
	public ArrayList<ArrayList<SchedulerProcess>> dataSet = new ArrayList<ArrayList<SchedulerProcess>>();
	
	public void init_order() {
		ArrayList<SchedulerProcess> pSet = new ArrayList<SchedulerProcess>() ;
		
		Iterator<ArrayList<SchedulerProcess>> iterator = dataSet.iterator();
		while(iterator.hasNext()){
			ArrayList<SchedulerProcess> obj = iterator.next();
			pSet.addAll(obj) ;
		}
		dataSet.clear() ;
		dataSet.add(pSet) ;
	}
				
	public void encode(DataWriter _writer) {		
		int setCount = dataSet.size() ;		
		_writer.writeByte(setCount);
		
		for(int s = 0; s < setCount; s++) {
			int pCount = dataSet.get(s).size() ;
			_writer.writeByte(pCount);
			for (int p = 0 ; p < pCount; p++)
				dataSet.get(s).get(p).encode(_writer);
		}		
	}

	public boolean decode(DataReader _reader) {		
		int setCount = _reader.readByte();
		
		dataSet.clear(); 
		for(int s = 0; s < setCount; s++) {
			int pCount = _reader.readByte();
			ArrayList<SchedulerProcess> set = new ArrayList<SchedulerProcess>() ;
						
			for (int p = 0 ; p < pCount; p++) {
				SchedulerProcess proc = new SchedulerProcess() ;
				proc.decode(_reader);
				set.add(proc);			
			}
			dataSet.add(set);
		}			
		return true;
	}
	
	public int getSize() {
		int size = 1 ; //setCount
		int setCount = dataSet.size() ;
		
		for (int s = 0; s<setCount; s++) {
			size += 1 ; //pCount
			int pCount = dataSet.get(s).size() ;
			if (pCount > 0)
				size += pCount * dataSet.get(s).get(0).getSize() ;
		}
		return size ;
	}
												
	public int size(){
		int result = 0;
		Iterator<ArrayList<SchedulerProcess>> iterator = dataSet.iterator();
		while(iterator.hasNext()){
			ArrayList<SchedulerProcess> obj = iterator.next();
			result += obj.size() ;
		}
		return result ;
	}
	
	public int isEmpty() {
		return dataSet.size() == 0 ? 1 : 0;
	}
	
	public void add(SchedulerProcess p){
		put(p) ;
	}

    public void print(){
    	if (dataSet.isEmpty())
    		Util.println("Empty !") ;
		else {
			Iterator<ArrayList<SchedulerProcess>> iterator = dataSet.iterator();
			while(iterator.hasNext()){
				ArrayList<SchedulerProcess> obj = iterator.next();
				Util.println(obj.toString()) ;
			}
			Util.println("") ;
		}
	}
	
	public String toString(){
		StringBuilder sb = new StringBuilder();
		
		if (dataSet.isEmpty())
    		sb.append("Empty!\n") ;
		else {
			Iterator<ArrayList<SchedulerProcess>> iterator = dataSet.iterator();
			while(iterator.hasNext()){
				ArrayList<SchedulerProcess> obj = iterator.next();
				sb.append(obj.toString()) ;
			}
			sb.append("\n") ;
		}
		return sb.toString();
	}
	
	public void add_time(int time) {
		Iterator<ArrayList<SchedulerProcess>> iterator = dataSet.iterator();
				    	    
    	int index = 0 ;         
    	while(iterator.hasNext()){
    		ArrayList<SchedulerProcess> obj = iterator.next();
    	    
    		if (obj.size() > 0) {		    			
    	       	for(SchedulerProcess proc : obj) {            		            	
    	           	proc.add_time(time) ;
    	        }
    	        dataSet.set(index, obj);
    	    }
    	    index ++ ;
    	}
	}
	
	public void sub_time(int time) {
		Iterator<ArrayList<SchedulerProcess>> iterator = dataSet.iterator();
				    	    
    	int index = 0 ;         
    	while(iterator.hasNext()){
    		ArrayList<SchedulerProcess> obj = iterator.next();
    	    
    		if (obj.size() > 0) {		    			
    	       	for(SchedulerProcess proc : obj) {            		            	
    	           	proc.sub_time(time) ;
    	        }
    	        dataSet.set(index, obj);
    	    }
    	    index ++ ;
    	}
	}
	
	public int hasProcess(SchedulerProcess proc){
        return hasProcess(proc.processID);
    }
    
    public int hasProcess(int processID){		        
        Iterator<ArrayList<SchedulerProcess>> iterator = dataSet.iterator();                    
        while(iterator.hasNext()){
            ArrayList<SchedulerProcess> obj = iterator.next();
            Iterator<SchedulerProcess> proc_iterator = obj.iterator();
            while(proc_iterator.hasNext()) {
                if (proc_iterator.next().processID == processID)
                	return 1 ;		                
            }
        } 
        return 0 ;
    }
    
    public int hasProcess(String pName){		        
        Iterator<ArrayList<SchedulerProcess>> iterator = dataSet.iterator();                    
        while(iterator.hasNext()){
            ArrayList<SchedulerProcess> obj = iterator.next();
            Iterator<SchedulerProcess> proc_iterator = obj.iterator();
            while(proc_iterator.hasNext()) {
                 if (SchedulerObject.getStaticPropertyObject(proc_iterator.next().refID).pName.equals(pName))
                	return 1 ;		                
            }
        } 
        return 0 ;
    }
    		    
    public void replace(SchedulerProcess running_process, SchedulerProcess _previous_run) {
    	Iterator<ArrayList<SchedulerProcess>> iterator = dataSet.iterator();
    	    
    	int index = 0 ;         
    	while(iterator.hasNext()){
    		ArrayList<SchedulerProcess> obj = iterator.next();
    	    
    		if (obj.size() > 0) {
    			int idx = 0 ;
    	       	for(SchedulerProcess proc : obj) {            		            	
    	           	if (proc.processID == running_process.processID) {
    	           		obj.remove(idx) ;
    	           		obj.add(idx, _previous_run);                		
    	           		dataSet.set(index,obj) ;
    	           		return ;
    	           	}            	
    	           	idx ++ ;
    	        }
    	    }
    	    index ++ ;
    	}
    }
    
    @SuppressWarnings("unchecked")
    public ArrayList<SchedulerProcess> getProcessSet(){
    	if (dataSet.isEmpty()) return null ;
        return (ArrayList<SchedulerProcess>) dataSet.get(0).clone() ; //not .remove() ;
    }
    		    		    			
	public SchedulerProcess getProcess(){
		return (SchedulerProcess) dataSet.get(0).toArray()[0] ;
	}
	
	public SchedulerProcess getNextProcess() {
		return null ; //need to edit
	}

	public SchedulerProcess getProcess(int processID) {
        Iterator<ArrayList<SchedulerProcess>> iterator = dataSet.iterator();                    
        while(iterator.hasNext()){
            ArrayList<SchedulerProcess> obj = iterator.next();
            Iterator<SchedulerProcess> proc_iterator = obj.iterator();
            while(proc_iterator.hasNext()) {
            	SchedulerProcess p = proc_iterator.next();
            	if (p.processID == processID)
            		return p ;
            }
        } 
        return null ;
	}

	public SchedulerProcess removeProcess(SchedulerProcess proc) {
		return removeProcess(proc.processID) ;
	}			
	
    public SchedulerProcess removeProcess(int processID){
    	if (processID < 0 ) return null ;
       
        //remove process which has processID		        				
        Iterator<ArrayList<SchedulerProcess>> iterator = dataSet.iterator();  
        int index = 0 ; //for the process set list
        while(iterator.hasNext()){				
        	ArrayList<SchedulerProcess> obj = iterator.next();
        	Iterator<SchedulerProcess> proc_iterator = obj.iterator();            
            while(proc_iterator.hasNext()) {
                SchedulerProcess proc = proc_iterator.next() ;
                if (proc.processID == processID) {
                    if (obj.size() == 1)
                    	dataSet.remove(obj) ;
                    else {
                    	obj.remove(proc) ;
                    	dataSet.set(index, obj) ;
                    }
                    return proc ;
                }                
            }
            index ++ ;
        }            
        return null ;
    }
    
	public SchedulerProcess get() {		
		return getProcess();
	}
	
	public ArrayList<SchedulerProcess> findProcessByrefID(int refID) {
		ArrayList<SchedulerProcess> result = new ArrayList<SchedulerProcess>() ;
		//public ArrayList<ArrayList<SchedulerProcess>> dataSet = new ArrayList<ArrayList<SchedulerProcess>>();
		for (ArrayList<SchedulerProcess> pList : dataSet) {
			for (SchedulerProcess p : pList) {
				//if (p.refID == (byte) refID) result.add(p) ;
				if (p.refID == refID) result.add(p) ;
			}
		}
		if (result.isEmpty())
			return null ;
		else
			return result ;
	}
	
	public abstract void put(ArrayList<SchedulerProcess> aL) ;
}
