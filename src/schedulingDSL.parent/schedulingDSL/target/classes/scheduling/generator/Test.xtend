package scheduling.generator

import scheduling.dsl.Configs
import scheduling.dsl.Model
import scheduling.dsl.Permutation
import scheduling.dsl.ProcessDSL
import scheduling.dsl.Rules
import scheduling.dsl.Scenarios

import scheduling.dsl.Verify
import scheduling.dsl.Specification
import scheduling.dsl.TestDSL

import static scheduling.generator.SchedulerTestGenerator.*
import scheduling.dsl.SchedulerDSL

class Test {
	
	public static Specification specification = null
	public static Configs configs = null
	public static Scenarios scenarios = null
	public static Permutation permutation= null
	public static Rules rules = null
	public static Verify verify = null
	public static Model model = null
	
	public static ProcessDSL procModel = null
	public static SchedulerDSL schModel = null
	
	static def  init(TestDSL test) {
		specification = test.specification
		configs = test.configs
		scenarios = test.scenarios		
		permutation = test.permutation
		
		rules = test.rule
		verify = test.verify
		SchedulerTestGenerator.testCount = 0		
	}	
}
		
		