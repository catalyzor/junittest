package junittest.ui;

import junittest.Activator;
import junittest.ISharedImageConstants;
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
				String pic = ISharedImageConstants.TEST;
				switch(en.ordinal()){
				case 3:
					pic = ISharedImageConstants.TESTERR;
				break;
				case 1:
					pic = ISharedImageConstants.TESTOK;
				break;
				case 2:
					pic = ISharedImageConstants.TESTFAIL;
				break;
				default:
					pic = ISharedImageConstants.TEST;
				}
				return Activator.getDefault().getImageRegistry().get(pic);
			}else{
				XMLLog log = XMLLog.log;
				if(log != null){
					String result = (res.getType() == IResource.PROJECT)?log.getTestResult(res.getName()):log.getTestResult(res.getProject().getName() + Messages.StatusLabelDecorator_4 + res.getProjectRelativePath().removeFirstSegments(1).toString());
					String path = ISharedImageConstants.TSUITE;
					switch(result){
					case "ERROR": //$NON-NLS-1$
						path = ISharedImageConstants.TSUITEERROR;
						break;
					case "FAIL": //$NON-NLS-1$
						path = ISharedImageConstants.TSUITEFAIL;
						break;
					case "OK": //$NON-NLS-1$
						path = ISharedImageConstants.TSUITEOK;
						break;
					default:
						path = ISharedImageConstants.TSUITE;
						break;
					}
					return Activator.getDefault().getImageRegistry().get(path);
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
