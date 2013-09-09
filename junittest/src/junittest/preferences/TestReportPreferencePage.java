package junittest.preferences;

import junittest.Activator;
import junittest.util.ReportUtils;

import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class TestReportPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage{

	/**
	 * Create the preference page.
	 */
	public TestReportPreferencePage() {
		super(FLAT);
		setDescription(Messages.TestReportPreferencePage_0);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		// Create the field editors
		addField(new ComboFieldEditor(PreferenceConstants.REPORT_FILE_TYPE, Messages.TestReportPreferencePage_1, new String[][]{{ReportUtils.REPORT_TYPE_PDF, ReportUtils.REPORT_TYPE_PDF}, {ReportUtils.REPORT_TYPE_HTML, ReportUtils.REPORT_TYPE_HTML}}, getFieldEditorParent()));
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}

}
