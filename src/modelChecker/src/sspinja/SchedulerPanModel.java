package sspinja;
import sspinja.Run;

import spinja.util.DataReader;
import spinja.util.DataWriter;
import spinja.promela.model.*;
import spinja.exceptions.*;
import sspinja.scheduler.promela.model.*;
import sspinja.scheduling.SchedulerObject;

public class SchedulerPanModel extends SchedulerPromelaModel {


	public static void main(String[] args) {
		//scheduler DFS
		Run run = new Run(6);
		run.parseArguments(args,"SchedulerPan");
		if (SchedulerPanModel.scheduler.InitSchedulerObject(run.scheduleropt.getOption()))
		run.search(SchedulerPanModel.class);
	}

	public int a;
	public int b;

	public SchedulerPanModel() throws SpinJaException {
		super("SchedulerPan", 9);

		// Initialize the default values
		// Initialize the starting processes
		a = 0;
		b = 0;
		addProcess(new t1_0());
		addProcess(new t2_0());
		addProcess(new t3_0());
	}

	public void encode(DataWriter _writer) {
		scheduler.encode(_writer);
		_writer.writeByte(_nrProcs);
		_writer.writeInt(a);
		_writer.writeInt(b);
		int _i = 0 ;
		int _pcount = 0 ;
		while (_pcount < _nrProcs && _i < 255) {
			if (_procs[_i] != null && SchedulerObject.processInScheduler[_i]) {
				_procs[_i].encode(_writer);
				_pcount++;
			}
			_i++;
		}
	}

	public boolean decode(DataReader _reader) {
		scheduler.decode(_reader) ; 
		_nrProcs = _reader.readByte();
		a = _reader.readInt();
		b = _reader.readInt();

		int _start = _reader.getMark();
		for(int _i = 0; _i < _nrProcs; _i++) {
			_reader.storeMark();
			if(SchedulerObject.processInScheduler[_i]) {
				if(_procs[_i] == null || !_procs[_i].decode(_reader)) { // change local variables
					_reader.resetMark();
					switch(_reader.peekByte()) {
						case 0: _procs[_i] = new t1_0(true, _i); break;
						case 1: _procs[_i] = new t2_0(true, _i); break;
						case 2: _procs[_i] = new t3_0(true, _i); break;
						default: return false;
					}
					if(!_procs[_i].decode(_reader)) return false;
				}
			}
		}
		_process_size = _reader.getMark() - _start;
		//updateProcessListInScheduler(_nrProcs) ;
		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("+ SchedulerPanModel: \n");
		sb.append("a = ").append(a).append('\t');
		sb.append("b = ").append(b).append('\t');
		for(int i = 0; i < _nrProcs; i++) {
		  sb.append('\n').append(_procs[i]);
		}
		for(int i = 0; i < _nrChannels; i++) {
		  sb.append('\n').append(_channels[i]);
		}
		return sb.toString();
	}

	public int getChannelCount() {
		return 0;
	}

	public class t1_0 extends PromelaProcess {

		public t1_0(boolean decoding, int pid) {
			super(SchedulerPanModel.this, pid, new State[4], 0);

			PromelaTransitionFactory factory;
			factory = 
				new PromelaTransitionFactory(false, 50, 0, 3, "((a + b) < 5)") {
					public final boolean isEnabled() { 
						return ((a + b) < 5);
					}

					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							public final void undoImpl() {
								//undo statement
							}
						};
					}
				};
			factory.append(
				new ElseTransitionFactory(51, 0, 1, false));
			_stateTable[0] = new State(t1_0.this, factory, false, false, false);

			factory = 
				new PromelaTransitionFactory(false, 37, 1, 2, "assert (a >= b)") {
					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							public final void takeImpl() throws ValidationException {
								if(!(a >= b)) throw new AssertionException("(a >= b)");
							}//1

							public final void undoImpl() {
								//undo statement
							}
						};
					}
				};
			_stateTable[1] = new State(t1_0.this, factory, false, false, false);

			factory = 
				new EndTransitionFactory(38);
			_stateTable[2] = new State(t1_0.this, factory, true, false, false);

			factory = 
				new PromelaTransitionFactory(false, 33, 3, 0, "a = (a + 2)") {
					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							private int _backup_a;

							public final void takeImpl() throws ValidationException {
								_backup_a = a;
								a = (a + 2);
							}//1

							public final void undoImpl() {
								//undo statement
								a = _backup_a;
							}
						};
					}
				};
			_stateTable[3] = new State(t1_0.this, factory, false, false, false);

		}

		public t1_0() throws ValidationException {
			this(false, _nrProcs);

		}

		public int getSize() {
		  return 2;
		}

		public void encode(DataWriter _writer) {
			_writer.writeByte(0x1);
			_writer.writeByte(_sid);
		}

		public boolean decode(DataReader _reader) {
			if(_reader.readByte() != 0x1) return false;
			_sid = _reader.readByte();
			return true;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			if(_exclusive ==  _pid) sb.append("<atomic>");
			sb.append("t1_0 (" +  _pid + "," + _sid + "): ");
			return sb.toString();
		}

		public String getName() {
			return "t1" ;
		}

		public int getChannelCount() {
			return 0;
		}
	}

	public class t2_0 extends PromelaProcess {

		public t2_0(boolean decoding, int pid) {
			super(SchedulerPanModel.this, pid, new State[4], 0);

			PromelaTransitionFactory factory;
			factory = 
				new PromelaTransitionFactory(false, 52, 0, 3, "((a + b) < 5)") {
					public final boolean isEnabled() { 
						return ((a + b) < 5);
					}

					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							public final void undoImpl() {
								//undo statement
							}
						};
					}
				};
			factory.append(
				new ElseTransitionFactory(53, 0, 1, false));
			_stateTable[0] = new State(t2_0.this, factory, false, false, false);

			factory = 
				new PromelaTransitionFactory(false, 45, 1, 2, "assert (a >= b)") {
					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							public final void takeImpl() throws ValidationException {
								if(!(a >= b)) throw new AssertionException("(a >= b)");
							}//1

							public final void undoImpl() {
								//undo statement
							}
						};
					}
				};
			_stateTable[1] = new State(t2_0.this, factory, false, false, false);

			factory = 
				new EndTransitionFactory(46);
			_stateTable[2] = new State(t2_0.this, factory, true, false, false);

			factory = 
				new PromelaTransitionFactory(false, 41, 3, 0, "b++") {
					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							private int _backup_b;

							public final void takeImpl() throws ValidationException {
								_backup_b = b;
								b++;
							}//1

							public final void undoImpl() {
								//undo statement
								b = _backup_b;
							}
						};
					}
				};
			_stateTable[3] = new State(t2_0.this, factory, false, false, false);

		}

		public t2_0() throws ValidationException {
			this(false, _nrProcs);

		}

		public int getSize() {
		  return 2;
		}

		public void encode(DataWriter _writer) {
			_writer.writeByte(0x2);
			_writer.writeByte(_sid);
		}

		public boolean decode(DataReader _reader) {
			if(_reader.readByte() != 0x2) return false;
			_sid = _reader.readByte();
			return true;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			if(_exclusive ==  _pid) sb.append("<atomic>");
			sb.append("t2_0 (" +  _pid + "," + _sid + "): ");
			return sb.toString();
		}

		public String getName() {
			return "t2" ;
		}

		public int getChannelCount() {
			return 0;
		}
	}

	public class t3_0 extends PromelaProcess {

		public t3_0(boolean decoding, int pid) {
			super(SchedulerPanModel.this, pid, new State[3], 0);

			PromelaTransitionFactory factory;
			factory = 
				new PromelaTransitionFactory(false, 47, 0, 1, "a++") {
					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							private int _backup_a;

							public final void takeImpl() throws ValidationException {
								_backup_a = a;
								a++;
							}//1

							public final void undoImpl() {
								//undo statement
								a = _backup_a;
							}
						};
					}
				};
			_stateTable[0] = new State(t3_0.this, factory, false, false, false);

			factory = 
				new PromelaTransitionFactory(false, 48, 1, 2, "b++") {
					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							private int _backup_b;

							public final void takeImpl() throws ValidationException {
								_backup_b = b;
								b++;
							}//1

							public final void undoImpl() {
								//undo statement
								b = _backup_b;
							}
						};
					}
				};
			_stateTable[1] = new State(t3_0.this, factory, false, false, false);

			factory = 
				new EndTransitionFactory(49);
			_stateTable[2] = new State(t3_0.this, factory, true, false, false);

		}

		public t3_0() throws ValidationException {
			this(false, _nrProcs);

		}

		public int getSize() {
		  return 2;
		}

		public void encode(DataWriter _writer) {
			_writer.writeByte(0x3);
			_writer.writeByte(_sid);
		}

		public boolean decode(DataReader _reader) {
			if(_reader.readByte() != 0x3) return false;
			_sid = _reader.readByte();
			return true;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			if(_exclusive ==  _pid) sb.append("<atomic>");
			sb.append("t3_0 (" +  _pid + "," + _sid + "): ");
			return sb.toString();
		}

		public String getName() {
			return "t3" ;
		}

		public int getChannelCount() {
			return 0;
		}
	}
	public String sysGet(String va) {
		String result = "";
		switch (va) {
			case "a" :
				result += a;
				break ;
			case "b" :
				result += b;
				break ;
		}
		return result ;
	}
}
