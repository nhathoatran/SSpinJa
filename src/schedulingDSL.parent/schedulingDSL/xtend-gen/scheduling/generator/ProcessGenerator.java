package scheduling.generator;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import scheduling.dsl.BoolValue;
import scheduling.dsl.ConfigProcess;
import scheduling.dsl.Element;
import scheduling.dsl.NumValue;
import scheduling.dsl.ParameterAssign;
import scheduling.dsl.ParameterList;
import scheduling.dsl.ParameterName;
import scheduling.dsl.ProcessConfig;
import scheduling.dsl.ProcessDSL;
import scheduling.dsl.ProcessDataDef;
import scheduling.dsl.ProcessDef;
import scheduling.dsl.ProcessPropertyDef;
import scheduling.dsl.ProcessPropertyName;
import scheduling.dsl.PropertyAssignment;
import scheduling.dsl.SchedulerDSL;
import scheduling.dsl.SporadicProcess;
import scheduling.dsl.Value;
import scheduling.generator.Data;
import scheduling.generator.Utilities;

@SuppressWarnings("all")
public class ProcessGenerator {
  public static CharSequence ProcessDatatoJavaCode(final ProcessDSL procModel) {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<ProcessDef> _process = procModel.getProcess();
      for(final ProcessDef p : _process) {
        {
          scheduling.dsl.Process _proctype = p.getProctype();
          boolean _notEquals = (!Objects.equal(_proctype, null));
          if (_notEquals) {
            _builder.append("/* for process ");
            scheduling.dsl.Process _proctype_1 = p.getProctype();
            String _name = _proctype_1.getName();
            _builder.append(_name, "");
            _builder.append(" */\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("int sch.");
            scheduling.dsl.Process _proctype_2 = p.getProctype();
            String _name_1 = _proctype_2.getName();
            _builder.append(_name_1, "");
            _builder.append(".get_processID() ;");
            _builder.newLineIfNotEmpty();
            _builder.append("byte sch.");
            scheduling.dsl.Process _proctype_3 = p.getProctype();
            String _name_2 = _proctype_3.getName();
            _builder.append(_name_2, "");
            _builder.append(".isNull() ;");
            _builder.newLineIfNotEmpty();
            {
              boolean _isEmpty = Data.intProperties.isEmpty();
              boolean _not = (!_isEmpty);
              if (_not) {
                {
                  Set<String> _keySet = Data.intProperties.keySet();
                  for(final String prop : _keySet) {
                    {
                      boolean _contains = Data.valPropertyList.contains(prop);
                      if (_contains) {
                        _builder.append("int sch.");
                        scheduling.dsl.Process _proctype_4 = p.getProctype();
                        String _name_3 = _proctype_4.getName();
                        _builder.append(_name_3, "");
                        _builder.append(".get_");
                        _builder.append(prop, "");
                        _builder.append("() ;");
                        _builder.newLineIfNotEmpty();
                      } else {
                        {
                          boolean _contains_1 = Data.varPropertyList.contains(prop);
                          if (_contains_1) {
                            _builder.append("int sch.");
                            scheduling.dsl.Process _proctype_5 = p.getProctype();
                            String _name_4 = _proctype_5.getName();
                            _builder.append(_name_4, "");
                            _builder.append(".");
                            _builder.append(prop, "");
                            _builder.append(" ;");
                            _builder.newLineIfNotEmpty();
                            _builder.append("int sch.");
                            scheduling.dsl.Process _proctype_6 = p.getProctype();
                            String _name_5 = _proctype_6.getName();
                            _builder.append(_name_5, "");
                            _builder.append(".get_");
                            _builder.append(prop, "");
                            _builder.append("() ;");
                            _builder.newLineIfNotEmpty();
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            {
              boolean _isEmpty_1 = Data.boolProperties.isEmpty();
              boolean _not_1 = (!_isEmpty_1);
              if (_not_1) {
                {
                  Set<String> _keySet_1 = Data.boolProperties.keySet();
                  for(final String prop_1 : _keySet_1) {
                    {
                      boolean _contains_2 = Data.valPropertyList.contains(prop_1);
                      if (_contains_2) {
                        _builder.append("bool sch.");
                        scheduling.dsl.Process _proctype_7 = p.getProctype();
                        String _name_6 = _proctype_7.getName();
                        _builder.append(_name_6, "");
                        _builder.append(".get_");
                        _builder.append(prop_1, "");
                        _builder.append("() ;");
                        _builder.newLineIfNotEmpty();
                      } else {
                        {
                          boolean _contains_3 = Data.varPropertyList.contains(prop_1);
                          if (_contains_3) {
                            _builder.append("bool sch.");
                            scheduling.dsl.Process _proctype_8 = p.getProctype();
                            String _name_7 = _proctype_8.getName();
                            _builder.append(_name_7, "");
                            _builder.append(".");
                            _builder.append(prop_1, "");
                            _builder.append(" ;");
                            _builder.newLineIfNotEmpty();
                            _builder.append("bool sch.");
                            scheduling.dsl.Process _proctype_9 = p.getProctype();
                            String _name_8 = _proctype_9.getName();
                            _builder.append(_name_8, "");
                            _builder.append(".get_");
                            _builder.append(prop_1, "");
                            _builder.append("() ;");
                            _builder.newLineIfNotEmpty();
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            {
              boolean _isEmpty_2 = Data.byteProperties.isEmpty();
              boolean _not_2 = (!_isEmpty_2);
              if (_not_2) {
                {
                  Set<String> _keySet_2 = Data.byteProperties.keySet();
                  for(final String prop_2 : _keySet_2) {
                    {
                      boolean _contains_4 = Data.valPropertyList.contains(prop_2);
                      if (_contains_4) {
                        _builder.append("byte sch.");
                        scheduling.dsl.Process _proctype_10 = p.getProctype();
                        String _name_9 = _proctype_10.getName();
                        _builder.append(_name_9, "");
                        _builder.append(".get_");
                        _builder.append(prop_2, "");
                        _builder.append("() ;");
                        _builder.newLineIfNotEmpty();
                      } else {
                        {
                          boolean _contains_5 = Data.varPropertyList.contains(prop_2);
                          if (_contains_5) {
                            _builder.append("byte sch.");
                            scheduling.dsl.Process _proctype_11 = p.getProctype();
                            String _name_10 = _proctype_11.getName();
                            _builder.append(_name_10, "");
                            _builder.append(".");
                            _builder.append(prop_2, "");
                            _builder.append(" ;");
                            _builder.newLineIfNotEmpty();
                            _builder.append("byte sch.");
                            scheduling.dsl.Process _proctype_12 = p.getProctype();
                            String _name_11 = _proctype_12.getName();
                            _builder.append(_name_11, "");
                            _builder.append(".get_");
                            _builder.append(prop_2, "");
                            _builder.append("() ;");
                            _builder.newLineIfNotEmpty();
                          }
                        }
                      }
                    }
                  }
                }
              }
            }
            {
              boolean _isEmpty_3 = Data.clockProperties.isEmpty();
              boolean _not_3 = (!_isEmpty_3);
              if (_not_3) {
                {
                  Set<String> _keySet_3 = Data.clockProperties.keySet();
                  for(final String prop_3 : _keySet_3) {
                    _builder.append("int sch.");
                    scheduling.dsl.Process _proctype_13 = p.getProctype();
                    String _name_12 = _proctype_13.getName();
                    _builder.append(_name_12, "");
                    _builder.append(".");
                    _builder.append(prop_3, "");
                    _builder.append(" ;");
                    _builder.newLineIfNotEmpty();
                    _builder.append("int sch.");
                    scheduling.dsl.Process _proctype_14 = p.getProctype();
                    String _name_13 = _proctype_14.getName();
                    _builder.append(_name_13, "");
                    _builder.append(".get_");
                    _builder.append(prop_3, "");
                    _builder.append("() ;");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
          }
        }
      }
    }
    _builder.newLine();
    _builder.append("/* for running_process and target_process process */\t");
    _builder.newLine();
    _builder.append("int sch.running_process.get_processID() ;\t");
    _builder.newLine();
    _builder.append("byte sch.running_process.isNull() ;");
    _builder.newLine();
    _builder.append("int sch.target_process.get_processID() ;");
    _builder.newLine();
    _builder.append("byte sch.target_process.isNull() ;");
    _builder.newLine();
    {
      boolean _isEmpty_4 = Data.intProperties.isEmpty();
      boolean _not_4 = (!_isEmpty_4);
      if (_not_4) {
        {
          Set<String> _keySet_4 = Data.intProperties.keySet();
          for(final String prop_4 : _keySet_4) {
            {
              boolean _contains_6 = Data.varPropertyList.contains(prop_4);
              if (_contains_6) {
                _builder.append("int sch.running_process.");
                _builder.append(prop_4, "");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
                _builder.append("int sch.target_process.");
                _builder.append(prop_4, "");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
                _builder.append("int sch.running_process.get_");
                _builder.append(prop_4, "");
                _builder.append("() ;");
                _builder.newLineIfNotEmpty();
                _builder.append("int sch.target_process.get_");
                _builder.append(prop_4, "");
                _builder.append("() ;");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _contains_7 = Data.valPropertyList.contains(prop_4);
              if (_contains_7) {
                _builder.append("int sch.running_process.get_");
                _builder.append(prop_4, "");
                _builder.append("() ;");
                _builder.newLineIfNotEmpty();
                _builder.append("int sch.target_process.get_");
                _builder.append(prop_4, "");
                _builder.append("() ;");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    {
      boolean _isEmpty_5 = Data.boolProperties.isEmpty();
      boolean _not_5 = (!_isEmpty_5);
      if (_not_5) {
        {
          Set<String> _keySet_5 = Data.boolProperties.keySet();
          for(final String prop_5 : _keySet_5) {
            {
              boolean _contains_8 = Data.varPropertyList.contains(prop_5);
              if (_contains_8) {
                _builder.append("bool sch.running_process.");
                _builder.append(prop_5, "");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
                _builder.append("bool sch.target_process.");
                _builder.append(prop_5, "");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
                _builder.append("bool sch.running_process.get_");
                _builder.append(prop_5, "");
                _builder.append("() ;");
                _builder.newLineIfNotEmpty();
                _builder.append("bool sch.target_process.get_");
                _builder.append(prop_5, "");
                _builder.append("() ;");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _contains_9 = Data.valPropertyList.contains(prop_5);
              if (_contains_9) {
                _builder.append("bool sch.running_process.get_");
                _builder.append(prop_5, "");
                _builder.append("() ;");
                _builder.newLineIfNotEmpty();
                _builder.append("bool sch.target_process.get_");
                _builder.append(prop_5, "");
                _builder.append("() ;");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    {
      boolean _isEmpty_6 = Data.byteProperties.isEmpty();
      boolean _not_6 = (!_isEmpty_6);
      if (_not_6) {
        {
          Set<String> _keySet_6 = Data.byteProperties.keySet();
          for(final String prop_6 : _keySet_6) {
            {
              boolean _contains_10 = Data.varPropertyList.contains(prop_6);
              if (_contains_10) {
                _builder.append("byte sch.running_process.");
                _builder.append(prop_6, "");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
                _builder.append("byte sch.target_process.");
                _builder.append(prop_6, "");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
                _builder.append("byte sch.running_process.get_");
                _builder.append(prop_6, "");
                _builder.append("() ;");
                _builder.newLineIfNotEmpty();
                _builder.append("byte sch.target_process.get_");
                _builder.append(prop_6, "");
                _builder.append("() ;");
                _builder.newLineIfNotEmpty();
              }
            }
            {
              boolean _contains_11 = Data.valPropertyList.contains(prop_6);
              if (_contains_11) {
                _builder.append("byte sch.running_process.get_");
                _builder.append(prop_6, "");
                _builder.append("() ;");
                _builder.newLineIfNotEmpty();
                _builder.append("byte sch.target_process.get_");
                _builder.append(prop_6, "");
                _builder.append("() ;");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    {
      boolean _isEmpty_7 = Data.clockProperties.isEmpty();
      boolean _not_7 = (!_isEmpty_7);
      if (_not_7) {
        {
          Set<String> _keySet_7 = Data.clockProperties.keySet();
          for(final String prop_7 : _keySet_7) {
            _builder.append("int sch.running_process.");
            _builder.append(prop_7, "");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
            _builder.append("int sch.target_process.");
            _builder.append(prop_7, "");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
            _builder.append("int sch.running_process.get_");
            _builder.append(prop_7, "");
            _builder.append("() ;");
            _builder.newLineIfNotEmpty();
            _builder.append("int sch.target_process.get_");
            _builder.append(prop_7, "");
            _builder.append("() ;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  public static CharSequence ProcessBasetoJavaCode() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package sspinja.scheduling ;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import java.util.ArrayList;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("//Automatic generation");
    _builder.newLine();
    _builder.append("public class SchedulerProcessBase {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static ArrayList<String> processList = new ArrayList<String>() ;\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void inc_time() {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void dec_time() {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void add_time(int time) {}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void sub_time(int time) {}\t\t\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence ProcesstoJavaCode(final ProcessDSL procModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package sspinja.scheduling ;");
    _builder.newLine();
    _builder.append("//Automatic generation");
    _builder.newLine();
    _builder.append("public class SchedulerProcess extends SchedulerProcess_");
    String _name = procModel.getName();
    _builder.append(_name, "");
    _builder.append(" { }\t");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public static CharSequence ProcessDSLtoJavaCode(final ProcessDSL procModel, final SchedulerDSL schModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package sspinja.scheduling ;");
    _builder.newLine();
    _builder.append("import spinja.util.*;");
    _builder.newLine();
    _builder.append("import spinja.exceptions.ValidationException;");
    _builder.newLine();
    _builder.append("import java.util.ArrayList;");
    _builder.newLine();
    _builder.newLine();
    String _parent = procModel.getParent();
    boolean parent = (!Objects.equal(_parent, null));
    _builder.newLineIfNotEmpty();
    _builder.append("//Automatic generation");
    _builder.newLine();
    _builder.append("public class SchedulerProcess_");
    String _name = procModel.getName();
    _builder.append(_name, "");
    _builder.append(" ");
    {
      String _parent_1 = procModel.getParent();
      boolean _notEquals = (!Objects.equal(_parent_1, null));
      if (_notEquals) {
        _builder.append(" extends SchedulerProcess_");
        String _parent_2 = procModel.getParent();
        _builder.append(_parent_2, "");
      } else {
        _builder.append(" extends SchedulerProcessBase");
      }
    }
    _builder.append(" {\t");
    _builder.newLineIfNotEmpty();
    {
      if ((!parent)) {
        _builder.append("\t");
        _builder.append("public int processID ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public int refID ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public int get_processID() {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return processID ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public void print(){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("Util.println(this.toString()) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
      }
    }
    {
      boolean _isEmpty = Data.varPropertyList.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        {
          for(final String pName : Data.varPropertyList) {
            {
              boolean _contains = Data.defPropertyList.contains(pName);
              if (_contains) {
                _builder.append("\t");
                _builder.append("public ");
                String _type = Data.getType(pName);
                _builder.append(_type, "\t");
                _builder.append(" ");
                _builder.append(pName, "\t");
                _builder.append(" ");
                {
                  String _value = Data.getValue(pName);
                  boolean _notEquals_1 = (!Objects.equal(_value, ""));
                  if (_notEquals_1) {
                    _builder.append(" = ");
                    String _value_1 = Data.getValue(pName);
                    _builder.append(_value_1, "\t");
                  }
                }
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    {
      boolean _isEmpty_1 = Data.varPropertyList.isEmpty();
      boolean _not_1 = (!_isEmpty_1);
      if (_not_1) {
        {
          for(final String pName_1 : Data.defPropertyList) {
            {
              ProcessDataDef _processdata = procModel.getProcessdata();
              boolean _notEquals_2 = (!Objects.equal(_processdata, null));
              if (_notEquals_2) {
                {
                  ProcessDataDef _processdata_1 = procModel.getProcessdata();
                  EList<ProcessPropertyDef> _properties = _processdata_1.getProperties();
                  boolean _notEquals_3 = (!Objects.equal(_properties, null));
                  if (_notEquals_3) {
                    {
                      ProcessDataDef _processdata_2 = procModel.getProcessdata();
                      EList<ProcessPropertyDef> _properties_1 = _processdata_2.getProperties();
                      for(final ProcessPropertyDef prop : _properties_1) {
                        {
                          EList<ProcessPropertyName> _name_1 = prop.getName();
                          for(final ProcessPropertyName pn : _name_1) {
                            {
                              String _trim = pName_1.trim();
                              String _name_2 = pn.getName();
                              String _string = _name_2.toString();
                              String _trim_1 = _string.trim();
                              boolean _equals = _trim.equals(_trim_1);
                              if (_equals) {
                                _builder.append("\t");
                                _builder.append("public ");
                                String _type_1 = Data.getType(pName_1);
                                _builder.append(_type_1, "\t");
                                _builder.append(" get_");
                                _builder.append(pName_1, "\t");
                                _builder.append("() {");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.append("return this.");
                                _builder.append(pName_1, "\t\t");
                                _builder.append(" ;");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("}");
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
      }
    }
    {
      boolean _isEmpty_2 = Data.valPropertyList.isEmpty();
      boolean _not_2 = (!_isEmpty_2);
      if (_not_2) {
        {
          for(final String pName_2 : Data.valPropertyList) {
            {
              ProcessDataDef _processdata_3 = procModel.getProcessdata();
              boolean _notEquals_4 = (!Objects.equal(_processdata_3, null));
              if (_notEquals_4) {
                {
                  ProcessDataDef _processdata_4 = procModel.getProcessdata();
                  EList<ProcessPropertyDef> _properties_2 = _processdata_4.getProperties();
                  boolean _notEquals_5 = (!Objects.equal(_properties_2, null));
                  if (_notEquals_5) {
                    {
                      ProcessDataDef _processdata_5 = procModel.getProcessdata();
                      EList<ProcessPropertyDef> _properties_3 = _processdata_5.getProperties();
                      for(final ProcessPropertyDef prop_1 : _properties_3) {
                        {
                          EList<ProcessPropertyName> _name_3 = prop_1.getName();
                          for(final ProcessPropertyName pn_1 : _name_3) {
                            {
                              boolean _equals_1 = pName_2.equals(pn_1);
                              if (_equals_1) {
                                _builder.append("\t");
                                _builder.append("public ");
                                String _type_2 = Data.getType(pName_2);
                                _builder.append(_type_2, "\t");
                                _builder.append(" get_");
                                _builder.append(pName_2, "\t");
                                _builder.append("() {");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.append("return SchedulerObject.getStaticPropertyObject(this.refID).");
                                _builder.append(pName_2, "\t\t");
                                _builder.append(" ;");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("}");
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
      }
    }
    _builder.append("\t\t\t\t\t");
    _builder.newLine();
    {
      if (((!parent) && Data.clockPropertyList.isEmpty())) {
        _builder.append("\t");
        _builder.append("public void inc_time() {}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public void dec_time() {}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public void add_time(int time) {}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public void sub_time(int time) {}");
        _builder.newLine();
      } else {
        {
          boolean _isEmpty_3 = Data.clockPropertyList.isEmpty();
          boolean _not_3 = (!_isEmpty_3);
          if (_not_3) {
            _builder.append("\t");
            _builder.append("public void inc_time() {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("super.inc_time() ;");
            _builder.newLine();
            {
              for(final String clock : Data.clockPropertyList) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append(clock, "\t\t");
                _builder.append(" ++ ;");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("}\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("public void dec_time() {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("super.dec_time() ;");
            _builder.newLine();
            {
              for(final String clock_1 : Data.clockPropertyList) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append(clock_1, "\t\t");
                _builder.append(" -- ;");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("}\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("public void add_time(int time) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("super.add_time(time) ;");
            _builder.newLine();
            {
              for(final String clock_2 : Data.clockPropertyList) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append(clock_2, "\t\t");
                _builder.append(" += time ;");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("}\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("public void sub_time(int time) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("super.sub_time(time) ;");
            _builder.newLine();
            {
              for(final String clock_3 : Data.clockPropertyList) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append(clock_3, "\t\t");
                _builder.append(" -= time ;");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("public int getSize() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("int size = 0;\t\t\t\t");
    _builder.newLine();
    {
      boolean _isEmpty_4 = Data.varPropertyList.isEmpty();
      boolean _not_4 = (!_isEmpty_4);
      if (_not_4) {
        {
          for(final String pName_3 : Data.varPropertyList) {
            _builder.append("\t\t");
            {
              String _type_3 = Data.getType(pName_3);
              boolean _equals_2 = Objects.equal(_type_3, "int");
              if (_equals_2) {
                _builder.append("size += 4 ; //");
                _builder.append(pName_3, "\t\t");
              }
            }
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            {
              String _type_4 = Data.getType(pName_3);
              boolean _equals_3 = Objects.equal(_type_4, "byte");
              if (_equals_3) {
                _builder.append("size += 1 ; //");
                _builder.append(pName_3, "\t\t");
              }
            }
            _builder.append("\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            {
              String _type_5 = Data.getType(pName_3);
              boolean _equals_4 = Objects.equal(_type_5, "boolean");
              if (_equals_4) {
                _builder.append("size += 1 ;//");
                _builder.append(pName_3, "\t\t");
              }
            }
            _builder.append("\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      if (parent) {
        _builder.append("\t\t");
        _builder.append("size += super.getSize() ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("return size ;");
        _builder.newLine();
      } else {
        _builder.append("\t\t");
        _builder.append("return 4 + 4 + size ; //processID, refID, process properties\t\t\t\t");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public SchedulerProcess_");
    String _name_4 = procModel.getName();
    _builder.append(_name_4, "\t");
    _builder.append("(int processID) {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("//this.processID = (byte) processID ;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("this.processID = processID ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public SchedulerProcess_");
    String _name_5 = procModel.getName();
    _builder.append(_name_5, "\t");
    _builder.append("() {}");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String getName() {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return SchedulerObject.getStaticPropertyObject(this.refID).pName + \"_\" + this.processID; ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public String toString() {");
    _builder.newLine();
    {
      String _parent_3 = procModel.getParent();
      boolean _equals_5 = Objects.equal(_parent_3, null);
      if (_equals_5) {
        _builder.append("\t\t");
        _builder.append("String res = \"    + Process ID=\" + this.processID ; ");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("res += \", Name: \" + SchedulerObject.getStaticPropertyObject(this.refID).pName + \", Ref ID=\" + this.refID ;");
        _builder.newLine();
      } else {
        _builder.append("\t\t");
        _builder.append("String res = super.toString() ;");
        _builder.newLine();
      }
    }
    {
      boolean _isEmpty_5 = Data.varPropertyList.isEmpty();
      boolean _not_5 = (!_isEmpty_5);
      if (_not_5) {
        {
          for(final String pName_4 : Data.varPropertyList) {
            _builder.append("\t\t");
            _builder.append("res +=  \", ");
            _builder.append(pName_4, "\t\t");
            _builder.append(" = \" + this.");
            _builder.append(pName_4, "\t\t");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t\t");
    _builder.append("return res ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void encode(DataWriter _writer) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("//_writer.writeByte(processID);");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("//_writer.writeByte(refID);");
    _builder.newLine();
    {
      if ((!parent)) {
        _builder.append("\t\t");
        _builder.append("_writer.writeInt(processID);");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("_writer.writeInt(refID);");
        _builder.newLine();
      } else {
        _builder.append("\t\t");
        _builder.append("super.encode(_writer) ;");
        _builder.newLine();
      }
    }
    {
      boolean _isEmpty_6 = Data.varPropertyList.isEmpty();
      boolean _not_6 = (!_isEmpty_6);
      if (_not_6) {
        {
          for(final String pName_5 : Data.varPropertyList) {
            {
              boolean _contains_1 = Data.defPropertyList.contains(pName_5);
              if (_contains_1) {
                _builder.append("\t\t");
                {
                  String _type_6 = Data.getType(pName_5);
                  boolean _equals_6 = Objects.equal(_type_6, "int");
                  if (_equals_6) {
                    _builder.append("_writer.writeInt(");
                    _builder.append(pName_5, "\t\t");
                    _builder.append(") ;");
                  }
                }
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                {
                  String _type_7 = Data.getType(pName_5);
                  boolean _equals_7 = Objects.equal(_type_7, "byte");
                  if (_equals_7) {
                    _builder.append("_writer.writeByte(");
                    _builder.append(pName_5, "\t\t");
                    _builder.append(") ;");
                  }
                }
                _builder.append("\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                {
                  String _type_8 = Data.getType(pName_5);
                  boolean _equals_8 = Objects.equal(_type_8, "boolean");
                  if (_equals_8) {
                    _builder.append("_writer.writeBool(");
                    _builder.append(pName_5, "\t\t");
                    _builder.append(") ;");
                  }
                }
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public boolean decode(DataReader _reader) {\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("//processID = (byte) _reader.readByte();");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("//refID = (byte) _reader.readByte();");
    _builder.newLine();
    {
      if ((!parent)) {
        _builder.append("\t\t");
        _builder.append("processID = _reader.readInt();");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("SchedulerObject.processInScheduler[processID] = true ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("refID = _reader.readInt();");
        _builder.newLine();
      } else {
        _builder.append("\t\t");
        _builder.append("super.decode(_reader);");
        _builder.newLine();
      }
    }
    {
      boolean _isEmpty_7 = Data.varPropertyList.isEmpty();
      boolean _not_7 = (!_isEmpty_7);
      if (_not_7) {
        {
          for(final String pName_6 : Data.varPropertyList) {
            {
              boolean _contains_2 = Data.defPropertyList.contains(pName_6);
              if (_contains_2) {
                _builder.append("\t\t");
                {
                  String _type_9 = Data.getType(pName_6);
                  boolean _equals_9 = Objects.equal(_type_9, "int");
                  if (_equals_9) {
                    _builder.append(pName_6, "\t\t");
                    _builder.append(" = _reader.readInt() ; ");
                  }
                }
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                {
                  String _type_10 = Data.getType(pName_6);
                  boolean _equals_10 = Objects.equal(_type_10, "byte");
                  if (_equals_10) {
                    _builder.append(pName_6, "\t\t");
                    _builder.append(" = (byte) _reader.readByte() ; ");
                  }
                }
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                {
                  String _type_11 = Data.getType(pName_6);
                  boolean _equals_11 = Objects.equal(_type_11, "boolean");
                  if (_equals_11) {
                    _builder.append(pName_6, "\t\t");
                    _builder.append(" = _reader.readBool() ; ");
                  }
                }
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    _builder.append("\t\t");
    _builder.append("return true;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      EList<ProcessDef> _process = procModel.getProcess();
      for(final ProcessDef pt : _process) {
        {
          scheduling.dsl.Process _proctype = pt.getProctype();
          boolean _notEquals_6 = (!Objects.equal(_proctype, null));
          if (_notEquals_6) {
            {
              ParameterList _paralist = pt.getParalist();
              boolean _notEquals_7 = (!Objects.equal(_paralist, null));
              if (_notEquals_7) {
                _builder.append("\t");
                _builder.append("//assign function\t\t");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("public void ");
                scheduling.dsl.Process _proctype_1 = pt.getProctype();
                String _name_6 = _proctype_1.getName();
                _builder.append(_name_6, "\t");
                _builder.append("(");
                ParameterList _paralist_1 = pt.getParalist();
                EList<ParameterAssign> _para = _paralist_1.getPara();
                final Function1<ParameterAssign, String> _function = (ParameterAssign it) -> {
                  Object _xifexpression = null;
                  scheduling.dsl.String _type_12 = it.getType();
                  String _string_1 = _type_12.toString();
                  boolean _equals_12 = _string_1.equals("bool");
                  if (_equals_12) {
                    _xifexpression = "boolean";
                  } else {
                    _xifexpression = it.getType();
                  }
                  String _plus = (_xifexpression + " ");
                  EList<ParameterName> _paraname = it.getParaname();
                  final Function1<ParameterName, String> _function_1 = (ParameterName it_1) -> {
                    return it_1.getName();
                  };
                  List<String> _map = ListExtensions.<ParameterName, String>map(_paraname, _function_1);
                  String _join = IterableExtensions.join(_map, ", ");
                  return (_plus + _join);
                };
                List<String> _map = ListExtensions.<ParameterAssign, String>map(_para, _function);
                String _join = IterableExtensions.join(_map, ", ");
                _builder.append(_join, "\t");
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                {
                  EList<PropertyAssignment> _propertyassignment = pt.getPropertyassignment();
                  for(final PropertyAssignment properass : _propertyassignment) {
                    {
                      ProcessPropertyName _propers = properass.getPropers();
                      String _name_7 = _propers.getName();
                      boolean _contains_3 = Data.varPropertyList.contains(_name_7);
                      if (_contains_3) {
                        {
                          ProcessPropertyName _propers_1 = properass.getPropers();
                          boolean _notEquals_8 = (!Objects.equal(_propers_1, null));
                          if (_notEquals_8) {
                            {
                              Value _pvalue = properass.getPvalue();
                              boolean _notEquals_9 = (!Objects.equal(_pvalue, null));
                              if (_notEquals_9) {
                                {
                                  Value _pvalue_1 = properass.getPvalue();
                                  NumValue _num = _pvalue_1.getNum();
                                  boolean _notEquals_10 = (!Objects.equal(_num, null));
                                  if (_notEquals_10) {
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.append("this.");
                                    ProcessPropertyName _propers_2 = properass.getPropers();
                                    String _name_8 = _propers_2.getName();
                                    _builder.append(_name_8, "\t\t");
                                    _builder.append(" = (");
                                    ProcessPropertyName _propers_3 = properass.getPropers();
                                    String _name_9 = _propers_3.getName();
                                    String _type_12 = Data.getType(_name_9);
                                    _builder.append(_type_12, "\t\t");
                                    _builder.append(") ");
                                    Value _pvalue_2 = properass.getPvalue();
                                    NumValue _num_1 = _pvalue_2.getNum();
                                    int _value_2 = _num_1.getValue();
                                    _builder.append(_value_2, "\t\t");
                                    _builder.append(" ;");
                                    _builder.newLineIfNotEmpty();
                                  } else {
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.append("this.");
                                    ProcessPropertyName _propers_4 = properass.getPropers();
                                    String _name_10 = _propers_4.getName();
                                    _builder.append(_name_10, "\t\t");
                                    _builder.append(" = ");
                                    Value _pvalue_3 = properass.getPvalue();
                                    BoolValue _bool = _pvalue_3.getBool();
                                    String _value_3 = _bool.getValue();
                                    _builder.append(_value_3, "\t\t");
                                    _builder.append(" ;");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              } else {
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.append("this.");
                                ProcessPropertyName _propers_5 = properass.getPropers();
                                String _name_11 = _propers_5.getName();
                                _builder.append(_name_11, "\t\t");
                                _builder.append(" = (");
                                ProcessPropertyName _propers_6 = properass.getPropers();
                                String _name_12 = _propers_6.getName();
                                String _type_13 = Data.getType(_name_12);
                                _builder.append(_type_13, "\t\t");
                                _builder.append(") ");
                                ParameterName _pname = properass.getPname();
                                String _name_13 = _pname.getName();
                                _builder.append(_name_13, "\t\t");
                                _builder.append(" ;");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        }
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
            _builder.append("//default value");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("public void ");
            scheduling.dsl.Process _proctype_2 = pt.getProctype();
            String _name_14 = _proctype_2.getName();
            _builder.append(_name_14, "\t");
            _builder.append("() {");
            _builder.newLineIfNotEmpty();
            {
              EList<PropertyAssignment> _propertyassignment_1 = pt.getPropertyassignment();
              for(final PropertyAssignment properass_1 : _propertyassignment_1) {
                {
                  ProcessPropertyName _propers_7 = properass_1.getPropers();
                  boolean _notEquals_11 = (!Objects.equal(_propers_7, null));
                  if (_notEquals_11) {
                    {
                      Value _pvalue_4 = properass_1.getPvalue();
                      boolean _notEquals_12 = (!Objects.equal(_pvalue_4, null));
                      if (_notEquals_12) {
                        {
                          Value _pvalue_5 = properass_1.getPvalue();
                          NumValue _num_2 = _pvalue_5.getNum();
                          boolean _notEquals_13 = (!Objects.equal(_num_2, null));
                          if (_notEquals_13) {
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("this.");
                            ProcessPropertyName _propers_8 = properass_1.getPropers();
                            String _name_15 = _propers_8.getName();
                            _builder.append(_name_15, "\t\t");
                            _builder.append(" = ");
                            Value _pvalue_6 = properass_1.getPvalue();
                            NumValue _num_3 = _pvalue_6.getNum();
                            int _value_4 = _num_3.getValue();
                            _builder.append(_value_4, "\t\t");
                            _builder.append(" ;");
                            _builder.newLineIfNotEmpty();
                          } else {
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("this.");
                            ProcessPropertyName _propers_9 = properass_1.getPropers();
                            String _name_16 = _propers_9.getName();
                            _builder.append(_name_16, "\t\t");
                            _builder.append(" = ");
                            Value _pvalue_7 = properass_1.getPvalue();
                            BoolValue _bool_1 = _pvalue_7.getBool();
                            String _value_5 = _bool_1.getValue();
                            _builder.append(_value_5, "\t\t");
                            _builder.append(" ;");
                            _builder.newLineIfNotEmpty();
                          }
                        }
                      } else {
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("this.");
                        ProcessPropertyName _propers_10 = properass_1.getPropers();
                        String _name_17 = _propers_10.getName();
                        _builder.append(_name_17, "\t\t");
                        _builder.append(" = ");
                        ParameterName _pname_1 = properass_1.getPname();
                        String _name_18 = _pname_1.getName();
                        ParameterList _paralist_2 = pt.getParalist();
                        EList<ParameterAssign> _para_1 = _paralist_2.getPara();
                        String _findValueInParameterList = Utilities.findValueInParameterList(_name_18, _para_1);
                        _builder.append(_findValueInParameterList, "\t\t");
                        _builder.append(" ; ");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                  }
                }
              }
            }
            _builder.append("\t");
            _builder.append("}\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("//init process with string parameter");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("public void ");
            scheduling.dsl.Process _proctype_3 = pt.getProctype();
            String _name_19 = _proctype_3.getName();
            _builder.append(_name_19, "\t");
            _builder.append("(String paraList) throws ValidationException {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("String[] parameters = paraList.split(\",\");\t\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("ArrayList<String> para = new ArrayList<String>() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("for (int i = 0; i < parameters.length; i++)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("if (! parameters[i].trim().isEmpty())");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("para.add(parameters[i]) ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (para.size() == 0) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            scheduling.dsl.Process _proctype_4 = pt.getProctype();
            String _name_20 = _proctype_4.getName();
            _builder.append(_name_20, "\t\t\t");
            _builder.append("() ;\t\t\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("} else {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("//check number of parameters");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("if (para.size() == ");
            int _numberParameters = ProcessGenerator.getNumberParameters(pt);
            _builder.append(_numberParameters, "\t\t\t");
            _builder.append(") \t\t\t\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t\t");
            String _genInitFunction = ProcessGenerator.genInitFunction(pt);
            _builder.append(_genInitFunction, "\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("else //raise exception");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("throw new ValidationException(\"Error init the process for scheduling: ");
            scheduling.dsl.Process _proctype_5 = pt.getProctype();
            String _name_21 = _proctype_5.getName();
            _builder.append(_name_21, "\t\t\t\t");
            _builder.append("(\" + paraList + \")\") ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t\t\t\t\t\t\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void initProcess(String pName, ArrayList<String> para){");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("//call only with execute function\t\t\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("switch (pName) {");
    _builder.newLine();
    {
      EList<ProcessDef> _process_1 = procModel.getProcess();
      for(final ProcessDef pt_1 : _process_1) {
        _builder.append("\t\t\t");
        _builder.append("case \"");
        scheduling.dsl.Process _proctype_6 = pt_1.getProctype();
        String _name_22 = _proctype_6.getName();
        _builder.append(_name_22, "\t\t\t");
        _builder.append("\" : ");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("\t");
        _builder.append("getRefID(pName) ;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t");
        _builder.append("if (para == null) {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t\t");
        scheduling.dsl.Process _proctype_7 = pt_1.getProctype();
        String _name_23 = _proctype_7.getName();
        _builder.append(_name_23, "\t\t\t\t\t");
        _builder.append("() ;\t\t\t\t\t\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("\t");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t\t");
        _builder.append("if (para.isEmpty()) {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t\t\t");
        scheduling.dsl.Process _proctype_8 = pt_1.getProctype();
        String _name_24 = _proctype_8.getName();
        _builder.append(_name_24, "\t\t\t\t\t\t");
        _builder.append("() ;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("\t\t");
        _builder.append("} else {\t");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t\t\t");
        _builder.append("//check number of parameters");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t\t\t");
        _builder.append("if (para.size() == ");
        int _numberParameters_1 = ProcessGenerator.getNumberParameters(pt_1);
        _builder.append(_numberParameters_1, "\t\t\t\t\t\t");
        _builder.append(") \t\t\t\t\t\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("\t\t\t\t");
        String _genInitFunction_1 = ProcessGenerator.genInitFunction(pt_1);
        _builder.append(_genInitFunction_1, "\t\t\t\t\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("\t\t\t");
        _builder.append("else");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t\t\t\t");
        _builder.append("System.out.println(\"Error init the process for scheduling\") ;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t");
        _builder.append("break ;");
        _builder.newLine();
      }
    }
    {
      String _parent_4 = procModel.getParent();
      boolean _equals_12 = Objects.equal(_parent_4, null);
      if (_equals_12) {
        _builder.append("\t\t\t");
        _builder.append("default : break ;");
        _builder.newLine();
      } else {
        _builder.append("\t\t\t");
        _builder.append("default : super.initProcess(pName, para) ; break ;");
        _builder.newLine();
      }
    }
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public void getRefID(String pName){");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if (processList.size() == 0) initProcessList() ;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("this.refID = processList.indexOf(pName);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("protected void initProcessList() {");
    _builder.newLine();
    {
      String _parent_5 = procModel.getParent();
      boolean _notEquals_14 = (!Objects.equal(_parent_5, null));
      if (_notEquals_14) {
        _builder.append("\t\t");
        _builder.append("super.initProcessList() ;");
        _builder.newLine();
      }
    }
    {
      EList<ProcessDef> _process_2 = procModel.getProcess();
      for(final ProcessDef pt_2 : _process_2) {
        _builder.append("\t\t");
        _builder.append("if (!processList.contains(\"");
        scheduling.dsl.Process _proctype_9 = pt_2.getProctype();
        String _name_25 = _proctype_9.getName();
        _builder.append(_name_25, "\t\t");
        _builder.append("\"))");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("\t");
        _builder.append("processList.add(\"");
        scheduling.dsl.Process _proctype_10 = pt_2.getProctype();
        String _name_26 = _proctype_10.getName();
        _builder.append(_name_26, "\t\t\t");
        _builder.append("\") ;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    return _builder;
  }
  
  public static int getNumberParameters(final ProcessDef proc) {
    ParameterList _paralist = proc.getParalist();
    boolean _equals = Objects.equal(_paralist, null);
    if (_equals) {
      return 0;
    } else {
      ParameterList _paralist_1 = proc.getParalist();
      EList<ParameterAssign> _para = _paralist_1.getPara();
      return _para.size();
    }
  }
  
  public static String genInitFunction(final ProcessDef proc) {
    scheduling.dsl.Process _proctype = proc.getProctype();
    String _name = _proctype.getName();
    String result = (_name + "(");
    ParameterList _paralist = proc.getParalist();
    boolean _notEquals = (!Objects.equal(_paralist, null));
    if (_notEquals) {
      for (int i = 0; (i < proc.getParalist().getPara().size()); i++) {
        {
          String _result = result;
          ParameterList _paralist_1 = proc.getParalist();
          EList<ParameterAssign> _para = _paralist_1.getPara();
          ParameterAssign _get = _para.get(i);
          scheduling.dsl.String _type = _get.getType();
          String _plus = ("StringUtil.convert2" + _type);
          String _plus_1 = (_plus + "(para.get(");
          String _plus_2 = (_plus_1 + Integer.valueOf(i));
          String _plus_3 = (_plus_2 + "))");
          result = (_result + _plus_3);
          ParameterList _paralist_2 = proc.getParalist();
          EList<ParameterAssign> _para_1 = _paralist_2.getPara();
          int _size = _para_1.size();
          int _minus = (_size - 1);
          boolean _lessThan = (i < _minus);
          if (_lessThan) {
            String _result_1 = result;
            result = (_result_1 + ", ");
          }
        }
      }
    }
    return (result + ") ;");
  }
  
  private static ArrayList<String> sporadicProcessList = new ArrayList<String>();
  
  private static ArrayList<Integer> startDuration = new ArrayList<Integer>();
  
  private static ArrayList<Integer> endDuration = new ArrayList<Integer>();
  
  private static ArrayList<Integer> limited = new ArrayList<Integer>();
  
  private static int maxsporadictime = 0;
  
  private static int minsporadictime = 0;
  
  public static boolean getsporadicProcessList(final ProcessDSL procModel) {
    boolean result = false;
    ProcessGenerator.sporadicProcessList.clear();
    ProcessGenerator.startDuration.clear();
    ProcessGenerator.endDuration.clear();
    ProcessGenerator.limited.clear();
    ProcessGenerator.maxsporadictime = 0;
    ProcessGenerator.minsporadictime = 0;
    ProcessConfig _processconfig = procModel.getProcessconfig();
    boolean _notEquals = (!Objects.equal(_processconfig, null));
    if (_notEquals) {
      ProcessConfig _processconfig_1 = procModel.getProcessconfig();
      EList<ConfigProcess> _procinit = _processconfig_1.getProcinit();
      for (final ConfigProcess proc : _procinit) {
        SporadicProcess _sporadic = proc.getSporadic();
        boolean _notEquals_1 = (!Objects.equal(_sporadic, null));
        if (_notEquals_1) {
          SporadicProcess _sporadic_1 = proc.getSporadic();
          Element _element = _sporadic_1.getElement();
          String _processInitFunction = ProcessGenerator.getProcessInitFunction(_element);
          ProcessGenerator.sporadicProcessList.add(_processInitFunction);
          SporadicProcess _sporadic_2 = proc.getSporadic();
          int _start = _sporadic_2.getStart();
          ProcessGenerator.startDuration.add(Integer.valueOf(_start));
          SporadicProcess _sporadic_3 = proc.getSporadic();
          int _start_1 = _sporadic_3.getStart();
          boolean _greaterThan = (_start_1 > ProcessGenerator.minsporadictime);
          if (_greaterThan) {
            SporadicProcess _sporadic_4 = proc.getSporadic();
            int _start_2 = _sporadic_4.getStart();
            ProcessGenerator.minsporadictime = _start_2;
          }
          SporadicProcess _sporadic_5 = proc.getSporadic();
          int _end = _sporadic_5.getEnd();
          boolean _greaterThan_1 = (_end > ProcessGenerator.maxsporadictime);
          if (_greaterThan_1) {
            SporadicProcess _sporadic_6 = proc.getSporadic();
            int _end_1 = _sporadic_6.getEnd();
            ProcessGenerator.maxsporadictime = _end_1;
          }
          SporadicProcess _sporadic_7 = proc.getSporadic();
          int _end_2 = _sporadic_7.getEnd();
          ProcessGenerator.endDuration.add(Integer.valueOf(_end_2));
          SporadicProcess _sporadic_8 = proc.getSporadic();
          int _max = _sporadic_8.getMax();
          boolean _lessEqualsThan = (_max <= 0);
          if (_lessEqualsThan) {
            ProcessGenerator.limited.add(Integer.valueOf(1));
          } else {
            SporadicProcess _sporadic_9 = proc.getSporadic();
            int _max_1 = _sporadic_9.getMax();
            ProcessGenerator.limited.add(Integer.valueOf(_max_1));
          }
          result = true;
        }
      }
    }
    return result;
  }
  
  public static CharSequence SporadicProcesstoJavaCode(final ProcessDSL procModel) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _sporadicProcessList = ProcessGenerator.getsporadicProcessList(procModel);
      if (_sporadicProcessList) {
        _builder.append("/*scheduler sporadic process -> set sporadicTime = ");
        _builder.append(Data.sporadicTime = true, "");
        _builder.append(" for realize the clock event*/\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("sporadic {\t\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        int id = 0;
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("int _time_sporadic ;\t\t\t\t");
        _builder.newLine();
        {
          for(final String sp : ProcessGenerator.sporadicProcessList) {
            _builder.append("\t");
            _builder.append("byte count_");
            String _replace = sp.replace("*", "");
            String _replace_1 = _replace.replace("(", "_");
            String _replace_2 = _replace_1.replace(")", "_");
            String _replace_3 = _replace_2.replace(",", "_");
            String _replace_4 = _replace_3.replace(" ", "");
            _builder.append(_replace_4, "\t");
            _builder.append("_");
            int _plusPlus = id++;
            _builder.append(_plusPlus, "\t");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("do");
        _builder.newLine();
        _builder.append("\t");
        int idx = 0;
        _builder.newLineIfNotEmpty();
        {
          for(final String sp_1 : ProcessGenerator.sporadicProcessList) {
            _builder.append("\t");
            String _replace_5 = sp_1.replace("*", "");
            String _replace_6 = _replace_5.replace("(", "_");
            String _replace_7 = _replace_6.replace(")", "_");
            String _replace_8 = _replace_7.replace(",", "_");
            final String p = _replace_8.replace(" ", "");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            final Integer start = ProcessGenerator.startDuration.get(idx);
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            final Integer end = ProcessGenerator.endDuration.get(idx);
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            final Integer max = ProcessGenerator.limited.get(idx);
            _builder.append("\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append(":: d_step { ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("(_time_sporadic >= ");
            _builder.append(start, "\t\t\t");
            _builder.append(" && _time_sporadic <= ");
            _builder.append(end, "\t\t\t");
            _builder.append(" && count_");
            _builder.append(p, "\t\t\t");
            _builder.append("_");
            _builder.append(idx, "\t\t\t");
            _builder.append(" < ");
            _builder.append(max, "\t\t\t");
            _builder.append(") ->  ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("sch_exec(");
            _builder.append(sp_1, "\t\t\t\t");
            _builder.append("); count_");
            _builder.append(p, "\t\t\t\t");
            _builder.append("_");
            int _plusPlus_1 = idx++;
            _builder.append(_plusPlus_1, "\t\t\t\t");
            _builder.append(" ++ ; _time_sporadic ++");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append(":: d_step {(_time_sporadic <= ");
        _builder.append(ProcessGenerator.maxsporadictime, "\t");
        _builder.append(") -> _time_sporadic++ }");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append(":: d_step {(_time_sporadic > ");
        _builder.append(ProcessGenerator.maxsporadictime, "\t");
        _builder.append(" ) -> skip }\t\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("od");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append("/* no scheduler sporadic process -> set sporadicTime = ");
        _builder.append(Data.sporadicTime = false, "");
        _builder.append(" for realize the _time variable */\t");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public static ArrayList<ArrayList<String>> powerSet(final ArrayList<String> list) {
    ArrayList<ArrayList<String>> result = new ArrayList<ArrayList<String>>();
    ArrayList<String> _arrayList = new ArrayList<String>();
    result.add(_arrayList);
    for (final String i : list) {
      {
        ArrayList<ArrayList<String>> temp = new ArrayList<ArrayList<String>>();
        for (final ArrayList<String> innerList : result) {
          {
            ArrayList<String> innerList1 = new ArrayList<String>(innerList);
            innerList1.add(i);
            temp.add(innerList1);
          }
        }
        result.addAll(temp);
      }
    }
    return result;
  }
  
  public static String generateStartOption(final ArrayList<String> list) {
    String result = "";
    int _size = list.size();
    boolean _notEquals = (_size != 0);
    if (_notEquals) {
      String _result = result;
      result = (_result + ":: d_step { ");
      int i = 0;
      for (final String variant : list) {
        {
          String _result_1 = result;
          String _replace = variant.replace("*", "");
          String _replace_1 = _replace.replace("(", "_");
          String _replace_2 = _replace_1.replace(")", "_");
          String _replace_3 = _replace_2.replace(",", "_");
          String _replace_4 = _replace_3.replace(" ", "");
          String _plus = ("! start_" + _replace_4);
          result = (_result_1 + _plus);
          int _size_1 = list.size();
          int _minus = (_size_1 - 1);
          boolean _lessThan = (i < _minus);
          if (_lessThan) {
            String _result_2 = result;
            result = (_result_2 + " && ");
          }
          i++;
        }
      }
      String _result_1 = result;
      result = (_result_1 + " -> ");
      for (final String variant_1 : list) {
        String _result_2 = result;
        int _indexOf = variant_1.indexOf("*");
        String _substring = variant_1.substring(0, _indexOf);
        String _plus = (" exec " + _substring);
        String _plus_1 = (_plus + "; ");
        result = (_result_2 + _plus_1);
      }
      for (final String variant_2 : list) {
        String _result_3 = result;
        String _replace = variant_2.replace("*", "");
        String _replace_1 = _replace.replace("(", "_");
        String _replace_2 = _replace_1.replace(")", "_");
        String _replace_3 = _replace_2.replace(",", "_");
        String _replace_4 = _replace_3.replace(" ", "");
        String _plus_2 = (" start_" + _replace_4);
        String _plus_3 = (_plus_2 + " = true; ");
        result = (_result_3 + _plus_3);
      }
      String _result_4 = result;
      result = (_result_4 + " } ");
    }
    return result;
  }
  
  public static String getProcessInitFunction(final Element element) {
    scheduling.dsl.Process _process = element.getProcess();
    String _name = _process.getName();
    String result = (_name + "(");
    EList<Value> _paraAssign = element.getParaAssign();
    boolean _notEquals = (!Objects.equal(_paraAssign, null));
    if (_notEquals) {
      int i = 0;
      Value value = null;
      for (i = 0; (i < element.getParaAssign().size()); i++) {
        {
          EList<Value> _paraAssign_1 = element.getParaAssign();
          Value _get = _paraAssign_1.get(i);
          value = _get;
          NumValue _num = value.getNum();
          boolean _notEquals_1 = (!Objects.equal(_num, null));
          if (_notEquals_1) {
            String _result = result;
            NumValue _num_1 = value.getNum();
            int _value = _num_1.getValue();
            result = (_result + Integer.valueOf(_value));
          } else {
            String _result_1 = result;
            BoolValue _bool = value.getBool();
            String _value_1 = _bool.getValue();
            result = (_result_1 + _value_1);
          }
          EList<Value> _paraAssign_2 = element.getParaAssign();
          int _size = _paraAssign_2.size();
          int _minus = (_size - 1);
          boolean _lessThan = (i < _minus);
          if (_lessThan) {
            String _result_2 = result;
            result = (_result_2 + ", ");
          }
        }
      }
    }
    String _result = result;
    result = (_result + ")");
    return result;
  }
  
  public static String getProcessInitFunction(final Element element, final ProcessDSL procModel) {
    ProcessDef proc = null;
    EList<ProcessDef> _process = procModel.getProcess();
    for (final ProcessDef p : _process) {
      scheduling.dsl.Process _proctype = p.getProctype();
      String _name = _proctype.getName();
      scheduling.dsl.Process _process_1 = element.getProcess();
      String _name_1 = _process_1.getName();
      boolean _equals = _name.equals(_name_1);
      if (_equals) {
        proc = p;
      }
    }
    scheduling.dsl.Process _process_2 = element.getProcess();
    String _name_2 = _process_2.getName();
    String _plus = (_name_2 + ".");
    scheduling.dsl.Process _process_3 = element.getProcess();
    String _name_3 = _process_3.getName();
    String _plus_1 = (_plus + _name_3);
    String result = (_plus_1 + "(");
    EList<Value> _paraAssign = element.getParaAssign();
    boolean _notEquals = (!Objects.equal(_paraAssign, null));
    if (_notEquals) {
      int i = 0;
      Value value = null;
      for (i = 0; (i < element.getParaAssign().size()); i++) {
        {
          EList<Value> _paraAssign_1 = element.getParaAssign();
          Value _get = _paraAssign_1.get(i);
          value = _get;
          NumValue _num = value.getNum();
          boolean _notEquals_1 = (!Objects.equal(_num, null));
          if (_notEquals_1) {
            String _result = result;
            ParameterList _paralist = proc.getParalist();
            EList<ParameterAssign> _para = _paralist.getPara();
            ParameterAssign _get_1 = _para.get(i);
            scheduling.dsl.String _type = _get_1.getType();
            String _plus_2 = ("(" + _type);
            String _plus_3 = (_plus_2 + ") ");
            ICompositeNode _node = NodeModelUtils.getNode(value);
            String _tokenText = NodeModelUtils.getTokenText(_node);
            String _plus_4 = (_plus_3 + _tokenText);
            result = (_result + _plus_4);
          } else {
            String _result_1 = result;
            BoolValue _bool = value.getBool();
            String _value = _bool.getValue();
            result = (_result_1 + _value);
          }
          EList<Value> _paraAssign_2 = element.getParaAssign();
          int _size = _paraAssign_2.size();
          int _minus = (_size - 1);
          boolean _lessThan = (i < _minus);
          if (_lessThan) {
            String _result_2 = result;
            result = (_result_2 + ", ");
          }
        }
      }
    }
    String _result = result;
    result = (_result + ") ;");
    return result;
  }
}
