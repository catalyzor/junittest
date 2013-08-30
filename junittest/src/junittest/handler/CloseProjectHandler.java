package junittest.handler;

import junittest.resource.ResourceManager;
import junittest.view.DeviceView;
import junittest.view.LogHistoryView;
import junittest.view.LogView;
import junittest.view.ProjectView;
import junittest.xml.XMLLog;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class CloseProjectHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		doClean(window);
		return null;
	}

	private void doClean(IWorkbenchWindow window) {
		// TODO Auto-generated method stub
		ResourceManager.getInstance().setProject(null);
		LogHistoryView lhv = (LogHistoryView) window.getActivePage().findView(LogHistoryView.ID);
		if(lhv != null){
			lhv.refreshView(null);
		}
		
		LogView lv = (LogView) window.getActivePage().findView(LogView.ID);
		if(lv != null){
			XMLLog.log = null;
			lv.setDoc(null);
			lv.refreshView();
		}
		
		DeviceView dv = (DeviceView) window.getActivePage().findView(DeviceView.ID);
		if(dv != null){
			dv.setInput(null);
		}

		ProjectView view = (ProjectView) window.getActivePage().findView(ProjectView.ID);
		if(view != null){
			view.getSite().getSelectionProvider().setSelection(StructuredSelection.EMPTY);
			view.changePrject(null);
		}
	}

}
