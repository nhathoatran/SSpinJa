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

import spinja.promela.compiler.automaton.Automaton;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.Token;
import spinja.promela.compiler.variable.Variable;
import spinja.promela.compiler.variable.VariableAccess;
import spinja.promela.compiler.variable.VariableType;

/**
 * An Identifiers is a reference to a variable. It can possibly also contain an expression which
 * calculated the number in the array of values it must have.
 * 
 * @author Marc de Jonge
 */
public class Identifier extends Expression {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -5928789117017713005L;

	private Variable var = null;

	private Expression arrayExpr = null;

	private int instanceIndex = -1; // no instance index 

	private Identifier sub = null;

	public Identifier getSub() {
		return sub;
	}

	public void setSub(Identifier sub) {
		this.sub = sub;
	}

	/**
	 * Creates a new Identifier that refers to the specified variable.
	 * 
	 * @param token
	 *            The token that is stored for debug reasons.
	 * @param var
	 *            The variable to which this identifier points.
	 */
	public Identifier(final Token token, final Variable var, Identifier sub) {
		super(token);
		this.var = var;
		arrayExpr = null;
		this.sub = sub;
	}

	/**
	 * Creates a new Identifier that refers to the specified variable on a specified place in the
	 * array (calculated using the expression).
	 * 
	 * @param token
	 *            The token that is stored for debug reasons.
	 * @param var
	 *            The variable to which this identifier points.
	 * @param arrayExpr
	 *            The expression that calculates the index in the array.
	 */
	public Identifier(final Token token, final Variable var, final Expression arrayExpr,
			Identifier sub) {
		super(token);
		this.var = var;
		this.arrayExpr = arrayExpr;
		this.sub = sub;
	}

	public Identifier(final Variable var) {
		super(null);
		this.var = var;
		arrayExpr = null;
		this.sub = null;
	}

	public Identifier(Identifier id, Identifier sub) {
		this(id.getToken(), id.var, id.getArrayExpr(), sub);
	}

	public Identifier(Identifier id, Variable sub) {
		this(id.getToken(), id.var, id.getArrayExpr(), new Identifier(sub));
	}

	public int getConstantValue() throws ParseException {
		return var.getConstantValue();
	}

	/**
	 * @return The expression that is used for the array
	 */
	public Expression getArrayExpr() {
		return arrayExpr;
	}

	@Override
	public String getIntExpression() throws ParseException {		
		if (var.getArraySize() > -1) {
			if (arrayExpr != null) {				
				return var.getName() + "[" + arrayExpr.getIntExpression() + "]";
			} else {
				assert (false);
				return var.getName() + "[0]";
			}
		} else {			
			if (var.toString().contains("."))
				return var.toString() ;
			return var.getName();
		}
	}

	public void getVariableList() {
		String varName = var.getName() ;
		if (! Automaton.varList.containsKey(varName))
			Automaton.varList.put(varName, 2) ;
		/*
		 * 0: useless
		 * 1: useful
		 * 2: not know
		 */
	}
	
	@Override
	public VariableType getResultType() {
		return var.getType();
	}

	@Override
	public String getSideEffect() {
		if (arrayExpr != null) {
			return arrayExpr.getSideEffect();
		} else {
			return null;
		}
	}

	/**
	 * @return The variable to which this identifier points.
	 */
	public Variable getVariable() {
		return var;
	}

	@Override
	public Set<VariableAccess> readVariables() {
		final Set<VariableAccess> set = new HashSet<VariableAccess>();
		set.add(new VariableAccess(var, arrayExpr));
		return set;
	}

	@Override
	public String toString() {
		String res = "";
        if (getInstanceIndex() != -1) {
            res += var.getOwner().getName();
            res += "[" + getInstanceIndex() + "]";
            res += ":";
            res += var.getRealName();
            if (var.getArraySize() > -1) {
                if (arrayExpr != null) {
                    res += "[" + arrayExpr.toString() + "]";
                } else {
                    assert (false);
                    res += "[0]";
                }
            }
            return res;
        }
		if (var.getArraySize() > -1) {
			if (arrayExpr != null) {
				res = var.toString() + "[" + arrayExpr.toString() + "]";
			} else {
				assert (false);
				res = var.toString() + "[0]";
			}
		} else {
			res = var.toString();
		}
		if (null != sub)
			res += "."+ sub.toString();
		return res;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Identifier)) 
			return false;
		Identifier oi = (Identifier)o;
		if (!var.equals(oi.var))
			return false;
		if (arrayExpr == null || oi.arrayExpr == null)
			return arrayExpr == oi.arrayExpr;
		return arrayExpr.equals(oi.arrayExpr);
	}

	/**
	 * Vor remote variable ref
	 * @return
	 */
    public int getInstanceIndex() {
        return instanceIndex;
    }

    public void setInstanceIndex(int instanceIndex) {
        this.instanceIndex = instanceIndex;
    }

	@Override
	public int getUselessWeight() {
		if (Automaton.varList.containsKey(var.getName()))
			return Automaton.varList.get(var.getName());
		else
			return 2 ; //useless
	}

	@Override
	public boolean getVarErrorAffect(String varName) {
		// TODO Auto-generated method stub
		if (var.getName().equals(varName))
			return true ;
		return false;
	}
}
