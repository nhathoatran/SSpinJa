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

import java.util.HashSet;
import java.util.Set;

import spinja.promela.compiler.ProcInstance;
import spinja.promela.compiler.Proctype;
import spinja.promela.compiler.automaton.State;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.Token;
import spinja.promela.compiler.variable.Variable;
import spinja.promela.compiler.variable.VariableAccess;
import spinja.promela.compiler.variable.VariableType;

public class RemoteRef extends Expression {
	
	private String processName;
	private Proctype process;
	private String label;
	private Expression expr;

	public RemoteRef(final Token id, final Token label, Expression e) {
		super(id);
		this.processName = id.image;
		this.label = label.image;
		this.expr = e;
	}

	public RemoteRef(Token t, Proctype process, String label, Expression expr) {
		super(t);
		this.setProcess(process);
		this.processName = process.getName();
		this.label = label;
		this.expr = expr;
	}

	@Override
	public String getIntExpression() throws ParseException {
		if (null == expr)
			return processName +"@"+ label;
		return processName +"["+ expr.getIntExpression() +"]@"+ label;
	}

	@Override
	public String toString() {
		if (null == expr)
			return processName +"@"+ label;
		return processName +"["+ expr.toString() +"]@"+ label;
	}

	public boolean equals(Object o) {
	    if (!(o instanceof RemoteRef))
	        return false;
	    RemoteRef other = (RemoteRef)o;
	    return processName.equals(other.processName) &&
	           label.equals(other.label) && 
	           (expr == other.expr ||
	           (expr != null && other.expr != null && expr.equals(other.expr)));
	}

	@Override
	public VariableType getResultType() {
		return VariableType.BOOL;
	}

	@Override
	public Set<VariableAccess> readVariables() {
		if (expr == null)
			return new HashSet<VariableAccess>();
		return expr.readVariables();
	}

	public Expression getExpr() {
		return expr;
	}

	public void setExpr(Expression expr) {
		this.expr = expr;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcess(String process) {
		this.processName = process;
	}

	public void setProcess(Proctype process) {
		this.process = process;
	}

	public Proctype getProcess() {
		return process;
	}

	private Variable pc = null;


	ProcInstance instance = null;
	public ProcInstance getInstance() {
		if (null != instance)
			return instance;
		if (1 == process.getInstances().size()) {
			instance = process.getInstances().get(0);
		} else if (null != getExpr()) {
			int val;
			try {
				val = getExpr().getConstantValue();
				for (ProcInstance i : process.getInstances()) {
					if (i.getID() == val) instance = i;
				}
			} catch (ParseException e1) {}
		}
		if (null == instance) throw new AssertionError("Cannot statically determine instance for "+ this);
		return instance;
	}

	int num = -1;
	public int getLabelId() {
		if (-1 != num)
			return num;
		getInstance();
		for (State s : instance.getAutomaton())
			if (s.hasLabel(getLabel())) num = s.getStateId();
		if (-1 == num)
			throw new AssertionError("Wrong label: "+ this +
								     " not found in proc "+ instance);
		return num;
	}


	@Override
	public int getUselessWeight() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public boolean getVarErrorAffect(String varName) {
		// TODO Auto-generated method stub
		return false;
	}
}
