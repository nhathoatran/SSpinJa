package espinja;
import espinja.Run;

import spinja.util.DataReader;
import spinja.util.DataWriter;
import spinja.promela.model.*;
import spinja.exceptions.*;

public class PanModel extends PromelaModel {


	public static void main(String[] args) {
		//CTL/RTCTL
		Run run = new Run(5);
		run.parseArguments(args,"Pan");
		run.setDepth(27);
		run.search(PanModel.class);
	}

	int v;

	public PanModel() throws SpinJaException {
		super("Pan", 5);

		// Initialize the default values
		v = 0;
		// Initialize the starting processes
		addProcess(new test_0());
	}

	public void encode(DataWriter _writer) {
		_writer.writeByte(_nrProcs);
		_writer.writeInt(v);
		for(byte _i = 0; _i < _nrProcs; _i++) { 
			_procs[_i].encode(_writer);
		}

	}

	public boolean decode(DataReader _reader) {
		_nrProcs = _reader.readByte();
		v = _reader.readInt();

		int _start = _reader.getMark();
		for(int _i = 0; _i < _nrProcs; _i++) {
			_reader.storeMark();
			if(_procs[_i] == null || !_procs[_i].decode(_reader)) {
				_reader.resetMark();
				switch(_reader.peekByte()) {
					case 0: _procs[_i] = new test_0(true, _i); break;
					case 1: _procs[_i] = new P_0(true, _i); break;
					case 2: _procs[_i] = new Q_0(true, _i); break;
					case 3: _procs[_i] = new TestCase0_0(true, _i); break;
					case 4: _procs[_i] = new TestCase1_0(true, _i); break;
					default: return false;
				}
				if(!_procs[_i].decode(_reader)) return false;
			}
		}
		_process_size = _reader.getMark() - _start;
		return true;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("PanModel: ");
		sb.append("v = ").append(v).append('\t');
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

	public class test_0 extends PromelaProcess {
		protected int _pc;
		protected int _pid;

		public test_0(boolean decoding, int pid) {
			super(PanModel.this, pid, new State[3], 0);

			PromelaTransitionFactory factory;
			factory = 
				new PromelaTransitionFactory(false, 36, 0, 2, "run TestCase0_0()") {
					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							public final void takeImpl() throws ValidationException {
								addProcess(new TestCase0_0());
							}//1

							public final void undoImpl() {
								//undo statement
								endProcess();
							}
						};
					}
				};
			factory.append(
				new PromelaTransitionFactory(false, 37, 0, 2, "run TestCase1_0()") {
					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							public final void takeImpl() throws ValidationException {
								addProcess(new TestCase1_0());
							}//1

							public final void undoImpl() {
								//undo statement
								endProcess();
							}
						};
					}
				});
			factory.append(
				new ElseTransitionFactory(38, 0, 1, false));
			_stateTable[0] = new State(test_0.this, factory, false, false, false);

			factory = 
				new PromelaTransitionFactory(true, 27, 1, 2, "skip") {
					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							public final void undoImpl() {
								//undo statement
							}
						};
					}
				};
			_stateTable[1] = new State(test_0.this, factory, false, false, false);

			factory = 
				new EndTransitionFactory(22);
			_stateTable[2] = new State(test_0.this, factory, true, false, false);

		}

		public test_0() throws ValidationException {
			this(false, _nrProcs);

			_pc = 0;
			_pid = 0;
		}

		public int getSize() {
		  return 5;
		}

		public void encode(DataWriter _writer) {
			_writer.writeByte(0x0);
			_writer.writeByte(_sid);
			_writer.writeShort(_pc);
			_writer.writeByte(_pid);
		}

		public boolean decode(DataReader _reader) {
			if(_reader.readByte() != 0x0) return false;
			_sid = _reader.readByte();
			_pc = _reader.readShort();
			_pid = _reader.readByte();
			return true;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			if(_exclusive ==  _pid) sb.append("<atomic>");
			sb.append("test_0 (" +  _pid + "," + _sid + "): ");
			return sb.toString();
		}

		public int getChannelCount() {
			return 0;
		}
		@Override
		public int getId() {return _pid;}
		@Override
		public int get_pid() {return _pid;}
		@Override
		public void set_pid(int _pid) {this._pid = _pid;}
	}

	public class P_0 extends PromelaProcess {
		protected int _pc;
		protected int _pid;
		protected int _bP0_0;

		public P_0(boolean decoding, int pid) {
			super(PanModel.this, pid, new State[1], 0);

			PromelaTransitionFactory factory;
			factory = 
				new PromelaTransitionFactory(false, 39, 0, 0, "(v < 5); _bP0_0 = false; if; v++; if") {
					public final boolean isEnabled() { 
						return (v < 5);
					}

					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							private byte[] _backup;

							public final void takeImpl() throws ValidationException {
								_backup = getProcess().getModel().encode();
								_bP0_0 = 0;
								if((v < 5)) {
									_bP0_0 = 1;
								} else {
								}
								v++;
								if((_bP0_0 != 0 )) {
									if(!(v <= 5)) throw new AssertionException("(v <= 5)");
								} else {
								}
							}//1

							public final void undoImpl() {
								if(!getProcess().getModel().decode(_backup)) throw new Error("Could not decode the backup!");
							}
						};
					}
				};
			_stateTable[0] = new State(P_0.this, factory, false, false, false);

		}

		public P_0() throws ValidationException {
			this(false, _nrProcs);

			_pc = -1;
			_pid = -1;
		}

		public int getSize() {
		  return 6;
		}

		public void encode(DataWriter _writer) {
			_writer.writeByte(0x1);
			_writer.writeByte(_sid);
			_writer.writeShort(_pc);
			_writer.writeByte(_pid);
			_writer.writeBoolean(_bP0_0);
		}

		public boolean decode(DataReader _reader) {
			if(_reader.readByte() != 0x1) return false;
			_sid = _reader.readByte();
			_pc = _reader.readShort();
			_pid = _reader.readByte();
			_bP0_0 = _reader.readBoolean();
			return true;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			if(_exclusive ==  _pid) sb.append("<atomic>");
			sb.append("P_0 (" +  _pid + "," + _sid + "): ");
			sb.append("_bP0_0 = ").append(_bP0_0).append('\t');
			return sb.toString();
		}

		public int getChannelCount() {
			return 0;
		}
		@Override
		public int getId() {return _pid;}
		@Override
		public int get_pid() {return _pid;}
		@Override
		public void set_pid(int _pid) {this._pid = _pid;}
	}

	public class Q_0 extends PromelaProcess {
		protected int _pc;
		protected int _pid;
		protected int _bQ0_0;

		public Q_0(boolean decoding, int pid) {
			super(PanModel.this, pid, new State[1], 0);

			PromelaTransitionFactory factory;
			factory = 
				new PromelaTransitionFactory(false, 40, 0, 0, "(v > 5); _bQ0_0 = false; if; v--; if") {
					public final boolean isEnabled() { 
						return (v > 5);
					}

					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							private byte[] _backup;

							public final void takeImpl() throws ValidationException {
								_backup = getProcess().getModel().encode();
								_bQ0_0 = 0;
								if((v > 5)) {
									_bQ0_0 = 1;
								} else {
								}
								v--;
								if((_bQ0_0 != 0 )) {
									if(!(v == 5)) throw new AssertionException("(v == 5)");
								} else {
								}
							}//1

							public final void undoImpl() {
								if(!getProcess().getModel().decode(_backup)) throw new Error("Could not decode the backup!");
							}
						};
					}
				};
			_stateTable[0] = new State(Q_0.this, factory, false, false, false);

		}

		public Q_0() throws ValidationException {
			this(false, _nrProcs);

			_pc = -1;
			_pid = -1;
		}

		public int getSize() {
		  return 6;
		}

		public void encode(DataWriter _writer) {
			_writer.writeByte(0x2);
			_writer.writeByte(_sid);
			_writer.writeShort(_pc);
			_writer.writeByte(_pid);
			_writer.writeBoolean(_bQ0_0);
		}

		public boolean decode(DataReader _reader) {
			if(_reader.readByte() != 0x2) return false;
			_sid = _reader.readByte();
			_pc = _reader.readShort();
			_pid = _reader.readByte();
			_bQ0_0 = _reader.readBoolean();
			return true;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			if(_exclusive ==  _pid) sb.append("<atomic>");
			sb.append("Q_0 (" +  _pid + "," + _sid + "): ");
			sb.append("_bQ0_0 = ").append(_bQ0_0).append('\t');
			return sb.toString();
		}

		public int getChannelCount() {
			return 0;
		}
		@Override
		public int getId() {return _pid;}
		@Override
		public int get_pid() {return _pid;}
		@Override
		public void set_pid(int _pid) {this._pid = _pid;}
	}

	public class TestCase0_0 extends PromelaProcess {
		protected int _pc;
		protected int _pid;

		public TestCase0_0(boolean decoding, int pid) {
			super(PanModel.this, pid, new State[2], 0);

			PromelaTransitionFactory factory;
			factory = 
				new PromelaTransitionFactory(false, 32, 0, 1, "printf(\"config1\");; v = 0; run P_0(); run Q_0()") {
					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							private int _backup_v;

							public final void takeImpl() throws ValidationException {
								printf("config1");
								_backup_v = v;
								v = 0;
								addProcess(new P_0());
								addProcess(new Q_0());
							}//1

							public final void undoImpl() {
								//undo statement
								endProcess();
								endProcess();
								v = _backup_v;
							}
						};
					}
				};
			_stateTable[0] = new State(TestCase0_0.this, factory, false, false, false);

			factory = 
				new EndTransitionFactory(33);
			_stateTable[1] = new State(TestCase0_0.this, factory, true, false, false);

		}

		public TestCase0_0() throws ValidationException {
			this(false, _nrProcs);

			_pc = -1;
			_pid = -1;
		}

		public int getSize() {
		  return 5;
		}

		public void encode(DataWriter _writer) {
			_writer.writeByte(0x3);
			_writer.writeByte(_sid);
			_writer.writeShort(_pc);
			_writer.writeByte(_pid);
		}

		public boolean decode(DataReader _reader) {
			if(_reader.readByte() != 0x3) return false;
			_sid = _reader.readByte();
			_pc = _reader.readShort();
			_pid = _reader.readByte();
			return true;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			if(_exclusive ==  _pid) sb.append("<atomic>");
			sb.append("TestCase0_0 (" +  _pid + "," + _sid + "): ");
			return sb.toString();
		}

		public int getChannelCount() {
			return 0;
		}
		@Override
		public int getId() {return _pid;}
		@Override
		public int get_pid() {return _pid;}
		@Override
		public void set_pid(int _pid) {this._pid = _pid;}
	}

	public class TestCase1_0 extends PromelaProcess {
		protected int _pc;
		protected int _pid;

		public TestCase1_0(boolean decoding, int pid) {
			super(PanModel.this, pid, new State[2], 0);

			PromelaTransitionFactory factory;
			factory = 
				new PromelaTransitionFactory(false, 34, 0, 1, "printf(\"config2\");; v = 6; run P_0(); run Q_0()") {
					public final PromelaTransition newTransition() {
						return new NonAtomicTransition() {
							private int _backup_v;

							public final void takeImpl() throws ValidationException {
								printf("config2");
								_backup_v = v;
								v = 6;
								addProcess(new P_0());
								addProcess(new Q_0());
							}//1

							public final void undoImpl() {
								//undo statement
								endProcess();
								endProcess();
								v = _backup_v;
							}
						};
					}
				};
			_stateTable[0] = new State(TestCase1_0.this, factory, false, false, false);

			factory = 
				new EndTransitionFactory(35);
			_stateTable[1] = new State(TestCase1_0.this, factory, true, false, false);

		}

		public TestCase1_0() throws ValidationException {
			this(false, _nrProcs);

			_pc = -1;
			_pid = -1;
		}

		public int getSize() {
		  return 5;
		}

		public void encode(DataWriter _writer) {
			_writer.writeByte(0x4);
			_writer.writeByte(_sid);
			_writer.writeShort(_pc);
			_writer.writeByte(_pid);
		}

		public boolean decode(DataReader _reader) {
			if(_reader.readByte() != 0x4) return false;
			_sid = _reader.readByte();
			_pc = _reader.readShort();
			_pid = _reader.readByte();
			return true;
		}

		public String toString() {
			StringBuilder sb = new StringBuilder();
			if(_exclusive ==  _pid) sb.append("<atomic>");
			sb.append("TestCase1_0 (" +  _pid + "," + _sid + "): ");
			return sb.toString();
		}

		public int getChannelCount() {
			return 0;
		}
		@Override
		public int getId() {return _pid;}
		@Override
		public int get_pid() {return _pid;}
		@Override
		public void set_pid(int _pid) {this._pid = _pid;}
	}
	
	//AF<=5 (v > 5)  
	public boolean[] init_sf() {
		boolean checked[] = new boolean[2];
		checked[1] = true ; //(v > 5) ;
		return checked ;
	}
	
	public boolean[] init_atomicf() {
		boolean result[] = new boolean[2];
		result[1] = (v > 5) ;
		return result ;
	}
	
	public boolean stateCheck() {
		return false ;
	}
	public boolean modelCheck() {
		return true ;
	}
	
	public boolean collectState() {
		return false ;
	}
}
