package junittest.ui;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

public class ResourceLabelDecorator implements ILabelDecorator {

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
		return true;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public Image decorateImage(Image image, Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String decorateText(String text, Object element) {
		// TODO Auto-generated method stub
		if(element instanceof IResource){
			return ((IResource)element).getName();
		}
		return null;
	}

}
