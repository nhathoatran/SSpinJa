package spinja.promela.compiler;

//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.and;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.assign;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.calc;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.chanContentsGuard;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.chanEmptyGuard;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.compare;
import static spinja.promela.compiler.ltsmin.model.LTSminUtil.constant;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.dieGuard;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.eq;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.error;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.getOutTransitionsOrNullSet;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.id;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.isRendezVousReadAction;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.isRendezVousSendAction;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.or;
//import static spinja.promela.compiler.ltsmin.model.LTSminUtil.pcGuard;
//import static spinja.promela.compiler.ltsmin.state.LTSminStateVector._NR_PR;
//import static spinja.promela.compiler.ltsmin.state.LTSminTypeChanStruct.bufferVar;
//import static spinja.promela.compiler.ltsmin.state.LTSminTypeChanStruct.elemVar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import spinja.promela.compiler.actions.Action;
import spinja.promela.compiler.actions.AssertAction;
import spinja.promela.compiler.actions.AssignAction;
import spinja.promela.compiler.actions.BreakAction;
import spinja.promela.compiler.actions.ChannelReadAction;
import spinja.promela.compiler.actions.ChannelSendAction;
import spinja.promela.compiler.actions.ElseAction;
import spinja.promela.compiler.actions.ExprAction;
import spinja.promela.compiler.actions.GotoAction;
import spinja.promela.compiler.actions.OptionAction;
import spinja.promela.compiler.actions.PrintAction;
import spinja.promela.compiler.actions.Sequence;
import spinja.promela.compiler.automaton.ActionTransition;
import spinja.promela.compiler.automaton.ElseTransition;
import spinja.promela.compiler.automaton.EndTransition;
import spinja.promela.compiler.automaton.GotoTransition;
import spinja.promela.compiler.automaton.NeverEndTransition;
import spinja.promela.compiler.automaton.State;
import spinja.promela.compiler.automaton.Transition;
import spinja.promela.compiler.automaton.UselessTransition;
import spinja.promela.compiler.expression.AritmicExpression;
import spinja.promela.compiler.expression.BooleanExpression;
import spinja.promela.compiler.expression.ChannelLengthExpression;
import spinja.promela.compiler.expression.ChannelOperation;
import spinja.promela.compiler.expression.ChannelReadExpression;
import spinja.promela.compiler.expression.CompareExpression;
import spinja.promela.compiler.expression.ConstantExpression;
import spinja.promela.compiler.expression.EvalExpression;
import spinja.promela.compiler.expression.Expression;
import spinja.promela.compiler.expression.GEN_Expression;
import spinja.promela.compiler.expression.Identifier;
import spinja.promela.compiler.expression.RemoteRef;
import spinja.promela.compiler.expression.RunExpression;
import spinja.promela.compiler.expression.SCH_API_Expression;
import spinja.promela.compiler.expression.SCH_EXEC_COMP_Expression;
import spinja.promela.compiler.expression.SCH_EXEC_Expression;
import spinja.promela.compiler.expression.SCH_GET_Expression;
import spinja.promela.compiler.ltsmin.model.ResetProcessAction;
//import spinja.promela.compiler.ltsmin.LTSminDebug.MessageKind;
//import spinja.promela.compiler.ltsmin.matrix.LTSminGuardAnd;
//import spinja.promela.compiler.ltsmin.matrix.LTSminGuardBase;
//import spinja.promela.compiler.ltsmin.matrix.LTSminGuardContainer;
//import spinja.promela.compiler.ltsmin.matrix.LTSminGuardNand;
//import spinja.promela.compiler.ltsmin.matrix.LTSminGuardNor;
//import spinja.promela.compiler.ltsmin.matrix.LTSminGuardOr;
//import spinja.promela.compiler.ltsmin.model.LTSminModel;
//import spinja.promela.compiler.ltsmin.model.LTSminState;
//import spinja.promela.compiler.ltsmin.model.LTSminTransition;
//import spinja.promela.compiler.ltsmin.model.ReadAction;
//import spinja.promela.compiler.ltsmin.model.ResetProcessAction;
//import spinja.promela.compiler.ltsmin.model.SendAction;
//import spinja.promela.compiler.ltsmin.state.LTSminStateVector;
import spinja.promela.compiler.optimizer.RenumberAll;
import spinja.promela.compiler.parser.ParseException;
import spinja.promela.compiler.parser.Preprocessor;
import spinja.promela.compiler.parser.Preprocessor.DefineMapping;
import spinja.promela.compiler.parser.Promela;
import spinja.promela.compiler.variable.ChannelVariable;
import spinja.promela.compiler.variable.Variable;

/**
 * Constructs the LTSminModel by walking over the SpinJa {@link Specification}.
 * First processes are instantiated by copying their CST.
 * 
 * @author Freark van der Berg, Alfons Laarman
 */
public class Instantiate {

	public List<Pair<ChannelReadAction,Transition>> pairs =
	        new ArrayList<Pair<ChannelReadAction,Transition>>();
	public static class Pair<L,R> {
		public L left; public R right;
		public Pair(L l, R r) { this.left = l; this.right = r; }
	}
	
	private final Specification spec;





	public Instantiate(Specification spec) {
		this.spec = spec;

	}

	/**
	 * generates and returns an LTSminModel from the provided Specification
	 */
	public void createInstantiate() {
		instantiate();
		//bindByReferenceCalls();
		return ;
	}

	/**
	 * Binds any channel type arguments of all RunExpressions by reference.
	 */
//	private void bindByReferenceCalls() {
//		if (spec.runs.size() > 0)
//			LTSminStateVector._NR_PR.setAssignedTo();
//		for (Proctype p : spec.getProcs()) {
//			if (p.getNrActive() > 0) continue;
//			List<RunExpression> rr = new ArrayList<RunExpression>();
//			for (RunExpression re : spec.runs)
//				if (re.getProctype().equals(p)) rr.add(re);
//			if (rr.size() == 0) {
//				continue;
//			}
//			if (rr.size() == 1 && p.getInstances().size() > 1) {
//				for (ProcInstance target : p.getInstances()) {
//					target.getVariable(Promela.C_STATE_PID).setAssignedTo(); // PID is changed
//					bindArguments(rr.get(0), target, true);
//				}
//			} else if (rr.size() == p.getInstances().size()) {
//				Iterator<ProcInstance> it = p.getInstances().iterator();
//				for (RunExpression re : rr) {
//					ProcInstance target = it.next();
//					//model.sv.getPID(target).setAssignedTo(); // PID is changed
//					target.getVariable(Promela.C_STATE_PID).setAssignedTo();
//					re.setInstance(target);
//					bindArguments(re, target, false);
//				}
//			} else {
//				for (ProcInstance target : p.getInstances()) {
//					target.getVariable(Promela.C_STATE_PID).setAssignedTo();
//					bindArguments(rr.get(0), target, true);
//					
//					//model.sv.getPID(target).setAssignedTo(); // PID is changed
//				}
//			}
//		}
//	}



    private List<String> iCount = new ArrayList<String>();

    private int getInstanceCount(Proctype p) {
    	DefineMapping nrInstances, original;
    	nrInstances = original = Preprocessor.defines("__instances_"+ p.getName());
		if (null != nrInstances) {
			int count = -1;
			while (-1 == count) try {
				count = Integer.parseInt(nrInstances.defineText.trim());
			} catch (NumberFormatException nf) {
				nrInstances = Preprocessor.defines(nrInstances.defineText.trim());
				if (null == nrInstances) break; 
			}
			if (-1 == count) throw new AssertionError("Cannot parse "+ original);
			return count;
		}
		// query instantiation count from user
		System.out.print("Provide instantiation number for proctype "+ p.getName() +": ");
//		InputStreamReader converter = new InputStreamReader(System.in);
//		BufferedReader in = new BufferedReader(converter);
//		String number;
//		try {
//			number = in.readLine();
//		} catch (IOException e) {throw new AssertionError(e);}
		
		int num = 1;
		iCount.add(p.getName() +" "+ num);
		return num;
    }
    
	/** Active processes can be differentiated from each other by the value of
	 * their process instantiation number, which is available in the predefined
	 * local variable _pid . Active processes are always instantiated in the
	 * order in which they appear in the model, so that the first such process
	 * (whether it is declared as an active process or as an init process) will
	 * receive the lowest instantiation number, which is zero. */
	private void instantiate() {
		List<ProcInstance> instances = new ArrayList<ProcInstance>();
		List<ProcInstance> active = new ArrayList<ProcInstance>();
		//spec.clearReadActions();

		int id = 0;
		for (Proctype p : spec.getProcs()) { // add active processes (including init)
			for (int i = 0; i < p.getNrActive(); i++) {
				ProcInstance instance = instantiate(p, id, i);
				p.addInstance(instance);
				active.add(instance);
				id++;
			}
		}

		// set number of processes to initial number of active processes.
//		try {
//			LTSminStateVector._NR_PR.setInitExpr(constant(id));
//		} catch (ParseException e) { assert (false); }

		for (Proctype p : spec.getProcs()) {
			if (0 != p.getNrActive())
				continue;
			int instanceCount = getInstanceCount(p);
			for (int i = 0; i < instanceCount; i++) {
				ProcInstance instance = instantiate(p, id, i);
				p.addInstance(instance);
				instances.add(instance);
				id++;
			}
		}
		if (null != spec.getNever()) {
			Proctype never = spec.getNever();
			ProcInstance n = instantiate(never, -1, -1);
			try {
				spec.setNever(n);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		if (null != spec.getSporadic()) {
			Proctype sporadic = spec.getSporadic();
			ProcInstance n = instantiate(sporadic, -1, -1);
			try {
				spec.setSporadic(n);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		//for (String binding : iCount)
//			debug.say(MessageKind.NORMAL, "#define __instances_"+ binding);
		active.addAll(instances);
//		for (ProcInstance instance : instances )
//			active.add(instance);
		spec.setInstances(active);
	}

	/**
	 * Copies proctype to an instance.
	 */
	private ProcInstance instantiate(Proctype p, int id, int index) {
		ProcInstance instance = new ProcInstance(p, index, id);
		Expression e = instantiate(p.getEnabler(), instance);
		instance.setEnabler(e);
		for (Variable var : p.getVariables()) {
			Variable newvar = instantiate(var, instance);
			if (newvar.getName().equals(Promela.C_STATE_PROC_COUNTER))
				newvar.setAssignedTo(); // Process counter is always assigned to
			instance.addVariable(newvar, p.getArguments().contains(var));
		}
		instance.lastArgument();
		for (String mapped : p.getVariableMappings().keySet()) {
			String to = p.getVariableMapping(mapped);
			instance.addVariableMapping(mapped, to);
		}
		HashMap<State, State> seen = new HashMap<State, State>();
		instantiate(p.getStartState(), instance.getStartState(), seen, instance);
		new RenumberAll().optimize(instance.getAutomaton());
		return instance;
	}
	
	/**
	* Copy the automaton
	*/
	private void instantiate(State state, State newState,
							 HashMap<State, State> seen, ProcInstance p) {
		if (null == state || null != seen.put(state, newState))
			return;
		newState.setLabels(state.getLabels());
		for (Transition trans : state.output) {
			State next = trans.getTo();
			State newNextState = null;
			if (null != next) if (seen.containsKey(next))
				newNextState = seen.get(next);
			else
				newNextState = new State(p.getAutomaton(), next.isInAtomic());
			Transition newTrans =
				(trans instanceof ActionTransition ? new ActionTransition(newState, newNextState) :
				(trans instanceof ElseTransition ? new ElseTransition(newState, newNextState) :
				(trans instanceof EndTransition ? new EndTransition(newState) :
				(trans instanceof NeverEndTransition ? new NeverEndTransition(newState) :
				(trans instanceof GotoTransition ? new GotoTransition(newState, newNextState, trans.getText().substring(5)) :
				(trans instanceof UselessTransition ? new UselessTransition(newState, newNextState, trans.getText()) :				
				 null))))));
			for (Action a : trans)
				newTrans.addAction(instantiate(a, newTrans, p, null));
			instantiate(next, newNextState, seen, p);
		}
	}

	private Variable instantiate(Variable var, ProcInstance p) {
		if (null == var.getOwner()) // global var, no copy required
			return var;
		if (!p.getTypeName().equals(var.getOwner().getName()))
			throw new AssertionError("Expected instance of type "+ var.getOwner().getName() +" not of "+ p.getTypeName());
		Variable newvar = var instanceof ChannelVariable ?
				new ChannelVariable(var.getName(), var.getArraySize()) :
				new Variable(var.getType(), var.getName(), var.getArraySize());
		newvar.setOwner(p);
		newvar.setType(var.getType());
		newvar.setRealName(var.getRealName());
		try {
			if (null != var.getInitExpr())
				newvar.setInitExpr(instantiate(var.getInitExpr(), p));
		} catch (ParseException e1) { throw new AssertionError("Identifier"); }
		if (newvar.getName().equals(Promela.C_STATE_PID)) {
			int initial_pid = (p.getNrActive() == 0 ? -1 : p.getID());
			try { newvar.setInitExpr(constant(initial_pid));
			} catch (ParseException e) { assert (false); }
		}
		return newvar;
	}

	/**
	 * Copy actions
	 */
	private Action instantiate(Action a, Transition t, ProcInstance p, OptionAction loop) {
		if(a instanceof AssignAction) {
			AssignAction as = (AssignAction)a;
			Identifier id = (Identifier)instantiate(as.getIdentifier(), p);
			Expression e = instantiate(as.getExpr(), p);
			id.getVariable().setAssignedTo();
			return new AssignAction(as.getToken(), id, e);
		} else if(a instanceof ResetProcessAction) {
			throw new AssertionError("Unexpected ResetProcessAction");
		} else if(a instanceof AssertAction) {
			AssertAction as = (AssertAction)a;
			Expression e = instantiate(as.getExpr(), p);
			return new AssertAction(as.getToken(), e);
		} else if(a instanceof PrintAction) {
			PrintAction pa = (PrintAction)a;
			PrintAction newpa = new PrintAction(pa.getToken(), pa.getString());
			for (final Expression expr : pa.getExprs())
				newpa.addExpression(instantiate(expr, p));
			return newpa;
		} else if(a instanceof ExprAction) {
			ExprAction ea = (ExprAction)a;
			Expression e = instantiate(ea.getExpression(), p);
			return new ExprAction(e);
		} else if(a instanceof OptionAction) { // options in a d_step sequence
			OptionAction oa = (OptionAction)a;
			OptionAction newoa = new OptionAction(oa.getToken(), oa.loops());
			newoa.hasSuccessor(oa.hasSuccessor());
			loop = newoa.loops() ? newoa : null;
			for (Sequence seq : oa)
				newoa.startNewOption((Sequence)instantiate(seq, t, p, loop)); 
			return newoa;
		} else if(a instanceof Sequence) {
			Sequence seq = (Sequence)a;
			Sequence newseq = new Sequence(seq.getToken());
			for (Action aa : seq) {
				Action sub = instantiate(aa, t, p, loop);
				newseq.addAction(sub);
			}
			return newseq;
		} else if(a instanceof BreakAction) {
			BreakAction ba = (BreakAction)a;
			BreakAction newba = new BreakAction(ba.getToken(), loop);
			return newba;
		} else if(a instanceof ElseAction) {
			return a; // readonly, hence can be shared
		} else if(a instanceof GotoAction) {
			return a; // readonly, hence can be shared
		} else if(a instanceof ChannelSendAction) {
			ChannelSendAction csa = (ChannelSendAction)a;
			Identifier id = (Identifier)instantiate(csa.getIdentifier(), p);
			ChannelSendAction newcsa = new ChannelSendAction(csa.getToken(), id);
			for (Expression e : csa.getExprs())
				newcsa.addExpression(instantiate(e, p));
			return newcsa;
		} else if(a instanceof ChannelReadAction) {
			ChannelReadAction cra = (ChannelReadAction)a;
			Identifier id = (Identifier)instantiate(cra.getIdentifier(), p);
			ChannelReadAction newcra = new ChannelReadAction(cra.getToken(), id, cra.isPoll(), cra.isRandom());
			for (Expression e : cra.getExprs()) {
				newcra.addExpression(instantiate(e, p));
				if (e instanceof Identifier) {
					((Identifier)e).getVariable().setAssignedTo();
				}
			}
			pairs.add(new Pair<ChannelReadAction, Transition>(newcra, t));
			return newcra;
		} else { // Handle not yet implemented action
			throw new AssertionError("LTSMinPrinter: Not yet implemented: "+a.getClass().getName());
		}
	}	
	
	/**
	 * Copy expressions with instantiated processes.
	 */
	private Expression instantiate(Expression e, ProcInstance p) {
		if (null == e) return null;

		if (e instanceof Identifier) { // also: LTSminIdentifier
			Identifier id = (Identifier)e;
			Variable var = id.getVariable();
			if (null != var.getOwner()) {
			    if (id.getInstanceIndex() != -1) {
			        for (ProcInstance i : var.getOwner().getInstances() ) {
			            if (i.getID() == id.getInstanceIndex()) p = i;
			        }
			        if (p == null) throw new AssertionError("ProcInstance "+ id.getInstanceIndex() +" not found for remote ref "+ id.toString());
			    } else if (!p.getTypeName().equals(var.getOwner().getName())) {
					throw new AssertionError("Expected instance of type "+ var.getOwner().getName() +" not of "+ p.getTypeName());
				}
				var = p.getVariable(var.getName()); // load copied variable
			}
			Expression arrayExpr = instantiate(id.getArrayExpr(), p);
			Identifier sub = (Identifier)instantiate(id.getSub(), p);
			return new Identifier(id.getToken(), var, arrayExpr, sub);
		} else if (e instanceof AritmicExpression) {
			AritmicExpression ae = (AritmicExpression)e;
			Expression ex1 = instantiate(ae.getExpr1(), p);
			Expression ex2 = instantiate(ae.getExpr2(), p);
			Expression ex3 = instantiate(ae.getExpr3(), p);
			return new AritmicExpression(ae.getToken(), ex1, ex2, ex3);
		} else if (e instanceof BooleanExpression) {
			BooleanExpression be = (BooleanExpression)e;
			Expression ex1 = instantiate(be.getExpr1(), p);
			Expression ex2 = instantiate(be.getExpr2(), p);
			return new BooleanExpression(be.getToken(), ex1, ex2);
		} else if (e instanceof CompareExpression) {
			CompareExpression ce = (CompareExpression)e;
			Expression ex1 = instantiate(ce.getExpr1(), p);
			Expression ex2 = instantiate(ce.getExpr2(), p);
			return new CompareExpression(ce.getToken(), ex1, ex2);
		} else if (e instanceof ChannelLengthExpression) {
			ChannelLengthExpression cle = (ChannelLengthExpression)e;
			Identifier id = (Identifier)cle.getExpression();
			Identifier newid = (Identifier)instantiate(id, p);
			try {
				return new ChannelLengthExpression(cle.getToken(), newid);
			} catch (ParseException e1) {
				throw new AssertionError(e1);
			}
		} else if (e instanceof ChannelReadExpression) {
			ChannelReadExpression cre = (ChannelReadExpression)e;
			Identifier id = (Identifier)instantiate(cre.getIdentifier(), p);
			ChannelReadExpression res = new ChannelReadExpression(cre.getToken(), id, cre.isRandom());
			for (Expression expr : cre.getExprs())
				res.addExpression(instantiate(expr, p));
			return res;
		} else if (e instanceof ChannelOperation) {
			ChannelOperation co = (ChannelOperation)e;
			Identifier id = (Identifier)instantiate(co.getExpression(), p);
			try {
				return new ChannelOperation(co.getToken(), id);
			} catch (ParseException e1) {
				throw new AssertionError("ChanOp");
			}
		} else if (e instanceof RunExpression) {
			RunExpression re = (RunExpression)e;
			RunExpression newre = new RunExpression(e.getToken(), spec.getProcess(re.getId())); 
			try {
				for (Expression expr : re.getExpressions())
					newre.addExpression(instantiate(expr, p));
			} catch (ParseException e1) {
				throw new AssertionError("RunExpression");
			}
			spec.runs.add(newre); // add runexpression to a list
			return newre;
		} else if (e instanceof EvalExpression) {
			EvalExpression eval = (EvalExpression)e;
			Expression ex = instantiate(eval.getExpression(), p);
			return new EvalExpression(e.getToken(), ex);
		} else if (e instanceof ConstantExpression) {
			return e; // readonly, hence can be shared
		} else if (e instanceof RemoteRef) {
			RemoteRef rr = (RemoteRef)e;
			Expression ex = instantiate(rr.getExpr(), p);
			Proctype proc = spec.getProcess(rr.getProcessName());
			if (null == proc) throw new AssertionError("Wrong process: "+ rr);
			RemoteRef ref = new RemoteRef(rr.getToken(), proc, rr.getLabel(), ex);
			spec.remoteRefs.add(ref);
	        return ref;
		} else if (e instanceof SCH_API_Expression	|| e instanceof SCH_GET_Expression 
				|| e instanceof SCH_EXEC_Expression || e instanceof SCH_EXEC_COMP_Expression
				|| e instanceof GEN_Expression) {
			return e ;
		}
		else {
			throw new AssertionError("LTSMinPrinter: Not yet implemented: "+e.getClass().getName());
		}
	}

	/**
	 * Binds any channel type arguments of all RunExpressions by reference.
	 */


//	private void bindArguments(RunExpression re, ProcInstance target,
//							   boolean dynamic) {
//		if (null == target) throw new AssertionError("Target of run expression is not found: "+ re.getId());
//		List<Variable> args = target.getArguments();
//		Iterator<Expression> eit = re.getExpressions().iterator();
//		if (args.size() != re.getExpressions().size())
//			throw error("Run expression's parameters do not match the proc's arguments.", re.getToken());
//		//write to the arguments of the target process
//		int 			count = 0;
//		for (Variable v : args) {
//			count++;
//			Expression param = eit.next();
//			if (v.getType() instanceof ChannelType) {
//				if (!(param instanceof Identifier))
//					throw error("Run expression's parameter for "+ v +" does not match the proc's argument type.", re.getToken());
//				Identifier id = (Identifier)param;
//				Variable varParameter = id.getVariable();
//				VariableType t = varParameter.getType();
//				if (!(t instanceof ChannelType))
//					throw error("Parameter "+ count +" of "+ re.getId() +" should be a channeltype.", re.getToken());
//				ChannelType ct = (ChannelType)t;
//				if (ct.getBufferSize() == -1)
//					throw error("Could not deduce channel declaration for parameter "+ count +" of "+ re.getId() +".", re.getToken());
//				if (dynamic || varParameter.getArraySize() > -1)
//					throw new AssertionError("Cannot dynamically bind "+ target.getTypeName() +" to the run expressions in presence of arguments of type channel.\n" +
//							"Change the proctype's arguments or unroll the loop with run expressions in the model.");
//				String name = v.getName();
////				debug.say(MessageKind.DEBUG, "Binding "+ target +"."+ name +" to "+ varParameter.getOwner() +"."+ varParameter.getName());
//				//List<ReadAction> ras = spec.getReadActions(v);
//				v.setRealName(v.getName()); //TODO: this is also a variable mapping
//				v.setType(varParameter.getType());
//				v.setOwner(varParameter.getOwner());
//				v.setName(varParameter.getName());
//				//if (null != ras) spec.addReadActions(v.ras);
//			} else if (!dynamic) {
//				try {
//					v.setInitExpr(param);
//				} catch (ParseException e) {
//					throw new AssertionError("Wrong type in run parameter." + e);
//				}
//			} else if (dynamic) {
//				v.setAssignedTo();
//			}
//		}
//		for (Variable v : target.getVariables()) {
//			if (null == v.getInitExpr()) continue;
//			try {
//				v.getInitExpr().getConstantValue();
//			} catch (ParseException e) {
////				if (dynamic)
////					throw new AssertionError("Cannot dynamically bind "+ target.getTypeName() +" to the run expressions in presence of arguments of init expressions that use the arguments.\n" +
////							"Change the proctype's arguments or unroll the loop with run expressions in the model.");
//				Expression init = v.getInitExpr();
//				v.unsetInitExpr();
//				re.addAction(new AssignAction(new Token(PromelaConstants.ASSIGN), id(v), init));
//			}
//		}
//	}



	
}
