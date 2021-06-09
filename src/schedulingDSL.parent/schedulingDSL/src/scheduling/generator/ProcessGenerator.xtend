package scheduling.generator

import java.util.ArrayList
import java.util.HashMap
import java.util.HashSet
import scheduling.dsl.Constraint
import scheduling.dsl.Element
import scheduling.dsl.ProcessDSL
import scheduling.dsl.ProcessDef
import scheduling.dsl.ProcessInit
import scheduling.dsl.SchedulerDSL
import scheduling.dsl.Value

import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

import static scheduling.generator.Data.*

import static extension org.eclipse.xtext.nodemodel.util.NodeModelUtils.*
import scheduling.dsl.Set
import static extension scheduling.generator.ArrayLiterals2.*

class ProcessGenerator {

	static def ProcessDatatoJavaCode(ProcessDSL procModel )
	'''	
		«FOR p: procModel.getProcess»									
			«IF p.proctype != null»						
				/* for process «p.proctype.name» */			
				int sch.«p.proctype.name».get_processID() ;
				byte sch.«p.proctype.name».isNull() ;
				«IF (! Data.intProperties.isEmpty())»
					«FOR prop : Data.intProperties.keySet()»		
						«IF Data.valPropertyList.contains(prop)»
							int sch.«p.proctype.name».get_«prop»() ;
						«ELSE»
							«IF Data.varPropertyList.contains(prop)»			
								int sch.«p.proctype.name».«prop» ;
								int sch.«p.proctype.name».get_«prop»() ;
							«ENDIF»					
						«ENDIF»
					«ENDFOR»
				«ENDIF»
				«IF (! Data.boolProperties.isEmpty())»
					«FOR prop : Data.boolProperties.keySet()»
						«IF Data.valPropertyList.contains(prop)»
							bool sch.«p.proctype.name».get_«prop»() ;
						«ELSE»
							«IF Data.varPropertyList.contains(prop)»			
								bool sch.«p.proctype.name».«prop» ;
								bool sch.«p.proctype.name».get_«prop»() ;
							«ENDIF»					
						«ENDIF»					
					«ENDFOR»
				«ENDIF»
				«IF (! Data.byteProperties.isEmpty())»
					«FOR prop : Data.byteProperties.keySet()»
						«IF Data.valPropertyList.contains(prop)»
							byte sch.«p.proctype.name».get_«prop»() ;
						«ELSE»
							«IF Data.varPropertyList.contains(prop)»			
								byte sch.«p.proctype.name».«prop» ;
								byte sch.«p.proctype.name».get_«prop»() ;
							«ENDIF»					
						«ENDIF»
					«ENDFOR»
				«ENDIF»
				«IF (! Data.clockProperties.isEmpty())»
					«FOR prop : Data.clockProperties.keySet()»
						int sch.«p.proctype.name».«prop» ;
						int sch.«p.proctype.name».get_«prop»() ;
					«ENDFOR»
				«ENDIF»
			«ENDIF»
		«ENDFOR»		
		
		/* for running_process and target_process process */	
		int sch.running_process.get_processID() ;	
		byte sch.running_process.isNull() ;
		int sch.target_process.get_processID() ;
		byte sch.target_process.isNull() ;
		«IF (! Data.intProperties.isEmpty())»
			«FOR prop : Data.intProperties.keySet()»
				«IF Data.varPropertyList.contains(prop)»				
					int sch.running_process.«prop» ;
					int sch.target_process.«prop» ;
					int sch.running_process.get_«prop»() ;
					int sch.target_process.get_«prop»() ;
				«ENDIF»		
				«IF Data.valPropertyList.contains(prop) »
					int sch.running_process.get_«prop»() ;
					int sch.target_process.get_«prop»() ;
				«ENDIF»				
			«ENDFOR»
		«ENDIF»
		«IF (! Data.boolProperties.isEmpty())»
			«FOR prop : Data.boolProperties.keySet()»
				«IF Data.varPropertyList.contains(prop)»
					bool sch.running_process.«prop» ;
					bool sch.target_process.«prop» ;
					bool sch.running_process.get_«prop»() ;
					bool sch.target_process.get_«prop»() ;
				«ENDIF»
				«IF Data.valPropertyList.contains(prop)»
					bool sch.running_process.get_«prop»() ;
					bool sch.target_process.get_«prop»() ;
				«ENDIF»					
			«ENDFOR»
		«ENDIF»
		«IF (! Data.byteProperties.isEmpty())»
			«FOR prop : Data.byteProperties.keySet()»
				«IF Data.varPropertyList.contains(prop)»							
					byte sch.running_process.«prop» ;
					byte sch.target_process.«prop» ;
					byte sch.running_process.get_«prop»() ;
					byte sch.target_process.get_«prop»() ;
				«ENDIF»
				«IF Data.valPropertyList.contains(prop)»							
					byte sch.running_process.get_«prop»() ;
					byte sch.target_process.get_«prop»() ;
				«ENDIF»
			«ENDFOR»
		«ENDIF»	
		«IF (! Data.clockProperties.isEmpty())»
			«FOR prop : Data.clockProperties.keySet()»
				int sch.running_process.«prop» ;
				int sch.target_process.«prop» ;
				int sch.running_process.get_«prop»() ;
				int sch.target_process.get_«prop»() ;
			«ENDFOR»
		«ENDIF»	
	'''

	static def ProcessBasetoJavaCode()
	'''
		package sspinja.scheduling ;
		
		import java.util.ArrayList;
		
		//Automatic generation
		public class SchedulerProcessBase {
			public static ArrayList<String> processList = new ArrayList<String>() ;	
			public void inc_time() {}
			public void dec_time() {}
			public void add_time(int time) {}
			public void sub_time(int time) {}			
		}
	'''
	static def ProcesstoJavaCode(ProcessDSL procModel)
	'''
		package sspinja.scheduling ;
		//Automatic generation
		public class SchedulerProcess extends SchedulerProcess_«procModel.name» { }	
	'''
	
	static def ProcessDSLtoJavaCode(ProcessDSL procModel, SchedulerDSL schModel) 
	'''		
		package sspinja.scheduling ;
		import spinja.util.*;
		import spinja.exceptions.ValidationException;
		import java.util.ArrayList;
		
		«var parent = (procModel.parent != null)»
		//Automatic generation
		public class SchedulerProcess_«procModel.name» «IF procModel.parent != null» extends SchedulerProcess_«procModel.parent»«ELSE» extends SchedulerProcessBase«ENDIF» {	
			«IF !parent»
				public int processID ;
				public int refID ;
				
				public int get_processID() {
					return processID ;
				}				
				public void print(){
					Util.println(this.toString()) ;
				}
				
			«ENDIF»			
			«IF !Data.varPropertyList.empty»
				«FOR pName : Data.varPropertyList»		
					«IF Data.defPropertyList.contains(pName)»				
						public «Data.getType(pName)» «pName» «IF Data.getValue(pName) != ''» = «Data.getValue(pName)»«ENDIF» ;
					«ENDIF»	
				«ENDFOR»
			«ENDIF»						
			«IF !Data.varPropertyList.empty»
				«FOR pName : Data.defPropertyList» 
«««				// .varPropertyList»
«««					pName:«pName»
					«IF procModel.processdata != null»
						«IF procModel.processdata.properties != null»
							«FOR prop : procModel.processdata.properties»	
								«FOR pn : prop.name»
«««									//pn: «pn.name» ? pName: «pName»
									«IF pName.trim().equals(pn.name.toString().trim())»				
										public «Data.getType(pName)» get_«pName»() {
											return this.«pName» ;
										}
									«ENDIF»
								«ENDFOR»
							«ENDFOR»
						«ENDIF»
					«ENDIF»
				«ENDFOR»
			«ENDIF»			
			«IF !Data.valPropertyList.empty»
				«FOR pName : Data.valPropertyList»	
					«IF procModel.processdata != null»
						«IF procModel.processdata.properties != null»
							«FOR prop : procModel.processdata.properties»	
								«FOR pn : prop.name»
									«IF pName.equals(pn)»				
										public «Data.getType(pName)» get_«pName»() {
											return SchedulerObject.getStaticPropertyObject(this.refID).«pName» ;
										}
									«ENDIF»
								«ENDFOR»
							«ENDFOR»
						«ENDIF»
					«ENDIF»
				«ENDFOR»
			«ENDIF»	
							
			«IF !parent && Data.clockPropertyList.empty »
				public void inc_time() {}
				public void dec_time() {}
				public void add_time(int time) {}
				public void sub_time(int time) {}
			«ELSE»
				«IF !Data.clockPropertyList.empty»
					public void inc_time() {
						super.inc_time() ;
						«FOR clock : Data.clockPropertyList»
							«clock» ++ ;
						«ENDFOR»
					}					
					public void dec_time() {
						super.dec_time() ;
						«FOR clock : Data.clockPropertyList»
							«clock» -- ;
						«ENDFOR»
					}					
					public void add_time(int time) {
						super.add_time(time) ;
						«FOR clock : Data.clockPropertyList»
							«clock» += time ;
						«ENDFOR»					
					}					
					public void sub_time(int time) {
						super.sub_time(time) ;
						«FOR clock : Data.clockPropertyList»
							«clock» -= time ;
						«ENDFOR»
					}
				«ENDIF»
			«ENDIF»
			public int getSize() {
				int size = 0;				
				«IF !Data.varPropertyList.empty»
					«FOR pName : Data.varPropertyList»						
						«IF Data.getType(pName) == 'int'»size += 4 ; //«pName»«ENDIF»
						«IF Data.getType(pName) == 'byte'»size += 1 ; //«pName»«ENDIF»	
						«IF Data.getType(pName) == 'boolean'»size += 1 ;//«pName»«ENDIF»	
					«ENDFOR»
				«ENDIF»
				«IF parent»
					size += super.getSize() ;
					return size ;
				«ELSE»
					return 4 + 4 + size ; //processID, refID, process properties				
				«ENDIF»
			}
			
			public SchedulerProcess_«procModel.name»(int processID) {
				//this.processID = (byte) processID ;
				this.processID = processID ;
			}			
			public SchedulerProcess_«procModel.name»() {}
			
			public String getName() {
				return SchedulerObject.getStaticPropertyObject(this.refID).pName + "_" + this.processID; 
			}
							
			public String toString() {
				«IF procModel.parent == null»			
					String res = "    + Process ID=" + this.processID ; 
					res += ", Name: " + SchedulerObject.getStaticPropertyObject(this.refID).pName + ", Ref ID=" + this.refID ;
				«ELSE»
					String res = super.toString() ;
				«ENDIF»
				«IF !Data.varPropertyList.empty»
					«FOR pName : Data.varPropertyList»
						res +=  ", «pName» = " + this.«pName» ;
					«ENDFOR»
				«ENDIF»				
				return res ;
			}
			
			public void encode(DataWriter _writer) {
				//_writer.writeByte(processID);
				//_writer.writeByte(refID);
				«IF !parent»
					_writer.writeInt(processID);
					_writer.writeInt(refID);
				«ELSE»
					super.encode(_writer) ;
				«ENDIF»
				«IF ! Data.varPropertyList.empty»
					«FOR pName : Data.varPropertyList»	
						«IF Data.defPropertyList.contains(pName)»					
							«IF Data.getType(pName) == 'int'»_writer.writeInt(«pName») ;«ENDIF»
							«IF Data.getType(pName) == 'byte'»_writer.writeByte(«pName») ;«ENDIF»	
							«IF Data.getType(pName) == 'boolean'»_writer.writeBool(«pName») ;«ENDIF»
						«ENDIF»	
					«ENDFOR»
				«ENDIF»
			}		
			public boolean decode(DataReader _reader) {		
				//processID = (byte) _reader.readByte();
				//refID = (byte) _reader.readByte();
				«IF !parent»
					processID = _reader.readInt();
					SchedulerObject.processInScheduler[processID] = true ;
					refID = _reader.readInt();
				«ELSE»
					super.decode(_reader);
				«ENDIF»
				«IF !Data.varPropertyList.empty»
					«FOR pName : Data.varPropertyList»	
						«IF Data.defPropertyList.contains(pName)»					
							«IF Data.getType(pName) == 'int'»«pName» = _reader.readInt() ; «ENDIF»
							«IF Data.getType(pName) == 'byte'»«pName» = (byte) _reader.readByte() ; «ENDIF»
							«IF Data.getType(pName) == 'boolean'»«pName» = _reader.readBool() ; «ENDIF»
						«ENDIF»	
					«ENDFOR»
				«ENDIF»
				return true;
			}
			
			«FOR pt: procModel.getProcess»						
				«IF pt.proctype != null»
					«IF pt.paralist != null»
						//assign function		
						public void «pt.proctype.name»(«pt.paralist.para.map[(if(type.toString.equals('bool')) 'boolean' else type) + ' ' + paraname.map[name].join(', ')].join(', ')») {
							«FOR properass: pt.propertyassignment»
								«IF Data.varPropertyList.contains(properass.propers.name)»								 
									«IF properass.propers != null» 
										«IF properass.pvalue != null» 
											«IF properass.pvalue.num != null» 
												this.«properass.propers.name» = («Data.getType(properass.propers.name)») «properass.pvalue.num.value» ;
											«ELSE»
												this.«properass.propers.name» = «properass.pvalue.bool.value» ;
											«ENDIF»
										«ELSE»
											this.«properass.propers.name» = («Data.getType(properass.propers.name)») «properass.pname.name» ;
										«ENDIF»								
									«ENDIF»
								«ENDIF»
							«ENDFOR»							
						}
					«ENDIF»					
					//default value
					public void «pt.proctype.name»() {
						«FOR properass: pt.propertyassignment»
«««							«IF Data.varPropertyList.contains(properass.propers.name)» for refinement								 
								«IF properass.propers != null» 
									«IF properass.pvalue != null» 
										«IF properass.pvalue.num != null» 
											this.«properass.propers.name» = «properass.pvalue.num.value» ;
										«ELSE»
											this.«properass.propers.name» = «properass.pvalue.bool.value» ;
										«ENDIF»
									«ELSE»										
										this.«properass.propers.name» = «Utilities.findValueInParameterList(properass.pname.name, pt.paralist.para)» ; 
									«ENDIF»								
								«ENDIF»
«««							«ENDIF»
						«ENDFOR»
					}					
					//init process with string parameter
					public void «pt.proctype.name»(String paraList) throws ValidationException {
						String[] parameters = paraList.split(",");							
						ArrayList<String> para = new ArrayList<String>() ;
						for (int i = 0; i < parameters.length; i++)
							if (! parameters[i].trim().isEmpty())
								para.add(parameters[i]) ;
						if (para.size() == 0) {
							«pt.proctype.name»() ;							
						} else {
							//check number of parameters
							if (para.size() == «getNumberParameters(pt)») 								
								«genInitFunction(pt)»
							else //raise exception
								throw new ValidationException("Error init the process for scheduling: «pt.proctype.name»(" + paraList + ")") ;
						}
					}
				«ENDIF»
			«ENDFOR»
											
			public void initProcess(String pName, ArrayList<String> para){
				//call only with execute function				
				switch (pName) {
					«FOR pt: procModel.getProcess»
						case "«pt.proctype.name»" : 
							getRefID(pName) ;
							if (para == null) {
								«pt.proctype.name»() ;								
							} else {
								if (para.isEmpty()) {
									«pt.proctype.name»() ;
								} else {	
									//check number of parameters
									if (para.size() == «getNumberParameters(pt)») 								
										«genInitFunction(pt)»
									else
										System.out.println("Error init the process for scheduling") ;
								}
							}
							break ;
					«ENDFOR»
					«IF procModel.parent == null»
						default : break ;
					«ELSE»
						default : super.initProcess(pName, para) ; break ;
					«ENDIF»
				}
			}
			public void getRefID(String pName){
				if (processList.size() == 0) initProcessList() ;
				this.refID = processList.indexOf(pName);
			}			
			protected void initProcessList() {
				«IF procModel.parent != null»
					super.initProcessList() ;
				«ENDIF»
				«FOR pt: procModel.getProcess»
					if (!processList.contains("«pt.proctype.name»"))
						processList.add("«pt.proctype.name»") ;
				«ENDFOR»
			}		
		}		
	'''	
	
	static def int getNumberParameters(ProcessDef proc) {		
		if (proc.paralist == null)
			return 0 
		else
			return proc.paralist.para.size
	}
	
	static def String genInitFunction(ProcessDef proc) {
		var result = proc.proctype.name	+ '('
		if (proc.paralist != null)
			for (var i = 0; i < proc.paralist.para.size; i++) {
				result += 'StringUtil.convert2' + proc.paralist.para.get(i).type + '(para.get(' + i + '))'
				if (i < proc.paralist.para.size - 1)
					  result += ', '				
			}
		return result + ') ;'
	}
	
	var static ArrayList<String> sporadicProcessList = new ArrayList<String>()
	var static ArrayList<Integer> startDuration = new ArrayList<Integer>()
	var static ArrayList<Integer> endDuration = new ArrayList<Integer>()
	var static ArrayList<Integer> limited = new ArrayList<Integer>()
	var static int maxsporadictime = 0
	var static int minsporadictime = 0
	
	static def boolean getsporadicProcessList(ProcessDSL procModel ) {
		var result = false		
		sporadicProcessList.clear
		startDuration.clear
		endDuration.clear
		limited.clear
		maxsporadictime = 0
		minsporadictime = 0
		if (procModel.processconfig != null) {		
			for (proc : procModel.processconfig.procinit){ 
				if (proc.sporadic != null) {
					sporadicProcessList.add(getProcessInitFunction(proc.sporadic.element) )
					startDuration.add(proc.sporadic.start)
					
					if (proc.sporadic.start > minsporadictime)
						minsporadictime = proc.sporadic.start
					if (proc.sporadic.end > maxsporadictime)
						maxsporadictime = proc.sporadic.end
						
					endDuration.add(proc.sporadic.end)
					if (proc.sporadic.max <= 0)
						limited.add(1)
					else
						limited.add(proc.sporadic.max)
						
					result = true							
				}			
			}		
		}		
		return result					
	}
	
	static def SporadicProcesstoJavaCode(ProcessDSL procModel )
	'''
		«IF getsporadicProcessList(procModel)»		
			/*scheduler sporadic process -> set sporadicTime = «Data.sporadicTime = true» for realize the clock event*/			
			sporadic {						
				«var id = 0»
				int _time_sporadic ;				
				«FOR sp : sporadicProcessList»
					byte count_«sp.replace('*','').replace('(','_').replace(')','_').replace(',','_').replace(' ','')»_«id++» ;
				«ENDFOR»
				do
				«var idx = 0»
				«FOR sp : sporadicProcessList» 
					«val p = sp.replace('*','').replace('(','_').replace(')','_').replace(',','_').replace(' ','') »
					«val start = startDuration.get(idx)»
					«val end = endDuration.get(idx)»
					«val max = limited.get(idx)»					
					:: d_step { 
							(_time_sporadic >= «start» && _time_sporadic <= «end» && count_«p»_«idx» < «max») ->  
								sch_exec(«sp»); count_«p»_«idx++» ++ ; _time_sporadic ++
						}
				«ENDFOR»
				:: d_step {(_time_sporadic <= «maxsporadictime») -> _time_sporadic++ }
				:: d_step {(_time_sporadic > «maxsporadictime» ) -> skip }				
				od
			}
		«ELSE»
			/* no scheduler sporadic process -> set sporadicTime = «Data.sporadicTime = false» for realize the _time variable */	
		«ENDIF»		
	'''
		
	
	static def ArrayList<ArrayList<String>> powerSet(ArrayList<String> list) {
		var ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>()
    	result.add(new ArrayList<String>())

	    for (String i : list) {
	        var ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>()
			 
	        for (ArrayList<String> innerList : result) {
	            var ArrayList<String> innerList1 = new ArrayList<String>(innerList);
	            innerList1.add(i);
	            temp.add(innerList1);
	        }
	        result.addAll(temp);
	    }
	
	    return result;
	}
	
	
	static def String generateStartOption(ArrayList<String> list) {
		var result = ''	
		if (list.size() != 0) {
			result += ':: d_step { '	
			var i = 0 	
			for (variant : list) {
				result += '! start_' + variant.replace('*','').replace('(','_').replace(')','_').replace(',','_').replace(' ','') 
				if (i < list.size() - 1)
					result += ' && '
				i ++ 
			}
			
			result += ' -> '
			for (variant : list) {
				result += ' exec ' + variant.substring(0,variant.indexOf('*')) + '; '			
			}
			
			for (variant : list) {
				result += ' start_' + variant.replace('*','').replace('(','_').replace(')','_').replace(',','_').replace(' ','') +  ' = true; '
			}
			result += ' } '
		}
		
		return result
	}

	
	static def String getProcessInitFunction(Element element) {
		var result = element.process.name + '('
				
		if (element.paraAssign != null) {
			var i = 0 ;
			var Value value = null;
			for (i=0; i<element.paraAssign.size; i++){
				value = element.paraAssign.get(i) 
				if (value.num != null) {					
					result += value.num.value										
				} else
					result += value.bool.value
				if (i < element.paraAssign.size - 1)
					result += ', '	
			}			
		}							
		result += ')'
		return result 
	}	
	
	static	def String getProcessInitFunction(Element element, ProcessDSL procModel ) {
		var ProcessDef proc = null		
		for (p : procModel.getProcess)
			if (p.proctype.name.equals(element.process.name)) 
				proc = p			
			
		var result = element.process.name + '.' + element.process.name + '('
				
		if (element.paraAssign != null) {
			var i = 0 ;
			var Value value = null;
			for (i=0; i<element.paraAssign.size; i++){
				value = element.paraAssign.get(i) 
				if (value.num != null) {					
					result += '(' + proc.paralist.para.get(i).type + ') ' + value.node.tokenText //.num.value										
				} else
					result += value.bool.value
				if (i < element.paraAssign.size - 1)
					result += ', '	
			}			
		}							
		result += ') ;'
		return result 
	}
}					