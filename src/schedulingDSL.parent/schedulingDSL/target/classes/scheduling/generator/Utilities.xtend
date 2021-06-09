package scheduling.generator

import org.eclipse.xtext.generator.IFileSystemAccess2
import org.eclipse.emf.common.util.EList
import scheduling.dsl.ParameterAssign

 
class Utilities {
	static def deleteGeneratedFiles(IFileSystemAccess2 fsa){
		if (fsa.isFile("../src/sspinja/verifier/Verifier.java")) {
			fsa.deleteFile("../src/sspinja/verifier/Verifier.java")		
		}
		if (fsa.isFile("../src/sspinja/simulator/Simulator.java")) {
			fsa.deleteFile("../src/sspinja/simulator/Simulator.java")		
		}
		
		if (fsa.isFile("../src/sspinja/scheduling/SchedulerObject.java")) {
			fsa.deleteFile("../src/sspinja/scheduling/SchedulerObject.java")		
		}
		if (fsa.isFile("../src/sspinja/scheduling/chedulerProcess.java")) {
			fsa.deleteFile("../src/sspinja/scheduling/SchedulerProcess.java")			
		}
		
		if (fsa.isFile("../src/file.dat")) {
			var CharSequence cs = fsa.readTextFile("../src/file.dat") 
			var String[] fileNames = cs.toString.split(",");             
            
			for (f : fileNames) {
				val fn = "../src/sspinja/scheduling/" + f.trim
				if (fsa.isFile(fn)) {
					fsa.deleteFile(fn)	
					System.out.println("delete: " + fn)
				}	
			}
		} 
	}
	
	static def String setInitValue(String varSet, String value) {	
		if (value.trim == '')	
			varSet.trim() + ' ; '
		else
			varSet.trim().replace(', ' , ' = ' + value + ', ' ) + ' = ' + value + ' ; '			
	}
	
	static def String initValue(String varSet, String value) {	
		if (value.trim != '')
			varSet.trim().replace(', ' , ' = ' + value + '; ' ) + ' = ' + value + ' ; '			
	}
		
	static def String findValueInParameterList(String variable, EList<ParameterAssign> paralist) {
		var String x = ''		
		for (para : paralist) {			
			for (pname : para.paraname) {
//				x += 'name:' + pname.name + ', var:' + variable + ';' + para.^val.bool  + para.^val.num.value + ';'
				if (pname.name.trim().equals(variable.trim()))					
					if (para.getVal().num != null)
						return para.getVal().num.value.toString()
					else
						return para.getVal().bool.value
			}
		}
		return x
	}
}