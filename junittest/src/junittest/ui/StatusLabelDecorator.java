package junittest.ui;

import junittest.Activator;
import junittest.debug.JUnitRunner;
import junittest.resource.ResourceManager;
import junittest.resource.TestResultEnum;
import junittest.util.Utilities;
import junittest.xml.XMLLog;

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
				case 3:
					pic = "icons/testerr.gif";
				break;
				case 1:
					pic = "icons/testok.gif";
				break;
				case 2:
					pic = "icons/testfail.gif";
				break;
				default:
					pic = "icons/test.gif";
				}
				return Activator.getImageDescriptor(pic).createImage();
			}else{
				XMLLog log = JUnitRunner.getInstance().getXMLLog();
				if(log != null){
					String result = (res.getType() == IResource.PROJECT)?log.getTestResult("/" + res.getName()):log.getTestResult("/" + res.getProject().getName() + "/" + res.getProjectRelativePath().removeFirstSegments(1).toString());
					String path = null;
					switch(result){
					case "ERROR":
						path = "icons/tsuiteerror.gif";
						break;
					case "FAIL":
						path = "icons/tsuitefail.gif";
						break;
					case "OK":
						path = "icons/tsuiteok.gif";
						break;
					default:
						path = "icons/tsuite.gif";
						break;
					}
					return Activator.getImageDescriptor(path).createImage();
				}
			}
		}
		return null;
	}

	@Override
	public String decorateText(String text, Object element) {
		// TODO Auto-generated method stub
//		IResource res = (IResource) Platform.getAdapterManager().getAdapter(element, IResource.class);
//		if(res != null){
//			TestResultEnum en = ResourceManager.getInstance().getMapResult().get(Utilities.getFullClassName(res));
//			if(en != null) return text + "   " + en.name();
//		}
		return text;
	}


}
