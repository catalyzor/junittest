package junittest.view;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import junittest.Activator;
import junittest.resource.ResourceManager;
import junittest.resource.TestResultConstants;
import junittest.resource.TestResultEnum;
import junittest.xml.XMLLog;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;

public class LogHistoryView extends ViewPart {
	private class TableLabelProvider extends LabelProvider implements ITableLabelProvider {
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
		public String getColumnText(Object element, int columnIndex) {
			IFile file = (IFile) Platform.getAdapterManager().getAdapter(element, IFile.class);
			if(file != null){
				switch (columnIndex) {
				case 0:
					String name = file.getName().substring(0, file.getName().length() - ResourceManager.SUFFIX_CLASS.length() + 1);
					return SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM,SimpleDateFormat.MEDIUM,Locale.CHINA).format(new Date(Long.parseLong(name)));
				case 1:
					if(XMLLog.log != null && file.getName().startsWith(XMLLog.log.getTime())){
						return "正在执行";
					}
					return "完成";
				case 2:
					try {
//						String str = XMLLog.getLogTestResult(file.getLocation().toOSString());
						String str = XMLLog.getLogTestResult(file);
						if(str != null && !str.trim().equals("")){
							str = TestResultConstants.RESULT_NAMES[TestResultEnum.valueOf(str).ordinal()];
						}
						return str;
//					} catch (DocumentException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
					}catch (Exception e){
						e.printStackTrace();
					}
				default:
					break;
				}
			}
			return element.toString();
		}
	}

	public static final String ID = "junittest.view.LogHistoryView"; //$NON-NLS-1$
	private Table table;
	private TableViewer tableViewer;

	public TableViewer getTableViewer(){
		return tableViewer;
	}
//	private RunListener runListener;
//	private IResourceChangeListener resListener;
	public LogHistoryView() {
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
//		};
//		resListener = new IResourceChangeListener() {
//			
//			@Override
//			public void resourceChanged(IResourceChangeEvent event) {
//				// TODO Auto-generated method stub
//				if(event.getType() == IResourceChangeEvent.PRE_DELETE){
//					refreshView();
//				}
//			}
//		};
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
//		ResourcesPlugin.getWorkspace().removeResourceChangeListener(resListener);
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
			Composite composite = new Composite(container, SWT.NONE);
			TableColumnLayout tcl_composite = new TableColumnLayout();
			composite.setLayout(tcl_composite);
			{
				tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION);
				table = tableViewer.getTable();
				table.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						doDoubleClick();
					}
				});
				table.setHeaderVisible(true);
				table.setLinesVisible(true);
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
					TableColumn tblclmnNewColumn = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnNewColumn, new ColumnPixelData(150, true, true));
					tblclmnNewColumn.setText("\u6267\u884C\u65F6\u95F4");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
					TableColumn tblclmnNewColumn_1 = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnNewColumn_1, new ColumnPixelData(150, true, true));
					tblclmnNewColumn_1.setText("\u72B6\u6001");
				}
				{
					TableViewerColumn tableViewerColumn = new TableViewerColumn(tableViewer, SWT.NONE);
					TableColumn tblclmnNewColumn_2 = tableViewerColumn.getColumn();
					tcl_composite.setColumnData(tblclmnNewColumn_2, new ColumnPixelData(150, true, true));
					tblclmnNewColumn_2.setText("\u7ED3\u679C");
				}
				tableViewer.setLabelProvider(new TableLabelProvider());
				tableViewer.setContentProvider(new ArrayContentProvider());
				refreshView(null);
			}
		}

		createActions();
		initializeToolBar();
		initializeMenu();
//		getSite().setSelectionProvider(tableViewer);
		MenuManager mm = new MenuManager();
		Menu menu = mm.createContextMenu(table);
		table.setMenu(menu);
		getSite().registerContextMenu(mm, tableViewer);
//		JUnitRunner.getInstance().addRunListener(runListener);
//		ResourcesPlugin.getWorkspace().addResourceChangeListener(resListener);
	}

	protected void doDoubleClick() {
		// TODO Auto-generated method stub
//		IStructuredSelection selection = (IStructuredSelection) tableViewer.getSelection();
//		if(!selection.isEmpty()){
//			IResource res = (IResource) selection.getFirstElement();
//			
//		}
		IHandlerService service = (IHandlerService) getSite().getService(IHandlerService.class);
		if(service != null){
			try {
				service.executeCommand("junittest.command.viewlog", null);
			} catch (ExecutionException | NotDefinedException
					| NotEnabledException | NotHandledException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ErrorDialog.openError(getSite().getShell(), "错误", "打开日志失败", new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
			}
		}
	}

	public void refreshView(IResource res){
		if(res == null){
			IProject project = ResourceManager.getInstance().getProject();
			if(project != null){
				try {
					tableViewer.setInput(project.getFolder(ResourceManager.FOLDER_LOG).members());
					tableViewer.refresh();
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else{
			tableViewer.update(res, null);
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
