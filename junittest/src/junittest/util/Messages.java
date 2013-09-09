package junittest.util;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "junittest.util.messages"; //$NON-NLS-1$
	public static String ReportUtils_0;
	public static String ReportUtils_1;
	public static String ReportUtils_2;
	public static String ReportUtils_3;
	public static String Utilities_0;
	public static String Utilities_1;
	public static String Utilities_2;
	public static String Utilities_3;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
