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

import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.Token;
import spinja.util.StringWriter;

/**
 * Gotos within d_steps! (Outside of d_steps, we generate automaton transitions)
 * 
 * @author laarman
 */
public class GotoAction extends Action {

	String id;
	
	public GotoAction(Token token, String id) {
		super(token);
		this.id = id;
	}

	@Override
	public String getEnabledExpression() throws ParseException {
		return null;
	}

	@Override
	public void printTakeStatement(StringWriter w) throws ParseException {
		w.appendLine("goto "+ id +";");
	}

	@Override
	public String toString() {
		return "goto "+ id +";";
	}

	public String getId() {
		return id;
	}

	@Override
	public int getUselessWeight() {
		return 2; //not sure useless or not
	}

	@Override
	public boolean getVarErrorAffect(String varName) {
		// TODO Auto-generated method stub
		return false;
	}
}
