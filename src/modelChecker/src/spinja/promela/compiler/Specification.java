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

package spinja.promela.compiler;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import espinja.CompileOnlyTest;
import spinja.promela.compiler.actions.Action;
import spinja.promela.compiler.actions.AssertAction;
import spinja.promela.compiler.actions.Sequence;
import spinja.promela.compiler.automaton.ActionTransition;
import spinja.promela.compiler.automaton.Transition;
import spinja.promela.compiler.expression.BooleanExpression;
import spinja.promela.compiler.expression.CTL_Expression;
import spinja.promela.compiler.expression.CompareExpression;
import spinja.promela.compiler.expression.Expression;
import spinja.promela.compiler.expression.RemoteRef;
import spinja.promela.compiler.expression.RunExpression;
//import spinja.promela.compiler.ltsmin.model.LTSminUtil;
//import spinja.promela.compiler.ltsmin.model.ReadAction;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.variable.ChannelType;
import spinja.promela.compiler.variable.ChannelVariable;
import spinja.promela.compiler.variable.CustomVariableType;
import spinja.promela.compiler.variable.Variable;
import spinja.promela.compiler.variable.VariableStore;
import spinja.promela.compiler.variable.VariableType;
import spinja.util.StringUtil;
import spinja.util.StringWriter;
import sspinja.CompileScheduler;
import sspinja.CompileSchedulerWithCTL;
import sspinja.scheduler.promela.model.SchedulerPromelaModel;
import sspinja.Config;

public class Specification implements Iterable<ProcInstance> {
	private CTL_Expression formula ;
	private final String name;

	private final List<Proctype> procs;
	private final List<ChannelType> channelTypes;
	private final Map<String, CustomVariableType> userTypes;

	private Proctype never;	
	private Proctype sporadic;
	private Proctype error ;
	private Proctype verify ;
	private Proctype initproc ;
	
	private final VariableStore varStore;
	public List<RunExpression> runs = new ArrayList<RunExpression>();
	private final List<String> mtypes;
	
	//private HashMap<Variable, List<ReadAction>> channels;
	private int searchOption = 0;

	public Specification(final String name) {
		this.name = name;
		this.searchOption = Config.searchOption ;
		procs = new ArrayList<Proctype>();
		channelTypes = new ArrayList<ChannelType>();
		userTypes = new HashMap<String, CustomVariableType>();
		varStore = new VariableStore();
		//channels = new HashMap<Variable, List<ReadAction>>();
		Variable hidden = new Variable(VariableType.INT, "_", -1);
		hidden.setHidden(true);
		varStore.addVariable(hidden);
		mtypes = new ArrayList<String>();
	}

	public List<String> getMTypes() {
		return mtypes;
	}

	public String getName() {
		return name;
	}

//	public void clearReadActions() {
//		channels.clear();
//	}

//	public void addReadAction(ChannelReadAction cra, Transition t) {
//		if (!LTSminUtil.isRendezVousReadAction(cra)) return;
//		Variable cv = cra.getIdentifier().getVariable();
//		List<ReadAction> raw = channels.get(cv);
//		if (raw == null) {
//			raw = new ArrayList<ReadAction>();
//			channels.put(cv, raw);
//		}
//		raw.add(new ReadAction(cra, t, t.getProc()));
//	}

	/**
	 * Creates a new Channel type for in this Specification.
	 * 
	 * @param bufferSize
	 * @return The new ChannelType
	 */
	public ChannelType newChannelType(int bufferSize) {
		ChannelType type = new ChannelType(channelTypes.size(), bufferSize);
		channelTypes.add(type);
		return type;
	}

	/**
	 * Creates a new Custom type for in this Specification.
	 * 
	 * @param bufferSize
	 * @return The new ChannelType
	 */
	public CustomVariableType newCustomType(String name) throws ParseException {
		if (userTypes.containsKey(name))
			throw new ParseException("Duplicate type declaration with name: "+ name);
		CustomVariableType type = new CustomVariableType(name);
		userTypes.put(name, type);
		return type;
	}
	
	public boolean usesRendezvousChannel() {
		for (ChannelType t : channelTypes) {
			if (t.getBufferSize() < 1) {
				return true;
			}
		}
		return false;
	}

	public void addMType(final String name) {
		mtypes.add(name);
	}

	public void addProc(final Proctype proc) throws ParseException {
		if (getProcess(proc.getName()) != null) {
			throw new ParseException("Duplicate proctype with name: " + proc.getName());
		}
		procs.add(proc);
	}

	private void generateConstructor(final StringWriter w) throws ParseException {
		w.appendLine("public ", name, "Model() throws SpinJaException {").indent();
		w.appendLine("super(\"", name, "\", ", varStore.getBufferSize() + 1
												+ (ChannelVariable.isChannelsUsed() ? 1 : 0)
												+ (usesAtomic() ? 1 : 0), ");");
		w.appendLine();
		w.appendLine("// Initialize the default values");
		for (final Variable var : varStore.getVariables()) {
			if ((Config.isCompileScheduler || Config.isCompileOnlyTest) && 
					(var.getName().equals("_test_depth") || StringUtil.containSchedulerKeyword(var.getName()))) {
				continue ;
			}
			var.printInitExpr(w);
		}		
		w.appendLine("// Initialize the starting processes");

		int initpos = -1 ;
		int pos = 0 ;
		for (final Proctype proc : procs) {	
			String pName = proc.getName() ;
			if (pName.equals("init_0")) {
				initpos = pos ;
				if (Config.isCompileScheduler)				
					proc.generateInitProcessForScheduler(w);
			} else {
				if (proc.getNrActive() > 0) {
						w.appendLine("addProcess(new "+ proc.getName()+"());");				
				}
			}
			pos++ ;
		}
		if (initpos != -1)
			procs.remove(initpos);
		
		w.outdent().appendLine("}");
		w.appendLine();
	}
	
	/**
	 * Generate the state for variables
	 * @param w
	 * @throws ParseException
	 */
	private void generateGetVarState(final StringWriter w) throws ParseException {
		w.appendLine("public VarState getVarState() {").indent();
		w.appendLine("return new VarState(" + listVariables() + ") ;") ;	
		w.outdent().appendLine("}");
		w.appendLine();
	}
	private String listVariables() {
		String result = "" ;
		int index = 0 ;
		int numVar = varStore.getVariables().size() ;
		
		for (final Variable var : varStore.getVariables()) {
			index ++ ;
			if ((Config.isCompileScheduler || Config.isCompileOnlyTest) && StringUtil.containSchedulerKeyword(var.getName()))
				continue ;
			
			//allow variable name with "_"
			if (! var.isHidden()) {
				result += var.getName() ;
				
				if (index < numVar)
					result += ", ";
			}			
		}
		return result ;				
	}
		
	private void generateMain(final StringWriter w) {
		w.appendLine("public static void main(String[] args) {").indent();		 
		switch (Config.searchOption) {
			case 0: w.appendLine("//DFS"); break ;
			case 1: w.appendLine("//Accept"); break ;			
			case 2: w.appendLine("//SchedulerCTLSearch"); break ; 
			case 3: w.appendLine("//scheduler Accept"); break ;
			case 4: w.appendLine("//test DFS"); break ;
			case 5: w.appendLine("//CTL/RTCTL"); break ;
			case 6: w.appendLine("//scheduler DFS"); break ;
			case 7: w.appendLine("//for heuristic"); break ;
			case 8: w.appendLine("//liveness check"); break ;
			case 9 : w.appendLine("//liveness check with heuristic"); break ;
			case 10 : w.appendLine("//scheduler CTL gen search"); break ;
			case 11 : w.appendLine("//scheduler CTL search (multi core)"); break ;
		}
		if (Config.searchOption != 0)
			w.appendLine("Run run = new Run(" + Config.searchOption +");") ;		
		else
			w.appendLine("Run run = new Run();") ;
		w.appendLine("run.parseArguments(args,\"" + name + "\");");
				
		for (final Variable var : varStore.getVariables()) {
			if (var.getName().equals("_test_depth")) {
				try {
					w.appendLine("run.setDepth(" + var.getConstantValue() + ");");					
				} catch (ParseException e) {
					e.printStackTrace();
				}
				break ;
			}
		}
		
		if (Config.isCompileScheduler) {
			w.appendLine("if (SchedulerPanModel.scheduler.InitSchedulerObject(run.scheduleropt.getOption()))");			
		} 
		
		w.appendLine("run.search(" + name + "Model.class);");
		w.outdent().appendLine("}");
		w.appendLine();
		
		if (error != null) {
			try {
				error.generateError(w);
			} catch (ParseException e) {
				e.printStackTrace();
			}						
		}		
	}

	/**
	 * Generates the complete Model object and returns it as a String.
	 * 
	 * @return A String that holds the complete Model for this Specification.
	 * @throws ParseException
	 *             When something went wrong while parsing the promela file.
	 */
	public String generateModel() throws ParseException {
		final StringWriter w = new StringWriter();

		if ( Config.isCompileScheduler) { 
			w.appendLine("package sspinja;");
			w.appendLine("import sspinja.Run;");
		} else {
			if (Config.isCompileOnlyTest) {
				w.appendLine("package espinja;");
				w.appendLine("import espinja.Run;");
			} else {
				w.appendLine("package sspinja;");
				w.appendLine("import spinja.Run;");
			}
		}
		
		w.appendLine();		
		w.appendLine("import spinja.util.DataReader;");
		w.appendLine("import spinja.util.DataWriter;");
		
		w.appendLine("import spinja.promela.model.*;");
		w.appendLine("import spinja.exceptions.*;");
		
		if (Config.isCompileScheduler) {
			w.appendLine("import sspinja.scheduler.promela.model.*;") ;
			w.appendLine("import sspinja.scheduling.SchedulerObject;") ;
		}
			
		
		w.appendLine();
		if (name.equals("SchedulerPan"))
			w.appendLine("public class ", name, "Model extends SchedulerPromelaModel {").indent();
		else
			if (name.equals("Claim"))
				w.appendLine("public class ", name, "Model extends PanModel {").indent();
			else
				w.appendLine("public class ", name, "Model extends PromelaModel {").indent();
		w.appendLine();
		w.appendLine();

		if (! name.equals("Claim")) {
			generateMain(w);
			generateCustomTypes(w);
			generateVariables(w);
			generateConstructor(w);
			
			//for heuristic
			if (name.equals("PanOptimize")) {
				generateAccepted(w);
				generateGetVarState(w) ;
			}
			
			generateStore(w);
			generateToString(w);
			
			generateGetChannelCount(w);
			generateProctypes(w);
			generateAtomicCheck(w); //for CTL formula
			w.outdent().appendLine("}");
		} else {
			generateMain(w);			
			generateClaimConstructor(w);
			generateProctypes(w);
			
			w.outdent().appendLine("}");
		}
				
		return w.toString();
	}
			
	public String generateSchedulerPanModel() throws ParseException {
		final StringWriter w = new StringWriter();
		w.appendLine("package sspinja;");
		w.appendLine("import sspinja.scheduler.promela.model.*;");
		w.appendLine("import spinja.exceptions.*;");
		w.appendLine("import sspinja.scheduling.SchedulerObject;");
		w.appendLine();
		w.appendLine("public class SchedulerPanModel extends SchedulerPromelaModel { ").indent() ;
		w.appendLine("public SchedulerPanModel() throws SpinJaException {").indent() ;
		w.appendLine("super(\"SchedulerPan\", 0);").outdent() ;
		w.appendLine("}").outdent() ;
		w.appendLine("}");		
		return w.toString();
	}
	
	public String generateVerify() throws ParseException {
		final StringWriter w = new StringWriter();
		formula = CTL_Expression.ctl_expression ;
		
		int length = formula.getLength();
		
		w.appendLine("package sspinja;");		
		w.appendLine();
		w.appendLine("//Model compiler") ;
		w.appendLine("public class CTLFormula {").indent();			
		w.appendLine("//" + formula.toString()) ;
		w.appendLine("public int length = " + length + ";") ;
		w.appendLine("public String opcode[] = new String[" + length + "];") ;		
		w.appendLine("public byte sf[][] = new byte[" + length + "][2]; //formula parameters") ;
		w.appendLine("public byte sn[] = new byte[" + length + "]; //formula superscripts") ;
		w.appendLine();
		
		
		w.appendLine("public CTLFormula () {").indent();			
		w.appendLine("//" + formula.toString()) ;
		formula.printFormula(w, formula, 0);				
		w.outdent().appendLine("}");
		w.appendLine();
		
		w.appendLine("public String toString () {").indent();			
		w.appendLine("return \"" + formula.toString() + "\";") ;						
		w.outdent().appendLine("}");
		w.appendLine();
		
		w.outdent().appendLine("}");
		w.appendLine();
		return w.toString();
	}
	public boolean hasVerify() {
		return verify != null ;
	}
	private void generateAtomicCheck(StringWriter w) throws ParseException {
		if (hasVerify()) {
			CTL_Expression formula = CTL_Expression.ctl_expression ;
			w.appendLine("");
			w.appendLine("//" + formula.toString()) ;
			w.appendLine("public boolean[] init_sf() {").indent();
			w.appendLine("boolean checked[] = new boolean[" + (formula.getLength()) + "];") ;
			formula.generateChekedAtomic(w, formula, 0);			
			w.appendLine("return checked ;");
			w.outdent().appendLine("}");
			
			w.appendLine("");
			w.appendLine("public boolean[] init_atomicf() {").indent();
			w.appendLine("boolean result[] = new boolean[" + (formula.getLength()) + "];") ;
			formula.generateAtomicCheckResult(w, formula, 0);			
			w.appendLine("return result ;");
			w.outdent().appendLine("}");
			
			String at = formula.getAt() ;
			w.appendLine("");
			w.appendLine("public boolean stateCheck() {").indent();
			if (at != null) 
				w.appendLine("return " + at + ";") ;
			else
				w.appendLine("return false ;") ;			
			w.outdent().appendLine("}");			
			
			w.appendLine("public boolean modelCheck() {").indent();			
				w.appendLine("return true ;") ;			
			w.outdent().appendLine("}");			
			
			
			w.appendLine("");
			w.appendLine("public boolean collectState() {").indent();
			if (at != null) 
				w.appendLine("return true ;") ;
			else
				w.appendLine("return false ;") ;			
			w.outdent().appendLine("}");
		} else {
			w.appendLine("public String sysGet(String va) {").indent();
			w.appendLine("String result = \"\";");
			w.appendLine("switch (va) {").indent() ;
			for (final Variable var : varStore.getVariables()) {				
				if ((Config.isCompileScheduler || Config.isCompileOnlyTest) && StringUtil.containSchedulerKeyword(var.getName())) //remove scheduler varialbes
					continue ;
				if (var.isHidden()) continue ;				
				w.appendLine("case \"" + var.getName() + "\" :").indent();
				w.appendLine("result += " + var.getName() + ";") ;
				w.appendLine("break ;").outdent() ;
			}
			w.outdent().appendLine("}");
			w.appendLine("return result ;") ;						
			w.outdent().appendLine("}");
		}
	}
	
	public String generateSchedulerState() throws ParseException {
		final StringWriter w = new StringWriter();
		
		w.appendLine("package sspinja.scheduling;");		
		w.appendLine();		
		w.appendLine("import java.util.ArrayList;");	
		w.appendLine("import spinja.model.Transition;");
		w.appendLine("import sspinja.GenerateCode;");
		w.appendLine();
		
		w.appendLine("public class SchedulerState { ;").indent() ;		
		w.appendLine("public boolean duplicate = false;") ;
		w.appendLine("public int identifier;") ;
		w.appendLine("public boolean results[];") ;
		w.appendLine("public boolean checked[];") ;		
		w.appendLine("public ArrayList<SchedulerState> next = new ArrayList<SchedulerState>();") ;
		w.appendLine("public ArrayList<Transition> trans = new ArrayList<Transition>();") ;
		w.appendLine();
		
		w.appendLine("public void update(int depth, boolean isCycle, boolean isCurrentState) {}");
		w.appendLine();
		
		w.appendLine("public void print(ArrayList<SchedulerState> schStateList, String pref, boolean fulltrans) {").indent();				
		w.appendLine("if (!schStateList.contains(this)) schStateList.add(this) ;");
		
		String s = "\"[\" + identifier ";
		for (int i=0; i<formula.getLength(); i++)
			s += "+ \",\" + results[" + i + "]";
		w.appendLine("System.out.print(pref + " + s + "+ \"]\") ;") ;
		w.appendLine("for (SchedulerState child : next) System.out.print(\",\" + child.identifier);") ;
		w.appendLine("System.out.println(\"\");") ;
		
		w.appendLine("int index = 0 ;") ;
		w.appendLine("for (SchedulerState s : next) {").indent() ;
		w.appendLine("if (!schStateList.contains(s)) {").indent() ;
		w.appendLine("if (fulltrans) {").indent() ;
		w.appendLine("s.print(schStateList, pref + trans.get(index++) + \"-->\", fulltrans);").outdent();
		w.appendLine("} else {").indent() ;
		w.appendLine("s.print(schStateList, pref + \"-->\", fulltrans);").outdent();
		w.appendLine("}").outdent();
		w.appendLine("}");
		
		w.outdent().appendLine("}");
		w.outdent().appendLine("}");
		
		w.appendLine("public void print() {").indent();
		w.appendLine("System.out.println(" + s + "+ \"]\") ;") ;
		w.outdent().appendLine("}");		
		w.outdent().appendLine("}");
		return w.toString();
	}
	
	
	private void generateAssertionFunction(StringWriter w) throws ParseException {		
		ArrayList<Transition> assertionTrans = getAssertTransition() ;
		if (assertionTrans != null) {
			int methodId = 0 ;
			for (Transition trans : assertionTrans) {
				if (trans instanceof ActionTransition) {
					Sequence seq = ((ActionTransition) trans).getSequence() ;
					Action act = seq.getAction(0);
					if (act instanceof AssertAction) {
						Expression expr = ((AssertAction) act).getExpr() ;
						if ((expr instanceof BooleanExpression) || (expr instanceof CompareExpression)) {
							Expression errorExpr = expr.convertAssertExpression() ;
							w.appendLine("public int getErrorDistance_" + methodId + "(){").indent();
							w.appendLine("//assert: " + expr.toString()) ;
							w.appendLine("//error: " + errorExpr.toString()) ;
							w.appendLine("int errorDistance = 0 ;") ;
							w.appendLine("errorDistance = " + errorExpr.getDistanceExpression()+ ";") ;
							if (trans.getProc().name.contains("never")) {
								w.appendLine("return errorDistance ;");
							} else {
								w.appendLine("return Math.max(Math.abs(HeuristicSearch.processLoc[" + trans.getProc().getID() + "] - " + trans.getTransId() + "), errorDistance) ;") ;
							}
							w.outdent().appendLine("}").appendLine();
							
							w.appendLine("public boolean checkError_" + methodId + "(){").indent();							
							w.appendLine("return " + errorExpr.getBoolExpression() + ";") ;														
							w.outdent().appendLine("}").appendLine();
							
							w.appendLine("public String errorState_"+ methodId + "(){").indent();							
							w.appendLine("return \"" + errorExpr.getBoolExpression() + "\";") ;														
							w.outdent().appendLine("}").appendLine();
							methodId ++ ;
						}
					}
				}
			}
		}
		
		
		w.appendLine("public int getAssertionDistance(){").indent();
		w.appendLine("int result = Integer.MAX_VALUE ;") ;
		if (assertionTrans != null) {
			int methodId = 0 ;
		
			for (Transition trans : assertionTrans) {
				if (trans instanceof ActionTransition) {
					Sequence seq = ((ActionTransition) trans).getSequence() ;
					Action act = seq.getAction(0);
					if (act instanceof AssertAction) {
						Expression expr = ((AssertAction) act).getExpr() ;
						if ((expr instanceof BooleanExpression) || (expr instanceof CompareExpression)) {						
							w.appendLine("result = Math.min(result, getErrorDistance_" + methodId + "());") ;
							methodId ++ ;
						}
					}
				}
			}		
		}
		w.appendLine("return result ;") ;							
		w.outdent().appendLine("}").appendLine();
		
		w.appendLine("public boolean checkError(){").indent();
		w.appendLine("boolean result = false ;") ;
		if (assertionTrans != null) {
			int methodId = 0 ;
		
			for (Transition trans : assertionTrans) {
				if (trans instanceof ActionTransition) {
					Sequence seq = ((ActionTransition) trans).getSequence() ;
					Action act = seq.getAction(0);
					if (act instanceof AssertAction) {
						Expression expr = ((AssertAction) act).getExpr() ;
						if ((expr instanceof BooleanExpression) || (expr instanceof CompareExpression)) {						
							w.appendLine("result = result || checkError_" + methodId + "();") ;
							methodId ++ ;
						}
					}
				}
			}		
		}
		w.appendLine("return result ;") ;							
		w.outdent().appendLine("}").appendLine();
	}
	
	public String generateVarState() throws ParseException {
		final StringWriter w = new StringWriter();
		w.appendLine("package spinja;");
		w.appendLine("import spinja.search.HeuristicSearch;");
		w.appendLine();
		
		w.appendLine("public class VarState {").indent();			
		generateVariables(w);			
		generateVarStateConstructor(w);
		
		w.appendLine("public int distance(VarState s) {").indent();
		generateDistance(w) ;
		w.outdent().appendLine("}");
		
		w.appendLine("public void print() {").indent();
		generatePrint(w);
		w.outdent().appendLine("}");
		
		w.appendLine();
		w.appendLine("//these for assertion ");
		generateAssertionFunction(w) ;
		
		w.outdent().appendLine("}");
		w.appendLine();
		return w.toString();
	}

	private void generateVarStateConstructor(final StringWriter w) {
		String st = "public VarState(" ;
		int index = 0 ;
		int numVar = varStore.getVariables().size() ;
		
		for (final Variable var : varStore.getVariables()) {
			index ++ ;
			if ((Config.isCompileScheduler || Config.isCompileOnlyTest) && StringUtil.containSchedulerKeyword(var.getName())) //remove scheduler varialbes
				continue ;
			if (var.isHidden()) continue ;
			st += var.getType().getJavaName() ;
			st += (var.getArraySize() > -1 ? "[]" : "") + " " +  var.getName() ;
			if (index < numVar)
				st += " , " ;
		}

		w.appendLine(st + ") {").indent();
		
		for (final Variable var : varStore.getVariables()) {
			try {
				var.printInitVarState(w);
			} catch (ParseException e) {				
				e.printStackTrace();
			}
		}
		
		index = 0 ;
		for (final Variable var : varStore.getVariables()) {
			index ++ ;
			if ((Config.isCompileScheduler || Config.isCompileOnlyTest) && StringUtil.containSchedulerKeyword(var.getName())) //remove scheduler varialbes
				continue ;
			if (var.isHidden()) continue ;
			
			if (var.getArraySize() > -1) {
				int aSize = var.getArraySize() ;
				String v = "" ;
				
				for (int i = 0; i< aSize ; i++) {
					v = var.getName() + "[" + i + "]" ;
					w.appendLine("this." + v + " = " +  v + ";") ;					
				} 				
			} else {
				w.appendLine("this." + var.getName() + " = " +  var.getName() + ";") ;									
			}						
		}
		w.outdent().appendLine("}");
		w.appendLine();
	}
	
	private void generateDistance(final StringWriter w) {
		String st = "return " ;
		int index = 0 ;
		int numVar = varStore.getVariables().size() ;
		
		if (numVar == 0)
			st += " 0" ;
		else {
			for (final Variable var : varStore.getVariables()) {
				index ++ ;
				if ((Config.isCompileScheduler || Config.isCompileOnlyTest) && StringUtil.containSchedulerKeyword(var.getName())) //remove scheduler varialbes
					continue ;
				
				String v = "";
				if (var.isHidden())
					continue ;
				if (var.getArraySize() > -1) {
					int aSize = var.getArraySize() ;
					for (int i = 0; i< aSize ; i++) {				
						v = var.getName() + "[" + i + "]" ;
						st += "Math.abs(this." + v + " - " + "s." + v + ")" ;
						if (i < aSize - 1) st += " + " ;
					} 				
				} else {
					v =  var.getName() ;				
					st += "Math.abs(this." + v + " - " + "s." + v + ")" ;								
				}
				if (index < numVar)
					st += " + " ;
			}
		}

		w.appendLine(st + " ;");
	}
		
	private void generatePrint(final StringWriter w) {
		String st = "System.out.print(" ;
		int index = 0 ;
		int numVar = varStore.getVariables().size() ;
		
		for (final Variable var : varStore.getVariables()) {
			index ++ ;
			if ((Config.isCompileScheduler || Config.isCompileOnlyTest) && StringUtil.containSchedulerKeyword(var.getName())) //remove scheduler varialbes
				continue ;
			String v = "";
			
			if (var.isHidden())
				continue ;
			
			if (var.getArraySize() > -1) {
				int aSize = var.getArraySize() ;
				for (int i = 0; i< aSize ; i++) {				
					v = var.getName() + "[" + i + "]" ;
					st += "\"" + v + " = \" + " + v  ;
					if (i < aSize - 1) st += " + " + "\", "  + "\" + " ;
				} 				
			} else {
				v =  var.getName() ;				
				st += "\"" + v + " = \" + " + v  ;								
			}
			if (index < numVar)				
				st += " + " + "\", "  + "\" + " ;
		}

		w.appendLine(st + ") ;");
	}

	private void generateClaimConstructor(final StringWriter w) {
		w.appendLine("public ClaimModel() throws SpinJaException {").indent();
		w.appendLine("super() ;");
		w.outdent().appendLine("}");
		w.appendLine();
	}
	
	private void generateCustomTypes(StringWriter w) {
		for (final ChannelType type : channelTypes) {
			type.generateClass(w);
		}
	}

	private void generateProctypes(final StringWriter w) throws ParseException {
		for (final Proctype proc : procs) {
			w.appendLine();
			proc.generateCode(w);
		}

		// Generate never claim
		if (never != null) {
			w.appendLine("public PromelaProcess getNever() throws ValidationException {").indent();
			w.appendLine("return new never();");
			w.outdent().appendLine("}").appendLine();
			never.generateCode(w);
		}
		
		// Generate sporadic process
		if (sporadic != null) {
			w.appendLine("public PromelaProcess getSporadic() throws ValidationException {").indent();
			w.appendLine("return new sporadic();");
			w.outdent().appendLine("}").appendLine();
			sporadic.generateCode(w);
		}
	}

	
	
	private void generateStore(final StringWriter w) {
		w.appendLine("public void encode(DataWriter _writer) {").indent();
		if (Config.isCompileScheduler) {
			w.appendLine("scheduler.encode(_writer);");
		}
		w.appendLine("_writer.writeByte(_nrProcs);");
					
		if (usesAtomic()) {
			w.appendLine("_writer.writeByte(_exclusive);");
		}
		if (ChannelVariable.isChannelsUsed()) {
			w.appendLine("_writer.writeByte(_nrChannels);");
		}
		varStore.printEncode(w);
		if (ChannelVariable.isChannelsUsed()) {
			w.appendLine("for(int _i = 0; _i < _nrChannels; _i++) {");
			w.indent();			
			w.appendLine("_channels[_i].encode(_writer);");
			w.outdent();
			w.appendLine("}");
		}
			
		if (Config.isCompileScheduler) {			
			w.appendLine("int _i = 0 ;");
			w.appendLine("int _pcount = 0 ;");
			w.appendLine("while (_pcount < _nrProcs && _i < 255) {").indent();
			w.appendLine("if (_procs[_i] != null && SchedulerObject.processInScheduler[_i]) {").indent();
			w.appendLine("_procs[_i].encode(_writer);");
			w.appendLine("_pcount++;");
			w.outdent().appendLine("}");
			w.appendLine("_i++;");
			w.outdent().appendLine("}");
		} else {
			w.appendLine("for(byte _i = 0; _i < _nrProcs; _i++) { ");
			w.indent();			
			w.appendLine("_procs[_i].encode(_writer);");
			w.outdent().appendLine("}");
			w.appendLine();
		}		
		w.outdent().appendLine("}");
		w.appendLine();
		
		w.appendLine("public boolean decode(DataReader _reader) {").indent();

		if (Config.isCompileScheduler) {
			w.appendLine("scheduler.decode(_reader) ; ");
		}
		
		w.appendLine("_nrProcs = _reader.readByte();");
		
		if (usesAtomic()) {
			w.appendLine("_exclusive = _reader.readByte();");
		}
		if (ChannelVariable.isChannelsUsed()) {
			w.appendLine("_nrChannels = _reader.readByte();");
		}
		varStore.printDecode(w);
		
		if (ChannelVariable.isChannelsUsed()) {
			w.appendLine();
			w.appendLine("for(int _i = 0; _i < _nrChannels; _i++) {");
			{
				w.indent();
				w.appendLine("_reader.storeMark();");
				w.appendLine("if(_channels[_i] == null || !_channels[_i].decode(_reader)) {");
				{
					w.indent();
					w.appendLine("_reader.resetMark();");
					if (channelTypes.size() > 0) {
						w.appendLine("switch(_reader.peekByte()) {");
						{
							w.indent();							
							for (int i = 0; i < channelTypes.size(); i++) {
								w.appendLine("case ", i, ": _channels[_i] = new Channel", channelTypes.get(
									i).getId(), "(); break;");				
							}
							w.appendLine("default: return false;");
							w.outdent();
						}
						w.appendLine("}");
					}
					w.appendLine("if(!_channels[_i].decode(_reader)) return false;");
					w.outdent();
				}
				w.appendLine("}");
				w.outdent();
			}
			w.appendLine("}");
		}
		w.appendLine();
		w.appendLine("int _start = _reader.getMark();");
		
		
		w.appendLine("for(int _i = 0; _i < _nrProcs; _i++) {");
		{
			w.indent();						
			if (Config.isCompileScheduler) {
				w.appendLine("_reader.storeMark();");
				w.appendLine("if(SchedulerObject.processInScheduler[_i]) {");
				{
					w.indent();
					w.appendLine("if(_procs[_i] == null || !_procs[_i].decode(_reader)) { // change local variables");
					{
						w.indent();
						w.appendLine("_reader.resetMark();");
						if (procs.size() > 0) {
							w.appendLine("switch(_reader.peekByte()) {");
							{
								w.indent();
								
								for (int i = 0; i < procs.size(); i++) {
									w.appendLine("case ", i, ": _procs[_i] = new ", procs.get(i).getName(),
										"(true, _i); break;");
								}
								w.appendLine("default: return false;");
								w.outdent();
							}
							w.appendLine("}");
						}
						w.appendLine("if(!_procs[_i].decode(_reader)) return false;");
						w.outdent();
						w.appendLine("}");
					}
					w.outdent();
				}				
			} else {
				w.appendLine("_reader.storeMark();");				
				w.appendLine("if(_procs[_i] == null || !_procs[_i].decode(_reader)) {");
				{
					w.indent();
					w.appendLine("_reader.resetMark();");
					if (procs.size() > 0) {
						w.appendLine("switch(_reader.peekByte()) {");
						{
							w.indent();
							
							for (int i = 0; i < procs.size(); i++) {
								w.appendLine("case ", i, ": _procs[_i] = new ", procs.get(i).getName(),
									"(true, _i); break;");
							}
							w.appendLine("default: return false;");
							w.outdent();
						}
						w.appendLine("}");
					}
					w.appendLine("if(!_procs[_i].decode(_reader)) return false;");
					w.outdent();
				}
			}
			w.appendLine("}");
			w.outdent();
		}
		w.appendLine("}");
		w.appendLine("_process_size = _reader.getMark() - _start;");
		if (Config.isCompileScheduler) {
			w.appendLine("//updateProcessListInScheduler(_nrProcs) ;");
		}
		w.appendLine("return true;");
		w.outdent().appendLine("}");
		w.appendLine();
	}

	private void generateAccepted(final StringWriter w) {
		w.appendLine("public boolean isAccepted() {").indent();
		w.appendLine("return false ;// (a == 2 || a == 5) ;");
		w.outdent().appendLine("}");
		w.appendLine();
	}
	
	private void generateToString(final StringWriter w) {
		w.appendLine("public String toString() {").indent();
		w.appendLine("StringBuilder sb = new StringBuilder();");
		w.appendLine("sb.append(\"+ ", name, "Model: \\n\");");
		varStore.printToString(w);
		w.appendLine("for(int i = 0; i < _nrProcs; i++) {");
		w.appendLine("  sb.append(\'\\n\').append(_procs[i]);");
		w.appendLine("}");
		w.appendLine("for(int i = 0; i < _nrChannels; i++) {");
		w.appendLine("  sb.append(\'\\n\').append(_channels[i]);");
		w.appendLine("}");
		w.appendLine("return sb.toString();");
		w.outdent().appendLine("}");
		w.appendLine();
	}
	
	protected void generateGetChannelCount(final StringWriter w) {
		w.appendLine("public int getChannelCount() {");
		w.indent();
		w.appendLine("return ", varStore.getChannelCount(), ";");
		w.outdent();
		w.appendLine("}");
	}

	private void generateVariables(final StringWriter w) {
		// Create the variables
		for (final Variable var : varStore.getVariables()) {
			if ((Config.isCompileScheduler || Config.isCompileOnlyTest) && StringUtil.containSchedulerKeyword(var.getName())) //remove scheduler varialbes
				continue ;
			String prefix = "";
			if (Config.isCompileScheduler)
				prefix = "public " ;			
			if (!var.isHidden()) {
				w.appendLine(prefix, var.getType().getJavaName(), (var.getArraySize() > -1 ? "[]" : ""),
					" ", var.getName(), ";");
			}else if(var.isWritten()){//HIL:26/08/2015: To fix the declaration issue of variable named '_'
				w.appendLine(prefix, var.getType().getJavaName(), (var.getArraySize() > -1 ? "[]" : ""),
						" ", var.getName(), ";");
			}
		}

		w.appendLine();
	}

	public String generateCastingVariable(String varName) {
		String result = "" ;		
		for (final Variable var : varStore.getVariables()) {
			if (var.getName().trim().equals(varName.trim())) {
				switch (var.getType().getJavaName()) {
					case "int" :
						result = "Integer.parseInt" ;
						break ;
					case "bool" :
						result = "Boolean.parseBoolean" ;
						break ;
					case "byte":
						result = "Byte.parseByte";
						break ;
				}
				break ;
			}
		}
		return result ;
	}
	
	public ArrayList<String> getVariableList() {
		ArrayList<String> varList = new ArrayList<String>() ;
		for (final Variable var : varStore.getVariables()) {
			if ((Config.isCompileScheduler || Config.isCompileOnlyTest) && StringUtil.containSchedulerKeyword(var.getName())) //remove scheduler varialbes
				continue ;
			if (!var.isHidden()) {
				varList.add(var.getName());
			}else if(var.isWritten()){//HIL:26/08/2015: To fix the declaration issue of variable named '_'
				varList.add(var.getName());
			}
		}
		return varList ;
	}
	
	public String generateRealVariables() throws ParseException {
		final StringWriter w = new StringWriter();
		
		for (final Variable var : varStore.getVariables()) {
			if ((Config.isCompileScheduler || Config.isCompileOnlyTest) && StringUtil.containSchedulerKeyword(var.getName()))
				continue ;
			if (!var.isHidden()) {
				if (var.getArraySize() > 1)
					w.appendLine(var.getType().getName(), " ", var.getName(), "[", var.getArraySize() ,"] ;");
				else
					w.appendLine(var.getType().getName(), " ", var.getName(), ";");
			}
		}
		return w.toString();
	}
	
	/**
	 * Returns mtype constant for an identifier. If there is no corresponding
	 * MType, 0 is returned
	 * @param name, the name of the identifier
	 * @return 0 or the number of the MType
	 */
	public int getMType(final String name) {
		int index = mtypes.indexOf(name);
		if (-1 == index) return 0;
		return mtypes.size() - index; // SPIN does reverse numbering of mtypes
	}

	public Proctype getNever() {
		return never;
	}
	
	public Proctype getSporadic() {
		return sporadic;
	}
	
	public Proctype getInitProc() {
		return initproc;
	}

	public Proctype getError() {
		return error;
	}
	
	public Proctype getProcess(final String name) {
		for (final Proctype proc : procs) {
			if (proc.getName().equals(name)) {
				return proc;
			}
		}
		return null;
	}

	public CustomVariableType getCustomType(final String name) throws ParseException {
		if (userTypes.containsKey(name)) {
			return userTypes.get(name);
		} else {
			throw new ParseException("Could not find a type with name: " + name);
		}
	}

	public Collection<CustomVariableType> getUserTypes() {
		return userTypes.values();
	}

	public VariableStore getVariableStore() {
		return varStore;
	}

	public boolean usesAtomic() {
		for (final Proctype p : procs) {
			if (p.getAutomaton().hasAtomic()) {
				return true;
			}
		}
		return false;
	}


	private List<ProcInstance> instances = null;

    public Set<RemoteRef> remoteRefs = new HashSet<RemoteRef>();
	
	@Override
	public Iterator<ProcInstance> iterator() {
		if (null == instances)
			throw new AssertionError("Processes were not instantiated");
		return instances.iterator();
	}

	public void setNever(final Proctype never) throws ParseException {
		this.never = never;
	}
	
	public void setSporadic(final Proctype sporadic) throws ParseException {		
		this.sporadic = sporadic;
	}
	
	public void setError(final Proctype error) throws ParseException {		
		this.error = error;
	}

	public void setVerify(final Proctype verify) throws ParseException {		
		this.verify = verify;
	}
	
	public void setInitProc(final Proctype initproc) throws ParseException {		
		this.initproc = initproc;
	}
	
	public void setInstances(List<ProcInstance> instances) {
		this.instances = instances;
	}

	public List<Proctype> getProcs() {
		return procs;
	}

	/**
	 * Get all assertions for heuristic search!
	 */
	public ArrayList<Transition> getAssertTransition() {
		ArrayList<Transition> alltrans = new ArrayList<Transition>() ;
		for (Proctype proc : this.procs) {
			ArrayList<Transition> asserttrans = proc.getAutomaton().getAssertTransition() ;
			if (asserttrans != null)
				alltrans.addAll(asserttrans) ;
		}
		if (never != null) {
			ArrayList<Transition> asserttrans = never.getAutomaton().getAssertTransition() ;
			if (asserttrans != null)
				alltrans.addAll(asserttrans) ;
		}
		if (alltrans.size() == 0)
			return null ;
		return alltrans;
	}
}
