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
 * The boolean expression can be any expression that takes two boolean values and returns a boolean
 * value, e.g. the AND expression.
 * 
 * @author Marc de Jonge
 */
public class BooleanExpression extends Expression {
	@SuppressWarnings("unused")
	private static final long serialVersionUID = -4022528945025403911L;

	private final Expression ex1, ex2;

	/**
	 * Creates a new BooleanExpression from only one subexpression.
	 * 
	 * @param token
	 *            The token that is used to determen what kind of calculation is does.
	 * @param only
	 *            The only subexpression.
	 */
	public BooleanExpression(final Token token, final Expression only) {
		this(token, only, null);
	}

	/**
	 * Creates a new BooleanExpression from two subexpressions.
	 * 
	 * @param token
	 *            The token that is used to determen what kind of calculation is does.
	 * @param left
	 *            The left part of the expression
	 * @param right
	 *            The right part of the expression.
	 */
	public BooleanExpression(final Token token, final Expression left, final Expression right) {
		super(token);
		ex1 = left;
		ex2 = right;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (!(o instanceof BooleanExpression))
			return false;
		BooleanExpression ce = (BooleanExpression)o;
		return getToken().kind == ce.getToken().kind && ex1.equals(ce.getExpr1()) &&
				(ex2==null && ce.getExpr2()==null || ex2.equals(ce.getExpr2()));
	}
	
	@Override
	public String getBoolExpression() throws ParseException {
		if (ex2 == null) {
			if (ex1 instanceof Identifier && ex1.toString().contains("."))
				return "(" + getToken().image + " ("	+ ex1.toString() + " != 0))";
			return "(" + getToken().image + ex1.getBoolExpression() + ")";
		} else {
			String s1, s2 ;
			if (ex1 instanceof Identifier && ex1.toString().contains("."))
				s1 = "(" + ex1.toString() + " != 0)";
			else
				s1 = ex1.getBoolExpression() ;
			
			if (ex2 instanceof Identifier && ex2.toString().contains("."))
				s2 = "(" + ex2.toString() + " != 0)" ;
			else
				s2 = ex2.getBoolExpression() ;
			
			return "(" + s1 + " " + getToken().image + " " + s2 + ")";
		}
	}
	
	public void getVariableList() {
		ex1.getErrorVariableList() ;				
		if (ex2 != null) 
			ex2.getErrorVariableList();
	}
	
	/*
	 * Estimate the distance from expression
	 */
	public String getDistanceExpression() throws ParseException {
		if (ex2 == null) {
			if (getToken().kind == PromelaConstants.LNOT )
				return ex1.getDistanceExpression() ;
			else 
				return "(" + ex1.getDistanceExpression() + " == 0 ? 1 : 0)";
		} else {
			switch (getToken().kind) {
				case PromelaConstants.LNOT:
					return ex1.getDistanceExpression() ;
				case PromelaConstants.LOR:
					return "Math.min(" + ex1.getDistanceExpression() + ", " + ex2.getDistanceExpression() + ")";
				case PromelaConstants.LAND:
					return "Math.max(" + ex1.getDistanceExpression() + ", " + ex2.getDistanceExpression() + ")";
			}	throw new MyParseException("Unimplemented aritmic type: " + getToken().image, getToken());			
		}
	}

	public Expression convertAssertExpression() throws ParseException{
		switch (getToken().kind) {
			case PromelaConstants.LNOT:
				return ex1 ;
			case PromelaConstants.LOR:				
				return new BooleanExpression(new Token(PromelaConstants.LAND, "&&"), ex1.convertAssertExpression(), ex2.convertAssertExpression());
			case PromelaConstants.LAND:
				return new BooleanExpression(new Token(PromelaConstants.LOR, "||"), ex1.convertAssertExpression(), ex2.convertAssertExpression());
		}	
		throw new MyParseException("Unimplemented aritmic type: " + getToken().image, getToken());
	}
	
	
	@Override
	public int getConstantValue() throws ParseException {
		switch (getToken().kind) {
			case PromelaConstants.LNOT:
				return ex1.getConstantValue() == 0 ? 1 : 0;
			case PromelaConstants.LOR:
				return (ex1.getConstantValue() != 0) || (ex2.getConstantValue() != 0) ? 1 : 0;
			case PromelaConstants.LAND:
				return (ex1.getConstantValue() != 0) && (ex2.getConstantValue() != 0) ? 1 : 0;
		}
		throw new MyParseException("Unimplemented aritmic type: " + getToken().image, getToken());
	}

	@Override
	public String getIntExpression() throws ParseException {
		if (ex2 == null) {
			return "(" + getToken().image + ex1.getIntExpression() + " ? 1 : 0)";
		} else {
			return "(" + ex1.getBoolExpression() + " " + getToken().image + " "
					+ ex2.getBoolExpression() + " ? 1 : 0)";
		}
	}

	@Override
	public VariableType getResultType() {
		return VariableType.BOOL;
	}

	@Override
	public String getSideEffect() {
		if ((ex1.getSideEffect() != null) || ((ex2 != null) && (ex2.getSideEffect() != null))) {
			return "Boolean expression has some side effect!";
		}
		return null;
	}

	@Override
	public Set<VariableAccess> readVariables() {
		final Set<VariableAccess> rv = new HashSet<VariableAccess>();
		if (ex1 != null) {
			rv.addAll(ex1.readVariables());
		}
		if (ex2 != null) {
			rv.addAll(ex2.readVariables());
		}
		return rv;
	}

	@Override
	public String toString() {
		try {
			return getBoolExpression();
		} catch (final ParseException ex) {
			return "";
		}
	}

	public Expression getExpr1() {
		return ex1;
	}
	public Expression getExpr2() {
		return ex2;
	}

	@Override
	public int getUselessWeight() {
		if (ex1.getUselessWeight() == 1)
			return 1 ;
		if (ex2 != null)
			if (ex2.getUselessWeight() == 1)
				return 1 ;
		return 2;
	}

	@Override
	public boolean getVarErrorAffect(String varName) {
		// TODO Auto-generated method stub
		return false;
	}
}
