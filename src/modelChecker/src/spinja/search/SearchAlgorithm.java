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

package spinja.search;

import static spinja.search.Message.DEADLOCK;
import static spinja.search.Message.DUPLICATE_STATE;
import static spinja.search.Message.EXCEED_DEPTH_ERROR;
import static spinja.search.Message.EXCEED_DEPTH_WARNING;
import static spinja.search.Message.NO_MORE_TRANSITIONS;
import static spinja.search.Message.TRANS_ERROR;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryNotificationInfo;
import java.util.Arrays;

import javax.management.Notification;
import javax.management.NotificationEmitter;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;

import spinja.exceptions.SpinJaException;
import spinja.model.Condition;
import spinja.model.Model;
import spinja.model.Transition;
import spinja.store.StateStore;
import spinja.store.hash.HashAlgorithm;
import spinja.util.ByteArrayStorage;
import spinja.util.Log;
//import sspinja.Gen;

/**
 * A search algorithm is algorithm that systematically tries all the 
 * possible options to execute the model in a smart way. 
 * The basic implementation of a search algorithm is a Depth First Search, 
 * but basically it can be anything. Even a random search is permitted.
 * 
 * @author Marc de Jonge
 */
public abstract class SearchAlgorithm<M extends Model<T>, 
									  T extends Transition> extends Algoritm
	implements NotificationListener, NotificationFilter {

	private static final long serialVersionUID = -9221923281786933752L;
	
	public static boolean print = false ;

	protected final M model;

	protected final StateStore store;

	protected final boolean checkForDeadlocks;

	protected final int maxErrors;

	protected int nrErrors;

	protected int atomicSteps, statesMatched;

	protected volatile boolean outOfMemory;

	protected int maxSize, maxDepth;

	protected final boolean errorExceedDepth;

	protected boolean printedDepthWarning;

	private long lastStates = 0;
	
	protected HashAlgorithm hash;

	protected final TransitionCalculator<M, T> nextTransition;
//	private int counter=-1;
	/**
	 * Initialized this search algorithm
	 * 
	 * @param model
	 * @param store
	 * @param checkForDeadlocks
	 * @param maxErrors
	 * @param errorExceedDepth
	 * @param nextTransition
	 */
	public SearchAlgorithm(final M model, final StateStore store, final boolean checkForDeadlocks,
		final int maxErrors, final boolean errorExceedDepth,
		final TransitionCalculator<M, T> nextTransition) {
		this.model = model;
		this.store = store;
		this.checkForDeadlocks = checkForDeadlocks;
		this.maxErrors = maxErrors == 0 ? Integer.MAX_VALUE : maxErrors;
		this.errorExceedDepth = errorExceedDepth;
		this.nextTransition = nextTransition;
		printedDepthWarning = false;
		nrErrors = 0;
		maxSize = 0;
		hash = HashAlgorithm.getDefaultAlgorithm();

		((NotificationEmitter) ManagementFactory.getMemoryMXBean()).addNotificationListener(this,
			this, null);
	}

	protected abstract boolean addState(byte[] state, int identifier);

	protected abstract boolean checkModelState();

	public int getAtomicSteps() {
		return atomicSteps;
	}

	/**
	 * @return The number of bytes that it has taken to store the search algorithm. This should also
	 *         contain the memory that is needed to store intermediate states and the stack.
	 */
	@Override
	public long getBytes() {
		return 26 + store.getBytes();
	}

	public abstract int getDepth();

	public int getMaxDepth() {
		return maxDepth;
	}

	public int getMaxSize() {
		return maxSize;
	}

	public int getNrErrors() {
		return nrErrors;
	}

	/**
	 * @see spinja.search.Algoritm#getNrStates()
	 */
	@Override
	public long getNrStates() {
		return store.getStored();
	}

	public int getStatesMatched() {
		return statesMatched;
	}

	/**
	 * @see javax.management.NotificationListener#handleNotification(javax.management.Notification,
	 *      java.lang.Object)
	 */
	public void handleNotification(final Notification notification, final Object handback) {
		printInfo();
		if (lastStates + (lastStates >> 7) >= store.getStored()) {
			outOfMemory = true;
		}
		lastStates = store.getStored();
	}

	protected abstract T getLastTransition();

	/**
	 * @see javax.management.NotificationFilter#isNotificationEnabled(javax.management.Notification)
	 */
	public boolean isNotificationEnabled(final Notification notification) {
		return notification.getType() == MemoryNotificationInfo.MEMORY_COLLECTION_THRESHOLD_EXCEEDED;
	}

	protected abstract Transition nextTransition();

	protected abstract void outputTrace(String name);

	protected boolean print(final String msg) {
		System.out.println(msg);
		return true;
	}

	/**
	 * Prints intermediate information
	 */
	public void printInfo() {
		final double memory = getBytes() / (1024.0 * 1024.0);
		System.out.printf(
			"Depth= %7d States= %4.3e Transitions= %4.3e Memory= %-6.3f Errors= %7d\n",
			maxDepth, (double) store.getStored(), (double) store.getStored() + statesMatched,
			memory, nrErrors);
	}

	/**
	 * Prints a summary of what is done. This can be information like how many states were explored
	 * or how many times it matched a state.
	 */
	@Override
	public void printSummary() {
		System.out.println("");
		System.out.println("State-vector " + maxSize + " byte, depth reached " + maxDepth
							+ ", errors: " + nrErrors);
		System.out.printf("%8d states, stored\n", store.getStored());
		System.out.printf("%8d states, matched\n", statesMatched);
		System.out.printf("%8d transitions (= stored+matched)\n", store.getStored() + statesMatched);
		System.out.printf("%8d atomic steps\n", atomicSteps);
		store.printSummary();
	}

	public void printSummary(PrintWriter out) {
		out.println("");
		out.println("State-vector " + maxSize + " byte, depth reached " + maxDepth
							+ ", errors: " + nrErrors);
		out.printf("%8d states, stored\n", store.getStored());
		out.printf("%8d states, matched\n", statesMatched);
		out.printf("%8d transitions (= stored+matched)\n", store.getStored() + statesMatched);
		out.printf("%8d atomic steps\n", atomicSteps);
	}
	
	protected void report(final Message msg) {
		report(msg, msg.getDefaultMessage());
	}	

	/**
	 * Reports a message with the corresponding text to the user, if necessary. Of all the errors
	 * that are found, only the first is printed and of for that one the
	 * {@link #outputTrace(String)} function is called. For each warning, the warning is printed.
	 * 
	 * @param msg
	 *            The type of message that is send.
	 * @param text
	 *            The text that is send with that message.
	 */
	protected final void report(final Message msg, final String text) {
		if (msg.isError()) {
			if (nrErrors < 1) {
				System.out.println("sspinja error: " + text + " (at depth " + getDepth() + ")");
				if (maxErrors == 1) {
					outputTrace(model.getName());
				}
			}
			nrErrors++;
		} else if (msg.isWarning()) {
			System.out.println("spinja warning: " + text + " (at depth " + getDepth() + ")");
		} else {
//			assert print("  " + text);
		}
	}

	protected abstract boolean restoreState();

	/**
	 * Executes the search algorithm. This function should do everything.
	 */
	int cnt = 0 ;
	public static String code = "";
	@Override
	public void execute() {
		byte[] state = storeModel();
//		int counter=-1;
		int identifier;
		if (model.conditionHolds(Condition.SHOULD_STORE_STATE)) {
			identifier = store.addState(state);
			if (identifier >= 0) {
//				assert print("  New state " + (store.getStored() - 1));
			}
		} else { // otherwise count it
			identifier = hash.hash(state, 0);
			atomicSteps++;
		}
		if (!addState(state, identifier)) {
			throw new RuntimeException("Could not even add the first state.");
		}

		
		// While there are still more states to explore
		// or the maximum number of errors was not yet found
		while ((nrErrors < maxErrors) && !Thread.currentThread().isInterrupted() && restoreState()) {
			if (outOfMemory) { // If there is too little memory left
				throw new OutOfMemoryError(); // throw a java error
			}
			// Make sure that the state matches the model
			assert checkModelState();

//			System.out.println("model: " + model);
			// Remember the largest state size and the largest depth that is
			// reached
			if (state.length > maxSize) {
				maxSize = state.length;
			}

			if (getDepth() > maxDepth) {
				maxDepth = getDepth();
			}			

			final Transition next = nextTransition();
//			if (cnt >= 1840 && cnt <= 1843) {
//				print = true ;
//			} else print = false ;
			
			
//			if (cnt > 1082) {
//				System.out.println("\n----------------------------------------");
//				System.out.println(model);
//			}
//			System.out.println("----------------------------------------");
//				System.out.println(model);
//			}
//			System.out.println(cnt++ + " Next: " + next);
			
			
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
//				if(model.conditionHolds(Condition.ACCEPT_STATE)){
//					System.out.println( "$$$$$$$$$No Transaction$$$$$$$$$ACCEPT_STATE:"+Arrays.hashCode(storeModel())+"###"+getDepth());
//				}else{
//					System.out.println(getDepth()+"$$$$$$$$$No Transaction$$$$$$$$$:NO_ACCEPT_STATE"+Arrays.hashCode(storeModel())+"##"+next);
//				}
//				System.out.println("- State done");
				
				stateDone();
				storeModel();
				continue;
			}
			
			try { // Take the next transition and check for errors
//				System.out.println( counter+"#########From:#################"+Arrays.hashCode(state)+"\t"+model.conditionHolds(Condition.ACCEPT_STATE));
//				if(model.conditionHolds(Condition.ACCEPT_STATE)){
//					System.out.println( "#########From############Accep State:"+Arrays.hashCode(storeModel())+"###"+getDepth());
//
//				}else{
//					System.out.println( "#########From############NO Accept State:"+Arrays.hashCode(storeModel())+"###"+getDepth());
//				}
				takeTransition(next);
//				takeTransition(next, cnt);
//				System.out.println("- Take: " + next);
			} catch (final SpinJaException ex) {
				report(TRANS_ERROR, ex.getMessage());
				ex.printStackTrace(); 
				try {
					outputTest(ex.getMessage(), Log.out);					
				} catch (IOException e) {
					e.printStackTrace();
				}
				continue;
			}

			state = storeModel();
			
//			restoreModel(state);
//			storeModel();
//			System.out.println( "#########TO###################:"+Arrays.hashCode(state)+"\t"+model.conditionHolds(Condition.ACCEPT_STATE)+"\t###"+model.toString()+"\t##"+next);
			// If the state should be stored, try to store it
			if (model.conditionHolds(Condition.SHOULD_STORE_STATE)) {
				int oldID = identifier ;
				identifier = store.addState(state);
				System.out.println("----------------------------------------");
				if (identifier < 0) {
					System.out.println(oldID + " -> " + -(identifier + 1) + " (visited)");					
					System.out.println(" - Action: " + next);
//					System.out.println(" - Code: " + (Gen.code == "" ? "<Empty>" : Gen.code));
//					Gen.code = "" ; //reset
					report(DUPLICATE_STATE.withState(state));
					nextTransition.duplicateState(model, getLastTransition(), state, -(identifier + 1), getSearchableStack());
					statesMatched++;
					
					undoTransition();
					continue;
				} else {
					System.out.println(oldID + " -> " + identifier);
					System.out.println(" - Action: " + next);
//					System.out.println(" - Code: " + (Gen.code == "" ? "<Empty>" : Gen.code));
//					Gen.code = "" ; //reset
					if ((store.getStored() & 0xfffff) == 0) {
						printInfo();
					}
//					assert print("  New state " + (store.getStored() - 1));
//					assert print(Arrays.toString(state));
//					assert print(model.toString());
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
	}

	protected abstract void stateDone();

	protected final ByteArrayStorage storage = new ByteArrayStorage();

	protected byte[] storeModel() {
		storage.init(model.getSize());
		model.encode(storage);
		return storage.getBuffer();
	}
	protected void restoreModel(byte[] byteArr) {
		storage.setBuffer(byteArr);
		model.decode(storage);
	}

	protected abstract void takeTransition(Transition next) throws SpinJaException;
	
	protected void takeTransition(Transition next, int id) throws SpinJaException {};

	protected abstract void undoTransition();

	public abstract SearchableStack getSearchableStack();

	protected void outputTest(String text, PrintWriter out) throws IOException {
		// TODO Auto-generated method stub
		
	}

	protected void print_trace() {
		// TODO Auto-generated method stub
		
	}
}
