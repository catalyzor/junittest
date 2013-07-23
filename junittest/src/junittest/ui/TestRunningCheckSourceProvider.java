package junittest.ui;

import java.util.HashMap;
import java.util.Map;

import junittest.debug.JUnitRunner;

import org.eclipse.ui.AbstractSourceProvider;
import org.eclipse.ui.ISources;

public class TestRunningCheckSourceProvider extends AbstractSourceProvider {

	public static final String SOURCE_NAME = "junittest.isRunning";
	public static final String STATE_TRUE = "true";
	public static final String STATE_FALSE = "false";
	public TestRunningCheckSourceProvider() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public Map getCurrentState() {
		// TODO Auto-generated method stub
		Map map = new HashMap(1);
//		String state = STATE_FALSE;
		map.put(SOURCE_NAME, Boolean.FALSE);
		if(JUnitRunner.getInstance().isRunning()){
//			state = STATE_TRUE;
			map.put(SOURCE_NAME, Boolean.TRUE);
		}
//		map.put(SOURCE_NAME, state);
		return map;
	}

	public void fireStateChange(){
		fireSourceChanged(ISources.WORKBENCH, getCurrentState());
	}
	@Override
	public String[] getProvidedSourceNames() {
		// TODO Auto-generated method stub
		return new String[]{SOURCE_NAME};
	}

}
