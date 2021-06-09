package scheduling.generator

import scheduling.dsl.Element
import scheduling.dsl.ProcessDSL
import scheduling.dsl.SchedulerDSL
import scheduling.dsl.SchedulerDef
import java.util.ArrayList
import scheduling.dsl.RTCTL
import scheduling.dsl.Expression

import static extension org.eclipse.xtext.nodemodel.util.NodeModelUtils.*
import scheduling.dsl.NumValue
import scheduling.dsl.EventOpt

class SchedulerGenerator {
//	------------ RTCTL
	
	static def SchedulerStatetoJavaCode(SchedulerDSL schModel) 
	'''
		package sspinja.scheduling;
					
		import java.util.ArrayList;
		import spinja.model.Transition;
		import sspinja.GenerateCode;	
		import sspinja.scheduler.promela.model.SchedulerPromelaModel;	
					
		//Scheduler converter: Automatic generation
		«IF schModel.verify != null»		
			public class SchedulerState {
				public ArrayList<SchedulerState> father = new ArrayList<SchedulerState>();
				public ArrayList<Transition> apptrans = new ArrayList<Transition>();
				public ArrayList<GenerateCode> generatecodes = new ArrayList<GenerateCode>() ;
				public boolean ndpoint = SchedulerPromelaModel.scheduler.checkNDpoint();
				
				public int identifier = -1;
				public int prunid = -1;
				public int execid = -1;
				public int runinstance = 0;
				
				public boolean results[];
				public boolean checked[];
				public boolean duplicate = false ;
				
				public ArrayList<SchedulerState> next = new ArrayList<SchedulerState>();
				public ArrayList<Transition> trans = new ArrayList<Transition>();
				public ArrayList<Integer> core = new ArrayList<Integer>();
				public ArrayList<Integer> newID = new ArrayList<Integer>();
				public ArrayList<Integer> endID = new ArrayList<Integer>();
				//public ArrayList<Integer> pID = new ArrayList<Integer>();
								
				«FOR check : Data.checkPointsList»
					public boolean «check» = false ; //check point					
					public int «check»_chkmin = -2; //no occurence 
					public int «check»_chkmax = -2; //no occurence
				«ENDFOR»
				
				public GenerateCode getGenerateCode(int id) {
					int index = getChildStateIndex(id) ;
					if (index != -1)
						return generatecodes.get(index);
					else
						return null ;
				}
				private int getChildStateIndex(int identifier) {
					int index = -1 ;
					for (SchedulerState state : next) {			
						if (state.identifier == identifier) {
							index++; break ;
						}
					}
					return index;
				}
				public void update(int depth, boolean isCycle, boolean isCurrentState) {
					if (isCurrentState) { //for checkpoint
						«FOR check : Data.checkPointsList»
							if (SchedulerPanModel.scheduler.«check») { //check point
								this.«check» = true ; //change only for current state
								if (isCycle) {
									this.«check»_chkmax = -1; //loop
									SchedulerPanModel.scheduler.«check»_max = -1;
								} else {									
									if (this.«check»_chkmin > depth || this.«check»_chkmin == -2) //-2 : no occurence
										this.«check»_chkmin = depth ;
									if (this.«check»_chkmax < depth && this.«check»_chkmax != -1) //-1 : occurents infinitely
										this.«check»_chkmax = depth ;
								}
							}
						«ENDFOR»
					} else {
						«FOR check : Data.checkPointsList»
							if (this.«check») { //check point
								if (isCycle) {
									this.«check»_chkmax = -1; //loop
									SchedulerPanModel.scheduler.«check»_max = -1;
								} else {
									if (this.«check»_chkmin > depth)
										this.«check»_chkmin = depth ;
									if (this.«check»_chkmax < depth && this.«check»_chkmax != -1) //-1 : occurents infinitely
										this.«check»_chkmax = depth ;
								}
							}
						«ENDFOR»
					}
				}
					
				public void print(ArrayList<SchedulerState> schStateList, String pref, boolean fulltrans) {
					if (!schStateList.contains(this)) schStateList.add(this) ;
					System.out.println(pref + "[" + identifier «FOR i:0..(RTCTLformulaLength(schModel.verify.formula)-1)» + ", results[«i»]=" + results[«i»]«ENDFOR»
						«FOR check : Data.checkPointsList» + ", («check»,min,max)=(" + «check» + "," + «check»_chkmin + "," + «check»_chkmax + ")" «ENDFOR» + "]" );
					
					System.out.println(pref + "[" + identifier «FOR i:0..(RTCTLformulaLength(schModel.verify.formula)-1)» + ", checked[«i»]=" + checked[«i»]«ENDFOR»
						«FOR check : Data.checkPointsList» + ", («check»,min,max)=(" + «check» + "," + «check»_chkmin + "," + «check»_chkmax + ")" «ENDFOR» + "]" );
											
					for (SchedulerState child : next) System.out.print("," + child.identifier);
					System.out.println("");
					int index = 0 ;
					for (SchedulerState s : next) {
						if (!schStateList.contains(s)) {
							if (fulltrans) {
								s.print(schStateList, pref + trans.get(index++) + "-->", fulltrans);
							} else {
								s.print(schStateList, pref + "-->", fulltrans);
							}
						}
					}
				}					
				public void print(String pref) {					
					System.out.println(pref + "[" + identifier «FOR i:0..(RTCTLformulaLength(schModel.verify.formula)-1)» + ", results[«i»]=" + results[«i»]«ENDFOR»
						«FOR check : Data.checkPointsList» + ", («check»,min,max)=(" + «check» + "," + «check»_chkmin + "," + «check»_chkmax + ")" «ENDFOR» + "]" );
						
					int index = 0 ;		
					for (SchedulerState s : next) {						
						s.print(pref + trans.get(index++) + "-->");
					}
				}				
				public void print() {
					System.out.println("[" + identifier «FOR i:0..(RTCTLformulaLength(schModel.verify.formula)-1)» + ", results[«i»]=" + results[«i»]«ENDFOR»
											«FOR check : Data.checkPointsList» + ", («check»,min,max)=(" + «check» + "," + «check»_chkmin + "," + «check»_chkmax + ")" «ENDFOR» + "]" );
				}
				public String toString() {
					String s = "[" + identifier «FOR i:0..(RTCTLformulaLength(schModel.verify.formula)-1)» + ", results[«i»]=" + results[«i»]«ENDFOR»
																«FOR check : Data.checkPointsList» + ", («check»,min,max)=(" + «check» + "," + «check»_chkmin + "," + «check»_chkmax + ")" «ENDFOR» + "]" );
					return s;
				}
				public void printchildlist() {
					System.out.print("Child: ");
					for (SchedulerState child : next) {
						System.out.print(child.identifier + ", ");
					}
					System.out.println();
				}
			}
		«ELSE»
			//Automatic generation
			public class SchedulerState {
				public ArrayList<SchedulerState> father = new ArrayList<SchedulerState>();
				public ArrayList<Transition> apptrans = new ArrayList<Transition>();
				public ArrayList<GenerateCode> generatecodes = new ArrayList<GenerateCode>() ;
				public boolean ndpoint = SchedulerPromelaModel.scheduler.checkNDpoint();
				
				public int identifier = -1;
				public int prunid = -1;
				public int execid = -1;
				public int runinstance = 0;
				
				public boolean results[];
				public boolean checked[];
				public boolean duplicate = false ;
				
				public ArrayList<SchedulerState> next = new ArrayList<SchedulerState>();
				public ArrayList<Transition> trans = new ArrayList<Transition>();
				public ArrayList<Integer> newID = new ArrayList<Integer>();
				public ArrayList<Integer> endID = new ArrayList<Integer>();
				//public ArrayList<Integer> pID = new ArrayList<Integer>();
				
				public GenerateCode getGenerateCode(int id) {
					return null ;
				}
				public void update(int depth, boolean isCycle, boolean isCurrentState) {}	
				public void print(ArrayList<SchedulerState> schStateList, String pref, boolean fulltrans) {
					if (!schStateList.contains(this)) schStateList.add(this) ;
					System.out.println(pref + "[" + prunid+ "," + identifier + "]");
					for (SchedulerState child : next) System.out.print("," + child.identifier);
					System.out.println("");
					int index = 0 ;
					for (SchedulerState s : next) {
						if (!schStateList.contains(s)) {
							if (fulltrans) {
								s.print(schStateList, pref + trans.get(index++) + "-->", fulltrans);
							} else {
								s.print(schStateList, pref + "-->", fulltrans);
							}
						}
					}
				}				
				public void print(String pref) {
					System.out.println(pref + "[" + prunid+ "," + identifier + "]");
					int index = 0 ;		
					for (SchedulerState s : next) {						
						s.print(pref + trans.get(index++) + "-->");
					}
				}
				public void print() {}
			}
		«ENDIF»
	'''
	
	static def int RTCTLformulaLength(RTCTL formula) {	
		if (formula.op.equals('or'))
			return 1 + RTCTLformulaLength(formula.f1) + RTCTLformulaLength(formula.f2)
		else
			if (formula.op.equals('implies'))
				return 1 + RTCTLformulaLength(formula.f1) + RTCTLformulaLength(formula.f2)
			else
				if (formula.exp != null)
					return 1
				else 
					if (formula.getOp() == 'AX' || formula.op == 'AF' || formula.op == 'AG' ||
						formula.op == 'EX' || formula.op == 'EF' || formula.op == 'EG' || formula.op == 'not') 				
							return 1 + RTCTLformulaLength(formula.f)
					else //formula.op == 'AU' || formula.op == 'EU' 	
						return 1 + RTCTLformulaLength(formula.f1) + RTCTLformulaLength(formula.f2)
	}
	
	static def CTLFormulatoJavaCode(SchedulerDSL schModel) 
	'''
		«IF schModel.verify != null»
			package sspinja.scheduling;
			
			//Scheduler converter: Automatic generation			
			public class CTLFormula {
				«var verify = schModel.verify»
				«var formula = verify.formula»
				«var flength = RTCTLformulaLength(formula)»
				«IF verify.at != null»
					//-- When «Statements.dispatchExpression(verify.at.cond)» : «Statements.dispatchRTCTLExpression(formula)»
				«ELSE»
					//«Statements.dispatchRTCTLExpression(formula)»
				«ENDIF»
				
				public int length = «flength» ;
				public String opcode[] = new String[«flength»];
				public byte sf[][] = new byte[«flength»][2] ; //formula parameters
				public byte sn[] = new byte[«flength»]; //formula superscripts
				
				public CTLFormula() {
					«printFormulaConstructor(schModel.verify.formula, 0)»
				}
				
				public String toString() {
					return "«verify.node.tokenText.replace("\"","")»" ;
					«IF verify.at != null»						
						//return "When «Statements.dispatchExpression(verify.at.cond).replace("\"","")» : «Statements.dispatchRTCTLExpression(formula).replace("\"","")»" ;
					«ELSE»
						//return "«Statements.dispatchRTCTLExpression(formula).replace("\"","")»" ;
					«ENDIF»
				}
			}
		«ELSE»
			package sspinja.scheduling;						
			
			//Scheduler converter: Automatic generation			
			public class CTLFormula {
				public int length ;
				public String opcode[] ;
				public byte sf[][]; //formula parameters
				public byte sn[]; //formula superscripts
				public String toString() {
					return "" ;
				}
			}
		«ENDIF»
	'''
	
	static def printFormulaConstructor(RTCTL formula, int i) 
	'''
		//----------------------------	
		«IF (formula.op.equals('or'))»
			opcode[«i»] = "«formula.op»" ; //«Statements.dispatchRTCTLExpression(formula)»
			sn[«i»]= -1; //no superscript			
			sf[«i»][0] = «i + 1» ;
			sf[«i»][1] = «i + 1 +  RTCTLformulaLength(formula.f1)» ;				
			«printFormulaConstructor(formula.f1, i + 1)»				
			«printFormulaConstructor(formula.f2, i + 1 + RTCTLformulaLength(formula.f1))»
		«ELSE»
			«IF (formula.op.equals('implies'))»
				opcode[«i»] = "«formula.op»" ; //«Statements.dispatchRTCTLExpression(formula)»
				sn[«i»]= -1; //no superscript			
				sf[«i»][0] = «i + 1» ;
				sf[«i»][1] = «i + 1 +  RTCTLformulaLength(formula.f1)» ;				
				«printFormulaConstructor(formula.f1, i + 1)»				
				«printFormulaConstructor(formula.f2, i + 1 + RTCTLformulaLength(formula.f1))»
			«ELSE»
				«IF (formula.exp != null)»
					opcode[«i»] = "atomic" ; //«Statements.dispatchExpression(formula.exp)»
					sn[«i»]= -1; //no superscript
					sf[«i»][0] = «i» ;
				«ELSE»
					«IF (formula.op == 'AX' || formula.op == 'AF' || formula.op == 'AG' ||	
						formula.op == 'EX' || formula.op == 'EF' || formula.op == 'EG'	|| formula.op == 'not')»				
						opcode[«i»] = "«formula.op»" ; //«Statements.dispatchRTCTLExpression(formula)» 
						«IF formula.lte == null»
							sn[«i»]= -1; //no superscript
						«ELSE»
							sn[«i»]= «formula.lte.num»; //superscript
						«ENDIF»
						sf[«i»][0] = «i + 1» ;				
						«printFormulaConstructor(formula.f, i+1)»				
					«ENDIF»
					«IF (formula.op == 'AU' || formula.op == 'EU' || formula.op == 'or')»
						opcode[«i»] = "«formula.op»" ; //«Statements.dispatchRTCTLExpression(formula)»
						«IF formula.lte == null»
							sn[«i»]= -1; //no superscript
						«ELSE»
							sn[«i»]= «formula.lte.num»; //superscript
						«ENDIF»
						sf[«i»][0] = «i + 1» ;
						sf[«i»][1] = «i + 1 +  RTCTLformulaLength(formula.f1)» ;				
						«printFormulaConstructor(formula.f1, i + 1)»				
						«printFormulaConstructor(formula.f2, i + 1 + RTCTLformulaLength(formula.f1))»
					«ENDIF»			
				«ENDIF»
			«ENDIF»
		«ENDIF»		
	'''

	static def printFormulaAtomicCheckResult(RTCTL formula, int i)  //init_atomicf
	'''		
		«IF (formula.exp != null)»			
			result[«i»] = «Statements.dispatchExpression(formula.exp)»;
		«ELSE»
			«IF formula.f != null»
				«printFormulaAtomicCheckResult(formula.f, i + 1)»
			«ELSE»
				«printFormulaAtomicCheckResult(formula.f1, i + 1)»
				«printFormulaAtomicCheckResult(formula.f2, i + 1 + RTCTLformulaLength(formula.f1))»
			«ENDIF»					
		«ENDIF»		
	'''
	
	static def declareSysVar(RTCTL formula)
	'''	
		«var sysvar = new ArrayList<String>()»	
		«sysvar = Statements.getVariables(sysvar, formula)»
		«FOR v : sysvar»
			String «v» = SchedulerPanModel.panmodel.sysGet("«v»") ;
		«ENDFOR»
	'''
	static def declareSysVar(Expression expression)
	'''	
		«var sysvar = new ArrayList<String>()»	
		«sysvar = Statements.getVariables(sysvar, expression)»
		«FOR v : sysvar»
			String «v» = SchedulerPanModel.panmodel.sysGet("«v»") ;
		«ENDFOR»		
	'''
	
	static def printInitAtomicf(RTCTL formula, int i) //call by init_atomicf
	'''	
		«IF formula.exp != null»
			{//RTCTL: «Statements.dispatchRTCTLExpression(formula)» //Exp: «Statements.dispatchExpression(formula.exp,false)»
				«Statements.dispatchExpressionwithProcess(formula.exp, 0)»
«««				«var ArrayList<String> pNameList = Statements.getProcessListFromExpression(formula.exp)»					
«««				«FOR pName : pNameList»
«««					ArrayList<SchedulerProcess> a_«pName» = findProcessByAlias("«pName»") ;
«««					if (a_«pName».size() == 0) 
«««						temp = false ;
«««				«ENDFOR»
«««				«var printedString = "temp = temp && " + Statements.dispatchExpression(formula.exp, false) + ";"»
«««				«printLoop(pNameList, printedString)»
				results[«i»] = result_0 ; 
			}
		«ELSE»
			«IF formula.f != null»
				«printInitAtomicf(formula.f, i + 1)»
			«ELSE»
				«printInitAtomicf(formula.f1, i + 1)»
				«printInitAtomicf(formula.f2, i + 1 + RTCTLformulaLength(formula.f1))»
				«IF formula.op.equals("or") || formula.op.equals("implies")»					
«««					«IF formula.f1.exp != null»// f1: «formula.f1.exp»
«««						«printInitAtomicf(formula.f1, i + 1)»
«««					«ENDIF»
«««					«IF formula.f2.exp != null»// f2: «formula.f2.exp» 
«««						«printInitAtomicf(formula.f2, i + 1 + RTCTLformulaLength(formula.f1))»
«««					«ENDIF»
					«IF formula.f1.exp != null && formula.f2.exp != null && formula.op.equals("or")»
						results[«i»] = results[«i + 1»] || results[«i+2»] ;
					«ENDIF»
					«IF formula.f1.exp != null && formula.f2.exp != null && formula.op.equals("implies")»
						results[«i»] = !results[«i + 1»] || results[«i+2»] ;
					«ENDIF»
				«ELSE»
					«printInitAtomicf(formula.f1, i + 1)»
					«printInitAtomicf(formula.f2, i + 1 + RTCTLformulaLength(formula.f1))»
				«ENDIF»
			«ENDIF»
		«ENDIF»		
	'''
	
	static def printFormulaCheckedAtomic(RTCTL formula, int i) //init_sf
	'''
		«IF formula.exp != null»
			checked[«i»] = true ; //«Statements.dispatchExpression(formula.exp,false)» ;
		«ELSE»
			«IF formula.f != null»
				«printFormulaCheckedAtomic(formula.f, i + 1)»
			«ELSE»
				«printFormulaCheckedAtomic(formula.f1, i + 1)»
				«printFormulaCheckedAtomic(formula.f2, i + 1 + RTCTLformulaLength(formula.f1))»
				«IF formula.op.equals("or") || formula.op.equals("implies")»
					«IF formula.f1.exp != null && formula.f2.exp != null»
						checked[«i»] = true ; //«Statements.dispatchRTCTLExpression(formula.f1)» (or, implies) «Statements.dispatchRTCTLExpression(formula.f2)» ;
					«ENDIF»
				«ENDIF»
			«ENDIF»
		«ENDIF»
	'''
//	------------ RTCTL
		
	
	
	static def SchedulertoJavaCode(ProcessDSL procModel,SchedulerDSL schModel)
	'''
		package sspinja.scheduling ;
		//Automatic generation
		public class SchedulerObject extends SchedulerObject_«schModel.scheduler.name» { 
			«IF schModel.scheduler.gen != null»
				«IF schModel.scheduler.gen.stepgeneration != null»
					public boolean checkNDpoint() {
						«IF schModel.scheduler.gen.stepgeneration.step.ND_behavior != null»
							«IF schModel.scheduler.gen.stepgeneration.step.ND_behavior.cond != null»
								return «Statements.dispatchExpression(schModel.scheduler.gen.stepgeneration.step.ND_behavior.cond)» ;
							«ELSE»
								return true ;
							«ENDIF»
						«ELSE»
							return true ;
						«ENDIF»
					}
				«ELSE»
					«IF schModel.scheduler.gen.processgeneration != null»
						public boolean checkNDpoint() {
							«IF schModel.scheduler.gen.processgeneration.process.ND_behavior != null»
								«IF schModel.scheduler.gen.processgeneration.process.ND_behavior.cond != null»
									return «Statements.dispatchExpression(schModel.scheduler.gen.processgeneration.process.ND_behavior.cond)» ;
								«ELSE»
									return true ;
								«ENDIF»
							«ELSE»
								return true ;
							«ENDIF»
						}
					«ELSE»
						public boolean checkNDpoint() {
							return false ;
						}
					«ENDIF»
				«ENDIF»
			«ELSE»
				public boolean checkNDpoint() {
					return false ;
				}
			«ENDIF»
			
			public RunningSet getRunningSet(){
				return _runningSet;
			}
		}	
	'''
	
	static def SchedulerDSLtoJavaCode(ProcessDSL procModel,SchedulerDSL schModel) 
	'''
		«var refinement = schModel.scheduler.parent != null»
		«var SchedulerDef sch = schModel.scheduler»
		«SchedulerClassHeader()»
		«IF schModel.scheduler.gen != null»	
		import sspinja.Generate;
		«ENDIF»
		«Data.getCollectionList(sch)» 
«««		//for indexing the collection (returning process)
«««		«genStaticPropertyClass()» 
		//Automatic generation
		public class SchedulerObject_«schModel.scheduler.name» «IF refinement» extends SchedulerObject_«schModel.scheduler.parent»«ENDIF»{
			«IF !refinement»
				public static ArrayList<StaticProperty> staticPropertyList = new ArrayList<StaticProperty>() ;
				public static ArrayList<String> processList = new ArrayList<String>() ;		
				public static boolean [] processInScheduler = new boolean [128]; //processes managed by scheduler
				public static byte pcount = 0 ;
				public static ArrayList<Byte> pcnt = new ArrayList<Byte>(); //instance of process id
				public String _action = "";				
				public static ArrayList<String> initprocesslist = new ArrayList<String>();
				public static SchedulerPromelaModel panmodel ;
				public int _schselopt ;
				public int _schnumopt ;
				public int [][] _opt;
				
				private static int newP = -1, endP = -1;
				public static int getnewP(){
					int result = newP;
					newP = -1;
					return result;
				}
				public static int getendP(){
					int result = endP;
					endP = -1;
					return result;
				}
				
				«IF Data.schModel.defcore != null»
					public int num_core = «Data.schModel.defcore.numcore» ;
					public int current_core = -1 ;
					
					public int switchCore(int lastcore) throws ValidationException {
						if (num_core == 1) return -1 ;
						if (lastcore < 0) 
							lastcore = 0 ;
						else
							lastcore = (lastcore + 1) ; //next core
						if (lastcore == current_core) 
							lastcore = (lastcore + 1) ;
						if (lastcore == num_core) return -1 ; //fail
						System.out.print("Switch from core: " + current_core + " to: " + lastcore + ": ");						
						current_core = lastcore ;												
						running_process = running_procs[current_core];						
						_schselopt = 0 ;
						
						if (running_process == null) {
							select_process(-1) ;
						}
						return current_core ;
					}
					
					public int selCore(int lastcore) throws ValidationException {
						if (num_core == 1) return -1 ;
						if (lastcore < 0) 
							lastcore = 0 ;
						else
							lastcore = (lastcore + 1) ; //next core						
						if (lastcore == num_core) return -1 ; //fail
						System.out.print("Select core: " + lastcore);						
						current_core = lastcore ;												
						running_process = running_procs[current_core];						
						_schselopt = 0 ;						
						if (running_process == null) {
							select_process(-1) ;
						}
						return current_core ;
					}	
				«ELSE» 
					public int switchCore(int lastcore) throws ValidationException {return -1;}
					public int selCore(int lastcore) throws ValidationException {return -1;}
				«ENDIF»
			
				public static void setPcnt(ArrayList<Byte> pcount) {
					pcnt.clear();
					pcnt.addAll(pcount) ;
				}
				public static ArrayList<Byte> getPcnt() {
					return pcnt ;
				}
				public void setSchedulerSelOption(int sel){
					_schselopt = sel;
				}
				
				public int nextSchedulerOption(int lastschopt) {
					if (lastschopt == -1) {
						lastschopt = 0 ;
					}	
					if (lastschopt < _schnumopt - 1) {							
						_schselopt = lastschopt + 1 ;
					} else {
						_schselopt = 0 ;
						return -1; //no more scheduler option
					}
					return _schselopt;
				}				
				public int firstSchedulerOption() {
					_schselopt = 0 ;
					return 0;
				}				
				
«««				//Generate template
«««				«Generation.generateTemplateFunction(schModel.scheduler)»
				«IF schModel.scheduler.gen == null»
					public boolean hasGenTemplate = false ;
«««					public void setGenCode(GenerateCode _code) {}
«««					public boolean isGenCode(){return false;}
				«ELSE»					
					public boolean hasGenTemplate = true ;	//to separate the events
«««					public int templateID = 0 ; //default value for template 
«««					public boolean isGenCode(){return true;}
«««					public void setGenCode(GenerateCode _generatecode) {
«««						switch(templateID) {
«««							«FOR i : 0..<Generation.templateCnt»
«««								case «i» :
«««									setGenCode_«i»(_generatecode) ;
«««									break ;
«««							«ENDFOR»
«««						}
«««					}
«««					//start template from «Generation.templateCnt = 0»				
				«ENDIF»
				
				public void setAction(String act) {
					_action = act ;
				}
				public String getAction() {
					return _action ;
				}
				public int getRefID(String pName) {
					int i = 0 ;			
					for (StaticProperty sP : staticPropertyList) {
						if (sP.pName.equals(pName))
							return i ;
						i ++ ;
					}
					return -1 ;
				}				
				public static StaticProperty getStaticPropertyObject(int refID) {
					for (StaticProperty sP : staticPropertyList)
						if (sP.refID == refID)
							return sP ;
					return null ;
				}				
				public ArrayList<SchedulerProcess> findProcessByAlias(String alias) {			
					ArrayList<SchedulerProcess> result = new ArrayList<SchedulerProcess>();
						
					if (alias.trim().equals("running_process")) {
						if (running_process != null) {
							result.add(running_process);
						}
					} else {				
						int idx = 0 ;		
						for (String procN : processList) {
							if (procN.trim().equals(alias.trim())) {
								SchedulerProcess target_process = findProcessByID(idx) ;
								if (target_process != null) 
									result.add(target_process) ;
							}
							idx ++ ;
						}
						for (StaticProperty stP : staticPropertyList) {
							if (stP.pName.trim().equals(alias.trim())) {
								int refID = stP.refID ;
								ArrayList<SchedulerProcess> resultStP = new ArrayList<SchedulerProcess>();
								resultStP = findProcessByrefID(refID) ;
								if (!resultStP.isEmpty()) {
									for (SchedulerProcess p1 : resultStP) {
										boolean add = true ;
										for (SchedulerProcess p2 : result) {
											if (p1.processID == p2.processID) 
												add = false;
										}
										if (add) {
											result.add(p1) ;
										}
									}
								}
							}
						}		
					}
					return result ;
				}				
				public ArrayList<SchedulerProcess> findProcess(String pName) {
					return findProcessByAlias(pName) ;
				}				
				public int existsProcess (String pName) {
					ArrayList<SchedulerProcess> aP = findProcess(pName) ;
					return aP.size() ;
				}
				public int existsProcess (int pID) {
					SchedulerProcess p = findProcessByID(pID) ;
					if (p == null) return 0 ;
					else return 1 ;
				}				
				public void updateProcessInSchedulerList() {
					for (int i = 0; i < 128; i ++) {
						if (processInScheduler[i]) {
							processInScheduler[i] = (findProcessByID(i) != null);
						}
					}
						 
				}		
			«ENDIF»
			«Data.getCheckPointsList(schModel)»			
			«FOR check : Data.checkPointsList»
				public boolean «check» ; //check point
				public int «check»_min = -2, «check»_max = -2; //no occurs				
			«ENDFOR»
			
			«genContructor(procModel, schModel.scheduler)»
«««			//realize time -> then gen data structure for time

			//------------------------ event handler -------------------------
			«genHandler(procModel,schModel.scheduler)»
			«IF sch.parent == null»
				«Handler.genMissingHandler(sch,procModel)»
			«ENDIF»
«««			need to be put after handler to realize the time

			//--------------- encoding function -------------------------------
			«genEncodeDecode(sch)»			
			
			«genUtilityFunction(sch)»
			«genPrint(sch,procModel)»
			«genRunningFunction(sch)»
			«Interface.genInterfaceFunction(sch)»

			//------------------------ genStaticProcessProperty -------------------------
			«IF sch.parent == null»
				«genStaticProcessProperty(procModel)»
			«ELSE»
				public void genStaticProcessProperty(){	
					super.genStaticProcessProperty() ;
				}
			«ENDIF»	
			
			«genDataStruture(schModel.scheduler)»			
			«genUtilitiesFunctions(schModel.scheduler)»	
			«IF schModel.scheduler.parent == null || schModel.verify != null»		
				«genCTLVerify(schModel)»
			«ENDIF»
		}
	'''
	
	static def genCTLVerify(SchedulerDSL schModel)
	'''
		//--------------- verification structure ---------------------------
		«IF schModel.verify == null»
			public boolean schedulerCheck() {return false ;	}
			public void initSchedulerState(SchedulerState schState, int depth) {}
			public boolean stateCheck() {return false;}			
			public boolean collectState() {	return false ;}			
			public void printAnalysisResult(PrintWriter out) {
				if (out != null) out.println("No Analysis result");
				System.out.println("No Analysis result") ;
			}
		«ELSE»
			public boolean schedulerCheck() {
				return true ;
			}
						
			«var verify = schModel.verify»
			«var formula = verify.formula»
			   
			public void initSchedulerState(SchedulerState schState, int depth) {
				//«Statements.dispatchRTCTLExpression(formula)»
				boolean checked[] = new boolean[«RTCTLformulaLength(formula)»];
				«printFormulaCheckedAtomic(formula,0)»
				
				schState.checked = checked ;
				
				boolean results[] = new boolean[«RTCTLformulaLength(formula)»];
				//SysVar: «declareSysVar(formula)» 
				«printInitAtomicf(formula,0)»				
				schState.results = results ;
				
				//check points for init scheduler state
				«FOR checkpoint : Data.checkPointsList»
					if (this.«checkpoint») {
						schState.«checkpoint» = this.«checkpoint» ;
						schState.«checkpoint»_chkmin = depth ;
						schState.«checkpoint»_chkmax = depth ;					
						this.«checkpoint» = false ; //to check other points
						if («checkpoint»_min > depth || «checkpoint»_min == -2) //-2 : no occurence
							«checkpoint»_min = depth ;
						if («checkpoint»_max < depth && «checkpoint»_max != -1 ) //-1 : occurents infinitely
							«checkpoint»_max = depth ;
					}
				«ENDFOR»
			}
			
			public boolean stateCheck() {
				«IF verify.at != null»
					boolean result = true ;
					//«declareSysVar(verify.at.cond)»
					«var ArrayList<String> pNameList = Statements.getProcessListFromExpression(verify.at.cond)»					
					«FOR pName : pNameList»
						ArrayList<SchedulerProcess> a_«pName» = findProcessByAlias("«pName»") ;
						if (a_«pName».size() == 0) 
							result = false ;
					«ENDFOR»						
					
					«var printedString = "result = result && " + Statements.dispatchExpression(verify.at.cond, false) + " ;"»
					«printLoop(pNameList, printedString)»					
				«ELSE»
					boolean result = false ;			
				«ENDIF»
				return result ;
			}
			
			public boolean collectState() {
				«IF verify.at != null»
					return true ;
				«ELSE»
					return false ;
				«ENDIF»
			}
			
			public void printAnalysisResult(PrintWriter out) {
				«IF Data.checkPointsList.size > 0»
					System.out.println("Analysis result: ") ;
					out.println("Analysis result: ") ;
					«FOR check : Data.checkPointsList»
						System.out.println("«check» (min,max)=(" + «check»_min + "," + «check»_max + ")");
						out.println("«check» (min,max)=(" + «check»_min + "," + «check»_max + ")");
					«ENDFOR»
					System.out.println("-1: Occurs infinitely") ;
					out.println("-1: Occurs infinitely") ;
					System.out.println("-2: No occurence") ;
					out.println("-2: No occurence") ;
					System.out.println(" n: Occurs at time n") ;
					out.println(" n: Occurs at time n") ;
				«ENDIF»
			}
		«ENDIF»
	'''
	static def printLoop(ArrayList<String> pNameList, String printedString)
	'''
		«IF pNameList.size > 0»
			«var pName = pNameList.remove(0)»
			for (SchedulerProcess «pName» : a_«pName») {
				«printLoop(pNameList, printedString)»
			}
		«ELSE»
			«printedString»
		«ENDIF»
	'''
	
	//----------------------------------------------------------------------------------------------
	
	
	static def genStaticProcessProperty(ProcessDSL procModel) 
	'''	
		public void genStaticProcessProperty(){	
			«var refID = 0»
			StaticProperty sP ;						
			«FOR pt: procModel.getProcess»
				«IF pt.proctype != null»
					//«refID»
					sP = new StaticProperty() ;
					sP.refID = «refID ++» ;
					sP.pName = "«pt.proctype.name»" ;
					«FOR properass: pt.propertyassignment»
						«IF Data.valPropertyList.contains(properass.propers.name)»
							«IF ! Data.varPropertyList.contains(properass.propers.name)»								 
								«IF properass.propers != null» 
									«IF properass.pvalue != null» 
										«IF properass.pvalue.num != null» 
											sP.«properass.propers.name» = «properass.pvalue.num.value» ;
										«ELSE»
											sP.«properass.propers.name» = «properass.pvalue.bool.value» ;
										«ENDIF»
									«ELSE»										
										sP.«properass.propers.name» = «Utilities.findValueInParameterList(properass.propers.name, pt.paralist.para)» ;
									«ENDIF»								
								«ENDIF»
							«ENDIF»
						«ENDIF»
					«ENDFOR»	
					staticPropertyList.add(sP) ;
				«ENDIF»					
			«ENDFOR»
		}
	'''
		
	static def String genPrint(SchedulerDef sch, ProcessDSL procModel) 
	'''
		public String getSchedulerName() {
			return "«sch.name»" ;
		}
		
		public String getProcessModelName() {
			return "«procModel.name»" ;
		}
		
		public String getSchedulingPolicy() {
			return "+ Scheduler: " + getSchedulerName() + " + Processes: " + getProcessModelName();
		}		
		
		public void print_all(){	
			Util.println("+ Scheduler: " + getSchedulerName());
			//Util.println("- SCH OPT: " + _schselopt + "/" + (_schnumopt - 1));
			«IF (Data.runTime)»
				«IF Data.schModel.defcore != null»		
					if (current_core >= 0)	{		 				
						System.out.println("- CURRENT CORE: " + current_core + " - Time_count/Time_slice: " + _time_count[current_core] + "/" + _time_slice[current_core]) ;					
						running_process = running_procs[current_core];
						_runningSet = _runningSets[current_core];
					}
					for (int i=0; i < num_core; i++) {
						System.out.println("  + Core " + i) ; 
						if (running_procs[i] == null) 
							System.out.println("\t- Running process: Null") ;
						else {	
							System.out.print("\t- Running process: ") ;				
							running_procs[i].print();
						}							
						if (_runningSets[i] == null) 
							System.out.println("\t- Running set: Empty") ;
						else {					
							System.out.print("\t- Running set: ") ;
							_runningSets[i].print() ;
						}
					}					
				«ELSE»
					Util.println("- Time_count/Time_slice: " + _time_count + "/" + _time_slice) ;
				«ENDIF»
			«ENDIF»			
			«FOR pClock : Data.periodicClockProperties.keySet()»
				Util.println("Periodic Clock «pClock»: " + «pClock») ;
				Util.println("- Periodic Clock Offset «pClock»_offset: " + «pClock»_offset) ;								
			«ENDFOR»
			«IF sch.parent != null»
				super.print_all() ;
			«ELSE»
				«IF Data.schModel.defcore != null»		
					if (current_core >= 0)	{		 				
						System.out.println("- CURRENT CORE: " + current_core) ;					
						running_process = running_procs[current_core];
						_runningSet = _runningSets[current_core];
					}
					for (int i=0; i < num_core; i++) {
						System.out.println("  + Core " + i) ; 
						if (running_procs[i] == null) 
							System.out.println("\t- Running process: Null") ;
						else {	
							System.out.print("\t- Running process: ") ;				
							running_procs[i].print();
						}							
						if (_runningSets[i] == null) 
							System.out.println("\t- Running set: Empty") ;
						else {					
							System.out.print("\t- Running set: ") ;
							_runningSets[i].print() ;
						}
					}					
				«ELSE»
					Util.println("- Running process: " ) ; 
					if (running_process == null) 
						Util.println("Null") ;
					else {					
						running_process.print();
					}
					Util.println("- Running set: " ) ;	
					if (_runningSet == null) 
						Util.println("Null") ;
					else {					
						_runningSet.print() ;
					}	
					//System.out.println(processList);
					//printProcessInScheduler() ;	
				«ENDIF»				
			«ENDIF»
			«IF sch.getSvar != null»
				«IF sch.getSvar.getVard != null»
					«FOR vs : sch.getSvar.getVard»
						«IF vs.varblockdef != null»
							«FOR vas : vs.varblockdef.vardef»
								«IF vas.type.toString.trim().operator_equals('clock')»
									«FOR va : vas.getName»
										System.out.print("Clock «va.name»: " + «va.name») ;
									«ENDFOR»
								«ENDIF»
							«ENDFOR»
						«ELSE»
							«IF vs.varsingledef.vardef.type.toString.trim().operator_equals('clock')»
								«FOR va : vs.varsingledef.vardef.getName»
									System.out.print("Clock «va.name»: " + «va.name») ;
								«ENDFOR»
							«ENDIF»
						«ENDIF»					
					«ENDFOR»
				«ENDIF»
			«ENDIF»
«««			«IF sch.schedulerdata != null»
«««				«var ArrayList<String> collectionlist = new ArrayList<String>()»
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.getProp == null»
«««						«IF ! collectionlist.contains(schdata.col.getName().name) && schdata.col.parent == null»
«««							//add collection «schdata.col.getName().name»: «collectionlist.add(schdata.col.getName().name)»	
«««						«ENDIF»
«««					«ENDIF»
«««				«ENDFOR»
				«FOR col : Data.collectionList»
					System.out.print("- Collection: «col» : ");
					«col».print() ;
				«ENDFOR»
«««			«ENDIF»			
		}
		
		public String toString() {
			StringBuilder sb = new StringBuilder();
			
			sb.append("\n\n") ;
			sb.append("+ Scheduler: " + getSchedulerName() + " + Processes: " + getProcessModelName());
			sb.append("\n") ;
			
			«IF sch.parent != null»
				sb.append(super.toString()) ;
			«ELSE»
							
				sb.append("- Running process: " ) ; 
				if (running_process == null) 
					sb.append("Null!") ;
				else {					
					sb.append(running_process.toString());
				}
				sb.append("\n") ;
				
				sb.append("- Running set: " ) ;	
				if (_runningSet == null) 
					sb.append("Null!");
				else {					
					sb.append(_runningSet.toString()) ;
					sb.append("\n") ;
				}					
			«ENDIF»
			
			«IF sch.schedulerdata != null»
				«FOR col : Data.collectionList»
					sb.append("- Collection «col» : "); 					
					sb.append(«col».toString()) ;
«««					sb.append("\n") ;
				«ENDFOR»
			«ENDIF»	
			
			return sb.toString();
		}
	'''
	
	static def SchedulerClassHeader()
	'''
		package sspinja.scheduling ;
		
		import java.io.PrintWriter;		
		import java.util.ArrayList;
		import java.util.HashMap;
		import java.util.Iterator;
		import spinja.util.DataReader;
		import spinja.util.DataWriter;
		import spinja.util.StringUtil;
		import spinja.util.Util;		
		import spinja.util.Log;
		import spinja.exceptions.*;
		import spinja.promela.model.PromelaProcess;		
		import sspinja.scheduler.search.SchedulerSearchAlgorithm;
		import spinja.util.ByteArrayStorage;
				
		import sspinja.scheduler.promela.model.SchedulerPromelaModel;
		import sspinja.SchedulerPanModel;
		import sspinja.SchedulerState;
		
	'''
	
	static def String genRunningFunction(SchedulerDef sch)
	'''	
		//--------------- running function -------------------------------
		«IF sch.parent == null»
			public void executeProcess(PromelaProcess proc, int processID, ArrayList<String> para) throws ValidationException {
				SchedulerProcess p = new SchedulerProcess() ;
				//p.processID = (byte) processID ;
				p.processID = processID ;
				newP = processID ;
				
				//initialize the process
				p.initProcess(proc.getName(), para);
				//Util.println("New process " + p) ;
				processInScheduler[processID] = true ;
				while (pcnt.size() < processID + 1) pcnt.add((byte) 0) ;		
					pcnt.set(processID, (byte) (pcnt.get(processID) + 1) ) ;
				config_new_process(p) ;		
			}				
			public int select_process_to_run(int lastProcessID) { 
				if (_runningSet.isEmpty() == 1)
					return -1 ;
					
				SchedulerProcess temp = _runningSet.getFirstProcess(lastProcessID) ; //selectProcessToRun();
				if (temp != null) {
					running_process = temp ; //selectProcessToRun() == null ? _running ; //= _runningSet.getNextProcess(running_process) ;
					«IF Data.schModel.defcore != null»
						running_procs[current_core] = running_process;
					«ENDIF»
					return running_process.processID ;
				}
				else return - 1;
			}
		«ENDIF»		
		public boolean replace_running_process(byte collectionIndex, SchedulerProcess running_process, SchedulerProcess previous_running) {
			«IF sch.parent != null»
				if (super.replace_running_process(collectionIndex, running_process, previous_running)) 
					return true ;
				byte numCol = (byte) super.getNumberProcessCollection() ;
			«ELSE»
				byte numCol = 0 ;
			«ENDIF»
			«IF sch.schedulerdata != null»
				«IF sch.schedulerdata.datadef != null»
					«var idx = 0»
					«FOR data : sch.schedulerdata.datadef»
						«IF data.datablockdef != null»
							«FOR d : data.datablockdef.datadef»
								«IF d.col != null»
									«IF d.col.parent == null»
										if (collectionIndex == (byte) («idx++» + numCol)) {
											if («d.col.name.name» != null)
												«d.col.name.name».replace(running_process, previous_running) ;								
											return true ;
										}
									«ENDIF»
								«ENDIF»
							«ENDFOR»
						«ELSE»
							«IF data.datasingledef.col != null»
								«IF data.datasingledef.col.parent == null»
									if (collectionIndex == (byte) («idx++» + numCol)) {
										if («data.datasingledef.col.name.name» != null)
											«data.datasingledef.col.name.name».replace(running_process, previous_running) ;								
										return true ;
									}
								«ENDIF»
							«ENDIF»
						«ENDIF»
					«ENDFOR»				
				«ENDIF»		
			«ENDIF»	
			return false ;			
		}	
		public void put_running_process(byte collectionIndex) {
			if (running_process != null) {
				put_process(running_process, collectionIndex) ;				
			}
		}		
		public boolean put_process(SchedulerProcess proc, byte collectionIndex) {	
			«IF sch.parent != null»
				if (super.put_process(proc, collectionIndex)) 
					return true ;
				byte numCol = (byte) super.getNumberProcessCollection() ;
			«ELSE»
				byte numCol = 0 ;
			«ENDIF»			
			«IF sch.schedulerdata != null»
				«IF sch.schedulerdata.datadef != null»
					«var idx = 0»
					«FOR data : sch.schedulerdata.datadef»
						«IF data.datablockdef != null»
							«FOR d : data.datablockdef.datadef»
								«IF d.col != null»
									«IF d.col.parent == null»
										if (collectionIndex == (byte) («idx++» + numCol)) {
											if («d.col.name.name» != null)
												«d.col.name.name».put(proc);
												//if (proc == running_process)
												//	running_process = null ;
											return true ;
										}
									«ENDIF»
								«ENDIF»
							«ENDFOR»
						«ELSE»
							«IF data.datasingledef.col != null»
								«IF data.datasingledef.col.parent == null»
									if (collectionIndex == (byte) («idx++» + numCol)) {
										if («data.datasingledef.col.name.name» != null)
											«data.datasingledef.col.name.name».put(proc);
											//if (proc == running_process)
											//	running_process = null ;
										return true ;
									}
								«ENDIF»
							«ENDIF»
						«ENDIF»
					«ENDFOR»
				«ENDIF»
			«ENDIF»			
			return false ;
		}		
		//--------------- timed function -------------------------------		
		public void time_out(){	
			«IF Data.runTime»
				«IF Data.schModel.defcore != null»
					//just only add time
					add_time(_time_slice[current_core] - _time_count[current_core]) ;
					_time_count[current_core] = _time_slice[current_core] ;
				«ELSE»
					//just only add time
					add_time(_time_slice - _time_count) ;
					_time_count = _time_slice ;
					//check_running_time_to_put_running_process() ;
				«ENDIF»
			«ELSE»
				//do nothing
			«ENDIF»
		}		
		public void inc_time() {
			«IF Data.runTime»
				«IF Data.schModel.defcore != null»
					if (_time_slice[current_core] != 0) { 
						if (_time_count[current_core] == _time_slice[current_core])
							_time_count[current_core] = 1 ;
						else
							_time_count[current_core] ++ ;
					}
				«ELSE»
					if (_time_slice != 0) { 
						if (_time_count == _time_slice)
							_time_count = 1 ;
						else
							_time_count ++ ;
					}
				«ENDIF»
			«ELSE»
				//no specific time to run -> do nothing
			«ENDIF»
			add_time(1) ;
		}		
		public void dec_time() {			
			«IF Data.runTime»
				«IF Data.schModel.defcore != null»
					if (_time_slice[current_core] != 0) { 
						if (_time_count[current_core] == 0)
							_time_count[current_core] = _time_slice[current_core] - 1 ;
						else
							_time_count[current_core] -- ;
					}
				«ELSE»
					if (_time_slice != 0) {
						if (_time_count == 0)
							_time_count = _time_slice - 1 ;
						else
							_time_count -- ;
					}
				«ENDIF»
			«ELSE»
				//no specific time to run -> do nothing
			«ENDIF»
			sub_time(1) ;
		}		
		public void add_time(int time) {
			«IF sch.getSvar != null»				
				«IF sch.getSvar.getVard != null»
					«FOR v : sch.getSvar.getVard»
						«IF v.varblockdef != null»
							«FOR vas : v.varblockdef.vardef »
								«IF vas.type.toString.trim().operator_equals('clock')»
									//clock variable
									«FOR va : vas.getName»
										«va.name» += time ;
									«ENDFOR»
								«ENDIF»
							«ENDFOR»
						«ELSE»
							«IF v.varsingledef.vardef.type.toString.trim().operator_equals('clock')»
								//clock variable
								«FOR va : v.varsingledef.vardef.getName»
									«va.name» += time ;
								«ENDFOR»
							«ENDIF»
						«ENDIF»					
					«ENDFOR»
				«ENDIF»
			«ENDIF»
			//clock for periodic process				
			«FOR pC : Data.periodicClockProperties.keySet()»
				«IF Data.periodicClockOffset.get(pC).equals("0")»
					«pC» += time ; //offset = 0
				«ELSE»
					if («pC»_offset < «Data.periodicClockOffset.get(pC)»)
						«pC»_offset += time ;
					if («pC»_offset == «Data.periodicClockOffset.get(pC)»)
						«pC» += time ;
				«ENDIF»
			«ENDFOR»
«««			«IF sch.schedulerdata != null»
«««				«var ArrayList<String> collectionlist = new ArrayList<String>()»
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.getProp == null»
«««						«IF ! collectionlist.contains(schdata.col.getName().name) && schdata.col.parent == null»
«««							//add collection «schdata.col.getName().name»: «collectionlist.add(schdata.col.getName().name)»	
«««						«ENDIF»
«««					«ENDIF»
«««				«ENDFOR»
				«FOR col : Data.collectionList»
					«col».add_time(time) ;				
				«ENDFOR»			
«««			«ENDIF»	
«««			«IF sch.schedulerdata != null»
«««				//scheduler data
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.col != null»
«««						«schdata.col.getName().name».add_time(time) ;
«««					«ENDIF»
«««				«ENDFOR»
«««			«ENDIF»	
			if (running_process != null)
				running_process.add_time(time) ;	
		}		
		public void sub_time(int time) {
			«IF sch.getSvar != null»
				«IF sch.getSvar.getVard != null»
					«FOR vas : sch.getSvar.getVard»
						«IF vas.varblockdef != null»
							«FOR v : vas.varblockdef.vardef»
								«IF v.type.toString.trim().operator_equals('clock')»
									//clock variable
									«FOR va : v.getName»
										«va.name» -= time ;
									«ENDFOR»
								«ENDIF»		
							«ENDFOR»
						«ELSE»
							«IF vas.varsingledef.vardef.type.toString.trim().operator_equals('clock')»
								//clock variable
								«FOR va : vas.varsingledef.vardef.getName»
									«va.name» -= time ;
								«ENDFOR»
							«ENDIF»
						«ENDIF»			
					«ENDFOR»
				«ENDIF»
			«ENDIF»
			//clock for periodic process				
			«FOR pC : Data.periodicClockProperties.keySet()»
				if («pC»_offset >= «Data.periodicClockOffset.get(pC)»)
					«pC» -= time ;
			«ENDFOR»
«««			«IF sch.schedulerdata != null»
«««				«var ArrayList<String> collectionlist = new ArrayList<String>()»
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.getProp == null»
«««						«IF ! collectionlist.contains(schdata.col.getName().name) && schdata.col.parent == null»
«««							//add collection «schdata.col.getName().name»: «collectionlist.add(schdata.col.getName().name)»	
«««						«ENDIF»
«««					«ENDIF»
«««				«ENDFOR»
				«FOR col : Data.collectionList»
					«col».sub_time(time) ;				
				«ENDFOR»			
«««			«ENDIF»				
«««			«IF sch.schedulerdata != null»
«««				//scheduler data
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.col != null»
«««						«schdata.col.getName().name».sub_time(time) ;
«««					«ENDIF»
«««				«ENDFOR»
«««			«ENDIF»		
			if (running_process != null)
				running_process.sub_time(time) ;
		}		
		public boolean check_running_time_to_put_running_process(){
			«var hasSelect = false»	
			«IF sch.parent != null»
				«IF sch.handler != null»
					«FOR event : sch.handler.event»
						«IF event.eventname.toString.trim().equals("select_process")»
							//change select behavior: «hasSelect = true»
						«ENDIF»
					«ENDFOR»
				«ENDIF»
			«ENDIF»
			«IF hasSelect || sch.parent == null»
				«IF hasSelect»
					//child changes the behavior of its super
				«ENDIF»
				//if scheduler use time slice, just put the running process into process collection after running time	
				//need to call select_process(-1)	
				«IF Data.runTime»
					«IF Data.schModel.defcore != null»
						if (_putColIndex[current_core] != null) {
							//for putting the running process to the destination collection
							//need to call select_process_set() to get other process
							if (_time_count[current_core] == _time_slice[current_core] && _time_slice[current_core] > 0) { 
								if (running_process != null){
									for (byte colID : _putColIndex[current_core])
										put_running_process(colID) ;						
									//_runningSet.dataSet.clear();
									running_process = null ;
									running_procs[current_core] = null ;
									return true ;
								}
							}
						}
					«ELSE»
						if (_putColIndex != -1) {
							//for putting the running process to the destination collection
							//need to call select_process_set() to get other process
							if (_time_count == _time_slice && _time_slice > 0) { 
								if (running_process != null){
									put_running_process(_putColIndex) ;						
									//_runningSet.dataSet.clear();
									running_process = null ;
									return true ;
								}
							}
						}
					«ENDIF»
				«ENDIF»
				return false ; //process still running
			«ELSE»
				//child follows the behavior of its super
				return super.check_running_time_to_put_running_process() ;
			«ENDIF»
		}		
	'''	
	
	static def String getPeriodicProcessKey (Element e) {
		var processKey= e.process.name 		
		for (value : e.paraAssign) {
			if (value.num != null) {	
				processKey += '_' + value.num.value + '_'										
			} else {
				processKey += '_' + value.bool.value + '_'
			}							
		}
		
		return processKey 
	}
	

	static def String getType(String type) {
		if (type.trim == 'clock') return "int"		
		if (type.trim == 'bool') return "boolean"
		if (type.trim == 'byte') return "byte"
		if (type.trim == 'int') return "int"
		if (type.trim == 'byte') return "byte"		
		return "error type"
	}
	
	static def genDataStruture(SchedulerDef sch)
	'''	
		//--------------- data structure ---------------------------
		«IF sch.parameterlist != null»
			//scheduler parameters
			«FOR para : sch.parameterlist.para»
				«FOR p : para.paraname»
					«getType(para.type.toString)»	«p.name» = «Utilities.findValueInParameterList(p.name, sch.parameterlist.para)»;
				«ENDFOR»
			«ENDFOR»
		«ENDIF»		
		«IF sch.parent == null»
			«IF Data.schModel.defcore != null»
				public SchedulerProcess running_procs[] = new SchedulerProcess[«Data.schModel.defcore.numcore»] ;
			«ENDIF»
			public SchedulerProcess running_process ;	
			
					
			public int size = 0 ;
			«IF Data.schModel.defcore != null»
				public ArrayList<Byte>[] _putColIndex = (ArrayList<Byte>[]) new ArrayList [«Data.schModel.defcore.numcore»] ; //for replacement
				public RunningSet _runningSets[] = new RunningSet[«Data.schModel.defcore.numcore»];
				public RunningSet _runningSet; //temporary store the running set				
			«ELSE»
				public byte _putColIndex = -1; //for replacement
				public RunningSet _runningSet; //temporary store the running set
			«ENDIF»			
		«ENDIF»		
		«IF (Data.runTime)»
			«IF Data.schModel.defcore != null»
				public int _time_count[] = new int[«Data.schModel.defcore.numcore»] ;
				public int _time_slice[] = new int[«Data.schModel.defcore.numcore»] ;
			«ELSE»
				public int _time_count = 0 ;
				public int _time_slice = 0 ;
			«ENDIF»			
			//public int _time = 0 ;					
		«ENDIF»		
		«IF sch.getSvar != null»			
			«IF sch.getSvar.getVard != null»
				//scheduler variables «var ArrayList<String> varlist = new ArrayList<String>()»
				«FOR v : sch.getSvar.getVard»	
					«IF v.ifdef == null»	
						«IF v.varblockdef != null»
							«FOR vas : v.varblockdef.vardef»
								«IF !varlist.contains(vas.name)»
									«IF vas.type.toString.trim().operator_equals('bool')»public boolean «IF vas.bvalue == null»«Utilities.setInitValue(vas.getName.map[name].join(', '), '')»«ELSE»«Utilities.setInitValue(vas.getName.map[name].join(', '), vas.bvalue.value.toString)»«ENDIF»«ENDIF»
									«IF vas.type.toString.trim().operator_equals('time')»public int «Utilities.setInitValue(vas.getName.map[name].join(', '),genNumber(vas.ivalue))» «ENDIF»						
									«IF vas.type.toString.trim() != 'bool' && vas.type.toString.trim() != 'time'»public int «Utilities.setInitValue(vas.getName.map[name].join(', '),genNumber(vas.ivalue))»«ENDIF» //int or clock variable
								«ENDIF»	
							«ENDFOR»
						«ELSE»
							«var vas = v.varsingledef.vardef»
							«IF !varlist.contains(vas.name)»
								«IF vas.type.toString.trim().operator_equals('bool')»public boolean «IF vas.bvalue == null»«Utilities.setInitValue(vas.getName.map[name].join(', '), '')»«ELSE»«Utilities.setInitValue(vas.getName.map[name].join(', '), vas.bvalue.value.toString)»«ENDIF»«ENDIF»
								«IF vas.type.toString.trim().operator_equals('time')»public int «Utilities.setInitValue(vas.getName.map[name].join(', '),genNumber(vas.ivalue))» «ENDIF»														
								«IF vas.type.toString.trim() != 'bool' && vas.type.toString.trim() != 'time'»public int «Utilities.setInitValue(vas.getName.map[name].join(', '),genNumber(vas.ivalue))»«ENDIF» //int, clock or temp variable								
							«ENDIF»
						«ENDIF»
					«ELSE»
						«IF v.varblockdef != null»
							«FOR vas : v.varblockdef.vardef»
								«FOR vs : vas.getName»
									«IF !varlist.contains(vs.name)»
										//add «vs.name»: «varlist.add(vs.name)»							
										«IF vas.type.toString.trim().operator_equals('bool')»public boolean «vs.name» ;//ifdef«ENDIF» 
										«IF vas.type.toString.trim().operator_equals('time')»public int «vs.name» ;//ifdef«ENDIF»
										«IF vas.type.toString.trim() != 'bool' && vas.type.toString.trim() != 'time'»public int «vs.name» ;//ifdef«ENDIF»										
									«ENDIF»
								«ENDFOR»
							«ENDFOR»
						«ELSE»
							«var vas = v.varsingledef.vardef»
							«FOR vs : vas.getName»
								«IF !varlist.contains(vs.name)»
									//add «vs.name»: «varlist.add(vs.name)»							
									«IF vas.type.toString.trim().operator_equals('bool')»public boolean «vs.name» ;//ifdef«ENDIF» 
									«IF vas.type.toString.trim().operator_equals('time')»public int «vs.name» ;//ifdef«ENDIF»
									«IF vas.type.toString.trim() != 'bool' && vas.type.toString.trim() != 'time'»public int «vs.name» ;//ifdef«ENDIF»										
								«ENDIF»
							«ENDFOR»
						«ENDIF» 
					«ENDIF»				
				«ENDFOR»
			«ENDIF»
		«ENDIF»	
		«FOR pClock : Data.periodicClockProperties.keySet()»
			//periodic process clock ?
			public int «pClock» = -1;
		«ENDFOR»
		«FOR pClock : Data.periodicClockOffset.keySet()»
			//offset for periodic process
			«IF pClock.length != 0»
				public int «pClock»_offset ;
			«ENDIF»
		«ENDFOR»
		«IF sch.schedulerdata != null»
			«var ArrayList<String> varlist = new ArrayList<String>()»
			«IF sch.schedulerdata.datadef != null»
				«FOR schdata : sch.schedulerdata.datadef»				
					«IF schdata.datablockdef != null»
						«FOR schdat : schdata.datablockdef.datadef»						
							«IF schdat.getProp != null»
								«var vas = schdat.getProp»
								«IF schdata.ifdef == null»	
									«IF !varlist.contains(vas.name)»
										«IF vas.type.toString.trim().operator_equals('bool')»public boolean «IF vas.bvalue == null»«Utilities.setInitValue(vas.getName.map[name].join(', '), '')»«ELSE»«Utilities.setInitValue(vas.getName.map[name].join(', '), vas.bvalue.value.toString)»«ENDIF»«ENDIF»
										«IF vas.type.toString.trim().operator_equals('time')»public int «Utilities.setInitValue(vas.getName.map[name].join(', '),genNumber(vas.ivalue))» «ENDIF»						
										«IF vas.type.toString.trim() != 'bool' && vas.type.toString.trim() != 'time'»public int «Utilities.setInitValue(vas.getName.map[name].join(', '),genNumber(vas.ivalue))»«ENDIF»
									«ENDIF»	
								«ELSE»
									«FOR v : vas.getName»
										«IF !varlist.contains(v.name)»
											//add «v.name»:«varlist.add(v.name)»					
											«IF vas.type.toString.trim().operator_equals('bool')»public boolean «v.name» ;//ifdef«ENDIF» 
											«IF vas.type.toString.trim().operator_equals('time')»public int «v.name» ;//ifdef«ENDIF»
											«IF vas.type.toString.trim() != 'bool' && vas.type.toString.trim() != 'time'»public int «v.name» ;//ifdef«ENDIF»		
										«ENDIF»
									«ENDFOR» 
								«ENDIF»
							«ENDIF»			
						«ENDFOR»
					«ELSE»
						«IF schdata.datasingledef.getProp != null»
							«var vas = schdata.datasingledef.getProp»
							«IF schdata.ifdef == null»	
								«IF !varlist.contains(vas.name)»
									«IF vas.type.toString.trim().operator_equals('bool')»public boolean «IF vas.bvalue == null»«Utilities.setInitValue(vas.getName.map[name].join(', '), '')»«ELSE»«Utilities.setInitValue(vas.getName.map[name].join(', '), vas.bvalue.value.toString)»«ENDIF»«ENDIF»
									«IF vas.type.toString.trim().operator_equals('time')»public int «Utilities.setInitValue(vas.getName.map[name].join(', '),genNumber(vas.ivalue))» «ENDIF»						
									«IF vas.type.toString.trim() != 'bool' && vas.type.toString.trim() != 'time'»
										«IF vas.ivalue != null»
											public int «Utilities.setInitValue(vas.getName.map[name].join(', '),genNumber(vas.ivalue))»
										«ELSE»
											public int «vas.getName.map[name].join(', ')» ;
										«ENDIF»
									«ENDIF»
								«ENDIF»	
							«ELSE»
								«FOR v : vas.getName»
									«IF !varlist.contains(v.name)»
										//add «v.name»:«varlist.add(v.name)»					
										«IF vas.type.toString.trim().operator_equals('bool')»public boolean «v.name» ;//ifdef«ENDIF» 
										«IF vas.type.toString.trim().operator_equals('time')»public int «v.name» ;//ifdef«ENDIF»
										«IF vas.type.toString.trim() != 'bool' && vas.type.toString.trim() != 'time'»public int «v.name» ;//ifdef«ENDIF»		
									«ENDIF»
								«ENDFOR» 
							«ENDIF»
						«ENDIF»					
					«ENDIF»
				«ENDFOR»
			«ENDIF»
			«FOR col : Data.collectionList»
				ProcessCollectionBase «col» ;
			«ENDFOR»
		«ENDIF»
	'''
	
	static def genNumber(NumValue v){		
		if (v.minus == null) return v.value.toString
		else return '-' + v.value.toString
	}
	
	static def genUtilitiesFunctions(SchedulerDef sch)
	'''	
		//--------------- genUtilitiesFunctions ---------------------------
		public boolean[] getProcessCheckList() {
			«IF sch.parent == null»
				boolean result[] = new boolean[128] ;
				if (running_process != null)
					result[running_process.processID] = true ;
			«ELSE»
				boolean result[] = super.getProcessCheckList() ;
			«ENDIF»
			«IF sch.schedulerdata != null»
«««				«var ArrayList<String> collectionlist = new ArrayList<String>()»
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.getProp == null»
«««						«IF ! collectionlist.contains(schdata.col.getName().name) && schdata.col.parent == null»
«««							//add collection «schdata.col.getName().name»: «collectionlist.add(schdata.col.getName().name)»	
«««						«ENDIF»
«««					«ENDIF»
«««				«ENDFOR»
				«FOR col : Data.collectionList»
					for (ArrayList<SchedulerProcess> procList : «col».dataSet)
						for (SchedulerProcess proc : procList)
							result[proc.processID] = true ;
				«ENDFOR»
			«ENDIF»
			return result ;
		}		
		public ProcessSet getProcessSet(String psName) {
			«IF sch.parent != null»
				ProcessSet result = super.getProcessSet(psName) ;
				if (result != null) return result ;
			«ENDIF»
			«IF sch.schedulerdata != null»
				//scheduler data
«««				«var ArrayList<String> collectionlist = new ArrayList<String>()»
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.getProp == null»
«««						«IF ! collectionlist.contains(schdata.col.getName().name) && schdata.col.parent == null»
«««							//add collection «schdata.col.getName().name»: «collectionlist.add(schdata.col.getName().name)»	
«««						«ENDIF»
«««					«ENDIF»
«««				«ENDFOR»
				«FOR col : Data.collectionList»
					if (psName == "«col»")
						return «col» ;
				«ENDFOR»
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.col != null»
«««						if (psName == "«schdata.col.getName().name»")
«««							return «schdata.col.getName().name» ;
«««					«ENDIF»
«««				«ENDFOR»
			«ENDIF»	
			return null ;
		}				
		public int getSize() {	
			«IF sch.parent == null»
				size = 0; //4 ; //schselopt
				«IF Data.schModel.defcore != null»
					//size += 4 ; //current_core
				«ENDIF»
			«ELSE»
				size = super.getSize() ;
			«ENDIF»
			«IF sch.getSvar != null»				
				//scheduler variables
				«IF sch.getSvar.getVard != null»
					«var ArrayList<String> varlist = new ArrayList<String>()»
					«FOR v : sch.getSvar.getVard»
						«IF v.varblockdef != null»
							«FOR vas : v.varblockdef.vardef»
								«IF vas.type.toString.trim().operator_equals('bool')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											size += 1; //«va.name» «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('time')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											size += 4; //«va.name» add «va.name» to list: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('int')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											size += 4; //«va.name» add «va.name» to list: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('clock')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											size += 4; //«va.name» add «va.name» to list: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
							«ENDFOR»
						«ELSE»
							«var vas = v.varsingledef.vardef»							
							«IF vas.type.toString.trim().operator_equals('bool')»
								«FOR va : vas.getName»
									«IF !varlist.contains(va.name)»
										size += 1; //«va.name» «varlist.add(va.name)»
									«ENDIF»
								«ENDFOR»
							«ENDIF»
							«IF vas.type.toString.trim().operator_equals('time')»
								«FOR va : vas.getName»
									«IF !varlist.contains(va.name)»
										size += 4; //«va.name» add «va.name» to list: «varlist.add(va.name)»
									«ENDIF»
								«ENDFOR»
							«ENDIF»
							«IF vas.type.toString.trim().operator_equals('int')»
								«FOR va : vas.getName»
									«IF !varlist.contains(va.name)»
										size += 4; //«va.name» add «va.name» to list: «varlist.add(va.name)»
									«ENDIF»
								«ENDFOR»
							«ENDIF»
							«IF vas.type.toString.trim().operator_equals('clock')»
								«FOR va : vas.getName»
									«IF !varlist.contains(va.name)»
										size += 4; //«va.name» add «va.name» to list: «varlist.add(va.name)»
									«ENDIF»
								«ENDFOR»
							«ENDIF»
						«ENDIF»	
					«ENDFOR»
				«ENDIF»
			«ENDIF»			
			«IF sch.schedulerdata != null»
				«IF sch.schedulerdata.datadef != null»		
					«var ArrayList<String> varlist = new ArrayList<String>()»		
					//scheduler variables data
					«FOR data : sch.schedulerdata.datadef»
						«IF data.datablockdef != null»				
							«FOR dat : data.datablockdef.datadef»
								«IF dat.prop != null»
									«var vas = dat.prop»				
									«IF vas.type.toString.trim().operator_equals('bool')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												size += 1; //«va.name» add «va.name» to list:  «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('time')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												size += 4; //«va.name»  add «va.name» to list: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('int')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												size += 4; //«va.name» add «va.name» to list:  «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('clock')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												size += 4; //«va.name» add «va.name» to list:  «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»						
								«ENDIF»					
							«ENDFOR»				
						«ELSE»
							«var dat = data.datasingledef»
							«IF dat.prop != null»
								«var vas = dat.prop»				
								«IF vas.type.toString.trim().operator_equals('bool')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											size += 1; //«va.name» add «va.name» to list:  «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('time')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											size += 4; //«va.name»  add «va.name» to list: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('int')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											size += 4; //«va.name» add «va.name» to list:  «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('clock')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											size += 4; //«va.name» add «va.name» to list:  «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»						
							«ENDIF»	
						«ENDIF»
					«ENDFOR»
				«ENDIF»
			«ENDIF»
			«IF (Data.runTime)»
				«IF Data.schModel.defcore != null»
					size += 8 *  «Data.schModel.defcore.numcore»; //_time_count, _time_slice for each core
				«ELSE»
					size += 8 ; //_time_count, _time_slice (system)
				«ENDIF»				
			«ELSE»
				//no time counter
			«ENDIF»
			«FOR pClock : Data.periodicClockProperties.keySet()»
				size += 4 ; //clock «pClock» ;
				size += 4 ; //clock «pClock»_offset ;
			«ENDFOR»
			«IF sch.parent == null»
				«IF Data.schModel.defcore != null»
					for (int i=0; i < «Data.schModel.defcore.numcore»; i++) {
						size += 1; //running_procs[i] != null
						if (running_procs[i] != null)
							size += running_procs[i].getSize() ;
						size += 1 + _putColIndex[i].size(); //_putColIndex
					}					
				«ELSE»
					size += 1 ; //running_process != null ?
					if (running_process != null)				
						size += running_process.getSize() ;			
					//size += _runningSet.getSize() ; // why disable? encode and store in different space (runningSet is stored in stack)
					size += 1 ; //_putColIndex -> for replacement
				«ENDIF»
			«ENDIF»
			«IF sch.schedulerdata != null»
«««				«var ArrayList<String> collectionlist = new ArrayList<String>()»					
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.getCol != null»							
«««						«IF ! collectionlist.contains(schdata.col.getName().name) && schdata.col.parent == null»
«««							//check collection «schdata.col.getName().name»: «collectionlist.add(schdata.col.getName().name)»
«««						«ENDIF»
«««					«ENDIF»
«««				«ENDFOR»			
				//no contains refines collections
				«FOR col : Data.collectionList»
					size += «col».getSize() ;
				«ENDFOR»
			«ENDIF»
«««			«IF sch.schedulerdata != null»				
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.col != null»		
«««						size += «schdata.col.getName().name».getSize() ;
«««					«ENDIF»	
«««				«ENDFOR»
«««			«ENDIF»			
«««			//size += 1 ; //size of chain ??
«««			//size += _chain.size() * 2 ;
			return size ;
		}
	'''
	
	
//	---------------------------------------------	
	static def String genHandler(ProcessDSL procModel, SchedulerDef sch) {
		var String result = ''		
		if (sch.handler != null) {		
			for (e : sch.handler.event) {				
				switch (e.eventname.toString.trim()) {
					case 'select_process' :
						result += Handler.genEventSelect(e) 
					case 'new_process' :
						result += Handler.genEventNew(e, sch)					
					case 'clock' :
						result += Handler.genEventClock(e,procModel)
					case 'pre_take' :
						result += Handler.genPreTake(e)
//					case 'take' :
//						result += Handler.genTake(e) 
					case 'post_take' :
						result += Handler.genPostTake(e) 					
				}
			}	
		}
		return result
	}


/*
	static def genChainClass()
	'''
		class ChainInfo {
			//public byte processID1 ;
			//public byte collectionID ;
			//public byte processID2 ;
			
			public int processID1 ;
			public byte collectionID ;
			public int processID2 ;
		}
	'''
 */	
	static def genUtilityFunction(SchedulerDef sch)
	'''
		/* ---------------------- utility function */
		public String getInstance(SchedulerProcess process) {
			return pcnt.get(process.processID) + ""; 
		}
		public int getRunningInstance() {
			if (running_process == null)
				return -1 ;
			else
				return pcnt.get(running_process.processID);
		}
		public int getRunningID() {
			if (running_process == null)
				return -1 ;
			else
				return running_process.processID ;//pcnt[running_process.processID];
		}
		«IF sch.parent == null»
			public static void printProcessInScheduler(){
				for (int i=0 ; i< 128 ; i++){
					System.out.print(processInScheduler[i] + ", ");
				}
				System.out.println();								
			}		
			public void printProcessInstance(){
				for (int i=0 ; i < pcnt.size() ; i++){
					System.out.print(pcnt.get(i) + ", ");
				}
				System.out.println();					
			}	
			public int addProcessList(String pName){
				//return the index of new process name in process list
				processList.add(pName) ;
				return processList.size() - 1 ;
			}			
			public int isNull(SchedulerProcess process) {
				if (process == null)
					return 1 ;
				else
					return 0 ;
			}			
			public static int getProcessID(String procName){ //or periodicKey
				/*
				* This function will return correct ID for new process (which has name) managed by scheduler 
				* When scheduler initial, it will call this function to initial the order of processes
				*/
				int id = 0;			
				for (String pName : processList){
					if (pName.equals(procName) && !processInScheduler[id]){	
						//processInModel[id] = true ;
						processInScheduler[id] = true ;
						return id ;	
					}
					id ++ ;				
				}
				if (processList.size() < 128) {
					processList.add(procName) ;
					id = processList.size() - 1 ;
					processInScheduler[id] = true;
					return id ;
				}
				return -1 ;
			}
		«ENDIF»			
		public int getCollectionIndex(String collectionName) {
			int numCol = 0 ;
			«IF sch.parent != null»
				int result = super.getCollectionIndex(collectionName) ;
				if (result > -1) return result ;
				numCol = super.getNumberProcessCollection() ;
			«ENDIF»			
			«IF sch.schedulerdata != null»				
				«IF Data.collectionList != null»					
					switch (collectionName) {
						«var idx = 0»
						«FOR col : Data.collectionList»
							case "«col»" : 
								return numCol + «idx++» ;
						«ENDFOR»					
						default :
							Util.println("Put back collection error") ;
							return -1 ;
					}
				«ENDIF»
			«ELSE»
				return -1 ;
			«ENDIF»
		}		
		public int getNumberProcessCollection() {
			«var cnt = 0»
			«IF sch.parent == null»
				int result = 0 ;
			«ELSE»
				int result = super.getNumberProcessCollection() ;
			«ENDIF»
			«IF sch.schedulerdata != null»
				«FOR data : sch.schedulerdata.datadef»
					«IF data.datablockdef != null»
						«FOR dat : data.datablockdef.datadef»
							«IF dat.col != null»
								«IF dat.col.parent != null»
									//«dat.col.name.name» : no
								«ELSE»
									//«dat.col.name.name» : «cnt++»
								«ENDIF»
							«ENDIF»	
						«ENDFOR»
					«ELSE»
						«IF data.datasingledef.col != null»
							«IF data.datasingledef.col.parent != null»
								//«data.datasingledef.col.name.name» : no
							«ELSE»
								//«data.datasingledef.col.name.name» : «cnt++»
							«ENDIF»
						«ENDIF»	
					«ENDIF»
				«ENDFOR»
			«ENDIF»
			result += «cnt» ;
			return result ;
		}		
		//return collection contains process -> needs to be considered
		public int getProcessCollectionID(int processID) {				
			int numCol = 0 ;
			«IF sch.parent != null»
				int result = super.getProcessCollectionID(processID) ;
				if (result >= 0) return result ;
				numCol = super.getNumberProcessCollection() ;
			«ENDIF»		
			
			«var idx = 0»				
			«IF sch.schedulerdata != null»
				«IF sch.schedulerdata.datadef != null»
					«FOR data : sch.schedulerdata.datadef»
						«IF data.datablockdef != null»					
							«FOR dat : data.datablockdef.datadef»
								«IF dat.col != null»
									«IF dat.col.parent == null»
										if («dat.col.name.name».hasProcess(processID) > 0) 
											return «idx++» + numCol ;
									«ENDIF»
								«ENDIF»	
							«ENDFOR»
						«ELSE»
							«IF data.datasingledef != null»
								«var dat = data.datasingledef»
								«IF dat.col != null»
									«IF dat.col.parent == null»
										if («dat.col.name.name».hasProcess(processID) > 0) 
											return «idx++» + numCol ;
									«ENDIF»
								«ENDIF»	
							«ENDIF»
						«ENDIF»
					«ENDFOR»
				«ENDIF»
			«ENDIF»			
			return -1 ;
		}					
		public boolean isTimer() {
			//boolean hasClockEventHandler = «Data.hasClockEventHandler» ; 
			//boolean hasPeriodicProcess = «Data.hasPeriodicProcess» ;
			//boolean runTime = «Data.runTime» ;
			//has clock data type = «Data.clockPropertyList.size > 0»
			«IF Data.hasClockEventHandler || Data.hasPeriodicProcess || Data.clockPropertyList.size > 0»
				return true ;
			«ELSE»
				«IF (Data.runTime)»
					«IF Data.schModel.defcore != null»
						if (_time_slice[current_core] != 0)
							return true ; 
						else
							return false ;
					«ELSE»
						if (_time_slice != 0)
							return true ; //(_time_slice > 0)
						else
							return false ;
					«ENDIF»
				«ELSE»
					«IF (Data.sporadicTime)»
						return true ; //sporadic tasks
					«ELSE»
						«IF sch.parent != null»
							return super.isTimer() ;
						«ELSE»
							return false ;
						«ENDIF»
					«ENDIF»
				«ENDIF»
			«ENDIF»			
		}					
		public SchedulerProcess findProcessByID(int processID) {
			SchedulerProcess proc = null ;
			«IF sch.parent == null»
				if (running_process != null) 
					if (running_process.processID == processID) 
						return running_process ;
			«ELSE»
				proc = super.findProcessByID(processID) ;
				if (proc != null)
					return proc ;
			«ENDIF»
			«IF sch.schedulerdata != null»
«««				«var ArrayList<String> collectionlist = new ArrayList<String>()»
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.getProp == null»
«««						«IF ! collectionlist.contains(schdata.col.getName().name) && schdata.col.parent == null»
«««							//add collection «schdata.col.getName().name»: «collectionlist.add(schdata.col.getName().name)»	
«««						«ENDIF»
«««					«ENDIF»
«««				«ENDFOR»
				«FOR col : Data.collectionList»
					proc = «col».getProcess(processID);
					if (proc != null) return proc ;
				«ENDFOR»			
			«ENDIF»				
«««			«FOR schdata : sch.schedulerdata.data»	
«««				«IF schdata.col != null»
«««					proc = «schdata.col.getName().name».getProcess(processID);
«««					if (proc != null) return proc ;						
«««				«ENDIF»					 
«««			«ENDFOR»
			return null ;
		}		
		public ArrayList<SchedulerProcess> findProcessByrefID(int refID) {
			ArrayList<SchedulerProcess> result = new ArrayList<SchedulerProcess>();			
			«IF sch.parent == null»
				if (running_process != null) 
					if (running_process.refID == refID) 
						result.add(running_process) ;
			«ELSE»
				result.addAll(super.findProcessByrefID(refID)) ;
			«ENDIF»			
			ArrayList<SchedulerProcess> temp = new ArrayList<SchedulerProcess>();
			«IF sch.schedulerdata != null»
«««				«var ArrayList<String> collectionlist = new ArrayList<String>()»
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.getProp == null»
«««						«IF ! collectionlist.contains(schdata.col.getName().name) && schdata.col.parent == null»
«««							//add collection «schdata.col.getName().name»: «collectionlist.add(schdata.col.getName().name)»	
«««						«ENDIF»
«««					«ENDIF»
«««				«ENDFOR»
				«FOR col : Data.collectionList»
					temp = «col».findProcessByrefID(refID);
					if (temp != null)
						result.addAll(temp);					
				«ENDFOR»			
			«ENDIF»		
«««			«FOR schdata : sch.schedulerdata.data»					
«««				«IF schdata.col != null»
«««					temp = «schdata.col.getName().name».findProcessByrefID(refID);
«««					if (temp != null)
«««						result.addAll(temp);
«««				«ENDIF»					 
«««			«ENDFOR»			
			return result ;
		}		
		public void remove_process(int processID) {
			«IF sch.parent != null»
				super.remove_process(processID) ;
			«ENDIF»
			«IF sch.schedulerdata != null»
«««				«var ArrayList<String> collectionlist = new ArrayList<String>()»
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.getProp == null»
«««						«IF ! collectionlist.contains(schdata.col.getName().name) && schdata.col.parent == null»
«««							//add collection «schdata.col.getName().name»: «collectionlist.add(schdata.col.getName().name)»	
«««						«ENDIF»
«««					«ENDIF»
«««				«ENDFOR»
				«FOR col : Data.collectionList»
					«col».removeProcess(processID);					
				«ENDFOR»			
			«ENDIF»	
			«IF Data.schModel.defcore != null»
				for (int i=0; i<num_core; i++) 
					if (i != current_core && _runningSets[i] != null)
						_runningSets[i].removeProcess(processID);
			«ENDIF»
«««			«FOR schdata : sch.schedulerdata.data»	
«««				«IF schdata.col != null»
«««					«schdata.col.getName().name».removeProcess(processID);
«««				«ENDIF»					 
«««			«ENDFOR»
		}		
		public int isEmpty() {
			«IF sch.parent != null»
				if (super.isEmpty() > 0)
					return 1 ;
			«ENDIF»
			«IF sch.schedulerdata != null»
«««				«var ArrayList<String> collectionlist = new ArrayList<String>()»
«««				«FOR schdata : sch.schedulerdata.data»
«««					«IF schdata.getProp == null»
«««						«IF ! collectionlist.contains(schdata.col.getName().name) && schdata.col.parent == null»
«««							//add collection «schdata.col.getName().name»: «collectionlist.add(schdata.col.getName().name)»	
«««						«ENDIF»
«««					«ENDIF»
«««				«ENDFOR»
				«FOR col : Data.collectionList»
					if («col».isEmpty() > 0)
						return 1 ;					
				«ENDFOR»			
			«ENDIF»	
«««			«FOR schdata : sch.schedulerdata.data»
«««				«IF schdata.col != null»
«««					if («schdata.col.getName().name».isEmpty() > 0)
«««						return 1 ;
«««				«ENDIF» 
«««			«ENDFOR»				
			return 0 ;
		}		
		public int hasProcess(String processName) {
			«IF sch.parent == null»			
				if (running_process != null)
					if (getStaticPropertyObject(running_process.refID).pName.trim().equals(processName.trim()))
						return 1 ;
			«ELSE»
				if (super.hasProcess(processName) == 1)
					return 1 ;
			«ENDIF»
			
			int result = 0 ;
			int processID = 0 ;
			for (String pName : processList) {
				if (pName.trim().equals(processName.trim())) { //check all process name in process list					
					«IF sch.schedulerdata != null»
«««						«var ArrayList<String> collectionlist = new ArrayList<String>()»
«««						«FOR schdata : sch.schedulerdata.data»
«««							«IF schdata.getProp == null»
«««								«IF ! collectionlist.contains(schdata.col.getName().name) && schdata.col.parent == null»
«««									//add collection «schdata.col.getName().name»: «collectionlist.add(schdata.col.getName().name)»	
«««								«ENDIF»
«««							«ENDIF»
«««						«ENDFOR»
						«FOR col : Data.collectionList»
							result = «col».hasProcess(processID);	
							if (result > 0) return result ;				
						«ENDFOR»			
					«ENDIF»	
«««					«FOR schdata : sch.schedulerdata.data»
«««						«IF schdata.col != null»
«««							result = «schdata.col.getName().name».hasProcess(processID);
«««						«ENDIF» 
«««					«ENDFOR»	
				}	
				processID ++ ;
			}		
			return result ;
		}
	'''


	static def genContructor(ProcessDSL procModel, SchedulerDef sch)
	'''	
		//--------------- constructor-------------------------------
		public SchedulerObject_«sch.name»() {
			//default constructor			
			«IF sch.handler != null»
				«var numopt = 1»
				«var enew = 1»
				«var eselect = 1»
				«var eclock = 1»				
				«FOR e : sch.handler.event»
					«IF e.event instanceof EventOpt»
						«val ev = e.event as EventOpt»						
						«IF e.eventname.toString.trim().equals( "new_process")»
							//new: «numopt *= ev.opt.size»-«enew = ev.opt.size» 
						«ELSE»
							«IF e.eventname.toString.trim().equals("select_process")»
								//select: «numopt *= ev.opt.size»-«eselect = ev.opt.size» 
							«ELSE»
								«IF e.eventname.toString.trim().equals("clock")»
									//clock: «numopt *= ev.opt.size»-«eclock = ev.opt.size» 
								«ENDIF»
							«ENDIF»
						«ENDIF»
					«ENDIF»
				«ENDFOR»
				_opt = new int[«numopt+1»][3];					
				int index = 0 ;
				for (int i = 1; i <= «enew» ; i++)
					for (int j = 1; j <= «eselect» ; j++)
						for (int k = 1; k <= «eclock» ; k++) {
							index ++ ;							
							_opt[index][0] = i ; //new
							_opt[index][1] = j ; //select
							_opt[index][2] = k ; //clock							
						}
				_schnumopt = «numopt»;
			«ENDIF»
		}		
		public boolean InitSchedulerObject(String args) {
			«IF sch.parameterlist != null»					
				String[] paras = args.split(",");			
				if (paras.length != 0 && paras.length != «sch.parameterlist.para.length») {
					System.out.println("Cannot initialize the scheduler");
					return false;
				}
				
				if (paras.length != 0) {					
					//initial the scheduler parameters «var pCount = 0»				
					«FOR para : sch.parameterlist.para»					
						«FOR p : para.paraname»
							if (paras[«pCount»].equals("")) {
								//skip parameter «pCount»
							} else {
								«IF para.type.toString.contains("int")»
									«p.name» = Integer.parseInt(paras[«pCount++»].trim());
								«ELSE» 
									«IF para.type.toString.contains("bool")»
										«p.name» = Boolean.parseBoolean(paras[«pCount++»].trim());
									«ELSE»
										«IF para.type.toString.contains("byte")»
											«p.name» = Integer.parseInt(paras[«pCount++»].trim());
										«ELSE»							
											System.out.println("Error convert parameter");
											System.out.println("Can not initial the scheduler");
											return ;							 
										«ENDIF»					
									«ENDIF»						
								«ENDIF»
							}
						«ENDFOR»
					«ENDFOR»
				}
			«ENDIF»
			«IF sch.parent == null»
				_runningSet = new RunningSet() ;			
				running_process = null ;
				genStaticProcessProperty() ;
				«IF Data.schModel.defcore != null»
					current_core = 0 ;
					for (int i=0 ; i < «Data.schModel.defcore.numcore»; i ++ ) {
						running_procs[i] = null ;
						_putColIndex[i] = new ArrayList<Byte>();
					}
				«ENDIF»
			«ELSE»
				super.InitSchedulerObject(args) ;
			«ENDIF»	
			//initial the variables
			«IF sch.getSvar != null»			
				«IF sch.getSvar.getVard != null»
					«FOR vas : sch.getSvar.getVard»	
						«IF vas.ifdef != null»
							if («Statements.dispatchExpression(vas.ifdef.cond)») {
								«IF vas.varblockdef != null»
									«FOR v : vas.varblockdef.vardef»
										«IF v.bvalue != null»
											«Utilities.initValue(v.getName.map[name].join(', '), v.bvalue.value.toString)»
										«ENDIF»
										«IF v.type.toString.trim().operator_equals('time')»
											«Utilities.initValue(v.getName.map[name].join(', '), v.ivalue.toString)»
										«ENDIF»						
										«IF v.type.toString.trim() != 'bool' && v.type.toString.trim() != 'time'»
											«Utilities.initValue(v.getName.map[name].join(', '), v.ivalue.toString)»
										«ENDIF»
									«ENDFOR»
								«ELSE»
									«IF vas.varsingledef != null»
										«var v = vas.varsingledef.vardef»
										«IF v.bvalue != null»
											«Utilities.initValue(v.getName.map[name].join(', '), v.bvalue.value.toString)»
										«ENDIF»
										«IF v.type.toString.trim().operator_equals('time')»
											«Utilities.initValue(v.getName.map[name].join(', '), v.ivalue.toString)»
										«ENDIF»						
										«IF v.type.toString.trim() != 'bool' && v.type.toString.trim() != 'time'»
											«Utilities.initValue(v.getName.map[name].join(', '), v.ivalue.toString)»
										«ENDIF»
									«ENDIF»
								«ENDIF»
							}
						«ENDIF»			
					«ENDFOR»
				«ENDIF»
			«ENDIF»		
			//initial the scheduler variables
			«IF sch.schedulerdata != null»
				«FOR data : sch.schedulerdata.datadef»
					«IF data.datablockdef != null»
						«FOR schdata : data.datablockdef.datadef»	
							«IF schdata.getProp != null»						
								«IF data.ifdef != null»
									if («Statements.dispatchExpression(data.ifdef.cond)») {
										«var vas = schdata.getProp»
										«IF vas.bvalue != null»
											«Utilities.initValue(vas.getName.map[name].join(', '), vas.bvalue.value.toString)»
										«ENDIF»
										«IF vas.type.toString.trim().operator_equals('time')»
											«Utilities.initValue(vas.getName.map[name].join(', '),genNumber(vas.ivalue))»
										«ENDIF»						
										«IF vas.type.toString.trim() != 'bool' && vas.type.toString.trim() != 'time'»
											«Utilities.initValue(vas.getName.map[name].join(', '),genNumber(vas.ivalue))»
										«ENDIF»
									}
								«ENDIF»
							«ENDIF»
						«ENDFOR»
					«ELSE»
						«IF data.datasingledef != null»
							«var schdata = data.datasingledef»
							«IF schdata.getProp != null»						
								«IF data.ifdef != null»
									if («Statements.dispatchExpression(data.ifdef.cond)») {
										«var vas = schdata.getProp»
										«IF vas.bvalue != null»
											«Utilities.initValue(vas.getName.map[name].join(', '), vas.bvalue.value.toString)»
										«ENDIF»
										«IF vas.type.toString.trim().operator_equals('time')»
											«Utilities.initValue(vas.getName.map[name].join(', '),genNumber(vas.ivalue))»
										«ENDIF»						
										«IF vas.type.toString.trim() != 'bool' && vas.type.toString.trim() != 'time'»
											«Utilities.initValue(vas.getName.map[name].join(', '),genNumber(vas.ivalue))»
										«ENDIF»
									}
								«ENDIF»
							«ENDIF»
						«ENDIF»
					«ENDIF»
				«ENDFOR»
			«ENDIF»			
			//initial the collections
			«IF sch.schedulerdata != null»
				«IF sch.schedulerdata.datadef != null»
					«FOR schdata : sch.schedulerdata.datadef»	
						«IF schdata.datablockdef != null»
							«IF schdata.ifdef != null»
								if («Statements.dispatchExpression(schdata.ifdef.cond)») {
							«ENDIF»
							«FOR dat : schdata.datablockdef.datadef»
								«IF dat.col != null»						
									«IF schdata.ifdef != null || dat.col.parent != null»										
										«IF (dat.col.comp != null)»
											«IF (dat.col.operationtype.toString.contains("fifo"))»
												«dat.col.getName().name» = new ProcessCollection«DataStructureGenerator.getcollectionName(dat.col)»_fifo() ;
											«ELSE»
												«IF dat.col.operationtype.toString.contains("lifo")»
													«dat.col.getName().name» = new ProcessCollection«DataStructureGenerator.getcollectionName(dat.col)»_lifo() ;
												«ELSE»
													«dat.col.getName().name» = new ProcessCollection«DataStructureGenerator.getcollectionName(dat.col)»() ;
												«ENDIF»
											«ENDIF»						
										«ELSE»
											«IF (dat.col.operationtype.toString.contains("fifo"))»
												«dat.col.getName().name» = new ProcessCollection_fifo() ;
											«ELSE»
												«IF dat.col.operationtype.toString.contains("lifo")»
													«dat.col.getName().name» = new ProcessCollection_lifo() ;
												«ELSE»
													«dat.col.getName().name» = new ProcessCollection() ;
												«ENDIF»
											«ENDIF»	
										«ENDIF»
									«ENDIF»
								«ENDIF»
							«ENDFOR»
							«IF schdata.ifdef != null»
								}
							«ENDIF»
						«ELSE»
							«IF schdata.datasingledef != null»
								«IF schdata.ifdef != null»
									if («Statements.dispatchExpression(schdata.ifdef.cond)») {
								«ENDIF»
								«var dat = schdata.datasingledef»
								«IF dat.col != null»
									«IF schdata.ifdef != null || dat.col.parent != null»										
										«IF (dat.col.comp != null)»
											«IF (dat.col.operationtype.toString.contains("fifo"))»
												«dat.col.getName().name» = new ProcessCollection«DataStructureGenerator.getcollectionName(dat.col)»_fifo() ;
											«ELSE»
												«IF dat.col.operationtype.toString.contains("lifo")»
													«dat.col.getName().name» = new ProcessCollection«DataStructureGenerator.getcollectionName(dat.col)»_lifo() ;
												«ELSE»
													«dat.col.getName().name» = new ProcessCollection«DataStructureGenerator.getcollectionName(dat.col)»() ;
												«ENDIF»
											«ENDIF»						
										«ELSE»
											«IF (dat.col.operationtype.toString.contains("fifo"))»
												«dat.col.getName().name» = new ProcessCollection_fifo() ;
											«ELSE»
												«IF dat.col.operationtype.toString.contains("lifo")»
													«dat.col.getName().name» = new ProcessCollection_lifo() ;
												«ELSE»
													«dat.col.getName().name» = new ProcessCollection() ;
												«ENDIF»
											«ENDIF»	
										«ENDIF»
									«ENDIF»
									«IF schdata.ifdef != null»
										}
									«ENDIF»
								«ENDIF»
							«ENDIF»
						«ENDIF»					
					«ENDFOR»
				«ENDIF»
				//ensure the collections are not null
				«IF sch.schedulerdata != null»
					«FOR dat : sch.schedulerdata.datadef»
						«IF dat.datablockdef != null»
							«FOR schdata : dat.datablockdef.datadef»	
								«IF schdata.col != null»		
									if («schdata.col.getName().name» == null) {
										«IF (schdata.col.comp != null)»
											«IF (schdata.col.operationtype.toString.contains("fifo"))»
												«schdata.col.getName().name» = new ProcessCollection«DataStructureGenerator.getcollectionName(schdata.col)»_fifo() ;
											«ELSE»
												«IF schdata.col.operationtype.toString.contains("lifo")»
													«schdata.col.getName().name» = new ProcessCollection«DataStructureGenerator.getcollectionName(schdata.col)»_lifo() ;
												«ELSE»
													«schdata.col.getName().name» = new ProcessCollection«DataStructureGenerator.getcollectionName(schdata.col)»() ;
												«ENDIF»
											«ENDIF»						
										«ELSE»
											«IF (schdata.col.operationtype.toString.contains("fifo"))»
												«schdata.col.getName().name» = new ProcessCollection_fifo() ;
											«ELSE»
												«IF schdata.col.operationtype.toString.contains("lifo")»
													«schdata.col.getName().name» = new ProcessCollection_lifo() ;
												«ELSE»
													«schdata.col.getName().name» = new ProcessCollection() ;
												«ENDIF»
											«ENDIF»	
										«ENDIF»
									}
								«ENDIF»														
							«ENDFOR»
						«ELSE»
							«IF dat.datasingledef != null»
								«var schdata = dat.datasingledef»
								«IF schdata.col != null»		
									if («schdata.col.getName().name» == null) {
										«IF (schdata.col.comp != null)»
											«IF (schdata.col.operationtype.toString.contains("fifo"))»
												«schdata.col.getName().name» = new ProcessCollection«DataStructureGenerator.getcollectionName(schdata.col)»_fifo() ;
											«ELSE»
												«IF schdata.col.operationtype.toString.contains("lifo")»
													«schdata.col.getName().name» = new ProcessCollection«DataStructureGenerator.getcollectionName(schdata.col)»_lifo() ;
												«ELSE»
													«schdata.col.getName().name» = new ProcessCollection«DataStructureGenerator.getcollectionName(schdata.col)»() ;
												«ENDIF»
											«ENDIF»						
										«ELSE»
											«IF (schdata.col.operationtype.toString.contains("fifo"))»
												«schdata.col.getName().name» = new ProcessCollection_fifo() ;
											«ELSE»
												«IF schdata.col.operationtype.toString.contains("lifo")»
													«schdata.col.getName().name» = new ProcessCollection_lifo() ;
												«ELSE»
													«schdata.col.getName().name» = new ProcessCollection() ;
												«ENDIF»
											«ENDIF»	
										«ENDIF»
									}
								«ENDIF»
							«ENDIF»
						«ENDIF»
					«ENDFOR»
				«ENDIF»
			«ENDIF»
			return true ;
		}	
		public int get_init_process_count() {
			«IF sch.parent != null»
				return super.get_init_process_count() ;
			«ELSE»			
				int pcnt = 0 ;
				«IF procModel != null»				
					«IF procModel.processinit != null»						
						«FOR ps : procModel.processinit.order»						
							«IF ps.set != null»
								pcnt += «ps.element.size» ;	
							«ENDIF»						
						«ENDFOR»
					«ENDIF»
				«ENDIF»
				return pcnt;
			«ENDIF»
		}				
		public void init_order() throws ValidationException {
			«IF sch.parent != null»
				super.init_order() ;
			«ELSE»			
				«IF procModel != null»				
					«IF procModel.processinit != null»
						//initial the order of processes (using order defined in process DSL)
						ArrayList<SchedulerProcess> procList = new ArrayList<SchedulerProcess>() ;
						«FOR ps : procModel.processinit.order»						
							«IF ps.set != null»														
								«FOR pn : ps.element»
									{
										int processID = getProcessID("«pn.process.name»") ;
										if (processID >= 0) { 
											//create new process in model
											//SchedulerPanModel.p
											//create new process information in scheduler
											SchedulerProcess «pn.process.name» = new SchedulerProcess() ;
											//«pn.process.name».processID = (byte) processID ;
											«pn.process.name».processID = processID ;
											
											while (pcnt.size() < processID + 1) pcnt.add((byte) 0) ;				
											pcnt.set(processID, (byte) (pcnt.get(processID) + 1));
															
											«pn.process.name».refID = getRefID("«pn.process.name»") ;										
											«ProcessGenerator.getProcessInitFunction(pn, procModel)»										
											//processList.set(processID, "«getPeriodicProcessKey(pn)»_0") ;
											processList.set(processID, "«pn.process.name»") ;
											
											procList.add(«pn.process.name») ;
										}// else ignore this initial process
									}
								«ENDFOR»
								if (!procList.isEmpty()) {
									addProcessList(procList) ;
									procList.clear() ;
								}							
							«ENDIF»						
							//---------------------------------------
						«ENDFOR»					
					«ELSE»
						//setting all processes in stack and queue into the same poset
						«FOR col : Data.collectionList»
							«col».init_order() ;
						«ENDFOR»
«««						«IF sch.schedulerdata != null»						
«««							«FOR schdata : sch.schedulerdata.data»
«««								«IF schdata.col != null»
«««									«schdata.col.getName().name».init_order() ;
«««								«ENDIF»						
«««							«ENDFOR»					
«««						«ENDIF»
					«ENDIF»
				«ENDIF»
			«ENDIF»
		}
				
		public void init() { 
			«IF sch.gen != null»
				//for the generation
				Generate.out.clear();
				Generate.initGeneration();
			«ENDIF»			
			«IF sch.schedulerinit != null»				
				//init statement
				«FOR stm : sch.schedulerinit.initstm»
					«Statements.dispatchStatement(stm,"")»
				«ENDFOR»
			«ENDIF»			
		}
	'''	

	static def genEncodeDecode(SchedulerDef sch)
	'''
«««		«var ArrayList<String> collectionlist = new ArrayList<String>()»			
«««		«IF sch.schedulerdata != null»		
«««			«FOR schdata : sch.schedulerdata.data»
«««				«IF schdata.getCol != null»							
«««					«IF ! collectionlist.contains(schdata.col.getName().name) && schdata.col.parent == null»
«««						//encode & decode preparation collection «schdata.col.getName().name»: «collectionlist.add(schdata.col.getName().name)»
«««					«ENDIF»
«««				«ENDIF»
«««			«ENDFOR»
«««		«ENDIF»						
		public void encode(DataWriter _writer) {					
			«IF sch.parent == null»		
				«IF Data.schModel.defcore != null»
					//_writer.writeInt(current_core);
					for (int i=0; i<«Data.schModel.defcore.numcore»;i++){
						if (running_procs[i] != null) {
							_writer.writeBool(true);
							running_procs[i].encode(_writer);
						} else {
							_writer.writeBool(false);
						}
						_writer.writeByte(_putColIndex[i].size());
						for (byte id : _putColIndex[i])
							_writer.writeByte(id) ; //for replace running process (selection)
					}
				«ELSE»			
					//_writer.writeInt(_schselopt);
					if (running_process == null)
						_writer.writeBool(false);
					else {		
						_writer.writeBool(true);
						running_process.encode(_writer);
					}
					_writer.writeByte(_putColIndex) ; //for replace running process (selection)
				«ENDIF»
				
			«ELSE»
				super.encode(_writer) ;
			«ENDIF»		
			«IF (Data.runTime)» 	
				«IF Data.schModel.defcore != null»
					for (int i=0; i<«Data.schModel.defcore.numcore»;i++){
						_writer.writeInt(_time_count[i]);
						_writer.writeInt(_time_slice[i]);
					}
				«ELSE»	
					//could be duplicated!
					_writer.writeInt(_time_count);
					_writer.writeInt(_time_slice);
				«ENDIF»				
			«ENDIF»
			«IF sch.getSvar != null»				
				«IF sch.getSvar.getVard != null»
					//scheduler variables «var ArrayList<String> varlist = new ArrayList<String>()»
					«FOR v : sch.getSvar.getVard»
						«IF v.varblockdef != null»
							«FOR vas : v.varblockdef.vardef»	
								«IF v.ifdef == null»
									«IF vas.type.toString.trim().operator_equals('bool')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												_writer.writeBool(«va.name»); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('time')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('int')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('clock')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
								«ELSE»
									«IF vas.type.toString.trim().operator_equals('bool')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												_writer.writeBool(«va.name»); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('time')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('int')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('clock')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»		
								«ENDIF»	
							«ENDFOR»
						«ELSE»
							«var vas = v.varsingledef.vardef»	
							«IF v.ifdef == null»
								«IF vas.type.toString.trim().operator_equals('bool')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeBool(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('time')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('int')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('clock')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
							«ELSE»
								«IF vas.type.toString.trim().operator_equals('bool')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeBool(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('time')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('int')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('clock')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»		
							«ENDIF»
						«ENDIF»
					«ENDFOR»
				«ENDIF»
			«ENDIF»		
			«IF sch.schedulerdata != null»
				//scheduler variables data «var ArrayList<String> varlist = new ArrayList<String>()»
				«FOR dat : sch.schedulerdata.datadef»
					«IF dat.datablockdef != null»
						«FOR data : dat.datablockdef.datadef »
							«IF data.prop != null»
								«var vas = data.prop»											
								«IF vas.type.toString.trim().operator_equals('bool')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeBool(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('time')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('int')»							
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('clock')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
							«ENDIF»
						«ENDFOR»
					«ELSE»
						«IF dat.datasingledef != null»
							«var data = dat.datasingledef»
							«IF data.prop != null»
								«var vas = data.prop»											
								«IF vas.type.toString.trim().operator_equals('bool')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeBool(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('time')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('int')»							
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('clock')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											_writer.writeInt(«va.name»); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
							«ENDIF»
						«ENDIF»
					«ENDIF»					
				«ENDFOR»				
			«ENDIF»	
			«FOR pC : Data.periodicClockProperties.keySet()»
				_writer.writeInt(«pC») ; // periodic process clock
				_writer.writeInt(«pC»_offset) ; // periodic process clock
			«ENDFOR»						
			//_runningSet.encode(_writer); //why disable? encode and store in different space	(runningSet is stored in stack)			
«««			initial before								
			«FOR col : Data.collectionList»
				«col».encode(_writer) ;
			«ENDFOR»
		}	
		public boolean decode(DataReader _reader) {				
			«IF sch.parent == null»		
				«IF Data.schModel.defcore != null»
					//current_core = _reader.readInt();
					for (int i=0; i<«Data.schModel.defcore.numcore»;i++){
						if (_reader.readBool()) {
							if (running_procs[i] == null) 
								running_procs[i] = new SchedulerProcess() ;
							running_procs[i].decode(_reader) ;
							processInScheduler[running_procs[i].processID] = true ;							
						} else {
							running_procs[i] = null ;
						}
						byte num = (byte) _reader.readByte() ;
						_putColIndex[i].clear() ;
						for (byte id=0; id < num; id++)
							_putColIndex[i].add((byte) _reader.readByte()) ; //for replacing running process
					}
					if (current_core >= 0)
						running_process = running_procs[current_core];
					else
						running_process = null;
				«ELSE»		
					//_schselopt = _reader.readInt();
					clearProcessInScheduler() ;
					if (_reader.readBool()) {
						if (running_process == null)
							running_process = new SchedulerProcess() ;
						running_process.decode(_reader) ;
						processInScheduler[running_process.processID] = true ;
					} else {
						running_process = null ;
					}
					_putColIndex = (byte) _reader.readByte() ; //for replacing running process
				«ENDIF»
			«ELSE»
				super.decode(_reader) ;
			«ENDIF»		
			«IF (Data.runTime)»	
				«IF Data.schModel.defcore != null»
					for (int i=0; i<«Data.schModel.defcore.numcore»;i++){
						_time_count[i] = _reader.readInt();
						_time_slice[i] = _reader.readInt();
					}
				«ELSE»	
					//could be duplicated!
					_time_count = _reader.readInt();
					_time_slice = _reader.readInt();
				«ENDIF»				
			«ENDIF»				
			«IF sch.getSvar != null»		
				«IF sch.getSvar.getVard != null»
					//scheduler variables «var ArrayList<String> varlist = new ArrayList<String>()»
					«FOR vs : sch.getSvar.getVard»				
						«IF vs.ifdef == null»	
							«IF vs.varblockdef != null»
								«FOR vas : vs.varblockdef.vardef»
									«IF vas.type.toString.trim().operator_equals('bool')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												«va.name» = _reader.readBool(); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('time')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('int')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('clock')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
								«ENDFOR»
							«ELSE»
								«var vas = vs.varsingledef.vardef»
								«IF vas.type.toString.trim().operator_equals('bool')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readBool(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('time')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('int')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('clock')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
							«ENDIF»
						«ELSE»
							«IF vs.varblockdef != null»
								«FOR vas : vs.varblockdef.vardef»
									«IF vas.type.toString.trim().operator_equals('bool')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												«va.name» = _reader.readBool(); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('time')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('int')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»
									«IF vas.type.toString.trim().operator_equals('clock')»
										«FOR va : vas.getName»
											«IF !varlist.contains(va.name)»
												«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
											«ENDIF»
										«ENDFOR»
									«ENDIF»				
								«ENDFOR»
							«ELSE»
								«var vas = vs.varsingledef.vardef»
								«IF vas.type.toString.trim().operator_equals('bool')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readBool(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('time')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('int')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('clock')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
							«ENDIF»
						«ENDIF»
					«ENDFOR»
				«ENDIF»
			«ENDIF»	
			«IF sch.schedulerdata != null»
				//scheduler variables data  «var ArrayList<String> varlist = new ArrayList<String>()»
				«FOR dat : sch.schedulerdata.datadef»
					«IF dat.datablockdef != null»
						«FOR data : dat.datablockdef.datadef»
							«IF data.prop != null»
								«var vas = data.prop»											
								«IF vas.type.toString.trim().operator_equals('bool')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readBool(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('time')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('int')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('clock')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
							«ENDIF»					
						«ENDFOR»				
					«ELSE»
						«IF dat.datasingledef != null»
							«var data = dat.datasingledef»
							«IF data.prop != null»
								«var vas = data.prop»											
								«IF vas.type.toString.trim().operator_equals('bool')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readBool(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('time')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('int')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
								«IF vas.type.toString.trim().operator_equals('clock')»
									«FOR va : vas.getName»
										«IF !varlist.contains(va.name)»
											«va.name» = _reader.readInt(); //add «va.name»: «varlist.add(va.name)»
										«ENDIF»
									«ENDFOR»
								«ENDIF»
							«ENDIF»
						«ENDIF»
					«ENDIF»
				«ENDFOR»
			«ENDIF»			
			«FOR pC : Data.periodicClockProperties.keySet()»
				«pC» = _reader.readInt() ; // periodic process clock
				«pC»_offset = _reader.readInt() ; // periodic process clock offset
			«ENDFOR»							
			//_runningSet = new ProcessCollection() ;
			//_runningSet.decode(_reader); //why disable? encode and store in different space (runningSet is stored in stack)			
			«FOR col : Data.collectionList»
				«col».decode(_reader) ;
			«ENDFOR»
«««			«IF sch.schedulerdata != null»
«««				«FOR schdata : sch.schedulerdata.data»				
«««					«IF schdata.col != null»							
«««						«schdata.col.getName().name».decode(_reader);//collection
«««					«ENDIF»
«««				«ENDFOR»
«««			«ENDIF»
			return true;
		}
		«IF sch.parent == null»
			public int getRunningSetSize(){
				«IF Data.schModel.defcore != null»
					if (current_core >= 0) {
						int size = «Data.schModel.defcore.numcore» ;
						_runningSets[current_core] = _runningSet;					
						for (int i=0; i<«Data.schModel.defcore.numcore»;i++){
							if (_runningSets[i] != null)
								size += _runningSets[i].getSize();
							else
								size ++ ;
						}	
						return size ;
					} else {
						return 0 ;
					}				
				«ELSE»
					return _runningSet.getSize();
				«ENDIF»
			}			
			public void encodeRunningSet(DataWriter _writer){
				«IF Data.schModel.defcore != null»		
					if (current_core >= 0) {								
						for (int i=0; i<«Data.schModel.defcore.numcore»;i++){
							if (_runningSets[i] != null) {
								_writer.writeBool(true);
								_runningSets[i].encode(_writer);
							} else {
								_writer.writeBool(false);
							}
						}	
					}				
				«ELSE»
					_runningSet.encode(_writer);
				«ENDIF»
			}
			public boolean decodeRunningSet(DataReader _reader) {
				«IF Data.schModel.defcore != null»
					if (current_core >= 0) {
						if (_reader.getSize() > 0) {
							boolean hasProcess ;
							for (int i=0; i<«Data.schModel.defcore.numcore»;i++){
								hasProcess = _reader.readBool();
								if (hasProcess) {
									_runningSets[i].decode(_reader);
								} else {
									if (_runningSets[i] != null)
										_runningSets[i].clear() ;
								}							
							}
							if (_runningSets[current_core] != null) 
								_runningSet = _runningSets[current_core] ;
							else
								if (_runningSet != null)
									_runningSet.clear() ;
						} else {
							for (int i=0; i<«Data.schModel.defcore.numcore»;i++){								
								if (_runningSets[i] != null) {
									_runningSets[i].clear() ;
								}							
							}
						}
					}
				«ELSE»
					_runningSet.decode(_reader);
				«ENDIF»				
				return true ;
			}
			protected void clearProcessInScheduler() {
				for (int i = 0 ; i < 128; i ++)
					processInScheduler[i] = false ;
			}
		«ENDIF»	
	'''


	static def StaticPropertytoJavaCode(SchedulerDSL schModel)
	'''
		package sspinja.scheduling ;
		//Automatic generation
		public class StaticProperty extends StaticProperty_«schModel.scheduler.name» { }	
	'''
	static def StaticPropertyDSLtoJavaCode(ProcessDSL procModel, SchedulerDSL schModel) 
	'''
		package sspinja.scheduling;
		
		//Automatic generation
		«var refinement = schModel.scheduler.parent != null»
		class StaticProperty_«schModel.scheduler.name» «IF refinement»extends StaticProperty_«schModel.scheduler.parent»«ENDIF»{
			«IF !refinement»
				public int refID ;
				public String pName ;
			«ENDIF»
			«IF !Data.valPropertyList.empty»
				«FOR pName : Data.valPropertyList»	
					«IF Data.defPropertyList.contains(pName)»
						public «Data.getType(pName)» get_«pName»() {
							return this.«pName» ;
						}
					«ENDIF»
«««					«IF procModel.processdata != null»
«««						«IF procModel.processdata.properties != null»
«««							«FOR prop : procModel.processdata.properties»	
«««								«FOR pn : prop.name»
«««									«IF pName.equals(pn)»				
«««										public «Data.getType(pName)» get_«pName»() {
«««											return SchedulerObject.getStaticPropertyObject(this.refID).«pName» ;
«««										}
«««									«ENDIF»
«««								«ENDFOR»
«««							«ENDFOR»
«««						«ENDIF»
«««					«ENDIF»
				«ENDFOR»
			«ENDIF»	
«««			«IF !Data.valPropertyList.empty»
«««				«FOR pName : Data.valPropertyList»						
«««					public «Data.getType(pName)» «pName» «IF Data.getValue(pName) != ''» = «Data.getValue(pName)»«ENDIF» ;	
«««				«ENDFOR»
«««			«ENDIF»
«««			«IF !Data.varPropertyList.empty»
«««				«FOR pName : Data.varPropertyList»						
«««					public «Data.getType(pName)» «pName» «IF Data.getValue(pName) != ''» = «Data.getValue(pName)»«ENDIF» ;	
«««				«ENDFOR»
«««			«ENDIF»
		}
	'''
	
	

// for test the property lists
	static def String intPropertyList() {
		var String st = ''
		if (! Data.intProperties.isEmpty())
			for (p : Data.intProperties.keySet())
				st = st + ' ' + p + ' = ? ' + Data.intProperties.get(p) + ','
		return st
	}
	static def String boolPropertyList() {
		var String st = ''
		for (p : Data.boolProperties.keySet())
				st = st + ' ' + p + ' = ? ' + Data.boolProperties.get(p) + ','
		return st
	}
	static def String bytePropertyList() {
		var String st = ''
		for (p : Data.byteProperties.keySet())
				st = st + ' ' + p + ' = ?' + Data.byteProperties.get(p) + ','
		return st
	}
	static def String clockPropertyList() {
		var String st = ''
		for (p : Data.clockProperties.keySet())
				st = st + ' ' + p + ' = ?' + Data.clockProperties.get(p) + ','
		return st
	}
	static def String periodicClockPropertyList() {
		var String st = ''
		for (p : Data.periodicClockProperties.keySet())
				st = st + ' ' + p + ' = ?' + Data.periodicClockProperties.get(p) + '; '
		return st
	}
	
	static def String VerifierToJavaCode()  
	'''		
		package sspinja.verifier;
		import sspinja.SchedulerPanModel;
		
		public class Verifier {
			public static boolean isVerified = false;
			public static void main(String[] args) {
				isVerified = true;
				SchedulerPanModel.main(args);
			}
		}	
	'''
	
	static def String SimulatorToJavaCode()  
	'''		
		package sspinja.simulator;
		import sspinja.ui.MainUI;
		
		public class Simulator {
			public static void main(String[] args) {
				MainUI.main(args);
			}
		}	
	'''
}