package junittest.debug;

import org.junit.runner.Description;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class JUnitRunner {

	private Class[] classes;
	private JUnitCore junit;
	private Thread runThread;
	
	private static JUnitRunner instance;
	
	private JUnitRunner(){
		junit = new JUnitCore();
		junit.addListener(new RunListener(){

			@Override
			public void testRunStarted(Description description)
					throws Exception {
				// TODO Auto-generated method stub
				super.testRunStarted(description);
			}

			@Override
			public void testRunFinished(Result result) throws Exception {
				// TODO Auto-generated method stub
				super.testRunFinished(result);
			}

			@Override
			public void testStarted(Description description) throws Exception {
				// TODO Auto-generated method stub
				super.testStarted(description);
			}

			@Override
			public void testFinished(Description description) throws Exception {
				// TODO Auto-generated method stub
				super.testFinished(description);
			}

			@Override
			public void testFailure(Failure failure) throws Exception {
				// TODO Auto-generated method stub
				super.testFailure(failure);
			}

			@Override
			public void testAssumptionFailure(Failure failure) {
				// TODO Auto-generated method stub
				super.testAssumptionFailure(failure);
			}

			@Override
			public void testIgnored(Description description) throws Exception {
				// TODO Auto-generated method stub
				super.testIgnored(description);
			}
			
		});
	}
	
	public static JUnitRunner getInstance(){
		if(instance == null)
			instance = new JUnitRunner();
		return instance;
	}
	
	public void start(Class[] classes){
		this.classes = classes;
		
		runThread = new Thread("Run Tests"){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Result result = junit.run(JUnitRunner.this.classes);
			}
			
		};
		
		runThread.start();
	}
	
	public void stop(){
		if(runThread != null){
			runThread.interrupt();
		}
	}
	
	public void pause(){
		if(runThread != null){
//			IProcess process = DebugPlugin.newProcess(null, null, null);
//			process.
		}
	}
}
