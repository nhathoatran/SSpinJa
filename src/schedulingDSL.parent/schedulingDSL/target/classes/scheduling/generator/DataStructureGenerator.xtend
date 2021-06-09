package scheduling.generator

import java.util.HashSet
import org.eclipse.xtext.generator.IFileSystemAccess2
import scheduling.dsl.CompVarDef
import scheduling.dsl.OrderingDef
import scheduling.dsl.SchedulerCollectionDef
import scheduling.dsl.SchedulerDSL
import scheduling.dsl.SchedulerDef
import java.util.ArrayList

class DataStructureGenerator {

	static SchedulerDef scheduler
	static OrderingDef order
	
	static def String getcollectionName(SchedulerCollectionDef coldef) {
		var String result = ''
		if (coldef != null)
			if (coldef.comp != null)
				for (comp : coldef.comp)
					result += '_' + comp.name
		return result 
	}
	static def genDataStructure(IFileSystemAccess2 fsa, SchedulerDSL schModel, ArrayList<String> genFiles) {
		scheduler = schModel.scheduler
		order = schModel.order
		
		//Utilities.deleteFiles(fsa) //remove data files before generating				
		fsa.generateFile("../src/sspinja/scheduling/ProcessSet.java", ProcessSettoJavaCode())
		fsa.generateFile("../src/sspinja/scheduling/ProcessCollectionBase.java", ProcessCollectionBasetoJavaCode())
		fsa.generateFile("../src/sspinja/scheduling/ProcessCollection.java", ProcessCollectiontoJavaCode())
		fsa.generateFile("../src/sspinja/scheduling/RunningSet.java", RunningSettoJavaCode())
		fsa.generateFile("../src/sspinja/scheduling/SortedProcessCollectionBase.java", SortedProcessCollectionBasetoJavaCode(schModel.order))
		
		genFiles.add("ProcessSet.java")
		genFiles.add("ProcessCollectionBase.java")
		genFiles.add("ProcessCollection.java")
		genFiles.add("RunningSet.java")
		genFiles.add("SortedProcessCollectionBase.java")
		
		if (scheduler.schedulerdata != null) { 					
			if (scheduler.schedulerdata.datadef != null) {				
				var HashSet<String> collectionType = new HashSet<String>() 
				for (schdata : scheduler.schedulerdata.datadef) {
					if (schdata.datablockdef != null) {
						for (data : schdata.datablockdef.datadef) {
							if (data.col != null) {						
								var String coltype = getcollectionName(data.col)
								if (data.col.operationtype.toString.contains("lifo") || data.col.operationtype.toString.contains("fifo") ) {
									coltype += '_' + data.col.operationtype
								}
								if (! coltype.trim.empty) {
									if (!collectionType.contains(coltype)) {
										collectionType.add(coltype)
										genFiles.add("ProcessCollection" + coltype + ".java")
										if (schModel.order != null)
											fsa.generateFile("../src/sspinja/scheduling/ProcessCollection" + coltype + ".java", genCollectionClass(coltype, data.col, data.col.operationtype.toString, schModel.order.vars))	
										else 
											fsa.generateFile("../src/sspinja/scheduling/ProcessCollection" + coltype + ".java", genCollectionClass(coltype, data.col, data.col.operationtype.toString, null))
									}
								}
							}
						}						 
					} else {
						if (schdata.datasingledef != null) {
							if (schdata.datasingledef.col != null) {						
								var String coltype = getcollectionName(schdata.datasingledef.col)
								if (schdata.datasingledef.col.operationtype.toString.contains("lifo") || schdata.datasingledef.col.operationtype.toString.contains("fifo") ) {
									coltype += '_' + schdata.datasingledef.col.operationtype
								} 
								if (!coltype.trim.empty) {
									if (!collectionType.contains(coltype)) {
										collectionType.add(coltype)
										genFiles.add("ProcessCollection" + coltype + ".java")
										if (schModel.order != null) 
											fsa.generateFile("../src/sspinja/scheduling/ProcessCollection" + coltype + ".java", genCollectionClass(coltype, schdata.datasingledef.col, schdata.datasingledef.col.operationtype.toString, schModel.order.vars))
										else 
											fsa.generateFile("../src/sspinja/scheduling/ProcessCollection" + coltype + ".java", genCollectionClass(coltype, schdata.datasingledef.col, schdata.datasingledef.col.operationtype.toString, null))
									}
								}
							}							
						}
					}	
				}
			}					
		}
	}

	static def ProcessSettoJavaCode()
	'''
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
	'''

	static def String ProcessCollectionBasetoJavaCode() 
	'''	
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
	'''	
	
	static def RunningSettoJavaCode()
	'''
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
	'''
		
	static def String SortedProcessCollectionBasetoJavaCode(OrderingDef ordering)
	'''
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
	'''
	
	static def String ProcessCollectiontoJavaCode () 
	'''		
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
	'''
	
	static def String genVars(CompVarDef vars)
	'''
		«IF vars != null»
			«FOR p : vars.vard»
				«IF p.type.toString.trim().operator_equals('bool')»boolean «ELSE»int «ENDIF»«IF p.pvalue != null»«IF p.pvalue.num != null»«Utilities.setInitValue(p.getName.map[name].join(', '), p.pvalue.num.value.toString)»«ENDIF»«IF p.pvalue.bool != null»«Utilities.setInitValue(p.getName.map[name].join(', '), p.pvalue.bool.value.toString)»«ENDIF»«ELSE»«p.getName.map[name].join(', ')» ;«ENDIF»
			«ENDFOR»
		«ENDIF»
	'''
	
	static def String genCollectionClass(String coltype, SchedulerCollectionDef col, String operationtype, CompVarDef vars) 
	'''	
		package sspinja.scheduling;
				
		«IF col.comp.size() > 0»
			import java.util.ArrayList;
			import java.util.Collections;
			import java.util.Comparator;
			import java.util.Iterator;
		
			//Automatic generation					
			public class ProcessCollection«coltype» extends SortedProcessCollectionBase {	
				«var HashSet<String> compareFunction = new HashSet<String>»
				«IF order != null»
					«IF order.compare != null»
						«FOR com : order.compare»
							«IF !compareFunction.contains(com.cname.name)»
								//«compareFunction.add(com.cname.name)»						
								private int compare_«com.cname.name»(Object _pn, Object _po) {
									SchedulerProcess «com.process1.name» = (SchedulerProcess) _pn ;
									SchedulerProcess «com.process2.name» = (SchedulerProcess) _po ;
									
									//generate variables
									«genVars(order.vars)»
										
									//generate code	
									«FOR sta: com.statements»
										«Statements.dispatchStatement(sta, '')»
									«ENDFOR»
								}
							«ENDIF»
						«ENDFOR»
					«ENDIF»
				«ENDIF»
				
				public int compare(Object p_n, Object p_o) { 
					int result = 0;
					«IF col.comp != null»
						«FOR colcomp : col.comp»
							result = compare_«colcomp.name»(p_n, p_o) ;
							if (result != 0) return result ;
						«ENDFOR»
					«ENDIF»
					
					«IF operationtype != null»
						«IF !operationtype.trim.empty»
							if (result == 0) {//«operationtype»
								«IF (operationtype.trim.equals("fifo"))»
									return -1 ; //fifo
								«ENDIF»
								«IF (operationtype.trim.equals("lifo"))»
									return 1 ; //lifo
								«ENDIF»						
							}
						«ENDIF»
					«ENDIF»
					return result ;
				}	
				
				public int compare_without_fifo_or_lifo(Object p_n, Object p_o) { 
					int result = 0;
					«IF col.comp != null»
						«FOR colcomp : col.comp»
							result = compare_«colcomp.name»(p_n, p_o) ;
							if (result != 0) return result ;
						«ENDFOR»
					«ENDIF»				
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
		«ELSE»
			import java.util.ArrayList;
			«IF operationtype.contains('lifo')»
				//Automatic generation
				public class ProcessCollection_«operationtype» extends SortedProcessCollectionBase {
					@SuppressWarnings("unchecked")
					public void put(ArrayList<SchedulerProcess> aL) {
						this.dataSet.add((ArrayList<SchedulerProcess>) aL.clone());
					}
					
					public int compare(Object pn, Object po) {
						return 1 ; //lifo						
					}
				}
			«ELSE»
				«IF operationtype.contains('fifo')»
					//Automatic generation
					public class ProcessCollection_«operationtype» extends SortedProcessCollectionBase {
						@SuppressWarnings("unchecked")
						public void put(ArrayList<SchedulerProcess> aL) {
							this.dataSet.add((ArrayList<SchedulerProcess>) aL.clone());
						}
						
						public int compare(Object pn, Object po) {
							return -1 ;	//fifo				
						}
					}
				«ENDIF»
			«ENDIF»
		«ENDIF»
	'''
}														