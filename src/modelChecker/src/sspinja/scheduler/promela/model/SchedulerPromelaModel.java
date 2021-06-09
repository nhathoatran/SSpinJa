// Copyright 2010, University of Twente, Formal Methods and Tools group
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package sspinja.scheduler.promela.model;

import spinja.exceptions.AssertionException;
import spinja.exceptions.SpinJaException;
import spinja.exceptions.ValidationException;
import spinja.promela.exceptions.TooManyProcessesException;
import spinja.promela.model.Channel;
import spinja.promela.model.PromelaModel;
import spinja.promela.model.PromelaProcess;
import spinja.promela.model.PromelaTransition;
import spinja.util.ByteArrayStorage;
import spinja.util.DataReader;
import spinja.util.DataWriter;
import sspinja.Config;
import sspinja.Run;
import sspinja.scheduling.SchedulerObject;
import sspinja.scheduling.SchedulerProcess;
import sspinja.scheduler.search.SchedulerStack;

public abstract class SchedulerPromelaModel extends PromelaModel {	
	public static boolean processInit = true ; //for first adding process in model
	public static SchedulerObject scheduler = new SchedulerObject();
	public static SchedulerPromelaModel panmodel ;
	public static String api_execute_result ; //return result for execute scheduler api
//	public static int _pcount = 0;
	
	public static void getProcessCheckList() {
		SchedulerObject.processInScheduler = scheduler.getProcessCheckList() ;
	}
	
	protected void sch_api(String funcName, String paraList) throws ValidationException  {
		scheduler.sch_api(funcName, paraList);
	}
	
	protected boolean sch_get(String processName, String property) {
		return scheduler.sch_get(processName, property) ;
	}
	
	
	public boolean[] init_atomicf() { return null; }
	public boolean[] init_sf() { return null; }
	public boolean stateCheck() {return false; }
	public boolean collectState() {return false;}
	public boolean modelCheck() {return false;}
	public String sysGet(String va) {return null ; }

	public int getRunID() {
		return scheduler.getRunningID() ;
	}
	protected boolean terminate() {
		if (scheduler.running_process == null)
			return false ;	
		endProcess(scheduler.running_process.processID) ;
		return true ;
	}
	
	public static SchedulerPromelaModel createPromelaModel(final PromelaModel model) throws ValidationException {
		panmodel = (SchedulerPromelaModel) model ;
		SchedulerObject.panmodel = panmodel ;
		return	panmodel;
	}	
	
	protected SchedulerPromelaModel(String name, int globalSize) throws SpinJaException {
		super(name, globalSize) ;		
	}

	public boolean getClaimTurn(){
		return false;
	}
	
	public boolean getSporadicTurn(){
		return false ;
	}
	
	protected void updateProcessListInScheduler(int _nrProcs) {		
		if (Run.testDepth != -1) //no test
			return ;
		int _count = 0;
		for (int i = 0 ; i < 255; i++) {
			if (_count == _nrProcs) break ;
			if (_procs[i] == null) {
				SchedulerObject.processInScheduler[i] = false ;				
			} else {
				SchedulerObject.processInScheduler[i] = true ;
				_count ++ ;
			}
		}	
	}
	
	/**
	 * Creates a new process within this model.
	 * 
	 * @param proctype
	 *            The new process that should be added to this model.
	 * @return The number of the process that was created.
	 * @throws TooManyProcessesException
	 *             When the maximum number of processes is already reached.
	 */
	public int addProcess(final PromelaProcess proctype) throws TooManyProcessesException {
		/*
		 * Initial processes, call from SchedulerPanModel constructor
		 */
		if (_nrProcs >= 255) {
			throw new TooManyProcessesException("too many processes");
		}
		
		if (Run.testDepth != -1) { //for test case
			_procs[_nrProcs++] = proctype;
			proctype.set_pid(_nrProcs - 1);
			_process_size += proctype.getSize();
			return _nrProcs - 1;
		}		
		SchedulerObject.processList.add(proctype.getName()) ;
//		SchedulerObject.pcnt[_nrProcs] = (byte)++_pcount ;
//		SchedulerObject.pcnt[_nrProcs]++ ;
		//SchedulerObject.pcnt[_nrProcs] = (byte) (_nrProcs +1) ;
//		SchedulerObject.pcnt[_nrProcs] = (byte) (++SchedulerObject._pcount) ;
		
		_procs[_nrProcs++] = proctype ;	
		proctype.set_pid(_nrProcs);		
		return _nrProcs;
	}
	
	public int addNewProcess(final PromelaProcess proctype) throws TooManyProcessesException {
		int index = 0 ;
		for (String pName : SchedulerObject.processList) {
			if (pName.equals(proctype.getName())) {
				if (!SchedulerObject.processInScheduler[index]) //found suitable index
					break ;
			}				
			index ++ ;					
		}		
		if (index >= SchedulerObject.processList.size()) {
			SchedulerObject.processList.add(proctype.getName()) ;
		}
	
		if (index >= _nrProcs) {  //new process				
			if (index > 128) { // 255) {
				throw new TooManyProcessesException("too many processes");
			}
		}						
		
		try {
			proctype.set_pid(index);			
			scheduler.new_process(proctype.getName(), index, null) ;
			SchedulerObject.processInScheduler[index] = true ;
//			SchedulerObject.pcnt[index] = (byte) (SchedulerObject._pcount + 1) ;
			//SchedulerObject.pcnt[index] = (byte) (_nrProcs + 1) ;
			//SchedulerObject.pcnt[index]++ ;
//			SchedulerObject.pcnt[index] = (byte)++_pcount ;
		} catch (ValidationException e) {		
			e.printStackTrace();
		}
		_nrProcs ++ ;	
		_process_size += proctype.getSize();
		return index ;
	}

	public int addProcess(final PromelaProcess proctype, final SchedulerProcess target) throws ValidationException {			
		if (_nrProcs >= 255) { //unsign byte -> int
			throw new TooManyProcessesException("too many processes");
		}
		
		int index = 0 ;
		String procName = proctype.getName() ;	
			
		for (String pName : SchedulerObject.processList) {
			if (pName.equals(procName)) {
				if (!SchedulerObject.processInScheduler[index])
					break ;
			}				
			index ++ ;					
		}		
		if (index >= SchedulerObject.processList.size()) {
			SchedulerObject.processList.add(procName) ;
		}
	
		if (index >= _nrProcs) {  //new process				
			if (index > 255) { //SchedulerObject.processList full, must be replaced
				throw new TooManyProcessesException("too many processes");
			} else {
				_procs[index] = proctype ;
			}
		}
		
		target.processID = (byte) index ;
		proctype.set_pid(index + 1);
		SchedulerObject.processInScheduler[index] = true ;
		scheduler.config_new_process(target) ;
		
		_nrProcs ++ ;
		_process_size += proctype.getSize();		
		return index ;
	}
		
	public Channel[] endProcess() { //wrong !
		if (Run.testDepth != -1) //test cases
		{
			_process_size -= _procs[--_nrProcs].getSize();
			int extraChannels = _procs[_nrProcs].getChannelCount();
//			_procs[_nrProcs] = null;
			
			if (extraChannels > 0) {
				Channel[] backup = new Channel[extraChannels];
				while (--extraChannels >= 0) {
					backup[extraChannels] = _channels[--_nrChannels];
					_channels[_nrChannels] = null; // delete channel!
				}
				return backup;
			} else {
				return null;
			}
		}
		
		if (scheduler.running_process == null)
			return null ;		
		int processID = scheduler.running_process.processID ;
		_nrProcs--;
		
		return endProcess(processID) ;
	}

	public Channel[] endProcess(int processID) {
		_process_size -= _procs[processID].getSize();
		int extraChannels = _nrChannels - _procs[processID].getChannelCount();		
//		_procs[processID] = null;
//		_nrProcs-- ;

		if (Run.testDepth == -1)
			SchedulerObject.processInScheduler[processID] = false ;
		
		try {		
			scheduler.terminate_process(processID); 
		} catch (ValidationException e) {		
			e.printStackTrace();
		}
		
		//check the channel
		if (extraChannels > 0) {
			Channel[] backup = new Channel[extraChannels];
			while (--extraChannels >= 0) {
				backup[extraChannels] = _channels[--_nrChannels];
				_channels[_nrChannels] = null; // delete channel!
			}
			return backup;
		} else {
			return null;
		}
	}
	
	public PromelaProcess getNever() throws ValidationException {
		return null;
	}
	
	public PromelaProcess getSporadic() throws ValidationException {
		return null ;
	}
	
	@Override
	public int getSize() {		
		//super.updateProcessSize(); //because of undoing
		int size = super.getSize() ;
		size += scheduler.getSize() ;
		
		size += 1 ; //number of processes
		size += super._nrProcs ; //position of process ?
		
		int _i = 0 ;
		int _pcnt = 0 ;
		while (_pcnt < _nrProcs && _i < 255) {				
			if (_procs[_i] != null && SchedulerObject.processInScheduler[_i]) {
				size += _procs[_i].getSize() ;
				_pcnt++;
			}
			_i++;
		}
		return size;
	}
	
	@Override
	public PromelaTransition nextTransition(PromelaTransition last) {
		if (Run.testDepth == -1) { //no test
			//change base on scheduler
			if (scheduler.running_process == null)
				return null ;
			
			int processID = scheduler.running_process.processID ;	
			assert (_procs[processID] != null) ;
			return _procs[processID].nextTransition((PromelaTransition) last) ;
		} else {
			PromelaTransition next = null;	
			if (_nrProcs > 0) {
				if (_exclusive == _NO_PROCESS) {
					int i = last== null ? _nrProcs - 1 : last.getProcess().get_pid();
					PromelaProcess proc = last == null ? _procs[i] : last.getProcess();
					
					next = proc.nextTransition(last);
					while (next == null && i > 0) {
						proc = _procs[--i];
						next = proc.nextTransition(null);
					}
				} else {
					next = _procs[_exclusive].nextTransition(last);
				}
			}
	
			return next;
		}
	}

	@Override
	public SchedulerPromelaModel clone() {
		try {
			return getClass().getConstructor(boolean.class).newInstance(_ignore_assert);
		} catch (Exception e) {
			System.err.println("error: kan geen correcte constructor vinden van dit model, er moet een constructor zijn met alleen een boolean als parameter");
			return null;
		}
	}
	
	private final ByteArrayStorage _store = new ByteArrayStorage();

	public byte[] encode() {
		_store.init(super.getSize() + scheduler.getSize());
		encode(_store);
		return _store.getBuffer();
	}

	public boolean decode(byte[] backup) {
		_store.setBuffer(backup);
		return decode(_store);
	}

	@Override
	public boolean decode(DataReader reader) {
		return false;
	}

	@Override
	public void encode(DataWriter _writer) {
	}
	
	
	
	public void undoEndProcess(final PromelaProcess proctype){
		undo_Scheduler();
		_procs[_nrProcs++] = proctype;
		_process_size += proctype.getSize();			
	}
	

	private int getProcessCount(String processName, String periodicKey) {
		//count the number of process
		int count = 0 ;		
		if (periodicKey.equals("")) {
			int _n = 0 ;
			int index = 0 ;
			while (_n < _nrProcs) {
				if (SchedulerObject.processInScheduler[index] ) {
					if (_procs[index] != null) {
						if (_procs[index].getName().trim().equals(processName.trim())) {
							count ++ ;
						}
					}
					_n++ ;
				}
				index++ ;
			}
		} else {
			int _n = 0 ;
			int index = 0 ;
			while (_n < _nrProcs) {
				if (SchedulerObject.processInScheduler[index] ) {
					if (_procs[index] != null) {
						if (SchedulerObject.processList.get(index).trim().equals(processName.trim())) {
							count ++ ;
						}
					}
					_n++ ;
				}
				index++ ;
			}
			
//			for (int i = 0 ; i < _nrProcs ; i++) {	
//				if (_procs[i] != null && SchedulerObject.processInScheduler[i] ) {
//					if (scheduler.processList.get(i).equals(periodicKey)) {
//						count ++ ;
//					}
//				}
//			}
		}
		return count ;
	}
	
	public int newProcess(PromelaProcess proctype) {
		return newProcess(proctype, 0, "") ;
	}
	
	public int newProcess(PromelaProcess proctype, int max, String periodicKey) {
		/*
		 * Scheduler -> create new Promela process (already registered with scheduler)
		 * Put process into promela model
		 * Return process ID
		 */
		int index = -1;
		String procName = proctype.getName() ;		
				
		if (max != 0) {
			if (getProcessCount(procName, periodicKey) >= max) //reach the max number of process
				return -1;
		}
		if (periodicKey.equals(""))
			index = SchedulerObject.getProcessID(procName) ;
		else
			index = SchedulerObject.getProcessID(periodicKey) ;
		
		if (index == -1) {
			return -1 ;
		}
		
		proctype.set_pid(index) ;		
		_procs[index] = proctype;		
		_process_size += proctype.getSize() ;
		_nrProcs ++ ;
		
		//SchedulerObject.pcnt[index] = (byte)++_pcount ;
//		_pcount = SchedulerObject.pcnt[index] = (byte) (SchedulerStack.getPmax()+1);
//		System.out.println("\n - new process -> id:" + index + ", instance: " + _pcount + "\n");
//		System.out.println(_nrProcs);
		return index ;
	}
	
	
	public SchedulerProcess API_newProcess(String processName) throws ValidationException {
		if (Config.isSchedulerSearch) {
			if (! Config.processInit)
				return scheduler.new_process(processName);
		}
		return null ;
	}
	
	public void undo_Scheduler() {
		//do nothing
	}
	
	public void restoreModel() {
		System.out.println("Restore model by searching algorithm & model");
	}

	public int new_process(String string, SchedulerProcess target_process) throws TooManyProcessesException, AssertionException, ValidationException {
		// TODO Auto-generated method stub
		return 0;
	}	
}
