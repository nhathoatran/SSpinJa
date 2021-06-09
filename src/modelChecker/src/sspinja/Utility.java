package sspinja;


import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;

public class Utility {

	public static void refreshDir() throws Exception {
		for(IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()){
		    project.refreshLocal(IResource.DEPTH_INFINITE, new NullProgressMonitor());
		}
	}
}
