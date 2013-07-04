package junittest.wizard;

import java.io.File;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

public class LoadWizardPage1 extends WizardPage {
	private Text txtPath;
	private Text txtName;

	/**
	 * Create the wizard.
	 */
	public LoadWizardPage1() {
		super("wizardPage");
		setTitle("Wizard Page title");
		setDescription("Wizard Page description");
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(3, false));
		
		Label lblNewLabel = new Label(container, SWT.NONE);
		lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel.setText("Import file:");
		
		txtPath = new Text(container, SWT.BORDER);
		txtPath.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validation();
			}
		});
		txtPath.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSelect = new Button(container, SWT.NONE);
		btnSelect.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog dialog = new FileDialog(getShell());
				dialog.setFilterExtensions(new String[]{"*.jar", "*.*"});
				String path = dialog.open();
				if(path != null) txtPath.setText(path);
			}
		});
		btnSelect.setText("select");
		
		Label lblNewLabel_1 = new Label(container, SWT.NONE);
		lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblNewLabel_1.setText("Project name:");
		
		txtName = new Text(container, SWT.BORDER);
		txtName.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent e) {
				validation();
			}
		});
		txtName.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		new Label(container, SWT.NONE);
		validation();
	}

	protected void validation() {
		// TODO Auto-generated method stub
		String error = null;
		if(txtPath.getText().trim().equals("")){
			error = "请选择需要导入的文件";
		}else if(!new File(txtPath.getText()).exists()){
			error = txtPath.getText() + "文件不存在";
		}else if(txtName.getText().trim().equals("")){
			error = "请输入工程名称";
		}else if(ResourcesPlugin.getWorkspace().getRoot().getProject(txtName.getText().trim()).exists()){
			error = "工程名已存在，请重新输入";
		}
		setErrorMessage(error);
		setPageComplete(error == null);
	}

	public String getProjectName(){
		return txtName.getText().trim();
	}
	
	public String getJarPath(){
		return txtPath.getText().trim();
	}
}
