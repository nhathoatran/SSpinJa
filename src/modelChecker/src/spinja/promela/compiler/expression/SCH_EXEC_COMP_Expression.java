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

package spinja.promela.compiler.expression;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import spinja.promela.compiler.ProcInstance;
import spinja.promela.compiler.Proctype;
import spinja.promela.compiler.actions.Action;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.Token;
import spinja.promela.compiler.variable.VariableAccess;
import spinja.promela.compiler.variable.VariableType;
import spinja.util.StringWriter;


public class SCH_EXEC_COMP_Expression extends Expression implements CompoundExpression {
	
	private final String id;

	private final Proctype proc;

	private final List<Expression> schexprs;	
	private String runpara;

	private ProcInstance instance = null;

	/**
	 * Creates a new RunExpression using the identifier specified to run the proctype.
	 * 
	 * @param token
	 *            The token that is stored for debug reasons.
	 * @param id
	 *            The name of the proctype that is to be started.
	 */
	public SCH_EXEC_COMP_Expression(final Token token, final String id) {
		super(token);
		this.id = id;
		this.proc = null;
		schexprs = new ArrayList<Expression>();		
	}

	public SCH_EXEC_COMP_Expression(final Token token, final Proctype proc) {
		super(token);
		this.id = proc.getName();
		this.proc = proc;
		schexprs = new ArrayList<Expression>();
	}

	public void addExpression(final Expression expr) throws ParseException {
		schexprs.add(expr);
	}

	public void add_list_para(final String para) throws ParseException {
		runpara = para ;
	}
	
	private String getArgs() throws ParseException {
		final StringWriter w = new StringWriter();
		for (final Expression expr : schexprs) {
			w.append(expr == schexprs.get(0) ? "" : ", ").append(expr.getIntExpression());
		}
		return w.toString();
	}
	
	/*private String getRunArgs() throws ParseException {
		final StringWriter w = new StringWriter();
		for (final Expression expr : runexprs) {
			w.append(expr == runexprs.get(0) ? "" : ", ").append(expr.getIntExpression());
		}
		return w.toString();
	}*/

	@Override
	public String getBoolExpression() {
		return "true";
	}

	@Override
	public String getIntExpression() throws ParseException {
		StringBuilder build=new StringBuilder("{ SchedulerProcess target = new SchedulerProcess(); ");
		build.append("target." + id + "(\"" + getArgs() + "\"); ") ;
		build.append("target.getRefID(\"" + id + "\");");
		build.append("addProcess(new " + id + "_0(" + runpara + "), target); } ") ;
		return build.toString();
	}

	@Override
	public VariableType getResultType() {
		return VariableType.PID;
	}

	@Override
	public String getSideEffect() {
	    try {
	        return getIntExpression();
	    } catch (ParseException e) {
	        throw new AssertionError();
	    }
	}

	@Override
	public Set<VariableAccess> readVariables() {
		final Set<VariableAccess> rv = new HashSet<VariableAccess>();
		for (final Expression expr : schexprs) {
			rv.addAll(expr.readVariables());
		}
		return rv;
	}

	@Override
	public String toString()  {		
		try {
			return "sch_exec_comp(" + id + "_0" + "(" + getArgs() +  ")(" + runpara + "))";
		} catch (ParseException e) {			
			e.printStackTrace();
		}
		return "" ;
	}

	public String getId() {
		return id;
	}

	public Proctype getProctype() {
		return proc;
	}

	public List<Expression> getExpressions() {
		return schexprs;
	}

	public void setInstance(ProcInstance pi) {
		instance = pi;
	}

	public List<ProcInstance> getInstances() {
		if (instance == null) return proc.getInstances();
		return Arrays.asList(instance);
	}

	private List<Action> actions = new ArrayList<Action>();

	public void addAction(Action action) {
		actions.add(action);
	}

	public List<Action> getActions() {
		return actions;
	}

	@Override
	public int getUselessWeight() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean getVarErrorAffect(String varName) {
		// TODO Auto-generated method stub
		return false;
	}
}
