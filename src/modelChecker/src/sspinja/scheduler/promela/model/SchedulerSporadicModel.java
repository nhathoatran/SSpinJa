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

public class SchedulerSporadicModel extends ConcurrentModel<PromelaTransition> implements TransitionListener {
	public static ConcurrentModel<PromelaTransition> createSporadic(final SchedulerPromelaModel model)
		throws ValidationException {
		final PromelaProcess sporadic = model.getSporadic();
		if (sporadic != null) {
			sporadic.set_pid(255);
			return new SchedulerSporadicModel(model, sporadic);
		} else {
			return model;
		}
	}

	private final SchedulerPromelaModel promelaModel;
	private final PromelaProcess sporadic;
	public static boolean sporadicTurn = true;
	
	public SchedulerPromelaModel getPromelaModel(){
		return promelaModel ;
	}
	public PromelaProcess getSporadic(){
		return sporadic ;
	}
	

	public boolean getClaimTurn(){
		return false;
	}
	
	public boolean getSporadicTurn(){
		return sporadicTurn ;
	}
	
	SchedulerSporadicModel(final SchedulerPromelaModel model, final PromelaProcess sporadic) {
		this.promelaModel = model;
		SchedulerPromelaModel.panmodel = (SchedulerPanModel) model ;
		model.addTransitionListener(this);
		this.sporadic= sporadic;
		sporadicTurn = true;
	}

	
	@Override
	public String getName() {
		return promelaModel.getName() + "-with-sporadic-process";
	}

	@Override
	public int getNrProcesses() {
		if (sporadicTurn) {
			return 1;
		} else {
			return promelaModel.getNrProcesses();
		}
	}

	@Override
	public PromelaProcess getProcess(final int index) {
		if (sporadicTurn) {
			if (index == 0) {
				return sporadic;
			} else {
				throw new IndexOutOfBoundsException();
			}
		} else {
			return promelaModel.getProcess(index);
		}
	}

	public int getSize() {
		return promelaModel.getSize() + sporadic.getSize() + 1;
	}

	@Override
	public SchedulerSporadicModel clone() {
		return new SchedulerSporadicModel(promelaModel, sporadic);
	}

	@Override
	public boolean conditionHolds(int condition) {
		if (!sporadicTurn) {
			if(condition == Condition.SHOULD_STORE_STATE) {
				return true;
			}
			return sporadic.conditionHolds(condition);
		} else {
			return promelaModel.conditionHolds(condition);
		}
	}

	@Override
	public PromelaTransition nextTransition(final PromelaTransition last) {
		if (sporadicTurn) {
			return sporadic.nextTransition(last);
		} else {
			return promelaModel.nextTransition(last);
		}
	}
	
	public boolean decode(DataReader reader) {
		sporadicTurn = reader.readBoolean() == 1;
		boolean result = sporadic.decode(reader)  &&  promelaModel.decode(reader);
		return result ;
	}
	
	public void encode(DataWriter writer) {
		writer.writeBoolean(sporadicTurn ? 1 : 0);
		sporadic.encode(writer);
		promelaModel.encode(writer);
	}

	@Override
	public String toString() {
		return promelaModel.toString() + " " + sporadic.toString() + " Sporadic turn: " + sporadicTurn;
	}

	
	public void transitionTaken(final TransitionEvent evt) {
		//if (SchedulerPanModel.scheduler.check_time_sporadic(((SchedulerPanModel.sporadic) sporadic).get_time_sporadic())) {
		sporadicTurn = !sporadicTurn;
		System.out.println("--> Change sporadicTurn: " + !sporadicTurn + " --> " +  sporadicTurn) ;
	}

	public void transitionUndo(final TransitionEvent evt) {
		sporadicTurn = !sporadicTurn;
		System.out.println("--> Change sporadicTurn: " + !sporadicTurn + " --> " +  sporadicTurn) ;
	}
	
	public PromelaProcess getNever() throws ValidationException {
		if (promelaModel != null)
			return promelaModel.getNever() ;
		return null;
	}
		
	public int getRunID() {
		return promelaModel.getRunID();
	}
	
	public int getExecID() {
		return sporadicTurn ? 255 : getRunID();
	}
}
