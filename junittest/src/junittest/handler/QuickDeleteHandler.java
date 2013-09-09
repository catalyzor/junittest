package junittest.handler;

import junittest.resource.ResourceManager;
import junittest.util.Utilities;
import junittest.view.LogHistoryView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class QuickDeleteHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		if(!MessageDialog.openConfirm(window.getShell(), Messages.QuickDeleteHandler_0, Messages.QuickDeleteHandler_1)){
			return null;
		}
		IResource res = Utilities.getLatestLogfile(ResourceManager.getInstance().getProject());
		if(res == null){
			MessageDialog.openError(window.getShell(), Messages.QuickDeleteHandler_2, Messages.QuickDeleteHandler_3);
			return null;
		}

		try {
			res.delete(true, null);
			Display.getDefault().asyncExec(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					LogHistoryView view = (LogHistoryView) window.getActivePage().findView(LogHistoryView.ID);
					if(view != null) view.refreshView(null);
				}
			});
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
