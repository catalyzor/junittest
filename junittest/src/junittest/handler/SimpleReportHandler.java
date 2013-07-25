package junittest.handler;

import junittest.resource.ResourceManager;
import junittest.util.ReportUtils;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class SimpleReportHandler extends AbstractHandler implements IHandler {

	public static final String FILE_NAME = "概要报告.pdf";
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		IStructuredSelection selection = (IStructuredSelection) HandlerUtil.getCurrentSelection(event);
		if(!selection.isEmpty()){
			IResource res = (IResource) selection.getFirstElement();
			String jasperfile = "reports/report4.jasper";
			String destFile = ResourceManager.getInstance().getProject().getFolder(ResourceManager.FOLDER_REPORT).getLocation().append(FILE_NAME).toOSString();
			ReportUtils.generateReport(res, jasperfile, ReportUtils.REPORT_TYPE_PDF, destFile);
			MessageDialog.openInformation(window.getShell(), "提示", "报告生成成功，路径为" + destFile);
		}
		return null;
	}

}
