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

import spinja.concurrent.model.ConcurrentModel;
import spinja.exceptions.ValidationException;
import spinja.model.Condition;
import spinja.model.listener.TransitionEvent;
import spinja.model.listener.TransitionListener;
import spinja.promela.model.PromelaProcess;
import spinja.promela.model.PromelaTransition;
import spinja.util.DataReader;
import spinja.util.DataWriter;
import sspinja.SchedulerPanModel;

public class SchedulerNeverClaimPromelaModel extends ConcurrentModel<PromelaTransition> implements TransitionListener {
	public static ConcurrentModel<PromelaTransition> createNever(final SchedulerPromelaModel model)
		throws ValidationException {
		final PromelaProcess never = model.getNever();
		if (never != null) {
			never.set_pid(255);
			return new SchedulerNeverClaimPromelaModel(model, never);
		} else {
			return model;
		}
	}

	
	private final SchedulerPromelaModel promelaModel;
	private final PromelaProcess neverProc;
	public static boolean claimTurn = true;
	
	public SchedulerPromelaModel getPromelaModel(){
		return promelaModel ;
	}
	
	public boolean getClaimTurn(){
		return claimTurn ;
	}
	
	public boolean getSporadicTurn(){
		return false ;
	}
	
	public PromelaProcess getNeverProc(){
		return neverProc ;
	}
	
	SchedulerNeverClaimPromelaModel(final SchedulerPromelaModel model, final PromelaProcess neverProc) {
		this.promelaModel = model ;
		SchedulerPromelaModel.panmodel = (SchedulerPanModel) model ;
		model.addTransitionListener(this);
		this.neverProc = neverProc;
		claimTurn = true;
	}


	@Override
	public String getName() {
		return promelaModel.getName() + "scheduler-promela-model-with-never-claim";
	}

	@Override
	public int getNrProcesses() {
		if (claimTurn) {
			return 1;
		} else {
			return promelaModel.getNrProcesses();
		}
	}

	@Override
	public PromelaProcess getProcess(final int index) {
		if (claimTurn) {
			if (index == 0) {
				return neverProc;
			} else {
				throw new IndexOutOfBoundsException();
			}
		} else {
			return promelaModel.getProcess(index);
		}
	}

	public int getSize() {
		return promelaModel.getSize() + neverProc.getSize() + 1;
	}
	
	public SchedulerPromelaModel getSporadicModel() {
		return promelaModel ;
	}

	@Override
	public SchedulerNeverClaimPromelaModel clone() {
		return new SchedulerNeverClaimPromelaModel(promelaModel, neverProc);
	}

	@Override
	public boolean conditionHolds(int condition) {
//		if (!claimTurn) {
		if (claimTurn) {
			if(condition == Condition.SHOULD_STORE_STATE) {
				return false;
			}
			return neverProc.conditionHolds(condition);
		} else {
			return promelaModel.conditionHolds(condition);
		}
	}


	@Override
	public PromelaTransition nextTransition(final PromelaTransition last) {
		if (claimTurn) {
			return neverProc.nextTransition(last);
		} else {
			return promelaModel.nextTransition(last);
		}
	}
	
	public boolean decode(DataReader reader) {
//		claimTurn = reader.readBoolean() == 1;
		claimTurn = reader.readBool();
		return neverProc.decode(reader)  && promelaModel.decode(reader);
	}
	
	public void encode(DataWriter writer) {
//		writer.writeBoolean(claimTurn ? 1 : 0);
		writer.writeBool(claimTurn);
		neverProc.encode(writer);
		promelaModel.encode(writer);
	}

	@Override
	public String toString() {
		return promelaModel.toString() + neverProc.toString();
	}

	public void transitionTaken(final TransitionEvent evt) {
		claimTurn = ! claimTurn;
		System.out.println("--> Change claimTurn: " + !claimTurn + " --> " +  claimTurn) ;
	}

//	public static void transitionTaken() {
//		System.out.println("--> claimTurn :   " +  claimTurn) ;
//		claimTurn = ! claimTurn;
//		System.out.println("--> claimTurn --> " +  claimTurn) ;
//	}
	public static void transitionUndo() {		
		claimTurn = ! claimTurn;
		System.out.println("--> Change claimTurn: " + !claimTurn + " --> " +  claimTurn) ;
	}
	
	public void transitionUndo(final TransitionEvent evt) {		
		claimTurn = ! claimTurn;
		System.out.println("--> Change claimTurn: " + !claimTurn + " --> " +  claimTurn) ;
	}
}
