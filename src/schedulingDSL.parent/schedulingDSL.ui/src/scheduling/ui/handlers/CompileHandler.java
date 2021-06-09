package scheduling.ui.handlers;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;

import sspinja.CompileScheduler;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class CompileHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {

		
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchWindow window = 
		        workbench == null ? null : workbench.getActiveWorkbenchWindow();
		IWorkbenchPage activePage = 
		        window == null ? null : window.getActivePage();

		IEditorPart editor = 
		        activePage == null ? null : activePage.getActiveEditor();
		IEditorInput input = 
		        editor == null ? null : editor.getEditorInput();
		IPath path = input instanceof FileEditorInput 
		        ? ((FileEditorInput)input).getPath()
		        : null;
		if (path != null)
		{
			//Call compile
			Set<String> set = new HashSet<>(); 
			set.add(path.toString());
			String[] newargs = set.toArray(new String[set.size()]) ;
			
			try {
				CompileScheduler.main(newargs);				
				MessageDialog.openInformation(
						window.getShell(),
						window.getActivePage().getLabel().toString(),
						"Compile " + path + "\nTo Java Model (src/sspinja/SchedulerPanModel.java)");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			MessageDialog.openInformation(
					window.getShell(),
					window.getActivePage().getLabel().toString() ,
					"CompilePromelaHandler: No file selected");
		}
		
		
		return null;
	}
}
