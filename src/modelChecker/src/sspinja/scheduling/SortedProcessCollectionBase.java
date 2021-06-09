package sspinja.scheduling ;
		
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

//Automatic generation
abstract class SortedProcessCollectionBase extends ProcessCollectionBase {			
	abstract int compare(Object pn, Object po) ;
	
	public void put(ArrayList<SchedulerProcess> aL) {
    	for (SchedulerProcess p : aL)
    		put(p) ;
    }
			    
	public void put(SchedulerProcess pn){
        if (dataSet.isEmpty()) {
            ArrayList<SchedulerProcess> set = new ArrayList<SchedulerProcess>() ;
            set.add(pn) ;
            dataSet.add(set) ;
        }
        else {
                Iterator<ArrayList<SchedulerProcess>> iterator = dataSet.iterator();

                int index = 0 ;
                boolean con = true ;

				ArrayList<SchedulerProcess> set = new ArrayList<SchedulerProcess> ();
				set.add(pn) ;
						                
                while(iterator.hasNext() && con){
                    ArrayList<SchedulerProcess> obj = iterator.next();

                    if (obj.size() > 0) { //toArray()[0] != null){
                        SchedulerProcess po = (SchedulerProcess) obj.toArray()[0];
                        int com = compare(pn, po) ; //collection get from [0]
                        if (com < 0) {
                            //continue 
                            index ++ ;
                        } else {
	                        if (com == 0) {		                        	
	                            //add to set
	                            obj.add(pn);
	                            //order by ID
	                            Collections.sort(obj, new Comparator<SchedulerProcess>() {
	                            	@Override
	                            	public int compare(SchedulerProcess pn, SchedulerProcess po) {
	                            		return pn.processID - po.processID;
	                            	}
	                            });			                            		    	
	                            dataSet.set(index, obj) ;
	                            return ;
	                        }    
	                        else {
	                            //insert here
	                            //HashSet<SchedulerProcess> set = new HashSet<SchedulerProcess> ();
	                            //set.add(p) ;
	                            dataSet.add(index, set);
	                            return ;
	                        }
	                    }
                    }
                    else { //size == 0
                    	dataSet.set(index,set) ;
                    	return ;
                    }
                }
                
                dataSet.add(set);
        }
    }
}
