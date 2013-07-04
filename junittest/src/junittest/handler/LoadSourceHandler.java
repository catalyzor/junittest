/**
 * 
 */
package junittest.handler;

import junittest.view.ProjectView;
import junittest.wizard.LoadWizard;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * @author Michael
 *
 */
public class LoadSourceHandler extends AbstractHandler implements IHandler {

	/* (non-Javadoc)
	 * @see org.eclipse.core.commands.IHandler#execute(org.eclipse.core.commands.ExecutionEvent)
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		LoadWizard wizard = new LoadWizard();
		WizardDialog dialog = new WizardDialog(window.getShell(), wizard);
		if(dialog.open() == IDialogConstants.OK_ID){
			ProjectView view = (ProjectView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ProjectView.ID);
			if(view != null){
				view.changePrject(wizard.getProject());
			}
		}
		return null;
	}

}
