package junittest.handler;

import java.io.IOException;

import junittest.Activator;
import junittest.preferences.PreferenceConstants;
import junittest.resource.ResourceManager;
import junittest.util.ReportUtils;
import junittest.view.LogHistoryView;
import net.sf.jasperreports.engine.JRException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class SimpleReportHandler extends AbstractHandler implements IHandler {

	public static final String FILE_NAME = "概要报告";
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		LogHistoryView view = (LogHistoryView) HandlerUtil.getActivePart(event);
		if (view == null) return null;
		
		IStructuredSelection selection = (IStructuredSelection) view.getTableViewer().getSelection();
		if(!selection.isEmpty()){
			IResource res = (IResource) selection.getFirstElement();
			String jasperfile = "reports/report4.jasper";
			String filetype = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.REPORT_FILE_TYPE);
			String destFile = ResourceManager.getInstance().getProject().getFolder(ResourceManager.FOLDER_REPORT).getLocation().append(FILE_NAME).addFileExtension(filetype.toLowerCase()).toOSString();
			try {
				ReportUtils.generateReport(res, FileLocator.resolve(Activator.getDefault().getBundle().getEntry(jasperfile)).getFile(), filetype, destFile);
				MessageDialog.openInformation(window.getShell(), "提示", "报告生成成功，路径为" + destFile);
			} catch (JRException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ErrorDialog.openError(window.getShell(), "错误", "生成报告失败", new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(),e));
			}
		}
		return null;
	}

}
