package junittest.userlog;

import junittest.view.LogView;

import org.dom4j.Element;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class UserLogUtil {

	private static Element currentNode;

	protected static void refreshLogView(final Element el) {
		// TODO Auto-generated method stub
		Display.getDefault().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				LogView view = (LogView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().findView(LogView.ID);
//				view.setDoc(el.getDocument());
				if(el == null){
					view.refreshView();
				}else{
					view.refreshNode(el);
				}
			}
		});
	}
	public static Object p_LevelLog(Object parent, String name, String value){
		Element el = null;
		if(parent == null){
//			return JUnitRunner.getInstance().getXMLLog().addElement(currentNode, name, value);
//		}else if(parent instanceof String){
//			return JUnitRunner.getInstance().getXMLLog().addElement((String)parent, name, value);
//		}else if(parent instanceof Element){
//			return JUnitRunner.getInstance().getXMLLog().addElement((Element)parent, name, value);
			el = (Element) p_LevelLog(name, value);
		}else if(parent instanceof Element){
			el = ((Element)parent).addElement(name).addText(value);
		}
		if(el != null){
//			refreshLogView(el);
		}
		return el;
	}
	

	public static Object p_LevelLog(String name, String value){
		return p_LevelLog(getCurrentNode(), name, value);
	}
	
	public static Object getCurrentNode(){
		return currentNode;
	}
	
	public static void setCurrentNode(Object node){
		currentNode = (Element) node;
	}
	
	public static Object addBrotherLog(String name, String value){
//		Element el = currentNode.getParent().addElement(name).addText(value);
//		return el;
		return p_LevelLog(currentNode.getParent(), name, value);
	}
	
	public static Object addSonLog(String name, String value){
//		Element el = currentNode.addElement(name).addText(value);
//		return el;
		return p_LevelLog(currentNode, name, value);
	}
}
