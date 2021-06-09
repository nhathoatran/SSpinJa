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

import spinja.exceptions.SpinJaException;
import spinja.model.Model;
import spinja.model.Transition;
import spinja.search.TransitionCalculator;
import spinja.store.StateStore;
import spinja.util.Log;
import sspinja.SchedulerPanModel;

public class SchedulerDepthFirstSearch<M extends Model<T>, T extends Transition> extends SchedulerSearchAlgorithm<M, T> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public SchedulerDepthFirstSearch(M model, StateStore store, int stackSize, boolean errorExceedDepth,
			boolean checkForDeadlocks, int maxErrors, TransitionCalculator<M, T> nextTransition) {
		super(model, store, stackSize, errorExceedDepth, checkForDeadlocks, maxErrors, nextTransition);
		Log.initLog(SchedulerPanModel.scheduler.getSchedulerName());
	}

	@Override
	protected Transition nextTransition() {		
		return model.nextTransition(null) ;
	}

	@Override
	protected Transition nextTransition(Transition last) {	
		return model.nextTransition((T) last) ;
	}	
	
	@Override
	protected void takeTransition(final Transition next) throws SpinJaException {
		stack.takeTransition(next);
	}
}