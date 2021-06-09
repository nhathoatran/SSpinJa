/**
 * 
 */
package spinja.promela.compiler.ltsmin.model;

import spinja.promela.compiler.Proctype;
import spinja.promela.compiler.actions.Action;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.PromelaConstants;
import spinja.promela.compiler.parser.Token;
import spinja.util.StringWriter;

public class ResetProcessAction extends Action {
	private Proctype process;

	public ResetProcessAction(Proctype process) {
		super(new Token(PromelaConstants.ASSIGN, "=reset="));
		this.process = process;
	}

	public Proctype getProcess() {
		return process;
	}

	@Override
	public String toString() {
		return "";
	}

	@Override
	public String getEnabledExpression() throws ParseException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public void printTakeStatement(StringWriter w) throws ParseException {
		throw new UnsupportedOperationException("Not supported yet.");
	}

	@Override
	public int getUselessWeight() {		
		return 2; //not know
	}

	@Override
	public boolean getVarErrorAffect(String varName) {
		// TODO Auto-generated method stub
		return false;
	}

}