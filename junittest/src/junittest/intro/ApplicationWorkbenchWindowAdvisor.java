package junittest.intro;

import junittest.debug.JUnitTestRunnerJob;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

    public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
        super(configurer);
    }

    public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
        return new ApplicationActionBarAdvisor(configurer);
    }
    
    public void preWindowOpen() {
        IWorkbenchWindowConfigurer configurer = getWindowConfigurer();
        configurer.setInitialSize(new Point(1000, 850));
        configurer.setShowMenuBar(true);
        configurer.setShowCoolBar(true);
        configurer.setShowStatusLine(true);
        configurer.setTitle(Messages.ApplicationWorkbenchWindowAdvisor_0);
        configurer.setShowProgressIndicator(true);
    }

	@Override
	public void postWindowClose() {
		// TODO Auto-generated method stub
//		super.postWindowClose();
		Job.getJobManager().cancel(JUnitTestRunnerJob.FAMILINAME);
	}
}
