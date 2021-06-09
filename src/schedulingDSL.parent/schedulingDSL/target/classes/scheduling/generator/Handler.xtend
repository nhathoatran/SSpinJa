package scheduling.generator

import scheduling.dsl.Element
import scheduling.dsl.EventDef
import scheduling.dsl.PeriodicProcess
import scheduling.dsl.ProcessDSL
import scheduling.dsl.ProcessDef
import scheduling.dsl.SchedulerDef
import scheduling.dsl.Value

import static scheduling.generator.Data.*
import java.util.ArrayList
import java.util.HashSet
import scheduling.dsl.EventStm
import scheduling.dsl.EventOpt
import scheduling.dsl.GetProcess

class Handler {
	
	static def String genMissingHandler(SchedulerDef sch, ProcessDSL procModel){	
		var result = ''		
		var hasselect_process = false 
		var hasNew_process = false
		var hasClock = false
		var haspreTake = false
		var haspostTake = false	
		if (sch.handler != null) {
			for (e: sch.handler.event) {
				if (e.eventname.toString.trim().equals('select_process'))
					hasselect_process = true
				else
					if (e.eventname.toString.trim().equals('new_process'))
						hasNew_process = true
					else
						if (e.eventname.toString.trim().equals('clock'))
							hasClock = true
						else
							if (e.eventname.toString.trim().equals('pre_take'))
								haspreTake = true
							else
								if (e.eventname.toString.trim().equals('post_take'))
									haspostTake = true
			}
		}	
		//default missing handler		
		if (!hasselect_process) result += genMissingselect_process
		if (!hasNew_process) result +=genMissingNew_process
		if (!hasClock) result +=genMissingClock(procModel)
		if (!haspreTake) result +=genMissingPreTake()		
		if (!haspostTake) result +=genMissingPostTake()
		result += genTerminate_processs(sch)		//really need
		return result		
	}
	
	
	static def genTerminate_processs(SchedulerDef sch) 
	'''
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
					«IF Data.schModel.defcore != null»
						running_procs[current_core] = null ;
					«ENDIF»
					running_process = null ;
					return processID;
				}
			}			
«««			«IF sch.schedulerdata != null»
«««				«var ArrayList<String> collectionlist = new ArrayList<String>()»
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.getProp == null»
«««						«IF ! collectionlist.contains(schdata.col.getName().name)»
«««							//add collection «schdata.col.getName().name»: «collectionlist.add(schdata.col.getName().name)»	
«««						«ENDIF»
«««					«ENDIF»
«««				«ENDFOR»
				«FOR col : Data.collectionList»
					«col».removeProcess(processID);
				«ENDFOR»				
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.col != null»						
«««						«schdata.col.getName().name».removeProcess(processID) ;
«««					«ENDIF»					
«««				«ENDFOR»
«««			«ENDIF»
			
			return processID ;
		}
	'''
	static def genMissingselect_process() 
	'''
		public void select_process(GenerateCode code) throws ValidationException{
			select_process(-1) ;
		}
		public int select_process(int lastProcessID) throws ValidationException {
			//default missing handler
			Util.println("--> Select process, default processing") ;
			return - 1;
		}
	'''	
	static def genMissingNew_process() 
	'''
		//API -> run(process) -> new_process EVENT
		//If no handle this API then the scheduler may not process new process event
		public SchedulerProcess new_process(String procName, int processID, ArrayList<String> para) throws ValidationException {					
			return null;
		}	
		public SchedulerProcess new_process(String procName) throws ValidationException {
			return null ;
		}
		public void new_process(String procName, int processID) throws ValidationException{ 
			//default missing handler 
			Util.println("New process : " + procName + ", but no processing") ;
		}
		public void new_process(SchedulerProcess target_process) {}
		public void config_new_process(SchedulerProcess target_process) {}
		public void addProcessList(ArrayList<SchedulerProcess> procList) throws ValidationException { }//called by init_order
	'''
	static def genMissingClock(ProcessDSL procModel) 
	'''
		/*
		public void clock(GenerateCode _code) throws ValidationException{
			this._code = _code ;
			clock() ;
		}
		*/
		public void clock() throws ValidationException{ 
			//default missing handler
			//hasClockEventHandler = «Data.hasClockEventHandler = false»
			//Util.println("--> Clock event : increase clock value, check running time") ;
			inc_time() ; //increase all clock including the running clock (_time_count)
			check_running_time_to_put_running_process() ; //to end the time slice
			«IF Data.schModel.defcore != null»				
				if (_runningSets[current_core] != null) {
					_runningSets[current_core].clear();
				}
			«ELSE»
				if (_runningSet != null) {
					_runningSet.clear();
				}	
			«ENDIF»
			«genClockEventForPeriodicProcess(procModel)»	
			if (!hasGenTemplate) {		
				if (running_process == null) {
					if (select_process(-1) < 0) {
						//Util.println("No running process");
					}
				}
			}
		}
	'''	
	static def genMissingPreTake() 
	'''
		public void preTake() throws ValidationException{}
	'''	
	static def genMissingPostTake() 
	'''
		public void postTake() throws ValidationException {}
	'''	
	
	static def genPreTake(EventDef e) 
	'''
		public void preTake() throws ValidationException{
			«IF e.event instanceof EventStm»				
				«FOR sta: (e.event as EventStm).statements»
					«Statements.genStm(sta,'')»
				«ENDFOR»
			«ENDIF»
		}
	'''
	static def genPostTake(EventDef e)
	'''
		public void postTake() throws ValidationException{
			«IF e.event instanceof EventStm»
				«FOR sta: (e.event as EventStm).statements»
					«Statements.genStm(sta,'')»
				«ENDFOR»
			«ENDIF»
		}
	'''
	
	static def genEventSelect(EventDef e) 
	'''			
		public int select_process(int lastProcessID) throws ValidationException {
			SchedulerProcess «e.processname.name» ;			
			«IF e.event instanceof EventStm»				
				«FOR sta: (e.event as EventStm).statements»
«««				«Statements.dispatchStatement(sta,e.processname.name)»
					«Statements.genStm(sta,e.processname.name)»
				«ENDFOR»
			«ELSE»
				«val numopt = (e.event as EventOpt).opt.size»
				//_schnumopt = «numopt» ;
				switch (_opt[_schselopt][1]) { //select
					«FOR i : 0..<numopt»
						case «i» : {
							«FOR sta: (e.event as EventOpt).opt.get(i).eventstm.statements»			
								«Statements.genStm(sta,e.processname.name)»
							«ENDFOR»
							}
							break ;
					«ENDFOR»
				}
			«ENDIF»
			
			if (running_process != null)
				return running_process.processID ;
			return -1;								
		}
		
		public void set_RunningSet() {
«««			«e.event»
			«IF e.event instanceof EventStm»				
				«FOR sta: (e.event as EventStm).statements»
«««					«sta.statement»
«««					check instaceof GetProcess
«««					«Statements.genStm(sta,e.processname.name)»
					«IF sta.statement instanceof GetProcess»
«««						instaceof GetProcess
						«Statements.dispatchStatementForSimulation(sta.statement as GetProcess)»
					«ENDIF»
				«ENDFOR»
			«ENDIF»							
		}
	'''
	
	static def genEventNew(EventDef e, SchedulerDef sch) //, int preemptionValue) 
	'''		
		«IF sch.parent == null»
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
		«ENDIF»
«««		«val templateCnt = Generation.templateCnt»		
		public void config_new_process (SchedulerProcess «e.processname.name») throws ValidationException {		
			//defined by new_process event handler	
			//initProcessList = «Data.initProcessList = false» - ?
			«IF e.event instanceof EventStm»				
				«FOR sta: (e.event as EventStm).statements»			
					«Statements.genStm(sta,e.processname.name)»
				«ENDFOR»
			«ELSE»
				«val numopt = (e.event as EventOpt).opt.size» 
				//_schnumopt = «numopt» ;
				switch (_opt[_schselopt][0]) { //new
					«FOR i : 0..<numopt»
						case «i» : {
							«FOR sta: (e.event as EventOpt).opt.get(i).eventstm.statements»			
								«Statements.genStm(sta,e.processname.name)»
							«ENDFOR»
							}
							break ;
					«ENDFOR»
				}
			«ENDIF»
«««			«FOR sta: e.statements»
«««				«Statements.dispatchStatement(sta, e.processname.name)»
«««				«Statements.genStm(sta,e.processname.name)»
«««			«ENDFOR»
			/*if (! hasGenTemplate) {	
				if (running_process == null) {
					select_process(-1) ;
				}
			} selecting is done by clock event*/	
		}				
		public void addProcessList(ArrayList<SchedulerProcess> procList) throws ValidationException { //called by init_order
«««			«var ArrayList<String> collectionlist= new ArrayList<String>()»
«««			«FOR schdata : sch.schedulerdata.data»	
«««				«IF schdata.col != null»
«««					«IF !collectionlist.contains(schdata.col.getName().name)»						
«««						ArrayList<SchedulerProcess> AL_«schdata.col.getName().name» = new ArrayList<SchedulerProcess>() ; //«collectionlist.add(schdata.col.getName().name)»
«««					«ENDIF»
«««				«ENDIF»
«««			«ENDFOR»
			«var HashSet<String> collectionlist = Statements.getProcessCollectionFromEvent(e)»
«««			//«Generation.templateCnt = templateCnt»
			«IF collectionlist.size() > 0»
				«FOR col : collectionlist»
					ArrayList<SchedulerProcess> AL_«col» = new ArrayList<SchedulerProcess>() ;
				«ENDFOR»
				for (SchedulerProcess «e.processname.name» : procList) {	
					//initProcessList = «Data.initProcessList = true» this value for managing initial order of process ~ put process in collection			
					«IF e.event instanceof EventStm»						
						«FOR sta: (e.event as EventStm).statements»			
							«Statements.genStm(sta,e.processname.name)»							
						«ENDFOR»
					«ELSE»
						«val numopt = (e.event as EventOpt).opt.size»
						//_schnumopt = «numopt» ;
						switch (_opt[_schselopt][0]) { //new
							«FOR i : 0..<numopt»
								case «i» : {
									«FOR sta: (e.event as EventOpt).opt.get(i).eventstm.statements»			
										«Statements.genStm(sta,e.processname.name)»
									«ENDFOR»
									}
									break ;
							«ENDFOR»
						}
					«ENDIF»
					
«««					«FOR sta: e.statements»
	«««					«Statements.dispatchStatement(sta, e.processname.name)»					
«««						«Statements.genStm(sta,e.processname.name)»
«««						«IF (sta.statement instanceof SetGenTemplate)»
«««							«Statements.templateCnt --»
«««						«ENDIF»
«««					«ENDFOR»
					//initProcessList = «Data.initProcessList = false»
					initprocesslist.add(getInstance(«e.processname.name»)) ;
				}
				
				«FOR col : collectionlist»
					if (!AL_«col».isEmpty() )		
						«col».put(AL_«col») ;
				«ENDFOR»
			«ENDIF»			
«««			«FOR schdata : sch.schedulerdata.data»											
«««				«IF schdata.col != null»
«««					if (!AL_«schdata.col.getName().name».isEmpty() )		
«««						«schdata.col.getName().name».put(AL_«schdata.col.getName().name») ;							
«««				«ENDIF»
«««			«ENDFOR»		
		}		
	'''
	static def ArrayList<String> getProcessCollectionFromHandler(EventDef e) {
		var ArrayList<String> result = new ArrayList<String>() 
		
		return result
	}
	static def genEventClock(EventDef e,ProcessDSL procModel)
	'''	
		public void clock() throws ValidationException{
			«IF e.event instanceof EventStm»										
				«IF ! (e.event as EventStm).statements.empty»
					//hasClockEventHandler = «Data.hasClockEventHandler = true»
				«ELSE»
					//hasClockEventHandler = «Data.hasClockEventHandler = false»
				«ENDIF»
				inc_time() ; //increase all clock including the running clock (_time_count)
				«FOR sta: (e.event as EventStm).statements»			
					«Statements.genStm(sta,'')»
				«ENDFOR»
			«ELSE»
				«val numopt = (e.event as EventOpt).opt.size» 
				//_schnumopt = «numopt» ;
				switch (_opt[_schselopt][2]) { //clock
					«FOR i : 0..<numopt»
						case «i» : {
							«IF ! (e.event as EventOpt).opt.get(i).eventstm.statements.empty»
								//hasClockEventHandler = «Data.hasClockEventHandler = true»
							«ELSE»
								//hasClockEventHandler = «Data.hasClockEventHandler = false»
							«ENDIF»
							inc_time() ; //increase all clock including the running clock (_time_count)
							«FOR sta: (e.event as EventOpt).opt.get(i).eventstm.statements»											
								«Statements.genStm(sta,"")»
							«ENDFOR»
							break ;
						}						
					«ENDFOR»
				}
			«ENDIF»
«««			«IF ! e.statements.empty»
«««				//hasClockEventHandler = «Data.hasClockEventHandler = true»
«««			«ELSE»
«««				//hasClockEventHandler = «Data.hasClockEventHandler = false»
«««			«ENDIF»
«««			inc_time() ; //increase all clock including the running clock (_time_count)
«««			«FOR sta: e.statements»
««««««				«Statements.dispatchStatement(sta,'')»
«««				«Statements.genStm(sta,'')»
«««			«ENDFOR»
			check_running_time_to_put_running_process() ; //end the time slice
			«IF Data.schModel.defcore != null»				
				if (_runningSets[current_core] != null) {
					_runningSets[current_core].clear();
				}
			«ELSE»
				if (_runningSet != null) {
					_runningSet.clear();
				}	
			«ENDIF»
			«genClockEventForPeriodicProcess(procModel)»		
			if (! hasGenTemplate) {				
				if (running_process == null) {
					if (select_process(-1) < 0) {
						//Util.println("No running process");
					}
				}
			}
		}		
	'''

	static def genClockEventForPeriodicProcess(ProcessDSL procModel) 
	'''		
		«IF (procModel.processconfig != null)»			
			«FOR proc : procModel.processconfig.procinit»
				«IF (proc.periodic != null)»
					«getNewProcessFunction(proc.periodic, procModel)»						
				«ENDIF»
			«ENDFOR»
			//reset periodic clock
			«FOR pClock : Data.periodicClockProperties.keySet()»
				if («pClock» == «Data.periodicClockProperties.get(pClock)»)
					«pClock» = -1 ;				
			«ENDFOR»
		«ENDIF»		
	'''
	
	static def String getNewProcessFunction(PeriodicProcess periodic, ProcessDSL procModel) 
	'''			
		//periodic process	
		if («Data.getPeriodicClockVariable(periodic)» == 0) { //period «Data.periodicClockProperties.get(Data.getPeriodicClockVariable(periodic))»){			 	
			//get the model for executing new process
			SchedulerPanModel panmodel = (SchedulerPanModel) SchedulerPromelaModel.panmodel ;
			//execute new periodic process			
			PromelaProcess proc = panmodel.new «periodic.element.process.name»_0() ;
			int id = SchedulerPanModel.panmodel.newProcess(proc, «periodic.max», "«getPeriodicProcessKey(periodic.element, procModel)»");
			
			if (id >= 0 ) {
				ArrayList<String> para = new ArrayList<String>() ;
				«IF periodic.element.paraAssign != null »
					«FOR par : periodic.element.paraAssign»
						«IF par.num != null»
							para.add("«par.num.value»") ;
						«ELSE»
							para.add("«par.bool.value»") ;
						«ENDIF»
					«ENDFOR»
				«ENDIF»
				executeProcess(proc, id, para) ;
			} else {
				//throw new ValidationException("Can not execute new process «getPeriodicProcessKey(periodic.element,procModel)»") ;
				Util.println("Can not execute new process «getPeriodicProcessKey(periodic.element,procModel)» - Number of processes reaches limit") ;
				Config.processLimit = true ;
			}			
		}		
	'''
	
	static def String getPeriodicProcessKey(Element element, ProcessDSL procModel) {
		var ProcessDef proc = null		
		for (p : procModel.getProcess)
			if (p.proctype.name.equals(element.process.name)) 
				proc = p			
			
		var result = element.process.name
				
		if (element.paraAssign != null) {
			var i = 0 ;
			var Value value = null;
			for (i=0; i<element.paraAssign.size; i++){
				value = element.paraAssign.get(i) 
				if (value.num != null) {					
					result += '_' + value.num.value										
				} else
					result += '_' + value.bool.value					
			}			
		}							
		result += '_0'
		return result 
	}
	
}