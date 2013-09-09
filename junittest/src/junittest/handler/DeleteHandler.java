package junittest.handler;

import java.util.Iterator;

import junittest.resource.ResourceManager;
import junittest.view.LogHistoryView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

public class DeleteHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		LogHistoryView view1 = (LogHistoryView) HandlerUtil.getActivePart(event);
		if(view1 == null) return null;
//		ISelection selection = HandlerUtil.getCurrentSelection(event);
		ISelection selection = view1.getTableViewer().getSelection();
		if(!selection.isEmpty() && IStructuredSelection.class.isInstance(selection) && MessageDialog.openConfirm(window.getShell(), Messages.DeleteHandler_0, Messages.DeleteHandler_1)){
			IStructuredSelection ss = (IStructuredSelection)selection;
			for(Iterator itr = ss.iterator() ; itr.hasNext();){
				Object obj =  itr.next();
				if(obj instanceof IResource){
					try {
						if(obj.equals(ResourceManager.getInstance().getProject())){
							IHandlerService service = (IHandlerService) PlatformUI.getWorkbench().getService(IHandlerService.class);
							if(service != null){
								try {
									service.executeCommand(Messages.DeleteHandler_2, null);
								} catch (NotDefinedException
										| NotEnabledException
										| NotHandledException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						((IResource)obj).delete(true, null);
						LogHistoryView view = (LogHistoryView) window.getActivePage().findView(LogHistoryView.ID);
						if(view != null) view.refreshView(null);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

}
