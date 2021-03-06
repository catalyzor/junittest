package junittest.handler;

import junittest.Activator;
import junittest.debug.JUnitRunner;
import junittest.resource.ResourceManager;
import junittest.ui.ResourceLabelProvider;
import junittest.view.DeviceView;
import junittest.view.LogHistoryView;
import junittest.view.LogView;
import junittest.view.ProjectView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

public class OpenProjectHandler extends AbstractHandler implements IHandler {

	private static final String CMD_CLOSE = "junittest.command.close";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		final IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
//		ElementListSelectionDialog dialog = new ElementListSelectionDialog(window.getShell(), new DecoratingLabelProvider(
//                new WorkbenchLabelProvider(), Activator.getDefault().getWorkbench()
//                .getDecoratorManager().getLabelDecorator()));
		final ElementListSelectionDialog dialog = new ElementListSelectionDialog(window.getShell(), new ResourceLabelProvider());
		dialog.setTitle(Messages.OpenProjectHandler_0);
		dialog.setElements(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		dialog.setMultipleSelection(false);
		if(dialog.open() == IDialogConstants.OK_ID){
			
			//close project
			IHandlerService service = (IHandlerService) window.getService(IHandlerService.class);
			if(service != null){
				try {
					service.executeCommand(CMD_CLOSE, null);
				} catch (NotDefinedException | NotEnabledException
						| NotHandledException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
				}
			}
			
			final ProjectView view = (ProjectView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ProjectView.ID);
			if(view != null){
				Job job = new Job(Messages.OpenProjectHandler_1) {
					
					@Override
					protected IStatus run(IProgressMonitor monitor) {
						// TODO Auto-generated method stub
						monitor.beginTask(Messages.OpenProjectHandler_2, IProgressMonitor.UNKNOWN);
						ResourceManager.getInstance().setProject((IProject) dialog.getFirstResult());
						Display.getDefault().asyncExec(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								view.changePrject(new Object[]{ dialog.getFirstResult()});
								LogHistoryView lhv = (LogHistoryView) window.getActivePage().findView(LogHistoryView.ID);
								if(lhv != null) lhv.refreshView(null);
								
								LogView lv = (LogView) window.getActivePage().findView(LogView.ID);
								if(lv != null){
									JUnitRunner.getInstance().setXMLLog(null);
									lv.setDoc(null);
									lv.refreshView();
								}
								DeviceView view = (DeviceView) window.getActivePage().findView(DeviceView.ID);
								if(view != null){
										view.setInput(ResourceManager.getInstance().getDeviceManager());
								}
							}
						});
						monitor.done();
						return Status.OK_STATUS;
					}
				};
				job.setPriority(Job.LONG);
				job.setUser(true);
				job.setSystem(true);
				job.schedule(500);
			}
		}
		return null;
	}

}
