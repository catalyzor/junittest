package junittest.view;

import junittest.Activator;
import junittest.preferences.PreferenceConstants;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;

public class AdditionLogView extends ViewPart {

	public static final String ID = "junittest.view.AdditionLogView"; //$NON-NLS-1$
	private Composite composite;
	private Text text;
	private Label lblNewLabel;
	private Composite container;

	public AdditionLogView() {
	}

	
	public Composite getContainer() {
		return container;
	}


	public void setContainer(Composite container) {
		this.container = container;
	}


	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
//		setPartName()
		container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		{
			ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
			scrolledComposite.setExpandHorizontal(true);
			scrolledComposite.setExpandVertical(true);
			{
				composite = new Composite(scrolledComposite, SWT.NONE);
				composite.setLayout(new GridLayout(1, false));
				{
					lblNewLabel = new Label(composite, SWT.NONE);
					lblNewLabel.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
					lblNewLabel.setText("New Label");
				}
				{
					text = new Text(composite, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
					text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
				}
			}
			scrolledComposite.setContent(composite);
			scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
		}

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	public void updateTitle(String title){
		setPartName(title);
	}
	
	public void updateCaseName(String casename){
		lblNewLabel.setText(casename);
		lblNewLabel.setToolTipText(lblNewLabel.getText());
	}
	public void appendContent(String content){
		//clear text if text length up to the limit.
		int limit = Activator.getDefault().getPreferenceStore().getInt(PreferenceConstants.VIEW_ADDITIONAL_LOG_CACHE);
		if(text.getText().length() + content.length() >= limit){
			text.setText(Messages.AdditionLogView_0);
		}
		text.append(content + '\n');
	}
	
	public void setContent(String content){
		if(content != null){
			text.setText(content);
		}
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
