package sspinja;

//Automatic generation
public class Code {
	public String clock = "" ;
	public String select_process ="";	
	public String new_process ="";
	public String pre_take ="";
	public String post_take ="";
	public String process_action ="";
	public String scheduling_action ="";			
	public String code ="";
	
		
	public void clearCode() {
		clock = "" ;
		select_process = "";		
		new_process = "";
		pre_take = "";
		post_take = "";
		process_action = "";
		scheduling_action = "";
		code = "" ;
	}
	
	public void clearcode(String _component) {
		switch (_component) {
			case "clock" :
				clock = "" ; break ;
			case "select_process" :
				select_process = ""; break ;
			case "new_process" :
				new_process = "" ; break ;
			case "pre_take" :
				pre_take = ""; break ;
			case "post_take" :
				post_take = "" ; break ;
			case "process_action" :
				process_action = ""; break ;
			case "scheduling_action" :
				scheduling_action = ""; break ;
			case "code" :
				code = ""; break ;
		}
	}
	
	public void setcode(String _component, String code) {
		switch (_component) {
			case "clock" :
				clock = code ; break ;
			case "select_process" :
				select_process = code ; break ;
			case "new_process" :
				new_process = code ; break ;
			case "pre_take" :
				pre_take = code ; break ;
			case "post_take" :
				post_take = code ; break ;
			case "process_action" :
				process_action = code; break ;
			case "scheduling_action" :
				scheduling_action = code; break ;
		}
	}
	
	protected Code clone() {
		Code newcode = new Code() ;
		
		newcode.clock = clock;
		newcode.select_process = select_process;		
		newcode.new_process = new_process;
		newcode.pre_take = pre_take;
		newcode.post_take = post_take;
		newcode.process_action  = process_action;
		newcode.scheduling_action  = scheduling_action;
		newcode.code = code ;
		return newcode ;
	}
	
	public static void addCodetoComponent(String code, String component, boolean breakline) {
		int index = -1;
	}
}
