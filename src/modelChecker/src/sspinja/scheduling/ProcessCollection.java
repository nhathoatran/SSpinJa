package sspinja.scheduling ;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;		
public class ProcessCollection extends ProcessCollectionBase implements ProcessSet{	
	public void put(SchedulerProcess p){
		ArrayList<SchedulerProcess> pSet ;
		if (dataSet.size() > 0) {
			pSet = dataSet.get(0);
			pSet.add(p);
			//order by process id
			Collections.sort(pSet, new Comparator<SchedulerProcess>() {
				@Override
				public int compare(SchedulerProcess pn, SchedulerProcess po) {
					return pn.processID - po.processID;
				}
			});
			dataSet.set(0, pSet);
		}
		else {
			pSet = new ArrayList<SchedulerProcess>() ;
			pSet.add(p);
			dataSet.add(0, pSet);
		}
	}
    
    public void put(ArrayList<SchedulerProcess> aL) { 
    	//order by process id   
    	Collections.sort(aL, new Comparator<SchedulerProcess>() {
			@Override
			public int compare(SchedulerProcess pn, SchedulerProcess po) {				
				return pn.processID - po.processID;
			}
			
		});	
    	dataSet.add(aL) ;
    }
}
