package junittest.debug;

import java.net.URLClassLoader;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import junittest.Activator;
import junittest.resource.ResourceManager;
import junittest.resource.TestResultEnum;
import junittest.ui.TestRunningCheckSourceProvider;
import junittest.userlog.UserLogUtil;
import junittest.view.LogHistoryView;
import junittest.view.LogView;
import junittest.xml.XMLLog;

import org.dom4j.Element;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.services.ISourceProviderService;
import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class JUnitRunner {

	private Class[] classes;
	private JUnitCore junit;
	private Thread runThread;
	protected Result result;
	private Object threadStateMark;
	private Boolean pause = false;
	private URLClassLoader urlClassLoad;
	private XMLLog logger;
	private boolean running;
	
	private static JUnitRunner instance;
	
	public XMLLog getXMLLog(){
		return logger;
	}
	public void setXMLLog(XMLLog logger){
		this.logger = logger;
	}
	public void addRunListener(RunListener runListener){
		junit.addListener(runListener);
	}
	
	public void removeRunListener(RunListener runListener){
		junit.removeListener(runListener);
	}
	private JUnitRunner(){
		junit = new JUnitCore();
		junit.addListener(new RunListener(){

			@Override
			public void testRunStarted(Description description)
					throws Exception {
				// TODO Auto-generated method stub
//				super.testRunStarted(description);
				setRunning(true);
				fireStateChange();
//				try {
//					Thread.sleep(5000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				if(pause){
					synchronized (threadStateMark) {
						threadStateMark.wait();
//						pause = false;
						setPause(false);
					}
				}
				System.out.println(Messages.JUnitRunner_0 + description.getDisplayName());
				if(description.isSuite()){
					for(Description des : description.getChildren()){
						if(des.isTest()){
							ResourceManager.getInstance().getMapResult().put(des.getClassName(), TestResultEnum.Ignore);
						}
					}
				}
			}

			@Override
			public void testRunFinished(Result result) throws Exception {
				// TODO Auto-generated method stub
//				super.testRunFinished(result);
				setRunning(false);
				fireStateChange();
				if(pause){
					synchronized (threadStateMark) {
						threadStateMark.wait();
//						pause = false;
						setPause(false);
					}
				}
				System.out.println(Messages.JUnitRunner_1 + result.getRunTime());
				if(!result.wasSuccessful()){
					for(Failure fail: result.getFailures()){
						ResourceManager.getInstance().getMapResult().put(fail.getDescription().getClassName(), TestResultEnum.FAIL);
					}
				}
				refreshLogHistoryView(null);
			}

			@Override
			public void testStarted(Description description) throws Exception {
				// TODO Auto-generated method stub
//				super.testStarted(description);
				Element el = JUnitRunner.getInstance().getXMLLog().getElement(description.getClassName());
				UserLogUtil.setCurrentNode(el.element(XMLLog.NODE_LOG));
				refreshLogView(el);
				if(pause){
					synchronized (threadStateMark) {
						threadStateMark.wait();
//						pause = false;
						setPause(false);
					}
				}
				System.out.println(Messages.JUnitRunner_2 + description.getDisplayName() + Messages.JUnitRunner_3);
			}

			@Override
			public void testFinished(Description description) throws Exception {
				// TODO Auto-generated method stub
//				super.testFinished(description);
				if(pause){
					synchronized (threadStateMark) {
						threadStateMark.wait();
//						pause = false;
						setPause(false);
					}
				}
				System.out.println(Messages.JUnitRunner_4 + description.getDisplayName() + Messages.JUnitRunner_5);
				if(description.isTest()){
					Map<String, TestResultEnum> map = ResourceManager.getInstance().getMapResult();
					if(!TestResultEnum.FAIL.equals(map.get(description.getClassName())) && !TestResultEnum.ERROR.equals(map.get(description.getClassName()))){
						ResourceManager.getInstance().getMapResult().put(description.getClassName(), TestResultEnum.OK);
					}

					Element el = JUnitRunner.getInstance().getXMLLog().getElement(description.getClassName());
					UserLogUtil.setCurrentNode(el.element(XMLLog.NODE_LOG));
					refreshLogView(el);
				}
			}

			@Override
			public void testFailure(Failure failure) throws Exception {
				// TODO Auto-generated method stub
//				super.testFailure(failure);
				if(pause){
					synchronized (threadStateMark) {
						threadStateMark.wait();
//						pause = false;
						setPause(false);
					}
				}
				System.out.println(Messages.JUnitRunner_6 + failure.getMessage());
				if(failure.getDescription().isTest()){
					ResourceManager.getInstance().getMapResult().put(failure.getDescription().getClassName(), TestResultEnum.FAIL);
				}
			}

			@Override
			public void testAssumptionFailure(Failure failure) {
				// TODO Auto-generated method stub
//				super.testAssumptionFailure(failure);
				if(pause){
					synchronized (threadStateMark) {
						try {
							threadStateMark.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
						}
//						pause = false;
						setPause(false);
					}
				}
				System.out.println(Messages.JUnitRunner_7 + failure.getMessage());
				if(failure.getDescription().isTest()){
					ResourceManager.getInstance().getMapResult().put(failure.getDescription().getClassName(), TestResultEnum.ERROR);
				}
			}

			@Override
			public void testIgnored(Description description) throws Exception {
				// TODO Auto-generated method stub
//				super.testIgnored(description);
				if(pause){
					synchronized (threadStateMark) {
						threadStateMark.wait();
//						pause = false;
						setPause(false);
					}
				}
				System.out.println(Messages.JUnitRunner_8 + description.getDisplayName() + Messages.JUnitRunner_9);
			}
			
		});
	}
	
	public static JUnitRunner getInstance(){
		if(instance == null)
			instance = new JUnitRunner();
		return instance;
	}
	
	public static void fireStateChange(){
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI.getWorkbench().getService(ISourceProviderService.class);
		TestRunningCheckSourceProvider sourceProvider = (TestRunningCheckSourceProvider) sourceProviderService.getSourceProvider(TestRunningCheckSourceProvider.SOURCE_NAME);
		sourceProvider.fireStateChange();
	}
	public void resume(){
		if(pause){
			pause = false;
			synchronized (pause) {
				pause.notify();
			}
		}
	}
	public void start(Class[] classes){
		if(pause){
			pause = false;
			synchronized (pause) {
				pause.notify();
			}
		}else{
			ResourceManager.getInstance().getMapResult().clear();
			this.classes = classes;
			runThread = new Thread(Messages.JUnitRunner_10){

				@Override
				public void run() {
					fireStateChange();
					// TODO Auto-generated method stub
					String name = Calendar.getInstance().getTimeInMillis() + Messages.JUnitRunner_11;
					IFolder folder = ResourceManager.getInstance().getProject().getFolder(ResourceManager.FOLDER_LOG).getFolder(name);
					if(!folder.exists()){
						try {
							folder.create(true, true, null);
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
						}
					}
					logger = new XMLLog(name, folder);
					logger.initStructure();
					logger.saveToFile();
					refreshLogView(null);
					refreshLogHistoryView(null);
					try {
						ResourceManager.getInstance().getProject().getFolder(ResourceManager.FOLDER_LOG).refreshLocal(IResource.DEPTH_ONE, null);
					} catch (CoreException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
					}
					result = junit.run(JUnitRunner.this.classes);
				}

			};

			runThread.start();
		}
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
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LogView view = (LogView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(LogView.ID);
				if(el == null){
					view.refreshView();
				}else{
					view.refreshNode(el);
				}
			}
		});
	}
	public void stop(){
		if(runThread != null){
			runThread.interrupt();
		}
		fireStateChange();
	}
	
	public void pause(){
		if(runThread != null){
//			IProcess process = DebugPlugin.newProcess(null, null, null);
//			process.
//			pause = true;
			setPause(true);
			setRunning(false);
		}
		fireStateChange();
	}
	public static void main(String[] args) {

		Job job = new Job(Messages.JUnitRunner_12) {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO Auto-generated method stub
				return null;
			}
		};
//		Object obj = new Object();
//		Job job = new Job("aa") {
//			
//			@Override
//			protected IStatus run(IProgressMonitor monitor) {
//				// TODO Auto-generated method stub
//				while(!monitor.isCanceled()){
//					System.out.println(new Date().getTime());
//					try {
//						Thread.sleep(1000);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//				return Status.OK_STATUS;
//			}
//		};
//		job.belongsTo(obj);
//		job.schedule();
//		job.getJobManager().
		MyThread t = JUnitRunner.getInstance().new MyThread();
		
		t.start();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
		}
		System.out.println(Messages.JUnitRunner_13);
//		try {
			t.setBool(true);
//			job.sleep();
//		Job.getJobManager().sleep(obj);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
		}
		System.out.println(Messages.JUnitRunner_14);
//		t.notify();
		synchronized (t.getObj()) {

			t.getObj().notify();
		}
//		job.wakeUp();
//		Job.getJobManager().wakeUp(obj);
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
		}
		System.out.println(Messages.JUnitRunner_15);
//		t.;
	}

	public class MyThread extends Thread{

		private Object obj = new Object();
		private boolean bool;
		public void setBool(boolean bool){
			this.bool = bool;
		}
		public Object getObj(){
			return obj;
		}
		@Override
		public void run() {
			// TODO Auto-generated method stub
//			super.run();
			while(true){
				System.out.println(new Date().getTime());
				try {
					synchronized (obj) {
						if(bool) obj.wait();
					}
					bool = false;
//					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
				}
			}
		}
		
	};
	public boolean isRunning(){
		return running;
	}
	
	public void setRunning(boolean bool){
		this.running = bool;
	}
	
	public void setPause(boolean bool){
		this.pause = bool;
		setRunning(!bool);
	}
}
