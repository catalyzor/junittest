package junittest.handler;

import junittest.resource.ResourceManager;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.dialogs.ListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class DeleteProjectHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ListSelectionDialog dialog = new ListSelectionDialog(window.getShell(), ResourcesPlugin.getWorkspace().getRoot().getProjects(), new ArrayContentProvider(), new DecoratingLabelProvider(new WorkbenchLabelProvider(), window.getWorkbench().getDecoratorManager().getLabelDecorator()), Messages.DeleteProjectHandler_0);
		if(dialog.open() == IDialogConstants.OK_ID){
			Object[] objs = dialog.getResult();
			if(objs != null && objs.length > 0){

				IProject[] projects = new IProject[objs.length];
				for(int i = 0; i < objs.length; i++){
					projects[i] = (IProject) objs[i];
				}
				ResourceManager.getInstance().deleteProject(projects);
			}
		}
		return null;
	}

}
