package junittest.handler;

import junittest.debug.JUnitRunner;
import junittest.debug.JUnitTestRunnerJob;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.jobs.Job;

public class ResumeHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
//		Job.getJobManager().wakeUp(JUnitTestRunnerJob.FAMILINAME);
		Job[] jobs = Job.getJobManager().find(JUnitTestRunnerJob.FAMILINAME);
		if(jobs != null && jobs.length > 0){
			jobs[0].setProperty(JUnitTestRunnerJob.STATE, null);
		}
		JUnitRunner.fireStateChange();
		return null;
	}

}
