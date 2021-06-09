package spinja.promela.compiler.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import spinja.exceptions.SpinJaException;
import spinja.promela.compiler.Specification;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.Token;
import spinja.promela.compiler.variable.Variable;
import spinja.promela.compiler.variable.VariableAccess;
import spinja.promela.compiler.variable.VariableType;
import spinja.util.StringUtil;
import spinja.util.StringWriter;

public class SCH_GET_Expression extends Expression implements CompoundExpression {

	private final String var;
	private final String procName;
	private final String pro;
	

	private final Specification specification;

	private final List<Expression> exprs;


	public SCH_GET_Expression(final Token token, final Specification spec, final String var, final String procName, final String pro) {
		super(token);
		specification = spec;
		this.var = var;
		this.procName = procName;
		this.pro = pro;		
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
		String result = "if (sch_get(\"" + procName + "\", \"" + pro + "\")) " ; 
		result += var + " = " + specification.generateCastingVariable(var) + "(api_execute_result) ; else new SpinJaException(\"sch_get_error\")" ; 
		return result ;
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
		return "sch_get (" + var + ", " + procName + ", " + pro + ")";
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
