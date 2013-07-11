package junittest.debug;

import java.net.URLClassLoader;
import java.util.Date;
import java.util.Map;

import junittest.resource.ResourceManager;
import junittest.resource.TestResultEnum;

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
	private Boolean pause = false;
	private URLClassLoader urlClassLoad;
	
	private static JUnitRunner instance;
	
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
				if(pause){
					synchronized (pause) {
						pause.wait();
						pause = false;
					}
				}
				System.out.println("testRunStarted:" + description.getDisplayName());
				if(description.isSuite()){
					for(Description des : description.getChildren()){
						if(des.isTest()){
							ResourceManager.getInstance().getMapResult().put(des.getClassName(), TestResultEnum.NORUN);
						}
					}
				}
			}

			@Override
			public void testRunFinished(Result result) throws Exception {
				// TODO Auto-generated method stub
//				super.testRunFinished(result);
				if(pause){
					synchronized (pause) {
						pause.wait();
						pause = false;
					}
				}
				System.out.println("testRunFinished:" + result.getRunTime());
				if(!result.wasSuccessful()){
					for(Failure fail: result.getFailures()){
						ResourceManager.getInstance().getMapResult().put(fail.getDescription().getClassName(), TestResultEnum.FAIL);
					}
				}
			}

			@Override
			public void testStarted(Description description) throws Exception {
				// TODO Auto-generated method stub
//				super.testStarted(description);
				if(pause){
					synchronized (pause) {
						pause.wait();
						pause = false;
					}
				}
				System.out.println("testStarted:" + description.getDisplayName() + "start");
			}

			@Override
			public void testFinished(Description description) throws Exception {
				// TODO Auto-generated method stub
//				super.testFinished(description);
				if(pause){
					synchronized (pause) {
						pause.wait();
						pause = false;
					}
				}
				System.out.println("testFinished:" + description.getDisplayName() + "finished");
				if(description.isTest()){
					Map<String, TestResultEnum> map = ResourceManager.getInstance().getMapResult();
					if(!TestResultEnum.FAIL.equals(map.get(description.getClassName())) && !TestResultEnum.PRE.equals(map.get(description.getClassName()))){
						ResourceManager.getInstance().getMapResult().put(description.getClassName(), TestResultEnum.PASS);
					}
				}
			}

			@Override
			public void testFailure(Failure failure) throws Exception {
				// TODO Auto-generated method stub
//				super.testFailure(failure);
				if(pause){
					synchronized (pause) {
						pause.wait();
						pause = false;
					}
				}
				System.out.println("testFailure:" + failure.getMessage());
				if(failure.getDescription().isTest()){
					ResourceManager.getInstance().getMapResult().put(failure.getDescription().getClassName(), TestResultEnum.FAIL);
				}
			}

			@Override
			public void testAssumptionFailure(Failure failure) {
				// TODO Auto-generated method stub
//				super.testAssumptionFailure(failure);
				if(pause){
					synchronized (pause) {
						try {
							pause.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						pause = false;
					}
				}
				System.out.println("testAssumptionFailure:" + failure.getMessage());
				if(failure.getDescription().isTest()){
					ResourceManager.getInstance().getMapResult().put(failure.getDescription().getClassName(), TestResultEnum.PRE);
				}
			}

			@Override
			public void testIgnored(Description description) throws Exception {
				// TODO Auto-generated method stub
//				super.testIgnored(description);
				if(pause){
					synchronized (pause) {
						pause.wait();
						pause = false;
					}
				}
				System.out.println("testIgnored:" + description.getDisplayName() + "ignored");
			}
			
		});
	}
	
	public static JUnitRunner getInstance(){
		if(instance == null)
			instance = new JUnitRunner();
		return instance;
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
			runThread = new Thread("Run Tests"){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					result = junit.run(JUnitRunner.this.classes);

				}

			};

			runThread.start();
		}
	}
	
	public void stop(){
		if(runThread != null){
			runThread.interrupt();
		}
	}
	
	public void pause(){
		if(runThread != null && runThread.isAlive()){
//			IProcess process = DebugPlugin.newProcess(null, null, null);
//			process.
			pause = true;
		}
	}
	public static void main(String[] args) {

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
		}
		System.out.println("thread wait...");
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
		}
		System.out.println("thread notify...");
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
		}
		System.out.println("thread stop...");
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
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	};
}
