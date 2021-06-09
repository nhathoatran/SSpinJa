package scheduling.generator

import java.util.ArrayList
import scheduling.dsl.InterfaceFunction
import scheduling.dsl.SchedulerDef
import java.util.HashSet


class Interface {
	static def genInterfaceFunction(SchedulerDef sch)
	'''
		//---------------interface function---------------------------------------
		«IF (sch.interface != null)»
			«FOR f: sch.interface.interfacefunction»		
				«genFunction(f)»
			«ENDFOR»
		«ENDIF»
		«genInterfaceFunctionCall(sch)»
	'''
	
	
	static def genFunction(InterfaceFunction i)
	'''		
		public void interface_«i.functionname.name» (String paraList) throws ValidationException {
			«var ArrayList<String> remainProcessList = new ArrayList<String>»
			«var HashSet<String> definedProcessSet = new HashSet<String>»			
			«IF i.parameterlist != null»
				String[] paras = paraList.split(",");
				if (paras.length != «i.parameterlist.para.length» + 1) {
					System.out.println("Can not call function «i.functionname.name»");
					return ;
				}																	
				// list the parameters «var pCount = 1» «remainProcessList.clear»				
				«FOR p : i.parameterlist.para»
					«IF p.type.toString.contains("int")»
						int «p.paraname» = Integer.parseInt(paras[«pCount++»].trim());
					«ELSE» 
						«IF p.type.toString.contains("bool")»
							boolean «p.paraname» = Boolean.parseBoolean(paras[«pCount++»].trim());
						«ELSE»
							«IF p.type.toString.contains("byte")»
								int «p.paraname» = Integer.parseInt(paras[«pCount++»].trim());
							«ELSE»
								«IF p.type.toString.contains("process")»
									//add «p.paraname» to remainProcessList -> «remainProcessList.add(p.paraname)» 
									//add «p.paraname» to definedProcessSet -> «definedProcessSet.add(p.paraname)»
									String s_«p.paraname» = paras[«pCount++»].trim();
									ArrayList<SchedulerProcess> a_«p.paraname» = findProcessByAlias(s_«p.paraname») ;									
								«ELSE»
									System.out.println("Error converting parameter");
									return ;
								«ENDIF» 
							«ENDIF»					
						«ENDIF»						
					«ENDIF»
				«ENDFOR»
			«ENDIF»
			
			//interface function work
			«IF i.statements != null»
«««				«var ArrayList<String> functionprocessList = new ArrayList<String>»//«functionprocessList.addAll(remainProcessList)»					
				«FOR st : i.statements»
					«var ArrayList<String> functionprocessList = new ArrayList<String>»//«functionprocessList.addAll(remainProcessList)»
					«var ArrayList<String> realRemainProcessList = new ArrayList<String>»//«realRemainProcessList.addAll(remainProcessList)»
					 //real remainProcessList: «realRemainProcessList», functionProcesslist: «functionprocessList»
					«Statements.genIfDef(st.ifdef)»
					«Statements.dispatchStatementwithProcessList(st.statement, realRemainProcessList, functionprocessList, definedProcessSet)»
					«Statements.genCloseIfDef(st.ifdef)» 
«««					«Statements.genStmwithProcessList(st, realRemainProcessList, functionprocessList)»
				«ENDFOR»
			«ENDIF»
			
			//check to select other process
			//if (running_process == null) {
			//	select_process(-1) ;
			//}
		}
	''' 

/*	
	static def String genProcessListHeader(Statements st, ArrayList<String> processList)
	'''
		«IF st instanceof MoveProcess»
			«IF processList.contains(st.process.name)»
				for («st.process.name» : a_«st.process.name» {
					«Statements.dispatchStatement(st)»
				}
			«ENDIF»
		«ENDIF»
		
		«IF st instanceof RemoveProcess»
			«IF processList.contains(st.process.name)»
				for («st.process.name» : a_«st.process.name» {
					«Statements.dispatchStatement(st)»
				}
			«ENDIF»
		«ENDIF»
		
		«IF st instanceof ChangeValue»
			«IF st instanceof ChangeValueUnOp»
				«IF processList.contains(st.getVar.name)»
					for («st.getVar.name» : a_«st.getVar.name» {						
						«Statements.dispatchStatement(st)»
					}
				«ENDIF»
			«ELSE»
				«IF st instanceof ChangeValueExpression»
					«IF processList.contains(st.getVar.name)»
						for («st.getVar.name» : a_«st.getVar.name» {						
							«Statements.dispatchStatement(st)»
						}
					«ENDIF»
				«ENDIF»
			«ENDIF»
		«ENDIF»
		
		«IF st instanceof IfStatement»
		«ENDIF»
		«IF st instanceof AssertStatement»
		«ENDIF»
		«IF st instanceof CallFunction»
		«ENDIF»
	'''
	
	static def String genProcessListHeaderForExpression(Expression ex, ArrayList<String> processList)
	'''
		«var ArrayList<String> pList = processList»
		«IF ex instanceof Or»
			«genProcessListHeaderForExpression(ex.left, pList)»
			«genProcessListHeaderForExpression(ex.right, pList)»
		«ENDIF»
		«IF ex instanceof And»
			«genProcessListHeaderForExpression(ex.left, pList)»
			«genProcessListHeaderForExpression(ex.right, pList)»
		«ENDIF»
		«IF ex instanceof Equality»
			«genProcessListHeaderForExpression(ex.left, pList)»
			«genProcessListHeaderForExpression(ex.right, pList)»
		«ENDIF»
		«IF ex instanceof Comparison»
			«genProcessListHeaderForExpression(ex.left, pList)»
			«genProcessListHeaderForExpression(ex.right, pList)»
		«ENDIF»	
		«IF ex instanceof Plus»
			«genProcessListHeaderForExpression(ex.left, pList)»
			«genProcessListHeaderForExpression(ex.right, pList)»
		«ENDIF»
		«IF ex instanceof Minus»
			«genProcessListHeaderForExpression(ex.left, pList)»
			«genProcessListHeaderForExpression(ex.right, pList)»
		«ENDIF»
		«IF ex instanceof MulOrDiv»
			«genProcessListHeaderForExpression(ex.left, pList)»
			«genProcessListHeaderForExpression(ex.right, pList)»
		«ENDIF»	
		«IF ex instanceof GetID»
			
		«ENDIF»
		«IF ex instanceof InExpression»
					
		«ENDIF»	
		«IF ex instanceof NullExpression»
							
		«ENDIF»
	'''
	
	static def String dispatchExpression(Expression exp){		 	
		if (exp instanceof Or) return "(" + dispatchExpression(exp.left) + (if(exp.right != null) " || " + dispatchExpression(exp.right)) + ")" 
		else if (exp instanceof And) return "(" + dispatchExpression(exp.left) + " && " + dispatchExpression(exp.right) + ")"
		else if (exp instanceof Equality) return "(" + dispatchExpression(exp.left) + exp.op + dispatchExpression(exp.right) + ")"
		else if (exp instanceof Comparison) return "(" + dispatchExpression(exp.left) + exp.op + dispatchExpression(exp.right) + ")"
		else if (exp instanceof Plus) return "(" + dispatchExpression(exp.left) + '+' + dispatchExpression(exp.right) + ")"
		else if (exp instanceof Minus) return "(" + dispatchExpression(exp.left) + '-' + dispatchExpression(exp.right) + ")"
		else if (exp instanceof MulOrDiv) return "(" + dispatchExpression(exp.left) + exp.op + dispatchExpression(exp.right) + ")"
		else if (exp instanceof Not) return "(!" + dispatchExpression(exp.expression) + ")"
		
		else if (exp instanceof GetID) return exp.proc.name + ".processID"
		else if (exp instanceof InExpression) return '('  + exp.col.name + '.hasProcess(' + exp.proc.toString + ') > 0)'
		else if (exp instanceof NullExpression) return '(' + schKeyword  + exp.proc.name + ' == null)'
		
		//else if (exp instanceof QualifiedNames) return  dispatchQualifiedNames(exp) //no meaningful
		else if (exp instanceof Atomic) {
			if (exp.getVar != null)
				if (schKeyword.empty)
					return  dispatchQualifiedNames(exp.getVar)				
			if (exp.node.tokenText.contains('.'))
				return (schKeyword + exp.node.tokenText)
			else return exp.node.tokenText			
		}	
	}
	
 */	
	
	
	static def String genInterfaceFunctionCall(SchedulerDef sch) 
	'''
		public boolean sch_api(String funcName, String paraList) throws ValidationException {
			«IF sch.parent != null»				
				if (super.sch_api(funcName, paraList))
					//already call super function
					return true ;
			«ENDIF»
			switch (funcName) {
				«IF (sch.interface != null)»
					«FOR f : sch.interface.interfacefunction»
						case "«f.functionname.name»" :						
							interface_«f.functionname.name»(paraList) ;							
							return true ;
					«ENDFOR»
				«ENDIF»
				default:
					System.out.println("Error calling Scheduler API function");
					return false ;
			}			
		}		
«««		private int runProcess(String paraList) {
«««			String[] paras = paraList.split(",");
«««			String processName = paras[0] ;
«««			ArrayList<String> para = new ArrayList<String>() ;
«««			
«««			for (int i = 1; i < paras.length; i++)
«««				para.add(paras[i]);
«««			PromelaProcess proc = null;
«««			int processID = -1;	
«««			switch (processName) {
«««				«FOR pt: Data.procModel.getProcess»						
«««					«IF pt.proctype != null»
«««						case "«pt.proctype.name»" :
«««							try {
«««								proc = new SchedulerPanModel.«pt.proctype.name»_0() ;  
«««								processID = panmodel.addProcess(proc);
«««								if (processID >=0) {
«««									executeProcess(proc, processID, para) ;
«««								} else {
«««									System.out.println("Error execute process «pt.proctype.name»");
«««								}
«««							} catch (Exception e) {
«««								e.printStackTrace() ;
«««							}
«««							break ;
«««					«ENDIF»
«««				«ENDFOR»
«««				default :
«««					System.out.println("Can not find process " + processName) ;
«««			}	
«««			return processID ;
«««		}
		public boolean sch_get(String processName, String property) {
			«IF sch.parent != null»
				if (super.sch_get(processName, property))
					return true ;
			«ENDIF»
			if (processName.trim().equals("scheduler")) {
				//scheduler data variable
				switch (property) {
					«var ArrayList<String> varlist = new ArrayList<String>()»
					«IF Data.schModel.scheduler.schedulerdata != null»
						«FOR d : Data.schModel.scheduler.schedulerdata.datadef»
							«IF d.datablockdef != null»
								«FOR data : d.datablockdef.datadef »
									«IF data.prop != null»								
										«FOR v : data.prop.name»
											«IF !varlist.contains(v.name)»
												case "«v.name»" :						
													SchedulerPromelaModel.api_execute_result = «generateCastingVariable(data.prop.type.toString())»(«v.name») ;
													return true ; //«varlist.add(v.name)» 
											«ENDIF»
										«ENDFOR»
									«ENDIF»								
								«ENDFOR»
							«ELSE»
								«IF d.datasingledef.prop != null»								
									«FOR v : d.datasingledef.prop.name»
										«IF !varlist.contains(v.name)»
											case "«v.name»" :						
												SchedulerPromelaModel.api_execute_result = «generateCastingVariable(d.datasingledef.prop.type.toString())»(«v.name») ;
												return true ; //«varlist.add(v.name)» 
										«ENDIF»
									«ENDFOR»
								«ENDIF»	
							«ENDIF»
						«ENDFOR»
					«ENDIF»								
					default:
						System.out.println("Error getting scheduler property");
				}
			} else {
				ArrayList<SchedulerProcess> aProcess = findProcess(processName) ;
				if (aProcess.size() == 1) {
					SchedulerProcess process = aProcess.get(0) ;
					switch (property) {
						«IF Data.procModel.processdata != null»
							«FOR prop : Data.procModel.processdata.properties»																
								«FOR pname : prop.name»
									case "«pname.name»" :						
										SchedulerPromelaModel.api_execute_result = «generateCastingVariable(Data.getType(pname.name))»(process.«pname.name») ;
										return true ;
								«ENDFOR»								
							«ENDFOR»
						«ENDIF»	
«««						«IF !Data.varPropertyList.empty»
«««							«FOR pName : Data.varPropertyList»
«««								case "«pName»" :						
«««									SchedulerPromelaModel.api_execute_result = «generateCastingVariable(Data.getType(pName))»(process.«pName») ;
«««									return true ; 111
«««							«ENDFOR»
«««						«ENDIF»
«««						«IF !Data.valPropertyList.empty»
«««							«FOR pName : Data.valPropertyList»						
«««								case "«pName»" :
«««									SchedulerPromelaModel.api_execute_result = «generateCastingVariable(Data.getType(pName))»(SchedulerObject.getStaticPropertyObject(process.refID).«pName») ;								
«««							«ENDFOR»
«««						«ENDIF»					
						default:
							System.out.println("Error getting process property");
					}
				} 
			}
			return false ;
		}
	'''
	
	def static String generateCastingVariable(String varType) {
		var String result = '' 
		
		switch (varType) {
			case 'int' :
				result = 'Integer.toString'				 
			case 'boolean' :
				result = 'Boolean.toString'
			case 'bool' :
				result = 'Boolean.toString'				
			case 'byte':
				result = 'Byte.toString'			 
			default :
				result = 'Integer.toString'
		}
		return result
	}
}