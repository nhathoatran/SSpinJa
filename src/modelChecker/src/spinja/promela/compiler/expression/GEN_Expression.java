package spinja.promela.compiler.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import spinja.promela.compiler.Proctype;
import spinja.promela.compiler.Specification;
import spinja.promela.compiler.parser.MyParseException;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.Token;
import spinja.promela.compiler.variable.VariableAccess;
import spinja.promela.compiler.variable.VariableType;
import spinja.util.StringWriter;

public class GEN_Expression extends Expression implements CompoundExpression {

	private final String funcName;
	private final String val;
	

	private final Specification specification;

	private final List<Expression> exprs;


	public GEN_Expression(final Token token, final Specification spec, final String funcName, final String val) {
		super(token);
		specification = spec;
		this.funcName = funcName;
		this.val = val;
		exprs = new ArrayList<Expression>();
	}

	public void addExpression(final Expression expr) throws ParseException {
		exprs.add(expr);
	}

	private String getArgs() throws ParseException {
		final StringWriter w = new StringWriter();
		for (final Expression expr : exprs) {
			w.append(expr == exprs.get(0) ? "" : ", ").append(expr.getIntExpression());
		}
		return w.toString();
	}

	@Override
	public String getBoolExpression() {
		return "true";
	}

	@Override
	public String getIntExpression() throws ParseException {
		return "Gen." + funcName + "(\"" + val + "\")";
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null ;
	}

	@Override
	public Set<VariableAccess> readVariables() {
		final Set<VariableAccess> rv = new HashSet<VariableAccess>();
		for (final Expression expr : exprs) {
			rv.addAll(expr.readVariables());
		}
		return rv;
	}

	@Override
	public String toString() {		
		if (val.trim().isEmpty())
			return "Generate ( " + funcName + ")";
		return "Generate ( " + funcName + ", " + val  + ")";
	}

	@Override
	public int getUselessWeight() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public boolean getVarErrorAffect(String varName) {
		// TODO Auto-generated method stub
		return false;
	}
}
