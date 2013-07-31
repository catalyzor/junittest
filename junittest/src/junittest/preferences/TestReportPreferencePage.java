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
		setDescription("\u62A5\u544A\u914D\u7F6E\u4FE1\u606F");
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		// Create the field editors
		addField(new ComboFieldEditor(PreferenceConstants.REPORT_FILE_TYPE, "\u62A5\u544A\u6587\u4EF6\u683C\u5F0F\uFF1A", new String[][]{{ReportUtils.REPORT_TYPE_PDF, ReportUtils.REPORT_TYPE_PDF}, {ReportUtils.REPORT_TYPE_HTML, ReportUtils.REPORT_TYPE_HTML}}, getFieldEditorParent()));
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}

}
