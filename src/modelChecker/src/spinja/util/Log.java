package spinja.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Log {
	public static PrintWriter out  ;
	public static boolean analysisresult = false ;
	public static String filename = "";
			
	public static void initLog(String name) {
		filename = name ;
		try {
			out = new PrintWriter(new BufferedWriter(new FileWriter(name + ".result")));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void flushOutputTest() {
		out.flush();
		out.close();		
		if (analysisresult)
			System.out.println("sspinja: wrote result to " + filename + ".result\n");
	}
}
