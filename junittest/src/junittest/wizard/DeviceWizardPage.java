package junittest.wizard;

import java.util.Properties;

import junittest.device.DeviceManager;
import junittest.device.DeviceManager.DeviceConfig;
import junittest.resource.ResourceManager;

import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

public class DeviceWizardPage extends WizardPage {

	Properties properties;
	private Combo[] combo;
	private Spinner[] spinner;
	private Button[] btnCheckButton;
	/**
	 * Create the wizard.
	 */
	public DeviceWizardPage() {
		super("设备");
		setTitle("设备");
		setDescription("配置设备信息");
		combo = new Combo[DeviceManager.getDeviceTypes().length];
		spinner = new Spinner[DeviceManager.getDeviceTypes().length];
		btnCheckButton = new Button[DeviceManager.getDeviceTypes().length];
	}

	/**
	 * Create contents of the wizard.
	 * @param parent
	 */
	public void createControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NULL);

		setControl(container);
		container.setLayout(new GridLayout(6, false));
		
		for(int i = 0;i < DeviceManager.getDeviceTypes().length;i ++){
			Label label = new Label(container, SWT.NONE);
			label.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			label.setText("\u8BBE\u5907\uFF1A");

			combo[i] = new Combo(container, SWT.READ_ONLY);
			combo[i].setItems(DeviceManager.getDeviceTypes());
			combo[i].add("", 0);
			combo[i].setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

			Label label_1 = new Label(container, SWT.NONE);
			label_1.setText("\u6570\u91CF\uFF1A");

			spinner[i] = new Spinner(container, SWT.BORDER);

			Label label_2 = new Label(container, SWT.NONE);
			label_2.setText("\u663E\u793A\u65E5\u5FD7\uFF1A");

			btnCheckButton[i] = new Button(container, SWT.CHECK);
		}
	}
	
	public void doPerform(){
		DeviceConfig[] configs = new DeviceConfig[DeviceManager.getDeviceTypes().length];
		for(int i = 0;i<DeviceManager.getDeviceTypes().length;i ++ ){
			if(combo[i].getSelectionIndex() >= 0){
				//			DeviceConfig config = new DeviceConfig(combo.getText(), spinner.getSelection(), btnCheckButton.getSelection());
//				DeviceManager.addDeviceConfig(ResourceManager.getInstance().getProject(), combo.getText(), spinner.getSelection(), btnCheckButton.getSelection());
				configs[i] = new DeviceConfig(combo[i].getText(), spinner[i].getSelection(), btnCheckButton[i].getSelection());
			}
		}
		DeviceManager.addDeviceConfig(ResourceManager.getInstance().getProject(), configs);
	}
}
