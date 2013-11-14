package junittest.debug;

import java.util.Map;

import junit.framework.AssertionFailedError;
import junittest.resource.ResourceManager;
import junittest.resource.TestResultEnum;
import junittest.userlog.UserLogUtil;
import junittest.view.LogHistoryView;
import junittest.view.LogView;
import junittest.view.ProjectView;
import junittest.view.ResultView;
import junittest.xml.XMLLog;

import org.dom4j.Element;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JUnitRunnerListener extends RunListener {

	private IProgressMonitor monitor;
	private XMLLog xmlLog;
	private Logger logger = LoggerFactory.getLogger(getClass());
//	private IProject project;
	private Job job;
	public Job getJob() {
		return job;
	}
	public void setJob(Job job) {
		this.job = job;
	}
	public IProject getProject() {
		return ResourceManager.getInstance().getProject();
	}
//	public void setProject(IProject project) {
//		this.project = project;
//	}
	public XMLLog getXmlLog(){
		return xmlLog;
	}
	
	public void setXmlLog(XMLLog log){
		this.xmlLog = log;
	}
	public IProgressMonitor getMonitor() {
		if(monitor == null) monitor = new NullProgressMonitor();
		return monitor;
	}

	public void setMonitor(IProgressMonitor monitor) {
		this.monitor = monitor;
	}

	@Override
	public void testRunStarted(Description description) throws Exception {
		// TODO Auto-generated method stub
		super.testRunStarted(description);
		logger.debug(Messages.JUnitRunnerListener_0);
		getMonitor().beginTask(Messages.JUnitRunnerListener_1, description.testCount() * 10);
	}

	@Override
	public void testRunFinished(Result result) throws Exception {
		// TODO Auto-generated method stub
		super.testRunFinished(result);
		getMonitor().done();
//		refreshLogHistoryView(null);
	}

	@Override
	public void testStarted(Description description) throws Exception {
		// TODO Auto-generated method stub
		checkState();
		super.testStarted(description);
		logger.debug(Messages.JUnitRunnerListener_2 + description.getClassName());
		getMonitor().subTask(description.getClassName());
		Element el = getXmlLog().getElement(description.getClassName());
		UserLogUtil.setCurrentNode(el.element(XMLLog.NODE_LOG));
		refreshLogView(el);
	}

	public void checkState() throws Exception{

//		if(getMonitor().isCanceled()) throw new Exception("用户取消");
		//check pause
		while((JUnitTestRunnerJob.STATE_PAUSE == job.getProperty(JUnitTestRunnerJob.STATE)) && !getMonitor().isCanceled()){
			Thread.sleep(500);
		}
	}
	@Override
	public void testFinished(Description description) throws Exception {
		// TODO Auto-generated method stub
		logger.debug(Messages.JUnitRunnerListener_3 + description.getClassName());
		checkState();
		super.testFinished(description);
		if(description.isTest()){
			Element el = getXmlLog().getElement(description.getClassName());
			Map<String, TestResultEnum> map = ResourceManager.getInstance().getMapResult();
			if(!TestResultEnum.FAIL.equals(map.get(description.getClassName())) && !TestResultEnum.ERROR.equals(map.get(description.getClassName()))){
				ResourceManager.getInstance().getMapResult().put(description.getClassName(), TestResultEnum.OK);
			}
			getXmlLog().updateTestResult(description.getClassName(), ResourceManager.getInstance().getMapResult().get(description.getClassName()));
			getXmlLog().saveToFile();
			ResourceManager.getInstance().getProject().getFolder(ResourceManager.FOLDER_LOG).refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(getMonitor(), 1));
			refreshProjectView(description.getClassName());
			refreshLogView(el);
			getMonitor().worked(10);
		}
	}

	@Override
	public void testFailure(Failure failure) throws Exception {
		// TODO Auto-generated method stub
		super.testFailure(failure);
		logger.debug(Messages.JUnitRunnerListener_4 + failure.getMessage());
		if(failure.getDescription().isTest()){
			if(failure.getException() != null && !(failure.getException() instanceof AssertionFailedError)){
				ResourceManager.getInstance().getMapResult().put(failure.getDescription().getClassName(), TestResultEnum.ERROR);
			}else{
				ResourceManager.getInstance().getMapResult().put(failure.getDescription().getClassName(), TestResultEnum.FAIL);
			}
		}
	}

	@Override
	public void testAssumptionFailure(Failure failure) {
		// TODO Auto-generated method stub
		super.testAssumptionFailure(failure);
		logger.debug(Messages.JUnitRunnerListener_5 + failure.getMessage());
		if(failure.getDescription().isTest()){
			ResourceManager.getInstance().getMapResult().put(failure.getDescription().getClassName(), TestResultEnum.ERROR);
		}
	}

	@Override
	public void testIgnored(Description description) throws Exception {
		// TODO Auto-generated method stub
		super.testIgnored(description);
		logger.debug(description.getClassName() + Messages.JUnitRunnerListener_6);
	}

	protected void refreshProjectView(final String classname){
		// TODO Auto-generated method stub
		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ProjectView view = (ProjectView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ProjectView.ID);
					view.refreshView(classname);
			}
		});
	}
	protected void refreshLogHistoryView(final IResource res) {
		// TODO Auto-generated method stub
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LogHistoryView view = (LogHistoryView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(LogHistoryView.ID);
					view.refreshView(res);
			}
		});
	}
	protected void refreshLogView(final Element el) {
		// TODO Auto-generated method stub
		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LogView view = (LogView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(LogView.ID);
				view.setDoc(getXmlLog().getDocument());
				if(el == null){
					view.refreshView();
				}else{
					view.refreshNode(el);
				}
			}
		});
	}
	
	public static void refreshResultView(final int ok, final int fail, final int error){

		Display.getDefault().syncExec(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				ResultView view = (ResultView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(ResultView.ID);
				if(view != null){
					view.updateResult(ok, fail, error);
				}
			}
		});
	}
}
