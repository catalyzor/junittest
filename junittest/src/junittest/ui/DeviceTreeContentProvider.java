package junittest.ui;

import junittest.device.DeviceManager;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.broadthinking.btt.device.ExtDeviceException;

public class DeviceTreeContentProvider implements ITreeContentProvider {

	private Object input;
	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		this.input = newInput;
	}

	@Override
	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		if(inputElement instanceof String[]){
			return (String[])inputElement;
		}else{
			return getChildren(inputElement);
		}
//		return null;
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		// TODO Auto-generated method stub
		if(parentElement instanceof String){
			String str = (String)parentElement;
//			if(str.equals(DeviceManager.TYPE_PHONE)){
				if(input instanceof DeviceManager){
					try {
						return ((DeviceManager)input).getDevices(str).toArray();
					} catch (ClassNotFoundException | InstantiationException
							| IllegalAccessException | ExtDeviceException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//			}
		}else if(parentElement instanceof DeviceManager){
			return ((DeviceManager)parentElement).getDeviceTypes();
		}
		return null;
	}

	@Override
	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		return true;
	}

}
