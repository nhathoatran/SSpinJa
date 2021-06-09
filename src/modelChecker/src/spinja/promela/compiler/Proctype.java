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

package spinja.promela.compiler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import spinja.exceptions.ValidationException;
import spinja.promela.compiler.automaton.Automaton;
import spinja.promela.compiler.automaton.State;
import spinja.promela.compiler.expression.CTL_Expression;
import spinja.promela.compiler.expression.Expression;
import spinja.promela.compiler.expression.Identifier;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.variable.Variable;
import spinja.promela.compiler.variable.VariableContainer;
import spinja.promela.compiler.variable.VariableStore;
import spinja.util.StringUtil;
import spinja.util.StringWriter;
import sspinja.CompileScheduler;
import sspinja.Config;

/**
 * This object represents a process in the Promela language. It contains a number of 'local'
 * variables, of which the first couple are the arguments. Also it contains a starting Node which
 * points to a complete graph. This graph represents all the actions that can be executed by this
 * process.
 * 
 * @author Marc de Jonge
 */
public class Proctype implements VariableContainer {
	/**
	 * The specification to which this {@link Proctype} belongs
	 */
	protected final Specification specification;

	/**
	 * The ID number of this {@link Proctype}.
	 */
	protected final int id;

	/**
	 * The number of active processes that are started when the model checking begins
	 */
	protected int nrActive;

	/**
	 * The priority that is only used when ran randomly.
	 */
	protected int priority;

	/**
	 * The name of the process in the Model.
	 */
	protected final String name;

	
	/**
	 * The starting Node which points to the complete graph.
	 */
	protected final Automaton automaton;

	/**
	 * The store where all the variables are stored.
	 */
	protected final VariableStore varStore;
	
    public void addVariableMapping(String s, String v) {
    	varStore.addVariableMapping(s, v);
    }

    public String getVariableMapping(String s) {
    	return varStore.getVariableMapping(s);
    }

    public Map<String, String> getVariableMappings() {
    	return varStore.getVariableMappings();
    }
    
    public boolean argumentsIsEmpty(){
    	return arguments.isEmpty() ;
    }
	/**
	 * The expression which can enable or disable all actions.
	 */
	private Expression enabler;

	/**
	 * While this boolean is true, each variable that is added to this {@link Proctype} is assumed
	 * to be an argument.
	 */
	private boolean isArgument;

	/**
	 * Here all the arguments are store. Please note that these are also stored in the
	 * VariableStore!
	 */
	private final List<Variable> arguments;

	private final List<Variable> channelXR = new ArrayList<Variable>();

	private final List<Variable> channelXS = new ArrayList<Variable>();

	/**
	 * Creates a new {@link Proctype} object.
	 * 
	 * @param specification
	 *            The specification in which this {@link Proctype} is defined.
	 * @param id
	 *            The identifying number of this {@link Proctype}
	 * @param nrActive
	 *            The number of processes that should be started when the model checking starts.
	 * @param name
	 *            The name of the {@link Proctype}.
	 */
	public Proctype(final Specification specification, final int id, final int nrActive,
		final String name) {
		this.specification = specification;
		this.id = id;
		this.nrActive = nrActive;
		this.name = name;
		
		automaton = new Automaton(this);
		varStore = new VariableStore();
		priority = 0;
		enabler = null;
		isArgument = true;
		arguments = new ArrayList<Variable>();
	}	

	public boolean equals(Object o) {
		if (o == null || !(o instanceof Proctype))
			return false;
		Proctype p = (Proctype)o;
		return p.getName().equals(name);
	}

	public int hashCode() {
		return name.hashCode(); 
	}

	/**
	 * Adds a new variable to this {@link Proctype}. While the lastArgument() function is not
	 * called this function assumes that every variable that is added is also an argument.
	 * 
	 * @param var
	 *            The variable that is to be added.
	 */
	public void addVariable(final Variable var) {
		addVariable(var, this.isArgument);
		var.setWritten(true);
	}

	public void addVariable(final Variable var, boolean isArgument) {
		varStore.addVariable(var);
		if (isArgument) {
			arguments.add(var);
			
		}
		var.setWritten(true);
	}

	public void prependVariable(final Variable var) {
		varStore.prependVariable(var);
		if (isArgument) {
			arguments.add(var);
			var.setWritten(true);
		}
	}

	/**
	 * Adds an identifier that points to a channel to the list of eXclusive Reads
	 * @param id
	 *            The identifier.
	 */
	public void addXR(final Identifier id) {
		channelXR.add(id.getVariable());
	}

	/**
	 * Adds an identifier that points to a channel to the list of eXclusive Sends
	 * @param id
	 *            The identifier.
	 */
	public void addXS(final Identifier id) {
		channelXS.add(id.getVariable());
	}

	/**
	 * Generates the code for the {@link Proctype}.
	 * @param w
	 *            The StringWriter that is used to output the code.
	 * @throws ParseException
	 *             When something went wrong while creating the source code.
	 */
	public void generateCode(final StringWriter w) throws ParseException {
		w.appendLine("public class ", getName(), " extends PromelaProcess {");
		w.indent();

		generateLocalVars(w);
		generateConstructor(w);
		generateStorable(w);
		if(!"never".equals(getName())){
		   if (!"sporadic".equals(getName())) {
			   generateToString(w);
		   } else {
			   generateToStringSporadic(w) ;
		   }
		}else{
			generateToStringNeverClaim(w);
		}
		
		if (Config.isCompileScheduler)
			generateGetName(w) ;
		
		generateGetChannelCount(w);
		//generateStateId(w);
		
		//if(!"never".equals(getName())){
		if (!Config.isCompileScheduler) {
			generateOverridenMethods(w);
			generateProcId(w);
		}
		//}
		if (enabler != null) {
			// SJTODO
		}

		w.outdent();
		w.appendLine("}");
	}

	protected void generateInitProcessForScheduler(final StringWriter w) throws ParseException {
		automaton.generateInitProcessForScheduler(w);
	}
	
	protected void generateError(final StringWriter w) throws ParseException {
		automaton.generateError(w);
	}
	
	protected void generateConstructor(final StringWriter w) throws ParseException {
		w.appendLine("public ", getName(), "(boolean decoding, int pid) {").indent();
		{
			if (specification.getNever() == this) {
				w.appendLine("super(", getSpecification().getName(), "Model.this, 0, new State[",
					automaton.size(), "], ", automaton.getStartState().getStateId(), ");");
			} else {
				if (specification.getSporadic() == this) {			
					w.appendLine("super(", getSpecification().getName(), "Model.this, 0, new State[",
					automaton.size(), "], ", automaton.getStartState().getStateId(), ");");
				} else {
					w.appendLine("super(", getSpecification().getName(), "Model.this, pid, new State[",
					automaton.size(), "], ", automaton.getStartState().getStateId(), ");");
				}
			}
			w.appendLine();
			// Generate the table
			automaton.generateTable(w);
			w.outdent().appendLine("}").appendLine();
		}

		w.appendLine("public ", getName(), "(", getArgs(), ") throws ValidationException {")
				.indent();
		{
			w.appendLine("this(false, _nrProcs);");
			w.appendLine();
			// Initialize default values for non arguments
			for (final Variable var : varStore.getVariables()) {
				if (Config.isCompileScheduler && StringUtil.containSchedulerKeyword(var.getName()))
					continue ;
				if (!arguments.contains(var)) {
					var.printInitExpr(w);
				}
			}
			if (!arguments.isEmpty()) {
				w.appendLine();
				
				// Store arguments
				for (final Variable var : arguments) {
					w.appendLine("this.", var.getName(), " = param_", var.getName(), ";");
				}
			}
			w.outdent().appendLine("}").appendLine();
		}
		
		if (! getArgs().equals("")) {
			//generate default constructor
			w.appendLine("public ", getName(), "() throws ValidationException {")
			.indent();
			{
				w.appendLine("this(false, _nrProcs);");
				w.appendLine();
				// Initialize default values for non arguments
				for (final Variable var : varStore.getVariables()) {
					if (Config.isCompileScheduler && StringUtil.containSchedulerKeyword(var.getName()))
						continue ;
					if (!arguments.contains(var)) {
						var.printInitExpr(w);
					}
				}				
				w.outdent().appendLine("}").appendLine();
			}
		}
	}

	protected void generateLocalVars(final StringWriter w) {
		for (final Variable var : varStore.getVariables()) {
			if (Config.isCompileScheduler && 
					StringUtil.containSchedulerKeyword(var.getName()))
				continue ; //define _pid is redundancy, _time_count is defined in scheduler
			w.appendLine("protected ", var.getType().getJavaName(), (var.getArraySize() > -1 ? "[]" : ""),
				" ", var.getName(), ";");
		}
		w.appendLine();
	}

	/*
	 * This part is the implementation of the VariableContainer that this proctype is...
	 */

	protected void generateStorable(final StringWriter w) {
		// The getSize function
		w.appendLine("public int getSize() {");
		if (Config.isCompileScheduler)
			w.appendLine("  return " + (varStore.getBufferSize() + 2 - 2 - 1) + ";"); //2: _pc (obmit), 1: _pid
//			w.appendLine("  return " + (varStore.getBufferSize() + 2 - 1) + ";"); //2: _pc (obmit)
		else
			w.appendLine("  return " + (varStore.getBufferSize() + 2) + ";"); //1: process position
		w.appendLine("}");
		w.appendLine();

		// The store function
		// w.appendLine("public int encode(byte[] _buffer, int _cnt) {");
		// w.indent();
		// w.appendLine("_buffer[_cnt++] = 0x", Integer.toHexString(id), ";");
		// w.appendLine("_buffer[_cnt++] = (byte)_sid;");
		// varStore.printEncode(w);
		// w.appendLine("return _cnt;");
		// w.outdent();
		// w.appendLine("}");
		// w.appendLine();
		w.appendLine("public void encode(DataWriter _writer) {");
		w.indent();
		if(id!=-1){//HIL:06/23/2015: to fix the array index exception. trying to encode -1 in byte variable.
			w.appendLine("_writer.writeByte(0x", Integer.toHexString(id), ");");
		}else{
			w.appendLine("_writer.writeByte(0x", Integer.toHexString(255), ");");
		}
		w.appendLine("_writer.writeByte(_sid);");
		varStore.printEncode(w);
		w.outdent();
		w.appendLine("}");
		w.appendLine();

		// The restore function
		// w.appendLine("public int decode(byte[] _buffer, int _cnt) {");
		// w.indent();
		// w.appendLine("if(_buffer[_cnt++] != 0x", Integer.toHexString(id), ") return -1;");
		// w.appendLine("_sid = _buffer[_cnt++] & 0xff;");
		// varStore.printDecode(w);
		// w.appendLine("return _cnt;");
		// w.outdent();
		// w.appendLine("}");
		// w.appendLine();
		w.appendLine("public boolean decode(DataReader _reader) {");
		w.indent();
		if(id!=-1){//HIL:06/23/2015: to fix the array index exception. trying to decode the  -1 from byte variable.
		w.appendLine("if(_reader.readByte() != 0x", Integer.toHexString(id), ") return false;");
		}else{
			w.appendLine("if(_reader.readByte() != 0x",Integer.toHexString(255), ") return false;");
		}
		w.appendLine("_sid = _reader.readByte();");
		varStore.printDecode(w);
		w.appendLine("return true;");
		w.outdent();
		w.appendLine("}");
		w.appendLine();
	}

	protected void generateToStringNeverClaim(final StringWriter w) {
		// The toString function
		w.appendLine("public String toString() {");
		w.indent();
		w.appendLine("StringBuilder sb = new StringBuilder();");
		w.appendLine("if(_exclusive ==  get_pid()) sb.append(\"<atomic>\");");
		w.appendLine("sb.append(\"" + getName() + " (\" +  get_pid() + \",\" + _sid + \"): \");");
		varStore.printToString(w);		
		w.appendLine("return sb.toString();");
		w.outdent();
		w.appendLine("}");
		w.appendLine();
	}
	protected void generateToStringSporadic(final StringWriter w) {
		// The toString function
		w.appendLine("public String toString() {");
		w.indent();
		w.appendLine("StringBuilder sb = new StringBuilder();");
		w.appendLine("if(_exclusive ==  get_pid()) sb.append(\"<atomic>\");");
		w.appendLine("sb.append(\"" + getName() + " (\" +  get_pid() + \",\" + _sid + \"): \");");
		varStore.printToString(w);
		w.appendLine("return sb.toString();");
		w.outdent();
		w.appendLine("}");
		w.appendLine();
	}
	protected void generateToString(final StringWriter w) {
		// The toString function
		w.appendLine("public String toString() {");
		w.indent();
		w.appendLine("StringBuilder sb = new StringBuilder();");
		w.appendLine("if(_exclusive ==  _pid) sb.append(\"<atomic>\");");
		w.appendLine("sb.append(\"" + getName() + " (\" +  _pid + \",\" + _sid + \"): \");");
		varStore.printToString(w);
		w.appendLine("return sb.toString();");
		w.outdent();
		w.appendLine("}");
		w.appendLine();
	}
	protected void generateGetChannelCount(final StringWriter w) {
		w.appendLine("public int getChannelCount() {");
		w.indent();
		w.appendLine("return ", varStore.getChannelCount(), ";");
		w.outdent();
		w.appendLine("}");
	}
//	protected void generateStateId(final StringWriter w) {
//		w.appendLine("protected int get_sid() {return _sid;}");
//		w.appendLine("protected void set_sid(int _sid) {this._sid = _sid;}");
//	}
	protected void generateProcId(final StringWriter w) {
		w.appendLine("@Override");
		w.appendLine("public int get_pid() {return _pid;}");
		w.appendLine("@Override");
		w.appendLine("public void set_pid(int _pid) {this._pid = _pid;}");
	}
	protected void generateOverridenMethods(final StringWriter w) {
		w.appendLine("@Override");
		w.appendLine("public int getId() {return _pid;}");
		//w.appendLine("public final State getCurrentState() { return _stateTable[_sid]; 	}");
	}
	protected String getArgs() {
		final StringWriter w = new StringWriter();
		for (final Variable var : arguments) {
			w.append(var == arguments.get(0) ? "" : ", ")
					.append(var.getType().getJavaName())
					.append(" param_")
					.append(var.getName());
		}
		return w.toString();
	}

	/**
	 * @return The Automaton that describes the actions of this {@link Proctype}
	 */
	public Automaton getAutomaton() {
		return automaton;
	}

	/**
	 * @return The name of this {@link Proctype}.
	 */
	public String getName() {
		return name;
	}

	/* Here all the generating code is places */

	/**
	 * @return The number of active processes of this type that have to be started when the model
	 *         checking starts.
	 */
	public int getNrActive() {
		return nrActive;
	}

	/**
	 * @return The {@link Specification} to which this {@link Proctype} belongs.
	 */
	public Specification getSpecification() {
		return specification;
	}

	/**
	 * @return The starting node which points to the complete graph with all the options.
	 */
	public State getStartState() {
		return automaton.getStartState();
	}

	/**
	 * @see spinja.promela.compiler.variable.VariableContainer#getVariable(java.lang.String)
	 */
	public Variable getVariable(final String name) {
		return varStore.getVariable(name);
	}

	/**
	 * @see spinja.promela.compiler.variable.VariableContainer#getVariables()
	 */
	public List<Variable> getVariables() {
		return varStore.getVariables();
	}

	/**
	 * @see spinja.promela.compiler.variable.VariableContainer#hasVariable(java.lang.String)
	 */
	public boolean hasVariable(final String name) {
		return varStore.hasVariable(name);
	}

	/* Exclusive send and read functions */

	/**
	 * @param var
	 *            The Variable that has to be tested.
	 * @return True when the given variable is set to be exclusively read by this {@link Proctype}.
	 */
	public boolean isXR(final Variable var) {
		return channelXR.contains(var);
	}

	/**
	 * @param var
	 *            The Variable that has to be tested.
	 * @return True when the given variable is set to be exclusively send by this {@link Proctype}.
	 */
	public boolean isXS(final Variable var) {
		return channelXS.contains(var);
	}

	/**
	 * Indicates that all variables that are added from now on are no longer arguments of this
	 * {@link Proctype}.
	 */
	public void lastArgument() {
		isArgument = false;
	}

	public boolean isArgument() {
		return isArgument;
	}

	/**
	 * Changes the enabler expression of the process.
	 * 
	 * @param enabler
	 *            The enabler expression.
	 */
	public void setEnabler(final Expression enabler) {
		this.enabler = enabler;
	}

	public Expression getEnabler() {
		return enabler;
	}

	/**
	 * Changes the priority of the process.
	 * 
	 * @param priority
	 *            The new priority.
	 */
	public void setPriority(final int priority) {
		this.priority = priority;
	}

	/**
	 * Returns the name of the process.
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return getName();
	}

	public int getID() {
		return id;
	}

	public List<Variable> getArguments() {
		return arguments;
	}

	public void setNrActive(int nrActive) {
		this.nrActive = nrActive;
	}

	List<ProcInstance> instances = new ArrayList<ProcInstance>();

	public void addInstance(ProcInstance instance) {
		instances.add(instance);
	}

	public List<ProcInstance> getInstances() {
		return instances;
	}

	
	//get the process name
    protected void generateGetName(final StringWriter w) {		
		w.appendLine("public String getName() {");
		w.indent();
		w.appendLine("return \"" + this.name + "\" ;") ;
	    w.outdent();
		w.appendLine("}");
		w.appendLine();
	}

	
}
