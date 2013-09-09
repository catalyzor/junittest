package junittest.intro;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "junittest.intro.messages"; //$NON-NLS-1$
	public static String ApplicationWorkbenchAdvisor_0;
	public static String ApplicationWorkbenchWindowAdvisor_0;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
