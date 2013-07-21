package junittest.view;

import junittest.debug.JUnitRunner;
import junittest.xml.XMLLog;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.tree.DefaultText;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

public class LogView extends ViewPart {
	private static class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			return super.getImage(element);
		}
		public String getText(Object element) {
			if(element instanceof Element){
				Element e = (Element) element;
				return e.getName() + " " + e.getText();
			}
			return super.getText(element);
		}
	}
	private static class TreeContentProvider implements ITreeContentProvider {
		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
		public void dispose() {
		}
		public Object[] getElements(Object inputElement) {
			if(inputElement instanceof Document){
				Document doc = (Document) inputElement;
				return new Object[]{doc.getRootElement()};
			}
			return getChildren(inputElement);
		}
		public Object[] getChildren(Object parentElement) {
//			return new Object[] { "item_0", "item_1", "item_2" };
			if(parentElement instanceof Element){
				Element element = (Element) parentElement;
				return element.content().toArray();
			}
			return null;
		}
		public Object getParent(Object element) {
			if(element instanceof Element){
				Element e = (Element) element;
				return e.getParent();
			}
			return null;
		}
		public boolean hasChildren(Object element) {
//			return getChildren(element).length > 0;
			if(element instanceof Element){
				Element e = (Element) element;
				if(e.hasContent() && !DefaultText.class.isInstance(e.content().get(0))){
					return true;
				}
			}
			return false;
		}
	}

	public static final String ID = "junittest.view.LogView"; //$NON-NLS-1$
	private TreeViewer treeViewer;

//	private RunListener runListener;
	private IResourceChangeListener listener;
	public LogView() {
		listener = new IResourceChangeListener() {
			
			@Override
			public void resourceChanged(IResourceChangeEvent event) {
				// TODO Auto-generated method stub
//				Object obj = event.getSource();
//				if(obj instanceof IResource){

					Display.getDefault().asyncExec(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
	
							refreshView();
						}
					});
				}
//			}
		};
//		runListener = new RunListener(){
//
//			@Override
//			public void testRunStarted(Description description)
//					throws Exception {
//				// TODO Auto-generated method stub
////				super.testRunStarted(description);
//				Display.getDefault().asyncExec(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//
//						refreshView();
//					}
//				});
//			}
//
//			@Override
//			public void testRunFinished(Result result) throws Exception {
//				// TODO Auto-generated method stub
////				super.testRunFinished(result);
//				Display.getDefault().asyncExec(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//
//						refreshView();
//					}
//				});
//			}
//
//			@Override
//			public void testStarted(Description description) throws Exception {
//				// TODO Auto-generated method stub
//				super.testStarted(description);
//			}
//
//			@Override
//			public void testFinished(Description description) throws Exception {
//				// TODO Auto-generated method stub
//				super.testFinished(description);
//			}
//
//			@Override
//			public void testFailure(Failure failure) throws Exception {
//				// TODO Auto-generated method stub
//				super.testFailure(failure);
//			}
//
//			@Override
//			public void testAssumptionFailure(Failure failure) {
//				// TODO Auto-generated method stub
//				super.testAssumptionFailure(failure);
//			}
//
//			@Override
//			public void testIgnored(Description description) throws Exception {
//				// TODO Auto-generated method stub
//				super.testIgnored(description);
//			}
//			
//		};
	}

	public void refreshView(){

//		ProjectView projectView = (ProjectView) getSite().getWorkbenchWindow().getActivePage().findView(ProjectView.ID);
//		if(projectView != null){
			XMLLog logger = JUnitRunner.getInstance().getXMLLog();
			if(logger != null){
				Document doc = logger.getDocument();
				treeViewer.setInput(doc);
//			}
//			treeViewer.refresh();
		}
	}
	/**
	 * Create contents of the view part.
	 * @param parent
	 */
	@Override
	public void createPartControl(Composite parent) {
		{
			{
				Composite composite = new Composite(parent, SWT.NONE);
				composite.setLayout(new FillLayout(SWT.HORIZONTAL));
				treeViewer = new TreeViewer(composite, SWT.BORDER);
				Tree tree = treeViewer.getTree();
				tree.setLinesVisible(true);
				treeViewer.setLabelProvider(new ViewerLabelProvider());
				treeViewer.setContentProvider(new TreeContentProvider());
				treeViewer.setAutoExpandLevel(3);
			}
		}

		createActions();
		initializeToolBar();
		initializeMenu();
//		JUnitRunner.getInstance().addRunListener(runListener);
		ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
	}

	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
//		JUnitRunner.getInstance().removeRunListener(runListener);
		ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
		super.dispose();
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
