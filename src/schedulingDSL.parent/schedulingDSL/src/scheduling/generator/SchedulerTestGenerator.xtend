package scheduling.generator

import java.util.ArrayList
import scheduling.dsl.ASSIGN
import scheduling.dsl.Action
import scheduling.dsl.Config
import scheduling.dsl.DeclareEvent
import scheduling.dsl.Event
import scheduling.dsl.Flow
import scheduling.dsl.GUARD
import scheduling.dsl.IFs
import scheduling.dsl.OneDec
import scheduling.dsl.SKIP
import scheduling.generator.Statements
import java.util.HashMap
import scheduling.dsl.Define
import scheduling.dsl.Function
import scheduling.dsl.ListElement
import javax.sound.sampled.EnumControl.Type

class SchedulerTestGenerator {
	
	public static int testCount = 0 
	static ArrayList<String> event = new ArrayList<String>() 
	public static HashMap<String,String> cst = new HashMap<String,String>()
	public static var HashMap<String, ArrayList<String>> defrefval = new HashMap<String, ArrayList<String>>()
	public static var ArrayList<String> funcparaval = new ArrayList<String>()
	public static var HashMap<String,String> mapfuncparamval = new HashMap<String,String>()
	public static var HashMap<String, ArrayList<String>> mapenumval = new HashMap<String,ArrayList<String>>()
	public static var HashMap<String, String> mapenumtype = new HashMap<String,String>
	
	static def getEnumType(String en) {
		if (mapenumtype.containsKey(en))
			return mapenumtype.get(en)
		return ''
	}
	static def getEnumVal(String type, String e) {
		if (mapenumval.containsKey(type)) 
			return mapenumval.get(type).indexOf(e).toString
		return e  
	}
	static def getEnumVal(String e) {
		for (elist : mapenumval.values) {
			var index = elist.indexOf(e)
			if (index >= 0)
				return index.toString
		}
//		val type = getEnumType(e)
//		if (!type.empty) {
//			return getEnumVal(type, e)
//		}
		return '(' + e + ')'
	}
	static def getReference(ListElement lval) {
		if (lval.id != null) {
			if (cst.containsValue(lval.id))
				return defrefval.get(lval.name).get(Integer.parseInt(cst.get(lval.id))-1)
			if (mapfuncparamval.containsKey(lval.id)) {
				var String result = defrefval.get(lval.name).get(Integer.parseInt(mapfuncparamval.get(lval.id))-1)
				return result
			}
		} else {
			return defrefval.get(lval.name).get(lval.num - 1)
		}
	}
	
	static def getValue(String v) {
		if (cst.containsKey(v))
			return cst.get(v)
		if (mapfuncparamval.containsKey(v))
			return mapfuncparamval.get(v)
		return getEnumVal(v)
	}
//	static def repReference(String exp){
//		var String result = exp
//		for (v : cst.keySet) {
//			result = result.replace('('+v+')', cst.get(v))
//		}
//		for (v : mapfuncparamval.keySet) {
//			result = result.replace('('+v+')', mapfuncparamval.get(v))
//		}
//		return result
//	}
//	static def genVar(String event){
//		var result = 'byte '
//		for (i : 0 ..< 0)
//			result += 'sch.' + event + i + ','
//		result += 'sch.' + event + 255 + ';'
//	}
	
	static def TesttoPromelaCode()
	'''					
		«IF (Test.specification != null)»
			«IF Test.specification.define != null»
				«initdefvalue(Test.specification.define)»
			«ENDIF»
«««		 && Test.scenarios != null && Test.rules != null)»			
«««		«IF (Test.specification.decl_sch != null)»
«««			«IF Test.procModel != null»	
«««			/* Generate variables for scheduler */			
«««				«FOR p : Test.procModel.process»
«««					byte sch.new_process.«p.proctype.name» ;
«««					/* byte sch.block_process.«p.proctype.name» ; */	
«««					/* byte sch.unblock_process.«p.proctype.name» ; */
«««					byte sch.terminate_process.«p.proctype.name» ;			
«««				«ENDFOR»
«««				«FOR i : 0..< 0»
«««					byte sch.block_process.«i» = 0;
«««					byte sch.unblock_process.«i» = 0;
«««				«ENDFOR»			
«««			«ENDIF»
«««			
«««			«IF Test.configs != null»
«««			/* Generate variables for scheduler configuration */
«««				«var index = 0»
«««				«FOR conf : Test.configs.config»	
«««					byte sch.config.«index++» ;
«««				«ENDFOR»
«««			«ENDIF»
«««		«ENDIF»		
		/* Generate checking code*/		
		/* 1 variables */				
			«IF Test.specification.decl_lst != null»	
				«cst.clear»				
				«mapenumval.clear»		
				«FOR dec : Test.specification.decl_lst.dec»					
					«genVariable(dec)»
«««					«IF dec.getVar != null»
«««						«FOR vname : dec.getVar.name»	
«««							«IF dec.getVar.type.toString == 'bool'» 
«««								int «vname.name»
«««							«ELSE»
«««								«dec.getVar.type» «vname.name»
«««							«ENDIF»	
«««														
«««							«IF vname.bvalue != null»
«««								=  «IF vname.bvalue.value == 'true'» 0 «ELSE» 1 «ENDIF»
«««							«ELSE» 
«««								=  «vname.ivalue»
«««							«ENDIF» ;
«««						«ENDFOR»
«««					«ELSE»
«««						«IF dec instanceof ArrayDec»
«««						
«««						«ENDIF»
«««					«ENDIF»
				«ENDFOR»
			«ENDIF»								
			int _test_depth = «getTestDepth()»			
			
		/* 2 proctype for each event*/
			«FOR e : Test.specification.event»				
				«genProctype(e, Test.specification.define)»
			«ENDFOR»		
«««		«IF Test.specification.decl_sch != null»
«««		/* scheduler data declaration*/		
«««			«initSchedulerEventList()»
«««			«genSchedulerTest()»
«««			«genSchedulerEvent()»
«««		«ENDIF»	
		
		/* 3 generate verication cases */
			«generateVerificationCase()»
			
		/* 4 generate run test code */
			«genRunVerificationCase()»
			
			«IF Test.verify != null»
		/* 5 verify */
			verify {
				«IF Test.verify.at != null»@«Statements.dispatchExpression(Test.verify.at.cond)» : «ENDIF»«Statements.dispatchRTCTLExpression(Test.verify.formula)»
			}
			«ENDIF»
		«ENDIF»				
	'''		
	
	static def int getTestDepth() {
		var int depth = 0
		if (Test.scenarios != null) {		
			if (Test.scenarios.flow != null) {			
				for (f : Test.scenarios.flow) {
					if (depth < f.event.size())
						depth = f.event.size() + 3
				}
			}//for preparing the stack
		} else if (Test.permutation != null) {
			if (depth < Test.permutation.step)
				depth = Test.permutation.step + 2
		}			
		return depth 
	}
//	
//	static def void initSchedulerEventList() {
//		event.clear
////		event.add('select_process')
//		event.add('new_process')
////		event.add('block_process')
////		event.add('unblock_process') 
////		event.add('clock')
////		event.add('preempt_process')
////		event.add('change_property')
////		event.add('terminate_process')
//	}
//	
//	static def genSchedulerTest() 
//	'''			
//		proctype scheduler() {
//			do
//				«FOR e : event»
//					:: run «e»() 
//				«ENDFOR»
//				:: else -> skip
//			od
//		}
//	'''
//	
//	
//	static def genSchedulerEvent() 
//	'''		
//		«FOR e : event»
//			proctype «e»() { /* for scheduler event «e» */
//				«genBooleanVariable(e)»		
//				«IF e.equals('new_process')»
//					if
//«««						test «Test»
//«««						test model «Test.model»
//«««						test proc «Test.procModel»
//						«IF Test.procModel != null»
//							«FOR p : Test.procModel.process»
//								:: d_step {
//										«genCheckBooleanVariable(e)»
//										
//										/* insert event «e»*/
//										sch.new_process.«p.proctype.name» = 0;
//										
//										«genConclusionBooleanVariable(e)»
//									}
//							«ENDFOR»
//						«ELSE»
//						«ENDIF»
//						:: skip
//					fi
//				«ELSE»
//					«IF e.equals('block_process')»
//						if
//							«FOR i : 0..< 0»
//								:: d_step {
//										«genCheckBooleanVariable(e)»										
//										/* insert event «e»*/
//										sch.block_process.«i» = 0;										
//										«genConclusionBooleanVariable(e)»
//									}
//							«ENDFOR»
//							:: skip
//						fi								
//					«ELSE»
//						«IF e.equals('unblock_process')»
//							if
//								«FOR i : 0..< 0»
//									:: d_step {
//											«genCheckBooleanVariable(e)»										
//											/* insert event «e»*/
//											sch.unblock_process.«i» = 0;										
//											«genConclusionBooleanVariable(e)»
//										}
//								«ENDFOR»
//								:: skip
//							fi								
//						«ELSE»
//							«genBooleanVariable(e)»
//							if
//								«IF Test.procModel != null»
//									«FOR p : Test.procModel.process»
//										:: d_step {
//												«genCheckBooleanVariable(e)»
//												
//												/* insert event «e»*/
//												sch.«e».«p.proctype.name» = 0;
//												
//												«genConclusionBooleanVariable(e)»
//											}
//									«ENDFOR»
//								«ELSE»						
//								«ENDIF»
//								:: skip
//							fi
//						«ENDIF»
//					«ENDIF»
//				«ENDIF»
//			}
//			
//		«ENDFOR»
//		
//	'''
	
	static def test() {
		
	}
	
	static def String genVariable(OneDec dec) {
		if (dec.const != null) {
			if (dec.const.bvalue != null)
				cst.put(dec.const.name,dec.const.bvalue.toString)
			else
				cst.put(dec.const.name,dec.const.ivalue.value.toString)			
		} else { 
			if (dec.enumdec != null) {
				var ArrayList<String> elist = new ArrayList<String>
				for (e : dec.enumdec.enumele)
					elist.add(e)				
				mapenumval.put(dec.enumdec.type.name, elist)
				return ''	
			} else {				
				var result = 'int '
				if (dec.getVar != null) {
					if (dec.getVar.enumtype != null) {
						for (vname : dec.getVar.name) {
							mapenumtype.put(vname.name, dec.getVar.enumtype.name)
						}
					}
					
					var index = 0 
					for (vname : dec.getVar.name) {
						result += vname.name
						index ++  
						
						if (dec.getVar.type.toString == 'bool') {
							if (vname.bvalue != null) {
								if (vname.bvalue.value == 'true')					
									result +=  ' = 0'
								else
									result +=  ' = 1'
							} 
						} else {
							if (vname.ivalue == null)
								result += ' = 0'
							else
								result += ' = ' + vname.ivalue 
						}
						if (dec.getVar.name.size() > 1)
							if (index <= dec.getVar.name.size() )
								result += ', '
						index ++
					}
				}
				return result + ' ;'
			}						
		}
	}
	
	static def String genProctype(DeclareEvent e, Define d)
	'''
		proctype «e.event.elabel.label»() { /* for event «e.event.elabel.label» */
			«val en = e.event.elabel.label»
			«genBooleanVariable(en,0)»			
			do
				:: d_step {					
					«dispatchEventGuard(e.event)»
					«genCheckBooleanVariable(en,0)»
					/* action for event «en»*/
					«dispatchEventAction(e.event, d)»
					«genConclusionBooleanVariable(en, 0)»
				}
			od
		}		
	'''	
	

	static def genBooleanVariable(String event, int id) 
	'''		
		«IF Test.rules != null»
			«var index = 0»
			«FOR rule :Test.rules.r»			
				«IF (rule.ename.elabel != null)»
					«IF (rule.ename.elabel.label == event)»
						bool _b«event»«index++»_«id» ;
					«ENDIF»
				«ELSE»				
					«IF (rule.ename.scheventname.toString.equals(event))»
						bool _b«event»«index++»_«id» ;
					«ENDIF»
				«ENDIF»
			«ENDFOR»
		«ENDIF»
	'''
	
	static def genCheckBooleanVariable(String event, int id) 
	'''		
		«IF Test.rules != null»
			«var index = 0»
			«FOR r :Test.rules.r»		
				«IF (r.ename.elabel != null)»
					«IF (r.ename.elabel.label == event)»
						
						/* pre-condition for rule: «r.rulename» */
						_b«event»«index»_«id» = false ;			
						«IF r.premise != null»
							if 
								:: «Statements.dispatchExpression(r.premise.expr)» -> _b«event»«index++»_«id» = true
								:: else -> skip 
							fi ;
						«ENDIF»				
					«ENDIF»
				«ELSE»
					«IF (r.ename.scheventname != null)»
						«IF (r.ename.scheventname.toString.equals(event))»
									
							/* pre-condition for rule: «r.rulename» */
							_b«event»«index»_«id» = false ;			
							«IF r.premise != null»
								if 
									:: «Statements.dispatchExpression(r.premise.expr)» -> _b«event»«index++»_«id» = true
									:: else -> skip 
								fi ;
							«ENDIF»				
						«ENDIF»
					«ENDIF»
				«ENDIF»
			«ENDFOR»
		«ENDIF»
	'''
	
	static def genConclusionBooleanVariable(String event, int id) 
	'''		
		«IF Test.rules != null»
			«var index = 0»
			«FOR t :Test.rules.r»			
				«IF (t.ename.elabel != null)»
					«IF (t.ename.elabel.label == event)»
						
						/* pos-condition for rule: «t.rulename»*/
						if 
							:: _b«event»«index++»_«id» -> assert(«Statements.dispatchExpression(t.conclude.expr)»)
							:: else -> skip 
						fi ;				
					«ENDIF»
				«ELSE»
					«IF (t.ename.scheventname != null)»
						«IF (t.ename.scheventname.toString.equals(event))»
							
							/* pos-condition for rule: «t.rulename»*/
							if 
								:: _b«event»«index++»_«id» -> assert(«Statements.dispatchExpression(t.conclude.expr)»)
								:: else -> skip 
							fi ;				
						«ENDIF»
					«ENDIF»
				«ENDIF»
			«ENDFOR»
		«ENDIF»
	'''
	
	static def dispatchEventGuard(Event e)
	'''
		«IF e.guard != null»
			/* guard for event «e.elabel.label»*/
			«Statements.dispatchExpression(e.guard)» -> 
		«ENDIF»
	'''
	
	static def dispatchEventAction(Event e, Define d)
	'''
		«IF e.sequence != null»
			«FOR act : e.sequence.action»«dispatchAction(act)»«ENDFOR»
		«ELSE»
			«dispatchEventActionForFunction(e, d)»
		«ENDIF»
	'''
	
	
		
	static def initdefvalue(Define d) {
		defrefval.clear
		for (r: d.reflist) {
			val n = r.name
			var ArrayList<String> ref = new ArrayList<String>
			
			for (item : r.item)
				if (item.bvalue != null)
					ref.add(item.bvalue.value)
				else
					if (item.ivalue != null)
						ref.add(item.ivalue.value.toString)
					else
						ref.add(item.id)
						
			defrefval.put(n,ref)
		}
	}
	
	static def int getFunctionParameterValue(String para) {
		return 0
	}
	
	static def dispatchEventActionForFunction(Event e, Define d) {
		funcparaval.clear
		mapfuncparamval.clear
		if (e.eventfunction.funcpara != null) {
			for (item : e.eventfunction.funcpara.item)
				if (item.bvalue != null)
					funcparaval.add(item.bvalue.value)
				else
					if (item.ivalue != null)
						funcparaval.add(item.ivalue.value.toString)
					else
						funcparaval.add(item.id)
		}
				
		for (f: d.function) {
			if (f.name.equals(e.eventfunction.name)) {
				var int i = 0 
				for (par : f.funcpara.para) {
					mapfuncparamval.put(par,funcparaval.get(i++))
				}
					
				return dispatchEventFunction(f)
			}
		}
	}
	
	static def String dispatchEventFunction(Function f){
		var String result = ''	
		for(act : f.sequence.action)
			result += dispatchActionWithFunction(act) 
		return result
	}
	
	static def dispatchActionWithFunction(Action a)
	'''	
		«IF (a instanceof IFs) »
			if
				«FOR opt : a.option.sequence»
					:: «FOR act : opt.action»«dispatchActionWithFunction(act)»«ENDFOR»
				«ENDFOR»
				«IF a.option.elses != null»
					:: else -> «dispatchActionWithFunction(a.option.elses.sequence)»
				«ENDIF»
			fi ;
		«ELSE»
			«IF (a instanceof ASSIGN)»
				«Statements.dispatchStatement(a,'')»
			«ELSE»
				«IF (a instanceof GUARD)»
					«Statements.dispatchExpression(a.expr)» -> «dispatchActionWithFunction(a.sequence)»
				«ELSE»	
					«IF (a instanceof SKIP)»								
						skip ;
					«ENDIF»
				«ENDIF»
			«ENDIF»
		«ENDIF»
	'''
	
	static def dispatchAction(Action a)
	'''	
		«IF (a instanceof IFs) »
			if
				«FOR opt : a.option.sequence»
					:: «FOR act : opt.action»«dispatchAction(act)»«ENDFOR»
				«ENDFOR»
				«IF a.option.elses != null»
					:: else -> «dispatchAction(a.option.elses.sequence)»
				«ENDIF»
			fi ;
		«ELSE»
			«IF (a instanceof ASSIGN)»
				«Statements.dispatchStatement(a,'')»
			«ELSE»
				«IF (a instanceof GUARD)»
					«Statements.dispatchExpression(a.expr)» -> «dispatchAction(a.sequence)»
				«ELSE»	
					«IF (a instanceof SKIP)»								
						skip ;
					«ENDIF»
				«ENDIF»
			«ENDIF»
		«ENDIF»
	'''
	
	static def generateVerificationCase()
	'''		
		«var scenCount = 0»	
«««		«var configIndex = 0»
		«IF Test.permutation != null»
			«IF Test.configs != null»
				«FOR conf : Test.configs.config»	
					proctype VerificationCase«testCount++»() {/*Verification case extended permutation with configuration: «conf.name»*/						
						d_step {
							/*configuration : «conf.name»*/
							«genConfiguration(conf)»
							/*run process*/
							«genRunProcess()»
						}
					}	
	«««						«IF (Test.specification.decl_sch != null)»
	«««							/* set configuration for scheduler */
	«««							sch.config.«configIndex++» = 0 ;
	«««						«ENDIF»				
«««							«IF Test.specification.decl_sch != null»
	«««							/*run scheduler*/
	«««							run scheduler()
«««							«ENDIF»	
				«ENDFOR»
			«ELSE»
				proctype VerificationCase«testCount++»() {/*Test case extended permutation with no configuration*/
					d_step {					 						
						/*run process*/
						«genRunProcess()»		
					}
				}
			«ENDIF»
		«ENDIF»
		
«««		«var configId = 0»		
		«IF Test.configs != null»
			«FOR conf : Test.configs.config»
				«IF Test.scenarios != null»
					«IF Test.scenarios.flow != null»
						«FOR flow : Test.scenarios.flow»				
							proctype VerificationCase«testCount++»() {/*Verification case scenario «scenCount» («genFlowString(flow)») with configuration: «conf.name»*/								
	«««							«IF (Test.specification.decl_sch != null)»
	«««								/* set configuration for scheduler */
	«««								sch.config.«configId++» = 0 ;
	«««							«ENDIF»
								«var index = 0»						
								«FOR e : flow.event»
									«IF e.elabel != null»
										«val en = e.elabel.label»
										«genBooleanVariable(en,index)»
										«IF index == 0»
											/*configuration : «conf.name»*/
											d_step {
												«genConfiguration(conf)»
											}
										«ENDIF»
										d_step {											
											/*------- event: «en» ---------*/	
«««											«genBooleanVariable(en,index)»
											«FOR ev : Test.specification.event»
												«IF ev.event.elabel.label == e.elabel.label»																		
													«dispatchEventGuard(ev.event)»
													«genCheckBooleanVariable(en, index)»
													
													/* action for event «en»*/
													«dispatchEventAction(ev.event, Test.specification.define)»
													«genConclusionBooleanVariable(en, index++)»
												«ENDIF»
											«ENDFOR»								
											/*---- end of event: «en» -----*/
										} 										
									«ELSE»
										«IF e.scheventname != null»
											«val en = e.scheventname.toString»
											/*------- event: «en» ---------*/										
«««												«genBooleanVariable(en,index)»																
												«genCheckBooleanVariable(en, index)»
												
												/* insert event */
												sch.«en»()	;						
												
												«genConclusionBooleanVariable(en, index++)»
											/*---- end of event: «en» -----*/
											
										«ENDIF»
									«ENDIF»
								«ENDFOR»					
							}	
						«ENDFOR»
					«ENDIF»
				«ENDIF»
			«ENDFOR»
		«ELSE»
			«IF Test.scenarios != null»
				«IF Test.scenarios.flow != null»
					«FOR flow : Test.scenarios.flow»				
						proctype VerificationCase«testCount++»() {/*Verification case scenario «scenCount» («genFlowString(flow)») with no configuration*/
							«var index = 0»						
							«FOR e : flow.event»
								«IF e.elabel != null»
									«val en = e.elabel.label»
									«genBooleanVariable(en,index)»
									d_step {
										/*------- event: «en» ---------*/	
«««										«genBooleanVariable(en,index)»
										«FOR ev : Test.specification.event»
											«IF ev.event.elabel.label == e.elabel.label»																
												«dispatchEventGuard(ev.event)»
												«genCheckBooleanVariable(en, index)»
												
												/* action for event «en»*/
												«dispatchEventAction(ev.event,Test.specification.define)»
												«genConclusionBooleanVariable(en, index++)»
											}
											«ENDIF»
										«ENDFOR»								
										/*---- end of event: «en» -----*/
									} 									
								«ELSE»
									«IF e.scheventname != null»
										«val en = e.scheventname.toString»
										/*------- event: «en» ---------*/
«««											«genBooleanVariable(en,index)»
											«genCheckBooleanVariable(en, index)»
											
											/* insert event */
											sch.«en»()	;						
											
											«genConclusionBooleanVariable(en, index++)»
										/*---- end of event: «en» -----*/										
									«ENDIF»
								«ENDIF»
							«ENDFOR»					
						}	
					«ENDFOR»
				«ENDIF»
			«ENDIF»
		«ENDIF»
	'''
	
	static def genFlowString(Flow f) {
		var result = ''
		var index = 0
		for (e : f.event) {
			if (e.elabel != null)
				result += e.elabel.label
			else
				result += e.scheventname.toString
			if (index < f.event.size() - 1) {
			 	result += '->'
				index ++			
			}
		}
		return result	 	
	}
	
	
	
	static def genConfiguration(Config conf)
	'''
		printf("«conf.name»") ;
		«IF (conf.config.getVar != null)»
			«FOR va : conf.config.getVar»
				«IF (va.getVal.bool != null)»
					«IF (va.getVal.bool.value == 'true')»
						«va.varName» = 0 ;						
					«ELSE» 
						«va.varName» = 1 ;
					«ENDIF»
				«ELSE»
					«va.varName» = «va.getVal.num.value» ;
				«ENDIF»
			«ENDFOR»
		«ENDIF»		
	'''
	
	static def genRunProcess() 
	'''
		«FOR e : Test.specification.event»
			run «e.event.elabel.label»() ;
		«ENDFOR»
	'''
	
	static def genRunVerificationCase() 
	'''
		active proctype test() {
			if
				«FOR i : 0 ..<testCount»
					:: run VerificationCase«i»()
				«ENDFOR»
«««				«IF Test.specification.decl_sch != null»
«««					:: run scheduler()
«««				«ENDIF»
				:: else -> skip
			fi
		}
	''' 
}