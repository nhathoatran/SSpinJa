package scheduling.generator;

import scheduling.dsl.Configs;
import scheduling.dsl.Model;
import scheduling.dsl.Permutation;
import scheduling.dsl.ProcessDSL;
import scheduling.dsl.Rules;
import scheduling.dsl.Scenarios;
import scheduling.dsl.SchedulerDSL;
import scheduling.dsl.Specification;
import scheduling.dsl.TestDSL;
import scheduling.dsl.Verify;
import scheduling.generator.SchedulerTestGenerator;

@SuppressWarnings("all")
public class Test {
  public static Specification specification = null;
  
  public static Configs configs = null;
  
  public static Scenarios scenarios = null;
  
  public static Permutation permutation = null;
  
  public static Rules rules = null;
  
  public static Verify verify = null;
  
  public static Model model = null;
  
  public static ProcessDSL procModel = null;
  
  public static SchedulerDSL schModel = null;
  
  public static int init(final TestDSL test) {
    int _xblockexpression = (int) 0;
    {
      Specification _specification = test.getSpecification();
      Test.specification = _specification;
      Configs _configs = test.getConfigs();
      Test.configs = _configs;
      Scenarios _scenarios = test.getScenarios();
      Test.scenarios = _scenarios;
      Permutation _permutation = test.getPermutation();
      Test.permutation = _permutation;
      Rules _rule = test.getRule();
      Test.rules = _rule;
      Verify _verify = test.getVerify();
      Test.verify = _verify;
      _xblockexpression = SchedulerTestGenerator.testCount = 0;
    }
    return _xblockexpression;
  }
}
