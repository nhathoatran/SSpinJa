package scheduling.generator

import org.eclipse.emf.common.util.EList
import scheduling.dsl.EventTemplate
import scheduling.dsl.GenCodeStatement
import scheduling.dsl.GenComponent
import scheduling.dsl.GenConfiguration
import scheduling.dsl.GenLnCodeStatement
import scheduling.dsl.ProcessGeneration
import scheduling.dsl.SchedulerDSL
import scheduling.dsl.StepGeneration

class GenerationGenerator {	
	static var compsize = -1
	static var templatesize = -1
	static var errorindex = -1
	static var stepORprocessIndex = -1
	
	static def initIndexValue(SchedulerDSL schModel){ //need to be called first	
		compsize = 0	
		templatesize = 0
		errorindex = -1
		stepORprocessIndex = -1
		if (schModel.scheduler.gen != null) {				
			for (component : schModel.scheduler.gen.genconfiguration.testpart.part) {
				if (component.name.equals("processes") || component.name.equals("steps"))
				 	stepORprocessIndex = templatesize
				else
					if (component.name.equals("error")) errorindex = templatesize
				templatesize++				
			}
			compsize = templatesize
			if (errorindex >= 0) compsize-- 
			if (stepORprocessIndex >= 0) compsize-- 
		} 
	}
	static def GenerationtoJavaCode(SchedulerDSL schModel) 
	'''		
		package sspinja.scheduling;		
		import spinja.util.StringWriter;
		
		//Automatic generation
		public class Generate {
			public static StringWriter out = new StringWriter();
«««			public static String _initBehaviors = "" ;
			private static boolean searching, error, property, ndbehavior;
			«IF templatesize > 0»
				public static String tempParts[][] = new String[«templatesize»][2];
				«IF compsize > 0»
					public static String component[] = new String[«compsize»];
				«ELSE»
					public static String component[] = new String[1];
				«ENDIF»				
				public static String errorMsg = "" ;
			«ENDIF»
						
			public static boolean getSearchingGenerateOption() { return searching ; }
			public static boolean getErrorGenerateOption() { return error ; }
			public static boolean getPropertyGenerateOption() { return property ;}
			public static boolean getNDBehaviorGenerateOption() { return ndbehavior ;}
			
			public static boolean isGenTestCase() {
				«IF schModel.scheduler.gen.genconfiguration.test.equals("case")» 
					return true;
				«ELSE»
					return false ;
				«ENDIF»
			}
			public static boolean isGenTestProgram() {
				«IF schModel.scheduler.gen.genconfiguration.test.equals("program")» 
					return true;
				«ELSE»
					return false ;
				«ENDIF»
			}
			public static boolean isGenTestData() {
				«IF schModel.scheduler.gen.genconfiguration.test.equals("data")» 
					return true;
				«ELSE»
					return false ;
				«ENDIF»
			}
			public static void initGeneration() {
				«IF compsize > 0»
					for (int i=0; i<«compsize»; i++)
						component[i] = "";
				«ENDIF»
				setGenerateOption() ;
				initComponent() ;
				«IF templatesize > 0»
					//prefix & postfix
					«FOR i : 0..<templatesize»						 
						«IF schModel.scheduler.gen.genconfiguration.testpart.part.get(i).prefix != null»
							tempParts[«i»][0] = «schModel.scheduler.gen.genconfiguration.testpart.part.get(i).prefix» ;
						«ELSE»
							tempParts[«i»][0] = "" ;
						«ENDIF»						 
						«IF schModel.scheduler.gen.genconfiguration.testpart.part.get(i).posfix != null»
							tempParts[«i»][1] = «schModel.scheduler.gen.genconfiguration.testpart.part.get(i).posfix» ;
						«ELSE»
							tempParts[«i»][1] = "" ;
						«ENDIF»
					«ENDFOR»
				«ENDIF»
			}
			public static void setErrorMsg(String msg){
				«IF errorindex >= 0»
					errorMsg = msg;
				«ENDIF»
			}
			public static String getErrorMsg(){
				«IF errorindex >= 0»
					return tempParts[«errorindex»][0];
				«ELSE»
					return "" ;
				«ENDIF»
			}
			«IF schModel.scheduler.gen != null»								
				«generateConfiguration(schModel.scheduler.gen.genconfiguration)»
				«genAddCodetoComponent(schModel.scheduler.gen.gencomponent)»
«««				«GenInitComponent(schModel.scheduler.gen.gencomponent.initgeneration)»
				«GenComponent(schModel.scheduler.gen.gencomponent)»
				«IF schModel.scheduler.gen.stepgeneration != null»
					«GenStepComponent(schModel.scheduler.gen.stepgeneration)»
«««					«IF schModel.scheduler.gen.genconfiguration.test.equals("case") && 
«««						schModel.scheduler.gen.systemgeneration.system.ND_behavior != null»
«««						public static boolean isGenNDBehavior() {
«««							if (ndbehavior) //ndbehavior is defined
«««								return true;
«««							else
«««								return false;
«««						}
«««					«ELSE»
«««						public static boolean isGenNDBehavior() {
«««							return false; //ndbehavior is not defined
«««						}
«««					«ENDIF»
				«ELSE»
					«GenProcessComponent(schModel.scheduler.gen.processgeneration)»
«««					«IF schModel.scheduler.gen.genconfiguration.test.equals("case") && 
«««						schModel.scheduler.gen.processgeneration.process.ND_behavior != null»
«««						public static boolean isGenNDBehavior() {
«««							if (ndbehavior) //ndbehavior is defined
«««								return true;
«««							else
«««								return false;
«««						}
«««					«ELSE»
«««						public static boolean isGenNDBehavior() {
«««							return false; //ndbehavior is not defined
«««						}
«««					«ENDIF»
				«ENDIF»
			«ELSE»
				public static void setGenerateOption(){}
				public static String getGenerateDirectory(){ return "Gen"; }
				public static String getGenerateFileName(){ return "gen"; }
				public static String getGenerateFileExtension(){ return "gen"; }
				public static void addCode(String code, String component, boolean breakline) {}
				public static void initComponent() {}						
			«ENDIF»
			public static int getTemplateSize(){
				return «templatesize»;
			}
			public static int getCompSize(){
				return «compsize»;
			}
«««			public static int getProcessBehaviorPosition(){
«««				return «codeindex» ;
«««			}
«««			public static int getInitsBehaviorPosition(){
«««				return «initindex» ;
«««			}
			public static int getErrorPosition(){
				return «errorindex» ;
			}
			public static int getActionsPosition(){
				return «stepORprocessIndex» ;
			}			
		}
	'''		
	static def generateConfiguration(GenConfiguration genconfiguration)
	'''	
		public static void setGenerateOption(){
			«IF genconfiguration.generateoption != null»	
				«IF genconfiguration.generateoption.option != null»		
					«FOR opt : genconfiguration.generateoption.option»
						«IF opt.getName.equals("searching")»
							searching = true ;
						«ENDIF»
						«IF opt.getName.equals("error")»
							error = true ;
						«ENDIF»		
						«IF opt.getName.equals("property")»
							property = true ;
						«ENDIF»	
						«IF opt.getName.equals("ndbehavior")»
							ndbehavior = true ;
						«ENDIF»				
						«IF opt.getName.equals("all")»
							searching = true ;
							error = true ;
							property = true ;
							ndbehavior = true ;
						«ENDIF»					
					«ENDFOR»	
				«ENDIF»
			«ELSE»
				searching = true ;
				error = true ;
				property = true ;
			«ENDIF»
		}	
	
		public static String getGenerateDirectory(){
			«IF genconfiguration.directory != null»			
				return «genconfiguration.directory.name» ;
			«ELSE»
				return "Gen" ;
			«ENDIF»
		}
	
		public static String getGenerateFileName(){
			«IF genconfiguration.filename != null»			
				return «genconfiguration.filename.name» ;
			«ELSE»
				return "Gen" ;
			«ENDIF»
		}
	
		public static String getGenerateFileExtension(){
			«IF genconfiguration.fileextension != null»			
				return «genconfiguration.fileextension.name» ;
			«ELSE»
				return "gen" ;
			«ENDIF»
		}
	'''
	
	static def genAddCodetoComponent(GenComponent gencomponent)
	'''	
		«IF gencomponent != null»
			public static void genCodeForComp(String code, String _component, boolean breakline) {
				«IF compsize > 0»
					int index = -1;
					«var index = 0»
					switch (_component) {				
						«FOR comp : gencomponent.component»
							case "«comp.name»":
								index = «index++»; break ;
						«ENDFOR»
					}			
					if (index >=0 && index <= «compsize») {						
						component[index] += breakline? code + "\n" : code ;
					}
				«ENDIF»
			}
			public static void initComponent() {
				«FOR component : gencomponent.component»
					«FOR stm : component.stm»
						«IF stm instanceof GenCodeStatement»
							«IF stm.comp != null»
								genCodeForComp(«Statements.dispatchExpression(stm.st)», "«stm.comp»", false);
							«ELSE»
								genCodeForComp(«Statements.dispatchExpression(stm.st)», "«component.name»", false);
							«ENDIF»
						«ENDIF»
						«IF stm instanceof GenLnCodeStatement»
							«IF stm.comp != null»
								genCodeForComp(«Statements.dispatchExpression(stm.st)», "«stm.comp»", true);
							«ELSE»
								genCodeForComp(«Statements.dispatchExpression(stm.st)», "«component.name»", true);
							«ENDIF»
						«ENDIF»
					«ENDFOR»  
				«ENDFOR»
			}
		«ELSE»
			public static void genCodeForComp(String code, String _component, boolean breakline) { }
			public static void initComponent() {}
		«ENDIF»
	'''
	
	static def GenStepComponent(StepGeneration stepgeneration)
	'''
		public static void setBehavior(GenerateCode _generatecode){
			//following behavior template
			_generatecode.gencode.code = "";
			«GenTemplate(stepgeneration.step.behavior.eventtemplate)»
		}
		public static void setNDBehavior(GenerateCode _generatecode){
			«IF (stepgeneration.step.ND_behavior != null)»
				//following ND behavior template
				_generatecode.gencode.code = "";
				«GenTemplate(stepgeneration.step.ND_behavior.eventtemplate)»
			«ELSE»
				//setBehavior(_generatecode);
			«ENDIF»
		}
		public static String generateProcess(int PID, int InstanceID, String PName, String Behaviors){
			return Behaviors;
		}
	'''
	
	static def GenTemplate(EList<EventTemplate> eventtemplate)
	'''
		«FOR e : eventtemplate»
			«IF e.prefix != null»
				«IF e.postfix != null»
					_generatecode.gencode.code += 
						«Statements.dispatchExpression(e.prefix).
							replace("<InstanceID>","\"<InstanceID>\"").
							replace("<PID>","\"<PID>\"").
							replace("<PName>","\"<PName>\"").
							replace("Integer.parseInt","")» + 
						_generatecode.gencode.«e.event.toString.replace("<","").replace(">","")» + 
						«Statements.dispatchExpression(e.postfix).
							replace("<InstanceID>","\"<InstanceID>\"").
							replace("<PID>","\"<PID>\"").
							replace("<PName>","\"<PName>\"").
							replace("Integer.parseInt","")» ;
				«ELSE»
					_generatecode.gencode.code += 
						«Statements.dispatchExpression(e.prefix).
							replace("<InstanceID>","\"<InstanceID>\"").
							replace("<PID>","\"<PID>\"").
							replace("<PName>","\"<PName>\"").
							replace("Integer.parseInt","")» + 
						_generatecode.gencode.«e.event.toString.replace("<","").replace(">","")» ;
				«ENDIF»
			«ELSE»
				«IF e.postfix != null»
					_generatecode.gencode.code += 
						_generatecode.gencode.«e.event.toString.replace("<","").replace(">","")» + 
						«Statements.dispatchExpression(e.postfix).
							replace("<InstanceID>","\"<InstanceID>\"").
							replace("<PID>","\"<PID>\"").
							replace("<PName>","\"<PName>\"").
							replace("Integer.parseInt","")» ;
				«ELSE»
					_generatecode.gencode.code += _generatecode.gencode.«e.event.toString.replace("<","").replace(">","")» ;
				«ENDIF»
			«ENDIF»								
		«ENDFOR»
	'''
	
	static def GenProcessComponent(ProcessGeneration processgeneration)
	'''	
		public static void setBehavior(GenerateCode _generatecode){
			//following behavior template
			_generatecode.gencode.code = "";
			«GenTemplate(processgeneration.process.behavior.eventtemplate)»				
		}
		public static void setNDBehavior(GenerateCode _generatecode){
			«IF (processgeneration.process.ND_behavior != null)»
				//following ND behavior template
				_generatecode.gencode.code = "";
				«GenTemplate(processgeneration.process.ND_behavior.eventtemplate)»
			«ELSE»
				//setBehavior(_generatecode);
			«ENDIF»				
		}
		«IF processgeneration.process.template != null»							
			public static String generateProcess(int PID, int InstanceID, String PName, String actions){
				return «Statements.dispatchExpression(processgeneration.process.template.templ)
						.replace("<InstanceID>","InstanceID")
						.replace("<PID>","PID")
						.replace("<PName>","PName")
						.replace("<actions>","actions")
						.replace("Integer.parseInt","")
						» ;
			}
		«ELSE»
			public static String generateProcess(int PID, int InstanceID, String PName, String actions){
				return actions;
			}
		«ENDIF»		
	'''
	
	static def GenComponent(GenComponent gencomp)
	'''								
		public static String generateComponent(String contains, int index){
			«IF gencomp != null»
				switch (index) {
					«var index = 0»
					«FOR component : gencomp.component»
						«IF component.template != null»
							case «index++» :
								return «Statements.dispatchExpression(component.template.templ).replace("<contains>", "contains")» ;
						«ENDIF»
					«ENDFOR»
				}
			«ENDIF»
			return contains ;
		}		
	'''
	
//	static def GenInitComponent(InitGeneration initgeneration)
//	'''	
//		«IF initgeneration != null»
//			public static String getGenerateInit(String initBehavior){				
//				return «Statements.dispatchExpression(initgeneration.init.template.templ).replace("<behaviors>","initBehavior")» ;
//			}
//				
//			public static void setInitBehavior(GenerateCode _generatecode){
//				//following init template
//				_generatecode.gencode.code = "";
//				«FOR e : initgeneration.init.behavior.eventtemplate»
//					«IF e.prefix != null»
//						«IF e.postfix != null»
//							_generatecode.gencode.code += «Statements.dispatchExpression(e.prefix)» + _generatecode.gencode.«e.event» + «Statements.dispatchExpression(e.postfix)» ;
//						«ELSE»
//							_generatecode.gencode.code += «Statements.dispatchExpression(e.prefix)» + _generatecode.gencode.«e.event» ;
//						«ENDIF»
//					«ELSE»
//						«IF e.postfix != null»
//							_generatecode.gencode.code += _generatecode.gencode.«e.event» + «Statements.dispatchExpression(e.postfix)» ;
//						«ELSE»
//							_generatecode.gencode.code += _generatecode.gencode.«e.event» ;
//						«ENDIF»
//					«ENDIF»								
//				«ENDFOR»
//				_initBehaviors = getGenerateInit(_generatecode.gencode.code);
//			}
//		«ELSE»
//			public static String getGenerateInit(String initBehavior){ return "" ; }
//			public static void setInitBehavior(GenerateCode _generatecode){}
//		«ENDIF»
//	'''
	static def GenerateCodetoJavaCode(SchedulerDSL schModel)
	'''
		package sspinja.scheduling;
		
		//Automatic generation
		public class Code {
			public String clock = "" ;
			public String select_process ="";	
			public String new_process ="";
			public String pre_take ="";
			public String post_take ="";
			public String process_action ="";
			public String scheduling_action ="";			
			public String code ="";
			
			«IF schModel.scheduler.gen != null»		
«««				//«FOR part : schModel.scheduler.gen.genconfiguration.testpart.part»«part.name» «ENDFOR»			
				public String component[] = new String[«compsize»];
				public static String s_component[] = new String[«compsize»];				
			«ENDIF»
				
			public void clearCode() {
				clock = "" ;
				select_process = "";		
				new_process = "";
				pre_take = "";
				post_take = "";
				process_action = "";
				scheduling_action = "";
				code = "" ;
				«IF schModel.scheduler.gen != null»
					for (int i = 0 ; i < «compsize»; i++)
						component[i] = "";
				«ENDIF»
			}
			
			public void clearcode(String _component) {
				switch (_component) {
					case "clock" :
						clock = "" ; break ;
					case "select_process" :
						select_process = ""; break ;
					case "new_process" :
						new_process = "" ; break ;
					case "pre_take" :
						pre_take = ""; break ;
					case "post_take" :
						post_take = "" ; break ;
					case "process_action" :
						process_action = ""; break ;
					case "scheduling_action" :
						scheduling_action = ""; break ;
					case "code" :
						code = ""; break ;
				}
			}
			
			public void setcode(String _component, String code) {
				switch (_component) {
					case "clock" :
						clock = code ; break ;
					case "select_process" :
						select_process = code ; break ;
					case "new_process" :
						new_process = code ; break ;
					case "pre_take" :
						pre_take = code ; break ;
					case "post_take" :
						post_take = code ; break ;
					case "process_action" :
						process_action = code; break ;
					case "scheduling_action" :
						scheduling_action = code; break ;
				}
				«IF schModel.scheduler.gen != null»
					for (int i = 0; i < «compsize»; i++) {
						if (s_component[i] != null) {
							if (component[i] != null) {
								component[i] += s_component[i];
							} else {
								component[i] = s_component[i] + "";
							}
						}
						s_component[i] = "" ;
					}
				«ENDIF»
			}
			
			protected Code clone() {
				Code newcode = new Code() ;
				
				newcode.clock = clock;
				newcode.select_process = select_process;		
				newcode.new_process = new_process;
				newcode.pre_take = pre_take;
				newcode.post_take = post_take;
				newcode.process_action  = process_action;
				newcode.scheduling_action  = scheduling_action;
				newcode.code = code ;
				«IF schModel.scheduler.gen != null»
					for (int i=0; i< «compsize»; i++){		
						if (component[i] == null) {
							newcode.component[i] = "";
						} else {
							newcode.component[i] = component[i] + "" ;
						}
					}
				«ENDIF»
				return newcode ;
			}
			
			public static void addCodetoComponent(String code, String component, boolean breakline) {
				int index = -1;
				«var index = 0»
				«IF schModel.scheduler.gen != null»
					switch (component) {					
						«FOR component : schModel.scheduler.gen.genconfiguration.testpart.part»
							case "«component.name»" :
								index = «index++»; break ;
						«ENDFOR»
					}				
				
					if (index >=0 && index <= «compsize») {		
						if (s_component[index] == null)
							s_component[index] = breakline? code + "\n" : code ;
						else
							s_component[index] += breakline? code + "\n" : code ;
					}
				«ENDIF»				
			}
		}
	'''
}