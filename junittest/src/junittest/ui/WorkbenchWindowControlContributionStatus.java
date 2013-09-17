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
		String strSum = "测试案例总数：";
		String strOK = "运行成功：";
		String strFail = "运行失败：";
		String strError = "预置条件执行失败：";
		label.setText(strSum + strOK + strFail + strError);
		return label;
	}

}
