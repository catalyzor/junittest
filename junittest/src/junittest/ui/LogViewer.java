package junittest.ui;

import java.io.File;
import java.util.Arrays;

import junittest.Activator;
import junittest.ISharedImageConstants;
import junittest.resource.ResourceManager;
import junittest.userlog.NameEnum;
import junittest.view.LogView;
import junittest.view.Messages;
import junittest.xml.XMLLog;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class LogViewer {

	protected Shell shell;
	private String name;
	private LogView view;
	private Document doc;
	private File mainLog;

//	/**
//	 * Launch the application.
//	 * @param args
//	 */
//	public static void main(String[] args) {
//		try {
//			LogViewer window = new LogViewer();
//			window.open();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}


	public LogView getView() {
		return view;
	}

	public void setView(LogView view) {
		this.view = view;
	}

	public File getMainLog() {
		return mainLog;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		view.refreshView();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	public void open(Display display){
		if(display == null){
			display = Display.getDefault();
		}
		createContents();
		view.getTreeViewer().setLabelProvider(new ViewerLabelProvider());
		view.refreshView();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	public LogViewer(String name, Document doc){
		this.name = name;
		this.view = new LogView(name);
		this.doc = doc;
	}
	/**
	 * Create contents of the window.
	 * @wbp.parser.entryPoint
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		//		shell.setText("SWT Application");
		shell.setText(name);
		shell.setLayout(new FillLayout(SWT.HORIZONTAL));

		final Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout());
		//LogView view = new LogView(name);
		view.setDoc(doc);
		view.setMainLog(getMainLog());
		view.createPartControl(composite);
//		
//		Group grpLog = new Group(shell, SWT.NONE);
//		grpLog.setText("log");
//		grpLog.setLayout(new FillLayout(SWT.HORIZONTAL));
//		
//		Text text = new Text(grpLog, SWT.BORDER);
//		view.refreshView();
//		TreeViewer treeviewer = view.getTreeViewer();
//		treeviewer.remove
	}

	public void setMainLog(File res) {
		// TODO Auto-generated method stub
		this.mainLog = res;
	}
	
	public static void main(String[] args) {
	      Display display = new Display ();
	      Shell shell = new Shell (display);
//	      shell.open ();
	      DirectoryDialog dialog = new DirectoryDialog(shell);
	      dialog.setText("打开");
	      dialog.setMessage("选择要打开的日志文件夹");
	      String doc = dialog.open();
	      if(doc != null){
	    	  File folder = new File(doc);
	    	  if(folder.exists()){
	    		  File file = new File(doc + File.separator + folder.getName() + "." + ResourceManager.SUFFIX_LOG);
	    		  if(file.exists()){
	    			  try {
						LogViewer viewer = new LogViewer(file.getName(), new SAXReader().read(file));
						viewer.setMainLog(file);
						viewer.open(display);
//						viewer.getView().getTreeViewer().setLabelProvider(new ViewerLabelProvider());
					} catch (DocumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	    		  }else{
	    			  MessageDialog.openError(shell, "错误", "没找到日志文件");
	    		  }
	    	  }
	      }
	      // start the event loop. We stop when the user has done
	      // something to dispose our window.
//	      while (!shell.isDisposed ()) {
//	         if (!display.readAndDispatch ())
//	            display.sleep ();
//	      }
//	      display.dispose ();

	}

	private static class ViewerLabelProvider extends LabelProvider {
		private String[] tags = Arrays.copyOf(NameEnum.TAGS, NameEnum.TAGS.length);
		public ViewerLabelProvider(){
			super();
			Arrays.sort(tags);
		}
		public Image getImage(Object element) {
			if(element instanceof Element){
				Element el = (Element) element;
				String name = el.getName();
				String path = ISharedImageConstants.TEST;
				if(name.endsWith(XMLLog.NODE_CASE)){
					switch (el.elementTextTrim(XMLLog.NODE_VERDICT)) {
					case "ERROR": //$NON-NLS-1$
						path = ISharedImageConstants.TESTERR;
						break;
					case "FAIL": //$NON-NLS-1$
//						path = ISharedImageConstants.TESTFAIL;
						path = NameEnum.TAG_RUNFAIL.toLowerCase() + ".png";
						break;
					case "OK": //$NON-NLS-1$
//						path = ISharedImageConstants.TESTOK;
						path = NameEnum.TAG_RUNSUCCESS.toLowerCase() + ".png";
						break;
					default:
						path = ISharedImageConstants.TEST;
						break;
					}
				}else if(name.startsWith(XMLLog.NODE_ROOT) || name.startsWith(XMLLog.NODE_SUITE)){
					if(el.element(XMLLog.NODE_VERDICT) != null){
						switch (el.elementTextTrim(XMLLog.NODE_VERDICT)) {
						case "ERROR": //$NON-NLS-1$
							path = ISharedImageConstants.TSUITEERROR;
							break;
						case "FAIL": //$NON-NLS-1$
							path = ISharedImageConstants.TSUITEFAIL;
							break;
						case "OK": //$NON-NLS-1$
							path = ISharedImageConstants.TSUITEOK;
							break;
						default:
							path = ISharedImageConstants.TSUITE;
							break;
						}
					}
				}else{
					if(Arrays.binarySearch(tags, el.getName()) >= 0){
						path = el.getName().toLowerCase() + ".png";
					}else{
						path = NameEnum.TAG_DEFAULT.toLowerCase() + ".png";
					}
				}
				return new Image(Display.getDefault(), "icons/" + path);
			}
			return super.getImage(element);
		}
		public String getText(Object element) {
			if(element instanceof Element){
				Element e = (Element) element;
				if(e.selectSingleNode(XMLLog.NODE_NAME) != null){
					String name = e.elementTextTrim(XMLLog.NODE_NAME);
					return name;
				}else if(e.getParent().getName().equals(XMLLog.NODE_PROPS)){
					return e.getName() + Messages.LogView_14 + e.getTextTrim();
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
}
