package scheduling.generator;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import scheduling.dsl.Action;
import scheduling.dsl.And;
import scheduling.dsl.AssertStatement;
import scheduling.dsl.Atomic;
import scheduling.dsl.BlockStatement;
import scheduling.dsl.BoolConstant;
import scheduling.dsl.BoolValue;
import scheduling.dsl.BranchID;
import scheduling.dsl.CallFunction;
import scheduling.dsl.ChangeAction;
import scheduling.dsl.ChangeListValue;
import scheduling.dsl.ChangeValue;
import scheduling.dsl.ChangeValueExpression;
import scheduling.dsl.ChangeValueUnOp;
import scheduling.dsl.CheckPoint;
import scheduling.dsl.Comparison;
import scheduling.dsl.DeclareProcess;
import scheduling.dsl.DefCore;
import scheduling.dsl.EmptyExpression;
import scheduling.dsl.Equality;
import scheduling.dsl.EventDef;
import scheduling.dsl.EventOpt;
import scheduling.dsl.EventStm;
import scheduling.dsl.ExecuteProcess;
import scheduling.dsl.ExistExpression;
import scheduling.dsl.ExistP;
import scheduling.dsl.ExistPID;
import scheduling.dsl.Expression;
import scheduling.dsl.GenCodeStatement;
import scheduling.dsl.GenLnCodeStatement;
import scheduling.dsl.GetIDExpression;
import scheduling.dsl.GetInstance;
import scheduling.dsl.GetName;
import scheduling.dsl.GetPID;
import scheduling.dsl.GetProcess;
import scheduling.dsl.HandlerDef;
import scheduling.dsl.HasID;
import scheduling.dsl.HasIDINT;
import scheduling.dsl.HasNameExpression;
import scheduling.dsl.IfDef;
import scheduling.dsl.IfStatement;
import scheduling.dsl.Implies;
import scheduling.dsl.InExpression;
import scheduling.dsl.InPExpression;
import scheduling.dsl.InPIDExpression;
import scheduling.dsl.IntConstant;
import scheduling.dsl.LTE;
import scheduling.dsl.ListElement;
import scheduling.dsl.LoopProcess;
import scheduling.dsl.Minus;
import scheduling.dsl.MoveProcess;
import scheduling.dsl.MulOrDiv;
import scheduling.dsl.NewElement;
import scheduling.dsl.NewProcessStatement;
import scheduling.dsl.Not;
import scheduling.dsl.NullExpression;
import scheduling.dsl.NumValue;
import scheduling.dsl.Opt;
import scheduling.dsl.Or;
import scheduling.dsl.OrderType;
import scheduling.dsl.ParaValue;
import scheduling.dsl.Plus;
import scheduling.dsl.PointID;
import scheduling.dsl.PrintLogStatement;
import scheduling.dsl.PrintStatement;
import scheduling.dsl.ProcessPropertyName;
import scheduling.dsl.QualifiedNames;
import scheduling.dsl.RTCTL;
import scheduling.dsl.RemoveProcess;
import scheduling.dsl.ReorderProcess;
import scheduling.dsl.ReturnStatement;
import scheduling.dsl.SchedulerDataDef;
import scheduling.dsl.SchedulerDef;
import scheduling.dsl.SchedulerSet;
import scheduling.dsl.SetExecTime;
import scheduling.dsl.SetProcess;
import scheduling.dsl.SetProcessInstance;
import scheduling.dsl.SetReturnCol;
import scheduling.dsl.SetReturnSet;
import scheduling.dsl.StateID;
import scheduling.dsl.Statement;
import scheduling.dsl.Step;
import scheduling.dsl.Stm;
import scheduling.dsl.SysVar;
import scheduling.dsl.TotalStep;
import scheduling.generator.Data;
import scheduling.generator.SchedulerTestGenerator;

@SuppressWarnings("all")
public class Statements {
  public static boolean hasRunTime(final SchedulerDef sch) {
    HandlerDef _handler = sch.getHandler();
    boolean _notEquals = (!Objects.equal(_handler, null));
    if (_notEquals) {
      HandlerDef _handler_1 = sch.getHandler();
      EList<EventDef> _event = _handler_1.getEvent();
      for (final EventDef e : _event) {
        EObject _event_1 = e.getEvent();
        if ((_event_1 instanceof EventStm)) {
          EObject _event_2 = e.getEvent();
          EList<Stm> _statements = ((EventStm) _event_2).getStatements();
          for (final Stm sta : _statements) {
            Statement _statement = sta.getStatement();
            boolean _isRunTime = Statements.isRunTime(_statement);
            if (_isRunTime) {
              return true;
            }
          }
        } else {
          EObject _event_3 = e.getEvent();
          EList<Opt> _opt = ((EventOpt) _event_3).getOpt();
          for (final Opt opt : _opt) {
            EventStm _eventstm = opt.getEventstm();
            EList<Stm> _statements_1 = _eventstm.getStatements();
            for (final Stm sta_1 : _statements_1) {
              Statement _statement_1 = sta_1.getStatement();
              boolean _isRunTime_1 = Statements.isRunTime(_statement_1);
              if (_isRunTime_1) {
                return true;
              }
            }
          }
        }
      }
    }
    return false;
  }
  
  protected static boolean _isRunTime(final MoveProcess stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final SetProcessInstance stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final LoopProcess stm) {
    Statement _statement = stm.getStatement();
    boolean _isRunTime = Statements.isRunTime(_statement);
    if (_isRunTime) {
      return true;
    }
    return false;
  }
  
  protected static boolean _isRunTime(final GetProcess stm) {
    Expression _time = stm.getTime();
    return (!Objects.equal(_time, null));
  }
  
  protected static boolean _isRunTime(final RemoveProcess stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final ChangeValue stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final SetExecTime stm) {
    return true;
  }
  
  protected static boolean _isRunTime(final SetReturnCol stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final SetReturnSet stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final IfStatement stm) {
    Statement _thenBlock = stm.getThenBlock();
    boolean _isRunTime = Statements.isRunTime(_thenBlock);
    if (_isRunTime) {
      return true;
    } else {
      Statement _elseBlock = stm.getElseBlock();
      boolean _notEquals = (!Objects.equal(_elseBlock, null));
      if (_notEquals) {
        Statement _elseBlock_1 = stm.getElseBlock();
        return Statements.isRunTime(_elseBlock_1);
      }
    }
    return false;
  }
  
  protected static boolean _isRunTime(final AssertStatement stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final PrintStatement stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final PrintLogStatement stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final GenCodeStatement stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final GenLnCodeStatement stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final NewProcessStatement stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final CallFunction stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final BlockStatement stm) {
    EList<Statement> _statements = stm.getStatements();
    for (final Statement sta : _statements) {
      boolean _isRunTime = Statements.isRunTime(sta);
      if (_isRunTime) {
        return true;
      }
    }
    return false;
  }
  
  protected static boolean _isRunTime(final ReturnStatement stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final CheckPoint stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final DeclareProcess stm) {
    return false;
  }
  
  protected static boolean _isRunTime(final SetProcess stm) {
    return false;
  }
  
  public static CharSequence genIfDef(final IfDef con) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _notEquals = (!Objects.equal(con, null));
      if (_notEquals) {
        _builder.append("if (");
        Expression _cond = con.getCond();
        String _dispatchExpression = Statements.dispatchExpression(_cond);
        _builder.append(_dispatchExpression, "");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public static CharSequence genCloseIfDef(final IfDef con) {
    StringConcatenation _builder = new StringConcatenation();
    {
      boolean _notEquals = (!Objects.equal(con, null));
      if (_notEquals) {
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public static CharSequence genStm(final Stm st, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    IfDef _ifdef = st.getIfdef();
    CharSequence _genIfDef = Statements.genIfDef(_ifdef);
    _builder.append(_genIfDef, "");
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    Statement _statement = st.getStatement();
    CharSequence _dispatchStatement = Statements.dispatchStatement(_statement, processName);
    _builder.append(_dispatchStatement, "");
    _builder.newLineIfNotEmpty();
    IfDef _ifdef_1 = st.getIfdef();
    CharSequence _genCloseIfDef = Statements.genCloseIfDef(_ifdef_1);
    _builder.append(_genCloseIfDef, "");
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public static String dispatchRTCTLExpression(final RTCTL rtctl_exp) {
    String result = null;
    String _op = rtctl_exp.getOp();
    boolean _equals = _op.equals("or");
    if (_equals) {
      RTCTL _f1 = rtctl_exp.getF1();
      String _dispatchRTCTLExpression = Statements.dispatchRTCTLExpression(_f1);
      String _plus = (_dispatchRTCTLExpression + " || ");
      RTCTL _f2 = rtctl_exp.getF2();
      String _dispatchRTCTLExpression_1 = Statements.dispatchRTCTLExpression(_f2);
      String _plus_1 = (_plus + _dispatchRTCTLExpression_1);
      result = _plus_1;
    } else {
      String _op_1 = rtctl_exp.getOp();
      boolean _equals_1 = _op_1.equals("implies");
      if (_equals_1) {
        RTCTL _f1_1 = rtctl_exp.getF1();
        String _dispatchRTCTLExpression_2 = Statements.dispatchRTCTLExpression(_f1_1);
        String _plus_2 = ("!(" + _dispatchRTCTLExpression_2);
        String _plus_3 = (_plus_2 + ") || ");
        RTCTL _f2_1 = rtctl_exp.getF2();
        String _dispatchRTCTLExpression_3 = Statements.dispatchRTCTLExpression(_f2_1);
        String _plus_4 = (_plus_3 + _dispatchRTCTLExpression_3);
        result = _plus_4;
      } else {
        Expression _exp = rtctl_exp.getExp();
        boolean _notEquals = (!Objects.equal(_exp, null));
        if (_notEquals) {
          Expression _exp_1 = rtctl_exp.getExp();
          String _dispatchExpression = Statements.dispatchExpression(_exp_1);
          String _plus_5 = ("(" + _dispatchExpression);
          String _plus_6 = (_plus_5 + ")");
          result = _plus_6;
        } else {
          String _op_2 = rtctl_exp.getOp();
          String _trim = _op_2.trim();
          boolean _equals_2 = _trim.equals("not");
          if (_equals_2) {
            RTCTL _f = rtctl_exp.getF();
            String _dispatchRTCTLExpression_4 = Statements.dispatchRTCTLExpression(_f);
            String _plus_7 = ("! (" + _dispatchRTCTLExpression_4);
            String _plus_8 = (_plus_7 + ")");
            result = _plus_8;
          } else {
            String _op_3 = rtctl_exp.getOp();
            String _plus_9 = (_op_3 + " ");
            result = _plus_9;
            LTE _lte = rtctl_exp.getLte();
            boolean _notEquals_1 = (!Objects.equal(_lte, null));
            if (_notEquals_1) {
              String _result = result;
              LTE _lte_1 = rtctl_exp.getLte();
              int _num = _lte_1.getNum();
              String _plus_10 = ("<=" + Integer.valueOf(_num));
              result = (_result + _plus_10);
            }
            RTCTL _f_1 = rtctl_exp.getF();
            boolean _notEquals_2 = (!Objects.equal(_f_1, null));
            if (_notEquals_2) {
              String _result_1 = result;
              RTCTL _f_2 = rtctl_exp.getF();
              String _dispatchRTCTLExpression_5 = Statements.dispatchRTCTLExpression(_f_2);
              result = (_result_1 + _dispatchRTCTLExpression_5);
            }
            if (((!Objects.equal(rtctl_exp.getF1(), null)) && (!Objects.equal(rtctl_exp.getF2(), null)))) {
              String _result_2 = result;
              RTCTL _f1_2 = rtctl_exp.getF1();
              String _dispatchRTCTLExpression_6 = Statements.dispatchRTCTLExpression(_f1_2);
              String _plus_11 = (_dispatchRTCTLExpression_6 + "  ");
              RTCTL _f2_2 = rtctl_exp.getF2();
              String _dispatchRTCTLExpression_7 = Statements.dispatchRTCTLExpression(_f2_2);
              String _plus_12 = (_plus_11 + _dispatchRTCTLExpression_7);
              result = (_result_2 + _plus_12);
            }
          }
        }
      }
    }
    return result;
  }
  
  public static String dispatchExpression(final Expression exp) {
    if ((exp instanceof Or)) {
      Expression _left = ((Or)exp).getLeft();
      String _dispatchExpression = Statements.dispatchExpression(_left);
      String _plus = ("(" + _dispatchExpression);
      String _xifexpression = null;
      Expression _right = ((Or)exp).getRight();
      boolean _notEquals = (!Objects.equal(_right, null));
      if (_notEquals) {
        Expression _right_1 = ((Or)exp).getRight();
        String _dispatchExpression_1 = Statements.dispatchExpression(_right_1);
        _xifexpression = (" || " + _dispatchExpression_1);
      }
      String _plus_1 = (_plus + _xifexpression);
      return (_plus_1 + ")");
    } else {
      if ((exp instanceof And)) {
        Expression _left_1 = ((And)exp).getLeft();
        String _dispatchExpression_2 = Statements.dispatchExpression(_left_1);
        String _plus_2 = ("(" + _dispatchExpression_2);
        String _plus_3 = (_plus_2 + " && ");
        Expression _right_2 = ((And)exp).getRight();
        String _dispatchExpression_3 = Statements.dispatchExpression(_right_2);
        String _plus_4 = (_plus_3 + _dispatchExpression_3);
        return (_plus_4 + ")");
      } else {
        if ((exp instanceof Equality)) {
          Expression _left_2 = ((Equality)exp).getLeft();
          String _dispatchExpression_4 = Statements.dispatchExpression(_left_2);
          String _plus_5 = ("(" + _dispatchExpression_4);
          String _op = ((Equality)exp).getOp();
          String _plus_6 = (_plus_5 + _op);
          Expression _right_3 = ((Equality)exp).getRight();
          String _dispatchExpression_5 = Statements.dispatchExpression(_right_3);
          String _plus_7 = (_plus_6 + _dispatchExpression_5);
          return (_plus_7 + ")");
        } else {
          if ((exp instanceof Comparison)) {
            Expression _left_3 = ((Comparison)exp).getLeft();
            String _dispatchExpression_6 = Statements.dispatchExpression(_left_3);
            String _plus_8 = ("(" + _dispatchExpression_6);
            String _op_1 = ((Comparison)exp).getOp();
            String _plus_9 = (_plus_8 + _op_1);
            Expression _right_4 = ((Comparison)exp).getRight();
            String _dispatchExpression_7 = Statements.dispatchExpression(_right_4);
            String _plus_10 = (_plus_9 + _dispatchExpression_7);
            return (_plus_10 + ")");
          } else {
            if ((exp instanceof Plus)) {
              Expression _left_4 = ((Plus)exp).getLeft();
              String _dispatchExpression_8 = Statements.dispatchExpression(_left_4);
              String _plus_11 = ("(" + _dispatchExpression_8);
              String _plus_12 = (_plus_11 + "+");
              Expression _right_5 = ((Plus)exp).getRight();
              String _dispatchExpression_9 = Statements.dispatchExpression(_right_5);
              String _plus_13 = (_plus_12 + _dispatchExpression_9);
              return (_plus_13 + ")");
            } else {
              if ((exp instanceof Minus)) {
                Expression _left_5 = ((Minus)exp).getLeft();
                String _dispatchExpression_10 = Statements.dispatchExpression(_left_5);
                String _plus_14 = ("(" + _dispatchExpression_10);
                String _plus_15 = (_plus_14 + "-");
                Expression _right_6 = ((Minus)exp).getRight();
                String _dispatchExpression_11 = Statements.dispatchExpression(_right_6);
                String _plus_16 = (_plus_15 + _dispatchExpression_11);
                return (_plus_16 + ")");
              } else {
                if ((exp instanceof MulOrDiv)) {
                  Expression _left_6 = ((MulOrDiv)exp).getLeft();
                  String _dispatchExpression_12 = Statements.dispatchExpression(_left_6);
                  String _plus_17 = ("(" + _dispatchExpression_12);
                  String _op_2 = ((MulOrDiv)exp).getOp();
                  String _plus_18 = (_plus_17 + _op_2);
                  Expression _right_7 = ((MulOrDiv)exp).getRight();
                  String _dispatchExpression_13 = Statements.dispatchExpression(_right_7);
                  String _plus_19 = (_plus_18 + _dispatchExpression_13);
                  return (_plus_19 + ")");
                } else {
                  if ((exp instanceof Implies)) {
                    Expression _left_7 = ((Implies)exp).getLeft();
                    String _dispatchExpression_14 = Statements.dispatchExpression(_left_7);
                    String _plus_20 = ("(! " + _dispatchExpression_14);
                    String _plus_21 = (_plus_20 + "||");
                    Expression _right_8 = ((Implies)exp).getRight();
                    String _dispatchExpression_15 = Statements.dispatchExpression(_right_8);
                    String _plus_22 = (_plus_21 + _dispatchExpression_15);
                    return (_plus_22 + ")");
                  } else {
                    if ((exp instanceof Not)) {
                      Expression _expression = ((Not)exp).getExpression();
                      String _dispatchExpression_16 = Statements.dispatchExpression(_expression);
                      String _plus_23 = ("!(" + _dispatchExpression_16);
                      return (_plus_23 + ")");
                    } else {
                      if ((exp instanceof GetIDExpression)) {
                        String _pN = ((GetIDExpression)exp).getPN();
                        String _replace = _pN.replace("\"", "");
                        String _replaceAll = _replace.replaceAll(" ", "");
                        String _trim = _replaceAll.trim();
                        return (_trim + ".processID");
                      } else {
                        if ((exp instanceof InExpression)) {
                          SchedulerSet _col = ((InExpression)exp).getCol();
                          String _name = _col.getName();
                          String _plus_24 = ("(" + _name);
                          String _plus_25 = (_plus_24 + ".hasProcess(");
                          String _pN_1 = ((InExpression)exp).getPN();
                          String _plus_26 = (_plus_25 + _pN_1);
                          return (_plus_26 + ") > 0)");
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
    if ((exp instanceof InPIDExpression)) {
      SchedulerSet _col_1 = ((InPIDExpression)exp).getCol();
      String _name_1 = _col_1.getName();
      String _plus_27 = ("(" + _name_1);
      String _plus_28 = (_plus_27 + ".hasProcess(");
      String _id = ((InPIDExpression)exp).getId();
      String _plus_29 = (_plus_28 + _id);
      return (_plus_29 + ") > 0)");
    }
    if ((exp instanceof InPExpression)) {
      SchedulerSet _col_2 = ((InPExpression)exp).getCol();
      String _name_2 = _col_2.getName();
      String _plus_30 = ("(" + _name_2);
      String _plus_31 = (_plus_30 + ".hasProcess(");
      int _id_1 = ((InPExpression)exp).getId();
      String _plus_32 = (_plus_31 + Integer.valueOf(_id_1));
      return (_plus_32 + ") > 0)");
    } else {
      if ((exp instanceof ExistExpression)) {
        String _pN_2 = ((ExistExpression)exp).getPN();
        String _plus_33 = ("(existsProcess(" + _pN_2);
        return (_plus_33 + ") > 0)");
      } else {
        if ((exp instanceof ExistPID)) {
          String _id_2 = ((ExistPID)exp).getId();
          String _plus_34 = ("(existsProcess(" + _id_2);
          return (_plus_34 + ") > 0)");
        } else {
          if ((exp instanceof ExistP)) {
            int _id_3 = ((ExistP)exp).getId();
            String _plus_35 = ("(existsProcess(" + Integer.valueOf(_id_3));
            return (_plus_35 + ") > 0)");
          } else {
            if ((exp instanceof HasNameExpression)) {
              scheduling.dsl.Process _proc = ((HasNameExpression)exp).getProc();
              String _name_3 = _proc.getName();
              String _plus_36 = ("(" + _name_3);
              String _plus_37 = (_plus_36 + " == null ? false : getStaticPropertyObject(");
              scheduling.dsl.Process _proc_1 = ((HasNameExpression)exp).getProc();
              String _name_4 = _proc_1.getName();
              String _plus_38 = (_plus_37 + _name_4);
              String _plus_39 = (_plus_38 + ".refID).pName.trim().equals(");
              String _pN_3 = ((HasNameExpression)exp).getPN();
              String _plus_40 = (_plus_39 + _pN_3);
              return (_plus_40 + "))");
            } else {
              if ((exp instanceof HasID)) {
                scheduling.dsl.Process _proc_2 = ((HasID)exp).getProc();
                String _name_5 = _proc_2.getName();
                String _plus_41 = ("(" + _name_5);
                String _plus_42 = (_plus_41 + " == null ? false : ");
                scheduling.dsl.Process _proc_3 = ((HasID)exp).getProc();
                String _name_6 = _proc_3.getName();
                String _plus_43 = (_plus_42 + _name_6);
                String _plus_44 = (_plus_43 + ".processID == ");
                String _id_4 = ((HasID)exp).getId();
                String _plus_45 = (_plus_44 + _id_4);
                return (_plus_45 + ")");
              } else {
                if ((exp instanceof HasIDINT)) {
                  scheduling.dsl.Process _proc_4 = ((HasIDINT)exp).getProc();
                  String _name_7 = _proc_4.getName();
                  String _plus_46 = ("(" + _name_7);
                  String _plus_47 = (_plus_46 + " == null ? false : ");
                  scheduling.dsl.Process _proc_5 = ((HasIDINT)exp).getProc();
                  String _name_8 = _proc_5.getName();
                  String _plus_48 = (_plus_47 + _name_8);
                  String _plus_49 = (_plus_48 + ".processID == ");
                  int _id_5 = ((HasIDINT)exp).getId();
                  String _plus_50 = (_plus_49 + Integer.valueOf(_id_5));
                  return (_plus_50 + ")");
                } else {
                  if ((exp instanceof GetName)) {
                    scheduling.dsl.Process _proc_6 = ((GetName)exp).getProc();
                    String _name_9 = _proc_6.getName();
                    String _plus_51 = ("(" + _name_9);
                    String _plus_52 = (_plus_51 + " == null ? \"\" : getStaticPropertyObject(");
                    scheduling.dsl.Process _proc_7 = ((GetName)exp).getProc();
                    String _name_10 = _proc_7.getName();
                    String _plus_53 = (_plus_52 + _name_10);
                    return (_plus_53 + ".refID).pName.trim() )");
                  } else {
                    if ((exp instanceof EmptyExpression)) {
                      SchedulerSet _col_3 = ((EmptyExpression)exp).getCol();
                      String _name_11 = _col_3.getName();
                      String _plus_54 = ("(" + _name_11);
                      return (_plus_54 + ".isEmpty() > 0)");
                    } else {
                      if ((exp instanceof NullExpression)) {
                        scheduling.dsl.Process _procName = ((NullExpression)exp).getProcName();
                        String _name_12 = _procName.getName();
                        String _plus_55 = ("(" + _name_12);
                        return (_plus_55 + " == null)");
                      } else {
                        if ((exp instanceof IntConstant)) {
                          ICompositeNode _node = NodeModelUtils.getNode(exp);
                          return NodeModelUtils.getTokenText(_node);
                        } else {
                          if ((exp instanceof BoolConstant)) {
                            String _value = ((BoolConstant)exp).getValue();
                            return _value.toString();
                          } else {
                            if ((exp instanceof StateID)) {
                              return "\"<StateID>\"";
                            } else {
                              if ((exp instanceof BranchID)) {
                                return "\"<BranchID>\"";
                              } else {
                                if ((exp instanceof GetInstance)) {
                                  scheduling.dsl.Process _procName_1 = ((GetInstance)exp).getProcName();
                                  String _name_13 = _procName_1.getName();
                                  String _plus_56 = ("getInstance(" + _name_13);
                                  return (_plus_56 + ")");
                                } else {
                                  if ((exp instanceof GetPID)) {
                                    scheduling.dsl.Process _procName_2 = ((GetPID)exp).getProcName();
                                    String _name_14 = _procName_2.getName();
                                    return (_name_14 + ".processID");
                                  } else {
                                    if ((exp instanceof Action)) {
                                      return "getAction()";
                                    } else {
                                      if ((exp instanceof Step)) {
                                        return "\"getStep()\"";
                                      } else {
                                        if ((exp instanceof TotalStep)) {
                                          return "\"getTotalStep()\"";
                                        } else {
                                          if ((exp instanceof Atomic)) {
                                            SysVar _sysvar = ((Atomic)exp).getSysvar();
                                            boolean _notEquals_1 = (!Objects.equal(_sysvar, null));
                                            if (_notEquals_1) {
                                              SysVar _sysvar_1 = ((Atomic)exp).getSysvar();
                                              String _vname = _sysvar_1.getVname();
                                              String _plus_57 = ("Integer.parseInt(SchedulerPanModel.panmodel.sysGet(\"" + _vname);
                                              return (_plus_57 + "\"))");
                                            }
                                            QualifiedNames _var = ((Atomic)exp).getVar();
                                            boolean _notEquals_2 = (!Objects.equal(_var, null));
                                            if (_notEquals_2) {
                                              QualifiedNames _var_1 = ((Atomic)exp).getVar();
                                              String v = Statements.dispatchQualifiedNames(_var_1);
                                              return SchedulerTestGenerator.getValue(v);
                                            } else {
                                              String _charseq = ((Atomic)exp).getCharseq();
                                              boolean _notEquals_3 = (!Objects.equal(_charseq, null));
                                              if (_notEquals_3) {
                                                String _charseq_1 = ((Atomic)exp).getCharseq();
                                                return _charseq_1.toString();
                                              } else {
                                                String _string = ((Atomic)exp).getString();
                                                boolean _notEquals_4 = (!Objects.equal(_string, null));
                                                if (_notEquals_4) {
                                                  String _string_1 = ((Atomic)exp).getString();
                                                  String _replace_1 = _string_1.replace("\"", "\\\"");
                                                  String _plus_58 = ("\"" + _replace_1);
                                                  return (_plus_58 + "\"");
                                                }
                                              }
                                            }
                                            ListElement _lele = ((Atomic)exp).getLele();
                                            boolean _notEquals_5 = (!Objects.equal(_lele, null));
                                            if (_notEquals_5) {
                                              ListElement _lele_1 = ((Atomic)exp).getLele();
                                              return SchedulerTestGenerator.getReference(_lele_1);
                                            }
                                            ICompositeNode _node_1 = NodeModelUtils.getNode(exp);
                                            return NodeModelUtils.getTokenText(_node_1);
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
              }
            }
          }
        }
      }
    }
    return null;
  }
  
  public static Object dispatchExpressionwithProcess(final Expression exp, final int count) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("boolean result_");
    _builder.append(count, "");
    _builder.append(" = true ;");
    _builder.newLineIfNotEmpty();
    {
      if ((exp instanceof Or)) {
        _builder.append(" //or\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("{");
        _builder.newLine();
        _builder.append("\t\t");
        Expression _left = ((Or)exp).getLeft();
        Object _dispatchExpressionwithProcess = Statements.dispatchExpressionwithProcess(_left, (count + 1));
        _builder.append(_dispatchExpressionwithProcess, "\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("result_");
        _builder.append(count, "\t\t");
        _builder.append(" = result_");
        _builder.append((count + 1), "\t\t");
        _builder.append(" ;");
        _builder.newLineIfNotEmpty();
        {
          Expression _right = ((Or)exp).getRight();
          boolean _notEquals = (!Objects.equal(_right, null));
          if (_notEquals) {
            _builder.append("\t\t");
            Expression _right_1 = ((Or)exp).getRight();
            Object _dispatchExpressionwithProcess_1 = Statements.dispatchExpressionwithProcess(_right_1, (count + 2));
            _builder.append(_dispatchExpressionwithProcess_1, "\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t");
            _builder.append("result_");
            _builder.append(count, "\t\t");
            _builder.append(" = result_");
            _builder.append((count + 1), "\t\t");
            _builder.append(" || result_");
            _builder.append((count + 2), "\t\t");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      } else {
        {
          if ((exp instanceof And)) {
            _builder.append("//and");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            Expression _left_1 = ((And)exp).getLeft();
            Object _dispatchExpressionwithProcess_2 = Statements.dispatchExpressionwithProcess(_left_1, (count + 1));
            _builder.append(_dispatchExpressionwithProcess_2, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("result_");
            _builder.append(count, "\t");
            _builder.append(" = result_");
            _builder.append((count + 1), "\t");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            Expression _right_2 = ((And)exp).getRight();
            Object _dispatchExpressionwithProcess_3 = Statements.dispatchExpressionwithProcess(_right_2, (count + 2));
            _builder.append(_dispatchExpressionwithProcess_3, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("result_");
            _builder.append(count, "\t");
            _builder.append(" = result_");
            _builder.append((count + 1), "\t");
            _builder.append(" && result_");
            _builder.append((count + 2), "\t");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
          } else {
            {
              if ((exp instanceof Equality)) {
                _builder.append("//equality");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("result_");
                _builder.append(count, "\t");
                _builder.append(" = ");
                Expression _left_2 = ((Equality)exp).getLeft();
                String _dispatchExpression = Statements.dispatchExpression(_left_2, true);
                _builder.append(_dispatchExpression, "\t");
                _builder.append(" == ");
                Expression _right_3 = ((Equality)exp).getRight();
                String _dispatchExpression_1 = Statements.dispatchExpression(_right_3, true);
                _builder.append(_dispatchExpression_1, "\t");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
              } else {
                {
                  if ((exp instanceof HasNameExpression)) {
                    _builder.append("//hasname");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("result_");
                    _builder.append(count, "\t");
                    _builder.append(" = ");
                    scheduling.dsl.Process _proc = ((HasNameExpression)exp).getProc();
                    String _name = _proc.getName();
                    _builder.append(_name, "\t");
                    _builder.append(" == null ? false : getStaticPropertyObject(");
                    scheduling.dsl.Process _proc_1 = ((HasNameExpression)exp).getProc();
                    String _name_1 = _proc_1.getName();
                    _builder.append(_name_1, "\t");
                    _builder.append(".refID).pName.trim().equals(");
                    String _pN = ((HasNameExpression)exp).getPN();
                    _builder.append(_pN, "\t");
                    _builder.append(") ;\t\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                  } else {
                    {
                      if ((exp instanceof HasID)) {
                        _builder.append("//hasID");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("result_");
                        _builder.append(count, "\t");
                        _builder.append(" = ");
                        scheduling.dsl.Process _proc_2 = ((HasID)exp).getProc();
                        String _name_2 = _proc_2.getName();
                        _builder.append(_name_2, "\t");
                        _builder.append(" == null ? false : ");
                        scheduling.dsl.Process _proc_3 = ((HasID)exp).getProc();
                        String _name_3 = _proc_3.getName();
                        _builder.append(_name_3, "\t");
                        _builder.append(".processID == ");
                        String _id = ((HasID)exp).getId();
                        _builder.append(_id, "\t");
                        _builder.append(" ;");
                        _builder.newLineIfNotEmpty();
                      } else {
                        {
                          if ((exp instanceof HasIDINT)) {
                            _builder.append("//hasIDINT");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("result_");
                            _builder.append(count, "\t");
                            _builder.append(" = ");
                            scheduling.dsl.Process _proc_4 = ((HasIDINT)exp).getProc();
                            String _name_4 = _proc_4.getName();
                            _builder.append(_name_4, "\t");
                            _builder.append(" == null ? false : ");
                            scheduling.dsl.Process _proc_5 = ((HasIDINT)exp).getProc();
                            String _name_5 = _proc_5.getName();
                            _builder.append(_name_5, "\t");
                            _builder.append(".processID == ");
                            int _id_1 = ((HasIDINT)exp).getId();
                            _builder.append(_id_1, "\t");
                            _builder.append(" ;");
                            _builder.newLineIfNotEmpty();
                          } else {
                            {
                              if ((exp instanceof InExpression)) {
                                _builder.append("//in");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("result_");
                                _builder.append(count, "\t");
                                _builder.append(" = ");
                                SchedulerSet _col = ((InExpression)exp).getCol();
                                String _name_6 = _col.getName();
                                _builder.append(_name_6, "\t");
                                _builder.append(".hasProcess(");
                                String _pN_1 = ((InExpression)exp).getPN();
                                _builder.append(_pN_1, "\t");
                                _builder.append(") > 0 ;");
                                _builder.newLineIfNotEmpty();
                              } else {
                                {
                                  if ((exp instanceof InPIDExpression)) {
                                    _builder.append("//in");
                                    _builder.newLineIfNotEmpty();
                                    _builder.append("\t");
                                    _builder.append("result_");
                                    _builder.append(count, "\t");
                                    _builder.append(" = ");
                                    SchedulerSet _col_1 = ((InPIDExpression)exp).getCol();
                                    String _name_7 = _col_1.getName();
                                    _builder.append(_name_7, "\t");
                                    _builder.append(".hasProcess(");
                                    String _id_2 = ((InPIDExpression)exp).getId();
                                    _builder.append(_id_2, "\t");
                                    _builder.append(") > 0 ;");
                                    _builder.newLineIfNotEmpty();
                                  } else {
                                    {
                                      if ((exp instanceof InPExpression)) {
                                        _builder.append("//in");
                                        _builder.newLineIfNotEmpty();
                                        _builder.append("\t");
                                        _builder.append("result_");
                                        _builder.append(count, "\t");
                                        _builder.append(" = ");
                                        SchedulerSet _col_2 = ((InPExpression)exp).getCol();
                                        String _name_8 = _col_2.getName();
                                        _builder.append(_name_8, "\t");
                                        _builder.append(".hasProcess(");
                                        int _id_3 = ((InPExpression)exp).getId();
                                        _builder.append(_id_3, "\t");
                                        _builder.append(") > 0 ;");
                                        _builder.newLineIfNotEmpty();
                                      } else {
                                        {
                                          if ((exp instanceof ExistExpression)) {
                                            _builder.append("//exist");
                                            _builder.newLineIfNotEmpty();
                                            _builder.append("\t");
                                            _builder.append("result_");
                                            _builder.append(count, "\t");
                                            _builder.append(" = existsProcess(");
                                            String _pN_2 = ((ExistExpression)exp).getPN();
                                            _builder.append(_pN_2, "\t");
                                            _builder.append(") > 0 ;");
                                            _builder.newLineIfNotEmpty();
                                          } else {
                                            {
                                              if ((exp instanceof ExistPID)) {
                                                _builder.append("//exist");
                                                _builder.newLineIfNotEmpty();
                                                _builder.append("\t");
                                                _builder.append("result_");
                                                _builder.append(count, "\t");
                                                _builder.append(" = existsProcess(");
                                                String _id_4 = ((ExistPID)exp).getId();
                                                _builder.append(_id_4, "\t");
                                                _builder.append(") > 0 ;");
                                                _builder.newLineIfNotEmpty();
                                              } else {
                                                {
                                                  if ((exp instanceof ExistP)) {
                                                    _builder.append("//exist");
                                                    _builder.newLineIfNotEmpty();
                                                    _builder.append("\t");
                                                    _builder.append("result_");
                                                    _builder.append(count, "\t");
                                                    _builder.append(" = existsProcess(");
                                                    int _id_5 = ((ExistP)exp).getId();
                                                    _builder.append(_id_5, "\t");
                                                    _builder.append(") > 0 ;");
                                                    _builder.newLineIfNotEmpty();
                                                  } else {
                                                    {
                                                      if ((exp instanceof EmptyExpression)) {
                                                        _builder.append("//empty");
                                                        _builder.newLineIfNotEmpty();
                                                        _builder.append("\t");
                                                        _builder.append("result_");
                                                        _builder.append(count, "\t");
                                                        _builder.append(" = ");
                                                        SchedulerSet _col_3 = ((EmptyExpression)exp).getCol();
                                                        String _name_9 = _col_3.getName();
                                                        _builder.append(_name_9, "\t");
                                                        _builder.append(".isEmpty > 0 ;");
                                                        _builder.newLineIfNotEmpty();
                                                      } else {
                                                        {
                                                          if ((exp instanceof NullExpression)) {
                                                            _builder.append("//null");
                                                            _builder.newLineIfNotEmpty();
                                                            _builder.append("\t");
                                                            _builder.append("result_");
                                                            _builder.append(count, "\t");
                                                            _builder.append(" = true ;");
                                                            _builder.newLineIfNotEmpty();
                                                            _builder.append("\t");
                                                            _builder.append("{");
                                                            _builder.newLine();
                                                            _builder.append("\t\t");
                                                            _builder.append("//ArrayList<SchedulerProcess> a_");
                                                            scheduling.dsl.Process _procName = ((NullExpression)exp).getProcName();
                                                            String _name_10 = _procName.getName();
                                                            _builder.append(_name_10, "\t\t");
                                                            _builder.append(" = findProcessByAlias(\"");
                                                            scheduling.dsl.Process _procName_1 = ((NullExpression)exp).getProcName();
                                                            String _name_11 = _procName_1.getName();
                                                            _builder.append(_name_11, "\t\t");
                                                            _builder.append("\") ;");
                                                            _builder.newLineIfNotEmpty();
                                                            _builder.append("\t\t");
                                                            _builder.append("if (a_");
                                                            scheduling.dsl.Process _procName_2 = ((NullExpression)exp).getProcName();
                                                            String _name_12 = _procName_2.getName();
                                                            _builder.append(_name_12, "\t\t");
                                                            _builder.append(".size() == 0)");
                                                            _builder.newLineIfNotEmpty();
                                                            _builder.append("\t\t\t");
                                                            _builder.append("result_");
                                                            _builder.append(count, "\t\t\t");
                                                            _builder.append(" = false ;");
                                                            _builder.newLineIfNotEmpty();
                                                            _builder.append("\t");
                                                            _builder.append("}");
                                                            _builder.newLine();
                                                          } else {
                                                            {
                                                              if ((exp instanceof Not)) {
                                                                _builder.append("{");
                                                                _builder.newLine();
                                                                _builder.append("\t");
                                                                Expression _expression = ((Not)exp).getExpression();
                                                                Object _dispatchExpressionwithProcess_4 = Statements.dispatchExpressionwithProcess(_expression, (count + 1));
                                                                _builder.append(_dispatchExpressionwithProcess_4, "\t");
                                                                _builder.newLineIfNotEmpty();
                                                                _builder.append("\t");
                                                                _builder.append("result_");
                                                                _builder.append(count, "\t");
                                                                _builder.append(" = ! result_");
                                                                _builder.append((count + 1), "\t");
                                                                _builder.append(" ;");
                                                                _builder.newLineIfNotEmpty();
                                                                _builder.append("}\t\t\t\t\t\t\t\t\t\t\t");
                                                                _builder.newLine();
                                                                _builder.append("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
                                                              } else {
                                                                _builder.append("//others");
                                                                _builder.newLineIfNotEmpty();
                                                                _builder.append("\t");
                                                                _builder.append("{");
                                                                _builder.newLine();
                                                                _builder.append("\t\t");
                                                                ArrayList<String> processList = Statements.getProcessListFromExpression(exp);
                                                                _builder.append("\t\t\t\t\t\t\t\t\t\t\t\t");
                                                                _builder.newLineIfNotEmpty();
                                                                {
                                                                  int _size = processList.size();
                                                                  boolean _equals = (_size == 0);
                                                                  if (_equals) {
                                                                    _builder.append("\t\t");
                                                                    _builder.append("result_");
                                                                    _builder.append(count, "\t\t");
                                                                    _builder.append(" =  ");
                                                                    String _dispatchExpression_2 = Statements.dispatchExpression(exp, false);
                                                                    _builder.append(_dispatchExpression_2, "\t\t");
                                                                    _builder.append(" ;");
                                                                    _builder.newLineIfNotEmpty();
                                                                  } else {
                                                                    {
                                                                      for(final String pname : processList) {
                                                                        _builder.append("\t\t");
                                                                        _builder.append("//ArrayList<SchedulerProcess> a_");
                                                                        _builder.append(pname, "\t\t");
                                                                        _builder.append(" = findProcessByAlias(\"");
                                                                        _builder.append(pname, "\t\t");
                                                                        _builder.append("\") ;");
                                                                        _builder.newLineIfNotEmpty();
                                                                      }
                                                                    }
                                                                    {
                                                                      for(final String pname_1 : processList) {
                                                                        _builder.append("\t\t");
                                                                        _builder.append("for (SchedulerProcess ");
                                                                        _builder.append(pname_1, "\t\t");
                                                                        _builder.append(" : a_");
                                                                        _builder.append(pname_1, "\t\t");
                                                                        _builder.append(")");
                                                                        _builder.newLineIfNotEmpty();
                                                                      }
                                                                    }
                                                                    _builder.append("\t\t");
                                                                    _builder.append("result_");
                                                                    _builder.append(count, "\t\t");
                                                                    _builder.append(" = result_");
                                                                    _builder.append(count, "\t\t");
                                                                    _builder.append(" && ");
                                                                    String _dispatchExpression_3 = Statements.dispatchExpression(exp, false);
                                                                    _builder.append(_dispatchExpression_3, "\t\t");
                                                                    _builder.append(" ; ");
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
  
  public static String dispatchExpression(final Expression exp, final boolean isNum) {
    if ((exp instanceof Or)) {
      Expression _left = ((Or)exp).getLeft();
      String _dispatchExpression = Statements.dispatchExpression(_left, false);
      String _plus = ("(" + _dispatchExpression);
      String _xifexpression = null;
      Expression _right = ((Or)exp).getRight();
      boolean _notEquals = (!Objects.equal(_right, null));
      if (_notEquals) {
        Expression _right_1 = ((Or)exp).getRight();
        String _dispatchExpression_1 = Statements.dispatchExpression(_right_1, false);
        _xifexpression = (" || " + _dispatchExpression_1);
      }
      String _plus_1 = (_plus + _xifexpression);
      return (_plus_1 + ")");
    } else {
      if ((exp instanceof And)) {
        Expression _left_1 = ((And)exp).getLeft();
        String _dispatchExpression_2 = Statements.dispatchExpression(_left_1, false);
        String _plus_2 = ("(" + _dispatchExpression_2);
        String _plus_3 = (_plus_2 + " && ");
        Expression _right_2 = ((And)exp).getRight();
        String _dispatchExpression_3 = Statements.dispatchExpression(_right_2, false);
        String _plus_4 = (_plus_3 + _dispatchExpression_3);
        return (_plus_4 + ")");
      } else {
        if ((exp instanceof Equality)) {
          Expression _left_2 = ((Equality)exp).getLeft();
          String _dispatchExpression_4 = Statements.dispatchExpression(_left_2, true);
          String _plus_5 = ("(" + _dispatchExpression_4);
          String _op = ((Equality)exp).getOp();
          String _plus_6 = (_plus_5 + _op);
          Expression _right_3 = ((Equality)exp).getRight();
          String _dispatchExpression_5 = Statements.dispatchExpression(_right_3, true);
          String _plus_7 = (_plus_6 + _dispatchExpression_5);
          return (_plus_7 + ")");
        } else {
          if ((exp instanceof Comparison)) {
            Expression _left_3 = ((Comparison)exp).getLeft();
            String _dispatchExpression_6 = Statements.dispatchExpression(_left_3, true);
            String _plus_8 = ("(" + _dispatchExpression_6);
            String _op_1 = ((Comparison)exp).getOp();
            String _plus_9 = (_plus_8 + _op_1);
            Expression _right_4 = ((Comparison)exp).getRight();
            String _dispatchExpression_7 = Statements.dispatchExpression(_right_4, true);
            String _plus_10 = (_plus_9 + _dispatchExpression_7);
            return (_plus_10 + ")");
          } else {
            if ((exp instanceof Plus)) {
              Expression _left_4 = ((Plus)exp).getLeft();
              String _dispatchExpression_8 = Statements.dispatchExpression(_left_4, true);
              String _plus_11 = ("(" + _dispatchExpression_8);
              String _plus_12 = (_plus_11 + "+");
              Expression _right_5 = ((Plus)exp).getRight();
              String _dispatchExpression_9 = Statements.dispatchExpression(_right_5, true);
              String _plus_13 = (_plus_12 + _dispatchExpression_9);
              return (_plus_13 + ")");
            } else {
              if ((exp instanceof Minus)) {
                Expression _left_5 = ((Minus)exp).getLeft();
                String _dispatchExpression_10 = Statements.dispatchExpression(_left_5, true);
                String _plus_14 = ("(" + _dispatchExpression_10);
                String _plus_15 = (_plus_14 + "-");
                Expression _right_6 = ((Minus)exp).getRight();
                String _dispatchExpression_11 = Statements.dispatchExpression(_right_6, true);
                String _plus_16 = (_plus_15 + _dispatchExpression_11);
                return (_plus_16 + ")");
              } else {
                if ((exp instanceof MulOrDiv)) {
                  Expression _left_6 = ((MulOrDiv)exp).getLeft();
                  String _dispatchExpression_12 = Statements.dispatchExpression(_left_6, true);
                  String _plus_17 = ("(" + _dispatchExpression_12);
                  String _op_2 = ((MulOrDiv)exp).getOp();
                  String _plus_18 = (_plus_17 + _op_2);
                  Expression _right_7 = ((MulOrDiv)exp).getRight();
                  String _dispatchExpression_13 = Statements.dispatchExpression(_right_7, true);
                  String _plus_19 = (_plus_18 + _dispatchExpression_13);
                  return (_plus_19 + ")");
                } else {
                  if ((exp instanceof Implies)) {
                    Expression _left_7 = ((Implies)exp).getLeft();
                    String _dispatchExpression_14 = Statements.dispatchExpression(_left_7, true);
                    String _plus_20 = ("(! " + _dispatchExpression_14);
                    String _plus_21 = (_plus_20 + "||");
                    Expression _right_8 = ((Implies)exp).getRight();
                    String _dispatchExpression_15 = Statements.dispatchExpression(_right_8, true);
                    String _plus_22 = (_plus_21 + _dispatchExpression_15);
                    return (_plus_22 + ")");
                  } else {
                    if ((exp instanceof Not)) {
                      Expression _expression = ((Not)exp).getExpression();
                      String _dispatchExpression_16 = Statements.dispatchExpression(_expression, false);
                      String _plus_23 = ("(!" + _dispatchExpression_16);
                      return (_plus_23 + ")");
                    } else {
                      if ((exp instanceof HasNameExpression)) {
                        scheduling.dsl.Process _proc = ((HasNameExpression)exp).getProc();
                        String _name = _proc.getName();
                        String _plus_24 = ("getStaticPropertyObject(" + _name);
                        String _plus_25 = (_plus_24 + ".refID).pName.trim().equals(");
                        String _pN = ((HasNameExpression)exp).getPN();
                        String _plus_26 = (_plus_25 + _pN);
                        return (_plus_26 + ")");
                      } else {
                        if ((exp instanceof HasID)) {
                          scheduling.dsl.Process _proc_1 = ((HasID)exp).getProc();
                          String _name_1 = _proc_1.getName();
                          String _plus_27 = ("(" + _name_1);
                          String _plus_28 = (_plus_27 + " == null ? false : ");
                          scheduling.dsl.Process _proc_2 = ((HasID)exp).getProc();
                          String _name_2 = _proc_2.getName();
                          String _plus_29 = (_plus_28 + _name_2);
                          String _plus_30 = (_plus_29 + ".processID == ");
                          String _id = ((HasID)exp).getId();
                          String _plus_31 = (_plus_30 + _id);
                          return (_plus_31 + ")");
                        } else {
                          if ((exp instanceof HasIDINT)) {
                            scheduling.dsl.Process _proc_3 = ((HasIDINT)exp).getProc();
                            String _name_3 = _proc_3.getName();
                            String _plus_32 = ("(" + _name_3);
                            String _plus_33 = (_plus_32 + " == null ? false : ");
                            scheduling.dsl.Process _proc_4 = ((HasIDINT)exp).getProc();
                            String _name_4 = _proc_4.getName();
                            String _plus_34 = (_plus_33 + _name_4);
                            String _plus_35 = (_plus_34 + ".processID == ");
                            int _id_1 = ((HasIDINT)exp).getId();
                            String _plus_36 = (_plus_35 + Integer.valueOf(_id_1));
                            return (_plus_36 + ")");
                          } else {
                            if ((exp instanceof GetIDExpression)) {
                              String _pN_1 = ((GetIDExpression)exp).getPN();
                              String _replace = _pN_1.replace("\"", "");
                              String _replaceAll = _replace.replaceAll(" ", "");
                              String _trim = _replaceAll.trim();
                              return (_trim + ".processID");
                            } else {
                              if ((exp instanceof InExpression)) {
                                SchedulerSet _col = ((InExpression)exp).getCol();
                                String _name_5 = _col.getName();
                                String _plus_37 = ("(" + _name_5);
                                String _plus_38 = (_plus_37 + ".hasProcess(");
                                String _pN_2 = ((InExpression)exp).getPN();
                                String _plus_39 = (_plus_38 + _pN_2);
                                return (_plus_39 + ") > 0)");
                              } else {
                                if ((exp instanceof InPIDExpression)) {
                                  SchedulerSet _col_1 = ((InPIDExpression)exp).getCol();
                                  String _name_6 = _col_1.getName();
                                  String _plus_40 = ("(" + _name_6);
                                  String _plus_41 = (_plus_40 + ".hasProcess(");
                                  String _id_2 = ((InPIDExpression)exp).getId();
                                  String _plus_42 = (_plus_41 + _id_2);
                                  return (_plus_42 + ") > 0)");
                                } else {
                                  if ((exp instanceof InPExpression)) {
                                    SchedulerSet _col_2 = ((InPExpression)exp).getCol();
                                    String _name_7 = _col_2.getName();
                                    String _plus_43 = ("(" + _name_7);
                                    String _plus_44 = (_plus_43 + ".hasProcess(");
                                    int _id_3 = ((InPExpression)exp).getId();
                                    String _plus_45 = (_plus_44 + Integer.valueOf(_id_3));
                                    return (_plus_45 + ") > 0)");
                                  } else {
                                    if ((exp instanceof ExistExpression)) {
                                      String _pN_3 = ((ExistExpression)exp).getPN();
                                      String _plus_46 = ("(existsProcess(" + _pN_3);
                                      return (_plus_46 + ") > 0)");
                                    } else {
                                      if ((exp instanceof ExistPID)) {
                                        String _id_4 = ((ExistPID)exp).getId();
                                        String _plus_47 = ("(existsProcess(" + _id_4);
                                        return (_plus_47 + ") > 0)");
                                      } else {
                                        if ((exp instanceof ExistP)) {
                                          int _id_5 = ((ExistP)exp).getId();
                                          String _plus_48 = ("(existsProcess(" + Integer.valueOf(_id_5));
                                          return (_plus_48 + ") > 0)");
                                        } else {
                                          if ((exp instanceof EmptyExpression)) {
                                            SchedulerSet _col_3 = ((EmptyExpression)exp).getCol();
                                            String _name_8 = _col_3.getName();
                                            String _plus_49 = ("(" + _name_8);
                                            return (_plus_49 + ".isEmpty() > 0)");
                                          } else {
                                            if ((exp instanceof NullExpression)) {
                                              scheduling.dsl.Process _procName = ((NullExpression)exp).getProcName();
                                              String _name_9 = _procName.getName();
                                              String _plus_50 = ("(" + _name_9);
                                              return (_plus_50 + " == null)");
                                            } else {
                                              if ((exp instanceof IntConstant)) {
                                                NumValue _value = ((IntConstant)exp).getValue();
                                                String _minus = _value.getMinus();
                                                boolean _equals = Objects.equal(_minus, null);
                                                if (_equals) {
                                                  NumValue _value_1 = ((IntConstant)exp).getValue();
                                                  int _value_2 = _value_1.getValue();
                                                  return Integer.valueOf(_value_2).toString();
                                                } else {
                                                  NumValue _value_3 = ((IntConstant)exp).getValue();
                                                  int _value_4 = _value_3.getValue();
                                                  String _string = Integer.valueOf(_value_4).toString();
                                                  return ("-" + _string);
                                                }
                                              } else {
                                                if ((exp instanceof BoolConstant)) {
                                                  String _value_5 = ((BoolConstant)exp).getValue();
                                                  return _value_5.toString();
                                                } else {
                                                  if ((exp instanceof GetInstance)) {
                                                    scheduling.dsl.Process _procName_1 = ((GetInstance)exp).getProcName();
                                                    String _name_10 = _procName_1.getName();
                                                    String _plus_51 = ("getInstance(" + _name_10);
                                                    return (_plus_51 + ")");
                                                  } else {
                                                    if ((exp instanceof GetPID)) {
                                                      scheduling.dsl.Process _procName_2 = ((GetPID)exp).getProcName();
                                                      String _name_11 = _procName_2.getName();
                                                      return (_name_11 + ".processID");
                                                    } else {
                                                      if ((exp instanceof Action)) {
                                                        return "getAction()";
                                                      } else {
                                                        if ((exp instanceof Step)) {
                                                          return "\"getStep()\"";
                                                        } else {
                                                          if ((exp instanceof TotalStep)) {
                                                            return "\"getTotalStep()\"";
                                                          } else {
                                                            if ((exp instanceof StateID)) {
                                                              return "\"<StateID>\"";
                                                            } else {
                                                              if ((exp instanceof BranchID)) {
                                                                return "\"<BranchID>\"";
                                                              } else {
                                                                if ((exp instanceof Atomic)) {
                                                                  SysVar _sysvar = ((Atomic)exp).getSysvar();
                                                                  boolean _notEquals_1 = (!Objects.equal(_sysvar, null));
                                                                  if (_notEquals_1) {
                                                                    if (isNum) {
                                                                      SysVar _sysvar_1 = ((Atomic)exp).getSysvar();
                                                                      String _vname = _sysvar_1.getVname();
                                                                      String _plus_52 = ("Integer.parseInt(" + _vname);
                                                                      return (_plus_52 + ")");
                                                                    } else {
                                                                      SysVar _sysvar_2 = ((Atomic)exp).getSysvar();
                                                                      String _vname_1 = _sysvar_2.getVname();
                                                                      String _plus_53 = ("Boolean.getBoolean(" + _vname_1);
                                                                      return (_plus_53 + ")");
                                                                    }
                                                                  }
                                                                  QualifiedNames _var = ((Atomic)exp).getVar();
                                                                  boolean _notEquals_2 = (!Objects.equal(_var, null));
                                                                  if (_notEquals_2) {
                                                                    QualifiedNames _var_1 = ((Atomic)exp).getVar();
                                                                    String _string_1 = _var_1.toString();
                                                                    String v = SchedulerTestGenerator.getValue(_string_1);
                                                                    boolean _isEmpty = v.isEmpty();
                                                                    if (_isEmpty) {
                                                                      QualifiedNames _var_2 = ((Atomic)exp).getVar();
                                                                      return Statements.dispatchQualifiedNames(_var_2);
                                                                    }
                                                                    return v;
                                                                  } else {
                                                                    String _charseq = ((Atomic)exp).getCharseq();
                                                                    boolean _notEquals_3 = (!Objects.equal(_charseq, null));
                                                                    if (_notEquals_3) {
                                                                      String _charseq_1 = ((Atomic)exp).getCharseq();
                                                                      return _charseq_1.toString();
                                                                    } else {
                                                                      String _string_2 = ((Atomic)exp).getString();
                                                                      boolean _notEquals_4 = (!Objects.equal(_string_2, null));
                                                                      if (_notEquals_4) {
                                                                        String _string_3 = ((Atomic)exp).getString();
                                                                        String _replace_1 = _string_3.replace("\"", "\\\"");
                                                                        String _plus_54 = ("\"" + _replace_1);
                                                                        return (_plus_54 + "\"");
                                                                      }
                                                                    }
                                                                  }
                                                                  ICompositeNode _node = NodeModelUtils.getNode(exp);
                                                                  return NodeModelUtils.getTokenText(_node);
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
        }
      }
    }
    return null;
  }
  
  public static ArrayList<String> getVariables(final ArrayList<String> varList, final RTCTL rtctl) {
    Expression _exp = rtctl.getExp();
    boolean _notEquals = (!Objects.equal(_exp, null));
    if (_notEquals) {
      Expression _exp_1 = rtctl.getExp();
      Statements.getVariables(varList, _exp_1);
    } else {
      RTCTL _f = rtctl.getF();
      boolean _notEquals_1 = (!Objects.equal(_f, null));
      if (_notEquals_1) {
        RTCTL _f_1 = rtctl.getF();
        Statements.getVariables(varList, _f_1);
      } else {
        RTCTL _f1 = rtctl.getF1();
        Statements.getVariables(varList, _f1);
        RTCTL _f2 = rtctl.getF2();
        Statements.getVariables(varList, _f2);
      }
    }
    return varList;
  }
  
  public static ArrayList<String> getVariables(final ArrayList<String> varList, final Expression exp) {
    if ((exp instanceof Or)) {
      Expression _left = ((Or)exp).getLeft();
      Statements.getVariables(varList, _left);
      Expression _right = ((Or)exp).getRight();
      Statements.getVariables(varList, _right);
    } else {
      if ((exp instanceof And)) {
        Expression _left_1 = ((And)exp).getLeft();
        Statements.getVariables(varList, _left_1);
        Expression _right_1 = ((And)exp).getRight();
        Statements.getVariables(varList, _right_1);
      } else {
        if ((exp instanceof Equality)) {
          Expression _left_2 = ((Equality)exp).getLeft();
          Statements.getVariables(varList, _left_2);
          Expression _right_2 = ((Equality)exp).getRight();
          Statements.getVariables(varList, _right_2);
        } else {
          if ((exp instanceof Comparison)) {
            Expression _left_3 = ((Comparison)exp).getLeft();
            Statements.getVariables(varList, _left_3);
            Expression _right_3 = ((Comparison)exp).getRight();
            Statements.getVariables(varList, _right_3);
          } else {
            if ((exp instanceof Plus)) {
              Expression _left_4 = ((Plus)exp).getLeft();
              Statements.getVariables(varList, _left_4);
              Expression _right_4 = ((Plus)exp).getRight();
              Statements.getVariables(varList, _right_4);
            } else {
              if ((exp instanceof Minus)) {
                Expression _left_5 = ((Minus)exp).getLeft();
                Statements.getVariables(varList, _left_5);
                Expression _right_5 = ((Minus)exp).getRight();
                Statements.getVariables(varList, _right_5);
              } else {
                if ((exp instanceof MulOrDiv)) {
                  Expression _left_6 = ((MulOrDiv)exp).getLeft();
                  Statements.getVariables(varList, _left_6);
                  Expression _right_6 = ((MulOrDiv)exp).getRight();
                  Statements.getVariables(varList, _right_6);
                } else {
                  if ((exp instanceof Implies)) {
                    Expression _left_7 = ((Implies)exp).getLeft();
                    Statements.getVariables(varList, _left_7);
                    Expression _right_7 = ((Implies)exp).getRight();
                    Statements.getVariables(varList, _right_7);
                  } else {
                    if ((exp instanceof Not)) {
                      Expression _expression = ((Not)exp).getExpression();
                      Statements.getVariables(varList, _expression);
                    } else {
                      if ((exp instanceof Atomic)) {
                        SysVar _sysvar = ((Atomic)exp).getSysvar();
                        boolean _notEquals = (!Objects.equal(_sysvar, null));
                        if (_notEquals) {
                          SysVar _sysvar_1 = ((Atomic)exp).getSysvar();
                          String _vname = _sysvar_1.getVname();
                          boolean _contains = varList.contains(_vname);
                          boolean _not = (!_contains);
                          if (_not) {
                            SysVar _sysvar_2 = ((Atomic)exp).getSysvar();
                            String _vname_1 = _sysvar_2.getVname();
                            varList.add(_vname_1);
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
    return varList;
  }
  
  public static String dispatchQualifiedNames(final QualifiedNames qname) {
    ProcessPropertyName _prop = qname.getProp();
    boolean _equals = Objects.equal(_prop, null);
    if (_equals) {
      String _name = qname.getName();
      return _name.toString();
    } else {
      String _name_1 = qname.getName();
      String _plus = (_name_1 + ".get_");
      ProcessPropertyName _prop_1 = qname.getProp();
      String _name_2 = _prop_1.getName();
      String _plus_1 = (_plus + _name_2);
      return (_plus_1 + "()");
    }
  }
  
  public static String dispatchQualifiedNamesInAssignStatement(final QualifiedNames qname) {
    ProcessPropertyName _prop = qname.getProp();
    boolean _equals = Objects.equal(_prop, null);
    if (_equals) {
      String _name = qname.getName();
      return _name.toString();
    } else {
      ProcessPropertyName _prop_1 = qname.getProp();
      String _name_1 = _prop_1.getName();
      boolean _contains = Data.varPropertyList.contains(_name_1);
      if (_contains) {
        String _name_2 = qname.getName();
        String _plus = (_name_2 + ".");
        ProcessPropertyName _prop_2 = qname.getProp();
        String _name_3 = _prop_2.getName();
        return (_plus + _name_3);
      } else {
        ProcessPropertyName _prop_3 = qname.getProp();
        String _name_4 = _prop_3.getName();
        String _plus_1 = ("Util.println(\"Can not change the process property " + _name_4);
        return (_plus_1 + ") ;");
      }
    }
  }
  
  protected static CharSequence _dispatchStatement(final ReturnStatement stm, final String processName) {
    String result = "";
    OrderType _value = stm.getValue();
    String _string = _value.toString();
    String _trim = _string.trim();
    switch (_trim) {
      case "greater":
        result = "1 ; //greater";
        break;
      case "less":
        result = "-1 ; //less";
        break;
      case "equal":
        result = "0 ; //equal";
        break;
    }
    return ("return " + result);
  }
  
  protected static CharSequence _dispatchStatement(final MoveProcess stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//MoveProcess ");
    _builder.append(processName, "");
    _builder.append("\t\t\t");
    _builder.newLineIfNotEmpty();
    {
      if (Data.initProcessList) {
        _builder.append("\t");
        _builder.append("//when initializing: put new process to temporary list (AL_?) for stack or queue, then put temporary list into stack or queue");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("AL_");
        SchedulerSet _colTo = stm.getColTo();
        String _name = _colTo.getName();
        _builder.append(_name, "\t");
        _builder.append(".add(");
        scheduling.dsl.Process _process = stm.getProcess();
        String _name_1 = _process.getName();
        _builder.append(_name_1, "\t");
        _builder.append(") ;");
        _builder.newLineIfNotEmpty();
      } else {
        {
          SchedulerDef _scheduler = Data.schModel.getScheduler();
          boolean _notEquals = (!Objects.equal(_scheduler, null));
          if (_notEquals) {
            {
              SchedulerDef _scheduler_1 = Data.schModel.getScheduler();
              SchedulerDataDef _schedulerdata = _scheduler_1.getSchedulerdata();
              boolean _notEquals_1 = (!Objects.equal(_schedulerdata, null));
              if (_notEquals_1) {
                _builder.append("\t");
                _builder.append("if (");
                scheduling.dsl.Process _process_1 = stm.getProcess();
                String _name_2 = _process_1.getName();
                _builder.append(_name_2, "\t");
                _builder.append(" != null) {");
                _builder.newLineIfNotEmpty();
                {
                  scheduling.dsl.Process _process_2 = stm.getProcess();
                  String _name_3 = _process_2.getName();
                  boolean _equals = processName.equals(_name_3);
                  if (_equals) {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("remove_process(");
                    scheduling.dsl.Process _process_3 = stm.getProcess();
                    String _name_4 = _process_3.getName();
                    _builder.append(_name_4, "\t\t");
                    _builder.append(".processID) ;");
                    _builder.newLineIfNotEmpty();
                  } else {
                    {
                      scheduling.dsl.Process _process_4 = stm.getProcess();
                      String _name_5 = _process_4.getName();
                      boolean _equals_1 = _name_5.equals("running_process");
                      boolean _not = (!_equals_1);
                      if (_not) {
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("//in a collection, use iterator");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("iterator_");
                        scheduling.dsl.Process _process_5 = stm.getProcess();
                        String _name_6 = _process_5.getName();
                        _builder.append(_name_6, "\t\t");
                        _builder.append(".remove() ;");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                  }
                }
                _builder.append("\t");
                _builder.append("\t");
                SchedulerSet _colTo_1 = stm.getColTo();
                String _name_7 = _colTo_1.getName();
                _builder.append(_name_7, "\t\t");
                _builder.append(".put(");
                scheduling.dsl.Process _process_6 = stm.getProcess();
                String _name_8 = _process_6.getName();
                _builder.append(_name_8, "\t\t");
                _builder.append(") ;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t\t");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("if (running_process != null) {");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t");
                _builder.append("if (running_process.processID == ");
                scheduling.dsl.Process _process_7 = stm.getProcess();
                String _name_9 = _process_7.getName();
                _builder.append(_name_9, "\t\t\t");
                _builder.append(".processID) {");
                _builder.newLineIfNotEmpty();
                {
                  DefCore _defcore = Data.schModel.getDefcore();
                  boolean _notEquals_2 = (!Objects.equal(_defcore, null));
                  if (_notEquals_2) {
                    _builder.append("\t");
                    _builder.append("\t\t\t");
                    _builder.append("running_procs[current_core] = null ;\t\t\t\t\t\t");
                    _builder.newLine();
                  }
                }
                _builder.append("\t");
                _builder.append("\t\t\t");
                _builder.append("running_process = null;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t");
                _builder.append("}");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("}\t");
                _builder.newLine();
                {
                  scheduling.dsl.Process _process_8 = stm.getProcess();
                  String _name_10 = _process_8.getName();
                  boolean _contains = _name_10.contains("running_process");
                  if (_contains) {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("//");
                    scheduling.dsl.Process _process_9 = stm.getProcess();
                    String _name_11 = _process_9.getName();
                    _builder.append(_name_11, "\t\t");
                    _builder.append(" = null ;");
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
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final ReorderProcess stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//ReorderProcess ");
    _builder.append(processName, "");
    _builder.append("\t\t\t");
    _builder.newLineIfNotEmpty();
    {
      if (Data.initProcessList) {
        _builder.append("\t");
        _builder.append("//reset does not affect to the initialization");
        _builder.newLine();
      } else {
        {
          SchedulerDef _scheduler = Data.schModel.getScheduler();
          boolean _notEquals = (!Objects.equal(_scheduler, null));
          if (_notEquals) {
            {
              SchedulerDef _scheduler_1 = Data.schModel.getScheduler();
              SchedulerDataDef _schedulerdata = _scheduler_1.getSchedulerdata();
              boolean _notEquals_1 = (!Objects.equal(_schedulerdata, null));
              if (_notEquals_1) {
                _builder.append("\t");
                _builder.append("if (");
                scheduling.dsl.Process _process = stm.getProcess();
                String _name = _process.getName();
                _builder.append(_name, "\t");
                _builder.append(" != null) {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("if (");
                SchedulerSet _colTo = stm.getColTo();
                String _name_1 = _colTo.getName();
                _builder.append(_name_1, "\t\t");
                _builder.append(".hasProcess(");
                scheduling.dsl.Process _process_1 = stm.getProcess();
                String _name_2 = _process_1.getName();
                _builder.append(_name_2, "\t\t");
                _builder.append(") > 0) {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t\t");
                SchedulerSet _colTo_1 = stm.getColTo();
                String _name_3 = _colTo_1.getName();
                _builder.append(_name_3, "\t\t\t");
                _builder.append(".removeProcess(");
                scheduling.dsl.Process _process_2 = stm.getProcess();
                String _name_4 = _process_2.getName();
                _builder.append(_name_4, "\t\t\t");
                _builder.append(".processID) ;\t\t\t\t\t\t\t\t\t\t\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t\t");
                SchedulerSet _colTo_2 = stm.getColTo();
                String _name_5 = _colTo_2.getName();
                _builder.append(_name_5, "\t\t\t");
                _builder.append(".put(");
                scheduling.dsl.Process _process_3 = stm.getProcess();
                String _name_6 = _process_3.getName();
                _builder.append(_name_6, "\t\t\t");
                _builder.append(") ;");
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
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final SetProcessInstance stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//SetProcessInstance ");
    _builder.append(processName, "");
    _builder.newLineIfNotEmpty();
    {
      scheduling.dsl.Process _process = stm.getProcess();
      String _name = _process.getName();
      boolean _contains = _name.contains("running_process");
      boolean _not = (!_contains);
      if (_not) {
        {
          if (Data.initProcessList) {
            _builder.append("\t");
            _builder.append("//when initializing: put new process to temporary list (AL_?) for stack or queue, then put temporary list into stack or queue");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("AL_");
            SchedulerSet _colTo = stm.getColTo();
            String _name_1 = _colTo.getName();
            _builder.append(_name_1, "\t");
            _builder.append(".add(");
            scheduling.dsl.Process _process_1 = stm.getProcess();
            String _name_2 = _process_1.getName();
            _builder.append(_name_2, "\t");
            _builder.append(") ;");
            _builder.newLineIfNotEmpty();
          } else {
            {
              SchedulerDef _scheduler = Data.schModel.getScheduler();
              boolean _notEquals = (!Objects.equal(_scheduler, null));
              if (_notEquals) {
                {
                  SchedulerDef _scheduler_1 = Data.schModel.getScheduler();
                  SchedulerDataDef _schedulerdata = _scheduler_1.getSchedulerdata();
                  boolean _notEquals_1 = (!Objects.equal(_schedulerdata, null));
                  if (_notEquals_1) {
                    _builder.append("\t");
                    _builder.append("if (running_process != null) {\t\t\t\t\t");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("if (");
                    scheduling.dsl.Process _process_2 = stm.getProcess();
                    String _name_3 = _process_2.getName();
                    _builder.append(_name_3, "\t\t");
                    _builder.append(".processID != running_process.processID) {\t\t\t\t\t\t\t\t\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t\t");
                    SchedulerSet _colTo_1 = stm.getColTo();
                    String _name_4 = _colTo_1.getName();
                    _builder.append(_name_4, "\t\t\t");
                    _builder.append(".put(");
                    scheduling.dsl.Process _process_3 = stm.getProcess();
                    String _name_5 = _process_3.getName();
                    _builder.append(_name_5, "\t\t\t");
                    _builder.append(") ;");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("}\t\t\t\t");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("} else {");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("\t");
                    SchedulerSet _colTo_2 = stm.getColTo();
                    String _name_6 = _colTo_2.getName();
                    _builder.append(_name_6, "\t\t");
                    _builder.append(".put(");
                    scheduling.dsl.Process _process_4 = stm.getProcess();
                    String _name_7 = _process_4.getName();
                    _builder.append(_name_7, "\t\t");
                    _builder.append(") ;");
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
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final RemoveProcess stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//RemoveProcess");
    _builder.newLine();
    {
      scheduling.dsl.Process _process = stm.getProcess();
      String _name = _process.getName();
      boolean _equals = Objects.equal(_name, "running_process");
      if (_equals) {
        _builder.append("\t");
        _builder.append("if (running_process != null) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("remove_process(running_process.processID) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("running_process = null ;");
        _builder.newLine();
        {
          DefCore _defcore = Data.schModel.getDefcore();
          boolean _notEquals = (!Objects.equal(_defcore, null));
          if (_notEquals) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_procs[current_core] = null ;");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("}\t\t\t\t");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("if (");
        scheduling.dsl.Process _process_1 = stm.getProcess();
        String _name_1 = _process_1.getName();
        _builder.append(_name_1, "\t");
        _builder.append(".processID == running_process.processID) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("running_process = null ;");
        _builder.newLine();
        {
          DefCore _defcore_1 = Data.schModel.getDefcore();
          boolean _notEquals_1 = (!Objects.equal(_defcore_1, null));
          if (_notEquals_1) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_procs[current_core] = null ;");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("try {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("select_process(-1) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("} catch (ValidationException e) {\t\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("e.printStackTrace();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("} else {\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("remove_process(");
        scheduling.dsl.Process _process_2 = stm.getProcess();
        String _name_2 = _process_2.getName();
        _builder.append(_name_2, "\t\t");
        _builder.append(".processID) ;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("SchedulerPanModel.panmodel.reduceProcess() ;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final ChangeAction stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//Change action");
    _builder.newLine();
    {
      EList<Statement> _sta = stm.getSta();
      for(final Statement st : _sta) {
        _builder.append("\t");
        Object _dispatchStatement = Statements.dispatchStatement(st, processName);
        _builder.append(_dispatchStatement, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final ChangeValueUnOp stm, final String processName) {
    QualifiedNames _var = stm.getVar();
    String _dispatchQualifiedNames = Statements.dispatchQualifiedNames(_var);
    String _op = stm.getOp();
    String _plus = (_dispatchQualifiedNames + _op);
    return (_plus + ";");
  }
  
  protected static CharSequence _dispatchStatement(final ChangeListValue stm, final String processName) {
    Expression _exp = stm.getExp();
    String exp = Statements.dispatchExpression(_exp);
    ListElement _lvar = stm.getLvar();
    String _reference = SchedulerTestGenerator.getReference(_lvar);
    String _plus = (_reference + "=");
    String _plus_1 = (_plus + exp);
    return (_plus_1 + ";");
  }
  
  protected static CharSequence _dispatchStatement(final ChangeValueExpression stm, final String processName) {
    String _xifexpression = null;
    QualifiedNames _var = stm.getVar();
    ProcessPropertyName _prop = _var.getProp();
    boolean _equals = Objects.equal(_prop, null);
    if (_equals) {
      QualifiedNames _var_1 = stm.getVar();
      String _dispatchQualifiedNamesInAssignStatement = Statements.dispatchQualifiedNamesInAssignStatement(_var_1);
      String _plus = (_dispatchQualifiedNamesInAssignStatement + "=");
      Expression _exp = stm.getExp();
      String _dispatchExpression = Statements.dispatchExpression(_exp);
      String _plus_1 = (_plus + _dispatchExpression);
      _xifexpression = (_plus_1 + ";");
    } else {
      String _xifexpression_1 = null;
      QualifiedNames _var_2 = stm.getVar();
      ProcessPropertyName _prop_1 = _var_2.getProp();
      String _name = _prop_1.getName();
      boolean _contains = Data.varPropertyList.contains(_name);
      if (_contains) {
        QualifiedNames _var_3 = stm.getVar();
        String _dispatchQualifiedNamesInAssignStatement_1 = Statements.dispatchQualifiedNamesInAssignStatement(_var_3);
        String _plus_2 = (_dispatchQualifiedNamesInAssignStatement_1 + "=(");
        QualifiedNames _var_4 = stm.getVar();
        ProcessPropertyName _prop_2 = _var_4.getProp();
        String _name_1 = _prop_2.getName();
        String _type = Data.getType(_name_1);
        String _plus_3 = (_plus_2 + _type);
        String _plus_4 = (_plus_3 + ")");
        Expression _exp_1 = stm.getExp();
        String _dispatchExpression_1 = Statements.dispatchExpression(_exp_1);
        String _plus_5 = (_plus_4 + _dispatchExpression_1);
        _xifexpression_1 = (_plus_5 + ";");
      } else {
        QualifiedNames _var_5 = stm.getVar();
        ProcessPropertyName _prop_3 = _var_5.getProp();
        String _name_2 = _prop_3.getName();
        String _plus_6 = ("Util.println(\"Can not change the process property " + _name_2);
        String _plus_7 = (_plus_6 + " of ");
        QualifiedNames _var_6 = stm.getVar();
        String _name_3 = _var_6.getName();
        String _plus_8 = (_plus_7 + _name_3);
        return (_plus_8 + "\") ;");
      }
      _xifexpression = _xifexpression_1;
    }
    return _xifexpression;
  }
  
  protected static CharSequence _dispatchStatement(final IfStatement stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    Expression _condition = stm.getCondition();
    ArrayList<String> processList = Statements.getProcessListFromExpression(_condition);
    _builder.newLineIfNotEmpty();
    _builder.append("if (");
    Expression _condition_1 = stm.getCondition();
    String _dispatchExpression = Statements.dispatchExpression(_condition_1);
    _builder.append(_dispatchExpression, "");
    _builder.append(")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    Statement _thenBlock = stm.getThenBlock();
    Object _dispatchStatement = Statements.dispatchStatement(_thenBlock, processName);
    _builder.append(_dispatchStatement, "\t");
    _builder.newLineIfNotEmpty();
    {
      Statement _elseBlock = stm.getElseBlock();
      boolean _notEquals = (!Objects.equal(_elseBlock, null));
      if (_notEquals) {
        _builder.append("else ");
        _builder.newLine();
        _builder.append("\t");
        Statement _elseBlock_1 = stm.getElseBlock();
        Object _dispatchStatement_1 = Statements.dispatchStatement(_elseBlock_1, processName);
        _builder.append(_dispatchStatement_1, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public static CharSequence dispatchStatementForSimulation(final GetProcess stm) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//GetProcess statement\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//1. Select process set");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("ArrayList<SchedulerProcess> runSet = ");
    SchedulerSet _colFrom = stm.getColFrom();
    String _name = _colFrom.getName();
    _builder.append(_name, "\t");
    _builder.append(".getProcessSet();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("if (runSet != null) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("_runningSet.dataSet = runSet ; //only get no remove");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}//GetProcess statement\t\t\t\t\t\t\t");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final GetProcess stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//GetProcess statement\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("SchedulerProcess previous_running = running_process;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//1. Select process set");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (lastProcessID < 0) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("ArrayList<SchedulerProcess> runSet = ");
    SchedulerSet _colFrom = stm.getColFrom();
    String _name = _colFrom.getName();
    _builder.append(_name, "\t\t");
    _builder.append(".getProcessSet();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("if (runSet != null) {");
    _builder.newLine();
    {
      DefCore _defcore = Data.schModel.getDefcore();
      boolean _notEquals = (!Objects.equal(_defcore, null));
      if (_notEquals) {
        _builder.append("\t\t\t");
        _builder.append("if (_runningSets[current_core] == null)");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t");
        _builder.append("_runningSets[current_core] = new RunningSet();");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("_runningSets[current_core].dataSet =  (ArrayList<SchedulerProcess>) runSet.clone() ;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("_runningSet = _runningSets[current_core]; //only get no remove");
        _builder.newLine();
      } else {
        _builder.append("\t\t\t");
        _builder.append("_runningSet.dataSet = runSet ; //only get no remove");
        _builder.newLine();
      }
    }
    {
      DefCore _defcore_1 = Data.schModel.getDefcore();
      boolean _notEquals_1 = (!Objects.equal(_defcore_1, null));
      if (_notEquals_1) {
        _builder.append("\t\t\t");
        _builder.append("_putColIndex[current_core].clear() ;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("_putColIndex[current_core].add((byte) getCollectionIndex(\"");
        SchedulerSet _colFrom_1 = stm.getColFrom();
        String _name_1 = _colFrom_1.getName();
        _builder.append(_name_1, "\t\t\t");
        _builder.append("\")) ; //for the replacement");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t\t\t");
        _builder.append("_putColIndex = (byte) getCollectionIndex(\"");
        SchedulerSet _colFrom_2 = stm.getColFrom();
        String _name_2 = _colFrom_2.getName();
        _builder.append(_name_2, "\t\t\t");
        _builder.append("\") ; //for the replacement");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t\t");
    _builder.append("} else {");
    _builder.newLine();
    {
      DefCore _defcore_2 = Data.schModel.getDefcore();
      boolean _notEquals_2 = (!Objects.equal(_defcore_2, null));
      if (_notEquals_2) {
        _builder.append("\t\t\t");
        _builder.append("if (_runningSets[current_core] != null)");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("\t");
        _builder.append("_runningSets[current_core].clear() ;");
        _builder.newLine();
      }
    }
    _builder.append("\t\t\t");
    _builder.append("if (_runningSet != null)");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("_runningSet.clear() ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("return - 1;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("} ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//2 Get first process which has different processID in _runningSet to run");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int processID = select_process_to_run(lastProcessID) ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (processID < 0) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("//Util.println(\"Error getting process to run - no process is selected\");");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return -1 ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//SchedulerProcess ");
    _builder.append(processName, "\t");
    _builder.append(" = running_process ;\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append(processName, "\t");
    _builder.append(" = running_process ;\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("if (lastProcessID >= 0) {\t\t");
    _builder.newLine();
    {
      DefCore _defcore_3 = Data.schModel.getDefcore();
      boolean _notEquals_3 = (!Objects.equal(_defcore_3, null));
      if (_notEquals_3) {
        _builder.append("\t\t");
        _builder.append("replace_running_process(_putColIndex[current_core].get(0),running_process, previous_running) ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("_putColIndex[current_core].clear();\t\t\t\t\t");
        _builder.newLine();
      } else {
        _builder.append("\t\t");
        _builder.append("replace_running_process(_putColIndex,running_process, previous_running) ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("}\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//remove it from collection");
    _builder.newLine();
    {
      DefCore _defcore_4 = Data.schModel.getDefcore();
      boolean _notEquals_4 = (!Objects.equal(_defcore_4, null));
      if (_notEquals_4) {
        _builder.append("\t");
        _builder.append("remove_process(");
        _builder.append(processName, "\t");
        _builder.append(".processID) ;");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t");
        SchedulerSet _colFrom_3 = stm.getColFrom();
        String _name_3 = _colFrom_3.getName();
        _builder.append(_name_3, "\t");
        _builder.append(".removeProcess(");
        _builder.append(processName, "\t");
        _builder.append(".processID) ;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("//3 change properties\t");
    _builder.newLine();
    {
      ChangeAction _change = stm.getChange();
      boolean _notEquals_5 = (!Objects.equal(_change, null));
      if (_notEquals_5) {
        {
          ChangeAction _change_1 = stm.getChange();
          EList<Statement> _sta = _change_1.getSta();
          for(final Statement st : _sta) {
            _builder.append("\t");
            Object _dispatchStatement = Statements.dispatchStatement(st, processName);
            _builder.append(_dispatchStatement, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("//4 set running parameters");
    _builder.newLine();
    {
      if (Data.runTime) {
        {
          DefCore _defcore_5 = Data.schModel.getDefcore();
          boolean _notEquals_6 = (!Objects.equal(_defcore_5, null));
          if (_notEquals_6) {
            {
              Expression _time = stm.getTime();
              boolean _notEquals_7 = (!Objects.equal(_time, null));
              if (_notEquals_7) {
                _builder.append("\t");
                _builder.append("_time_count[current_core] = 0 ;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("_time_slice[current_core] = ");
                Expression _time_1 = stm.getTime();
                String _dispatchExpression = Statements.dispatchExpression(_time_1);
                _builder.append(_dispatchExpression, "\t");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("if (_time_slice[current_core] <= 0)");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("throw new ValidationException(\"Invalide running time: \" + _time_slice) ;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("_putColIndex[current_core] = (byte) getCollectionIndex(\"");
                SchedulerSet _colTo = stm.getColTo();
                String _name_4 = _colTo.getName();
                _builder.append(_name_4, "\t");
                _builder.append("\") ;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("_time_count[current_core] = 0 ;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("_time_slice[current_core] = 0 ;\t\t\t\t\t\t\t\t");
                _builder.newLine();
              }
            }
          } else {
            {
              Expression _time_2 = stm.getTime();
              boolean _notEquals_8 = (!Objects.equal(_time_2, null));
              if (_notEquals_8) {
                _builder.append("\t");
                _builder.append("_time_count = 0 ;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("_time_slice = ");
                Expression _time_3 = stm.getTime();
                String _dispatchExpression_1 = Statements.dispatchExpression(_time_3);
                _builder.append(_dispatchExpression_1, "\t");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("if (_time_slice <= 0)");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("throw new ValidationException(\"Invalide running time: \" + _time_slice) ;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("_putColIndex = (byte) getCollectionIndex(\"");
                SchedulerSet _colTo_1 = stm.getColTo();
                String _name_5 = _colTo_1.getName();
                _builder.append(_name_5, "\t");
                _builder.append("\") ;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("_time_count = 0 ;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("_time_slice = 0 ;\t\t\t\t\t\t\t\t");
                _builder.newLine();
              }
            }
          }
        }
      }
    }
    _builder.append("}//GetProcess statement\t\t\t\t\t\t\t");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final BlockStatement stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//BlockStatement");
    _builder.newLine();
    {
      EList<Statement> _statements = stm.getStatements();
      for(final Statement st : _statements) {
        _builder.append("\t");
        Object _dispatchStatement = Statements.dispatchStatement(st, processName);
        _builder.append(_dispatchStatement, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final AssertStatement stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//assert statement\t\t\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if(!(");
    Expression _expr = stm.getExpr();
    String _dispatchExpression = Statements.dispatchExpression(_expr);
    _builder.append(_dispatchExpression, "\t");
    _builder.append(")) throw new AssertionException(\"!(");
    Expression _expr_1 = stm.getExpr();
    ICompositeNode _node = NodeModelUtils.getNode(_expr_1);
    String _tokenText = NodeModelUtils.getTokenText(_node);
    String _replace = _tokenText.replace("\"", "");
    _builder.append(_replace, "\t");
    _builder.append("\") ;");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final LoopProcess stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//Loop Process (original statement)\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Iterator<ArrayList<SchedulerProcess>> iterator_");
    SchedulerSet _colFrom = stm.getColFrom();
    String _name = _colFrom.getName();
    _builder.append(_name, "\t");
    _builder.append(" = ");
    SchedulerSet _colFrom_1 = stm.getColFrom();
    String _name_1 = _colFrom_1.getName();
    _builder.append(_name_1, "\t");
    _builder.append(".dataSet.iterator();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("while(iterator_");
    SchedulerSet _colFrom_2 = stm.getColFrom();
    String _name_2 = _colFrom_2.getName();
    _builder.append(_name_2, "\t");
    _builder.append(".hasNext()){\t\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("Iterator<SchedulerProcess> iterator_");
    scheduling.dsl.Process _pname = stm.getPname();
    String _name_3 = _pname.getName();
    _builder.append(_name_3, "\t\t");
    _builder.append(" = iterator_");
    SchedulerSet _colFrom_3 = stm.getColFrom();
    String _name_4 = _colFrom_3.getName();
    _builder.append(_name_4, "\t\t");
    _builder.append(".next().iterator();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("while(iterator_");
    scheduling.dsl.Process _pname_1 = stm.getPname();
    String _name_5 = _pname_1.getName();
    _builder.append(_name_5, "\t\t");
    _builder.append(".hasNext()){");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("SchedulerProcess ");
    scheduling.dsl.Process _pname_2 = stm.getPname();
    String _name_6 = _pname_2.getName();
    _builder.append(_name_6, "\t\t\t");
    _builder.append(" = iterator_");
    scheduling.dsl.Process _pname_3 = stm.getPname();
    String _name_7 = _pname_3.getName();
    _builder.append(_name_7, "\t\t\t");
    _builder.append(".next();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    Statement _statement = stm.getStatement();
    Object _dispatchStatement = Statements.dispatchStatement(_statement, processName);
    _builder.append(_dispatchStatement, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final NewProcessStatement stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//NewProcessStatement");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//get the model for executing new process");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("SchedulerPanModel panmodel = (SchedulerPanModel) SchedulerPromelaModel.panmodel ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//execute new process\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("PromelaProcess proc = panmodel.new ");
    NewElement _element = stm.getElement();
    scheduling.dsl.Process _process = _element.getProcess();
    String _name = _process.getName();
    _builder.append(_name, "\t");
    _builder.append("_0() ;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("int id = SchedulerPanModel.panmodel.newProcess(proc, ");
    int _max = stm.getMax();
    _builder.append(_max, "\t");
    _builder.append(", \"\");");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("if (id >= 0 ) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("ArrayList<String> para = new ArrayList<String>() ;");
    _builder.newLine();
    {
      NewElement _element_1 = stm.getElement();
      EList<ParaValue> _paraAssign = _element_1.getParaAssign();
      boolean _notEquals = (!Objects.equal(_paraAssign, null));
      if (_notEquals) {
        {
          NewElement _element_2 = stm.getElement();
          EList<ParaValue> _paraAssign_1 = _element_2.getParaAssign();
          for(final ParaValue par : _paraAssign_1) {
            {
              NumValue _num = par.getNum();
              boolean _notEquals_1 = (!Objects.equal(_num, null));
              if (_notEquals_1) {
                _builder.append("\t\t");
                _builder.append("para.add(\"");
                NumValue _num_1 = par.getNum();
                int _value = _num_1.getValue();
                _builder.append(_value, "\t\t");
                _builder.append("\") ;");
                _builder.newLineIfNotEmpty();
              } else {
                {
                  BoolValue _bool = par.getBool();
                  boolean _notEquals_2 = (!Objects.equal(_bool, null));
                  if (_notEquals_2) {
                    _builder.append("\t\t");
                    _builder.append("para.add(\"");
                    BoolValue _bool_1 = par.getBool();
                    String _value_1 = _bool_1.getValue();
                    _builder.append(_value_1, "\t\t");
                    _builder.append("\") ;");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t\t");
                    _builder.append("para.add(Integer.toString(");
                    String _id = par.getId();
                    _builder.append(_id, "\t\t");
                    _builder.append(")) ;");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
          }
        }
      }
    }
    _builder.append("\t\t");
    _builder.append("executeProcess(proc, id, para) ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("} else {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("//System.out.println(\"Can not execute new process ");
    NewElement _element_3 = stm.getElement();
    scheduling.dsl.Process _process_1 = _element_3.getProcess();
    String _name_1 = _process_1.getName();
    _builder.append(_name_1, "\t\t");
    _builder.append("\") ; //throw new ValidationException");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final ExecuteProcess stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//ExecuteProcess");
    _builder.newLine();
    {
      if (Data.initProcessList) {
        _builder.append("\t");
        _builder.append("//running_process = ");
        scheduling.dsl.Process _process = stm.getProcess();
        String _name = _process.getName();
        _builder.append(_name, "\t");
        _builder.append(" ; do not execute process when initializing");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t");
        _builder.append("running_process = ");
        scheduling.dsl.Process _process_1 = stm.getProcess();
        String _name_1 = _process_1.getName();
        _builder.append(_name_1, "\t");
        _builder.append(" ;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final PrintStatement stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//PrintStatement");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("System.out.println(");
    Expression _st = stm.getSt();
    String _dispatchExpression = Statements.dispatchExpression(_st);
    _builder.append(_dispatchExpression, "\t");
    _builder.append(") ;");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final PrintLogStatement stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//PrintLogStatement");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Log.out.println(");
    Expression _st = stm.getSt();
    String _dispatchExpression = Statements.dispatchExpression(_st);
    _builder.append(_dispatchExpression, "\t");
    _builder.append(") ;");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final GenCodeStatement stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    {
      String _comp = stm.getComp();
      boolean _equals = Objects.equal(_comp, null);
      if (_equals) {
        _builder.append("Generate.out.append(");
        Expression _st = stm.getSt();
        String _dispatchExpression = Statements.dispatchExpression(_st);
        _builder.append(_dispatchExpression, "");
        _builder.append(") ;");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("Code.addCodetoComponent(");
        Expression _st_1 = stm.getSt();
        String _dispatchExpression_1 = Statements.dispatchExpression(_st_1);
        _builder.append(_dispatchExpression_1, "");
        _builder.append(", \"");
        String _comp_1 = stm.getComp();
        _builder.append(_comp_1, "");
        _builder.append("\",false);");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final GenLnCodeStatement stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    {
      String _comp = stm.getComp();
      boolean _equals = Objects.equal(_comp, null);
      if (_equals) {
        _builder.append("Generate.out.appendLine(");
        Expression _st = stm.getSt();
        String _dispatchExpression = Statements.dispatchExpression(_st);
        _builder.append(_dispatchExpression, "");
        _builder.append(") ;");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("Code.addCodetoComponent(");
        Expression _st_1 = stm.getSt();
        String _dispatchExpression_1 = Statements.dispatchExpression(_st_1);
        _builder.append(_dispatchExpression_1, "");
        _builder.append(", \"");
        String _comp_1 = stm.getComp();
        _builder.append(_comp_1, "");
        _builder.append("\",true);");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final CheckPoint stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//CheckPoint");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("this.");
    PointID _pointid = stm.getPointid();
    String _name = _pointid.getName();
    _builder.append(_name, "\t");
    _builder.append(" = true ;\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final SetExecTime stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//SetExecTime");
    _builder.newLine();
    {
      DefCore _defcore = Data.schModel.getDefcore();
      boolean _notEquals = (!Objects.equal(_defcore, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("_time_slice[current_core] = ");
        Expression _expr = stm.getExpr();
        String _dispatchExpression = Statements.dispatchExpression(_expr);
        _builder.append(_dispatchExpression, "\t");
        _builder.append(" ;\t//runtime = ");
        _builder.append(Data.runTime = true, "\t");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t");
        _builder.append("_time_slice = ");
        Expression _expr_1 = stm.getExpr();
        String _dispatchExpression_1 = Statements.dispatchExpression(_expr_1);
        _builder.append(_dispatchExpression_1, "\t");
        _builder.append(" ;\t//runtime = ");
        _builder.append(Data.runTime = true, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final SetReturnCol stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//SetReturnCol");
    _builder.newLine();
    {
      DefCore _defcore = Data.schModel.getDefcore();
      boolean _notEquals = (!Objects.equal(_defcore, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("_putColIndex[current_core].clear();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("_putColIndex[current_core].add((byte) getCollectionIndex(\"");
        SchedulerSet _col = stm.getCol();
        String _name = _col.getName();
        _builder.append(_name, "\t");
        _builder.append("\")) ; //runtime = ");
        _builder.append(Data.runTime = true, "\t");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t");
        _builder.append("_putColIndex = (byte) getCollectionIndex(\"");
        SchedulerSet _col_1 = stm.getCol();
        String _name_1 = _col_1.getName();
        _builder.append(_name_1, "\t");
        _builder.append("\") ; //runtime = ");
        _builder.append(Data.runTime = true, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final SetReturnSet stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//SetReturnSet");
    _builder.newLine();
    {
      DefCore _defcore = Data.schModel.getDefcore();
      boolean _notEquals = (!Objects.equal(_defcore, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("_putColIndex[current_core].clear();");
        _builder.newLine();
        {
          EList<SchedulerSet> _col = stm.getCol();
          for(final SchedulerSet s : _col) {
            _builder.append("\t");
            _builder.append("_putColIndex[current_core].add((byte) getCollectionIndex(\"");
            String _name = s.getName();
            _builder.append(_name, "\t");
            _builder.append("\")) ; //runtime = ");
            _builder.append(Data.runTime = true, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("_putColIndex = (byte) getCollectionIndex(\"");
        EList<SchedulerSet> _col_1 = stm.getCol();
        SchedulerSet _get = _col_1.get(0);
        String _name_1 = _get.getName();
        _builder.append(_name_1, "\t");
        _builder.append("\") ; //runtime = ");
        _builder.append(Data.runTime = true, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final DeclareProcess stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//DeclareProcess");
    _builder.newLine();
    _builder.append("SchedulerProcess ");
    scheduling.dsl.Process _process = stm.getProcess();
    String _name = _process.getName();
    _builder.append(_name, "");
    _builder.append(" ;\t\t");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatement(final SetProcess stm, final String processName) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//SetProcess");
    _builder.newLine();
    {
      String _pid = stm.getPid();
      boolean _equals = Objects.equal(_pid, null);
      if (_equals) {
        scheduling.dsl.Process _process = stm.getProcess();
        String _name = _process.getName();
        _builder.append(_name, "");
        _builder.append(" = findProcessByID(");
        int _id = stm.getId();
        _builder.append(_id, "");
        _builder.append(") ;\t\t\t");
        _builder.newLineIfNotEmpty();
      } else {
        scheduling.dsl.Process _process_1 = stm.getProcess();
        String _name_1 = _process_1.getName();
        _builder.append(_name_1, "");
        _builder.append(" = findProcessByID(");
        String _pid_1 = stm.getPid();
        _builder.append(_pid_1, "");
        _builder.append(") ;");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public static CharSequence genStmwithProcessList(final Stm st, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    CharSequence _xblockexpression = null;
    {
      IfDef _ifdef = st.getIfdef();
      String _plus = ("1. remain: " + _ifdef);
      System.out.println(_plus);
      IfDef _ifdef_1 = st.getIfdef();
      Statements.genIfDef(_ifdef_1);
      Statement _statement = st.getStatement();
      Statements.dispatchStatementwithProcessList(_statement, remainProcessList, functionProcessList, definedProcessSet);
      IfDef _ifdef_2 = st.getIfdef();
      _xblockexpression = Statements.genCloseIfDef(_ifdef_2);
    }
    return _xblockexpression;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final ReturnStatement stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//no return statement supported (interface function)");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final SetExecTime stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//no SetExecTime statement supported (interface function)");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final SetReturnCol stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//no SetReturnCol statement supported (interface function)");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final SetReturnSet stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//no SetReturnSet statement supported (interface function)");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final MoveProcess stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//MoveProcess: (interface function), remainder (for loop): ");
    _builder.append(remainProcessList, "");
    _builder.append(", function process list (for loop): ");
    _builder.append(functionProcessList, "");
    _builder.append("\t\t");
    _builder.newLineIfNotEmpty();
    {
      scheduling.dsl.Process _process = stm.getProcess();
      String _name = _process.getName();
      boolean _contains = _name.contains("running_process");
      if (_contains) {
        _builder.append("\t");
        _builder.append("if (running_process != null) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        SchedulerSet _colTo = stm.getColTo();
        String _name_1 = _colTo.getName();
        _builder.append(_name_1, "\t\t");
        _builder.append(".put(running_process) ;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("running_process = null ;");
        _builder.newLine();
        {
          DefCore _defcore = Data.schModel.getDefcore();
          boolean _notEquals = (!Objects.equal(_defcore, null));
          if (_notEquals) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_procs[current_core] = null ;");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      } else {
        {
          scheduling.dsl.Process _process_1 = stm.getProcess();
          String _name_2 = _process_1.getName();
          boolean _contains_1 = functionProcessList.contains(_name_2);
          if (_contains_1) {
            _builder.append("\t");
            _builder.append("if (a_");
            scheduling.dsl.Process _process_2 = stm.getProcess();
            String _name_3 = _process_2.getName();
            _builder.append(_name_3, "\t");
            _builder.append(".size() == 0) { ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("//");
            scheduling.dsl.Process _process_3 = stm.getProcess();
            String _name_4 = _process_3.getName();
            _builder.append(_name_4, "\t\t");
            _builder.append(" = null -> do nothing");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("} else {//loop the set a_");
            scheduling.dsl.Process _process_4 = stm.getProcess();
            String _name_5 = _process_4.getName();
            _builder.append(_name_5, "\t");
            _builder.newLineIfNotEmpty();
            {
              scheduling.dsl.Process _process_5 = stm.getProcess();
              String _name_6 = _process_5.getName();
              boolean _contains_2 = remainProcessList.contains(_name_6);
              if (_contains_2) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("for (SchedulerProcess ");
                scheduling.dsl.Process _process_6 = stm.getProcess();
                String _name_7 = _process_6.getName();
                _builder.append(_name_7, "\t\t");
                _builder.append(" : a_");
                scheduling.dsl.Process _process_7 = stm.getProcess();
                String _name_8 = _process_7.getName();
                _builder.append(_name_8, "\t\t");
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                {
                  SchedulerDef _scheduler = Data.schModel.getScheduler();
                  boolean _notEquals_1 = (!Objects.equal(_scheduler, null));
                  if (_notEquals_1) {
                    {
                      SchedulerDef _scheduler_1 = Data.schModel.getScheduler();
                      SchedulerDataDef _schedulerdata = _scheduler_1.getSchedulerdata();
                      boolean _notEquals_2 = (!Objects.equal(_schedulerdata, null));
                      if (_notEquals_2) {
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("if (");
                        scheduling.dsl.Process _process_8 = stm.getProcess();
                        String _name_9 = _process_8.getName();
                        _builder.append(_name_9, "\t\t\t");
                        _builder.append(" != null) {");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("remove_process(");
                        scheduling.dsl.Process _process_9 = stm.getProcess();
                        String _name_10 = _process_9.getName();
                        _builder.append(_name_10, "\t\t\t\t");
                        _builder.append(".processID);\t\t\t\t\t\t\t");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("\t");
                        SchedulerSet _colTo_1 = stm.getColTo();
                        String _name_11 = _colTo_1.getName();
                        _builder.append(_name_11, "\t\t\t\t");
                        _builder.append(".put(");
                        scheduling.dsl.Process _process_10 = stm.getProcess();
                        String _name_12 = _process_10.getName();
                        _builder.append(_name_12, "\t\t\t\t");
                        _builder.append(") ;");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
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
                _builder.append("\t");
                _builder.append("if (running_process != null) {");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t\t");
                _builder.append("if (");
                scheduling.dsl.Process _process_11 = stm.getProcess();
                String _name_13 = _process_11.getName();
                _builder.append(_name_13, "\t\t\t\t");
                _builder.append(".processID == running_process.processID)");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t\t\t");
                _builder.append("running_process = null ;");
                _builder.newLine();
                {
                  DefCore _defcore_1 = Data.schModel.getDefcore();
                  boolean _notEquals_3 = (!Objects.equal(_defcore_1, null));
                  if (_notEquals_3) {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t\t");
                    _builder.append("running_procs[current_core] = null ;");
                    _builder.newLine();
                  }
                }
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              } else {
                {
                  SchedulerDef _scheduler_2 = Data.schModel.getScheduler();
                  boolean _notEquals_4 = (!Objects.equal(_scheduler_2, null));
                  if (_notEquals_4) {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("if (");
                    scheduling.dsl.Process _process_12 = stm.getProcess();
                    String _name_14 = _process_12.getName();
                    _builder.append(_name_14, "\t\t");
                    _builder.append(" != null) {\t");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("remove_process(");
                    scheduling.dsl.Process _process_13 = stm.getProcess();
                    String _name_15 = _process_13.getName();
                    _builder.append(_name_15, "\t\t\t");
                    _builder.append(".processID) ;\t\t\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t");
                    SchedulerSet _colTo_2 = stm.getColTo();
                    String _name_16 = _colTo_2.getName();
                    _builder.append(_name_16, "\t\t\t");
                    _builder.append(".put(");
                    scheduling.dsl.Process _process_14 = stm.getProcess();
                    String _name_17 = _process_14.getName();
                    _builder.append(_name_17, "\t\t\t");
                    _builder.append(") ;");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("if (running_process != null) {");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t\t");
                    _builder.append("if (");
                    scheduling.dsl.Process _process_15 = stm.getProcess();
                    String _name_18 = _process_15.getName();
                    _builder.append(_name_18, "\t\t\t\t");
                    _builder.append(".processID == running_process.processID)");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t\t\t");
                    _builder.append("running_process = null ;");
                    _builder.newLine();
                    {
                      DefCore _defcore_2 = Data.schModel.getDefcore();
                      boolean _notEquals_5 = (!Objects.equal(_defcore_2, null));
                      if (_notEquals_5) {
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("\t\t");
                        _builder.append("running_procs[current_core] = null ;");
                        _builder.newLine();
                      }
                    }
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("}");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("}");
                    _builder.newLine();
                  }
                }
              }
            }
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("iterator_");
            scheduling.dsl.Process _process_16 = stm.getProcess();
            String _name_19 = _process_16.getName();
            _builder.append(_name_19, "\t");
            _builder.append(".remove() ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            SchedulerSet _colTo_3 = stm.getColTo();
            String _name_20 = _colTo_3.getName();
            _builder.append(_name_20, "\t");
            _builder.append(".put(");
            scheduling.dsl.Process _process_17 = stm.getProcess();
            String _name_21 = _process_17.getName();
            _builder.append(_name_21, "\t");
            _builder.append(") ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("if (running_process != null) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (");
            scheduling.dsl.Process _process_18 = stm.getProcess();
            String _name_22 = _process_18.getName();
            _builder.append(_name_22, "\t\t");
            _builder.append(".processID == running_process.processID)");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("running_process = null ;");
            _builder.newLine();
            {
              DefCore _defcore_3 = Data.schModel.getDefcore();
              boolean _notEquals_6 = (!Objects.equal(_defcore_3, null));
              if (_notEquals_6) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("running_procs[current_core] = null ;");
                _builder.newLine();
              }
            }
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("}\t");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final ReorderProcess stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//ReorderProcess: (interface function), remainder (for loop): ");
    _builder.append(remainProcessList, "");
    _builder.append(", function process list (for loop): ");
    _builder.append(functionProcessList, "");
    _builder.append("\t\t");
    _builder.newLineIfNotEmpty();
    {
      scheduling.dsl.Process _process = stm.getProcess();
      String _name = _process.getName();
      boolean _contains = _name.contains("running_process");
      if (_contains) {
        _builder.append("\t");
        _builder.append("//do nothing with running_process");
        _builder.newLine();
      } else {
        {
          scheduling.dsl.Process _process_1 = stm.getProcess();
          String _name_1 = _process_1.getName();
          boolean _contains_1 = functionProcessList.contains(_name_1);
          if (_contains_1) {
            _builder.append("\t");
            _builder.append("if (a_");
            scheduling.dsl.Process _process_2 = stm.getProcess();
            String _name_2 = _process_2.getName();
            _builder.append(_name_2, "\t");
            _builder.append(".size() == 0) { ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("//");
            scheduling.dsl.Process _process_3 = stm.getProcess();
            String _name_3 = _process_3.getName();
            _builder.append(_name_3, "\t\t");
            _builder.append(" = null -> do nothing");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("} else {//loop the set a_");
            scheduling.dsl.Process _process_4 = stm.getProcess();
            String _name_4 = _process_4.getName();
            _builder.append(_name_4, "\t");
            _builder.newLineIfNotEmpty();
            {
              scheduling.dsl.Process _process_5 = stm.getProcess();
              String _name_5 = _process_5.getName();
              boolean _contains_2 = remainProcessList.contains(_name_5);
              if (_contains_2) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("for (SchedulerProcess ");
                scheduling.dsl.Process _process_6 = stm.getProcess();
                String _name_6 = _process_6.getName();
                _builder.append(_name_6, "\t\t");
                _builder.append(" : a_");
                scheduling.dsl.Process _process_7 = stm.getProcess();
                String _name_7 = _process_7.getName();
                _builder.append(_name_7, "\t\t");
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("if (");
                SchedulerSet _colTo = stm.getColTo();
                String _name_8 = _colTo.getName();
                _builder.append(_name_8, "\t\t\t");
                _builder.append(".hasProcess(");
                scheduling.dsl.Process _process_8 = stm.getProcess();
                String _name_9 = _process_8.getName();
                _builder.append(_name_9, "\t\t\t");
                _builder.append(")) {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t\t");
                SchedulerSet _colTo_1 = stm.getColTo();
                String _name_10 = _colTo_1.getName();
                _builder.append(_name_10, "\t\t\t\t");
                _builder.append(".remove_process(");
                scheduling.dsl.Process _process_9 = stm.getProcess();
                String _name_11 = _process_9.getName();
                _builder.append(_name_11, "\t\t\t\t");
                _builder.append(".processID);\t\t\t\t\t\t\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t\t");
                SchedulerSet _colTo_2 = stm.getColTo();
                String _name_12 = _colTo_2.getName();
                _builder.append(_name_12, "\t\t\t\t");
                _builder.append(".put(");
                scheduling.dsl.Process _process_10 = stm.getProcess();
                String _name_13 = _process_10.getName();
                _builder.append(_name_13, "\t\t\t\t");
                _builder.append(") ;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("}\t\t\t\t\t\t\t\t\t\t");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              } else {
                {
                  SchedulerDef _scheduler = Data.schModel.getScheduler();
                  boolean _notEquals = (!Objects.equal(_scheduler, null));
                  if (_notEquals) {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("if (");
                    scheduling.dsl.Process _process_11 = stm.getProcess();
                    String _name_14 = _process_11.getName();
                    _builder.append(_name_14, "\t\t");
                    _builder.append(" != null) {\t");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("if (");
                    SchedulerSet _colTo_3 = stm.getColTo();
                    String _name_15 = _colTo_3.getName();
                    _builder.append(_name_15, "\t\t\t");
                    _builder.append(".hasProcess(");
                    scheduling.dsl.Process _process_12 = stm.getProcess();
                    String _name_16 = _process_12.getName();
                    _builder.append(_name_16, "\t\t\t");
                    _builder.append(")>0) {");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t\t");
                    SchedulerSet _colTo_4 = stm.getColTo();
                    String _name_17 = _colTo_4.getName();
                    _builder.append(_name_17, "\t\t\t\t");
                    _builder.append(".removeProcess(");
                    scheduling.dsl.Process _process_13 = stm.getProcess();
                    String _name_18 = _process_13.getName();
                    _builder.append(_name_18, "\t\t\t\t");
                    _builder.append(".processID);\t\t\t\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t\t");
                    SchedulerSet _colTo_5 = stm.getColTo();
                    String _name_19 = _colTo_5.getName();
                    _builder.append(_name_19, "\t\t\t\t");
                    _builder.append(".put(");
                    scheduling.dsl.Process _process_14 = stm.getProcess();
                    String _name_20 = _process_14.getName();
                    _builder.append(_name_20, "\t\t\t\t");
                    _builder.append(") ;");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("}");
                    _builder.newLine();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("}");
                    _builder.newLine();
                  }
                }
              }
            }
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("iterator_");
            scheduling.dsl.Process _process_15 = stm.getProcess();
            String _name_21 = _process_15.getName();
            _builder.append(_name_21, "\t");
            _builder.append(".remove() ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            SchedulerSet _colTo_6 = stm.getColTo();
            String _name_22 = _colTo_6.getName();
            _builder.append(_name_22, "\t");
            _builder.append(".put(");
            scheduling.dsl.Process _process_16 = stm.getProcess();
            String _name_23 = _process_16.getName();
            _builder.append(_name_23, "\t");
            _builder.append(") ;\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}\t");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final SetProcessInstance stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//SetProcessInstance: (interface function), remainder (for loop): ");
    _builder.append(remainProcessList, "");
    _builder.append(", function process list (for loop): ");
    _builder.append(functionProcessList, "");
    _builder.append("\t\t");
    _builder.newLineIfNotEmpty();
    {
      scheduling.dsl.Process _process = stm.getProcess();
      String _name = _process.getName();
      boolean _contains = _name.contains("running_process");
      if (_contains) {
      } else {
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (running_process != null) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("if (");
        scheduling.dsl.Process _process_1 = stm.getProcess();
        String _name_1 = _process_1.getName();
        _builder.append(_name_1, "\t\t");
        _builder.append(".processID == running_process.processID) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("} else {");
        _builder.newLine();
        {
          scheduling.dsl.Process _process_2 = stm.getProcess();
          String _name_2 = _process_2.getName();
          boolean _contains_1 = functionProcessList.contains(_name_2);
          if (_contains_1) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (a_");
            scheduling.dsl.Process _process_3 = stm.getProcess();
            String _name_3 = _process_3.getName();
            _builder.append(_name_3, "\t\t");
            _builder.append(".size() == 0) { ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("//");
            scheduling.dsl.Process _process_4 = stm.getProcess();
            String _name_4 = _process_4.getName();
            _builder.append(_name_4, "\t\t\t");
            _builder.append(" = null -> do nothing");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("} else {//loop the set a_");
            scheduling.dsl.Process _process_5 = stm.getProcess();
            String _name_5 = _process_5.getName();
            _builder.append(_name_5, "\t\t");
            _builder.newLineIfNotEmpty();
            {
              scheduling.dsl.Process _process_6 = stm.getProcess();
              String _name_6 = _process_6.getName();
              boolean _contains_2 = remainProcessList.contains(_name_6);
              if (_contains_2) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("for (SchedulerProcess ");
                scheduling.dsl.Process _process_7 = stm.getProcess();
                String _name_7 = _process_7.getName();
                _builder.append(_name_7, "\t\t\t");
                _builder.append(" : a_");
                scheduling.dsl.Process _process_8 = stm.getProcess();
                String _name_8 = _process_8.getName();
                _builder.append(_name_8, "\t\t\t");
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                {
                  SchedulerDef _scheduler = Data.schModel.getScheduler();
                  boolean _notEquals = (!Objects.equal(_scheduler, null));
                  if (_notEquals) {
                    {
                      SchedulerDef _scheduler_1 = Data.schModel.getScheduler();
                      SchedulerDataDef _schedulerdata = _scheduler_1.getSchedulerdata();
                      boolean _notEquals_1 = (!Objects.equal(_schedulerdata, null));
                      if (_notEquals_1) {
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("if (");
                        scheduling.dsl.Process _process_9 = stm.getProcess();
                        String _name_9 = _process_9.getName();
                        _builder.append(_name_9, "\t\t\t\t");
                        _builder.append(" != null) {\t\t\t\t\t\t\t");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("\t");
                        SchedulerSet _colTo = stm.getColTo();
                        String _name_10 = _colTo.getName();
                        _builder.append(_name_10, "\t\t\t\t\t");
                        _builder.append(".put(");
                        scheduling.dsl.Process _process_10 = stm.getProcess();
                        String _name_11 = _process_10.getName();
                        _builder.append(_name_11, "\t\t\t\t\t");
                        _builder.append(") ;");
                        _builder.newLineIfNotEmpty();
                        _builder.append("\t");
                        _builder.append("\t");
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
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              } else {
                {
                  SchedulerDef _scheduler_2 = Data.schModel.getScheduler();
                  boolean _notEquals_2 = (!Objects.equal(_scheduler_2, null));
                  if (_notEquals_2) {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("if (");
                    scheduling.dsl.Process _process_11 = stm.getProcess();
                    String _name_12 = _process_11.getName();
                    _builder.append(_name_12, "\t\t\t");
                    _builder.append(" != null) {");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t");
                    SchedulerSet _colTo_1 = stm.getColTo();
                    String _name_13 = _colTo_1.getName();
                    _builder.append(_name_13, "\t\t\t\t");
                    _builder.append(".put(");
                    scheduling.dsl.Process _process_12 = stm.getProcess();
                    String _name_14 = _process_12.getName();
                    _builder.append(_name_14, "\t\t\t\t");
                    _builder.append(") ;");
                    _builder.newLineIfNotEmpty();
                    _builder.append("\t");
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
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("\t");
            SchedulerSet _colTo_2 = stm.getColTo();
            String _name_15 = _colTo_2.getName();
            _builder.append(_name_15, "\t\t");
            _builder.append(".put(");
            scheduling.dsl.Process _process_13 = stm.getProcess();
            String _name_16 = _process_13.getName();
            _builder.append(_name_16, "\t\t");
            _builder.append(") ;\t\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("}\t");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final RemoveProcess stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//remove process (interface function), remainder (for loop): ");
    _builder.append(remainProcessList, "");
    _builder.append(", function list: ");
    _builder.append(functionProcessList, "");
    _builder.newLineIfNotEmpty();
    {
      scheduling.dsl.Process _process = stm.getProcess();
      String _name = _process.getName();
      boolean _contains = _name.contains("running_process");
      if (_contains) {
        _builder.append("\t");
        _builder.append("if (running_process != null) {\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("endP = running_process.processID ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("processInScheduler[running_process.processID] = false ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("SchedulerPanModel.panmodel.reduceProcess() ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("running_process = null ;");
        _builder.newLine();
        {
          DefCore _defcore = Data.schModel.getDefcore();
          boolean _notEquals = (!Objects.equal(_defcore, null));
          if (_notEquals) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_procs[current_core] = null ;");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      } else {
        {
          scheduling.dsl.Process _process_1 = stm.getProcess();
          String _name_1 = _process_1.getName();
          boolean _contains_1 = functionProcessList.contains(_name_1);
          if (_contains_1) {
            _builder.append("\t");
            _builder.append("if (a_");
            scheduling.dsl.Process _process_2 = stm.getProcess();
            String _name_2 = _process_2.getName();
            _builder.append(_name_2, "\t");
            _builder.append(".size() == 0) { ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("//");
            scheduling.dsl.Process _process_3 = stm.getProcess();
            String _name_3 = _process_3.getName();
            _builder.append(_name_3, "\t\t");
            _builder.append(" = null -> do nothing");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("} else {//loop the set a_");
            scheduling.dsl.Process _process_4 = stm.getProcess();
            String _name_4 = _process_4.getName();
            _builder.append(_name_4, "\t");
            _builder.append("\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("for (SchedulerProcess ");
            scheduling.dsl.Process _process_5 = stm.getProcess();
            String _name_5 = _process_5.getName();
            _builder.append(_name_5, "\t\t");
            _builder.append(" : a_");
            scheduling.dsl.Process _process_6 = stm.getProcess();
            String _name_6 = _process_6.getName();
            _builder.append(_name_6, "\t\t");
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("if (running_process != null) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("if (");
            scheduling.dsl.Process _process_7 = stm.getProcess();
            String _name_7 = _process_7.getName();
            _builder.append(_name_7, "\t\t\t\t");
            _builder.append(".processID == running_process.processID) {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t\t\t");
            _builder.append("endP = running_process.processID ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t\t");
            _builder.append("processInScheduler[running_process.processID] = false ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t\t");
            _builder.append("running_process = null ;");
            _builder.newLine();
            {
              DefCore _defcore_1 = Data.schModel.getDefcore();
              boolean _notEquals_1 = (!Objects.equal(_defcore_1, null));
              if (_notEquals_1) {
                _builder.append("\t");
                _builder.append("\t\t\t\t");
                _builder.append("running_procs[current_core] = null ;");
                _builder.newLine();
              }
            }
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("} else {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t\t");
            _builder.append("remove_process(");
            scheduling.dsl.Process _process_8 = stm.getProcess();
            String _name_8 = _process_8.getName();
            _builder.append(_name_8, "\t\t\t\t\t");
            _builder.append(".processID) ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t\t\t");
            _builder.append("processInScheduler[");
            scheduling.dsl.Process _process_9 = stm.getProcess();
            String _name_9 = _process_9.getName();
            _builder.append(_name_9, "\t\t\t\t\t");
            _builder.append(".processID] = false ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("}\t\t\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("} else {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("remove_process(");
            scheduling.dsl.Process _process_10 = stm.getProcess();
            String _name_10 = _process_10.getName();
            _builder.append(_name_10, "\t\t\t\t");
            _builder.append(".processID) ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("processInScheduler[");
            scheduling.dsl.Process _process_11 = stm.getProcess();
            String _name_11 = _process_11.getName();
            _builder.append(_name_11, "\t\t\t\t");
            _builder.append(".processID] = false ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("SchedulerPanModel.panmodel.reduceProcess() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("iterator_");
            scheduling.dsl.Process _process_12 = stm.getProcess();
            String _name_12 = _process_12.getName();
            _builder.append(_name_12, "\t");
            _builder.append(".remove() ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("SchedulerPanModel.panmodel.reduceProcess() ;");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final ChangeAction stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//Change statement -> interface function");
    _builder.newLine();
    {
      EList<Statement> _sta = stm.getSta();
      for(final Statement st : _sta) {
        _builder.append("\t");
        Object _dispatchStatementwithProcessList = Statements.dispatchStatementwithProcessList(st, remainProcessList, functionProcessList, definedProcessSet);
        _builder.append(_dispatchStatementwithProcessList, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final ChangeValueUnOp stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//change value unop (interface function), remainder (for loop): ");
    _builder.append(remainProcessList, "");
    _builder.append(", function list: ");
    _builder.append(functionProcessList, "");
    _builder.append("\t\t");
    _builder.newLineIfNotEmpty();
    {
      if (((remainProcessList.size() == 0) && (functionProcessList.size() == 0))) {
        _builder.append("\t");
        CharSequence _dispatchStatement = Statements.dispatchStatement(stm, "");
        _builder.append(_dispatchStatement, "\t");
        _builder.newLineIfNotEmpty();
      } else {
        {
          int _size = remainProcessList.size();
          boolean _notEquals = (_size != 0);
          if (_notEquals) {
            _builder.append("\t");
            String pName = remainProcessList.remove(0);
            _builder.append("//remove ");
            _builder.append(pName, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("//add ");
            _builder.append(pName, "\t");
            _builder.append(" : defined ");
            boolean _add = definedProcessSet.add(pName);
            _builder.append(_add, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("for (SchedulerProcess ");
            _builder.append(pName, "\t");
            _builder.append(" : a_");
            _builder.append(pName, "\t");
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            QualifiedNames _var = stm.getVar();
            String _dispatchQualifiedNameswithProcessList = Statements.dispatchQualifiedNameswithProcessList(_var);
            String _op = stm.getOp();
            String _plus = (_dispatchQualifiedNameswithProcessList + _op);
            _builder.append(_plus, "\t\t");
            _builder.append(" ;\t\t\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            String pN = functionProcessList.remove(0);
            _builder.append("//remove ");
            _builder.append(pN, "\t");
            _builder.append("\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            {
              boolean _contains = definedProcessSet.contains(pN);
              if (_contains) {
                _builder.append("\t");
                QualifiedNames _var_1 = stm.getVar();
                String _dispatchQualifiedNameswithProcessList_1 = Statements.dispatchQualifiedNameswithProcessList(_var_1);
                String _op_1 = stm.getOp();
                String _plus_1 = (_dispatchQualifiedNameswithProcessList_1 + _op_1);
                _builder.append(_plus_1, "\t");
                _builder.append(" \';\'");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("//add ");
                _builder.append(pN, "\t");
                _builder.append(" to definedProcessSet: ");
                boolean _add_1 = definedProcessSet.add(pN);
                _builder.append(_add_1, "\t");
                _builder.append("\t\t\t\t\t\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("ArrayList<SchedulerProcess> a_");
                _builder.append(pN, "\t");
                _builder.append(" = findProcessByAlias(\"");
                _builder.append(pN, "\t");
                _builder.append("\") ;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("for (SchedulerProcess ");
                _builder.append(pN, "\t");
                _builder.append(" : a_");
                _builder.append(pN, "\t");
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                QualifiedNames _var_2 = stm.getVar();
                String _dispatchQualifiedNameswithProcessList_2 = Statements.dispatchQualifiedNameswithProcessList(_var_2);
                String _op_2 = stm.getOp();
                String _plus_2 = (_dispatchQualifiedNameswithProcessList_2 + _op_2);
                _builder.append(_plus_2, "\t\t");
                _builder.append(" \';\'");
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
    _builder.append("}\t\t");
    _builder.newLine();
    return _builder;
  }
  
  public static String dispatchQualifiedNameswithProcessList(final QualifiedNames qname) {
    ProcessPropertyName _prop = qname.getProp();
    boolean _equals = Objects.equal(_prop, null);
    if (_equals) {
      String _name = qname.getName();
      return _name.toString();
    } else {
      String _name_1 = qname.getName();
      String _plus = (_name_1 + ".");
      ProcessPropertyName _prop_1 = qname.getProp();
      String _name_2 = _prop_1.getName();
      return (_plus + _name_2);
    }
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final ChangeValueExpression stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    CharSequence _xblockexpression = null;
    {
      Expression _exp = stm.getExp();
      ArrayList<String> stmProcessList = Statements.getProcessListFromExpression(_exp);
      QualifiedNames _var = stm.getVar();
      ProcessPropertyName _prop = _var.getProp();
      boolean _notEquals = (!Objects.equal(_prop, null));
      if (_notEquals) {
        QualifiedNames _var_1 = stm.getVar();
        String _name = _var_1.getName();
        functionProcessList.add(_name);
      }
      _xblockexpression = Statements.genChangeValue(stm, remainProcessList, stmProcessList, definedProcessSet);
    }
    return _xblockexpression;
  }
  
  public static CharSequence genChangeValue(final ChangeValueExpression stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//change value (interface function), remainder (for loop): ");
    _builder.append(remainProcessList, "");
    _builder.append(", function list: ");
    _builder.append(functionProcessList, "");
    _builder.append("\t\t");
    _builder.newLineIfNotEmpty();
    {
      if (((remainProcessList.size() == 0) && (functionProcessList.size() == 0))) {
        _builder.append("\t");
        CharSequence _dispatchStatement = Statements.dispatchStatement(stm, "");
        _builder.append(_dispatchStatement, "\t");
        _builder.newLineIfNotEmpty();
      } else {
        {
          int _size = remainProcessList.size();
          boolean _notEquals = (_size != 0);
          if (_notEquals) {
            _builder.append("\t");
            String pName = remainProcessList.remove(0);
            _builder.append("//remove ");
            _builder.append(pName, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("//add ");
            _builder.append(pName, "\t");
            _builder.append(" : defined ");
            boolean _add = definedProcessSet.add(pName);
            _builder.append(_add, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("for (SchedulerProcess ");
            _builder.append(pName, "\t");
            _builder.append(" : a_");
            _builder.append(pName, "\t");
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            Object _genChangeValue = Statements.genChangeValue(stm, remainProcessList, functionProcessList, definedProcessSet);
            _builder.append(_genChangeValue, "\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            String pN = functionProcessList.remove(0);
            _builder.append("//remove ");
            _builder.append(pN, "\t");
            _builder.append("\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            {
              boolean _contains = definedProcessSet.contains(pN);
              if (_contains) {
                _builder.append("\t");
                Object _genChangeValue_1 = Statements.genChangeValue(stm, remainProcessList, functionProcessList, definedProcessSet);
                _builder.append(_genChangeValue_1, "\t");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("//add ");
                _builder.append(pN, "\t");
                _builder.append(" to definedProcessSet: ");
                boolean _add_1 = definedProcessSet.add(pN);
                _builder.append(_add_1, "\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("ArrayList<SchedulerProcess> a_");
                _builder.append(pN, "\t");
                _builder.append(" = findProcessByAlias(\"");
                _builder.append(pN, "\t");
                _builder.append("\") ;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("for (SchedulerProcess ");
                _builder.append(pN, "\t");
                _builder.append(" : a_");
                _builder.append(pN, "\t");
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                Object _genChangeValue_2 = Statements.genChangeValue(stm, remainProcessList, functionProcessList, definedProcessSet);
                _builder.append(_genChangeValue_2, "\t\t");
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
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genIfStatement(final IfStatement stm, final ArrayList<String> processListInStatement, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//IfStatement (interface function), processListInStatement: ");
    _builder.append(processListInStatement, "");
    _builder.append(", remainder (for loop): ");
    _builder.append(remainProcessList, "");
    _builder.append(", function process list (for loop): ");
    _builder.append(functionProcessList, "");
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    {
      int _size = processListInStatement.size();
      boolean _equals = (_size == 0);
      if (_equals) {
        {
          int _size_1 = remainProcessList.size();
          boolean _notEquals = (_size_1 != 0);
          if (_notEquals) {
            _builder.append("\t");
            String pName = remainProcessList.remove(0);
            _builder.append("//remove ");
            _builder.append(pName, "\t");
            _builder.append(" from remainProcessList");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("//add ");
            _builder.append(pName, "\t");
            _builder.append(" to definedProcessSet ");
            boolean _add = definedProcessSet.add(pName);
            _builder.append(_add, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("for (SchedulerProcess ");
            _builder.append(pName, "\t");
            _builder.append(" : a_");
            _builder.append(pName, "\t");
            _builder.append(") {\t\t\t\t\t ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            Object _genIfStatement = Statements.genIfStatement(stm, processListInStatement, remainProcessList, functionProcessList, definedProcessSet);
            _builder.append(_genIfStatement, "\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("if (");
            Expression _condition = stm.getCondition();
            String _dispatchExpression = Statements.dispatchExpression(_condition);
            _builder.append(_dispatchExpression, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            Statement _thenBlock = stm.getThenBlock();
            Object _dispatchStatementwithProcessList = Statements.dispatchStatementwithProcessList(_thenBlock, remainProcessList, functionProcessList, definedProcessSet);
            _builder.append(_dispatchStatementwithProcessList, "\t\t");
            _builder.newLineIfNotEmpty();
            {
              Statement _elseBlock = stm.getElseBlock();
              boolean _notEquals_1 = (!Objects.equal(_elseBlock, null));
              if (_notEquals_1) {
                _builder.append("\t");
                _builder.append("else ");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                Statement _elseBlock_1 = stm.getElseBlock();
                Object _dispatchStatementwithProcessList_1 = Statements.dispatchStatementwithProcessList(_elseBlock_1, remainProcessList, functionProcessList, definedProcessSet);
                _builder.append(_dispatchStatementwithProcessList_1, "\t\t");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      } else {
        _builder.append("\t");
        String pName_1 = processListInStatement.remove(0);
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("//add ");
        _builder.append(pName_1, "\t");
        _builder.append(" : defined ");
        boolean _add_1 = definedProcessSet.add(pName_1);
        _builder.append(_add_1, "\t");
        _builder.newLineIfNotEmpty();
        {
          boolean _contains = remainProcessList.contains(pName_1);
          if (_contains) {
            _builder.append("\t");
            _builder.append("//loop through the set: ");
            boolean _remove = remainProcessList.remove(pName_1);
            _builder.append(_remove, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("for (SchedulerProcess ");
            _builder.append(pName_1, "\t");
            _builder.append(" : a_");
            _builder.append(pName_1, "\t");
            _builder.append(") {\t\t\t\t\t ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            Object _genIfStatement_1 = Statements.genIfStatement(stm, processListInStatement, remainProcessList, functionProcessList, definedProcessSet);
            _builder.append(_genIfStatement_1, "\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            Object _genIfStatement_2 = Statements.genIfStatement(stm, processListInStatement, remainProcessList, functionProcessList, definedProcessSet);
            _builder.append(_genIfStatement_2, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final IfStatement stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    CharSequence _xblockexpression = null;
    {
      Expression _condition = stm.getCondition();
      ArrayList<String> processListInStatement = Statements.getProcessListFromExpression(_condition);
      _xblockexpression = Statements.genIfStatement(stm, processListInStatement, remainProcessList, functionProcessList, definedProcessSet);
    }
    return _xblockexpression;
  }
  
  public static ArrayList<String> getProcessListFromExpression(final Expression exp) {
    ArrayList<String> resultList = new ArrayList<String>();
    ArrayList<String> tempList = new ArrayList<String>();
    if ((exp instanceof Or)) {
      Expression _left = ((Or)exp).getLeft();
      ArrayList<String> _processListFromExpression = Statements.getProcessListFromExpression(_left);
      tempList = _processListFromExpression;
      for (final String pName : tempList) {
        boolean _contains = resultList.contains(pName);
        boolean _not = (!_contains);
        if (_not) {
          resultList.add(pName);
        }
      }
      Expression _right = ((Or)exp).getRight();
      ArrayList<String> _processListFromExpression_1 = Statements.getProcessListFromExpression(_right);
      tempList = _processListFromExpression_1;
      for (final String pName_1 : tempList) {
        boolean _contains_1 = resultList.contains(pName_1);
        boolean _not_1 = (!_contains_1);
        if (_not_1) {
          resultList.add(pName_1);
        }
      }
    } else {
      if ((exp instanceof And)) {
        Expression _left_1 = ((And)exp).getLeft();
        ArrayList<String> _processListFromExpression_2 = Statements.getProcessListFromExpression(_left_1);
        tempList = _processListFromExpression_2;
        for (final String pName_2 : tempList) {
          boolean _contains_2 = resultList.contains(pName_2);
          boolean _not_2 = (!_contains_2);
          if (_not_2) {
            resultList.add(pName_2);
          }
        }
        Expression _right_1 = ((And)exp).getRight();
        ArrayList<String> _processListFromExpression_3 = Statements.getProcessListFromExpression(_right_1);
        tempList = _processListFromExpression_3;
        for (final String pName_3 : tempList) {
          boolean _contains_3 = resultList.contains(pName_3);
          boolean _not_3 = (!_contains_3);
          if (_not_3) {
            resultList.add(pName_3);
          }
        }
      } else {
        if ((exp instanceof Equality)) {
          Expression _left_2 = ((Equality)exp).getLeft();
          ArrayList<String> _processListFromExpression_4 = Statements.getProcessListFromExpression(_left_2);
          tempList = _processListFromExpression_4;
          for (final String pName_4 : tempList) {
            boolean _contains_4 = resultList.contains(pName_4);
            boolean _not_4 = (!_contains_4);
            if (_not_4) {
              resultList.add(pName_4);
            }
          }
          Expression _right_2 = ((Equality)exp).getRight();
          ArrayList<String> _processListFromExpression_5 = Statements.getProcessListFromExpression(_right_2);
          tempList = _processListFromExpression_5;
          for (final String pName_5 : tempList) {
            boolean _contains_5 = resultList.contains(pName_5);
            boolean _not_5 = (!_contains_5);
            if (_not_5) {
              resultList.add(pName_5);
            }
          }
        } else {
          if ((exp instanceof Comparison)) {
            Expression _left_3 = ((Comparison)exp).getLeft();
            ArrayList<String> _processListFromExpression_6 = Statements.getProcessListFromExpression(_left_3);
            tempList = _processListFromExpression_6;
            for (final String pName_6 : tempList) {
              boolean _contains_6 = resultList.contains(pName_6);
              boolean _not_6 = (!_contains_6);
              if (_not_6) {
                resultList.add(pName_6);
              }
            }
            Expression _right_3 = ((Comparison)exp).getRight();
            ArrayList<String> _processListFromExpression_7 = Statements.getProcessListFromExpression(_right_3);
            tempList = _processListFromExpression_7;
            for (final String pName_7 : tempList) {
              boolean _contains_7 = resultList.contains(pName_7);
              boolean _not_7 = (!_contains_7);
              if (_not_7) {
                resultList.add(pName_7);
              }
            }
          } else {
            if ((exp instanceof Plus)) {
              Expression _left_4 = ((Plus)exp).getLeft();
              ArrayList<String> _processListFromExpression_8 = Statements.getProcessListFromExpression(_left_4);
              tempList = _processListFromExpression_8;
              for (final String pName_8 : tempList) {
                boolean _contains_8 = resultList.contains(pName_8);
                boolean _not_8 = (!_contains_8);
                if (_not_8) {
                  resultList.add(pName_8);
                }
              }
              Expression _right_4 = ((Plus)exp).getRight();
              ArrayList<String> _processListFromExpression_9 = Statements.getProcessListFromExpression(_right_4);
              tempList = _processListFromExpression_9;
              for (final String pName_9 : tempList) {
                boolean _contains_9 = resultList.contains(pName_9);
                boolean _not_9 = (!_contains_9);
                if (_not_9) {
                  resultList.add(pName_9);
                }
              }
            } else {
              if ((exp instanceof Minus)) {
                Expression _left_5 = ((Minus)exp).getLeft();
                ArrayList<String> _processListFromExpression_10 = Statements.getProcessListFromExpression(_left_5);
                tempList = _processListFromExpression_10;
                for (final String pName_10 : tempList) {
                  boolean _contains_10 = resultList.contains(pName_10);
                  boolean _not_10 = (!_contains_10);
                  if (_not_10) {
                    resultList.add(pName_10);
                  }
                }
                Expression _right_5 = ((Minus)exp).getRight();
                ArrayList<String> _processListFromExpression_11 = Statements.getProcessListFromExpression(_right_5);
                tempList = _processListFromExpression_11;
                for (final String pName_11 : tempList) {
                  boolean _contains_11 = resultList.contains(pName_11);
                  boolean _not_11 = (!_contains_11);
                  if (_not_11) {
                    resultList.add(pName_11);
                  }
                }
              } else {
                if ((exp instanceof MulOrDiv)) {
                  Expression _left_6 = ((MulOrDiv)exp).getLeft();
                  ArrayList<String> _processListFromExpression_12 = Statements.getProcessListFromExpression(_left_6);
                  tempList = _processListFromExpression_12;
                  for (final String pName_12 : tempList) {
                    boolean _contains_12 = resultList.contains(pName_12);
                    boolean _not_12 = (!_contains_12);
                    if (_not_12) {
                      resultList.add(pName_12);
                    }
                  }
                  Expression _right_6 = ((MulOrDiv)exp).getRight();
                  ArrayList<String> _processListFromExpression_13 = Statements.getProcessListFromExpression(_right_6);
                  tempList = _processListFromExpression_13;
                  for (final String pName_13 : tempList) {
                    boolean _contains_13 = resultList.contains(pName_13);
                    boolean _not_13 = (!_contains_13);
                    if (_not_13) {
                      resultList.add(pName_13);
                    }
                  }
                } else {
                  if ((exp instanceof Implies)) {
                    Expression _left_7 = ((Implies)exp).getLeft();
                    ArrayList<String> _processListFromExpression_14 = Statements.getProcessListFromExpression(_left_7);
                    tempList = _processListFromExpression_14;
                    for (final String pName_14 : tempList) {
                      boolean _contains_14 = resultList.contains(pName_14);
                      boolean _not_14 = (!_contains_14);
                      if (_not_14) {
                        resultList.add(pName_14);
                      }
                    }
                    Expression _right_7 = ((Implies)exp).getRight();
                    ArrayList<String> _processListFromExpression_15 = Statements.getProcessListFromExpression(_right_7);
                    tempList = _processListFromExpression_15;
                    for (final String pName_15 : tempList) {
                      boolean _contains_15 = resultList.contains(pName_15);
                      boolean _not_15 = (!_contains_15);
                      if (_not_15) {
                        resultList.add(pName_15);
                      }
                    }
                  } else {
                    if ((exp instanceof Not)) {
                      Expression _expression = ((Not)exp).getExpression();
                      ArrayList<String> _processListFromExpression_16 = Statements.getProcessListFromExpression(_expression);
                      tempList = _processListFromExpression_16;
                      for (final String pName_16 : tempList) {
                        boolean _contains_16 = resultList.contains(pName_16);
                        boolean _not_16 = (!_contains_16);
                        if (_not_16) {
                          resultList.add(pName_16);
                        }
                      }
                    } else {
                      if ((exp instanceof GetIDExpression)) {
                        String _pN = ((GetIDExpression)exp).getPN();
                        String _replace = _pN.replace("\"", "");
                        String _replaceAll = _replace.replaceAll(" ", "");
                        String _trim = _replaceAll.trim();
                        boolean _contains_17 = resultList.contains(_trim);
                        boolean _not_17 = (!_contains_17);
                        if (_not_17) {
                          String _pN_1 = ((GetIDExpression)exp).getPN();
                          String _replace_1 = _pN_1.replace("\"", "");
                          String _replaceAll_1 = _replace_1.replaceAll(" ", "");
                          String _trim_1 = _replaceAll_1.trim();
                          resultList.add(_trim_1);
                        }
                      } else {
                        if ((exp instanceof InExpression)) {
                          String _pN_2 = ((InExpression)exp).getPN();
                          String _replace_2 = _pN_2.replace("\"", "");
                          String _replaceAll_2 = _replace_2.replaceAll(" ", "");
                          String _trim_2 = _replaceAll_2.trim();
                          boolean _contains_18 = resultList.contains(_trim_2);
                          boolean _not_18 = (!_contains_18);
                          if (_not_18) {
                            String _pN_3 = ((InExpression)exp).getPN();
                            String _replace_3 = _pN_3.replace("\"", "");
                            String _replaceAll_3 = _replace_3.replaceAll(" ", "");
                            String _trim_3 = _replaceAll_3.trim();
                            resultList.add(_trim_3);
                          }
                        } else {
                          if ((exp instanceof InPIDExpression)) {
                            String _id = ((InPIDExpression)exp).getId();
                            String _replace_4 = _id.replace("\"", "");
                            String _replaceAll_4 = _replace_4.replaceAll(" ", "");
                            String _trim_4 = _replaceAll_4.trim();
                            boolean _contains_19 = resultList.contains(_trim_4);
                            boolean _not_19 = (!_contains_19);
                            if (_not_19) {
                              String _id_1 = ((InPIDExpression)exp).getId();
                              String _replace_5 = _id_1.replace("\"", "");
                              String _replaceAll_5 = _replace_5.replaceAll(" ", "");
                              String _trim_5 = _replaceAll_5.trim();
                              resultList.add(_trim_5);
                            }
                          } else {
                            if ((exp instanceof EmptyExpression)) {
                            } else {
                              if ((exp instanceof NullExpression)) {
                                scheduling.dsl.Process _procName = ((NullExpression)exp).getProcName();
                                String _name = _procName.getName();
                                boolean _contains_20 = resultList.contains(_name);
                                boolean _not_20 = (!_contains_20);
                                if (_not_20) {
                                  scheduling.dsl.Process _procName_1 = ((NullExpression)exp).getProcName();
                                  String _name_1 = _procName_1.getName();
                                  resultList.add(_name_1);
                                }
                              } else {
                                if ((exp instanceof IntConstant)) {
                                } else {
                                  if ((exp instanceof BoolConstant)) {
                                  } else {
                                    if ((exp instanceof StateID)) {
                                    } else {
                                      if ((exp instanceof BranchID)) {
                                      } else {
                                        if ((exp instanceof GetInstance)) {
                                        } else {
                                          if ((exp instanceof GetPID)) {
                                          } else {
                                            if ((exp instanceof Action)) {
                                            } else {
                                              if ((exp instanceof Step)) {
                                              } else {
                                                if ((exp instanceof TotalStep)) {
                                                } else {
                                                  if ((exp instanceof ExistExpression)) {
                                                  } else {
                                                    if ((exp instanceof HasNameExpression)) {
                                                      String _pN_4 = ((HasNameExpression)exp).getPN();
                                                      String _replace_6 = _pN_4.replace("\"", "");
                                                      String _replaceAll_6 = _replace_6.replaceAll(" ", "");
                                                      String _replaceAll_7 = _replaceAll_6.replaceAll(" ", "");
                                                      String _trim_6 = _replaceAll_7.trim();
                                                      boolean _contains_21 = resultList.contains(_trim_6);
                                                      boolean _not_21 = (!_contains_21);
                                                      if (_not_21) {
                                                        String _pN_5 = ((HasNameExpression)exp).getPN();
                                                        String _replace_7 = _pN_5.replace("\"", "");
                                                        String _replaceAll_8 = _replace_7.replaceAll(" ", "");
                                                        String _trim_7 = _replaceAll_8.trim();
                                                        resultList.add(_trim_7);
                                                      }
                                                    } else {
                                                      if ((exp instanceof Atomic)) {
                                                        QualifiedNames _var = ((Atomic)exp).getVar();
                                                        boolean _notEquals = (!Objects.equal(_var, null));
                                                        if (_notEquals) {
                                                          QualifiedNames _var_1 = ((Atomic)exp).getVar();
                                                          ProcessPropertyName _prop = _var_1.getProp();
                                                          boolean _notEquals_1 = (!Objects.equal(_prop, null));
                                                          if (_notEquals_1) {
                                                            QualifiedNames _var_2 = ((Atomic)exp).getVar();
                                                            String _name_2 = _var_2.getName();
                                                            resultList.add(_name_2);
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
    return resultList;
  }
  
  public static ArrayList<String> getProcessListFromRTCTLExpression(final RTCTL rtctl_exp) {
    ArrayList<String> resultList = new ArrayList<String>();
    Expression _exp = rtctl_exp.getExp();
    boolean _notEquals = (!Objects.equal(_exp, null));
    if (_notEquals) {
      Expression _exp_1 = rtctl_exp.getExp();
      ArrayList<String> _processListFromExpression = Statements.getProcessListFromExpression(_exp_1);
      resultList = _processListFromExpression;
    } else {
      RTCTL _f = rtctl_exp.getF();
      boolean _notEquals_1 = (!Objects.equal(_f, null));
      if (_notEquals_1) {
        RTCTL _f_1 = rtctl_exp.getF();
        ArrayList<String> _processListFromRTCTLExpression = Statements.getProcessListFromRTCTLExpression(_f_1);
        resultList = _processListFromRTCTLExpression;
      } else {
        RTCTL _f1 = rtctl_exp.getF1();
        ArrayList<String> _processListFromRTCTLExpression_1 = Statements.getProcessListFromRTCTLExpression(_f1);
        resultList = _processListFromRTCTLExpression_1;
        RTCTL _f2 = rtctl_exp.getF2();
        ArrayList<String> _processListFromRTCTLExpression_2 = Statements.getProcessListFromRTCTLExpression(_f2);
        resultList.addAll(_processListFromRTCTLExpression_2);
      }
    }
    return resultList;
  }
  
  public static HashSet<String> getProcessCollectionFromStatement(final Statement st) {
    HashSet<String> resultList = new HashSet<String>();
    if ((st instanceof MoveProcess)) {
      SchedulerSet _colTo = ((MoveProcess)st).getColTo();
      String _name = _colTo.getName();
      resultList.add(_name);
    }
    if ((st instanceof SetProcessInstance)) {
      SchedulerSet _colTo_1 = ((SetProcessInstance)st).getColTo();
      String _name_1 = _colTo_1.getName();
      resultList.add(_name_1);
    }
    if ((st instanceof LoopProcess)) {
      SchedulerSet _colFrom = ((LoopProcess)st).getColFrom();
      String _name_2 = _colFrom.getName();
      resultList.add(_name_2);
    }
    if ((st instanceof BlockStatement)) {
      EList<Statement> _statements = ((BlockStatement)st).getStatements();
      for (final Statement sta : _statements) {
        HashSet<String> _processCollectionFromStatement = Statements.getProcessCollectionFromStatement(sta);
        resultList.addAll(_processCollectionFromStatement);
      }
    }
    if ((st instanceof IfStatement)) {
      Statement _thenBlock = ((IfStatement)st).getThenBlock();
      HashSet<String> _processCollectionFromStatement_1 = Statements.getProcessCollectionFromStatement(_thenBlock);
      resultList.addAll(_processCollectionFromStatement_1);
      Statement _elseBlock = ((IfStatement)st).getElseBlock();
      boolean _notEquals = (!Objects.equal(_elseBlock, null));
      if (_notEquals) {
        Statement _elseBlock_1 = ((IfStatement)st).getElseBlock();
        HashSet<String> _processCollectionFromStatement_2 = Statements.getProcessCollectionFromStatement(_elseBlock_1);
        resultList.addAll(_processCollectionFromStatement_2);
      }
    }
    return resultList;
  }
  
  public static HashSet<String> getProcessCollectionFromEvent(final EventDef e) {
    HashSet<String> result = new HashSet<String>();
    EObject _event = e.getEvent();
    if ((_event instanceof EventStm)) {
      EObject _event_1 = e.getEvent();
      EList<Stm> _statements = ((EventStm) _event_1).getStatements();
      for (final Stm sta : _statements) {
        Statement _statement = sta.getStatement();
        HashSet<String> _processCollectionFromStatement = Statements.getProcessCollectionFromStatement(_statement);
        result.addAll(_processCollectionFromStatement);
      }
    } else {
      EObject _event_2 = e.getEvent();
      EList<Opt> _opt = ((EventOpt) _event_2).getOpt();
      for (final Opt opt : _opt) {
        EventStm _eventstm = opt.getEventstm();
        EList<Stm> _statements_1 = _eventstm.getStatements();
        for (final Stm sta_1 : _statements_1) {
          Statement _statement_1 = sta_1.getStatement();
          HashSet<String> _processCollectionFromStatement_1 = Statements.getProcessCollectionFromStatement(_statement_1);
          result.addAll(_processCollectionFromStatement_1);
        }
      }
    }
    return result;
  }
  
  public static String dispatchQualifiedNamesInAssignStatementwithProcessList(final QualifiedNames qname, final ArrayList<String> remainProcessList) {
    ProcessPropertyName _prop = qname.getProp();
    boolean _equals = Objects.equal(_prop, null);
    if (_equals) {
      String _name = qname.getName();
      return _name.toString();
    } else {
      String _name_1 = qname.getName();
      String _plus = (_name_1 + ".");
      ProcessPropertyName _prop_1 = qname.getProp();
      String _name_2 = _prop_1.getName();
      return (_plus + _name_2);
    }
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final GetProcess stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//GetProcess statement\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//no support for selecting a process to run");
    _builder.newLine();
    _builder.append("}\t\t\t\t\t\t");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final BlockStatement stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//Block statement (interface function)\t");
    _builder.newLine();
    {
      EList<Statement> _statements = stm.getStatements();
      for(final Statement st : _statements) {
        _builder.append("\t");
        Object _dispatchStatementwithProcessList = Statements.dispatchStatementwithProcessList(st, remainProcessList, functionProcessList, definedProcessSet);
        _builder.append(_dispatchStatementwithProcessList, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final AssertStatement stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//assert statement\t(interface function)\t\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    Expression _expr = stm.getExpr();
    ArrayList<String> processListInStatement = Statements.getProcessListFromExpression(_expr);
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _genAssert = Statements.genAssert(stm, processListInStatement, remainProcessList, functionProcessList, definedProcessSet);
    _builder.append(_genAssert, "\t");
    _builder.append("\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genAssert(final AssertStatement stm, final ArrayList<String> processListInStatement, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//remain process list: ");
    _builder.append(remainProcessList, "");
    _builder.append(", processList : ");
    _builder.append(processListInStatement, "");
    _builder.newLineIfNotEmpty();
    {
      int _size = processListInStatement.size();
      boolean _equals = (_size == 0);
      if (_equals) {
        _builder.append("//dispatch statement");
        _builder.newLine();
        CharSequence _dispatchStatement = Statements.dispatchStatement(stm, "");
        _builder.append(_dispatchStatement, "");
        _builder.newLineIfNotEmpty();
      } else {
        String pName = processListInStatement.remove(0);
        _builder.append("//remove ");
        _builder.append(pName, "");
        _builder.newLineIfNotEmpty();
        {
          boolean _contains = remainProcessList.contains(pName);
          if (_contains) {
            _builder.append("for (SchedulerProcess ");
            _builder.append(pName, "");
            _builder.append(" : a_");
            _builder.append(pName, "");
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("//");
            boolean _remove = remainProcessList.remove(pName);
            _builder.append(_remove, "\t");
            _builder.append(" ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            Object _genAssert = Statements.genAssert(stm, processListInStatement, remainProcessList, functionProcessList, definedProcessSet);
            _builder.append(_genAssert, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("}");
            _builder.newLine();
          } else {
            Object _genAssert_1 = Statements.genAssert(stm, processListInStatement, remainProcessList, functionProcessList, definedProcessSet);
            _builder.append(_genAssert_1, "");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final NewProcessStatement stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//NewProcessStatement");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("SchedulerPanModel panmodel = (SchedulerPanModel) SchedulerPromelaModel.panmodel ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("PromelaProcess proc = panmodel.new ");
    NewElement _element = stm.getElement();
    scheduling.dsl.Process _process = _element.getProcess();
    String _name = _process.getName();
    _builder.append(_name, "\t");
    _builder.append("_0() ;\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("int id = SchedulerPanModel.panmodel.newProcess(proc, ");
    int _max = stm.getMax();
    _builder.append(_max, "\t");
    _builder.append(", \"\");");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("if (id >= 0 ) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("ArrayList<String> para = new ArrayList<String>() ;");
    _builder.newLine();
    {
      NewElement _element_1 = stm.getElement();
      EList<ParaValue> _paraAssign = _element_1.getParaAssign();
      boolean _notEquals = (!Objects.equal(_paraAssign, null));
      if (_notEquals) {
        {
          NewElement _element_2 = stm.getElement();
          EList<ParaValue> _paraAssign_1 = _element_2.getParaAssign();
          for(final ParaValue par : _paraAssign_1) {
            {
              NumValue _num = par.getNum();
              boolean _notEquals_1 = (!Objects.equal(_num, null));
              if (_notEquals_1) {
                _builder.append("\t\t");
                _builder.append("para.add(\"");
                NumValue _num_1 = par.getNum();
                int _value = _num_1.getValue();
                _builder.append(_value, "\t\t");
                _builder.append("\") ;");
                _builder.newLineIfNotEmpty();
              } else {
                {
                  BoolValue _bool = par.getBool();
                  boolean _notEquals_2 = (!Objects.equal(_bool, null));
                  if (_notEquals_2) {
                    _builder.append("\t\t");
                    _builder.append("para.add(\"");
                    BoolValue _bool_1 = par.getBool();
                    String _value_1 = _bool_1.getValue();
                    _builder.append(_value_1, "\t\t");
                    _builder.append("\") ;");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t\t");
                    _builder.append("para.add(Integer.toString(");
                    String _id = par.getId();
                    _builder.append(_id, "\t\t");
                    _builder.append(")) ;");
                    _builder.newLineIfNotEmpty();
                  }
                }
              }
            }
          }
        }
      }
    }
    _builder.append("\t\t");
    _builder.append("executeProcess(proc, id, para) ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("} else {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("//System.out.println(\"Can not execute new process ");
    NewElement _element_3 = stm.getElement();
    scheduling.dsl.Process _process_1 = _element_3.getProcess();
    String _name_1 = _process_1.getName();
    _builder.append(_name_1, "\t\t");
    _builder.append("\") ; //throw new ValidationException");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final LoopProcess stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//Loop Process: -> not contain process parameter (interface function)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Iterator<ArrayList<SchedulerProcess>> iterator_");
    SchedulerSet _colFrom = stm.getColFrom();
    String _name = _colFrom.getName();
    _builder.append(_name, "\t");
    _builder.append(" = ");
    SchedulerSet _colFrom_1 = stm.getColFrom();
    String _name_1 = _colFrom_1.getName();
    _builder.append(_name_1, "\t");
    _builder.append(".dataSet.iterator();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("while(iterator_");
    SchedulerSet _colFrom_2 = stm.getColFrom();
    String _name_2 = _colFrom_2.getName();
    _builder.append(_name_2, "\t");
    _builder.append(".hasNext()){\t\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("Iterator<SchedulerProcess> iterator_");
    scheduling.dsl.Process _pname = stm.getPname();
    String _name_3 = _pname.getName();
    _builder.append(_name_3, "\t\t");
    _builder.append(" = iterator_");
    SchedulerSet _colFrom_3 = stm.getColFrom();
    String _name_4 = _colFrom_3.getName();
    _builder.append(_name_4, "\t\t");
    _builder.append(".next().iterator();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("while(iterator_");
    scheduling.dsl.Process _pname_1 = stm.getPname();
    String _name_5 = _pname_1.getName();
    _builder.append(_name_5, "\t\t");
    _builder.append(".hasNext()){");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.append("SchedulerProcess ");
    scheduling.dsl.Process _pname_2 = stm.getPname();
    String _name_6 = _pname_2.getName();
    _builder.append(_name_6, "\t\t\t");
    _builder.append(" = iterator_");
    scheduling.dsl.Process _pname_3 = stm.getPname();
    String _name_7 = _pname_3.getName();
    _builder.append(_name_7, "\t\t\t");
    _builder.append(".next();");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    Statement _statement = stm.getStatement();
    Object _dispatchStatementwithProcessList = Statements.dispatchStatementwithProcessList(_statement, remainProcessList, functionProcessList, definedProcessSet);
    _builder.append(_dispatchStatementwithProcessList, "\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final ExecuteProcess stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//ExecuteProcess -> not contain process parameter (interface function)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("running_process = ");
    scheduling.dsl.Process _process = stm.getProcess();
    String _name = _process.getName();
    _builder.append(_name, "\t");
    _builder.append(" ;");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final PrintStatement stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//PrintStatement -> not contain process parameter (interface function)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("System.out.println(");
    Expression _st = stm.getSt();
    String _dispatchExpression = Statements.dispatchExpression(_st);
    _builder.append(_dispatchExpression, "\t");
    _builder.append(") ;");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final GenCodeStatement stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//GenLnCodeStatement: (interface function), ");
    _builder.newLine();
    _builder.append("//remainder (for loop): ");
    _builder.append(remainProcessList, "");
    _builder.append(", function process list (for loop): ");
    _builder.append(functionProcessList, "");
    _builder.append("\t\t\t\t\t\t\t");
    _builder.newLineIfNotEmpty();
    {
      if (((remainProcessList.size() == 0) && (functionProcessList.size() == 0))) {
        _builder.append("\t");
        CharSequence _dispatchStatement = Statements.dispatchStatement(stm, "");
        _builder.append(_dispatchStatement, "\t");
        _builder.newLineIfNotEmpty();
      } else {
        {
          int _size = remainProcessList.size();
          boolean _notEquals = (_size != 0);
          if (_notEquals) {
            _builder.append("\t");
            String pName = remainProcessList.remove(0);
            _builder.append("//remove ");
            _builder.append(pName, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("//add ");
            _builder.append(pName, "\t");
            _builder.append(" to definedProcessSet ");
            boolean _add = definedProcessSet.add(pName);
            _builder.append(_add, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("for (SchedulerProcess ");
            _builder.append(pName, "\t");
            _builder.append(" : a_");
            _builder.append(pName, "\t");
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            {
              String _comp = stm.getComp();
              boolean _equals = Objects.equal(_comp, null);
              if (_equals) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("Generate.out.appendLine(");
                Expression _st = stm.getSt();
                String _dispatchExpression = Statements.dispatchExpression(_st);
                _builder.append(_dispatchExpression, "\t\t");
                _builder.append(") ;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("Code.addCodetoComponent(");
                Expression _st_1 = stm.getSt();
                String _dispatchExpression_1 = Statements.dispatchExpression(_st_1);
                _builder.append(_dispatchExpression_1, "\t\t");
                _builder.append(", \"");
                String _comp_1 = stm.getComp();
                _builder.append(_comp_1, "\t\t");
                _builder.append("\",false);");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            String pN = functionProcessList.remove(0);
            _builder.append("//remove ");
            _builder.append(pN, "\t");
            _builder.append("\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            {
              boolean _contains = definedProcessSet.contains(pN);
              if (_contains) {
                {
                  String _comp_2 = stm.getComp();
                  boolean _equals_1 = Objects.equal(_comp_2, null);
                  if (_equals_1) {
                    _builder.append("\t");
                    _builder.append("Generate.out.appendLine(");
                    Expression _st_2 = stm.getSt();
                    String _dispatchExpression_2 = Statements.dispatchExpression(_st_2);
                    _builder.append(_dispatchExpression_2, "\t");
                    _builder.append(") ;");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("Code.addCodetoComponent(");
                    Expression _st_3 = stm.getSt();
                    String _dispatchExpression_3 = Statements.dispatchExpression(_st_3);
                    _builder.append(_dispatchExpression_3, "\t");
                    _builder.append(", \"");
                    String _comp_3 = stm.getComp();
                    _builder.append(_comp_3, "\t");
                    _builder.append("\",false);");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                _builder.append("\t");
                _builder.append("//add ");
                _builder.append(pN, "\t");
                _builder.append(" to definedProcessSet: ");
                boolean _add_1 = definedProcessSet.add(pN);
                _builder.append(_add_1, "\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("ArrayList<SchedulerProcess> a_");
                _builder.append(pN, "\t");
                _builder.append(" = findProcessByAlias(\"");
                _builder.append(pN, "\t");
                _builder.append("\") ;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("for (SchedulerProcess ");
                _builder.append(pN, "\t");
                _builder.append(" : a_");
                _builder.append(pN, "\t");
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                {
                  String _comp_4 = stm.getComp();
                  boolean _equals_2 = Objects.equal(_comp_4, null);
                  if (_equals_2) {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("Generate.out.appendLine(");
                    Expression _st_4 = stm.getSt();
                    String _dispatchExpression_4 = Statements.dispatchExpression(_st_4);
                    _builder.append(_dispatchExpression_4, "\t\t");
                    _builder.append(") ;");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("Code.addCodetoComponent(");
                    Expression _st_5 = stm.getSt();
                    String _dispatchExpression_5 = Statements.dispatchExpression(_st_5);
                    _builder.append(_dispatchExpression_5, "\t\t");
                    _builder.append(", \"");
                    String _comp_5 = stm.getComp();
                    _builder.append(_comp_5, "\t\t");
                    _builder.append("\",false);");
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
      }
    }
    _builder.append("}\t\t");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final GenLnCodeStatement stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//GenLnCodeStatement: (interface function)");
    _builder.newLine();
    _builder.append("//remainder (for loop): ");
    _builder.append(remainProcessList, "");
    _builder.append(", function process list (for loop): ");
    _builder.append(functionProcessList, "");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t\t");
    _builder.newLine();
    {
      if (((remainProcessList.size() == 0) && (functionProcessList.size() == 0))) {
        _builder.append("\t");
        CharSequence _dispatchStatement = Statements.dispatchStatement(stm, "");
        _builder.append(_dispatchStatement, "\t");
        _builder.newLineIfNotEmpty();
      } else {
        {
          int _size = remainProcessList.size();
          boolean _notEquals = (_size != 0);
          if (_notEquals) {
            _builder.append("\t");
            String pName = remainProcessList.remove(0);
            _builder.append("//remove ");
            _builder.append(pName, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("//add ");
            _builder.append(pName, "\t");
            _builder.append(" : defined ");
            boolean _add = definedProcessSet.add(pName);
            _builder.append(_add, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("for (SchedulerProcess ");
            _builder.append(pName, "\t");
            _builder.append(" : a_");
            _builder.append(pName, "\t");
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            {
              String _comp = stm.getComp();
              boolean _equals = Objects.equal(_comp, null);
              if (_equals) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("Generate.out.appendLine(");
                Expression _st = stm.getSt();
                String _dispatchExpression = Statements.dispatchExpression(_st);
                _builder.append(_dispatchExpression, "\t\t");
                _builder.append(") ;");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("Code.addCodetoComponent(");
                Expression _st_1 = stm.getSt();
                String _dispatchExpression_1 = Statements.dispatchExpression(_st_1);
                _builder.append(_dispatchExpression_1, "\t\t");
                _builder.append(", \"");
                String _comp_1 = stm.getComp();
                _builder.append(_comp_1, "\t\t");
                _builder.append("\",true);");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            String pN = functionProcessList.remove(0);
            _builder.append("//remove ");
            _builder.append(pN, "\t");
            _builder.append("\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            {
              boolean _contains = definedProcessSet.contains(pN);
              if (_contains) {
                {
                  String _comp_2 = stm.getComp();
                  boolean _equals_1 = Objects.equal(_comp_2, null);
                  if (_equals_1) {
                    _builder.append("\t");
                    _builder.append("Generate.out.appendLine(");
                    Expression _st_2 = stm.getSt();
                    String _dispatchExpression_2 = Statements.dispatchExpression(_st_2);
                    _builder.append(_dispatchExpression_2, "\t");
                    _builder.append(") ;");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("Code.addCodetoComponent(");
                    Expression _st_3 = stm.getSt();
                    String _dispatchExpression_3 = Statements.dispatchExpression(_st_3);
                    _builder.append(_dispatchExpression_3, "\t");
                    _builder.append(", \"");
                    String _comp_3 = stm.getComp();
                    _builder.append(_comp_3, "\t");
                    _builder.append("\",true);");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                _builder.append("\t");
                _builder.append("//add ");
                _builder.append(pN, "\t");
                _builder.append(" to definedProcessSet: ");
                boolean _add_1 = definedProcessSet.add(pN);
                _builder.append(_add_1, "\t");
                _builder.append("\t\t\t\t\t\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("ArrayList<SchedulerProcess> a_");
                _builder.append(pN, "\t");
                _builder.append(" = findProcessByAlias(\"");
                _builder.append(pN, "\t");
                _builder.append("\") ;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("for (SchedulerProcess ");
                _builder.append(pN, "\t");
                _builder.append(" : a_");
                _builder.append(pN, "\t");
                _builder.append(") {");
                _builder.newLineIfNotEmpty();
                {
                  String _comp_4 = stm.getComp();
                  boolean _equals_2 = Objects.equal(_comp_4, null);
                  if (_equals_2) {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("Generate.out.appendLine(");
                    Expression _st_4 = stm.getSt();
                    String _dispatchExpression_4 = Statements.dispatchExpression(_st_4);
                    _builder.append(_dispatchExpression_4, "\t\t");
                    _builder.append(") ;");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("Code.addCodetoComponent(");
                    Expression _st_5 = stm.getSt();
                    String _dispatchExpression_5 = Statements.dispatchExpression(_st_5);
                    _builder.append(_dispatchExpression_5, "\t\t");
                    _builder.append(", \"");
                    String _comp_5 = stm.getComp();
                    _builder.append(_comp_5, "\t\t");
                    _builder.append("\",true);");
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
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final PrintLogStatement stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//PrintLogStatement -> not contain process parameter (interface function)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Log.out.println(");
    Expression _st = stm.getSt();
    String _dispatchExpression = Statements.dispatchExpression(_st);
    _builder.append(_dispatchExpression, "\t");
    _builder.append(") ;");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final CheckPoint stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("{//CheckPoint -> not contain process parameter (interface function)\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("this.");
    PointID _pointid = stm.getPointid();
    String _name = _pointid.getName();
    _builder.append(_name, "\t");
    _builder.append(" = true ;");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final DeclareProcess stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//DeclareProcess");
    _builder.newLine();
    _builder.append("SchedulerProcess ");
    scheduling.dsl.Process _process = stm.getProcess();
    String _name = _process.getName();
    _builder.append(_name, "");
    _builder.append(" ;\t");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  protected static CharSequence _dispatchStatementwithProcessList(final SetProcess stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//SetProcess");
    _builder.newLine();
    {
      String _pid = stm.getPid();
      boolean _equals = Objects.equal(_pid, null);
      if (_equals) {
        scheduling.dsl.Process _process = stm.getProcess();
        String _name = _process.getName();
        _builder.append(_name, "");
        _builder.append(" = findProcessByID(");
        int _id = stm.getId();
        _builder.append(_id, "");
        _builder.append(") ;\t\t\t");
        _builder.newLineIfNotEmpty();
      } else {
        scheduling.dsl.Process _process_1 = stm.getProcess();
        String _name_1 = _process_1.getName();
        _builder.append(_name_1, "");
        _builder.append(" = findProcessByID(");
        String _pid_1 = stm.getPid();
        _builder.append(_pid_1, "");
        _builder.append(") ;");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public static boolean isRunTime(final Statement stm) {
    if (stm instanceof ChangeValue) {
      return _isRunTime((ChangeValue)stm);
    } else if (stm instanceof AssertStatement) {
      return _isRunTime((AssertStatement)stm);
    } else if (stm instanceof BlockStatement) {
      return _isRunTime((BlockStatement)stm);
    } else if (stm instanceof CallFunction) {
      return _isRunTime((CallFunction)stm);
    } else if (stm instanceof CheckPoint) {
      return _isRunTime((CheckPoint)stm);
    } else if (stm instanceof DeclareProcess) {
      return _isRunTime((DeclareProcess)stm);
    } else if (stm instanceof GenCodeStatement) {
      return _isRunTime((GenCodeStatement)stm);
    } else if (stm instanceof GenLnCodeStatement) {
      return _isRunTime((GenLnCodeStatement)stm);
    } else if (stm instanceof GetProcess) {
      return _isRunTime((GetProcess)stm);
    } else if (stm instanceof IfStatement) {
      return _isRunTime((IfStatement)stm);
    } else if (stm instanceof LoopProcess) {
      return _isRunTime((LoopProcess)stm);
    } else if (stm instanceof MoveProcess) {
      return _isRunTime((MoveProcess)stm);
    } else if (stm instanceof NewProcessStatement) {
      return _isRunTime((NewProcessStatement)stm);
    } else if (stm instanceof PrintLogStatement) {
      return _isRunTime((PrintLogStatement)stm);
    } else if (stm instanceof PrintStatement) {
      return _isRunTime((PrintStatement)stm);
    } else if (stm instanceof RemoveProcess) {
      return _isRunTime((RemoveProcess)stm);
    } else if (stm instanceof ReturnStatement) {
      return _isRunTime((ReturnStatement)stm);
    } else if (stm instanceof SetExecTime) {
      return _isRunTime((SetExecTime)stm);
    } else if (stm instanceof SetProcess) {
      return _isRunTime((SetProcess)stm);
    } else if (stm instanceof SetProcessInstance) {
      return _isRunTime((SetProcessInstance)stm);
    } else if (stm instanceof SetReturnCol) {
      return _isRunTime((SetReturnCol)stm);
    } else if (stm instanceof SetReturnSet) {
      return _isRunTime((SetReturnSet)stm);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(stm).toString());
    }
  }
  
  public static CharSequence dispatchStatement(final EObject stm, final String processName) {
    if (stm instanceof ChangeListValue) {
      return _dispatchStatement((ChangeListValue)stm, processName);
    } else if (stm instanceof ChangeValueExpression) {
      return _dispatchStatement((ChangeValueExpression)stm, processName);
    } else if (stm instanceof ChangeValueUnOp) {
      return _dispatchStatement((ChangeValueUnOp)stm, processName);
    } else if (stm instanceof AssertStatement) {
      return _dispatchStatement((AssertStatement)stm, processName);
    } else if (stm instanceof BlockStatement) {
      return _dispatchStatement((BlockStatement)stm, processName);
    } else if (stm instanceof CheckPoint) {
      return _dispatchStatement((CheckPoint)stm, processName);
    } else if (stm instanceof DeclareProcess) {
      return _dispatchStatement((DeclareProcess)stm, processName);
    } else if (stm instanceof GenCodeStatement) {
      return _dispatchStatement((GenCodeStatement)stm, processName);
    } else if (stm instanceof GenLnCodeStatement) {
      return _dispatchStatement((GenLnCodeStatement)stm, processName);
    } else if (stm instanceof GetProcess) {
      return _dispatchStatement((GetProcess)stm, processName);
    } else if (stm instanceof IfStatement) {
      return _dispatchStatement((IfStatement)stm, processName);
    } else if (stm instanceof LoopProcess) {
      return _dispatchStatement((LoopProcess)stm, processName);
    } else if (stm instanceof MoveProcess) {
      return _dispatchStatement((MoveProcess)stm, processName);
    } else if (stm instanceof NewProcessStatement) {
      return _dispatchStatement((NewProcessStatement)stm, processName);
    } else if (stm instanceof PrintLogStatement) {
      return _dispatchStatement((PrintLogStatement)stm, processName);
    } else if (stm instanceof PrintStatement) {
      return _dispatchStatement((PrintStatement)stm, processName);
    } else if (stm instanceof RemoveProcess) {
      return _dispatchStatement((RemoveProcess)stm, processName);
    } else if (stm instanceof ReorderProcess) {
      return _dispatchStatement((ReorderProcess)stm, processName);
    } else if (stm instanceof ReturnStatement) {
      return _dispatchStatement((ReturnStatement)stm, processName);
    } else if (stm instanceof SetExecTime) {
      return _dispatchStatement((SetExecTime)stm, processName);
    } else if (stm instanceof SetProcess) {
      return _dispatchStatement((SetProcess)stm, processName);
    } else if (stm instanceof SetProcessInstance) {
      return _dispatchStatement((SetProcessInstance)stm, processName);
    } else if (stm instanceof SetReturnCol) {
      return _dispatchStatement((SetReturnCol)stm, processName);
    } else if (stm instanceof SetReturnSet) {
      return _dispatchStatement((SetReturnSet)stm, processName);
    } else if (stm instanceof ChangeAction) {
      return _dispatchStatement((ChangeAction)stm, processName);
    } else if (stm instanceof ExecuteProcess) {
      return _dispatchStatement((ExecuteProcess)stm, processName);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(stm, processName).toString());
    }
  }
  
  public static CharSequence dispatchStatementwithProcessList(final EObject stm, final ArrayList<String> remainProcessList, final ArrayList<String> functionProcessList, final HashSet<String> definedProcessSet) {
    if (stm instanceof ChangeValueExpression) {
      return _dispatchStatementwithProcessList((ChangeValueExpression)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof ChangeValueUnOp) {
      return _dispatchStatementwithProcessList((ChangeValueUnOp)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof AssertStatement) {
      return _dispatchStatementwithProcessList((AssertStatement)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof BlockStatement) {
      return _dispatchStatementwithProcessList((BlockStatement)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof CheckPoint) {
      return _dispatchStatementwithProcessList((CheckPoint)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof DeclareProcess) {
      return _dispatchStatementwithProcessList((DeclareProcess)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof GenCodeStatement) {
      return _dispatchStatementwithProcessList((GenCodeStatement)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof GenLnCodeStatement) {
      return _dispatchStatementwithProcessList((GenLnCodeStatement)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof GetProcess) {
      return _dispatchStatementwithProcessList((GetProcess)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof IfStatement) {
      return _dispatchStatementwithProcessList((IfStatement)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof LoopProcess) {
      return _dispatchStatementwithProcessList((LoopProcess)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof MoveProcess) {
      return _dispatchStatementwithProcessList((MoveProcess)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof NewProcessStatement) {
      return _dispatchStatementwithProcessList((NewProcessStatement)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof PrintLogStatement) {
      return _dispatchStatementwithProcessList((PrintLogStatement)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof PrintStatement) {
      return _dispatchStatementwithProcessList((PrintStatement)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof RemoveProcess) {
      return _dispatchStatementwithProcessList((RemoveProcess)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof ReorderProcess) {
      return _dispatchStatementwithProcessList((ReorderProcess)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof ReturnStatement) {
      return _dispatchStatementwithProcessList((ReturnStatement)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof SetExecTime) {
      return _dispatchStatementwithProcessList((SetExecTime)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof SetProcess) {
      return _dispatchStatementwithProcessList((SetProcess)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof SetProcessInstance) {
      return _dispatchStatementwithProcessList((SetProcessInstance)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof SetReturnCol) {
      return _dispatchStatementwithProcessList((SetReturnCol)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof SetReturnSet) {
      return _dispatchStatementwithProcessList((SetReturnSet)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof ChangeAction) {
      return _dispatchStatementwithProcessList((ChangeAction)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else if (stm instanceof ExecuteProcess) {
      return _dispatchStatementwithProcessList((ExecuteProcess)stm, remainProcessList, functionProcessList, definedProcessSet);
    } else {
      throw new IllegalArgumentException("Unhandled parameter types: " +
        Arrays.<Object>asList(stm, remainProcessList, functionProcessList, definedProcessSet).toString());
    }
  }
}
