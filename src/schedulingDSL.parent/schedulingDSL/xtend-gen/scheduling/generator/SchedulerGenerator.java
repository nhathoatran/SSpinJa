package scheduling.generator;

import com.google.common.base.Objects;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.nodemodel.ICompositeNode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.xbase.lib.Conversions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IntegerRange;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import scheduling.dsl.BoolValue;
import scheduling.dsl.CTL_AT;
import scheduling.dsl.ComparationName;
import scheduling.dsl.DataBlockDef;
import scheduling.dsl.DataDef;
import scheduling.dsl.DataSingleDef;
import scheduling.dsl.DefCore;
import scheduling.dsl.Element;
import scheduling.dsl.EventDef;
import scheduling.dsl.EventOpt;
import scheduling.dsl.Expression;
import scheduling.dsl.Generate;
import scheduling.dsl.HandlerDef;
import scheduling.dsl.IfDef;
import scheduling.dsl.LTE;
import scheduling.dsl.ND_Behavior;
import scheduling.dsl.NumValue;
import scheduling.dsl.Opt;
import scheduling.dsl.ParameterAssign;
import scheduling.dsl.ParameterList;
import scheduling.dsl.ParameterName;
import scheduling.dsl.ProcessDSL;
import scheduling.dsl.ProcessDef;
import scheduling.dsl.ProcessGeneration;
import scheduling.dsl.ProcessInit;
import scheduling.dsl.ProcessPropertyName;
import scheduling.dsl.PropertyAssignment;
import scheduling.dsl.RTCTL;
import scheduling.dsl.SchedulerCollectionDef;
import scheduling.dsl.SchedulerDSL;
import scheduling.dsl.SchedulerDataDef;
import scheduling.dsl.SchedulerDef;
import scheduling.dsl.SchedulerInit;
import scheduling.dsl.SchedulerPropertyDef;
import scheduling.dsl.SchedulerPropertyName;
import scheduling.dsl.SchedulerSet;
import scheduling.dsl.Statement;
import scheduling.dsl.StepGeneration;
import scheduling.dsl.Template;
import scheduling.dsl.Value;
import scheduling.dsl.VarBlockDef;
import scheduling.dsl.VarDecl;
import scheduling.dsl.VarDef;
import scheduling.dsl.VarDefinition;
import scheduling.dsl.VarName;
import scheduling.dsl.VarSingleDef;
import scheduling.dsl.Verify;
import scheduling.generator.Data;
import scheduling.generator.DataStructureGenerator;
import scheduling.generator.Handler;
import scheduling.generator.Interface;
import scheduling.generator.ProcessGenerator;
import scheduling.generator.Statements;
import scheduling.generator.Utilities;

@SuppressWarnings("all")
public class SchedulerGenerator {
  public static CharSequence SchedulerStatetoJavaCode(final SchedulerDSL schModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package sspinja.scheduling;");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.newLine();
    _builder.append("import java.util.ArrayList;");
    _builder.newLine();
    _builder.append("import spinja.model.Transition;");
    _builder.newLine();
    _builder.append("import sspinja.GenerateCode;\t");
    _builder.newLine();
    _builder.append("import sspinja.scheduler.promela.model.SchedulerPromelaModel;\t");
    _builder.newLine();
    _builder.append("\t\t\t");
    _builder.newLine();
    _builder.append("//Scheduler converter: Automatic generation");
    _builder.newLine();
    {
      Verify _verify = schModel.getVerify();
      boolean _notEquals = (!Objects.equal(_verify, null));
      if (_notEquals) {
        _builder.append("public class SchedulerState {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<SchedulerState> father = new ArrayList<SchedulerState>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<Transition> apptrans = new ArrayList<Transition>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<GenerateCode> generatecodes = new ArrayList<GenerateCode>() ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public boolean ndpoint = SchedulerPromelaModel.scheduler.checkNDpoint();");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public int identifier = -1;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public int prunid = -1;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public int execid = -1;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public int runinstance = 0;");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public boolean results[];");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public boolean checked[];");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public boolean duplicate = false ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<SchedulerState> next = new ArrayList<SchedulerState>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<Transition> trans = new ArrayList<Transition>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<Integer> core = new ArrayList<Integer>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<Integer> newID = new ArrayList<Integer>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<Integer> endID = new ArrayList<Integer>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("//public ArrayList<Integer> pID = new ArrayList<Integer>();");
        _builder.newLine();
        _builder.append("\t\t\t\t\t");
        _builder.newLine();
        {
          for(final String check : Data.checkPointsList) {
            _builder.append("\t");
            _builder.append("public boolean ");
            _builder.append(check, "\t");
            _builder.append(" = false ; //check point\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("public int ");
            _builder.append(check, "\t");
            _builder.append("_chkmin = -2; //no occurence ");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("public int ");
            _builder.append(check, "\t");
            _builder.append("_chkmax = -2; //no occurence");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public GenerateCode getGenerateCode(int id) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("int index = getChildStateIndex(id) ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("if (index != -1)");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("return generatecodes.get(index);");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("else");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("return null ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("private int getChildStateIndex(int identifier) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("int index = -1 ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("for (SchedulerState state : next) {\t\t\t");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("if (state.identifier == identifier) {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("index++; break ;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("return index;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public void update(int depth, boolean isCycle, boolean isCurrentState) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("if (isCurrentState) { //for checkpoint");
        _builder.newLine();
        {
          for(final String check_1 : Data.checkPointsList) {
            _builder.append("\t\t\t");
            _builder.append("if (SchedulerPanModel.scheduler.");
            _builder.append(check_1, "\t\t\t");
            _builder.append(") { //check point");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t");
            _builder.append("this.");
            _builder.append(check_1, "\t\t\t\t");
            _builder.append(" = true ; //change only for current state");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t");
            _builder.append("if (isCycle) {");
            _builder.newLine();
            _builder.append("\t\t\t");
            _builder.append("\t\t");
            _builder.append("this.");
            _builder.append(check_1, "\t\t\t\t\t");
            _builder.append("_chkmax = -1; //loop");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t\t");
            _builder.append("SchedulerPanModel.scheduler.");
            _builder.append(check_1, "\t\t\t\t\t");
            _builder.append("_max = -1;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t");
            _builder.append("} else {\t\t\t\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t\t\t");
            _builder.append("\t\t");
            _builder.append("if (this.");
            _builder.append(check_1, "\t\t\t\t\t");
            _builder.append("_chkmin > depth || this.");
            _builder.append(check_1, "\t\t\t\t\t");
            _builder.append("_chkmin == -2) //-2 : no occurence");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t\t\t");
            _builder.append("this.");
            _builder.append(check_1, "\t\t\t\t\t\t");
            _builder.append("_chkmin = depth ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t\t");
            _builder.append("if (this.");
            _builder.append(check_1, "\t\t\t\t\t");
            _builder.append("_chkmax < depth && this.");
            _builder.append(check_1, "\t\t\t\t\t");
            _builder.append("_chkmax != -1) //-1 : occurents infinitely");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t\t\t");
            _builder.append("this.");
            _builder.append(check_1, "\t\t\t\t\t\t");
            _builder.append("_chkmax = depth ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t\t\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
        _builder.append("\t\t");
        _builder.append("} else {");
        _builder.newLine();
        {
          for(final String check_2 : Data.checkPointsList) {
            _builder.append("\t\t\t");
            _builder.append("if (this.");
            _builder.append(check_2, "\t\t\t");
            _builder.append(") { //check point");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t");
            _builder.append("if (isCycle) {");
            _builder.newLine();
            _builder.append("\t\t\t");
            _builder.append("\t\t");
            _builder.append("this.");
            _builder.append(check_2, "\t\t\t\t\t");
            _builder.append("_chkmax = -1; //loop");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t\t");
            _builder.append("SchedulerPanModel.scheduler.");
            _builder.append(check_2, "\t\t\t\t\t");
            _builder.append("_max = -1;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t");
            _builder.append("} else {");
            _builder.newLine();
            _builder.append("\t\t\t");
            _builder.append("\t\t");
            _builder.append("if (this.");
            _builder.append(check_2, "\t\t\t\t\t");
            _builder.append("_chkmin > depth)");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t\t\t");
            _builder.append("this.");
            _builder.append(check_2, "\t\t\t\t\t\t");
            _builder.append("_chkmin = depth ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t\t");
            _builder.append("if (this.");
            _builder.append(check_2, "\t\t\t\t\t");
            _builder.append("_chkmax < depth && this.");
            _builder.append(check_2, "\t\t\t\t\t");
            _builder.append("_chkmax != -1) //-1 : occurents infinitely");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t\t\t");
            _builder.append("this.");
            _builder.append(check_2, "\t\t\t\t\t\t");
            _builder.append("_chkmax = depth ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t\t\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public void print(ArrayList<SchedulerState> schStateList, String pref, boolean fulltrans) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("if (!schStateList.contains(this)) schStateList.add(this) ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("System.out.println(pref + \"[\" + identifier ");
        {
          Verify _verify_1 = schModel.getVerify();
          RTCTL _formula = _verify_1.getFormula();
          int _RTCTLformulaLength = SchedulerGenerator.RTCTLformulaLength(_formula);
          int _minus = (_RTCTLformulaLength - 1);
          IntegerRange _upTo = new IntegerRange(0, _minus);
          for(final Integer i : _upTo) {
            _builder.append(" + \", results[");
            _builder.append(i, "\t\t");
            _builder.append("]=\" + results[");
            _builder.append(i, "\t\t");
            _builder.append("]");
          }
        }
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        {
          for(final String check_3 : Data.checkPointsList) {
            _builder.append(" + \", (");
            _builder.append(check_3, "\t\t\t");
            _builder.append(",min,max)=(\" + ");
            _builder.append(check_3, "\t\t\t");
            _builder.append(" + \",\" + ");
            _builder.append(check_3, "\t\t\t");
            _builder.append("_chkmin + \",\" + ");
            _builder.append(check_3, "\t\t\t");
            _builder.append("_chkmax + \")\" ");
          }
        }
        _builder.append(" + \"]\" );");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("System.out.println(pref + \"[\" + identifier ");
        {
          Verify _verify_2 = schModel.getVerify();
          RTCTL _formula_1 = _verify_2.getFormula();
          int _RTCTLformulaLength_1 = SchedulerGenerator.RTCTLformulaLength(_formula_1);
          int _minus_1 = (_RTCTLformulaLength_1 - 1);
          IntegerRange _upTo_1 = new IntegerRange(0, _minus_1);
          for(final Integer i_1 : _upTo_1) {
            _builder.append(" + \", checked[");
            _builder.append(i_1, "\t\t");
            _builder.append("]=\" + checked[");
            _builder.append(i_1, "\t\t");
            _builder.append("]");
          }
        }
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        {
          for(final String check_4 : Data.checkPointsList) {
            _builder.append(" + \", (");
            _builder.append(check_4, "\t\t\t");
            _builder.append(",min,max)=(\" + ");
            _builder.append(check_4, "\t\t\t");
            _builder.append(" + \",\" + ");
            _builder.append(check_4, "\t\t\t");
            _builder.append("_chkmin + \",\" + ");
            _builder.append(check_4, "\t\t\t");
            _builder.append("_chkmax + \")\" ");
          }
        }
        _builder.append(" + \"]\" );");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("for (SchedulerState child : next) System.out.print(\",\" + child.identifier);");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("System.out.println(\"\");");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("int index = 0 ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("for (SchedulerState s : next) {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("if (!schStateList.contains(s)) {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("if (fulltrans) {");
        _builder.newLine();
        _builder.append("\t\t\t\t\t");
        _builder.append("s.print(schStateList, pref + trans.get(index++) + \"-->\", fulltrans);");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("\t\t\t\t\t");
        _builder.append("s.print(schStateList, pref + \"-->\", fulltrans);");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("}");
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
        _builder.append("public void print(String pref) {\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("System.out.println(pref + \"[\" + identifier ");
        {
          Verify _verify_3 = schModel.getVerify();
          RTCTL _formula_2 = _verify_3.getFormula();
          int _RTCTLformulaLength_2 = SchedulerGenerator.RTCTLformulaLength(_formula_2);
          int _minus_2 = (_RTCTLformulaLength_2 - 1);
          IntegerRange _upTo_2 = new IntegerRange(0, _minus_2);
          for(final Integer i_2 : _upTo_2) {
            _builder.append(" + \", results[");
            _builder.append(i_2, "\t\t");
            _builder.append("]=\" + results[");
            _builder.append(i_2, "\t\t");
            _builder.append("]");
          }
        }
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        {
          for(final String check_5 : Data.checkPointsList) {
            _builder.append(" + \", (");
            _builder.append(check_5, "\t\t\t");
            _builder.append(",min,max)=(\" + ");
            _builder.append(check_5, "\t\t\t");
            _builder.append(" + \",\" + ");
            _builder.append(check_5, "\t\t\t");
            _builder.append("_chkmin + \",\" + ");
            _builder.append(check_5, "\t\t\t");
            _builder.append("_chkmax + \")\" ");
          }
        }
        _builder.append(" + \"]\" );");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("int index = 0 ;\t\t");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("for (SchedulerState s : next) {\t\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("s.print(pref + trans.get(index++) + \"-->\");");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public void print() {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("System.out.println(\"[\" + identifier ");
        {
          Verify _verify_4 = schModel.getVerify();
          RTCTL _formula_3 = _verify_4.getFormula();
          int _RTCTLformulaLength_3 = SchedulerGenerator.RTCTLformulaLength(_formula_3);
          int _minus_3 = (_RTCTLformulaLength_3 - 1);
          IntegerRange _upTo_3 = new IntegerRange(0, _minus_3);
          for(final Integer i_3 : _upTo_3) {
            _builder.append(" + \", results[");
            _builder.append(i_3, "\t\t");
            _builder.append("]=\" + results[");
            _builder.append(i_3, "\t\t");
            _builder.append("]");
          }
        }
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t\t\t\t\t\t");
        {
          for(final String check_6 : Data.checkPointsList) {
            _builder.append(" + \", (");
            _builder.append(check_6, "\t\t\t\t\t\t\t\t");
            _builder.append(",min,max)=(\" + ");
            _builder.append(check_6, "\t\t\t\t\t\t\t\t");
            _builder.append(" + \",\" + ");
            _builder.append(check_6, "\t\t\t\t\t\t\t\t");
            _builder.append("_chkmin + \",\" + ");
            _builder.append(check_6, "\t\t\t\t\t\t\t\t");
            _builder.append("_chkmax + \")\" ");
          }
        }
        _builder.append(" + \"]\" );");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public String toString() {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("String s = \"[\" + identifier ");
        {
          Verify _verify_5 = schModel.getVerify();
          RTCTL _formula_4 = _verify_5.getFormula();
          int _RTCTLformulaLength_4 = SchedulerGenerator.RTCTLformulaLength(_formula_4);
          int _minus_4 = (_RTCTLformulaLength_4 - 1);
          IntegerRange _upTo_4 = new IntegerRange(0, _minus_4);
          for(final Integer i_4 : _upTo_4) {
            _builder.append(" + \", results[");
            _builder.append(i_4, "\t\t");
            _builder.append("]=\" + results[");
            _builder.append(i_4, "\t\t");
            _builder.append("]");
          }
        }
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t\t\t\t\t\t\t\t\t\t\t");
        {
          for(final String check_7 : Data.checkPointsList) {
            _builder.append(" + \", (");
            _builder.append(check_7, "\t\t\t\t\t\t\t\t\t\t\t\t\t");
            _builder.append(",min,max)=(\" + ");
            _builder.append(check_7, "\t\t\t\t\t\t\t\t\t\t\t\t\t");
            _builder.append(" + \",\" + ");
            _builder.append(check_7, "\t\t\t\t\t\t\t\t\t\t\t\t\t");
            _builder.append("_chkmin + \",\" + ");
            _builder.append(check_7, "\t\t\t\t\t\t\t\t\t\t\t\t\t");
            _builder.append("_chkmax + \")\" ");
          }
        }
        _builder.append(" + \"]\" );");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("return s;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public void printchildlist() {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("System.out.print(\"Child: \");");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("for (SchedulerState child : next) {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("System.out.print(child.identifier + \", \");");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("System.out.println();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append("//Automatic generation");
        _builder.newLine();
        _builder.append("public class SchedulerState {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<SchedulerState> father = new ArrayList<SchedulerState>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<Transition> apptrans = new ArrayList<Transition>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<GenerateCode> generatecodes = new ArrayList<GenerateCode>() ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public boolean ndpoint = SchedulerPromelaModel.scheduler.checkNDpoint();");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public int identifier = -1;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public int prunid = -1;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public int execid = -1;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public int runinstance = 0;");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public boolean results[];");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public boolean checked[];");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public boolean duplicate = false ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<SchedulerState> next = new ArrayList<SchedulerState>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<Transition> trans = new ArrayList<Transition>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<Integer> newID = new ArrayList<Integer>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public ArrayList<Integer> endID = new ArrayList<Integer>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("//public ArrayList<Integer> pID = new ArrayList<Integer>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public GenerateCode getGenerateCode(int id) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("return null ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public void update(int depth, boolean isCycle, boolean isCurrentState) {}\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public void print(ArrayList<SchedulerState> schStateList, String pref, boolean fulltrans) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("if (!schStateList.contains(this)) schStateList.add(this) ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("System.out.println(pref + \"[\" + prunid+ \",\" + identifier + \"]\");");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("for (SchedulerState child : next) System.out.print(\",\" + child.identifier);");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("System.out.println(\"\");");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("int index = 0 ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("for (SchedulerState s : next) {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("if (!schStateList.contains(s)) {");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("if (fulltrans) {");
        _builder.newLine();
        _builder.append("\t\t\t\t\t");
        _builder.append("s.print(schStateList, pref + trans.get(index++) + \"-->\", fulltrans);");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("\t\t\t\t\t");
        _builder.append("s.print(schStateList, pref + \"-->\", fulltrans);");
        _builder.newLine();
        _builder.append("\t\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public void print(String pref) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("System.out.println(pref + \"[\" + prunid+ \",\" + identifier + \"]\");");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("int index = 0 ;\t\t");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("for (SchedulerState s : next) {\t\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("s.print(pref + trans.get(index++) + \"-->\");");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public void print() {}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public static int RTCTLformulaLength(final RTCTL formula) {
    String _op = formula.getOp();
    boolean _equals = _op.equals("or");
    if (_equals) {
      RTCTL _f1 = formula.getF1();
      int _RTCTLformulaLength = SchedulerGenerator.RTCTLformulaLength(_f1);
      int _plus = (1 + _RTCTLformulaLength);
      RTCTL _f2 = formula.getF2();
      int _RTCTLformulaLength_1 = SchedulerGenerator.RTCTLformulaLength(_f2);
      return (_plus + _RTCTLformulaLength_1);
    } else {
      String _op_1 = formula.getOp();
      boolean _equals_1 = _op_1.equals("implies");
      if (_equals_1) {
        RTCTL _f1_1 = formula.getF1();
        int _RTCTLformulaLength_2 = SchedulerGenerator.RTCTLformulaLength(_f1_1);
        int _plus_1 = (1 + _RTCTLformulaLength_2);
        RTCTL _f2_1 = formula.getF2();
        int _RTCTLformulaLength_3 = SchedulerGenerator.RTCTLformulaLength(_f2_1);
        return (_plus_1 + _RTCTLformulaLength_3);
      } else {
        Expression _exp = formula.getExp();
        boolean _notEquals = (!Objects.equal(_exp, null));
        if (_notEquals) {
          return 1;
        } else {
          if (((((((Objects.equal(formula.getOp(), "AX") || Objects.equal(formula.getOp(), "AF")) || Objects.equal(formula.getOp(), "AG")) || 
            Objects.equal(formula.getOp(), "EX")) || Objects.equal(formula.getOp(), "EF")) || Objects.equal(formula.getOp(), "EG")) || Objects.equal(formula.getOp(), "not"))) {
            RTCTL _f = formula.getF();
            int _RTCTLformulaLength_4 = SchedulerGenerator.RTCTLformulaLength(_f);
            return (1 + _RTCTLformulaLength_4);
          } else {
            RTCTL _f1_2 = formula.getF1();
            int _RTCTLformulaLength_5 = SchedulerGenerator.RTCTLformulaLength(_f1_2);
            int _plus_2 = (1 + _RTCTLformulaLength_5);
            RTCTL _f2_2 = formula.getF2();
            int _RTCTLformulaLength_6 = SchedulerGenerator.RTCTLformulaLength(_f2_2);
            return (_plus_2 + _RTCTLformulaLength_6);
          }
        }
      }
    }
  }
  
  public static CharSequence CTLFormulatoJavaCode(final SchedulerDSL schModel) {
    StringConcatenation _builder = new StringConcatenation();
    {
      Verify _verify = schModel.getVerify();
      boolean _notEquals = (!Objects.equal(_verify, null));
      if (_notEquals) {
        _builder.append("package sspinja.scheduling;");
        _builder.newLine();
        _builder.newLine();
        _builder.append("//Scheduler converter: Automatic generation\t\t\t");
        _builder.newLine();
        _builder.append("public class CTLFormula {");
        _builder.newLine();
        _builder.append("\t");
        Verify verify = schModel.getVerify();
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        RTCTL formula = verify.getFormula();
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        int flength = SchedulerGenerator.RTCTLformulaLength(formula);
        _builder.newLineIfNotEmpty();
        {
          CTL_AT _at = verify.getAt();
          boolean _notEquals_1 = (!Objects.equal(_at, null));
          if (_notEquals_1) {
            _builder.append("\t");
            _builder.append("//-- When ");
            CTL_AT _at_1 = verify.getAt();
            Expression _cond = _at_1.getCond();
            String _dispatchExpression = Statements.dispatchExpression(_cond);
            _builder.append(_dispatchExpression, "\t");
            _builder.append(" : ");
            String _dispatchRTCTLExpression = Statements.dispatchRTCTLExpression(formula);
            _builder.append(_dispatchRTCTLExpression, "\t");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("//");
            String _dispatchRTCTLExpression_1 = Statements.dispatchRTCTLExpression(formula);
            _builder.append(_dispatchRTCTLExpression_1, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public int length = ");
        _builder.append(flength, "\t");
        _builder.append(" ;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("public String opcode[] = new String[");
        _builder.append(flength, "\t");
        _builder.append("];");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("public byte sf[][] = new byte[");
        _builder.append(flength, "\t");
        _builder.append("][2] ; //formula parameters");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("public byte sn[] = new byte[");
        _builder.append(flength, "\t");
        _builder.append("]; //formula superscripts");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public CTLFormula() {");
        _builder.newLine();
        _builder.append("\t\t");
        Verify _verify_1 = schModel.getVerify();
        RTCTL _formula = _verify_1.getFormula();
        CharSequence _printFormulaConstructor = SchedulerGenerator.printFormulaConstructor(_formula, 0);
        _builder.append(_printFormulaConstructor, "\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public String toString() {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("return \"");
        ICompositeNode _node = NodeModelUtils.getNode(verify);
        String _tokenText = NodeModelUtils.getTokenText(_node);
        String _replace = _tokenText.replace("\"", "");
        _builder.append(_replace, "\t\t");
        _builder.append("\" ;");
        _builder.newLineIfNotEmpty();
        {
          CTL_AT _at_2 = verify.getAt();
          boolean _notEquals_2 = (!Objects.equal(_at_2, null));
          if (_notEquals_2) {
            _builder.append("\t\t");
            _builder.append("//return \"When ");
            CTL_AT _at_3 = verify.getAt();
            Expression _cond_1 = _at_3.getCond();
            String _dispatchExpression_1 = Statements.dispatchExpression(_cond_1);
            String _replace_1 = _dispatchExpression_1.replace("\"", "");
            _builder.append(_replace_1, "\t\t");
            _builder.append(" : ");
            String _dispatchRTCTLExpression_2 = Statements.dispatchRTCTLExpression(formula);
            String _replace_2 = _dispatchRTCTLExpression_2.replace("\"", "");
            _builder.append(_replace_2, "\t\t");
            _builder.append("\" ;");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t\t");
            _builder.append("//return \"");
            String _dispatchRTCTLExpression_3 = Statements.dispatchRTCTLExpression(formula);
            String _replace_3 = _dispatchRTCTLExpression_3.replace("\"", "");
            _builder.append(_replace_3, "\t\t");
            _builder.append("\" ;");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append("package sspinja.scheduling;\t\t\t\t\t\t");
        _builder.newLine();
        _builder.newLine();
        _builder.append("//Scheduler converter: Automatic generation\t\t\t");
        _builder.newLine();
        _builder.append("public class CTLFormula {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public int length ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public String opcode[] ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public byte sf[][]; //formula parameters");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public byte sn[]; //formula superscripts");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public String toString() {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("return \"\" ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public static CharSequence printFormulaConstructor(final RTCTL formula, final int i) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//----------------------------\t");
    _builder.newLine();
    {
      String _op = formula.getOp();
      boolean _equals = _op.equals("or");
      if (_equals) {
        _builder.append("opcode[");
        _builder.append(i, "");
        _builder.append("] = \"");
        String _op_1 = formula.getOp();
        _builder.append(_op_1, "");
        _builder.append("\" ; //");
        String _dispatchRTCTLExpression = Statements.dispatchRTCTLExpression(formula);
        _builder.append(_dispatchRTCTLExpression, "");
        _builder.newLineIfNotEmpty();
        _builder.append("sn[");
        _builder.append(i, "");
        _builder.append("]= -1; //no superscript\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("sf[");
        _builder.append(i, "");
        _builder.append("][0] = ");
        _builder.append((i + 1), "");
        _builder.append(" ;");
        _builder.newLineIfNotEmpty();
        _builder.append("sf[");
        _builder.append(i, "");
        _builder.append("][1] = ");
        RTCTL _f1 = formula.getF1();
        int _RTCTLformulaLength = SchedulerGenerator.RTCTLformulaLength(_f1);
        int _plus = ((i + 1) + _RTCTLformulaLength);
        _builder.append(_plus, "");
        _builder.append(" ;\t\t\t\t");
        _builder.newLineIfNotEmpty();
        RTCTL _f1_1 = formula.getF1();
        Object _printFormulaConstructor = SchedulerGenerator.printFormulaConstructor(_f1_1, (i + 1));
        _builder.append(_printFormulaConstructor, "");
        _builder.append("\t\t\t\t");
        _builder.newLineIfNotEmpty();
        RTCTL _f2 = formula.getF2();
        RTCTL _f1_2 = formula.getF1();
        int _RTCTLformulaLength_1 = SchedulerGenerator.RTCTLformulaLength(_f1_2);
        int _plus_1 = ((i + 1) + _RTCTLformulaLength_1);
        Object _printFormulaConstructor_1 = SchedulerGenerator.printFormulaConstructor(_f2, _plus_1);
        _builder.append(_printFormulaConstructor_1, "");
        _builder.newLineIfNotEmpty();
      } else {
        {
          String _op_2 = formula.getOp();
          boolean _equals_1 = _op_2.equals("implies");
          if (_equals_1) {
            _builder.append("opcode[");
            _builder.append(i, "");
            _builder.append("] = \"");
            String _op_3 = formula.getOp();
            _builder.append(_op_3, "");
            _builder.append("\" ; //");
            String _dispatchRTCTLExpression_1 = Statements.dispatchRTCTLExpression(formula);
            _builder.append(_dispatchRTCTLExpression_1, "");
            _builder.newLineIfNotEmpty();
            _builder.append("sn[");
            _builder.append(i, "");
            _builder.append("]= -1; //no superscript\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("sf[");
            _builder.append(i, "");
            _builder.append("][0] = ");
            _builder.append((i + 1), "");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
            _builder.append("sf[");
            _builder.append(i, "");
            _builder.append("][1] = ");
            RTCTL _f1_3 = formula.getF1();
            int _RTCTLformulaLength_2 = SchedulerGenerator.RTCTLformulaLength(_f1_3);
            int _plus_2 = ((i + 1) + _RTCTLformulaLength_2);
            _builder.append(_plus_2, "");
            _builder.append(" ;\t\t\t\t");
            _builder.newLineIfNotEmpty();
            RTCTL _f1_4 = formula.getF1();
            Object _printFormulaConstructor_2 = SchedulerGenerator.printFormulaConstructor(_f1_4, (i + 1));
            _builder.append(_printFormulaConstructor_2, "");
            _builder.append("\t\t\t\t");
            _builder.newLineIfNotEmpty();
            RTCTL _f2_1 = formula.getF2();
            RTCTL _f1_5 = formula.getF1();
            int _RTCTLformulaLength_3 = SchedulerGenerator.RTCTLformulaLength(_f1_5);
            int _plus_3 = ((i + 1) + _RTCTLformulaLength_3);
            Object _printFormulaConstructor_3 = SchedulerGenerator.printFormulaConstructor(_f2_1, _plus_3);
            _builder.append(_printFormulaConstructor_3, "");
            _builder.newLineIfNotEmpty();
          } else {
            {
              Expression _exp = formula.getExp();
              boolean _notEquals = (!Objects.equal(_exp, null));
              if (_notEquals) {
                _builder.append("opcode[");
                _builder.append(i, "");
                _builder.append("] = \"atomic\" ; //");
                Expression _exp_1 = formula.getExp();
                String _dispatchExpression = Statements.dispatchExpression(_exp_1);
                _builder.append(_dispatchExpression, "");
                _builder.newLineIfNotEmpty();
                _builder.append("sn[");
                _builder.append(i, "");
                _builder.append("]= -1; //no superscript");
                _builder.newLineIfNotEmpty();
                _builder.append("sf[");
                _builder.append(i, "");
                _builder.append("][0] = ");
                _builder.append(i, "");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
              } else {
                {
                  if (((((((Objects.equal(formula.getOp(), "AX") || Objects.equal(formula.getOp(), "AF")) || Objects.equal(formula.getOp(), "AG")) || 
                    Objects.equal(formula.getOp(), "EX")) || Objects.equal(formula.getOp(), "EF")) || Objects.equal(formula.getOp(), "EG")) || Objects.equal(formula.getOp(), "not"))) {
                    _builder.append("opcode[");
                    _builder.append(i, "");
                    _builder.append("] = \"");
                    String _op_4 = formula.getOp();
                    _builder.append(_op_4, "");
                    _builder.append("\" ; //");
                    String _dispatchRTCTLExpression_2 = Statements.dispatchRTCTLExpression(formula);
                    _builder.append(_dispatchRTCTLExpression_2, "");
                    _builder.append(" ");
                    _builder.newLineIfNotEmpty();
                    {
                      LTE _lte = formula.getLte();
                      boolean _equals_2 = Objects.equal(_lte, null);
                      if (_equals_2) {
                        _builder.append("sn[");
                        _builder.append(i, "");
                        _builder.append("]= -1; //no superscript");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("sn[");
                        _builder.append(i, "");
                        _builder.append("]= ");
                        LTE _lte_1 = formula.getLte();
                        int _num = _lte_1.getNum();
                        _builder.append(_num, "");
                        _builder.append("; //superscript");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                    _builder.append("sf[");
                    _builder.append(i, "");
                    _builder.append("][0] = ");
                    _builder.append((i + 1), "");
                    _builder.append(" ;\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                    RTCTL _f = formula.getF();
                    Object _printFormulaConstructor_4 = SchedulerGenerator.printFormulaConstructor(_f, (i + 1));
                    _builder.append(_printFormulaConstructor_4, "");
                    _builder.append("\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                  }
                }
                {
                  if (((Objects.equal(formula.getOp(), "AU") || Objects.equal(formula.getOp(), "EU")) || Objects.equal(formula.getOp(), "or"))) {
                    _builder.append("opcode[");
                    _builder.append(i, "");
                    _builder.append("] = \"");
                    String _op_5 = formula.getOp();
                    _builder.append(_op_5, "");
                    _builder.append("\" ; //");
                    String _dispatchRTCTLExpression_3 = Statements.dispatchRTCTLExpression(formula);
                    _builder.append(_dispatchRTCTLExpression_3, "");
                    _builder.newLineIfNotEmpty();
                    {
                      LTE _lte_2 = formula.getLte();
                      boolean _equals_3 = Objects.equal(_lte_2, null);
                      if (_equals_3) {
                        _builder.append("sn[");
                        _builder.append(i, "");
                        _builder.append("]= -1; //no superscript");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("sn[");
                        _builder.append(i, "");
                        _builder.append("]= ");
                        LTE _lte_3 = formula.getLte();
                        int _num_1 = _lte_3.getNum();
                        _builder.append(_num_1, "");
                        _builder.append("; //superscript");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                    _builder.append("sf[");
                    _builder.append(i, "");
                    _builder.append("][0] = ");
                    _builder.append((i + 1), "");
                    _builder.append(" ;");
                    _builder.newLineIfNotEmpty();
                    _builder.append("sf[");
                    _builder.append(i, "");
                    _builder.append("][1] = ");
                    RTCTL _f1_6 = formula.getF1();
                    int _RTCTLformulaLength_4 = SchedulerGenerator.RTCTLformulaLength(_f1_6);
                    int _plus_4 = ((i + 1) + _RTCTLformulaLength_4);
                    _builder.append(_plus_4, "");
                    _builder.append(" ;\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                    RTCTL _f1_7 = formula.getF1();
                    Object _printFormulaConstructor_5 = SchedulerGenerator.printFormulaConstructor(_f1_7, (i + 1));
                    _builder.append(_printFormulaConstructor_5, "");
                    _builder.append("\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                    RTCTL _f2_2 = formula.getF2();
                    RTCTL _f1_8 = formula.getF1();
                    int _RTCTLformulaLength_5 = SchedulerGenerator.RTCTLformulaLength(_f1_8);
                    int _plus_5 = ((i + 1) + _RTCTLformulaLength_5);
                    Object _printFormulaConstructor_6 = SchedulerGenerator.printFormulaConstructor(_f2_2, _plus_5);
                    _builder.append(_printFormulaConstructor_6, "");
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
  
  public static Object printFormulaAtomicCheckResult(final RTCTL formula, final int i) {
    StringConcatenation _builder = new StringConcatenation();
    {
      Expression _exp = formula.getExp();
      boolean _notEquals = (!Objects.equal(_exp, null));
      if (_notEquals) {
        _builder.append("result[");
        _builder.append(i, "");
        _builder.append("] = ");
        Expression _exp_1 = formula.getExp();
        String _dispatchExpression = Statements.dispatchExpression(_exp_1);
        _builder.append(_dispatchExpression, "");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      } else {
        {
          RTCTL _f = formula.getF();
          boolean _notEquals_1 = (!Objects.equal(_f, null));
          if (_notEquals_1) {
            RTCTL _f_1 = formula.getF();
            Object _printFormulaAtomicCheckResult = SchedulerGenerator.printFormulaAtomicCheckResult(_f_1, (i + 1));
            _builder.append(_printFormulaAtomicCheckResult, "");
            _builder.newLineIfNotEmpty();
          } else {
            RTCTL _f1 = formula.getF1();
            Object _printFormulaAtomicCheckResult_1 = SchedulerGenerator.printFormulaAtomicCheckResult(_f1, (i + 1));
            _builder.append(_printFormulaAtomicCheckResult_1, "");
            _builder.newLineIfNotEmpty();
            RTCTL _f2 = formula.getF2();
            RTCTL _f1_1 = formula.getF1();
            int _RTCTLformulaLength = SchedulerGenerator.RTCTLformulaLength(_f1_1);
            int _plus = ((i + 1) + _RTCTLformulaLength);
            Object _printFormulaAtomicCheckResult_2 = SchedulerGenerator.printFormulaAtomicCheckResult(_f2, _plus);
            _builder.append(_printFormulaAtomicCheckResult_2, "");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  public static CharSequence declareSysVar(final RTCTL formula) {
    StringConcatenation _builder = new StringConcatenation();
    ArrayList<String> sysvar = new ArrayList<String>();
    _builder.append("\t");
    _builder.newLineIfNotEmpty();
    ArrayList<String> _variables = Statements.getVariables(sysvar, formula);
    ArrayList<String> _sysvar = sysvar = _variables;
    _builder.append(_sysvar, "");
    _builder.newLineIfNotEmpty();
    {
      for(final String v : sysvar) {
        _builder.append("String ");
        _builder.append(v, "");
        _builder.append(" = SchedulerPanModel.panmodel.sysGet(\"");
        _builder.append(v, "");
        _builder.append("\") ;");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public static CharSequence declareSysVar(final Expression expression) {
    StringConcatenation _builder = new StringConcatenation();
    ArrayList<String> sysvar = new ArrayList<String>();
    _builder.append("\t");
    _builder.newLineIfNotEmpty();
    ArrayList<String> _variables = Statements.getVariables(sysvar, expression);
    ArrayList<String> _sysvar = sysvar = _variables;
    _builder.append(_sysvar, "");
    _builder.newLineIfNotEmpty();
    {
      for(final String v : sysvar) {
        _builder.append("String ");
        _builder.append(v, "");
        _builder.append(" = SchedulerPanModel.panmodel.sysGet(\"");
        _builder.append(v, "");
        _builder.append("\") ;");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public static Object printInitAtomicf(final RTCTL formula, final int i) {
    StringConcatenation _builder = new StringConcatenation();
    {
      Expression _exp = formula.getExp();
      boolean _notEquals = (!Objects.equal(_exp, null));
      if (_notEquals) {
        _builder.append("{//RTCTL: ");
        String _dispatchRTCTLExpression = Statements.dispatchRTCTLExpression(formula);
        _builder.append(_dispatchRTCTLExpression, "");
        _builder.append(" //Exp: ");
        Expression _exp_1 = formula.getExp();
        String _dispatchExpression = Statements.dispatchExpression(_exp_1, false);
        _builder.append(_dispatchExpression, "");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        Expression _exp_2 = formula.getExp();
        Object _dispatchExpressionwithProcess = Statements.dispatchExpressionwithProcess(_exp_2, 0);
        _builder.append(_dispatchExpressionwithProcess, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("results[");
        _builder.append(i, "\t");
        _builder.append("] = result_0 ; ");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
      } else {
        {
          RTCTL _f = formula.getF();
          boolean _notEquals_1 = (!Objects.equal(_f, null));
          if (_notEquals_1) {
            RTCTL _f_1 = formula.getF();
            Object _printInitAtomicf = SchedulerGenerator.printInitAtomicf(_f_1, (i + 1));
            _builder.append(_printInitAtomicf, "");
            _builder.newLineIfNotEmpty();
          } else {
            RTCTL _f1 = formula.getF1();
            Object _printInitAtomicf_1 = SchedulerGenerator.printInitAtomicf(_f1, (i + 1));
            _builder.append(_printInitAtomicf_1, "");
            _builder.newLineIfNotEmpty();
            RTCTL _f2 = formula.getF2();
            RTCTL _f1_1 = formula.getF1();
            int _RTCTLformulaLength = SchedulerGenerator.RTCTLformulaLength(_f1_1);
            int _plus = ((i + 1) + _RTCTLformulaLength);
            Object _printInitAtomicf_2 = SchedulerGenerator.printInitAtomicf(_f2, _plus);
            _builder.append(_printInitAtomicf_2, "");
            _builder.newLineIfNotEmpty();
            {
              if ((formula.getOp().equals("or") || formula.getOp().equals("implies"))) {
                {
                  if ((((!Objects.equal(formula.getF1().getExp(), null)) && (!Objects.equal(formula.getF2().getExp(), null))) && formula.getOp().equals("or"))) {
                    _builder.append("results[");
                    _builder.append(i, "");
                    _builder.append("] = results[");
                    _builder.append((i + 1), "");
                    _builder.append("] || results[");
                    _builder.append((i + 2), "");
                    _builder.append("] ;");
                    _builder.newLineIfNotEmpty();
                  }
                }
                {
                  if ((((!Objects.equal(formula.getF1().getExp(), null)) && (!Objects.equal(formula.getF2().getExp(), null))) && formula.getOp().equals("implies"))) {
                    _builder.append("results[");
                    _builder.append(i, "");
                    _builder.append("] = !results[");
                    _builder.append((i + 1), "");
                    _builder.append("] || results[");
                    _builder.append((i + 2), "");
                    _builder.append("] ;");
                    _builder.newLineIfNotEmpty();
                  }
                }
              } else {
                RTCTL _f1_2 = formula.getF1();
                Object _printInitAtomicf_3 = SchedulerGenerator.printInitAtomicf(_f1_2, (i + 1));
                _builder.append(_printInitAtomicf_3, "");
                _builder.newLineIfNotEmpty();
                RTCTL _f2_1 = formula.getF2();
                RTCTL _f1_3 = formula.getF1();
                int _RTCTLformulaLength_1 = SchedulerGenerator.RTCTLformulaLength(_f1_3);
                int _plus_1 = ((i + 1) + _RTCTLformulaLength_1);
                Object _printInitAtomicf_4 = SchedulerGenerator.printInitAtomicf(_f2_1, _plus_1);
                _builder.append(_printInitAtomicf_4, "");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    return _builder;
  }
  
  public static Object printFormulaCheckedAtomic(final RTCTL formula, final int i) {
    StringConcatenation _builder = new StringConcatenation();
    {
      Expression _exp = formula.getExp();
      boolean _notEquals = (!Objects.equal(_exp, null));
      if (_notEquals) {
        _builder.append("checked[");
        _builder.append(i, "");
        _builder.append("] = true ; //");
        Expression _exp_1 = formula.getExp();
        String _dispatchExpression = Statements.dispatchExpression(_exp_1, false);
        _builder.append(_dispatchExpression, "");
        _builder.append(" ;");
        _builder.newLineIfNotEmpty();
      } else {
        {
          RTCTL _f = formula.getF();
          boolean _notEquals_1 = (!Objects.equal(_f, null));
          if (_notEquals_1) {
            RTCTL _f_1 = formula.getF();
            Object _printFormulaCheckedAtomic = SchedulerGenerator.printFormulaCheckedAtomic(_f_1, (i + 1));
            _builder.append(_printFormulaCheckedAtomic, "");
            _builder.newLineIfNotEmpty();
          } else {
            RTCTL _f1 = formula.getF1();
            Object _printFormulaCheckedAtomic_1 = SchedulerGenerator.printFormulaCheckedAtomic(_f1, (i + 1));
            _builder.append(_printFormulaCheckedAtomic_1, "");
            _builder.newLineIfNotEmpty();
            RTCTL _f2 = formula.getF2();
            RTCTL _f1_1 = formula.getF1();
            int _RTCTLformulaLength = SchedulerGenerator.RTCTLformulaLength(_f1_1);
            int _plus = ((i + 1) + _RTCTLformulaLength);
            Object _printFormulaCheckedAtomic_2 = SchedulerGenerator.printFormulaCheckedAtomic(_f2, _plus);
            _builder.append(_printFormulaCheckedAtomic_2, "");
            _builder.newLineIfNotEmpty();
            {
              if ((formula.getOp().equals("or") || formula.getOp().equals("implies"))) {
                {
                  if (((!Objects.equal(formula.getF1().getExp(), null)) && (!Objects.equal(formula.getF2().getExp(), null)))) {
                    _builder.append("checked[");
                    _builder.append(i, "");
                    _builder.append("] = true ; //");
                    RTCTL _f1_2 = formula.getF1();
                    String _dispatchRTCTLExpression = Statements.dispatchRTCTLExpression(_f1_2);
                    _builder.append(_dispatchRTCTLExpression, "");
                    _builder.append(" (or, implies) ");
                    RTCTL _f2_1 = formula.getF2();
                    String _dispatchRTCTLExpression_1 = Statements.dispatchRTCTLExpression(_f2_1);
                    _builder.append(_dispatchRTCTLExpression_1, "");
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
  
  public static CharSequence SchedulertoJavaCode(final ProcessDSL procModel, final SchedulerDSL schModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package sspinja.scheduling ;");
    _builder.newLine();
    _builder.append("//Automatic generation");
    _builder.newLine();
    _builder.append("public class SchedulerObject extends SchedulerObject_");
    SchedulerDef _scheduler = schModel.getScheduler();
    String _name = _scheduler.getName();
    _builder.append(_name, "");
    _builder.append(" { ");
    _builder.newLineIfNotEmpty();
    {
      SchedulerDef _scheduler_1 = schModel.getScheduler();
      Generate _gen = _scheduler_1.getGen();
      boolean _notEquals = (!Objects.equal(_gen, null));
      if (_notEquals) {
        {
          SchedulerDef _scheduler_2 = schModel.getScheduler();
          Generate _gen_1 = _scheduler_2.getGen();
          StepGeneration _stepgeneration = _gen_1.getStepgeneration();
          boolean _notEquals_1 = (!Objects.equal(_stepgeneration, null));
          if (_notEquals_1) {
            _builder.append("\t");
            _builder.append("public boolean checkNDpoint() {");
            _builder.newLine();
            {
              SchedulerDef _scheduler_3 = schModel.getScheduler();
              Generate _gen_2 = _scheduler_3.getGen();
              StepGeneration _stepgeneration_1 = _gen_2.getStepgeneration();
              Template _step = _stepgeneration_1.getStep();
              ND_Behavior _nD_behavior = _step.getND_behavior();
              boolean _notEquals_2 = (!Objects.equal(_nD_behavior, null));
              if (_notEquals_2) {
                {
                  SchedulerDef _scheduler_4 = schModel.getScheduler();
                  Generate _gen_3 = _scheduler_4.getGen();
                  StepGeneration _stepgeneration_2 = _gen_3.getStepgeneration();
                  Template _step_1 = _stepgeneration_2.getStep();
                  ND_Behavior _nD_behavior_1 = _step_1.getND_behavior();
                  Expression _cond = _nD_behavior_1.getCond();
                  boolean _notEquals_3 = (!Objects.equal(_cond, null));
                  if (_notEquals_3) {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("return ");
                    SchedulerDef _scheduler_5 = schModel.getScheduler();
                    Generate _gen_4 = _scheduler_5.getGen();
                    StepGeneration _stepgeneration_3 = _gen_4.getStepgeneration();
                    Template _step_2 = _stepgeneration_3.getStep();
                    ND_Behavior _nD_behavior_2 = _step_2.getND_behavior();
                    Expression _cond_1 = _nD_behavior_2.getCond();
                    String _dispatchExpression = Statements.dispatchExpression(_cond_1);
                    _builder.append(_dispatchExpression, "\t\t");
                    _builder.append(" ;");
                    _builder.newLineIfNotEmpty();
                  } else {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("return true ;");
                    _builder.newLine();
                  }
                }
              } else {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("return true ;");
                _builder.newLine();
              }
            }
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            {
              SchedulerDef _scheduler_6 = schModel.getScheduler();
              Generate _gen_5 = _scheduler_6.getGen();
              ProcessGeneration _processgeneration = _gen_5.getProcessgeneration();
              boolean _notEquals_4 = (!Objects.equal(_processgeneration, null));
              if (_notEquals_4) {
                _builder.append("\t");
                _builder.append("public boolean checkNDpoint() {");
                _builder.newLine();
                {
                  SchedulerDef _scheduler_7 = schModel.getScheduler();
                  Generate _gen_6 = _scheduler_7.getGen();
                  ProcessGeneration _processgeneration_1 = _gen_6.getProcessgeneration();
                  Template _process = _processgeneration_1.getProcess();
                  ND_Behavior _nD_behavior_3 = _process.getND_behavior();
                  boolean _notEquals_5 = (!Objects.equal(_nD_behavior_3, null));
                  if (_notEquals_5) {
                    {
                      SchedulerDef _scheduler_8 = schModel.getScheduler();
                      Generate _gen_7 = _scheduler_8.getGen();
                      ProcessGeneration _processgeneration_2 = _gen_7.getProcessgeneration();
                      Template _process_1 = _processgeneration_2.getProcess();
                      ND_Behavior _nD_behavior_4 = _process_1.getND_behavior();
                      Expression _cond_2 = _nD_behavior_4.getCond();
                      boolean _notEquals_6 = (!Objects.equal(_cond_2, null));
                      if (_notEquals_6) {
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("return ");
                        SchedulerDef _scheduler_9 = schModel.getScheduler();
                        Generate _gen_8 = _scheduler_9.getGen();
                        ProcessGeneration _processgeneration_3 = _gen_8.getProcessgeneration();
                        Template _process_2 = _processgeneration_3.getProcess();
                        ND_Behavior _nD_behavior_5 = _process_2.getND_behavior();
                        Expression _cond_3 = _nD_behavior_5.getCond();
                        String _dispatchExpression_1 = Statements.dispatchExpression(_cond_3);
                        _builder.append(_dispatchExpression_1, "\t\t");
                        _builder.append(" ;");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("return true ;");
                        _builder.newLine();
                      }
                    }
                  } else {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("return true ;");
                    _builder.newLine();
                  }
                }
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              } else {
                _builder.append("\t");
                _builder.append("public boolean checkNDpoint() {");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("return false ;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("}");
                _builder.newLine();
              }
            }
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("public boolean checkNDpoint() {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return false ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public RunningSet getRunningSet(){");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return _runningSet;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}\t");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence SchedulerDSLtoJavaCode(final ProcessDSL procModel, final SchedulerDSL schModel) {
    StringConcatenation _builder = new StringConcatenation();
    SchedulerDef _scheduler = schModel.getScheduler();
    String _parent = _scheduler.getParent();
    boolean refinement = (!Objects.equal(_parent, null));
    _builder.newLineIfNotEmpty();
    SchedulerDef sch = schModel.getScheduler();
    _builder.newLineIfNotEmpty();
    CharSequence _SchedulerClassHeader = SchedulerGenerator.SchedulerClassHeader();
    _builder.append(_SchedulerClassHeader, "");
    _builder.newLineIfNotEmpty();
    {
      SchedulerDef _scheduler_1 = schModel.getScheduler();
      Generate _gen = _scheduler_1.getGen();
      boolean _notEquals = (!Objects.equal(_gen, null));
      if (_notEquals) {
        _builder.append("import sspinja.Generate;");
        _builder.newLine();
      }
    }
    Data.getCollectionList(sch);
    _builder.append(" ");
    _builder.newLineIfNotEmpty();
    _builder.append("//Automatic generation");
    _builder.newLine();
    _builder.append("public class SchedulerObject_");
    SchedulerDef _scheduler_2 = schModel.getScheduler();
    String _name = _scheduler_2.getName();
    _builder.append(_name, "");
    _builder.append(" ");
    {
      if (refinement) {
        _builder.append(" extends SchedulerObject_");
        SchedulerDef _scheduler_3 = schModel.getScheduler();
        String _parent_1 = _scheduler_3.getParent();
        _builder.append(_parent_1, "");
      }
    }
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    {
      if ((!refinement)) {
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public static ArrayList<StaticProperty> staticPropertyList = new ArrayList<StaticProperty>() ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public static ArrayList<String> processList = new ArrayList<String>() ;\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public static boolean [] processInScheduler = new boolean [128]; //processes managed by scheduler");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public static byte pcount = 0 ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public static ArrayList<Byte> pcnt = new ArrayList<Byte>(); //instance of process id");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public String _action = \"\";\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public static ArrayList<String> initprocesslist = new ArrayList<String>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public static SchedulerPromelaModel panmodel ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public int _schselopt ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public int _schnumopt ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public int [][] _opt;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("private static int newP = -1, endP = -1;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public static int getnewP(){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("int result = newP;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("newP = -1;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return result;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public static int getendP(){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("int result = endP;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("endP = -1;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return result;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.newLine();
        {
          DefCore _defcore = Data.schModel.getDefcore();
          boolean _notEquals_1 = (!Objects.equal(_defcore, null));
          if (_notEquals_1) {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("public int num_core = ");
            DefCore _defcore_1 = Data.schModel.getDefcore();
            int _numcore = _defcore_1.getNumcore();
            _builder.append(_numcore, "\t\t");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("public int current_core = -1 ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("public int switchCore(int lastcore) throws ValidationException {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (num_core == 1) return -1 ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (lastcore < 0) ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("lastcore = 0 ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("else");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("lastcore = (lastcore + 1) ; //next core");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (lastcore == current_core) ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("lastcore = (lastcore + 1) ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (lastcore == num_core) return -1 ; //fail");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("System.out.print(\"Switch from core: \" + current_core + \" to: \" + lastcore + \": \");\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("current_core = lastcore ;\t\t\t\t\t\t\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_process = running_procs[current_core];\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_schselopt = 0 ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (running_process == null) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("select_process(-1) ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("return current_core ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("public int selCore(int lastcore) throws ValidationException {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (num_core == 1) return -1 ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (lastcore < 0) ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("lastcore = 0 ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("else");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("lastcore = (lastcore + 1) ; //next core\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (lastcore == num_core) return -1 ; //fail");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("System.out.print(\"Select core: \" + lastcore);\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("current_core = lastcore ;\t\t\t\t\t\t\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_process = running_procs[current_core];\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_schselopt = 0 ;\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (running_process == null) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("select_process(-1) ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("return current_core ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}\t");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("public int switchCore(int lastcore) throws ValidationException {return -1;}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("public int selCore(int lastcore) throws ValidationException {return -1;}");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public static void setPcnt(ArrayList<Byte> pcount) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("pcnt.clear();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("pcnt.addAll(pcount) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public static ArrayList<Byte> getPcnt() {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return pcnt ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public void setSchedulerSelOption(int sel){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("_schselopt = sel;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public int nextSchedulerOption(int lastschopt) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("if (lastschopt == -1) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("lastschopt = 0 ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("}\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("if (lastschopt < _schnumopt - 1) {\t\t\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("_schselopt = lastschopt + 1 ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("_schselopt = 0 ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("return -1; //no more scheduler option");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return _schselopt;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public int firstSchedulerOption() {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("_schselopt = 0 ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return 0;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.newLine();
        {
          SchedulerDef _scheduler_4 = schModel.getScheduler();
          Generate _gen_1 = _scheduler_4.getGen();
          boolean _equals = Objects.equal(_gen_1, null);
          if (_equals) {
            _builder.append("\t");
            _builder.append("public boolean hasGenTemplate = false ;");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("public boolean hasGenTemplate = true ;\t//to separate the events");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public void setAction(String act) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("_action = act ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public String getAction() {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return _action ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public int getRefID(String pName) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("int i = 0 ;\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("for (StaticProperty sP : staticPropertyList) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("if (sP.pName.equals(pName))");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t");
        _builder.append("return i ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("i ++ ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return -1 ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public static StaticProperty getStaticPropertyObject(int refID) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("for (StaticProperty sP : staticPropertyList)");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("if (sP.refID == refID)");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t");
        _builder.append("return sP ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return null ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public ArrayList<SchedulerProcess> findProcessByAlias(String alias) {\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("ArrayList<SchedulerProcess> result = new ArrayList<SchedulerProcess>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("if (alias.trim().equals(\"running_process\")) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("if (running_process != null) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t");
        _builder.append("result.add(running_process);");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("} else {\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("int idx = 0 ;\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("for (String procN : processList) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t");
        _builder.append("if (procN.trim().equals(alias.trim())) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t");
        _builder.append("SchedulerProcess target_process = findProcessByID(idx) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t");
        _builder.append("if (target_process != null) ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t\t");
        _builder.append("result.add(target_process) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t");
        _builder.append("idx ++ ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("for (StaticProperty stP : staticPropertyList) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t");
        _builder.append("if (stP.pName.trim().equals(alias.trim())) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t");
        _builder.append("int refID = stP.refID ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t");
        _builder.append("ArrayList<SchedulerProcess> resultStP = new ArrayList<SchedulerProcess>();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t");
        _builder.append("resultStP = findProcessByrefID(refID) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t");
        _builder.append("if (!resultStP.isEmpty()) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t\t");
        _builder.append("for (SchedulerProcess p1 : resultStP) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t\t\t");
        _builder.append("boolean add = true ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t\t\t");
        _builder.append("for (SchedulerProcess p2 : result) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t\t\t\t");
        _builder.append("if (p1.processID == p2.processID) ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t\t\t\t\t");
        _builder.append("add = false;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t\t\t");
        _builder.append("if (add) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t\t\t\t");
        _builder.append("result.add(p1) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("}\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return result ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public ArrayList<SchedulerProcess> findProcess(String pName) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return findProcessByAlias(pName) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public int existsProcess (String pName) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("ArrayList<SchedulerProcess> aP = findProcess(pName) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return aP.size() ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public int existsProcess (int pID) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("SchedulerProcess p = findProcessByID(pID) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("if (p == null) return 0 ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("else return 1 ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("public void updateProcessInSchedulerList() {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("for (int i = 0; i < 128; i ++) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("if (processInScheduler[i]) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t\t");
        _builder.append("processInScheduler[i] = (findProcessByID(i) != null);");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("}\t\t");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    Data.getCheckPointsList(schModel);
    _builder.append("\t\t\t");
    _builder.newLineIfNotEmpty();
    {
      for(final String check : Data.checkPointsList) {
        _builder.append("\t");
        _builder.append("public boolean ");
        _builder.append(check, "\t");
        _builder.append(" ; //check point");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("public int ");
        _builder.append(check, "\t");
        _builder.append("_min = -2, ");
        _builder.append(check, "\t");
        _builder.append("_max = -2; //no occurs\t\t\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    SchedulerDef _scheduler_5 = schModel.getScheduler();
    CharSequence _genContructor = SchedulerGenerator.genContructor(procModel, _scheduler_5);
    _builder.append(_genContructor, "\t");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//------------------------ event handler -------------------------");
    _builder.newLine();
    _builder.append("\t");
    SchedulerDef _scheduler_6 = schModel.getScheduler();
    String _genHandler = SchedulerGenerator.genHandler(procModel, _scheduler_6);
    _builder.append(_genHandler, "\t");
    _builder.newLineIfNotEmpty();
    {
      String _parent_2 = sch.getParent();
      boolean _equals_1 = Objects.equal(_parent_2, null);
      if (_equals_1) {
        _builder.append("\t");
        String _genMissingHandler = Handler.genMissingHandler(sch, procModel);
        _builder.append(_genMissingHandler, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//--------------- encoding function -------------------------------");
    _builder.newLine();
    _builder.append("\t");
    CharSequence _genEncodeDecode = SchedulerGenerator.genEncodeDecode(sch);
    _builder.append(_genEncodeDecode, "\t");
    _builder.append("\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    CharSequence _genUtilityFunction = SchedulerGenerator.genUtilityFunction(sch);
    _builder.append(_genUtilityFunction, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _genPrint = SchedulerGenerator.genPrint(sch, procModel);
    _builder.append(_genPrint, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    String _genRunningFunction = SchedulerGenerator.genRunningFunction(sch);
    _builder.append(_genRunningFunction, "\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    CharSequence _genInterfaceFunction = Interface.genInterfaceFunction(sch);
    _builder.append(_genInterfaceFunction, "\t");
    _builder.newLineIfNotEmpty();
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//------------------------ genStaticProcessProperty -------------------------");
    _builder.newLine();
    {
      String _parent_3 = sch.getParent();
      boolean _equals_2 = Objects.equal(_parent_3, null);
      if (_equals_2) {
        _builder.append("\t");
        CharSequence _genStaticProcessProperty = SchedulerGenerator.genStaticProcessProperty(procModel);
        _builder.append(_genStaticProcessProperty, "\t");
        _builder.newLineIfNotEmpty();
      } else {
        _builder.append("\t");
        _builder.append("public void genStaticProcessProperty(){\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("super.genStaticProcessProperty() ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    SchedulerDef _scheduler_7 = schModel.getScheduler();
    CharSequence _genDataStruture = SchedulerGenerator.genDataStruture(_scheduler_7);
    _builder.append(_genDataStruture, "\t");
    _builder.append("\t\t\t");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    SchedulerDef _scheduler_8 = schModel.getScheduler();
    CharSequence _genUtilitiesFunctions = SchedulerGenerator.genUtilitiesFunctions(_scheduler_8);
    _builder.append(_genUtilitiesFunctions, "\t");
    _builder.append("\t");
    _builder.newLineIfNotEmpty();
    {
      if ((Objects.equal(schModel.getScheduler().getParent(), null) || (!Objects.equal(schModel.getVerify(), null)))) {
        _builder.append("\t");
        CharSequence _genCTLVerify = SchedulerGenerator.genCTLVerify(schModel);
        _builder.append(_genCTLVerify, "\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genCTLVerify(final SchedulerDSL schModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//--------------- verification structure ---------------------------");
    _builder.newLine();
    {
      Verify _verify = schModel.getVerify();
      boolean _equals = Objects.equal(_verify, null);
      if (_equals) {
        _builder.append("public boolean schedulerCheck() {return false ;\t}");
        _builder.newLine();
        _builder.append("public void initSchedulerState(SchedulerState schState, int depth) {}");
        _builder.newLine();
        _builder.append("public boolean stateCheck() {return false;}\t\t\t");
        _builder.newLine();
        _builder.append("public boolean collectState() {\treturn false ;}\t\t\t");
        _builder.newLine();
        _builder.append("public void printAnalysisResult(PrintWriter out) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (out != null) out.println(\"No Analysis result\");");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("System.out.println(\"No Analysis result\") ;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append("public boolean schedulerCheck() {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return true ;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.newLine();
        Verify verify = schModel.getVerify();
        _builder.newLineIfNotEmpty();
        RTCTL formula = verify.getFormula();
        _builder.newLineIfNotEmpty();
        _builder.append("   ");
        _builder.newLine();
        _builder.append("public void initSchedulerState(SchedulerState schState, int depth) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("//");
        String _dispatchRTCTLExpression = Statements.dispatchRTCTLExpression(formula);
        _builder.append(_dispatchRTCTLExpression, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("boolean checked[] = new boolean[");
        int _RTCTLformulaLength = SchedulerGenerator.RTCTLformulaLength(formula);
        _builder.append(_RTCTLformulaLength, "\t");
        _builder.append("];");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        Object _printFormulaCheckedAtomic = SchedulerGenerator.printFormulaCheckedAtomic(formula, 0);
        _builder.append(_printFormulaCheckedAtomic, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("schState.checked = checked ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("boolean results[] = new boolean[");
        int _RTCTLformulaLength_1 = SchedulerGenerator.RTCTLformulaLength(formula);
        _builder.append(_RTCTLformulaLength_1, "\t");
        _builder.append("];");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("//SysVar: ");
        CharSequence _declareSysVar = SchedulerGenerator.declareSysVar(formula);
        _builder.append(_declareSysVar, "\t");
        _builder.append(" ");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        Object _printInitAtomicf = SchedulerGenerator.printInitAtomicf(formula, 0);
        _builder.append(_printInitAtomicf, "\t");
        _builder.append("\t\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("schState.results = results ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("//check points for init scheduler state");
        _builder.newLine();
        {
          for(final String checkpoint : Data.checkPointsList) {
            _builder.append("\t");
            _builder.append("if (this.");
            _builder.append(checkpoint, "\t");
            _builder.append(") {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("schState.");
            _builder.append(checkpoint, "\t\t");
            _builder.append(" = this.");
            _builder.append(checkpoint, "\t\t");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("schState.");
            _builder.append(checkpoint, "\t\t");
            _builder.append("_chkmin = depth ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("schState.");
            _builder.append(checkpoint, "\t\t");
            _builder.append("_chkmax = depth ;\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("this.");
            _builder.append(checkpoint, "\t\t");
            _builder.append(" = false ; //to check other points");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (");
            _builder.append(checkpoint, "\t\t");
            _builder.append("_min > depth || ");
            _builder.append(checkpoint, "\t\t");
            _builder.append("_min == -2) //-2 : no occurence");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append(checkpoint, "\t\t\t");
            _builder.append("_min = depth ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (");
            _builder.append(checkpoint, "\t\t");
            _builder.append("_max < depth && ");
            _builder.append(checkpoint, "\t\t");
            _builder.append("_max != -1 ) //-1 : occurents infinitely");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append(checkpoint, "\t\t\t");
            _builder.append("_max = depth ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("public boolean stateCheck() {");
        _builder.newLine();
        {
          CTL_AT _at = verify.getAt();
          boolean _notEquals = (!Objects.equal(_at, null));
          if (_notEquals) {
            _builder.append("\t");
            _builder.append("boolean result = true ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("//");
            CTL_AT _at_1 = verify.getAt();
            Expression _cond = _at_1.getCond();
            CharSequence _declareSysVar_1 = SchedulerGenerator.declareSysVar(_cond);
            _builder.append(_declareSysVar_1, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            CTL_AT _at_2 = verify.getAt();
            Expression _cond_1 = _at_2.getCond();
            ArrayList<String> pNameList = Statements.getProcessListFromExpression(_cond_1);
            _builder.append("\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            {
              for(final String pName : pNameList) {
                _builder.append("\t");
                _builder.append("ArrayList<SchedulerProcess> a_");
                _builder.append(pName, "\t");
                _builder.append(" = findProcessByAlias(\"");
                _builder.append(pName, "\t");
                _builder.append("\") ;");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("if (a_");
                _builder.append(pName, "\t");
                _builder.append(".size() == 0) ");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("result = false ;");
                _builder.newLine();
              }
            }
            _builder.append("\t");
            _builder.newLine();
            _builder.append("\t");
            CTL_AT _at_3 = verify.getAt();
            Expression _cond_2 = _at_3.getCond();
            String _dispatchExpression = Statements.dispatchExpression(_cond_2, false);
            String _plus = ("result = result && " + _dispatchExpression);
            String printedString = (_plus + " ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            CharSequence _printLoop = SchedulerGenerator.printLoop(pNameList, printedString);
            _builder.append(_printLoop, "\t");
            _builder.append("\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("boolean result = false ;\t\t\t");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("return result ;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("public boolean collectState() {");
        _builder.newLine();
        {
          CTL_AT _at_4 = verify.getAt();
          boolean _notEquals_1 = (!Objects.equal(_at_4, null));
          if (_notEquals_1) {
            _builder.append("\t");
            _builder.append("return true ;");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("return false ;");
            _builder.newLine();
          }
        }
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("public void printAnalysisResult(PrintWriter out) {");
        _builder.newLine();
        {
          int _size = Data.checkPointsList.size();
          boolean _greaterThan = (_size > 0);
          if (_greaterThan) {
            _builder.append("\t");
            _builder.append("System.out.println(\"Analysis result: \") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("out.println(\"Analysis result: \") ;");
            _builder.newLine();
            {
              for(final String check : Data.checkPointsList) {
                _builder.append("\t");
                _builder.append("System.out.println(\"");
                _builder.append(check, "\t");
                _builder.append(" (min,max)=(\" + ");
                _builder.append(check, "\t");
                _builder.append("_min + \",\" + ");
                _builder.append(check, "\t");
                _builder.append("_max + \")\");");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("out.println(\"");
                _builder.append(check, "\t");
                _builder.append(" (min,max)=(\" + ");
                _builder.append(check, "\t");
                _builder.append("_min + \",\" + ");
                _builder.append(check, "\t");
                _builder.append("_max + \")\");");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("System.out.println(\"-1: Occurs infinitely\") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("out.println(\"-1: Occurs infinitely\") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("System.out.println(\"-2: No occurence\") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("out.println(\"-2: No occurence\") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("System.out.println(\" n: Occurs at time n\") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("out.println(\" n: Occurs at time n\") ;");
            _builder.newLine();
          }
        }
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public static CharSequence printLoop(final ArrayList<String> pNameList, final String printedString) {
    StringConcatenation _builder = new StringConcatenation();
    {
      int _size = pNameList.size();
      boolean _greaterThan = (_size > 0);
      if (_greaterThan) {
        String pName = pNameList.remove(0);
        _builder.newLineIfNotEmpty();
        _builder.append("for (SchedulerProcess ");
        _builder.append(pName, "");
        _builder.append(" : a_");
        _builder.append(pName, "");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        Object _printLoop = SchedulerGenerator.printLoop(pNameList, printedString);
        _builder.append(_printLoop, "\t");
        _builder.newLineIfNotEmpty();
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append(printedString, "");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }
  
  public static CharSequence genStaticProcessProperty(final ProcessDSL procModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void genStaticProcessProperty(){\t");
    _builder.newLine();
    _builder.append("\t");
    int refID = 0;
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("StaticProperty sP ;\t\t\t\t\t\t");
    _builder.newLine();
    {
      EList<ProcessDef> _process = procModel.getProcess();
      for(final ProcessDef pt : _process) {
        {
          scheduling.dsl.Process _proctype = pt.getProctype();
          boolean _notEquals = (!Objects.equal(_proctype, null));
          if (_notEquals) {
            _builder.append("\t");
            _builder.append("//");
            _builder.append(refID, "\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("sP = new StaticProperty() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("sP.refID = ");
            int _plusPlus = refID++;
            _builder.append(_plusPlus, "\t");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("sP.pName = \"");
            scheduling.dsl.Process _proctype_1 = pt.getProctype();
            String _name = _proctype_1.getName();
            _builder.append(_name, "\t");
            _builder.append("\" ;");
            _builder.newLineIfNotEmpty();
            {
              EList<PropertyAssignment> _propertyassignment = pt.getPropertyassignment();
              for(final PropertyAssignment properass : _propertyassignment) {
                {
                  ProcessPropertyName _propers = properass.getPropers();
                  String _name_1 = _propers.getName();
                  boolean _contains = Data.valPropertyList.contains(_name_1);
                  if (_contains) {
                    {
                      ProcessPropertyName _propers_1 = properass.getPropers();
                      String _name_2 = _propers_1.getName();
                      boolean _contains_1 = Data.varPropertyList.contains(_name_2);
                      boolean _not = (!_contains_1);
                      if (_not) {
                        {
                          ProcessPropertyName _propers_2 = properass.getPropers();
                          boolean _notEquals_1 = (!Objects.equal(_propers_2, null));
                          if (_notEquals_1) {
                            {
                              Value _pvalue = properass.getPvalue();
                              boolean _notEquals_2 = (!Objects.equal(_pvalue, null));
                              if (_notEquals_2) {
                                {
                                  Value _pvalue_1 = properass.getPvalue();
                                  NumValue _num = _pvalue_1.getNum();
                                  boolean _notEquals_3 = (!Objects.equal(_num, null));
                                  if (_notEquals_3) {
                                    _builder.append("\t");
                                    _builder.append("sP.");
                                    ProcessPropertyName _propers_3 = properass.getPropers();
                                    String _name_3 = _propers_3.getName();
                                    _builder.append(_name_3, "\t");
                                    _builder.append(" = ");
                                    Value _pvalue_2 = properass.getPvalue();
                                    NumValue _num_1 = _pvalue_2.getNum();
                                    int _value = _num_1.getValue();
                                    _builder.append(_value, "\t");
                                    _builder.append(" ;");
                                    _builder.newLineIfNotEmpty();
                                  } else {
                                    _builder.append("\t");
                                    _builder.append("sP.");
                                    ProcessPropertyName _propers_4 = properass.getPropers();
                                    String _name_4 = _propers_4.getName();
                                    _builder.append(_name_4, "\t");
                                    _builder.append(" = ");
                                    Value _pvalue_3 = properass.getPvalue();
                                    BoolValue _bool = _pvalue_3.getBool();
                                    String _value_1 = _bool.getValue();
                                    _builder.append(_value_1, "\t");
                                    _builder.append(" ;");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              } else {
                                _builder.append("\t");
                                _builder.append("sP.");
                                ProcessPropertyName _propers_5 = properass.getPropers();
                                String _name_5 = _propers_5.getName();
                                _builder.append(_name_5, "\t");
                                _builder.append(" = ");
                                ProcessPropertyName _propers_6 = properass.getPropers();
                                String _name_6 = _propers_6.getName();
                                ParameterList _paralist = pt.getParalist();
                                EList<ParameterAssign> _para = _paralist.getPara();
                                String _findValueInParameterList = Utilities.findValueInParameterList(_name_6, _para);
                                _builder.append(_findValueInParameterList, "\t");
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
              }
            }
            _builder.append("\t");
            _builder.append("staticPropertyList.add(sP) ;");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static String genPrint(final SchedulerDef sch, final ProcessDSL procModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public String getSchedulerName() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return \"");
    String _name = sch.getName();
    _builder.append(_name, "\t");
    _builder.append("\" ;");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public String getProcessModelName() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return \"");
    String _name_1 = procModel.getName();
    _builder.append(_name_1, "\t");
    _builder.append("\" ;");
    _builder.newLineIfNotEmpty();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public String getSchedulingPolicy() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return \"+ Scheduler: \" + getSchedulerName() + \" + Processes: \" + getProcessModelName();");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public void print_all(){\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("Util.println(\"+ Scheduler: \" + getSchedulerName());");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//Util.println(\"- SCH OPT: \" + _schselopt + \"/\" + (_schnumopt - 1));");
    _builder.newLine();
    {
      if (Data.runTime) {
        {
          DefCore _defcore = Data.schModel.getDefcore();
          boolean _notEquals = (!Objects.equal(_defcore, null));
          if (_notEquals) {
            _builder.append("\t");
            _builder.append("if (current_core >= 0)\t{\t\t \t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("System.out.println(\"- CURRENT CORE: \" + current_core + \" - Time_count/Time_slice: \" + _time_count[current_core] + \"/\" + _time_slice[current_core]) ;\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_process = running_procs[current_core];");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_runningSet = _runningSets[current_core];");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("for (int i=0; i < num_core; i++) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("System.out.println(\"  + Core \" + i) ; ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (running_procs[i] == null) ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("System.out.println(\"\\t- Running process: Null\") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("else {\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("System.out.print(\"\\t- Running process: \") ;\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("running_procs[i].print();");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}\t\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (_runningSets[i] == null) ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("System.out.println(\"\\t- Running set: Empty\") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("else {\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("System.out.print(\"\\t- Running set: \") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_runningSets[i].print() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}\t\t\t\t\t");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("Util.println(\"- Time_count/Time_slice: \" + _time_count + \"/\" + _time_slice) ;");
            _builder.newLine();
          }
        }
      }
    }
    {
      Set<String> _keySet = Data.periodicClockProperties.keySet();
      for(final String pClock : _keySet) {
        _builder.append("\t");
        _builder.append("Util.println(\"Periodic Clock ");
        _builder.append(pClock, "\t");
        _builder.append(": \" + ");
        _builder.append(pClock, "\t");
        _builder.append(") ;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("Util.println(\"- Periodic Clock Offset ");
        _builder.append(pClock, "\t");
        _builder.append("_offset: \" + ");
        _builder.append(pClock, "\t");
        _builder.append("_offset) ;\t\t\t\t\t\t\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      String _parent = sch.getParent();
      boolean _notEquals_1 = (!Objects.equal(_parent, null));
      if (_notEquals_1) {
        _builder.append("\t");
        _builder.append("super.print_all() ;");
        _builder.newLine();
      } else {
        {
          DefCore _defcore_1 = Data.schModel.getDefcore();
          boolean _notEquals_2 = (!Objects.equal(_defcore_1, null));
          if (_notEquals_2) {
            _builder.append("\t");
            _builder.append("if (current_core >= 0)\t{\t\t \t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("System.out.println(\"- CURRENT CORE: \" + current_core) ;\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_process = running_procs[current_core];");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_runningSet = _runningSets[current_core];");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("for (int i=0; i < num_core; i++) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("System.out.println(\"  + Core \" + i) ; ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (running_procs[i] == null) ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("System.out.println(\"\\t- Running process: Null\") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("else {\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("System.out.print(\"\\t- Running process: \") ;\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("running_procs[i].print();");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}\t\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (_runningSets[i] == null) ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("System.out.println(\"\\t- Running set: Empty\") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("else {\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("System.out.print(\"\\t- Running set: \") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_runningSets[i].print() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}\t\t\t\t\t");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("Util.println(\"- Running process: \" ) ; ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("if (running_process == null) ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("Util.println(\"Null\") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("else {\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_process.print();");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("Util.println(\"- Running set: \" ) ;\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("if (_runningSet == null) ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("Util.println(\"Null\") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("else {\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_runningSet.print() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("//System.out.println(processList);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("//printProcessInScheduler() ;\t");
            _builder.newLine();
          }
        }
      }
    }
    {
      VarDef _svar = sch.getSvar();
      boolean _notEquals_3 = (!Objects.equal(_svar, null));
      if (_notEquals_3) {
        {
          VarDef _svar_1 = sch.getSvar();
          EList<VarDecl> _vard = _svar_1.getVard();
          boolean _notEquals_4 = (!Objects.equal(_vard, null));
          if (_notEquals_4) {
            {
              VarDef _svar_2 = sch.getSvar();
              EList<VarDecl> _vard_1 = _svar_2.getVard();
              for(final VarDecl vs : _vard_1) {
                {
                  VarBlockDef _varblockdef = vs.getVarblockdef();
                  boolean _notEquals_5 = (!Objects.equal(_varblockdef, null));
                  if (_notEquals_5) {
                    {
                      VarBlockDef _varblockdef_1 = vs.getVarblockdef();
                      EList<VarDefinition> _vardef = _varblockdef_1.getVardef();
                      for(final VarDefinition vas : _vardef) {
                        {
                          scheduling.dsl.String _type = vas.getType();
                          String _string = _type.toString();
                          String _trim = _string.trim();
                          boolean _equals = Objects.equal(_trim, "clock");
                          if (_equals) {
                            {
                              EList<VarName> _name_2 = vas.getName();
                              for(final VarName va : _name_2) {
                                _builder.append("\t");
                                _builder.append("System.out.print(\"Clock ");
                                String _name_3 = va.getName();
                                _builder.append(_name_3, "\t");
                                _builder.append(": \" + ");
                                String _name_4 = va.getName();
                                _builder.append(_name_4, "\t");
                                _builder.append(") ;");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        }
                      }
                    }
                  } else {
                    {
                      VarSingleDef _varsingledef = vs.getVarsingledef();
                      VarDefinition _vardef_1 = _varsingledef.getVardef();
                      scheduling.dsl.String _type_1 = _vardef_1.getType();
                      String _string_1 = _type_1.toString();
                      String _trim_1 = _string_1.trim();
                      boolean _equals_1 = Objects.equal(_trim_1, "clock");
                      if (_equals_1) {
                        {
                          VarSingleDef _varsingledef_1 = vs.getVarsingledef();
                          VarDefinition _vardef_2 = _varsingledef_1.getVardef();
                          EList<VarName> _name_5 = _vardef_2.getName();
                          for(final VarName va_1 : _name_5) {
                            _builder.append("\t");
                            _builder.append("System.out.print(\"Clock ");
                            String _name_6 = va_1.getName();
                            _builder.append(_name_6, "\t");
                            _builder.append(": \" + ");
                            String _name_7 = va_1.getName();
                            _builder.append(_name_7, "\t");
                            _builder.append(") ;");
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
    {
      for(final String col : Data.collectionList) {
        _builder.append("System.out.print(\"- Collection: ");
        _builder.append(col, "");
        _builder.append(" : \");");
        _builder.newLineIfNotEmpty();
        _builder.append(col, "");
        _builder.append(".print() ;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public String toString() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("StringBuilder sb = new StringBuilder();");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("sb.append(\"\\n\\n\") ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("sb.append(\"+ Scheduler: \" + getSchedulerName() + \" + Processes: \" + getProcessModelName());");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("sb.append(\"\\n\") ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.newLine();
    {
      String _parent_1 = sch.getParent();
      boolean _notEquals_6 = (!Objects.equal(_parent_1, null));
      if (_notEquals_6) {
        _builder.append("\t");
        _builder.append("sb.append(super.toString()) ;");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("sb.append(\"- Running process: \" ) ; ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (running_process == null) ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("sb.append(\"Null!\") ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("else {\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("sb.append(running_process.toString());");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("sb.append(\"\\n\") ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("sb.append(\"- Running set: \" ) ;\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (_runningSet == null) ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("sb.append(\"Null!\");");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("else {\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("sb.append(_runningSet.toString()) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("sb.append(\"\\n\") ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}\t\t\t\t\t");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    {
      SchedulerDataDef _schedulerdata = sch.getSchedulerdata();
      boolean _notEquals_7 = (!Objects.equal(_schedulerdata, null));
      if (_notEquals_7) {
        {
          for(final String col_1 : Data.collectionList) {
            _builder.append("\t");
            _builder.append("sb.append(\"- Collection ");
            _builder.append(col_1, "\t");
            _builder.append(" : \"); \t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("sb.append(");
            _builder.append(col_1, "\t");
            _builder.append(".toString()) ;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return sb.toString();");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static CharSequence SchedulerClassHeader() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package sspinja.scheduling ;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("import java.io.PrintWriter;\t\t");
    _builder.newLine();
    _builder.append("import java.util.ArrayList;");
    _builder.newLine();
    _builder.append("import java.util.HashMap;");
    _builder.newLine();
    _builder.append("import java.util.Iterator;");
    _builder.newLine();
    _builder.append("import spinja.util.DataReader;");
    _builder.newLine();
    _builder.append("import spinja.util.DataWriter;");
    _builder.newLine();
    _builder.append("import spinja.util.StringUtil;");
    _builder.newLine();
    _builder.append("import spinja.util.Util;\t\t");
    _builder.newLine();
    _builder.append("import spinja.util.Log;");
    _builder.newLine();
    _builder.append("import spinja.exceptions.*;");
    _builder.newLine();
    _builder.append("import spinja.promela.model.PromelaProcess;\t\t");
    _builder.newLine();
    _builder.append("import sspinja.scheduler.search.SchedulerSearchAlgorithm;");
    _builder.newLine();
    _builder.append("import spinja.util.ByteArrayStorage;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("import sspinja.scheduler.promela.model.SchedulerPromelaModel;");
    _builder.newLine();
    _builder.append("import sspinja.SchedulerPanModel;");
    _builder.newLine();
    _builder.append("import sspinja.SchedulerState;");
    _builder.newLine();
    _builder.newLine();
    return _builder;
  }
  
  public static String genRunningFunction(final SchedulerDef sch) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//--------------- running function -------------------------------");
    _builder.newLine();
    {
      String _parent = sch.getParent();
      boolean _equals = Objects.equal(_parent, null);
      if (_equals) {
        _builder.append("public void executeProcess(PromelaProcess proc, int processID, ArrayList<String> para) throws ValidationException {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("SchedulerProcess p = new SchedulerProcess() ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("//p.processID = (byte) processID ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("p.processID = processID ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("newP = processID ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("//initialize the process");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("p.initProcess(proc.getName(), para);");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("//Util.println(\"New process \" + p) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("processInScheduler[processID] = true ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("while (pcnt.size() < processID + 1) pcnt.add((byte) 0) ;\t\t");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("pcnt.set(processID, (byte) (pcnt.get(processID) + 1) ) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("config_new_process(p) ;\t\t");
        _builder.newLine();
        _builder.append("}\t\t\t\t");
        _builder.newLine();
        _builder.append("public int select_process_to_run(int lastProcessID) { ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (_runningSet.isEmpty() == 1)");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("return -1 ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("SchedulerProcess temp = _runningSet.getFirstProcess(lastProcessID) ; //selectProcessToRun();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (temp != null) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("running_process = temp ; //selectProcessToRun() == null ? _running ; //= _runningSet.getNextProcess(running_process) ;");
        _builder.newLine();
        {
          DefCore _defcore = Data.schModel.getDefcore();
          boolean _notEquals = (!Objects.equal(_defcore, null));
          if (_notEquals) {
            _builder.append("\t\t");
            _builder.append("running_procs[current_core] = running_process;");
            _builder.newLine();
          }
        }
        _builder.append("\t\t");
        _builder.append("return running_process.processID ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("else return - 1;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("public boolean replace_running_process(byte collectionIndex, SchedulerProcess running_process, SchedulerProcess previous_running) {");
    _builder.newLine();
    {
      String _parent_1 = sch.getParent();
      boolean _notEquals_1 = (!Objects.equal(_parent_1, null));
      if (_notEquals_1) {
        _builder.append("\t");
        _builder.append("if (super.replace_running_process(collectionIndex, running_process, previous_running)) ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return true ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("byte numCol = (byte) super.getNumberProcessCollection() ;");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("byte numCol = 0 ;");
        _builder.newLine();
      }
    }
    {
      SchedulerDataDef _schedulerdata = sch.getSchedulerdata();
      boolean _notEquals_2 = (!Objects.equal(_schedulerdata, null));
      if (_notEquals_2) {
        {
          SchedulerDataDef _schedulerdata_1 = sch.getSchedulerdata();
          EList<DataDef> _datadef = _schedulerdata_1.getDatadef();
          boolean _notEquals_3 = (!Objects.equal(_datadef, null));
          if (_notEquals_3) {
            _builder.append("\t");
            int idx = 0;
            _builder.newLineIfNotEmpty();
            {
              SchedulerDataDef _schedulerdata_2 = sch.getSchedulerdata();
              EList<DataDef> _datadef_1 = _schedulerdata_2.getDatadef();
              for(final DataDef data : _datadef_1) {
                {
                  DataBlockDef _datablockdef = data.getDatablockdef();
                  boolean _notEquals_4 = (!Objects.equal(_datablockdef, null));
                  if (_notEquals_4) {
                    {
                      DataBlockDef _datablockdef_1 = data.getDatablockdef();
                      EList<DataSingleDef> _datadef_2 = _datablockdef_1.getDatadef();
                      for(final DataSingleDef d : _datadef_2) {
                        {
                          SchedulerCollectionDef _col = d.getCol();
                          boolean _notEquals_5 = (!Objects.equal(_col, null));
                          if (_notEquals_5) {
                            {
                              SchedulerCollectionDef _col_1 = d.getCol();
                              String _parent_2 = _col_1.getParent();
                              boolean _equals_1 = Objects.equal(_parent_2, null);
                              if (_equals_1) {
                                _builder.append("\t");
                                _builder.append("if (collectionIndex == (byte) (");
                                int _plusPlus = idx++;
                                _builder.append(_plusPlus, "\t");
                                _builder.append(" + numCol)) {");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.append("if (");
                                SchedulerCollectionDef _col_2 = d.getCol();
                                SchedulerSet _name = _col_2.getName();
                                String _name_1 = _name.getName();
                                _builder.append(_name_1, "\t\t");
                                _builder.append(" != null)");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("\t\t");
                                SchedulerCollectionDef _col_3 = d.getCol();
                                SchedulerSet _name_2 = _col_3.getName();
                                String _name_3 = _name_2.getName();
                                _builder.append(_name_3, "\t\t\t");
                                _builder.append(".replace(running_process, previous_running) ;\t\t\t\t\t\t\t\t");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.append("return true ;");
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
                  } else {
                    {
                      DataSingleDef _datasingledef = data.getDatasingledef();
                      SchedulerCollectionDef _col_4 = _datasingledef.getCol();
                      boolean _notEquals_6 = (!Objects.equal(_col_4, null));
                      if (_notEquals_6) {
                        {
                          DataSingleDef _datasingledef_1 = data.getDatasingledef();
                          SchedulerCollectionDef _col_5 = _datasingledef_1.getCol();
                          String _parent_3 = _col_5.getParent();
                          boolean _equals_2 = Objects.equal(_parent_3, null);
                          if (_equals_2) {
                            _builder.append("\t");
                            _builder.append("if (collectionIndex == (byte) (");
                            int _plusPlus_1 = idx++;
                            _builder.append(_plusPlus_1, "\t");
                            _builder.append(" + numCol)) {");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("if (");
                            DataSingleDef _datasingledef_2 = data.getDatasingledef();
                            SchedulerCollectionDef _col_6 = _datasingledef_2.getCol();
                            SchedulerSet _name_4 = _col_6.getName();
                            String _name_5 = _name_4.getName();
                            _builder.append(_name_5, "\t\t");
                            _builder.append(" != null)");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            DataSingleDef _datasingledef_3 = data.getDatasingledef();
                            SchedulerCollectionDef _col_7 = _datasingledef_3.getCol();
                            SchedulerSet _name_6 = _col_7.getName();
                            String _name_7 = _name_6.getName();
                            _builder.append(_name_7, "\t\t\t");
                            _builder.append(".replace(running_process, previous_running) ;\t\t\t\t\t\t\t\t");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("return true ;");
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
              }
            }
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return false ;\t\t\t");
    _builder.newLine();
    _builder.append("}\t");
    _builder.newLine();
    _builder.append("public void put_running_process(byte collectionIndex) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (running_process != null) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("put_process(running_process, collectionIndex) ;\t\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public boolean put_process(SchedulerProcess proc, byte collectionIndex) {\t");
    _builder.newLine();
    {
      String _parent_4 = sch.getParent();
      boolean _notEquals_7 = (!Objects.equal(_parent_4, null));
      if (_notEquals_7) {
        _builder.append("\t");
        _builder.append("if (super.put_process(proc, collectionIndex)) ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return true ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("byte numCol = (byte) super.getNumberProcessCollection() ;");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("byte numCol = 0 ;");
        _builder.newLine();
      }
    }
    {
      SchedulerDataDef _schedulerdata_3 = sch.getSchedulerdata();
      boolean _notEquals_8 = (!Objects.equal(_schedulerdata_3, null));
      if (_notEquals_8) {
        {
          SchedulerDataDef _schedulerdata_4 = sch.getSchedulerdata();
          EList<DataDef> _datadef_3 = _schedulerdata_4.getDatadef();
          boolean _notEquals_9 = (!Objects.equal(_datadef_3, null));
          if (_notEquals_9) {
            _builder.append("\t");
            int idx_1 = 0;
            _builder.newLineIfNotEmpty();
            {
              SchedulerDataDef _schedulerdata_5 = sch.getSchedulerdata();
              EList<DataDef> _datadef_4 = _schedulerdata_5.getDatadef();
              for(final DataDef data_1 : _datadef_4) {
                {
                  DataBlockDef _datablockdef_2 = data_1.getDatablockdef();
                  boolean _notEquals_10 = (!Objects.equal(_datablockdef_2, null));
                  if (_notEquals_10) {
                    {
                      DataBlockDef _datablockdef_3 = data_1.getDatablockdef();
                      EList<DataSingleDef> _datadef_5 = _datablockdef_3.getDatadef();
                      for(final DataSingleDef d_1 : _datadef_5) {
                        {
                          SchedulerCollectionDef _col_8 = d_1.getCol();
                          boolean _notEquals_11 = (!Objects.equal(_col_8, null));
                          if (_notEquals_11) {
                            {
                              SchedulerCollectionDef _col_9 = d_1.getCol();
                              String _parent_5 = _col_9.getParent();
                              boolean _equals_3 = Objects.equal(_parent_5, null);
                              if (_equals_3) {
                                _builder.append("\t");
                                _builder.append("if (collectionIndex == (byte) (");
                                int _plusPlus_2 = idx_1++;
                                _builder.append(_plusPlus_2, "\t");
                                _builder.append(" + numCol)) {");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.append("if (");
                                SchedulerCollectionDef _col_10 = d_1.getCol();
                                SchedulerSet _name_8 = _col_10.getName();
                                String _name_9 = _name_8.getName();
                                _builder.append(_name_9, "\t\t");
                                _builder.append(" != null)");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("\t\t");
                                SchedulerCollectionDef _col_11 = d_1.getCol();
                                SchedulerSet _name_10 = _col_11.getName();
                                String _name_11 = _name_10.getName();
                                _builder.append(_name_11, "\t\t\t");
                                _builder.append(".put(proc);");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("\t\t");
                                _builder.append("//if (proc == running_process)");
                                _builder.newLine();
                                _builder.append("\t");
                                _builder.append("\t\t");
                                _builder.append("//\trunning_process = null ;");
                                _builder.newLine();
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.append("return true ;");
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
                  } else {
                    {
                      DataSingleDef _datasingledef_4 = data_1.getDatasingledef();
                      SchedulerCollectionDef _col_12 = _datasingledef_4.getCol();
                      boolean _notEquals_12 = (!Objects.equal(_col_12, null));
                      if (_notEquals_12) {
                        {
                          DataSingleDef _datasingledef_5 = data_1.getDatasingledef();
                          SchedulerCollectionDef _col_13 = _datasingledef_5.getCol();
                          String _parent_6 = _col_13.getParent();
                          boolean _equals_4 = Objects.equal(_parent_6, null);
                          if (_equals_4) {
                            _builder.append("\t");
                            _builder.append("if (collectionIndex == (byte) (");
                            int _plusPlus_3 = idx_1++;
                            _builder.append(_plusPlus_3, "\t");
                            _builder.append(" + numCol)) {");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("if (");
                            DataSingleDef _datasingledef_6 = data_1.getDatasingledef();
                            SchedulerCollectionDef _col_14 = _datasingledef_6.getCol();
                            SchedulerSet _name_12 = _col_14.getName();
                            String _name_13 = _name_12.getName();
                            _builder.append(_name_13, "\t\t");
                            _builder.append(" != null)");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            DataSingleDef _datasingledef_7 = data_1.getDatasingledef();
                            SchedulerCollectionDef _col_15 = _datasingledef_7.getCol();
                            SchedulerSet _name_14 = _col_15.getName();
                            String _name_15 = _name_14.getName();
                            _builder.append(_name_15, "\t\t\t");
                            _builder.append(".put(proc);");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.append("//if (proc == running_process)");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.append("//\trunning_process = null ;");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("return true ;");
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
              }
            }
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return false ;");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("//--------------- timed function -------------------------------\t\t");
    _builder.newLine();
    _builder.append("public void time_out(){\t");
    _builder.newLine();
    {
      if (Data.runTime) {
        {
          DefCore _defcore_1 = Data.schModel.getDefcore();
          boolean _notEquals_13 = (!Objects.equal(_defcore_1, null));
          if (_notEquals_13) {
            _builder.append("\t");
            _builder.append("//just only add time");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("add_time(_time_slice[current_core] - _time_count[current_core]) ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("_time_count[current_core] = _time_slice[current_core] ;");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("//just only add time");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("add_time(_time_slice - _time_count) ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("_time_count = _time_slice ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("//check_running_time_to_put_running_process() ;");
            _builder.newLine();
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("//do nothing");
        _builder.newLine();
      }
    }
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public void inc_time() {");
    _builder.newLine();
    {
      if (Data.runTime) {
        {
          DefCore _defcore_2 = Data.schModel.getDefcore();
          boolean _notEquals_14 = (!Objects.equal(_defcore_2, null));
          if (_notEquals_14) {
            _builder.append("\t");
            _builder.append("if (_time_slice[current_core] != 0) { ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (_time_count[current_core] == _time_slice[current_core])");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_time_count[current_core] = 1 ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("else");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_time_count[current_core] ++ ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("if (_time_slice != 0) { ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (_time_count == _time_slice)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_time_count = 1 ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("else");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_time_count ++ ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("//no specific time to run -> do nothing");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("add_time(1) ;");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public void dec_time() {\t\t\t");
    _builder.newLine();
    {
      if (Data.runTime) {
        {
          DefCore _defcore_3 = Data.schModel.getDefcore();
          boolean _notEquals_15 = (!Objects.equal(_defcore_3, null));
          if (_notEquals_15) {
            _builder.append("\t");
            _builder.append("if (_time_slice[current_core] != 0) { ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (_time_count[current_core] == 0)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_time_count[current_core] = _time_slice[current_core] - 1 ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("else");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_time_count[current_core] -- ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("if (_time_slice != 0) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (_time_count == 0)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_time_count = _time_slice - 1 ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("else");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_time_count -- ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("//no specific time to run -> do nothing");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("sub_time(1) ;");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public void add_time(int time) {");
    _builder.newLine();
    {
      VarDef _svar = sch.getSvar();
      boolean _notEquals_16 = (!Objects.equal(_svar, null));
      if (_notEquals_16) {
        {
          VarDef _svar_1 = sch.getSvar();
          EList<VarDecl> _vard = _svar_1.getVard();
          boolean _notEquals_17 = (!Objects.equal(_vard, null));
          if (_notEquals_17) {
            {
              VarDef _svar_2 = sch.getSvar();
              EList<VarDecl> _vard_1 = _svar_2.getVard();
              for(final VarDecl v : _vard_1) {
                {
                  VarBlockDef _varblockdef = v.getVarblockdef();
                  boolean _notEquals_18 = (!Objects.equal(_varblockdef, null));
                  if (_notEquals_18) {
                    {
                      VarBlockDef _varblockdef_1 = v.getVarblockdef();
                      EList<VarDefinition> _vardef = _varblockdef_1.getVardef();
                      for(final VarDefinition vas : _vardef) {
                        {
                          scheduling.dsl.String _type = vas.getType();
                          String _string = _type.toString();
                          String _trim = _string.trim();
                          boolean _equals_5 = Objects.equal(_trim, "clock");
                          if (_equals_5) {
                            _builder.append("\t");
                            _builder.append("//clock variable");
                            _builder.newLine();
                            {
                              EList<VarName> _name_16 = vas.getName();
                              for(final VarName va : _name_16) {
                                _builder.append("\t");
                                String _name_17 = va.getName();
                                _builder.append(_name_17, "\t");
                                _builder.append(" += time ;");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        }
                      }
                    }
                  } else {
                    {
                      VarSingleDef _varsingledef = v.getVarsingledef();
                      VarDefinition _vardef_1 = _varsingledef.getVardef();
                      scheduling.dsl.String _type_1 = _vardef_1.getType();
                      String _string_1 = _type_1.toString();
                      String _trim_1 = _string_1.trim();
                      boolean _equals_6 = Objects.equal(_trim_1, "clock");
                      if (_equals_6) {
                        _builder.append("\t");
                        _builder.append("//clock variable");
                        _builder.newLine();
                        {
                          VarSingleDef _varsingledef_1 = v.getVarsingledef();
                          VarDefinition _vardef_2 = _varsingledef_1.getVardef();
                          EList<VarName> _name_18 = _vardef_2.getName();
                          for(final VarName va_1 : _name_18) {
                            _builder.append("\t");
                            String _name_19 = va_1.getName();
                            _builder.append(_name_19, "\t");
                            _builder.append(" += time ;");
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
    _builder.append("\t");
    _builder.append("//clock for periodic process\t\t\t\t");
    _builder.newLine();
    {
      Set<String> _keySet = Data.periodicClockProperties.keySet();
      for(final String pC : _keySet) {
        {
          String _get = Data.periodicClockOffset.get(pC);
          boolean _equals_7 = _get.equals("0");
          if (_equals_7) {
            _builder.append("\t");
            _builder.append(pC, "\t");
            _builder.append(" += time ; //offset = 0");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("if (");
            _builder.append(pC, "\t");
            _builder.append("_offset < ");
            String _get_1 = Data.periodicClockOffset.get(pC);
            _builder.append(_get_1, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append(pC, "\t\t");
            _builder.append("_offset += time ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("if (");
            _builder.append(pC, "\t");
            _builder.append("_offset == ");
            String _get_2 = Data.periodicClockOffset.get(pC);
            _builder.append(_get_2, "\t");
            _builder.append(")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append(pC, "\t\t");
            _builder.append(" += time ;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      for(final String col : Data.collectionList) {
        _builder.append(col, "");
        _builder.append(".add_time(time) ;\t\t\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("if (running_process != null)");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("running_process.add_time(time) ;\t");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public void sub_time(int time) {");
    _builder.newLine();
    {
      VarDef _svar_3 = sch.getSvar();
      boolean _notEquals_19 = (!Objects.equal(_svar_3, null));
      if (_notEquals_19) {
        {
          VarDef _svar_4 = sch.getSvar();
          EList<VarDecl> _vard_2 = _svar_4.getVard();
          boolean _notEquals_20 = (!Objects.equal(_vard_2, null));
          if (_notEquals_20) {
            {
              VarDef _svar_5 = sch.getSvar();
              EList<VarDecl> _vard_3 = _svar_5.getVard();
              for(final VarDecl vas_1 : _vard_3) {
                {
                  VarBlockDef _varblockdef_2 = vas_1.getVarblockdef();
                  boolean _notEquals_21 = (!Objects.equal(_varblockdef_2, null));
                  if (_notEquals_21) {
                    {
                      VarBlockDef _varblockdef_3 = vas_1.getVarblockdef();
                      EList<VarDefinition> _vardef_3 = _varblockdef_3.getVardef();
                      for(final VarDefinition v_1 : _vardef_3) {
                        {
                          scheduling.dsl.String _type_2 = v_1.getType();
                          String _string_2 = _type_2.toString();
                          String _trim_2 = _string_2.trim();
                          boolean _equals_8 = Objects.equal(_trim_2, "clock");
                          if (_equals_8) {
                            _builder.append("\t");
                            _builder.append("//clock variable");
                            _builder.newLine();
                            {
                              EList<VarName> _name_20 = v_1.getName();
                              for(final VarName va_2 : _name_20) {
                                _builder.append("\t");
                                String _name_21 = va_2.getName();
                                _builder.append(_name_21, "\t");
                                _builder.append(" -= time ;");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        }
                      }
                    }
                  } else {
                    {
                      VarSingleDef _varsingledef_2 = vas_1.getVarsingledef();
                      VarDefinition _vardef_4 = _varsingledef_2.getVardef();
                      scheduling.dsl.String _type_3 = _vardef_4.getType();
                      String _string_3 = _type_3.toString();
                      String _trim_3 = _string_3.trim();
                      boolean _equals_9 = Objects.equal(_trim_3, "clock");
                      if (_equals_9) {
                        _builder.append("\t");
                        _builder.append("//clock variable");
                        _builder.newLine();
                        {
                          VarSingleDef _varsingledef_3 = vas_1.getVarsingledef();
                          VarDefinition _vardef_5 = _varsingledef_3.getVardef();
                          EList<VarName> _name_22 = _vardef_5.getName();
                          for(final VarName va_3 : _name_22) {
                            _builder.append("\t");
                            String _name_23 = va_3.getName();
                            _builder.append(_name_23, "\t");
                            _builder.append(" -= time ;");
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
    _builder.append("\t");
    _builder.append("//clock for periodic process\t\t\t\t");
    _builder.newLine();
    {
      Set<String> _keySet_1 = Data.periodicClockProperties.keySet();
      for(final String pC_1 : _keySet_1) {
        _builder.append("\t");
        _builder.append("if (");
        _builder.append(pC_1, "\t");
        _builder.append("_offset >= ");
        String _get_3 = Data.periodicClockOffset.get(pC_1);
        _builder.append(_get_3, "\t");
        _builder.append(")");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append(pC_1, "\t\t");
        _builder.append(" -= time ;");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      for(final String col_1 : Data.collectionList) {
        _builder.append(col_1, "");
        _builder.append(".sub_time(time) ;\t\t\t\t");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("if (running_process != null)");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("running_process.sub_time(time) ;");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public boolean check_running_time_to_put_running_process(){");
    _builder.newLine();
    _builder.append("\t");
    boolean hasSelect = false;
    _builder.append("\t");
    _builder.newLineIfNotEmpty();
    {
      String _parent_7 = sch.getParent();
      boolean _notEquals_22 = (!Objects.equal(_parent_7, null));
      if (_notEquals_22) {
        {
          HandlerDef _handler = sch.getHandler();
          boolean _notEquals_23 = (!Objects.equal(_handler, null));
          if (_notEquals_23) {
            {
              HandlerDef _handler_1 = sch.getHandler();
              EList<EventDef> _event = _handler_1.getEvent();
              for(final EventDef event : _event) {
                {
                  scheduling.dsl.String _eventname = event.getEventname();
                  String _string_4 = _eventname.toString();
                  String _trim_4 = _string_4.trim();
                  boolean _equals_10 = _trim_4.equals("select_process");
                  if (_equals_10) {
                    _builder.append("\t");
                    _builder.append("//change select behavior: ");
                    _builder.append(hasSelect = true, "\t");
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
      if ((hasSelect || Objects.equal(sch.getParent(), null))) {
        {
          if (hasSelect) {
            _builder.append("\t");
            _builder.append("//child changes the behavior of its super");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("//if scheduler use time slice, just put the running process into process collection after running time\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("//need to call select_process(-1)\t");
        _builder.newLine();
        {
          if (Data.runTime) {
            {
              DefCore _defcore_4 = Data.schModel.getDefcore();
              boolean _notEquals_24 = (!Objects.equal(_defcore_4, null));
              if (_notEquals_24) {
                _builder.append("\t");
                _builder.append("if (_putColIndex[current_core] != null) {");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("//for putting the running process to the destination collection");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("//need to call select_process_set() to get other process");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("if (_time_count[current_core] == _time_slice[current_core] && _time_slice[current_core] > 0) { ");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t");
                _builder.append("if (running_process != null){");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t\t");
                _builder.append("for (byte colID : _putColIndex[current_core])");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t\t\t");
                _builder.append("put_running_process(colID) ;\t\t\t\t\t\t");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t\t");
                _builder.append("//_runningSet.dataSet.clear();");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t\t");
                _builder.append("running_process = null ;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t\t");
                _builder.append("running_procs[current_core] = null ;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t\t");
                _builder.append("return true ;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t");
                _builder.append("}");
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
                _builder.append("if (_putColIndex != -1) {");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("//for putting the running process to the destination collection");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("//need to call select_process_set() to get other process");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("if (_time_count == _time_slice && _time_slice > 0) { ");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t");
                _builder.append("if (running_process != null){");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t\t");
                _builder.append("put_running_process(_putColIndex) ;\t\t\t\t\t\t");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t\t");
                _builder.append("//_runningSet.dataSet.clear();");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t\t");
                _builder.append("running_process = null ;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t\t");
                _builder.append("return true ;");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t\t");
                _builder.append("}");
                _builder.newLine();
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
        _builder.append("\t");
        _builder.append("return false ; //process still running");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("//child follows the behavior of its super");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return super.check_running_time_to_put_running_process() ;");
        _builder.newLine();
      }
    }
    _builder.append("}\t\t");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static String getPeriodicProcessKey(final Element e) {
    scheduling.dsl.Process _process = e.getProcess();
    String processKey = _process.getName();
    EList<Value> _paraAssign = e.getParaAssign();
    for (final Value value : _paraAssign) {
      NumValue _num = value.getNum();
      boolean _notEquals = (!Objects.equal(_num, null));
      if (_notEquals) {
        String _processKey = processKey;
        NumValue _num_1 = value.getNum();
        int _value = _num_1.getValue();
        String _plus = ("_" + Integer.valueOf(_value));
        String _plus_1 = (_plus + "_");
        processKey = (_processKey + _plus_1);
      } else {
        String _processKey_1 = processKey;
        BoolValue _bool = value.getBool();
        String _value_1 = _bool.getValue();
        String _plus_2 = ("_" + _value_1);
        String _plus_3 = (_plus_2 + "_");
        processKey = (_processKey_1 + _plus_3);
      }
    }
    return processKey;
  }
  
  public static String getType(final String type) {
    String _trim = type.trim();
    boolean _equals = Objects.equal(_trim, "clock");
    if (_equals) {
      return "int";
    }
    String _trim_1 = type.trim();
    boolean _equals_1 = Objects.equal(_trim_1, "bool");
    if (_equals_1) {
      return "boolean";
    }
    String _trim_2 = type.trim();
    boolean _equals_2 = Objects.equal(_trim_2, "byte");
    if (_equals_2) {
      return "byte";
    }
    String _trim_3 = type.trim();
    boolean _equals_3 = Objects.equal(_trim_3, "int");
    if (_equals_3) {
      return "int";
    }
    String _trim_4 = type.trim();
    boolean _equals_4 = Objects.equal(_trim_4, "byte");
    if (_equals_4) {
      return "byte";
    }
    return "error type";
  }
  
  public static CharSequence genDataStruture(final SchedulerDef sch) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//--------------- data structure ---------------------------");
    _builder.newLine();
    {
      ParameterList _parameterlist = sch.getParameterlist();
      boolean _notEquals = (!Objects.equal(_parameterlist, null));
      if (_notEquals) {
        _builder.append("//scheduler parameters");
        _builder.newLine();
        {
          ParameterList _parameterlist_1 = sch.getParameterlist();
          EList<ParameterAssign> _para = _parameterlist_1.getPara();
          for(final ParameterAssign para : _para) {
            {
              EList<ParameterName> _paraname = para.getParaname();
              for(final ParameterName p : _paraname) {
                scheduling.dsl.String _type = para.getType();
                String _string = _type.toString();
                String _type_1 = SchedulerGenerator.getType(_string);
                _builder.append(_type_1, "");
                _builder.append("\t");
                String _name = p.getName();
                _builder.append(_name, "");
                _builder.append(" = ");
                String _name_1 = p.getName();
                ParameterList _parameterlist_2 = sch.getParameterlist();
                EList<ParameterAssign> _para_1 = _parameterlist_2.getPara();
                String _findValueInParameterList = Utilities.findValueInParameterList(_name_1, _para_1);
                _builder.append(_findValueInParameterList, "");
                _builder.append(";");
                _builder.newLineIfNotEmpty();
              }
            }
          }
        }
      }
    }
    {
      String _parent = sch.getParent();
      boolean _equals = Objects.equal(_parent, null);
      if (_equals) {
        {
          DefCore _defcore = Data.schModel.getDefcore();
          boolean _notEquals_1 = (!Objects.equal(_defcore, null));
          if (_notEquals_1) {
            _builder.append("public SchedulerProcess running_procs[] = new SchedulerProcess[");
            DefCore _defcore_1 = Data.schModel.getDefcore();
            int _numcore = _defcore_1.getNumcore();
            _builder.append(_numcore, "");
            _builder.append("] ;");
            _builder.newLineIfNotEmpty();
          }
        }
        _builder.append("public SchedulerProcess running_process ;\t");
        _builder.newLine();
        _builder.newLine();
        _builder.append("\t\t");
        _builder.newLine();
        _builder.append("public int size = 0 ;");
        _builder.newLine();
        {
          DefCore _defcore_2 = Data.schModel.getDefcore();
          boolean _notEquals_2 = (!Objects.equal(_defcore_2, null));
          if (_notEquals_2) {
            _builder.append("public ArrayList<Byte>[] _putColIndex = (ArrayList<Byte>[]) new ArrayList [");
            DefCore _defcore_3 = Data.schModel.getDefcore();
            int _numcore_1 = _defcore_3.getNumcore();
            _builder.append(_numcore_1, "");
            _builder.append("] ; //for replacement");
            _builder.newLineIfNotEmpty();
            _builder.append("public RunningSet _runningSets[] = new RunningSet[");
            DefCore _defcore_4 = Data.schModel.getDefcore();
            int _numcore_2 = _defcore_4.getNumcore();
            _builder.append(_numcore_2, "");
            _builder.append("];");
            _builder.newLineIfNotEmpty();
            _builder.append("public RunningSet _runningSet; //temporary store the running set\t\t\t\t");
            _builder.newLine();
          } else {
            _builder.append("public byte _putColIndex = -1; //for replacement");
            _builder.newLine();
            _builder.append("public RunningSet _runningSet; //temporary store the running set");
            _builder.newLine();
          }
        }
      }
    }
    {
      if (Data.runTime) {
        {
          DefCore _defcore_5 = Data.schModel.getDefcore();
          boolean _notEquals_3 = (!Objects.equal(_defcore_5, null));
          if (_notEquals_3) {
            _builder.append("public int _time_count[] = new int[");
            DefCore _defcore_6 = Data.schModel.getDefcore();
            int _numcore_3 = _defcore_6.getNumcore();
            _builder.append(_numcore_3, "");
            _builder.append("] ;");
            _builder.newLineIfNotEmpty();
            _builder.append("public int _time_slice[] = new int[");
            DefCore _defcore_7 = Data.schModel.getDefcore();
            int _numcore_4 = _defcore_7.getNumcore();
            _builder.append(_numcore_4, "");
            _builder.append("] ;");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("public int _time_count = 0 ;");
            _builder.newLine();
            _builder.append("public int _time_slice = 0 ;");
            _builder.newLine();
          }
        }
        _builder.append("//public int _time = 0 ;\t\t\t\t\t");
        _builder.newLine();
      }
    }
    {
      VarDef _svar = sch.getSvar();
      boolean _notEquals_4 = (!Objects.equal(_svar, null));
      if (_notEquals_4) {
        {
          VarDef _svar_1 = sch.getSvar();
          EList<VarDecl> _vard = _svar_1.getVard();
          boolean _notEquals_5 = (!Objects.equal(_vard, null));
          if (_notEquals_5) {
            _builder.append("//scheduler variables ");
            ArrayList<String> varlist = new ArrayList<String>();
            _builder.newLineIfNotEmpty();
            {
              VarDef _svar_2 = sch.getSvar();
              EList<VarDecl> _vard_1 = _svar_2.getVard();
              for(final VarDecl v : _vard_1) {
                {
                  IfDef _ifdef = v.getIfdef();
                  boolean _equals_1 = Objects.equal(_ifdef, null);
                  if (_equals_1) {
                    {
                      VarBlockDef _varblockdef = v.getVarblockdef();
                      boolean _notEquals_6 = (!Objects.equal(_varblockdef, null));
                      if (_notEquals_6) {
                        {
                          VarBlockDef _varblockdef_1 = v.getVarblockdef();
                          EList<VarDefinition> _vardef = _varblockdef_1.getVardef();
                          for(final VarDefinition vas : _vardef) {
                            {
                              EList<VarName> _name_2 = vas.getName();
                              boolean _contains = varlist.contains(_name_2);
                              boolean _not = (!_contains);
                              if (_not) {
                                {
                                  scheduling.dsl.String _type_2 = vas.getType();
                                  String _string_1 = _type_2.toString();
                                  String _trim = _string_1.trim();
                                  boolean _equals_2 = Objects.equal(_trim, "bool");
                                  if (_equals_2) {
                                    _builder.append("public boolean ");
                                    {
                                      BoolValue _bvalue = vas.getBvalue();
                                      boolean _equals_3 = Objects.equal(_bvalue, null);
                                      if (_equals_3) {
                                        EList<VarName> _name_3 = vas.getName();
                                        final Function1<VarName, String> _function = (VarName it) -> {
                                          return it.getName();
                                        };
                                        List<String> _map = ListExtensions.<VarName, String>map(_name_3, _function);
                                        String _join = IterableExtensions.join(_map, ", ");
                                        String _setInitValue = Utilities.setInitValue(_join, "");
                                        _builder.append(_setInitValue, "");
                                      } else {
                                        EList<VarName> _name_4 = vas.getName();
                                        final Function1<VarName, String> _function_1 = (VarName it) -> {
                                          return it.getName();
                                        };
                                        List<String> _map_1 = ListExtensions.<VarName, String>map(_name_4, _function_1);
                                        String _join_1 = IterableExtensions.join(_map_1, ", ");
                                        BoolValue _bvalue_1 = vas.getBvalue();
                                        String _value = _bvalue_1.getValue();
                                        String _string_2 = _value.toString();
                                        String _setInitValue_1 = Utilities.setInitValue(_join_1, _string_2);
                                        _builder.append(_setInitValue_1, "");
                                      }
                                    }
                                  }
                                }
                                _builder.newLineIfNotEmpty();
                                {
                                  scheduling.dsl.String _type_3 = vas.getType();
                                  String _string_3 = _type_3.toString();
                                  String _trim_1 = _string_3.trim();
                                  boolean _equals_4 = Objects.equal(_trim_1, "time");
                                  if (_equals_4) {
                                    _builder.append("public int ");
                                    EList<VarName> _name_5 = vas.getName();
                                    final Function1<VarName, String> _function_2 = (VarName it) -> {
                                      return it.getName();
                                    };
                                    List<String> _map_2 = ListExtensions.<VarName, String>map(_name_5, _function_2);
                                    String _join_2 = IterableExtensions.join(_map_2, ", ");
                                    NumValue _ivalue = vas.getIvalue();
                                    String _genNumber = SchedulerGenerator.genNumber(_ivalue);
                                    String _setInitValue_2 = Utilities.setInitValue(_join_2, _genNumber);
                                    _builder.append(_setInitValue_2, "");
                                    _builder.append(" ");
                                  }
                                }
                                _builder.append("\t\t\t\t\t\t");
                                _builder.newLineIfNotEmpty();
                                {
                                  if (((!Objects.equal(vas.getType().toString().trim(), "bool")) && (!Objects.equal(vas.getType().toString().trim(), "time")))) {
                                    _builder.append("public int ");
                                    EList<VarName> _name_6 = vas.getName();
                                    final Function1<VarName, String> _function_3 = (VarName it) -> {
                                      return it.getName();
                                    };
                                    List<String> _map_3 = ListExtensions.<VarName, String>map(_name_6, _function_3);
                                    String _join_3 = IterableExtensions.join(_map_3, ", ");
                                    NumValue _ivalue_1 = vas.getIvalue();
                                    String _genNumber_1 = SchedulerGenerator.genNumber(_ivalue_1);
                                    String _setInitValue_3 = Utilities.setInitValue(_join_3, _genNumber_1);
                                    _builder.append(_setInitValue_3, "");
                                  }
                                }
                                _builder.append(" //int or clock variable");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        }
                      } else {
                        VarSingleDef _varsingledef = v.getVarsingledef();
                        VarDefinition vas_1 = _varsingledef.getVardef();
                        _builder.newLineIfNotEmpty();
                        {
                          EList<VarName> _name_7 = vas_1.getName();
                          boolean _contains_1 = varlist.contains(_name_7);
                          boolean _not_1 = (!_contains_1);
                          if (_not_1) {
                            {
                              scheduling.dsl.String _type_4 = vas_1.getType();
                              String _string_4 = _type_4.toString();
                              String _trim_2 = _string_4.trim();
                              boolean _equals_5 = Objects.equal(_trim_2, "bool");
                              if (_equals_5) {
                                _builder.append("public boolean ");
                                {
                                  BoolValue _bvalue_2 = vas_1.getBvalue();
                                  boolean _equals_6 = Objects.equal(_bvalue_2, null);
                                  if (_equals_6) {
                                    EList<VarName> _name_8 = vas_1.getName();
                                    final Function1<VarName, String> _function_4 = (VarName it) -> {
                                      return it.getName();
                                    };
                                    List<String> _map_4 = ListExtensions.<VarName, String>map(_name_8, _function_4);
                                    String _join_4 = IterableExtensions.join(_map_4, ", ");
                                    String _setInitValue_4 = Utilities.setInitValue(_join_4, "");
                                    _builder.append(_setInitValue_4, "");
                                  } else {
                                    EList<VarName> _name_9 = vas_1.getName();
                                    final Function1<VarName, String> _function_5 = (VarName it) -> {
                                      return it.getName();
                                    };
                                    List<String> _map_5 = ListExtensions.<VarName, String>map(_name_9, _function_5);
                                    String _join_5 = IterableExtensions.join(_map_5, ", ");
                                    BoolValue _bvalue_3 = vas_1.getBvalue();
                                    String _value_1 = _bvalue_3.getValue();
                                    String _string_5 = _value_1.toString();
                                    String _setInitValue_5 = Utilities.setInitValue(_join_5, _string_5);
                                    _builder.append(_setInitValue_5, "");
                                  }
                                }
                              }
                            }
                            _builder.newLineIfNotEmpty();
                            {
                              scheduling.dsl.String _type_5 = vas_1.getType();
                              String _string_6 = _type_5.toString();
                              String _trim_3 = _string_6.trim();
                              boolean _equals_7 = Objects.equal(_trim_3, "time");
                              if (_equals_7) {
                                _builder.append("public int ");
                                EList<VarName> _name_10 = vas_1.getName();
                                final Function1<VarName, String> _function_6 = (VarName it) -> {
                                  return it.getName();
                                };
                                List<String> _map_6 = ListExtensions.<VarName, String>map(_name_10, _function_6);
                                String _join_6 = IterableExtensions.join(_map_6, ", ");
                                NumValue _ivalue_2 = vas_1.getIvalue();
                                String _genNumber_2 = SchedulerGenerator.genNumber(_ivalue_2);
                                String _setInitValue_6 = Utilities.setInitValue(_join_6, _genNumber_2);
                                _builder.append(_setInitValue_6, "");
                                _builder.append(" ");
                              }
                            }
                            _builder.append("\t\t\t\t\t\t\t\t\t\t\t\t\t\t");
                            _builder.newLineIfNotEmpty();
                            {
                              if (((!Objects.equal(vas_1.getType().toString().trim(), "bool")) && (!Objects.equal(vas_1.getType().toString().trim(), "time")))) {
                                _builder.append("public int ");
                                EList<VarName> _name_11 = vas_1.getName();
                                final Function1<VarName, String> _function_7 = (VarName it) -> {
                                  return it.getName();
                                };
                                List<String> _map_7 = ListExtensions.<VarName, String>map(_name_11, _function_7);
                                String _join_7 = IterableExtensions.join(_map_7, ", ");
                                NumValue _ivalue_3 = vas_1.getIvalue();
                                String _genNumber_3 = SchedulerGenerator.genNumber(_ivalue_3);
                                String _setInitValue_7 = Utilities.setInitValue(_join_7, _genNumber_3);
                                _builder.append(_setInitValue_7, "");
                              }
                            }
                            _builder.append(" //int, clock or temp variable\t\t\t\t\t\t\t\t");
                            _builder.newLineIfNotEmpty();
                          }
                        }
                      }
                    }
                  } else {
                    {
                      VarBlockDef _varblockdef_2 = v.getVarblockdef();
                      boolean _notEquals_7 = (!Objects.equal(_varblockdef_2, null));
                      if (_notEquals_7) {
                        {
                          VarBlockDef _varblockdef_3 = v.getVarblockdef();
                          EList<VarDefinition> _vardef_1 = _varblockdef_3.getVardef();
                          for(final VarDefinition vas_2 : _vardef_1) {
                            {
                              EList<VarName> _name_12 = vas_2.getName();
                              for(final VarName vs : _name_12) {
                                {
                                  String _name_13 = vs.getName();
                                  boolean _contains_2 = varlist.contains(_name_13);
                                  boolean _not_2 = (!_contains_2);
                                  if (_not_2) {
                                    _builder.append("//add ");
                                    String _name_14 = vs.getName();
                                    _builder.append(_name_14, "");
                                    _builder.append(": ");
                                    String _name_15 = vs.getName();
                                    boolean _add = varlist.add(_name_15);
                                    _builder.append(_add, "");
                                    _builder.append("\t\t\t\t\t\t\t");
                                    _builder.newLineIfNotEmpty();
                                    {
                                      scheduling.dsl.String _type_6 = vas_2.getType();
                                      String _string_7 = _type_6.toString();
                                      String _trim_4 = _string_7.trim();
                                      boolean _equals_8 = Objects.equal(_trim_4, "bool");
                                      if (_equals_8) {
                                        _builder.append("public boolean ");
                                        String _name_16 = vs.getName();
                                        _builder.append(_name_16, "");
                                        _builder.append(" ;//ifdef");
                                      }
                                    }
                                    _builder.append(" ");
                                    _builder.newLineIfNotEmpty();
                                    {
                                      scheduling.dsl.String _type_7 = vas_2.getType();
                                      String _string_8 = _type_7.toString();
                                      String _trim_5 = _string_8.trim();
                                      boolean _equals_9 = Objects.equal(_trim_5, "time");
                                      if (_equals_9) {
                                        _builder.append("public int ");
                                        String _name_17 = vs.getName();
                                        _builder.append(_name_17, "");
                                        _builder.append(" ;//ifdef");
                                      }
                                    }
                                    _builder.newLineIfNotEmpty();
                                    {
                                      if (((!Objects.equal(vas_2.getType().toString().trim(), "bool")) && (!Objects.equal(vas_2.getType().toString().trim(), "time")))) {
                                        _builder.append("public int ");
                                        String _name_18 = vs.getName();
                                        _builder.append(_name_18, "");
                                        _builder.append(" ;//ifdef");
                                      }
                                    }
                                    _builder.append("\t\t\t\t\t\t\t\t\t\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                      } else {
                        VarSingleDef _varsingledef_1 = v.getVarsingledef();
                        VarDefinition vas_3 = _varsingledef_1.getVardef();
                        _builder.newLineIfNotEmpty();
                        {
                          EList<VarName> _name_19 = vas_3.getName();
                          for(final VarName vs_1 : _name_19) {
                            {
                              String _name_20 = vs_1.getName();
                              boolean _contains_3 = varlist.contains(_name_20);
                              boolean _not_3 = (!_contains_3);
                              if (_not_3) {
                                _builder.append("//add ");
                                String _name_21 = vs_1.getName();
                                _builder.append(_name_21, "");
                                _builder.append(": ");
                                String _name_22 = vs_1.getName();
                                boolean _add_1 = varlist.add(_name_22);
                                _builder.append(_add_1, "");
                                _builder.append("\t\t\t\t\t\t\t");
                                _builder.newLineIfNotEmpty();
                                {
                                  scheduling.dsl.String _type_8 = vas_3.getType();
                                  String _string_9 = _type_8.toString();
                                  String _trim_6 = _string_9.trim();
                                  boolean _equals_10 = Objects.equal(_trim_6, "bool");
                                  if (_equals_10) {
                                    _builder.append("public boolean ");
                                    String _name_23 = vs_1.getName();
                                    _builder.append(_name_23, "");
                                    _builder.append(" ;//ifdef");
                                  }
                                }
                                _builder.append(" ");
                                _builder.newLineIfNotEmpty();
                                {
                                  scheduling.dsl.String _type_9 = vas_3.getType();
                                  String _string_10 = _type_9.toString();
                                  String _trim_7 = _string_10.trim();
                                  boolean _equals_11 = Objects.equal(_trim_7, "time");
                                  if (_equals_11) {
                                    _builder.append("public int ");
                                    String _name_24 = vs_1.getName();
                                    _builder.append(_name_24, "");
                                    _builder.append(" ;//ifdef");
                                  }
                                }
                                _builder.newLineIfNotEmpty();
                                {
                                  if (((!Objects.equal(vas_3.getType().toString().trim(), "bool")) && (!Objects.equal(vas_3.getType().toString().trim(), "time")))) {
                                    _builder.append("public int ");
                                    String _name_25 = vs_1.getName();
                                    _builder.append(_name_25, "");
                                    _builder.append(" ;//ifdef");
                                  }
                                }
                                _builder.append("\t\t\t\t\t\t\t\t\t\t");
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
      }
    }
    {
      Set<String> _keySet = Data.periodicClockProperties.keySet();
      for(final String pClock : _keySet) {
        _builder.append("//periodic process clock ?");
        _builder.newLine();
        _builder.append("public int ");
        _builder.append(pClock, "");
        _builder.append(" = -1;");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      Set<String> _keySet_1 = Data.periodicClockOffset.keySet();
      for(final String pClock_1 : _keySet_1) {
        _builder.append("//offset for periodic process");
        _builder.newLine();
        {
          int _length = pClock_1.length();
          boolean _notEquals_8 = (_length != 0);
          if (_notEquals_8) {
            _builder.append("public int ");
            _builder.append(pClock_1, "");
            _builder.append("_offset ;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      SchedulerDataDef _schedulerdata = sch.getSchedulerdata();
      boolean _notEquals_9 = (!Objects.equal(_schedulerdata, null));
      if (_notEquals_9) {
        ArrayList<String> varlist_1 = new ArrayList<String>();
        _builder.newLineIfNotEmpty();
        {
          SchedulerDataDef _schedulerdata_1 = sch.getSchedulerdata();
          EList<DataDef> _datadef = _schedulerdata_1.getDatadef();
          boolean _notEquals_10 = (!Objects.equal(_datadef, null));
          if (_notEquals_10) {
            {
              SchedulerDataDef _schedulerdata_2 = sch.getSchedulerdata();
              EList<DataDef> _datadef_1 = _schedulerdata_2.getDatadef();
              for(final DataDef schdata : _datadef_1) {
                {
                  DataBlockDef _datablockdef = schdata.getDatablockdef();
                  boolean _notEquals_11 = (!Objects.equal(_datablockdef, null));
                  if (_notEquals_11) {
                    {
                      DataBlockDef _datablockdef_1 = schdata.getDatablockdef();
                      EList<DataSingleDef> _datadef_2 = _datablockdef_1.getDatadef();
                      for(final DataSingleDef schdat : _datadef_2) {
                        {
                          SchedulerPropertyDef _prop = schdat.getProp();
                          boolean _notEquals_12 = (!Objects.equal(_prop, null));
                          if (_notEquals_12) {
                            SchedulerPropertyDef vas_4 = schdat.getProp();
                            _builder.newLineIfNotEmpty();
                            {
                              IfDef _ifdef_1 = schdata.getIfdef();
                              boolean _equals_12 = Objects.equal(_ifdef_1, null);
                              if (_equals_12) {
                                {
                                  EList<SchedulerPropertyName> _name_26 = vas_4.getName();
                                  boolean _contains_4 = varlist_1.contains(_name_26);
                                  boolean _not_4 = (!_contains_4);
                                  if (_not_4) {
                                    {
                                      scheduling.dsl.String _type_10 = vas_4.getType();
                                      String _string_11 = _type_10.toString();
                                      String _trim_8 = _string_11.trim();
                                      boolean _equals_13 = Objects.equal(_trim_8, "bool");
                                      if (_equals_13) {
                                        _builder.append("public boolean ");
                                        {
                                          BoolValue _bvalue_4 = vas_4.getBvalue();
                                          boolean _equals_14 = Objects.equal(_bvalue_4, null);
                                          if (_equals_14) {
                                            EList<SchedulerPropertyName> _name_27 = vas_4.getName();
                                            final Function1<SchedulerPropertyName, String> _function_8 = (SchedulerPropertyName it) -> {
                                              return it.getName();
                                            };
                                            List<String> _map_8 = ListExtensions.<SchedulerPropertyName, String>map(_name_27, _function_8);
                                            String _join_8 = IterableExtensions.join(_map_8, ", ");
                                            String _setInitValue_8 = Utilities.setInitValue(_join_8, "");
                                            _builder.append(_setInitValue_8, "");
                                          } else {
                                            EList<SchedulerPropertyName> _name_28 = vas_4.getName();
                                            final Function1<SchedulerPropertyName, String> _function_9 = (SchedulerPropertyName it) -> {
                                              return it.getName();
                                            };
                                            List<String> _map_9 = ListExtensions.<SchedulerPropertyName, String>map(_name_28, _function_9);
                                            String _join_9 = IterableExtensions.join(_map_9, ", ");
                                            BoolValue _bvalue_5 = vas_4.getBvalue();
                                            String _value_2 = _bvalue_5.getValue();
                                            String _string_12 = _value_2.toString();
                                            String _setInitValue_9 = Utilities.setInitValue(_join_9, _string_12);
                                            _builder.append(_setInitValue_9, "");
                                          }
                                        }
                                      }
                                    }
                                    _builder.newLineIfNotEmpty();
                                    {
                                      scheduling.dsl.String _type_11 = vas_4.getType();
                                      String _string_13 = _type_11.toString();
                                      String _trim_9 = _string_13.trim();
                                      boolean _equals_15 = Objects.equal(_trim_9, "time");
                                      if (_equals_15) {
                                        _builder.append("public int ");
                                        EList<SchedulerPropertyName> _name_29 = vas_4.getName();
                                        final Function1<SchedulerPropertyName, String> _function_10 = (SchedulerPropertyName it) -> {
                                          return it.getName();
                                        };
                                        List<String> _map_10 = ListExtensions.<SchedulerPropertyName, String>map(_name_29, _function_10);
                                        String _join_10 = IterableExtensions.join(_map_10, ", ");
                                        NumValue _ivalue_4 = vas_4.getIvalue();
                                        String _genNumber_4 = SchedulerGenerator.genNumber(_ivalue_4);
                                        String _setInitValue_10 = Utilities.setInitValue(_join_10, _genNumber_4);
                                        _builder.append(_setInitValue_10, "");
                                        _builder.append(" ");
                                      }
                                    }
                                    _builder.append("\t\t\t\t\t\t");
                                    _builder.newLineIfNotEmpty();
                                    {
                                      if (((!Objects.equal(vas_4.getType().toString().trim(), "bool")) && (!Objects.equal(vas_4.getType().toString().trim(), "time")))) {
                                        _builder.append("public int ");
                                        EList<SchedulerPropertyName> _name_30 = vas_4.getName();
                                        final Function1<SchedulerPropertyName, String> _function_11 = (SchedulerPropertyName it) -> {
                                          return it.getName();
                                        };
                                        List<String> _map_11 = ListExtensions.<SchedulerPropertyName, String>map(_name_30, _function_11);
                                        String _join_11 = IterableExtensions.join(_map_11, ", ");
                                        NumValue _ivalue_5 = vas_4.getIvalue();
                                        String _genNumber_5 = SchedulerGenerator.genNumber(_ivalue_5);
                                        String _setInitValue_11 = Utilities.setInitValue(_join_11, _genNumber_5);
                                        _builder.append(_setInitValue_11, "");
                                      }
                                    }
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              } else {
                                {
                                  EList<SchedulerPropertyName> _name_31 = vas_4.getName();
                                  for(final SchedulerPropertyName v_1 : _name_31) {
                                    {
                                      String _name_32 = v_1.getName();
                                      boolean _contains_5 = varlist_1.contains(_name_32);
                                      boolean _not_5 = (!_contains_5);
                                      if (_not_5) {
                                        _builder.append("//add ");
                                        String _name_33 = v_1.getName();
                                        _builder.append(_name_33, "");
                                        _builder.append(":");
                                        String _name_34 = v_1.getName();
                                        boolean _add_2 = varlist_1.add(_name_34);
                                        _builder.append(_add_2, "");
                                        _builder.append("\t\t\t\t\t");
                                        _builder.newLineIfNotEmpty();
                                        {
                                          scheduling.dsl.String _type_12 = vas_4.getType();
                                          String _string_14 = _type_12.toString();
                                          String _trim_10 = _string_14.trim();
                                          boolean _equals_16 = Objects.equal(_trim_10, "bool");
                                          if (_equals_16) {
                                            _builder.append("public boolean ");
                                            String _name_35 = v_1.getName();
                                            _builder.append(_name_35, "");
                                            _builder.append(" ;//ifdef");
                                          }
                                        }
                                        _builder.append(" ");
                                        _builder.newLineIfNotEmpty();
                                        {
                                          scheduling.dsl.String _type_13 = vas_4.getType();
                                          String _string_15 = _type_13.toString();
                                          String _trim_11 = _string_15.trim();
                                          boolean _equals_17 = Objects.equal(_trim_11, "time");
                                          if (_equals_17) {
                                            _builder.append("public int ");
                                            String _name_36 = v_1.getName();
                                            _builder.append(_name_36, "");
                                            _builder.append(" ;//ifdef");
                                          }
                                        }
                                        _builder.newLineIfNotEmpty();
                                        {
                                          if (((!Objects.equal(vas_4.getType().toString().trim(), "bool")) && (!Objects.equal(vas_4.getType().toString().trim(), "time")))) {
                                            _builder.append("public int ");
                                            String _name_37 = v_1.getName();
                                            _builder.append(_name_37, "");
                                            _builder.append(" ;//ifdef");
                                          }
                                        }
                                        _builder.append("\t\t");
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
                  } else {
                    {
                      DataSingleDef _datasingledef = schdata.getDatasingledef();
                      SchedulerPropertyDef _prop_1 = _datasingledef.getProp();
                      boolean _notEquals_13 = (!Objects.equal(_prop_1, null));
                      if (_notEquals_13) {
                        DataSingleDef _datasingledef_1 = schdata.getDatasingledef();
                        SchedulerPropertyDef vas_5 = _datasingledef_1.getProp();
                        _builder.newLineIfNotEmpty();
                        {
                          IfDef _ifdef_2 = schdata.getIfdef();
                          boolean _equals_18 = Objects.equal(_ifdef_2, null);
                          if (_equals_18) {
                            {
                              EList<SchedulerPropertyName> _name_38 = vas_5.getName();
                              boolean _contains_6 = varlist_1.contains(_name_38);
                              boolean _not_6 = (!_contains_6);
                              if (_not_6) {
                                {
                                  scheduling.dsl.String _type_14 = vas_5.getType();
                                  String _string_16 = _type_14.toString();
                                  String _trim_12 = _string_16.trim();
                                  boolean _equals_19 = Objects.equal(_trim_12, "bool");
                                  if (_equals_19) {
                                    _builder.append("public boolean ");
                                    {
                                      BoolValue _bvalue_6 = vas_5.getBvalue();
                                      boolean _equals_20 = Objects.equal(_bvalue_6, null);
                                      if (_equals_20) {
                                        EList<SchedulerPropertyName> _name_39 = vas_5.getName();
                                        final Function1<SchedulerPropertyName, String> _function_12 = (SchedulerPropertyName it) -> {
                                          return it.getName();
                                        };
                                        List<String> _map_12 = ListExtensions.<SchedulerPropertyName, String>map(_name_39, _function_12);
                                        String _join_12 = IterableExtensions.join(_map_12, ", ");
                                        String _setInitValue_12 = Utilities.setInitValue(_join_12, "");
                                        _builder.append(_setInitValue_12, "");
                                      } else {
                                        EList<SchedulerPropertyName> _name_40 = vas_5.getName();
                                        final Function1<SchedulerPropertyName, String> _function_13 = (SchedulerPropertyName it) -> {
                                          return it.getName();
                                        };
                                        List<String> _map_13 = ListExtensions.<SchedulerPropertyName, String>map(_name_40, _function_13);
                                        String _join_13 = IterableExtensions.join(_map_13, ", ");
                                        BoolValue _bvalue_7 = vas_5.getBvalue();
                                        String _value_3 = _bvalue_7.getValue();
                                        String _string_17 = _value_3.toString();
                                        String _setInitValue_13 = Utilities.setInitValue(_join_13, _string_17);
                                        _builder.append(_setInitValue_13, "");
                                      }
                                    }
                                  }
                                }
                                _builder.newLineIfNotEmpty();
                                {
                                  scheduling.dsl.String _type_15 = vas_5.getType();
                                  String _string_18 = _type_15.toString();
                                  String _trim_13 = _string_18.trim();
                                  boolean _equals_21 = Objects.equal(_trim_13, "time");
                                  if (_equals_21) {
                                    _builder.append("public int ");
                                    EList<SchedulerPropertyName> _name_41 = vas_5.getName();
                                    final Function1<SchedulerPropertyName, String> _function_14 = (SchedulerPropertyName it) -> {
                                      return it.getName();
                                    };
                                    List<String> _map_14 = ListExtensions.<SchedulerPropertyName, String>map(_name_41, _function_14);
                                    String _join_14 = IterableExtensions.join(_map_14, ", ");
                                    NumValue _ivalue_6 = vas_5.getIvalue();
                                    String _genNumber_6 = SchedulerGenerator.genNumber(_ivalue_6);
                                    String _setInitValue_14 = Utilities.setInitValue(_join_14, _genNumber_6);
                                    _builder.append(_setInitValue_14, "");
                                    _builder.append(" ");
                                  }
                                }
                                _builder.append("\t\t\t\t\t\t");
                                _builder.newLineIfNotEmpty();
                                {
                                  if (((!Objects.equal(vas_5.getType().toString().trim(), "bool")) && (!Objects.equal(vas_5.getType().toString().trim(), "time")))) {
                                    {
                                      NumValue _ivalue_7 = vas_5.getIvalue();
                                      boolean _notEquals_14 = (!Objects.equal(_ivalue_7, null));
                                      if (_notEquals_14) {
                                        _builder.append("public int ");
                                        EList<SchedulerPropertyName> _name_42 = vas_5.getName();
                                        final Function1<SchedulerPropertyName, String> _function_15 = (SchedulerPropertyName it) -> {
                                          return it.getName();
                                        };
                                        List<String> _map_15 = ListExtensions.<SchedulerPropertyName, String>map(_name_42, _function_15);
                                        String _join_15 = IterableExtensions.join(_map_15, ", ");
                                        NumValue _ivalue_8 = vas_5.getIvalue();
                                        String _genNumber_7 = SchedulerGenerator.genNumber(_ivalue_8);
                                        String _setInitValue_15 = Utilities.setInitValue(_join_15, _genNumber_7);
                                        _builder.append(_setInitValue_15, "");
                                        _builder.newLineIfNotEmpty();
                                      } else {
                                        _builder.append("public int ");
                                        EList<SchedulerPropertyName> _name_43 = vas_5.getName();
                                        final Function1<SchedulerPropertyName, String> _function_16 = (SchedulerPropertyName it) -> {
                                          return it.getName();
                                        };
                                        List<String> _map_16 = ListExtensions.<SchedulerPropertyName, String>map(_name_43, _function_16);
                                        String _join_16 = IterableExtensions.join(_map_16, ", ");
                                        _builder.append(_join_16, "");
                                        _builder.append(" ;");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          } else {
                            {
                              EList<SchedulerPropertyName> _name_44 = vas_5.getName();
                              for(final SchedulerPropertyName v_2 : _name_44) {
                                {
                                  String _name_45 = v_2.getName();
                                  boolean _contains_7 = varlist_1.contains(_name_45);
                                  boolean _not_7 = (!_contains_7);
                                  if (_not_7) {
                                    _builder.append("//add ");
                                    String _name_46 = v_2.getName();
                                    _builder.append(_name_46, "");
                                    _builder.append(":");
                                    String _name_47 = v_2.getName();
                                    boolean _add_3 = varlist_1.add(_name_47);
                                    _builder.append(_add_3, "");
                                    _builder.append("\t\t\t\t\t");
                                    _builder.newLineIfNotEmpty();
                                    {
                                      scheduling.dsl.String _type_16 = vas_5.getType();
                                      String _string_19 = _type_16.toString();
                                      String _trim_14 = _string_19.trim();
                                      boolean _equals_22 = Objects.equal(_trim_14, "bool");
                                      if (_equals_22) {
                                        _builder.append("public boolean ");
                                        String _name_48 = v_2.getName();
                                        _builder.append(_name_48, "");
                                        _builder.append(" ;//ifdef");
                                      }
                                    }
                                    _builder.append(" ");
                                    _builder.newLineIfNotEmpty();
                                    {
                                      scheduling.dsl.String _type_17 = vas_5.getType();
                                      String _string_20 = _type_17.toString();
                                      String _trim_15 = _string_20.trim();
                                      boolean _equals_23 = Objects.equal(_trim_15, "time");
                                      if (_equals_23) {
                                        _builder.append("public int ");
                                        String _name_49 = v_2.getName();
                                        _builder.append(_name_49, "");
                                        _builder.append(" ;//ifdef");
                                      }
                                    }
                                    _builder.newLineIfNotEmpty();
                                    {
                                      if (((!Objects.equal(vas_5.getType().toString().trim(), "bool")) && (!Objects.equal(vas_5.getType().toString().trim(), "time")))) {
                                        _builder.append("public int ");
                                        String _name_50 = v_2.getName();
                                        _builder.append(_name_50, "");
                                        _builder.append(" ;//ifdef");
                                      }
                                    }
                                    _builder.append("\t\t");
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
          }
        }
        {
          for(final String col : Data.collectionList) {
            _builder.append("ProcessCollectionBase ");
            _builder.append(col, "");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    return _builder;
  }
  
  public static String genNumber(final NumValue v) {
    String _minus = v.getMinus();
    boolean _equals = Objects.equal(_minus, null);
    if (_equals) {
      int _value = v.getValue();
      return Integer.valueOf(_value).toString();
    } else {
      int _value_1 = v.getValue();
      String _string = Integer.valueOf(_value_1).toString();
      return ("-" + _string);
    }
  }
  
  public static CharSequence genUtilitiesFunctions(final SchedulerDef sch) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//--------------- genUtilitiesFunctions ---------------------------");
    _builder.newLine();
    _builder.append("public boolean[] getProcessCheckList() {");
    _builder.newLine();
    {
      String _parent = sch.getParent();
      boolean _equals = Objects.equal(_parent, null);
      if (_equals) {
        _builder.append("\t");
        _builder.append("boolean result[] = new boolean[128] ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (running_process != null)");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("result[running_process.processID] = true ;");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("boolean result[] = super.getProcessCheckList() ;");
        _builder.newLine();
      }
    }
    {
      SchedulerDataDef _schedulerdata = sch.getSchedulerdata();
      boolean _notEquals = (!Objects.equal(_schedulerdata, null));
      if (_notEquals) {
        {
          for(final String col : Data.collectionList) {
            _builder.append("\t");
            _builder.append("for (ArrayList<SchedulerProcess> procList : ");
            _builder.append(col, "\t");
            _builder.append(".dataSet)");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("for (SchedulerProcess proc : procList)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("result[proc.processID] = true ;");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return result ;");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public ProcessSet getProcessSet(String psName) {");
    _builder.newLine();
    {
      String _parent_1 = sch.getParent();
      boolean _notEquals_1 = (!Objects.equal(_parent_1, null));
      if (_notEquals_1) {
        _builder.append("\t");
        _builder.append("ProcessSet result = super.getProcessSet(psName) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (result != null) return result ;");
        _builder.newLine();
      }
    }
    {
      SchedulerDataDef _schedulerdata_1 = sch.getSchedulerdata();
      boolean _notEquals_2 = (!Objects.equal(_schedulerdata_1, null));
      if (_notEquals_2) {
        _builder.append("\t");
        _builder.append("//scheduler data");
        _builder.newLine();
        {
          for(final String col_1 : Data.collectionList) {
            _builder.append("\t");
            _builder.append("if (psName == \"");
            _builder.append(col_1, "\t");
            _builder.append("\")");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("return ");
            _builder.append(col_1, "\t\t");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return null ;");
    _builder.newLine();
    _builder.append("}\t\t\t\t");
    _builder.newLine();
    _builder.append("public int getSize() {\t");
    _builder.newLine();
    {
      String _parent_2 = sch.getParent();
      boolean _equals_1 = Objects.equal(_parent_2, null);
      if (_equals_1) {
        _builder.append("\t");
        _builder.append("size = 0; //4 ; //schselopt");
        _builder.newLine();
        {
          DefCore _defcore = Data.schModel.getDefcore();
          boolean _notEquals_3 = (!Objects.equal(_defcore, null));
          if (_notEquals_3) {
            _builder.append("\t");
            _builder.append("//size += 4 ; //current_core");
            _builder.newLine();
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("size = super.getSize() ;");
        _builder.newLine();
      }
    }
    {
      VarDef _svar = sch.getSvar();
      boolean _notEquals_4 = (!Objects.equal(_svar, null));
      if (_notEquals_4) {
        _builder.append("\t");
        _builder.append("//scheduler variables");
        _builder.newLine();
        {
          VarDef _svar_1 = sch.getSvar();
          EList<VarDecl> _vard = _svar_1.getVard();
          boolean _notEquals_5 = (!Objects.equal(_vard, null));
          if (_notEquals_5) {
            _builder.append("\t");
            ArrayList<String> varlist = new ArrayList<String>();
            _builder.newLineIfNotEmpty();
            {
              VarDef _svar_2 = sch.getSvar();
              EList<VarDecl> _vard_1 = _svar_2.getVard();
              for(final VarDecl v : _vard_1) {
                {
                  VarBlockDef _varblockdef = v.getVarblockdef();
                  boolean _notEquals_6 = (!Objects.equal(_varblockdef, null));
                  if (_notEquals_6) {
                    {
                      VarBlockDef _varblockdef_1 = v.getVarblockdef();
                      EList<VarDefinition> _vardef = _varblockdef_1.getVardef();
                      for(final VarDefinition vas : _vardef) {
                        {
                          scheduling.dsl.String _type = vas.getType();
                          String _string = _type.toString();
                          String _trim = _string.trim();
                          boolean _equals_2 = Objects.equal(_trim, "bool");
                          if (_equals_2) {
                            {
                              EList<VarName> _name = vas.getName();
                              for(final VarName va : _name) {
                                {
                                  String _name_1 = va.getName();
                                  boolean _contains = varlist.contains(_name_1);
                                  boolean _not = (!_contains);
                                  if (_not) {
                                    _builder.append("\t");
                                    _builder.append("size += 1; //");
                                    String _name_2 = va.getName();
                                    _builder.append(_name_2, "\t");
                                    _builder.append(" ");
                                    String _name_3 = va.getName();
                                    boolean _add = varlist.add(_name_3);
                                    _builder.append(_add, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_1 = vas.getType();
                          String _string_1 = _type_1.toString();
                          String _trim_1 = _string_1.trim();
                          boolean _equals_3 = Objects.equal(_trim_1, "time");
                          if (_equals_3) {
                            {
                              EList<VarName> _name_4 = vas.getName();
                              for(final VarName va_1 : _name_4) {
                                {
                                  String _name_5 = va_1.getName();
                                  boolean _contains_1 = varlist.contains(_name_5);
                                  boolean _not_1 = (!_contains_1);
                                  if (_not_1) {
                                    _builder.append("\t");
                                    _builder.append("size += 4; //");
                                    String _name_6 = va_1.getName();
                                    _builder.append(_name_6, "\t");
                                    _builder.append(" add ");
                                    String _name_7 = va_1.getName();
                                    _builder.append(_name_7, "\t");
                                    _builder.append(" to list: ");
                                    String _name_8 = va_1.getName();
                                    boolean _add_1 = varlist.add(_name_8);
                                    _builder.append(_add_1, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_2 = vas.getType();
                          String _string_2 = _type_2.toString();
                          String _trim_2 = _string_2.trim();
                          boolean _equals_4 = Objects.equal(_trim_2, "int");
                          if (_equals_4) {
                            {
                              EList<VarName> _name_9 = vas.getName();
                              for(final VarName va_2 : _name_9) {
                                {
                                  String _name_10 = va_2.getName();
                                  boolean _contains_2 = varlist.contains(_name_10);
                                  boolean _not_2 = (!_contains_2);
                                  if (_not_2) {
                                    _builder.append("\t");
                                    _builder.append("size += 4; //");
                                    String _name_11 = va_2.getName();
                                    _builder.append(_name_11, "\t");
                                    _builder.append(" add ");
                                    String _name_12 = va_2.getName();
                                    _builder.append(_name_12, "\t");
                                    _builder.append(" to list: ");
                                    String _name_13 = va_2.getName();
                                    boolean _add_2 = varlist.add(_name_13);
                                    _builder.append(_add_2, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_3 = vas.getType();
                          String _string_3 = _type_3.toString();
                          String _trim_3 = _string_3.trim();
                          boolean _equals_5 = Objects.equal(_trim_3, "clock");
                          if (_equals_5) {
                            {
                              EList<VarName> _name_14 = vas.getName();
                              for(final VarName va_3 : _name_14) {
                                {
                                  String _name_15 = va_3.getName();
                                  boolean _contains_3 = varlist.contains(_name_15);
                                  boolean _not_3 = (!_contains_3);
                                  if (_not_3) {
                                    _builder.append("\t");
                                    _builder.append("size += 4; //");
                                    String _name_16 = va_3.getName();
                                    _builder.append(_name_16, "\t");
                                    _builder.append(" add ");
                                    String _name_17 = va_3.getName();
                                    _builder.append(_name_17, "\t");
                                    _builder.append(" to list: ");
                                    String _name_18 = va_3.getName();
                                    boolean _add_3 = varlist.add(_name_18);
                                    _builder.append(_add_3, "\t");
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
                    _builder.append("\t");
                    VarSingleDef _varsingledef = v.getVarsingledef();
                    VarDefinition vas_1 = _varsingledef.getVardef();
                    _builder.append("\t\t\t\t\t\t\t");
                    _builder.newLineIfNotEmpty();
                    {
                      scheduling.dsl.String _type_4 = vas_1.getType();
                      String _string_4 = _type_4.toString();
                      String _trim_4 = _string_4.trim();
                      boolean _equals_6 = Objects.equal(_trim_4, "bool");
                      if (_equals_6) {
                        {
                          EList<VarName> _name_19 = vas_1.getName();
                          for(final VarName va_4 : _name_19) {
                            {
                              String _name_20 = va_4.getName();
                              boolean _contains_4 = varlist.contains(_name_20);
                              boolean _not_4 = (!_contains_4);
                              if (_not_4) {
                                _builder.append("\t");
                                _builder.append("size += 1; //");
                                String _name_21 = va_4.getName();
                                _builder.append(_name_21, "\t");
                                _builder.append(" ");
                                String _name_22 = va_4.getName();
                                boolean _add_4 = varlist.add(_name_22);
                                _builder.append(_add_4, "\t");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        }
                      }
                    }
                    {
                      scheduling.dsl.String _type_5 = vas_1.getType();
                      String _string_5 = _type_5.toString();
                      String _trim_5 = _string_5.trim();
                      boolean _equals_7 = Objects.equal(_trim_5, "time");
                      if (_equals_7) {
                        {
                          EList<VarName> _name_23 = vas_1.getName();
                          for(final VarName va_5 : _name_23) {
                            {
                              String _name_24 = va_5.getName();
                              boolean _contains_5 = varlist.contains(_name_24);
                              boolean _not_5 = (!_contains_5);
                              if (_not_5) {
                                _builder.append("\t");
                                _builder.append("size += 4; //");
                                String _name_25 = va_5.getName();
                                _builder.append(_name_25, "\t");
                                _builder.append(" add ");
                                String _name_26 = va_5.getName();
                                _builder.append(_name_26, "\t");
                                _builder.append(" to list: ");
                                String _name_27 = va_5.getName();
                                boolean _add_5 = varlist.add(_name_27);
                                _builder.append(_add_5, "\t");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        }
                      }
                    }
                    {
                      scheduling.dsl.String _type_6 = vas_1.getType();
                      String _string_6 = _type_6.toString();
                      String _trim_6 = _string_6.trim();
                      boolean _equals_8 = Objects.equal(_trim_6, "int");
                      if (_equals_8) {
                        {
                          EList<VarName> _name_28 = vas_1.getName();
                          for(final VarName va_6 : _name_28) {
                            {
                              String _name_29 = va_6.getName();
                              boolean _contains_6 = varlist.contains(_name_29);
                              boolean _not_6 = (!_contains_6);
                              if (_not_6) {
                                _builder.append("\t");
                                _builder.append("size += 4; //");
                                String _name_30 = va_6.getName();
                                _builder.append(_name_30, "\t");
                                _builder.append(" add ");
                                String _name_31 = va_6.getName();
                                _builder.append(_name_31, "\t");
                                _builder.append(" to list: ");
                                String _name_32 = va_6.getName();
                                boolean _add_6 = varlist.add(_name_32);
                                _builder.append(_add_6, "\t");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        }
                      }
                    }
                    {
                      scheduling.dsl.String _type_7 = vas_1.getType();
                      String _string_7 = _type_7.toString();
                      String _trim_7 = _string_7.trim();
                      boolean _equals_9 = Objects.equal(_trim_7, "clock");
                      if (_equals_9) {
                        {
                          EList<VarName> _name_33 = vas_1.getName();
                          for(final VarName va_7 : _name_33) {
                            {
                              String _name_34 = va_7.getName();
                              boolean _contains_7 = varlist.contains(_name_34);
                              boolean _not_7 = (!_contains_7);
                              if (_not_7) {
                                _builder.append("\t");
                                _builder.append("size += 4; //");
                                String _name_35 = va_7.getName();
                                _builder.append(_name_35, "\t");
                                _builder.append(" add ");
                                String _name_36 = va_7.getName();
                                _builder.append(_name_36, "\t");
                                _builder.append(" to list: ");
                                String _name_37 = va_7.getName();
                                boolean _add_7 = varlist.add(_name_37);
                                _builder.append(_add_7, "\t");
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
      }
    }
    {
      SchedulerDataDef _schedulerdata_2 = sch.getSchedulerdata();
      boolean _notEquals_7 = (!Objects.equal(_schedulerdata_2, null));
      if (_notEquals_7) {
        {
          SchedulerDataDef _schedulerdata_3 = sch.getSchedulerdata();
          EList<DataDef> _datadef = _schedulerdata_3.getDatadef();
          boolean _notEquals_8 = (!Objects.equal(_datadef, null));
          if (_notEquals_8) {
            _builder.append("\t");
            ArrayList<String> varlist_1 = new ArrayList<String>();
            _builder.append("\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("//scheduler variables data");
            _builder.newLine();
            {
              SchedulerDataDef _schedulerdata_4 = sch.getSchedulerdata();
              EList<DataDef> _datadef_1 = _schedulerdata_4.getDatadef();
              for(final DataDef data : _datadef_1) {
                {
                  DataBlockDef _datablockdef = data.getDatablockdef();
                  boolean _notEquals_9 = (!Objects.equal(_datablockdef, null));
                  if (_notEquals_9) {
                    {
                      DataBlockDef _datablockdef_1 = data.getDatablockdef();
                      EList<DataSingleDef> _datadef_2 = _datablockdef_1.getDatadef();
                      for(final DataSingleDef dat : _datadef_2) {
                        {
                          SchedulerPropertyDef _prop = dat.getProp();
                          boolean _notEquals_10 = (!Objects.equal(_prop, null));
                          if (_notEquals_10) {
                            _builder.append("\t");
                            SchedulerPropertyDef vas_2 = dat.getProp();
                            _builder.append("\t\t\t\t");
                            _builder.newLineIfNotEmpty();
                            {
                              scheduling.dsl.String _type_8 = vas_2.getType();
                              String _string_8 = _type_8.toString();
                              String _trim_8 = _string_8.trim();
                              boolean _equals_10 = Objects.equal(_trim_8, "bool");
                              if (_equals_10) {
                                {
                                  EList<SchedulerPropertyName> _name_38 = vas_2.getName();
                                  for(final SchedulerPropertyName va_8 : _name_38) {
                                    {
                                      String _name_39 = va_8.getName();
                                      boolean _contains_8 = varlist_1.contains(_name_39);
                                      boolean _not_8 = (!_contains_8);
                                      if (_not_8) {
                                        _builder.append("\t");
                                        _builder.append("size += 1; //");
                                        String _name_40 = va_8.getName();
                                        _builder.append(_name_40, "\t");
                                        _builder.append(" add ");
                                        String _name_41 = va_8.getName();
                                        _builder.append(_name_41, "\t");
                                        _builder.append(" to list:  ");
                                        String _name_42 = va_8.getName();
                                        boolean _add_8 = varlist_1.add(_name_42);
                                        _builder.append(_add_8, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_9 = vas_2.getType();
                              String _string_9 = _type_9.toString();
                              String _trim_9 = _string_9.trim();
                              boolean _equals_11 = Objects.equal(_trim_9, "time");
                              if (_equals_11) {
                                {
                                  EList<SchedulerPropertyName> _name_43 = vas_2.getName();
                                  for(final SchedulerPropertyName va_9 : _name_43) {
                                    {
                                      String _name_44 = va_9.getName();
                                      boolean _contains_9 = varlist_1.contains(_name_44);
                                      boolean _not_9 = (!_contains_9);
                                      if (_not_9) {
                                        _builder.append("\t");
                                        _builder.append("size += 4; //");
                                        String _name_45 = va_9.getName();
                                        _builder.append(_name_45, "\t");
                                        _builder.append("  add ");
                                        String _name_46 = va_9.getName();
                                        _builder.append(_name_46, "\t");
                                        _builder.append(" to list: ");
                                        String _name_47 = va_9.getName();
                                        boolean _add_9 = varlist_1.add(_name_47);
                                        _builder.append(_add_9, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_10 = vas_2.getType();
                              String _string_10 = _type_10.toString();
                              String _trim_10 = _string_10.trim();
                              boolean _equals_12 = Objects.equal(_trim_10, "int");
                              if (_equals_12) {
                                {
                                  EList<SchedulerPropertyName> _name_48 = vas_2.getName();
                                  for(final SchedulerPropertyName va_10 : _name_48) {
                                    {
                                      String _name_49 = va_10.getName();
                                      boolean _contains_10 = varlist_1.contains(_name_49);
                                      boolean _not_10 = (!_contains_10);
                                      if (_not_10) {
                                        _builder.append("\t");
                                        _builder.append("size += 4; //");
                                        String _name_50 = va_10.getName();
                                        _builder.append(_name_50, "\t");
                                        _builder.append(" add ");
                                        String _name_51 = va_10.getName();
                                        _builder.append(_name_51, "\t");
                                        _builder.append(" to list:  ");
                                        String _name_52 = va_10.getName();
                                        boolean _add_10 = varlist_1.add(_name_52);
                                        _builder.append(_add_10, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_11 = vas_2.getType();
                              String _string_11 = _type_11.toString();
                              String _trim_11 = _string_11.trim();
                              boolean _equals_13 = Objects.equal(_trim_11, "clock");
                              if (_equals_13) {
                                {
                                  EList<SchedulerPropertyName> _name_53 = vas_2.getName();
                                  for(final SchedulerPropertyName va_11 : _name_53) {
                                    {
                                      String _name_54 = va_11.getName();
                                      boolean _contains_11 = varlist_1.contains(_name_54);
                                      boolean _not_11 = (!_contains_11);
                                      if (_not_11) {
                                        _builder.append("\t");
                                        _builder.append("size += 4; //");
                                        String _name_55 = va_11.getName();
                                        _builder.append(_name_55, "\t");
                                        _builder.append(" add ");
                                        String _name_56 = va_11.getName();
                                        _builder.append(_name_56, "\t");
                                        _builder.append(" to list:  ");
                                        String _name_57 = va_11.getName();
                                        boolean _add_11 = varlist_1.add(_name_57);
                                        _builder.append(_add_11, "\t");
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
                  } else {
                    _builder.append("\t");
                    DataSingleDef dat_1 = data.getDatasingledef();
                    _builder.newLineIfNotEmpty();
                    {
                      SchedulerPropertyDef _prop_1 = dat_1.getProp();
                      boolean _notEquals_11 = (!Objects.equal(_prop_1, null));
                      if (_notEquals_11) {
                        _builder.append("\t");
                        SchedulerPropertyDef vas_3 = dat_1.getProp();
                        _builder.append("\t\t\t\t");
                        _builder.newLineIfNotEmpty();
                        {
                          scheduling.dsl.String _type_12 = vas_3.getType();
                          String _string_12 = _type_12.toString();
                          String _trim_12 = _string_12.trim();
                          boolean _equals_14 = Objects.equal(_trim_12, "bool");
                          if (_equals_14) {
                            {
                              EList<SchedulerPropertyName> _name_58 = vas_3.getName();
                              for(final SchedulerPropertyName va_12 : _name_58) {
                                {
                                  String _name_59 = va_12.getName();
                                  boolean _contains_12 = varlist_1.contains(_name_59);
                                  boolean _not_12 = (!_contains_12);
                                  if (_not_12) {
                                    _builder.append("\t");
                                    _builder.append("size += 1; //");
                                    String _name_60 = va_12.getName();
                                    _builder.append(_name_60, "\t");
                                    _builder.append(" add ");
                                    String _name_61 = va_12.getName();
                                    _builder.append(_name_61, "\t");
                                    _builder.append(" to list:  ");
                                    String _name_62 = va_12.getName();
                                    boolean _add_12 = varlist_1.add(_name_62);
                                    _builder.append(_add_12, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_13 = vas_3.getType();
                          String _string_13 = _type_13.toString();
                          String _trim_13 = _string_13.trim();
                          boolean _equals_15 = Objects.equal(_trim_13, "time");
                          if (_equals_15) {
                            {
                              EList<SchedulerPropertyName> _name_63 = vas_3.getName();
                              for(final SchedulerPropertyName va_13 : _name_63) {
                                {
                                  String _name_64 = va_13.getName();
                                  boolean _contains_13 = varlist_1.contains(_name_64);
                                  boolean _not_13 = (!_contains_13);
                                  if (_not_13) {
                                    _builder.append("\t");
                                    _builder.append("size += 4; //");
                                    String _name_65 = va_13.getName();
                                    _builder.append(_name_65, "\t");
                                    _builder.append("  add ");
                                    String _name_66 = va_13.getName();
                                    _builder.append(_name_66, "\t");
                                    _builder.append(" to list: ");
                                    String _name_67 = va_13.getName();
                                    boolean _add_13 = varlist_1.add(_name_67);
                                    _builder.append(_add_13, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_14 = vas_3.getType();
                          String _string_14 = _type_14.toString();
                          String _trim_14 = _string_14.trim();
                          boolean _equals_16 = Objects.equal(_trim_14, "int");
                          if (_equals_16) {
                            {
                              EList<SchedulerPropertyName> _name_68 = vas_3.getName();
                              for(final SchedulerPropertyName va_14 : _name_68) {
                                {
                                  String _name_69 = va_14.getName();
                                  boolean _contains_14 = varlist_1.contains(_name_69);
                                  boolean _not_14 = (!_contains_14);
                                  if (_not_14) {
                                    _builder.append("\t");
                                    _builder.append("size += 4; //");
                                    String _name_70 = va_14.getName();
                                    _builder.append(_name_70, "\t");
                                    _builder.append(" add ");
                                    String _name_71 = va_14.getName();
                                    _builder.append(_name_71, "\t");
                                    _builder.append(" to list:  ");
                                    String _name_72 = va_14.getName();
                                    boolean _add_14 = varlist_1.add(_name_72);
                                    _builder.append(_add_14, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_15 = vas_3.getType();
                          String _string_15 = _type_15.toString();
                          String _trim_15 = _string_15.trim();
                          boolean _equals_17 = Objects.equal(_trim_15, "clock");
                          if (_equals_17) {
                            {
                              EList<SchedulerPropertyName> _name_73 = vas_3.getName();
                              for(final SchedulerPropertyName va_15 : _name_73) {
                                {
                                  String _name_74 = va_15.getName();
                                  boolean _contains_15 = varlist_1.contains(_name_74);
                                  boolean _not_15 = (!_contains_15);
                                  if (_not_15) {
                                    _builder.append("\t");
                                    _builder.append("size += 4; //");
                                    String _name_75 = va_15.getName();
                                    _builder.append(_name_75, "\t");
                                    _builder.append(" add ");
                                    String _name_76 = va_15.getName();
                                    _builder.append(_name_76, "\t");
                                    _builder.append(" to list:  ");
                                    String _name_77 = va_15.getName();
                                    boolean _add_15 = varlist_1.add(_name_77);
                                    _builder.append(_add_15, "\t");
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
          }
        }
      }
    }
    {
      if (Data.runTime) {
        {
          DefCore _defcore_1 = Data.schModel.getDefcore();
          boolean _notEquals_12 = (!Objects.equal(_defcore_1, null));
          if (_notEquals_12) {
            _builder.append("\t");
            _builder.append("size += 8 *  ");
            DefCore _defcore_2 = Data.schModel.getDefcore();
            int _numcore = _defcore_2.getNumcore();
            _builder.append(_numcore, "\t");
            _builder.append("; //_time_count, _time_slice for each core");
            _builder.newLineIfNotEmpty();
          } else {
            _builder.append("\t");
            _builder.append("size += 8 ; //_time_count, _time_slice (system)");
            _builder.newLine();
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("//no time counter");
        _builder.newLine();
      }
    }
    {
      Set<String> _keySet = Data.periodicClockProperties.keySet();
      for(final String pClock : _keySet) {
        _builder.append("\t");
        _builder.append("size += 4 ; //clock ");
        _builder.append(pClock, "\t");
        _builder.append(" ;");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("size += 4 ; //clock ");
        _builder.append(pClock, "\t");
        _builder.append("_offset ;");
        _builder.newLineIfNotEmpty();
      }
    }
    {
      String _parent_3 = sch.getParent();
      boolean _equals_18 = Objects.equal(_parent_3, null);
      if (_equals_18) {
        {
          DefCore _defcore_3 = Data.schModel.getDefcore();
          boolean _notEquals_13 = (!Objects.equal(_defcore_3, null));
          if (_notEquals_13) {
            _builder.append("\t");
            _builder.append("for (int i=0; i < ");
            DefCore _defcore_4 = Data.schModel.getDefcore();
            int _numcore_1 = _defcore_4.getNumcore();
            _builder.append(_numcore_1, "\t");
            _builder.append("; i++) {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("size += 1; //running_procs[i] != null");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (running_procs[i] != null)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("size += running_procs[i].getSize() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("size += 1 + _putColIndex[i].size(); //_putColIndex");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}\t\t\t\t\t");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("size += 1 ; //running_process != null ?");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("if (running_process != null)\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("size += running_process.getSize() ;\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("//size += _runningSet.getSize() ; // why disable? encode and store in different space (runningSet is stored in stack)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("size += 1 ; //_putColIndex -> for replacement");
            _builder.newLine();
          }
        }
      }
    }
    {
      SchedulerDataDef _schedulerdata_5 = sch.getSchedulerdata();
      boolean _notEquals_14 = (!Objects.equal(_schedulerdata_5, null));
      if (_notEquals_14) {
        _builder.append("\t");
        _builder.append("//no contains refines collections");
        _builder.newLine();
        {
          for(final String col_2 : Data.collectionList) {
            _builder.append("\t");
            _builder.append("size += ");
            _builder.append(col_2, "\t");
            _builder.append(".getSize() ;");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return size ;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static String genHandler(final ProcessDSL procModel, final SchedulerDef sch) {
    String result = "";
    HandlerDef _handler = sch.getHandler();
    boolean _notEquals = (!Objects.equal(_handler, null));
    if (_notEquals) {
      HandlerDef _handler_1 = sch.getHandler();
      EList<EventDef> _event = _handler_1.getEvent();
      for (final EventDef e : _event) {
        scheduling.dsl.String _eventname = e.getEventname();
        String _string = _eventname.toString();
        String _trim = _string.trim();
        switch (_trim) {
          case "select_process":
            String _result = result;
            CharSequence _genEventSelect = Handler.genEventSelect(e);
            result = (_result + _genEventSelect);
            break;
          case "new_process":
            String _result_1 = result;
            CharSequence _genEventNew = Handler.genEventNew(e, sch);
            result = (_result_1 + _genEventNew);
            break;
          case "clock":
            String _result_2 = result;
            CharSequence _genEventClock = Handler.genEventClock(e, procModel);
            result = (_result_2 + _genEventClock);
            break;
          case "pre_take":
            String _result_3 = result;
            CharSequence _genPreTake = Handler.genPreTake(e);
            result = (_result_3 + _genPreTake);
            break;
          case "post_take":
            String _result_4 = result;
            CharSequence _genPostTake = Handler.genPostTake(e);
            result = (_result_4 + _genPostTake);
            break;
        }
      }
    }
    return result;
  }
  
  /**
   * static def genChainClass()
   * '''
   * class ChainInfo {
   * //public byte processID1 ;
   * //public byte collectionID ;
   * //public byte processID2 ;
   * 
   * public int processID1 ;
   * public byte collectionID ;
   * public int processID2 ;
   * }
   * '''
   */
  public static CharSequence genUtilityFunction(final SchedulerDef sch) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("/* ---------------------- utility function */");
    _builder.newLine();
    _builder.append("public String getInstance(SchedulerProcess process) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return pcnt.get(process.processID) + \"\"; ");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("public int getRunningInstance() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (running_process == null)");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return -1 ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("else");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return pcnt.get(running_process.processID);");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.append("public int getRunningID() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("if (running_process == null)");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return -1 ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("else");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("return running_process.processID ;//pcnt[running_process.processID];");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    {
      String _parent = sch.getParent();
      boolean _equals = Objects.equal(_parent, null);
      if (_equals) {
        _builder.append("public static void printProcessInScheduler(){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("for (int i=0 ; i< 128 ; i++){");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("System.out.print(processInScheduler[i] + \", \");");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("System.out.println();\t\t\t\t\t\t\t\t");
        _builder.newLine();
        _builder.append("}\t\t");
        _builder.newLine();
        _builder.append("public void printProcessInstance(){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("for (int i=0 ; i < pcnt.size() ; i++){");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("System.out.print(pcnt.get(i) + \", \");");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("System.out.println();\t\t\t\t\t");
        _builder.newLine();
        _builder.append("}\t");
        _builder.newLine();
        _builder.append("public int addProcessList(String pName){");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("//return the index of new process name in process list");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("processList.add(pName) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return processList.size() - 1 ;");
        _builder.newLine();
        _builder.append("}\t\t\t");
        _builder.newLine();
        _builder.append("public int isNull(SchedulerProcess process) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (process == null)");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("return 1 ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("else");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("return 0 ;");
        _builder.newLine();
        _builder.append("}\t\t\t");
        _builder.newLine();
        _builder.append("public static int getProcessID(String procName){ //or periodicKey");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("/*");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("* This function will return correct ID for new process (which has name) managed by scheduler ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("* When scheduler initial, it will call this function to initial the order of processes");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("*/");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("int id = 0;\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("for (String pName : processList){");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("if (pName.equals(procName) && !processInScheduler[id]){\t");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("//processInModel[id] = true ;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("processInScheduler[id] = true ;");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("return id ;\t");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("id ++ ;\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (processList.size() < 128) {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("processList.add(procName) ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("id = processList.size() - 1 ;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("processInScheduler[id] = true;");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("return id ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("return -1 ;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("public int getCollectionIndex(String collectionName) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int numCol = 0 ;");
    _builder.newLine();
    {
      String _parent_1 = sch.getParent();
      boolean _notEquals = (!Objects.equal(_parent_1, null));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("int result = super.getCollectionIndex(collectionName) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (result > -1) return result ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("numCol = super.getNumberProcessCollection() ;");
        _builder.newLine();
      }
    }
    {
      SchedulerDataDef _schedulerdata = sch.getSchedulerdata();
      boolean _notEquals_1 = (!Objects.equal(_schedulerdata, null));
      if (_notEquals_1) {
        {
          boolean _notEquals_2 = (!Objects.equal(Data.collectionList, null));
          if (_notEquals_2) {
            _builder.append("\t");
            _builder.append("switch (collectionName) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            int idx = 0;
            _builder.newLineIfNotEmpty();
            {
              for(final String col : Data.collectionList) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("case \"");
                _builder.append(col, "\t\t");
                _builder.append("\" : ");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("return numCol + ");
                int _plusPlus = idx++;
                _builder.append(_plusPlus, "\t\t\t");
                _builder.append(" ;");
                _builder.newLineIfNotEmpty();
              }
            }
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("default :");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("Util.println(\"Put back collection error\") ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("return -1 ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("return -1 ;");
        _builder.newLine();
      }
    }
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public int getNumberProcessCollection() {");
    _builder.newLine();
    _builder.append("\t");
    int cnt = 0;
    _builder.newLineIfNotEmpty();
    {
      String _parent_2 = sch.getParent();
      boolean _equals_1 = Objects.equal(_parent_2, null);
      if (_equals_1) {
        _builder.append("\t");
        _builder.append("int result = 0 ;");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("int result = super.getNumberProcessCollection() ;");
        _builder.newLine();
      }
    }
    {
      SchedulerDataDef _schedulerdata_1 = sch.getSchedulerdata();
      boolean _notEquals_3 = (!Objects.equal(_schedulerdata_1, null));
      if (_notEquals_3) {
        {
          SchedulerDataDef _schedulerdata_2 = sch.getSchedulerdata();
          EList<DataDef> _datadef = _schedulerdata_2.getDatadef();
          for(final DataDef data : _datadef) {
            {
              DataBlockDef _datablockdef = data.getDatablockdef();
              boolean _notEquals_4 = (!Objects.equal(_datablockdef, null));
              if (_notEquals_4) {
                {
                  DataBlockDef _datablockdef_1 = data.getDatablockdef();
                  EList<DataSingleDef> _datadef_1 = _datablockdef_1.getDatadef();
                  for(final DataSingleDef dat : _datadef_1) {
                    {
                      SchedulerCollectionDef _col = dat.getCol();
                      boolean _notEquals_5 = (!Objects.equal(_col, null));
                      if (_notEquals_5) {
                        {
                          SchedulerCollectionDef _col_1 = dat.getCol();
                          String _parent_3 = _col_1.getParent();
                          boolean _notEquals_6 = (!Objects.equal(_parent_3, null));
                          if (_notEquals_6) {
                            _builder.append("\t");
                            _builder.append("//");
                            SchedulerCollectionDef _col_2 = dat.getCol();
                            SchedulerSet _name = _col_2.getName();
                            String _name_1 = _name.getName();
                            _builder.append(_name_1, "\t");
                            _builder.append(" : no");
                            _builder.newLineIfNotEmpty();
                          } else {
                            _builder.append("\t");
                            _builder.append("//");
                            SchedulerCollectionDef _col_3 = dat.getCol();
                            SchedulerSet _name_2 = _col_3.getName();
                            String _name_3 = _name_2.getName();
                            _builder.append(_name_3, "\t");
                            _builder.append(" : ");
                            int _plusPlus_1 = cnt++;
                            _builder.append(_plusPlus_1, "\t");
                            _builder.newLineIfNotEmpty();
                          }
                        }
                      }
                    }
                  }
                }
              } else {
                {
                  DataSingleDef _datasingledef = data.getDatasingledef();
                  SchedulerCollectionDef _col_4 = _datasingledef.getCol();
                  boolean _notEquals_7 = (!Objects.equal(_col_4, null));
                  if (_notEquals_7) {
                    {
                      DataSingleDef _datasingledef_1 = data.getDatasingledef();
                      SchedulerCollectionDef _col_5 = _datasingledef_1.getCol();
                      String _parent_4 = _col_5.getParent();
                      boolean _notEquals_8 = (!Objects.equal(_parent_4, null));
                      if (_notEquals_8) {
                        _builder.append("\t");
                        _builder.append("//");
                        DataSingleDef _datasingledef_2 = data.getDatasingledef();
                        SchedulerCollectionDef _col_6 = _datasingledef_2.getCol();
                        SchedulerSet _name_4 = _col_6.getName();
                        String _name_5 = _name_4.getName();
                        _builder.append(_name_5, "\t");
                        _builder.append(" : no");
                        _builder.newLineIfNotEmpty();
                      } else {
                        _builder.append("\t");
                        _builder.append("//");
                        DataSingleDef _datasingledef_3 = data.getDatasingledef();
                        SchedulerCollectionDef _col_7 = _datasingledef_3.getCol();
                        SchedulerSet _name_6 = _col_7.getName();
                        String _name_7 = _name_6.getName();
                        _builder.append(_name_7, "\t");
                        _builder.append(" : ");
                        int _plusPlus_2 = cnt++;
                        _builder.append(_plusPlus_2, "\t");
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
    _builder.append("\t");
    _builder.append("result += ");
    _builder.append(cnt, "\t");
    _builder.append(" ;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("return result ;");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("//return collection contains process -> needs to be considered");
    _builder.newLine();
    _builder.append("public int getProcessCollectionID(int processID) {\t\t\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int numCol = 0 ;");
    _builder.newLine();
    {
      String _parent_5 = sch.getParent();
      boolean _notEquals_9 = (!Objects.equal(_parent_5, null));
      if (_notEquals_9) {
        _builder.append("\t");
        _builder.append("int result = super.getProcessCollectionID(processID) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (result >= 0) return result ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("numCol = super.getNumberProcessCollection() ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    int idx_1 = 0;
    _builder.append("\t\t\t\t");
    _builder.newLineIfNotEmpty();
    {
      SchedulerDataDef _schedulerdata_3 = sch.getSchedulerdata();
      boolean _notEquals_10 = (!Objects.equal(_schedulerdata_3, null));
      if (_notEquals_10) {
        {
          SchedulerDataDef _schedulerdata_4 = sch.getSchedulerdata();
          EList<DataDef> _datadef_2 = _schedulerdata_4.getDatadef();
          boolean _notEquals_11 = (!Objects.equal(_datadef_2, null));
          if (_notEquals_11) {
            {
              SchedulerDataDef _schedulerdata_5 = sch.getSchedulerdata();
              EList<DataDef> _datadef_3 = _schedulerdata_5.getDatadef();
              for(final DataDef data_1 : _datadef_3) {
                {
                  DataBlockDef _datablockdef_2 = data_1.getDatablockdef();
                  boolean _notEquals_12 = (!Objects.equal(_datablockdef_2, null));
                  if (_notEquals_12) {
                    {
                      DataBlockDef _datablockdef_3 = data_1.getDatablockdef();
                      EList<DataSingleDef> _datadef_4 = _datablockdef_3.getDatadef();
                      for(final DataSingleDef dat_1 : _datadef_4) {
                        {
                          SchedulerCollectionDef _col_8 = dat_1.getCol();
                          boolean _notEquals_13 = (!Objects.equal(_col_8, null));
                          if (_notEquals_13) {
                            {
                              SchedulerCollectionDef _col_9 = dat_1.getCol();
                              String _parent_6 = _col_9.getParent();
                              boolean _equals_2 = Objects.equal(_parent_6, null);
                              if (_equals_2) {
                                _builder.append("\t");
                                _builder.append("if (");
                                SchedulerCollectionDef _col_10 = dat_1.getCol();
                                SchedulerSet _name_8 = _col_10.getName();
                                String _name_9 = _name_8.getName();
                                _builder.append(_name_9, "\t");
                                _builder.append(".hasProcess(processID) > 0) ");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.append("return ");
                                int _plusPlus_3 = idx_1++;
                                _builder.append(_plusPlus_3, "\t\t");
                                _builder.append(" + numCol ;");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        }
                      }
                    }
                  } else {
                    {
                      DataSingleDef _datasingledef_4 = data_1.getDatasingledef();
                      boolean _notEquals_14 = (!Objects.equal(_datasingledef_4, null));
                      if (_notEquals_14) {
                        _builder.append("\t");
                        DataSingleDef dat_2 = data_1.getDatasingledef();
                        _builder.newLineIfNotEmpty();
                        {
                          SchedulerCollectionDef _col_11 = dat_2.getCol();
                          boolean _notEquals_15 = (!Objects.equal(_col_11, null));
                          if (_notEquals_15) {
                            {
                              SchedulerCollectionDef _col_12 = dat_2.getCol();
                              String _parent_7 = _col_12.getParent();
                              boolean _equals_3 = Objects.equal(_parent_7, null);
                              if (_equals_3) {
                                _builder.append("\t");
                                _builder.append("if (");
                                SchedulerCollectionDef _col_13 = dat_2.getCol();
                                SchedulerSet _name_10 = _col_13.getName();
                                String _name_11 = _name_10.getName();
                                _builder.append(_name_11, "\t");
                                _builder.append(".hasProcess(processID) > 0) ");
                                _builder.newLineIfNotEmpty();
                                _builder.append("\t");
                                _builder.append("\t");
                                _builder.append("return ");
                                int _plusPlus_4 = idx_1++;
                                _builder.append(_plusPlus_4, "\t\t");
                                _builder.append(" + numCol ;");
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
      }
    }
    _builder.append("\t");
    _builder.append("return -1 ;");
    _builder.newLine();
    _builder.append("}\t\t\t\t\t");
    _builder.newLine();
    _builder.append("public boolean isTimer() {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//boolean hasClockEventHandler = ");
    _builder.append(Data.hasClockEventHandler, "\t");
    _builder.append(" ; ");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("//boolean hasPeriodicProcess = ");
    _builder.append(Data.hasPeriodicProcess, "\t");
    _builder.append(" ;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("//boolean runTime = ");
    _builder.append(Data.runTime, "\t");
    _builder.append(" ;");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("//has clock data type = ");
    int _size = Data.clockPropertyList.size();
    boolean _greaterThan = (_size > 0);
    _builder.append(_greaterThan, "\t");
    _builder.newLineIfNotEmpty();
    {
      if (((Data.hasClockEventHandler || Data.hasPeriodicProcess) || (Data.clockPropertyList.size() > 0))) {
        _builder.append("\t");
        _builder.append("return true ;");
        _builder.newLine();
      } else {
        {
          if (Data.runTime) {
            {
              DefCore _defcore = Data.schModel.getDefcore();
              boolean _notEquals_16 = (!Objects.equal(_defcore, null));
              if (_notEquals_16) {
                _builder.append("\t");
                _builder.append("if (_time_slice[current_core] != 0)");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("return true ; ");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("else");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("return false ;");
                _builder.newLine();
              } else {
                _builder.append("\t");
                _builder.append("if (_time_slice != 0)");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("return true ; //(_time_slice > 0)");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("else");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("return false ;");
                _builder.newLine();
              }
            }
          } else {
            {
              if (Data.sporadicTime) {
                _builder.append("\t");
                _builder.append("return true ; //sporadic tasks");
                _builder.newLine();
              } else {
                {
                  String _parent_8 = sch.getParent();
                  boolean _notEquals_17 = (!Objects.equal(_parent_8, null));
                  if (_notEquals_17) {
                    _builder.append("\t");
                    _builder.append("return super.isTimer() ;");
                    _builder.newLine();
                  } else {
                    _builder.append("\t");
                    _builder.append("return false ;");
                    _builder.newLine();
                  }
                }
              }
            }
          }
        }
      }
    }
    _builder.append("}\t\t\t\t\t");
    _builder.newLine();
    _builder.append("public SchedulerProcess findProcessByID(int processID) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("SchedulerProcess proc = null ;");
    _builder.newLine();
    {
      String _parent_9 = sch.getParent();
      boolean _equals_4 = Objects.equal(_parent_9, null);
      if (_equals_4) {
        _builder.append("\t");
        _builder.append("if (running_process != null) ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("if (running_process.processID == processID) ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return running_process ;");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("proc = super.findProcessByID(processID) ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (proc != null)");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return proc ;");
        _builder.newLine();
      }
    }
    {
      SchedulerDataDef _schedulerdata_6 = sch.getSchedulerdata();
      boolean _notEquals_18 = (!Objects.equal(_schedulerdata_6, null));
      if (_notEquals_18) {
        {
          for(final String col_1 : Data.collectionList) {
            _builder.append("\t");
            _builder.append("proc = ");
            _builder.append(col_1, "\t");
            _builder.append(".getProcess(processID);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("if (proc != null) return proc ;");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return null ;");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public ArrayList<SchedulerProcess> findProcessByrefID(int refID) {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("ArrayList<SchedulerProcess> result = new ArrayList<SchedulerProcess>();\t\t\t");
    _builder.newLine();
    {
      String _parent_10 = sch.getParent();
      boolean _equals_5 = Objects.equal(_parent_10, null);
      if (_equals_5) {
        _builder.append("\t");
        _builder.append("if (running_process != null) ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("if (running_process.refID == refID) ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("result.add(running_process) ;");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("result.addAll(super.findProcessByrefID(refID)) ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("ArrayList<SchedulerProcess> temp = new ArrayList<SchedulerProcess>();");
    _builder.newLine();
    {
      SchedulerDataDef _schedulerdata_7 = sch.getSchedulerdata();
      boolean _notEquals_19 = (!Objects.equal(_schedulerdata_7, null));
      if (_notEquals_19) {
        {
          for(final String col_2 : Data.collectionList) {
            _builder.append("\t");
            _builder.append("temp = ");
            _builder.append(col_2, "\t");
            _builder.append(".findProcessByrefID(refID);");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("if (temp != null)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("result.addAll(temp);\t\t\t\t\t");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return result ;");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public void remove_process(int processID) {");
    _builder.newLine();
    {
      String _parent_11 = sch.getParent();
      boolean _notEquals_20 = (!Objects.equal(_parent_11, null));
      if (_notEquals_20) {
        _builder.append("\t");
        _builder.append("super.remove_process(processID) ;");
        _builder.newLine();
      }
    }
    {
      SchedulerDataDef _schedulerdata_8 = sch.getSchedulerdata();
      boolean _notEquals_21 = (!Objects.equal(_schedulerdata_8, null));
      if (_notEquals_21) {
        {
          for(final String col_3 : Data.collectionList) {
            _builder.append("\t");
            _builder.append(col_3, "\t");
            _builder.append(".removeProcess(processID);\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    {
      DefCore _defcore_1 = Data.schModel.getDefcore();
      boolean _notEquals_22 = (!Objects.equal(_defcore_1, null));
      if (_notEquals_22) {
        _builder.append("\t");
        _builder.append("for (int i=0; i<num_core; i++) ");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("if (i != current_core && _runningSets[i] != null)");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("_runningSets[i].removeProcess(processID);");
        _builder.newLine();
      }
    }
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public int isEmpty() {");
    _builder.newLine();
    {
      String _parent_12 = sch.getParent();
      boolean _notEquals_23 = (!Objects.equal(_parent_12, null));
      if (_notEquals_23) {
        _builder.append("\t");
        _builder.append("if (super.isEmpty() > 0)");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return 1 ;");
        _builder.newLine();
      }
    }
    {
      SchedulerDataDef _schedulerdata_9 = sch.getSchedulerdata();
      boolean _notEquals_24 = (!Objects.equal(_schedulerdata_9, null));
      if (_notEquals_24) {
        {
          for(final String col_4 : Data.collectionList) {
            _builder.append("\t");
            _builder.append("if (");
            _builder.append(col_4, "\t");
            _builder.append(".isEmpty() > 0)");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("return 1 ;\t\t\t\t\t");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return 0 ;");
    _builder.newLine();
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public int hasProcess(String processName) {");
    _builder.newLine();
    {
      String _parent_13 = sch.getParent();
      boolean _equals_6 = Objects.equal(_parent_13, null);
      if (_equals_6) {
        _builder.append("\t");
        _builder.append("if (running_process != null)");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("if (getStaticPropertyObject(running_process.refID).pName.trim().equals(processName.trim()))");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("return 1 ;");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("if (super.hasProcess(processName) == 1)");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return 1 ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int result = 0 ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("int processID = 0 ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("for (String pName : processList) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("if (pName.trim().equals(processName.trim())) { //check all process name in process list\t\t\t\t\t");
    _builder.newLine();
    {
      SchedulerDataDef _schedulerdata_10 = sch.getSchedulerdata();
      boolean _notEquals_25 = (!Objects.equal(_schedulerdata_10, null));
      if (_notEquals_25) {
        {
          for(final String col_5 : Data.collectionList) {
            _builder.append("\t\t\t");
            _builder.append("result = ");
            _builder.append(col_5, "\t\t\t");
            _builder.append(".hasProcess(processID);\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t\t\t");
            _builder.append("if (result > 0) return result ;\t\t\t\t");
            _builder.newLine();
          }
        }
      }
    }
    _builder.append("\t\t");
    _builder.append("}\t");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("processID ++ ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("return result ;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genContructor(final ProcessDSL procModel, final SchedulerDef sch) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("//--------------- constructor-------------------------------");
    _builder.newLine();
    _builder.append("public SchedulerObject_");
    String _name = sch.getName();
    _builder.append(_name, "");
    _builder.append("() {");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("//default constructor\t\t\t");
    _builder.newLine();
    {
      HandlerDef _handler = sch.getHandler();
      boolean _notEquals = (!Objects.equal(_handler, null));
      if (_notEquals) {
        _builder.append("\t");
        int numopt = 1;
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        int enew = 1;
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        int eselect = 1;
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        int eclock = 1;
        _builder.append("\t\t\t\t");
        _builder.newLineIfNotEmpty();
        {
          HandlerDef _handler_1 = sch.getHandler();
          EList<EventDef> _event = _handler_1.getEvent();
          for(final EventDef e : _event) {
            {
              EObject _event_1 = e.getEvent();
              if ((_event_1 instanceof EventOpt)) {
                _builder.append("\t");
                EObject _event_2 = e.getEvent();
                final EventOpt ev = ((EventOpt) _event_2);
                _builder.append("\t\t\t\t\t\t");
                _builder.newLineIfNotEmpty();
                {
                  scheduling.dsl.String _eventname = e.getEventname();
                  String _string = _eventname.toString();
                  String _trim = _string.trim();
                  boolean _equals = _trim.equals("new_process");
                  if (_equals) {
                    _builder.append("\t");
                    _builder.append("//new: ");
                    int _numopt = numopt;
                    EList<Opt> _opt = ev.getOpt();
                    int _size = _opt.size();
                    int _multiply = numopt = (_numopt * _size);
                    _builder.append(_multiply, "\t");
                    _builder.append("-");
                    EList<Opt> _opt_1 = ev.getOpt();
                    int _size_1 = _opt_1.size();
                    int _enew = enew = _size_1;
                    _builder.append(_enew, "\t");
                    _builder.append(" ");
                    _builder.newLineIfNotEmpty();
                  } else {
                    {
                      scheduling.dsl.String _eventname_1 = e.getEventname();
                      String _string_1 = _eventname_1.toString();
                      String _trim_1 = _string_1.trim();
                      boolean _equals_1 = _trim_1.equals("select_process");
                      if (_equals_1) {
                        _builder.append("\t");
                        _builder.append("//select: ");
                        int _numopt_1 = numopt;
                        EList<Opt> _opt_2 = ev.getOpt();
                        int _size_2 = _opt_2.size();
                        int _multiply_1 = numopt = (_numopt_1 * _size_2);
                        _builder.append(_multiply_1, "\t");
                        _builder.append("-");
                        EList<Opt> _opt_3 = ev.getOpt();
                        int _size_3 = _opt_3.size();
                        int _eselect = eselect = _size_3;
                        _builder.append(_eselect, "\t");
                        _builder.append(" ");
                        _builder.newLineIfNotEmpty();
                      } else {
                        {
                          scheduling.dsl.String _eventname_2 = e.getEventname();
                          String _string_2 = _eventname_2.toString();
                          String _trim_2 = _string_2.trim();
                          boolean _equals_2 = _trim_2.equals("clock");
                          if (_equals_2) {
                            _builder.append("\t");
                            _builder.append("//clock: ");
                            int _numopt_2 = numopt;
                            EList<Opt> _opt_4 = ev.getOpt();
                            int _size_4 = _opt_4.size();
                            int _multiply_2 = numopt = (_numopt_2 * _size_4);
                            _builder.append(_multiply_2, "\t");
                            _builder.append("-");
                            EList<Opt> _opt_5 = ev.getOpt();
                            int _size_5 = _opt_5.size();
                            int _eclock = eclock = _size_5;
                            _builder.append(_eclock, "\t");
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
        _builder.append("\t");
        _builder.append("_opt = new int[");
        _builder.append((numopt + 1), "\t");
        _builder.append("][3];\t\t\t\t\t");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("int index = 0 ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("for (int i = 1; i <= ");
        _builder.append(enew, "\t");
        _builder.append(" ; i++)");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("for (int j = 1; j <= ");
        _builder.append(eselect, "\t\t");
        _builder.append(" ; j++)");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("for (int k = 1; k <= ");
        _builder.append(eclock, "\t\t\t");
        _builder.append(" ; k++) {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("index ++ ;\t\t\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("_opt[index][0] = i ; //new");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("_opt[index][1] = j ; //select");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t\t");
        _builder.append("_opt[index][2] = k ; //clock\t\t\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("_schnumopt = ");
        _builder.append(numopt, "\t");
        _builder.append(";");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}\t\t");
    _builder.newLine();
    _builder.append("public boolean InitSchedulerObject(String args) {");
    _builder.newLine();
    {
      ParameterList _parameterlist = sch.getParameterlist();
      boolean _notEquals_1 = (!Objects.equal(_parameterlist, null));
      if (_notEquals_1) {
        _builder.append("\t");
        _builder.append("String[] paras = args.split(\",\");\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (paras.length != 0 && paras.length != ");
        ParameterList _parameterlist_1 = sch.getParameterlist();
        EList<ParameterAssign> _para = _parameterlist_1.getPara();
        int _length = ((Object[])Conversions.unwrapArray(_para, Object.class)).length;
        _builder.append(_length, "\t");
        _builder.append(") {");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("System.out.println(\"Cannot initialize the scheduler\");");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("return false;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("if (paras.length != 0) {\t\t\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("//initial the scheduler parameters ");
        int pCount = 0;
        _builder.append("\t\t\t\t");
        _builder.newLineIfNotEmpty();
        {
          ParameterList _parameterlist_2 = sch.getParameterlist();
          EList<ParameterAssign> _para_1 = _parameterlist_2.getPara();
          for(final ParameterAssign para : _para_1) {
            {
              EList<ParameterName> _paraname = para.getParaname();
              for(final ParameterName p : _paraname) {
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("if (paras[");
                _builder.append(pCount, "\t\t");
                _builder.append("].equals(\"\")) {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("//skip parameter ");
                _builder.append(pCount, "\t\t\t");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("} else {");
                _builder.newLine();
                {
                  scheduling.dsl.String _type = para.getType();
                  String _string_3 = _type.toString();
                  boolean _contains = _string_3.contains("int");
                  if (_contains) {
                    _builder.append("\t");
                    _builder.append("\t");
                    _builder.append("\t");
                    String _name_1 = p.getName();
                    _builder.append(_name_1, "\t\t\t");
                    _builder.append(" = Integer.parseInt(paras[");
                    int _plusPlus = pCount++;
                    _builder.append(_plusPlus, "\t\t\t");
                    _builder.append("].trim());");
                    _builder.newLineIfNotEmpty();
                  } else {
                    {
                      scheduling.dsl.String _type_1 = para.getType();
                      String _string_4 = _type_1.toString();
                      boolean _contains_1 = _string_4.contains("bool");
                      if (_contains_1) {
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("\t");
                        String _name_2 = p.getName();
                        _builder.append(_name_2, "\t\t\t");
                        _builder.append(" = Boolean.parseBoolean(paras[");
                        int _plusPlus_1 = pCount++;
                        _builder.append(_plusPlus_1, "\t\t\t");
                        _builder.append("].trim());");
                        _builder.newLineIfNotEmpty();
                      } else {
                        {
                          scheduling.dsl.String _type_2 = para.getType();
                          String _string_5 = _type_2.toString();
                          boolean _contains_2 = _string_5.contains("byte");
                          if (_contains_2) {
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("\t");
                            String _name_3 = p.getName();
                            _builder.append(_name_3, "\t\t\t");
                            _builder.append(" = Integer.parseInt(paras[");
                            int _plusPlus_2 = pCount++;
                            _builder.append(_plusPlus_2, "\t\t\t");
                            _builder.append("].trim());");
                            _builder.newLineIfNotEmpty();
                          } else {
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("System.out.println(\"Error convert parameter\");");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("System.out.println(\"Can not initial the scheduler\");");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("return ;\t\t\t\t\t\t\t ");
                            _builder.newLine();
                          }
                        }
                      }
                    }
                  }
                }
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
      }
    }
    {
      String _parent = sch.getParent();
      boolean _equals_3 = Objects.equal(_parent, null);
      if (_equals_3) {
        _builder.append("\t");
        _builder.append("_runningSet = new RunningSet() ;\t\t\t");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("running_process = null ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("genStaticProcessProperty() ;");
        _builder.newLine();
        {
          DefCore _defcore = Data.schModel.getDefcore();
          boolean _notEquals_2 = (!Objects.equal(_defcore, null));
          if (_notEquals_2) {
            _builder.append("\t");
            _builder.append("current_core = 0 ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("for (int i=0 ; i < ");
            DefCore _defcore_1 = Data.schModel.getDefcore();
            int _numcore = _defcore_1.getNumcore();
            _builder.append(_numcore, "\t");
            _builder.append("; i ++ ) {");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_procs[i] = null ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_putColIndex[i] = new ArrayList<Byte>();");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("super.InitSchedulerObject(args) ;");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("//initial the variables");
    _builder.newLine();
    {
      VarDef _svar = sch.getSvar();
      boolean _notEquals_3 = (!Objects.equal(_svar, null));
      if (_notEquals_3) {
        {
          VarDef _svar_1 = sch.getSvar();
          EList<VarDecl> _vard = _svar_1.getVard();
          boolean _notEquals_4 = (!Objects.equal(_vard, null));
          if (_notEquals_4) {
            {
              VarDef _svar_2 = sch.getSvar();
              EList<VarDecl> _vard_1 = _svar_2.getVard();
              for(final VarDecl vas : _vard_1) {
                {
                  IfDef _ifdef = vas.getIfdef();
                  boolean _notEquals_5 = (!Objects.equal(_ifdef, null));
                  if (_notEquals_5) {
                    _builder.append("\t");
                    _builder.append("if (");
                    IfDef _ifdef_1 = vas.getIfdef();
                    Expression _cond = _ifdef_1.getCond();
                    String _dispatchExpression = Statements.dispatchExpression(_cond);
                    _builder.append(_dispatchExpression, "\t");
                    _builder.append(") {");
                    _builder.newLineIfNotEmpty();
                    {
                      VarBlockDef _varblockdef = vas.getVarblockdef();
                      boolean _notEquals_6 = (!Objects.equal(_varblockdef, null));
                      if (_notEquals_6) {
                        {
                          VarBlockDef _varblockdef_1 = vas.getVarblockdef();
                          EList<VarDefinition> _vardef = _varblockdef_1.getVardef();
                          for(final VarDefinition v : _vardef) {
                            {
                              BoolValue _bvalue = v.getBvalue();
                              boolean _notEquals_7 = (!Objects.equal(_bvalue, null));
                              if (_notEquals_7) {
                                _builder.append("\t");
                                _builder.append("\t");
                                EList<VarName> _name_4 = v.getName();
                                final Function1<VarName, String> _function = (VarName it) -> {
                                  return it.getName();
                                };
                                List<String> _map = ListExtensions.<VarName, String>map(_name_4, _function);
                                String _join = IterableExtensions.join(_map, ", ");
                                BoolValue _bvalue_1 = v.getBvalue();
                                String _value = _bvalue_1.getValue();
                                String _string_6 = _value.toString();
                                String _initValue = Utilities.initValue(_join, _string_6);
                                _builder.append(_initValue, "\t\t");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                            {
                              scheduling.dsl.String _type_3 = v.getType();
                              String _string_7 = _type_3.toString();
                              String _trim_3 = _string_7.trim();
                              boolean _equals_4 = Objects.equal(_trim_3, "time");
                              if (_equals_4) {
                                _builder.append("\t");
                                _builder.append("\t");
                                EList<VarName> _name_5 = v.getName();
                                final Function1<VarName, String> _function_1 = (VarName it) -> {
                                  return it.getName();
                                };
                                List<String> _map_1 = ListExtensions.<VarName, String>map(_name_5, _function_1);
                                String _join_1 = IterableExtensions.join(_map_1, ", ");
                                NumValue _ivalue = v.getIvalue();
                                String _string_8 = _ivalue.toString();
                                String _initValue_1 = Utilities.initValue(_join_1, _string_8);
                                _builder.append(_initValue_1, "\t\t");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                            {
                              if (((!Objects.equal(v.getType().toString().trim(), "bool")) && (!Objects.equal(v.getType().toString().trim(), "time")))) {
                                _builder.append("\t");
                                _builder.append("\t");
                                EList<VarName> _name_6 = v.getName();
                                final Function1<VarName, String> _function_2 = (VarName it) -> {
                                  return it.getName();
                                };
                                List<String> _map_2 = ListExtensions.<VarName, String>map(_name_6, _function_2);
                                String _join_2 = IterableExtensions.join(_map_2, ", ");
                                NumValue _ivalue_1 = v.getIvalue();
                                String _string_9 = _ivalue_1.toString();
                                String _initValue_2 = Utilities.initValue(_join_2, _string_9);
                                _builder.append(_initValue_2, "\t\t");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                          }
                        }
                      } else {
                        {
                          VarSingleDef _varsingledef = vas.getVarsingledef();
                          boolean _notEquals_8 = (!Objects.equal(_varsingledef, null));
                          if (_notEquals_8) {
                            _builder.append("\t");
                            _builder.append("\t");
                            VarSingleDef _varsingledef_1 = vas.getVarsingledef();
                            VarDefinition v_1 = _varsingledef_1.getVardef();
                            _builder.newLineIfNotEmpty();
                            {
                              BoolValue _bvalue_2 = v_1.getBvalue();
                              boolean _notEquals_9 = (!Objects.equal(_bvalue_2, null));
                              if (_notEquals_9) {
                                _builder.append("\t");
                                _builder.append("\t");
                                EList<VarName> _name_7 = v_1.getName();
                                final Function1<VarName, String> _function_3 = (VarName it) -> {
                                  return it.getName();
                                };
                                List<String> _map_3 = ListExtensions.<VarName, String>map(_name_7, _function_3);
                                String _join_3 = IterableExtensions.join(_map_3, ", ");
                                BoolValue _bvalue_3 = v_1.getBvalue();
                                String _value_1 = _bvalue_3.getValue();
                                String _string_10 = _value_1.toString();
                                String _initValue_3 = Utilities.initValue(_join_3, _string_10);
                                _builder.append(_initValue_3, "\t\t");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                            {
                              scheduling.dsl.String _type_4 = v_1.getType();
                              String _string_11 = _type_4.toString();
                              String _trim_4 = _string_11.trim();
                              boolean _equals_5 = Objects.equal(_trim_4, "time");
                              if (_equals_5) {
                                _builder.append("\t");
                                _builder.append("\t");
                                EList<VarName> _name_8 = v_1.getName();
                                final Function1<VarName, String> _function_4 = (VarName it) -> {
                                  return it.getName();
                                };
                                List<String> _map_4 = ListExtensions.<VarName, String>map(_name_8, _function_4);
                                String _join_4 = IterableExtensions.join(_map_4, ", ");
                                NumValue _ivalue_2 = v_1.getIvalue();
                                String _string_12 = _ivalue_2.toString();
                                String _initValue_4 = Utilities.initValue(_join_4, _string_12);
                                _builder.append(_initValue_4, "\t\t");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                            {
                              if (((!Objects.equal(v_1.getType().toString().trim(), "bool")) && (!Objects.equal(v_1.getType().toString().trim(), "time")))) {
                                _builder.append("\t");
                                _builder.append("\t");
                                EList<VarName> _name_9 = v_1.getName();
                                final Function1<VarName, String> _function_5 = (VarName it) -> {
                                  return it.getName();
                                };
                                List<String> _map_5 = ListExtensions.<VarName, String>map(_name_9, _function_5);
                                String _join_5 = IterableExtensions.join(_map_5, ", ");
                                NumValue _ivalue_3 = v_1.getIvalue();
                                String _string_13 = _ivalue_3.toString();
                                String _initValue_5 = Utilities.initValue(_join_5, _string_13);
                                _builder.append(_initValue_5, "\t\t");
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
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("//initial the scheduler variables");
    _builder.newLine();
    {
      SchedulerDataDef _schedulerdata = sch.getSchedulerdata();
      boolean _notEquals_10 = (!Objects.equal(_schedulerdata, null));
      if (_notEquals_10) {
        {
          SchedulerDataDef _schedulerdata_1 = sch.getSchedulerdata();
          EList<DataDef> _datadef = _schedulerdata_1.getDatadef();
          for(final DataDef data : _datadef) {
            {
              DataBlockDef _datablockdef = data.getDatablockdef();
              boolean _notEquals_11 = (!Objects.equal(_datablockdef, null));
              if (_notEquals_11) {
                {
                  DataBlockDef _datablockdef_1 = data.getDatablockdef();
                  EList<DataSingleDef> _datadef_1 = _datablockdef_1.getDatadef();
                  for(final DataSingleDef schdata : _datadef_1) {
                    {
                      SchedulerPropertyDef _prop = schdata.getProp();
                      boolean _notEquals_12 = (!Objects.equal(_prop, null));
                      if (_notEquals_12) {
                        {
                          IfDef _ifdef_2 = data.getIfdef();
                          boolean _notEquals_13 = (!Objects.equal(_ifdef_2, null));
                          if (_notEquals_13) {
                            _builder.append("\t");
                            _builder.append("if (");
                            IfDef _ifdef_3 = data.getIfdef();
                            Expression _cond_1 = _ifdef_3.getCond();
                            String _dispatchExpression_1 = Statements.dispatchExpression(_cond_1);
                            _builder.append(_dispatchExpression_1, "\t");
                            _builder.append(") {");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t");
                            SchedulerPropertyDef vas_1 = schdata.getProp();
                            _builder.newLineIfNotEmpty();
                            {
                              BoolValue _bvalue_4 = vas_1.getBvalue();
                              boolean _notEquals_14 = (!Objects.equal(_bvalue_4, null));
                              if (_notEquals_14) {
                                _builder.append("\t");
                                _builder.append("\t");
                                EList<SchedulerPropertyName> _name_10 = vas_1.getName();
                                final Function1<SchedulerPropertyName, String> _function_6 = (SchedulerPropertyName it) -> {
                                  return it.getName();
                                };
                                List<String> _map_6 = ListExtensions.<SchedulerPropertyName, String>map(_name_10, _function_6);
                                String _join_6 = IterableExtensions.join(_map_6, ", ");
                                BoolValue _bvalue_5 = vas_1.getBvalue();
                                String _value_2 = _bvalue_5.getValue();
                                String _string_14 = _value_2.toString();
                                String _initValue_6 = Utilities.initValue(_join_6, _string_14);
                                _builder.append(_initValue_6, "\t\t");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                            {
                              scheduling.dsl.String _type_5 = vas_1.getType();
                              String _string_15 = _type_5.toString();
                              String _trim_5 = _string_15.trim();
                              boolean _equals_6 = Objects.equal(_trim_5, "time");
                              if (_equals_6) {
                                _builder.append("\t");
                                _builder.append("\t");
                                EList<SchedulerPropertyName> _name_11 = vas_1.getName();
                                final Function1<SchedulerPropertyName, String> _function_7 = (SchedulerPropertyName it) -> {
                                  return it.getName();
                                };
                                List<String> _map_7 = ListExtensions.<SchedulerPropertyName, String>map(_name_11, _function_7);
                                String _join_7 = IterableExtensions.join(_map_7, ", ");
                                NumValue _ivalue_4 = vas_1.getIvalue();
                                String _genNumber = SchedulerGenerator.genNumber(_ivalue_4);
                                String _initValue_7 = Utilities.initValue(_join_7, _genNumber);
                                _builder.append(_initValue_7, "\t\t");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                            {
                              if (((!Objects.equal(vas_1.getType().toString().trim(), "bool")) && (!Objects.equal(vas_1.getType().toString().trim(), "time")))) {
                                _builder.append("\t");
                                _builder.append("\t");
                                EList<SchedulerPropertyName> _name_12 = vas_1.getName();
                                final Function1<SchedulerPropertyName, String> _function_8 = (SchedulerPropertyName it) -> {
                                  return it.getName();
                                };
                                List<String> _map_8 = ListExtensions.<SchedulerPropertyName, String>map(_name_12, _function_8);
                                String _join_8 = IterableExtensions.join(_map_8, ", ");
                                NumValue _ivalue_5 = vas_1.getIvalue();
                                String _genNumber_1 = SchedulerGenerator.genNumber(_ivalue_5);
                                String _initValue_8 = Utilities.initValue(_join_8, _genNumber_1);
                                _builder.append(_initValue_8, "\t\t");
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
              } else {
                {
                  DataSingleDef _datasingledef = data.getDatasingledef();
                  boolean _notEquals_15 = (!Objects.equal(_datasingledef, null));
                  if (_notEquals_15) {
                    _builder.append("\t");
                    DataSingleDef schdata_1 = data.getDatasingledef();
                    _builder.newLineIfNotEmpty();
                    {
                      SchedulerPropertyDef _prop_1 = schdata_1.getProp();
                      boolean _notEquals_16 = (!Objects.equal(_prop_1, null));
                      if (_notEquals_16) {
                        {
                          IfDef _ifdef_4 = data.getIfdef();
                          boolean _notEquals_17 = (!Objects.equal(_ifdef_4, null));
                          if (_notEquals_17) {
                            _builder.append("\t");
                            _builder.append("if (");
                            IfDef _ifdef_5 = data.getIfdef();
                            Expression _cond_2 = _ifdef_5.getCond();
                            String _dispatchExpression_2 = Statements.dispatchExpression(_cond_2);
                            _builder.append(_dispatchExpression_2, "\t");
                            _builder.append(") {");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t");
                            SchedulerPropertyDef vas_2 = schdata_1.getProp();
                            _builder.newLineIfNotEmpty();
                            {
                              BoolValue _bvalue_6 = vas_2.getBvalue();
                              boolean _notEquals_18 = (!Objects.equal(_bvalue_6, null));
                              if (_notEquals_18) {
                                _builder.append("\t");
                                _builder.append("\t");
                                EList<SchedulerPropertyName> _name_13 = vas_2.getName();
                                final Function1<SchedulerPropertyName, String> _function_9 = (SchedulerPropertyName it) -> {
                                  return it.getName();
                                };
                                List<String> _map_9 = ListExtensions.<SchedulerPropertyName, String>map(_name_13, _function_9);
                                String _join_9 = IterableExtensions.join(_map_9, ", ");
                                BoolValue _bvalue_7 = vas_2.getBvalue();
                                String _value_3 = _bvalue_7.getValue();
                                String _string_16 = _value_3.toString();
                                String _initValue_9 = Utilities.initValue(_join_9, _string_16);
                                _builder.append(_initValue_9, "\t\t");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                            {
                              scheduling.dsl.String _type_6 = vas_2.getType();
                              String _string_17 = _type_6.toString();
                              String _trim_6 = _string_17.trim();
                              boolean _equals_7 = Objects.equal(_trim_6, "time");
                              if (_equals_7) {
                                _builder.append("\t");
                                _builder.append("\t");
                                EList<SchedulerPropertyName> _name_14 = vas_2.getName();
                                final Function1<SchedulerPropertyName, String> _function_10 = (SchedulerPropertyName it) -> {
                                  return it.getName();
                                };
                                List<String> _map_10 = ListExtensions.<SchedulerPropertyName, String>map(_name_14, _function_10);
                                String _join_10 = IterableExtensions.join(_map_10, ", ");
                                NumValue _ivalue_6 = vas_2.getIvalue();
                                String _genNumber_2 = SchedulerGenerator.genNumber(_ivalue_6);
                                String _initValue_10 = Utilities.initValue(_join_10, _genNumber_2);
                                _builder.append(_initValue_10, "\t\t");
                                _builder.newLineIfNotEmpty();
                              }
                            }
                            {
                              if (((!Objects.equal(vas_2.getType().toString().trim(), "bool")) && (!Objects.equal(vas_2.getType().toString().trim(), "time")))) {
                                _builder.append("\t");
                                _builder.append("\t");
                                EList<SchedulerPropertyName> _name_15 = vas_2.getName();
                                final Function1<SchedulerPropertyName, String> _function_11 = (SchedulerPropertyName it) -> {
                                  return it.getName();
                                };
                                List<String> _map_11 = ListExtensions.<SchedulerPropertyName, String>map(_name_15, _function_11);
                                String _join_11 = IterableExtensions.join(_map_11, ", ");
                                NumValue _ivalue_7 = vas_2.getIvalue();
                                String _genNumber_3 = SchedulerGenerator.genNumber(_ivalue_7);
                                String _initValue_11 = Utilities.initValue(_join_11, _genNumber_3);
                                _builder.append(_initValue_11, "\t\t");
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
    _builder.append("\t");
    _builder.append("//initial the collections");
    _builder.newLine();
    {
      SchedulerDataDef _schedulerdata_2 = sch.getSchedulerdata();
      boolean _notEquals_19 = (!Objects.equal(_schedulerdata_2, null));
      if (_notEquals_19) {
        {
          SchedulerDataDef _schedulerdata_3 = sch.getSchedulerdata();
          EList<DataDef> _datadef_2 = _schedulerdata_3.getDatadef();
          boolean _notEquals_20 = (!Objects.equal(_datadef_2, null));
          if (_notEquals_20) {
            {
              SchedulerDataDef _schedulerdata_4 = sch.getSchedulerdata();
              EList<DataDef> _datadef_3 = _schedulerdata_4.getDatadef();
              for(final DataDef schdata_2 : _datadef_3) {
                {
                  DataBlockDef _datablockdef_2 = schdata_2.getDatablockdef();
                  boolean _notEquals_21 = (!Objects.equal(_datablockdef_2, null));
                  if (_notEquals_21) {
                    {
                      IfDef _ifdef_6 = schdata_2.getIfdef();
                      boolean _notEquals_22 = (!Objects.equal(_ifdef_6, null));
                      if (_notEquals_22) {
                        _builder.append("\t");
                        _builder.append("if (");
                        IfDef _ifdef_7 = schdata_2.getIfdef();
                        Expression _cond_3 = _ifdef_7.getCond();
                        String _dispatchExpression_3 = Statements.dispatchExpression(_cond_3);
                        _builder.append(_dispatchExpression_3, "\t");
                        _builder.append(") {");
                        _builder.newLineIfNotEmpty();
                      }
                    }
                    {
                      DataBlockDef _datablockdef_3 = schdata_2.getDatablockdef();
                      EList<DataSingleDef> _datadef_4 = _datablockdef_3.getDatadef();
                      for(final DataSingleDef dat : _datadef_4) {
                        {
                          SchedulerCollectionDef _col = dat.getCol();
                          boolean _notEquals_23 = (!Objects.equal(_col, null));
                          if (_notEquals_23) {
                            {
                              if (((!Objects.equal(schdata_2.getIfdef(), null)) || (!Objects.equal(dat.getCol().getParent(), null)))) {
                                {
                                  SchedulerCollectionDef _col_1 = dat.getCol();
                                  EList<ComparationName> _comp = _col_1.getComp();
                                  boolean _notEquals_24 = (!Objects.equal(_comp, null));
                                  if (_notEquals_24) {
                                    {
                                      SchedulerCollectionDef _col_2 = dat.getCol();
                                      scheduling.dsl.String _operationtype = _col_2.getOperationtype();
                                      String _string_18 = _operationtype.toString();
                                      boolean _contains_3 = _string_18.contains("fifo");
                                      if (_contains_3) {
                                        _builder.append("\t");
                                        SchedulerCollectionDef _col_3 = dat.getCol();
                                        SchedulerSet _name_16 = _col_3.getName();
                                        String _name_17 = _name_16.getName();
                                        _builder.append(_name_17, "\t");
                                        _builder.append(" = new ProcessCollection");
                                        SchedulerCollectionDef _col_4 = dat.getCol();
                                        String _collectionName = DataStructureGenerator.getcollectionName(_col_4);
                                        _builder.append(_collectionName, "\t");
                                        _builder.append("_fifo() ;");
                                        _builder.newLineIfNotEmpty();
                                      } else {
                                        {
                                          SchedulerCollectionDef _col_5 = dat.getCol();
                                          scheduling.dsl.String _operationtype_1 = _col_5.getOperationtype();
                                          String _string_19 = _operationtype_1.toString();
                                          boolean _contains_4 = _string_19.contains("lifo");
                                          if (_contains_4) {
                                            _builder.append("\t");
                                            SchedulerCollectionDef _col_6 = dat.getCol();
                                            SchedulerSet _name_18 = _col_6.getName();
                                            String _name_19 = _name_18.getName();
                                            _builder.append(_name_19, "\t");
                                            _builder.append(" = new ProcessCollection");
                                            SchedulerCollectionDef _col_7 = dat.getCol();
                                            String _collectionName_1 = DataStructureGenerator.getcollectionName(_col_7);
                                            _builder.append(_collectionName_1, "\t");
                                            _builder.append("_lifo() ;");
                                            _builder.newLineIfNotEmpty();
                                          } else {
                                            _builder.append("\t");
                                            SchedulerCollectionDef _col_8 = dat.getCol();
                                            SchedulerSet _name_20 = _col_8.getName();
                                            String _name_21 = _name_20.getName();
                                            _builder.append(_name_21, "\t");
                                            _builder.append(" = new ProcessCollection");
                                            SchedulerCollectionDef _col_9 = dat.getCol();
                                            String _collectionName_2 = DataStructureGenerator.getcollectionName(_col_9);
                                            _builder.append(_collectionName_2, "\t");
                                            _builder.append("() ;");
                                            _builder.newLineIfNotEmpty();
                                          }
                                        }
                                      }
                                    }
                                  } else {
                                    {
                                      SchedulerCollectionDef _col_10 = dat.getCol();
                                      scheduling.dsl.String _operationtype_2 = _col_10.getOperationtype();
                                      String _string_20 = _operationtype_2.toString();
                                      boolean _contains_5 = _string_20.contains("fifo");
                                      if (_contains_5) {
                                        _builder.append("\t");
                                        SchedulerCollectionDef _col_11 = dat.getCol();
                                        SchedulerSet _name_22 = _col_11.getName();
                                        String _name_23 = _name_22.getName();
                                        _builder.append(_name_23, "\t");
                                        _builder.append(" = new ProcessCollection_fifo() ;");
                                        _builder.newLineIfNotEmpty();
                                      } else {
                                        {
                                          SchedulerCollectionDef _col_12 = dat.getCol();
                                          scheduling.dsl.String _operationtype_3 = _col_12.getOperationtype();
                                          String _string_21 = _operationtype_3.toString();
                                          boolean _contains_6 = _string_21.contains("lifo");
                                          if (_contains_6) {
                                            _builder.append("\t");
                                            SchedulerCollectionDef _col_13 = dat.getCol();
                                            SchedulerSet _name_24 = _col_13.getName();
                                            String _name_25 = _name_24.getName();
                                            _builder.append(_name_25, "\t");
                                            _builder.append(" = new ProcessCollection_lifo() ;");
                                            _builder.newLineIfNotEmpty();
                                          } else {
                                            _builder.append("\t");
                                            SchedulerCollectionDef _col_14 = dat.getCol();
                                            SchedulerSet _name_26 = _col_14.getName();
                                            String _name_27 = _name_26.getName();
                                            _builder.append(_name_27, "\t");
                                            _builder.append(" = new ProcessCollection() ;");
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
                    {
                      IfDef _ifdef_8 = schdata_2.getIfdef();
                      boolean _notEquals_25 = (!Objects.equal(_ifdef_8, null));
                      if (_notEquals_25) {
                        _builder.append("\t");
                        _builder.append("}");
                        _builder.newLine();
                      }
                    }
                  } else {
                    {
                      DataSingleDef _datasingledef_1 = schdata_2.getDatasingledef();
                      boolean _notEquals_26 = (!Objects.equal(_datasingledef_1, null));
                      if (_notEquals_26) {
                        {
                          IfDef _ifdef_9 = schdata_2.getIfdef();
                          boolean _notEquals_27 = (!Objects.equal(_ifdef_9, null));
                          if (_notEquals_27) {
                            _builder.append("\t");
                            _builder.append("if (");
                            IfDef _ifdef_10 = schdata_2.getIfdef();
                            Expression _cond_4 = _ifdef_10.getCond();
                            String _dispatchExpression_4 = Statements.dispatchExpression(_cond_4);
                            _builder.append(_dispatchExpression_4, "\t");
                            _builder.append(") {");
                            _builder.newLineIfNotEmpty();
                          }
                        }
                        _builder.append("\t");
                        DataSingleDef dat_1 = schdata_2.getDatasingledef();
                        _builder.newLineIfNotEmpty();
                        {
                          SchedulerCollectionDef _col_15 = dat_1.getCol();
                          boolean _notEquals_28 = (!Objects.equal(_col_15, null));
                          if (_notEquals_28) {
                            {
                              if (((!Objects.equal(schdata_2.getIfdef(), null)) || (!Objects.equal(dat_1.getCol().getParent(), null)))) {
                                {
                                  SchedulerCollectionDef _col_16 = dat_1.getCol();
                                  EList<ComparationName> _comp_1 = _col_16.getComp();
                                  boolean _notEquals_29 = (!Objects.equal(_comp_1, null));
                                  if (_notEquals_29) {
                                    {
                                      SchedulerCollectionDef _col_17 = dat_1.getCol();
                                      scheduling.dsl.String _operationtype_4 = _col_17.getOperationtype();
                                      String _string_22 = _operationtype_4.toString();
                                      boolean _contains_7 = _string_22.contains("fifo");
                                      if (_contains_7) {
                                        _builder.append("\t");
                                        SchedulerCollectionDef _col_18 = dat_1.getCol();
                                        SchedulerSet _name_28 = _col_18.getName();
                                        String _name_29 = _name_28.getName();
                                        _builder.append(_name_29, "\t");
                                        _builder.append(" = new ProcessCollection");
                                        SchedulerCollectionDef _col_19 = dat_1.getCol();
                                        String _collectionName_3 = DataStructureGenerator.getcollectionName(_col_19);
                                        _builder.append(_collectionName_3, "\t");
                                        _builder.append("_fifo() ;");
                                        _builder.newLineIfNotEmpty();
                                      } else {
                                        {
                                          SchedulerCollectionDef _col_20 = dat_1.getCol();
                                          scheduling.dsl.String _operationtype_5 = _col_20.getOperationtype();
                                          String _string_23 = _operationtype_5.toString();
                                          boolean _contains_8 = _string_23.contains("lifo");
                                          if (_contains_8) {
                                            _builder.append("\t");
                                            SchedulerCollectionDef _col_21 = dat_1.getCol();
                                            SchedulerSet _name_30 = _col_21.getName();
                                            String _name_31 = _name_30.getName();
                                            _builder.append(_name_31, "\t");
                                            _builder.append(" = new ProcessCollection");
                                            SchedulerCollectionDef _col_22 = dat_1.getCol();
                                            String _collectionName_4 = DataStructureGenerator.getcollectionName(_col_22);
                                            _builder.append(_collectionName_4, "\t");
                                            _builder.append("_lifo() ;");
                                            _builder.newLineIfNotEmpty();
                                          } else {
                                            _builder.append("\t");
                                            SchedulerCollectionDef _col_23 = dat_1.getCol();
                                            SchedulerSet _name_32 = _col_23.getName();
                                            String _name_33 = _name_32.getName();
                                            _builder.append(_name_33, "\t");
                                            _builder.append(" = new ProcessCollection");
                                            SchedulerCollectionDef _col_24 = dat_1.getCol();
                                            String _collectionName_5 = DataStructureGenerator.getcollectionName(_col_24);
                                            _builder.append(_collectionName_5, "\t");
                                            _builder.append("() ;");
                                            _builder.newLineIfNotEmpty();
                                          }
                                        }
                                      }
                                    }
                                  } else {
                                    {
                                      SchedulerCollectionDef _col_25 = dat_1.getCol();
                                      scheduling.dsl.String _operationtype_6 = _col_25.getOperationtype();
                                      String _string_24 = _operationtype_6.toString();
                                      boolean _contains_9 = _string_24.contains("fifo");
                                      if (_contains_9) {
                                        _builder.append("\t");
                                        SchedulerCollectionDef _col_26 = dat_1.getCol();
                                        SchedulerSet _name_34 = _col_26.getName();
                                        String _name_35 = _name_34.getName();
                                        _builder.append(_name_35, "\t");
                                        _builder.append(" = new ProcessCollection_fifo() ;");
                                        _builder.newLineIfNotEmpty();
                                      } else {
                                        {
                                          SchedulerCollectionDef _col_27 = dat_1.getCol();
                                          scheduling.dsl.String _operationtype_7 = _col_27.getOperationtype();
                                          String _string_25 = _operationtype_7.toString();
                                          boolean _contains_10 = _string_25.contains("lifo");
                                          if (_contains_10) {
                                            _builder.append("\t");
                                            SchedulerCollectionDef _col_28 = dat_1.getCol();
                                            SchedulerSet _name_36 = _col_28.getName();
                                            String _name_37 = _name_36.getName();
                                            _builder.append(_name_37, "\t");
                                            _builder.append(" = new ProcessCollection_lifo() ;");
                                            _builder.newLineIfNotEmpty();
                                          } else {
                                            _builder.append("\t");
                                            SchedulerCollectionDef _col_29 = dat_1.getCol();
                                            SchedulerSet _name_38 = _col_29.getName();
                                            String _name_39 = _name_38.getName();
                                            _builder.append(_name_39, "\t");
                                            _builder.append(" = new ProcessCollection() ;");
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
                              IfDef _ifdef_11 = schdata_2.getIfdef();
                              boolean _notEquals_30 = (!Objects.equal(_ifdef_11, null));
                              if (_notEquals_30) {
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
        _builder.append("\t");
        _builder.append("//ensure the collections are not null");
        _builder.newLine();
        {
          SchedulerDataDef _schedulerdata_5 = sch.getSchedulerdata();
          boolean _notEquals_31 = (!Objects.equal(_schedulerdata_5, null));
          if (_notEquals_31) {
            {
              SchedulerDataDef _schedulerdata_6 = sch.getSchedulerdata();
              EList<DataDef> _datadef_5 = _schedulerdata_6.getDatadef();
              for(final DataDef dat_2 : _datadef_5) {
                {
                  DataBlockDef _datablockdef_4 = dat_2.getDatablockdef();
                  boolean _notEquals_32 = (!Objects.equal(_datablockdef_4, null));
                  if (_notEquals_32) {
                    {
                      DataBlockDef _datablockdef_5 = dat_2.getDatablockdef();
                      EList<DataSingleDef> _datadef_6 = _datablockdef_5.getDatadef();
                      for(final DataSingleDef schdata_3 : _datadef_6) {
                        {
                          SchedulerCollectionDef _col_30 = schdata_3.getCol();
                          boolean _notEquals_33 = (!Objects.equal(_col_30, null));
                          if (_notEquals_33) {
                            _builder.append("\t");
                            _builder.append("if (");
                            SchedulerCollectionDef _col_31 = schdata_3.getCol();
                            SchedulerSet _name_40 = _col_31.getName();
                            String _name_41 = _name_40.getName();
                            _builder.append(_name_41, "\t");
                            _builder.append(" == null) {");
                            _builder.newLineIfNotEmpty();
                            {
                              SchedulerCollectionDef _col_32 = schdata_3.getCol();
                              EList<ComparationName> _comp_2 = _col_32.getComp();
                              boolean _notEquals_34 = (!Objects.equal(_comp_2, null));
                              if (_notEquals_34) {
                                {
                                  SchedulerCollectionDef _col_33 = schdata_3.getCol();
                                  scheduling.dsl.String _operationtype_8 = _col_33.getOperationtype();
                                  String _string_26 = _operationtype_8.toString();
                                  boolean _contains_11 = _string_26.contains("fifo");
                                  if (_contains_11) {
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    SchedulerCollectionDef _col_34 = schdata_3.getCol();
                                    SchedulerSet _name_42 = _col_34.getName();
                                    String _name_43 = _name_42.getName();
                                    _builder.append(_name_43, "\t\t");
                                    _builder.append(" = new ProcessCollection");
                                    SchedulerCollectionDef _col_35 = schdata_3.getCol();
                                    String _collectionName_6 = DataStructureGenerator.getcollectionName(_col_35);
                                    _builder.append(_collectionName_6, "\t\t");
                                    _builder.append("_fifo() ;");
                                    _builder.newLineIfNotEmpty();
                                  } else {
                                    {
                                      SchedulerCollectionDef _col_36 = schdata_3.getCol();
                                      scheduling.dsl.String _operationtype_9 = _col_36.getOperationtype();
                                      String _string_27 = _operationtype_9.toString();
                                      boolean _contains_12 = _string_27.contains("lifo");
                                      if (_contains_12) {
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        SchedulerCollectionDef _col_37 = schdata_3.getCol();
                                        SchedulerSet _name_44 = _col_37.getName();
                                        String _name_45 = _name_44.getName();
                                        _builder.append(_name_45, "\t\t");
                                        _builder.append(" = new ProcessCollection");
                                        SchedulerCollectionDef _col_38 = schdata_3.getCol();
                                        String _collectionName_7 = DataStructureGenerator.getcollectionName(_col_38);
                                        _builder.append(_collectionName_7, "\t\t");
                                        _builder.append("_lifo() ;");
                                        _builder.newLineIfNotEmpty();
                                      } else {
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        SchedulerCollectionDef _col_39 = schdata_3.getCol();
                                        SchedulerSet _name_46 = _col_39.getName();
                                        String _name_47 = _name_46.getName();
                                        _builder.append(_name_47, "\t\t");
                                        _builder.append(" = new ProcessCollection");
                                        SchedulerCollectionDef _col_40 = schdata_3.getCol();
                                        String _collectionName_8 = DataStructureGenerator.getcollectionName(_col_40);
                                        _builder.append(_collectionName_8, "\t\t");
                                        _builder.append("() ;");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              } else {
                                {
                                  SchedulerCollectionDef _col_41 = schdata_3.getCol();
                                  scheduling.dsl.String _operationtype_10 = _col_41.getOperationtype();
                                  String _string_28 = _operationtype_10.toString();
                                  boolean _contains_13 = _string_28.contains("fifo");
                                  if (_contains_13) {
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    SchedulerCollectionDef _col_42 = schdata_3.getCol();
                                    SchedulerSet _name_48 = _col_42.getName();
                                    String _name_49 = _name_48.getName();
                                    _builder.append(_name_49, "\t\t");
                                    _builder.append(" = new ProcessCollection_fifo() ;");
                                    _builder.newLineIfNotEmpty();
                                  } else {
                                    {
                                      SchedulerCollectionDef _col_43 = schdata_3.getCol();
                                      scheduling.dsl.String _operationtype_11 = _col_43.getOperationtype();
                                      String _string_29 = _operationtype_11.toString();
                                      boolean _contains_14 = _string_29.contains("lifo");
                                      if (_contains_14) {
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        SchedulerCollectionDef _col_44 = schdata_3.getCol();
                                        SchedulerSet _name_50 = _col_44.getName();
                                        String _name_51 = _name_50.getName();
                                        _builder.append(_name_51, "\t\t");
                                        _builder.append(" = new ProcessCollection_lifo() ;");
                                        _builder.newLineIfNotEmpty();
                                      } else {
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        SchedulerCollectionDef _col_45 = schdata_3.getCol();
                                        SchedulerSet _name_52 = _col_45.getName();
                                        String _name_53 = _name_52.getName();
                                        _builder.append(_name_53, "\t\t");
                                        _builder.append(" = new ProcessCollection() ;");
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
                      DataSingleDef _datasingledef_2 = dat_2.getDatasingledef();
                      boolean _notEquals_35 = (!Objects.equal(_datasingledef_2, null));
                      if (_notEquals_35) {
                        _builder.append("\t");
                        DataSingleDef schdata_4 = dat_2.getDatasingledef();
                        _builder.newLineIfNotEmpty();
                        {
                          SchedulerCollectionDef _col_46 = schdata_4.getCol();
                          boolean _notEquals_36 = (!Objects.equal(_col_46, null));
                          if (_notEquals_36) {
                            _builder.append("\t");
                            _builder.append("if (");
                            SchedulerCollectionDef _col_47 = schdata_4.getCol();
                            SchedulerSet _name_54 = _col_47.getName();
                            String _name_55 = _name_54.getName();
                            _builder.append(_name_55, "\t");
                            _builder.append(" == null) {");
                            _builder.newLineIfNotEmpty();
                            {
                              SchedulerCollectionDef _col_48 = schdata_4.getCol();
                              EList<ComparationName> _comp_3 = _col_48.getComp();
                              boolean _notEquals_37 = (!Objects.equal(_comp_3, null));
                              if (_notEquals_37) {
                                {
                                  SchedulerCollectionDef _col_49 = schdata_4.getCol();
                                  scheduling.dsl.String _operationtype_12 = _col_49.getOperationtype();
                                  String _string_30 = _operationtype_12.toString();
                                  boolean _contains_15 = _string_30.contains("fifo");
                                  if (_contains_15) {
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    SchedulerCollectionDef _col_50 = schdata_4.getCol();
                                    SchedulerSet _name_56 = _col_50.getName();
                                    String _name_57 = _name_56.getName();
                                    _builder.append(_name_57, "\t\t");
                                    _builder.append(" = new ProcessCollection");
                                    SchedulerCollectionDef _col_51 = schdata_4.getCol();
                                    String _collectionName_9 = DataStructureGenerator.getcollectionName(_col_51);
                                    _builder.append(_collectionName_9, "\t\t");
                                    _builder.append("_fifo() ;");
                                    _builder.newLineIfNotEmpty();
                                  } else {
                                    {
                                      SchedulerCollectionDef _col_52 = schdata_4.getCol();
                                      scheduling.dsl.String _operationtype_13 = _col_52.getOperationtype();
                                      String _string_31 = _operationtype_13.toString();
                                      boolean _contains_16 = _string_31.contains("lifo");
                                      if (_contains_16) {
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        SchedulerCollectionDef _col_53 = schdata_4.getCol();
                                        SchedulerSet _name_58 = _col_53.getName();
                                        String _name_59 = _name_58.getName();
                                        _builder.append(_name_59, "\t\t");
                                        _builder.append(" = new ProcessCollection");
                                        SchedulerCollectionDef _col_54 = schdata_4.getCol();
                                        String _collectionName_10 = DataStructureGenerator.getcollectionName(_col_54);
                                        _builder.append(_collectionName_10, "\t\t");
                                        _builder.append("_lifo() ;");
                                        _builder.newLineIfNotEmpty();
                                      } else {
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        SchedulerCollectionDef _col_55 = schdata_4.getCol();
                                        SchedulerSet _name_60 = _col_55.getName();
                                        String _name_61 = _name_60.getName();
                                        _builder.append(_name_61, "\t\t");
                                        _builder.append(" = new ProcessCollection");
                                        SchedulerCollectionDef _col_56 = schdata_4.getCol();
                                        String _collectionName_11 = DataStructureGenerator.getcollectionName(_col_56);
                                        _builder.append(_collectionName_11, "\t\t");
                                        _builder.append("() ;");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              } else {
                                {
                                  SchedulerCollectionDef _col_57 = schdata_4.getCol();
                                  scheduling.dsl.String _operationtype_14 = _col_57.getOperationtype();
                                  String _string_32 = _operationtype_14.toString();
                                  boolean _contains_17 = _string_32.contains("fifo");
                                  if (_contains_17) {
                                    _builder.append("\t");
                                    _builder.append("\t");
                                    SchedulerCollectionDef _col_58 = schdata_4.getCol();
                                    SchedulerSet _name_62 = _col_58.getName();
                                    String _name_63 = _name_62.getName();
                                    _builder.append(_name_63, "\t\t");
                                    _builder.append(" = new ProcessCollection_fifo() ;");
                                    _builder.newLineIfNotEmpty();
                                  } else {
                                    {
                                      SchedulerCollectionDef _col_59 = schdata_4.getCol();
                                      scheduling.dsl.String _operationtype_15 = _col_59.getOperationtype();
                                      String _string_33 = _operationtype_15.toString();
                                      boolean _contains_18 = _string_33.contains("lifo");
                                      if (_contains_18) {
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        SchedulerCollectionDef _col_60 = schdata_4.getCol();
                                        SchedulerSet _name_64 = _col_60.getName();
                                        String _name_65 = _name_64.getName();
                                        _builder.append(_name_65, "\t\t");
                                        _builder.append(" = new ProcessCollection_lifo() ;");
                                        _builder.newLineIfNotEmpty();
                                      } else {
                                        _builder.append("\t");
                                        _builder.append("\t");
                                        SchedulerCollectionDef _col_61 = schdata_4.getCol();
                                        SchedulerSet _name_66 = _col_61.getName();
                                        String _name_67 = _name_66.getName();
                                        _builder.append(_name_67, "\t\t");
                                        _builder.append(" = new ProcessCollection() ;");
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
                  }
                }
              }
            }
          }
        }
      }
    }
    _builder.append("\t");
    _builder.append("return true ;");
    _builder.newLine();
    _builder.append("}\t");
    _builder.newLine();
    _builder.append("public int get_init_process_count() {");
    _builder.newLine();
    {
      String _parent_1 = sch.getParent();
      boolean _notEquals_38 = (!Objects.equal(_parent_1, null));
      if (_notEquals_38) {
        _builder.append("\t");
        _builder.append("return super.get_init_process_count() ;");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("int pcnt = 0 ;");
        _builder.newLine();
        {
          boolean _notEquals_39 = (!Objects.equal(procModel, null));
          if (_notEquals_39) {
            {
              ProcessInit _processinit = procModel.getProcessinit();
              boolean _notEquals_40 = (!Objects.equal(_processinit, null));
              if (_notEquals_40) {
                {
                  ProcessInit _processinit_1 = procModel.getProcessinit();
                  EList<scheduling.dsl.Set> _order = _processinit_1.getOrder();
                  for(final scheduling.dsl.Set ps : _order) {
                    {
                      String _set = ps.getSet();
                      boolean _notEquals_41 = (!Objects.equal(_set, null));
                      if (_notEquals_41) {
                        _builder.append("\t");
                        _builder.append("pcnt += ");
                        EList<Element> _element = ps.getElement();
                        int _size_6 = _element.size();
                        _builder.append(_size_6, "\t");
                        _builder.append(" ;\t");
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
        _builder.append("return pcnt;");
        _builder.newLine();
      }
    }
    _builder.append("}\t\t\t\t");
    _builder.newLine();
    _builder.append("public void init_order() throws ValidationException {");
    _builder.newLine();
    {
      String _parent_2 = sch.getParent();
      boolean _notEquals_42 = (!Objects.equal(_parent_2, null));
      if (_notEquals_42) {
        _builder.append("\t");
        _builder.append("super.init_order() ;");
        _builder.newLine();
      } else {
        {
          boolean _notEquals_43 = (!Objects.equal(procModel, null));
          if (_notEquals_43) {
            {
              ProcessInit _processinit_2 = procModel.getProcessinit();
              boolean _notEquals_44 = (!Objects.equal(_processinit_2, null));
              if (_notEquals_44) {
                _builder.append("\t");
                _builder.append("//initial the order of processes (using order defined in process DSL)");
                _builder.newLine();
                _builder.append("\t");
                _builder.append("ArrayList<SchedulerProcess> procList = new ArrayList<SchedulerProcess>() ;");
                _builder.newLine();
                {
                  ProcessInit _processinit_3 = procModel.getProcessinit();
                  EList<scheduling.dsl.Set> _order_1 = _processinit_3.getOrder();
                  for(final scheduling.dsl.Set ps_1 : _order_1) {
                    {
                      String _set_1 = ps_1.getSet();
                      boolean _notEquals_45 = (!Objects.equal(_set_1, null));
                      if (_notEquals_45) {
                        {
                          EList<Element> _element_1 = ps_1.getElement();
                          for(final Element pn : _element_1) {
                            _builder.append("\t");
                            _builder.append("{");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("int processID = getProcessID(\"");
                            scheduling.dsl.Process _process = pn.getProcess();
                            String _name_68 = _process.getName();
                            _builder.append(_name_68, "\t\t");
                            _builder.append("\") ;");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("if (processID >= 0) { ");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.append("//create new process in model");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.append("//SchedulerPanModel.p");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.append("//create new process information in scheduler");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.append("SchedulerProcess ");
                            scheduling.dsl.Process _process_1 = pn.getProcess();
                            String _name_69 = _process_1.getName();
                            _builder.append(_name_69, "\t\t\t");
                            _builder.append(" = new SchedulerProcess() ;");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.append("//");
                            scheduling.dsl.Process _process_2 = pn.getProcess();
                            String _name_70 = _process_2.getName();
                            _builder.append(_name_70, "\t\t\t");
                            _builder.append(".processID = (byte) processID ;");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            scheduling.dsl.Process _process_3 = pn.getProcess();
                            String _name_71 = _process_3.getName();
                            _builder.append(_name_71, "\t\t\t");
                            _builder.append(".processID = processID ;");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.append("while (pcnt.size() < processID + 1) pcnt.add((byte) 0) ;\t\t\t\t");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.append("pcnt.set(processID, (byte) (pcnt.get(processID) + 1));");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t\t\t\t\t\t");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            scheduling.dsl.Process _process_4 = pn.getProcess();
                            String _name_72 = _process_4.getName();
                            _builder.append(_name_72, "\t\t\t");
                            _builder.append(".refID = getRefID(\"");
                            scheduling.dsl.Process _process_5 = pn.getProcess();
                            String _name_73 = _process_5.getName();
                            _builder.append(_name_73, "\t\t\t");
                            _builder.append("\") ;\t\t\t\t\t\t\t\t\t\t");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            String _processInitFunction = ProcessGenerator.getProcessInitFunction(pn, procModel);
                            _builder.append(_processInitFunction, "\t\t\t");
                            _builder.append("\t\t\t\t\t\t\t\t\t\t");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.append("//processList.set(processID, \"");
                            String _periodicProcessKey = SchedulerGenerator.getPeriodicProcessKey(pn);
                            _builder.append(_periodicProcessKey, "\t\t\t");
                            _builder.append("_0\") ;");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.append("processList.set(processID, \"");
                            scheduling.dsl.Process _process_6 = pn.getProcess();
                            String _name_74 = _process_6.getName();
                            _builder.append(_name_74, "\t\t\t");
                            _builder.append("\") ;");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("\t\t");
                            _builder.append("procList.add(");
                            scheduling.dsl.Process _process_7 = pn.getProcess();
                            String _name_75 = _process_7.getName();
                            _builder.append(_name_75, "\t\t\t");
                            _builder.append(") ;");
                            _builder.newLineIfNotEmpty();
                            _builder.append("\t");
                            _builder.append("\t");
                            _builder.append("}// else ignore this initial process");
                            _builder.newLine();
                            _builder.append("\t");
                            _builder.append("}");
                            _builder.newLine();
                          }
                        }
                        _builder.append("\t");
                        _builder.append("if (!procList.isEmpty()) {");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("addProcessList(procList) ;");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("\t");
                        _builder.append("procList.clear() ;");
                        _builder.newLine();
                        _builder.append("\t");
                        _builder.append("}\t\t\t\t\t\t\t");
                        _builder.newLine();
                      }
                    }
                    _builder.append("\t");
                    _builder.append("//---------------------------------------");
                    _builder.newLine();
                  }
                }
              } else {
                _builder.append("\t");
                _builder.append("//setting all processes in stack and queue into the same poset");
                _builder.newLine();
                {
                  for(final String col : Data.collectionList) {
                    _builder.append("\t");
                    _builder.append(col, "\t");
                    _builder.append(".init_order() ;");
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
    _builder.append("\t\t");
    _builder.newLine();
    _builder.append("public void init() { ");
    _builder.newLine();
    {
      Generate _gen = sch.getGen();
      boolean _notEquals_46 = (!Objects.equal(_gen, null));
      if (_notEquals_46) {
        _builder.append("\t");
        _builder.append("//for the generation");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("Generate.out.clear();");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("Generate.initGeneration();");
        _builder.newLine();
      }
    }
    {
      SchedulerInit _schedulerinit = sch.getSchedulerinit();
      boolean _notEquals_47 = (!Objects.equal(_schedulerinit, null));
      if (_notEquals_47) {
        _builder.append("\t");
        _builder.append("//init statement");
        _builder.newLine();
        {
          SchedulerInit _schedulerinit_1 = sch.getSchedulerinit();
          EList<Statement> _initstm = _schedulerinit_1.getInitstm();
          for(final Statement stm : _initstm) {
            _builder.append("\t");
            CharSequence _dispatchStatement = Statements.dispatchStatement(stm, "");
            _builder.append(_dispatchStatement, "\t");
            _builder.newLineIfNotEmpty();
          }
        }
      }
    }
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static CharSequence genEncodeDecode(final SchedulerDef sch) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("public void encode(DataWriter _writer) {\t\t\t\t\t");
    _builder.newLine();
    {
      String _parent = sch.getParent();
      boolean _equals = Objects.equal(_parent, null);
      if (_equals) {
        {
          DefCore _defcore = Data.schModel.getDefcore();
          boolean _notEquals = (!Objects.equal(_defcore, null));
          if (_notEquals) {
            _builder.append("\t");
            _builder.append("//_writer.writeInt(current_core);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("for (int i=0; i<");
            DefCore _defcore_1 = Data.schModel.getDefcore();
            int _numcore = _defcore_1.getNumcore();
            _builder.append(_numcore, "\t");
            _builder.append(";i++){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (running_procs[i] != null) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_writer.writeBool(true);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("running_procs[i].encode(_writer);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("} else {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_writer.writeBool(false);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_writer.writeByte(_putColIndex[i].size());");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("for (byte id : _putColIndex[i])");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_writer.writeByte(id) ; //for replace running process (selection)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("//_writer.writeInt(_schselopt);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("if (running_process == null)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_writer.writeBool(false);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("else {\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_writer.writeBool(true);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_process.encode(_writer);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("_writer.writeByte(_putColIndex) ; //for replace running process (selection)");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.newLine();
      } else {
        _builder.append("\t");
        _builder.append("super.encode(_writer) ;");
        _builder.newLine();
      }
    }
    {
      if (Data.runTime) {
        {
          DefCore _defcore_2 = Data.schModel.getDefcore();
          boolean _notEquals_1 = (!Objects.equal(_defcore_2, null));
          if (_notEquals_1) {
            _builder.append("\t");
            _builder.append("for (int i=0; i<");
            DefCore _defcore_3 = Data.schModel.getDefcore();
            int _numcore_1 = _defcore_3.getNumcore();
            _builder.append(_numcore_1, "\t");
            _builder.append(";i++){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_writer.writeInt(_time_count[i]);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_writer.writeInt(_time_slice[i]);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("//could be duplicated!");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("_writer.writeInt(_time_count);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("_writer.writeInt(_time_slice);");
            _builder.newLine();
          }
        }
      }
    }
    {
      VarDef _svar = sch.getSvar();
      boolean _notEquals_2 = (!Objects.equal(_svar, null));
      if (_notEquals_2) {
        {
          VarDef _svar_1 = sch.getSvar();
          EList<VarDecl> _vard = _svar_1.getVard();
          boolean _notEquals_3 = (!Objects.equal(_vard, null));
          if (_notEquals_3) {
            _builder.append("\t");
            _builder.append("//scheduler variables ");
            ArrayList<String> varlist = new ArrayList<String>();
            _builder.newLineIfNotEmpty();
            {
              VarDef _svar_2 = sch.getSvar();
              EList<VarDecl> _vard_1 = _svar_2.getVard();
              for(final VarDecl v : _vard_1) {
                {
                  VarBlockDef _varblockdef = v.getVarblockdef();
                  boolean _notEquals_4 = (!Objects.equal(_varblockdef, null));
                  if (_notEquals_4) {
                    {
                      VarBlockDef _varblockdef_1 = v.getVarblockdef();
                      EList<VarDefinition> _vardef = _varblockdef_1.getVardef();
                      for(final VarDefinition vas : _vardef) {
                        {
                          IfDef _ifdef = v.getIfdef();
                          boolean _equals_1 = Objects.equal(_ifdef, null);
                          if (_equals_1) {
                            {
                              scheduling.dsl.String _type = vas.getType();
                              String _string = _type.toString();
                              String _trim = _string.trim();
                              boolean _equals_2 = Objects.equal(_trim, "bool");
                              if (_equals_2) {
                                {
                                  EList<VarName> _name = vas.getName();
                                  for(final VarName va : _name) {
                                    {
                                      String _name_1 = va.getName();
                                      boolean _contains = varlist.contains(_name_1);
                                      boolean _not = (!_contains);
                                      if (_not) {
                                        _builder.append("\t");
                                        _builder.append("_writer.writeBool(");
                                        String _name_2 = va.getName();
                                        _builder.append(_name_2, "\t");
                                        _builder.append("); //add ");
                                        String _name_3 = va.getName();
                                        _builder.append(_name_3, "\t");
                                        _builder.append(": ");
                                        String _name_4 = va.getName();
                                        boolean _add = varlist.add(_name_4);
                                        _builder.append(_add, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_1 = vas.getType();
                              String _string_1 = _type_1.toString();
                              String _trim_1 = _string_1.trim();
                              boolean _equals_3 = Objects.equal(_trim_1, "time");
                              if (_equals_3) {
                                {
                                  EList<VarName> _name_5 = vas.getName();
                                  for(final VarName va_1 : _name_5) {
                                    {
                                      String _name_6 = va_1.getName();
                                      boolean _contains_1 = varlist.contains(_name_6);
                                      boolean _not_1 = (!_contains_1);
                                      if (_not_1) {
                                        _builder.append("\t");
                                        _builder.append("_writer.writeInt(");
                                        String _name_7 = va_1.getName();
                                        _builder.append(_name_7, "\t");
                                        _builder.append("); //add ");
                                        String _name_8 = va_1.getName();
                                        _builder.append(_name_8, "\t");
                                        _builder.append(": ");
                                        String _name_9 = va_1.getName();
                                        boolean _add_1 = varlist.add(_name_9);
                                        _builder.append(_add_1, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_2 = vas.getType();
                              String _string_2 = _type_2.toString();
                              String _trim_2 = _string_2.trim();
                              boolean _equals_4 = Objects.equal(_trim_2, "int");
                              if (_equals_4) {
                                {
                                  EList<VarName> _name_10 = vas.getName();
                                  for(final VarName va_2 : _name_10) {
                                    {
                                      String _name_11 = va_2.getName();
                                      boolean _contains_2 = varlist.contains(_name_11);
                                      boolean _not_2 = (!_contains_2);
                                      if (_not_2) {
                                        _builder.append("\t");
                                        _builder.append("_writer.writeInt(");
                                        String _name_12 = va_2.getName();
                                        _builder.append(_name_12, "\t");
                                        _builder.append("); //add ");
                                        String _name_13 = va_2.getName();
                                        _builder.append(_name_13, "\t");
                                        _builder.append(": ");
                                        String _name_14 = va_2.getName();
                                        boolean _add_2 = varlist.add(_name_14);
                                        _builder.append(_add_2, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_3 = vas.getType();
                              String _string_3 = _type_3.toString();
                              String _trim_3 = _string_3.trim();
                              boolean _equals_5 = Objects.equal(_trim_3, "clock");
                              if (_equals_5) {
                                {
                                  EList<VarName> _name_15 = vas.getName();
                                  for(final VarName va_3 : _name_15) {
                                    {
                                      String _name_16 = va_3.getName();
                                      boolean _contains_3 = varlist.contains(_name_16);
                                      boolean _not_3 = (!_contains_3);
                                      if (_not_3) {
                                        _builder.append("\t");
                                        _builder.append("_writer.writeInt(");
                                        String _name_17 = va_3.getName();
                                        _builder.append(_name_17, "\t");
                                        _builder.append("); //add ");
                                        String _name_18 = va_3.getName();
                                        _builder.append(_name_18, "\t");
                                        _builder.append(": ");
                                        String _name_19 = va_3.getName();
                                        boolean _add_3 = varlist.add(_name_19);
                                        _builder.append(_add_3, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                          } else {
                            {
                              scheduling.dsl.String _type_4 = vas.getType();
                              String _string_4 = _type_4.toString();
                              String _trim_4 = _string_4.trim();
                              boolean _equals_6 = Objects.equal(_trim_4, "bool");
                              if (_equals_6) {
                                {
                                  EList<VarName> _name_20 = vas.getName();
                                  for(final VarName va_4 : _name_20) {
                                    {
                                      String _name_21 = va_4.getName();
                                      boolean _contains_4 = varlist.contains(_name_21);
                                      boolean _not_4 = (!_contains_4);
                                      if (_not_4) {
                                        _builder.append("\t");
                                        _builder.append("_writer.writeBool(");
                                        String _name_22 = va_4.getName();
                                        _builder.append(_name_22, "\t");
                                        _builder.append("); //add ");
                                        String _name_23 = va_4.getName();
                                        _builder.append(_name_23, "\t");
                                        _builder.append(": ");
                                        String _name_24 = va_4.getName();
                                        boolean _add_4 = varlist.add(_name_24);
                                        _builder.append(_add_4, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_5 = vas.getType();
                              String _string_5 = _type_5.toString();
                              String _trim_5 = _string_5.trim();
                              boolean _equals_7 = Objects.equal(_trim_5, "time");
                              if (_equals_7) {
                                {
                                  EList<VarName> _name_25 = vas.getName();
                                  for(final VarName va_5 : _name_25) {
                                    {
                                      String _name_26 = va_5.getName();
                                      boolean _contains_5 = varlist.contains(_name_26);
                                      boolean _not_5 = (!_contains_5);
                                      if (_not_5) {
                                        _builder.append("\t");
                                        _builder.append("_writer.writeInt(");
                                        String _name_27 = va_5.getName();
                                        _builder.append(_name_27, "\t");
                                        _builder.append("); //add ");
                                        String _name_28 = va_5.getName();
                                        _builder.append(_name_28, "\t");
                                        _builder.append(": ");
                                        String _name_29 = va_5.getName();
                                        boolean _add_5 = varlist.add(_name_29);
                                        _builder.append(_add_5, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_6 = vas.getType();
                              String _string_6 = _type_6.toString();
                              String _trim_6 = _string_6.trim();
                              boolean _equals_8 = Objects.equal(_trim_6, "int");
                              if (_equals_8) {
                                {
                                  EList<VarName> _name_30 = vas.getName();
                                  for(final VarName va_6 : _name_30) {
                                    {
                                      String _name_31 = va_6.getName();
                                      boolean _contains_6 = varlist.contains(_name_31);
                                      boolean _not_6 = (!_contains_6);
                                      if (_not_6) {
                                        _builder.append("\t");
                                        _builder.append("_writer.writeInt(");
                                        String _name_32 = va_6.getName();
                                        _builder.append(_name_32, "\t");
                                        _builder.append("); //add ");
                                        String _name_33 = va_6.getName();
                                        _builder.append(_name_33, "\t");
                                        _builder.append(": ");
                                        String _name_34 = va_6.getName();
                                        boolean _add_6 = varlist.add(_name_34);
                                        _builder.append(_add_6, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_7 = vas.getType();
                              String _string_7 = _type_7.toString();
                              String _trim_7 = _string_7.trim();
                              boolean _equals_9 = Objects.equal(_trim_7, "clock");
                              if (_equals_9) {
                                {
                                  EList<VarName> _name_35 = vas.getName();
                                  for(final VarName va_7 : _name_35) {
                                    {
                                      String _name_36 = va_7.getName();
                                      boolean _contains_7 = varlist.contains(_name_36);
                                      boolean _not_7 = (!_contains_7);
                                      if (_not_7) {
                                        _builder.append("\t");
                                        _builder.append("_writer.writeInt(");
                                        String _name_37 = va_7.getName();
                                        _builder.append(_name_37, "\t");
                                        _builder.append("); //add ");
                                        String _name_38 = va_7.getName();
                                        _builder.append(_name_38, "\t");
                                        _builder.append(": ");
                                        String _name_39 = va_7.getName();
                                        boolean _add_7 = varlist.add(_name_39);
                                        _builder.append(_add_7, "\t");
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
                  } else {
                    _builder.append("\t");
                    VarSingleDef _varsingledef = v.getVarsingledef();
                    VarDefinition vas_1 = _varsingledef.getVardef();
                    _builder.append("\t");
                    _builder.newLineIfNotEmpty();
                    {
                      IfDef _ifdef_1 = v.getIfdef();
                      boolean _equals_10 = Objects.equal(_ifdef_1, null);
                      if (_equals_10) {
                        {
                          scheduling.dsl.String _type_8 = vas_1.getType();
                          String _string_8 = _type_8.toString();
                          String _trim_8 = _string_8.trim();
                          boolean _equals_11 = Objects.equal(_trim_8, "bool");
                          if (_equals_11) {
                            {
                              EList<VarName> _name_40 = vas_1.getName();
                              for(final VarName va_8 : _name_40) {
                                {
                                  String _name_41 = va_8.getName();
                                  boolean _contains_8 = varlist.contains(_name_41);
                                  boolean _not_8 = (!_contains_8);
                                  if (_not_8) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeBool(");
                                    String _name_42 = va_8.getName();
                                    _builder.append(_name_42, "\t");
                                    _builder.append("); //add ");
                                    String _name_43 = va_8.getName();
                                    _builder.append(_name_43, "\t");
                                    _builder.append(": ");
                                    String _name_44 = va_8.getName();
                                    boolean _add_8 = varlist.add(_name_44);
                                    _builder.append(_add_8, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_9 = vas_1.getType();
                          String _string_9 = _type_9.toString();
                          String _trim_9 = _string_9.trim();
                          boolean _equals_12 = Objects.equal(_trim_9, "time");
                          if (_equals_12) {
                            {
                              EList<VarName> _name_45 = vas_1.getName();
                              for(final VarName va_9 : _name_45) {
                                {
                                  String _name_46 = va_9.getName();
                                  boolean _contains_9 = varlist.contains(_name_46);
                                  boolean _not_9 = (!_contains_9);
                                  if (_not_9) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeInt(");
                                    String _name_47 = va_9.getName();
                                    _builder.append(_name_47, "\t");
                                    _builder.append("); //add ");
                                    String _name_48 = va_9.getName();
                                    _builder.append(_name_48, "\t");
                                    _builder.append(": ");
                                    String _name_49 = va_9.getName();
                                    boolean _add_9 = varlist.add(_name_49);
                                    _builder.append(_add_9, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_10 = vas_1.getType();
                          String _string_10 = _type_10.toString();
                          String _trim_10 = _string_10.trim();
                          boolean _equals_13 = Objects.equal(_trim_10, "int");
                          if (_equals_13) {
                            {
                              EList<VarName> _name_50 = vas_1.getName();
                              for(final VarName va_10 : _name_50) {
                                {
                                  String _name_51 = va_10.getName();
                                  boolean _contains_10 = varlist.contains(_name_51);
                                  boolean _not_10 = (!_contains_10);
                                  if (_not_10) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeInt(");
                                    String _name_52 = va_10.getName();
                                    _builder.append(_name_52, "\t");
                                    _builder.append("); //add ");
                                    String _name_53 = va_10.getName();
                                    _builder.append(_name_53, "\t");
                                    _builder.append(": ");
                                    String _name_54 = va_10.getName();
                                    boolean _add_10 = varlist.add(_name_54);
                                    _builder.append(_add_10, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_11 = vas_1.getType();
                          String _string_11 = _type_11.toString();
                          String _trim_11 = _string_11.trim();
                          boolean _equals_14 = Objects.equal(_trim_11, "clock");
                          if (_equals_14) {
                            {
                              EList<VarName> _name_55 = vas_1.getName();
                              for(final VarName va_11 : _name_55) {
                                {
                                  String _name_56 = va_11.getName();
                                  boolean _contains_11 = varlist.contains(_name_56);
                                  boolean _not_11 = (!_contains_11);
                                  if (_not_11) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeInt(");
                                    String _name_57 = va_11.getName();
                                    _builder.append(_name_57, "\t");
                                    _builder.append("); //add ");
                                    String _name_58 = va_11.getName();
                                    _builder.append(_name_58, "\t");
                                    _builder.append(": ");
                                    String _name_59 = va_11.getName();
                                    boolean _add_11 = varlist.add(_name_59);
                                    _builder.append(_add_11, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                      } else {
                        {
                          scheduling.dsl.String _type_12 = vas_1.getType();
                          String _string_12 = _type_12.toString();
                          String _trim_12 = _string_12.trim();
                          boolean _equals_15 = Objects.equal(_trim_12, "bool");
                          if (_equals_15) {
                            {
                              EList<VarName> _name_60 = vas_1.getName();
                              for(final VarName va_12 : _name_60) {
                                {
                                  String _name_61 = va_12.getName();
                                  boolean _contains_12 = varlist.contains(_name_61);
                                  boolean _not_12 = (!_contains_12);
                                  if (_not_12) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeBool(");
                                    String _name_62 = va_12.getName();
                                    _builder.append(_name_62, "\t");
                                    _builder.append("); //add ");
                                    String _name_63 = va_12.getName();
                                    _builder.append(_name_63, "\t");
                                    _builder.append(": ");
                                    String _name_64 = va_12.getName();
                                    boolean _add_12 = varlist.add(_name_64);
                                    _builder.append(_add_12, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_13 = vas_1.getType();
                          String _string_13 = _type_13.toString();
                          String _trim_13 = _string_13.trim();
                          boolean _equals_16 = Objects.equal(_trim_13, "time");
                          if (_equals_16) {
                            {
                              EList<VarName> _name_65 = vas_1.getName();
                              for(final VarName va_13 : _name_65) {
                                {
                                  String _name_66 = va_13.getName();
                                  boolean _contains_13 = varlist.contains(_name_66);
                                  boolean _not_13 = (!_contains_13);
                                  if (_not_13) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeInt(");
                                    String _name_67 = va_13.getName();
                                    _builder.append(_name_67, "\t");
                                    _builder.append("); //add ");
                                    String _name_68 = va_13.getName();
                                    _builder.append(_name_68, "\t");
                                    _builder.append(": ");
                                    String _name_69 = va_13.getName();
                                    boolean _add_13 = varlist.add(_name_69);
                                    _builder.append(_add_13, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_14 = vas_1.getType();
                          String _string_14 = _type_14.toString();
                          String _trim_14 = _string_14.trim();
                          boolean _equals_17 = Objects.equal(_trim_14, "int");
                          if (_equals_17) {
                            {
                              EList<VarName> _name_70 = vas_1.getName();
                              for(final VarName va_14 : _name_70) {
                                {
                                  String _name_71 = va_14.getName();
                                  boolean _contains_14 = varlist.contains(_name_71);
                                  boolean _not_14 = (!_contains_14);
                                  if (_not_14) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeInt(");
                                    String _name_72 = va_14.getName();
                                    _builder.append(_name_72, "\t");
                                    _builder.append("); //add ");
                                    String _name_73 = va_14.getName();
                                    _builder.append(_name_73, "\t");
                                    _builder.append(": ");
                                    String _name_74 = va_14.getName();
                                    boolean _add_14 = varlist.add(_name_74);
                                    _builder.append(_add_14, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_15 = vas_1.getType();
                          String _string_15 = _type_15.toString();
                          String _trim_15 = _string_15.trim();
                          boolean _equals_18 = Objects.equal(_trim_15, "clock");
                          if (_equals_18) {
                            {
                              EList<VarName> _name_75 = vas_1.getName();
                              for(final VarName va_15 : _name_75) {
                                {
                                  String _name_76 = va_15.getName();
                                  boolean _contains_15 = varlist.contains(_name_76);
                                  boolean _not_15 = (!_contains_15);
                                  if (_not_15) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeInt(");
                                    String _name_77 = va_15.getName();
                                    _builder.append(_name_77, "\t");
                                    _builder.append("); //add ");
                                    String _name_78 = va_15.getName();
                                    _builder.append(_name_78, "\t");
                                    _builder.append(": ");
                                    String _name_79 = va_15.getName();
                                    boolean _add_15 = varlist.add(_name_79);
                                    _builder.append(_add_15, "\t");
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
          }
        }
      }
    }
    {
      SchedulerDataDef _schedulerdata = sch.getSchedulerdata();
      boolean _notEquals_5 = (!Objects.equal(_schedulerdata, null));
      if (_notEquals_5) {
        _builder.append("\t");
        _builder.append("//scheduler variables data ");
        ArrayList<String> varlist_1 = new ArrayList<String>();
        _builder.newLineIfNotEmpty();
        {
          SchedulerDataDef _schedulerdata_1 = sch.getSchedulerdata();
          EList<DataDef> _datadef = _schedulerdata_1.getDatadef();
          for(final DataDef dat : _datadef) {
            {
              DataBlockDef _datablockdef = dat.getDatablockdef();
              boolean _notEquals_6 = (!Objects.equal(_datablockdef, null));
              if (_notEquals_6) {
                {
                  DataBlockDef _datablockdef_1 = dat.getDatablockdef();
                  EList<DataSingleDef> _datadef_1 = _datablockdef_1.getDatadef();
                  for(final DataSingleDef data : _datadef_1) {
                    {
                      SchedulerPropertyDef _prop = data.getProp();
                      boolean _notEquals_7 = (!Objects.equal(_prop, null));
                      if (_notEquals_7) {
                        _builder.append("\t");
                        SchedulerPropertyDef vas_2 = data.getProp();
                        _builder.append("\t\t\t\t\t\t\t\t\t\t\t");
                        _builder.newLineIfNotEmpty();
                        {
                          scheduling.dsl.String _type_16 = vas_2.getType();
                          String _string_16 = _type_16.toString();
                          String _trim_16 = _string_16.trim();
                          boolean _equals_19 = Objects.equal(_trim_16, "bool");
                          if (_equals_19) {
                            {
                              EList<SchedulerPropertyName> _name_80 = vas_2.getName();
                              for(final SchedulerPropertyName va_16 : _name_80) {
                                {
                                  String _name_81 = va_16.getName();
                                  boolean _contains_16 = varlist_1.contains(_name_81);
                                  boolean _not_16 = (!_contains_16);
                                  if (_not_16) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeBool(");
                                    String _name_82 = va_16.getName();
                                    _builder.append(_name_82, "\t");
                                    _builder.append("); //add ");
                                    String _name_83 = va_16.getName();
                                    _builder.append(_name_83, "\t");
                                    _builder.append(": ");
                                    String _name_84 = va_16.getName();
                                    boolean _add_16 = varlist_1.add(_name_84);
                                    _builder.append(_add_16, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_17 = vas_2.getType();
                          String _string_17 = _type_17.toString();
                          String _trim_17 = _string_17.trim();
                          boolean _equals_20 = Objects.equal(_trim_17, "time");
                          if (_equals_20) {
                            {
                              EList<SchedulerPropertyName> _name_85 = vas_2.getName();
                              for(final SchedulerPropertyName va_17 : _name_85) {
                                {
                                  String _name_86 = va_17.getName();
                                  boolean _contains_17 = varlist_1.contains(_name_86);
                                  boolean _not_17 = (!_contains_17);
                                  if (_not_17) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeInt(");
                                    String _name_87 = va_17.getName();
                                    _builder.append(_name_87, "\t");
                                    _builder.append("); //add ");
                                    String _name_88 = va_17.getName();
                                    _builder.append(_name_88, "\t");
                                    _builder.append(": ");
                                    String _name_89 = va_17.getName();
                                    boolean _add_17 = varlist_1.add(_name_89);
                                    _builder.append(_add_17, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_18 = vas_2.getType();
                          String _string_18 = _type_18.toString();
                          String _trim_18 = _string_18.trim();
                          boolean _equals_21 = Objects.equal(_trim_18, "int");
                          if (_equals_21) {
                            {
                              EList<SchedulerPropertyName> _name_90 = vas_2.getName();
                              for(final SchedulerPropertyName va_18 : _name_90) {
                                {
                                  String _name_91 = va_18.getName();
                                  boolean _contains_18 = varlist_1.contains(_name_91);
                                  boolean _not_18 = (!_contains_18);
                                  if (_not_18) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeInt(");
                                    String _name_92 = va_18.getName();
                                    _builder.append(_name_92, "\t");
                                    _builder.append("); //add ");
                                    String _name_93 = va_18.getName();
                                    _builder.append(_name_93, "\t");
                                    _builder.append(": ");
                                    String _name_94 = va_18.getName();
                                    boolean _add_18 = varlist_1.add(_name_94);
                                    _builder.append(_add_18, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_19 = vas_2.getType();
                          String _string_19 = _type_19.toString();
                          String _trim_19 = _string_19.trim();
                          boolean _equals_22 = Objects.equal(_trim_19, "clock");
                          if (_equals_22) {
                            {
                              EList<SchedulerPropertyName> _name_95 = vas_2.getName();
                              for(final SchedulerPropertyName va_19 : _name_95) {
                                {
                                  String _name_96 = va_19.getName();
                                  boolean _contains_19 = varlist_1.contains(_name_96);
                                  boolean _not_19 = (!_contains_19);
                                  if (_not_19) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeInt(");
                                    String _name_97 = va_19.getName();
                                    _builder.append(_name_97, "\t");
                                    _builder.append("); //add ");
                                    String _name_98 = va_19.getName();
                                    _builder.append(_name_98, "\t");
                                    _builder.append(": ");
                                    String _name_99 = va_19.getName();
                                    boolean _add_19 = varlist_1.add(_name_99);
                                    _builder.append(_add_19, "\t");
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
              } else {
                {
                  DataSingleDef _datasingledef = dat.getDatasingledef();
                  boolean _notEquals_8 = (!Objects.equal(_datasingledef, null));
                  if (_notEquals_8) {
                    _builder.append("\t");
                    DataSingleDef data_1 = dat.getDatasingledef();
                    _builder.newLineIfNotEmpty();
                    {
                      SchedulerPropertyDef _prop_1 = data_1.getProp();
                      boolean _notEquals_9 = (!Objects.equal(_prop_1, null));
                      if (_notEquals_9) {
                        _builder.append("\t");
                        SchedulerPropertyDef vas_3 = data_1.getProp();
                        _builder.append("\t\t\t\t\t\t\t\t\t\t\t");
                        _builder.newLineIfNotEmpty();
                        {
                          scheduling.dsl.String _type_20 = vas_3.getType();
                          String _string_20 = _type_20.toString();
                          String _trim_20 = _string_20.trim();
                          boolean _equals_23 = Objects.equal(_trim_20, "bool");
                          if (_equals_23) {
                            {
                              EList<SchedulerPropertyName> _name_100 = vas_3.getName();
                              for(final SchedulerPropertyName va_20 : _name_100) {
                                {
                                  String _name_101 = va_20.getName();
                                  boolean _contains_20 = varlist_1.contains(_name_101);
                                  boolean _not_20 = (!_contains_20);
                                  if (_not_20) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeBool(");
                                    String _name_102 = va_20.getName();
                                    _builder.append(_name_102, "\t");
                                    _builder.append("); //add ");
                                    String _name_103 = va_20.getName();
                                    _builder.append(_name_103, "\t");
                                    _builder.append(": ");
                                    String _name_104 = va_20.getName();
                                    boolean _add_20 = varlist_1.add(_name_104);
                                    _builder.append(_add_20, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_21 = vas_3.getType();
                          String _string_21 = _type_21.toString();
                          String _trim_21 = _string_21.trim();
                          boolean _equals_24 = Objects.equal(_trim_21, "time");
                          if (_equals_24) {
                            {
                              EList<SchedulerPropertyName> _name_105 = vas_3.getName();
                              for(final SchedulerPropertyName va_21 : _name_105) {
                                {
                                  String _name_106 = va_21.getName();
                                  boolean _contains_21 = varlist_1.contains(_name_106);
                                  boolean _not_21 = (!_contains_21);
                                  if (_not_21) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeInt(");
                                    String _name_107 = va_21.getName();
                                    _builder.append(_name_107, "\t");
                                    _builder.append("); //add ");
                                    String _name_108 = va_21.getName();
                                    _builder.append(_name_108, "\t");
                                    _builder.append(": ");
                                    String _name_109 = va_21.getName();
                                    boolean _add_21 = varlist_1.add(_name_109);
                                    _builder.append(_add_21, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_22 = vas_3.getType();
                          String _string_22 = _type_22.toString();
                          String _trim_22 = _string_22.trim();
                          boolean _equals_25 = Objects.equal(_trim_22, "int");
                          if (_equals_25) {
                            {
                              EList<SchedulerPropertyName> _name_110 = vas_3.getName();
                              for(final SchedulerPropertyName va_22 : _name_110) {
                                {
                                  String _name_111 = va_22.getName();
                                  boolean _contains_22 = varlist_1.contains(_name_111);
                                  boolean _not_22 = (!_contains_22);
                                  if (_not_22) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeInt(");
                                    String _name_112 = va_22.getName();
                                    _builder.append(_name_112, "\t");
                                    _builder.append("); //add ");
                                    String _name_113 = va_22.getName();
                                    _builder.append(_name_113, "\t");
                                    _builder.append(": ");
                                    String _name_114 = va_22.getName();
                                    boolean _add_22 = varlist_1.add(_name_114);
                                    _builder.append(_add_22, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_23 = vas_3.getType();
                          String _string_23 = _type_23.toString();
                          String _trim_23 = _string_23.trim();
                          boolean _equals_26 = Objects.equal(_trim_23, "clock");
                          if (_equals_26) {
                            {
                              EList<SchedulerPropertyName> _name_115 = vas_3.getName();
                              for(final SchedulerPropertyName va_23 : _name_115) {
                                {
                                  String _name_116 = va_23.getName();
                                  boolean _contains_23 = varlist_1.contains(_name_116);
                                  boolean _not_23 = (!_contains_23);
                                  if (_not_23) {
                                    _builder.append("\t");
                                    _builder.append("_writer.writeInt(");
                                    String _name_117 = va_23.getName();
                                    _builder.append(_name_117, "\t");
                                    _builder.append("); //add ");
                                    String _name_118 = va_23.getName();
                                    _builder.append(_name_118, "\t");
                                    _builder.append(": ");
                                    String _name_119 = va_23.getName();
                                    boolean _add_23 = varlist_1.add(_name_119);
                                    _builder.append(_add_23, "\t");
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
          }
        }
      }
    }
    {
      Set<String> _keySet = Data.periodicClockProperties.keySet();
      for(final String pC : _keySet) {
        _builder.append("\t");
        _builder.append("_writer.writeInt(");
        _builder.append(pC, "\t");
        _builder.append(") ; // periodic process clock");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("_writer.writeInt(");
        _builder.append(pC, "\t");
        _builder.append("_offset) ; // periodic process clock");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("//_runningSet.encode(_writer); //why disable? encode and store in different space\t(runningSet is stored in stack)\t\t\t");
    _builder.newLine();
    {
      for(final String col : Data.collectionList) {
        _builder.append("\t");
        _builder.append(col, "\t");
        _builder.append(".encode(_writer) ;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("}\t");
    _builder.newLine();
    _builder.append("public boolean decode(DataReader _reader) {\t\t\t\t");
    _builder.newLine();
    {
      String _parent_1 = sch.getParent();
      boolean _equals_27 = Objects.equal(_parent_1, null);
      if (_equals_27) {
        {
          DefCore _defcore_4 = Data.schModel.getDefcore();
          boolean _notEquals_10 = (!Objects.equal(_defcore_4, null));
          if (_notEquals_10) {
            _builder.append("\t");
            _builder.append("//current_core = _reader.readInt();");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("for (int i=0; i<");
            DefCore _defcore_5 = Data.schModel.getDefcore();
            int _numcore_2 = _defcore_5.getNumcore();
            _builder.append(_numcore_2, "\t");
            _builder.append(";i++){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (_reader.readBool()) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("if (running_procs[i] == null) ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("running_procs[i] = new SchedulerProcess() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("running_procs[i].decode(_reader) ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("processInScheduler[running_procs[i].processID] = true ;\t\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("} else {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("running_procs[i] = null ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("byte num = (byte) _reader.readByte() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_putColIndex[i].clear() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("for (byte id=0; id < num; id++)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("_putColIndex[i].add((byte) _reader.readByte()) ; //for replacing running process");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("if (current_core >= 0)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_process = running_procs[current_core];");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("else");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_process = null;");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("//_schselopt = _reader.readInt();");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("clearProcessInScheduler() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("if (_reader.readBool()) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (running_process == null)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("running_process = new SchedulerProcess() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_process.decode(_reader) ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("processInScheduler[running_process.processID] = true ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("} else {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("running_process = null ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("_putColIndex = (byte) _reader.readByte() ; //for replacing running process");
            _builder.newLine();
          }
        }
      } else {
        _builder.append("\t");
        _builder.append("super.decode(_reader) ;");
        _builder.newLine();
      }
    }
    {
      if (Data.runTime) {
        {
          DefCore _defcore_6 = Data.schModel.getDefcore();
          boolean _notEquals_11 = (!Objects.equal(_defcore_6, null));
          if (_notEquals_11) {
            _builder.append("\t");
            _builder.append("for (int i=0; i<");
            DefCore _defcore_7 = Data.schModel.getDefcore();
            int _numcore_3 = _defcore_7.getNumcore();
            _builder.append(_numcore_3, "\t");
            _builder.append(";i++){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_time_count[i] = _reader.readInt();");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_time_slice[i] = _reader.readInt();");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("//could be duplicated!");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("_time_count = _reader.readInt();");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("_time_slice = _reader.readInt();");
            _builder.newLine();
          }
        }
      }
    }
    {
      VarDef _svar_3 = sch.getSvar();
      boolean _notEquals_12 = (!Objects.equal(_svar_3, null));
      if (_notEquals_12) {
        {
          VarDef _svar_4 = sch.getSvar();
          EList<VarDecl> _vard_2 = _svar_4.getVard();
          boolean _notEquals_13 = (!Objects.equal(_vard_2, null));
          if (_notEquals_13) {
            _builder.append("\t");
            _builder.append("//scheduler variables ");
            ArrayList<String> varlist_2 = new ArrayList<String>();
            _builder.newLineIfNotEmpty();
            {
              VarDef _svar_5 = sch.getSvar();
              EList<VarDecl> _vard_3 = _svar_5.getVard();
              for(final VarDecl vs : _vard_3) {
                {
                  IfDef _ifdef_2 = vs.getIfdef();
                  boolean _equals_28 = Objects.equal(_ifdef_2, null);
                  if (_equals_28) {
                    {
                      VarBlockDef _varblockdef_2 = vs.getVarblockdef();
                      boolean _notEquals_14 = (!Objects.equal(_varblockdef_2, null));
                      if (_notEquals_14) {
                        {
                          VarBlockDef _varblockdef_3 = vs.getVarblockdef();
                          EList<VarDefinition> _vardef_1 = _varblockdef_3.getVardef();
                          for(final VarDefinition vas_4 : _vardef_1) {
                            {
                              scheduling.dsl.String _type_24 = vas_4.getType();
                              String _string_24 = _type_24.toString();
                              String _trim_24 = _string_24.trim();
                              boolean _equals_29 = Objects.equal(_trim_24, "bool");
                              if (_equals_29) {
                                {
                                  EList<VarName> _name_120 = vas_4.getName();
                                  for(final VarName va_24 : _name_120) {
                                    {
                                      String _name_121 = va_24.getName();
                                      boolean _contains_24 = varlist_2.contains(_name_121);
                                      boolean _not_24 = (!_contains_24);
                                      if (_not_24) {
                                        _builder.append("\t");
                                        String _name_122 = va_24.getName();
                                        _builder.append(_name_122, "\t");
                                        _builder.append(" = _reader.readBool(); //add ");
                                        String _name_123 = va_24.getName();
                                        _builder.append(_name_123, "\t");
                                        _builder.append(": ");
                                        String _name_124 = va_24.getName();
                                        boolean _add_24 = varlist_2.add(_name_124);
                                        _builder.append(_add_24, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_25 = vas_4.getType();
                              String _string_25 = _type_25.toString();
                              String _trim_25 = _string_25.trim();
                              boolean _equals_30 = Objects.equal(_trim_25, "time");
                              if (_equals_30) {
                                {
                                  EList<VarName> _name_125 = vas_4.getName();
                                  for(final VarName va_25 : _name_125) {
                                    {
                                      String _name_126 = va_25.getName();
                                      boolean _contains_25 = varlist_2.contains(_name_126);
                                      boolean _not_25 = (!_contains_25);
                                      if (_not_25) {
                                        _builder.append("\t");
                                        String _name_127 = va_25.getName();
                                        _builder.append(_name_127, "\t");
                                        _builder.append(" = _reader.readInt(); //add ");
                                        String _name_128 = va_25.getName();
                                        _builder.append(_name_128, "\t");
                                        _builder.append(": ");
                                        String _name_129 = va_25.getName();
                                        boolean _add_25 = varlist_2.add(_name_129);
                                        _builder.append(_add_25, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_26 = vas_4.getType();
                              String _string_26 = _type_26.toString();
                              String _trim_26 = _string_26.trim();
                              boolean _equals_31 = Objects.equal(_trim_26, "int");
                              if (_equals_31) {
                                {
                                  EList<VarName> _name_130 = vas_4.getName();
                                  for(final VarName va_26 : _name_130) {
                                    {
                                      String _name_131 = va_26.getName();
                                      boolean _contains_26 = varlist_2.contains(_name_131);
                                      boolean _not_26 = (!_contains_26);
                                      if (_not_26) {
                                        _builder.append("\t");
                                        String _name_132 = va_26.getName();
                                        _builder.append(_name_132, "\t");
                                        _builder.append(" = _reader.readInt(); //add ");
                                        String _name_133 = va_26.getName();
                                        _builder.append(_name_133, "\t");
                                        _builder.append(": ");
                                        String _name_134 = va_26.getName();
                                        boolean _add_26 = varlist_2.add(_name_134);
                                        _builder.append(_add_26, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_27 = vas_4.getType();
                              String _string_27 = _type_27.toString();
                              String _trim_27 = _string_27.trim();
                              boolean _equals_32 = Objects.equal(_trim_27, "clock");
                              if (_equals_32) {
                                {
                                  EList<VarName> _name_135 = vas_4.getName();
                                  for(final VarName va_27 : _name_135) {
                                    {
                                      String _name_136 = va_27.getName();
                                      boolean _contains_27 = varlist_2.contains(_name_136);
                                      boolean _not_27 = (!_contains_27);
                                      if (_not_27) {
                                        _builder.append("\t");
                                        String _name_137 = va_27.getName();
                                        _builder.append(_name_137, "\t");
                                        _builder.append(" = _reader.readInt(); //add ");
                                        String _name_138 = va_27.getName();
                                        _builder.append(_name_138, "\t");
                                        _builder.append(": ");
                                        String _name_139 = va_27.getName();
                                        boolean _add_27 = varlist_2.add(_name_139);
                                        _builder.append(_add_27, "\t");
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
                        _builder.append("\t");
                        VarSingleDef _varsingledef_1 = vs.getVarsingledef();
                        VarDefinition vas_5 = _varsingledef_1.getVardef();
                        _builder.newLineIfNotEmpty();
                        {
                          scheduling.dsl.String _type_28 = vas_5.getType();
                          String _string_28 = _type_28.toString();
                          String _trim_28 = _string_28.trim();
                          boolean _equals_33 = Objects.equal(_trim_28, "bool");
                          if (_equals_33) {
                            {
                              EList<VarName> _name_140 = vas_5.getName();
                              for(final VarName va_28 : _name_140) {
                                {
                                  String _name_141 = va_28.getName();
                                  boolean _contains_28 = varlist_2.contains(_name_141);
                                  boolean _not_28 = (!_contains_28);
                                  if (_not_28) {
                                    _builder.append("\t");
                                    String _name_142 = va_28.getName();
                                    _builder.append(_name_142, "\t");
                                    _builder.append(" = _reader.readBool(); //add ");
                                    String _name_143 = va_28.getName();
                                    _builder.append(_name_143, "\t");
                                    _builder.append(": ");
                                    String _name_144 = va_28.getName();
                                    boolean _add_28 = varlist_2.add(_name_144);
                                    _builder.append(_add_28, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_29 = vas_5.getType();
                          String _string_29 = _type_29.toString();
                          String _trim_29 = _string_29.trim();
                          boolean _equals_34 = Objects.equal(_trim_29, "time");
                          if (_equals_34) {
                            {
                              EList<VarName> _name_145 = vas_5.getName();
                              for(final VarName va_29 : _name_145) {
                                {
                                  String _name_146 = va_29.getName();
                                  boolean _contains_29 = varlist_2.contains(_name_146);
                                  boolean _not_29 = (!_contains_29);
                                  if (_not_29) {
                                    _builder.append("\t");
                                    String _name_147 = va_29.getName();
                                    _builder.append(_name_147, "\t");
                                    _builder.append(" = _reader.readInt(); //add ");
                                    String _name_148 = va_29.getName();
                                    _builder.append(_name_148, "\t");
                                    _builder.append(": ");
                                    String _name_149 = va_29.getName();
                                    boolean _add_29 = varlist_2.add(_name_149);
                                    _builder.append(_add_29, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_30 = vas_5.getType();
                          String _string_30 = _type_30.toString();
                          String _trim_30 = _string_30.trim();
                          boolean _equals_35 = Objects.equal(_trim_30, "int");
                          if (_equals_35) {
                            {
                              EList<VarName> _name_150 = vas_5.getName();
                              for(final VarName va_30 : _name_150) {
                                {
                                  String _name_151 = va_30.getName();
                                  boolean _contains_30 = varlist_2.contains(_name_151);
                                  boolean _not_30 = (!_contains_30);
                                  if (_not_30) {
                                    _builder.append("\t");
                                    String _name_152 = va_30.getName();
                                    _builder.append(_name_152, "\t");
                                    _builder.append(" = _reader.readInt(); //add ");
                                    String _name_153 = va_30.getName();
                                    _builder.append(_name_153, "\t");
                                    _builder.append(": ");
                                    String _name_154 = va_30.getName();
                                    boolean _add_30 = varlist_2.add(_name_154);
                                    _builder.append(_add_30, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_31 = vas_5.getType();
                          String _string_31 = _type_31.toString();
                          String _trim_31 = _string_31.trim();
                          boolean _equals_36 = Objects.equal(_trim_31, "clock");
                          if (_equals_36) {
                            {
                              EList<VarName> _name_155 = vas_5.getName();
                              for(final VarName va_31 : _name_155) {
                                {
                                  String _name_156 = va_31.getName();
                                  boolean _contains_31 = varlist_2.contains(_name_156);
                                  boolean _not_31 = (!_contains_31);
                                  if (_not_31) {
                                    _builder.append("\t");
                                    String _name_157 = va_31.getName();
                                    _builder.append(_name_157, "\t");
                                    _builder.append(" = _reader.readInt(); //add ");
                                    String _name_158 = va_31.getName();
                                    _builder.append(_name_158, "\t");
                                    _builder.append(": ");
                                    String _name_159 = va_31.getName();
                                    boolean _add_31 = varlist_2.add(_name_159);
                                    _builder.append(_add_31, "\t");
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
                      VarBlockDef _varblockdef_4 = vs.getVarblockdef();
                      boolean _notEquals_15 = (!Objects.equal(_varblockdef_4, null));
                      if (_notEquals_15) {
                        {
                          VarBlockDef _varblockdef_5 = vs.getVarblockdef();
                          EList<VarDefinition> _vardef_2 = _varblockdef_5.getVardef();
                          for(final VarDefinition vas_6 : _vardef_2) {
                            {
                              scheduling.dsl.String _type_32 = vas_6.getType();
                              String _string_32 = _type_32.toString();
                              String _trim_32 = _string_32.trim();
                              boolean _equals_37 = Objects.equal(_trim_32, "bool");
                              if (_equals_37) {
                                {
                                  EList<VarName> _name_160 = vas_6.getName();
                                  for(final VarName va_32 : _name_160) {
                                    {
                                      String _name_161 = va_32.getName();
                                      boolean _contains_32 = varlist_2.contains(_name_161);
                                      boolean _not_32 = (!_contains_32);
                                      if (_not_32) {
                                        _builder.append("\t");
                                        String _name_162 = va_32.getName();
                                        _builder.append(_name_162, "\t");
                                        _builder.append(" = _reader.readBool(); //add ");
                                        String _name_163 = va_32.getName();
                                        _builder.append(_name_163, "\t");
                                        _builder.append(": ");
                                        String _name_164 = va_32.getName();
                                        boolean _add_32 = varlist_2.add(_name_164);
                                        _builder.append(_add_32, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_33 = vas_6.getType();
                              String _string_33 = _type_33.toString();
                              String _trim_33 = _string_33.trim();
                              boolean _equals_38 = Objects.equal(_trim_33, "time");
                              if (_equals_38) {
                                {
                                  EList<VarName> _name_165 = vas_6.getName();
                                  for(final VarName va_33 : _name_165) {
                                    {
                                      String _name_166 = va_33.getName();
                                      boolean _contains_33 = varlist_2.contains(_name_166);
                                      boolean _not_33 = (!_contains_33);
                                      if (_not_33) {
                                        _builder.append("\t");
                                        String _name_167 = va_33.getName();
                                        _builder.append(_name_167, "\t");
                                        _builder.append(" = _reader.readInt(); //add ");
                                        String _name_168 = va_33.getName();
                                        _builder.append(_name_168, "\t");
                                        _builder.append(": ");
                                        String _name_169 = va_33.getName();
                                        boolean _add_33 = varlist_2.add(_name_169);
                                        _builder.append(_add_33, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_34 = vas_6.getType();
                              String _string_34 = _type_34.toString();
                              String _trim_34 = _string_34.trim();
                              boolean _equals_39 = Objects.equal(_trim_34, "int");
                              if (_equals_39) {
                                {
                                  EList<VarName> _name_170 = vas_6.getName();
                                  for(final VarName va_34 : _name_170) {
                                    {
                                      String _name_171 = va_34.getName();
                                      boolean _contains_34 = varlist_2.contains(_name_171);
                                      boolean _not_34 = (!_contains_34);
                                      if (_not_34) {
                                        _builder.append("\t");
                                        String _name_172 = va_34.getName();
                                        _builder.append(_name_172, "\t");
                                        _builder.append(" = _reader.readInt(); //add ");
                                        String _name_173 = va_34.getName();
                                        _builder.append(_name_173, "\t");
                                        _builder.append(": ");
                                        String _name_174 = va_34.getName();
                                        boolean _add_34 = varlist_2.add(_name_174);
                                        _builder.append(_add_34, "\t");
                                        _builder.newLineIfNotEmpty();
                                      }
                                    }
                                  }
                                }
                              }
                            }
                            {
                              scheduling.dsl.String _type_35 = vas_6.getType();
                              String _string_35 = _type_35.toString();
                              String _trim_35 = _string_35.trim();
                              boolean _equals_40 = Objects.equal(_trim_35, "clock");
                              if (_equals_40) {
                                {
                                  EList<VarName> _name_175 = vas_6.getName();
                                  for(final VarName va_35 : _name_175) {
                                    {
                                      String _name_176 = va_35.getName();
                                      boolean _contains_35 = varlist_2.contains(_name_176);
                                      boolean _not_35 = (!_contains_35);
                                      if (_not_35) {
                                        _builder.append("\t");
                                        String _name_177 = va_35.getName();
                                        _builder.append(_name_177, "\t");
                                        _builder.append(" = _reader.readInt(); //add ");
                                        String _name_178 = va_35.getName();
                                        _builder.append(_name_178, "\t");
                                        _builder.append(": ");
                                        String _name_179 = va_35.getName();
                                        boolean _add_35 = varlist_2.add(_name_179);
                                        _builder.append(_add_35, "\t");
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
                        _builder.append("\t");
                        VarSingleDef _varsingledef_2 = vs.getVarsingledef();
                        VarDefinition vas_7 = _varsingledef_2.getVardef();
                        _builder.newLineIfNotEmpty();
                        {
                          scheduling.dsl.String _type_36 = vas_7.getType();
                          String _string_36 = _type_36.toString();
                          String _trim_36 = _string_36.trim();
                          boolean _equals_41 = Objects.equal(_trim_36, "bool");
                          if (_equals_41) {
                            {
                              EList<VarName> _name_180 = vas_7.getName();
                              for(final VarName va_36 : _name_180) {
                                {
                                  String _name_181 = va_36.getName();
                                  boolean _contains_36 = varlist_2.contains(_name_181);
                                  boolean _not_36 = (!_contains_36);
                                  if (_not_36) {
                                    _builder.append("\t");
                                    String _name_182 = va_36.getName();
                                    _builder.append(_name_182, "\t");
                                    _builder.append(" = _reader.readBool(); //add ");
                                    String _name_183 = va_36.getName();
                                    _builder.append(_name_183, "\t");
                                    _builder.append(": ");
                                    String _name_184 = va_36.getName();
                                    boolean _add_36 = varlist_2.add(_name_184);
                                    _builder.append(_add_36, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_37 = vas_7.getType();
                          String _string_37 = _type_37.toString();
                          String _trim_37 = _string_37.trim();
                          boolean _equals_42 = Objects.equal(_trim_37, "time");
                          if (_equals_42) {
                            {
                              EList<VarName> _name_185 = vas_7.getName();
                              for(final VarName va_37 : _name_185) {
                                {
                                  String _name_186 = va_37.getName();
                                  boolean _contains_37 = varlist_2.contains(_name_186);
                                  boolean _not_37 = (!_contains_37);
                                  if (_not_37) {
                                    _builder.append("\t");
                                    String _name_187 = va_37.getName();
                                    _builder.append(_name_187, "\t");
                                    _builder.append(" = _reader.readInt(); //add ");
                                    String _name_188 = va_37.getName();
                                    _builder.append(_name_188, "\t");
                                    _builder.append(": ");
                                    String _name_189 = va_37.getName();
                                    boolean _add_37 = varlist_2.add(_name_189);
                                    _builder.append(_add_37, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_38 = vas_7.getType();
                          String _string_38 = _type_38.toString();
                          String _trim_38 = _string_38.trim();
                          boolean _equals_43 = Objects.equal(_trim_38, "int");
                          if (_equals_43) {
                            {
                              EList<VarName> _name_190 = vas_7.getName();
                              for(final VarName va_38 : _name_190) {
                                {
                                  String _name_191 = va_38.getName();
                                  boolean _contains_38 = varlist_2.contains(_name_191);
                                  boolean _not_38 = (!_contains_38);
                                  if (_not_38) {
                                    _builder.append("\t");
                                    String _name_192 = va_38.getName();
                                    _builder.append(_name_192, "\t");
                                    _builder.append(" = _reader.readInt(); //add ");
                                    String _name_193 = va_38.getName();
                                    _builder.append(_name_193, "\t");
                                    _builder.append(": ");
                                    String _name_194 = va_38.getName();
                                    boolean _add_38 = varlist_2.add(_name_194);
                                    _builder.append(_add_38, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_39 = vas_7.getType();
                          String _string_39 = _type_39.toString();
                          String _trim_39 = _string_39.trim();
                          boolean _equals_44 = Objects.equal(_trim_39, "clock");
                          if (_equals_44) {
                            {
                              EList<VarName> _name_195 = vas_7.getName();
                              for(final VarName va_39 : _name_195) {
                                {
                                  String _name_196 = va_39.getName();
                                  boolean _contains_39 = varlist_2.contains(_name_196);
                                  boolean _not_39 = (!_contains_39);
                                  if (_not_39) {
                                    _builder.append("\t");
                                    String _name_197 = va_39.getName();
                                    _builder.append(_name_197, "\t");
                                    _builder.append(" = _reader.readInt(); //add ");
                                    String _name_198 = va_39.getName();
                                    _builder.append(_name_198, "\t");
                                    _builder.append(": ");
                                    String _name_199 = va_39.getName();
                                    boolean _add_39 = varlist_2.add(_name_199);
                                    _builder.append(_add_39, "\t");
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
          }
        }
      }
    }
    {
      SchedulerDataDef _schedulerdata_2 = sch.getSchedulerdata();
      boolean _notEquals_16 = (!Objects.equal(_schedulerdata_2, null));
      if (_notEquals_16) {
        _builder.append("\t");
        _builder.append("//scheduler variables data  ");
        ArrayList<String> varlist_3 = new ArrayList<String>();
        _builder.newLineIfNotEmpty();
        {
          SchedulerDataDef _schedulerdata_3 = sch.getSchedulerdata();
          EList<DataDef> _datadef_2 = _schedulerdata_3.getDatadef();
          for(final DataDef dat_1 : _datadef_2) {
            {
              DataBlockDef _datablockdef_2 = dat_1.getDatablockdef();
              boolean _notEquals_17 = (!Objects.equal(_datablockdef_2, null));
              if (_notEquals_17) {
                {
                  DataBlockDef _datablockdef_3 = dat_1.getDatablockdef();
                  EList<DataSingleDef> _datadef_3 = _datablockdef_3.getDatadef();
                  for(final DataSingleDef data_2 : _datadef_3) {
                    {
                      SchedulerPropertyDef _prop_2 = data_2.getProp();
                      boolean _notEquals_18 = (!Objects.equal(_prop_2, null));
                      if (_notEquals_18) {
                        _builder.append("\t");
                        SchedulerPropertyDef vas_8 = data_2.getProp();
                        _builder.append("\t\t\t\t\t\t\t\t\t\t\t");
                        _builder.newLineIfNotEmpty();
                        {
                          scheduling.dsl.String _type_40 = vas_8.getType();
                          String _string_40 = _type_40.toString();
                          String _trim_40 = _string_40.trim();
                          boolean _equals_45 = Objects.equal(_trim_40, "bool");
                          if (_equals_45) {
                            {
                              EList<SchedulerPropertyName> _name_200 = vas_8.getName();
                              for(final SchedulerPropertyName va_40 : _name_200) {
                                {
                                  String _name_201 = va_40.getName();
                                  boolean _contains_40 = varlist_3.contains(_name_201);
                                  boolean _not_40 = (!_contains_40);
                                  if (_not_40) {
                                    _builder.append("\t");
                                    String _name_202 = va_40.getName();
                                    _builder.append(_name_202, "\t");
                                    _builder.append(" = _reader.readBool(); //add ");
                                    String _name_203 = va_40.getName();
                                    _builder.append(_name_203, "\t");
                                    _builder.append(": ");
                                    String _name_204 = va_40.getName();
                                    boolean _add_40 = varlist_3.add(_name_204);
                                    _builder.append(_add_40, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_41 = vas_8.getType();
                          String _string_41 = _type_41.toString();
                          String _trim_41 = _string_41.trim();
                          boolean _equals_46 = Objects.equal(_trim_41, "time");
                          if (_equals_46) {
                            {
                              EList<SchedulerPropertyName> _name_205 = vas_8.getName();
                              for(final SchedulerPropertyName va_41 : _name_205) {
                                {
                                  String _name_206 = va_41.getName();
                                  boolean _contains_41 = varlist_3.contains(_name_206);
                                  boolean _not_41 = (!_contains_41);
                                  if (_not_41) {
                                    _builder.append("\t");
                                    String _name_207 = va_41.getName();
                                    _builder.append(_name_207, "\t");
                                    _builder.append(" = _reader.readInt(); //add ");
                                    String _name_208 = va_41.getName();
                                    _builder.append(_name_208, "\t");
                                    _builder.append(": ");
                                    String _name_209 = va_41.getName();
                                    boolean _add_41 = varlist_3.add(_name_209);
                                    _builder.append(_add_41, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_42 = vas_8.getType();
                          String _string_42 = _type_42.toString();
                          String _trim_42 = _string_42.trim();
                          boolean _equals_47 = Objects.equal(_trim_42, "int");
                          if (_equals_47) {
                            {
                              EList<SchedulerPropertyName> _name_210 = vas_8.getName();
                              for(final SchedulerPropertyName va_42 : _name_210) {
                                {
                                  String _name_211 = va_42.getName();
                                  boolean _contains_42 = varlist_3.contains(_name_211);
                                  boolean _not_42 = (!_contains_42);
                                  if (_not_42) {
                                    _builder.append("\t");
                                    String _name_212 = va_42.getName();
                                    _builder.append(_name_212, "\t");
                                    _builder.append(" = _reader.readInt(); //add ");
                                    String _name_213 = va_42.getName();
                                    _builder.append(_name_213, "\t");
                                    _builder.append(": ");
                                    String _name_214 = va_42.getName();
                                    boolean _add_42 = varlist_3.add(_name_214);
                                    _builder.append(_add_42, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_43 = vas_8.getType();
                          String _string_43 = _type_43.toString();
                          String _trim_43 = _string_43.trim();
                          boolean _equals_48 = Objects.equal(_trim_43, "clock");
                          if (_equals_48) {
                            {
                              EList<SchedulerPropertyName> _name_215 = vas_8.getName();
                              for(final SchedulerPropertyName va_43 : _name_215) {
                                {
                                  String _name_216 = va_43.getName();
                                  boolean _contains_43 = varlist_3.contains(_name_216);
                                  boolean _not_43 = (!_contains_43);
                                  if (_not_43) {
                                    _builder.append("\t");
                                    String _name_217 = va_43.getName();
                                    _builder.append(_name_217, "\t");
                                    _builder.append(" = _reader.readInt(); //add ");
                                    String _name_218 = va_43.getName();
                                    _builder.append(_name_218, "\t");
                                    _builder.append(": ");
                                    String _name_219 = va_43.getName();
                                    boolean _add_43 = varlist_3.add(_name_219);
                                    _builder.append(_add_43, "\t");
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
              } else {
                {
                  DataSingleDef _datasingledef_1 = dat_1.getDatasingledef();
                  boolean _notEquals_19 = (!Objects.equal(_datasingledef_1, null));
                  if (_notEquals_19) {
                    _builder.append("\t");
                    DataSingleDef data_3 = dat_1.getDatasingledef();
                    _builder.newLineIfNotEmpty();
                    {
                      SchedulerPropertyDef _prop_3 = data_3.getProp();
                      boolean _notEquals_20 = (!Objects.equal(_prop_3, null));
                      if (_notEquals_20) {
                        _builder.append("\t");
                        SchedulerPropertyDef vas_9 = data_3.getProp();
                        _builder.append("\t\t\t\t\t\t\t\t\t\t\t");
                        _builder.newLineIfNotEmpty();
                        {
                          scheduling.dsl.String _type_44 = vas_9.getType();
                          String _string_44 = _type_44.toString();
                          String _trim_44 = _string_44.trim();
                          boolean _equals_49 = Objects.equal(_trim_44, "bool");
                          if (_equals_49) {
                            {
                              EList<SchedulerPropertyName> _name_220 = vas_9.getName();
                              for(final SchedulerPropertyName va_44 : _name_220) {
                                {
                                  String _name_221 = va_44.getName();
                                  boolean _contains_44 = varlist_3.contains(_name_221);
                                  boolean _not_44 = (!_contains_44);
                                  if (_not_44) {
                                    _builder.append("\t");
                                    String _name_222 = va_44.getName();
                                    _builder.append(_name_222, "\t");
                                    _builder.append(" = _reader.readBool(); //add ");
                                    String _name_223 = va_44.getName();
                                    _builder.append(_name_223, "\t");
                                    _builder.append(": ");
                                    String _name_224 = va_44.getName();
                                    boolean _add_44 = varlist_3.add(_name_224);
                                    _builder.append(_add_44, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_45 = vas_9.getType();
                          String _string_45 = _type_45.toString();
                          String _trim_45 = _string_45.trim();
                          boolean _equals_50 = Objects.equal(_trim_45, "time");
                          if (_equals_50) {
                            {
                              EList<SchedulerPropertyName> _name_225 = vas_9.getName();
                              for(final SchedulerPropertyName va_45 : _name_225) {
                                {
                                  String _name_226 = va_45.getName();
                                  boolean _contains_45 = varlist_3.contains(_name_226);
                                  boolean _not_45 = (!_contains_45);
                                  if (_not_45) {
                                    _builder.append("\t");
                                    String _name_227 = va_45.getName();
                                    _builder.append(_name_227, "\t");
                                    _builder.append(" = _reader.readInt(); //add ");
                                    String _name_228 = va_45.getName();
                                    _builder.append(_name_228, "\t");
                                    _builder.append(": ");
                                    String _name_229 = va_45.getName();
                                    boolean _add_45 = varlist_3.add(_name_229);
                                    _builder.append(_add_45, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_46 = vas_9.getType();
                          String _string_46 = _type_46.toString();
                          String _trim_46 = _string_46.trim();
                          boolean _equals_51 = Objects.equal(_trim_46, "int");
                          if (_equals_51) {
                            {
                              EList<SchedulerPropertyName> _name_230 = vas_9.getName();
                              for(final SchedulerPropertyName va_46 : _name_230) {
                                {
                                  String _name_231 = va_46.getName();
                                  boolean _contains_46 = varlist_3.contains(_name_231);
                                  boolean _not_46 = (!_contains_46);
                                  if (_not_46) {
                                    _builder.append("\t");
                                    String _name_232 = va_46.getName();
                                    _builder.append(_name_232, "\t");
                                    _builder.append(" = _reader.readInt(); //add ");
                                    String _name_233 = va_46.getName();
                                    _builder.append(_name_233, "\t");
                                    _builder.append(": ");
                                    String _name_234 = va_46.getName();
                                    boolean _add_46 = varlist_3.add(_name_234);
                                    _builder.append(_add_46, "\t");
                                    _builder.newLineIfNotEmpty();
                                  }
                                }
                              }
                            }
                          }
                        }
                        {
                          scheduling.dsl.String _type_47 = vas_9.getType();
                          String _string_47 = _type_47.toString();
                          String _trim_47 = _string_47.trim();
                          boolean _equals_52 = Objects.equal(_trim_47, "clock");
                          if (_equals_52) {
                            {
                              EList<SchedulerPropertyName> _name_235 = vas_9.getName();
                              for(final SchedulerPropertyName va_47 : _name_235) {
                                {
                                  String _name_236 = va_47.getName();
                                  boolean _contains_47 = varlist_3.contains(_name_236);
                                  boolean _not_47 = (!_contains_47);
                                  if (_not_47) {
                                    _builder.append("\t");
                                    String _name_237 = va_47.getName();
                                    _builder.append(_name_237, "\t");
                                    _builder.append(" = _reader.readInt(); //add ");
                                    String _name_238 = va_47.getName();
                                    _builder.append(_name_238, "\t");
                                    _builder.append(": ");
                                    String _name_239 = va_47.getName();
                                    boolean _add_47 = varlist_3.add(_name_239);
                                    _builder.append(_add_47, "\t");
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
          }
        }
      }
    }
    {
      Set<String> _keySet_1 = Data.periodicClockProperties.keySet();
      for(final String pC_1 : _keySet_1) {
        _builder.append("\t");
        _builder.append(pC_1, "\t");
        _builder.append(" = _reader.readInt() ; // periodic process clock");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append(pC_1, "\t");
        _builder.append("_offset = _reader.readInt() ; // periodic process clock offset");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("//_runningSet = new ProcessCollection() ;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("//_runningSet.decode(_reader); //why disable? encode and store in different space (runningSet is stored in stack)\t\t\t");
    _builder.newLine();
    {
      for(final String col_1 : Data.collectionList) {
        _builder.append("\t");
        _builder.append(col_1, "\t");
        _builder.append(".decode(_reader) ;");
        _builder.newLineIfNotEmpty();
      }
    }
    _builder.append("\t");
    _builder.append("return true;");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    {
      String _parent_2 = sch.getParent();
      boolean _equals_53 = Objects.equal(_parent_2, null);
      if (_equals_53) {
        _builder.append("public int getRunningSetSize(){");
        _builder.newLine();
        {
          DefCore _defcore_8 = Data.schModel.getDefcore();
          boolean _notEquals_21 = (!Objects.equal(_defcore_8, null));
          if (_notEquals_21) {
            _builder.append("\t");
            _builder.append("if (current_core >= 0) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("int size = ");
            DefCore _defcore_9 = Data.schModel.getDefcore();
            int _numcore_4 = _defcore_9.getNumcore();
            _builder.append(_numcore_4, "\t\t");
            _builder.append(" ;");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("_runningSets[current_core] = _runningSet;\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("for (int i=0; i<");
            DefCore _defcore_10 = Data.schModel.getDefcore();
            int _numcore_5 = _defcore_10.getNumcore();
            _builder.append(_numcore_5, "\t\t");
            _builder.append(";i++){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("if (_runningSets[i] != null)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("size += _runningSets[i].getSize();");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("else");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("size ++ ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("return size ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("} else {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("return 0 ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}\t\t\t\t");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("return _runningSet.getSize();");
            _builder.newLine();
          }
        }
        _builder.append("}\t\t\t");
        _builder.newLine();
        _builder.append("public void encodeRunningSet(DataWriter _writer){");
        _builder.newLine();
        {
          DefCore _defcore_11 = Data.schModel.getDefcore();
          boolean _notEquals_22 = (!Objects.equal(_defcore_11, null));
          if (_notEquals_22) {
            _builder.append("\t");
            _builder.append("if (current_core >= 0) {\t\t\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("for (int i=0; i<");
            DefCore _defcore_12 = Data.schModel.getDefcore();
            int _numcore_6 = _defcore_12.getNumcore();
            _builder.append(_numcore_6, "\t\t");
            _builder.append(";i++){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("if (_runningSets[i] != null) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("_writer.writeBool(true);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("_runningSets[i].encode(_writer);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("} else {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("_writer.writeBool(false);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("}\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("}\t\t\t\t");
            _builder.newLine();
          } else {
            _builder.append("\t");
            _builder.append("_runningSet.encode(_writer);");
            _builder.newLine();
          }
        }
        _builder.append("}");
        _builder.newLine();
        _builder.append("public boolean decodeRunningSet(DataReader _reader) {");
        _builder.newLine();
        {
          DefCore _defcore_13 = Data.schModel.getDefcore();
          boolean _notEquals_23 = (!Objects.equal(_defcore_13, null));
          if (_notEquals_23) {
            _builder.append("\t");
            _builder.append("if (current_core >= 0) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("if (_reader.getSize() > 0) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("boolean hasProcess ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("for (int i=0; i<");
            DefCore _defcore_14 = Data.schModel.getDefcore();
            int _numcore_7 = _defcore_14.getNumcore();
            _builder.append(_numcore_7, "\t\t\t");
            _builder.append(";i++){");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("hasProcess = _reader.readBool();");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("if (hasProcess) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t\t");
            _builder.append("_runningSets[i].decode(_reader);");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("} else {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t\t");
            _builder.append("if (_runningSets[i] != null)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t\t\t");
            _builder.append("_runningSets[i].clear() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("}\t\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("}");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("if (_runningSets[current_core] != null) ");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("_runningSet = _runningSets[current_core] ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("else");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("if (_runningSet != null)");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t\t");
            _builder.append("_runningSet.clear() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t");
            _builder.append("} else {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("for (int i=0; i<");
            DefCore _defcore_15 = Data.schModel.getDefcore();
            int _numcore_8 = _defcore_15.getNumcore();
            _builder.append(_numcore_8, "\t\t\t");
            _builder.append(";i++){\t\t\t\t\t\t\t\t");
            _builder.newLineIfNotEmpty();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("if (_runningSets[i] != null) {");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t\t");
            _builder.append("_runningSets[i].clear() ;");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t\t");
            _builder.append("}\t\t\t\t\t\t\t");
            _builder.newLine();
            _builder.append("\t");
            _builder.append("\t\t");
            _builder.append("}");
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
            _builder.append("_runningSet.decode(_reader);");
            _builder.newLine();
          }
        }
        _builder.append("\t");
        _builder.append("return true ;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.append("protected void clearProcessInScheduler() {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("for (int i = 0 ; i < 128; i ++)");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("processInScheduler[i] = false ;");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    return _builder;
  }
  
  public static CharSequence StaticPropertytoJavaCode(final SchedulerDSL schModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package sspinja.scheduling ;");
    _builder.newLine();
    _builder.append("//Automatic generation");
    _builder.newLine();
    _builder.append("public class StaticProperty extends StaticProperty_");
    SchedulerDef _scheduler = schModel.getScheduler();
    String _name = _scheduler.getName();
    _builder.append(_name, "");
    _builder.append(" { }\t");
    _builder.newLineIfNotEmpty();
    return _builder;
  }
  
  public static CharSequence StaticPropertyDSLtoJavaCode(final ProcessDSL procModel, final SchedulerDSL schModel) {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package sspinja.scheduling;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("//Automatic generation");
    _builder.newLine();
    SchedulerDef _scheduler = schModel.getScheduler();
    String _parent = _scheduler.getParent();
    boolean refinement = (!Objects.equal(_parent, null));
    _builder.newLineIfNotEmpty();
    _builder.append("class StaticProperty_");
    SchedulerDef _scheduler_1 = schModel.getScheduler();
    String _name = _scheduler_1.getName();
    _builder.append(_name, "");
    _builder.append(" ");
    {
      if (refinement) {
        _builder.append("extends StaticProperty_");
        SchedulerDef _scheduler_2 = schModel.getScheduler();
        String _parent_1 = _scheduler_2.getParent();
        _builder.append(_parent_1, "");
      }
    }
    _builder.append("{");
    _builder.newLineIfNotEmpty();
    {
      if ((!refinement)) {
        _builder.append("\t");
        _builder.append("public int refID ;");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("public String pName ;");
        _builder.newLine();
      }
    }
    {
      boolean _isEmpty = Data.valPropertyList.isEmpty();
      boolean _not = (!_isEmpty);
      if (_not) {
        {
          for(final String pName : Data.valPropertyList) {
            {
              boolean _contains = Data.defPropertyList.contains(pName);
              if (_contains) {
                _builder.append("\t");
                _builder.append("public ");
                String _type = Data.getType(pName);
                _builder.append(_type, "\t");
                _builder.append(" get_");
                _builder.append(pName, "\t");
                _builder.append("() {");
                _builder.newLineIfNotEmpty();
                _builder.append("\t");
                _builder.append("\t");
                _builder.append("return this.");
                _builder.append(pName, "\t\t");
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
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }
  
  public static String intPropertyList() {
    String st = "";
    boolean _isEmpty = Data.intProperties.isEmpty();
    boolean _not = (!_isEmpty);
    if (_not) {
      Set<String> _keySet = Data.intProperties.keySet();
      for (final String p : _keySet) {
        String _get = Data.intProperties.get(p);
        String _plus = ((((st + " ") + p) + " = ? ") + _get);
        String _plus_1 = (_plus + ",");
        st = _plus_1;
      }
    }
    return st;
  }
  
  public static String boolPropertyList() {
    String st = "";
    Set<String> _keySet = Data.boolProperties.keySet();
    for (final String p : _keySet) {
      String _get = Data.boolProperties.get(p);
      String _plus = ((((st + " ") + p) + " = ? ") + _get);
      String _plus_1 = (_plus + ",");
      st = _plus_1;
    }
    return st;
  }
  
  public static String bytePropertyList() {
    String st = "";
    Set<String> _keySet = Data.byteProperties.keySet();
    for (final String p : _keySet) {
      String _get = Data.byteProperties.get(p);
      String _plus = ((((st + " ") + p) + " = ?") + _get);
      String _plus_1 = (_plus + ",");
      st = _plus_1;
    }
    return st;
  }
  
  public static String clockPropertyList() {
    String st = "";
    Set<String> _keySet = Data.clockProperties.keySet();
    for (final String p : _keySet) {
      String _get = Data.clockProperties.get(p);
      String _plus = ((((st + " ") + p) + " = ?") + _get);
      String _plus_1 = (_plus + ",");
      st = _plus_1;
    }
    return st;
  }
  
  public static String periodicClockPropertyList() {
    String st = "";
    Set<String> _keySet = Data.periodicClockProperties.keySet();
    for (final String p : _keySet) {
      String _get = Data.periodicClockProperties.get(p);
      String _plus = ((((st + " ") + p) + " = ?") + _get);
      String _plus_1 = (_plus + "; ");
      st = _plus_1;
    }
    return st;
  }
  
  public static String VerifierToJavaCode() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package sspinja.verifier;");
    _builder.newLine();
    _builder.append("import sspinja.SchedulerPanModel;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public class Verifier {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static boolean isVerified = false;");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static void main(String[] args) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("isVerified = true;");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("SchedulerPanModel.main(args);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}\t");
    _builder.newLine();
    return _builder.toString();
  }
  
  public static String SimulatorToJavaCode() {
    StringConcatenation _builder = new StringConcatenation();
    _builder.append("package sspinja.simulator;");
    _builder.newLine();
    _builder.append("import sspinja.ui.MainUI;");
    _builder.newLine();
    _builder.newLine();
    _builder.append("public class Simulator {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("public static void main(String[] args) {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("MainUI.main(args);");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}\t");
    _builder.newLine();
    return _builder.toString();
  }
}
