package junittest.ui;

import java.io.File;

import junittest.resource.ResourceManager;
import junittest.view.LogView;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
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


	public File getMainLog() {
		return mainLog;
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
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
		view.refreshView();
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
}
