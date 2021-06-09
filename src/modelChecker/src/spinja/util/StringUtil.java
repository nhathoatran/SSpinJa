package spinja.util;


public class StringUtil {
	public static String changeRunStatement(String text){
		if (text.contains("run")) { //change the syntax
			String t = text.replace("run", "execute(");
			String t1= t.replaceAll(" ", "");
			String t2 = t1.replace("))", ")") ;
			String t3 = t2.replace("execute(", "addProcess(new ");
			return t3 + ")" ;
		} else {
			if (text.contains("execute")) {
				String tex1 = text.replaceAll(" ", "") ;				
				String tex = tex1.replace("execute(", "addProcess(new ");
				return tex ;
			} else
				return text ;
		}
		
	}
	public static boolean containSchedulerKeyword(String st){
		switch (st.trim()) {
			case "running_process":
			case "target_process" : 
			case "_pid":
			case "_pc":
			case "_time_count":
			case "_time_slice":
			case "_test_depth":
				return true ;
			default :
				return false ;
		}
	}
	
	public static boolean containsSchedulerBehavior(String st){
		return  st.contains("sch_api") || st.contains("sch_api_self") || 
				st.contains("sch_exec") || st.contains("sch_get");
	}
	
	public static boolean convert2bool(String val) {
		if (val.equals("true"))
			return true ;
		else
			return false ;
	}
	
	public static int convert2int(String val) {
		return Integer.parseInt(val) ;
	}
	public static int convert2time(String val) {
		return Integer.parseInt(val) ;
	}
	public static int convert2clock(String val) {
		return Integer.parseInt(val) ;
	}
	public static byte convert2byte(String val) {
		return Byte.parseByte(val) ;
	}
}