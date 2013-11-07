package junittest.handler;

import junittest.Activator;
import junittest.device.DeviceManager;
import junittest.resource.ResourceManager;
import junittest.view.DeviceView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import com.broadthinking.btt.device.ExtDeviceException;

public class DeviceRefreshHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindow(event);
		DeviceView view = (DeviceView) window.getActivePage().findView(DeviceView.ID);
		DeviceManager device = ResourceManager.getInstance().getDeviceManager();
		if(device != null){
			try {
				device.init();
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | ExtDeviceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
			}
		}
		if(view != null) view.setInput(device);
		return null;
	}

}
