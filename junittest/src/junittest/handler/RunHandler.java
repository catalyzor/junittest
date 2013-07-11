package junittest.handler;

import java.util.List;

import junittest.debug.JUnitRunner;
import junittest.resource.ResourceManager;
import junittest.view.ProjectView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

public class RunHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		ProjectView view = (ProjectView) window.getActivePage().findView(ProjectView.ID);
		List<String> list = view.getSelectedCases();
		Class[] classes = new Class[list.size()];
		for (int i = 0; i < classes.length; i++) {
			try {
				classes[i] = ResourceManager.getInstance().urlClassLoad.loadClass(list.get(i));
//				classes[i] = Class.forName(list.get(i));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		JUnitRunner.getInstance().start(classes);
		return null;
	}

}
