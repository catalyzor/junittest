package junittest.view;

import junittest.resource.ResourceManager;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.model.BaseWorkbenchContentProvider;
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
			checkboxTreeViewer.setContentProvider(new ITreeContentProvider() {
				
				@Override
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void dispose() {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public boolean hasChildren(Object element) {
					// TODO Auto-generated method stub
//					IContainer folder = (IContainer) Platform.getAdapterManager().getAdapter(element, IContainer.class);
//					if(folder != null){
//						try {
//							IResource[] ress = folder.members();
//							if(ress != null && ress.length > 0) return true;
//						} catch (CoreException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//					}
					return new BaseWorkbenchContentProvider().hasChildren(element);
				}
				
				@Override
				public Object getParent(Object element) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Object[] getElements(Object inputElement) {
					// TODO Auto-generated method stub
					return new Object[]{inputElement};
				}
				
				@Override
				public Object[] getChildren(Object parentElement) {
					// TODO Auto-generated method stub
					if(parentElement instanceof IProject){
						return new BaseWorkbenchContentProvider().getChildren(((IProject)parentElement).getFolder(ResourceManager.FOLDER_CASE));
					}
					return new BaseWorkbenchContentProvider().getChildren(parentElement);
				}
			});
			checkboxTreeViewer.setLabelProvider(new WorkbenchLabelProvider());
//			checkboxTreeViewer.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		}

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	public void changePrject(IProject project){
		checkboxTreeViewer.setInput(project);
		checkboxTreeViewer.refresh();
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
