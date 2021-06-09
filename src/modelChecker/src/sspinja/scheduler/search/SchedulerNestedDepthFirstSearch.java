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

import java.util.Arrays;

import spinja.exceptions.SpinJaException;
import spinja.exceptions.ValidationException;
import spinja.model.Model;
import spinja.model.Transition;
import spinja.search.TransitionCalculator;
import spinja.store.StateStore;
import spinja.util.ByteArrayStorage;
import spinja.util.DummyStdOut;

public abstract class SchedulerNestedDepthFirstSearch<M extends Model<T>, T extends Transition> extends SchedulerDepthFirstSearch<M, T> {
	
	private final Transition enterNestedSearch = new Transition() {
		@Override
		public int getId() {
			return Integer.MAX_VALUE;
		}
     
		@Override
		public void take() throws ValidationException {
			toggle = true;
			seed = storeModel();
		}

		@Override
		public String toString() {
			return "Enter nested search";
		}
		
		@Override
		public void undo() {
			toggle = false;
			seed = null;
		}		
	};

	private static final long serialVersionUID = -6333219782324980671L;

	private boolean toggle;

	private byte[] seed;

	public SchedulerNestedDepthFirstSearch(final M model, final StateStore store, final int stackSize,
		final boolean errorExceedDepth, final boolean checkForDeadlocks, final int maxErrors,
		final TransitionCalculator<M, T> nextTransition) {
		super(model, store, stackSize, errorExceedDepth, checkForDeadlocks, maxErrors,	nextTransition);
		toggle = false;
		seed = null;
	}

	

	protected abstract boolean conditionHolds();
	
	protected abstract String getDescription();
	
	@Override
	protected Transition nextTransition() {
		final Transition last = stack.getLastTransition();

		if (last == enterNestedSearch) {
			return super.nextTransition(); //just start 2nd search, get next transition last == null
		} else {
			T next = (T) super.nextTransition(last) ;
			
			if (next == null && last != null && !toggle && conditionHolds()) {
				//enter nest search only when a cycle founded (duplicate state)
				return enterNestedSearch;
			}
			return next;
		}
	}


	@Override
	protected Transition nextTransition(Transition last) {		
		return nextTransition();
	}
	
		
	@Override
	protected byte[] storeModel() {
		storage.init(model.getSize() + 1);
		storage.writeBool(toggle);
		model.encode(storage);
		return storage.getBuffer();
	}
	
	protected void restoreModel() {
		byte [] state = stack.getTop();			
		ByteArrayStorage reader = new ByteArrayStorage();
		reader.setBuffer(state);
		toggle = reader.readBool();
		model.decode(reader);	
	}
	
	@Override
	protected void takeTransition(Transition next) throws SpinJaException {
		stack.print();
		System.out.println(model);
		super.takeTransition(next);
		if (toggle && conditionHolds()) {
			byte[] curr = storeModel();
			if (Arrays.equals(curr, seed)) {
				throw new ValidationException(getDescription() + " detected");
			}

			curr[0] = 0;
			if (stack.containsState(curr)) {
				throw new ValidationException(getDescription() + " detected");
			}
		}
	}
}
