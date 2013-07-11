package junittest.ui;

import junittest.Activator;
import junittest.resource.ResourceManager;
import junittest.resource.TestResultEnum;
import junittest.util.Utilities;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class StatusLabelDecorator implements
		ILabelDecorator{

	@Override
	public void addListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		// TODO Auto-generated method stub
		IResource res = (IResource) Platform.getAdapterManager().getAdapter(element, IResource.class);
		if(res != null){
			String classname = Utilities.getFullClassName(res);
			return ResourceManager.getInstance().getMapResult().containsKey(classname);
		}
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image decorateImage(Image image, Object element) {
		// TODO Auto-generated method stub
		IResource res = (IResource) Platform.getAdapterManager().getAdapter(element, IResource.class);
		if(res != null){
			TestResultEnum en = ResourceManager.getInstance().getMapResult().get(Utilities.getFullClassName(res));
			if(en != null){
				String pic = "icons/test.gif";
				switch(en.ordinal()){
				case 2:
					pic = "icons/testerr.gif";
				break;
				case 1:
					pic = "icons/testok.gif";
				break;
				case 3:
					pic = "icons/testfail.gif";
				break;
				default:
					pic = "icons/test.gif";
				}
				return Activator.getImageDescriptor(pic).createImage();
			}
		}
		return null;
	}

	@Override
	public String decorateText(String text, Object element) {
		// TODO Auto-generated method stub
		IResource res = (IResource) Platform.getAdapterManager().getAdapter(element, IResource.class);
		if(res != null){
			TestResultEnum en = ResourceManager.getInstance().getMapResult().get(Utilities.getFullClassName(res));
			if(en != null) return text + "   " + en.name();
		}
		return null;
	}


}
