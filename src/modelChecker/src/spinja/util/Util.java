package spinja.util ;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;

public class Util {
	public static int max(int a, int b) {
		return (a > b) ? a : b ;
	}

	public static int min(int a, int b) {
		return (a > b) ? b : a ;
	}
	
	public static void print(String s){
		System.out.print(s);
	}
	
	public static void println(String s){
		System.out.println(s);
	}
	
//	public static void refreshDir() throws Exception {
//		System.out.println("refresh directory");
//		for(IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()){
//			System.out.println(project.toString());
//		    project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
//		}
//		System.out.println("refresh directory");
//	}
	
	public static void clearDir(){
		File userDir = new File(System.getProperty("user.dir") + "/spinja");		
		File[] listFiles = userDir.listFiles() ; 

		for (File file : listFiles) {
		    if (file.isFile()) {
		    	System.out.print(file.toString());
		    }
		}
	}
	
	
}