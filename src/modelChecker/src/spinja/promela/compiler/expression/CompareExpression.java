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

import spinja.promela.compiler.parser.MyParseException;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.PromelaConstants;
import spinja.promela.compiler.parser.Token;
import spinja.promela.compiler.variable.VariableAccess;
import spinja.promela.compiler.variable.VariableType;

/**
 * The compare expression can be any expression that compares to integer values and returns a
 * boolean.
 * 
 * @author Marc de Jonge
 */
public class CompareExpression extends Expression {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -7625932622450298223L;

	private final Expression ex1, ex2;

	/**
	 * Creates a new BooleanExpression from two subexpressions.
	 * 
	 * @param token
	 *            The token that is used to determen what kind of calculation is does.
	 * @param left
	 *            The left part of the expression
	 * @param right
	 *            The right part of the expression.
	 * @throws ParseException
	 *             When something went wrong while creating the net AritmicExpression.
	 */
	public CompareExpression(final Token token, final Expression left, final Expression right) {
		super(token);
		ex1 = left;
		ex2 = right;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof CompareExpression))
			return false;
		CompareExpression ce = (CompareExpression)o;
		return getToken().kind == ce.getToken().kind &&
				ex1.equals(ce.ex1) && ex2.equals(ce.ex2);
	}
	
	@Override
	public String getBoolExpression() throws ParseException {
		if (ex1 instanceof Identifier && ex1.toString().contains("."))
			if (ex2 instanceof Identifier && ex1.toString().contains("."))
				return "(" + ex1.toString() + " " + getToken().image + " " + ex2.toString()	+ ")";
			else
				return "(" + ex1.toString() + " " + getToken().image + " " + ex2.getIntExpression()	+ ")";
		
		return "(" + ex1.getIntExpression() + " " + getToken().image + " " + ex2.getIntExpression()	+ ")";
	}
	
	

	public String getDistanceExpression() throws ParseException {
		switch (getToken().kind) {			
			case PromelaConstants.LT:
				return "(" + ex1.getIntExpression() + " - " + ex2.getIntExpression() + ") < 0 ? 0 : (" +  ex1.getIntExpression() + " - " + ex2.getIntExpression() + ")";
			case PromelaConstants.GT:
				return "(" + ex1.getIntExpression() + " - " + ex2.getIntExpression() + ") > 0 ? 0 : (" +  ex2.getIntExpression() + " - " + ex1.getIntExpression() + ")";
			case PromelaConstants.LTE:
				return "(" + ex1.getIntExpression() + " - " + ex2.getIntExpression() + ") <= 0 ? 0 : (" +  ex1.getIntExpression() + " - " + ex2.getIntExpression() + ")";
			case PromelaConstants.GTE:
				return "(" + ex1.getIntExpression() + " - " + ex2.getIntExpression() + ") >= 0 ? 0 : (" +  ex2.getIntExpression() + " - " + ex1.getIntExpression() + ")";
			case PromelaConstants.EQ:
				return "(Math.abs(" + ex1.getIntExpression() + " - " + ex2.getIntExpression() + "))" ; // == 0 ? 0 : 1 )";
			case PromelaConstants.NEQ:
				return "(Math.abs(" + ex1.getIntExpression() + " - " + ex2.getIntExpression() + ") == 0 ? 1 : 0 )";			
		}	throw new MyParseException("Unimplemented compare type: " + getToken().image, getToken());
	}
	
	public CompareExpression convertAssertExpression() throws ParseException{		
		switch (getToken().kind) {			
			case PromelaConstants.LT:
				return new CompareExpression(new Token(PromelaConstants.GTE, ">="), ex1, ex2) ;				
			case PromelaConstants.GT:
				return new CompareExpression(new Token(PromelaConstants.LTE, "<="), ex1, ex2) ;				
			case PromelaConstants.LTE:
				return new CompareExpression(new Token(PromelaConstants.GT, ">"), ex1, ex2) ;
			case PromelaConstants.GTE:
				return new CompareExpression(new Token(PromelaConstants.LT, "<"), ex1, ex2) ;
			case PromelaConstants.EQ:
				return new CompareExpression(new Token(PromelaConstants.NEQ, "!="), ex1, ex2) ;				
			case PromelaConstants.NEQ:
				return new CompareExpression(new Token(PromelaConstants.EQ, "=="), ex1, ex2) ;			
		}	throw new MyParseException("Unimplemented compare type: " + getToken().image, getToken());		
	}
	
	@Override
	public int getConstantValue() throws ParseException {
		switch (getToken().kind) {
			case PromelaConstants.LT:
				return ex1.getConstantValue() < ex2.getConstantValue() ? 1 : 0;
			case PromelaConstants.GT:
				return ex1.getConstantValue() > ex2.getConstantValue() ? 1 : 0;
			case PromelaConstants.LTE:
				return ex1.getConstantValue() <= ex2.getConstantValue() ? 1 : 0;
			case PromelaConstants.GTE:
				return ex1.getConstantValue() >= ex2.getConstantValue() ? 1 : 0;
			case PromelaConstants.EQ:
				return ex1.getConstantValue() == ex2.getConstantValue() ? 1 : 0;
			case PromelaConstants.NEQ:
				return ex1.getConstantValue() != ex2.getConstantValue() ? 1 : 0;
		}
		throw new MyParseException("Unimplemented compare type: " + getToken().image, getToken());
	}

	public void getVariableList() {
		switch (getToken().kind) {
			case PromelaConstants.LT:
			case PromelaConstants.GT:
			case PromelaConstants.LTE:
			case PromelaConstants.GTE:
			case PromelaConstants.EQ:
			case PromelaConstants.NEQ:
				ex1.getErrorVariableList() ;				
				if (ex2 != null) ex2.getErrorVariableList() ;
		}		
	}
	
	@Override
	public String getIntExpression() throws ParseException {
		return "(" + ex1.getIntExpression() + " " + getToken().image + " " + ex2.getIntExpression()
				+ " ? 1 : 0)";
	}

	@Override
	public VariableType getResultType() {
		return VariableType.BOOL;
	}

	@Override
	public String getSideEffect() {
		if ((ex1.getSideEffect() != null) || (ex2.getSideEffect() != null)) {
			return "size effect!";
		}
		return null;
	}

	@Override
	public Set<VariableAccess> readVariables() {
		final Set<VariableAccess> rv = new HashSet<VariableAccess>();
		rv.addAll(ex1.readVariables());
		rv.addAll(ex2.readVariables());
		return rv;
	}

	@Override
	public String toString() {
		return "(" + ex1 + " " + getToken().image + " " + ex2
					+ ")";
	}

	public Expression getExpr1() {
		return ex1;
	}
	public Expression getExpr2() {
		return ex2;
	}

	@Override
	public int getUselessWeight() {
		return 0 ;
		//return ex1.getUselessWeight() + ex2.getUselessWeight() == 0 ? 0 : 2;
	}

	@Override
	public boolean getVarErrorAffect(String varName) {
		// TODO Auto-generated method stub
		return false;
	}
}
