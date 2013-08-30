package junittest.preferences;

import junittest.Activator;
import junittest.util.ReportUtils;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Text;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#initializeDefaultPreferences()
	 */
	public void initializeDefaultPreferences() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
//		store.setDefault(PreferenceConstants.P_BOOLEAN, true);
//		store.setDefault(PreferenceConstants.P_CHOICE, "choice2");
//		store.setDefault(PreferenceConstants.P_STRING,
//				"Default value");
		store.setDefault(PreferenceConstants.RUN_TIMEOUT, 5000);
		store.setDefault(PreferenceConstants.REPORT_FILE_TYPE, ReportUtils.REPORT_TYPE_PDF);
		store.setDefault(PreferenceConstants.REPORT_LOG_MAIN, true);
		store.setDefault(PreferenceConstants.REPORT_LOG_ADDITIONAL, true);
		store.setDefault(PreferenceConstants.REPORT_STANDARD_DEFINITION, true);
		store.setDefault(PreferenceConstants.REPORT_VERDICT_ERROR, true);
		store.setDefault(PreferenceConstants.REPORT_VERDICT_FAIL, true);
		store.setDefault(PreferenceConstants.REPORT_VERDICT_IGNORE, true);
		store.setDefault(PreferenceConstants.REPORT_VERDICT_OK, true);
		store.setDefault(PreferenceConstants.VIEW_ADDITIONAL_LOG_CACHE, 5000);
	}

}
