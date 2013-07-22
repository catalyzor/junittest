package junittest.handler;

import java.util.Iterator;

import junittest.view.LogHistoryView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class DeleteHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		if(!selection.isEmpty() && IStructuredSelection.class.isInstance(selection) && MessageDialog.openConfirm(window.getShell(), "и╬ЁЩ", "х╥хои╬ЁЩ")){
			IStructuredSelection ss = (IStructuredSelection)selection;
			for(Iterator itr = ss.iterator() ; itr.hasNext();){
				Object obj =  itr.next();
				if(obj instanceof IResource){
					try {
						((IResource)obj).delete(true, null);
						LogHistoryView view = (LogHistoryView) window.getActivePage().findView(LogHistoryView.ID);
						if(view != null) view.refreshView();
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
