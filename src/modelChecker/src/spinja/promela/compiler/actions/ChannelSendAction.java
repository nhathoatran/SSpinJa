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
import java.util.Collections;
import java.util.List;

import spinja.promela.compiler.Proctype;
import spinja.promela.compiler.automaton.Automaton;
import spinja.promela.compiler.expression.CompoundExpression;
import spinja.promela.compiler.expression.Expression;
import spinja.promela.compiler.expression.Identifier;
import spinja.promela.compiler.expression.MTypeReference;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.Token;
import spinja.promela.compiler.variable.ChannelVariable;
import spinja.promela.compiler.variable.Variable;
import spinja.promela.compiler.variable.VariableAccess;
import spinja.util.StringWriter;

public class ChannelSendAction extends Action implements CompoundExpression {
	private final Identifier id;

	private final List<Expression> exprs;

	public ChannelSendAction(final Token token, final Identifier id) {
		super(token);
		this.id = id;
		exprs = new ArrayList<Expression>();
	}

	public void addExpression(final Expression expr) {
		exprs.add(expr);
		for (final VariableAccess va : expr.readVariables()) {
			va.getVar().setRead(true);
		}
	}

	public boolean isRendezVous() {
		Variable v = id.getVariable();
		if (!(v instanceof ChannelVariable)) throw new AssertionError("Channel operation on non-channel "+ id);
		ChannelVariable cv = (ChannelVariable)v;
		return cv.getType().isRendezVous();
	}

	@Override
	public String getEnabledExpression() {
		return id + " != -1 && !_channels[" + id + "].isRendezVous() && _channels[" + id
				+ "].canSend()";
	}

	@Override
	public boolean isLocal(final Proctype proc) {
		if (!proc.isXS(id.getVariable())) {
			return false;
		}
		for (final Expression expr : exprs) {
			for (final VariableAccess va : expr.readVariables()) {
				if (!proc.hasVariable(va.getVar().getName())) {
					return false;
				}
			}
		}
		return super.isLocal(proc);
	}

	@Override
	public void printExtraFunctions(final StringWriter w) {
		w.appendLine("public int[] getRendezvous() {");
		w.indent();
		w.appendLine("if(!_channels[", id, "].isRendezVous()) return null;");
		w.appendPrefix().append("return new int[]{").append(id);
		for (final Expression expr : exprs) {
			if(expr instanceof MTypeReference){//HIL 01/07/2015: To fix the compile error in generated promela model.
				w.append(", ").append(((MTypeReference)expr).getNumber());
			}else{
				w.append(", ").append(expr.toString());
			}
		}
		w.append("};").appendPostfix();
		w.outdent();
		w.appendLine("}");
		w.appendLine();
	}

	@Override
	public void printTakeStatement(final StringWriter w) throws ParseException {
		w.appendPrefix().append("_channels[").append(id).append("].send(");
		for (final Expression expr : exprs) {
			w.append(expr.getIntExpression()).append(", ");
		}
		w.setLength(w.length() - 2);
		w.append(");").appendPostfix();
	}

	@Override
	public List<Identifier> getChangedVariables() {
		return Collections.emptyList();
	}

	@Override
	public void printUndoStatement(final StringWriter w) {
		w.appendLine("_channels[", id, "].readLast();");
	}

	@Override
	public String toString() {
		final StringWriter w = new StringWriter();
		w.append(id).append("!");
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
