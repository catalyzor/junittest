package junittest.ui;

import java.util.HashMap;
import java.util.Map;

import junittest.debug.JUnitTestRunnerJob;

import org.eclipse.core.runtime.jobs.IJobChangeEvent;
import org.eclipse.core.runtime.jobs.IJobChangeListener;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

public class TestRunningCheckSourceProvider extends AbstractSourceProvider implements IJobChangeListener {

	public static final String SOURCE_NAME = Messages.TestRunningCheckSourceProvider_0;
	public static final String PAUSE = Messages.TestRunningCheckSourceProvider_1;
	public static final String STATE_TRUE = Messages.TestRunningCheckSourceProvider_2;
	public static final String STATE_FALSE = Messages.TestRunningCheckSourceProvider_3;
	public TestRunningCheckSourceProvider() {
		// TODO Auto-generated constructor stub
		Job.getJobManager().addJobChangeListener(this);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

		Job.getJobManager().removeJobChangeListener(this);
	}

	@Override
	public Map getCurrentState() {
		// TODO Auto-generated method stub
		Map map = new HashMap(1);
//		String state = STATE_FALSE;
		map.put(SOURCE_NAME, Boolean.FALSE);
		map.put(PAUSE, Boolean.FALSE);
//		if(JUnitRunner.getInstance().isRunning()){
		Job[] jobs = Job.getJobManager().find(JUnitTestRunnerJob.FAMILINAME);
		if(jobs != null && jobs.length > 0){
//			state = STATE_TRUE;
			if(jobs[0].getState() == Job.RUNNING){
				map.put(SOURCE_NAME, Boolean.TRUE);
			}
			if(JUnitTestRunnerJob.STATE_PAUSE == jobs[0].getProperty(JUnitTestRunnerJob.STATE)){
				map.put(PAUSE, Boolean.TRUE);
			}
		}
//		map.put(SOURCE_NAME, state);
		return map;
	}

	public void fireStateChange(){
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				fireSourceChanged(ISources.WORKBENCH, getCurrentState());
			}
		});
	}
	@Override
	public String[] getProvidedSourceNames() {
		// TODO Auto-generated method stub
		return new String[]{SOURCE_NAME, PAUSE};
	}

	@Override
	public void aboutToRun(IJobChangeEvent event) {
		// TODO Auto-generated method stub
		fireStateChange();
	}

	@Override
	public void awake(IJobChangeEvent event) {
		// TODO Auto-generated method stub

		fireStateChange();
	}

	@Override
	public void done(IJobChangeEvent event) {
		// TODO Auto-generated method stub

		fireStateChange();
	}

	@Override
	public void running(IJobChangeEvent event) {
		// TODO Auto-generated method stub

		fireStateChange();
	}

	@Override
	public void scheduled(IJobChangeEvent event) {
		// TODO Auto-generated method stub

		fireStateChange();
	}

	@Override
	public void sleeping(IJobChangeEvent event) {
		// TODO Auto-generated method stub

		fireStateChange();
	}

}
