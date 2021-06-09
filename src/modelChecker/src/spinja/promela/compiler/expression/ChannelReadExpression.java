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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import spinja.promela.compiler.automaton.Automaton;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.Token;
import spinja.promela.compiler.variable.VariableAccess;
import spinja.promela.compiler.variable.VariableType;
import spinja.util.StringWriter;

public class ChannelReadExpression extends Expression implements CompoundExpression {
	private final Identifier id;

	private final List<Expression> exprs;
	
	private final boolean random; 

	public ChannelReadExpression(final Token token, final Identifier id, boolean random) {
		super(token);
		this.id = id;
		this.random = random;
		exprs = new ArrayList<Expression>();
	}

	public void addExpression(final Expression expr) {
		exprs.add(expr);
		if (!(expr instanceof Identifier)) {
			for (final VariableAccess va : expr.readVariables()) {
				va.getVar().setRead(true);
			}
		} else {
			((Identifier) expr).getVariable().setWritten(true);
		}
	}

	@Override
	public String getBoolExpression() throws ParseException {
		return "_channels[SPINJA].length()"; //SJTODO: SpinJa semantics
	}

	@Override
	public String toString() {
		final StringWriter w = new StringWriter();
		w.append(id).append("?");
		for (final Expression expr : exprs) {
			w.append(expr.toString()).append(",");
		}
		w.setLength(w.length() - 1);
		return w.toString();
	}

	public Identifier getIdentifier() {
		return id;
	}

	public List<Expression> getExprs() {
		return exprs;
	}

	@Override
	public VariableType getResultType() {
		return VariableType.BOOL;
	}

	@Override
	public Set<VariableAccess> readVariables() {
		final Set<VariableAccess> rv = new HashSet<VariableAccess>();
		for (final Expression e : exprs)
			rv.addAll(e.readVariables());
		rv.addAll(id.readVariables());
		return rv;
	}

	public boolean isRandom() {
		return random;
	}

	
	@Override
	public int getUselessWeight() {
		if (Automaton.varList.containsKey(id))
			return Automaton.varList.get(id);
		for (Expression expr : exprs)
			if (expr.getUselessWeight() == 1)
				return 1 ;
		return 2;
	}

	@Override
	public boolean getVarErrorAffect(String varName) {
		// TODO Auto-generated method stub
		return false;
	}
}
