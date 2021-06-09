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

public class SchedulerNeverClaimSporadicModel extends ConcurrentModel<PromelaTransition> implements TransitionListener {
	
	public static ConcurrentModel<PromelaTransition> createNever(final SchedulerPromelaModel model) throws ValidationException {
		final PromelaProcess never = model.getNever();
		final PromelaProcess sporadic = model.getSporadic();
		if (never != null && sporadic != null) {
			never.set_pid(255);
			sporadic.set_pid(255);
			return new SchedulerNeverClaimSporadicModel(model, never, sporadic);
		} else {
			return model;
		}
	}

	
//	private SchedulerSporadicModel sporadicModel;
	private final PromelaProcess neverProc;
	public static boolean claimTurn = true;
	
	private final SchedulerPromelaModel promelaModel;
	private final PromelaProcess sporadicProc;
	public static boolean sporadicTurn = true;
	
//	public SchedulerSporadicModel getSporadicModel(){
//		return sporadicModel ;
//	}
	
	public SchedulerPromelaModel getPromelaModel(){
		return promelaModel ; //sporadicModel.getPromelaModel() ;
	}
	
	public boolean getClaimTurn(){
		return claimTurn ;
	}
	
	public boolean getSporadicTurn(){
		return sporadicTurn ;
	}
	
	public PromelaProcess getNeverProc(){
		return neverProc ;
	}
	
//	SchedulerNeverClaimSporadicModel(final SchedulerSporadicModel model, final PromelaProcess neverProc) {
//		this.sporadicModel = model ;		
//		model.addTransitionListener(this);
//		this.neverProc = neverProc;
//		claimTurn = true;
//	}
	
	SchedulerNeverClaimSporadicModel(final SchedulerPromelaModel model, final PromelaProcess neverProc, final PromelaProcess sporadic) {
//		this.sporadicModel = model ;
		this.promelaModel = model;		
		SchedulerPromelaModel.panmodel = (SchedulerPanModel) model;
		model.addTransitionListener(this);
		this.neverProc = neverProc;
		claimTurn = true;
		this.sporadicProc= sporadic;
		sporadicTurn = true;
	}

	@Override
	public String getName() {
		return promelaModel.getName() + "-with-never-claim & sporadic";
	}

	@Override
	public int getNrProcesses() {
		if (claimTurn) {
			return 1;
		} else {
			if (sporadicTurn) {
				return 1;
			} else {
				return promelaModel.getNrProcesses();
			}
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
			if (sporadicTurn) {
				if (index == 0) {
					return sporadicProc;
				} else {
					throw new IndexOutOfBoundsException();
				}
			} else {
				return promelaModel.getProcess(index);
			}
		}
	}

	public int getSize() {
		return promelaModel.getSize() + sporadicProc.getSize() + neverProc.getSize() + 1;
	}
	
	

	@Override
	public SchedulerNeverClaimSporadicModel clone() {
		return new SchedulerNeverClaimSporadicModel(promelaModel, sporadicProc, neverProc);
	}

	@Override
	public boolean conditionHolds(int condition) {
		if (!claimTurn) {
			if(condition == Condition.SHOULD_STORE_STATE) {
				return false;
			}
			return neverProc.conditionHolds(condition);
		} else {
			if (!sporadicTurn) {
				if(condition == Condition.SHOULD_STORE_STATE) {
					return true;
				}
				return sporadicProc.conditionHolds(condition);
			} else {
				return promelaModel.conditionHolds(condition);
			}
		}
	}

	@Override
	public PromelaTransition nextTransition(final PromelaTransition last) {
		if (claimTurn) {
			return neverProc.nextTransition(last);
		} else {
			if (sporadicTurn) {
				return sporadicProc.nextTransition(last);
			} else {
				return promelaModel.nextTransition(last);
			}
		}
	}
	
	public boolean decode(DataReader reader) {
		claimTurn = reader.readBoolean() == 1;
		boolean result = neverProc.decode(reader)  ;
		if (result) {
			sporadicTurn = reader.readBoolean() == 1 ;
			result = sporadicProc.decode(reader) && promelaModel.decode(reader);
		}
		return result ;
	}
	
	public void encode(DataWriter writer) {
		writer.writeBoolean(claimTurn ? 1 : 0);
		neverProc.encode(writer);
		writer.writeBoolean(sporadicTurn ? 1 : 0);
		sporadicProc.encode(writer);
		promelaModel.encode(writer);		
	}

	@Override
	public String toString() {
		return promelaModel.toString() + "\n" + 
				neverProc.toString() + " Claim turn: " + claimTurn + "\n" + 
				sporadicProc.toString() + " Sporadic turn: " + sporadicTurn;
	}

	public  void transitionTaken(final TransitionEvent evt) {
		if (!claimTurn) {
			sporadicTurn = !sporadicTurn;
			System.out.println("--> Change sporadicTurn: " + !sporadicTurn + " --> " +  sporadicTurn) ;
		}
		claimTurn = !claimTurn;
		System.out.println("--> Change claimTurn: " + !claimTurn + " --> " +  claimTurn) ;
	}
	
//	public static void transitionTaken() {
//		if (!claimTurn) {
//			SchedulerSporadicModel.transitionTaken() ;
//		}
//		claimTurn = !claimTurn ;
//		System.out.println("--> Change claimTurn --> " +  claimTurn) ;
//	}
//
//	public static void transitionUndo() {
//		if (!claimTurn) {
//			sporadicTurn = !sporadicTurn;
//			System.out.println("--> Change sporadicTurn: " + !sporadicTurn + " --> " +  sporadicTurn) ;		
//		}
//		claimTurn = !claimTurn ;
//		System.out.println("--> Change claimTurn: " + !claimTurn + " --> " +  claimTurn) ;
//	}
	
	public void transitionUndo(final TransitionEvent evt) {
		if (!claimTurn) {
			sporadicTurn = !sporadicTurn;
			System.out.println("--> Change sporadicTurn: " + !sporadicTurn + " --> " +  sporadicTurn) ;		
		}
		claimTurn = !claimTurn ;
		System.out.println("--> Change claimTurn: " + !claimTurn + " --> " +  claimTurn) ;
	}
}
