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

package spinja.promela.compiler.automaton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import spinja.promela.compiler.Proctype;
import spinja.promela.compiler.actions.Action;
import spinja.promela.compiler.actions.AssertAction;
import spinja.promela.compiler.actions.ExprAction;
import spinja.promela.compiler.actions.Sequence;
import spinja.promela.compiler.expression.BooleanExpression;
import spinja.promela.compiler.expression.CompareExpression;
import spinja.promela.compiler.expression.Expression;
import spinja.promela.compiler.parser.ParseException;
import spinja.util.StringWriter;
import spinja.util.UnModifiableIterator;

/**
 * An Automaton is a simple LTS that holds {@link State}s and {@link Transition}s. It can be used
 * to generate the Transition table for each {@link Proctype}.
 * 
 * @author Marc de Jonge
 */
public class Automaton implements Iterable<State> {
	private State startState;

	private final Proctype proctype;

	public static HashMap<String, Integer> varList = new HashMap<String, Integer>() ;
	/**
	 * Constructor of Automaton for a specified {@link Proctype}.
	 * 
	 * @param proctype
	 *            The {@link Proctype} to which this {@link Automaton} belongs.
	 */
	public Automaton(final Proctype proctype) {
		this.proctype = proctype;
		startState = new State(this, false);
	}
	
	public void generateInitProcessForScheduler(final StringWriter w) throws ParseException {
		assert (w != null);		
		String text = "";
		for (final State state : this) {			
			for (Transition trans : state.output) {	
				if (trans instanceof EndTransition)
					break ;
				if (trans instanceof ActionTransition)
					text = trans.getText();				
					if (text.contains("run")) {
						trans.printRunTransition(w); //addProcess(new P(??)) with parameters!
					} else	{
						if (text.contains("execute")) 
							w.appendLine(text.replace("execute (", "addProcess(new ") + ";");
						else
							w.appendLine(trans.getText() + ";"); //for only assignments of variables
					} 
			}
		}
	}
	
	/**
	 * Generates the transition table in java code.
	 * @param w
	 *            The {@link StringWriter} that can be used to write the data to.
	 * @throws ParseException
	 *             When something went wrong while converting the input.
	 */
	public void generateTable(final StringWriter w) throws ParseException {
		assert (w != null);
		w.appendLine("PromelaTransitionFactory factory;");
		for (final State state : this) {
			w.setSavePoint();
			int cnt = 0;
			for (Transition trans : state.output) {
				if (cnt == 0) {
					w.appendLine("factory = ");
				} else {
					w.appendLine("factory.append(");
				}
				w.indent();
				trans.printTransition(w);
				w.outdent();
				w.removePostfix().append(cnt == 0 ? ";" : ");").appendPostfix();
				cnt++;
			}
			if (cnt == 0) {
				w.appendLine("factory = null;");
			}
			w.appendLine("_stateTable[", state.getStateId(), "] = new State(",
				getProctype().getName(), ".this, factory, ", state.isEndingState(), ", ",
				state.isProgressState(), ", ", state.isAcceptState(), ");");
			w.appendLine();
		}
	}
	
	/**
	 * Check the affect of variable to error state
	 */
	public boolean varErrorAffect(String varName){
		for (final State state : this) {
			for (Transition trans : state.output) {
				if (trans.getVarErrorAffect(varName))
					return true;
			}
		}
		return false ;
	}
	
	/**
	 * Get transition in automaton to check
	 * Ex. the distance (for useless transition)
	 */
	public ArrayList<Transition> getAllTransiton() {
		ArrayList<Transition> alltrans = new ArrayList<Transition>() ;
		for (final State state : this) {			
			for (Transition trans : state.output) {
				alltrans.add(trans) ;		
			}			
		}
		if (alltrans.size() == 0)
			return null ;
		return alltrans;
	}
	
	public void setTransitionUselessWeight() {
		for (final State state : this) {
			int transCount = 0 ;
			for (Transition trans : state.output) {
				transCount ++ ;	
				if (trans instanceof ActionTransition) {
					Sequence seq = ((ActionTransition) trans).getSequence() ;
					for (int i =0 ; i< seq.getNrActions(); i++) {
						Action act = seq.getAction(i);
						if (act.getUselessWeight() == 1) { //useful
							trans.uselessWeight = 1 ;
							break ;
						}	
					}
				}				
			}
			if (transCount > 1) {
				for (Transition trans : state.output) {
					if (trans.uselessWeight == 2) {
						trans.uselessWeight = 0 ; //true useless
					}
				}
			} else 
				for (Transition trans : state.output) {
					trans.uselessWeight = 1 ;
				}
		}
	}
	
	/*
	 * get error variable in automaton
	 * need to call this function for all automaton to get all of error variables before set the useless weight 
	 */
	public void getVariables() {
		for (final State state : this) {
			for (Transition trans : state.output) {	
				if (trans instanceof ActionTransition) {
					Sequence seq = ((ActionTransition) trans).getSequence() ;
					for (int i =0 ; i< seq.getNrActions(); i++) {
						Action act = seq.getAction(i);
						//get error variable list
						if (act instanceof AssertAction) {
							Expression expr = ((AssertAction) act).getExpr() ;
							if ((expr instanceof BooleanExpression) || (expr instanceof CompareExpression)) {														
								expr.getErrorVariableList() ;							
							}
						}
					}
				}					
			}
		}
	}
	
	/**
	 * Get all assertions from automaton for heuristic search!
	 */
	public ArrayList<Transition> getAssertTransition() {
		ArrayList<Transition> alltrans = new ArrayList<Transition>() ;
		for (final State state : this) {
			for (Transition trans : state.output) {
				int actCount = trans.getActionCount() ;				
				for (int i = 0 ; i< actCount ; i ++ ) {
					Action act = trans.getAction(i) ;
					if (act instanceof AssertAction) {
						alltrans.add(trans) ;
						break ;
					}
				}		
			}
		}
		if (alltrans.size() == 0)
			return null ;
		return alltrans;
	}
	
	/**
	 * Generate error functions from error state
	 */
	public void generateError(final StringWriter w) throws ParseException {
		assert (w != null);		
		for (final State state : this) {
			w.setSavePoint();
			//int cnt = 0;
			for (Transition trans : state.output) {
				if (trans instanceof ActionTransition) {
					Sequence seq = ((ActionTransition) trans).getSequence() ;
					Action act = seq.getAction(0);
					if (act instanceof ExprAction) {
						Expression expr = ((ExprAction) act).getExpression() ;
						if ((expr instanceof BooleanExpression) || (expr instanceof CompareExpression)) {						
							w.appendLine("public int getErrorDistance(){").indent();
							w.appendLine("int result = 0 ;") ;
							w.appendLine("result = " + expr.getDistanceExpression() + ";") ;
							w.appendLine("return result ;") ;							
							w.outdent().appendLine("}").appendLine();
							
							w.appendLine("public boolean checkError(){").indent();							
							w.appendLine("return " + expr.getBoolExpression() + ";") ;														
							w.outdent().appendLine("}").appendLine();
							
							w.appendLine("public String errorState(){").indent();							
							w.appendLine("return \"" + expr.getBoolExpression() + "\";") ;														
							w.outdent().appendLine("}").appendLine();
						}
					}
				}								
			}			
			w.appendLine();
		}
	}
	
//	/**
//	 * get all variables appear in assertion
//	 */
//	public void getErrorVariableList(){		
//		for (final State state : this) {			
//			for (Transition trans : state.output) {
//				if (trans instanceof ActionTransition) {
//					Sequence seq = ((ActionTransition) trans).getSequence() ;
//					Action act = seq.getAction(0);
//					if (act instanceof ExprAction) {
//						Expression expr = ((ExprAction) act).getExpression() ;
//						if ((expr instanceof BooleanExpression) || (expr instanceof CompareExpression)) {														
//							expr.getVariableList();							
//						}
//					}
//				}								
//			}			
//		}
//	}

	/**
	 * @return The {@link Proctype} to which this {@link Automaton} belongs.
	 */
	public Proctype getProctype() {
		return proctype;
	}

	/**
	 * @return The current starting state of this {@link Automaton}.
	 */
	public State getStartState() {
		return startState;
	}

	/**
	 * Sets the new starting state to the given state.
	 * @param startState
	 *            The new starting state.
	 */
	public void setStartState(State startState) {
		if (startState.getAutomaton() != this) {
			throw new IllegalArgumentException("The state must belong to this automaton!");
		}
		this.startState = startState;
	}

	/**
	 * @return True when one of the states that is held by this {@link Automaton} uses the atomic
	 *         token.
	 */
	public boolean hasAtomic() {
		for (final State s : this) {
			if (s.isInAtomic()) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return a new Iterator that can be used to go over all States.
	 * 
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<State> iterator() {
		return new UnModifiableIterator<State>() {

		    private Iterator<State> it;

            public boolean hasNext() { return it.hasNext(); }
            public State next()      { return it.next(); }

			public void init() {
			    List<State> list = new ArrayList<State>();
			    Stack<State> stack = new Stack<State>();
	            Set<State> states = new HashSet<State>(Collections.singleton(startState));

			    stack.push(startState);
				while (!stack.isEmpty()) {
					State next = stack.pop();
					list.add(next);
    				for (final Transition out : next.output) {
                        if (out.getTo() != null && states.add(out.getTo())) {
                            stack.push(out.getTo());
    					}
    				}
				}

				states = null;
				stack = null;
				it = list.listIterator();
			}
		};
	}

	/**
	 * @return The number of states that are reachable from the current starting state.
	 */
	public int size() {
		int cnt = 0;
		for (@SuppressWarnings("unused")
		final State state : this) {
			cnt++;
		}
		return cnt;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		final StringWriter w = new StringWriter();
		w.appendLine("Graph for proctype ", proctype);
		w.indent();
		for (final State state : this) {
			w.appendLine(state);
		}
		return w.toString();
	}
}
