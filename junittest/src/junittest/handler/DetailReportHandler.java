package junittest.handler;

import java.io.IOException;

import junittest.Activator;
import junittest.preferences.PreferenceConstants;
import junittest.util.ReportUtils;
import junittest.view.LogHistoryView;
import net.sf.jasperreports.engine.JRException;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class DetailReportHandler extends AbstractHandler implements IHandler {

	public static final String FILE_NAME = Messages.DetailReportHandler_0;
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		LogHistoryView view = (LogHistoryView) HandlerUtil.getActivePart(event);
		if (view == null) return null;
		
		IStructuredSelection selection = (IStructuredSelection) view.getTableViewer().getSelection();
		if(!selection.isEmpty()){
			final IResource res = (IResource) selection.getFirstElement();
			final String jasperfile = Messages.DetailReportHandler_1;
			final String filetype = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.REPORT_FILE_TYPE);
//			String destFile = ResourceManager.getInstance().getProject().getFolder(ResourceManager.FOLDER_REPORT).getLocation().append(FILE_NAME).addFileExtension(filetype.toLowerCase()).toOSString();
			FileDialog dialog = new FileDialog(window.getShell(), SWT.SAVE);
			dialog.setFilterExtensions(new String[]{Messages.DetailReportHandler_2 + filetype.toLowerCase()});
			dialog.setFileName(FILE_NAME);
			dialog.setOverwrite(true);
			final String destFile = dialog.open();
			if(destFile == null) return null;
			Job job = new Job(Messages.DetailReportHandler_3) {
				
				@Override
				protected IStatus run(IProgressMonitor monitor) {
					monitor.beginTask(Messages.DetailReportHandler_4, IProgressMonitor.UNKNOWN);
					try {
//						InputStream is = Activator.getDefault().getBundle().getEntry(jasperfile).openStream();
						ReportUtils.generateReport(res, FileLocator.resolve(Activator.getDefault().getBundle().getEntry(jasperfile)).getFile(), filetype, destFile);
//						is.close();
						Display.getDefault().asyncExec(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								MessageDialog.openInformation(new Shell(), Messages.DetailReportHandler_5, Messages.DetailReportHandler_6 + destFile);
							}
						});
					} catch (JRException | IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Display.getDefault().asyncExec(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								ErrorDialog.openError(new Shell(), Messages.DetailReportHandler_7, Messages.DetailReportHandler_8, new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(),e));
							}
						});
					}
					monitor.done();
					return Status.OK_STATUS;
				}
			};
			job.setPriority(Job.LONG);
//			job.setu
			job.schedule();
		}
		return null;
	}

}
