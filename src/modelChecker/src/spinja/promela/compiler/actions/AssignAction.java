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
import java.util.Collection;
import java.util.List;

import spinja.promela.compiler.Proctype;
import spinja.promela.compiler.automaton.Automaton;
import spinja.promela.compiler.expression.AritmicExpression;
import spinja.promela.compiler.expression.Expression;
import spinja.promela.compiler.expression.Identifier;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.PromelaConstants;
import spinja.promela.compiler.parser.Token;
import spinja.promela.compiler.variable.VariableAccess;
import spinja.util.StringUtil;
import spinja.util.StringWriter;
import sspinja.Run;

public class AssignAction extends Action {
	private final Identifier id;

	private Expression expr;

	public AssignAction(final Token token, final Identifier id) {
		this(token, id, null);
	}

	public AssignAction(final Token token, final Identifier id, final Expression expr) {
		super(token);
		this.id = id;
		this.expr = expr;
		if (expr != null) {
			for (final VariableAccess var : expr.readVariables()) {
				var.getVar().setRead(true);
			}
		}

		id.getVariable().setWritten(true);
		if ((getToken().kind == PromelaConstants.INCR)
			|| (getToken().kind == PromelaConstants.DECR)) {
			id.getVariable().setRead(true);
		}
	}

	@Override
	public String getEnabledExpression() {
		return null;
	}

	public Expression getExpr() {
		return expr;
	}

	@Override
	public boolean isLocal(final Proctype proc) {
		if (expr != null && !(new ExprAction(expr)).isLocal(proc))
		    return false;
        return proc.hasVariable(id.getVariable().getName()) && super.isLocal(proc);
	}

	@Override
	public Collection<Identifier> getChangedVariables() {
		List<Identifier> res = new ArrayList<Identifier>(1);
		res.add(id);
		return res;
	}

	@Override
	public void printTakeStatement(final StringWriter w) throws ParseException {
		final String mask = id.getVariable().getType().getMask();
		switch (getToken().kind) {
			case PromelaConstants.ASSIGN:
				try {					
					int value = expr.getConstantValue();
					if (id.toString().contains("."))
						w.appendLine(id.toString(), " = ", value, ";");
					else
						w.appendLine(id.getIntExpression(), " = ", value
																& id.getVariable()
																		.getType()
																		.getMaskInt(), ";");
				} catch (ParseException ex) {
					// Could not get Constant value
					String l, r ;
					if (id.toString().contains(".")) {
						l = id.toString() ;
					} else {
						l = id.getIntExpression() ;
					}
					if (expr instanceof Identifier && expr.toString().contains(".") )
						r = expr.toString();
					else 
						r = expr.getIntExpression() ;
					
					w.appendLine(l, " = ", r, (mask == null ? "" : " & " + mask), ";");
				}
				break;
			case PromelaConstants.INCR:
				if (id.toString().contains(".")) {
					w.appendLine(id.toString(), "++;");
				} else {
					if (mask == null) {
						w.appendLine(id.getIntExpression(), "++;");
					} else {
						w.appendLine(id.getIntExpression(), " = (", id.getIntExpression(), " + 1) & ",
							mask, ";");
					}
				}
				break;
			case PromelaConstants.DECR:
				if (id.toString().contains(".")) {
					w.appendLine(id.toString(), "--;");
				} else {
					if (mask == null) {
						w.appendLine(id.getIntExpression(), "--;");
					} else {
						w.appendLine(id.getIntExpression(), " = (", id.getIntExpression(), " - 1) & ",
							mask, ";");
					}
				}
				break;
			default:
				throw new ParseException("unknown assignment type");
		}
	}

	@Override
	public void printUndoStatement(final StringWriter w) throws ParseException {
		switch (getToken().kind) {
			case PromelaConstants.ASSIGN:
				if (expr.getSideEffect() != null) {
					w.appendLine("endProcess();");
				}
			case PromelaConstants.INCR:
			case PromelaConstants.DECR:
				break;
			default:
				throw new ParseException("unknown assignment type");
		}
	}

	@Override
	public String toString() {
		switch (getToken().kind) {
			case PromelaConstants.ASSIGN:
				return id.toString() + " = " + expr.toString();
			case PromelaConstants.INCR:
				return id.toString() + "++";
			case PromelaConstants.DECR:
				return id.toString() + "--";
			default:
				return "unknown assignment type";
		}
	}

	public Identifier getIdentifier() {
		return id;
	}

	public void setExpr(AritmicExpression calc) {
		expr = calc;
	}

	@Override
	public int getUselessWeight() {
		if (Automaton.varList.containsKey(id.toString()))
			return Automaton.varList.get(id.toString());
		return 2; //not sure useless or not
	}

	@Override
	public boolean getVarErrorAffect(String varName) {
		// TODO Auto-generated method stub
		if (Automaton.varList.containsKey(id.toString())) {
			if (expr != null)
				if (expr.getVarErrorAffect(varName))
					return true ;
		}
		return false;
	}
}
