package scheduling.generator

import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import scheduling.dsl.Constraint
import scheduling.dsl.Element
import scheduling.dsl.ProcessConfiguration
import scheduling.dsl.ProcessInit
import scheduling.dsl.Set

import static extension org.eclipse.xtext.nodemodel.util.NodeModelUtils.*
import scheduling.dsl.Constructor
import scheduling.dsl.ProcessBehaviors
import scheduling.dsl.Method
import scheduling.dsl.ParameterList
import scheduling.dsl.InterfaceParameterList
import scheduling.dsl.ProcessType


class ProcessConfigurationGenerator { 
	static var ArrayList<ArrayList<String>> configList = new ArrayList<ArrayList<String>>()
	static var ArrayList<String> elementList
	static val ScriptEngineManager mgr = new ScriptEngineManager()
   	static val ScriptEngine engine = mgr.getEngineByName("JavaScript")
   	static public var double seconds = 0 
   		
	static def evaluateExpression (String exp) {		
   		return engine.eval(exp)
	}
	
	static var int attcount
	static var ArrayList<String> attlist
	static var HashMap<Integer, HashSet<Integer>> attval //not accept boolean values
	static var HashSet<String> assignOk = new HashSet<String>
	static var HashSet<String> assignNotOk = new HashSet<String>
	static var int [] assign
	static var HashMap<String,String> defvalue = new HashMap<String,String>
	
	public static var ArrayList<String> assignConfigList = new ArrayList<String> //assignment for attributes
	
	
	static def initAttributeValue(ProcessConfiguration processconfiguration) { //? type
		var index = 0
		
		if (processconfiguration.attribute != null) {
			attcount = processconfiguration.attribute.att.size
			attval = new HashMap<Integer, HashSet<Integer>> 
			System.out.println('1. attribute count:' + attcount)
			for (att : processconfiguration.attribute.att) {				
				var HashSet<Integer> valuelist = new HashSet<Integer> ()				
				
				if (att.list != null) {
					for (l : att.list.list) {
						if (l.num != null) {
							valuelist.add(l.num.value)
						} else {
							if (l.range != null) {
								var int start = Integer.parseInt(l.range.start.node.tokenText)
								var int end = Integer.parseInt(l.range.end.node.tokenText) 
								for( i: start ..< end + 1) valuelist.add(i)
							}
						}
					}
				} else {
					if (att.^default.num != null) {
						valuelist.add(att.^default.num.value)						
					}
				}						
	
				attval.put(index++, valuelist)
				attlist.add(att.name)
				if (att.^default.num != null) {
					defvalue.put(att.name,att.^default.num.value.toString)
				} else {
					defvalue.put(att.name,att.^default.bool.value)
				}
				System.out.println('attribute ' + att.name + ' : ' + valuelist)				
			}
		} 
		assign = newIntArrayOfSize(attcount)
//		System.out.println('------------ init attribute finished')
	}

	static def ProcessModeltoJavaCode(ProcessConfiguration processconfiguration)
	'''
		«IF processconfiguration.defbehaviorproc.processbehaviors != null»
			«FOR proc : processconfiguration.defbehaviorproc.processbehaviors.processbehavior»
				«IF proc.constructor != null»
					proctype «proc.constructor.processname» () {
						«GenProcessBehavior(processconfiguration.defbehaviorproc.processbehaviors)»
					}
				«ENDIF»
			«ENDFOR»
		«ELSE»
			«FOR proctype :  processconfiguration.defbehaviorproc.proctype.proctype»									
				proctype «proctype.name» () {					
					«GenProcessBehavior(proctype)»					
				}
			«ENDFOR»
		«ENDIF»	
				
		init {
			«FOR set : processconfiguration.processinit.order»
				«FOR p : set.element»
					run «p.process.name»() ;
				«ENDFOR»
			«ENDFOR»	
		}
	'''
	
	static def ProcessConfigurationResumetoJavaCode(String name)
	'''
		* Generate process configuration & process model
		+ Configuration file: «name»
		Total configurations: «assignConfigList.size()» elapsed time «seconds» miliseconds
		--- Generate finished ---
	'''
	static def GenProcessBehavior(ProcessType proctype)
	'''
		do
			«FOR behave : proctype.processbehavior»
				«IF behave.method != null»
					«GenInstanceBehave(behave.method)»
				«ENDIF»
			«ENDFOR»
			:: skip ;
		od
	'''
		
	static def GenProcessBehavior(ProcessBehaviors behaviors)
	'''
		do
			«FOR behave : behaviors.processbehavior»
				«IF behave.method != null»
					«GenInstanceBehave(behave.method)»
				«ENDIF»
			«ENDFOR»
			:: skip ;
		od
	'''
	
	static def GenInstanceBehave(Method method)
	'''
		«IF method.parameterlist == null»
			:: sch_api_self(«method.functionname.name») ;
		«ELSE»
			«GenFunctionCall(method)»
			«FOR call : functioncall»
				:: sch_api_self(«method.functionname.name», «call») ;
			«ENDFOR»
		«ENDIF»
	'''
	
	static ArrayList<String> functioncall = new ArrayList<String>
	
	static def GenFunctionCall(Method method) {
		functioncall.clear		
		for (para : method.parameterlist.para) {
			for (ass : method.assignparameters) {
				if (para.paraname == ass.paraname) { 
					var ArrayList<String> valuelist = new ArrayList<String>

					for (l : ass.list.list) {						
						if (l.num != null) {
							valuelist.add(l.num.value.toString)
						} else {
							if (l.range != null) {
								var int start = Integer.parseInt(l.range.start.node.tokenText)
								var int end = Integer.parseInt(l.range.end.node.tokenText) 
								for( i: start ..< end + 1) valuelist.add(i.toString)
							} else {
								valuelist.add(l.node.tokenText)
							}
						}
					}
					var ArrayList<String> newlist = new ArrayList<String>
					if (functioncall.size == 0)
						functioncall = valuelist
					else {
						for (ele1 : functioncall) {
							for (ele2 : valuelist)
							newlist.add(ele1 + ',' + ele2)
						}
						functioncall.clear
						functioncall = newlist
					}
				}					
			}
		}
		var ArrayList<String> resultlist = new ArrayList<String>
		for (ass : functioncall)
			if (checkParamConstraint(method,ass))
				resultlist.add(ass)
		functioncall = resultlist
		System.out.println(method.functionname.name + ", Function call size: " + functioncall.size)
	}
	
	static def checkParamConstraint(Method method, String paraassign) {
		val String [] assign = paraassign.split(",")		
		if (method.constraints != null)  {			
			for (con : method.constraints.constraint) {
				if (! assignparavalueforconstraint(method.parameterlist, assign,con)) 
					return false					
			}	
		}		
		return true 
	}
	
	static def assignparavalueforconstraint(InterfaceParameterList paralist, String[] assign, Constraint con) {		
		var String curexp = Statements.dispatchExpression(con.condition.expr)
		var i = 0
		for (para : paralist.para) {			
			curexp = curexp.replace('('+para.paraname+')',assign.get(i++).toString)			
		}
		if (evaluateExpression(curexp).toString.equals('false')) return false
		else return true
	}
	
	static def genContructorParameter(ParameterList paralist){
		var result = ''
		var i = 0
		for (para : paralist.para) {
			result += para.type + ' '			
			val defval = defvalue.get(para.paraname.get(0).name.trim)
			
			var index = 0
			for (pname : para.paraname) {
				result += pname.name + '=' + defval
				index++
				if (index < para.paraname.size)
					 result += '; '				
			}
//			if (para.^val != null) {
//				if (para.^val.nondef != null)
//					result += '=' + defval 
//			}
			i++
			if (i < paralist.para.size)
				result += '; '							
		}
		return result
	}
	static def ProcessConfigurationtoJavaCode(ProcessConfiguration processconfiguration, String assignment, int index)
	'''
		def process «processconfiguration.name»_«index» «IF processconfiguration.parent != null»refines «processconfiguration.parent»«ENDIF» {			
			«IF processconfiguration.attribute != null»
				attribute {
					«FOR att : processconfiguration.attribute.att»
						«IF att.^default != null»
							«IF att.^default.num != null»
								var «att.type» «att.name» = «att.^default.num.value» ;  
							«ELSE»
								 «IF att.^default.bool != null»
								 	var «att.type» «att.name» = «att.^default.bool.value» ;
								 «ELSE»
								 	var «att.type» «att.name» ;
								 «ENDIF»
							«ENDIF»
						«ELSE» 
							var «att.type» «att.name» ;
						«ENDIF»			
					«ENDFOR»
				}
			«ENDIF»
			
			«IF processconfiguration.defbehaviorproc.processbehaviors != null»
				«FOR proc : processconfiguration.defbehaviorproc.processbehaviors.processbehavior»
					«IF proc.constructor != null»
						proctype «proc.constructor.processname» («IF proc.constructor.paralist != null»«genContructorParameter(proc.constructor.paralist)»«ENDIF») {
							«IF proc.constructor.paralist != null»
								«FOR paras : proc.constructor.paralist.para»
									«FOR pname : paras.paraname»
										this.«pname.name» = «pname.name» ;
									«ENDFOR»
								«ENDFOR»
							«ENDIF»
						}
					«ENDIF»
				«ENDFOR»
			«ELSE»
				«FOR proctype :  processconfiguration.defbehaviorproc.proctype.proctype»
					«FOR procbehave : proctype.processbehavior»
						«IF procbehave.constructor != null»
							proctype «procbehave.constructor.processname» («IF procbehave.constructor.paralist != null»«genContructorParameter(procbehave.constructor.paralist)»«ENDIF») {
								«FOR paras : procbehave.constructor.paralist.para»
									«FOR pname : paras.paraname»
										this.«pname.name» = «pname.name» ;
									«ENDFOR»
								«ENDFOR»
							}
						«ENDIF»
					«ENDFOR»
				«ENDFOR»
			«ENDIF»			
		}		
		«IF processconfiguration.processconfig != null»
			config {
				«FOR config : processconfiguration.processconfig.procinit»
					«config.node.tokenText»
				«ENDFOR»
			}
		«ENDIF»		
		«IF processconfiguration.processinit != null»
			init {
				[«assignment»]
			}						
		«ENDIF»
	'''
	
	static def genConfiguration(ProcessConfiguration processconfiguration) {
		//1

		assignOk.clear
		assignNotOk.clear
		assignConfigList.clear
		attlist = new ArrayList<String>
		val double startTime = System.currentTimeMillis() ;
		
		initAttributeValue(processconfiguration)
		if (attlist.size == 0) {
			var result = ''
			for (set : processconfiguration.processinit.order)
				if (result.length == 0)
					result = set.node.tokenText
				else
					result += ', ' + set.node.tokenText
			assignConfigList.add(result)
		} else {		
			genconfiguration(processconfiguration.processinit)		
			assignConfigList.clear
			if (configList.size > 0) 
				combineConfig(0, configList.size)//				
		}
		seconds = System.currentTimeMillis() - startTime ;		
//		seconds = (System.currentTimeMillis() - startTime) / 1e3
//		System.out.printf("spinja: gen configuration elapsed time %.2f seconds\n", seconds)		
//		return result
	}
	
	static def genProcessConfiguration(ProcessConfiguration processconfiguration, String config, int index) {
		return ProcessConfigurationtoJavaCode(processconfiguration, config, index) //config = assignment
	}
	
	static def combineConfig(int index, int setsize){
		if (index < setsize) {
			if (assignConfigList.size == 0)
				assignConfigList = configList.get(index)
			else {
				var ArrayList<String> newass = new ArrayList<String>
				for (pre : assignConfigList)
					for (pos : configList.get(index))
						newass.add(pre + ',' + pos)
				assignConfigList = newass
			}
			combineConfig(index + 1, setsize)
		}
	}
		
	static def genconfiguration(ProcessInit pinit) {	
		//2
		configList.clear
		var index = 0					
		for (set : pinit.order) {
			elementList = new ArrayList<String>	
//			System.out.println('Gen set: ' + (index++))				
			genSet(set) //-> set to 
			var ArrayList<String> newelementlist = new ArrayList<String>
			
			for (ele : elementList) {
				newelementlist.add('{' + ele + '}')
			}
			configList.add(newelementlist)
//			System.out.println('add set: ' + newelementlist)
		}		
	}
	
	
	static def genSet(Set set) {
		//3				
		for (element : set.element) {
			var process = findProcessConstructor(element)
			if (process != null) {
				var ArrayList<String> oldelementlist = new ArrayList<String>
				var ArrayList<String> newelementlist = new ArrayList<String>
				
				oldelementlist = elementList			
//				System.out.println('+ Old assign: ' + elementList)
//				System.out.println('+ Assign attribute for process: ' + process.processname)
				elementList = new ArrayList<String>
				assignProcessAttribute(process,element,0,'','') //for process (~element)
			
				if (oldelementlist.size == 0) {
					for (ele : elementList)
						newelementlist.add(ele)				
				} else {
					for (oldele : oldelementlist)
						for (newele : elementList)
							newelementlist.add(oldele + ',' +  newele )
				}
				elementList = newelementlist
//				System.out.println('new element list: ' + elementList)				
			}
		}
//		System.out.println('elementList size: ' + elementList.size)
	}
		
	static def getAttributeIndex(Constructor process, String attname) {		 
		var attindex = 0
		for (pr : process.paralist.para) {
			for (att : pr.paraname) {
//				System.out.println(att.name)
				if (att.name == attname) {
					return attindex					
				} else {
					attindex ++
				}	
			}			
		}
		return -1
	}
	
	static def assignProcessAttribute(Constructor process, Element element, int index, String sassign, String parastr) {
		//4
//		System.out.println(index + '  1 parastr: ' + parastr)
		val attname = attlist.get(index)
//		System.out.println('attribute :' + attname)	
					
		val processattindex = getAttributeIndex(process,attname)
		val defval = defvalue.get(attname)				
		var strassign = sassign
//		System.out.println('-- Assign attribute: ' + attname + ', index: ' + index + ', process index: ' + processattindex)
//		System.out.println('index/count: ' + index + '/' + (attcount-1))
		
		
		if (processattindex == -1) { //missing parameter (non assign attribute)
			//assign default value -> reedit grammar		
				
//			System.out.println('set default value for ' + attname + ' = 0')
			if (index == attcount - 1) {
//				System.out.println(strassign)
				checkAttributeConstraint(process,strassign, parastr)
			} else {
				if (index == 0) 
					strassign += defval
				else
					strassign += ',' + defval
//				System.out.println('2 parastr: ' + parastr)
				assign.set(index, Integer.parseInt(defval))
				assignProcessAttribute(process, element, index+1, strassign, parastr)
				strassign = sassign
			}
				
		} else {	
			if (element.paraAssign.size == 0) {
				elementList.add(process.processname + '()')
				return
			}	
			var para = element.paraAssign.get(processattindex) //of process definition
			
			if (para.nondef == null) {//value				
				if (index == 0) {
					strassign = para.node.tokenText
				} else {
					if (strassign.length > 0)
						strassign += ',' + para.node.tokenText
					else 
						strassign = para.node.tokenText
				}
				var nextpara = 	para.node.tokenText
				
				assign.set(index, para.num.value)
				
				if (para.num != null) {
					assign.set(index,para.num.value) //array of assignment
//					System.out.println('set value for ' + attname + ': ' + para.num.value)
				} else {
					//boolean value -> ?
				}	
				
//				System.out.println('Check: ' + parastr + ',' + nextpara)
				
				if (index == attcount - 1) {
					if (parastr.length > 0) {
						checkAttributeConstraint(process,strassign, parastr + ',' + nextpara)
							
					} else
						checkAttributeConstraint(process,strassign, nextpara)
				} else {
					if (parastr.length > 0) {
						assignProcessAttribute(process, element, index+1, strassign, parastr + ',' + nextpara)					
					} else {
						assignProcessAttribute(process, element, index+1, strassign, nextpara)
					}
					strassign = sassign
				}			
			} else { //?
//				System.out.println('Attval: ' + attval)		
				val oldassign = strassign		
				for (v : attval.get(index)) {
					if (index == 0)
						strassign = oldassign + v
					else
						strassign = oldassign + ',' + v
					assign.set(index, v)
					if (index == attcount - 1) {
						if (parastr.length > 0) {
//							System.out.println('Check: ' + parastr + ',' + v )						
							checkAttributeConstraint(process,strassign, parastr + ',' + v)
						} else
							checkAttributeConstraint(process,strassign, ''+ v)
					} else {
						if (parastr.length > 0)
							assignProcessAttribute(process, element, index+1, strassign, parastr + ',' + v)
						else
							assignProcessAttribute(process, element, index+1, strassign, '' + v)
						strassign = sassign
					}					
				}
			}
		}
	}
	
	static def checkAttributeConstraintForProcess(Constructor process, String strassign) {
		if (Data.procConfig.defbehaviorproc.proctype != null) {
			for (proctype : Data.procConfig.defbehaviorproc.proctype.proctype) {
				if (proctype.name.equals(process.processname)) {
					if (proctype.constraints != null) {
						for (con : proctype.constraints.constraint) {
							if (! assignvalueforconstraint(con,strassign)) {
								return false ;
							}						
						}
					}
					return true ;
				}
			}			
		}
		return true ;
	}
	static def checkAttributeConstraint(Constructor process, String strassign, String paraassign) {
		if (assignOk.contains(strassign) ) {
			if (checkAttributeConstraintForProcess(process,strassign)) {
				elementList.add(process.processname + '(' + paraassign + ')')
				return true
			}
		}
			 
		if (assignNotOk.contains(strassign)){
			return false
		}
		
		if (Data.procConfig.attribute != null)  {
			if (Data.procConfig.attribute.constraints != null)  {
				for (con : Data.procConfig.attribute.constraints.constraint) {
					if (! assignvalueforconstraint(con,strassign)) {	
						assignNotOk.add(strassign) 
						return false					
					}
				}
			}	
		}
		
		if (checkAttributeConstraintForProcess(process,strassign)) {
			elementList.add(process.processname + '(' + paraassign + ')')
		}
		//elementList.add(process.processname + '(' + paraassign + ')')		
		assignOk.add(strassign)
		return true 
	}
	
	
	static def assignvalueforconstraint(Constraint con,String strasign) {		
		var String curexp = Statements.dispatchExpression(con.condition.expr)
		for (i : 0..<attcount) {
			curexp = curexp.replace('('+attlist.get(i)+')',assign.get(i).toString)
		}			
//		System.out.println(curexp)		
		if (evaluateExpression(curexp).toString.equals('false')) return false
		else return true // exp + evaluateExpression(exp)
	}

	static def findProcessConstructor(Element e) {
//		System.out.println('Find: ' + e.process.name)
		if (Data.procConfig.defbehaviorproc.processbehaviors != null) {
			for (procbehave : Data.procConfig.defbehaviorproc.processbehaviors.processbehavior) {			
				if (procbehave.constructor != null){	
	//				System.out.println('Found:' + procbehave.constructor.processname)		
					if (procbehave.constructor.processname == e.process.name) {
						return procbehave.constructor					
					}
				}			
			}
		} else {
			for (proctype : Data.procConfig.defbehaviorproc.proctype.proctype) {
				for (procbehave : proctype.processbehavior) {
					if (procbehave.constructor != null){	
		//				System.out.println('Found:' + procbehave.constructor.processname)		
						if (procbehave.constructor.processname == e.process.name) {
							return procbehave.constructor					
						}
					}
				}
			}
		}
		return null
	}
}