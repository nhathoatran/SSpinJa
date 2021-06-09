package scheduling.generator;

import com.google.common.base.Objects;
import org.eclipse.emf.common.util.EList;
import org.eclipse.xtext.generator.IFileSystemAccess2;
import scheduling.dsl.BoolValue;
import scheduling.dsl.NumValue;
import scheduling.dsl.ParameterAssign;
import scheduling.dsl.ParameterName;
import scheduling.dsl.Value;

@SuppressWarnings("all")
public class Utilities {
  public static void deleteGeneratedFiles(final IFileSystemAccess2 fsa) {
    boolean _isFile = fsa.isFile("../src/sspinja/verifier/Verifier.java");
    if (_isFile) {
      fsa.deleteFile("../src/sspinja/verifier/Verifier.java");
    }
    boolean _isFile_1 = fsa.isFile("../src/sspinja/simulator/Simulator.java");
    if (_isFile_1) {
      fsa.deleteFile("../src/sspinja/simulator/Simulator.java");
    }
    boolean _isFile_2 = fsa.isFile("../src/sspinja/scheduling/SchedulerObject.java");
    if (_isFile_2) {
      fsa.deleteFile("../src/sspinja/scheduling/SchedulerObject.java");
    }
    boolean _isFile_3 = fsa.isFile("../src/sspinja/scheduling/chedulerProcess.java");
    if (_isFile_3) {
      fsa.deleteFile("../src/sspinja/scheduling/SchedulerProcess.java");
    }
    boolean _isFile_4 = fsa.isFile("../src/file.dat");
    if (_isFile_4) {
      CharSequence cs = fsa.readTextFile("../src/file.dat");
      String _string = cs.toString();
      String[] fileNames = _string.split(",");
      for (final String f : fileNames) {
        {
          String _trim = f.trim();
          final String fn = ("../src/sspinja/scheduling/" + _trim);
          boolean _isFile_5 = fsa.isFile(fn);
          if (_isFile_5) {
            fsa.deleteFile(fn);
            System.out.println(("delete: " + fn));
          }
        }
      }
    }
  }
  
  public static String setInitValue(final String varSet, final String value) {
    String _xifexpression = null;
    String _trim = value.trim();
    boolean _equals = Objects.equal(_trim, "");
    if (_equals) {
      String _trim_1 = varSet.trim();
      _xifexpression = (_trim_1 + " ; ");
    } else {
      String _trim_2 = varSet.trim();
      String _replace = _trim_2.replace(", ", ((" = " + value) + ", "));
      String _plus = (_replace + " = ");
      String _plus_1 = (_plus + value);
      _xifexpression = (_plus_1 + " ; ");
    }
    return _xifexpression;
  }
  
  public static String initValue(final String varSet, final String value) {
    String _xifexpression = null;
    String _trim = value.trim();
    boolean _notEquals = (!Objects.equal(_trim, ""));
    if (_notEquals) {
      String _trim_1 = varSet.trim();
      String _replace = _trim_1.replace(", ", ((" = " + value) + "; "));
      String _plus = (_replace + " = ");
      String _plus_1 = (_plus + value);
      _xifexpression = (_plus_1 + " ; ");
    }
    return _xifexpression;
  }
  
  public static String findValueInParameterList(final String variable, final EList<ParameterAssign> paralist) {
    String x = "";
    for (final ParameterAssign para : paralist) {
      EList<ParameterName> _paraname = para.getParaname();
      for (final ParameterName pname : _paraname) {
        String _name = pname.getName();
        String _trim = _name.trim();
        String _trim_1 = variable.trim();
        boolean _equals = _trim.equals(_trim_1);
        if (_equals) {
          Value _val = para.getVal();
          NumValue _num = _val.getNum();
          boolean _notEquals = (!Objects.equal(_num, null));
          if (_notEquals) {
            Value _val_1 = para.getVal();
            NumValue _num_1 = _val_1.getNum();
            int _value = _num_1.getValue();
            return Integer.valueOf(_value).toString();
          } else {
            Value _val_2 = para.getVal();
            BoolValue _bool = _val_2.getBool();
            return _bool.getValue();
          }
        }
      }
    }
    return x;
  }
}
