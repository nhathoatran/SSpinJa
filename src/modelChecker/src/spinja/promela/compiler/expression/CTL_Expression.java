package spinja.promela.compiler.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import spinja.promela.compiler.Specification;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.Token;
import spinja.promela.compiler.variable.VariableAccess;
import spinja.promela.compiler.variable.VariableType;
import spinja.util.StringWriter;

public class CTL_Expression extends Expression implements CompoundExpression {

	private final Token t ;
	private final CTL_Expression exp1, exp2;	
	private final Expression exp ;
	private final Expression at ;
	
	private final List<Expression> exprs;	
	public static CTL_Expression ctl_expression ;
	
	private int num ;
	
	public String getAt() {
		if (at == null)
			return null ;
		else
			return at.toString() ;
	}
	
	public CTL_Expression(Expression at, Token t, CTL_Expression ex1, CTL_Expression ex2, String num) {
		super(t);
		this.at = at ;
		this.t = t ;
		this.exp1 = ex1 ;
		this.exp2 = ex2 ;			
		this.exp = null ;
		exprs = new ArrayList<Expression>();
		ctl_expression = this ;
		this.num = Integer.parseInt(num) ;
	}

	public CTL_Expression(Expression at, Token t, Expression exp, String num) {
		super(t);
		this.at = at ;
		this.t = t ;
		this.exp1 = null ;
		this.exp2 = null ;
		this.exp = exp ;
		exprs = new ArrayList<Expression>();
		ctl_expression = this ;
		this.num = Integer.parseInt(num) ;
	}

	public CTL_Expression(Expression at, Expression exp) {
		super(null);
		this.at = at ;
		this.t = null ;
		this.exp1 = null ;
		this.exp2 = null ;
		this.exp = exp ;
		exprs = new ArrayList<Expression>();
		ctl_expression = this ;
		this.num = 0 ;
	}

	
	public int getLength() {
		int length = 0 ;
		if (t != null)
			length += 1 ;
		
		if (exp != null) 
			if (exp instanceof CTL_Expression)
				length += ((CTL_Expression) exp).getLength() ;
			else 
				length += 1 ;
						
		if (exp1 != null)
			if (exp1.getLength() == 0)
				length +=  1;
			else 
				length += exp1.getLength() ;
		
		if (exp2 != null)
			if (exp2.getLength() == 0)
				length +=  1;
			else 
				length += exp2.getLength() ;
			
		return length ;		
	}
	
	public void printFormula(StringWriter w, CTL_Expression formula, int i) {	
		w.appendLine();
		if (formula.at != null)
			printFormula(w, (CTL_Expression) formula.exp, i);
		else {
			if (formula.t == null) {
				if (formula.exp instanceof CTL_Expression)
					printFormula(w, (CTL_Expression)formula.exp, i + 1) ;
				else {
					w.appendLine("opcode["+i+"]=\"atomic\"; //" + formula.toString());
					w.appendLine("sn["+i+"]= -1; //no superscript");
					w.appendLine("sf["+i+"][0]=" + i + ";");				
				}
			} else {
				String op = formula.t.toString() ;
				if (op == "AX" || op == "AF" || op == "AG" ||	op == "EX" 	|| op == "EF" || op == "EG") {
					w.appendLine("opcode["+i+"]=\"" + op + "\"; //" + formula.toString() ) ;
					
					if (formula.num == 0)
						w.appendLine("sn["+i+"]= -1; //no superscript");
					else
						w.appendLine("sn["+i+"]= " + formula.num + "; //superscript");
					
					w.appendLine("sf["+i+"][0]=" + (i + 1) + ";");
					if (formula.exp != null) {
						if (formula.exp instanceof CTL_Expression)
							printFormula(w, (CTL_Expression) formula.exp, i+1) ;
					} else 
						if (formula.exp1 != null)						
							printFormula(w, (CTL_Expression) formula.exp1, i+1) ;
				}
				if (op == "AU" || op == "EU") {
					w.appendLine("opcode["+i+"]=\"" + op + "\"; //" + formula.toString() );
					
					if (formula.num == 0)
						w.appendLine("sn["+i+"]= -1; //no superscript");
					else
						w.appendLine("sn["+i+"]= " + formula.num + " ; //superscript");
					
					w.appendLine("sf["+i+"][0]=" + (i + 1) + ";");
					w.appendLine("sf["+i+"][1]=" + (i + 1 + formula.exp2.getLength()) + ";");
					printFormula(w, formula.exp1, i+1) ;
					printFormula(w, formula.exp2, i+1 + formula.exp1.getLength()) ;
				}
			}
		}
	}
	
	public void generateAtomicCheck(StringWriter w, CTL_Expression formula, int i) {		
		if (this.at != null) i-- ;
		if (formula.t == null) {
			if (formula.exp instanceof CTL_Expression)
				generateAtomicCheck(w, (CTL_Expression)formula.exp, i + 1) ;
			else {
				w.appendLine("case "+i+":" );
				w.indent().appendLine("return " + formula.toString() + ";").outdent();				
			}
		} else {
			String op = formula.t.toString() ;
			if (op == "AX" || op == "AF" || op == "AG" ||	op == "EX" 	|| op == "EF" || op == "EG") {
				if (formula.exp != null) {
					if (formula.exp instanceof CTL_Expression)
						generateAtomicCheck(w, (CTL_Expression) formula.exp, i+1) ;
				} else 
					if (formula.exp1 != null)						
						generateAtomicCheck(w, (CTL_Expression) formula.exp1, i+1) ;
			}
			if (op == "AU" || op == "EU") {
				generateAtomicCheck(w, formula.exp1, i+1) ;
				generateAtomicCheck(w, formula.exp2, i+1 + formula.exp1.getLength() + 1) ;
			}
		}
	}
	
	public void generateAtomicCheckResult(StringWriter w, CTL_Expression formula, int i) {		
		if (formula.at != null) {
			generateAtomicCheckResult(w, (CTL_Expression)formula.exp, i) ;
		} else {
			if (formula.t == null) {
				if (formula.exp instanceof CTL_Expression)
					generateAtomicCheckResult(w, (CTL_Expression)formula.exp, i + 1) ;
				else {
					w.appendLine("result["+i+"] = " + formula.toString() + ";");				
				}
			} else {
				String op = formula.t.toString() ;
				if (op == "AX" || op == "AF" || op == "AG" ||	op == "EX" 	|| op == "EF" || op == "EG") {
					if (formula.exp != null) {
						if (formula.exp instanceof CTL_Expression)
							generateAtomicCheckResult(w, (CTL_Expression) formula.exp, i+1) ;
					} else 
						if (formula.exp1 != null)						
							generateAtomicCheckResult(w, (CTL_Expression) formula.exp1, i+1) ;
				}
				if (op == "AU" || op == "EU") {
					generateAtomicCheckResult(w, formula.exp1, i+1) ;
					generateAtomicCheckResult(w, formula.exp2, i+1 + formula.exp1.getLength()) ;
				}
			}
		}
	}
	
	public void generateChekedAtomic(StringWriter w, CTL_Expression formula, int i) {	
		//if (this.at != null) i-- ;
		if (formula.at != null) {
			generateChekedAtomic(w, (CTL_Expression)formula.exp, i) ;
		} else {
			if (formula.t == null) {
				if (formula.exp instanceof CTL_Expression)
					generateChekedAtomic(w, (CTL_Expression)formula.exp, i + 1) ;
				else {
					w.appendLine("checked["+i+"] = true ; //" + formula.toString() + ";");				
				}
			} else {
				String op = formula.t.toString() ;
				if (op == "AX" || op == "AF" || op == "AG" ||	op == "EX" 	|| op == "EF" || op == "EG") {
					if (formula.exp != null) {
						if (formula.exp instanceof CTL_Expression)
							generateChekedAtomic(w, (CTL_Expression) formula.exp, i+1) ;
					} else 
						if (formula.exp1 != null)						
							generateChekedAtomic(w, (CTL_Expression) formula.exp1, i+1) ;
				}
				if (op == "AU" || op == "EU") {
					generateChekedAtomic(w, formula.exp1, i+1) ;
					generateChekedAtomic(w, formula.exp2, i+1 + formula.exp1.getLength()) ;
				}
			}
		}
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
		if (t == null)
			return exp1.toString();
		if (exp2 != null)
			return "(" + exp1.toString() + t + exp2.toString() + ")";
		return "(" + t + exp1.toString() + ")";
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
		String prefix = "" ;
		String sn = "" ;
		if (at != null)
			prefix = "When " + at.toString() + " : ";		 
		if (num != 0)
			sn = "<=" + num ;
		
		if (t != null) 
			if (exp1 != null)
				if (exp2 != null)
					return prefix + t + sn + " " + exp1.toString() + " "+ exp2.toString() + " ";
				else
					return prefix + t + sn + " " + exp1.toString() ;
			else
				return prefix + t + sn + " " + exp.toString() + " ";
		return prefix + exp.toString() + " ";
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
