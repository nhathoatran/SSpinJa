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

package spinja.promela.model;

import spinja.concurrent.model.Process;
import spinja.model.Condition;
import sspinja.Config;
import sspinja.Run;

public abstract class PromelaProcess extends Process<PromelaTransition> {
	protected final PromelaModel _model;

	protected final State[] _stateTable;

	protected int _sid;

	protected  int _pid;

	
	public PromelaProcess(PromelaModel promelaModel, int pid, final State[] table, final int startState) {
		_model = promelaModel;
		set_pid(pid);
		_stateTable = table;
		this.set_sid(startState);
	}

	abstract public int getChannelCount();

	public  State getCurrentState() {
		return _stateTable[get_sid()];
	}
	
	@Override
	public boolean conditionHolds(int condition) {
		switch (condition) {
			case Condition.END_STATE:
				return getCurrentState().isEndState();
			case Condition.PROGRESS_STATE:
				return getCurrentState().isProgressState();
			case Condition.ACCEPT_STATE:
				return getCurrentState().isAcceptState();
			case Condition.SHOULD_STORE_STATE:
				return _model._exclusive == PromelaModel._NO_PROCESS;
			default:
				return false;
		}
	}

	public boolean isEnabled() {
		return true;
	}

	@Override
	public PromelaProcess clone() {
		try {
			return getClass().getConstructor().newInstance();
		} catch (Exception e) {
			return null;
		}
	}

	public int countNumTrans() {
		PromelaTransition last = null ;
		int count = 0 ;
		
		while ((last = nextTransition(last)) != null)
			count ++ ;
		
		return count ;
	}
	
	@Override
	public PromelaTransition nextTransition(final PromelaTransition last) {
		if (_model._exclusive != PromelaModel._NO_PROCESS && _model._exclusive != get_pid()) {
			return null;
		}

		int[] msg = null;
		PromelaTransitionFactory timeoutFactory = null;
		PromelaTransitionFactory elseFactory = null;
		PromelaTransitionFactory factory = null;

		if (last == null) {
			factory = getCurrentState().getFirst();
		} else { 
			if (last instanceof RendezvousTransition) {
				RendezvousTransition next = ((RendezvousTransition) last).nextRendezvousTransition();
				if (next != null) {
					return next;
				}
				factory = last.getFactory().getNext();
			} else {
				factory = last.getFactory() == null ? null : last.getFactory().getNext();
			}
		}
		
				
		while (factory != null) {	
			if (Config.isSchedulerSearch && factory.toString().contains("-end-"))
				return factory.newTransition();
			if (factory.isEnabled()) {
				return factory.newTransition();
			} else if ((msg = factory.getRendezvous()) != null) {
				for (int i = _model._nrProcs - 1; i >= 0; i--) {
					PromelaProcess proc = _model._procs[i];
					if (proc != this) {
						for (PromelaTransitionFactory read = proc.getCurrentState().getFirst(); read != null; read = read.getNext()) {
							if (read.canReadRendezvous(msg)) {
								return new RendezvousTransition(_model, factory.newTransition(),
									read.newTransition());
							}
						}
					}
				}
			} else if (last == null && factory.isElse()) {
				elseFactory = factory;
			} else if (timeoutFactory == null && factory.canTimeout()) {
				timeoutFactory = factory;
			}	
			factory = factory.getNext();
		}	

		if (elseFactory != null) {
			return elseFactory.newTransition();
		} else if(_model._exclusive == get_pid() && last == null) {
			return _model.newEndAtomic();
		}

		if (timeoutFactory != null && !_model._timeout && !_model._ignore_timeout) {
			_model._ignore_timeout = true;
			for (int i = _model._nrProcs - 1; i >= 0; i--) {
				if (i != get_pid() && _model._procs[i].nextTransition(null) != null) {
					_model._ignore_timeout = false;
					return null;
				}
			}
			_model._ignore_timeout = false;
			_model._timeout = true;
			if (timeoutFactory.isEnabled()) {
				_model._timeout = false;
				return timeoutFactory.newTransition();
			}
			PromelaTransition next = nextTransition(last);
			_model._timeout = false;
			return next;
		}

		return null;
	}
	
	public PromelaProcess prevProcess() {
		if(get_pid() > 0) {
			return _model._procs[get_pid() - 1];
		} else {
			return null;
		}
	}

	public PromelaProcess nextProcess() {
		if (get_pid() + 1 < _model._nrProcs) {
			return _model._procs[get_pid() + 1];
		} else {
			return null;
		}
	}

	public PromelaModel getModel() {
		return _model;
	}

	@Override
	public boolean onlyLocalTransitions() {
		for (PromelaTransitionFactory temp = getCurrentState().getFirst(); temp != null; temp = temp.getNext()) {
			if (!temp.isLocal()) {
				return false;
			}
		}
		return true;
	}

	public int get_sid() {
		return _sid;
	}

	public void set_sid(int _sid) {
		this._sid = _sid;
	}

	public int get_pid() {
		return _pid;
	}

	public int getId() {
		return _pid ;
	}
	public void set_pid(int _pid) {
		this._pid = _pid;
	}
	
	public String getName() {
		return null ;
	};

}