package junittest.debug;

import java.util.Calendar;
import java.util.List;

import junittest.resource.ResourceManager;
import junittest.xml.XMLLog;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.QualifiedName;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;

public class JUnitTestRunnerJob extends Job {

	public static final String FAMILINAME = "JUnitTestRunnerFamily";
//	private Class[] classes;
	private List<String> lstClasses;
	private JUnitCore jUnitCore;
	private JUnitRunnerListener runListener;
	public static final QualifiedName STATE = new QualifiedName("state", "state");
	public static final String STATE_PAUSE = "pause";
	public JUnitTestRunnerJob(String name) {
		super(name);
		// TODO Auto-generated constructor stub
		jUnitCore = new JUnitCore();
		runListener = new JUnitRunnerListener();
		runListener.setJob(this);
		jUnitCore.addListener(runListener);
//		this.addJobChangeListener(new IJobChangeListener() {
//			
//			@Override
//			public void sleeping(IJobChangeEvent event) {
//				// TODO Auto-generated method stub
//				event.getJob().sleep();
//			}
//			
//			@Override
//			public void scheduled(IJobChangeEvent event) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void running(IJobChangeEvent event) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void done(IJobChangeEvent event) {
//				// TODO Auto-generated method stub
//				
//			}
//			
//			@Override
//			public void awake(IJobChangeEvent event) {
//				// TODO Auto-generated method stub
//				event.getJob().wakeUp();
//			}
//			
//			@Override
//			public void aboutToRun(IJobChangeEvent event) {
//				// TODO Auto-generated method stub
////				event.getJob().sleep();
//			}
//		});
	}

	public JUnitCore getJUnitCore(){
		return jUnitCore;
	}
	
	public XMLLog getXmlLog(){
		return this.runListener.getXmlLog();
	}
//	public Class[] getClasses() {
//		return classes;
//	}

	public void setClasses(List<String> list) {
		this.lstClasses = list;
	}

	@Override
	public boolean belongsTo(Object family) {
		// TODO Auto-generated method stub
		return FAMILINAME == family;
	}

	@Override
	protected IStatus run(IProgressMonitor monitor) {
		// TODO Auto-generated method stub
		monitor.beginTask("执行测试", lstClasses.size() * 10);
		ResourceManager.getInstance().getMapResult().clear();
		String name = Calendar.getInstance().getTimeInMillis() + "";
		this.runListener.setXmlLog(new XMLLog(name, this.runListener.getProject()));
//		getMonitor().subTask("生成日志文件");
//		logger.debug("初始化日志结构");
		getXmlLog().initStructure();
//		logger.debug("保存文件");
		IPath path = getXmlLog().saveToFile();
		IResource logfile = this.runListener.getProject().getFile(path.makeRelativeTo(this.runListener.getProject().getLocation()));
//		logger.debug("保存完毕");
		try {
			this.runListener.getProject().getFolder(ResourceManager.FOLDER_LOG).refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.runListener.refreshProjectView(null);
		this.runListener.refreshLogHistoryView(null);
		this.runListener.refreshLogView(null);
//		try{
//			Result result = this.jUnitCore.run(classes);
//		}catch(Exception e){
//			return Status.CANCEL_STATUS;
//		}
		for(String c: this.lstClasses){
			if(monitor.isCanceled()){
				XMLLog.log = null;
				this.runListener.refreshLogHistoryView(logfile);
				return Status.CANCEL_STATUS;
			}
			runListener.setMonitor(new SubProgressMonitor(monitor, 10));
			
			try {
				Result result = this.jUnitCore.run(Class.forName(c, false, ResourceManager.getInstance().urlClassLoad));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		XMLLog.log = null;
		return Status.OK_STATUS;
	}

	@Override
	protected void canceling() {
		// TODO Auto-generated method stub
		super.canceling();
	}

	@Override
	public boolean shouldRun() {
		// TODO Auto-generated method stub
		return super.shouldRun();
	}

}
