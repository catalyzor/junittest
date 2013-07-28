package junittest.view;

import java.util.ArrayList;
import java.util.List;

import junittest.Activator;
import junittest.resource.ResourceManager;
import junittest.util.Utilities;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
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
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.eclipse.ui.part.ViewPart;

public class ProjectView extends ViewPart implements IResourceChangeListener {

	public static final String ID = "junittest.view.ProjectView"; //$NON-NLS-1$
	private CheckboxTreeViewer checkboxTreeViewer;

//	private RunListener runListener;
	private IProject project;
	
	public CheckboxTreeViewer getCheckboxTreeViewer() {
		return checkboxTreeViewer;
	}

	public ProjectView() {
//		runListener = new RunListener(){
//
////			private XMLLog logger;
//			@Override
//			public void testRunFinished(Result result) throws Exception {
//				// TODO Auto-generated method stub
////				super.testRunFinished(result);
//				Display.getDefault().asyncExec(new Runnable() {
//					
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//						checkboxTreeViewer.refresh(true);
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
//			public void testFinished(final Description description) throws Exception {
//				// TODO Auto-generated method stub
////				super.testFinished(description);
//				final XMLLog logger = JUnitRunner.getInstance().getXMLLog();
//				if(logger != null){
//					logger.updateTestResult(project.getName() + "." + description.getClassName(), ResourceManager.getInstance().getMapResult().get(description.getClassName()));
////					Job job = new Job("update test result."){
////
////						@Override
////						protected IStatus run(IProgressMonitor monitor) {
//							// TODO Auto-generated method stub
//							logger.saveToFile();
//							try {
//								ResourceManager.getInstance().getProject().getFolder(ResourceManager.FOLDER_LOG).refreshLocal(IResource.DEPTH_INFINITE, null);
//							} catch (CoreException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
////							return Status.OK_STATUS;
//						}
//
////					};
////					job.setSystem(true);
////					job.schedule();
////				}
//			}
//
//			@Override
//			public void testRunStarted(Description description)
//					throws Exception {
//				// TODO Auto-generated method stub
////				super.testRunStarted(description);
////				Job job = new Job("Output log file.") {
////					
////					@Override
////					protected IStatus run(IProgressMonitor monitor) {
////						// TODO Auto-generated method stub
//////						String name = SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.LONG, SimpleDateFormat.MEDIUM, Locale.CHINA).format(Calendar.getInstance().getTime());
////						
////						return Status.OK_STATUS;
////					}
////				};
////				job.setSystem(true);
////				job.schedule();
//			}
//			
//		};
//		JUnitRunner.getInstance().addRunListener(runListener);
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
//		JUnitRunner.getInstance().removeRunListener(runListener);
		super.dispose();
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
								if(children != null && children.length > 0){
									ArrayList<IResource> lstResource = new ArrayList<>();
									for(int i = 0; i < children.length; i++){
//										if(!children[i].getName().toUpperCase().equals("META-INF")){
										if(!Utilities.isFilted(children[i])){
											lstResource.add(children[i]);
										}
									}
									return lstResource.toArray();
								}
							} catch (CoreException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}else if(folder != null){
						try {
							IResource[] children = folder.members();
							if(children != null && children.length > 0){
								List<IResource> list = new ArrayList<>();
								for(IResource res : children){
									switch(res.getType()){
									case IResource.FILE:
										if(res.getFileExtension() == null && !res.getFileExtension().toUpperCase().equals("CLASS")){
											break;
										}
									case IResource.FOLDER:
										list.add(res);
									}
								}
								return list.toArray();
							}
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
		ResourcesPlugin.getWorkspace().addResourceChangeListener(this, IResourceChangeEvent.PRE_DELETE);
	}

	public void changePrject(Object[] objects){
		checkboxTreeViewer.setInput(objects);
//		checkboxTreeViewer.refresh();
		if(objects != null && objects.length > 0){
			project = (IProject) objects[0];
			checkboxTreeViewer.setSubtreeChecked(objects[0], true);
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

	@Override
	public void resourceChanged(IResourceChangeEvent event) {
		// TODO Auto-generated method stub
		if(checkboxTreeViewer.getInput() != null && event.getType() == IResourceChangeEvent.PRE_DELETE){
			if(event.getResource().equals(((Object[])checkboxTreeViewer.getInput())[0])){
				Display.getDefault().asyncExec(new Runnable() {
					
					@Override
					public void run() {
						// TODO Auto-generated method stub
						changePrject(null);
					}
				});
			}
		}
	}
	
	public List<String> getSelectedCases(){
		List<String> list = new ArrayList<>();
		Object[] objs = checkboxTreeViewer.getCheckedElements();
		for(Object obj: objs){
			IFile file = (IFile) Platform.getAdapterManager().getAdapter(obj, IFile.class);
			if(file != null) list.add(Utilities.getFullClassName(file));
		}
		return list;
	}
	
	public void refreshView(String classname){
		if(classname == null){
			getCheckboxTreeViewer().refresh(true);
			return;
		}
		String path = classname.replaceAll("\\.", "/") + "." + ResourceManager.SUFFIX_CLASS;
		IResource res = ResourceManager.getInstance().getProject().getFolder(ResourceManager.FOLDER_CASE).findMember(path);
		refreshView(res);
	}
	public void refreshView(IResource res){
		if(res != null){
			getCheckboxTreeViewer().update(res, null);
			if(res.getParent() != null && res.getType() != IResource.PROJECT){
				refreshView(res.getParent());
			}
		}
	}
}
