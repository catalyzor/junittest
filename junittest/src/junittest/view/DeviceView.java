package junittest.view;

import java.util.List;

import junittest.Activator;
import junittest.ISharedImageConstants;
import junittest.device.DeviceManager;
import junittest.device.DeviceManager.Device;
import junittest.ui.DeviceTreeContentProvider;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ColumnPixelData;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.ui.part.ViewPart;

import com.broadthinking.btt.device.ExtDeviceException;

public class DeviceView extends ViewPart {
	private static class ViewerLabelProvider extends LabelProvider implements ITableLabelProvider{
		public Image getImage(Object element) {
			return super.getImage(element);
		}
		public String getText(Object element) {
			return super.getText(element);
		}
		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			if(element instanceof Device){
				Device device = (Device)element;
				switch(columnIndex){
//				case 0:
//					return device.getName();
				case 1:
//					return device.isConncted()?"connected":"disconnect";
					return device.isConncted()?Activator.getDefault().getImageRegistry().get(ISharedImageConstants.SUCCESS_OVR):null;
				}
			}
			return null;
		}
		@Override
		public String getColumnText(Object element, int columnIndex) {
			// TODO Auto-generated method stub
			if(element instanceof String){
				if(columnIndex == 0){
					return ((String)element);
				}
			}else if(element instanceof Device){
				Device device = (Device)element;
				switch(columnIndex){
				case 0:
					return device.getName();
				case 1:
					return device.isConncted()?"connected":"disconnect";
				}
			}
			return null;
		}
	}
//	private static class TreeContentProvider implements ITreeContentProvider {
//		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
//		}
//		public void dispose() {
//		}
//		public Object[] getElements(Object inputElement) {
//			return getChildren(inputElement);
//		}
//		public Object[] getChildren(Object parentElement) {
//			return new Object[] { "item_0", "item_1", "item_2" };
//		}
//		public Object getParent(Object element) {
//			return null;
//		}
//		public boolean hasChildren(Object element) {
//			return getChildren(element).length > 0;
//		}
//	}

	public static final String ID = "junittest.view.DeviceView"; //$NON-NLS-1$
	private TreeViewer treeViewer;

	public DeviceView() {
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
			TreeColumnLayout tcl_composite = new TreeColumnLayout();
			composite.setLayout(tcl_composite);
			{
				treeViewer = new TreeViewer(composite, SWT.BORDER);
				Tree tree = treeViewer.getTree();
				tree.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseDoubleClick(MouseEvent e) {
						IStructuredSelection selection = (IStructuredSelection)treeViewer.getSelection();
						Object obj = selection.getFirstElement();
						if(obj instanceof Device){
							Device device = (Device)obj;
							try {
								if(device.isConncted()){
									device.disconnnect();
								}else{
									List<Device> lstDevice = null;
									try {
										lstDevice = ((DeviceManager)treeViewer.getInput()).getDevices(device.getType());
									} catch (ClassNotFoundException
											| InstantiationException
											| IllegalAccessException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
									int num = 0;
									for(Device dev: lstDevice){
										if(dev.isConncted()) num++;
									}
									if(num < device.getNum()){
										device.connect();
									}else{
										MessageDialog.openInformation(getSite().getShell(), "提示", "无法连接，设备连接数已达上限");
									}
								}
							} catch (ExtDeviceException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							treeViewer.update(device, null);
						}else{
							treeViewer.refresh(obj);
						}
					}
				});
				tree.setHeaderVisible(true);
				tree.setLinesVisible(true);
				{
					TreeColumn treeColumn = new TreeColumn(tree, SWT.NONE);
					tcl_composite.setColumnData(treeColumn, new ColumnPixelData(93, true, true));
					treeColumn.setText("\u8BBE\u5907");
				}
				{
					TreeColumn treeColumn = new TreeColumn(tree, SWT.NONE);
					tcl_composite.setColumnData(treeColumn, new ColumnPixelData(86, true, true));
					treeColumn.setText("\u72B6\u6001");
				}
				treeViewer.setLabelProvider(new ViewerLabelProvider());
				treeViewer.setContentProvider(new DeviceTreeContentProvider());
			}
		}

		createActions();
		initializeToolBar();
		initializeMenu();
	}

	public void setInput(DeviceManager deviceMng){
		treeViewer.setInput(deviceMng);
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
