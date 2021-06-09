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

import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.Token;
import spinja.promela.compiler.variable.VariableAccess;
import spinja.promela.compiler.variable.VariableType;


public class TerminateExpression extends Expression implements CompoundExpression {
	

	public TerminateExpression(final Token token) {
		super(token);
	}

	@Override
	public String getBoolExpression() {
		return "true";
	}

	@Override
	public String getIntExpression() throws ParseException {
		StringBuilder build=new StringBuilder("");
		build.append("terminate(); ") ;		
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
		return rv;
	}

	@Override
	public String toString() {
		return "terminate()";		
	}
	
	@Override
	public void addExpression(Expression expr) throws ParseException {
		// TODO Auto-generated method stub
		
	}


	@Override
	public int getUselessWeight() {		
		return 1; //useful
	}

	@Override
	public boolean getVarErrorAffect(String varName) {
		// TODO Auto-generated method stub
		return false;
	}
}
