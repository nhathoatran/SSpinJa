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

/**
 * The run expression represents the starting of a new proctype. The run expression can be used to
 * read the number of the proctype that has be created (and is therefor the only expression with a
 * sideeffect).
 * 
 * @author Marc de Jonge
 */
public class RunExpression extends Expression implements CompoundExpression {

	private final String id;

	private final Proctype proc;

	private final List<Expression> exprs;

	private ProcInstance instance = null;

	/**
	 * Creates a new RunExpression using the identifier specified to run the proctype.
	 * 
	 * @param token
	 *            The token that is stored for debug reasons.
	 * @param id
	 *            The name of the proctype that is to be started.
	 */
	public RunExpression(final Token token, final String id) {
		super(token);
		this.id = id;
		this.proc = null;
		exprs = new ArrayList<Expression>();
	}

	public RunExpression(final Token token, final Proctype proc) {
		super(token);
		this.id = proc.getName();
		this.proc = proc;
		exprs = new ArrayList<Expression>();
	}

	public void addExpression(final Expression expr) throws ParseException {
		exprs.add(expr);
	}

	private String getArgs() throws ParseException {
		final StringWriter w = new StringWriter();
		for (final Expression expr : exprs) {
			if (expr.toString().contains("."))
				w.append(expr == exprs.get(0) ? "" : ", ").append(expr.toString());
			else
				w.append(expr == exprs.get(0) ? "" : ", ").append(expr.getIntExpression());
		}
		return w.toString();
	}

	@Override
	public String getBoolExpression() {
		return "true";
	}

	@Override
	public String getIntExpression() throws ParseException {
		ProcInstance instance = getInstances().get(0);
		StringBuilder build=new StringBuilder("");
		build.append("addProcess(new " + instance.getName() + "(" + getArgs() + "))");
		return build.toString();//HIL 05/26/2015:elevator.3.prom error fix
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
		for (final Expression expr : exprs) {
			rv.addAll(expr.readVariables());
		}
		return rv;
	}

	@Override
	public String toString() {
		ProcInstance instance = getInstances().get(0);
		
		try {
			return "run " + instance.getName() + "(" + getArgs() + ")";
		} catch (final Exception ex) {
			return "run " + instance.getName() + "()";
		}
	}

	public String getId() {
		return id;
	}

	public Proctype getProctype() {
		return proc;
	}

	public List<Expression> getExpressions() {
		return exprs;
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
