package junittest.ui;

import junittest.view.LogView;

import org.dom4j.Document;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class LogViewer {

	protected Shell shell;
	private String name;
	private LogView view;
	private Document doc;

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
		
		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayout(new FillLayout());
//LogView view = new LogView(name);
		view.setDoc(doc);
view.createPartControl(composite);
view.refreshView();
	}
}
