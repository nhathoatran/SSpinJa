package scheduling.generator

import java.util.HashMap
import java.util.HashSet
import scheduling.dsl.And
import scheduling.dsl.Atomic
import scheduling.dsl.BlockStatement
import scheduling.dsl.ChangeValue
import scheduling.dsl.ChangeValueExpression
import scheduling.dsl.ChangeValueUnOp
import scheduling.dsl.CheckPoint
import scheduling.dsl.Comparison
import scheduling.dsl.Equality
import scheduling.dsl.Expression
import scheduling.dsl.GetProcess
import scheduling.dsl.IfStatement
import scheduling.dsl.LoopProcess
import scheduling.dsl.Minus
import scheduling.dsl.Model
import scheduling.dsl.MulOrDiv
import scheduling.dsl.Not
import scheduling.dsl.Or
import scheduling.dsl.PeriodicProcess
import scheduling.dsl.Plus
import scheduling.dsl.ProcessDSL
import scheduling.dsl.QualifiedNames
import scheduling.dsl.RTCTL

import scheduling.dsl.Statement
import scheduling.dsl.TestDSL

import static extension org.eclipse.xtext.nodemodel.util.NodeModelUtils.*
import scheduling.dsl.Implies
import scheduling.dsl.ProcessConfiguration
import scheduling.dsl.EventStm
import scheduling.dsl.EventOpt
import scheduling.dsl.Model
import scheduling.dsl.SchedulerDSL
import scheduling.dsl.SchedulerDef

class Data {
	
	public static Model model = null
	public static ProcessConfiguration procConfig = null
	public static ProcessDSL procModel = null
	public static SchedulerDSL schModel = null	
	public static TestDSL testMolel = null 
	
	public static  HashMap<String, String> intProperties = new HashMap<String, String>() ;
	public static  HashMap<String, String> boolProperties = new HashMap<String, String>() ;
	public static  HashMap<String, String> byteProperties = new HashMap<String, String>() ;
	public static  HashMap<String, String> clockProperties = new HashMap<String, String>() ;
	public static  HashMap<String, String> periodicClockProperties = new HashMap<String, String>() ;
	public static  HashMap<String, String> periodicClockOffset = new HashMap<String, String>() ;
	
	public static  HashSet<String> varPropertyList = new HashSet<String>() ;
	public static  HashSet<String> valPropertyList = new HashSet<String>() ;
	public static  HashSet<String> defPropertyList = new HashSet<String>() ;
	
	public static  HashSet<String> fixedvarPropertyList = new HashSet<String>() ;
	public static  HashSet<String> fixedvalPropertyList = new HashSet<String>() ;
	
	public static  HashSet<String> clockPropertyList = new HashSet<String>() ;	
	public static  HashSet<String> collectionList = new HashSet<String>() ; //for getting the index of the collection to generate code	
	
	public static boolean  hasClockEventHandler = false ;
	public static boolean hasPeriodicProcess = false ;
		 
	public static boolean runTime = false
	public static boolean sporadicTime = false
	public static boolean initProcessList = false //for checking the initial processes at the first run
	
	public static HashSet<String> checkPointsList = new HashSet<String>() ;
	
	//-------------------------------------------------------------------------------------------------------------------------
	static def fixClockProperty(){
		for (p : clockPropertyList) {
			valPropertyList.remove(p)
			if (! varPropertyList.contains(p))
				varPropertyList.add(p) ;
		}	
	}
	//debug 
//	static def String valPropertyList() {
//		var String st = ''
//		if (! valPropertyList.isEmpty())
//			for (p : valPropertyList)
//				st = st + ' ' + p 
//		return st
//	}
//	static def String varPropertyList() {
//		var String st = ''
//		for (p : varPropertyList)
//				st = st + ' ' + p
//		return st
//	}

	static def getVarValPropertyInSchedulerModel(SchedulerDSL schModel){
		if (schModel == null)
			return
		var SchedulerDef sch = schModel.scheduler
		if (sch != null) {
			if (sch.handler != null) {
				if (sch.handler.event != null) {
					for (h : sch.handler.event) {
						if (h.event instanceof EventStm)
							for (sta : (h.event as EventStm).statements) {
								getVarValPropertyInStatement(sta.statement)
							}
						else 
							for (opt : (h.event as EventOpt).opt)
								for (sta : opt.eventstm.statements) {
									getVarValPropertyInStatement(sta.statement)
								}
					}
				}
			}					
		}
		if (schModel.order != null) {
			for (comp : schModel.order.compare){
				for (sta : comp.statements) {
					getVarValPropertyInStatement(sta)
				}					
			}				
		}
		if (schModel.verify != null) {
			if (schModel.verify.at != null)
				getStaticPropertyInExpression(schModel.verify.at.cond)				
			getStaticPropertyInRTCTLExpression(schModel.verify.formula)			
		}
//		if (schModel.gentemplate != null) {
//			for (sta : schModel.gentemplate.statements) {
//				getVarValPropertyInStatement(sta.statement)
//			}
//		}		
	}
	
	static def getStaticPropertyInRTCTLExpression(RTCTL rtctl_exp) {
		if (rtctl_exp.op.equals('or')) {
			getStaticPropertyInRTCTLExpression(rtctl_exp.f1)
			getStaticPropertyInRTCTLExpression(rtctl_exp.f2)	
		} else {
			if (rtctl_exp.op.equals('implies')) {
				getStaticPropertyInRTCTLExpression(rtctl_exp.f1)
				getStaticPropertyInRTCTLExpression(rtctl_exp.f2)	
			} else {
				if (rtctl_exp.exp != null)
					getStaticPropertyInExpression(rtctl_exp.exp)
				else {
					if (rtctl_exp.f != null)
						getStaticPropertyInRTCTLExpression(rtctl_exp.f)
					else {
						getStaticPropertyInRTCTLExpression(rtctl_exp.f1)
						getStaticPropertyInRTCTLExpression(rtctl_exp.f2)
					}					
				}				
			}
		}
	}
	static def getVarValPropertyInStatement(Statement sta){
		if (sta instanceof GetProcess){ //run action
			if (sta.change != null) {				
				for (st : sta.change.sta)
					if (st instanceof ChangeValueUnOp )
						getVarValPropertyInStatement(st)
					else
						if (st instanceof ChangeValueExpression)
							getVarValPropertyInStatement(st)
			}
			if (sta.time != null) {
				getStaticPropertyInExpression(sta.time)						
			}
		}
		
		if (sta instanceof IfStatement) {
			getStaticPropertyInExpression(sta.condition)
			getVarValPropertyInStatement(sta.thenBlock)
			if (sta.elseBlock != null)
				getVarValPropertyInStatement(sta.elseBlock)
		}
		if (sta instanceof BlockStatement) {
			for (st : sta.statements)
				getVarValPropertyInStatement(st)
		}
		if (sta instanceof ChangeValue) {
			if (sta instanceof ChangeValueUnOp) {
				var sn = getProcessNameInQualifiedNames(sta.getVar.node.tokenText)
				if (! sn.equals(''))
					Data.varPropertyList.add(sn)
			}
			if (sta instanceof ChangeValueExpression) {
				if (sta.getVar != null) { //qualified name
					if (sta.getVar.prop != null)
						Data.varPropertyList.add(sta.getVar.prop.name)
				}
				getStaticPropertyInExpression(sta.exp)
			}
		}
	}	
	
	static def getProcessNameInQualifiedNames(String qn) {
		var int pos = qn.indexOf('.')
		if (pos < 0) 
			return ''
		else
			return qn.substring(0,pos)
		
	}
	
	static def getStaticPropertyInExpression(Expression exp){
		if (exp instanceof Or) {
			getStaticPropertyInExpression(exp.left) 
			if(exp.right != null) getStaticPropertyInExpression(exp.right)
		}
		else if (exp instanceof And) {
			getStaticPropertyInExpression(exp.left)			
			getStaticPropertyInExpression(exp.right)			
		}
		else if (exp instanceof Equality) {
			getStaticPropertyInExpression(exp.left) 
			getStaticPropertyInExpression(exp.right)			
		}
		else if (exp instanceof Comparison) {
			getStaticPropertyInExpression(exp.left) 
			getStaticPropertyInExpression(exp.right)		
		}
		else if (exp instanceof Plus) {
			getStaticPropertyInExpression(exp.left)
			getStaticPropertyInExpression(exp.right)			
		}
		else if (exp instanceof Minus) {
			getStaticPropertyInExpression(exp.left)
			getStaticPropertyInExpression(exp.right)			
		}
		else if (exp instanceof MulOrDiv) { 
			getStaticPropertyInExpression(exp.left)
			getStaticPropertyInExpression(exp.right)			
		}
		else if (exp instanceof Implies) {
			getStaticPropertyInExpression(exp.left)
			getStaticPropertyInExpression(exp.right)			
		}
		else if (exp instanceof Not) {
			getStaticPropertyInExpression(exp.expression)			
		}		
		else if (exp instanceof QualifiedNames) { 
			getStaticPropertyInQualifiedNames(exp)
		}
		else if (exp instanceof Atomic) {
				if (exp.getVar != null)
				getStaticPropertyInQualifiedNames(exp.getVar)	
		}
	}
	
	static def getStaticPropertyInQualifiedNames(QualifiedNames qname) {
		if (qname.prop != null) {
			Data.valPropertyList.add(qname.prop.name)
//			System.out.println(qname.prop.name)
		}
	}
	static def removeVariableFromValueList(){
		for (p : varPropertyList)
			valPropertyList.remove(p)
		//consider all properties are var
		for (p : valPropertyList) {
			varPropertyList.add(p)			
		}
	}
	static def addfixedVariable() {
		for (p : fixedvalPropertyList) {
			varPropertyList.remove(p)
			valPropertyList.add(p)
//			System.out.println(p) 
		}
			
		for (p : fixedvarPropertyList){
			valPropertyList.remove(p)
			varPropertyList.add(p)			
		}
	}
	
	static def getCheckPointsList(SchedulerDSL schModel) {
		if (schModel == null)
			return
		var SchedulerDef sch = schModel.scheduler
		if (sch != null) {
			if (sch.handler != null) {
				if (sch.handler.event != null) {
					for (h : sch.handler.event) {
						if (h.event instanceof EventStm)
							for (sta : (h.event as EventStm).statements) {				
								getCheckPointList(sta.statement)
							}
						else
							for (opt : (h.event as EventOpt).opt)
								for (sta : opt.eventstm.statements) {				
									getCheckPointList(sta.statement)
								}
					}
				}
			}	
			if (sch.interface != null) {
				for (i : sch.interface.interfacefunction) {
					for (sta : i.statements) {
						getCheckPointList(sta.statement)
					}
				}
			}
				 			
		}
		
	}
	
	static def getCheckPointList(Statement sta) {
		if (sta instanceof CheckPoint) {							
			if (!checkPointsList.contains(sta.pointid.name)) {
				checkPointsList.add(sta.pointid.name)									
			}	
		} else {
			if (sta instanceof LoopProcess)
				getCheckPointList(sta.statement)
			else
				if (sta instanceof IfStatement) {
					getCheckPointList(sta.thenBlock)
					if (sta.elseBlock != null)
						getCheckPointList(sta.elseBlock)
				} else 
					if (sta instanceof BlockStatement)
						for (si : sta.statements)
							getCheckPointList(si)
		} 
		
	}
//	---------------------------------------------
	static def String getType(String pName) {
		if (intProperties.get(pName) != null) return "int"
		if (boolProperties.get(pName) != null) return "boolean"
		if (byteProperties.get(pName) != null) return "byte"
		if (clockProperties.get(pName) != null) return "int"
		if (periodicClockProperties.get(pName) != null) return "int"
		if (periodicClockOffset.get(pName) != null) return "int"
		return "int"
	}
	
	static def String getValue(String pName) {
		if (intProperties.get(pName) != null) return intProperties.get(pName)
		if (boolProperties.get(pName) != null) return boolProperties.get(pName)
		if (byteProperties.get(pName) != null) return byteProperties.get(pName)
		if (clockProperties.get(pName) != null) return clockProperties.get(pName)
		if (periodicClockProperties.get(pName) != null) return periodicClockProperties.get(pName)
		if (periodicClockOffset.get(pName) != null) return periodicClockOffset.get(pName)
		return ''
	}
	
	static def String getPeriodicClockVariable (PeriodicProcess periodic) {
		var clockName = periodic.element.process.name + '_'
		hasPeriodicProcess = true
		for (value : periodic.element.paraAssign) {
			if (value.num != null) {	
				clockName += value.num.value + '_'										
			} else {
				clockName += value.bool.value + '_'
			}							
		}
		clockName += '_' + periodic.period.value + '_' + periodic.offset.value
		
		return clockName 
	}
	
	static def getPeriodicClock(ProcessDSL procModel) {		
		if (procModel.processconfig != null) {
			for (proc : procModel.processconfig.procinit) {
				if (proc.periodic != null) {						
					periodicClockProperties.put(getPeriodicClockVariable(proc.periodic), proc.periodic.period.value.toString)
					periodicClockOffset.put(getPeriodicClockVariable(proc.periodic), proc.periodic.offset.value.toString)
				}				
			}
		}
	}
	
	static def getProcessPropertyListFromProcessModel(ProcessDSL procModel){		
		if (procModel.processdata != null) {				
			if (procModel.processdata.properties != null) {
				for (p : procModel.processdata.properties) {												
					if (p.isVal)
						for (pn : p.name) 
							fixedvalPropertyList.add(pn.name) ;
					
					if (p.isVar)
						for (pn : p.name) 
							fixedvarPropertyList.add(pn.name) ;
																
					switch (p.type.toString) {							
						case 'time',
						case 'int':								
							for (pn : p.name) {						
								varPropertyList.add(pn.name) //?			
								if (p.pvalue == null) 
									intProperties.put(pn.name, '')
								else										
									intProperties.put(pn.name, p.pvalue.node.tokenText) //num.value.toString)									
							}								 
						case 'bool': 							
							for (pn : p.name) {
								varPropertyList.add(pn.name) //?
								if (p.pvalue == null)
									boolProperties.put(pn.name, '')
								else										
									boolProperties.put(pn.name, p.pvalue.bool.value.toString)										
							}								
						case 'byte':	
							for (pn : p.name) {
								varPropertyList.add(pn.name) //?							
								if (p.pvalue == null)
									byteProperties.put(pn.name, '')
								else										
									byteProperties.put(pn.name, p.pvalue.num.value.toString)
							}	
						case 'clock':	
							for (pn : p.name) {
								varPropertyList.add(pn.name) //?
								if (p.pvalue == null)
									clockProperties.put(pn.name, '')
								else										
									clockProperties.put(pn.name, p.pvalue.num.value.toString)
								clockPropertyList.add(pn.name)		
							}								
						default : return 
					}
				}
			}			
		}
	}
	
	
	static def getCollectionList(SchedulerDef sch) {
		collectionList.clear		 
		if (sch.schedulerdata != null)
			for (schdata : sch.schedulerdata.datadef) {				
				if (schdata.datablockdef != null) { 
					for (data : schdata.datablockdef.datadef)
						if (data.col != null)
							collectionList.add(data.col.getName.name.trim)
				} else {
					if (schdata.datasingledef.col != null)
						collectionList.add(schdata.datasingledef.col.getName.name.trim)
				} 
			}
	}
	
	//this for optimize scheduler data	
	//this for optimize process properties
	static def initPropertyList() {
		intProperties.clear		
		boolProperties.clear
		byteProperties.clear		
		clockProperties.clear
		
		periodicClockProperties.clear
		periodicClockOffset.clear
		varPropertyList.clear
		valPropertyList.clear
		defPropertyList.clear
		fixedvarPropertyList.clear
		fixedvalPropertyList.clear
		clockPropertyList.clear
	}
	

	static def init(ProcessDSL procModel, SchedulerDSL schModel) {
		initPropertyList()
		
		getPeriodicClock(procModel)
		getProcessPropertyListFromProcessModel(procModel)
		getVarValPropertyInSchedulerModel(schModel)			
		removeVariableFromValueList()
		fixClockProperty()
		addfixedVariable()
		runTime = Statements.hasRunTime(schModel.scheduler)
		checkPointsList.clear() 
		//getCollectionList(schModel.scheduler)
		if (Data.procModel.processdata != null)
			for (proplist : Data.procModel.processdata.properties)
				for (pname : proplist.name)
					defPropertyList.add(pname.name)		
	}
}
		