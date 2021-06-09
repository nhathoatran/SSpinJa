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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import spinja.promela.compiler.Proctype;
import spinja.promela.compiler.automaton.Automaton;
import spinja.promela.compiler.expression.Identifier;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.Token;
import spinja.util.StringUtil;
import spinja.util.StringWriter;

public class OptionAction extends Action implements Iterable<Sequence> {
	private final boolean loops;
	
	private String label = null;

	private final List<Sequence> options;

	private boolean hasSuccessor = false;
	static int number = 0;

	public OptionAction(Token token, boolean loops) {
		super(token);
		this.loops = loops;
		if (loops) {
			label = "do_label_"+ number++;
		}
		options = new ArrayList<Sequence>();
	}

	@Override
	public String getEnabledExpression() throws ParseException {
		return null;
	}

	public Sequence startNewOption(Sequence seq) {
		options.add(seq);
		return seq;
	}

	public Sequence startNewOption() {
		Sequence seq = new Sequence();
		return startNewOption(seq);
	}

	@Override
	public void printTakeStatement(StringWriter w) throws ParseException {
		if (loops) {
			w.appendLine("while(true) {").indent();
		}
		boolean startedIf = false;
		boolean endedIf = false;
		for (Sequence seq : options) {
			String enable = seq.getEnabledExpression();
			if (seq.startsWithElse() || enable == null || enable.equals("true")) {
				if (startedIf) {
					w.outdent().appendLine("} else {").indent();
				}
				seq.printTakeStatement(w, false);
				endedIf = true;
				break;
			} else {
				if (!startedIf) {
					w.appendLine("if(", enable, ") {").indent();
					startedIf = true;
				} else {
					w.outdent().appendLine("} else if(", enable, ") {").indent();
				}
				seq.printTakeStatement(w, false);
			}
		}
		if (startedIf) {
			if (!endedIf && loops) {
				w.outdent().appendLine("} else {").indent();
				w.appendLine("break;");
			}
			w.outdent().appendLine("}");
		}
		if (loops) {
			w.outdent().appendLine("}");
		}
	}

	public boolean loops() {
		return loops;
	}

	@Override
	public boolean isLocal(Proctype proc) {
		for (Sequence seq : options) {
			if (!seq.isLocal(proc)) {
				return false;
			}
		}
		return true;
	}

	@Override
	public void printUndoStatement(StringWriter w) throws ParseException {

	}

	@Override
	public Collection<Identifier> getChangedVariables() {
		Set<Identifier> ids = new HashSet<Identifier>();
		for (Sequence seq : options) {
			ids.addAll(seq.getChangedVariables());
		}
		return ids;
	}

	@Override
	public boolean isComplex() {
		for (Sequence seq : options) {
			for (Identifier id : seq.getChangedVariables()) {
				if (id.getVariable().getArraySize() > -1) {
					return true;
				}
			}
			for (Action action : seq) {
				if (action instanceof ChannelSendAction) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public String toString() {
		return loops ? "do" : "if";
	}

	@Override
	public Iterator<Sequence> iterator() {
		return options.iterator();
	}
	
	public String getLabel() {
		return label;
	}

	public boolean hasSuccessor() {
		return hasSuccessor;
	}

	public void hasSuccessor(boolean hasSuccessor) {
		this.hasSuccessor = hasSuccessor;
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
