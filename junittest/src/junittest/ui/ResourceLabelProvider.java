package junittest.ui;

import junittest.Activator;
import junittest.ISharedImageConstants;

import org.eclipse.core.resources.IResource;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;

public class ResourceLabelProvider extends LabelProvider {

	@Override
	public Image getImage(Object element) {
		// TODO Auto-generated method stub
//		return Activator.getImageDescriptor("icons/tsuite.gif").createImage();
		return Activator.getDefault().getImageRegistry().get(ISharedImageConstants.TSUITE);
	}

	@Override
	public String getText(Object element) {
		// TODO Auto-generated method stub
		if(element instanceof IResource){
			return ((IResource)element).getName();
		}
		return super.getText(element);
	}

}
