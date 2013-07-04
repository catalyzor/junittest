package junittest.handler;

import junittest.view.ProjectView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.model.WorkbenchLabelProvider;

public class OpenProjectHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ElementListSelectionDialog dialog = new ElementListSelectionDialog(window.getShell(), new WorkbenchLabelProvider());
		dialog.setTitle("选择要打开的工程");
		dialog.setElements(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		dialog.setMultipleSelection(false);
		if(dialog.open() == IDialogConstants.OK_ID){
			ProjectView view = (ProjectView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ProjectView.ID);
			if(view != null){
				view.changePrject((IProject) dialog.getFirstResult());
			}
		}
		return null;
	}

}
