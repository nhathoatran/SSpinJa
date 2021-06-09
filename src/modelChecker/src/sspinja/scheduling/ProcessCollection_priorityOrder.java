package sspinja.scheduling;
		
	import java.util.ArrayList;
	import java.util.Collections;
	import java.util.Comparator;
	import java.util.Iterator;

	//Automatic generation					
	public class ProcessCollection_priorityOrder extends SortedProcessCollectionBase {	
		//true						
		private int compare_priorityOrder(Object _pn, Object _po) {
			SchedulerProcess p_n = (SchedulerProcess) _pn ;
			SchedulerProcess p_o = (SchedulerProcess) _po ;
			
			//generate variables
			int x ;
				
			//generate code	
			x=((p_n.get_priority())-(p_o.get_priority()));
			if (((x)>0))
				return 1 ; //greater
			else 
				if (((x)==0))
					return 0 ; //equal
				else 
					return -1 ; //less
		}
		
		public int compare(Object p_n, Object p_o) { 
			int result = 0;
			result = compare_priorityOrder(p_n, p_o) ;
			if (result != 0) return result ;
			
			if (result == 0) {//int_temp
			}
			return result ;
		}	
		
		public int compare_without_fifo_or_lifo(Object p_n, Object p_o) { 
			int result = 0;
			result = compare_priorityOrder(p_n, p_o) ;
			if (result != 0) return result ;
			return result ;
		}	
						 			
		@SuppressWarnings("unchecked")
		@Override
		public void put(ArrayList<SchedulerProcess> aL) {
			put2DataSet((ArrayList<SchedulerProcess>) aL.clone());
		}
		
		public void put2DataSet(ArrayList<SchedulerProcess> aL){
			ArrayList<ArrayList<SchedulerProcess>> setOfset = new ArrayList<ArrayList<SchedulerProcess>>() ;
			
	        for (SchedulerProcess p : aL) {
				if (setOfset.isEmpty()) {
		            ArrayList<SchedulerProcess> set = new ArrayList<SchedulerProcess>() ;
		            set.add(p) ;
		            setOfset.add(set) ;
		        }
		        else {
	                Iterator<ArrayList<SchedulerProcess>> iterator = setOfset.iterator();	
	                int index = 0 ;
	                boolean added = false ;							                
	                while(iterator.hasNext() && !added){
	                    ArrayList<SchedulerProcess> obj = iterator.next();	
	                    if (obj.size() > 0) { //toArray()[0] != null){
	                        SchedulerProcess proc = (SchedulerProcess) obj.toArray()[0];
	                        int com = compare_without_fifo_or_lifo(p, proc) ;
	                        if (com < 0) {//continue
	                            index ++ ;
	                        } else {
		                        if (com == 0) {		                        	
		                            //add to set
		                            obj.add(p);
		                            //order by ID
		                            Collections.sort(obj, new Comparator<SchedulerProcess>() {
		                            	@Override
		                            	public int compare(SchedulerProcess pn, SchedulerProcess po) {
		                            		return pn.processID - po.processID;
		                            	}
		                            });			                            		    	
		                            setOfset.set(index, obj) ;	
		                            added = true ;
		                        } else {//insert here
		                        	ArrayList<SchedulerProcess> set = new ArrayList<SchedulerProcess> ();
		        					set.add(p) ;
		        					setOfset.add(index, set);
		        					added = true ;
		                        }
	                        }
	                    } else {//size == 0
	                    	ArrayList<SchedulerProcess> set = new ArrayList<SchedulerProcess> ();
	    					set.add(p) ;
	                    	setOfset.set(index,set) ;
	                    	added = true ;
	                    }
	                }
	                if (!added) {
		                ArrayList<SchedulerProcess> set = new ArrayList<SchedulerProcess> ();
						set.add(p) ;
		                setOfset.add(set);
	                }
		        }
	        }        
	        this.dataSet.addAll(setOfset);
	    }
	}
