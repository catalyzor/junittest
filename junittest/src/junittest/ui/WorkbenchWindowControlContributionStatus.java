package junittest.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.menus.WorkbenchWindowControlContribution;

public class WorkbenchWindowControlContributionStatus extends
		WorkbenchWindowControlContribution {

	public WorkbenchWindowControlContributionStatus() {
		// TODO Auto-generated constructor stub
	}

	public WorkbenchWindowControlContributionStatus(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createControl(Composite parent) {
		Label label = new Label(parent, SWT.NONE);
		String strSum = Messages.WorkbenchWindowControlContributionStatus_0;
		String strOK = Messages.WorkbenchWindowControlContributionStatus_1;
		String strFail = Messages.WorkbenchWindowControlContributionStatus_2;
		String strError = Messages.WorkbenchWindowControlContributionStatus_3;
		label.setText(strSum + strOK + strFail + strError);
		return label;
	}

}
