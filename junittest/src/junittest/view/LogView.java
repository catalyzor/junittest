package junittest.view;

import junittest.debug.JUnitRunner;
import junittest.xml.XMLLog;

import org.dom4j.Document;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.junit.runner.notification.RunListener;

public class LogView extends ViewPart {
	private static class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}
		public String getText(Object element) {
			return super.getText(element);
		}
	}
	private static class TreeContentProvider implements ITreeContentProvider {
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}
		public Object[] getChildren(Object parentElement) {
			return new Object[] { "item_0", "item_1", "item_2" };
		}
		public Object getParent(Object element) {
			return null;
		}
		public boolean hasChildren(Object element) {
			return getChildren(element).length > 0;
		}
	}

	public static final String ID = "junittest.view.LogView"; //$NON-NLS-1$
	private TreeViewer treeViewer;

	private RunListener runListener;
	public LogView() {
		runListener = new RunListener(){

			@Override
			public void testRunStarted(Description description)
					throws Exception {
				// TODO Auto-generated method stub
//				super.testRunStarted(description);
				refreshView();
			}

			@Override
			public void testRunFinished(Result result) throws Exception {
				// TODO Auto-generated method stub
				super.testRunFinished(result);
			}

			@Override
			public void testStarted(Description description) throws Exception {
				// TODO Auto-generated method stub
				super.testStarted(description);
			}

			@Override
			public void testFinished(Description description) throws Exception {
				// TODO Auto-generated method stub
				super.testFinished(description);
			}

			@Override
			public void testFailure(Failure failure) throws Exception {
				// TODO Auto-generated method stub
				super.testFailure(failure);
			}

			@Override
			public void testAssumptionFailure(Failure failure) {
				// TODO Auto-generated method stub
				super.testAssumptionFailure(failure);
			}

			@Override
			public void testIgnored(Description description) throws Exception {
				// TODO Auto-generated method stub
				super.testIgnored(description);
			}
			
		};
		JUnitRunner.getInstance().addRunListener(runListener);
	}

	public void refreshView(){

		ProjectView projectView = (ProjectView) getSite().getWorkbenchWindow().getActivePage().findView(ProjectView.ID);
		if(projectView != null){
			XMLLog logger = projectView.getXMLLog();
			Document doc = logger.getDocument();
			treeViewer.setInput(doc);
		}
	}
	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		{
			treeViewer = new TreeViewer(parent, SWT.BORDER);
			Tree tree = treeViewer.getTree();
			treeViewer.setLabelProvider(new ViewerLabelProvider());
			treeViewer.setContentProvider(new TreeContentProvider());
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
