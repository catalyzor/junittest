package junittest.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.jface.preference.ComboFieldEditor;

public class TestReportPreferencePage extends FieldEditorPreferencePage {

	/**
	 * Create the preference page.
	 */
	public TestReportPreferencePage() {
		super(FLAT);
		setDescription("\u62A5\u544A\u914D\u7F6E\u4FE1\u606F");
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		// Create the field editors
		addField(new ComboFieldEditor("id", "\u62A5\u544A\u6587\u4EF6\u683C\u5F0F\uFF1A", new String[][]{{"name_1", "value_1"}, {"name_2", "value_2"}}, getFieldEditorParent()));
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}

}
