package scheduling.generator;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.xbase.lib.ExclusiveRange;
import scheduling.dsl.BoolValue;
import scheduling.dsl.ConfigProcess;
import scheduling.dsl.DefCore;
import scheduling.dsl.Element;
import scheduling.dsl.EventDef;
import scheduling.dsl.EventOpt;
import scheduling.dsl.EventStm;
import scheduling.dsl.GetProcess;
import scheduling.dsl.HandlerDef;
import scheduling.dsl.NumValue;
import scheduling.dsl.Opt;
import scheduling.dsl.PeriodicProcess;
import scheduling.dsl.ProcessConfig;
import scheduling.dsl.ProcessDSL;
import scheduling.dsl.ProcessDef;
import scheduling.dsl.SchedulerDef;
import scheduling.dsl.Statement;
import scheduling.dsl.Stm;
import scheduling.dsl.Value;
import scheduling.generator.Data;
import scheduling.generator.Statements;

@SuppressWarnings("all")
public class Handler {
  public static String genMissingHandler(final SchedulerDef sch, final ProcessDSL procModel) {
    String result = "";
    boolean hasselect_process = false;
    boolean hasNew_process = false;
    boolean hasClock = false;
    boolean haspreTake = false;
    boolean haspostTake = false;
    HandlerDef _handler = sch.getHandler();
    boolean _notEquals = (!Objects.equal(_handler, null));
    if (_notEquals) {
      HandlerDef _handler_1 = sch.getHandler();
      EList<EventDef> _event = _handler_1.getEvent();
      for (final EventDef e : _event) {
        scheduling.dsl.String _eventname = e.getEventname();
        String _string = _eventname.toString();
        String _trim = _string.trim();
        boolean _equals = _trim.equals("select_process");
        if (_equals) {
          hasselect_process = true;
        } else {
          scheduling.dsl.String _eventname_1 = e.getEventname();
          String _string_1 = _eventname_1.toString();
          String _trim_1 = _string_1.trim();
          boolean _equals_1 = _trim_1.equals("new_process");
          if (_equals_1) {
            hasNew_process = true;
          } else {
            scheduling.dsl.String _eventname_2 = e.getEventname();
            String _string_2 = _eventname_2.toString();
            String _trim_2 = _string_2.trim();
            boolean _equals_2 = _trim_2.equals("clock");
            if (_equals_2) {
              hasClock = true;
            } else {
              scheduling.dsl.String _eventname_3 = e.getEventname();
              String _string_3 = _eventname_3.toString();
              String _trim_3 = _string_3.trim();
              boolean _equals_3 = _trim_3.equals("pre_take");
              if (_equals_3) {
                haspreTake = true;
              } else {
                scheduling.dsl.String _eventname_4 = e.getEventname();
                String _string_4 = _eventname_4.toString();
                String _trim_4 = _string_4.trim();
                boolean _equals_4 = _trim_4.equals("post_take");
                if (_equals_4) {
                  haspostTake = true;
                }
              }
            }
          }
        }
      }
    }
    if ((!hasselect_process)) {
      String _result = result;
      CharSequence _genMissingselect_process = Handler.genMissingselect_process();
      result = (_result + _genMissingselect_process);
    }
    if ((!hasNew_process)) {
      String _result_1 = result;
      CharSequence _genMissingNew_process = Handler.genMissingNew_process();
      result = (_result_1 + _genMissingNew_process);
    }
    if ((!hasClock)) {
      String _result_2 = result;
      CharSequence _genMissingClock = Handler.genMissingClock(procModel);
      result = (_result_2 + _genMissingClock);
    }
    if ((!haspreTake)) {
      String _result_3 = result;
      CharSequence _genMissingPreTake = Handler.genMissingPreTake();
      result = (_result_3 + _genMissingPreTake);
    }
    if ((!haspostTake)) {
      String _result_4 = result;
      CharSequence _genMissingPostTake = Handler.genMissingPostTake();
      result = (_result_4 + _genMissingPostTake);
    }
    String _result_5 = result;
    CharSequence _genTerminate_processs = Handler.genTerminate_processs(sch);
    result = (_result_5 + _genTerminate_processs);
    return result;
  }
  
  public static CharSequence genTerminate_processs(final SchedulerDef sch) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public int terminate_process(String procName) throws ValidationException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//default missing handler\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("SchedulerProcess target_process = null ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Util.println(\"--> Terminate process: \" + procName + \" default processing\") ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int id = 0 ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("for (String procN : processList) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if (procN.contains(procName)) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("SchedulerProcess terminate_target_process = findProcessByID(id) ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("target_process = terminate_target_process ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("if (terminate_target_process != null) {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("return terminate_process(id) ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("} ");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("id ++ ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Util.println(\"--> Cannot find process: \" + procName) ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return -1 ;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("public int terminate_process(int processID) throws ValidationException {\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//Util.println(\"--> Remove process : \" + processID + \", default processing\") ;\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("endP = (byte) processID ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (running_process != null) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if (running_process.processID == processID) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("SchedulerObject.processInScheduler[processID] = false ;");
    _builder.newLine();
    {
      DefCore _defcore = Data.schModel.getDefcore();
      boolean _notEquals = (!Objects.equal(_defcore, null));
      if (_notEquals) {
        _builder.append("\t\t\t");
        _builder.append("running_procs[current_core] = null ;");
        _builder.newLine();
      }
    }
    _builder.append("\t\t\t");
    _builder.append("running_process = null ;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("return processID;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\t\t\t");
    _builder.newLine();
    {
      for(final String col : Data.collectionList) {
        _builder.append(col, "");
        _builder.append(".removeProcess(processID);");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return processID ;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genMissingselect_process() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void select_process(GenerateCode code) throws ValidationException{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("select_process(-1) ;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("public int select_process(int lastProcessID) throws ValidationException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//default missing handler");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Util.println(\"--> Select process, default processing\") ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return - 1;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genMissingNew_process() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//API -> run(process) -> new_process EVENT");
    _builder.newLine();
    _builder.append("//If no handle this API then the scheduler may not process new process event");
    _builder.newLine();
    _builder.append("public SchedulerProcess new_process(String procName, int processID, ArrayList<String> para) throws ValidationException {\t\t\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return null;");
    _builder.newLine();
    _builder.append("}\t");
    _builder.newLine();
    _builder.append("public SchedulerProcess new_process(String procName) throws ValidationException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return null ;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("public void new_process(String procName, int processID) throws ValidationException{ ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//default missing handler ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Util.println(\"New process : \" + procName + \", but no processing\") ;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("public void new_process(SchedulerProcess target_process) {}");
    _builder.newLine();
    _builder.append("public void config_new_process(SchedulerProcess target_process) {}");
    _builder.newLine();
    _builder.append("public void addProcessList(ArrayList<SchedulerProcess> procList) throws ValidationException { }//called by init_order");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genMissingClock(final ProcessDSL procModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/*");
    _builder.newLine();
    _builder.append("public void clock(GenerateCode _code) throws ValidationException{");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("this._code = _code ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("clock() ;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("*/");
    _builder.newLine();
    _builder.append("public void clock() throws ValidationException{ ");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//default missing handler");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//hasClockEventHandler = ");
    _builder.append(Data.hasClockEventHandler = false, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("//Util.println(\"--> Clock event : increase clock value, check running time\") ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("inc_time() ; //increase all clock including the running clock (_time_count)");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("check_running_time_to_put_running_process() ; //to end the time slice");
    _builder.newLine();
    {
      DefCore _defcore = Data.schModel.getDefcore();
      boolean _notEquals = (!Objects.equal(_defcore, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("if (_runningSets[current_core] != null) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("_runningSets[current_core].clear();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("if (_runningSet != null) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("_runningSet.clear();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}\t");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    CharSequence _genClockEventForPeriodicProcess = Handler.genClockEventForPeriodicProcess(procModel);
    _builder.append(_genClockEventForPeriodicProcess, "\t");
    _builder.append("\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("if (!hasGenTemplate) {\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if (running_process == null) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("if (select_process(-1) < 0) {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("//Util.println(\"No running process\");");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
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
  
  public static CharSequence genMissingPreTake() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void preTake() throws ValidationException{}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genMissingPostTake() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void postTake() throws ValidationException {}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genPreTake(final EventDef e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void preTake() throws ValidationException{");
    _builder.newLine();
    {
      EObject _event = e.getEvent();
      if ((_event instanceof EventStm)) {
        {
          EObject _event_1 = e.getEvent();
          EList<Stm> _statements = ((EventStm) _event_1).getStatements();
          for(final Stm sta : _statements) {
            _builder.append("\t");
            CharSequence _genStm = Statements.genStm(sta, "");
            _builder.append(_genStm, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genPostTake(final EventDef e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void postTake() throws ValidationException{");
    _builder.newLine();
    {
      EObject _event = e.getEvent();
      if ((_event instanceof EventStm)) {
        {
          EObject _event_1 = e.getEvent();
          EList<Stm> _statements = ((EventStm) _event_1).getStatements();
          for(final Stm sta : _statements) {
            _builder.append("\t");
            CharSequence _genStm = Statements.genStm(sta, "");
            _builder.append(_genStm, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genEventSelect(final EventDef e) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public int select_process(int lastProcessID) throws ValidationException {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("SchedulerProcess ");
    scheduling.dsl.Process _processname = e.getProcessname();
    String _name = _processname.getName();
    _builder.append(_name, "\t");
    _builder.append(" ;\t\t\t");
    _builder.newLineIfNotEmpty();
    {
      EObject _event = e.getEvent();
      if ((_event instanceof EventStm)) {
        {
          EObject _event_1 = e.getEvent();
          EList<Stm> _statements = ((EventStm) _event_1).getStatements();
          for(final Stm sta : _statements) {
            _builder.append("\t");
            scheduling.dsl.Process _processname_1 = e.getProcessname();
            String _name_1 = _processname_1.getName();
            CharSequence _genStm = Statements.genStm(sta, _name_1);
            _builder.append(_genStm, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      } else {
        _builder.append("\t");
        EObject _event_2 = e.getEvent();
        EList<Opt> _opt = ((EventOpt) _event_2).getOpt();
        final int numopt = _opt.size();
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("//_schnumopt = ");
        _builder.append(numopt, "\t");
        _builder.append(" ;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("switch (_opt[_schselopt][1]) { //select");
        _builder.newLine();
        {
          ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, numopt, true);
          for(final Integer i : _doubleDotLessThan) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("case ");
            _builder.append(i, "\t\t");
            _builder.append(" : {");
            _builder.newLineIfNotEmpty();
            {
              EObject _event_3 = e.getEvent();
              EList<Opt> _opt_1 = ((EventOpt) _event_3).getOpt();
              Opt _get = _opt_1.get((i).intValue());
              EventStm _eventstm = _get.getEventstm();
              EList<Stm> _statements_1 = _eventstm.getStatements();
              for(final Stm sta_1 : _statements_1) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                scheduling.dsl.Process _processname_2 = e.getProcessname();
                String _name_2 = _processname_2.getName();
                CharSequence _genStm_1 = Statements.genStm(sta_1, _name_2);
                _builder.append(_genStm_1, "\t\t\t");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("break ;");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (running_process != null)");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return running_process.processID ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return -1;\t\t\t\t\t\t\t\t");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public void set_RunningSet() {");
    _builder.newLine();
    {
      EObject _event_4 = e.getEvent();
      if ((_event_4 instanceof EventStm)) {
        {
          EObject _event_5 = e.getEvent();
          EList<Stm> _statements_2 = ((EventStm) _event_5).getStatements();
          for(final Stm sta_2 : _statements_2) {
            {
              Statement _statement = sta_2.getStatement();
              if ((_statement instanceof GetProcess)) {
                _builder.append("\t");
                Statement _statement_1 = sta_2.getStatement();
                CharSequence _dispatchStatementForSimulation = Statements.dispatchStatementForSimulation(((GetProcess) _statement_1));
                _builder.append(_dispatchStatementForSimulation, "\t");
                _builder.newLineIfNotEmpty();
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
  
  public static CharSequence genEventNew(final EventDef e, final SchedulerDef sch) {
    StringConcatenation _builder = new StringConcatenation();
    {
      String _parent = sch.getParent();
      boolean _equals = Objects.equal(_parent, null);
      if (_equals) {
        _builder.append("public SchedulerProcess new_process(String procName) throws ValidationException {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("int index = 0 ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("for (String pName : SchedulerObject.processList) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("if (pName.equals(procName)) {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("if (!SchedulerObject.processInScheduler[index]) //  && !processInModel[index]) //found suitable index");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("break ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}\t\t\t\t");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("index ++ ;\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (index >= SchedulerObject.processList.size()) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("SchedulerObject.processList.add(procName) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (index > 255) { //SchedulerObject.processList full, must be replaced");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("for (int i = 0 ; i < 255 ; i ++) {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("if ( SchedulerObject.processList.get(i).equals(\"\")) { //slot is free");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("index = i ;");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("SchedulerObject.processList.set(index, procName) ;");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("break ;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("try {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("new_process(procName, index, null) ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("SchedulerObject.processInScheduler[index] = true ;\t");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("newP = (byte) index ;\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("} catch (ValidationException e) {\t\t");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("e.printStackTrace();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return new_process(procName, -1, null) ;");
        _builder.newLine();
        _builder.append("}\t\t\t");
        _builder.newLine();
        _builder.append("public SchedulerProcess new_process(String procName, int processID, ArrayList<String> para) throws ValidationException {\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("//Util.println(\"--> new_process(\" + procName + \")\") ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("SchedulerProcess new_process_target_process = new SchedulerProcess();\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (processID >= 0) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("//target_process.processID = (byte) processID ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("new_process_target_process.processID = processID ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("while (pcnt.size() < processID + 1) pcnt.add((byte) 0) ;\t\t\t\t");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("pcnt.set(processID, (byte) (pcnt.get(processID) + 1));");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("return null ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}\t\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("new_process_target_process.initProcess(procName, para) ;\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("config_new_process(new_process_target_process) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return new_process_target_process ;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("public void config_new_process (SchedulerProcess ");
    scheduling.dsl.Process _processname = e.getProcessname();
    String _name = _processname.getName();
    _builder.append(_name, "");
    _builder.append(") throws ValidationException {\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("//defined by new_process event handler\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//initProcessList = ");
    _builder.append(Data.initProcessList = false, "\t");
    _builder.append(" - ?");
    _builder.newLineIfNotEmpty();
    {
      EObject _event = e.getEvent();
      if ((_event instanceof EventStm)) {
        {
          EObject _event_1 = e.getEvent();
          EList<Stm> _statements = ((EventStm) _event_1).getStatements();
          for(final Stm sta : _statements) {
            _builder.append("\t");
            scheduling.dsl.Process _processname_1 = e.getProcessname();
            String _name_1 = _processname_1.getName();
            CharSequence _genStm = Statements.genStm(sta, _name_1);
            _builder.append(_genStm, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      } else {
        _builder.append("\t");
        EObject _event_2 = e.getEvent();
        EList<Opt> _opt = ((EventOpt) _event_2).getOpt();
        final int numopt = _opt.size();
        _builder.append(" ");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("//_schnumopt = ");
        _builder.append(numopt, "\t");
        _builder.append(" ;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("switch (_opt[_schselopt][0]) { //new");
        _builder.newLine();
        {
          ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, numopt, true);
          for(final Integer i : _doubleDotLessThan) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("case ");
            _builder.append(i, "\t\t");
            _builder.append(" : {");
            _builder.newLineIfNotEmpty();
            {
              EObject _event_3 = e.getEvent();
              EList<Opt> _opt_1 = ((EventOpt) _event_3).getOpt();
              Opt _get = _opt_1.get((i).intValue());
              EventStm _eventstm = _get.getEventstm();
              EList<Stm> _statements_1 = _eventstm.getStatements();
              for(final Stm sta_1 : _statements_1) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                scheduling.dsl.Process _processname_2 = e.getProcessname();
                String _name_2 = _processname_2.getName();
                CharSequence _genStm_1 = Statements.genStm(sta_1, _name_2);
                _builder.append(_genStm_1, "\t\t\t");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("break ;");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("/*if (! hasGenTemplate) {\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if (running_process == null) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("select_process(-1) ;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("} selecting is done by clock event*/\t");
    _builder.newLine();
    _builder.append("}\t\t\t\t");
    _builder.newLine();
    _builder.append("public void addProcessList(ArrayList<SchedulerProcess> procList) throws ValidationException { //called by init_order");
    _builder.newLine();
    _builder.append("\t");
    HashSet<String> collectionlist = Statements.getProcessCollectionFromEvent(e);
    _builder.newLineIfNotEmpty();
    {
      int _size = collectionlist.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        {
          for(final String col : collectionlist) {
            _builder.append("\t");
            _builder.append("ArrayList<SchedulerProcess> AL_");
            _builder.append(col, "\t");
            _builder.append(" = new ArrayList<SchedulerProcess>() ;");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("for (SchedulerProcess ");
        scheduling.dsl.Process _processname_3 = e.getProcessname();
        String _name_3 = _processname_3.getName();
        _builder.append(_name_3, "\t");
        _builder.append(" : procList) {\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("//initProcessList = ");
        _builder.append(Data.initProcessList = true, "\t\t");
        _builder.append(" this value for managing initial order of process ~ put process in collection\t\t\t");
        _builder.newLineIfNotEmpty();
        {
          EObject _event_4 = e.getEvent();
          if ((_event_4 instanceof EventStm)) {
            {
              EObject _event_5 = e.getEvent();
              EList<Stm> _statements_2 = ((EventStm) _event_5).getStatements();
              for(final Stm sta_2 : _statements_2) {
                _builder.append("\t");
                _builder.append("\t");
                scheduling.dsl.Process _processname_4 = e.getProcessname();
                String _name_4 = _processname_4.getName();
                CharSequence _genStm_2 = Statements.genStm(sta_2, _name_4);
                _builder.append(_genStm_2, "\t\t");
                _builder.append("\t\t\t\t\t\t\t");
                _builder.newLineIfNotEmpty();
              }
            }
          } else {
            _builder.append("\t");
            _builder.append("\t");
            EObject _event_6 = e.getEvent();
            EList<Opt> _opt_2 = ((EventOpt) _event_6).getOpt();
            final int numopt_1 = _opt_2.size();
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("//_schnumopt = ");
            _builder.append(numopt_1, "\t\t");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("switch (_opt[_schselopt][0]) { //new");
            _builder.newLine();
            {
              ExclusiveRange _doubleDotLessThan_1 = new ExclusiveRange(0, numopt_1, true);
              for(final Integer i_1 : _doubleDotLessThan_1) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("case ");
                _builder.append(i_1, "\t\t\t");
                _builder.append(" : {");
                _builder.newLineIfNotEmpty();
                {
                  EObject _event_7 = e.getEvent();
                  EList<Opt> _opt_3 = ((EventOpt) _event_7).getOpt();
                  Opt _get_1 = _opt_3.get((i_1).intValue());
                  EventStm _eventstm_1 = _get_1.getEventstm();
                  EList<Stm> _statements_3 = _eventstm_1.getStatements();
                  for(final Stm sta_3 : _statements_3) {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t");
                    scheduling.dsl.Process _processname_5 = e.getProcessname();
                    String _name_5 = _processname_5.getName();
                    CharSequence _genStm_3 = Statements.genStm(sta_3, _name_5);
                    _builder.append(_genStm_3, "\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                  }
                }
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("break ;");
                _builder.newLine();
              }
            }
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("//initProcessList = ");
        _builder.append(Data.initProcessList = false, "\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("initprocesslist.add(getInstance(");
        scheduling.dsl.Process _processname_6 = e.getProcessname();
        String _name_6 = _processname_6.getName();
        _builder.append(_name_6, "\t\t");
        _builder.append(")) ;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        {
          for(final String col_1 : collectionlist) {
            _builder.append("\t");
            _builder.append("if (!AL_");
            _builder.append(col_1, "\t");
            _builder.append(".isEmpty() )\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append(col_1, "\t\t");
            _builder.append(".put(AL_");
            _builder.append(col_1, "\t\t");
            _builder.append(") ;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}\t\t");
    _builder.newLine();
    return _builder;
  }
  
  public static ArrayList<String> getProcessCollectionFromHandler(final EventDef e) {
    ArrayList<String> result = new ArrayList<String>();
    return result;
  }
  
  public static CharSequence genEventClock(final EventDef e, final ProcessDSL procModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void clock() throws ValidationException{");
    _builder.newLine();
    {
      EObject _event = e.getEvent();
      if ((_event instanceof EventStm)) {
        {
          EObject _event_1 = e.getEvent();
          EList<Stm> _statements = ((EventStm) _event_1).getStatements();
          boolean _isEmpty = _statements.isEmpty();
          boolean _not = (!_isEmpty);
          if (_not) {
            _builder.append("\t");
            _builder.append("//hasClockEventHandler = ");
            _builder.append(Data.hasClockEventHandler = true, "\t");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("//hasClockEventHandler = ");
            _builder.append(Data.hasClockEventHandler = false, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("inc_time() ; //increase all clock including the running clock (_time_count)");
        _builder.newLine();
        {
          EObject _event_2 = e.getEvent();
          EList<Stm> _statements_1 = ((EventStm) _event_2).getStatements();
          for(final Stm sta : _statements_1) {
            _builder.append("\t");
            CharSequence _genStm = Statements.genStm(sta, "");
            _builder.append(_genStm, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      } else {
        _builder.append("\t");
        EObject _event_3 = e.getEvent();
        EList<Opt> _opt = ((EventOpt) _event_3).getOpt();
        final int numopt = _opt.size();
        _builder.append(" ");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("//_schnumopt = ");
        _builder.append(numopt, "\t");
        _builder.append(" ;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("switch (_opt[_schselopt][2]) { //clock");
        _builder.newLine();
        {
          ExclusiveRange _doubleDotLessThan = new ExclusiveRange(0, numopt, true);
          for(final Integer i : _doubleDotLessThan) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("case ");
            _builder.append(i, "\t\t");
            _builder.append(" : {");
            _builder.newLineIfNotEmpty();
            {
              EObject _event_4 = e.getEvent();
              EList<Opt> _opt_1 = ((EventOpt) _event_4).getOpt();
              Opt _get = _opt_1.get((i).intValue());
              EventStm _eventstm = _get.getEventstm();
              EList<Stm> _statements_2 = _eventstm.getStatements();
              boolean _isEmpty_1 = _statements_2.isEmpty();
              boolean _not_1 = (!_isEmpty_1);
              if (_not_1) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("//hasClockEventHandler = ");
                _builder.append(Data.hasClockEventHandler = true, "\t\t\t");
                _builder.newLineIfNotEmpty();
              } else {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("//hasClockEventHandler = ");
                _builder.append(Data.hasClockEventHandler = false, "\t\t\t");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("inc_time() ; //increase all clock including the running clock (_time_count)");
            _builder.newLine();
            {
              EObject _event_5 = e.getEvent();
              EList<Opt> _opt_2 = ((EventOpt) _event_5).getOpt();
              Opt _get_1 = _opt_2.get((i).intValue());
              EventStm _eventstm_1 = _get_1.getEventstm();
              EList<Stm> _statements_3 = _eventstm_1.getStatements();
              for(final Stm sta_1 : _statements_3) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                CharSequence _genStm_1 = Statements.genStm(sta_1, "");
                _builder.append(_genStm_1, "\t\t\t");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("break ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}\t\t\t\t\t\t");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("check_running_time_to_put_running_process() ; //end the time slice");
    _builder.newLine();
    {
      DefCore _defcore = Data.schModel.getDefcore();
      boolean _notEquals = (!Objects.equal(_defcore, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("if (_runningSets[current_core] != null) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("_runningSets[current_core].clear();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("if (_runningSet != null) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("_runningSet.clear();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}\t");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    CharSequence _genClockEventForPeriodicProcess = Handler.genClockEventForPeriodicProcess(procModel);
    _builder.append(_genClockEventForPeriodicProcess, "\t");
    _builder.append("\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("if (! hasGenTemplate) {\t\t\t\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if (running_process == null) {");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("if (select_process(-1) < 0) {");
    _builder.newLine();
    _builder.append("\t\t\t\t");
    _builder.append("//Util.println(\"No running process\");");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genClockEventForPeriodicProcess(final ProcessDSL procModel) {
    StringConcatenation _builder = new StringConcatenation();
    {
      ProcessConfig _processconfig = procModel.getProcessconfig();
      boolean _notEquals = (!Objects.equal(_processconfig, null));
      if (_notEquals) {
        {
          ProcessConfig _processconfig_1 = procModel.getProcessconfig();
          EList<ConfigProcess> _procinit = _processconfig_1.getProcinit();
          for(final ConfigProcess proc : _procinit) {
            {
              PeriodicProcess _periodic = proc.getPeriodic();
              boolean _notEquals_1 = (!Objects.equal(_periodic, null));
              if (_notEquals_1) {
                PeriodicProcess _periodic_1 = proc.getPeriodic();
                String _newProcessFunction = Handler.getNewProcessFunction(_periodic_1, procModel);
                _builder.append(_newProcessFunction, "");
                _builder.append("\t\t\t\t\t\t");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
        _builder.append("//reset periodic clock");
        _builder.newLine();
        {
          Set<String> _keySet = Data.periodicClockProperties.keySet();
          for(final String pClock : _keySet) {
            _builder.append("if (");
            _builder.append(pClock, "");
            _builder.append(" == ");
            String _get = Data.periodicClockProperties.get(pClock);
            _builder.append(_get, "");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append(pClock, "\t");
            _builder.append(" = -1 ;\t\t\t\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  public static String getNewProcessFunction(final PeriodicProcess periodic, final ProcessDSL procModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//periodic process\t");
    _builder.newLine();
    _builder.append("if (");
    String _periodicClockVariable = Data.getPeriodicClockVariable(periodic);
    _builder.append(_periodicClockVariable, "");
    _builder.append(" == 0) { //period ");
    String _periodicClockVariable_1 = Data.getPeriodicClockVariable(periodic);
    String _get = Data.periodicClockProperties.get(_periodicClockVariable_1);
    _builder.append(_get, "");
    _builder.append("){\t\t\t \t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("//get the model for executing new process");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("SchedulerPanModel panmodel = (SchedulerPanModel) SchedulerPromelaModel.panmodel ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//execute new periodic process\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("PromelaProcess proc = panmodel.new ");
    Element _element = periodic.getElement();
    scheduling.dsl.Process _process = _element.getProcess();
    String _name = _process.getName();
    _builder.append(_name, "\t");
    _builder.append("_0() ;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("int id = SchedulerPanModel.panmodel.newProcess(proc, ");
    int _max = periodic.getMax();
    _builder.append(_max, "\t");
    _builder.append(", \"");
    Element _element_1 = periodic.getElement();
    String _periodicProcessKey = Handler.getPeriodicProcessKey(_element_1, procModel);
    _builder.append(_periodicProcessKey, "\t");
    _builder.append("\");");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (id >= 0 ) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("ArrayList<String> para = new ArrayList<String>() ;");
    _builder.newLine();
    {
      Element _element_2 = periodic.getElement();
      EList<Value> _paraAssign = _element_2.getParaAssign();
      boolean _notEquals = (!Objects.equal(_paraAssign, null));
      if (_notEquals) {
        {
          Element _element_3 = periodic.getElement();
          EList<Value> _paraAssign_1 = _element_3.getParaAssign();
          for(final Value par : _paraAssign_1) {
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
                _builder.append("\t\t");
                _builder.append("para.add(\"");
                BoolValue _bool = par.getBool();
                String _value_1 = _bool.getValue();
                _builder.append(_value_1, "\t\t");
                _builder.append("\") ;");
                _builder.newLineIfNotEmpty();
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
    _builder.append("//throw new ValidationException(\"Can not execute new process ");
    Element _element_4 = periodic.getElement();
    String _periodicProcessKey_1 = Handler.getPeriodicProcessKey(_element_4, procModel);
    _builder.append(_periodicProcessKey_1, "\t\t");
    _builder.append("\") ;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("Util.println(\"Can not execute new process ");
    Element _element_5 = periodic.getElement();
    String _periodicProcessKey_2 = Handler.getPeriodicProcessKey(_element_5, procModel);
    _builder.append(_periodicProcessKey_2, "\t\t");
    _builder.append(" - Number of processes reaches limit\") ;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t\t");
    _builder.append("Config.processLimit = true ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\t\t\t");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static String getPeriodicProcessKey(final Element element, final ProcessDSL procModel) {
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
    String result = _process_2.getName();
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
            String _plus = ("_" + Integer.valueOf(_value));
            result = (_result + _plus);
          } else {
            String _result_1 = result;
            BoolValue _bool = value.getBool();
            String _value_1 = _bool.getValue();
            String _plus_1 = ("_" + _value_1);
            result = (_result_1 + _plus_1);
          }
        }
      }
    }
    String _result = result;
    result = (_result + "_0");
    return result;
  }
}
