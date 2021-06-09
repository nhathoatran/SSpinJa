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

package spinja.promela.compiler.actions;

import java.util.ArrayList;
import java.util.List;

import spinja.promela.compiler.Proctype;
import spinja.promela.compiler.automaton.Automaton;
import spinja.promela.compiler.expression.CompoundExpression;
import spinja.promela.compiler.expression.Expression;
import spinja.promela.compiler.expression.Identifier;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.Token;
import spinja.promela.compiler.variable.ChannelVariable;
import spinja.promela.compiler.variable.Variable;
import spinja.promela.compiler.variable.VariableAccess;
import spinja.util.StringWriter;

public class ChannelReadAction extends Action implements CompoundExpression {
	private final Identifier id;

	private final List<Expression> exprs;

	private final boolean poll;
	private final boolean random;
	
	public ChannelReadAction(final Token token, final Identifier id, boolean poll, boolean random) {
		super(token);
		this.id = id;
		exprs = new ArrayList<Expression>();
		this.poll = poll;
		this.random = random;
	}

	public ChannelReadAction(final Token token, final Identifier id) {
		this(token, id, false, false);
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
	public String getEnabledExpression() {
		return "false";
	}

	@Override
	public boolean isLocal(final Proctype proc) {
		if (!proc.isXR(id.getVariable())) {
			return false;
		}
		for (final Expression expr : exprs) {
			if (expr instanceof Identifier) {
				final Variable var = ((Identifier) expr).getVariable();
				if (!proc.hasVariable(var.getName())) {
					return false;
				}
			} else {
				for (final VariableAccess va : expr.readVariables()) {
					if (!proc.hasVariable(va.getVar().getName())) {
						return false;
					}
				}
			}
		}
		return super.isLocal(proc);
	}

	@Override
	public void printEnabledFunction(final StringWriter w) throws ParseException {
		w.appendLine("public boolean isEnabled() {");
		w.indent();
		w.appendLine("if(", id, " == -1 || _channels[", id, "].isRendezVous() || !_channels[",
			id, "].canRead()) {");
		w.indent();
		w.appendLine("return false;");
		w.outdent();
		w.appendLine("} else {");
		w.indent();

		w.appendLine("int[] _tmp = _channels[", id, "].peek();");
		w.appendLine("if(_tmp.length != ", exprs.size(),
			") throw new UnexpectedStateException(\"Channel returned the wrong number of variables\");");
		boolean first = true;
		for (int i = 0; i < exprs.size(); i++) {
			final Expression expr = exprs.get(i);
			if (!(expr instanceof Identifier)) {
				w.appendLine(first ? "return " : " && ", "_tmp[", i, "] == ",
					expr.getIntExpression());
				first = false;
			}
		}
		if (first) {
			w.appendLine("return true;");
		} else {
			w.removePostfix().append(";").appendPostfix();
		}
		w.outdent();
		w.appendLine("}");
		w.outdent();
		w.appendLine("}");
	}

	@Override
	public void printExtraFunctions(final StringWriter w) throws ParseException {
		w.appendLine();
		w.appendLine("public boolean canReadRendezvous(int[] _values) {");
		w.indent();
		w.appendLine("return _channels[", id, "].isRendezVous()");
		w.appendLine("         && _values.length == ", exprs.size() + 1);
		w.appendLine("         && _values[0] == ", id);
		for (int i = 0; i < exprs.size(); i++) {
			if (!(exprs.get(i) instanceof Identifier)) {
				w.appendLine("         && _values[", i + 1, "] == ", exprs.get(i)
						.getIntExpression());
			}
		}
		w.removePostfix().append(";").appendPostfix();
		w.outdent();
		w.appendLine("}");
		w.appendLine();
	}

	@Override
	public List<Identifier> getChangedVariables() {
		List<Identifier> res = new ArrayList<Identifier>(exprs.size());
		for (Expression expr : exprs) {
			if (expr instanceof Identifier) {
				res.add((Identifier) expr);
			}
		}
		return res;
	}

	@Override
	public void printTakeStatement(final StringWriter w) throws ParseException {
		w.appendLine("int[] _tmp = _channels[", id, "].read();");
		for (int i = 0; i < exprs.size(); i++) {
			final Expression expr = exprs.get(i);
			if (expr instanceof Identifier) {
				String mask = expr.getResultType().getMask();
				w.appendLine(expr.getIntExpression(), " = _tmp[", i, "]", mask == null	? ""
																						: " & "
																							+ mask,
					"; ");
			}
		}
	}

	@Override
	public void printUndoStatement(final StringWriter w) throws ParseException {
		w.appendPrefix().append("_channels[").append(id).append("].sendFirst(");
		for (final Expression expr : exprs) {
			w.append(expr.getIntExpression()).append(", ");
		}
		w.setLength(w.length() - 2);
		w.append(");").appendPostfix();
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

	public boolean isPoll() {
		return poll;
	}

	public boolean isNormal() {
		return !poll;
	}

	public boolean isRendezVous() {
		Variable v = id.getVariable();
		if (!(v instanceof ChannelVariable)) throw new AssertionError("Channel operation on non-channel "+ id);
		ChannelVariable cv = (ChannelVariable)v;
		return cv.getType().isRendezVous();
	}

	public boolean isRandom() {
		return random;
	}

	@Override
	public int getUselessWeight() {
		if (Automaton.varList.containsKey(id.toString()))
			return Automaton.varList.get(id.toString()); // useful
		else
			return 2; //not sure useless or not
	}

	@Override
	public boolean getVarErrorAffect(String varName) {
		// TODO Auto-generated method stub
		return false;
	}
}
