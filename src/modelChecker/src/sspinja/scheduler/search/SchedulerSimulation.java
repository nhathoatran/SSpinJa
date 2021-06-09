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

package sspinja.scheduler.search;

import static spinja.search.Message.DEADLOCK;
import static spinja.search.Message.DUPLICATE_STATE;
import static spinja.search.Message.EXCEED_DEPTH_ERROR;
import static spinja.search.Message.EXCEED_DEPTH_WARNING;
import static spinja.search.Message.LIVELOCK;
import static spinja.search.Message.NO_MORE_TRANSITIONS;
import static spinja.search.Message.TRANS_ERROR;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import org.graphstream.graph.Node;

import spinja.exceptions.SpinJaException;
import spinja.exceptions.ValidationException;
import spinja.model.Condition;
import spinja.model.Model;
import spinja.model.SchedulerTransition;
import spinja.model.Transition;
import spinja.options.NumberOption;
import spinja.promela.compiler.Proctype;
import spinja.promela.compiler.Specification;
import spinja.promela.compiler.automaton.State;
import spinja.promela.model.PromelaProcess;
import spinja.promela.model.PromelaTransition;
import spinja.search.SearchAlgorithm;
import spinja.search.SearchableStack;
import spinja.search.TransitionCalculator;
import spinja.store.ProbingHashTable;
import spinja.store.StateStore;
import spinja.store.hash.HashAlgorithm;
import spinja.util.ByteArrayStorage;
import spinja.util.Log;
import spinja.util.Util;
import sspinja.Config;
import sspinja.scheduling.SchedulerObject;
import sspinja.SchedulerPanModel;
import sspinja.scheduling.SchedulerProcess;
import sspinja.scheduler.promela.model.SchedulerNeverClaimPromelaModel;
import sspinja.scheduler.promela.model.SchedulerNeverClaimSporadicModel;
import sspinja.scheduler.promela.model.SchedulerPromelaModel;
import sspinja.scheduler.promela.model.SchedulerSporadicModel;
import sspinja.ui.MainUI;

/**
 * Scheduler Search is a search algorithm based on scheduler guide 
 */

public class SchedulerSimulation<M extends Model<T>, T extends Transition> extends SchedulerSearchAlgorithm<M, T> {
			
	protected final ByteArrayStorage storage = new ByteArrayStorage();
	protected final ByteArrayStorage runningSetStorage = new ByteArrayStorage();
	
	protected final SchedulerStack stack;
    protected boolean printedDepthWarning;
    protected int running_processID ;
    public byte modelType = 0 ;
    
    private final ArrayList<String> enabled = new ArrayList<String>();
    MainUI mainUI;
    
    ArrayList<Transition> lOptionTransitions = new ArrayList<Transition>();
    byte[] state;				
	int identifier ;
	boolean firstSaveState = true;
	public boolean isStop = false ;
	public boolean isSimulate = false;
	
	//for trail simulation
	public boolean isTrailSimulate = false;
	ArrayList<Integer> lStateFrom = new ArrayList<Integer>();
	ArrayList<Integer> lStateTo = new ArrayList<Integer>();
	ArrayList<Integer> lTransID = new ArrayList<Integer>();
	int trailID = 0;
	
	public boolean isMultiple = false;
	boolean isSelectProcess = true;
	public int actionIndex = 0; //~trail index
	
	byte[] runningSetState ;			
	final StateStore runningSetStore;
	int runningSetIdentifier;	

	protected HashAlgorithm hash;

	int stateCount = 0 ;
	int stateMatch = 0 ;

    private final NumberOption hashEntries = new NumberOption('w',
    		"sets the number of entries in the hash table to 2^N", 21, 3, 31);
   
    public void setMainUI(MainUI mainUI) {
    	this.mainUI = mainUI;
    }
	@SuppressWarnings("unchecked")
	public SchedulerSimulation(M model, StateStore store, int stackSize, boolean errorExceedDepth,
			boolean checkForDeadlocks, int maxErrors, TransitionCalculator<M, T> nextTransition) {
		super(model, store, stackSize, errorExceedDepth, checkForDeadlocks, maxErrors, nextTransition);
		Log.initLog(SchedulerPanModel.scheduler.getSchedulerName());


		this.stack = new SchedulerStack(stackSize);
		runningSetStore = new ProbingHashTable(hashEntries.getValue());
				
		if (model instanceof SchedulerNeverClaimSporadicModel) {
			modelType = 3 ;
		} else {
			if (model instanceof SchedulerNeverClaimPromelaModel) {
				modelType = 2 ;
			} else {
				if (model instanceof SchedulerSporadicModel) {
					modelType = 1 ;
				}
			}
		}		
		hash = HashAlgorithm.getDefaultAlgorithm();
	}
		
	
	protected void restoreRunningSet() {
		byte [] runningSetState = stack.getRunningSetTop();
		if (runningSetState != null) {
			ByteArrayStorage reader = new ByteArrayStorage();
			reader.setBuffer(runningSetState);
			SchedulerPromelaModel.scheduler.decodeRunningSet(reader) ;
		}
	}
	
	protected void restoreModel() {
		byte [] state = stack.getTop();			
		ByteArrayStorage reader = new ByteArrayStorage();
		reader.setBuffer(state);
		model.decode(reader);
	}
	
	protected byte[] storeRunningSet() {		
		runningSetStorage.init(SchedulerPromelaModel.scheduler.getRunningSetSize());
		SchedulerPromelaModel.scheduler.encodeRunningSet(runningSetStorage);
		return runningSetStorage.getBuffer();
	}
	
		
	protected void addRunningSetState(){
		runningSetState = storeRunningSet() ;
		runningSetIdentifier = runningSetStore.addState(runningSetState);
		stack.pushRunningSet(runningSetState, runningSetIdentifier) ;		
	}
	
	
	@Override
	protected boolean checkModelState() {
		final byte[] buffer = storeModel();
		return Arrays.equals(buffer, stack.getTop());
	}

	/**
	 * @see spinja.search.Algoritm#freeMemory()
	 */
	@Override
	public void freeMemory() {
		stack.clearStack();		
	}
		

	@Override
	protected boolean addState(final byte[] state, int identifier) {
		return stack.push(state, identifier);
	}
			
	protected void replaceState(final byte[] state, int identifier) {
		stack.replaceLastState(state, identifier);		
	}
	
	public void printStack(){
		Util.println("+ System stack");
		stack.print();		
	}

	protected void printBytes(byte[] state) {
		for (int i=0 ;i < state.length;i++)
			System.out.print(Byte.toString(state[i]) + ' ');
		Util.println("");
	}

	
//	@Override
	public void execute() {
		try {
			SchedulerPromelaModel.scheduler.init_order(); //init the set of process
		} catch (final SpinJaException ex) {
			report(TRANS_ERROR, ex.getMessage());
		}
		
		storeModelandStack(); //first state
		
		isSelectProcess = true; //no process is running
		displaySetProcesses();	//first time for the selection
	}
	
	public void setStop(){
		isStop = true;
	}
	
	public void setSimulate(){
		isSimulate = true;
		isMultiple = false;
		isStop = false;
	}
	
	public void setTrailSimulate(){
		
		isTrailSimulate = true;
		isStop = false;
	}
	
	public boolean isContinueSimulate() {
		return (isSimulate && !isMultiple && !isStop);
	}
	
	public boolean isContinueTrailSimulate() {
		return (isTrailSimulate && !isStop);
	}
	
	public void goToNextSelection(){
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	if (mainUI.lNextAction.size() == 0) { //no choice
					isMultiple = false;
					return;
				}
            	
            	//already select action
				if (mainUI.lNextAction.getSelectedAction().contains("Select")) {
					takeProcessAndDisplayNextActions(mainUI.lNextAction.getSelectedItem());
				} else {
					takeActionAndDisplayNextActions(mainUI.lNextAction.getSelectedItem());
				}
								
            	if (isContinueSimulate()) {
            		//isMultiple is already checked in take...
            		mainUI.btnSimu.doClick();
            	}
            }
        });
	}
	
	
	public void goToNextTrailSelection(){
		SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
            	if (mainUI.lNextAction.size() == 0) { //no choice
					isMultiple = false;
					return;
				}
            	
            	int stateTo;
            	
            	if (mainUI.lNextAction.size() == 1) {
//            		if (trailID < lStateTo.size())
//            		System.out.println(trailID + "> * One option: From " + lStateFrom.get(trailID) + " State to: " + lStateTo.get(trailID));
	            	
            		boolean isSchedulingBehavior = false;
            		//only 1 choice -> let's go
					if (mainUI.lNextAction.getSelectedAction().contains("Select")) {
						System.out.println("Select process");
						isSchedulingBehavior = true;
						takeProcessAndDisplayNextActions(mainUI.lNextAction.getSelectedItem()); //to find correct process
					} else {
//						System.out.println("take action");						
						takeActionAndDisplayNextActions(mainUI.lNextAction.getSelectedItem());
					}
					
					if (trailID < lStateTo.size()) {
						if (lTransID.get(trailID) != 0 && isSchedulingBehavior) { //scheduling transition
            				stateTo = lStateFrom.get(trailID); //fromState is toState
						} else {
							stateTo = lStateTo.get(trailID);
							trailID++;
						}
						
//						System.out.println(trailID + " size == 1, State to: " + lStateTo.get(trailID));
						//then check destination state
						if (!isCurrentState(stateTo)) {
							//the destination state is not match -> trail error
							JOptionPane.showMessageDialog(null, 
									"Trail is not matched!", 
			                        "ERROR", 
			                        JOptionPane.ERROR_MESSAGE);
						} else {
//							System.out.println(trailID + " : matched");
			            	if (isContinueTrailSimulate()) {
			            		//next action in the trail
			                	
			            		mainUI.btnLoadingTrail.doClick();
			            	}
						}
					}
            	} else {
            		//isMultiple = true;
//            		System.out.println(trailID + "> * Multiple option: From " + lStateFrom.get(trailID) + " State to: " + lStateTo.get(trailID));
            		int tryOptionIndex = 0;
            		boolean hasOtherTry = true;
					boolean isMatch = false;
					stateTo = lStateTo.get(trailID);
					
            		if (mainUI.lNextAction.getSelectedAction().contains("Select")) {
            			if (lTransID.get(trailID) != 0) //scheduling transition
            				stateTo = lStateFrom.get(trailID); //fromState is toState
            			
            			//how many processes can be selected  
						takeProcess(tryOptionIndex);
						while (!isMatch && hasOtherTry) {
							isMatch = isCurrentState(stateTo);
							if (!isMatch){
								hasOtherTry = getOtherProcessfromRunningSet();
							}
						}		
					} else {
//						System.out.println("Take transition");
						
						//find correct transition
						for (tryOptionIndex = 0; tryOptionIndex < lTransID.size(); tryOptionIndex++) {
							if (lOptionTransitions.get(tryOptionIndex).getId() == lTransID.get(trailID)) {
								takeActionAndDisplayNextActions(tryOptionIndex);
							}
							
							isMatch = isCurrentState(lStateTo.get(trailID));
							if (!isMatch) {
	    						undoTransition();
	    					} else {
	    						break;
	    					}
	        			}
					}
            		
            		if (!isMatch) {
            			//no more option -> error!
	            		JOptionPane.showMessageDialog(null, 
								"Trail is not matched!", 
		                        "ERROR", 
		                        JOptionPane.ERROR_MESSAGE);
            		} else {
//            			System.out.println(trailID + " : matched");
            			//next action in the trail
            			if (lTransID.get(trailID) == 0 && mainUI.lNextAction.getSelectedAction().contains("Select"))
            				trailID++;
            			
            			if (lTransID.get(trailID) != 0 && !mainUI.lNextAction.getSelectedAction().contains("Select"))
            				trailID++;
            			
//	                	System.out.println("next trailID: " + trailID);
            			if (mainUI.lNextAction.getSelectedAction().contains("Select"))  {
            				//add to trace
            				mainUI.lTraces.addItem(actionIndex + ". " + mainUI.lNextAction.getSelectedAction());
            				mainUI.lSystemStates.addItem(getSystemInformation(actionIndex++));
            				displaySetActions(false);
            			}
		            	if (isContinueTrailSimulate()) {
		            		mainUI.btnLoadingTrail.doClick();
		            	}
					}//multiple option
            	}
            }
        });
	}
	
	public String getSystemInformation(int index) {
		StringBuilder sb = new StringBuilder();
		sb.append("<html>");
		sb.append("[" + index + ".]<br>");
		sb.append(model.toString().replace("\n", "<br>&nbsp;&nbsp;&nbsp;&nbsp;"));
		sb.append(SchedulerPanModel.scheduler.toString().replace("\n\n", "<br>").replace("\n", "<br>&nbsp;&nbsp;&nbsp;&nbsp;"));
		sb.append("</html>");
		return sb.toString();
	}
	
	public void printSystemInformation() { 
		System.out.println("---------------------------------");
		System.out.println(model.toString());
		System.out.println(SchedulerPanModel.scheduler.toString());
		stack.print();
	}
	
	private void storeModelandStack() {
		state  = storeModel();	//encode of the model			
		if (model.conditionHolds(Condition.SHOULD_STORE_STATE)) {
			identifier = store.addState(state);
		} else {
			identifier = hash.hash(state, 0);			
			atomicSteps++;
		}
				
		if (!addState(state, identifier)) { //add to stack
			throw new RuntimeException("Could not even add the state.");
		}
		addRunningSetState();	//add to stack	
	}
	
	private boolean isCurrentState(int id) {
		state  = storeModel();
		int index ;
		if (model.conditionHolds(Condition.SHOULD_STORE_STATE)) {
			index = store.addState(state);
		} else {
			index = hash.hash(state, 0);			
			atomicSteps++;
		}
		return (id == index) || (-1 - id == index); //already existed
	}
	
	
	public int displaySetProcesses(){
		SchedulerPromelaModel.scheduler.set_RunningSet(); //get possible running processes in the running set
		
		enabled.clear();
		//set the set of processes which can be run
		for (SchedulerProcess p : SchedulerPromelaModel.scheduler.getRunningSet().dataSet){
			enabled.add("Select Process: " + p.getName());
		}
		
		mainUI.setNextSelections(enabled);
		if (enabled.size() > 1) {
			isMultiple = true;
			mainUI.lNextAction.listAction.setEnabled(true);
		} else {
			isMultiple = false;
			mainUI.lNextAction.listAction.setEnabled(false); //can not select
		}
			
		return enabled.size();
	}
	
	public void takeProcess(int selectedItem) {	//selectedItem is from combobox
		try {			
			//SchedulerPromelaModel.scheduler.running_process == null
			running_processID = SchedulerPromelaModel.scheduler.select_process(-1) ; //running_process != null
			
			if (selectedItem > 0){
				int preProcessID = SchedulerPromelaModel.scheduler._runningSet.dataSet.get(selectedItem - 1).processID; //get previous process ID
				running_processID = SchedulerPromelaModel.scheduler.select_process(preProcessID) ; //select the destination process
			} 
			
			saveSchedulerTransitionToStack();
			storeModelandStack();
			
			System.out.println("Take process: " + running_processID);
		} catch (final SpinJaException ex) {
			report(TRANS_ERROR, ex.getMessage());
		}
	}
	
	private void saveSchedulerTransitionToStack(){
		//save the trace to stack
		SchedulerTransition schTrans = new SchedulerTransition(1); //select process
		stack.pushSchTrans(schTrans);
	}
	
	
	
	public void takeProcessAndDisplayNextActions(int selectedItem) {	
		//add the trace and the state
		mainUI.lTraces.addItem(actionIndex + ". " + mainUI.lNextAction.getSelectedAction());
		mainUI.lSystemStates.addItem(getSystemInformation(actionIndex++));
		
		//select the process
		takeProcess(selectedItem);
		
		//save the trace to stack //already done in takeProcess
//		SchedulerTransition schTrans = new SchedulerTransition(1); //select process
//		stack.pushSchTrans(schTrans);
//		storeModelandStack();
		
		//process is selected (running_process != null), display possible actions of the current process
		displaySetActions(false); //no error
	}

	public void displaySetActions(boolean error){
		enabled.clear();
		lOptionTransitions.clear();
		
		stack.print();
		
		if (!error) {
			T curr = null;
			while ((curr = nextTransition.next(model, curr)) != null) {
				lOptionTransitions.add(curr);
				enabled.add(curr.toString());
			}
		
			if (enabled.size() == 0) {
				System.out.println("Deadlock");
				mainUI.setNextSelections(null);
				JOptionPane.showMessageDialog(null, 
						"DEADLOOK", 
                        "ERROR", 
                        JOptionPane.ERROR_MESSAGE);
			} else {
				mainUI.setNextSelections(enabled);
				if (enabled.size() > 1) {
					isMultiple = true;
					mainUI.lNextAction.listAction.setEnabled(true);
				} else {
					isMultiple = false;
					mainUI.lNextAction.listAction.setEnabled(false); //can not select
				}
			}
		} else {
			mainUI.setNextSelections(null);
		}
	}
	
	public void takeActionAndDisplayNextActions(int actIndex) {
		mainUI.lTraces.addItem(actionIndex + ". " + mainUI.lNextAction.getSelectedAction());
		mainUI.lSystemStates.addItem(getSystemInformation(actionIndex++));
		
		if (outOfMemory) { // If there is too little memory left
			throw new OutOfMemoryError(); // throw a java error
		}
		// Make sure that the state matches the model
		assert checkModelState();
		
		if (state.length > maxSize) {
			maxSize = state.length;
		}

		if (getDepth() > maxDepth) {
			maxDepth = getDepth();
		}			
		
		if (lOptionTransitions.size() == 0) {
			if (checkForDeadlocks && !model.conditionHolds(Condition.END_STATE)
				&& getLastTransition() == null) {
				report(DEADLOCK);
				try {
					outputTest("Deadlock", Log.out);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				report(NO_MORE_TRANSITIONS);
			}
			
		} else {
			Transition action = lOptionTransitions.get(actIndex);
			boolean isTimer ;
			isTimer = SchedulerPromelaModel.scheduler.isTimer();			
			if (outOfMemory) {			
				throw new OutOfMemoryError();
			}
			 
			try {				
				takeTransition(action);
				System.out.println("Take action: " + action.toString());
				mainUI.setLastActionEdge(action.getId() + "");
				
				SchedulerPromelaModel.scheduler._runningSet.dataSet.clear();

				if (isTimer) {
					try {
						SchedulerPromelaModel.scheduler.clock();
					} catch (ValidationException e) {
						e.printStackTrace();
						report(TRANS_ERROR, e.getMessage());
						JOptionPane.showMessageDialog(null, 
								e.getMessage() + "\nSee results on " + 
										SchedulerPromelaModel.scheduler.getSchedulerName() + "Error.trail", 
		                        "ERROR", 
		                        JOptionPane.ERROR_MESSAGE);
					}
				}
				
				if (SchedulerPromelaModel.scheduler.running_process == null) {
					displaySetProcesses();
				} else {
					displaySetActions(false);
				}
				storeModelandStack();

			} catch (SpinJaException e) {					
				e.printStackTrace();
//				print_trace(); //to console
				outputErrorTrace(SchedulerPromelaModel.scheduler.getSchedulerName()); //to trail file
				report(TRANS_ERROR, e.getMessage());
				JOptionPane.showMessageDialog(null, 
						e.getMessage() + "\nSee results on " + 
								SchedulerPromelaModel.scheduler.getSchedulerName() + "Error.trail", 
                        "ERROR", 
                        JOptionPane.ERROR_MESSAGE);
				displaySetActions(true); //with error
				isStop = true;
			}	
		}
	} 
	

	
	
	protected boolean getOtherProcessfromRunningSet() {
		if (SchedulerPromelaModel.scheduler._runningSet == null)
			return false ;
		if (SchedulerPromelaModel.scheduler._runningSet.size() <= 1) //true
			return false ;
		else {						
			try {
				if (SchedulerPromelaModel.scheduler.running_process != null) {
					running_processID = SchedulerPromelaModel.scheduler.running_process.processID ;
					running_processID = SchedulerPromelaModel.scheduler.select_process(running_processID) ;
				} else return false ;
			} catch (final SpinJaException e) {			
				e.printStackTrace();
				report(TRANS_ERROR, e.getMessage());
			}			
			//no other process
			if (running_processID < 0)
				return false ;
		    else 
		    	return true ;
		}
	}
	
	public void printInfo() {		
		final double memory = getBytes() / (1024.0 * 1024.0);
		Util.println("----------------------------------------------------");				
		Util.println("System state: " +  store.getStored());
		Util.println("System state match: " + statesMatched);
		Util.println("System state memory: " + (double) store.getBytes()/(1024 * 1024));
		Util.println("----------------------------------------------------");		
	}

	/**
	 * @see spinja.search.SearchAlgorithm#getBytes()
	 */
	@Override
	public long getBytes() {
		return 26 + store.getBytes() ; 
	}
	
	

	@Override
	public int getDepth() {
		return stack.getSize();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected T getLastTransition() {
		return (T)stack.getLastTransition();
	}

	
	@Override
	public SearchableStack getSearchableStack() {
		return stack;
	}

	/**
	 * This function prints the trace to a .trail file so that it can be rerun.
	 * 
	 * @param name
	 *            The name of the file (without extension)
	 */
	@Override
	protected void outputTrace(final String name) {
		try {
			final PrintWriter out = new PrintWriter(
				new BufferedWriter(new FileWriter(name + ".trail")));
			for (int i = 0; i < stack.getSize(); i++) {
				if (stack.getTransition(i) != null) {
					out.println(stack.getTransition(i).getId());
				} else {
					break;
				}
			}
			out.flush();
			out.close();
			Util.println("sspinja: wrote " + name + "_Error.trail\n");
		} catch (final IOException ex) {
			Util.println("sspinja: error while writing " + name + ".trail: " + ex.getMessage());
		}
	}

	public void outputErrorTrace(final String name) {
		if (!isTrailSimulate) {
			try {
				final PrintWriter out = new PrintWriter(
					new BufferedWriter(new FileWriter(name + "_Error.trail")));
				for (int i = 0; i < stack.getSize(); i++) {
					if (stack.getTransition(i) != null) {
						out.println(stack.identifiers[i] + "->" + stack.lastTransition[i].getId() + "->" + stack.lastTransition[i].toString());
					} else {
						break;
					}
				}
				out.flush();
				out.close();
				Util.println("sspinja: wrote " + name + "_Error.trail\n");
			} catch (final IOException ex) {
				Util.println("sspinja: error while writing " + name + "_Error.trail: " + ex.getMessage());
			}
		}
	}
	
	@Override
	protected boolean restoreState() {
		return stack.getSize() > 0;
	}

	@Override
	protected void stateDone() {	
		stack.pop() ;
		if (stack.getSize() > 0) {
			restoreModel();
			restoreRunningSet();
		}
	}


	public Transition callUndoTransition(){
		if (stack.top() <= 0) {
			actionIndex = 0;
			return null;
		}
		
		stack.print();
		Transition lastTrans = stack.getLastTransition() ;
		
		while (lastTrans == null) { 
			restoreModel();
			restoreRunningSet();
			
			stack.pop();
			lastTrans = stack.getLastTransition() ;
			stack.print();
		}
		
		if (actionIndex > 0) 
			actionIndex--;	
		
		
		if (lastTrans.toString().contains("-end-") || lastTrans.toString().contains("Select process")) { //process behaviors && scheduler behaviors
			System.out.println(" - ID != 0 restore : " + lastTrans.toString());
			restoreModel();
			restoreRunningSet();
		} else { //scheduling action
			System.out.println(lastTrans.getId() + " - ID == 0 undo: " + lastTrans.toString());		
			lastTrans.undo();
		}
		
		
		stack.resetLastTransition();
		
		
		
		T curr = null;
		while ((curr = nextTransition.next(model, curr)) != null) {
			System.out.println("Next: " + curr.toString());
		}
//		System.out.println(getSystemInformation(0));
		
//		stack.pop();
		
		
//		if (lastTrans != null) {
//			String trans = lastTrans.toString() ;
//			if (lastTrans instanceof PromelaTransition) {
//				PromelaProcess proc = ((PromelaTransition) lastTrans).getProcess() ;
//				if (proc == null && trans.contains("-end-")) { //claim
//					restoreModel();
//					restoreRunningSet();
//				}else {
//					String procName = proc.getName() ;
//					if (procName != null) {
//						if (procName.contains("sporadic") ) {					
//							if (trans.contains("skip")) {
//								stateDone() ;
//							} else {
//								restoreModel();
//								restoreRunningSet();
//							}
//						} else { //can be -end- transition
////							stateDone();
//							stack.pop();
//							stack.print();
//							restoreModel();
//							restoreRunningSet();
//						}
//					} else {
//						if (trans.contains("-end-")) {
//							stateDone();
//						} else {							
//							restoreModel();
//							restoreRunningSet();
//						}
//					}
//				}
//			} else { //instance of SchedulerTranstion
//				restoreModel();
//				restoreRunningSet();				
//			}
//		} else { //lastTrans == null
//			restoreModel();
//			restoreRunningSet();
//			stateDone();
//		}	
		
		 
		
		return lastTrans;
	}

	public void writeTrailFile (final File fileToSave) {
		String fileDir = fileToSave.getAbsolutePath().substring(0, fileToSave.getAbsolutePath().lastIndexOf("/"));
	    String fileName = fileToSave.getName();
	    
		final File trailFile = new File(fileDir, fileName + ".trail");

		stack.print();
		String out = "";
		for (int i=0; i<stack.getSize(); i++){
			if (stack.lastTransition[i] != null)
				out += stack.identifiers[i] + "->" + stack.lastTransition[i].getId() + "->[" + stack.lastTransition[i].toString() + "]\n";
		}

//		System.out.println(out);
		try {
			FileOutputStream fos = new FileOutputStream(trailFile);
			fos.write(out.getBytes());
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void readTrailFile (final File fileToRead) {
		String[] parts;		
    	BufferedReader bufReader;
    	
    	
        try {
            //read per line
            bufReader = new BufferedReader(new FileReader(fileToRead));
            String line = "";
           
            int stateFrom;
            int stateTo;
            int transID;
            boolean isFirst = true;
            
            line = bufReader.readLine();
    		if (line != null) {
    			parts = line.split("->");
        		stateFrom = Integer.parseInt(parts[0]);
        		lStateFrom.add(stateFrom);
        		transID = Integer.parseInt(parts[1]);
        		lTransID.add(transID);
    		}
            while (line != null) {
            	line = bufReader.readLine();
        		if (line != null) {
	        		parts = line.split("->");
	        		stateFrom = Integer.parseInt(parts[0]);
	        		lStateTo.add(stateFrom);
	        		lStateFrom.add(stateFrom);
	        		transID = Integer.parseInt(parts[1]);
	        		lTransID.add(transID);
        		}
            }
            bufReader.close();
        } catch (IOException e) {
        	//graph file not found or error
        } finally {
        	
        }
	}
	

	
	@Override
	protected void print_trace() {
		stack.print();		
	}


	@Override
	protected Transition nextTransition() {
		// TODO Auto-generated method stub		
		return null;
	}


	@Override
	protected void takeTransition(final Transition next) throws SpinJaException {
		stack.takeTransition(next);
	}


	@Override
	protected Transition nextTransition(Transition last) {
		// TODO Auto-generated method stub
		return model.nextTransition((T) last) ;
	}
	
	
}


