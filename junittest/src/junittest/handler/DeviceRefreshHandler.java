package junittest.handler;

import junittest.device.DeviceManager;
import junittest.resource.ResourceManager;
import junittest.view.DeviceView;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.IHandler;
import org.eclipse.ui.handlers.HandlerUtil;

import com.broadthinking.btt.device.ExtDeviceException;

public class DeviceRefreshHandler extends AbstractHandler implements IHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// TODO Auto-generated method stub
		DeviceView view = (DeviceView) HandlerUtil.getActivePart(event);
		DeviceManager device = ResourceManager.getInstance().getDeviceManager();
		if(device != null){
			try {
				device.init();
			} catch (ClassNotFoundException | InstantiationException
					| IllegalAccessException | ExtDeviceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		view.setInput(device);
		return null;
	}

}
