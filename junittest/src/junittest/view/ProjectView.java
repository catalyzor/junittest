package junittest.view;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

public class ProjectView extends ViewPart {

	public static final String ID = "junittest.view.ProjectView"; //$NON-NLS-1$
	private CheckboxTreeViewer checkboxTreeViewer;

	public ProjectView() {
	}

	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new FillLayout(SWT.HORIZONTAL));
		{
			checkboxTreeViewer = new CheckboxTreeViewer(container, SWT.BORDER);
			Tree tree = checkboxTreeViewer.getTree();
			getSite().setSelectionProvider(checkboxTreeViewer);
//			checkboxTreeViewer.setContentProvider(new ArrayContentProvider());
			checkboxTreeViewer.setLabelProvider(new WorkbenchLabelProvider());
//			checkboxTreeViewer.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		}

		createActions();
		initializeToolBar();
		initializeMenu();
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
		IToolBarManager toolbarManager = getViewSite().getActionBars()
				.getToolBarManager();
//		toolbarManager.add(ActionFactory.REFRESH.create(getSite().getWorkbenchWindow()));
	}

	/**
	 * Initialize the menu.
	 */
	private void initializeMenu() {
		IMenuManager menuManager = getViewSite().getActionBars()
				.getMenuManager();
	}

	@Override
	public void setFocus() {
		// Set the focus
	}
}
