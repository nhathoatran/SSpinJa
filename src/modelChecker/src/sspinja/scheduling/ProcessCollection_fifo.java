package sspinja.scheduling;
		
import java.util.ArrayList;
//Automatic generation
public class ProcessCollection_fifo extends SortedProcessCollectionBase {
	@SuppressWarnings("unchecked")
	public void put(ArrayList<SchedulerProcess> aL) {
		this.dataSet.add((ArrayList<SchedulerProcess>) aL.clone());
	}
	
	public int compare(Object pn, Object po) {
		return -1 ;	//fifo				
	}
}
