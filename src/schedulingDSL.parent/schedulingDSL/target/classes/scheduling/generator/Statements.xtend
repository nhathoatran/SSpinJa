package scheduling.generator

import java.util.ArrayList
import java.util.HashSet
import scheduling.dsl.Action
import scheduling.dsl.And
import scheduling.dsl.AssertStatement
import scheduling.dsl.Atomic
import scheduling.dsl.BlockStatement
import scheduling.dsl.BoolConstant
import scheduling.dsl.CallFunction
import scheduling.dsl.ChangeAction
import scheduling.dsl.ChangeListValue
import scheduling.dsl.ChangeValue
import scheduling.dsl.ChangeValueExpression
import scheduling.dsl.ChangeValueUnOp
import scheduling.dsl.CheckPoint
import scheduling.dsl.Comparison
import scheduling.dsl.DeclareProcess
import scheduling.dsl.EmptyExpression
import scheduling.dsl.Equality
import scheduling.dsl.EventDef
import scheduling.dsl.EventOpt
import scheduling.dsl.EventStm
import scheduling.dsl.ExecuteProcess
import scheduling.dsl.ExistExpression
import scheduling.dsl.ExistP
import scheduling.dsl.ExistPID
import scheduling.dsl.Expression
import scheduling.dsl.GenCodeStatement
import scheduling.dsl.GenLnCodeStatement
import scheduling.dsl.GetIDExpression
import scheduling.dsl.GetInstance
import scheduling.dsl.GetPID
import scheduling.dsl.GetProcess
import scheduling.dsl.HasID
import scheduling.dsl.HasIDINT
import scheduling.dsl.HasNameExpression
import scheduling.dsl.IfDef
import scheduling.dsl.IfStatement
import scheduling.dsl.Implies
import scheduling.dsl.InExpression
import scheduling.dsl.InPExpression
import scheduling.dsl.InPIDExpression
import scheduling.dsl.IntConstant
import scheduling.dsl.LoopProcess
import scheduling.dsl.Minus
import scheduling.dsl.MoveProcess
import scheduling.dsl.MulOrDiv
import scheduling.dsl.NewProcessStatement
import scheduling.dsl.Not
import scheduling.dsl.NullExpression
import scheduling.dsl.Or
import scheduling.dsl.Plus
import scheduling.dsl.PrintLogStatement
import scheduling.dsl.PrintStatement
import scheduling.dsl.QualifiedNames
import scheduling.dsl.RTCTL
import scheduling.dsl.RemoveProcess
import scheduling.dsl.ReorderProcess
import scheduling.dsl.ReturnStatement
import scheduling.dsl.SchedulerDef
import scheduling.dsl.SchedulerSet
import scheduling.dsl.SetExecTime
import scheduling.dsl.SetProcess
import scheduling.dsl.SetProcessInstance
import scheduling.dsl.SetReturnCol
import scheduling.dsl.SetReturnSet
import scheduling.dsl.Statement
import scheduling.dsl.Step
import scheduling.dsl.Stm
import scheduling.dsl.TotalStep
import scheduling.generator.SchedulerTestGenerator
import static scheduling.generator.Data.*
import static extension org.eclipse.xtext.nodemodel.util.NodeModelUtils.*
import scheduling.dsl.GetName
import scheduling.dsl.StateID
import scheduling.dsl.BranchID

class Statements {
	static def boolean hasRunTime(SchedulerDef sch) {				
		if (sch.handler != null) {		
			for (e : sch.handler.event) {	
				if (e.event instanceof EventStm)			
					for (sta: (e.event as EventStm).statements) {
						if (isRunTime(sta.statement)) return true
					}
				else
					for (opt :(e.event as EventOpt).opt)
						for (sta: opt.eventstm.statements) {
							if (isRunTime(sta.statement)) return true
						}
			}	
		}
		return false		
	} 
	
	static def dispatch boolean isRunTime(MoveProcess stm) {
		return false
	}
	static def dispatch boolean isRunTime(SetProcessInstance stm) {
		return false
	}
	static def dispatch boolean isRunTime(LoopProcess stm) {
		if (isRunTime(stm.statement)) return true		
		return false
	}
	static def dispatch boolean isRunTime(GetProcess stm) {
		return stm.time != null
	}
	static def dispatch boolean isRunTime(RemoveProcess stm) {
		return false
	}
	static def dispatch boolean isRunTime(ChangeValue stm) {
		return false
	}
	static def dispatch boolean isRunTime(SetExecTime stm) {		
		return true
	}
	static def dispatch boolean isRunTime(SetReturnCol stm) {		
		return false
	}
	static def dispatch boolean isRunTime(SetReturnSet stm) {		
		return false
	}	
	static def dispatch boolean isRunTime(IfStatement stm) {
		if (isRunTime(stm.thenBlock))
			return true
		else
			if (stm.elseBlock != null)
				return isRunTime(stm.elseBlock)
			return false
	}
	static def dispatch boolean isRunTime(AssertStatement stm) {
		return false
	}
	static def dispatch boolean isRunTime(PrintStatement stm) {
		return false
	}
	static def dispatch boolean isRunTime(PrintLogStatement stm) {
		return false
	}
	static def dispatch boolean isRunTime(GenCodeStatement stm) {
		return false
	}
	static def dispatch boolean isRunTime(GenLnCodeStatement stm) {
		return false
	}
	static def dispatch boolean isRunTime(NewProcessStatement stm) {
		return false
	}
	static def dispatch boolean isRunTime(CallFunction stm) {
		return false
	}
	static def dispatch boolean isRunTime(BlockStatement stm) {		
		for (sta : stm.statements) {		
			if (isRunTime(sta)) return true 
		}
		return false
	}
	static def dispatch boolean isRunTime(ReturnStatement stm) {
		return false
	}
	static def dispatch boolean isRunTime(CheckPoint stm) {
		return false
	}
	static def dispatch boolean isRunTime(DeclareProcess stm) {
		return false
	}
	static def dispatch boolean isRunTime(SetProcess stm) {
		return false
	}	
	
	
	
	static def genIfDef(IfDef con)
	'''
		«IF con != null»
			if («dispatchExpression(con.cond)») {
		«ENDIF»
	'''
	static def genCloseIfDef(IfDef con)
	'''
		«IF con != null»
			}
		«ENDIF»
	'''
	
	static def genStm (Stm st, String processName) 
	'''
		«genIfDef(st.ifdef)» 
		«dispatchStatement(st.statement, processName)»
		«genCloseIfDef(st.ifdef)» 
	'''
	
	static def String dispatchRTCTLExpression(RTCTL rtctl_exp) {
		var String result 
		if (rtctl_exp.op.equals('or')) {
			result = dispatchRTCTLExpression(rtctl_exp.f1) + " || " + dispatchRTCTLExpression(rtctl_exp.f2)
		} else {
			if (rtctl_exp.op.equals('implies')) {
			result = "!(" + dispatchRTCTLExpression(rtctl_exp.f1) + ") || " + dispatchRTCTLExpression(rtctl_exp.f2)
		} else {
			if (rtctl_exp.exp != null)
				result = "(" + dispatchExpression(rtctl_exp.exp) + ")"
			else {
				if (rtctl_exp.op.trim.equals("not"))
					result = "! (" + dispatchRTCTLExpression(rtctl_exp.f) + ")"
				else {
						result =  rtctl_exp.op + " "
						if (rtctl_exp.lte != null)
							result += "<=" + rtctl_exp.lte.num 
						if (rtctl_exp.f != null)
							result += dispatchRTCTLExpression(rtctl_exp.f)
						if (rtctl_exp.f1 != null && rtctl_exp.f2 != null)
							result += dispatchRTCTLExpression(rtctl_exp.f1) + "  " + dispatchRTCTLExpression(rtctl_exp.f2)					
					}
				}
			}	
		}			
		return result
	}
	static def String dispatchExpression(Expression exp) {
		if (exp instanceof Or)
			return "(" + dispatchExpression(exp.left) + (if(exp.right != null) " || " + dispatchExpression(exp.right)) +  ")"
		else if (exp instanceof And)
			return "(" + dispatchExpression(exp.left) + " && " + dispatchExpression(exp.right) + ")"
		else if (exp instanceof Equality)
			return "(" + dispatchExpression(exp.left) + exp.op + dispatchExpression(exp.right) + ")"
		else if (exp instanceof Comparison)
			return "(" + dispatchExpression(exp.left) + exp.op + dispatchExpression(exp.right) + ")"
		else if (exp instanceof Plus)
			return "(" + dispatchExpression(exp.left) + '+' + dispatchExpression(exp.right) + ")"
		else if (exp instanceof Minus)
			return "(" + dispatchExpression(exp.left) + '-' + dispatchExpression(exp.right) + ")"
		else if (exp instanceof MulOrDiv)
			return "(" + dispatchExpression(exp.left) + exp.op + dispatchExpression(exp.right) + ")"
		else if (exp instanceof Implies)
			return "(! " + dispatchExpression(exp.left) + '||' + dispatchExpression(exp.right) + ")"
		else if (exp instanceof Not)
			return "!(" + dispatchExpression(exp.expression) + ")"
		else if (exp instanceof GetIDExpression)				
			return exp.PN.replace("\"","").replaceAll(" ","").trim + ".processID"
		else if (exp instanceof InExpression)
			return '(' + exp.col.name + '.hasProcess(' + exp.PN + ') > 0)'
		if (exp instanceof InPIDExpression)
			return '(' + exp.col.name + '.hasProcess(' + exp.id + ') > 0)'
		if (exp instanceof InPExpression)
			return '(' + exp.col.name + '.hasProcess(' + exp.id + ') > 0)'
			
		else if (exp instanceof ExistExpression) return '(existsProcess(' + exp.PN + ') > 0)'
		else if (exp instanceof ExistPID) return '(existsProcess(' + exp.id + ') > 0)'
		else if (exp instanceof ExistP) return '(existsProcess(' + exp.id + ') > 0)'
		
		else if (exp instanceof HasNameExpression)
			return '(' + exp.proc.name +' == null ? false : getStaticPropertyObject(' + exp.proc.name + '.refID).pName.trim().equals(' + exp.PN + '))'
			
		else if (exp instanceof HasID)
			return '(' + exp.proc.name +' == null ? false : ' + exp.proc.name + '.processID == ' + exp.id + ')'
		else if (exp instanceof HasIDINT)
			return '(' + exp.proc.name +' == null ? false : ' + exp.proc.name + '.processID == ' + exp.id + ')'
		
		else if (exp instanceof GetName)
			return '(' + exp.proc.name +' == null ? "" : getStaticPropertyObject(' + exp.proc.name + '.refID).pName.trim() )'
				
		else if (exp instanceof EmptyExpression)
			return '(' + exp.col.name + '.isEmpty() > 0)'
		else if (exp instanceof NullExpression)
			return '(' + exp.procName.name + ' == null)'		
		else if (exp instanceof IntConstant)
			return exp.node.tokenText
		else if (exp instanceof BoolConstant)
			return exp.value.toString	
		else if (exp instanceof StateID)	
			return '"<StateID>"'
		else if (exp instanceof BranchID)	
			return '"<BranchID>"'	
		else if (exp instanceof GetInstance)
			return 'getInstance('+exp.procName.name +')'
		else if (exp instanceof GetPID)
			return exp.procName.name +'.processID'	
		else if (exp instanceof Action)
			return 'getAction()'
		else if (exp instanceof Step)
			return '"getStep()"'
		else if (exp instanceof TotalStep)
			return '"getTotalStep()"'	
		else if (exp instanceof Atomic) {
			if (exp.sysvar != null)
				return 'Integer.parseInt(SchedulerPanModel.panmodel.sysGet("' + exp.sysvar.vname + '"))'			
			if (exp.getVar != null) {
				var v = dispatchQualifiedNames(exp.getVar)
				return SchedulerTestGenerator.getValue(v)				
			}
				//return '('+ dispatchQualifiedNames(exp.getVar) + ')'
			else
				if (exp.charseq != null)
					return exp.charseq.toString	
				else
					if (exp.string != null)
						return '"' + exp.string.replace('\"','\\"' ) + '"'	
					if(exp.lele != null) 
						return SchedulerTestGenerator.getReference(exp.lele)
			return exp.node.tokenText
		}
	}
	
	static def dispatchExpressionwithProcess(Expression exp, int count) 
	'''	
		boolean result_«count» = true ;
		«IF (exp instanceof Or)» //or		
			{
				«dispatchExpressionwithProcess(exp.left, count+1)»
				result_«count» = result_«count+1» ;
				«IF exp.right != null»
					«dispatchExpressionwithProcess(exp.right, count+2)»
					result_«count» = result_«count+1» || result_«count+2» ;
				«ENDIF»
			}
		«ELSE»
			«IF (exp instanceof And)»//and
				«dispatchExpressionwithProcess(exp.left, count+1)»
				result_«count» = result_«count+1» ;
				«dispatchExpressionwithProcess(exp.right, count+2)»
				result_«count» = result_«count+1» && result_«count+2» ;
			«ELSE»
				«IF (exp instanceof Equality)»//equality
					result_«count» = «dispatchExpression(exp.left,true)» == «dispatchExpression(exp.right, true)» ;
				«ELSE»		
					«IF (exp instanceof HasNameExpression)»//hasname
						result_«count» = «exp.proc.name» == null ? false : getStaticPropertyObject(«exp.proc.name».refID).pName.trim().equals(«exp.PN») ;					
					«ELSE»	
						«IF (exp instanceof HasID)»//hasID
							result_«count» = «exp.proc.name» == null ? false : «exp.proc.name».processID == «exp.id» ;
						«ELSE»
							«IF (exp instanceof HasIDINT)»//hasIDINT
								result_«count» = «exp.proc.name» == null ? false : «exp.proc.name».processID == «exp.id» ;
							«ELSE»				
								«IF (exp instanceof InExpression)»//in
									result_«count» = «exp.col.name».hasProcess(«exp.PN») > 0 ;
								«ELSE»
									«IF (exp instanceof InPIDExpression)»//in
										result_«count» = «exp.col.name».hasProcess(«exp.id») > 0 ;
									«ELSE»
										«IF (exp instanceof InPExpression)»//in
											result_«count» = «exp.col.name».hasProcess(«exp.id») > 0 ;
										«ELSE»
											«IF (exp instanceof ExistExpression)»//exist
												result_«count» = existsProcess(«exp.PN») > 0 ;
											«ELSE» 
												«IF (exp instanceof ExistPID)»//exist
													result_«count» = existsProcess(«exp.id») > 0 ;
												«ELSE»
													«IF (exp instanceof ExistP)»//exist
														result_«count» = existsProcess(«exp.id») > 0 ;
													«ELSE»						
														«IF (exp instanceof EmptyExpression)»//empty
															result_«count» = «exp.col.name».isEmpty > 0 ;
														«ELSE»
															«IF (exp instanceof NullExpression)»//null
																result_«count» = true ;
																{
																	//ArrayList<SchedulerProcess> a_«exp.procName.name» = findProcessByAlias("«exp.procName.name»") ;
																	if (a_«exp.procName.name».size() == 0)
																		result_«count» = false ;
																}
															«ELSE»
																«IF exp instanceof Not»
																	{
																		«dispatchExpressionwithProcess(exp.expression, count + 1)»
																		result_«count» = ! result_«count + 1» ;
																	}											
																«ELSE»//others
																	{
																		«var ArrayList<String> processList = getProcessListFromExpression(exp)»												
																		«IF processList.size() == 0»
																			result_«count» =  «Statements.dispatchExpression(exp,false)» ;
																		«ELSE»
																			«FOR pname : processList»
																				//ArrayList<SchedulerProcess> a_«pname» = findProcessByAlias("«pname»") ;
																			«ENDFOR»
																			«FOR pname : processList»
																				for (SchedulerProcess «pname» : a_«pname»)
																			«ENDFOR»
																			result_«count» = result_«count» && «dispatchExpression(exp, false)» ; 
																		«ENDIF»
																	}
																«ENDIF»															
															«ENDIF»
														«ENDIF»
													«ENDIF»
												«ENDIF»
											«ENDIF»
										«ENDIF»
									«ENDIF»
								«ENDIF»
							«ENDIF»
						«ENDIF»
					«ENDIF»
				«ENDIF»		
			«ENDIF»
		«ENDIF»
	'''
	static def String dispatchExpression(Expression exp, boolean isNum) {
		if (exp instanceof Or)
			return "(" + dispatchExpression(exp.left, false) + (if(exp.right != null) " || " + dispatchExpression(exp.right, false)) + ")"
		else if (exp instanceof And)
			return "(" + dispatchExpression(exp.left, false) + " && " + dispatchExpression(exp.right, false) + ")"
		else if (exp instanceof Equality)
			return "(" + dispatchExpression(exp.left, true) + exp.op + dispatchExpression(exp.right, true) + ")"
		else if (exp instanceof Comparison)
			return "(" + dispatchExpression(exp.left, true) + exp.op + dispatchExpression(exp.right, true) + ")"
	
		else if (exp instanceof Plus)
			return "(" + dispatchExpression(exp.left, true) + '+' + dispatchExpression(exp.right, true) + ")"
		else if (exp instanceof Minus)
			return "(" + dispatchExpression(exp.left, true) + '-' + dispatchExpression(exp.right, true) + ")"
		else if (exp instanceof MulOrDiv)
			return "(" + dispatchExpression(exp.left, true) + exp.op + dispatchExpression(exp.right, true) + ")"
		else if (exp instanceof Implies)
			return "(! " + dispatchExpression(exp.left, true) + "||" + dispatchExpression(exp.right, true) + ")"
		else if (exp instanceof Not)
			return "(!" + dispatchExpression(exp.expression, false) + ")"
			
		else if (exp instanceof HasNameExpression)
			return 'getStaticPropertyObject(' + exp.proc.name + '.refID).pName.trim().equals(' + exp.PN + ')'
		
		else if (exp instanceof HasID)
			return '(' + exp.proc.name +' == null ? false : ' + exp.proc.name + '.processID == ' + exp.id + ')'	
		else if (exp instanceof HasIDINT)
			return '(' + exp.proc.name +' == null ? false : ' + exp.proc.name + '.processID == ' + exp.id + ')'
			
		else if (exp instanceof GetIDExpression)		
			return exp.PN.replace("\"","").replaceAll(" ","").trim + ".processID"
		else if (exp instanceof InExpression)
			return '(' + exp.col.name + '.hasProcess(' + exp.PN + ') > 0)'
		else if (exp instanceof InPIDExpression)
			return '(' + exp.col.name + '.hasProcess(' + exp.id + ') > 0)'
		else if (exp instanceof InPExpression)
			return '(' + exp.col.name + '.hasProcess(' + exp.id + ') > 0)'
		
		else if (exp instanceof ExistExpression)
			return '(existsProcess(' + exp.PN + ') > 0)'
		else if (exp instanceof ExistPID)
			return '(existsProcess(' + exp.id + ') > 0)'
		else if (exp instanceof ExistP)
			return '(existsProcess(' + exp.id + ') > 0)'
				
			
		else if (exp instanceof EmptyExpression)
			return '(' + exp.col.name + '.isEmpty() > 0)'
		else if (exp instanceof NullExpression)
			return '(' + exp.procName.name + ' == null)'
		else if (exp instanceof IntConstant)
			if (exp.value.minus == null) return exp.value.value.toString
			else return '-' + exp.value.value.toString
		else if (exp instanceof BoolConstant)
			return exp.value.toString	
		else if (exp instanceof GetInstance)
			return 'getInstance('+exp.procName.name +')'
		else if (exp instanceof GetPID)
			return exp.procName.name +'.processID'
		else if (exp instanceof Action)
			return 'getAction()'
		else if (exp instanceof Step)
			return '"getStep()"'
		else if (exp instanceof TotalStep)
			return '"getTotalStep()"'
		else if (exp instanceof StateID)	
			return '"<StateID>"'
		else if (exp instanceof BranchID)	
			return '"<BranchID>"'	
		else if (exp instanceof Atomic) {
			if (exp.sysvar != null)
				if (isNum)
					return "Integer.parseInt(" + exp.sysvar.vname + ")"
				else
					return "Boolean.getBoolean(" + exp.sysvar.vname + ")"
			if (exp.getVar != null) {
				var v = SchedulerTestGenerator.getValue(exp.getVar.toString)
				if (v.empty)
					return dispatchQualifiedNames(exp.getVar)
				return v
			} else {
				if (exp.charseq != null)
						return exp.charseq.toString	
					else
						if (exp.string != null)
							return '"' + exp.string.replace('\"','\\"' ) + '"'							
			}			
			return exp.node.tokenText
		}
	}
	static def ArrayList<String> getVariables(ArrayList<String> varList, RTCTL rtctl) {
		if (rtctl.exp != null)
			getVariables(varList, rtctl.exp)
		else
			if (rtctl.f != null)
				getVariables(varList, rtctl.f)
			else {
				getVariables(varList, rtctl.f1)
				getVariables(varList, rtctl.f2)
			}
		return varList
	}
	static def ArrayList<String> getVariables(ArrayList<String> varList, Expression exp) {
		if (exp instanceof Or) {
			getVariables(varList, exp.left)
			getVariables(varList, exp.right)
		} else
		if (exp instanceof And) {
			getVariables(varList, exp.left)
			getVariables(varList, exp.right)
		} else
		if (exp instanceof Equality) {
			getVariables(varList, exp.left)
			getVariables(varList, exp.right)
		} else
		if (exp instanceof Comparison) {
			getVariables(varList, exp.left)
			getVariables(varList, exp.right)
		} else
		if (exp instanceof Plus) {
			getVariables(varList, exp.left)
			getVariables(varList, exp.right)
		} else
		if (exp instanceof Minus) {
			getVariables(varList, exp.left)
			getVariables(varList, exp.right)
		} else
		if (exp instanceof MulOrDiv) {
			getVariables(varList, exp.left)
			getVariables(varList, exp.right)
		} else
		if (exp instanceof Implies) {
			getVariables(varList, exp.left)
			getVariables(varList, exp.right)
		} else
		if (exp instanceof Not) {
			getVariables(varList, exp.expression)			
		} else
		if (exp instanceof Atomic) {
			if (exp.sysvar != null)
				if (!varList.contains(exp.sysvar.vname))
					varList.add(exp.sysvar.vname)			
		}
		return varList
	}
	
//	static def String dispatchAvarElement(AvarElement avar) {
//		if (avar.num != null)
//			return SchedulerTestGenerator.defrefval.get(avar.name).get(avar.num.value)
//		else
//			if (SchedulerTestGenerator.cst.containsKey(avar.id)) //const
//				return SchedulerTestGenerator.defrefval.get(avar.name).get(Integer.parseInt(SchedulerTestGenerator.cst.get(avar.id)))
//			else //ref id
//				return SchedulerTestGenerator.defrefval.get(avar.name).get(Integer.parseInt(SchedulerTestGenerator.funcparaval.get(avar.id))
//	}
	static def String dispatchQualifiedNames(QualifiedNames qname) {
		if (qname.prop == null) {
			return qname.name.toString()
		} else {
			return qname.name + '.get_' + qname.prop.name + '()'
//			if (Data.varPropertyList.contains(qname.prop.name))
//				return qname.name + '.get_' + qname.prop.name + '()'
//			else
//				return 'SchedulerObject.getStaticPropertyObject(' + qname.name + '.refID).' + qname.prop.name
		}
	}
	static def String dispatchQualifiedNamesInAssignStatement(QualifiedNames qname) {
		if (qname.prop == null) {
			return qname.name.toString()
		} else {
			if (Data.varPropertyList.contains(qname.prop.name))
				return qname.name + '.' + qname.prop.name
			else
				return 'Util.println("Can not change the process property ' + qname.prop.name + ') ;'
		}
	}

	static def dispatch dispatchStatement(ReturnStatement stm, String processName) {
		var result = ''
		switch stm.value.toString().trim() {
			case 'greater': result = '1 ; //greater'
			case 'less': result = '-1 ; //less'
			case 'equal': result = '0 ; //equal'
//			case 'fifo': result = 1 
//			case 'lifo' : result = -1  			
		}		
		return 'return ' + result
	}
	static def dispatch dispatchStatement(MoveProcess stm, String processName) 
	'''		
		{//MoveProcess «processName»			
			«IF Data.initProcessList»					
				//when initializing: put new process to temporary list (AL_?) for stack or queue, then put temporary list into stack or queue
				AL_«stm.colTo.name».add(«stm.process.name») ;
			«ELSE»
				«IF Data.schModel.scheduler != null»
					«IF Data.schModel.scheduler.schedulerdata != null»					
						if («stm.process.name» != null) {
							«IF (processName.equals(stm.process.name))»
								remove_process(«stm.process.name».processID) ;
							«ELSE»		
								«IF (! stm.process.name.equals("running_process"))»											
									//in a collection, use iterator
									iterator_«stm.process.name».remove() ;
								«ENDIF»
							«ENDIF»				
							«stm.colTo.name».put(«stm.process.name») ;
								
							if (running_process != null) {
								if (running_process.processID == «stm.process.name».processID) {
									«IF Data.schModel.defcore != null»
										running_procs[current_core] = null ;						
									«ENDIF»
									running_process = null;
								}
							}	
							«IF stm.process.name.contains("running_process")»
								//«stm.process.name» = null ;
							«ENDIF»				
						}
					«ENDIF»
				«ENDIF»
			«ENDIF»		
		}
	'''
	static def dispatch dispatchStatement(ReorderProcess stm, String processName) //e.g. processName = target 
	'''		
		{//ReorderProcess «processName»			
			«IF Data.initProcessList»					
				//reset does not affect to the initialization
			«ELSE»
				«IF Data.schModel.scheduler != null»
					«IF Data.schModel.scheduler.schedulerdata != null»					
						if («stm.process.name» != null) {
							if («stm.colTo.name».hasProcess(«stm.process.name») > 0) {
								«stm.colTo.name».removeProcess(«stm.process.name».processID) ;											
								«stm.colTo.name».put(«stm.process.name») ;
							}
						}
					«ENDIF»
				«ENDIF»
			«ENDIF»		
		}
	'''
	static def dispatch dispatchStatement(SetProcessInstance stm, String processName) 
	'''		
		{//SetProcessInstance «processName»
			«IF !stm.process.name.contains("running_process")»							
				«IF Data.initProcessList»					
					//when initializing: put new process to temporary list (AL_?) for stack or queue, then put temporary list into stack or queue
					AL_«stm.colTo.name».add(«stm.process.name») ;
				«ELSE»
					«IF Data.schModel.scheduler != null»
						«IF Data.schModel.scheduler.schedulerdata != null»
							if (running_process != null) {					
								if («stm.process.name».processID != running_process.processID) {												
									«stm.colTo.name».put(«stm.process.name») ;
								}				
							} else {
								«stm.colTo.name».put(«stm.process.name») ;
							}
						«ENDIF»
					«ENDIF»
				«ENDIF»
			«ENDIF»		
		}
	'''
	static def dispatch dispatchStatement(RemoveProcess stm, String processName) 
	'''		
		{//RemoveProcess
			«IF stm.process.name == "running_process"»
				if (running_process != null) {
					remove_process(running_process.processID) ;
					running_process = null ;
					«IF Data.schModel.defcore != null»
						running_procs[current_core] = null ;
					«ENDIF»
				}				
			«ELSE»				
				if («stm.process.name».processID == running_process.processID) {
					running_process = null ;
					«IF Data.schModel.defcore != null»
						running_procs[current_core] = null ;
					«ENDIF»
					try {
						select_process(-1) ;
					} catch (ValidationException e) {						
						e.printStackTrace();
					}
				} else {	
					remove_process(«stm.process.name».processID) ;
				}
			«ENDIF»	
			SchedulerPanModel.panmodel.reduceProcess() ;
		}
	'''	
	static def dispatch dispatchStatement(ChangeAction stm, String processName) 
	'''				
		{//Change action
			«FOR st : stm.sta»
				«dispatchStatement(st, processName)»
			«ENDFOR»
		}
	'''
	static def dispatch dispatchStatement(ChangeValueUnOp stm, String processName) {
		dispatchQualifiedNames(stm.getVar) + stm.getOp() + ';'
	}
	static def dispatch dispatchStatement(ChangeListValue stm, String processName) {
		var String exp = dispatchExpression(stm.exp)		
		//return SchedulerTestGenerator.getReference(stm.lvar) + '=' + SchedulerTestGenerator.repReference(exp)  + ';'
		return SchedulerTestGenerator.getReference(stm.lvar) + '=' + exp  + ';'
	}
	static def dispatch dispatchStatement(ChangeValueExpression stm, String processName) {
		if (stm.getVar.prop == null)
			dispatchQualifiedNamesInAssignStatement(stm.getVar) + '=' + dispatchExpression(stm.getExp()) + ';'
		else if (Data.varPropertyList.contains(stm.getVar.prop.name))
			dispatchQualifiedNamesInAssignStatement(stm.getVar) + '=(' + Data.getType(stm.getVar.prop.name) + ')' +
				dispatchExpression(stm.getExp()) + ';'
		else
			return 'Util.println("Can not change the process property ' + stm.getVar.prop.name + ' of ' +
				stm.getVar.name + '") ;'
	}
	static def dispatch dispatchStatement(IfStatement stm, String processName) 
	'''
		«var ArrayList<String> processList = getProcessListFromExpression(stm.condition)»
		if («dispatchExpression(stm.condition)»)
			«dispatchStatement(stm.thenBlock, processName)»
		«IF (stm.elseBlock != null)»
			else 
				«dispatchStatement(stm.elseBlock, processName)»
		«ENDIF»
	'''
	static def dispatchStatementForSimulation(GetProcess stm) 
	'''
		{//GetProcess statement		
			//1. Select process set
			ArrayList<SchedulerProcess> runSet = «stm.colFrom.name».getProcessSet();
			if (runSet != null) {
				_runningSet.dataSet = runSet ; //only get no remove
			}
		}//GetProcess statement							
	'''
	static def dispatch dispatchStatement(GetProcess stm, String processName) 
	'''
		{//GetProcess statement		
			SchedulerProcess previous_running = running_process;
			//1. Select process set
			if (lastProcessID < 0) {
				ArrayList<SchedulerProcess> runSet = «stm.colFrom.name».getProcessSet();
				if (runSet != null) {
					«IF Data.schModel.defcore != null»					
						if (_runningSets[current_core] == null)
							_runningSets[current_core] = new RunningSet();
						_runningSets[current_core].dataSet =  (ArrayList<SchedulerProcess>) runSet.clone() ;
						_runningSet = _runningSets[current_core]; //only get no remove
					«ELSE»
						_runningSet.dataSet = runSet ; //only get no remove
					«ENDIF»							
					«IF Data.schModel.defcore != null»
						_putColIndex[current_core].clear() ;
						_putColIndex[current_core].add((byte) getCollectionIndex("«stm.colFrom.name»")) ; //for the replacement
					«ELSE»
						_putColIndex = (byte) getCollectionIndex("«stm.colFrom.name»") ; //for the replacement
					«ENDIF»
				} else {
					«IF Data.schModel.defcore != null»
						if (_runningSets[current_core] != null)
							_runningSets[current_core].clear() ;
					«ENDIF»
					if (_runningSet != null)
						_runningSet.clear() ;
					return - 1;
				}
			} 
			//2 Get first process which has different processID in _runningSet to run
			int processID = select_process_to_run(lastProcessID) ;
			if (processID < 0) {
				//Util.println("Error getting process to run - no process is selected");
				return -1 ;
			}			
			//SchedulerProcess «processName» = running_process ;	
			«processName» = running_process ;		
			if (lastProcessID >= 0) {		
				«IF Data.schModel.defcore != null»					
					replace_running_process(_putColIndex[current_core].get(0),running_process, previous_running) ;
					_putColIndex[current_core].clear();					
				«ELSE»
					replace_running_process(_putColIndex,running_process, previous_running) ;
				«ENDIF»
			}			
			//remove it from collection
			«IF Data.schModel.defcore != null»
				remove_process(«processName».processID) ;
			«ELSE»
				«stm.colFrom.name».removeProcess(«processName».processID) ;
			«ENDIF»			
			//3 change properties	
			«IF (stm.change != null)»
				«FOR st : stm.change.sta»
					«dispatchStatement(st, processName)»
				«ENDFOR»
			«ENDIF»			
			//4 set running parameters
			«IF Data.runTime»
				«IF Data.schModel.defcore != null»
					«IF stm.time != null»				
						_time_count[current_core] = 0 ;
						_time_slice[current_core] = «dispatchExpression(stm.time)» ;
						if (_time_slice[current_core] <= 0)
							throw new ValidationException("Invalide running time: " + _time_slice) ;
						_putColIndex[current_core] = (byte) getCollectionIndex("«stm.colTo.name»") ;
					«ELSE»
						_time_count[current_core] = 0 ;
						_time_slice[current_core] = 0 ;								
					«ENDIF»
				«ELSE»
					«IF stm.time != null»				
						_time_count = 0 ;
						_time_slice = «dispatchExpression(stm.time)» ;
						if (_time_slice <= 0)
							throw new ValidationException("Invalide running time: " + _time_slice) ;
						_putColIndex = (byte) getCollectionIndex("«stm.colTo.name»") ;
					«ELSE»
						_time_count = 0 ;
						_time_slice = 0 ;								
					«ENDIF»
				«ENDIF»
			«ENDIF»
		}//GetProcess statement							
	'''
	static def dispatch dispatchStatement(BlockStatement stm, String processName) 
	'''
		{//BlockStatement
			«FOR st : stm.statements»
				«dispatchStatement(st, processName)»
			«ENDFOR»
		}
	'''
	static def dispatch dispatchStatement(AssertStatement stm, String processName)
	'''	
		{//assert statement					
«««			if(!(«dispatchExpression(stm.expr)»)) throw new AssertionException("!(«dispatchExpression(stm.expr)»") ;
			if(!(«dispatchExpression(stm.expr)»)) throw new AssertionException("!(«stm.expr.node.tokenText.replace("\"","")»") ;
		}
	'''
	static def dispatch dispatchStatement(LoopProcess stm, String processName)
	'''
		{//Loop Process (original statement)			
			Iterator<ArrayList<SchedulerProcess>> iterator_«stm.colFrom.name» = «stm.colFrom.name».dataSet.iterator();
			while(iterator_«stm.colFrom.name».hasNext()){				
				Iterator<SchedulerProcess> iterator_«stm.pname.name» = iterator_«stm.colFrom.name».next().iterator();
				while(iterator_«stm.pname.name».hasNext()){
					SchedulerProcess «stm.pname.name» = iterator_«stm.pname.name».next();
					«dispatchStatement(stm.statement, processName)»
				}
			}
		}
	'''
	static def dispatch dispatchStatement(NewProcessStatement stm, String processName)
	'''
		{//NewProcessStatement
			//get the model for executing new process
			SchedulerPanModel panmodel = (SchedulerPanModel) SchedulerPromelaModel.panmodel ;
			//execute new process		
			PromelaProcess proc = panmodel.new «stm.element.process.name»_0() ;
			int id = SchedulerPanModel.panmodel.newProcess(proc, «stm.max», "");
			if (id >= 0 ) {
				ArrayList<String> para = new ArrayList<String>() ;
				«IF stm.element.paraAssign != null »
					«FOR par : stm.element.paraAssign»
						«IF par.num != null»
							para.add("«par.num.value»") ;
						«ELSE»
							«IF par.bool != null»
								para.add("«par.bool.value»") ;
							«ELSE»
								para.add(Integer.toString(«par.id»)) ;
							«ENDIF»
						«ENDIF»
					«ENDFOR»
				«ENDIF»
				executeProcess(proc, id, para) ;
			} else {
				//System.out.println("Can not execute new process «stm.element.process.name»") ; //throw new ValidationException
			}
		}
	'''
	static def dispatch dispatchStatement(ExecuteProcess stm, String processName)
	'''
		{//ExecuteProcess
			«IF Data.initProcessList»
				//running_process = «stm.process.name» ; do not execute process when initializing
			«ELSE»
				running_process = «stm.process.name» ;
			«ENDIF»
		}
	'''
	static def dispatch dispatchStatement(PrintStatement stm, String processName)
	'''
		{//PrintStatement
			System.out.println(«dispatchExpression(stm.st)») ;
		}
	'''
	static def dispatch dispatchStatement(PrintLogStatement stm, String processName)
	'''
		{//PrintLogStatement
			Log.out.println(«dispatchExpression(stm.st)») ;
		}
	'''
	static def dispatch dispatchStatement(GenCodeStatement stm, String processName)
	'''
		«IF stm.comp == null»
			Generate.out.append(«dispatchExpression(stm.st)») ;
		«ELSE»
			Code.addCodetoComponent(«dispatchExpression(stm.st)», "«stm.comp»",false);
		«ENDIF»
	'''
	static def dispatch dispatchStatement(GenLnCodeStatement stm, String processName)
	'''
		«IF stm.comp == null»
			Generate.out.appendLine(«dispatchExpression(stm.st)») ;
		«ELSE»
			Code.addCodetoComponent(«dispatchExpression(stm.st)», "«stm.comp»",true);
		«ENDIF»		
	'''
	static def dispatch dispatchStatement(CheckPoint stm, String processName)
	'''
		{//CheckPoint
			this.«stm.pointid.name» = true ;			
		}
	'''
	static def dispatch dispatchStatement(SetExecTime stm, String processName)
	'''
		{//SetExecTime
			«IF Data.schModel.defcore != null»
				_time_slice[current_core] = «Statements.dispatchExpression(stm.expr)» ;	//runtime = «Data.runTime = true»
			«ELSE»
				_time_slice = «Statements.dispatchExpression(stm.expr)» ;	//runtime = «Data.runTime = true»
			«ENDIF»
		}
	'''
	static def dispatch dispatchStatement(SetReturnCol stm, String processName)
	'''
		{//SetReturnCol
			«IF Data.schModel.defcore != null»
				_putColIndex[current_core].clear();
				_putColIndex[current_core].add((byte) getCollectionIndex("«stm.col.name»")) ; //runtime = «Data.runTime = true»
			«ELSE»
				_putColIndex = (byte) getCollectionIndex("«stm.col.name»") ; //runtime = «Data.runTime = true»
			«ENDIF»	
		}
	'''
	static def dispatch dispatchStatement(SetReturnSet stm, String processName)
	'''
		{//SetReturnSet
			«IF Data.schModel.defcore != null»
				_putColIndex[current_core].clear();
				«FOR SchedulerSet s : stm.col»
					_putColIndex[current_core].add((byte) getCollectionIndex("«s.name»")) ; //runtime = «Data.runTime = true»
				«ENDFOR»
			«ELSE»
				_putColIndex = (byte) getCollectionIndex("«stm.col.get(0).name»") ; //runtime = «Data.runTime = true»
			«ENDIF»	
		}
	'''
	
	static def dispatch dispatchStatement(DeclareProcess stm, String processName)
	'''
		//DeclareProcess
		SchedulerProcess «stm.process.name» ;		
	'''
	static def dispatch dispatchStatement(SetProcess stm, String processName)
	'''
		//SetProcess
		«IF stm.pid == null»
			«stm.process.name» = findProcessByID(«stm.id») ;			
		«ELSE»	
			«stm.process.name» = findProcessByID(«stm.pid») ;
		«ENDIF»		
	'''
//	static def dispatch dispatchStatement(SetGenTemplate stm, String processName)
//	'''
//		templateID = «templateCnt++» ;
//	'''	
	
		
	//-------------------------------------------------------------------------------------------------------------------------------
	// this part for dispatching statement with process list
	
	static def genStmwithProcessList (Stm st, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) {
		System.out.println("1. remain: " + st.ifdef)
		genIfDef(st.ifdef)
		dispatchStatementwithProcessList(st.statement, remainProcessList, functionProcessList, definedProcessSet)		
		genCloseIfDef(st.ifdef) 
	}
	static def dispatch dispatchStatementwithProcessList(ReturnStatement stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''
		//no return statement supported (interface function)
	'''
	static def dispatch dispatchStatementwithProcessList(SetExecTime stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''
		//no SetExecTime statement supported (interface function)
	'''
	static def dispatch dispatchStatementwithProcessList(SetReturnCol stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''
		//no SetReturnCol statement supported (interface function)
	'''
	static def dispatch dispatchStatementwithProcessList(SetReturnSet stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''
		//no SetReturnSet statement supported (interface function)
	'''
	
	static def dispatch dispatchStatementwithProcessList(MoveProcess stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''		
		{//MoveProcess: (interface function), remainder (for loop): «remainProcessList», function process list (for loop): «functionProcessList»		
			«IF stm.process.name.contains("running_process")»
				if (running_process != null) {
					«stm.colTo.name».put(running_process) ;
					running_process = null ;
					«IF Data.schModel.defcore != null»
						running_procs[current_core] = null ;
					«ENDIF»
				}
			«ELSE»
				«IF functionProcessList.contains(stm.process.name)»
					if (a_«stm.process.name».size() == 0) { 
						//«stm.process.name» = null -> do nothing
					} else {//loop the set a_«stm.process.name»
						«IF remainProcessList.contains(stm.process.name)»
							for (SchedulerProcess «stm.process.name» : a_«stm.process.name») {
								«IF Data.schModel.scheduler != null»
									«IF Data.schModel.scheduler.schedulerdata != null»
										if («stm.process.name» != null) {
«««											//removeProcess(«stm.process.name») ;
											remove_process(«stm.process.name».processID);							
											«stm.colTo.name».put(«stm.process.name») ;
										}
									«ENDIF»
								«ENDIF»							
								if (running_process != null) {
									if («stm.process.name».processID == running_process.processID)
										running_process = null ;
									«IF Data.schModel.defcore != null»
										running_procs[current_core] = null ;
									«ENDIF»
								}
							}
						«ELSE»
							«IF Data.schModel.scheduler != null»
								if («stm.process.name» != null) {	
									remove_process(«stm.process.name».processID) ;						
									«stm.colTo.name».put(«stm.process.name») ;
									if (running_process != null) {
										if («stm.process.name».processID == running_process.processID)
											running_process = null ;
										«IF Data.schModel.defcore != null»
											running_procs[current_core] = null ;
										«ENDIF»
									}
								}
							«ENDIF»
						«ENDIF»
					}
				«ELSE»
					iterator_«stm.process.name».remove() ;
					«stm.colTo.name».put(«stm.process.name») ;
					if (running_process != null) {
						if («stm.process.name».processID == running_process.processID)
							running_process = null ;
						«IF Data.schModel.defcore != null»
							running_procs[current_core] = null ;
						«ENDIF»
					}
				«ENDIF»
			«ENDIF»			
		}	
	'''
	
	static def dispatch dispatchStatementwithProcessList(ReorderProcess stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''		
		{//ReorderProcess: (interface function), remainder (for loop): «remainProcessList», function process list (for loop): «functionProcessList»		
			«IF stm.process.name.contains("running_process")»
				//do nothing with running_process
			«ELSE»
				«IF functionProcessList.contains(stm.process.name)»
					if (a_«stm.process.name».size() == 0) { 
						//«stm.process.name» = null -> do nothing
					} else {//loop the set a_«stm.process.name»
						«IF remainProcessList.contains(stm.process.name)»
							for (SchedulerProcess «stm.process.name» : a_«stm.process.name») {
								if («stm.colTo.name».hasProcess(«stm.process.name»)) {
									«stm.colTo.name».remove_process(«stm.process.name».processID);							
									«stm.colTo.name».put(«stm.process.name») ;
								}										
							}
						«ELSE»
							«IF Data.schModel.scheduler != null»
								if («stm.process.name» != null) {	
									if («stm.colTo.name».hasProcess(«stm.process.name»)>0) {
										«stm.colTo.name».removeProcess(«stm.process.name».processID);							
										«stm.colTo.name».put(«stm.process.name») ;
									}
								}
							«ENDIF»
						«ENDIF»
					}
				«ELSE»
					iterator_«stm.process.name».remove() ;
					«stm.colTo.name».put(«stm.process.name») ;					
				«ENDIF»
			«ENDIF»			
		}	
	'''
	
	
	static def dispatch dispatchStatementwithProcessList(SetProcessInstance stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''		
		{//SetProcessInstance: (interface function), remainder (for loop): «remainProcessList», function process list (for loop): «functionProcessList»		
			«IF stm.process.name.contains("running_process")»				
			«ELSE»
				
				if (running_process != null) {
					if («stm.process.name».processID == running_process.processID) {
					}
				} else {
					«IF functionProcessList.contains(stm.process.name)»
						if (a_«stm.process.name».size() == 0) { 
							//«stm.process.name» = null -> do nothing
						} else {//loop the set a_«stm.process.name»
							«IF remainProcessList.contains(stm.process.name)»
								for (SchedulerProcess «stm.process.name» : a_«stm.process.name») {
									«IF Data.schModel.scheduler != null»
										«IF Data.schModel.scheduler.schedulerdata != null»
											if («stm.process.name» != null) {							
												«stm.colTo.name».put(«stm.process.name») ;
											}
										«ENDIF»
									«ENDIF»							
								}
							«ELSE»
								«IF Data.schModel.scheduler != null»
									if («stm.process.name» != null) {
										«stm.colTo.name».put(«stm.process.name») ;
									}
								«ENDIF»
							«ENDIF»
						}
					«ELSE»						
						«stm.colTo.name».put(«stm.process.name») ;						
					«ENDIF»
				}
			«ENDIF»			
		}	
	'''
	static def dispatch dispatchStatementwithProcessList(RemoveProcess stm,	ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''		
		{//remove process (interface function), remainder (for loop): «remainProcessList», function list: «functionProcessList»
			«IF stm.process.name.contains("running_process")»
				if (running_process != null) {					
					endP = running_process.processID ;
					processInScheduler[running_process.processID] = false ;
					SchedulerPanModel.panmodel.reduceProcess() ;
					running_process = null ;
					«IF Data.schModel.defcore != null»
						running_procs[current_core] = null ;
					«ENDIF»
				}
			«ELSE»
				«IF functionProcessList.contains(stm.process.name)»
					if (a_«stm.process.name».size() == 0) { 
						//«stm.process.name» = null -> do nothing
					} else {//loop the set a_«stm.process.name»					
						for (SchedulerProcess «stm.process.name» : a_«stm.process.name») {
							if (running_process != null) {
								if («stm.process.name».processID == running_process.processID) {
									endP = running_process.processID ;
									processInScheduler[running_process.processID] = false ;
									running_process = null ;
									«IF Data.schModel.defcore != null»
										running_procs[current_core] = null ;
									«ENDIF»
								} else {
									remove_process(«stm.process.name».processID) ;
									processInScheduler[«stm.process.name».processID] = false ;
								}								
							} else {
«««								«FOR col : Data.collectionList»
«««									«col».removeProcess(«stm.process.name».processID) ;
«««								«ENDFOR»
								remove_process(«stm.process.name».processID) ;
								processInScheduler[«stm.process.name».processID] = false ;
							}
							SchedulerPanModel.panmodel.reduceProcess() ;
						}
					}
				«ELSE»
					iterator_«stm.process.name».remove() ;
					SchedulerPanModel.panmodel.reduceProcess() ;
				«ENDIF»
			«ENDIF»
		}
	'''
	static def dispatch dispatchStatementwithProcessList(ChangeAction stm,	ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''				
		{//Change statement -> interface function
			«FOR st : stm.sta»
				«dispatchStatementwithProcessList(st, remainProcessList, functionProcessList, definedProcessSet)»
			«ENDFOR»
		}
	'''
	static def dispatch dispatchStatementwithProcessList(ChangeValueUnOp stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''
		{//change value unop (interface function), remainder (for loop): «remainProcessList», function list: «functionProcessList»		
			«IF remainProcessList.size == 0 && functionProcessList.size == 0»
				«dispatchStatement(stm, '')»
			«ELSE»
				«IF remainProcessList.size != 0»
					«var pName = remainProcessList.remove(0)»//remove «pName»
					//add «pName» : defined «definedProcessSet.add(pName)»
«««					«IF remainProcessList.contains(pName)»
					for (SchedulerProcess «pName» : a_«pName») {
						«dispatchQualifiedNameswithProcessList(stm.getVar) + stm.getOp()» ;							
					}
«««					«ENDIF»
				«ELSE»
					«var pN = functionProcessList.remove(0)»//remove «pN»					
					«IF (definedProcessSet.contains(pN))»
						«dispatchQualifiedNameswithProcessList(stm.getVar) + stm.getOp()» ';'
					«ELSE»
						//add «pN» to definedProcessSet: «definedProcessSet.add(pN)»						
						ArrayList<SchedulerProcess> a_«pN» = findProcessByAlias("«pN»") ;
						for (SchedulerProcess «pN» : a_«pN») {
							«dispatchQualifiedNameswithProcessList(stm.getVar) + stm.getOp()» ';'
						}
					«ENDIF»
				«ENDIF»
			«ENDIF»
		}		
	'''
		
	static def String dispatchQualifiedNameswithProcessList(QualifiedNames qname){
		if (qname.prop == null)
			return qname.name.toString
		else
			return qname.name + '.' + qname.prop.name 
	}
	static def dispatch dispatchStatementwithProcessList(ChangeValueExpression stm,	ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	{
		var ArrayList<String> stmProcessList = getProcessListFromExpression(stm.exp)		
		if (stm.getVar.prop != null) functionProcessList.add(stm.getVar.name)		
		genChangeValue(stm, remainProcessList, stmProcessList,definedProcessSet)			
	}

	static def genChangeValue(ChangeValueExpression stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''
		{//change value (interface function), remainder (for loop): «remainProcessList», function list: «functionProcessList»		
			«IF remainProcessList.size == 0 && functionProcessList.size == 0»
				«dispatchStatement(stm, '')»
			«ELSE»
				«IF remainProcessList.size != 0»
					«var pName = remainProcessList.remove(0)»//remove «pName»
					//add «pName» : defined «definedProcessSet.add(pName)»
«««					«IF remainProcessList.contains(pName)»
«««						//Loop through the set «remainProcessList.remove(pName)»
					for (SchedulerProcess «pName» : a_«pName») {
						«genChangeValue(stm, remainProcessList, functionProcessList, definedProcessSet)»
					}
«««					«ENDIF»
				«ELSE»
					«var pN = functionProcessList.remove(0)»//remove «pN»					
					«IF (definedProcessSet.contains(pN))»						
						«genChangeValue(stm, remainProcessList, functionProcessList, definedProcessSet)»
					«ELSE»
						//add «pN» to definedProcessSet: «definedProcessSet.add(pN)»
						ArrayList<SchedulerProcess> a_«pN» = findProcessByAlias("«pN»") ;
						for (SchedulerProcess «pN» : a_«pN») {
							«genChangeValue(stm, remainProcessList, functionProcessList, definedProcessSet)»
						}
					«ENDIF»
				«ENDIF»
			«ENDIF»
		}
	'''
	static def genIfStatement(IfStatement stm,  ArrayList<String> processListInStatement, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''
		{//IfStatement (interface function), processListInStatement: «processListInStatement», remainder (for loop): «remainProcessList», function process list (for loop): «functionProcessList» 
			«IF processListInStatement.size() == 0»			
				«IF remainProcessList.size() != 0»
					«var pName = remainProcessList.remove(0)»//remove «pName» from remainProcessList
					//add «pName» to definedProcessSet «definedProcessSet.add(pName)»
«««					//remove «pName» from functionProcessList: «functionProcessList.remove(pName)»
					for (SchedulerProcess «pName» : a_«pName») {					 
						«genIfStatement(stm, processListInStatement, remainProcessList, functionProcessList, definedProcessSet)»
					}
				«ELSE»				
					if («dispatchExpression(stm.condition)»)
						«dispatchStatementwithProcessList(stm.thenBlock, remainProcessList, functionProcessList, definedProcessSet)»
					«IF (stm.elseBlock != null)»
					else 
						«dispatchStatementwithProcessList(stm.elseBlock, remainProcessList, functionProcessList, definedProcessSet)»
					«ENDIF»
				«ENDIF»
			«ELSE»
				«var pName = processListInStatement.remove(0)»
				//add «pName» : defined «definedProcessSet.add(pName)»
				«IF remainProcessList.contains(pName)»
					//loop through the set: «remainProcessList.remove(pName)»
					for (SchedulerProcess «pName» : a_«pName») {					 
						«genIfStatement(stm, processListInStatement, remainProcessList, functionProcessList, definedProcessSet)»
					}
				«ELSE»
					«genIfStatement(stm, processListInStatement, remainProcessList, functionProcessList, definedProcessSet)»
				«ENDIF»			
			«ENDIF»
		}
	'''
		
	static def dispatch dispatchStatementwithProcessList(IfStatement stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) {	
		var ArrayList<String> processListInStatement = getProcessListFromExpression(stm.condition)		
		genIfStatement(stm, processListInStatement, remainProcessList, functionProcessList, definedProcessSet)
	}

	static def ArrayList<String> getProcessListFromExpression(Expression exp) {
		var ArrayList<String> resultList = new ArrayList<String>()
		var ArrayList<String> tempList = new ArrayList<String>()
		if (exp instanceof Or) {
			tempList = getProcessListFromExpression(exp.left)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
			tempList = getProcessListFromExpression(exp.right)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
		} else if (exp instanceof And) {
			tempList = getProcessListFromExpression(exp.left)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
			tempList = getProcessListFromExpression(exp.right)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
		} else if (exp instanceof Equality) {
			tempList = getProcessListFromExpression(exp.left)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
			tempList = getProcessListFromExpression(exp.right)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
		} else if (exp instanceof Comparison) {			
			tempList = getProcessListFromExpression(exp.left)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
			tempList = getProcessListFromExpression(exp.right)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
		} else if (exp instanceof Plus) {
			tempList = getProcessListFromExpression(exp.left)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
			tempList = getProcessListFromExpression(exp.right)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
		} else if (exp instanceof Minus) {
			tempList = getProcessListFromExpression(exp.left)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
			tempList = getProcessListFromExpression(exp.right)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
		} else if (exp instanceof MulOrDiv) {
			tempList = getProcessListFromExpression(exp.left)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
			tempList = getProcessListFromExpression(exp.right)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
		} else if (exp instanceof Implies) {
			tempList = getProcessListFromExpression(exp.left)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
			tempList = getProcessListFromExpression(exp.right)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
		} else if (exp instanceof Not) {
			tempList = getProcessListFromExpression(exp.expression)
			for (String pName : tempList)
				if (!resultList.contains(pName))
					resultList.add(pName)
		} else if (exp instanceof GetIDExpression) {			
			if (!resultList.contains(exp.PN.replace("\"","").replaceAll(" ","").trim))
				resultList.add(exp.PN.replace("\"","").replaceAll(" ","").trim)
		} else if (exp instanceof InExpression) {
			if (!resultList.contains(exp.PN.replace("\"","").replaceAll(" ","").trim))
				resultList.add(exp.PN.replace("\"","").replaceAll(" ","").trim)
		} else if (exp instanceof InPIDExpression) {
			if (!resultList.contains(exp.id.replace("\"","").replaceAll(" ","").trim))
				resultList.add(exp.id.replace("\"","").replaceAll(" ","").trim)
		} 
//		else if (exp instanceof InPExpression) {
//			if (!resultList.contains(exp.PN.replace("\"","").replaceAll(" ","").trim))
//				resultList.add(exp.PN.replace("\"","").replaceAll(" ","").trim)
//		} 
		
		
			else if (exp instanceof EmptyExpression) { //process collection -> nothing
		} else if (exp instanceof NullExpression) {
			if (!resultList.contains(exp.procName.name))
				resultList.add(exp.procName.name)
		} else if (exp instanceof IntConstant) {
		} else if (exp instanceof BoolConstant) {
		} else if (exp instanceof StateID) {
		} else if (exp instanceof BranchID) {
		} else if (exp instanceof GetInstance){
		} else if (exp instanceof GetPID) {	
		} else if (exp instanceof Action) {
		} else if (exp instanceof Step) {
		} else if (exp instanceof TotalStep){
		} else if (exp instanceof ExistExpression) {
//			if (!resultList.contains(exp.proc.toString))
//				resultList.add(exp.proc.toString)
		}else if (exp instanceof HasNameExpression) {
			if (!resultList.contains(exp.PN.replace("\"","").replaceAll(" ","").replaceAll(" ","").trim))
				resultList.add(exp.PN.replace("\"","").replaceAll(" ","").trim)
		} else if (exp instanceof Atomic) {
			//System.out.println("atomic" + exp.charseq + exp.getVar)
			if (exp.getVar != null) {
				if (exp.getVar.prop != null)
					resultList.add(exp.getVar.name)				
			} 
		}	
		return resultList
	}
	static def ArrayList<String> getProcessListFromRTCTLExpression(RTCTL rtctl_exp) {
		var ArrayList<String> resultList = new ArrayList<String>()
		if (rtctl_exp.exp != null)
			resultList = getProcessListFromExpression(rtctl_exp.exp)
		else 
			if (rtctl_exp.f != null)
				resultList = getProcessListFromRTCTLExpression(rtctl_exp.f)
			else {
				resultList = getProcessListFromRTCTLExpression(rtctl_exp.f1)
				resultList.addAll(getProcessListFromRTCTLExpression(rtctl_exp.f2))
			}
		return resultList		
	}
	static def HashSet<String> getProcessCollectionFromStatement(Statement st) {
		var HashSet<String> resultList = new HashSet<String>()
		if (st instanceof MoveProcess) {
			resultList.add(st.colTo.name)
		}	
		if (st instanceof SetProcessInstance) {
			resultList.add(st.colTo.name)
		}				
		if (st instanceof LoopProcess)
			resultList.add(st.colFrom.name)
		if (st instanceof BlockStatement) {
			for (sta : st.statements)
				resultList.addAll(getProcessCollectionFromStatement(sta))	
		}	
		if (st instanceof IfStatement) {
			resultList.addAll(getProcessCollectionFromStatement(st.thenBlock))
			if (st.elseBlock != null)
				resultList.addAll(getProcessCollectionFromStatement(st.elseBlock))
		}			
		return resultList ;
	}
	static def HashSet<String> getProcessCollectionFromEvent(EventDef e) {
		var HashSet<String> result = new HashSet<String>() 
		if (e.event instanceof EventStm)
			for (sta : (e.event as EventStm).statements)
				result.addAll(getProcessCollectionFromStatement(sta.statement))
		else		
			for (opt :(e.event as EventOpt).opt)
				for (sta : opt.eventstm.statements)
					result.addAll(getProcessCollectionFromStatement(sta.statement))
		return result
	}
	static def String dispatchQualifiedNamesInAssignStatementwithProcessList(QualifiedNames qname, ArrayList<String> remainProcessList) {
		if (qname.prop == null)
			return qname.name.toString
		else
			return qname.name + '.' + qname.prop.name 
	}
	

	static def dispatch dispatchStatementwithProcessList(GetProcess stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''			
		{//GetProcess statement		
			//no support for selecting a process to run
		}						
	'''
	static def dispatch dispatchStatementwithProcessList(BlockStatement stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''
		{//Block statement (interface function)	
			«FOR st : stm.statements»
				«dispatchStatementwithProcessList(st, remainProcessList, functionProcessList, definedProcessSet)»
			«ENDFOR»
		}
	'''
	static def dispatch dispatchStatementwithProcessList(AssertStatement stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''			
		{//assert statement	(interface function)				
			«var ArrayList<String> processListInStatement = getProcessListFromExpression(stm.expr)»
			«genAssert(stm, processListInStatement, remainProcessList, functionProcessList, definedProcessSet)»			
		}

	'''
	static def genAssert(AssertStatement stm,  ArrayList<String> processListInStatement, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''		
		//remain process list: «remainProcessList», processList : «processListInStatement»
		«IF processListInStatement.size() == 0»
			//dispatch statement
			«dispatchStatement(stm, '')»
		«ELSE»
			«var pName = processListInStatement.remove(0)»//remove «pName»
			«IF remainProcessList.contains(pName)»
				for (SchedulerProcess «pName» : a_«pName») {
					//«remainProcessList.remove(pName)» 
					«genAssert(stm, processListInStatement,remainProcessList, functionProcessList, definedProcessSet)»
				}
			«ELSE»
				«genAssert(stm, processListInStatement,remainProcessList, functionProcessList, definedProcessSet)»
			«ENDIF»
		«ENDIF»
	'''
	static def dispatch dispatchStatementwithProcessList(NewProcessStatement stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet) 
	'''
		{//NewProcessStatement
			SchedulerPanModel panmodel = (SchedulerPanModel) SchedulerPromelaModel.panmodel ;
			PromelaProcess proc = panmodel.new «stm.element.process.name»_0() ;			
			int id = SchedulerPanModel.panmodel.newProcess(proc, «stm.max», "");
			if (id >= 0 ) {
				ArrayList<String> para = new ArrayList<String>() ;
				«IF stm.element.paraAssign != null »
					«FOR par : stm.element.paraAssign»
						«IF par.num != null»
							para.add("«par.num.value»") ;
						«ELSE»
							«IF par.bool != null»
								para.add("«par.bool.value»") ;
							«ELSE»
								para.add(Integer.toString(«par.id»)) ;
							«ENDIF»
						«ENDIF»
					«ENDFOR»
				«ENDIF»
				executeProcess(proc, id, para) ;
			} else {
				//System.out.println("Can not execute new process «stm.element.process.name»") ; //throw new ValidationException
			}
		}
	'''
	static def dispatch dispatchStatementwithProcessList(LoopProcess stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet)
	'''
		{//Loop Process: -> not contain process parameter (interface function)
			Iterator<ArrayList<SchedulerProcess>> iterator_«stm.colFrom.name» = «stm.colFrom.name».dataSet.iterator();
			while(iterator_«stm.colFrom.name».hasNext()){				
				Iterator<SchedulerProcess> iterator_«stm.pname.name» = iterator_«stm.colFrom.name».next().iterator();
				while(iterator_«stm.pname.name».hasNext()){
					SchedulerProcess «stm.pname.name» = iterator_«stm.pname.name».next();
					«dispatchStatementwithProcessList(stm.statement, remainProcessList, functionProcessList, definedProcessSet)»
				}
			}
		}
	'''
	static def dispatch dispatchStatementwithProcessList(ExecuteProcess stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet)
	'''
		{//ExecuteProcess -> not contain process parameter (interface function)
			running_process = «stm.process.name» ;
		}
	''' 
	static def dispatch dispatchStatementwithProcessList(PrintStatement stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet)
	'''
		{//PrintStatement -> not contain process parameter (interface function)
			System.out.println(«dispatchExpression(stm.st)») ;
		}
	'''
	static def dispatch dispatchStatementwithProcessList(GenCodeStatement stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet)
	'''
		{//GenLnCodeStatement: (interface function), 
		//remainder (for loop): «remainProcessList», function process list (for loop): «functionProcessList»							
			«IF remainProcessList.size == 0 && functionProcessList.size == 0»
				«dispatchStatement(stm, '')»
			«ELSE»
				«IF remainProcessList.size != 0»
					«var pName = remainProcessList.remove(0)»//remove «pName»
					//add «pName» to definedProcessSet «definedProcessSet.add(pName)»
					for (SchedulerProcess «pName» : a_«pName») {
						«IF stm.comp == null»
							Generate.out.appendLine(«dispatchExpression(stm.st)») ;
						«ELSE»
							Code.addCodetoComponent(«dispatchExpression(stm.st)», "«stm.comp»",false);
						«ENDIF»					
					}
				«ELSE»
					«var pN = functionProcessList.remove(0)»//remove «pN»					
					«IF (definedProcessSet.contains(pN))»
						«IF stm.comp == null»
							Generate.out.appendLine(«dispatchExpression(stm.st)») ;
						«ELSE»
							Code.addCodetoComponent(«dispatchExpression(stm.st)», "«stm.comp»",false);
						«ENDIF»
					«ELSE»
						//add «pN» to definedProcessSet: «definedProcessSet.add(pN)»
						ArrayList<SchedulerProcess> a_«pN» = findProcessByAlias("«pN»") ;
						for (SchedulerProcess «pN» : a_«pN») {
							«IF stm.comp == null»
								Generate.out.appendLine(«dispatchExpression(stm.st)») ;
							«ELSE»
								Code.addCodetoComponent(«dispatchExpression(stm.st)», "«stm.comp»",false);
							«ENDIF»
						}
					«ENDIF»
				«ENDIF»
			«ENDIF»			
		}		
	'''
	static def dispatch dispatchStatementwithProcessList(GenLnCodeStatement stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet)
	'''
		{//GenLnCodeStatement: (interface function)
		//remainder (for loop): «remainProcessList», function process list (for loop): «functionProcessList»
					
			«IF remainProcessList.size == 0 && functionProcessList.size == 0»
				«dispatchStatement(stm, '')»
			«ELSE»
				«IF remainProcessList.size != 0»
					«var pName = remainProcessList.remove(0)»//remove «pName»
					//add «pName» : defined «definedProcessSet.add(pName)»
					for (SchedulerProcess «pName» : a_«pName») {
						«IF stm.comp == null»
							Generate.out.appendLine(«dispatchExpression(stm.st)») ;
						«ELSE»
							Code.addCodetoComponent(«dispatchExpression(stm.st)», "«stm.comp»",true);
						«ENDIF»						
					}
				«ELSE»
					«var pN = functionProcessList.remove(0)»//remove «pN»					
					«IF (definedProcessSet.contains(pN))»
						«IF stm.comp == null»
							Generate.out.appendLine(«dispatchExpression(stm.st)») ;
						«ELSE»
							Code.addCodetoComponent(«dispatchExpression(stm.st)», "«stm.comp»",true);
						«ENDIF»						
					«ELSE»						
						//add «pN» to definedProcessSet: «definedProcessSet.add(pN)»						
						ArrayList<SchedulerProcess> a_«pN» = findProcessByAlias("«pN»") ;
						for (SchedulerProcess «pN» : a_«pN») {
							«IF stm.comp == null»
								Generate.out.appendLine(«dispatchExpression(stm.st)») ;
							«ELSE»
								Code.addCodetoComponent(«dispatchExpression(stm.st)», "«stm.comp»",true);
							«ENDIF»							
						}
					«ENDIF»					
				«ENDIF»
			«ENDIF»			
		}
	'''
	static def dispatch dispatchStatementwithProcessList(PrintLogStatement stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet)
	'''
		{//PrintLogStatement -> not contain process parameter (interface function)
			Log.out.println(«dispatchExpression(stm.st)») ;
		}
	'''
	static def dispatch dispatchStatementwithProcessList(CheckPoint stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet)
	'''
		{//CheckPoint -> not contain process parameter (interface function)			
			this.«stm.pointid.name» = true ;
		}
	'''
	static def dispatch dispatchStatementwithProcessList(DeclareProcess stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet)
	'''
		//DeclareProcess
		SchedulerProcess «stm.process.name» ;	
	'''
	static def dispatch dispatchStatementwithProcessList(SetProcess stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet)
	'''
		//SetProcess
		«IF stm.pid == null»
			«stm.process.name» = findProcessByID(«stm.id») ;			
		«ELSE»	
			«stm.process.name» = findProcessByID(«stm.pid») ;
		«ENDIF»
	'''
//	static def dispatch dispatchStatementwithProcessList(SetGenTemplate stm, ArrayList<String> remainProcessList, ArrayList<String> functionProcessList, HashSet<String> definedProcessSet)
//	'''		
//	'''	
}
