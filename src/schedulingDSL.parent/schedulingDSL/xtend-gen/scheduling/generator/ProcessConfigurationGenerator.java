package scheduling.generator;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import scheduling.dsl.AssignParameters;
import scheduling.dsl.AttDef;
import scheduling.dsl.Attribute;
import scheduling.dsl.BoolValue;
import scheduling.dsl.Condition;
import scheduling.dsl.ConfigProcess;
import scheduling.dsl.Constraint;
import scheduling.dsl.Constraints;
import scheduling.dsl.Constructor;
import scheduling.dsl.DefBehaviorProc;
import scheduling.dsl.Element;
import scheduling.dsl.Expression;
import scheduling.dsl.FunctionName;
import scheduling.dsl.InterfaceParameterDeclare;
import scheduling.dsl.InterfaceParameterList;
import scheduling.dsl.List;
import scheduling.dsl.ListDef;
import scheduling.dsl.Method;
import scheduling.dsl.NumValue;
import scheduling.dsl.ParameterAssign;
import scheduling.dsl.ParameterList;
import scheduling.dsl.ParameterName;
import scheduling.dsl.ProcType;
import scheduling.dsl.ProcessBehavior;
import scheduling.dsl.ProcessBehaviors;
import scheduling.dsl.ProcessConfig;
import scheduling.dsl.ProcessConfiguration;
import scheduling.dsl.ProcessInit;
import scheduling.dsl.ProcessType;
import scheduling.dsl.Range;
import scheduling.dsl.Set;
import scheduling.dsl.Value;
import scheduling.generator.Data;
import scheduling.generator.Statements;

@SuppressWarnings("all")
public class ProcessConfigurationGenerator {
  private static ArrayList<ArrayList<String>> configList = new ArrayList<ArrayList<String>>();
  
  private static ArrayList<String> elementList;
  
  private final static ScriptEngineManager mgr = new ScriptEngineManager();
  
  private final static ScriptEngine engine = ProcessConfigurationGenerator.mgr.getEngineByName("JavaScript");
  
  public static double seconds = 0;
  
  public static Object evaluateExpression(final String exp) {
    try {
      return ProcessConfigurationGenerator.engine.eval(exp);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }
  
  private static int attcount;
  
  private static ArrayList<String> attlist;
  
  private static HashMap<Integer, HashSet<Integer>> attval;
  
  private static HashSet<String> assignOk = new HashSet<String>();
  
  private static HashSet<String> assignNotOk = new HashSet<String>();
  
  private static int[] assign;
  
  private static HashMap<String, String> defvalue = new HashMap<String, String>();
  
  public static ArrayList<String> assignConfigList = new ArrayList<String>();
  
  public static int[] initAttributeValue(final ProcessConfiguration processconfiguration) {
    int[] _xblockexpression = null;
    {
      int index = 0;
      Attribute _attribute = processconfiguration.getAttribute();
      boolean _notEquals = (!Objects.equal(_attribute, null));
      if (_notEquals) {
        Attribute _attribute_1 = processconfiguration.getAttribute();
        EList<AttDef> _att = _attribute_1.getAtt();
        int _size = _att.size();
        ProcessConfigurationGenerator.attcount = _size;
        HashMap<Integer, HashSet<Integer>> _hashMap = new HashMap<Integer, HashSet<Integer>>();
        ProcessConfigurationGenerator.attval = _hashMap;
        System.out.println(("1. attribute count:" + Integer.valueOf(ProcessConfigurationGenerator.attcount)));
        Attribute _attribute_2 = processconfiguration.getAttribute();
        EList<AttDef> _att_1 = _attribute_2.getAtt();
        for (final AttDef att : _att_1) {
          {
            HashSet<Integer> valuelist = new HashSet<Integer>();
            ListDef _list = att.getList();
            boolean _notEquals_1 = (!Objects.equal(_list, null));
            if (_notEquals_1) {
              ListDef _list_1 = att.getList();
              EList<List> _list_2 = _list_1.getList();
              for (final List l : _list_2) {
                NumValue _num = l.getNum();
                boolean _notEquals_2 = (!Objects.equal(_num, null));
                if (_notEquals_2) {
                  NumValue _num_1 = l.getNum();
                  int _value = _num_1.getValue();
                  valuelist.add(Integer.valueOf(_value));
                } else {
                  Range _range = l.getRange();
                  boolean _notEquals_3 = (!Objects.equal(_range, null));
                  if (_notEquals_3) {
                    Range _range_1 = l.getRange();
                    NumValue _start = _range_1.getStart();
                    ICompositeNode _node = NodeModelUtils.getNode(_start);
                    String _tokenText = NodeModelUtils.getTokenText(_node);
                    int start = Integer.parseInt(_tokenText);
                    Range _range_2 = l.getRange();
                    NumValue _end = _range_2.getEnd();
                    ICompositeNode _node_1 = NodeModelUtils.getNode(_end);
                    String _tokenText_1 = NodeModelUtils.getTokenText(_node_1);
                    int end = Integer.parseInt(_tokenText_1);
                    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(start, (end + 1), true);
                    for (final Integer i : _doubleDotLessThan) {
                      valuelist.add(i);
                    }
                  }
                }
              }
            } else {
              Value _default = att.getDefault();
              NumValue _num_2 = _default.getNum();
              boolean _notEquals_4 = (!Objects.equal(_num_2, null));
              if (_notEquals_4) {
                Value _default_1 = att.getDefault();
                NumValue _num_3 = _default_1.getNum();
                int _value_1 = _num_3.getValue();
                valuelist.add(Integer.valueOf(_value_1));
              }
            }
            int _plusPlus = index++;
            ProcessConfigurationGenerator.attval.put(Integer.valueOf(_plusPlus), valuelist);
            String _name = att.getName();
            ProcessConfigurationGenerator.attlist.add(_name);
            Value _default_2 = att.getDefault();
            NumValue _num_4 = _default_2.getNum();
            boolean _notEquals_5 = (!Objects.equal(_num_4, null));
            if (_notEquals_5) {
              String _name_1 = att.getName();
              Value _default_3 = att.getDefault();
              NumValue _num_5 = _default_3.getNum();
              int _value_2 = _num_5.getValue();
              String _string = Integer.valueOf(_value_2).toString();
              ProcessConfigurationGenerator.defvalue.put(_name_1, _string);
            } else {
              String _name_2 = att.getName();
              Value _default_4 = att.getDefault();
              BoolValue _bool = _default_4.getBool();
              String _value_3 = _bool.getValue();
              ProcessConfigurationGenerator.defvalue.put(_name_2, _value_3);
            }
            String _name_3 = att.getName();
            String _plus = ("attribute " + _name_3);
            String _plus_1 = (_plus + " : ");
            String _plus_2 = (_plus_1 + valuelist);
            System.out.println(_plus_2);
          }
        }
      }
      int[] _newIntArrayOfSize = new int[ProcessConfigurationGenerator.attcount];
      _xblockexpression = ProcessConfigurationGenerator.assign = _newIntArrayOfSize;
    }
    return _xblockexpression;
  }
  
  public static CharSequence ProcessModeltoJavaCode(final ProcessConfiguration processconfiguration) {
    StringConcatenation _builder = new StringConcatenation();
    {
      DefBehaviorProc _defbehaviorproc = processconfiguration.getDefbehaviorproc();
      ProcessBehaviors _processbehaviors = _defbehaviorproc.getProcessbehaviors();
      boolean _notEquals = (!Objects.equal(_processbehaviors, null));
      if (_notEquals) {
        {
          DefBehaviorProc _defbehaviorproc_1 = processconfiguration.getDefbehaviorproc();
          ProcessBehaviors _processbehaviors_1 = _defbehaviorproc_1.getProcessbehaviors();
          EList<ProcessBehavior> _processbehavior = _processbehaviors_1.getProcessbehavior();
          for(final ProcessBehavior proc : _processbehavior) {
            {
              Constructor _constructor = proc.getConstructor();
              boolean _notEquals_1 = (!Objects.equal(_constructor, null));
              if (_notEquals_1) {
                _builder.append("proctype ");
                Constructor _constructor_1 = proc.getConstructor();
                String _processname = _constructor_1.getProcessname();
                _builder.append(_processname, "");
                _builder.append(" () {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                DefBehaviorProc _defbehaviorproc_2 = processconfiguration.getDefbehaviorproc();
                ProcessBehaviors _processbehaviors_2 = _defbehaviorproc_2.getProcessbehaviors();
                CharSequence _GenProcessBehavior = ProcessConfigurationGenerator.GenProcessBehavior(_processbehaviors_2);
                _builder.append(_GenProcessBehavior, "\t");
                _builder.newLineIfNotEmpty();
                _builder.append("}");
                _builder.newLine();
              }
            }
          }
        }
      } else {
        {
          DefBehaviorProc _defbehaviorproc_3 = processconfiguration.getDefbehaviorproc();
          ProcType _proctype = _defbehaviorproc_3.getProctype();
          EList<ProcessType> _proctype_1 = _proctype.getProctype();
          for(final ProcessType proctype : _proctype_1) {
            _builder.append("proctype ");
            String _name = proctype.getName();
            _builder.append(_name, "");
            _builder.append(" () {\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            CharSequence _GenProcessBehavior_1 = ProcessConfigurationGenerator.GenProcessBehavior(proctype);
            _builder.append(_GenProcessBehavior_1, "\t");
            _builder.append("\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("init {");
    _builder.newLine();
    {
      ProcessInit _processinit = processconfiguration.getProcessinit();
      EList<Set> _order = _processinit.getOrder();
      for(final Set set : _order) {
        {
          EList<Element> _element = set.getElement();
          for(final Element p : _element) {
            _builder.append("\t");
            _builder.append("run ");
            scheduling.dsl.Process _process = p.getProcess();
            String _name_1 = _process.getName();
            _builder.append(_name_1, "\t");
            _builder.append("() ;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence ProcessConfigurationResumetoJavaCode(final String name) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("* Generate process configuration & process model");
    _builder.newLine();
    _builder.append("+ Configuration file: ");
    _builder.append(name, "");
    _builder.newLineIfNotEmpty();
    _builder.append("Total configurations: ");
    int _size = ProcessConfigurationGenerator.assignConfigList.size();
    _builder.append(_size, "");
    _builder.append(" elapsed time ");
    _builder.append(ProcessConfigurationGenerator.seconds, "");
    _builder.append(" miliseconds");
    _builder.newLineIfNotEmpty();
    _builder.append("--- Generate finished ---");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence GenProcessBehavior(final ProcessType proctype) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("do");
    _builder.newLine();
    {
      EList<ProcessBehavior> _processbehavior = proctype.getProcessbehavior();
      for(final ProcessBehavior behave : _processbehavior) {
        {
          Method _method = behave.getMethod();
          boolean _notEquals = (!Objects.equal(_method, null));
          if (_notEquals) {
            _builder.append("\t");
            Method _method_1 = behave.getMethod();
            CharSequence _GenInstanceBehave = ProcessConfigurationGenerator.GenInstanceBehave(_method_1);
            _builder.append(_GenInstanceBehave, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append(":: skip ;");
    _builder.newLine();
    _builder.append("od");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence GenProcessBehavior(final ProcessBehaviors behaviors) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("do");
    _builder.newLine();
    {
      EList<ProcessBehavior> _processbehavior = behaviors.getProcessbehavior();
      for(final ProcessBehavior behave : _processbehavior) {
        {
          Method _method = behave.getMethod();
          boolean _notEquals = (!Objects.equal(_method, null));
          if (_notEquals) {
            _builder.append("\t");
            Method _method_1 = behave.getMethod();
            CharSequence _GenInstanceBehave = ProcessConfigurationGenerator.GenInstanceBehave(_method_1);
            _builder.append(_GenInstanceBehave, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append(":: skip ;");
    _builder.newLine();
    _builder.append("od");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence GenInstanceBehave(final Method method) {
    StringConcatenation _builder = new StringConcatenation();
    {
      InterfaceParameterList _parameterlist = method.getParameterlist();
      boolean _equals = Objects.equal(_parameterlist, null);
      if (_equals) {
        _builder.append(":: sch_api_self(");
        FunctionName _functionname = method.getFunctionname();
        String _name = _functionname.getName();
        _builder.append(_name, "");
        _builder.append(") ;");
        _builder.newLineIfNotEmpty();
      } else {
        ProcessConfigurationGenerator.GenFunctionCall(method);
        _builder.newLineIfNotEmpty();
        {
          for(final String call : ProcessConfigurationGenerator.functioncall) {
            _builder.append(":: sch_api_self(");
            FunctionName _functionname_1 = method.getFunctionname();
            String _name_1 = _functionname_1.getName();
            _builder.append(_name_1, "");
            _builder.append(", ");
            _builder.append(call, "");
            _builder.append(") ;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  private static ArrayList<String> functioncall = new ArrayList<String>();
  
  public static void GenFunctionCall(final Method method) {
    ProcessConfigurationGenerator.functioncall.clear();
    InterfaceParameterList _parameterlist = method.getParameterlist();
    EList<InterfaceParameterDeclare> _para = _parameterlist.getPara();
    for (final InterfaceParameterDeclare para : _para) {
      EList<AssignParameters> _assignparameters = method.getAssignparameters();
      for (final AssignParameters ass : _assignparameters) {
        String _paraname = para.getParaname();
        String _paraname_1 = ass.getParaname();
        boolean _equals = Objects.equal(_paraname, _paraname_1);
        if (_equals) {
          ArrayList<String> valuelist = new ArrayList<String>();
          ListDef _list = ass.getList();
          EList<List> _list_1 = _list.getList();
          for (final List l : _list_1) {
            NumValue _num = l.getNum();
            boolean _notEquals = (!Objects.equal(_num, null));
            if (_notEquals) {
              NumValue _num_1 = l.getNum();
              int _value = _num_1.getValue();
              String _string = Integer.valueOf(_value).toString();
              valuelist.add(_string);
            } else {
              Range _range = l.getRange();
              boolean _notEquals_1 = (!Objects.equal(_range, null));
              if (_notEquals_1) {
                Range _range_1 = l.getRange();
                NumValue _start = _range_1.getStart();
                ICompositeNode _node = NodeModelUtils.getNode(_start);
                String _tokenText = NodeModelUtils.getTokenText(_node);
                int start = Integer.parseInt(_tokenText);
                Range _range_2 = l.getRange();
                NumValue _end = _range_2.getEnd();
                ICompositeNode _node_1 = NodeModelUtils.getNode(_end);
                String _tokenText_1 = NodeModelUtils.getTokenText(_node_1);
                int end = Integer.parseInt(_tokenText_1);
                ExclusiveRange _doubleDotLessThan = new ExclusiveRange(start, (end + 1), true);
                for (final Integer i : _doubleDotLessThan) {
                  String _string_1 = i.toString();
                  valuelist.add(_string_1);
                }
              } else {
                ICompositeNode _node_2 = NodeModelUtils.getNode(l);
                String _tokenText_2 = NodeModelUtils.getTokenText(_node_2);
                valuelist.add(_tokenText_2);
              }
            }
          }
          ArrayList<String> newlist = new ArrayList<String>();
          int _size = ProcessConfigurationGenerator.functioncall.size();
          boolean _equals_1 = (_size == 0);
          if (_equals_1) {
            ProcessConfigurationGenerator.functioncall = valuelist;
          } else {
            for (final String ele1 : ProcessConfigurationGenerator.functioncall) {
              for (final String ele2 : valuelist) {
                newlist.add(((ele1 + ",") + ele2));
              }
            }
            ProcessConfigurationGenerator.functioncall.clear();
            ProcessConfigurationGenerator.functioncall = newlist;
          }
        }
      }
    }
    ArrayList<String> resultlist = new ArrayList<String>();
    for (final String ass_1 : ProcessConfigurationGenerator.functioncall) {
      boolean _checkParamConstraint = ProcessConfigurationGenerator.checkParamConstraint(method, ass_1);
      if (_checkParamConstraint) {
        resultlist.add(ass_1);
      }
    }
    ProcessConfigurationGenerator.functioncall = resultlist;
    FunctionName _functionname = method.getFunctionname();
    String _name = _functionname.getName();
    String _plus = (_name + ", Function call size: ");
    int _size_1 = ProcessConfigurationGenerator.functioncall.size();
    String _plus_1 = (_plus + Integer.valueOf(_size_1));
    System.out.println(_plus_1);
  }
  
  public static boolean checkParamConstraint(final Method method, final String paraassign) {
    final String[] assign = paraassign.split(",");
    Constraints _constraints = method.getConstraints();
    boolean _notEquals = (!Objects.equal(_constraints, null));
    if (_notEquals) {
      Constraints _constraints_1 = method.getConstraints();
      EList<Constraint> _constraint = _constraints_1.getConstraint();
      for (final Constraint con : _constraint) {
        InterfaceParameterList _parameterlist = method.getParameterlist();
        boolean _assignparavalueforconstraint = ProcessConfigurationGenerator.assignparavalueforconstraint(_parameterlist, assign, con);
        boolean _not = (!_assignparavalueforconstraint);
        if (_not) {
          return false;
        }
      }
    }
    return true;
  }
  
  public static boolean assignparavalueforconstraint(final InterfaceParameterList paralist, final String[] assign, final Constraint con) {
    Condition _condition = con.getCondition();
    Expression _expr = _condition.getExpr();
    String curexp = Statements.dispatchExpression(_expr);
    int i = 0;
    EList<InterfaceParameterDeclare> _para = paralist.getPara();
    for (final InterfaceParameterDeclare para : _para) {
      String _paraname = para.getParaname();
      String _plus = ("(" + _paraname);
      String _plus_1 = (_plus + ")");
      int _plusPlus = i++;
      String _get = assign[_plusPlus];
      String _string = _get.toString();
      String _replace = curexp.replace(_plus_1, _string);
      curexp = _replace;
    }
    Object _evaluateExpression = ProcessConfigurationGenerator.evaluateExpression(curexp);
    String _string_1 = _evaluateExpression.toString();
    boolean _equals = _string_1.equals("false");
    if (_equals) {
      return false;
    } else {
      return true;
    }
  }
  
  public static String genContructorParameter(final ParameterList paralist) {
    String result = "";
    int i = 0;
    EList<ParameterAssign> _para = paralist.getPara();
    for (final ParameterAssign para : _para) {
      {
        String _result = result;
        scheduling.dsl.String _type = para.getType();
        String _plus = (_type + " ");
        result = (_result + _plus);
        EList<ParameterName> _paraname = para.getParaname();
        ParameterName _get = _paraname.get(0);
        String _name = _get.getName();
        String _trim = _name.trim();
        final String defval = ProcessConfigurationGenerator.defvalue.get(_trim);
        int index = 0;
        EList<ParameterName> _paraname_1 = para.getParaname();
        for (final ParameterName pname : _paraname_1) {
          {
            String _result_1 = result;
            String _name_1 = pname.getName();
            String _plus_1 = (_name_1 + "=");
            String _plus_2 = (_plus_1 + defval);
            result = (_result_1 + _plus_2);
            index++;
            EList<ParameterName> _paraname_2 = para.getParaname();
            int _size = _paraname_2.size();
            boolean _lessThan = (index < _size);
            if (_lessThan) {
              String _result_2 = result;
              result = (_result_2 + "; ");
            }
          }
        }
        i++;
        EList<ParameterAssign> _para_1 = paralist.getPara();
        int _size = _para_1.size();
        boolean _lessThan = (i < _size);
        if (_lessThan) {
          String _result_1 = result;
          result = (_result_1 + "; ");
        }
      }
    }
    return result;
  }
  
  public static CharSequence ProcessConfigurationtoJavaCode(final ProcessConfiguration processconfiguration, final String assignment, final int index) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("def process ");
    String _name = processconfiguration.getName();
    _builder.append(_name, "");
    _builder.append("_");
    _builder.append(index, "");
    _builder.append(" ");
    {
      String _parent = processconfiguration.getParent();
      boolean _notEquals = (!Objects.equal(_parent, null));
      if (_notEquals) {
        _builder.append("refines ");
        String _parent_1 = processconfiguration.getParent();
        _builder.append(_parent_1, "");
      }
    }
    _builder.append(" {\t\t\t");
    _builder.newLineIfNotEmpty();
    {
      Attribute _attribute = processconfiguration.getAttribute();
      boolean _notEquals_1 = (!Objects.equal(_attribute, null));
      if (_notEquals_1) {
        _builder.append("\t");
        _builder.append("attribute {");
        _builder.newLine();
        {
          Attribute _attribute_1 = processconfiguration.getAttribute();
          EList<AttDef> _att = _attribute_1.getAtt();
          for(final AttDef att : _att) {
            {
              Value _default = att.getDefault();
              boolean _notEquals_2 = (!Objects.equal(_default, null));
              if (_notEquals_2) {
                {
                  Value _default_1 = att.getDefault();
                  NumValue _num = _default_1.getNum();
                  boolean _notEquals_3 = (!Objects.equal(_num, null));
                  if (_notEquals_3) {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("var ");
                    scheduling.dsl.String _type = att.getType();
                    _builder.append(_type, "\t\t");
                    _builder.append(" ");
                    String _name_1 = att.getName();
                    _builder.append(_name_1, "\t\t");
                    _builder.append(" = ");
                    Value _default_2 = att.getDefault();
                    NumValue _num_1 = _default_2.getNum();
                    int _value = _num_1.getValue();
                    _builder.append(_value, "\t\t");
                    _builder.append(" ;  ");
                    _builder.newLineIfNotEmpty();
                  } else {
                    {
                      Value _default_3 = att.getDefault();
                      BoolValue _bool = _default_3.getBool();
                      boolean _notEquals_4 = (!Objects.equal(_bool, null));
                      if (_notEquals_4) {
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("var ");
                        scheduling.dsl.String _type_1 = att.getType();
                        _builder.append(_type_1, "\t\t");
                        _builder.append(" ");
                        String _name_2 = att.getName();
                        _builder.append(_name_2, "\t\t");
                        _builder.append(" = ");
                        Value _default_4 = att.getDefault();
                        BoolValue _bool_1 = _default_4.getBool();
                        String _value_1 = _bool_1.getValue();
                        _builder.append(_value_1, "\t\t");
                        _builder.append(" ;");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("var ");
                        scheduling.dsl.String _type_2 = att.getType();
                        _builder.append(_type_2, "\t\t");
                        _builder.append(" ");
                        String _name_3 = att.getName();
                        _builder.append(_name_3, "\t\t");
                        _builder.append(" ;");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                  }
                }
              } else {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("var ");
                scheduling.dsl.String _type_3 = att.getType();
                _builder.append(_type_3, "\t\t");
                _builder.append(" ");
                String _name_4 = att.getName();
                _builder.append(_name_4, "\t\t");
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
    _builder.newLine();
    {
      DefBehaviorProc _defbehaviorproc = processconfiguration.getDefbehaviorproc();
      ProcessBehaviors _processbehaviors = _defbehaviorproc.getProcessbehaviors();
      boolean _notEquals_5 = (!Objects.equal(_processbehaviors, null));
      if (_notEquals_5) {
        {
          DefBehaviorProc _defbehaviorproc_1 = processconfiguration.getDefbehaviorproc();
          ProcessBehaviors _processbehaviors_1 = _defbehaviorproc_1.getProcessbehaviors();
          EList<ProcessBehavior> _processbehavior = _processbehaviors_1.getProcessbehavior();
          for(final ProcessBehavior proc : _processbehavior) {
            {
              Constructor _constructor = proc.getConstructor();
              boolean _notEquals_6 = (!Objects.equal(_constructor, null));
              if (_notEquals_6) {
                _builder.append("\t");
                _builder.append("proctype ");
                Constructor _constructor_1 = proc.getConstructor();
                String _processname = _constructor_1.getProcessname();
                _builder.append(_processname, "\t");
                _builder.append(" (");
                {
                  Constructor _constructor_2 = proc.getConstructor();
                  ParameterList _paralist = _constructor_2.getParalist();
                  boolean _notEquals_7 = (!Objects.equal(_paralist, null));
                  if (_notEquals_7) {
                    Constructor _constructor_3 = proc.getConstructor();
                    ParameterList _paralist_1 = _constructor_3.getParalist();
                    String _genContructorParameter = ProcessConfigurationGenerator.genContructorParameter(_paralist_1);
                    _builder.append(_genContructorParameter, "\t");
                  }
                }
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                {
                  Constructor _constructor_4 = proc.getConstructor();
                  ParameterList _paralist_2 = _constructor_4.getParalist();
                  boolean _notEquals_8 = (!Objects.equal(_paralist_2, null));
                  if (_notEquals_8) {
                    {
                      Constructor _constructor_5 = proc.getConstructor();
                      ParameterList _paralist_3 = _constructor_5.getParalist();
                      EList<ParameterAssign> _para = _paralist_3.getPara();
                      for(final ParameterAssign paras : _para) {
                        {
                          EList<ParameterName> _paraname = paras.getParaname();
                          for(final ParameterName pname : _paraname) {
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("this.");
                            String _name_5 = pname.getName();
                            _builder.append(_name_5, "\t\t");
                            _builder.append(" = ");
                            String _name_6 = pname.getName();
                            _builder.append(_name_6, "\t\t");
                            _builder.append(" ;");
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
              }
            }
          }
        }
      } else {
        {
          DefBehaviorProc _defbehaviorproc_2 = processconfiguration.getDefbehaviorproc();
          ProcType _proctype = _defbehaviorproc_2.getProctype();
          EList<ProcessType> _proctype_1 = _proctype.getProctype();
          for(final ProcessType proctype : _proctype_1) {
            {
              EList<ProcessBehavior> _processbehavior_1 = proctype.getProcessbehavior();
              for(final ProcessBehavior procbehave : _processbehavior_1) {
                {
                  Constructor _constructor_6 = procbehave.getConstructor();
                  boolean _notEquals_9 = (!Objects.equal(_constructor_6, null));
                  if (_notEquals_9) {
                    _builder.append("\t");
                    _builder.append("proctype ");
                    Constructor _constructor_7 = procbehave.getConstructor();
                    String _processname_1 = _constructor_7.getProcessname();
                    _builder.append(_processname_1, "\t");
                    _builder.append(" (");
                    {
                      Constructor _constructor_8 = procbehave.getConstructor();
                      ParameterList _paralist_4 = _constructor_8.getParalist();
                      boolean _notEquals_10 = (!Objects.equal(_paralist_4, null));
                      if (_notEquals_10) {
                        Constructor _constructor_9 = procbehave.getConstructor();
                        ParameterList _paralist_5 = _constructor_9.getParalist();
                        String _genContructorParameter_1 = ProcessConfigurationGenerator.genContructorParameter(_paralist_5);
                        _builder.append(_genContructorParameter_1, "\t");
                      }
                    }
                    _builder.append(") {");
                    _builder.newLineIfNotEmpty();
                    {
                      Constructor _constructor_10 = procbehave.getConstructor();
                      ParameterList _paralist_6 = _constructor_10.getParalist();
                      EList<ParameterAssign> _para_1 = _paralist_6.getPara();
                      for(final ParameterAssign paras_1 : _para_1) {
                        {
                          EList<ParameterName> _paraname_1 = paras_1.getParaname();
                          for(final ParameterName pname_1 : _paraname_1) {
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("this.");
                            String _name_7 = pname_1.getName();
                            _builder.append(_name_7, "\t\t");
                            _builder.append(" = ");
                            String _name_8 = pname_1.getName();
                            _builder.append(_name_8, "\t\t");
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
              }
            }
          }
        }
      }
    }
    _builder.append("}\t\t");
    _builder.newLine();
    {
      ProcessConfig _processconfig = processconfiguration.getProcessconfig();
      boolean _notEquals_11 = (!Objects.equal(_processconfig, null));
      if (_notEquals_11) {
        _builder.append("config {");
        _builder.newLine();
        {
          ProcessConfig _processconfig_1 = processconfiguration.getProcessconfig();
          EList<ConfigProcess> _procinit = _processconfig_1.getProcinit();
          for(final ConfigProcess config : _procinit) {
            _builder.append("\t");
            ICompositeNode _node = NodeModelUtils.getNode(config);
            String _tokenText = NodeModelUtils.getTokenText(_node);
            _builder.append(_tokenText, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("}");
        _builder.newLine();
      }
    }
    {
      ProcessInit _processinit = processconfiguration.getProcessinit();
      boolean _notEquals_12 = (!Objects.equal(_processinit, null));
      if (_notEquals_12) {
        _builder.append("init {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("[");
        _builder.append(assignment, "\t");
        _builder.append("]");
        _builder.newLineIfNotEmpty();
        _builder.append("}\t\t\t\t\t\t");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public static double genConfiguration(final ProcessConfiguration processconfiguration) {
    double _xblockexpression = (double) 0;
    {
      ProcessConfigurationGenerator.assignOk.clear();
      ProcessConfigurationGenerator.assignNotOk.clear();
      ProcessConfigurationGenerator.assignConfigList.clear();
      ArrayList<String> _arrayList = new ArrayList<String>();
      ProcessConfigurationGenerator.attlist = _arrayList;
      final double startTime = System.currentTimeMillis();
      ProcessConfigurationGenerator.initAttributeValue(processconfiguration);
      int _size = ProcessConfigurationGenerator.attlist.size();
      boolean _equals = (_size == 0);
      if (_equals) {
        String result = "";
        ProcessInit _processinit = processconfiguration.getProcessinit();
        EList<Set> _order = _processinit.getOrder();
        for (final Set set : _order) {
          int _length = result.length();
          boolean _equals_1 = (_length == 0);
          if (_equals_1) {
            ICompositeNode _node = NodeModelUtils.getNode(set);
            String _tokenText = NodeModelUtils.getTokenText(_node);
            result = _tokenText;
          } else {
            String _result = result;
            ICompositeNode _node_1 = NodeModelUtils.getNode(set);
            String _tokenText_1 = NodeModelUtils.getTokenText(_node_1);
            String _plus = (", " + _tokenText_1);
            result = (_result + _plus);
          }
        }
        ProcessConfigurationGenerator.assignConfigList.add(result);
      } else {
        ProcessInit _processinit_1 = processconfiguration.getProcessinit();
        ProcessConfigurationGenerator.genconfiguration(_processinit_1);
        ProcessConfigurationGenerator.assignConfigList.clear();
        int _size_1 = ProcessConfigurationGenerator.configList.size();
        boolean _greaterThan = (_size_1 > 0);
        if (_greaterThan) {
          int _size_2 = ProcessConfigurationGenerator.configList.size();
          ProcessConfigurationGenerator.combineConfig(0, _size_2);
        }
      }
      long _currentTimeMillis = System.currentTimeMillis();
      double _minus = (_currentTimeMillis - startTime);
      _xblockexpression = ProcessConfigurationGenerator.seconds = _minus;
    }
    return _xblockexpression;
  }
  
  public static CharSequence genProcessConfiguration(final ProcessConfiguration processconfiguration, final String config, final int index) {
    return ProcessConfigurationGenerator.ProcessConfigurationtoJavaCode(processconfiguration, config, index);
  }
  
  public static Object combineConfig(final int index, final int setsize) {
    Object _xifexpression = null;
    if ((index < setsize)) {
      Object _xblockexpression = null;
      {
        int _size = ProcessConfigurationGenerator.assignConfigList.size();
        boolean _equals = (_size == 0);
        if (_equals) {
          ArrayList<String> _get = ProcessConfigurationGenerator.configList.get(index);
          ProcessConfigurationGenerator.assignConfigList = _get;
        } else {
          ArrayList<String> newass = new ArrayList<String>();
          for (final String pre : ProcessConfigurationGenerator.assignConfigList) {
            ArrayList<String> _get_1 = ProcessConfigurationGenerator.configList.get(index);
            for (final String pos : _get_1) {
              newass.add(((pre + ",") + pos));
            }
          }
          ProcessConfigurationGenerator.assignConfigList = newass;
        }
        _xblockexpression = ProcessConfigurationGenerator.combineConfig((index + 1), setsize);
      }
      _xifexpression = _xblockexpression;
    }
    return _xifexpression;
  }
  
  public static void genconfiguration(final ProcessInit pinit) {
    ProcessConfigurationGenerator.configList.clear();
    int index = 0;
    EList<Set> _order = pinit.getOrder();
    for (final Set set : _order) {
      {
        ArrayList<String> _arrayList = new ArrayList<String>();
        ProcessConfigurationGenerator.elementList = _arrayList;
        ProcessConfigurationGenerator.genSet(set);
        ArrayList<String> newelementlist = new ArrayList<String>();
        for (final String ele : ProcessConfigurationGenerator.elementList) {
          newelementlist.add((("{" + ele) + "}"));
        }
        ProcessConfigurationGenerator.configList.add(newelementlist);
      }
    }
  }
  
  public static void genSet(final Set set) {
    EList<Element> _element = set.getElement();
    for (final Element element : _element) {
      {
        Constructor process = ProcessConfigurationGenerator.findProcessConstructor(element);
        boolean _notEquals = (!Objects.equal(process, null));
        if (_notEquals) {
          ArrayList<String> oldelementlist = new ArrayList<String>();
          ArrayList<String> newelementlist = new ArrayList<String>();
          oldelementlist = ProcessConfigurationGenerator.elementList;
          ArrayList<String> _arrayList = new ArrayList<String>();
          ProcessConfigurationGenerator.elementList = _arrayList;
          ProcessConfigurationGenerator.assignProcessAttribute(process, element, 0, "", "");
          int _size = oldelementlist.size();
          boolean _equals = (_size == 0);
          if (_equals) {
            for (final String ele : ProcessConfigurationGenerator.elementList) {
              newelementlist.add(ele);
            }
          } else {
            for (final String oldele : oldelementlist) {
              for (final String newele : ProcessConfigurationGenerator.elementList) {
                newelementlist.add(((oldele + ",") + newele));
              }
            }
          }
          ProcessConfigurationGenerator.elementList = newelementlist;
        }
      }
    }
  }
  
  public static int getAttributeIndex(final Constructor process, final String attname) {
    int attindex = 0;
    ParameterList _paralist = process.getParalist();
    EList<ParameterAssign> _para = _paralist.getPara();
    for (final ParameterAssign pr : _para) {
      EList<ParameterName> _paraname = pr.getParaname();
      for (final ParameterName att : _paraname) {
        String _name = att.getName();
        boolean _equals = Objects.equal(_name, attname);
        if (_equals) {
          return attindex;
        } else {
          attindex++;
        }
      }
    }
    return (-1);
  }
  
  public static void assignProcessAttribute(final Constructor process, final Element element, final int index, final String sassign, final String parastr) {
    final String attname = ProcessConfigurationGenerator.attlist.get(index);
    final int processattindex = ProcessConfigurationGenerator.getAttributeIndex(process, attname);
    final String defval = ProcessConfigurationGenerator.defvalue.get(attname);
    String strassign = sassign;
    if ((processattindex == (-1))) {
      if ((index == (ProcessConfigurationGenerator.attcount - 1))) {
        ProcessConfigurationGenerator.checkAttributeConstraint(process, strassign, parastr);
      } else {
        if ((index == 0)) {
          String _strassign = strassign;
          strassign = (_strassign + defval);
        } else {
          String _strassign_1 = strassign;
          strassign = (_strassign_1 + ("," + defval));
        }
        int _parseInt = Integer.parseInt(defval);
        ProcessConfigurationGenerator.assign[index] = _parseInt;
        ProcessConfigurationGenerator.assignProcessAttribute(process, element, (index + 1), strassign, parastr);
        strassign = sassign;
      }
    } else {
      EList<Value> _paraAssign = element.getParaAssign();
      int _size = _paraAssign.size();
      boolean _equals = (_size == 0);
      if (_equals) {
        String _processname = process.getProcessname();
        String _plus = (_processname + "()");
        ProcessConfigurationGenerator.elementList.add(_plus);
        return;
      }
      EList<Value> _paraAssign_1 = element.getParaAssign();
      Value para = _paraAssign_1.get(processattindex);
      String _nondef = para.getNondef();
      boolean _equals_1 = Objects.equal(_nondef, null);
      if (_equals_1) {
        if ((index == 0)) {
          ICompositeNode _node = NodeModelUtils.getNode(para);
          String _tokenText = NodeModelUtils.getTokenText(_node);
          strassign = _tokenText;
        } else {
          int _length = strassign.length();
          boolean _greaterThan = (_length > 0);
          if (_greaterThan) {
            String _strassign_2 = strassign;
            ICompositeNode _node_1 = NodeModelUtils.getNode(para);
            String _tokenText_1 = NodeModelUtils.getTokenText(_node_1);
            String _plus_1 = ("," + _tokenText_1);
            strassign = (_strassign_2 + _plus_1);
          } else {
            ICompositeNode _node_2 = NodeModelUtils.getNode(para);
            String _tokenText_2 = NodeModelUtils.getTokenText(_node_2);
            strassign = _tokenText_2;
          }
        }
        ICompositeNode _node_3 = NodeModelUtils.getNode(para);
        String nextpara = NodeModelUtils.getTokenText(_node_3);
        NumValue _num = para.getNum();
        int _value = _num.getValue();
        ProcessConfigurationGenerator.assign[index] = _value;
        NumValue _num_1 = para.getNum();
        boolean _notEquals = (!Objects.equal(_num_1, null));
        if (_notEquals) {
          NumValue _num_2 = para.getNum();
          int _value_1 = _num_2.getValue();
          ProcessConfigurationGenerator.assign[index] = _value_1;
        } else {
        }
        if ((index == (ProcessConfigurationGenerator.attcount - 1))) {
          int _length_1 = parastr.length();
          boolean _greaterThan_1 = (_length_1 > 0);
          if (_greaterThan_1) {
            ProcessConfigurationGenerator.checkAttributeConstraint(process, strassign, ((parastr + ",") + nextpara));
          } else {
            ProcessConfigurationGenerator.checkAttributeConstraint(process, strassign, nextpara);
          }
        } else {
          int _length_2 = parastr.length();
          boolean _greaterThan_2 = (_length_2 > 0);
          if (_greaterThan_2) {
            ProcessConfigurationGenerator.assignProcessAttribute(process, element, (index + 1), strassign, ((parastr + ",") + nextpara));
          } else {
            ProcessConfigurationGenerator.assignProcessAttribute(process, element, (index + 1), strassign, nextpara);
          }
          strassign = sassign;
        }
      } else {
        final String oldassign = strassign;
        HashSet<Integer> _get = ProcessConfigurationGenerator.attval.get(Integer.valueOf(index));
        for (final Integer v : _get) {
          {
            if ((index == 0)) {
              strassign = (oldassign + v);
            } else {
              strassign = ((oldassign + ",") + v);
            }
            ProcessConfigurationGenerator.assign[index] = (v).intValue();
            if ((index == (ProcessConfigurationGenerator.attcount - 1))) {
              int _length_3 = parastr.length();
              boolean _greaterThan_3 = (_length_3 > 0);
              if (_greaterThan_3) {
                ProcessConfigurationGenerator.checkAttributeConstraint(process, strassign, ((parastr + ",") + v));
              } else {
                ProcessConfigurationGenerator.checkAttributeConstraint(process, strassign, ("" + v));
              }
            } else {
              int _length_4 = parastr.length();
              boolean _greaterThan_4 = (_length_4 > 0);
              if (_greaterThan_4) {
                ProcessConfigurationGenerator.assignProcessAttribute(process, element, (index + 1), strassign, ((parastr + ",") + v));
              } else {
                ProcessConfigurationGenerator.assignProcessAttribute(process, element, (index + 1), strassign, ("" + v));
              }
              strassign = sassign;
            }
          }
        }
      }
    }
  }
  
  public static boolean checkAttributeConstraintForProcess(final Constructor process, final String strassign) {
    DefBehaviorProc _defbehaviorproc = Data.procConfig.getDefbehaviorproc();
    ProcType _proctype = _defbehaviorproc.getProctype();
    boolean _notEquals = (!Objects.equal(_proctype, null));
    if (_notEquals) {
      DefBehaviorProc _defbehaviorproc_1 = Data.procConfig.getDefbehaviorproc();
      ProcType _proctype_1 = _defbehaviorproc_1.getProctype();
      EList<ProcessType> _proctype_2 = _proctype_1.getProctype();
      for (final ProcessType proctype : _proctype_2) {
        String _name = proctype.getName();
        String _processname = process.getProcessname();
        boolean _equals = _name.equals(_processname);
        if (_equals) {
          Constraints _constraints = proctype.getConstraints();
          boolean _notEquals_1 = (!Objects.equal(_constraints, null));
          if (_notEquals_1) {
            Constraints _constraints_1 = proctype.getConstraints();
            EList<Constraint> _constraint = _constraints_1.getConstraint();
            for (final Constraint con : _constraint) {
              boolean _assignvalueforconstraint = ProcessConfigurationGenerator.assignvalueforconstraint(con, strassign);
              boolean _not = (!_assignvalueforconstraint);
              if (_not) {
                return false;
              }
            }
          }
          return true;
        }
      }
    }
    return true;
  }
  
  public static boolean checkAttributeConstraint(final Constructor process, final String strassign, final String paraassign) {
    boolean _contains = ProcessConfigurationGenerator.assignOk.contains(strassign);
    if (_contains) {
      boolean _checkAttributeConstraintForProcess = ProcessConfigurationGenerator.checkAttributeConstraintForProcess(process, strassign);
      if (_checkAttributeConstraintForProcess) {
        String _processname = process.getProcessname();
        String _plus = (_processname + "(");
        String _plus_1 = (_plus + paraassign);
        String _plus_2 = (_plus_1 + ")");
        ProcessConfigurationGenerator.elementList.add(_plus_2);
        return true;
      }
    }
    boolean _contains_1 = ProcessConfigurationGenerator.assignNotOk.contains(strassign);
    if (_contains_1) {
      return false;
    }
    Attribute _attribute = Data.procConfig.getAttribute();
    boolean _notEquals = (!Objects.equal(_attribute, null));
    if (_notEquals) {
      Attribute _attribute_1 = Data.procConfig.getAttribute();
      Constraints _constraints = _attribute_1.getConstraints();
      boolean _notEquals_1 = (!Objects.equal(_constraints, null));
      if (_notEquals_1) {
        Attribute _attribute_2 = Data.procConfig.getAttribute();
        Constraints _constraints_1 = _attribute_2.getConstraints();
        EList<Constraint> _constraint = _constraints_1.getConstraint();
        for (final Constraint con : _constraint) {
          boolean _assignvalueforconstraint = ProcessConfigurationGenerator.assignvalueforconstraint(con, strassign);
          boolean _not = (!_assignvalueforconstraint);
          if (_not) {
            ProcessConfigurationGenerator.assignNotOk.add(strassign);
            return false;
          }
        }
      }
    }
    boolean _checkAttributeConstraintForProcess_1 = ProcessConfigurationGenerator.checkAttributeConstraintForProcess(process, strassign);
    if (_checkAttributeConstraintForProcess_1) {
      String _processname_1 = process.getProcessname();
      String _plus_3 = (_processname_1 + "(");
      String _plus_4 = (_plus_3 + paraassign);
      String _plus_5 = (_plus_4 + ")");
      ProcessConfigurationGenerator.elementList.add(_plus_5);
    }
    ProcessConfigurationGenerator.assignOk.add(strassign);
    return true;
  }
  
  public static boolean assignvalueforconstraint(final Constraint con, final String strasign) {
    Condition _condition = con.getCondition();
    Expression _expr = _condition.getExpr();
    String curexp = Statements.dispatchExpression(_expr);
    ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, ProcessConfigurationGenerator.attcount, true);
    for (final Integer i : _doubleDotLessThan) {
      String _get = ProcessConfigurationGenerator.attlist.get((i).intValue());
      String _plus = ("(" + _get);
      String _plus_1 = (_plus + ")");
      int _get_1 = ProcessConfigurationGenerator.assign[(i).intValue()];
      String _string = Integer.valueOf(_get_1).toString();
      String _replace = curexp.replace(_plus_1, _string);
      curexp = _replace;
    }
    Object _evaluateExpression = ProcessConfigurationGenerator.evaluateExpression(curexp);
    String _string_1 = _evaluateExpression.toString();
    boolean _equals = _string_1.equals("false");
    if (_equals) {
      return false;
    } else {
      return true;
    }
  }
  
  public static Constructor findProcessConstructor(final Element e) {
    DefBehaviorProc _defbehaviorproc = Data.procConfig.getDefbehaviorproc();
    ProcessBehaviors _processbehaviors = _defbehaviorproc.getProcessbehaviors();
    boolean _notEquals = (!Objects.equal(_processbehaviors, null));
    if (_notEquals) {
      DefBehaviorProc _defbehaviorproc_1 = Data.procConfig.getDefbehaviorproc();
      ProcessBehaviors _processbehaviors_1 = _defbehaviorproc_1.getProcessbehaviors();
      EList<ProcessBehavior> _processbehavior = _processbehaviors_1.getProcessbehavior();
      for (final ProcessBehavior procbehave : _processbehavior) {
        Constructor _constructor = procbehave.getConstructor();
        boolean _notEquals_1 = (!Objects.equal(_constructor, null));
        if (_notEquals_1) {
          Constructor _constructor_1 = procbehave.getConstructor();
          String _processname = _constructor_1.getProcessname();
          scheduling.dsl.Process _process = e.getProcess();
          String _name = _process.getName();
          boolean _equals = Objects.equal(_processname, _name);
          if (_equals) {
            return procbehave.getConstructor();
          }
        }
      }
    } else {
      DefBehaviorProc _defbehaviorproc_2 = Data.procConfig.getDefbehaviorproc();
      ProcType _proctype = _defbehaviorproc_2.getProctype();
      EList<ProcessType> _proctype_1 = _proctype.getProctype();
      for (final ProcessType proctype : _proctype_1) {
        EList<ProcessBehavior> _processbehavior_1 = proctype.getProcessbehavior();
        for (final ProcessBehavior procbehave_1 : _processbehavior_1) {
          Constructor _constructor_2 = procbehave_1.getConstructor();
          boolean _notEquals_2 = (!Objects.equal(_constructor_2, null));
          if (_notEquals_2) {
            Constructor _constructor_3 = procbehave_1.getConstructor();
            String _processname_1 = _constructor_3.getProcessname();
            scheduling.dsl.Process _process_1 = e.getProcess();
            String _name_1 = _process_1.getName();
            boolean _equals_1 = Objects.equal(_processname_1, _name_1);
            if (_equals_1) {
              return procbehave_1.getConstructor();
            }
          }
        }
      }
    }
    return null;
  }
}
