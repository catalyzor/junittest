package junittest.ui;

import junittest.Activator;
import junittest.resource.ResourceManager;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.Platform;
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
        if (Platform.getAdapterManager().getAdapter(element, IContainer.class) != null) {
//            return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
        	return Activator.getImageDescriptor("icons/tsuite.gif").createImage();
        } else {
//            return Activator.getDefault().getEditorRegistry()
//                    .getImageDescriptor(decorateText("", element));
//        	return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
			//TODO: what are the implications for content types?  Should I guess?
        	return Activator.getImageDescriptor("icons/test.gif").createImage();
        }
    }

	@Override
	public String decorateText(String text, Object element) {
		// TODO Auto-generated method stub
		if(element instanceof IResource){
			String name = ((IResource)element).getName();
			if(name.endsWith("." + ResourceManager.SUFFIX_CLASS)){
				name = name.substring(0, name.length() - ResourceManager.SUFFIX_CLASS.length() - 1);
			}
			return name;
		}
		return null;
	}

}
