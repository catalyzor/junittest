package junittest.view;

import java.util.List;

import junittest.Activator;
import junittest.ISharedImageConstants;
import junittest.debug.JUnitRunner;
import junittest.xml.XMLLog;

import org.dom4j.Document;
import org.dom4j.Element;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.part.ViewPart;

public class LogView extends ViewPart {
	private static class ViewerLabelProvider extends LabelProvider {
		public Image getImage(Object element) {
			if(element instanceof Element){
				Element el = (Element) element;
				String name = el.getName();
				String path = ISharedImageConstants.TEST;
				if(name.endsWith(XMLLog.NODE_CASE)){
					switch (el.elementTextTrim(XMLLog.NODE_VERDICT)) {
					case "ERROR":
						path = ISharedImageConstants.TESTERR;
						break;
					case "FAIL":
						path = ISharedImageConstants.TESTFAIL;
						break;
					case "OK":
						path = ISharedImageConstants.TESTOK;
						break;
					default:
						path = ISharedImageConstants.TEST;
						break;
					}
				}else{
					if(el.element(XMLLog.NODE_VERDICT) != null){
						switch (el.elementTextTrim(XMLLog.NODE_VERDICT)) {
						case "ERROR":
							path = ISharedImageConstants.TSUITEERROR;
							break;
						case "FAIL":
							path = ISharedImageConstants.TSUITEFAIL;
							break;
						case "OK":
							path = ISharedImageConstants.TSUITEOK;
							break;
						default:
							path = ISharedImageConstants.TSUITE;
							break;
						}
					}
				}
				return Activator.getDefault().getImageRegistry().get(path);
			}
			return super.getImage(element);
		}
		public String getText(Object element) {
			if(element instanceof Element){
				Element e = (Element) element;
				if(e.selectSingleNode(XMLLog.NODE_NAME) != null){
					String name = e.elementTextTrim(XMLLog.NODE_NAME);
					return name;
				}else if(e.getName().equals(XMLLog.NODE_LOG)){
					return e.getName();
				}else{
					return e.getTextTrim();
				}
//				if(name.endsWith("." + ResourceManager.SUFFIX_CLASS)){
//					name = name.substring(0, name.length() - ResourceManager.SUFFIX_CLASS.length() - 1);
//				}
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
//				List<Element> list = new ArrayList<>();
//				for(int i = 0;i < element.content().size();i ++){
//					if(!DefaultText.class.isInstance(element.content().get(i))){
//						list.add((Element) element.content().get(i));
//					}
//				}
//				List<Element> list = element.selectNodes(XMLLog.NODE_SUITE + " | " + XMLLog.NODE_CASE + " | ");
				if(element.getName().equals(XMLLog.NODE_ROOT) || element.getName().equals(XMLLog.NODE_SUITE)){
					List<Element> list = element.selectNodes(XMLLog.NODE_SUITE + " | " + XMLLog.NODE_CASE + " | " + XMLLog.NODE_PROPS + " | " + XMLLog.NODE_LOG);
					return list.toArray();
				}else if(element.getName().equals(XMLLog.NODE_CASE)){
					List<Element> list = element.selectNodes(XMLLog.NODE_PROPS + " | " + XMLLog.NODE_LOG);
					return list.toArray();
				}else if(element.getName().equals(XMLLog.NODE_PROPS)){
					return element.content().toArray();
				}else if(element.getName().equals(XMLLog.NODE_LOG)){
					return element.content().toArray();
				}
//				return list.toArray();
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
//				if(e.hasContent() && !DefaultText.class.isInstance(e.content().get(0))){
//					return true;
//				}
//				List<Element> list = e.selectNodes(XMLLog.NODE_SUITE + " | " + XMLLog.NODE_CASE + " | ");
//				return !list.isEmpty();
				if(e.getName().equals(XMLLog.NODE_ROOT) || e.getName().equals(XMLLog.NODE_SUITE)){
					List<Element> list = e.selectNodes(XMLLog.NODE_SUITE + " | " + XMLLog.NODE_CASE + " | " + XMLLog.NODE_PROPS + " | " + XMLLog.NODE_LOG);
					return !list.isEmpty();
				}else if(e.getName().equals(XMLLog.NODE_CASE)){
					List<Element> list = e.selectNodes(XMLLog.NODE_PROPS + " | " + XMLLog.NODE_LOG);
					return !list.isEmpty();
				}else if(e.getName().equals(XMLLog.NODE_PROPS)){
					return e.hasContent();
				}else if(e.getName().equals(XMLLog.NODE_LOG)){
					return e.hasContent();
				}
			}
			return false;
		}
	}

	public static final String ID = "junittest.view.LogView"; //$NON-NLS-1$
	private TreeViewer treeViewer;
	private Document doc;

//	private RunListener runListener;
//	private IResourceChangeListener listener;
	public LogView() {
//		listener = new IResourceChangeListener() {
//			
//			@Override
//			public void resourceChanged(IResourceChangeEvent event) {
//				// TODO Auto-generated method stub
////				Object obj = event.getSource();
////				if(obj instanceof IResource){
//
//					Display.getDefault().asyncExec(new Runnable() {
//						
//						@Override
//						public void run() {
//							// TODO Auto-generated method stub
//	
//							setDoc(null);
//							refreshView();
//						}
//					});
//				}
////			}
//		};
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
	
	public LogView(String name){
		
	}

	public Document getDoc() {
		if(doc == null){
			XMLLog logger = JUnitRunner.getInstance().getXMLLog();
			if(logger != null){
				doc = logger.getDocument();
			}
		}
		return doc;
	}

	public void setDoc(Document doc) {
		this.doc = doc;
	}

	public void refreshView(){

//		ProjectView projectView = (ProjectView) getSite().getWorkbenchWindow().getActivePage().findView(ProjectView.ID);
//		if(projectView != null){
			
				treeViewer.setInput(getDoc());
//			}
//			treeViewer.refresh();
//		}
	}
	public void refreshNode(Element element){
		treeViewer.refresh(element);
		treeViewer.expandToLevel(element, 1);
//		treeViewer.setSelection(new StructuredSelection(element));
		updateNodeParent(element.getParent());
	}
	public void updateNodeParent(Element element){
		if(element == null) return;
		treeViewer.update(element, null);
		updateNodeParent(element.getParent());
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

//		createActions();
//		initializeToolBar();
//		initializeMenu();
//		JUnitRunner.getInstance().addRunListener(runListener);
//		if(listener != null)ResourcesPlugin.getWorkspace().addResourceChangeListener(listener);
	}

	
	@Override
	public void dispose() {
		// TODO Auto-generated method stub
//		JUnitRunner.getInstance().removeRunListener(runListener);
//		ResourcesPlugin.getWorkspace().removeResourceChangeListener(listener);
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
