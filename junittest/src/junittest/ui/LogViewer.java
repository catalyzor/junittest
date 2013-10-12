package junittest.ui;

import java.io.File;

import junittest.view.LogView;

import org.dom4j.Document;
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
	    	  
	      }
	      // start the event loop. We stop when the user has done
	      // something to dispose our window.
	      while (!shell.isDisposed ()) {
	         if (!display.readAndDispatch ())
	            display.sleep ();
	      }
	      display.dispose ();

	}
}
