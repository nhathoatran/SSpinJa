package scheduling.generator;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.HashSet;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.Conversions;
import scheduling.dsl.DataBlockDef;
import scheduling.dsl.DataDef;
import scheduling.dsl.DataSingleDef;
import scheduling.dsl.FunctionName;
import scheduling.dsl.IfDef;
import scheduling.dsl.InterfaceDef;
import scheduling.dsl.InterfaceFunction;
import scheduling.dsl.InterfaceParameterDeclare;
import scheduling.dsl.InterfaceParameterList;
import scheduling.dsl.ProcessDataDef;
import scheduling.dsl.ProcessPropertyDef;
import scheduling.dsl.ProcessPropertyName;
import scheduling.dsl.SchedulerDataDef;
import scheduling.dsl.SchedulerDef;
import scheduling.dsl.SchedulerPropertyDef;
import scheduling.dsl.SchedulerPropertyName;
import scheduling.dsl.Statement;
import scheduling.dsl.Stm;
import scheduling.generator.Data;
import scheduling.generator.Statements;

@SuppressWarnings("all")
public class Interface {
  public static CharSequence genInterfaceFunction(final SchedulerDef sch) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//---------------interface function---------------------------------------");
    _builder.newLine();
    {
      InterfaceDef _interface = sch.getInterface();
      boolean _notEquals = (!Objects.equal(_interface, null));
      if (_notEquals) {
        {
          InterfaceDef _interface_1 = sch.getInterface();
          EList<InterfaceFunction> _interfacefunction = _interface_1.getInterfacefunction();
          for(final InterfaceFunction f : _interfacefunction) {
            CharSequence _genFunction = Interface.genFunction(f);
            _builder.append(_genFunction, "");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    String _genInterfaceFunctionCall = Interface.genInterfaceFunctionCall(sch);
    _builder.append(_genInterfaceFunctionCall, "");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public static CharSequence genFunction(final InterfaceFunction i) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void interface_");
    FunctionName _functionname = i.getFunctionname();
    String _name = _functionname.getName();
    _builder.append(_name, "");
    _builder.append(" (String paraList) throws ValidationException {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    ArrayList<String> remainProcessList = new ArrayList<String>();
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    HashSet<String> definedProcessSet = new HashSet<String>();
    _builder.append("\t\t\t");
    _builder.newLineIfNotEmpty();
    {
      InterfaceParameterList _parameterlist = i.getParameterlist();
      boolean _notEquals = (!Objects.equal(_parameterlist, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("String[] paras = paraList.split(\",\");");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (paras.length != ");
        InterfaceParameterList _parameterlist_1 = i.getParameterlist();
        EList<InterfaceParameterDeclare> _para = _parameterlist_1.getPara();
        int _length = ((Object[])Conversions.unwrapArray(_para, Object.class)).length;
        _builder.append(_length, "\t");
        _builder.append(" + 1) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("System.out.println(\"Can not call function ");
        FunctionName _functionname_1 = i.getFunctionname();
        String _name_1 = _functionname_1.getName();
        _builder.append(_name_1, "\t\t");
        _builder.append("\");");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("// list the parameters ");
        int pCount = 1;
        _builder.append(" ");
        remainProcessList.clear();
        _builder.append("\t\t\t\t");
        _builder.newLineIfNotEmpty();
        {
          InterfaceParameterList _parameterlist_2 = i.getParameterlist();
          EList<InterfaceParameterDeclare> _para_1 = _parameterlist_2.getPara();
          for(final InterfaceParameterDeclare p : _para_1) {
            {
              scheduling.dsl.String _type = p.getType();
              String _string = _type.toString();
              boolean _contains = _string.contains("int");
              if (_contains) {
                _builder.append("\t");
                _builder.append("int ");
                String _paraname = p.getParaname();
                _builder.append(_paraname, "\t");
                _builder.append(" = Integer.parseInt(paras[");
                int _plusPlus = pCount++;
                _builder.append(_plusPlus, "\t");
                _builder.append("].trim());");
                _builder.newLineIfNotEmpty();
              } else {
                {
                  scheduling.dsl.String _type_1 = p.getType();
                  String _string_1 = _type_1.toString();
                  boolean _contains_1 = _string_1.contains("bool");
                  if (_contains_1) {
                    _builder.append("\t");
                    _builder.append("boolean ");
                    String _paraname_1 = p.getParaname();
                    _builder.append(_paraname_1, "\t");
                    _builder.append(" = Boolean.parseBoolean(paras[");
                    int _plusPlus_1 = pCount++;
                    _builder.append(_plusPlus_1, "\t");
                    _builder.append("].trim());");
                    _builder.newLineIfNotEmpty();
                  } else {
                    {
                      scheduling.dsl.String _type_2 = p.getType();
                      String _string_2 = _type_2.toString();
                      boolean _contains_2 = _string_2.contains("byte");
                      if (_contains_2) {
                        _builder.append("\t");
                        _builder.append("int ");
                        String _paraname_2 = p.getParaname();
                        _builder.append(_paraname_2, "\t");
                        _builder.append(" = Integer.parseInt(paras[");
                        int _plusPlus_2 = pCount++;
                        _builder.append(_plusPlus_2, "\t");
                        _builder.append("].trim());");
                        _builder.newLineIfNotEmpty();
                      } else {
                        {
                          scheduling.dsl.String _type_3 = p.getType();
                          String _string_3 = _type_3.toString();
                          boolean _contains_3 = _string_3.contains("process");
                          if (_contains_3) {
                            _builder.append("\t");
                            _builder.append("//add ");
                            String _paraname_3 = p.getParaname();
                            _builder.append(_paraname_3, "\t");
                            _builder.append(" to remainProcessList -> ");
                            String _paraname_4 = p.getParaname();
                            boolean _add = remainProcessList.add(_paraname_4);
                            _builder.append(_add, "\t");
                            _builder.append(" ");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("//add ");
                            String _paraname_5 = p.getParaname();
                            _builder.append(_paraname_5, "\t");
                            _builder.append(" to definedProcessSet -> ");
                            String _paraname_6 = p.getParaname();
                            boolean _add_1 = definedProcessSet.add(_paraname_6);
                            _builder.append(_add_1, "\t");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("String s_");
                            String _paraname_7 = p.getParaname();
                            _builder.append(_paraname_7, "\t");
                            _builder.append(" = paras[");
                            int _plusPlus_3 = pCount++;
                            _builder.append(_plusPlus_3, "\t");
                            _builder.append("].trim();");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("ArrayList<SchedulerProcess> a_");
                            String _paraname_8 = p.getParaname();
                            _builder.append(_paraname_8, "\t");
                            _builder.append(" = findProcessByAlias(s_");
                            String _paraname_9 = p.getParaname();
                            _builder.append(_paraname_9, "\t");
                            _builder.append(") ;\t\t\t\t\t\t\t\t\t");
                            _builder.newLineIfNotEmpty();
                          } else {
                            _builder.append("\t");
                            _builder.append("System.out.println(\"Error converting parameter\");");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("return ;");
                            _builder.newLine();
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//interface function work");
    _builder.newLine();
    {
      EList<Stm> _statements = i.getStatements();
      boolean _notEquals_1 = (!Objects.equal(_statements, null));
      if (_notEquals_1) {
        {
          EList<Stm> _statements_1 = i.getStatements();
          for(final Stm st : _statements_1) {
            _builder.append("\t");
            ArrayList<String> functionprocessList = new ArrayList<String>();
            _builder.append("//");
            boolean _addAll = functionprocessList.addAll(remainProcessList);
            _builder.append(_addAll, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            ArrayList<String> realRemainProcessList = new ArrayList<String>();
            _builder.append("//");
            boolean _addAll_1 = realRemainProcessList.addAll(remainProcessList);
            _builder.append(_addAll_1, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append(" ");
            _builder.append("//real remainProcessList: ");
            _builder.append(realRemainProcessList, "\t ");
            _builder.append(", functionProcesslist: ");
            _builder.append(functionprocessList, "\t ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            IfDef _ifdef = st.getIfdef();
            CharSequence _genIfDef = Statements.genIfDef(_ifdef);
            _builder.append(_genIfDef, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            Statement _statement = st.getStatement();
            CharSequence _dispatchStatementwithProcessList = Statements.dispatchStatementwithProcessList(_statement, realRemainProcessList, functionprocessList, definedProcessSet);
            _builder.append(_dispatchStatementwithProcessList, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            IfDef _ifdef_1 = st.getIfdef();
            CharSequence _genCloseIfDef = Statements.genCloseIfDef(_ifdef_1);
            _builder.append(_genCloseIfDef, "\t");
            _builder.append(" ");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//check to select other process");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//if (running_process == null) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//\tselect_process(-1) ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  /**
   * static def String genProcessListHeader(Statements st, ArrayList<String> processList)
   * '''
   * «IF st instanceof MoveProcess»
   * «IF processList.contains(st.process.name)»
   * for («st.process.name» : a_«st.process.name» {
   * «Statements.dispatchStatement(st)»
   * }
   * «ENDIF»
   * «ENDIF»
   * 
   * «IF st instanceof RemoveProcess»
   * «IF processList.contains(st.process.name)»
   * for («st.process.name» : a_«st.process.name» {
   * «Statements.dispatchStatement(st)»
   * }
   * «ENDIF»
   * «ENDIF»
   * 
   * «IF st instanceof ChangeValue»
   * «IF st instanceof ChangeValueUnOp»
   * «IF processList.contains(st.getVar.name)»
   * for («st.getVar.name» : a_«st.getVar.name» {
   * «Statements.dispatchStatement(st)»
   * }
   * «ENDIF»
   * «ELSE»
   * «IF st instanceof ChangeValueExpression»
   * «IF processList.contains(st.getVar.name)»
   * for («st.getVar.name» : a_«st.getVar.name» {
   * «Statements.dispatchStatement(st)»
   * }
   * «ENDIF»
   * «ENDIF»
   * «ENDIF»
   * «ENDIF»
   * 
   * «IF st instanceof IfStatement»
   * «ENDIF»
   * «IF st instanceof AssertStatement»
   * «ENDIF»
   * «IF st instanceof CallFunction»
   * «ENDIF»
   * '''
   * 
   * static def String genProcessListHeaderForExpression(Expression ex, ArrayList<String> processList)
   * '''
   * «var ArrayList<String> pList = processList»
   * «IF ex instanceof Or»
   * «genProcessListHeaderForExpression(ex.left, pList)»
   * «genProcessListHeaderForExpression(ex.right, pList)»
   * «ENDIF»
   * «IF ex instanceof And»
   * «genProcessListHeaderForExpression(ex.left, pList)»
   * «genProcessListHeaderForExpression(ex.right, pList)»
   * «ENDIF»
   * «IF ex instanceof Equality»
   * «genProcessListHeaderForExpression(ex.left, pList)»
   * «genProcessListHeaderForExpression(ex.right, pList)»
   * «ENDIF»
   * «IF ex instanceof Comparison»
   * «genProcessListHeaderForExpression(ex.left, pList)»
   * «genProcessListHeaderForExpression(ex.right, pList)»
   * «ENDIF»
   * «IF ex instanceof Plus»
   * «genProcessListHeaderForExpression(ex.left, pList)»
   * «genProcessListHeaderForExpression(ex.right, pList)»
   * «ENDIF»
   * «IF ex instanceof Minus»
   * «genProcessListHeaderForExpression(ex.left, pList)»
   * «genProcessListHeaderForExpression(ex.right, pList)»
   * «ENDIF»
   * «IF ex instanceof MulOrDiv»
   * «genProcessListHeaderForExpression(ex.left, pList)»
   * «genProcessListHeaderForExpression(ex.right, pList)»
   * «ENDIF»
   * «IF ex instanceof GetID»
   * 
   * «ENDIF»
   * «IF ex instanceof InExpression»
   * 
   * «ENDIF»
   * «IF ex instanceof NullExpression»
   * 
   * «ENDIF»
   * '''
   * 
   * static def String dispatchExpression(Expression exp){
   * if (exp instanceof Or) return "(" + dispatchExpression(exp.left) + (if(exp.right != null) " || " + dispatchExpression(exp.right)) + ")"
   * else if (exp instanceof And) return "(" + dispatchExpression(exp.left) + " && " + dispatchExpression(exp.right) + ")"
   * else if (exp instanceof Equality) return "(" + dispatchExpression(exp.left) + exp.op + dispatchExpression(exp.right) + ")"
   * else if (exp instanceof Comparison) return "(" + dispatchExpression(exp.left) + exp.op + dispatchExpression(exp.right) + ")"
   * else if (exp instanceof Plus) return "(" + dispatchExpression(exp.left) + '+' + dispatchExpression(exp.right) + ")"
   * else if (exp instanceof Minus) return "(" + dispatchExpression(exp.left) + '-' + dispatchExpression(exp.right) + ")"
   * else if (exp instanceof MulOrDiv) return "(" + dispatchExpression(exp.left) + exp.op + dispatchExpression(exp.right) + ")"
   * else if (exp instanceof Not) return "(!" + dispatchExpression(exp.expression) + ")"
   * 
   * else if (exp instanceof GetID) return exp.proc.name + ".processID"
   * else if (exp instanceof InExpression) return '('  + exp.col.name + '.hasProcess(' + exp.proc.toString + ') > 0)'
   * else if (exp instanceof NullExpression) return '(' + schKeyword  + exp.proc.name + ' == null)'
   * 
   * //else if (exp instanceof QualifiedNames) return  dispatchQualifiedNames(exp) //no meaningful
   * else if (exp instanceof Atomic) {
   * if (exp.getVar != null)
   * if (schKeyword.empty)
   * return  dispatchQualifiedNames(exp.getVar)
   * if (exp.node.tokenText.contains('.'))
   * return (schKeyword + exp.node.tokenText)
   * else return exp.node.tokenText
   * }
   * }
   */
  public static String genInterfaceFunctionCall(final SchedulerDef sch) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public boolean sch_api(String funcName, String paraList) throws ValidationException {");
    _builder.newLine();
    {
      String _parent = sch.getParent();
      boolean _notEquals = (!Objects.equal(_parent, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("if (super.sch_api(funcName, paraList))");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("//already call super function");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return true ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("switch (funcName) {");
    _builder.newLine();
    {
      InterfaceDef _interface = sch.getInterface();
      boolean _notEquals_1 = (!Objects.equal(_interface, null));
      if (_notEquals_1) {
        {
          InterfaceDef _interface_1 = sch.getInterface();
          EList<InterfaceFunction> _interfacefunction = _interface_1.getInterfacefunction();
          for(final InterfaceFunction f : _interfacefunction) {
            _builder.append("\t\t");
            _builder.append("case \"");
            FunctionName _functionname = f.getFunctionname();
            String _name = _functionname.getName();
            _builder.append(_name, "\t\t");
            _builder.append("\" :\t\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            _builder.append("interface_");
            FunctionName _functionname_1 = f.getFunctionname();
            String _name_1 = _functionname_1.getName();
            _builder.append(_name_1, "\t\t\t");
            _builder.append("(paraList) ;\t\t\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("\t");
            _builder.append("return true ;");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("System.out.println(\"Error calling Scheduler API function\");");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("return false ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\t\t\t");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public boolean sch_get(String processName, String property) {");
    _builder.newLine();
    {
      String _parent_1 = sch.getParent();
      boolean _notEquals_2 = (!Objects.equal(_parent_1, null));
      if (_notEquals_2) {
        _builder.append("\t");
        _builder.append("if (super.sch_get(processName, property))");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return true ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("if (processName.trim().equals(\"scheduler\")) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("//scheduler data variable");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch (property) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    ArrayList<String> varlist = new ArrayList<String>();
    _builder.newLineIfNotEmpty();
    {
      SchedulerDef _scheduler = Data.schModel.getScheduler();
      SchedulerDataDef _schedulerdata = _scheduler.getSchedulerdata();
      boolean _notEquals_3 = (!Objects.equal(_schedulerdata, null));
      if (_notEquals_3) {
        {
          SchedulerDef _scheduler_1 = Data.schModel.getScheduler();
          SchedulerDataDef _schedulerdata_1 = _scheduler_1.getSchedulerdata();
          EList<DataDef> _datadef = _schedulerdata_1.getDatadef();
          for(final DataDef d : _datadef) {
            {
              DataBlockDef _datablockdef = d.getDatablockdef();
              boolean _notEquals_4 = (!Objects.equal(_datablockdef, null));
              if (_notEquals_4) {
                {
                  DataBlockDef _datablockdef_1 = d.getDatablockdef();
                  EList<DataSingleDef> _datadef_1 = _datablockdef_1.getDatadef();
                  for(final DataSingleDef data : _datadef_1) {
                    {
                      SchedulerPropertyDef _prop = data.getProp();
                      boolean _notEquals_5 = (!Objects.equal(_prop, null));
                      if (_notEquals_5) {
                        {
                          SchedulerPropertyDef _prop_1 = data.getProp();
                          EList<SchedulerPropertyName> _name_2 = _prop_1.getName();
                          for(final SchedulerPropertyName v : _name_2) {
                            {
                              String _name_3 = v.getName();
                              boolean _contains = varlist.contains(_name_3);
                              boolean _not = (!_contains);
                              if (_not) {
                                _builder.append("\t\t\t");
                                _builder.append("case \"");
                                String _name_4 = v.getName();
                                _builder.append(_name_4, "\t\t\t");
                                _builder.append("\" :\t\t\t\t\t\t");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t\t\t");
                                _builder.append("\t");
                                _builder.append("SchedulerPromelaModel.api_execute_result = ");
                                SchedulerPropertyDef _prop_2 = data.getProp();
                                scheduling.dsl.String _type = _prop_2.getType();
                                String _string = _type.toString();
                                String _generateCastingVariable = Interface.generateCastingVariable(_string);
                                _builder.append(_generateCastingVariable, "\t\t\t\t");
                                _builder.append("(");
                                String _name_5 = v.getName();
                                _builder.append(_name_5, "\t\t\t\t");
                                _builder.append(") ;");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t\t\t");
                                _builder.append("\t");
                                _builder.append("return true ; //");
                                String _name_6 = v.getName();
                                boolean _add = varlist.add(_name_6);
                                _builder.append(_add, "\t\t\t\t");
                                _builder.append(" ");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        }
                      }
                    }
                  }
                }
              } else {
                {
                  DataSingleDef _datasingledef = d.getDatasingledef();
                  SchedulerPropertyDef _prop_3 = _datasingledef.getProp();
                  boolean _notEquals_6 = (!Objects.equal(_prop_3, null));
                  if (_notEquals_6) {
                    {
                      DataSingleDef _datasingledef_1 = d.getDatasingledef();
                      SchedulerPropertyDef _prop_4 = _datasingledef_1.getProp();
                      EList<SchedulerPropertyName> _name_7 = _prop_4.getName();
                      for(final SchedulerPropertyName v_1 : _name_7) {
                        {
                          String _name_8 = v_1.getName();
                          boolean _contains_1 = varlist.contains(_name_8);
                          boolean _not_1 = (!_contains_1);
                          if (_not_1) {
                            _builder.append("\t\t\t");
                            _builder.append("case \"");
                            String _name_9 = v_1.getName();
                            _builder.append(_name_9, "\t\t\t");
                            _builder.append("\" :\t\t\t\t\t\t");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t\t\t");
                            _builder.append("\t");
                            _builder.append("SchedulerPromelaModel.api_execute_result = ");
                            DataSingleDef _datasingledef_2 = d.getDatasingledef();
                            SchedulerPropertyDef _prop_5 = _datasingledef_2.getProp();
                            scheduling.dsl.String _type_1 = _prop_5.getType();
                            String _string_1 = _type_1.toString();
                            String _generateCastingVariable_1 = Interface.generateCastingVariable(_string_1);
                            _builder.append(_generateCastingVariable_1, "\t\t\t\t");
                            _builder.append("(");
                            String _name_10 = v_1.getName();
                            _builder.append(_name_10, "\t\t\t\t");
                            _builder.append(") ;");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t\t\t");
                            _builder.append("\t");
                            _builder.append("return true ; //");
                            String _name_11 = v_1.getName();
                            boolean _add_1 = varlist.add(_name_11);
                            _builder.append(_add_1, "\t\t\t\t");
                            _builder.append(" ");
                            _builder.newLineIfNotEmpty();
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    _builder.append("\t\t\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("System.out.println(\"Error getting scheduler property\");");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("} else {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("ArrayList<SchedulerProcess> aProcess = findProcess(processName) ;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if (aProcess.size() == 1) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("SchedulerProcess process = aProcess.get(0) ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("switch (property) {");
    _builder.newLine();
    {
      ProcessDataDef _processdata = Data.procModel.getProcessdata();
      boolean _notEquals_7 = (!Objects.equal(_processdata, null));
      if (_notEquals_7) {
        {
          ProcessDataDef _processdata_1 = Data.procModel.getProcessdata();
          EList<ProcessPropertyDef> _properties = _processdata_1.getProperties();
          for(final ProcessPropertyDef prop : _properties) {
            {
              EList<ProcessPropertyName> _name_12 = prop.getName();
              for(final ProcessPropertyName pname : _name_12) {
                _builder.append("\t\t\t\t");
                _builder.append("case \"");
                String _name_13 = pname.getName();
                _builder.append(_name_13, "\t\t\t\t");
                _builder.append("\" :\t\t\t\t\t\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t\t\t");
                _builder.append("\t");
                _builder.append("SchedulerPromelaModel.api_execute_result = ");
                String _name_14 = pname.getName();
                String _type_2 = Data.getType(_name_14);
                String _generateCastingVariable_2 = Interface.generateCastingVariable(_type_2);
                _builder.append(_generateCastingVariable_2, "\t\t\t\t\t");
                _builder.append("(process.");
                String _name_15 = pname.getName();
                _builder.append(_name_15, "\t\t\t\t\t");
                _builder.append(") ;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t\t\t");
                _builder.append("\t");
                _builder.append("return true ;");
                _builder.newLine();
              }
            }
          }
        }
      }
    }
    _builder.append("\t\t\t\t");
    _builder.append("default:");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.append("System.out.println(\"Error getting process property\");");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("} ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return false ;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static String generateCastingVariable(final String varType) {
    String result = "";
    switch (varType) {
      case "int":
        result = "Integer.toString";
        break;
      case "boolean":
        result = "Boolean.toString";
        break;
      case "bool":
        result = "Boolean.toString";
        break;
      case "byte":
        result = "Byte.toString";
        break;
      default:
        result = "Integer.toString";
        break;
    }
    return result;
  }
}
