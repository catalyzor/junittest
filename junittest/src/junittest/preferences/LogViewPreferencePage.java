package junittest.preferences;

import junittest.Activator;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

public class LogViewPreferencePage extends FieldEditorPreferencePage implements
		IWorkbenchPreferencePage {

	/**
	 * Create the preference page.
	 */
	public LogViewPreferencePage() {
		super(FLAT);
		setMessage(Messages.LogViewPreferencePage_0);
//		setTitle("\u65E5\u5FD7\u914D\u7F6E");
		setDescription(Messages.LogViewPreferencePage_1);
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		{
			// Create the field editors
			IntegerFieldEditor integerFieldEditor = new IntegerFieldEditor(PreferenceConstants.VIEW_ADDITIONAL_LOG_CACHE, Messages.LogViewPreferencePage_3, getFieldEditorParent());
			integerFieldEditor.setValidRange(1, Text.LIMIT);
			addField(integerFieldEditor);
		}
	}

	/**
	 * Initialize the preference page.
	 */
	public void init(IWorkbench workbench) {
		// Initialize the preference page
	}

}
