package junittest.handler;

import junittest.Activator;
import junittest.resource.ResourceManager;
import junittest.ui.LogViewer;
import junittest.util.Utilities;

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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class QuickViewLogHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IResource res = Utilities.getLatestLogfile(ResourceManager.getInstance().getProject());
		if(res == null){
			MessageDialog.openError(window.getShell(), Messages.QuickViewLogHandler_0, Messages.QuickViewLogHandler_1);
			return null;
		}
		if(res.exists()){
			SAXReader reader = new SAXReader();
			try {
				Document doc = reader.read(res.getLocation().toFile());
				LogViewer view = new LogViewer(res.getName(), doc);
				view.setMainLog(res);
				view.open();
			} catch (DocumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ErrorDialog.openError(window.getShell(), Messages.QuickViewLogHandler_2, Messages.QuickViewLogHandler_3, new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
			}
		}
		return null;
	}

}
