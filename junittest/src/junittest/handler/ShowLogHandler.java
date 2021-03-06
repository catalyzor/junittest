package junittest.handler;

import junittest.Activator;
import junittest.ui.LogViewer;
import junittest.view.LogHistoryView;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class ShowLogHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IWorkbenchPart part = HandlerUtil.getActivePart(event);
		if(part == null) return null;

		IStructuredSelection selection = (IStructuredSelection) ((LogHistoryView)part).getTableViewer().getSelection();
//		IStructuredSelection selection = (IStructuredSelection)HandlerUtil.getCurrentSelection(event);
		if(!selection.isEmpty()){
			IResource res = (IResource) selection.getFirstElement();
			if(res.exists()){
				SAXReader reader = new SAXReader();
				try {
					Document doc = reader.read(res.getLocation().toFile());
					LogViewer view = new LogViewer(res.getName(), doc);
					view.setMainLog(res.getLocation().toFile());
					view.open();
				} catch (DocumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
					ErrorDialog.openError(window.getShell(), Messages.ShowLogHandler_0, Messages.ShowLogHandler_1, new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
				}
			}
		}
		return null;
	}

}
