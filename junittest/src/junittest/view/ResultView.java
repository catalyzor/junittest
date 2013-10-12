package junittest.view;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class ResultView extends ViewPart {

	public static final String ID = "junittest.view.ResultView"; //$NON-NLS-1$
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;

	public ResultView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(2, false));
		{
			Label lblNewLabel = new Label(container, SWT.NONE);
			lblNewLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblNewLabel.setText(Messages.ResultView_0);
		}
		{
			text = new Text(container, SWT.BORDER);
			text.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
		{
			Label lblNewLabel_1 = new Label(container, SWT.NONE);
			lblNewLabel_1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblNewLabel_1.setText(Messages.ResultView_1);
		}
		{
			text_1 = new Text(container, SWT.BORDER);
			text_1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
		{
			Label lblNewLabel_2 = new Label(container, SWT.NONE);
			lblNewLabel_2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblNewLabel_2.setText(Messages.ResultView_2);
		}
		{
			text_2 = new Text(container, SWT.BORDER);
			text_2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}
		{
			Label lblNewLabel_3 = new Label(container, SWT.NONE);
			lblNewLabel_3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
			lblNewLabel_3.setText(Messages.ResultView_3);
		}
		{
			text_3 = new Text(container, SWT.BORDER);
			text_3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		}

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	public void updateResult(int ok, int fail, int error){
		text.setText("" + ok); //$NON-NLS-1$
		text_1.setText("" + fail); //$NON-NLS-1$
		text_2.setText("" + error); //$NON-NLS-1$
		text_3.setText(ok + fail + error + ""); //$NON-NLS-1$
	}
	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Initialize the toolbar.
	 */
	private void initializeToolBar() {
//		IToolBarManager toolbarManager = getViewSite().getActionBars()
//				.getToolBarManager();
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
//		IMenuManager menuManager = getViewSite().getActionBars()
//				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}

}
