package scheduling.generator;

import com.google.common.base.Objects;
import java.util.HashMap;
import java.util.HashSet;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import scheduling.dsl.And;
import scheduling.dsl.Atomic;
import scheduling.dsl.BlockStatement;
import scheduling.dsl.BoolValue;
import scheduling.dsl.CTL_AT;
import scheduling.dsl.ChangeAction;
import scheduling.dsl.ChangeValue;
import scheduling.dsl.ChangeValueExpression;
import scheduling.dsl.ChangeValueUnOp;
import scheduling.dsl.CheckPoint;
import scheduling.dsl.ComparationDef;
import scheduling.dsl.Comparison;
import scheduling.dsl.ConfigProcess;
import scheduling.dsl.DataBlockDef;
import scheduling.dsl.DataDef;
import scheduling.dsl.DataSingleDef;
import scheduling.dsl.Element;
import scheduling.dsl.Equality;
import scheduling.dsl.EventDef;
import scheduling.dsl.EventOpt;
import scheduling.dsl.EventStm;
import scheduling.dsl.Expression;
import scheduling.dsl.GetProcess;
import scheduling.dsl.HandlerDef;
import scheduling.dsl.IfStatement;
import scheduling.dsl.Implies;
import scheduling.dsl.InterfaceDef;
import scheduling.dsl.InterfaceFunction;
import scheduling.dsl.LoopProcess;
import scheduling.dsl.Minus;
import scheduling.dsl.Model;
import scheduling.dsl.MulOrDiv;
import scheduling.dsl.Not;
import scheduling.dsl.NumValue;
import scheduling.dsl.Opt;
import scheduling.dsl.Or;
import scheduling.dsl.OrderingDef;
import scheduling.dsl.PeriodicProcess;
import scheduling.dsl.Plus;
import scheduling.dsl.PointID;
import scheduling.dsl.ProcessConfig;
import scheduling.dsl.ProcessConfiguration;
import scheduling.dsl.ProcessDSL;
import scheduling.dsl.ProcessDataDef;
import scheduling.dsl.ProcessPropertyDef;
import scheduling.dsl.ProcessPropertyName;
import scheduling.dsl.QualifiedNames;
import scheduling.dsl.RTCTL;
import scheduling.dsl.SchedulerCollectionDef;
import scheduling.dsl.SchedulerDSL;
import scheduling.dsl.SchedulerDataDef;
import scheduling.dsl.SchedulerDef;
import scheduling.dsl.SchedulerSet;
import scheduling.dsl.Statement;
import scheduling.dsl.Stm;
import scheduling.dsl.TestDSL;
import scheduling.dsl.Value;
import scheduling.dsl.Verify;
import scheduling.generator.Statements;

@SuppressWarnings("all")
public class Data {
  public static Model model = null;
  
  public static ProcessConfiguration procConfig = null;
  
  public static ProcessDSL procModel = null;
  
  public static SchedulerDSL schModel = null;
  
  public static TestDSL testMolel = null;
  
  public static HashMap<String, String> intProperties = new HashMap<String, String>();
  
  public static HashMap<String, String> boolProperties = new HashMap<String, String>();
  
  public static HashMap<String, String> byteProperties = new HashMap<String, String>();
  
  public static HashMap<String, String> clockProperties = new HashMap<String, String>();
  
  public static HashMap<String, String> periodicClockProperties = new HashMap<String, String>();
  
  public static HashMap<String, String> periodicClockOffset = new HashMap<String, String>();
  
  public static HashSet<String> varPropertyList = new HashSet<String>();
  
  public static HashSet<String> valPropertyList = new HashSet<String>();
  
  public static HashSet<String> defPropertyList = new HashSet<String>();
  
  public static HashSet<String> fixedvarPropertyList = new HashSet<String>();
  
  public static HashSet<String> fixedvalPropertyList = new HashSet<String>();
  
  public static HashSet<String> clockPropertyList = new HashSet<String>();
  
  public static HashSet<String> collectionList = new HashSet<String>();
  
  public static boolean hasClockEventHandler = false;
  
  public static boolean hasPeriodicProcess = false;
  
  public static boolean runTime = false;
  
  public static boolean sporadicTime = false;
  
  public static boolean initProcessList = false;
  
  public static HashSet<String> checkPointsList = new HashSet<String>();
  
  public static void fixClockProperty() {
    for (final String p : Data.clockPropertyList) {
      {
        Data.valPropertyList.remove(p);
        boolean _contains = Data.varPropertyList.contains(p);
        boolean _not = (!_contains);
        if (_not) {
          Data.varPropertyList.add(p);
        }
      }
    }
  }
  
  public static void getVarValPropertyInSchedulerModel(final SchedulerDSL schModel) {
    boolean _equals = Objects.equal(schModel, null);
    if (_equals) {
      return;
    }
    SchedulerDef sch = schModel.getScheduler();
    boolean _notEquals = (!Objects.equal(sch, null));
    if (_notEquals) {
      HandlerDef _handler = sch.getHandler();
      boolean _notEquals_1 = (!Objects.equal(_handler, null));
      if (_notEquals_1) {
        HandlerDef _handler_1 = sch.getHandler();
        EList<EventDef> _event = _handler_1.getEvent();
        boolean _notEquals_2 = (!Objects.equal(_event, null));
        if (_notEquals_2) {
          HandlerDef _handler_2 = sch.getHandler();
          EList<EventDef> _event_1 = _handler_2.getEvent();
          for (final EventDef h : _event_1) {
            EObject _event_2 = h.getEvent();
            if ((_event_2 instanceof EventStm)) {
              EObject _event_3 = h.getEvent();
              EList<Stm> _statements = ((EventStm) _event_3).getStatements();
              for (final Stm sta : _statements) {
                Statement _statement = sta.getStatement();
                Data.getVarValPropertyInStatement(_statement);
              }
            } else {
              EObject _event_4 = h.getEvent();
              EList<Opt> _opt = ((EventOpt) _event_4).getOpt();
              for (final Opt opt : _opt) {
                EventStm _eventstm = opt.getEventstm();
                EList<Stm> _statements_1 = _eventstm.getStatements();
                for (final Stm sta_1 : _statements_1) {
                  Statement _statement_1 = sta_1.getStatement();
                  Data.getVarValPropertyInStatement(_statement_1);
                }
              }
            }
          }
        }
      }
    }
    OrderingDef _order = schModel.getOrder();
    boolean _notEquals_3 = (!Objects.equal(_order, null));
    if (_notEquals_3) {
      OrderingDef _order_1 = schModel.getOrder();
      EList<ComparationDef> _compare = _order_1.getCompare();
      for (final ComparationDef comp : _compare) {
        EList<Statement> _statements_2 = comp.getStatements();
        for (final Statement sta_2 : _statements_2) {
          Data.getVarValPropertyInStatement(sta_2);
        }
      }
    }
    Verify _verify = schModel.getVerify();
    boolean _notEquals_4 = (!Objects.equal(_verify, null));
    if (_notEquals_4) {
      Verify _verify_1 = schModel.getVerify();
      CTL_AT _at = _verify_1.getAt();
      boolean _notEquals_5 = (!Objects.equal(_at, null));
      if (_notEquals_5) {
        Verify _verify_2 = schModel.getVerify();
        CTL_AT _at_1 = _verify_2.getAt();
        Expression _cond = _at_1.getCond();
        Data.getStaticPropertyInExpression(_cond);
      }
      Verify _verify_3 = schModel.getVerify();
      RTCTL _formula = _verify_3.getFormula();
      Data.getStaticPropertyInRTCTLExpression(_formula);
    }
  }
  
  public static Object getStaticPropertyInRTCTLExpression(final RTCTL rtctl_exp) {
    Object _xifexpression = null;
    String _op = rtctl_exp.getOp();
    boolean _equals = _op.equals("or");
    if (_equals) {
      Object _xblockexpression = null;
      {
        RTCTL _f1 = rtctl_exp.getF1();
        Data.getStaticPropertyInRTCTLExpression(_f1);
        RTCTL _f2 = rtctl_exp.getF2();
        _xblockexpression = Data.getStaticPropertyInRTCTLExpression(_f2);
      }
      _xifexpression = _xblockexpression;
    } else {
      Object _xifexpression_1 = null;
      String _op_1 = rtctl_exp.getOp();
      boolean _equals_1 = _op_1.equals("implies");
      if (_equals_1) {
        Object _xblockexpression_1 = null;
        {
          RTCTL _f1 = rtctl_exp.getF1();
          Data.getStaticPropertyInRTCTLExpression(_f1);
          RTCTL _f2 = rtctl_exp.getF2();
          _xblockexpression_1 = Data.getStaticPropertyInRTCTLExpression(_f2);
        }
        _xifexpression_1 = _xblockexpression_1;
      } else {
        Object _xifexpression_2 = null;
        Expression _exp = rtctl_exp.getExp();
        boolean _notEquals = (!Objects.equal(_exp, null));
        if (_notEquals) {
          Expression _exp_1 = rtctl_exp.getExp();
          _xifexpression_2 = Data.getStaticPropertyInExpression(_exp_1);
        } else {
          Object _xifexpression_3 = null;
          RTCTL _f = rtctl_exp.getF();
          boolean _notEquals_1 = (!Objects.equal(_f, null));
          if (_notEquals_1) {
            RTCTL _f_1 = rtctl_exp.getF();
            _xifexpression_3 = Data.getStaticPropertyInRTCTLExpression(_f_1);
          } else {
            Object _xblockexpression_2 = null;
            {
              RTCTL _f1 = rtctl_exp.getF1();
              Data.getStaticPropertyInRTCTLExpression(_f1);
              RTCTL _f2 = rtctl_exp.getF2();
              _xblockexpression_2 = Data.getStaticPropertyInRTCTLExpression(_f2);
            }
            _xifexpression_3 = _xblockexpression_2;
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public static Object getVarValPropertyInStatement(final Statement sta) {
    Object _xblockexpression = null;
    {
      if ((sta instanceof GetProcess)) {
        ChangeAction _change = ((GetProcess)sta).getChange();
        boolean _notEquals = (!Objects.equal(_change, null));
        if (_notEquals) {
          ChangeAction _change_1 = ((GetProcess)sta).getChange();
          EList<Statement> _sta = _change_1.getSta();
          for (final Statement st : _sta) {
            if ((st instanceof ChangeValueUnOp)) {
              Data.getVarValPropertyInStatement(st);
            } else {
              if ((st instanceof ChangeValueExpression)) {
                Data.getVarValPropertyInStatement(st);
              }
            }
          }
        }
        Expression _time = ((GetProcess)sta).getTime();
        boolean _notEquals_1 = (!Objects.equal(_time, null));
        if (_notEquals_1) {
          Expression _time_1 = ((GetProcess)sta).getTime();
          Data.getStaticPropertyInExpression(_time_1);
        }
      }
      if ((sta instanceof IfStatement)) {
        Expression _condition = ((IfStatement)sta).getCondition();
        Data.getStaticPropertyInExpression(_condition);
        Statement _thenBlock = ((IfStatement)sta).getThenBlock();
        Data.getVarValPropertyInStatement(_thenBlock);
        Statement _elseBlock = ((IfStatement)sta).getElseBlock();
        boolean _notEquals_2 = (!Objects.equal(_elseBlock, null));
        if (_notEquals_2) {
          Statement _elseBlock_1 = ((IfStatement)sta).getElseBlock();
          Data.getVarValPropertyInStatement(_elseBlock_1);
        }
      }
      if ((sta instanceof BlockStatement)) {
        EList<Statement> _statements = ((BlockStatement)sta).getStatements();
        for (final Statement st_1 : _statements) {
          Data.getVarValPropertyInStatement(st_1);
        }
      }
      Object _xifexpression = null;
      if ((sta instanceof ChangeValue)) {
        Object _xblockexpression_1 = null;
        {
          if ((sta instanceof ChangeValueUnOp)) {
            QualifiedNames _var = ((ChangeValueUnOp)sta).getVar();
            ICompositeNode _node = NodeModelUtils.getNode(_var);
            String _tokenText = NodeModelUtils.getTokenText(_node);
            String sn = Data.getProcessNameInQualifiedNames(_tokenText);
            boolean _equals = sn.equals("");
            boolean _not = (!_equals);
            if (_not) {
              Data.varPropertyList.add(sn);
            }
          }
          Object _xifexpression_1 = null;
          if ((sta instanceof ChangeValueExpression)) {
            Object _xblockexpression_2 = null;
            {
              QualifiedNames _var_1 = ((ChangeValueExpression)sta).getVar();
              boolean _notEquals_3 = (!Objects.equal(_var_1, null));
              if (_notEquals_3) {
                QualifiedNames _var_2 = ((ChangeValueExpression)sta).getVar();
                ProcessPropertyName _prop = _var_2.getProp();
                boolean _notEquals_4 = (!Objects.equal(_prop, null));
                if (_notEquals_4) {
                  QualifiedNames _var_3 = ((ChangeValueExpression)sta).getVar();
                  ProcessPropertyName _prop_1 = _var_3.getProp();
                  String _name = _prop_1.getName();
                  Data.varPropertyList.add(_name);
                }
              }
              Expression _exp = ((ChangeValueExpression)sta).getExp();
              _xblockexpression_2 = Data.getStaticPropertyInExpression(_exp);
            }
            _xifexpression_1 = _xblockexpression_2;
          }
          _xblockexpression_1 = _xifexpression_1;
        }
        _xifexpression = _xblockexpression_1;
      }
      _xblockexpression = _xifexpression;
    }
    return _xblockexpression;
  }
  
  public static String getProcessNameInQualifiedNames(final String qn) {
    int pos = qn.indexOf(".");
    if ((pos < 0)) {
      return "";
    } else {
      return qn.substring(0, pos);
    }
  }
  
  public static Object getStaticPropertyInExpression(final Expression exp) {
    Object _xifexpression = null;
    if ((exp instanceof Or)) {
      Object _xblockexpression = null;
      {
        Expression _left = ((Or)exp).getLeft();
        Data.getStaticPropertyInExpression(_left);
        Object _xifexpression_1 = null;
        Expression _right = ((Or)exp).getRight();
        boolean _notEquals = (!Objects.equal(_right, null));
        if (_notEquals) {
          Expression _right_1 = ((Or)exp).getRight();
          _xifexpression_1 = Data.getStaticPropertyInExpression(_right_1);
        }
        _xblockexpression = _xifexpression_1;
      }
      _xifexpression = _xblockexpression;
    } else {
      Object _xifexpression_1 = null;
      if ((exp instanceof And)) {
        Object _xblockexpression_1 = null;
        {
          Expression _left = ((And)exp).getLeft();
          Data.getStaticPropertyInExpression(_left);
          Expression _right = ((And)exp).getRight();
          _xblockexpression_1 = Data.getStaticPropertyInExpression(_right);
        }
        _xifexpression_1 = _xblockexpression_1;
      } else {
        Object _xifexpression_2 = null;
        if ((exp instanceof Equality)) {
          Object _xblockexpression_2 = null;
          {
            Expression _left = ((Equality)exp).getLeft();
            Data.getStaticPropertyInExpression(_left);
            Expression _right = ((Equality)exp).getRight();
            _xblockexpression_2 = Data.getStaticPropertyInExpression(_right);
          }
          _xifexpression_2 = _xblockexpression_2;
        } else {
          Object _xifexpression_3 = null;
          if ((exp instanceof Comparison)) {
            Object _xblockexpression_3 = null;
            {
              Expression _left = ((Comparison)exp).getLeft();
              Data.getStaticPropertyInExpression(_left);
              Expression _right = ((Comparison)exp).getRight();
              _xblockexpression_3 = Data.getStaticPropertyInExpression(_right);
            }
            _xifexpression_3 = _xblockexpression_3;
          } else {
            Object _xifexpression_4 = null;
            if ((exp instanceof Plus)) {
              Object _xblockexpression_4 = null;
              {
                Expression _left = ((Plus)exp).getLeft();
                Data.getStaticPropertyInExpression(_left);
                Expression _right = ((Plus)exp).getRight();
                _xblockexpression_4 = Data.getStaticPropertyInExpression(_right);
              }
              _xifexpression_4 = _xblockexpression_4;
            } else {
              Object _xifexpression_5 = null;
              if ((exp instanceof Minus)) {
                Object _xblockexpression_5 = null;
                {
                  Expression _left = ((Minus)exp).getLeft();
                  Data.getStaticPropertyInExpression(_left);
                  Expression _right = ((Minus)exp).getRight();
                  _xblockexpression_5 = Data.getStaticPropertyInExpression(_right);
                }
                _xifexpression_5 = _xblockexpression_5;
              } else {
                Object _xifexpression_6 = null;
                if ((exp instanceof MulOrDiv)) {
                  Object _xblockexpression_6 = null;
                  {
                    Expression _left = ((MulOrDiv)exp).getLeft();
                    Data.getStaticPropertyInExpression(_left);
                    Expression _right = ((MulOrDiv)exp).getRight();
                    _xblockexpression_6 = Data.getStaticPropertyInExpression(_right);
                  }
                  _xifexpression_6 = _xblockexpression_6;
                } else {
                  Object _xifexpression_7 = null;
                  if ((exp instanceof Implies)) {
                    Object _xblockexpression_7 = null;
                    {
                      Expression _left = ((Implies)exp).getLeft();
                      Data.getStaticPropertyInExpression(_left);
                      Expression _right = ((Implies)exp).getRight();
                      _xblockexpression_7 = Data.getStaticPropertyInExpression(_right);
                    }
                    _xifexpression_7 = _xblockexpression_7;
                  } else {
                    Object _xifexpression_8 = null;
                    if ((exp instanceof Not)) {
                      Expression _expression = ((Not)exp).getExpression();
                      _xifexpression_8 = Data.getStaticPropertyInExpression(_expression);
                    } else {
                      boolean _xifexpression_9 = false;
                      if ((exp instanceof QualifiedNames)) {
                        _xifexpression_9 = Data.getStaticPropertyInQualifiedNames(((QualifiedNames)exp));
                      } else {
                        boolean _xifexpression_10 = false;
                        if ((exp instanceof Atomic)) {
                          boolean _xifexpression_11 = false;
                          QualifiedNames _var = ((Atomic)exp).getVar();
                          boolean _notEquals = (!Objects.equal(_var, null));
                          if (_notEquals) {
                            QualifiedNames _var_1 = ((Atomic)exp).getVar();
                            _xifexpression_11 = Data.getStaticPropertyInQualifiedNames(_var_1);
                          }
                          _xifexpression_10 = _xifexpression_11;
                        }
                        _xifexpression_9 = _xifexpression_10;
                      }
                      _xifexpression_8 = Boolean.valueOf(_xifexpression_9);
                    }
                    _xifexpression_7 = _xifexpression_8;
                  }
                  _xifexpression_6 = _xifexpression_7;
                }
                _xifexpression_5 = _xifexpression_6;
              }
              _xifexpression_4 = _xifexpression_5;
            }
            _xifexpression_3 = _xifexpression_4;
          }
          _xifexpression_2 = _xifexpression_3;
        }
        _xifexpression_1 = _xifexpression_2;
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  public static boolean getStaticPropertyInQualifiedNames(final QualifiedNames qname) {
    boolean _xifexpression = false;
    ProcessPropertyName _prop = qname.getProp();
    boolean _notEquals = (!Objects.equal(_prop, null));
    if (_notEquals) {
      ProcessPropertyName _prop_1 = qname.getProp();
      String _name = _prop_1.getName();
      _xifexpression = Data.valPropertyList.add(_name);
    }
    return _xifexpression;
  }
  
  public static void removeVariableFromValueList() {
    for (final String p : Data.varPropertyList) {
      Data.valPropertyList.remove(p);
    }
    for (final String p_1 : Data.valPropertyList) {
      Data.varPropertyList.add(p_1);
    }
  }
  
  public static void addfixedVariable() {
    for (final String p : Data.fixedvalPropertyList) {
      {
        Data.varPropertyList.remove(p);
        Data.valPropertyList.add(p);
      }
    }
    for (final String p_1 : Data.fixedvarPropertyList) {
      {
        Data.valPropertyList.remove(p_1);
        Data.varPropertyList.add(p_1);
      }
    }
  }
  
  public static void getCheckPointsList(final SchedulerDSL schModel) {
    boolean _equals = Objects.equal(schModel, null);
    if (_equals) {
      return;
    }
    SchedulerDef sch = schModel.getScheduler();
    boolean _notEquals = (!Objects.equal(sch, null));
    if (_notEquals) {
      HandlerDef _handler = sch.getHandler();
      boolean _notEquals_1 = (!Objects.equal(_handler, null));
      if (_notEquals_1) {
        HandlerDef _handler_1 = sch.getHandler();
        EList<EventDef> _event = _handler_1.getEvent();
        boolean _notEquals_2 = (!Objects.equal(_event, null));
        if (_notEquals_2) {
          HandlerDef _handler_2 = sch.getHandler();
          EList<EventDef> _event_1 = _handler_2.getEvent();
          for (final EventDef h : _event_1) {
            EObject _event_2 = h.getEvent();
            if ((_event_2 instanceof EventStm)) {
              EObject _event_3 = h.getEvent();
              EList<Stm> _statements = ((EventStm) _event_3).getStatements();
              for (final Stm sta : _statements) {
                Statement _statement = sta.getStatement();
                Data.getCheckPointList(_statement);
              }
            } else {
              EObject _event_4 = h.getEvent();
              EList<Opt> _opt = ((EventOpt) _event_4).getOpt();
              for (final Opt opt : _opt) {
                EventStm _eventstm = opt.getEventstm();
                EList<Stm> _statements_1 = _eventstm.getStatements();
                for (final Stm sta_1 : _statements_1) {
                  Statement _statement_1 = sta_1.getStatement();
                  Data.getCheckPointList(_statement_1);
                }
              }
            }
          }
        }
      }
      InterfaceDef _interface = sch.getInterface();
      boolean _notEquals_3 = (!Objects.equal(_interface, null));
      if (_notEquals_3) {
        InterfaceDef _interface_1 = sch.getInterface();
        EList<InterfaceFunction> _interfacefunction = _interface_1.getInterfacefunction();
        for (final InterfaceFunction i : _interfacefunction) {
          EList<Stm> _statements_2 = i.getStatements();
          for (final Stm sta_2 : _statements_2) {
            Statement _statement_2 = sta_2.getStatement();
            Data.getCheckPointList(_statement_2);
          }
        }
      }
    }
  }
  
  public static Object getCheckPointList(final Statement sta) {
    Object _xifexpression = null;
    if ((sta instanceof CheckPoint)) {
      boolean _xifexpression_1 = false;
      PointID _pointid = ((CheckPoint)sta).getPointid();
      String _name = _pointid.getName();
      boolean _contains = Data.checkPointsList.contains(_name);
      boolean _not = (!_contains);
      if (_not) {
        PointID _pointid_1 = ((CheckPoint)sta).getPointid();
        String _name_1 = _pointid_1.getName();
        _xifexpression_1 = Data.checkPointsList.add(_name_1);
      }
      _xifexpression = Boolean.valueOf(_xifexpression_1);
    } else {
      Object _xifexpression_2 = null;
      if ((sta instanceof LoopProcess)) {
        Statement _statement = ((LoopProcess)sta).getStatement();
        _xifexpression_2 = Data.getCheckPointList(_statement);
      } else {
        Object _xifexpression_3 = null;
        if ((sta instanceof IfStatement)) {
          Object _xblockexpression = null;
          {
            Statement _thenBlock = ((IfStatement)sta).getThenBlock();
            Data.getCheckPointList(_thenBlock);
            Object _xifexpression_4 = null;
            Statement _elseBlock = ((IfStatement)sta).getElseBlock();
            boolean _notEquals = (!Objects.equal(_elseBlock, null));
            if (_notEquals) {
              Statement _elseBlock_1 = ((IfStatement)sta).getElseBlock();
              _xifexpression_4 = Data.getCheckPointList(_elseBlock_1);
            }
            _xblockexpression = _xifexpression_4;
          }
          _xifexpression_3 = _xblockexpression;
        } else {
          if ((sta instanceof BlockStatement)) {
            EList<Statement> _statements = ((BlockStatement)sta).getStatements();
            for (final Statement si : _statements) {
              Data.getCheckPointList(si);
            }
          }
        }
        _xifexpression_2 = _xifexpression_3;
      }
      _xifexpression = _xifexpression_2;
    }
    return _xifexpression;
  }
  
  public static String getType(final String pName) {
    String _get = Data.intProperties.get(pName);
    boolean _notEquals = (!Objects.equal(_get, null));
    if (_notEquals) {
      return "int";
    }
    String _get_1 = Data.boolProperties.get(pName);
    boolean _notEquals_1 = (!Objects.equal(_get_1, null));
    if (_notEquals_1) {
      return "boolean";
    }
    String _get_2 = Data.byteProperties.get(pName);
    boolean _notEquals_2 = (!Objects.equal(_get_2, null));
    if (_notEquals_2) {
      return "byte";
    }
    String _get_3 = Data.clockProperties.get(pName);
    boolean _notEquals_3 = (!Objects.equal(_get_3, null));
    if (_notEquals_3) {
      return "int";
    }
    String _get_4 = Data.periodicClockProperties.get(pName);
    boolean _notEquals_4 = (!Objects.equal(_get_4, null));
    if (_notEquals_4) {
      return "int";
    }
    String _get_5 = Data.periodicClockOffset.get(pName);
    boolean _notEquals_5 = (!Objects.equal(_get_5, null));
    if (_notEquals_5) {
      return "int";
    }
    return "int";
  }
  
  public static String getValue(final String pName) {
    String _get = Data.intProperties.get(pName);
    boolean _notEquals = (!Objects.equal(_get, null));
    if (_notEquals) {
      return Data.intProperties.get(pName);
    }
    String _get_1 = Data.boolProperties.get(pName);
    boolean _notEquals_1 = (!Objects.equal(_get_1, null));
    if (_notEquals_1) {
      return Data.boolProperties.get(pName);
    }
    String _get_2 = Data.byteProperties.get(pName);
    boolean _notEquals_2 = (!Objects.equal(_get_2, null));
    if (_notEquals_2) {
      return Data.byteProperties.get(pName);
    }
    String _get_3 = Data.clockProperties.get(pName);
    boolean _notEquals_3 = (!Objects.equal(_get_3, null));
    if (_notEquals_3) {
      return Data.clockProperties.get(pName);
    }
    String _get_4 = Data.periodicClockProperties.get(pName);
    boolean _notEquals_4 = (!Objects.equal(_get_4, null));
    if (_notEquals_4) {
      return Data.periodicClockProperties.get(pName);
    }
    String _get_5 = Data.periodicClockOffset.get(pName);
    boolean _notEquals_5 = (!Objects.equal(_get_5, null));
    if (_notEquals_5) {
      return Data.periodicClockOffset.get(pName);
    }
    return "";
  }
  
  public static String getPeriodicClockVariable(final PeriodicProcess periodic) {
    Element _element = periodic.getElement();
    scheduling.dsl.Process _process = _element.getProcess();
    String _name = _process.getName();
    String clockName = (_name + "_");
    Data.hasPeriodicProcess = true;
    Element _element_1 = periodic.getElement();
    EList<Value> _paraAssign = _element_1.getParaAssign();
    for (final Value value : _paraAssign) {
      NumValue _num = value.getNum();
      boolean _notEquals = (!Objects.equal(_num, null));
      if (_notEquals) {
        String _clockName = clockName;
        NumValue _num_1 = value.getNum();
        int _value = _num_1.getValue();
        String _plus = (Integer.valueOf(_value) + "_");
        clockName = (_clockName + _plus);
      } else {
        String _clockName_1 = clockName;
        BoolValue _bool = value.getBool();
        String _value_1 = _bool.getValue();
        String _plus_1 = (_value_1 + "_");
        clockName = (_clockName_1 + _plus_1);
      }
    }
    String _clockName_2 = clockName;
    NumValue _period = periodic.getPeriod();
    int _value_2 = _period.getValue();
    String _plus_2 = ("_" + Integer.valueOf(_value_2));
    String _plus_3 = (_plus_2 + "_");
    NumValue _offset = periodic.getOffset();
    int _value_3 = _offset.getValue();
    String _plus_4 = (_plus_3 + Integer.valueOf(_value_3));
    clockName = (_clockName_2 + _plus_4);
    return clockName;
  }
  
  public static void getPeriodicClock(final ProcessDSL procModel) {
    ProcessConfig _processconfig = procModel.getProcessconfig();
    boolean _notEquals = (!Objects.equal(_processconfig, null));
    if (_notEquals) {
      ProcessConfig _processconfig_1 = procModel.getProcessconfig();
      EList<ConfigProcess> _procinit = _processconfig_1.getProcinit();
      for (final ConfigProcess proc : _procinit) {
        PeriodicProcess _periodic = proc.getPeriodic();
        boolean _notEquals_1 = (!Objects.equal(_periodic, null));
        if (_notEquals_1) {
          PeriodicProcess _periodic_1 = proc.getPeriodic();
          String _periodicClockVariable = Data.getPeriodicClockVariable(_periodic_1);
          PeriodicProcess _periodic_2 = proc.getPeriodic();
          NumValue _period = _periodic_2.getPeriod();
          int _value = _period.getValue();
          String _string = Integer.valueOf(_value).toString();
          Data.periodicClockProperties.put(_periodicClockVariable, _string);
          PeriodicProcess _periodic_3 = proc.getPeriodic();
          String _periodicClockVariable_1 = Data.getPeriodicClockVariable(_periodic_3);
          PeriodicProcess _periodic_4 = proc.getPeriodic();
          NumValue _offset = _periodic_4.getOffset();
          int _value_1 = _offset.getValue();
          String _string_1 = Integer.valueOf(_value_1).toString();
          Data.periodicClockOffset.put(_periodicClockVariable_1, _string_1);
        }
      }
    }
  }
  
  public static void getProcessPropertyListFromProcessModel(final ProcessDSL procModel) {
    ProcessDataDef _processdata = procModel.getProcessdata();
    boolean _notEquals = (!Objects.equal(_processdata, null));
    if (_notEquals) {
      ProcessDataDef _processdata_1 = procModel.getProcessdata();
      EList<ProcessPropertyDef> _properties = _processdata_1.getProperties();
      boolean _notEquals_1 = (!Objects.equal(_properties, null));
      if (_notEquals_1) {
        ProcessDataDef _processdata_2 = procModel.getProcessdata();
        EList<ProcessPropertyDef> _properties_1 = _processdata_2.getProperties();
        for (final ProcessPropertyDef p : _properties_1) {
          {
            boolean _isVal = p.isVal();
            if (_isVal) {
              EList<ProcessPropertyName> _name = p.getName();
              for (final ProcessPropertyName pn : _name) {
                String _name_1 = pn.getName();
                Data.fixedvalPropertyList.add(_name_1);
              }
            }
            boolean _isVar = p.isVar();
            if (_isVar) {
              EList<ProcessPropertyName> _name_2 = p.getName();
              for (final ProcessPropertyName pn_1 : _name_2) {
                String _name_3 = pn_1.getName();
                Data.fixedvarPropertyList.add(_name_3);
              }
            }
            scheduling.dsl.String _type = p.getType();
            String _string = _type.toString();
            switch (_string) {
              case "time":
              case "int":
                EList<ProcessPropertyName> _name_4 = p.getName();
                for (final ProcessPropertyName pn_2 : _name_4) {
                  {
                    String _name_5 = pn_2.getName();
                    Data.varPropertyList.add(_name_5);
                    Value _pvalue = p.getPvalue();
                    boolean _equals = Objects.equal(_pvalue, null);
                    if (_equals) {
                      String _name_6 = pn_2.getName();
                      Data.intProperties.put(_name_6, "");
                    } else {
                      String _name_7 = pn_2.getName();
                      Value _pvalue_1 = p.getPvalue();
                      ICompositeNode _node = NodeModelUtils.getNode(_pvalue_1);
                      String _tokenText = NodeModelUtils.getTokenText(_node);
                      Data.intProperties.put(_name_7, _tokenText);
                    }
                  }
                }
                break;
              case "bool":
                EList<ProcessPropertyName> _name_5 = p.getName();
                for (final ProcessPropertyName pn_3 : _name_5) {
                  {
                    String _name_6 = pn_3.getName();
                    Data.varPropertyList.add(_name_6);
                    Value _pvalue = p.getPvalue();
                    boolean _equals = Objects.equal(_pvalue, null);
                    if (_equals) {
                      String _name_7 = pn_3.getName();
                      Data.boolProperties.put(_name_7, "");
                    } else {
                      String _name_8 = pn_3.getName();
                      Value _pvalue_1 = p.getPvalue();
                      BoolValue _bool = _pvalue_1.getBool();
                      String _value = _bool.getValue();
                      String _string_1 = _value.toString();
                      Data.boolProperties.put(_name_8, _string_1);
                    }
                  }
                }
                break;
              case "byte":
                EList<ProcessPropertyName> _name_6 = p.getName();
                for (final ProcessPropertyName pn_4 : _name_6) {
                  {
                    String _name_7 = pn_4.getName();
                    Data.varPropertyList.add(_name_7);
                    Value _pvalue = p.getPvalue();
                    boolean _equals = Objects.equal(_pvalue, null);
                    if (_equals) {
                      String _name_8 = pn_4.getName();
                      Data.byteProperties.put(_name_8, "");
                    } else {
                      String _name_9 = pn_4.getName();
                      Value _pvalue_1 = p.getPvalue();
                      NumValue _num = _pvalue_1.getNum();
                      int _value = _num.getValue();
                      String _string_1 = Integer.valueOf(_value).toString();
                      Data.byteProperties.put(_name_9, _string_1);
                    }
                  }
                }
                break;
              case "clock":
                EList<ProcessPropertyName> _name_7 = p.getName();
                for (final ProcessPropertyName pn_5 : _name_7) {
                  {
                    String _name_8 = pn_5.getName();
                    Data.varPropertyList.add(_name_8);
                    Value _pvalue = p.getPvalue();
                    boolean _equals = Objects.equal(_pvalue, null);
                    if (_equals) {
                      String _name_9 = pn_5.getName();
                      Data.clockProperties.put(_name_9, "");
                    } else {
                      String _name_10 = pn_5.getName();
                      Value _pvalue_1 = p.getPvalue();
                      NumValue _num = _pvalue_1.getNum();
                      int _value = _num.getValue();
                      String _string_1 = Integer.valueOf(_value).toString();
                      Data.clockProperties.put(_name_10, _string_1);
                    }
                    String _name_11 = pn_5.getName();
                    Data.clockPropertyList.add(_name_11);
                  }
                }
                break;
              default:
                return;
            }
          }
        }
      }
    }
  }
  
  public static void getCollectionList(final SchedulerDef sch) {
    Data.collectionList.clear();
    SchedulerDataDef _schedulerdata = sch.getSchedulerdata();
    boolean _notEquals = (!Objects.equal(_schedulerdata, null));
    if (_notEquals) {
      SchedulerDataDef _schedulerdata_1 = sch.getSchedulerdata();
      EList<DataDef> _datadef = _schedulerdata_1.getDatadef();
      for (final DataDef schdata : _datadef) {
        DataBlockDef _datablockdef = schdata.getDatablockdef();
        boolean _notEquals_1 = (!Objects.equal(_datablockdef, null));
        if (_notEquals_1) {
          DataBlockDef _datablockdef_1 = schdata.getDatablockdef();
          EList<DataSingleDef> _datadef_1 = _datablockdef_1.getDatadef();
          for (final DataSingleDef data : _datadef_1) {
            SchedulerCollectionDef _col = data.getCol();
            boolean _notEquals_2 = (!Objects.equal(_col, null));
            if (_notEquals_2) {
              SchedulerCollectionDef _col_1 = data.getCol();
              SchedulerSet _name = _col_1.getName();
              String _name_1 = _name.getName();
              String _trim = _name_1.trim();
              Data.collectionList.add(_trim);
            }
          }
        } else {
          DataSingleDef _datasingledef = schdata.getDatasingledef();
          SchedulerCollectionDef _col_2 = _datasingledef.getCol();
          boolean _notEquals_3 = (!Objects.equal(_col_2, null));
          if (_notEquals_3) {
            DataSingleDef _datasingledef_1 = schdata.getDatasingledef();
            SchedulerCollectionDef _col_3 = _datasingledef_1.getCol();
            SchedulerSet _name_2 = _col_3.getName();
            String _name_3 = _name_2.getName();
            String _trim_1 = _name_3.trim();
            Data.collectionList.add(_trim_1);
          }
        }
      }
    }
  }
  
  public static void initPropertyList() {
    Data.intProperties.clear();
    Data.boolProperties.clear();
    Data.byteProperties.clear();
    Data.clockProperties.clear();
    Data.periodicClockProperties.clear();
    Data.periodicClockOffset.clear();
    Data.varPropertyList.clear();
    Data.valPropertyList.clear();
    Data.defPropertyList.clear();
    Data.fixedvarPropertyList.clear();
    Data.fixedvalPropertyList.clear();
    Data.clockPropertyList.clear();
  }
  
  public static void init(final ProcessDSL procModel, final SchedulerDSL schModel) {
    Data.initPropertyList();
    Data.getPeriodicClock(procModel);
    Data.getProcessPropertyListFromProcessModel(procModel);
    Data.getVarValPropertyInSchedulerModel(schModel);
    Data.removeVariableFromValueList();
    Data.fixClockProperty();
    Data.addfixedVariable();
    SchedulerDef _scheduler = schModel.getScheduler();
    boolean _hasRunTime = Statements.hasRunTime(_scheduler);
    Data.runTime = _hasRunTime;
    Data.checkPointsList.clear();
    ProcessDataDef _processdata = Data.procModel.getProcessdata();
    boolean _notEquals = (!Objects.equal(_processdata, null));
    if (_notEquals) {
      ProcessDataDef _processdata_1 = Data.procModel.getProcessdata();
      EList<ProcessPropertyDef> _properties = _processdata_1.getProperties();
      for (final ProcessPropertyDef proplist : _properties) {
        EList<ProcessPropertyName> _name = proplist.getName();
        for (final ProcessPropertyName pname : _name) {
          String _name_1 = pname.getName();
          Data.defPropertyList.add(_name_1);
        }
      }
    }
  }
}
