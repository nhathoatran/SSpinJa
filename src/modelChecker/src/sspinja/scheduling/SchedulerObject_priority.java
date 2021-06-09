package sspinja.scheduling ;

import java.io.PrintWriter;		
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import spinja.util.DataReader;
import spinja.util.DataWriter;
import spinja.util.StringUtil;
import spinja.util.Util;		
import spinja.util.Log;
import spinja.exceptions.*;
import spinja.promela.model.PromelaProcess;		
import sspinja.scheduler.search.SchedulerSearchAlgorithm;
import spinja.util.ByteArrayStorage;
		
import sspinja.scheduler.promela.model.SchedulerPromelaModel;
import sspinja.SchedulerPanModel;
import sspinja.SchedulerState;

//Automatic generation
public class SchedulerObject_priority {
		public static ArrayList<StaticProperty> staticPropertyList = new ArrayList<StaticProperty>() ;
		public static ArrayList<String> processList = new ArrayList<String>() ;		
		public static boolean [] processInScheduler = new boolean [128]; //processes managed by scheduler
		public static byte pcount = 0 ;
		public static ArrayList<Byte> pcnt = new ArrayList<Byte>(); //instance of process id
		public String _action = "";				
		public static ArrayList<String> initprocesslist = new ArrayList<String>();
		public static SchedulerPromelaModel panmodel ;
		public int _schselopt ;
		public int _schnumopt ;
		public int [][] _opt;
		
		private static int newP = -1, endP = -1;
		public static int getnewP(){
			int result = newP;
			newP = -1;
			return result;
		}
		public static int getendP(){
			int result = endP;
			endP = -1;
			return result;
		}
		
		public int switchCore(int lastcore) throws ValidationException {return -1;}
		public int selCore(int lastcore) throws ValidationException {return -1;}
	
		public static void setPcnt(ArrayList<Byte> pcount) {
			pcnt.clear();
			pcnt.addAll(pcount) ;
		}
		public static ArrayList<Byte> getPcnt() {
			return pcnt ;
		}
		public void setSchedulerSelOption(int sel){
			_schselopt = sel;
		}
		
		public int nextSchedulerOption(int lastschopt) {
			if (lastschopt == -1) {
				lastschopt = 0 ;
			}	
			if (lastschopt < _schnumopt - 1) {							
				_schselopt = lastschopt + 1 ;
			} else {
				_schselopt = 0 ;
				return -1; //no more scheduler option
			}
			return _schselopt;
		}				
		public int firstSchedulerOption() {
			_schselopt = 0 ;
			return 0;
		}				
		
	public boolean hasGenTemplate = false ;
		
		public void setAction(String act) {
			_action = act ;
		}
		public String getAction() {
			return _action ;
		}
		public int getRefID(String pName) {
			int i = 0 ;			
			for (StaticProperty sP : staticPropertyList) {
				if (sP.pName.equals(pName))
					return i ;
				i ++ ;
			}
			return -1 ;
		}				
		public static StaticProperty getStaticPropertyObject(int refID) {
			for (StaticProperty sP : staticPropertyList)
				if (sP.refID == refID)
					return sP ;
			return null ;
		}				
		public ArrayList<SchedulerProcess> findProcessByAlias(String alias) {			
			ArrayList<SchedulerProcess> result = new ArrayList<SchedulerProcess>();
				
			if (alias.trim().equals("running_process")) {
				if (running_process != null) {
					result.add(running_process);
				}
			} else {				
				int idx = 0 ;		
				for (String procN : processList) {
					if (procN.trim().equals(alias.trim())) {
						SchedulerProcess target_process = findProcessByID(idx) ;
						if (target_process != null) 
							result.add(target_process) ;
					}
					idx ++ ;
				}
				for (StaticProperty stP : staticPropertyList) {
					if (stP.pName.trim().equals(alias.trim())) {
						int refID = stP.refID ;
						ArrayList<SchedulerProcess> resultStP = new ArrayList<SchedulerProcess>();
						resultStP = findProcessByrefID(refID) ;
						if (!resultStP.isEmpty()) {
							for (SchedulerProcess p1 : resultStP) {
								boolean add = true ;
								for (SchedulerProcess p2 : result) {
									if (p1.processID == p2.processID) 
										add = false;
								}
								if (add) {
									result.add(p1) ;
								}
							}
						}
					}
				}		
			}
			return result ;
		}				
		public ArrayList<SchedulerProcess> findProcess(String pName) {
			return findProcessByAlias(pName) ;
		}				
		public int existsProcess (String pName) {
			ArrayList<SchedulerProcess> aP = findProcess(pName) ;
			return aP.size() ;
		}
		public int existsProcess (int pID) {
			SchedulerProcess p = findProcessByID(pID) ;
			if (p == null) return 0 ;
			else return 1 ;
		}				
		public void updateProcessInSchedulerList() {
			for (int i = 0; i < 128; i ++) {
				if (processInScheduler[i]) {
					processInScheduler[i] = (findProcessByID(i) != null);
				}
			}
				 
		}		
	
	//--------------- constructor-------------------------------
	public SchedulerObject_priority() {
		//default constructor			
		_opt = new int[2][3];					
		int index = 0 ;
		for (int i = 1; i <= 1 ; i++)
			for (int j = 1; j <= 1 ; j++)
				for (int k = 1; k <= 1 ; k++) {
					index ++ ;							
					_opt[index][0] = i ; //new
					_opt[index][1] = j ; //select
					_opt[index][2] = k ; //clock							
				}
		_schnumopt = 1;
	}		
	public boolean InitSchedulerObject(String args) {
		_runningSet = new RunningSet() ;			
		running_process = null ;
		genStaticProcessProperty() ;
		//initial the variables
		//initial the scheduler variables
		//initial the collections
		//ensure the collections are not null
		if (ready == null) {
			ready = new ProcessCollection_priorityOrder() ;
		}
		return true ;
	}	
	public int get_init_process_count() {
		int pcnt = 0 ;
		pcnt += 3 ;	
		return pcnt;
	}				
	public void init_order() throws ValidationException {
		//initial the order of processes (using order defined in process DSL)
		ArrayList<SchedulerProcess> procList = new ArrayList<SchedulerProcess>() ;
		{
			int processID = getProcessID("t1") ;
			if (processID >= 0) { 
				//create new process in model
				//SchedulerPanModel.p
				//create new process information in scheduler
				SchedulerProcess t1 = new SchedulerProcess() ;
				//t1.processID = (byte) processID ;
				t1.processID = processID ;
				
				while (pcnt.size() < processID + 1) pcnt.add((byte) 0) ;				
				pcnt.set(processID, (byte) (pcnt.get(processID) + 1));
								
				t1.refID = getRefID("t1") ;										
				t1.t1() ;										
				//processList.set(processID, "t1_0") ;
				processList.set(processID, "t1") ;
				
				procList.add(t1) ;
			}// else ignore this initial process
		}
		{
			int processID = getProcessID("t2") ;
			if (processID >= 0) { 
				//create new process in model
				//SchedulerPanModel.p
				//create new process information in scheduler
				SchedulerProcess t2 = new SchedulerProcess() ;
				//t2.processID = (byte) processID ;
				t2.processID = processID ;
				
				while (pcnt.size() < processID + 1) pcnt.add((byte) 0) ;				
				pcnt.set(processID, (byte) (pcnt.get(processID) + 1));
								
				t2.refID = getRefID("t2") ;										
				t2.t2() ;										
				//processList.set(processID, "t2_0") ;
				processList.set(processID, "t2") ;
				
				procList.add(t2) ;
			}// else ignore this initial process
		}
		{
			int processID = getProcessID("t3") ;
			if (processID >= 0) { 
				//create new process in model
				//SchedulerPanModel.p
				//create new process information in scheduler
				SchedulerProcess t3 = new SchedulerProcess() ;
				//t3.processID = (byte) processID ;
				t3.processID = processID ;
				
				while (pcnt.size() < processID + 1) pcnt.add((byte) 0) ;				
				pcnt.set(processID, (byte) (pcnt.get(processID) + 1));
								
				t3.refID = getRefID("t3") ;										
				t3.t3() ;										
				//processList.set(processID, "t3_0") ;
				processList.set(processID, "t3") ;
				
				procList.add(t3) ;
			}// else ignore this initial process
		}
		if (!procList.isEmpty()) {
			addProcessList(procList) ;
			procList.clear() ;
		}							
		//---------------------------------------
	}
			
	public void init() { 
	}

	//------------------------ event handler -------------------------
	public int select_process(int lastProcessID) throws ValidationException {
		SchedulerProcess target_process ;			
		{//GetProcess statement		
			SchedulerProcess previous_running = running_process;
			//1. Select process set
			if (lastProcessID < 0) {
				ArrayList<SchedulerProcess> runSet = ready.getProcessSet();
				if (runSet != null) {
					_runningSet.dataSet = runSet ; //only get no remove
					_putColIndex = (byte) getCollectionIndex("ready") ; //for the replacement
				} else {
					if (_runningSet != null)
						_runningSet.clear() ;
					return - 1;
				}
			} 
			//2 Get first process which has different processID in _runningSet to run
			int processID = select_process_to_run(lastProcessID) ;
			if (processID < 0) {
				//Util.println("Error getting process to run - no process is selected");
				return -1 ;
			}			
			//SchedulerProcess target_process = running_process ;	
			target_process = running_process ;		
			if (lastProcessID >= 0) {		
				replace_running_process(_putColIndex,running_process, previous_running) ;
			}			
			//remove it from collection
			ready.removeProcess(target_process.processID) ;
			//3 change properties	
			//4 set running parameters
		}//GetProcess statement							
		
		if (running_process != null)
			return running_process.processID ;
		return -1;								
	}
	
	public void set_RunningSet() {
		{//GetProcess statement		
			//1. Select process set
			ArrayList<SchedulerProcess> runSet = ready.getProcessSet();
			if (runSet != null) {
				_runningSet.dataSet = runSet ; //only get no remove
			}
		}//GetProcess statement							
	}
	public SchedulerProcess new_process(String procName) throws ValidationException {
		int index = 0 ;
		for (String pName : SchedulerObject.processList) {
			if (pName.equals(procName)) {
				if (!SchedulerObject.processInScheduler[index]) //  && !processInModel[index]) //found suitable index
					break ;
			}				
			index ++ ;					
		}		
		if (index >= SchedulerObject.processList.size()) {
			SchedulerObject.processList.add(procName) ;
		}
					
		if (index > 255) { //SchedulerObject.processList full, must be replaced
			for (int i = 0 ; i < 255 ; i ++) {
				if ( SchedulerObject.processList.get(i).equals("")) { //slot is free
					index = i ;
					SchedulerObject.processList.set(index, procName) ;
					break ;
				}
			}
		}					
		
		try {
			new_process(procName, index, null) ;
			SchedulerObject.processInScheduler[index] = true ;	
			newP = (byte) index ;			
		} catch (ValidationException e) {		
			e.printStackTrace();
		}
					
		return new_process(procName, -1, null) ;
	}			
	public SchedulerProcess new_process(String procName, int processID, ArrayList<String> para) throws ValidationException {					
		//Util.println("--> new_process(" + procName + ")") ;
		SchedulerProcess new_process_target_process = new SchedulerProcess();			
		if (processID >= 0) {
			//target_process.processID = (byte) processID ;
			new_process_target_process.processID = processID ;
			while (pcnt.size() < processID + 1) pcnt.add((byte) 0) ;				
				pcnt.set(processID, (byte) (pcnt.get(processID) + 1));
		} else {
			return null ;
		}						
		new_process_target_process.initProcess(procName, para) ;			
		config_new_process(new_process_target_process) ;
		return new_process_target_process ;
	}
	public void config_new_process (SchedulerProcess target_process) throws ValidationException {		
		//defined by new_process event handler	
		//initProcessList = false - ?
		{//MoveProcess target_process			
			if (target_process != null) {
				remove_process(target_process.processID) ;
				ready.put(target_process) ;
					
				if (running_process != null) {
					if (running_process.processID == target_process.processID) {
						running_process = null;
					}
				}	
			}
		}
		/*if (! hasGenTemplate) {	
			if (running_process == null) {
				select_process(-1) ;
			}
		} selecting is done by clock event*/	
	}				
	public void addProcessList(ArrayList<SchedulerProcess> procList) throws ValidationException { //called by init_order
		ArrayList<SchedulerProcess> AL_ready = new ArrayList<SchedulerProcess>() ;
		for (SchedulerProcess target_process : procList) {	
			//initProcessList = true this value for managing initial order of process ~ put process in collection			
			{//MoveProcess target_process			
				//when initializing: put new process to temporary list (AL_?) for stack or queue, then put temporary list into stack or queue
				AL_ready.add(target_process) ;
			}
			
				//initProcessList = false
			initprocesslist.add(getInstance(target_process)) ;
		}
		
		if (!AL_ready.isEmpty() )		
			ready.put(AL_ready) ;
	}		
	/*
	public void clock(GenerateCode _code) throws ValidationException{
		this._code = _code ;
		clock() ;
	}
	*/
	public void clock() throws ValidationException{ 
		//default missing handler
		//hasClockEventHandler = false
		//Util.println("--> Clock event : increase clock value, check running time") ;
		inc_time() ; //increase all clock including the running clock (_time_count)
		check_running_time_to_put_running_process() ; //to end the time slice
		if (_runningSet != null) {
			_runningSet.clear();
		}	
		if (!hasGenTemplate) {		
			if (running_process == null) {
				if (select_process(-1) < 0) {
					//Util.println("No running process");
				}
			}
		}
	}
	public void preTake() throws ValidationException{}
	public void postTake() throws ValidationException {}
	public int terminate_process(String procName) throws ValidationException {
		//default missing handler			
		SchedulerProcess target_process = null ;
		Util.println("--> Terminate process: " + procName + " default processing") ;
		int id = 0 ;
		for (String procN : processList) {
			if (procN.contains(procName)) {
				SchedulerProcess terminate_target_process = findProcessByID(id) ;
				target_process = terminate_target_process ;
				if (terminate_target_process != null) {
					return terminate_process(id) ;
				} 
			}
			id ++ ;
		}
		Util.println("--> Cannot find process: " + procName) ;
		return -1 ;
	}
	public int terminate_process(int processID) throws ValidationException {			
		//Util.println("--> Remove process : " + processID + ", default processing") ;			
		endP = (byte) processID ;
		if (running_process != null) {
			if (running_process.processID == processID) {
				SchedulerObject.processInScheduler[processID] = false ;
				running_process = null ;
				return processID;
			}
		}			
	ready.removeProcess(processID);
		
		return processID ;
	}

	//--------------- encoding function -------------------------------
	public void encode(DataWriter _writer) {					
		//_writer.writeInt(_schselopt);
		if (running_process == null)
			_writer.writeBool(false);
		else {		
			_writer.writeBool(true);
			running_process.encode(_writer);
		}
		_writer.writeByte(_putColIndex) ; //for replace running process (selection)
		
		//scheduler variables data 
		//_runningSet.encode(_writer); //why disable? encode and store in different space	(runningSet is stored in stack)			
		ready.encode(_writer) ;
	}	
	public boolean decode(DataReader _reader) {				
		//_schselopt = _reader.readInt();
		clearProcessInScheduler() ;
		if (_reader.readBool()) {
			if (running_process == null)
				running_process = new SchedulerProcess() ;
			running_process.decode(_reader) ;
			processInScheduler[running_process.processID] = true ;
		} else {
			running_process = null ;
		}
		_putColIndex = (byte) _reader.readByte() ; //for replacing running process
		//scheduler variables data  
		//_runningSet = new ProcessCollection() ;
		//_runningSet.decode(_reader); //why disable? encode and store in different space (runningSet is stored in stack)			
		ready.decode(_reader) ;
		return true;
	}
	public int getRunningSetSize(){
		return _runningSet.getSize();
	}			
	public void encodeRunningSet(DataWriter _writer){
		_runningSet.encode(_writer);
	}
	public boolean decodeRunningSet(DataReader _reader) {
		_runningSet.decode(_reader);
		return true ;
	}
	protected void clearProcessInScheduler() {
		for (int i = 0 ; i < 128; i ++)
			processInScheduler[i] = false ;
	}
	
	/* ---------------------- utility function */
	public String getInstance(SchedulerProcess process) {
		return pcnt.get(process.processID) + ""; 
	}
	public int getRunningInstance() {
		if (running_process == null)
			return -1 ;
		else
			return pcnt.get(running_process.processID);
	}
	public int getRunningID() {
		if (running_process == null)
			return -1 ;
		else
			return running_process.processID ;//pcnt[running_process.processID];
	}
	public static void printProcessInScheduler(){
		for (int i=0 ; i< 128 ; i++){
			System.out.print(processInScheduler[i] + ", ");
		}
		System.out.println();								
	}		
	public void printProcessInstance(){
		for (int i=0 ; i < pcnt.size() ; i++){
			System.out.print(pcnt.get(i) + ", ");
		}
		System.out.println();					
	}	
	public int addProcessList(String pName){
		//return the index of new process name in process list
		processList.add(pName) ;
		return processList.size() - 1 ;
	}			
	public int isNull(SchedulerProcess process) {
		if (process == null)
			return 1 ;
		else
			return 0 ;
	}			
	public static int getProcessID(String procName){ //or periodicKey
		/*
		* This function will return correct ID for new process (which has name) managed by scheduler 
		* When scheduler initial, it will call this function to initial the order of processes
		*/
		int id = 0;			
		for (String pName : processList){
			if (pName.equals(procName) && !processInScheduler[id]){	
				//processInModel[id] = true ;
				processInScheduler[id] = true ;
				return id ;	
			}
			id ++ ;				
		}
		if (processList.size() < 128) {
			processList.add(procName) ;
			id = processList.size() - 1 ;
			processInScheduler[id] = true;
			return id ;
		}
		return -1 ;
	}
	public int getCollectionIndex(String collectionName) {
		int numCol = 0 ;
		switch (collectionName) {
			case "ready" : 
				return numCol + 0 ;
			default :
				Util.println("Put back collection error") ;
				return -1 ;
		}
	}		
	public int getNumberProcessCollection() {
		int result = 0 ;
		//ready : 0
		result += 1 ;
		return result ;
	}		
	//return collection contains process -> needs to be considered
	public int getProcessCollectionID(int processID) {				
		int numCol = 0 ;
		
		if (ready.hasProcess(processID) > 0) 
			return 0 + numCol ;
		return -1 ;
	}					
	public boolean isTimer() {
		//boolean hasClockEventHandler = false ; 
		//boolean hasPeriodicProcess = false ;
		//boolean runTime = false ;
		//has clock data type = false
		return false ;
	}					
	public SchedulerProcess findProcessByID(int processID) {
		SchedulerProcess proc = null ;
		if (running_process != null) 
			if (running_process.processID == processID) 
				return running_process ;
		proc = ready.getProcess(processID);
		if (proc != null) return proc ;
		return null ;
	}		
	public ArrayList<SchedulerProcess> findProcessByrefID(int refID) {
		ArrayList<SchedulerProcess> result = new ArrayList<SchedulerProcess>();			
		if (running_process != null) 
			if (running_process.refID == refID) 
				result.add(running_process) ;
		ArrayList<SchedulerProcess> temp = new ArrayList<SchedulerProcess>();
		temp = ready.findProcessByrefID(refID);
		if (temp != null)
			result.addAll(temp);					
		return result ;
	}		
	public void remove_process(int processID) {
		ready.removeProcess(processID);					
	}		
	public int isEmpty() {
		if (ready.isEmpty() > 0)
			return 1 ;					
		return 0 ;
	}		
	public int hasProcess(String processName) {
		if (running_process != null)
			if (getStaticPropertyObject(running_process.refID).pName.trim().equals(processName.trim()))
				return 1 ;
		
		int result = 0 ;
		int processID = 0 ;
		for (String pName : processList) {
			if (pName.trim().equals(processName.trim())) { //check all process name in process list					
				result = ready.hasProcess(processID);	
				if (result > 0) return result ;				
			}	
			processID ++ ;
		}		
		return result ;
	}
	public String getSchedulerName() {
		return "priority" ;
	}
	
	public String getProcessModelName() {
		return "priority" ;
	}
	
	public String getSchedulingPolicy() {
		return "+ Scheduler: " + getSchedulerName() + " + Processes: " + getProcessModelName();
	}		
	
	public void print_all(){	
		Util.println("+ Scheduler: " + getSchedulerName());
		//Util.println("- SCH OPT: " + _schselopt + "/" + (_schnumopt - 1));
		Util.println("- Running process: " ) ; 
		if (running_process == null) 
			Util.println("Null") ;
		else {					
			running_process.print();
		}
		Util.println("- Running set: " ) ;	
		if (_runningSet == null) 
			Util.println("Null") ;
		else {					
			_runningSet.print() ;
		}	
		//System.out.println(processList);
		//printProcessInScheduler() ;	
	System.out.print("- Collection: ready : ");
	ready.print() ;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("\n\n") ;
		sb.append("+ Scheduler: " + getSchedulerName() + " + Processes: " + getProcessModelName());
		sb.append("\n") ;
		
					
		sb.append("- Running process: " ) ; 
		if (running_process == null) 
			sb.append("Null!") ;
		else {					
			sb.append(running_process.toString());
		}
		sb.append("\n") ;
		
		sb.append("- Running set: " ) ;	
		if (_runningSet == null) 
			sb.append("Null!");
		else {					
			sb.append(_runningSet.toString()) ;
			sb.append("\n") ;
		}					
		
		sb.append("- Collection ready : "); 					
		sb.append(ready.toString()) ;
		
		return sb.toString();
	}
	//--------------- running function -------------------------------
	public void executeProcess(PromelaProcess proc, int processID, ArrayList<String> para) throws ValidationException {
		SchedulerProcess p = new SchedulerProcess() ;
		//p.processID = (byte) processID ;
		p.processID = processID ;
		newP = processID ;
		
		//initialize the process
		p.initProcess(proc.getName(), para);
		//Util.println("New process " + p) ;
		processInScheduler[processID] = true ;
		while (pcnt.size() < processID + 1) pcnt.add((byte) 0) ;		
			pcnt.set(processID, (byte) (pcnt.get(processID) + 1) ) ;
		config_new_process(p) ;		
	}				
	public int select_process_to_run(int lastProcessID) { 
		if (_runningSet.isEmpty() == 1)
			return -1 ;
			
		SchedulerProcess temp = _runningSet.getFirstProcess(lastProcessID) ; //selectProcessToRun();
		if (temp != null) {
			running_process = temp ; //selectProcessToRun() == null ? _running ; //= _runningSet.getNextProcess(running_process) ;
			return running_process.processID ;
		}
		else return - 1;
	}
	public boolean replace_running_process(byte collectionIndex, SchedulerProcess running_process, SchedulerProcess previous_running) {
		byte numCol = 0 ;
		if (collectionIndex == (byte) (0 + numCol)) {
			if (ready != null)
				ready.replace(running_process, previous_running) ;								
			return true ;
		}
		return false ;			
	}	
	public void put_running_process(byte collectionIndex) {
		if (running_process != null) {
			put_process(running_process, collectionIndex) ;				
		}
	}		
	public boolean put_process(SchedulerProcess proc, byte collectionIndex) {	
		byte numCol = 0 ;
		if (collectionIndex == (byte) (0 + numCol)) {
			if (ready != null)
				ready.put(proc);
				//if (proc == running_process)
				//	running_process = null ;
			return true ;
		}
		return false ;
	}		
	//--------------- timed function -------------------------------		
	public void time_out(){	
		//do nothing
	}		
	public void inc_time() {
		//no specific time to run -> do nothing
		add_time(1) ;
	}		
	public void dec_time() {			
		//no specific time to run -> do nothing
		sub_time(1) ;
	}		
	public void add_time(int time) {
		//clock for periodic process				
	ready.add_time(time) ;				
		if (running_process != null)
			running_process.add_time(time) ;	
	}		
	public void sub_time(int time) {
		//clock for periodic process				
	ready.sub_time(time) ;				
		if (running_process != null)
			running_process.sub_time(time) ;
	}		
	public boolean check_running_time_to_put_running_process(){
		//if scheduler use time slice, just put the running process into process collection after running time	
		//need to call select_process(-1)	
		return false ; //process still running
	}		
	//---------------interface function---------------------------------------
	public boolean sch_api(String funcName, String paraList) throws ValidationException {
		switch (funcName) {
			default:
				System.out.println("Error calling Scheduler API function");
				return false ;
		}			
	}		
	public boolean sch_get(String processName, String property) {
		if (processName.trim().equals("scheduler")) {
			//scheduler data variable
			switch (property) {
				default:
					System.out.println("Error getting scheduler property");
			}
		} else {
			ArrayList<SchedulerProcess> aProcess = findProcess(processName) ;
			if (aProcess.size() == 1) {
				SchedulerProcess process = aProcess.get(0) ;
				switch (property) {
					case "priority" :						
						SchedulerPromelaModel.api_execute_result = Byte.toString(process.priority) ;
						return true ;
					default:
						System.out.println("Error getting process property");
				}
			} 
		}
		return false ;
	}

	//------------------------ genStaticProcessProperty -------------------------
	public void genStaticProcessProperty(){	
		StaticProperty sP ;						
		//0
		sP = new StaticProperty() ;
		sP.refID = 0 ;
		sP.pName = "t1" ;
		staticPropertyList.add(sP) ;
		//1
		sP = new StaticProperty() ;
		sP.refID = 1 ;
		sP.pName = "t2" ;
		staticPropertyList.add(sP) ;
		//2
		sP = new StaticProperty() ;
		sP.refID = 2 ;
		sP.pName = "t3" ;
		staticPropertyList.add(sP) ;
	}
	
	//--------------- data structure ---------------------------
	public SchedulerProcess running_process ;	
	
			
	public int size = 0 ;
	public byte _putColIndex = -1; //for replacement
	public RunningSet _runningSet; //temporary store the running set
	ProcessCollectionBase ready ;
	//--------------- genUtilitiesFunctions ---------------------------
	public boolean[] getProcessCheckList() {
		boolean result[] = new boolean[128] ;
		if (running_process != null)
			result[running_process.processID] = true ;
		for (ArrayList<SchedulerProcess> procList : ready.dataSet)
			for (SchedulerProcess proc : procList)
				result[proc.processID] = true ;
		return result ;
	}		
	public ProcessSet getProcessSet(String psName) {
		//scheduler data
		if (psName == "ready")
			return ready ;
		return null ;
	}				
	public int getSize() {	
		size = 0; //4 ; //schselopt
		//scheduler variables data
		//no time counter
		size += 1 ; //running_process != null ?
		if (running_process != null)				
			size += running_process.getSize() ;			
		//size += _runningSet.getSize() ; // why disable? encode and store in different space (runningSet is stored in stack)
		size += 1 ; //_putColIndex -> for replacement
		//no contains refines collections
		size += ready.getSize() ;
		return size ;
	}
	//--------------- verification structure ---------------------------
	public boolean schedulerCheck() {return false ;	}
	public void initSchedulerState(SchedulerState schState, int depth) {}
	public boolean stateCheck() {return false;}			
	public boolean collectState() {	return false ;}			
	public void printAnalysisResult(PrintWriter out) {
		if (out != null) out.println("No Analysis result");
		System.out.println("No Analysis result") ;
	}
}
