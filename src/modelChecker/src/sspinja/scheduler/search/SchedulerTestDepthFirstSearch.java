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
import static spinja.search.Message.TRANS_ERROR;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import spinja.exceptions.SpinJaException;
import spinja.exceptions.ValidationException;
import spinja.model.Condition;
import spinja.model.Model;
import spinja.model.Transition;
import spinja.promela.model.PromelaProcess;
import spinja.promela.model.PromelaTransition;
import spinja.search.TransitionCalculator;
import spinja.store.StateStore;
import spinja.util.DummyStdOut;
import spinja.util.StringUtil;
import sspinja.Config;
import sspinja.Run;
import sspinja.scheduling.SchedulerObject;
import sspinja.SchedulerPanModel;
import sspinja.scheduler.promela.model.*;

/**
 * The Depth First Search is a basic complete search algorithm. It tries every transition that it
 * can find in a depth first way.
 * 
 * @author Marc de Jonge
 *
 */
public class SchedulerTestDepthFirstSearch<M extends Model<T>, T extends Transition> extends SchedulerSearchAlgorithm<M, T> {
	
	
	private static final long serialVersionUID = 1L;
	private PrintWriter out  ;
	
	public SchedulerTestDepthFirstSearch(M model, StateStore store, int stackSize, boolean errorExceedDepth,
			boolean checkForDeadlocks, int maxErrors, TransitionCalculator<M, T> nextTransition) {				
		super(model, store, stackSize, errorExceedDepth, checkForDeadlocks, maxErrors, nextTransition);
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(model.getName() + ".test.result")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected Transition nextTransition() {		
		return model.nextTransition(null) ;
	}

	@Override
	protected Transition nextTransition(Transition last) {	
		return nextTransition.next(model, getLastTransition()) ;
	}	
	
	@Override
	protected void takeTransition(final Transition next) throws SpinJaException {
		
		stack.takeTransition(next);
	}
	
	public void execute() {
		System.out.println("Begin execute");
		Config.processInit = false ;		
		
		try {
			SchedulerPromelaModel.scheduler.init_order();
		} catch (final SpinJaException ex) {
			report(TRANS_ERROR, ex.getMessage());
		}		

		try {
			running_processID = SchedulerPromelaModel.scheduler.select_process(-1) ;
		} catch (final SpinJaException ex) {
			report(TRANS_ERROR, ex.getMessage());
		}
		
		if (running_processID < 0) {
			System.out.println("Error running process ");
			return ;
		}	
		byte[] state  = storeModel(); 
		
		int identifier ;	
		if (model.conditionHolds(Condition.SHOULD_STORE_STATE)) {
			identifier = store.addState(state);
			if (identifier >= 0) {
				print(" + New state " + (store.getStored() - 1));
				printBytes(state) ;
			} else {
				print("  --> No New state " );
			}
		} else {
			identifier = hash.hash(state, 0);			
			atomicSteps++;
		}
		if (!addState(state, identifier)) {
			throw new RuntimeException("Could not even add the first state.");
		}
			
		Transition last = null ;
		Transition next = null;	
		while ((nrErrors < Integer.MAX_VALUE) && !Thread.currentThread().isInterrupted() && restoreState()) {			
			if (outOfMemory) {				
				throw new OutOfMemoryError();
			}
		
			assert checkModelState();
			if (state.length > maxSize) {
				maxSize = state.length;
			}
					
			if (getDepth() - 1 > maxDepth) {
				maxDepth = getDepth() - 1;
			}
						
			last = stack.getLastTransition() ;			
			next = nextTransition(last) ;				

			if (next == null) {  			
				if (last == null) { 					
					if (!SchedulerPromelaModel.scheduler.isTimer()) {
						DummyStdOut.enableStdOut();
						report(DEADLOCK);
						DummyStdOut.disableStdOut();
					}
					try {							
						SchedulerPromelaModel.scheduler.clock();					
					} catch (ValidationException e) {
						e.printStackTrace();						
						report(TRANS_ERROR, e.getMessage());
						break ;
					}
					state = storeModel() ;
					
					if (model.conditionHolds(Condition.SHOULD_STORE_STATE)) {
						identifier = store.addState(state);
						if (identifier >= 0) {
							print(" + New state " + (store.getStored() - 1));
							printBytes(state) ;
						} else {
							if (identifier < 0) {	
								report(DUPLICATE_STATE.withState(state));
								nextTransition.duplicateState((M) model, getLastTransition(), state, -(identifier + 1), getSearchableStack());
								statesMatched++;
								
								if ( Config.isCheckAcceptionCycle) {
									System.out.println("*. Duplicate state -> .....");
									printStack() ;
									undoTransition();
									continue;
								} else {
									DummyStdOut.enableStdOut();
									report(DEADLOCK) ;
									DummyStdOut.disableStdOut();
								}
								
							}
						}
					}				
				} else {
					if (SchedulerPanModel.scheduler._runningSet.isEmpty() == 0) {
						System.out.println("\n\n->>>> Select other process in poset");
						
						try {
							running_processID = SchedulerPanModel.scheduler.select_process(SchedulerPanModel.scheduler.running_process.processID) ;
						} catch (final SpinJaException e) {			
							e.printStackTrace();							
							report(TRANS_ERROR, e.getMessage());							
							break ;
						}						
						
						if (running_processID < 0) {
				        	System.out.println("No process can be selected");
				        	break ;
				        }
						stack.resetLastTransition();
					} else {						
						stateDone() ;
						continue ;
					}					
				}
			} 
			else  {
					try {
						System.out.println("--> Take transtion: " + next);
						takeTransition(next);
					} catch (SpinJaException e) {					
						e.printStackTrace();						
						report(TRANS_ERROR, e.getMessage());					
						continue ;
					}
					if ( (modelType == 0) 
							|| (modelType == 1 && SchedulerSporadicModel.sporadicTurn)
							|| (modelType == 2 && SchedulerNeverClaimPromelaModel.claimTurn)
							|| (modelType == 3 && (SchedulerNeverClaimSporadicModel.claimTurn && SchedulerNeverClaimSporadicModel.sporadicTurn))
							) {
								SchedulerPanModel.scheduler._runningSet.dataSet.clear();						
					}
					
					if (model.conditionHolds(Condition.SHOULD_STORE_STATE)) {
						state = storeModel() ;
						identifier = store.addState(state);
						
						if (identifier < 0) {						
							report(DUPLICATE_STATE.withState(state));
							nextTransition.duplicateState((M) model, getLastTransition(), state, -(identifier + 1), getSearchableStack());
							statesMatched++;							
							print(" + Duplicate state " + (store.getStored() - 1));
							printBytes(state) ;
							printStack() ;

							if ( Config.isCheckAcceptionCycle) { 
								System.out.println("*. Duplicate state -> .....");
								printStack() ;								
								continue;
							}							
							undoTransition();
							continue;	

						} else {
							if ((store.getStored() & 0xfffff) == 0) {
								printInfo();
							}
						}
					} else {
						atomicSteps++;
						identifier = hash.hash(state, 0);
					}
					
					
					if (!addState(state, identifier) ){
						if (errorExceedDepth) {
							report(EXCEED_DEPTH_ERROR);
						} else if (!printedDepthWarning) {
							report(EXCEED_DEPTH_WARNING);
							printedDepthWarning = true;
						}
						undoTransition(); 
						continue;
					}
				}
			
			
		}
		freeMemory();
		if (Run.testDepth != -1)
			flushOutputTest(model.getName());
	}
	
	@Override
	protected void outputTest(final String text, PrintWriter out) throws IOException {
		out.println(text);
		for (int i = 0; i < stack.getSize(); i++) {
			if (stack.getTransition(i) != null) {
				out.println(i + "." + stack.getTransition(i).toString());
			} else {
				break;
			}
		}		
		out.println("------------------------------------------------");
	}
	protected void flushOutputTest(String name) {
		out.flush();
		out.close();
		DummyStdOut.enableStdOut();
		System.out.println("sspinja: wrote " + name + ".test.result\n");
	}
	
	@Override
	protected void undoTransition() {
		Transition lastTrans = stack.getLastTransition() ;
		if (lastTrans != null) {
			String trans = lastTrans.toString() ;
			print("--> 1. Undo " + trans) ;
			
			if (lastTrans instanceof PromelaTransition) {
				PromelaProcess proc = ((PromelaTransition) lastTrans).getProcess() ;
				if (proc == null && trans.contains("-end-")) {
					restoreModel();
				}
				else {
					if (proc.getName().contains("sporadic") ) {
						if (trans.contains("skip")) {							
							stateDone() ;
						} else {
							restoreModel();
						}				
					}
					else {
						if (trans.contains("-end-") || StringUtil.containSchedulerKeyword(trans) || trans.contains("stop") || trans.contains("terminate") )
							restoreModel();
						else {
							restoreModel();
						}
					}
				}
			}			
		} else {
			System.out.println("Only scheduler transition -> undo model");
			restoreModel();
			stateDone(); 
		}
		SchedulerPromelaModel.scheduler.updateProcessInSchedulerList() ;
	}
	
	public void updateProcessList() {
		
	}
	
}

