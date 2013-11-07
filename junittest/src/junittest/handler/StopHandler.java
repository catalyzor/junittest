package junittest.handler;

import junittest.Activator;
import junittest.debug.JUnitTestRunnerJob;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.handlers.IHandlerService;

public class StopHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
//		JUnitRunner.getInstance().stop();
		Job.getJobManager().cancel(JUnitTestRunnerJob.FAMILINAME);
		IHandlerService service = (IHandlerService) HandlerUtil.getActiveWorkbenchWindow(event).getService(IHandlerService.class);
		if(service != null){
			try {
				service.executeCommand(Messages.StopHandler_0, null);
			} catch (NotDefinedException | NotEnabledException
					| NotHandledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
			}
		}
		Job[] jobs = Job.getJobManager().find(JUnitTestRunnerJob.FAMILINAME);
		if(jobs != null && jobs.length > 0){
			jobs[0].getThread().interrupt();
		}
		return null;
	}

}
