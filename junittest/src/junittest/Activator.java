package junittest;

import junittest.userlog.NameEnum;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "junittest"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;
	
	/**
	 * The constructor
	 */
	public Activator() {
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		// TODO Auto-generated method stub
//		super.initializeImageRegistry(reg);
		String path = "icons/";
		reg.put(ISharedImageConstants.TEST, getImageDescriptor(path + ISharedImageConstants.TEST));
		reg.put(ISharedImageConstants.TESTERR, getImageDescriptor(path + ISharedImageConstants.TESTERR));
		reg.put(ISharedImageConstants.TESTFAIL, getImageDescriptor(path + ISharedImageConstants.TESTFAIL));
		reg.put(ISharedImageConstants.TESTOK, getImageDescriptor(path + ISharedImageConstants.TESTOK));
		reg.put(ISharedImageConstants.TSUITE, getImageDescriptor(path + ISharedImageConstants.TSUITE));
		reg.put(ISharedImageConstants.TSUITEERROR, getImageDescriptor(path + ISharedImageConstants.TSUITEERROR));
		reg.put(ISharedImageConstants.TSUITEFAIL, getImageDescriptor(path + ISharedImageConstants.TSUITEFAIL));
		reg.put(ISharedImageConstants.TSUITEOK, getImageDescriptor(path + ISharedImageConstants.TSUITEOK));
		
		for(String str : NameEnum.TAGS){
			reg.put(str, getImageDescriptor(path + str.toLowerCase() + ".png"));
		}
	}
	
//	public static ImageDescriptor getImageFromRegistry(String path){
//		getDefault().getImageRegistry()
//	}
}
