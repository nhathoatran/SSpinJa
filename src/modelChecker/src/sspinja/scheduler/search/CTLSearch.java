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
import static spinja.search.Message.NO_MORE_TRANSITIONS;
import static spinja.search.Message.TRANS_ERROR;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import spinja.exceptions.SpinJaException;
import spinja.model.Condition;
import spinja.model.Model;
import spinja.model.Transition;
import spinja.promela.model.PromelaModel;
import spinja.search.DepthFirstSearch;
import spinja.search.TransitionCalculator;
import spinja.store.StateStore;
import spinja.util.DummyStdOut;
import spinja.util.Log;
import spinja.util.Util;
import sspinja.scheduling.CTLFormula;
import sspinja.Config;
import sspinja.Run;
import sspinja.SchedulerPanModel;
import sspinja.SchedulerState;
import sspinja.scheduler.promela.model.SchedulerPromelaModel;

public class CTLSearch<M extends Model<T>, T extends Transition> extends DepthFirstSearch<M, T> {	
	private static final long serialVersionUID = 1L;

//	protected PrintWriter out  ;
	protected HashMap<Integer, SchedulerState> schedulerstatehashmap;
	protected HashMap<Integer, ArrayList<Transition>> inittracehashmap;	
	protected int state_count = 0 ;
	protected SchedulerState startSchedulerState = null ;
	protected SchedulerState currentSchedulerState = null ;
	protected ArrayList<SchedulerState> verifyStatesList ;
	protected CTLFormula formula = new CTLFormula() ;
	protected ArrayList<Integer> cyclicSchedulerStateIDList;
	ArrayList<Transition> starttrace ;
	HashSet<SchedulerState> visitedStates ;
	ArrayList<Transition> trace ;
	
	public CTLSearch(M model, StateStore store, int stackSize, boolean errorExceedDepth,
			boolean checkForDeadlocks, int maxErrors, TransitionCalculator<M, T> nextTransition) {
		super(model, store, stackSize, errorExceedDepth, checkForDeadlocks, maxErrors, nextTransition);
		
		inittracehashmap = new HashMap<Integer, ArrayList<Transition>>();
		schedulerstatehashmap = new HashMap<Integer, SchedulerState>();
		verifyStatesList = new ArrayList<SchedulerState>() ;
		cyclicSchedulerStateIDList = new ArrayList<Integer>() ;
		Log.initLog(model.getName() );		
	}
	
	public void execute() {
		InitGraph() ;	

		
		
		
		if (verifyStatesList.size() == 0) {
			System.out.println("No state to be verified");
		} else {
			if (!Config.processLimit && ((PromelaModel) model).modelCheck()) {
				LabelGraph() ;
				visitedStates = new HashSet<SchedulerState>() ;
				trace = new ArrayList<Transition>() ;
				for (SchedulerState schedulerstate : verifyStatesList) {
					trace.clear();
					trace.addAll(inittracehashmap.get(schedulerstate.identifier)) ;
					PrintTrace(true,trace, schedulerstate, 0, formula.length - 1, formula.sn[0]);
				}
								
			} else {
				Log.out.println(formula.toString() + " : Not determined") ;
				Log.out.println("Search incomplete") ;
				System.out.println("Search incomplete") ;
				System.out.println(formula.toString() + " : Not determined") ;
				Log.analysisresult = true ;
			}
		}

	}
	
	protected void InitGraph() {
		byte[] state = storeModel();
		int counter=-1;
		int identifier;
		if (model.conditionHolds(Condition.SHOULD_STORE_STATE)) {
			identifier = store.addState(state);
			if (identifier >= 0) {
			}
		} else { // otherwise count it
			identifier = hash.hash(state, 0);
			atomicSteps++;
		}
		if (!addState(state, identifier)) {
			throw new RuntimeException("Could not even add the first state.");
		}

		put_schedulerState(identifier, 0, null) ; //depth = 0, starting state
		startSchedulerState = schedulerstatehashmap.get(identifier) ;
		
		
		// While there are still more states to explore
		// or the maximum number of errors was not yet found
		while ((nrErrors < maxErrors) && !Thread.currentThread().isInterrupted() && restoreState()) {
			if (outOfMemory) { // If there is too little memory left
				throw new OutOfMemoryError(); // throw a java error
			}
			// Make sure that the state matches the model
			assert checkModelState();
		
			currentSchedulerState = stack.getTopIdentifer() < 0 ? null : schedulerstatehashmap.get(stack.getTopIdentifer()) ;
			
			if (stack.getSize() == 1) {
				starttrace = new ArrayList<Transition> () ; 
			}
			
			if (stack.getSize() == 3) { //initial node of the graph
				if (identifier > 0) {
					if (!verifyStatesList.contains(currentSchedulerState))
						verifyStatesList.add(currentSchedulerState) ;
					inittracehashmap.put(identifier, starttrace) ;
				}
			}
			
			if (state.length > maxSize) {
				maxSize = state.length;
			}

			if (getDepth() > maxDepth) {
				maxDepth = getDepth();
			}			

			final Transition next = nextTransition();
			
			if (stack.getSize() < 3 && next != null) {
				starttrace.add(next) ;
			}
				
				
			if (next == null) {
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
				stateDone();
				storeModel();
				continue;
			}
			try {
				counter++;
				takeTransition(next);
			} catch (final SpinJaException ex) {
				report(TRANS_ERROR, ex.getMessage());
				try {
					outputTest(ex.getMessage(), Log.out);
				} catch (IOException e) {
					e.printStackTrace();
				}
				continue;
			}

			state = storeModel();
			if (model.conditionHolds(Condition.SHOULD_STORE_STATE)) {
				identifier = store.addState(state);
				if (identifier < 0) {
					SchedulerState schState = schedulerstatehashmap.get(-1-identifier) ;
					if (!currentSchedulerState.next.contains(schState)) {
						currentSchedulerState.next.add(schState);
						currentSchedulerState.trans.add(stack.getLastTransition()) ;
					}
					
					
					report(DUPLICATE_STATE.withState(state));
					nextTransition.duplicateState(model, getLastTransition(), state, -(identifier + 1), getSearchableStack());
					statesMatched++;
					undoTransition();
					continue;
				} else {
					put_schedulerState(identifier, stack.getSize(),next) ;					
					if ((store.getStored() & 0xfffff) == 0) {
						printInfo();
					}
				}
			} else { // otherwise count it
				atomicSteps++;
				identifier = hash.hash(state, 0);
			}

			// Push the state onto the stack/queue/...
			if (!addState(state, identifier)) {
				if (errorExceedDepth) {
					report(EXCEED_DEPTH_ERROR);
				} else if (!printedDepthWarning) {
					report(EXCEED_DEPTH_WARNING);
					printedDepthWarning = true;
				}
				undoTransition();
			}
		}
		freeMemory();
	} //end initgraph
	
	protected void put_schedulerState(int identifier, int depth, Transition trans) {
		SchedulerState schState = new SchedulerState() ;
		schState.results = ((PromelaModel) model).init_atomicf() ;
		schState.checked = ((PromelaModel) model).init_sf() ;
		
		schState.identifier = identifier ;
		//schState.father = currentSchedulerState ;
		
		if (currentSchedulerState != null) {
			currentSchedulerState.next.add(schState) ;
			currentSchedulerState.trans.add(trans) ;
		}
		schedulerstatehashmap.put(identifier, schState); //if hash table is over
		state_count ++ ;
		if (((PromelaModel) model).stateCheck()) {
			verifyStatesList.add(schState);
		}
	}	
	private void getCyclicSchedulerStateID(ArrayList<Integer> listID) {
		for (int i = 0 ; i < listID.size(); i++)
			if (!cyclicSchedulerStateIDList.contains(listID.get(i))){
				cyclicSchedulerStateIDList.add(listID.get(i)) ;
			}
	}
	private void updateSchedulerState(int stateid, int depth, boolean isCycle, boolean isCurrentState) {
		SchedulerState schState = schedulerstatehashmap.get(stateid) ;
		schState.duplicate = isCycle ; //????
		schState.update(depth, isCycle, isCurrentState) ;
	}
	
	
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
	private void PrintGraph() {		
		ArrayList<SchedulerState> schStateList = new ArrayList<SchedulerState>() ;
		startSchedulerState.print(schStateList, "",false);
	}
	private void LabelGraph() {		
//		System.out.println("Graph before label");
//		PrintGraph();

		for (SchedulerState s : verifyStatesList) {
			for (int fi=formula.length-1; fi >= 0; fi --) {	
//				System.out.println(formula.opcode[fi]);
				if (formula.opcode[fi] != "atomic") {
					int n = formula.sn[fi] ;
					labelgraph(fi, s, n) ;
				}
			}
//			System.out.println("Graph result");
//			PrintGraph();						
			
			if (s.results[0]) {
				System.out.println("+ Check system (start tate: " + s.identifier + ") with: " + formula.toString().trim() + ": Satisfied") ;
				Log.out.println("+ Check system (start state: " + s.identifier + ") with: " + formula.toString().trim() + ": Satisfied");
				Log.analysisresult = true ;
			} else {
				System.out.println("+ Check system (start state: " + s.identifier + ") with: " + formula.toString().trim() + ": Unsatisfied");
				Log.out.println("+ Check system (start state: " + s.identifier + ") with: " + formula.toString().trim() + ": Unsatisfied");
				Log.analysisresult = false ;
			}
		}
//		System.out.println("Graph result");
//		PrintGraph();
	}
	private void labelgraph(int fi, SchedulerState s, int n){
//		System.out.println("--------------------------------") ;
//		System.out.println("label [" + fi + "]:" +  formula.opcode[fi] + " for " + s.identifier); s.print(); s.printchildlist();
		switch (formula.opcode[fi]) {
			case "atomic" :
				break ; //already labeled	
			case "not" :
				label_not(fi,s);
				break ;	
			case "or" :
				label_or(fi,s) ;
				break;
			case "implies" :
				label_implies(fi,s) ;
				break;	
			case "AX" :
				label_ax(fi,s);
				break ;
			case "AF" :				
				label_af(fi,s,n);
				break ;
			case "AG" :
				label_ag(fi,s, n);
				break ;
				
			case "EX" :
				label_ex(fi,s);
				break ;
			case "EF" :
				label_ef(fi,s,n);
				break ;
			case "EG" :
				label_eg(fi,s, n);
				break ;			
			case "AU" :
				label_au(fi,s, n);
				break ;				
			case "EU" :
				label_eu(fi,s, n);
				break ;
		}
//		System.out.println("-> result labeled [" + fi + "] " + formula.opcode[fi] + " for " + s.identifier); s.print(); s.printchildlist();
	}
	
	private void label_not(int fi, SchedulerState s) {
		if (s.checked[fi]) return ;
		byte s1 = formula.sf[fi][0] ;
		if (!s.checked[s1]) labelgraph(s1,s,-1);
		s.results[fi] = !s.results[s1];
		s.checked[fi] = true ;
	}
	private void label_or(int fi, SchedulerState s) {
		if (s.checked[fi]) return ;
		byte s1 = formula.sf[fi][0] ;
		byte s2 = formula.sf[fi][1] ;
		if (!s.checked[s1]) labelgraph(s1,s,-1);
		if (!s.checked[s2]) labelgraph(s2,s,-1);
		s.results[fi] = s.results[s1] || s.results[s2];
		s.checked[fi] = true ;
//		for (SchedulerState child : s.next) { //check all
//			label_or(fi,child) ;					
//		}
	}
	private void label_implies(int fi, SchedulerState s) {
		if (s.checked[fi]) return ;
		
		byte s1 = formula.sf[fi][0] ;
		byte s2 = formula.sf[fi][1] ;
		if (!s.checked[s1]) labelgraph(s1,s,-1);
		if (!s.checked[s2]) labelgraph(s2,s,-1);
		s.results[fi] = ! s.results[s1] || s.results[s2];		
		s.checked[fi] = true ;
//		for (SchedulerState child : s.next) { //check all
//			label_implies(fi,child) ;					
//		}
	}
	
	private void label_ax(int fi, SchedulerState s) {
		//for all path starting at s, next time fi -> s1 (fi == AX s1)
		if (s.checked[fi]) return ;
		byte s1 = formula.sf[fi][0] ;
		boolean result = true ;
		for (SchedulerState child : s.next) {
			if (!child.checked[s1])
				labelgraph(s1, child, -1) ;
			if (! child.results[s1]) {
				result = false ;
				break ;
			}
		}
		s.results[fi] = result ;
		s.checked[fi] = true ;
	}
	private void label_af(int fi, SchedulerState s, int n) {
		//for all paths starting at s, eventually fi
		if (s.checked[fi]) return ;
		byte s1 = formula.sf[fi][0] ;	//formula -> s1
		s.checked[fi] = true ;
					
		if (!s.checked[s1])
			labelgraph(s1, s, n) ;		
		
		if (s.results[s1]) {	//check node		
			s.results[fi] = true ;
		} else {
			if (s.next.size() == 0) //empty
				s.results[fi] = false ;
			else {
				boolean noderesult = true ; //exist false -> false
				for (SchedulerState child : s.next) { //check paths
					if (child.checked[fi]) { //loop ?
						if (child.results[fi]) {
						} else {							
							noderesult = false ;							
							break ;
						}
					} else {												
						if (n == -1) { //no superscript
							labelgraph(fi,child, -1);						
						} else {
							if (n > 1) {
								labelgraph(fi, child, n-1);									
							} else {//n == 0
								child.results[fi] = child.results[s1] ; //stop the checking	
							}
						}	
						if (!child.results[fi]) {
							noderesult = false ;
							break ;
						}
					}					
				}
				s.results[fi] = noderesult ;				
			}
		}
	}
	private void label_ag(int fi, SchedulerState s, int n) {
		//for all paths starting at s, always fi
		if (s.checked[fi]) return ;
		s.checked[fi] = true ;
		
		byte s1 = formula.sf[fi][0] ;	//formula fi -> result by s1	
		if (!s.checked[s1])
			labelgraph(s1, s, n) ;
//		System.out.println("Before:");
//		s.print() ;
		
		if (!s.results[s1]) {	//check node			
			s.results[fi] = false ;
		} else {								
			if (s.next.size() == 0) //empty
				s.results[fi] = true ; //= s.results[s1]
			else {
				boolean noderesult = true ; //exist false -> false
				for (SchedulerState child : s.next) { //check paths
					if (child.checked[fi]) { //loop																			
						if (child.results[s1]) {
						} else {		
							child.results[fi] = false ;
							noderesult = false ;
							break ;
						}
					} else {
						if (n == -1) { //no superscript
							labelgraph(fi,child, -1);						
						} else {
							if (n > 1) {
								labelgraph(fi, child, n-1);									
							} else {//n == 0
								child.results[fi] = child.results[s1] ; //stop the checking	
							}
						}	
						if (!child.results[s1]) {
							noderesult = false ;
							break ;
						}
					}
					 
				}
				s.results[fi] = noderesult ;			
			}
		}
//		System.out.println("After:");
//		s.print() ;
	}

	private void label_ex(int fi, SchedulerState s) {
		//exist path starting at s, next time fi -> s1
		if (s.checked[fi]) return ;
		byte s1 = formula.sf[fi][0] ;
		boolean result = false ;
		for (SchedulerState child : s.next) {
			if (!child.checked[s1])
				labelgraph(s1, child, -1) ;
			if (child.results[s1]) {
				result = true ;
				break ;
			}
		}
		s.results[fi] = result ;
		s.checked[fi] = true ;
	}
	private void label_ef(int fi, SchedulerState s, int n) {
		//exist a path starting at s, eventually fi
		if (s.checked[fi]) return ;
		byte s1 = formula.sf[fi][0] ;	//formula -> s1
		s.checked[fi] = true ;
					
		if (!s.checked[s1])
			labelgraph(s1, s, n) ;
		
		if (s.results[s1]) {	//check node		
			s.results[fi] = true ;
		} else {
			if (s.next.size() == 0) //empty
				s.results[fi] = false ; //s1 = false
			else {
				boolean noderesult = false ; //exist true -> true
				
				for (SchedulerState child : s.next) { //check paths
					if (child.checked[fi]) { //loop
						if (child.results[fi]) {				
							noderesult = true ;
							break ;
						} else {				
						}
					} else {												
						if (n == -1) { //no superscript
							labelgraph(fi,child, -1);						
						} else {
							if (n > 1) {
								labelgraph(fi, child, n-1);									
							} else {//n == 0
								child.results[fi] = child.results[s1] ; //stop the checking	
							}
						}	
						if (child.results[fi]) {
							noderesult = true ;
							break ;
						}
					}					
				}
				s.results[fi] = noderesult ;				
			}
		}		
	}
	private void label_eg(int fi, SchedulerState s, int n) {
		//exists a path starting at s, always fi		
		if (s.checked[fi]) return ;
		byte s1 = formula.sf[fi][0] ;	//formula fi -> result by s1
		s.checked[fi] = true ;
			
		if (!s.checked[s1])
			labelgraph(s1, s, n) ;
		
		s.checked[fi] = true ;
		if (!s.results[s1]) {	//check node		
			s.results[fi] = false ;
		} else {								
			if (s.next.size() == 0) //empty
				s.results[fi] = true ; //= s.results[s1]
			else {
				boolean noderesult = false ; //exist true -> true
				for (SchedulerState child : s.next) { //check paths
					if (child.checked[fi]) { //loop
						if (child.results[s1]) {
							child.results[fi] = true ; //update
							noderesult = true ;
							break ;
						} else {							
						}
					} else {												
						if (n == -1) { //no superscript
							labelgraph(fi,child, -1);						
						} else {
							if (n > 1) {
								labelgraph(fi, child, n-1);									
							} else {//n == 0
								child.results[fi] = child.results[s1] ; //stop the checking	
							}
						}	
						if (child.results[fi]) {
							noderesult = true ;
							break ;
						}
					}					
				}
				s.results[fi] = noderesult ;			
			}
		}
	}
	
	private void label_au(int fi, SchedulerState s, int n) {
		//all path starting at s, s1 until s2
		if (s.checked[fi]) return ;
		byte s1 = formula.sf[fi][0] ;	//formula fi -> determined by s1, s2
		byte s2 = formula.sf[fi][1] ;		
		
		if (!s.checked[s1])
			labelgraph(s1, s, n) ;
		if (!s.checked[s2])
			labelgraph(s2, s, n) ;
		
		s.checked[fi] = true ;
		if (s.results[s2]) { //s2 = true -> ok
			s.results[fi] = true ;			
		} else { //s2 false
			if (!s.results[s1]) { //s1 false, s2 false
				s.results[fi] = false ;				
			} else { //s1 true, s2 false
				boolean noderesult = true ; //exist path false -> false
				for (SchedulerState child : s.next) { //check paths
					if (child.checked[fi]) { //loop 
						if (!child.results[s1] && !child.results[s2]) {							
							noderesult = false ;
							break ;
						} else {
							child.results[fi] = true ;
						}
					} else {
						if (n == -1) { //no superscript
							labelgraph(fi,child, -1);						
						} else { 
							if (n > 1) {
								labelgraph(fi, child, n-1);
							} else {//n == 0
								child.results[fi] = child.results[s1] ; //stop the checking
							}							
						}
					}
					if (!child.results[fi]) {						
						noderesult = false ;
						break ;
					}			
				}
				s.results[fi] = noderesult ;
			}
		}			
	}
	private void label_eu(int fi, SchedulerState s, int n) {
		//exist path starting at s, s1 until s2
		byte s1 = formula.sf[fi][0] ;	//formula fi -> determined by s1, s2
		byte s2 = formula.sf[fi][1] ;		
		
		if (!s.checked[s1])
			labelgraph(s1, s, n) ;
		if (!s.checked[s2])
			labelgraph(s1, s, n) ;
		
		s.checked[fi] = true ;
		if (s.results[s2]) { //s2 = true -> ok
			s.results[fi] = true ;			
		} else { //s2 false
			if (!s.results[s1]) { //s1 false, s2 false
				s.results[fi] = false ;				
			} else { //s1 true, s2 false
				boolean noderesult = false ; //exist path true -> true
				for (SchedulerState child : s.next) { //check paths
					if (child.checked[fi]) { //loop 
						if (!child.results[s1] && !child.results[s2]) {
							//false -> ignore
							child.results[fi] = false ;
						} else {
							child.results[fi] = true ;
							noderesult = true ;
							break ;
						} 
					} else {
						if (n == -1) { //no superscript
							labelgraph(fi,child, -1);						
						} else { 
							if (n > 1) {
								labelgraph(fi, child, n-1);
							} else {//n == 0								
								child.results[fi] = child.results[s1] ; //stop the checking
							}							
						}
					}
					if (child.results[fi]) {						
						noderesult = true ;
						break ;
					}			
				}
				s.results[fi] = noderesult ;
			}
		}
	}

/*
	protected void outputTrace(final String text, ArrayList<Transition> trace, PrintWriter out) throws IOException {					
		if (trace.size() == 0) {
//			out.println(". No trace");
//			System.out.println(". No trace");
		} else {
			out.println("------------------------------------------------");
			System.out.println("------------------------------------------------");
			out.println(text);
			System.out.println(text);
			for (int i=0; i<trace.size();i++) {
				out.println(i + ". " + trace.get(i));
				System.out.println(i + ". " + trace.get(i));
			}				
			out.println("------------------------------------------------");
			System.out.println("------------------------------------------------");
		}
	}
	protected void PrintTrace(boolean expectedValue, ArrayList<Transition> trace, SchedulerState s, int fi, int length, int n) {
		//PrintTrace(true,0,visitedStates, trace, schedulerstate, 0 (fi) , formula.length - 1, formula.sn[0]);
		int index;
		byte s1, s2 ;
		Transition trans ;
		
		if (s.results[fi] != expectedValue || !s.checked[fi]) {
			try {
				outputTrace(formula.toString(),trace,Log.out) ;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ;
		}
		
		switch (formula.opcode[fi]) {
			case "atomic" :
				try {
					outputTrace(formula.toString(),trace,Log.out) ;
				} catch (IOException e) {
					e.printStackTrace();
				}
				break ;
			
			case "not" :
				s1 = formula.sf[fi][0];											
				PrintTrace(!expectedValue, trace, s, s1, length, n); //do not care super script n	
				break ;
			
			case "implies" :
				s1 = formula.sf[fi][0];
				s2 = formula.sf[fi][1];
				
				if (expectedValue) { //true: F -> (T,F) & T -> T
					if (s.results[s1]) { //s1 true, must s.checked[s1]
						PrintTrace(true, trace, s, s2, length, n); //follow s2
					} else { //s1 false or true (don't care)
						PrintTrace(false,trace, s, s1, s2, n);
					}
				} else { //false: T(s1)->F(s2)
					PrintTrace(false, trace, s, s1, s2, n);
				}
				break ;
				
			case "or" :
				s1 = formula.sf[fi][0];
				s2 = formula.sf[fi][1];
				
				if (expectedValue) { //expected true					
					if (s.results[s1]) { //s1 true (checked)
						PrintTrace(true, trace, s, s1, s2, n);					
					} else { //s2 true
						PrintTrace(true, trace, s, s2, length, n); //follow s2
					}			
				} else { //expected false s1 & s2 -> false					
					if (formula.opcode[s1] == "atomic") {
						PrintTrace(false, trace, s, s2, length, -1) ;
					} else {
						if (formula.opcode[s2] == "atomic") {
							PrintTrace(false, trace, s, s1, s2, -1) ;
						} else {
							if (!visitedStates.contains(s)) {
								visitedStates.add(s) ;	//for first adding
							}
							index = 0 ;
							for  (SchedulerState child : s.next) {
								trace.add(s.trans.get(index)) ;
								if (child.checked[s1] && child.checked[s2]) {
									if (!visitedStates.contains(child)) {
										visitedStates.add(child);
										PrintTrace(expectedValue, trace, child, fi, length, -1) ;											
										//visitedStates.remove(visitedStates.size()-1) ;
									} else { //loop found -> print out
										try {
											outputTrace(formula.toString(),trace,Log.out) ;
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
									index ++ ;
									trace.remove(trace.size()-1) ;
								}
							}
						}
					}
				}
				break ;					
				
			case "AX" : //all (next node) children must be true
				s1 = formula.sf[fi][0];
				index = 0 ;
				for  (SchedulerState child : s.next) {
					trace.add(s.trans.get(index)) ;											
					PrintTrace(expectedValue,trace, child, s1, length, n); //do not care super script n
					trace.remove(trace.size()-1) ;									
					index ++ ;
				}
				break ;
			
				
				
			case "AF" : //all children future true (with supper script)				
				s1 = formula.sf[fi][0];
								
				if (s.results[s1] == expectedValue) { //ok current state has expectedValue				
					if (n == -1) { //no superscript
						PrintTrace(expectedValue, trace, s, fi, length, -1) ;					
					} else { 
						if (n > 1) {
							index = 0 ;
							for  (SchedulerState child : s.next) {
								trace.add(s.trans.get(index)) ;
								if (!visitedStates.contains(child)) {
									visitedStates.add(child);
									PrintTrace(expectedValue, trace, child, fi, length, n-1);
								}
								trace.remove(trace.size()-1) ;
								index++ ;
							}
						} else {//n == 0
							try {
								outputTrace(formula.toString(),trace,Log.out) ;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}							
					}
				} else { //consider next state
					if (!visitedStates.contains(s)) {
						visitedStates.add(s) ;	//for first adding
					}
					index = 0 ;
					for  (SchedulerState child : s.next) {
						trace.add(s.trans.get(index)) ;
						if (!visitedStates.contains(child)) {
							visitedStates.add(child);							
							if (n == -1) { //no superscript
								PrintTrace(expectedValue,trace, child, fi, length, -1) ;					
							} else { 
								if (n > 1) {
									PrintTrace(expectedValue,trace, child, fi, length, n-1);
								} else {//n == 0
									try {
										outputTrace(formula.toString(),trace,Log.out) ;
									} catch (IOException e) {
										e.printStackTrace();
									}
								}							
							}								
							//visitedStates.remove(visitedStates.size()-1) ;
						} else { //loop found -> print out
							try {
								outputTrace(formula.toString(),trace,Log.out) ;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						index ++ ;
						trace.remove(trace.size()-1) ;
					}
				}
				break ;
				
			case "AG" :				
				s1 = formula.sf[fi][0];
								
				//consider every state
				if (!visitedStates.contains(s)) {
					visitedStates.add(s) ;	//for first adding
				}
				index = 0 ;
				for  (SchedulerState child : s.next) {
					trace.add(s.trans.get(index)) ;
					if (!visitedStates.contains(child)) {
						visitedStates.add(child);						
						if (n == -1) { //no superscript
							PrintTrace(expectedValue,trace, child, fi, length, -1) ;					
						} else { 
							if (n > 1) {
								PrintTrace(expectedValue,trace, child, fi, length, n-1);
							} else {//n == 0
								try {
									outputTrace(formula.toString(),trace,Log.out) ;
								} catch (IOException e) {
									e.printStackTrace();
								}
							}							
						}							
						//visitedStates.remove(visitedStates.size()-1) ;
					} else { //already visited -> print out (detect a loop)
						try {
							outputTrace(formula.toString(),trace,Log.out) ;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					trace.remove(trace.size()-1) ;
					index ++ ;
				}				
				break ;
				
			case "EX" :		
				s1 = formula.sf[fi][0];
				index = 0 ;
				for  (SchedulerState child : s.next) {
					if (s.checked[fi] && s.results[fi]== expectedValue) {
						trace.add(s.trans.get(index)) ;											
						PrintTrace(expectedValue,trace, child, s1, length, n); //do not care super script n
						trace.remove(trace.size()-1) ;
//						break;
					}									
					index ++ ;
				}
				break ;				
				
			case "EF" :				
				s1 = formula.sf[fi][0];
				
				if (s.results[s1] == expectedValue) { //ok current state has expectedValue				
					if (n == -1) { //no superscript
						PrintTrace(expectedValue, trace, s, s1, length, -1) ;					
					} else { 
						if (n > 1) {
							PrintTrace(expectedValue, trace, s, s1, length, n-1);
						} else {//n == 0
							try {
								outputTrace(formula.toString(),trace,Log.out) ;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}							
					}
				} else { //consider next state
					if (!visitedStates.contains(s)) {
						visitedStates.add(s) ;	//for first adding
					}
					index = 0 ;
					for  (SchedulerState child : s.next) {
						if (child.checked[fi] && child.results[fi] == expectedValue) {
							trace.add(s.trans.get(index)) ;
							if (!visitedStates.contains(child)) {
								visitedStates.add(child);							
								if (n == -1) { //no superscript
									PrintTrace(expectedValue,trace, child, fi, length, -1) ;					
								} else { 
									if (n > 1) {
										PrintTrace(expectedValue,trace, child, fi, length, n-1);
									} else {//n == 0
										try {
											outputTrace(formula.toString(),trace,Log.out) ;
										} catch (IOException e) {
											e.printStackTrace();
										}
									}							
								}
							} else { //loop found -> print out
								try {
									outputTrace(formula.toString(),trace,Log.out) ;
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							trace.remove(trace.size()-1) ;
						}
						index ++ ;						
					}
				}
				break ;
				
				
				
			case "EG" :				
				s1 = formula.sf[fi][0];
				if (!visitedStates.contains(s)) {
					visitedStates.add(s) ;	//for first adding
				}
				index = 0 ;	
				for  (SchedulerState child : s.next) {
					if (child.checked[fi] && child.results[fi] == expectedValue) {
						trace.add(s.trans.get(index)) ;
						if (!visitedStates.contains(child)) {
							visitedStates.add(child);						
							if (n == -1) { //no superscript
								PrintTrace(expectedValue,trace, child, fi, length, -1) ;					
							} else { 
								if (n > 1) {
									PrintTrace(expectedValue,trace, child, fi, length, n-1);
								} else {//n == 0
									try {
										outputTrace(formula.toString(),trace,Log.out) ;
									} catch (IOException e) {
										e.printStackTrace();
									}
								}							
							}
						} else { //already visited -> print out (detect a loop)
							try {
								outputTrace(formula.toString(),trace,Log.out) ;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						trace.remove(trace.size()-1) ;
					}
					index ++ ;
				}				
				break ;
			
			case "AU" :				
				s1 = formula.sf[fi][0] ;	//formula fi -> determined by s1, s2
				s2 = formula.sf[fi][1] ;		
				if (!visitedStates.contains(s)) {
					visitedStates.add(s) ;	//for first adding
				}
				
				index = 0 ;
				for  (SchedulerState child : s.next) { //check all children					
					trace.add( s.trans.get(index) ) ;					
					if (! visitedStates.contains(child)) { //loop
						visitedStates.add(child) ;
						if (n == -1) { //no superscript
							PrintTrace(expectedValue,  trace, child, fi, length, -1) ;					
						} else { 
							if (n > 1) {
								PrintTrace(expectedValue,  trace, child, fi, length, n-1);
							} else {//n == 0
								try {
									outputTrace(formula.toString(),trace,Log.out) ;
								} catch (IOException e) {
									e.printStackTrace();
								}
							}							
						}
					} else {						
						try {
							outputTrace(formula.toString(),trace,Log.out) ;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}					
					trace.remove(trace.size()-1) ;
					index ++ ;
				}
				break ;
				
				
			case "EU" :
				if (s.results[fi] != expectedValue) return ;
				s1 = formula.sf[fi][0] ;	//formula fi -> determined by s1, s2
				s2 = formula.sf[fi][1] ;		
				
				if (s.results[s2]) { //s2 = true -> ok
					if (trace.size() > 0)
						try {
							outputTrace(formula.toString(),trace,Log.out) ;
						} catch (IOException e) {
							e.printStackTrace();
						}
					else
						s.print();
					break ;
				}
					
				if (s.next.size() == 0) { //no child
					if (trace.size() > 0)
						try {
							outputTrace(formula.toString(),trace,Log.out) ;
						} catch (IOException e) {
							e.printStackTrace();
						}
					else
						s.print();
				} else {
					index = 0 ;
					for  (SchedulerState child : s.next) { //check all children
						if (child.results[fi]) {
							trans = s.trans.get(index) ;
							if (visitedStates.contains(child)) { //loop
								try {
									outputTrace(formula.toString(),trace,Log.out) ;
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								trace.add(trans) ;	
								visitedStates.add(child) ;
								if (n == -1) { //no superscript
									PrintTrace(expectedValue,  trace, child, fi, length, -1) ;					
								} else { 
									if (n > 1) {
										PrintTrace(expectedValue,  trace, child, fi, length, n-1);
									} else {//n == 0
										if (trace.size() > 0)
											try {
												outputTrace(formula.toString(),trace,Log.out) ;
											} catch (IOException e) {
												e.printStackTrace();
											}
										else
											s.print();
									}							
								}
								trace.remove(trace.size()-1) ;
								//visitedStates.remove(visitedStates.size()-1) ;
							}
							break ;
						}
						index ++ ;
					}
				}
				break ;
			
		}
	}
*/


	protected void outputTrace(final String text, ArrayList<Transition> trace, PrintWriter out) throws IOException {					
		if (trace.size() == 0) {
			out.println(". No trace");
//			System.out.println(". No trace");
		} else {
			out.println("------------------------------------------------");
//			System.out.println("------------------------------------------------");
			out.println(text);
//			System.out.println(text);
			
			for (int i=0; i<trace.size();i++) {
				out.println(i + ". " + trace.get(i));
//				System.out.println(i + ". " + trace.get(i));
			}				
			out.println("------------------------------------------------");
//			System.out.println("------------------------------------------------");
		}
	}
	protected void PrintTrace(boolean expectedValue, ArrayList<Transition> trace, SchedulerState s, int fi, int length, int n) {
		//PrintTrace(true,0,visitedStates, trace, schedulerstate, 0 (fi) , formula.length - 1, formula.sn[0]);
		int index;
		byte s1, s2 ;
		Transition trans ;
		
		if (s.results[fi] != expectedValue) return ;
		if (!s.checked[fi]) {
			try {
				outputTrace(formula.toString(),trace,Log.out) ;
			} catch (IOException e) {
				e.printStackTrace();
			}
			return ;
		}
		
		switch (formula.opcode[fi]) {
			case "atomic" :
				try {
					outputTrace(formula.toString(),trace,Log.out) ;
				} catch (IOException e) {
					e.printStackTrace();
				}
				break ;
			
			case "not" :
				s1 = formula.sf[fi][0];											
				PrintTrace(!expectedValue, trace, s, s1, length, n); //do not care super script n	
				break ;
			
			case "implies" :
				s1 = formula.sf[fi][0];
				s2 = formula.sf[fi][1];
				
				if (expectedValue) { //true: F -> (T,F) & T -> T
					if (s.results[s1]) { //s1 true, must s.checked[s1]
						PrintTrace(true, trace, s, s2, length, n); //follow s2
					} else { //s1 false or true (don't care)
						PrintTrace(false,trace, s, s1, s2, n);
					}
				} else { //false: T(s1)->F(s2)
					PrintTrace(false, trace, s, s1, s2, n);
				}
				break ;
				
			case "or" :
				s1 = formula.sf[fi][0];
				s2 = formula.sf[fi][1];
				
				if (expectedValue) { //expected true					
					if (s.results[s1]) { //s1 true (checked)
						PrintTrace(true, trace, s, s1, s2, n);					
					} else { //s2 true
						PrintTrace(true, trace, s, s2, length, n); //follow s2
					}			
				} else { //expected false s1 & s2 -> false					
					if (formula.opcode[s1] == "atomic") {
						PrintTrace(false, trace, s, s2, length, -1) ;
					} else {
						if (formula.opcode[s2] == "atomic") {
							PrintTrace(false, trace, s, s1, s2, -1) ;
						} else {
							if (!visitedStates.contains(s)) {
								visitedStates.add(s) ;	//for first adding
							}
							index = 0 ;
							for  (SchedulerState child : s.next) {
								trace.add(s.trans.get(index)) ;
								if (child.checked[s1] && child.checked[s2]) {
									if (!visitedStates.contains(child)) {
										visitedStates.add(child);
										PrintTrace(expectedValue, trace, child, fi, length, -1) ;											
										//visitedStates.remove(visitedStates.size()-1) ;
									} else { //loop found -> print out
										try {
											outputTrace(formula.toString(),trace,Log.out) ;
										} catch (IOException e) {
											e.printStackTrace();
										}
									}
									index ++ ;
									trace.remove(trace.size()-1) ;
								}
							}
						}
					}
				}
				break ;					
				
			case "AX" : //all (next node) children must be true
				s1 = formula.sf[fi][0];
				index = 0 ;
				for  (SchedulerState child : s.next) {
					trace.add(s.trans.get(index)) ;											
					PrintTrace(expectedValue,trace, child, s1, length, n); //do not care super script n
					trace.remove(trace.size()-1) ;									
					index ++ ;
				}
				break ;
			
				
				
			case "AF" : //all children future true (with supper script)				
				s1 = formula.sf[fi][0];
								
				if (s.results[s1] == expectedValue) { //ok current state has expectedValue				
					if (n == -1) { //no superscript
						PrintTrace(expectedValue, trace, s, s1, length, -1) ;					
					} else { 
						if (n > 1) {
							PrintTrace(expectedValue, trace, s, s1, length, n-1);
						} else {//n == 0
							try {
								outputTrace(formula.toString(),trace,Log.out) ;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}							
					}
				} else { //consider next state
					if (!visitedStates.contains(s)) {
						visitedStates.add(s) ;	//for first adding
					}
					index = 0 ;
					for  (SchedulerState child : s.next) {
						trace.add(s.trans.get(index)) ;
						if (!visitedStates.contains(child)) {
							visitedStates.add(child);							
							if (n == -1) { //no superscript
								PrintTrace(expectedValue,trace, child, fi, length, -1) ;					
							} else { 
								if (n > 1) {
									PrintTrace(expectedValue,trace, child, fi, length, n-1);
								} else {//n == 0
									try {
										outputTrace(formula.toString(),trace,Log.out) ;
									} catch (IOException e) {
										e.printStackTrace();
									}
								}							
							}								
							//visitedStates.remove(visitedStates.size()-1) ;
						} else { //loop found -> print out
							try {
								outputTrace(formula.toString(),trace,Log.out) ;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						index ++ ;
						trace.remove(trace.size()-1) ;
					}
				}
				break ;
				
			case "AG" :				
				s1 = formula.sf[fi][0];
								
				//consider every state
				if (!visitedStates.contains(s)) {
					visitedStates.add(s) ;	//for first adding
				}
				index = 0 ;
				for  (SchedulerState child : s.next) {
					trace.add(s.trans.get(index)) ;
					if (!visitedStates.contains(child)) {
						visitedStates.add(child);						
						if (n == -1) { //no superscript
							PrintTrace(expectedValue,trace, child, fi, length, -1) ;					
						} else { 
							if (n > 1) {
								PrintTrace(expectedValue,trace, child, fi, length, n-1);
							} else {//n == 0
								try {
									outputTrace(formula.toString(),trace,Log.out) ;
								} catch (IOException e) {
									e.printStackTrace();
								}
							}							
						}							
						//visitedStates.remove(visitedStates.size()-1) ;
					} else { //already visited -> print out (detect a loop)
						try {
							outputTrace(formula.toString(),trace,Log.out) ;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
					trace.remove(trace.size()-1) ;
					index ++ ;
				}				
				break ;
				
			case "EX" :		
				s1 = formula.sf[fi][0];
				index = 0 ;
				for  (SchedulerState child : s.next) {
					if (s.checked[fi] && s.results[fi]== expectedValue) {
						trace.add(s.trans.get(index)) ;											
						PrintTrace(expectedValue,trace, child, s1, length, n); //do not care super script n
						trace.remove(trace.size()-1) ;
//						break;
					}									
					index ++ ;
				}
				break ;				
				
			case "EF" :				
				s1 = formula.sf[fi][0];
				
				if (s.results[s1] == expectedValue) { //ok current state has expectedValue				
					if (n == -1) { //no superscript
						PrintTrace(expectedValue, trace, s, s1, length, -1) ;					
					} else { 
						if (n > 1) {
							PrintTrace(expectedValue, trace, s, s1, length, n-1);
						} else {//n == 0
							try {
								outputTrace(formula.toString(),trace,Log.out) ;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}							
					}
				} else { //consider next state
					if (!visitedStates.contains(s)) {
						visitedStates.add(s) ;	//for first adding
					}
					index = 0 ;
					for  (SchedulerState child : s.next) {
						if (child.checked[fi] && child.results[fi] == expectedValue) {
							trace.add(s.trans.get(index)) ;
							if (!visitedStates.contains(child)) {
								visitedStates.add(child);							
								if (n == -1) { //no superscript
									PrintTrace(expectedValue,trace, child, fi, length, -1) ;					
								} else { 
									if (n > 1) {
										PrintTrace(expectedValue,trace, child, fi, length, n-1);
									} else {//n == 0
										try {
											outputTrace(formula.toString(),trace,Log.out) ;
										} catch (IOException e) {
											e.printStackTrace();
										}
									}							
								}
							} else { //loop found -> print out
								try {
									outputTrace(formula.toString(),trace,Log.out) ;
								} catch (IOException e) {
									e.printStackTrace();
								}
							}
							trace.remove(trace.size()-1) ;
						}
						index ++ ;						
					}
				}
				break ;
				
				
				
			case "EG" :				
				s1 = formula.sf[fi][0];
				if (!visitedStates.contains(s)) {
					visitedStates.add(s) ;	//for first adding
				}
				index = 0 ;	
				for  (SchedulerState child : s.next) {
					if (child.checked[fi] && child.results[fi] == expectedValue) {
						trace.add(s.trans.get(index)) ;
						if (!visitedStates.contains(child)) {
							visitedStates.add(child);						
							if (n == -1) { //no superscript
								PrintTrace(expectedValue,trace, child, fi, length, -1) ;					
							} else { 
								if (n > 1) {
									PrintTrace(expectedValue,trace, child, fi, length, n-1);
								} else {//n == 0
									try {
										outputTrace(formula.toString(),trace,Log.out) ;
									} catch (IOException e) {
										e.printStackTrace();
									}
								}							
							}
						} else { //already visited -> print out (detect a loop)
							try {
								outputTrace(formula.toString(),trace,Log.out) ;
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
						trace.remove(trace.size()-1) ;
					}
					index ++ ;
				}				
				break ;
			
			case "AU" :				
				s1 = formula.sf[fi][0] ;	//formula fi -> determined by s1, s2
				s2 = formula.sf[fi][1] ;		
				if (!visitedStates.contains(s)) {
					visitedStates.add(s) ;	//for first adding
				}
				
				index = 0 ;
				for  (SchedulerState child : s.next) { //check all children					
					trace.add( s.trans.get(index) ) ;					
					if (! visitedStates.contains(child)) { //loop
						visitedStates.add(child) ;
						if (n == -1) { //no superscript
							PrintTrace(expectedValue,  trace, child, fi, length, -1) ;					
						} else { 
							if (n > 1) {
								PrintTrace(expectedValue,  trace, child, fi, length, n-1);
							} else {//n == 0
								try {
									outputTrace(formula.toString(),trace,Log.out) ;
								} catch (IOException e) {
									e.printStackTrace();
								}
							}							
						}
					} else {						
						try {
							outputTrace(formula.toString(),trace,Log.out) ;
						} catch (IOException e) {
							e.printStackTrace();
						}
					}					
					trace.remove(trace.size()-1) ;
					index ++ ;
				}
				break ;
				
				
			case "EU" :
				if (s.results[fi] != expectedValue) return ;
				s1 = formula.sf[fi][0] ;	//formula fi -> determined by s1, s2
				s2 = formula.sf[fi][1] ;		
				
				if (s.results[s2]) { //s2 = true -> ok
					if (trace.size() > 0)
						try {
							outputTrace(formula.toString(),trace,Log.out) ;
						} catch (IOException e) {
							e.printStackTrace();
						}
					else
						s.print();
					break ;
				}
					
				if (s.next.size() == 0) { //no child
					if (trace.size() > 0)
						try {
							outputTrace(formula.toString(),trace,Log.out) ;
						} catch (IOException e) {
							e.printStackTrace();
						}
					else
						s.print();
				} else {
					index = 0 ;
					for  (SchedulerState child : s.next) { //check all children
						if (child.results[fi]) {
							trans = s.trans.get(index) ;
							if (visitedStates.contains(child)) { //loop
								try {
									outputTrace(formula.toString(),trace,Log.out) ;
								} catch (IOException e) {
									e.printStackTrace();
								}
							} else {
								trace.add(trans) ;	
								visitedStates.add(child) ;
								if (n == -1) { //no superscript
									PrintTrace(expectedValue,  trace, child, fi, length, -1) ;					
								} else { 
									if (n > 1) {
										PrintTrace(expectedValue,  trace, child, fi, length, n-1);
									} else {//n == 0
										if (trace.size() > 0)
											try {
												outputTrace(formula.toString(),trace,Log.out) ;
											} catch (IOException e) {
												e.printStackTrace();
											}
										else
											s.print();
									}							
								}
								trace.remove(trace.size()-1) ;
								//visitedStates.remove(visitedStates.size()-1) ;
							}
							break ;
						}
						index ++ ;
					}
				}
				break ;
			
		}
	}
}

