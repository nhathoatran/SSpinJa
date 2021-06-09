package scheduling.generator;

import com.google.common.base.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import scheduling.dsl.Behavior;
import scheduling.dsl.Component;
import scheduling.dsl.Directory;
import scheduling.dsl.EventTemplate;
import scheduling.dsl.Expression;
import scheduling.dsl.FileExtension;
import scheduling.dsl.FileName;
import scheduling.dsl.GenCodeStatement;
import scheduling.dsl.GenComponent;
import scheduling.dsl.GenConfiguration;
import scheduling.dsl.GenLnCodeStatement;
import scheduling.dsl.GenPart;
import scheduling.dsl.Generate;
import scheduling.dsl.GenerateOption;
import scheduling.dsl.ND_Behavior;
import scheduling.dsl.ProcessGeneration;
import scheduling.dsl.SchedulerDSL;
import scheduling.dsl.SchedulerDef;
import scheduling.dsl.SetTemplate;
import scheduling.dsl.Statement;
import scheduling.dsl.StepGeneration;
import scheduling.dsl.Template;
import scheduling.dsl.TestPart;
import scheduling.generator.Statements;

@SuppressWarnings("all")
public class GenerationGenerator {
  private static int compsize = (-1);
  
  private static int templatesize = (-1);
  
  private static int errorindex = (-1);
  
  private static int stepORprocessIndex = (-1);
  
  public static int initIndexValue(final SchedulerDSL schModel) {
    int _xblockexpression = (int) 0;
    {
      GenerationGenerator.compsize = 0;
      GenerationGenerator.templatesize = 0;
      GenerationGenerator.errorindex = (-1);
      GenerationGenerator.stepORprocessIndex = (-1);
      int _xifexpression = (int) 0;
      SchedulerDef _scheduler = schModel.getScheduler();
      Generate _gen = _scheduler.getGen();
      boolean _notEquals = (!Objects.equal(_gen, null));
      if (_notEquals) {
        int _xblockexpression_1 = (int) 0;
        {
          SchedulerDef _scheduler_1 = schModel.getScheduler();
          Generate _gen_1 = _scheduler_1.getGen();
          GenConfiguration _genconfiguration = _gen_1.getGenconfiguration();
          TestPart _testpart = _genconfiguration.getTestpart();
          EList<GenPart> _part = _testpart.getPart();
          for (final GenPart component : _part) {
            {
              if ((component.getName().equals("processes") || component.getName().equals("steps"))) {
                GenerationGenerator.stepORprocessIndex = GenerationGenerator.templatesize;
              } else {
                String _name = component.getName();
                boolean _equals = _name.equals("error");
                if (_equals) {
                  GenerationGenerator.errorindex = GenerationGenerator.templatesize;
                }
              }
              GenerationGenerator.templatesize++;
            }
          }
          GenerationGenerator.compsize = GenerationGenerator.templatesize;
          if ((GenerationGenerator.errorindex >= 0)) {
            GenerationGenerator.compsize--;
          }
          int _xifexpression_1 = (int) 0;
          if ((GenerationGenerator.stepORprocessIndex >= 0)) {
            _xifexpression_1 = GenerationGenerator.compsize--;
          }
          _xblockexpression_1 = _xifexpression_1;
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public static CharSequence GenerationtoJavaCode(final SchedulerDSL schModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package sspinja.scheduling;\t\t");
    _builder.newLine();
    _builder.append("import spinja.util.StringWriter;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("//Automatic generation");
    _builder.newLine();
    _builder.append("public class Generate {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static StringWriter out = new StringWriter();");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("private static boolean searching, error, property, ndbehavior;");
    _builder.newLine();
    {
      if ((GenerationGenerator.templatesize > 0)) {
        _builder.append("\t");
        _builder.append("public static String tempParts[][] = new String[");
        _builder.append(GenerationGenerator.templatesize, "\t");
        _builder.append("][2];");
        _builder.newLineIfNotEmpty();
        {
          if ((GenerationGenerator.compsize > 0)) {
            _builder.append("\t");
            _builder.append("public static String component[] = new String[");
            _builder.append(GenerationGenerator.compsize, "\t");
            _builder.append("];");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("public static String component[] = new String[1];");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("public static String errorMsg = \"\" ;");
        _builder.newLine();
      }
    }
    _builder.append("\t\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static boolean getSearchingGenerateOption() { return searching ; }");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static boolean getErrorGenerateOption() { return error ; }");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static boolean getPropertyGenerateOption() { return property ;}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static boolean getNDBehaviorGenerateOption() { return ndbehavior ;}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static boolean isGenTestCase() {");
    _builder.newLine();
    {
      SchedulerDef _scheduler = schModel.getScheduler();
      Generate _gen = _scheduler.getGen();
      GenConfiguration _genconfiguration = _gen.getGenconfiguration();
      String _test = _genconfiguration.getTest();
      boolean _equals = _test.equals("case");
      if (_equals) {
        _builder.append("\t\t");
        _builder.append("return true;");
        _builder.newLine();
      } else {
        _builder.append("\t\t");
        _builder.append("return false ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static boolean isGenTestProgram() {");
    _builder.newLine();
    {
      SchedulerDef _scheduler_1 = schModel.getScheduler();
      Generate _gen_1 = _scheduler_1.getGen();
      GenConfiguration _genconfiguration_1 = _gen_1.getGenconfiguration();
      String _test_1 = _genconfiguration_1.getTest();
      boolean _equals_1 = _test_1.equals("program");
      if (_equals_1) {
        _builder.append("\t\t");
        _builder.append("return true;");
        _builder.newLine();
      } else {
        _builder.append("\t\t");
        _builder.append("return false ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static boolean isGenTestData() {");
    _builder.newLine();
    {
      SchedulerDef _scheduler_2 = schModel.getScheduler();
      Generate _gen_2 = _scheduler_2.getGen();
      GenConfiguration _genconfiguration_2 = _gen_2.getGenconfiguration();
      String _test_2 = _genconfiguration_2.getTest();
      boolean _equals_2 = _test_2.equals("data");
      if (_equals_2) {
        _builder.append("\t\t");
        _builder.append("return true;");
        _builder.newLine();
      } else {
        _builder.append("\t\t");
        _builder.append("return false ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static void initGeneration() {");
    _builder.newLine();
    {
      if ((GenerationGenerator.compsize > 0)) {
        _builder.append("\t\t");
        _builder.append("for (int i=0; i<");
        _builder.append(GenerationGenerator.compsize, "\t\t");
        _builder.append("; i++)");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("component[i] = \"\";");
        _builder.newLine();
      }
    }
    _builder.append("\t\t");
    _builder.append("setGenerateOption() ;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("initComponent() ;");
    _builder.newLine();
    {
      if ((GenerationGenerator.templatesize > 0)) {
        _builder.append("\t\t");
        _builder.append("//prefix & postfix");
        _builder.newLine();
        {
          ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, GenerationGenerator.templatesize, true);
          for(final Integer i : _doubleDotLessThan) {
            {
              SchedulerDef _scheduler_3 = schModel.getScheduler();
              Generate _gen_3 = _scheduler_3.getGen();
              GenConfiguration _genconfiguration_3 = _gen_3.getGenconfiguration();
              TestPart _testpart = _genconfiguration_3.getTestpart();
              EList<GenPart> _part = _testpart.getPart();
              GenPart _get = _part.get((i).intValue());
              String _prefix = _get.getPrefix();
              boolean _notEquals = (!Objects.equal(_prefix, null));
              if (_notEquals) {
                _builder.append("\t\t");
                _builder.append("tempParts[");
                _builder.append(i, "\t\t");
                _builder.append("][0] = ");
                SchedulerDef _scheduler_4 = schModel.getScheduler();
                Generate _gen_4 = _scheduler_4.getGen();
                GenConfiguration _genconfiguration_4 = _gen_4.getGenconfiguration();
                TestPart _testpart_1 = _genconfiguration_4.getTestpart();
                EList<GenPart> _part_1 = _testpart_1.getPart();
                GenPart _get_1 = _part_1.get((i).intValue());
                String _prefix_1 = _get_1.getPrefix();
                _builder.append(_prefix_1, "\t\t");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t\t");
                _builder.append("tempParts[");
                _builder.append(i, "\t\t");
                _builder.append("][0] = \"\" ;");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              SchedulerDef _scheduler_5 = schModel.getScheduler();
              Generate _gen_5 = _scheduler_5.getGen();
              GenConfiguration _genconfiguration_5 = _gen_5.getGenconfiguration();
              TestPart _testpart_2 = _genconfiguration_5.getTestpart();
              EList<GenPart> _part_2 = _testpart_2.getPart();
              GenPart _get_2 = _part_2.get((i).intValue());
              String _posfix = _get_2.getPosfix();
              boolean _notEquals_1 = (!Objects.equal(_posfix, null));
              if (_notEquals_1) {
                _builder.append("\t\t");
                _builder.append("tempParts[");
                _builder.append(i, "\t\t");
                _builder.append("][1] = ");
                SchedulerDef _scheduler_6 = schModel.getScheduler();
                Generate _gen_6 = _scheduler_6.getGen();
                GenConfiguration _genconfiguration_6 = _gen_6.getGenconfiguration();
                TestPart _testpart_3 = _genconfiguration_6.getTestpart();
                EList<GenPart> _part_3 = _testpart_3.getPart();
                GenPart _get_3 = _part_3.get((i).intValue());
                String _posfix_1 = _get_3.getPosfix();
                _builder.append(_posfix_1, "\t\t");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t\t");
                _builder.append("tempParts[");
                _builder.append(i, "\t\t");
                _builder.append("][1] = \"\" ;");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static void setErrorMsg(String msg){");
    _builder.newLine();
    {
      if ((GenerationGenerator.errorindex >= 0)) {
        _builder.append("\t\t");
        _builder.append("errorMsg = msg;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static String getErrorMsg(){");
    _builder.newLine();
    {
      if ((GenerationGenerator.errorindex >= 0)) {
        _builder.append("\t\t");
        _builder.append("return tempParts[");
        _builder.append(GenerationGenerator.errorindex, "\t\t");
        _builder.append("][0];");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t\t");
        _builder.append("return \"\" ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    {
      SchedulerDef _scheduler_7 = schModel.getScheduler();
      Generate _gen_7 = _scheduler_7.getGen();
      boolean _notEquals_2 = (!Objects.equal(_gen_7, null));
      if (_notEquals_2) {
        _builder.append("\t");
        SchedulerDef _scheduler_8 = schModel.getScheduler();
        Generate _gen_8 = _scheduler_8.getGen();
        GenConfiguration _genconfiguration_7 = _gen_8.getGenconfiguration();
        CharSequence _generateConfiguration = GenerationGenerator.generateConfiguration(_genconfiguration_7);
        _builder.append(_generateConfiguration, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        SchedulerDef _scheduler_9 = schModel.getScheduler();
        Generate _gen_9 = _scheduler_9.getGen();
        GenComponent _gencomponent = _gen_9.getGencomponent();
        CharSequence _genAddCodetoComponent = GenerationGenerator.genAddCodetoComponent(_gencomponent);
        _builder.append(_genAddCodetoComponent, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        SchedulerDef _scheduler_10 = schModel.getScheduler();
        Generate _gen_10 = _scheduler_10.getGen();
        GenComponent _gencomponent_1 = _gen_10.getGencomponent();
        CharSequence _GenComponent = GenerationGenerator.GenComponent(_gencomponent_1);
        _builder.append(_GenComponent, "\t");
        _builder.newLineIfNotEmpty();
        {
          SchedulerDef _scheduler_11 = schModel.getScheduler();
          Generate _gen_11 = _scheduler_11.getGen();
          StepGeneration _stepgeneration = _gen_11.getStepgeneration();
          boolean _notEquals_3 = (!Objects.equal(_stepgeneration, null));
          if (_notEquals_3) {
            _builder.append("\t");
            SchedulerDef _scheduler_12 = schModel.getScheduler();
            Generate _gen_12 = _scheduler_12.getGen();
            StepGeneration _stepgeneration_1 = _gen_12.getStepgeneration();
            CharSequence _GenStepComponent = GenerationGenerator.GenStepComponent(_stepgeneration_1);
            _builder.append(_GenStepComponent, "\t");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            SchedulerDef _scheduler_13 = schModel.getScheduler();
            Generate _gen_13 = _scheduler_13.getGen();
            ProcessGeneration _processgeneration = _gen_13.getProcessgeneration();
            CharSequence _GenProcessComponent = GenerationGenerator.GenProcessComponent(_processgeneration);
            _builder.append(_GenProcessComponent, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("public static void setGenerateOption(){}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public static String getGenerateDirectory(){ return \"Gen\"; }");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public static String getGenerateFileName(){ return \"gen\"; }");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public static String getGenerateFileExtension(){ return \"gen\"; }");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public static void addCode(String code, String component, boolean breakline) {}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public static void initComponent() {}\t\t\t\t\t\t");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("public static int getTemplateSize(){");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return ");
    _builder.append(GenerationGenerator.templatesize, "\t\t");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static int getCompSize(){");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return ");
    _builder.append(GenerationGenerator.compsize, "\t\t");
    _builder.append(";");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static int getErrorPosition(){");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return ");
    _builder.append(GenerationGenerator.errorindex, "\t\t");
    _builder.append(" ;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static int getActionsPosition(){");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return ");
    _builder.append(GenerationGenerator.stepORprocessIndex, "\t\t");
    _builder.append(" ;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}\t\t\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence generateConfiguration(final GenConfiguration genconfiguration) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("\t");
    _builder.append("public static void setGenerateOption(){");
    _builder.newLine();
    {
      GenerateOption _generateoption = genconfiguration.getGenerateoption();
      boolean _notEquals = (!Objects.equal(_generateoption, null));
      if (_notEquals) {
        {
          GenerateOption _generateoption_1 = genconfiguration.getGenerateoption();
          EList<scheduling.dsl.String> _option = _generateoption_1.getOption();
          boolean _notEquals_1 = (!Objects.equal(_option, null));
          if (_notEquals_1) {
            {
              GenerateOption _generateoption_2 = genconfiguration.getGenerateoption();
              EList<scheduling.dsl.String> _option_1 = _generateoption_2.getOption();
              for(final scheduling.dsl.String opt : _option_1) {
                {
                  String _name = opt.getName();
                  boolean _equals = _name.equals("searching");
                  if (_equals) {
                    _builder.append("\t\t");
                    _builder.append("searching = true ;");
                    _builder.newLine();
                  }
                }
                {
                  String _name_1 = opt.getName();
                  boolean _equals_1 = _name_1.equals("error");
                  if (_equals_1) {
                    _builder.append("\t\t");
                    _builder.append("error = true ;");
                    _builder.newLine();
                  }
                }
                {
                  String _name_2 = opt.getName();
                  boolean _equals_2 = _name_2.equals("property");
                  if (_equals_2) {
                    _builder.append("\t\t");
                    _builder.append("property = true ;");
                    _builder.newLine();
                  }
                }
                {
                  String _name_3 = opt.getName();
                  boolean _equals_3 = _name_3.equals("ndbehavior");
                  if (_equals_3) {
                    _builder.append("\t\t");
                    _builder.append("ndbehavior = true ;");
                    _builder.newLine();
                  }
                }
                {
                  String _name_4 = opt.getName();
                  boolean _equals_4 = _name_4.equals("all");
                  if (_equals_4) {
                    _builder.append("\t\t");
                    _builder.append("searching = true ;");
                    _builder.newLine();
                    _builder.append("\t\t");
                    _builder.append("error = true ;");
                    _builder.newLine();
                    _builder.append("\t\t");
                    _builder.append("property = true ;");
                    _builder.newLine();
                    _builder.append("\t\t");
                    _builder.append("ndbehavior = true ;");
                    _builder.newLine();
                  }
                }
              }
            }
          }
        }
      } else {
        _builder.append("\t\t");
        _builder.append("searching = true ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("error = true ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("property = true ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}\t");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static String getGenerateDirectory(){");
    _builder.newLine();
    {
      Directory _directory = genconfiguration.getDirectory();
      boolean _notEquals_2 = (!Objects.equal(_directory, null));
      if (_notEquals_2) {
        _builder.append("\t\t");
        _builder.append("return ");
        Directory _directory_1 = genconfiguration.getDirectory();
        String _name_5 = _directory_1.getName();
        _builder.append(_name_5, "\t\t");
        _builder.append(" ;");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t\t");
        _builder.append("return \"Gen\" ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static String getGenerateFileName(){");
    _builder.newLine();
    {
      FileName _filename = genconfiguration.getFilename();
      boolean _notEquals_3 = (!Objects.equal(_filename, null));
      if (_notEquals_3) {
        _builder.append("\t\t");
        _builder.append("return ");
        FileName _filename_1 = genconfiguration.getFilename();
        String _name_6 = _filename_1.getName();
        _builder.append(_name_6, "\t\t");
        _builder.append(" ;");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t\t");
        _builder.append("return \"Gen\" ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static String getGenerateFileExtension(){");
    _builder.newLine();
    {
      FileExtension _fileextension = genconfiguration.getFileextension();
      boolean _notEquals_4 = (!Objects.equal(_fileextension, null));
      if (_notEquals_4) {
        _builder.append("\t\t");
        _builder.append("return ");
        FileExtension _fileextension_1 = genconfiguration.getFileextension();
        String _name_7 = _fileextension_1.getName();
        _builder.append(_name_7, "\t\t");
        _builder.append(" ;");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t\t");
        _builder.append("return \"gen\" ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genAddCodetoComponent(final GenComponent gencomponent) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _notEquals = (!Objects.equal(gencomponent, null));
      if (_notEquals) {
        _builder.append("public static void genCodeForComp(String code, String _component, boolean breakline) {");
        _builder.newLine();
        {
          if ((GenerationGenerator.compsize > 0)) {
            _builder.append("\t");
            _builder.append("int index = -1;");
            _builder.newLine();
            _builder.append("\t");
            int index = 0;
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("switch (_component) {\t\t\t\t");
            _builder.newLine();
            {
              EList<Component> _component = gencomponent.getComponent();
              for(final Component comp : _component) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("case \"");
                String _name = comp.getName();
                _builder.append(_name, "\t\t");
                _builder.append("\":");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("index = ");
                int _plusPlus = index++;
                _builder.append(_plusPlus, "\t\t\t");
                _builder.append("; break ;");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("}\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("if (index >=0 && index <= ");
            _builder.append(GenerationGenerator.compsize, "\t");
            _builder.append(") {\t\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("component[index] += breakline? code + \"\\n\" : code ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
        _builder.append("}");
        _builder.newLine();
        _builder.append("public static void initComponent() {");
        _builder.newLine();
        {
          EList<Component> _component_1 = gencomponent.getComponent();
          for(final Component component : _component_1) {
            {
              EList<Statement> _stm = component.getStm();
              for(final Statement stm : _stm) {
                {
                  if ((stm instanceof GenCodeStatement)) {
                    {
                      String _comp = ((GenCodeStatement)stm).getComp();
                      boolean _notEquals_1 = (!Objects.equal(_comp, null));
                      if (_notEquals_1) {
                        _builder.append("\t");
                        _builder.append("genCodeForComp(");
                        Expression _st = ((GenCodeStatement)stm).getSt();
                        String _dispatchExpression = Statements.dispatchExpression(_st);
                        _builder.append(_dispatchExpression, "\t");
                        _builder.append(", \"");
                        String _comp_1 = ((GenCodeStatement)stm).getComp();
                        _builder.append(_comp_1, "\t");
                        _builder.append("\", false);");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        _builder.append("genCodeForComp(");
                        Expression _st_1 = ((GenCodeStatement)stm).getSt();
                        String _dispatchExpression_1 = Statements.dispatchExpression(_st_1);
                        _builder.append(_dispatchExpression_1, "\t");
                        _builder.append(", \"");
                        String _name_1 = component.getName();
                        _builder.append(_name_1, "\t");
                        _builder.append("\", false);");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                  }
                }
                {
                  if ((stm instanceof GenLnCodeStatement)) {
                    {
                      String _comp_2 = ((GenLnCodeStatement)stm).getComp();
                      boolean _notEquals_2 = (!Objects.equal(_comp_2, null));
                      if (_notEquals_2) {
                        _builder.append("\t");
                        _builder.append("genCodeForComp(");
                        Expression _st_2 = ((GenLnCodeStatement)stm).getSt();
                        String _dispatchExpression_2 = Statements.dispatchExpression(_st_2);
                        _builder.append(_dispatchExpression_2, "\t");
                        _builder.append(", \"");
                        String _comp_3 = ((GenLnCodeStatement)stm).getComp();
                        _builder.append(_comp_3, "\t");
                        _builder.append("\", true);");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        _builder.append("genCodeForComp(");
                        Expression _st_3 = ((GenLnCodeStatement)stm).getSt();
                        String _dispatchExpression_3 = Statements.dispatchExpression(_st_3);
                        _builder.append(_dispatchExpression_3, "\t");
                        _builder.append(", \"");
                        String _name_2 = component.getName();
                        _builder.append(_name_2, "\t");
                        _builder.append("\", true);");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                  }
                }
              }
            }
          }
        }
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append("public static void genCodeForComp(String code, String _component, boolean breakline) { }");
        _builder.newLine();
        _builder.append("public static void initComponent() {}");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public static CharSequence GenStepComponent(final StepGeneration stepgeneration) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public static void setBehavior(GenerateCode _generatecode){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//following behavior template");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("_generatecode.gencode.code = \"\";");
    _builder.newLine();
    _builder.append("\t");
    Template _step = stepgeneration.getStep();
    Behavior _behavior = _step.getBehavior();
    EList<EventTemplate> _eventtemplate = _behavior.getEventtemplate();
    CharSequence _GenTemplate = GenerationGenerator.GenTemplate(_eventtemplate);
    _builder.append(_GenTemplate, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.append("public static void setNDBehavior(GenerateCode _generatecode){");
    _builder.newLine();
    {
      Template _step_1 = stepgeneration.getStep();
      ND_Behavior _nD_behavior = _step_1.getND_behavior();
      boolean _notEquals = (!Objects.equal(_nD_behavior, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("//following ND behavior template");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("_generatecode.gencode.code = \"\";");
        _builder.newLine();
        _builder.append("\t");
        Template _step_2 = stepgeneration.getStep();
        ND_Behavior _nD_behavior_1 = _step_2.getND_behavior();
        EList<EventTemplate> _eventtemplate_1 = _nD_behavior_1.getEventtemplate();
        CharSequence _GenTemplate_1 = GenerationGenerator.GenTemplate(_eventtemplate_1);
        _builder.append(_GenTemplate_1, "\t");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t");
        _builder.append("//setBehavior(_generatecode);");
        _builder.newLine();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.append("public static String generateProcess(int PID, int InstanceID, String PName, String Behaviors){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return Behaviors;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence GenTemplate(final EList<EventTemplate> eventtemplate) {
    StringConcatenation _builder = new StringConcatenation();
    {
      for(final EventTemplate e : eventtemplate) {
        {
          Expression _prefix = e.getPrefix();
          boolean _notEquals = (!Objects.equal(_prefix, null));
          if (_notEquals) {
            {
              Expression _postfix = e.getPostfix();
              boolean _notEquals_1 = (!Objects.equal(_postfix, null));
              if (_notEquals_1) {
                _builder.append("_generatecode.gencode.code += ");
                _builder.newLine();
                _builder.append("\t");
                Expression _prefix_1 = e.getPrefix();
                String _dispatchExpression = Statements.dispatchExpression(_prefix_1);
                String _replace = _dispatchExpression.replace("<InstanceID>", "\"<InstanceID>\"");
                String _replace_1 = _replace.replace("<PID>", "\"<PID>\"");
                String _replace_2 = _replace_1.replace("<PName>", "\"<PName>\"");
                String _replace_3 = _replace_2.replace("Integer.parseInt", "");
                _builder.append(_replace_3, "\t");
                _builder.append(" + ");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("_generatecode.gencode.");
                scheduling.dsl.String _event = e.getEvent();
                String _string = _event.toString();
                String _replace_4 = _string.replace("<", "");
                String _replace_5 = _replace_4.replace(">", "");
                _builder.append(_replace_5, "\t");
                _builder.append(" + ");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                Expression _postfix_1 = e.getPostfix();
                String _dispatchExpression_1 = Statements.dispatchExpression(_postfix_1);
                String _replace_6 = _dispatchExpression_1.replace("<InstanceID>", "\"<InstanceID>\"");
                String _replace_7 = _replace_6.replace("<PID>", "\"<PID>\"");
                String _replace_8 = _replace_7.replace("<PName>", "\"<PName>\"");
                String _replace_9 = _replace_8.replace("Integer.parseInt", "");
                _builder.append(_replace_9, "\t");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("_generatecode.gencode.code += ");
                _builder.newLine();
                _builder.append("\t");
                Expression _prefix_2 = e.getPrefix();
                String _dispatchExpression_2 = Statements.dispatchExpression(_prefix_2);
                String _replace_10 = _dispatchExpression_2.replace("<InstanceID>", "\"<InstanceID>\"");
                String _replace_11 = _replace_10.replace("<PID>", "\"<PID>\"");
                String _replace_12 = _replace_11.replace("<PName>", "\"<PName>\"");
                String _replace_13 = _replace_12.replace("Integer.parseInt", "");
                _builder.append(_replace_13, "\t");
                _builder.append(" + ");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("_generatecode.gencode.");
                scheduling.dsl.String _event_1 = e.getEvent();
                String _string_1 = _event_1.toString();
                String _replace_14 = _string_1.replace("<", "");
                String _replace_15 = _replace_14.replace(">", "");
                _builder.append(_replace_15, "\t");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
              }
            }
          } else {
            {
              Expression _postfix_2 = e.getPostfix();
              boolean _notEquals_2 = (!Objects.equal(_postfix_2, null));
              if (_notEquals_2) {
                _builder.append("_generatecode.gencode.code += ");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("_generatecode.gencode.");
                scheduling.dsl.String _event_2 = e.getEvent();
                String _string_2 = _event_2.toString();
                String _replace_16 = _string_2.replace("<", "");
                String _replace_17 = _replace_16.replace(">", "");
                _builder.append(_replace_17, "\t");
                _builder.append(" + ");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                Expression _postfix_3 = e.getPostfix();
                String _dispatchExpression_3 = Statements.dispatchExpression(_postfix_3);
                String _replace_18 = _dispatchExpression_3.replace("<InstanceID>", "\"<InstanceID>\"");
                String _replace_19 = _replace_18.replace("<PID>", "\"<PID>\"");
                String _replace_20 = _replace_19.replace("<PName>", "\"<PName>\"");
                String _replace_21 = _replace_20.replace("Integer.parseInt", "");
                _builder.append(_replace_21, "\t");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("_generatecode.gencode.code += _generatecode.gencode.");
                scheduling.dsl.String _event_3 = e.getEvent();
                String _string_3 = _event_3.toString();
                String _replace_22 = _string_3.replace("<", "");
                String _replace_23 = _replace_22.replace(">", "");
                _builder.append(_replace_23, "");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    return _builder;
  }
  
  public static CharSequence GenProcessComponent(final ProcessGeneration processgeneration) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public static void setBehavior(GenerateCode _generatecode){");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//following behavior template");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("_generatecode.gencode.code = \"\";");
    _builder.newLine();
    _builder.append("\t");
    Template _process = processgeneration.getProcess();
    Behavior _behavior = _process.getBehavior();
    EList<EventTemplate> _eventtemplate = _behavior.getEventtemplate();
    CharSequence _GenTemplate = GenerationGenerator.GenTemplate(_eventtemplate);
    _builder.append(_GenTemplate, "\t");
    _builder.append("\t\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.append("public static void setNDBehavior(GenerateCode _generatecode){");
    _builder.newLine();
    {
      Template _process_1 = processgeneration.getProcess();
      ND_Behavior _nD_behavior = _process_1.getND_behavior();
      boolean _notEquals = (!Objects.equal(_nD_behavior, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("//following ND behavior template");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("_generatecode.gencode.code = \"\";");
        _builder.newLine();
        _builder.append("\t");
        Template _process_2 = processgeneration.getProcess();
        ND_Behavior _nD_behavior_1 = _process_2.getND_behavior();
        EList<EventTemplate> _eventtemplate_1 = _nD_behavior_1.getEventtemplate();
        CharSequence _GenTemplate_1 = GenerationGenerator.GenTemplate(_eventtemplate_1);
        _builder.append(_GenTemplate_1, "\t");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t");
        _builder.append("//setBehavior(_generatecode);");
        _builder.newLine();
      }
    }
    _builder.append("}");
    _builder.newLine();
    {
      Template _process_3 = processgeneration.getProcess();
      SetTemplate _template = _process_3.getTemplate();
      boolean _notEquals_1 = (!Objects.equal(_template, null));
      if (_notEquals_1) {
        _builder.append("public static String generateProcess(int PID, int InstanceID, String PName, String actions){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return ");
        Template _process_4 = processgeneration.getProcess();
        SetTemplate _template_1 = _process_4.getTemplate();
        Expression _templ = _template_1.getTempl();
        String _dispatchExpression = Statements.dispatchExpression(_templ);
        String _replace = _dispatchExpression.replace("<InstanceID>", "InstanceID");
        String _replace_1 = _replace.replace("<PID>", "PID");
        String _replace_2 = _replace_1.replace("<PName>", "PName");
        String _replace_3 = _replace_2.replace("<actions>", "actions");
        String _replace_4 = _replace_3.replace("Integer.parseInt", "");
        _builder.append(_replace_4, "\t");
        _builder.append(" ;");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append("public static String generateProcess(int PID, int InstanceID, String PName, String actions){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return actions;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public static CharSequence GenComponent(final GenComponent gencomp) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public static String generateComponent(String contains, int index){");
    _builder.newLine();
    {
      boolean _notEquals = (!Objects.equal(gencomp, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("switch (index) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        int index = 0;
        _builder.newLineIfNotEmpty();
        {
          EList<Component> _component = gencomp.getComponent();
          for(final Component component : _component) {
            {
              SetTemplate _template = component.getTemplate();
              boolean _notEquals_1 = (!Objects.equal(_template, null));
              if (_notEquals_1) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("case ");
                int _plusPlus = index++;
                _builder.append(_plusPlus, "\t\t");
                _builder.append(" :");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("return ");
                SetTemplate _template_1 = component.getTemplate();
                Expression _templ = _template_1.getTempl();
                String _dispatchExpression = Statements.dispatchExpression(_templ);
                String _replace = _dispatchExpression.replace("<contains>", "contains");
                _builder.append(_replace, "\t\t\t");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("return contains ;");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence GenerateCodetoJavaCode(final SchedulerDSL schModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package sspinja.scheduling;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("//Automatic generation");
    _builder.newLine();
    _builder.append("public class Code {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String clock = \"\" ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String select_process =\"\";\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String new_process =\"\";");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String pre_take =\"\";");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String post_take =\"\";");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String process_action =\"\";");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String scheduling_action =\"\";\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String code =\"\";");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      SchedulerDef _scheduler = schModel.getScheduler();
      Generate _gen = _scheduler.getGen();
      boolean _notEquals = (!Objects.equal(_gen, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("public String component[] = new String[");
        _builder.append(GenerationGenerator.compsize, "\t");
        _builder.append("];");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("public static String s_component[] = new String[");
        _builder.append(GenerationGenerator.compsize, "\t");
        _builder.append("];\t\t\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void clearCode() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("clock = \"\" ;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("select_process = \"\";\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("new_process = \"\";");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("pre_take = \"\";");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("post_take = \"\";");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("process_action = \"\";");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("scheduling_action = \"\";");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("code = \"\" ;");
    _builder.newLine();
    {
      SchedulerDef _scheduler_1 = schModel.getScheduler();
      Generate _gen_1 = _scheduler_1.getGen();
      boolean _notEquals_1 = (!Objects.equal(_gen_1, null));
      if (_notEquals_1) {
        _builder.append("\t\t");
        _builder.append("for (int i = 0 ; i < ");
        _builder.append(GenerationGenerator.compsize, "\t\t");
        _builder.append("; i++)");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("component[i] = \"\";");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void clearcode(String _component) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch (_component) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"clock\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("clock = \"\" ; break ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"select_process\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("select_process = \"\"; break ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"new_process\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("new_process = \"\" ; break ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"pre_take\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("pre_take = \"\"; break ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"post_take\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("post_take = \"\" ; break ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"process_action\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("process_action = \"\"; break ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"scheduling_action\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("scheduling_action = \"\"; break ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"code\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("code = \"\"; break ;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void setcode(String _component, String code) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch (_component) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"clock\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("clock = code ; break ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"select_process\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("select_process = code ; break ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"new_process\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("new_process = code ; break ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"pre_take\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("pre_take = code ; break ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"post_take\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("post_take = code ; break ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"process_action\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("process_action = code; break ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("case \"scheduling_action\" :");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("scheduling_action = code; break ;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    {
      SchedulerDef _scheduler_2 = schModel.getScheduler();
      Generate _gen_2 = _scheduler_2.getGen();
      boolean _notEquals_2 = (!Objects.equal(_gen_2, null));
      if (_notEquals_2) {
        _builder.append("\t\t");
        _builder.append("for (int i = 0; i < ");
        _builder.append(GenerationGenerator.compsize, "\t\t");
        _builder.append("; i++) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("if (s_component[i] != null) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t\t");
        _builder.append("if (component[i] != null) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t\t\t");
        _builder.append("component[i] += s_component[i];");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t\t");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t\t\t");
        _builder.append("component[i] = s_component[i] + \"\";");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("s_component[i] = \"\" ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("protected Code clone() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("Code newcode = new Code() ;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("newcode.clock = clock;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("newcode.select_process = select_process;\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("newcode.new_process = new_process;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("newcode.pre_take = pre_take;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("newcode.post_take = post_take;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("newcode.process_action  = process_action;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("newcode.scheduling_action  = scheduling_action;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("newcode.code = code ;");
    _builder.newLine();
    {
      SchedulerDef _scheduler_3 = schModel.getScheduler();
      Generate _gen_3 = _scheduler_3.getGen();
      boolean _notEquals_3 = (!Objects.equal(_gen_3, null));
      if (_notEquals_3) {
        _builder.append("\t\t");
        _builder.append("for (int i=0; i< ");
        _builder.append(GenerationGenerator.compsize, "\t\t");
        _builder.append("; i++){\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("if (component[i] == null) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t\t");
        _builder.append("newcode.component[i] = \"\";");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t\t");
        _builder.append("newcode.component[i] = component[i] + \"\" ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t\t");
    _builder.append("return newcode ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static void addCodetoComponent(String code, String component, boolean breakline) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("int index = -1;");
    _builder.newLine();
    _builder.append("\t\t");
    int index = 0;
    _builder.newLineIfNotEmpty();
    {
      SchedulerDef _scheduler_4 = schModel.getScheduler();
      Generate _gen_4 = _scheduler_4.getGen();
      boolean _notEquals_4 = (!Objects.equal(_gen_4, null));
      if (_notEquals_4) {
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("switch (component) {\t\t\t\t\t");
        _builder.newLine();
        {
          SchedulerDef _scheduler_5 = schModel.getScheduler();
          Generate _gen_5 = _scheduler_5.getGen();
          GenConfiguration _genconfiguration = _gen_5.getGenconfiguration();
          TestPart _testpart = _genconfiguration.getTestpart();
          EList<GenPart> _part = _testpart.getPart();
          for(final GenPart component : _part) {
            _builder.append("\t\t");
            _builder.append("\t\t");
            _builder.append("case \"");
            String _name = component.getName();
            _builder.append(_name, "\t\t\t\t");
            _builder.append("\" :");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t\t");
            _builder.append("\t");
            _builder.append("index = ");
            int _plusPlus = index++;
            _builder.append(_plusPlus, "\t\t\t\t\t");
            _builder.append("; break ;");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("}\t\t\t\t");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("if (index >=0 && index <= ");
        _builder.append(GenerationGenerator.compsize, "\t\t\t");
        _builder.append(") {\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("\t\t");
        _builder.append("if (s_component[index] == null)");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t\t\t");
        _builder.append("s_component[index] = breakline? code + \"\\n\" : code ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t\t");
        _builder.append("else");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t\t\t");
        _builder.append("s_component[index] += breakline? code + \"\\n\" : code ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
