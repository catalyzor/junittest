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
		setMessage("\u914D\u7F6E\u65E5\u5FD7\u76F8\u5173\u4FE1\u606F");
//		setTitle("\u65E5\u5FD7\u914D\u7F6E");
		setDescription("\u65E5\u5FD7\u914D\u7F6E");
		setPreferenceStore(Activator.getDefault().getPreferenceStore());
	}

	/**
	 * Create contents of the preference page.
	 */
	@Override
	protected void createFieldEditors() {
		{
			// Create the field editors
			IntegerFieldEditor integerFieldEditor = new IntegerFieldEditor("id", "\u8BBE\u5907\u65E5\u5FD7\u5B57\u7B26\u4E0A\u9650", getFieldEditorParent());
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
