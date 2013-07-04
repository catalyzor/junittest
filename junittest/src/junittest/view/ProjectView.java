package junittest.view;

import junittest.Activator;
import junittest.resource.ResourceManager;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.DecoratingLabelProvider;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
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
			checkboxTreeViewer.addCheckStateListener(new ICheckStateListener() {
				public void checkStateChanged(CheckStateChangedEvent event) {
					checkboxTreeViewer.setSubtreeChecked(event.getElement(), event.getChecked());
				}
			});
			Tree tree = checkboxTreeViewer.getTree();
			getSite().setSelectionProvider(checkboxTreeViewer);
//			checkboxTreeViewer.setContentProvider(new BaseWorkbenchContentProvider());
			checkboxTreeViewer.setContentProvider(new ITreeContentProvider() {
				
				@Override
				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
					// TODO Auto-generated method stub
					viewer.refresh();
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
					IProject project = (IProject) Platform.getAdapterManager().getAdapter(element, IProject.class);
					IFolder folder = (IFolder) Platform.getAdapterManager().getAdapter(element, IFolder.class);
					if(project != null){
						IFolder cases = project.getFolder(ResourceManager.FOLDER_CASE);
						if(cases.exists()){
							try {
								IResource[] children = cases.members();
								return (children != null) && (children.length > 0);
							} catch (CoreException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}else if(folder != null){
						try {
							IResource[] children = folder.members();
							return (children != null) && (children.length > 0);
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return false;
				}
				
				@Override
				public Object getParent(Object element) {
					// TODO Auto-generated method stub
					return null;
				}
				
				@Override
				public Object[] getElements(Object inputElement) {
					// TODO Auto-generated method stub

					if(inputElement instanceof Object[]){
						return (Object[]) inputElement;
					}else{
						return getChildren(inputElement);
					}
//					IProject project = (IProject) Platform.getAdapterManager().getAdapter(inputElement, IProject.class);
//					if(project != null){
//						return new Object[]{inputElement};
//					}else{
//						return getChildren(inputElement);
//					}
				}
				
				@Override
				public Object[] getChildren(Object parentElement) {
					// TODO Auto-generated method stub
//					if(parentElement instanceof IProject){
//						return new BaseWorkbenchContentProvider().getChildren(((IProject)parentElement).getFolder(ResourceManager.FOLDER_CASE));
//					}
//					return new BaseWorkbenchContentProvider().getChildren(parentElement);
					IProject project = (IProject) Platform.getAdapterManager().getAdapter(parentElement, IProject.class);
					IFolder folder = (IFolder) Platform.getAdapterManager().getAdapter(parentElement, IFolder.class);
					if(project != null){
						IFolder cases = project.getFolder(ResourceManager.FOLDER_CASE);
						if(cases.exists()){
							try {
								IResource[] children = cases.members();
								return children;
							} catch (CoreException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}else if(folder != null){
						try {
							IResource[] children = folder.members();
							return children;
						} catch (CoreException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					return null;
				}
			});
			checkboxTreeViewer.setLabelProvider(new DecoratingLabelProvider(
	                new WorkbenchLabelProvider(), Activator.getDefault().getWorkbench()
                    .getDecoratorManager().getLabelDecorator()));
//			checkboxTreeViewer.setInput(ResourcesPlugin.getWorkspace().getRoot().getProjects());
		}

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	public void changePrject(Object[] objects){
		checkboxTreeViewer.setInput(objects);
//		checkboxTreeViewer.refresh();
		if(objects != null && objects.length > 0)
			checkboxTreeViewer.setSubtreeChecked(objects[0], true);
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
