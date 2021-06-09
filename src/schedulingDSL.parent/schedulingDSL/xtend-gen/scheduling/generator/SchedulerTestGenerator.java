package scheduling.generator;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import scheduling.dsl.ASSIGN;
import scheduling.dsl.Action;
import scheduling.dsl.BoolValue;
import scheduling.dsl.CTL_AT;
import scheduling.dsl.Condition;
import scheduling.dsl.Config;
import scheduling.dsl.Configuration;
import scheduling.dsl.ConstDec;
import scheduling.dsl.DeclareEvent;
import scheduling.dsl.DeclareList;
import scheduling.dsl.Define;
import scheduling.dsl.ELABEL;
import scheduling.dsl.ELSEs;
import scheduling.dsl.ENAME;
import scheduling.dsl.EnumDec;
import scheduling.dsl.EnumType;
import scheduling.dsl.Event;
import scheduling.dsl.EventFunction;
import scheduling.dsl.Expression;
import scheduling.dsl.Flow;
import scheduling.dsl.FuncPara;
import scheduling.dsl.Function;
import scheduling.dsl.GUARD;
import scheduling.dsl.IFs;
import scheduling.dsl.ListElement;
import scheduling.dsl.NumValue;
import scheduling.dsl.OneDec;
import scheduling.dsl.Options;
import scheduling.dsl.ParaListItem;
import scheduling.dsl.RTCTL;
import scheduling.dsl.RefList;
import scheduling.dsl.RefListItem;
import scheduling.dsl.Rule;
import scheduling.dsl.SKIP;
import scheduling.dsl.SequenceAction;
import scheduling.dsl.TypeName;
import scheduling.dsl.VDec;
import scheduling.dsl.Value;
import scheduling.dsl.VarDec;
import scheduling.dsl.VarInit;
import scheduling.generator.Statements;
import scheduling.generator.Test;

@SuppressWarnings("all")
public class SchedulerTestGenerator {
  public static int testCount = 0;
  
  private static ArrayList<String> event = new ArrayList<String>();
  
  public static HashMap<String, String> cst = new HashMap<String, String>();
  
  public static HashMap<String, ArrayList<String>> defrefval = new HashMap<String, ArrayList<String>>();
  
  public static ArrayList<String> funcparaval = new ArrayList<String>();
  
  public static HashMap<String, String> mapfuncparamval = new HashMap<String, String>();
  
  public static HashMap<String, ArrayList<String>> mapenumval = new HashMap<String, ArrayList<String>>();
  
  public static HashMap<String, String> mapenumtype = new HashMap<String, String>();
  
  public static String getEnumType(final String en) {
    boolean _containsKey = SchedulerTestGenerator.mapenumtype.containsKey(en);
    if (_containsKey) {
      return SchedulerTestGenerator.mapenumtype.get(en);
    }
    return "";
  }
  
  public static String getEnumVal(final String type, final String e) {
    boolean _containsKey = SchedulerTestGenerator.mapenumval.containsKey(type);
    if (_containsKey) {
      ArrayList<String> _get = SchedulerTestGenerator.mapenumval.get(type);
      int _indexOf = _get.indexOf(e);
      return Integer.valueOf(_indexOf).toString();
    }
    return e;
  }
  
  public static String getEnumVal(final String e) {
    Collection<ArrayList<String>> _values = SchedulerTestGenerator.mapenumval.values();
    for (final ArrayList<String> elist : _values) {
      {
        int index = elist.indexOf(e);
        if ((index >= 0)) {
          return Integer.valueOf(index).toString();
        }
      }
    }
    return (("(" + e) + ")");
  }
  
  public static String getReference(final ListElement lval) {
    String _id = lval.getId();
    boolean _notEquals = (!Objects.equal(_id, null));
    if (_notEquals) {
      String _id_1 = lval.getId();
      boolean _containsValue = SchedulerTestGenerator.cst.containsValue(_id_1);
      if (_containsValue) {
        String _name = lval.getName();
        ArrayList<String> _get = SchedulerTestGenerator.defrefval.get(_name);
        String _id_2 = lval.getId();
        String _get_1 = SchedulerTestGenerator.cst.get(_id_2);
        int _parseInt = Integer.parseInt(_get_1);
        int _minus = (_parseInt - 1);
        return _get.get(_minus);
      }
      String _id_3 = lval.getId();
      boolean _containsKey = SchedulerTestGenerator.mapfuncparamval.containsKey(_id_3);
      if (_containsKey) {
        String _name_1 = lval.getName();
        ArrayList<String> _get_2 = SchedulerTestGenerator.defrefval.get(_name_1);
        String _id_4 = lval.getId();
        String _get_3 = SchedulerTestGenerator.mapfuncparamval.get(_id_4);
        int _parseInt_1 = Integer.parseInt(_get_3);
        int _minus_1 = (_parseInt_1 - 1);
        String result = _get_2.get(_minus_1);
        return result;
      }
    } else {
      String _name_2 = lval.getName();
      ArrayList<String> _get_4 = SchedulerTestGenerator.defrefval.get(_name_2);
      int _num = lval.getNum();
      int _minus_2 = (_num - 1);
      return _get_4.get(_minus_2);
    }
    return null;
  }
  
  public static String getValue(final String v) {
    boolean _containsKey = SchedulerTestGenerator.cst.containsKey(v);
    if (_containsKey) {
      return SchedulerTestGenerator.cst.get(v);
    }
    boolean _containsKey_1 = SchedulerTestGenerator.mapfuncparamval.containsKey(v);
    if (_containsKey_1) {
      return SchedulerTestGenerator.mapfuncparamval.get(v);
    }
    return SchedulerTestGenerator.getEnumVal(v);
  }
  
  public static CharSequence TesttoPromelaCode() {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _notEquals = (!Objects.equal(Test.specification, null));
      if (_notEquals) {
        {
          Define _define = Test.specification.getDefine();
          boolean _notEquals_1 = (!Objects.equal(_define, null));
          if (_notEquals_1) {
            _builder.append("\t");
            Define _define_1 = Test.specification.getDefine();
            SchedulerTestGenerator.initdefvalue(_define_1);
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("/* Generate checking code*/\t\t");
        _builder.newLine();
        _builder.append("/* 1 variables */\t\t\t\t");
        _builder.newLine();
        {
          DeclareList _decl_lst = Test.specification.getDecl_lst();
          boolean _notEquals_2 = (!Objects.equal(_decl_lst, null));
          if (_notEquals_2) {
            _builder.append("\t");
            SchedulerTestGenerator.cst.clear();
            _builder.append("\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            SchedulerTestGenerator.mapenumval.clear();
            _builder.append("\t\t");
            _builder.newLineIfNotEmpty();
            {
              DeclareList _decl_lst_1 = Test.specification.getDecl_lst();
              EList<OneDec> _dec = _decl_lst_1.getDec();
              for(final OneDec dec : _dec) {
                _builder.append("\t");
                String _genVariable = SchedulerTestGenerator.genVariable(dec);
                _builder.append(_genVariable, "\t");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
        _builder.append("\t");
        _builder.append("int _test_depth = ");
        int _testDepth = SchedulerTestGenerator.getTestDepth();
        _builder.append(_testDepth, "\t");
        _builder.append("\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("/* 2 proctype for each event*/");
        _builder.newLine();
        {
          EList<DeclareEvent> _event = Test.specification.getEvent();
          for(final DeclareEvent e : _event) {
            _builder.append("\t");
            Define _define_2 = Test.specification.getDefine();
            String _genProctype = SchedulerTestGenerator.genProctype(e, _define_2);
            _builder.append(_genProctype, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.newLine();
        _builder.append("/* 3 generate verication cases */");
        _builder.newLine();
        _builder.append("\t");
        CharSequence _generateVerificationCase = SchedulerTestGenerator.generateVerificationCase();
        _builder.append(_generateVerificationCase, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("/* 4 generate run test code */");
        _builder.newLine();
        _builder.append("\t");
        CharSequence _genRunVerificationCase = SchedulerTestGenerator.genRunVerificationCase();
        _builder.append(_genRunVerificationCase, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        {
          boolean _notEquals_3 = (!Objects.equal(Test.verify, null));
          if (_notEquals_3) {
            _builder.append("/* 5 verify */");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("verify {");
            _builder.newLine();
            _builder.append("\t\t");
            {
              CTL_AT _at = Test.verify.getAt();
              boolean _notEquals_4 = (!Objects.equal(_at, null));
              if (_notEquals_4) {
                _builder.append("@");
                CTL_AT _at_1 = Test.verify.getAt();
                Expression _cond = _at_1.getCond();
                String _dispatchExpression = Statements.dispatchExpression(_cond);
                _builder.append(_dispatchExpression, "\t\t");
                _builder.append(" : ");
              }
            }
            RTCTL _formula = Test.verify.getFormula();
            String _dispatchRTCTLExpression = Statements.dispatchRTCTLExpression(_formula);
            _builder.append(_dispatchRTCTLExpression, "\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    return _builder;
  }
  
  public static int getTestDepth() {
    int depth = 0;
    boolean _notEquals = (!Objects.equal(Test.scenarios, null));
    if (_notEquals) {
      EList<Flow> _flow = Test.scenarios.getFlow();
      boolean _notEquals_1 = (!Objects.equal(_flow, null));
      if (_notEquals_1) {
        EList<Flow> _flow_1 = Test.scenarios.getFlow();
        for (final Flow f : _flow_1) {
          EList<ENAME> _event = f.getEvent();
          int _size = _event.size();
          boolean _lessThan = (depth < _size);
          if (_lessThan) {
            EList<ENAME> _event_1 = f.getEvent();
            int _size_1 = _event_1.size();
            int _plus = (_size_1 + 3);
            depth = _plus;
          }
        }
      }
    } else {
      boolean _notEquals_2 = (!Objects.equal(Test.permutation, null));
      if (_notEquals_2) {
        int _step = Test.permutation.getStep();
        boolean _lessThan_1 = (depth < _step);
        if (_lessThan_1) {
          int _step_1 = Test.permutation.getStep();
          int _plus_1 = (_step_1 + 2);
          depth = _plus_1;
        }
      }
    }
    return depth;
  }
  
  public static Object test() {
    return null;
  }
  
  public static String genVariable(final OneDec dec) {
    String _xifexpression = null;
    ConstDec _const = dec.getConst();
    boolean _notEquals = (!Objects.equal(_const, null));
    if (_notEquals) {
      String _xifexpression_1 = null;
      ConstDec _const_1 = dec.getConst();
      BoolValue _bvalue = _const_1.getBvalue();
      boolean _notEquals_1 = (!Objects.equal(_bvalue, null));
      if (_notEquals_1) {
        ConstDec _const_2 = dec.getConst();
        String _name = _const_2.getName();
        ConstDec _const_3 = dec.getConst();
        BoolValue _bvalue_1 = _const_3.getBvalue();
        String _string = _bvalue_1.toString();
        _xifexpression_1 = SchedulerTestGenerator.cst.put(_name, _string);
      } else {
        ConstDec _const_4 = dec.getConst();
        String _name_1 = _const_4.getName();
        ConstDec _const_5 = dec.getConst();
        NumValue _ivalue = _const_5.getIvalue();
        int _value = _ivalue.getValue();
        String _string_1 = Integer.valueOf(_value).toString();
        _xifexpression_1 = SchedulerTestGenerator.cst.put(_name_1, _string_1);
      }
      _xifexpression = _xifexpression_1;
    } else {
      EnumDec _enumdec = dec.getEnumdec();
      boolean _notEquals_2 = (!Objects.equal(_enumdec, null));
      if (_notEquals_2) {
        ArrayList<String> elist = new ArrayList<String>();
        EnumDec _enumdec_1 = dec.getEnumdec();
        EList<String> _enumele = _enumdec_1.getEnumele();
        for (final String e : _enumele) {
          elist.add(e);
        }
        EnumDec _enumdec_2 = dec.getEnumdec();
        EnumType _type = _enumdec_2.getType();
        String _name_2 = _type.getName();
        SchedulerTestGenerator.mapenumval.put(_name_2, elist);
        return "";
      } else {
        String result = "int ";
        VarDec _var = dec.getVar();
        boolean _notEquals_3 = (!Objects.equal(_var, null));
        if (_notEquals_3) {
          VarDec _var_1 = dec.getVar();
          EnumType _enumtype = _var_1.getEnumtype();
          boolean _notEquals_4 = (!Objects.equal(_enumtype, null));
          if (_notEquals_4) {
            VarDec _var_2 = dec.getVar();
            EList<VDec> _name_3 = _var_2.getName();
            for (final VDec vname : _name_3) {
              String _name_4 = vname.getName();
              VarDec _var_3 = dec.getVar();
              EnumType _enumtype_1 = _var_3.getEnumtype();
              String _name_5 = _enumtype_1.getName();
              SchedulerTestGenerator.mapenumtype.put(_name_4, _name_5);
            }
          }
          int index = 0;
          VarDec _var_4 = dec.getVar();
          EList<VDec> _name_6 = _var_4.getName();
          for (final VDec vname_1 : _name_6) {
            {
              String _result = result;
              String _name_7 = vname_1.getName();
              result = (_result + _name_7);
              index++;
              VarDec _var_5 = dec.getVar();
              TypeName _type_1 = _var_5.getType();
              String _string_2 = _type_1.toString();
              boolean _equals = Objects.equal(_string_2, "bool");
              if (_equals) {
                BoolValue _bvalue_2 = vname_1.getBvalue();
                boolean _notEquals_5 = (!Objects.equal(_bvalue_2, null));
                if (_notEquals_5) {
                  BoolValue _bvalue_3 = vname_1.getBvalue();
                  String _value_1 = _bvalue_3.getValue();
                  boolean _equals_1 = Objects.equal(_value_1, "true");
                  if (_equals_1) {
                    String _result_1 = result;
                    result = (_result_1 + " = 0");
                  } else {
                    String _result_2 = result;
                    result = (_result_2 + " = 1");
                  }
                }
              } else {
                NumValue _ivalue_1 = vname_1.getIvalue();
                boolean _equals_2 = Objects.equal(_ivalue_1, null);
                if (_equals_2) {
                  String _result_3 = result;
                  result = (_result_3 + " = 0");
                } else {
                  String _result_4 = result;
                  NumValue _ivalue_2 = vname_1.getIvalue();
                  String _plus = (" = " + _ivalue_2);
                  result = (_result_4 + _plus);
                }
              }
              VarDec _var_6 = dec.getVar();
              EList<VDec> _name_8 = _var_6.getName();
              int _size = _name_8.size();
              boolean _greaterThan = (_size > 1);
              if (_greaterThan) {
                VarDec _var_7 = dec.getVar();
                EList<VDec> _name_9 = _var_7.getName();
                int _size_1 = _name_9.size();
                boolean _lessEqualsThan = (index <= _size_1);
                if (_lessEqualsThan) {
                  String _result_5 = result;
                  result = (_result_5 + ", ");
                }
              }
              index++;
            }
          }
        }
        return (result + " ;");
      }
    }
    return _xifexpression;
  }
  
  public static String genProctype(final DeclareEvent e, final Define d) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("proctype ");
    Event _event = e.getEvent();
    ELABEL _elabel = _event.getElabel();
    String _label = _elabel.getLabel();
    _builder.append(_label, "");
    _builder.append("() { /* for event ");
    Event _event_1 = e.getEvent();
    ELABEL _elabel_1 = _event_1.getElabel();
    String _label_1 = _elabel_1.getLabel();
    _builder.append(_label_1, "");
    _builder.append(" */");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    Event _event_2 = e.getEvent();
    ELABEL _elabel_2 = _event_2.getElabel();
    final String en = _elabel_2.getLabel();
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _genBooleanVariable = SchedulerTestGenerator.genBooleanVariable(en, 0);
    _builder.append(_genBooleanVariable, "\t");
    _builder.append("\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("do");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append(":: d_step {\t\t\t\t\t");
    _builder.newLine();
    _builder.append("\t\t\t");
    Event _event_3 = e.getEvent();
    CharSequence _dispatchEventGuard = SchedulerTestGenerator.dispatchEventGuard(_event_3);
    _builder.append(_dispatchEventGuard, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    CharSequence _genCheckBooleanVariable = SchedulerTestGenerator.genCheckBooleanVariable(en, 0);
    _builder.append(_genCheckBooleanVariable, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("/* action for event ");
    _builder.append(en, "\t\t\t");
    _builder.append("*/");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    Event _event_4 = e.getEvent();
    CharSequence _dispatchEventAction = SchedulerTestGenerator.dispatchEventAction(_event_4, d);
    _builder.append(_dispatchEventAction, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    CharSequence _genConclusionBooleanVariable = SchedulerTestGenerator.genConclusionBooleanVariable(en, 0);
    _builder.append(_genConclusionBooleanVariable, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("od");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static CharSequence genBooleanVariable(final String event, final int id) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _notEquals = (!Objects.equal(Test.rules, null));
      if (_notEquals) {
        int index = 0;
        _builder.newLineIfNotEmpty();
        {
          EList<Rule> _r = Test.rules.getR();
          for(final Rule rule : _r) {
            {
              ENAME _ename = rule.getEname();
              ELABEL _elabel = _ename.getElabel();
              boolean _notEquals_1 = (!Objects.equal(_elabel, null));
              if (_notEquals_1) {
                {
                  ENAME _ename_1 = rule.getEname();
                  ELABEL _elabel_1 = _ename_1.getElabel();
                  String _label = _elabel_1.getLabel();
                  boolean _equals = Objects.equal(_label, event);
                  if (_equals) {
                    _builder.append("bool _b");
                    _builder.append(event, "");
                    int _plusPlus = index++;
                    _builder.append(_plusPlus, "");
                    _builder.append("_");
                    _builder.append(id, "");
                    _builder.append(" ;");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                {
                  ENAME _ename_2 = rule.getEname();
                  scheduling.dsl.String _scheventname = _ename_2.getScheventname();
                  String _string = _scheventname.toString();
                  boolean _equals_1 = _string.equals(event);
                  if (_equals_1) {
                    _builder.append("bool _b");
                    _builder.append(event, "");
                    int _plusPlus_1 = index++;
                    _builder.append(_plusPlus_1, "");
                    _builder.append("_");
                    _builder.append(id, "");
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
    return _builder;
  }
  
  public static CharSequence genCheckBooleanVariable(final String event, final int id) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _notEquals = (!Objects.equal(Test.rules, null));
      if (_notEquals) {
        int index = 0;
        _builder.newLineIfNotEmpty();
        {
          EList<Rule> _r = Test.rules.getR();
          for(final Rule r : _r) {
            {
              ENAME _ename = r.getEname();
              ELABEL _elabel = _ename.getElabel();
              boolean _notEquals_1 = (!Objects.equal(_elabel, null));
              if (_notEquals_1) {
                {
                  ENAME _ename_1 = r.getEname();
                  ELABEL _elabel_1 = _ename_1.getElabel();
                  String _label = _elabel_1.getLabel();
                  boolean _equals = Objects.equal(_label, event);
                  if (_equals) {
                    _builder.newLine();
                    _builder.append("/* pre-condition for rule: ");
                    String _rulename = r.getRulename();
                    _builder.append(_rulename, "");
                    _builder.append(" */");
                    _builder.newLineIfNotEmpty();
                    _builder.append("_b");
                    _builder.append(event, "");
                    _builder.append(index, "");
                    _builder.append("_");
                    _builder.append(id, "");
                    _builder.append(" = false ;\t\t\t");
                    _builder.newLineIfNotEmpty();
                    {
                      Condition _premise = r.getPremise();
                      boolean _notEquals_2 = (!Objects.equal(_premise, null));
                      if (_notEquals_2) {
                        _builder.append("if ");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append(":: ");
                        Condition _premise_1 = r.getPremise();
                        Expression _expr = _premise_1.getExpr();
                        String _dispatchExpression = Statements.dispatchExpression(_expr);
                        _builder.append(_dispatchExpression, "\t");
                        _builder.append(" -> _b");
                        _builder.append(event, "\t");
                        int _plusPlus = index++;
                        _builder.append(_plusPlus, "\t");
                        _builder.append("_");
                        _builder.append(id, "\t");
                        _builder.append(" = true");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append(":: else -> skip ");
                        _builder.newLine();
                        _builder.append("fi ;");
                        _builder.newLine();
                      }
                    }
                  }
                }
              } else {
                {
                  ENAME _ename_2 = r.getEname();
                  scheduling.dsl.String _scheventname = _ename_2.getScheventname();
                  boolean _notEquals_3 = (!Objects.equal(_scheventname, null));
                  if (_notEquals_3) {
                    {
                      ENAME _ename_3 = r.getEname();
                      scheduling.dsl.String _scheventname_1 = _ename_3.getScheventname();
                      String _string = _scheventname_1.toString();
                      boolean _equals_1 = _string.equals(event);
                      if (_equals_1) {
                        _builder.append("\t\t");
                        _builder.newLine();
                        _builder.append("/* pre-condition for rule: ");
                        String _rulename_1 = r.getRulename();
                        _builder.append(_rulename_1, "");
                        _builder.append(" */");
                        _builder.newLineIfNotEmpty();
                        _builder.append("_b");
                        _builder.append(event, "");
                        _builder.append(index, "");
                        _builder.append("_");
                        _builder.append(id, "");
                        _builder.append(" = false ;\t\t\t");
                        _builder.newLineIfNotEmpty();
                        {
                          Condition _premise_2 = r.getPremise();
                          boolean _notEquals_4 = (!Objects.equal(_premise_2, null));
                          if (_notEquals_4) {
                            _builder.append("if ");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append(":: ");
                            Condition _premise_3 = r.getPremise();
                            Expression _expr_1 = _premise_3.getExpr();
                            String _dispatchExpression_1 = Statements.dispatchExpression(_expr_1);
                            _builder.append(_dispatchExpression_1, "\t");
                            _builder.append(" -> _b");
                            _builder.append(event, "\t");
                            int _plusPlus_1 = index++;
                            _builder.append(_plusPlus_1, "\t");
                            _builder.append("_");
                            _builder.append(id, "\t");
                            _builder.append(" = true");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append(":: else -> skip ");
                            _builder.newLine();
                            _builder.append("fi ;");
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
    return _builder;
  }
  
  public static CharSequence genConclusionBooleanVariable(final String event, final int id) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _notEquals = (!Objects.equal(Test.rules, null));
      if (_notEquals) {
        int index = 0;
        _builder.newLineIfNotEmpty();
        {
          EList<Rule> _r = Test.rules.getR();
          for(final Rule t : _r) {
            {
              ENAME _ename = t.getEname();
              ELABEL _elabel = _ename.getElabel();
              boolean _notEquals_1 = (!Objects.equal(_elabel, null));
              if (_notEquals_1) {
                {
                  ENAME _ename_1 = t.getEname();
                  ELABEL _elabel_1 = _ename_1.getElabel();
                  String _label = _elabel_1.getLabel();
                  boolean _equals = Objects.equal(_label, event);
                  if (_equals) {
                    _builder.newLine();
                    _builder.append("/* pos-condition for rule: ");
                    String _rulename = t.getRulename();
                    _builder.append(_rulename, "");
                    _builder.append("*/");
                    _builder.newLineIfNotEmpty();
                    _builder.append("if ");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append(":: _b");
                    _builder.append(event, "\t");
                    int _plusPlus = index++;
                    _builder.append(_plusPlus, "\t");
                    _builder.append("_");
                    _builder.append(id, "\t");
                    _builder.append(" -> assert(");
                    Condition _conclude = t.getConclude();
                    Expression _expr = _conclude.getExpr();
                    String _dispatchExpression = Statements.dispatchExpression(_expr);
                    _builder.append(_dispatchExpression, "\t");
                    _builder.append(")");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append(":: else -> skip ");
                    _builder.newLine();
                    _builder.append("fi ;\t\t\t\t");
                    _builder.newLine();
                  }
                }
              } else {
                {
                  ENAME _ename_2 = t.getEname();
                  scheduling.dsl.String _scheventname = _ename_2.getScheventname();
                  boolean _notEquals_2 = (!Objects.equal(_scheventname, null));
                  if (_notEquals_2) {
                    {
                      ENAME _ename_3 = t.getEname();
                      scheduling.dsl.String _scheventname_1 = _ename_3.getScheventname();
                      String _string = _scheventname_1.toString();
                      boolean _equals_1 = _string.equals(event);
                      if (_equals_1) {
                        _builder.newLine();
                        _builder.append("/* pos-condition for rule: ");
                        String _rulename_1 = t.getRulename();
                        _builder.append(_rulename_1, "");
                        _builder.append("*/");
                        _builder.newLineIfNotEmpty();
                        _builder.append("if ");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append(":: _b");
                        _builder.append(event, "\t");
                        int _plusPlus_1 = index++;
                        _builder.append(_plusPlus_1, "\t");
                        _builder.append("_");
                        _builder.append(id, "\t");
                        _builder.append(" -> assert(");
                        Condition _conclude_1 = t.getConclude();
                        Expression _expr_1 = _conclude_1.getExpr();
                        String _dispatchExpression_1 = Statements.dispatchExpression(_expr_1);
                        _builder.append(_dispatchExpression_1, "\t");
                        _builder.append(")");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append(":: else -> skip ");
                        _builder.newLine();
                        _builder.append("fi ;\t\t\t\t");
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
    return _builder;
  }
  
  public static CharSequence dispatchEventGuard(final Event e) {
    StringConcatenation _builder = new StringConcatenation();
    {
      Expression _guard = e.getGuard();
      boolean _notEquals = (!Objects.equal(_guard, null));
      if (_notEquals) {
        _builder.append("/* guard for event ");
        ELABEL _elabel = e.getElabel();
        String _label = _elabel.getLabel();
        _builder.append(_label, "");
        _builder.append("*/");
        _builder.newLineIfNotEmpty();
        Expression _guard_1 = e.getGuard();
        String _dispatchExpression = Statements.dispatchExpression(_guard_1);
        _builder.append(_dispatchExpression, "");
        _builder.append(" -> ");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public static CharSequence dispatchEventAction(final Event e, final Define d) {
    StringConcatenation _builder = new StringConcatenation();
    {
      SequenceAction _sequence = e.getSequence();
      boolean _notEquals = (!Objects.equal(_sequence, null));
      if (_notEquals) {
        {
          SequenceAction _sequence_1 = e.getSequence();
          EList<Action> _action = _sequence_1.getAction();
          for(final Action act : _action) {
            CharSequence _dispatchAction = SchedulerTestGenerator.dispatchAction(act);
            _builder.append(_dispatchAction, "");
          }
        }
        _builder.newLineIfNotEmpty();
      } else {
        String _dispatchEventActionForFunction = SchedulerTestGenerator.dispatchEventActionForFunction(e, d);
        _builder.append(_dispatchEventActionForFunction, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public static void initdefvalue(final Define d) {
    SchedulerTestGenerator.defrefval.clear();
    EList<RefList> _reflist = d.getReflist();
    for (final RefList r : _reflist) {
      {
        final String n = r.getName();
        ArrayList<String> ref = new ArrayList<String>();
        EList<RefListItem> _item = r.getItem();
        for (final RefListItem item : _item) {
          BoolValue _bvalue = item.getBvalue();
          boolean _notEquals = (!Objects.equal(_bvalue, null));
          if (_notEquals) {
            BoolValue _bvalue_1 = item.getBvalue();
            String _value = _bvalue_1.getValue();
            ref.add(_value);
          } else {
            NumValue _ivalue = item.getIvalue();
            boolean _notEquals_1 = (!Objects.equal(_ivalue, null));
            if (_notEquals_1) {
              NumValue _ivalue_1 = item.getIvalue();
              int _value_1 = _ivalue_1.getValue();
              String _string = Integer.valueOf(_value_1).toString();
              ref.add(_string);
            } else {
              String _id = item.getId();
              ref.add(_id);
            }
          }
        }
        SchedulerTestGenerator.defrefval.put(n, ref);
      }
    }
  }
  
  public static int getFunctionParameterValue(final String para) {
    return 0;
  }
  
  public static String dispatchEventActionForFunction(final Event e, final Define d) {
    SchedulerTestGenerator.funcparaval.clear();
    SchedulerTestGenerator.mapfuncparamval.clear();
    EventFunction _eventfunction = e.getEventfunction();
    ParaListItem _funcpara = _eventfunction.getFuncpara();
    boolean _notEquals = (!Objects.equal(_funcpara, null));
    if (_notEquals) {
      EventFunction _eventfunction_1 = e.getEventfunction();
      ParaListItem _funcpara_1 = _eventfunction_1.getFuncpara();
      EList<RefListItem> _item = _funcpara_1.getItem();
      for (final RefListItem item : _item) {
        BoolValue _bvalue = item.getBvalue();
        boolean _notEquals_1 = (!Objects.equal(_bvalue, null));
        if (_notEquals_1) {
          BoolValue _bvalue_1 = item.getBvalue();
          String _value = _bvalue_1.getValue();
          SchedulerTestGenerator.funcparaval.add(_value);
        } else {
          NumValue _ivalue = item.getIvalue();
          boolean _notEquals_2 = (!Objects.equal(_ivalue, null));
          if (_notEquals_2) {
            NumValue _ivalue_1 = item.getIvalue();
            int _value_1 = _ivalue_1.getValue();
            String _string = Integer.valueOf(_value_1).toString();
            SchedulerTestGenerator.funcparaval.add(_string);
          } else {
            String _id = item.getId();
            SchedulerTestGenerator.funcparaval.add(_id);
          }
        }
      }
    }
    EList<Function> _function = d.getFunction();
    for (final Function f : _function) {
      String _name = f.getName();
      EventFunction _eventfunction_2 = e.getEventfunction();
      String _name_1 = _eventfunction_2.getName();
      boolean _equals = _name.equals(_name_1);
      if (_equals) {
        int i = 0;
        FuncPara _funcpara_2 = f.getFuncpara();
        EList<String> _para = _funcpara_2.getPara();
        for (final String par : _para) {
          int _plusPlus = i++;
          String _get = SchedulerTestGenerator.funcparaval.get(_plusPlus);
          SchedulerTestGenerator.mapfuncparamval.put(par, _get);
        }
        return SchedulerTestGenerator.dispatchEventFunction(f);
      }
    }
    return null;
  }
  
  public static String dispatchEventFunction(final Function f) {
    String result = "";
    SequenceAction _sequence = f.getSequence();
    EList<Action> _action = _sequence.getAction();
    for (final Action act : _action) {
      String _result = result;
      CharSequence _dispatchActionWithFunction = SchedulerTestGenerator.dispatchActionWithFunction(act);
      result = (_result + _dispatchActionWithFunction);
    }
    return result;
  }
  
  public static CharSequence dispatchActionWithFunction(final Action a) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((a instanceof IFs)) {
        _builder.append("if");
        _builder.newLine();
        {
          Options _option = ((IFs)a).getOption();
          EList<SequenceAction> _sequence = _option.getSequence();
          for(final SequenceAction opt : _sequence) {
            _builder.append("\t");
            _builder.append(":: ");
            {
              EList<Action> _action = opt.getAction();
              for(final Action act : _action) {
                Object _dispatchActionWithFunction = SchedulerTestGenerator.dispatchActionWithFunction(act);
                _builder.append(_dispatchActionWithFunction, "\t");
              }
            }
            _builder.newLineIfNotEmpty();
          }
        }
        {
          Options _option_1 = ((IFs)a).getOption();
          ELSEs _elses = _option_1.getElses();
          boolean _notEquals = (!Objects.equal(_elses, null));
          if (_notEquals) {
            _builder.append("\t");
            _builder.append(":: else -> ");
            Options _option_2 = ((IFs)a).getOption();
            ELSEs _elses_1 = _option_2.getElses();
            Action _sequence_1 = _elses_1.getSequence();
            Object _dispatchActionWithFunction_1 = SchedulerTestGenerator.dispatchActionWithFunction(_sequence_1);
            _builder.append(_dispatchActionWithFunction_1, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("fi ;");
        _builder.newLine();
      } else {
        {
          if ((a instanceof ASSIGN)) {
            Object _dispatchStatement = Statements.dispatchStatement(a, "");
            _builder.append(_dispatchStatement, "");
            _builder.newLineIfNotEmpty();
          } else {
            {
              if ((a instanceof GUARD)) {
                Expression _expr = ((GUARD)a).getExpr();
                String _dispatchExpression = Statements.dispatchExpression(_expr);
                _builder.append(_dispatchExpression, "");
                _builder.append(" -> ");
                Action _sequence_2 = ((GUARD)a).getSequence();
                Object _dispatchActionWithFunction_2 = SchedulerTestGenerator.dispatchActionWithFunction(_sequence_2);
                _builder.append(_dispatchActionWithFunction_2, "");
                _builder.newLineIfNotEmpty();
              } else {
                {
                  if ((a instanceof SKIP)) {
                    _builder.append("skip ;");
                    _builder.newLine();
                  }
                }
              }
            }
          }
        }
      }
    }
    return _builder;
  }
  
  public static CharSequence dispatchAction(final Action a) {
    StringConcatenation _builder = new StringConcatenation();
    {
      if ((a instanceof IFs)) {
        _builder.append("if");
        _builder.newLine();
        {
          Options _option = ((IFs)a).getOption();
          EList<SequenceAction> _sequence = _option.getSequence();
          for(final SequenceAction opt : _sequence) {
            _builder.append("\t");
            _builder.append(":: ");
            {
              EList<Action> _action = opt.getAction();
              for(final Action act : _action) {
                Object _dispatchAction = SchedulerTestGenerator.dispatchAction(act);
                _builder.append(_dispatchAction, "\t");
              }
            }
            _builder.newLineIfNotEmpty();
          }
        }
        {
          Options _option_1 = ((IFs)a).getOption();
          ELSEs _elses = _option_1.getElses();
          boolean _notEquals = (!Objects.equal(_elses, null));
          if (_notEquals) {
            _builder.append("\t");
            _builder.append(":: else -> ");
            Options _option_2 = ((IFs)a).getOption();
            ELSEs _elses_1 = _option_2.getElses();
            Action _sequence_1 = _elses_1.getSequence();
            Object _dispatchAction_1 = SchedulerTestGenerator.dispatchAction(_sequence_1);
            _builder.append(_dispatchAction_1, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("fi ;");
        _builder.newLine();
      } else {
        {
          if ((a instanceof ASSIGN)) {
            Object _dispatchStatement = Statements.dispatchStatement(a, "");
            _builder.append(_dispatchStatement, "");
            _builder.newLineIfNotEmpty();
          } else {
            {
              if ((a instanceof GUARD)) {
                Expression _expr = ((GUARD)a).getExpr();
                String _dispatchExpression = Statements.dispatchExpression(_expr);
                _builder.append(_dispatchExpression, "");
                _builder.append(" -> ");
                Action _sequence_2 = ((GUARD)a).getSequence();
                Object _dispatchAction_2 = SchedulerTestGenerator.dispatchAction(_sequence_2);
                _builder.append(_dispatchAction_2, "");
                _builder.newLineIfNotEmpty();
              } else {
                {
                  if ((a instanceof SKIP)) {
                    _builder.append("skip ;");
                    _builder.newLine();
                  }
                }
              }
            }
          }
        }
      }
    }
    return _builder;
  }
  
  public static CharSequence generateVerificationCase() {
    StringConcatenation _builder = new StringConcatenation();
    int scenCount = 0;
    _builder.append("\t");
    _builder.newLineIfNotEmpty();
    {
      boolean _notEquals = (!Objects.equal(Test.permutation, null));
      if (_notEquals) {
        {
          boolean _notEquals_1 = (!Objects.equal(Test.configs, null));
          if (_notEquals_1) {
            {
              EList<Config> _config = Test.configs.getConfig();
              for(final Config conf : _config) {
                _builder.append("proctype VerificationCase");
                int _plusPlus = SchedulerTestGenerator.testCount++;
                _builder.append(_plusPlus, "");
                _builder.append("() {/*Verification case extended permutation with configuration: ");
                String _name = conf.getName();
                _builder.append(_name, "");
                _builder.append("*/\t\t\t\t\t\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("d_step {");
                _builder.newLine();
                _builder.append("\t\t");
                _builder.append("/*configuration : ");
                String _name_1 = conf.getName();
                _builder.append(_name_1, "\t\t");
                _builder.append("*/");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                CharSequence _genConfiguration = SchedulerTestGenerator.genConfiguration(conf);
                _builder.append(_genConfiguration, "\t\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t\t");
                _builder.append("/*run process*/");
                _builder.newLine();
                _builder.append("\t\t");
                CharSequence _genRunProcess = SchedulerTestGenerator.genRunProcess();
                _builder.append(_genRunProcess, "\t\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
                _builder.append("}\t");
                _builder.newLine();
              }
            }
          } else {
            _builder.append("proctype VerificationCase");
            int _plusPlus_1 = SchedulerTestGenerator.testCount++;
            _builder.append(_plusPlus_1, "");
            _builder.append("() {/*Test case extended permutation with no configuration*/");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("d_step {\t\t\t\t\t \t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t\t");
            _builder.append("/*run process*/");
            _builder.newLine();
            _builder.append("\t\t");
            CharSequence _genRunProcess_1 = SchedulerTestGenerator.genRunProcess();
            _builder.append(_genRunProcess_1, "\t\t");
            _builder.append("\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.newLine();
    {
      boolean _notEquals_2 = (!Objects.equal(Test.configs, null));
      if (_notEquals_2) {
        {
          EList<Config> _config_1 = Test.configs.getConfig();
          for(final Config conf_1 : _config_1) {
            {
              boolean _notEquals_3 = (!Objects.equal(Test.scenarios, null));
              if (_notEquals_3) {
                {
                  EList<Flow> _flow = Test.scenarios.getFlow();
                  boolean _notEquals_4 = (!Objects.equal(_flow, null));
                  if (_notEquals_4) {
                    {
                      EList<Flow> _flow_1 = Test.scenarios.getFlow();
                      for(final Flow flow : _flow_1) {
                        _builder.append("proctype VerificationCase");
                        int _plusPlus_2 = SchedulerTestGenerator.testCount++;
                        _builder.append(_plusPlus_2, "");
                        _builder.append("() {/*Verification case scenario ");
                        _builder.append(scenCount, "");
                        _builder.append(" (");
                        String _genFlowString = SchedulerTestGenerator.genFlowString(flow);
                        _builder.append(_genFlowString, "");
                        _builder.append(") with configuration: ");
                        String _name_2 = conf_1.getName();
                        _builder.append(_name_2, "");
                        _builder.append("*/\t\t\t\t\t\t\t\t");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t\t\t\t\t");
                        int index = 0;
                        _builder.append("\t\t\t\t\t\t");
                        _builder.newLineIfNotEmpty();
                        {
                          EList<ENAME> _event = flow.getEvent();
                          for(final ENAME e : _event) {
                            {
                              ELABEL _elabel = e.getElabel();
                              boolean _notEquals_5 = (!Objects.equal(_elabel, null));
                              if (_notEquals_5) {
                                _builder.append("\t");
                                ELABEL _elabel_1 = e.getElabel();
                                final String en = _elabel_1.getLabel();
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                CharSequence _genBooleanVariable = SchedulerTestGenerator.genBooleanVariable(en, index);
                                _builder.append(_genBooleanVariable, "\t");
                                _builder.newLineIfNotEmpty();
                                {
                                  if ((index == 0)) {
                                    _builder.append("\t");
                                    _builder.append("/*configuration : ");
                                    String _name_3 = conf_1.getName();
                                    _builder.append(_name_3, "\t");
                                    _builder.append("*/");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.append("d_step {");
                                    _builder.newLine();
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    CharSequence _genConfiguration_1 = SchedulerTestGenerator.genConfiguration(conf_1);
                                    _builder.append(_genConfiguration_1, "\t\t");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.append("}");
                                    _builder.newLine();
                                  }
                                }
                                _builder.append("\t");
                                _builder.append("d_step {\t\t\t\t\t\t\t\t\t\t\t");
                                _builder.newLine();
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.append("/*------- event: ");
                                _builder.append(en, "\t\t");
                                _builder.append(" ---------*/\t");
                                _builder.newLineIfNotEmpty();
                                {
                                  EList<DeclareEvent> _event_1 = Test.specification.getEvent();
                                  for(final DeclareEvent ev : _event_1) {
                                    {
                                      Event _event_2 = ev.getEvent();
                                      ELABEL _elabel_2 = _event_2.getElabel();
                                      String _label = _elabel_2.getLabel();
                                      ELABEL _elabel_3 = e.getElabel();
                                      String _label_1 = _elabel_3.getLabel();
                                      boolean _equals = Objects.equal(_label, _label_1);
                                      if (_equals) {
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        Event _event_3 = ev.getEvent();
                                        CharSequence _dispatchEventGuard = SchedulerTestGenerator.dispatchEventGuard(_event_3);
                                        _builder.append(_dispatchEventGuard, "\t\t");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        CharSequence _genCheckBooleanVariable = SchedulerTestGenerator.genCheckBooleanVariable(en, index);
                                        _builder.append(_genCheckBooleanVariable, "\t\t");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        _builder.newLine();
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        _builder.append("/* action for event ");
                                        _builder.append(en, "\t\t");
                                        _builder.append("*/");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        Event _event_4 = ev.getEvent();
                                        Define _define = Test.specification.getDefine();
                                        CharSequence _dispatchEventAction = SchedulerTestGenerator.dispatchEventAction(_event_4, _define);
                                        _builder.append(_dispatchEventAction, "\t\t");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        int _plusPlus_3 = index++;
                                        CharSequence _genConclusionBooleanVariable = SchedulerTestGenerator.genConclusionBooleanVariable(en, _plusPlus_3);
                                        _builder.append(_genConclusionBooleanVariable, "\t\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.append("/*---- end of event: ");
                                _builder.append(en, "\t\t");
                                _builder.append(" -----*/");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("} \t\t\t\t\t\t\t\t\t\t");
                                _builder.newLine();
                              } else {
                                {
                                  scheduling.dsl.String _scheventname = e.getScheventname();
                                  boolean _notEquals_6 = (!Objects.equal(_scheventname, null));
                                  if (_notEquals_6) {
                                    _builder.append("\t");
                                    scheduling.dsl.String _scheventname_1 = e.getScheventname();
                                    final String en_1 = _scheventname_1.toString();
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.append("/*------- event: ");
                                    _builder.append(en_1, "\t");
                                    _builder.append(" ---------*/\t\t\t\t\t\t\t\t\t\t");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    CharSequence _genCheckBooleanVariable_1 = SchedulerTestGenerator.genCheckBooleanVariable(en_1, index);
                                    _builder.append(_genCheckBooleanVariable_1, "\t\t");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.newLine();
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.append("/* insert event */");
                                    _builder.newLine();
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.append("sch.");
                                    _builder.append(en_1, "\t\t");
                                    _builder.append("()\t;\t\t\t\t\t\t");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.newLine();
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    int _plusPlus_4 = index++;
                                    CharSequence _genConclusionBooleanVariable_1 = SchedulerTestGenerator.genConclusionBooleanVariable(en_1, _plusPlus_4);
                                    _builder.append(_genConclusionBooleanVariable_1, "\t\t");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.append("/*---- end of event: ");
                                    _builder.append(en_1, "\t");
                                    _builder.append(" -----*/");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.newLine();
                                  }
                                }
                              }
                            }
                          }
                        }
                        _builder.append("}\t");
                        _builder.newLine();
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
          boolean _notEquals_7 = (!Objects.equal(Test.scenarios, null));
          if (_notEquals_7) {
            {
              EList<Flow> _flow_2 = Test.scenarios.getFlow();
              boolean _notEquals_8 = (!Objects.equal(_flow_2, null));
              if (_notEquals_8) {
                {
                  EList<Flow> _flow_3 = Test.scenarios.getFlow();
                  for(final Flow flow_1 : _flow_3) {
                    _builder.append("proctype VerificationCase");
                    int _plusPlus_5 = SchedulerTestGenerator.testCount++;
                    _builder.append(_plusPlus_5, "");
                    _builder.append("() {/*Verification case scenario ");
                    _builder.append(scenCount, "");
                    _builder.append(" (");
                    String _genFlowString_1 = SchedulerTestGenerator.genFlowString(flow_1);
                    _builder.append(_genFlowString_1, "");
                    _builder.append(") with no configuration*/");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    int index_1 = 0;
                    _builder.append("\t\t\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                    {
                      EList<ENAME> _event_5 = flow_1.getEvent();
                      for(final ENAME e_1 : _event_5) {
                        {
                          ELABEL _elabel_4 = e_1.getElabel();
                          boolean _notEquals_9 = (!Objects.equal(_elabel_4, null));
                          if (_notEquals_9) {
                            _builder.append("\t");
                            ELABEL _elabel_5 = e_1.getElabel();
                            final String en_2 = _elabel_5.getLabel();
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            CharSequence _genBooleanVariable_1 = SchedulerTestGenerator.genBooleanVariable(en_2, index_1);
                            _builder.append(_genBooleanVariable_1, "\t");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("d_step {");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("/*------- event: ");
                            _builder.append(en_2, "\t\t");
                            _builder.append(" ---------*/\t");
                            _builder.newLineIfNotEmpty();
                            {
                              EList<DeclareEvent> _event_6 = Test.specification.getEvent();
                              for(final DeclareEvent ev_1 : _event_6) {
                                {
                                  Event _event_7 = ev_1.getEvent();
                                  ELABEL _elabel_6 = _event_7.getElabel();
                                  String _label_2 = _elabel_6.getLabel();
                                  ELABEL _elabel_7 = e_1.getElabel();
                                  String _label_3 = _elabel_7.getLabel();
                                  boolean _equals_1 = Objects.equal(_label_2, _label_3);
                                  if (_equals_1) {
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    Event _event_8 = ev_1.getEvent();
                                    CharSequence _dispatchEventGuard_1 = SchedulerTestGenerator.dispatchEventGuard(_event_8);
                                    _builder.append(_dispatchEventGuard_1, "\t\t\t");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    CharSequence _genCheckBooleanVariable_2 = SchedulerTestGenerator.genCheckBooleanVariable(en_2, index_1);
                                    _builder.append(_genCheckBooleanVariable_2, "\t\t\t");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.newLine();
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.append("/* action for event ");
                                    _builder.append(en_2, "\t\t\t");
                                    _builder.append("*/");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    Event _event_9 = ev_1.getEvent();
                                    Define _define_1 = Test.specification.getDefine();
                                    CharSequence _dispatchEventAction_1 = SchedulerTestGenerator.dispatchEventAction(_event_9, _define_1);
                                    _builder.append(_dispatchEventAction_1, "\t\t\t");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    int _plusPlus_6 = index_1++;
                                    CharSequence _genConclusionBooleanVariable_2 = SchedulerTestGenerator.genConclusionBooleanVariable(en_2, _plusPlus_6);
                                    _builder.append(_genConclusionBooleanVariable_2, "\t\t\t");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    _builder.append("}");
                                    _builder.newLine();
                                  }
                                }
                              }
                            }
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("/*---- end of event: ");
                            _builder.append(en_2, "\t\t");
                            _builder.append(" -----*/");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("} \t\t\t\t\t\t\t\t\t");
                            _builder.newLine();
                          } else {
                            {
                              scheduling.dsl.String _scheventname_2 = e_1.getScheventname();
                              boolean _notEquals_10 = (!Objects.equal(_scheventname_2, null));
                              if (_notEquals_10) {
                                _builder.append("\t");
                                scheduling.dsl.String _scheventname_3 = e_1.getScheventname();
                                final String en_3 = _scheventname_3.toString();
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("/*------- event: ");
                                _builder.append(en_3, "\t");
                                _builder.append(" ---------*/");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("\t");
                                CharSequence _genCheckBooleanVariable_3 = SchedulerTestGenerator.genCheckBooleanVariable(en_3, index_1);
                                _builder.append(_genCheckBooleanVariable_3, "\t\t");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.newLine();
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.append("/* insert event */");
                                _builder.newLine();
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.append("sch.");
                                _builder.append(en_3, "\t\t");
                                _builder.append("()\t;\t\t\t\t\t\t");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.newLine();
                                _builder.append("\t");
                                _builder.append("\t");
                                int _plusPlus_7 = index_1++;
                                CharSequence _genConclusionBooleanVariable_3 = SchedulerTestGenerator.genConclusionBooleanVariable(en_3, _plusPlus_7);
                                _builder.append(_genConclusionBooleanVariable_3, "\t\t");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("/*---- end of event: ");
                                _builder.append(en_3, "\t");
                                _builder.append(" -----*/\t\t\t\t\t\t\t\t\t\t");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        }
                      }
                    }
                    _builder.append("}\t");
                    _builder.newLine();
                  }
                }
              }
            }
          }
        }
      }
    }
    return _builder;
  }
  
  public static String genFlowString(final Flow f) {
    String result = "";
    int index = 0;
    EList<ENAME> _event = f.getEvent();
    for (final ENAME e : _event) {
      {
        ELABEL _elabel = e.getElabel();
        boolean _notEquals = (!Objects.equal(_elabel, null));
        if (_notEquals) {
          String _result = result;
          ELABEL _elabel_1 = e.getElabel();
          String _label = _elabel_1.getLabel();
          result = (_result + _label);
        } else {
          String _result_1 = result;
          scheduling.dsl.String _scheventname = e.getScheventname();
          String _string = _scheventname.toString();
          result = (_result_1 + _string);
        }
        EList<ENAME> _event_1 = f.getEvent();
        int _size = _event_1.size();
        int _minus = (_size - 1);
        boolean _lessThan = (index < _minus);
        if (_lessThan) {
          String _result_2 = result;
          result = (_result_2 + "->");
          index++;
        }
      }
    }
    return result;
  }
  
  public static CharSequence genConfiguration(final Config conf) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("printf(\"");
    String _name = conf.getName();
    _builder.append(_name, "");
    _builder.append("\") ;");
    _builder.newLineIfNotEmpty();
    {
      Configuration _config = conf.getConfig();
      EList<VarInit> _var = _config.getVar();
      boolean _notEquals = (!Objects.equal(_var, null));
      if (_notEquals) {
        {
          Configuration _config_1 = conf.getConfig();
          EList<VarInit> _var_1 = _config_1.getVar();
          for(final VarInit va : _var_1) {
            {
              Value _val = va.getVal();
              BoolValue _bool = _val.getBool();
              boolean _notEquals_1 = (!Objects.equal(_bool, null));
              if (_notEquals_1) {
                {
                  Value _val_1 = va.getVal();
                  BoolValue _bool_1 = _val_1.getBool();
                  String _value = _bool_1.getValue();
                  boolean _equals = Objects.equal(_value, "true");
                  if (_equals) {
                    String _varName = va.getVarName();
                    _builder.append(_varName, "");
                    _builder.append(" = 0 ;\t\t\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                  } else {
                    String _varName_1 = va.getVarName();
                    _builder.append(_varName_1, "");
                    _builder.append(" = 1 ;");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                String _varName_2 = va.getVarName();
                _builder.append(_varName_2, "");
                _builder.append(" = ");
                Value _val_2 = va.getVal();
                NumValue _num = _val_2.getNum();
                int _value_1 = _num.getValue();
                _builder.append(_value_1, "");
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
  
  public static CharSequence genRunProcess() {
    StringConcatenation _builder = new StringConcatenation();
    {
      EList<DeclareEvent> _event = Test.specification.getEvent();
      for(final DeclareEvent e : _event) {
        _builder.append("run ");
        Event _event_1 = e.getEvent();
        ELABEL _elabel = _event_1.getElabel();
        String _label = _elabel.getLabel();
        _builder.append(_label, "");
        _builder.append("() ;");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public static CharSequence genRunVerificationCase() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("active proctype test() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if");
    _builder.newLine();
    {
      ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, SchedulerTestGenerator.testCount, true);
      for(final Integer i : _doubleDotLessThan) {
        _builder.append("\t\t");
        _builder.append(":: run VerificationCase");
        _builder.append(i, "\t\t");
        _builder.append("()");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append(":: else -> skip");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("fi");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
}
