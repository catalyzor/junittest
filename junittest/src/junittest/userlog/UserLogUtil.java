package junittest.userlog;

import junittest.view.LogView;

import org.dom4j.Element;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class UserLogUtil {

	private static Element currentNode;
	public static int counter = 0;

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
		
		counter ++;
		if(el != null && counter >= 10){
			refreshLogView(el);
			counter = 0;
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
	public static Object getGrandFatherNode(){
		return currentNode.getParent().getParent();
	}
	public static Object getFatherNode(){
		return currentNode.getParent();
	}
	public static Object getSonNode(){
		return (Element) currentNode.selectSingleNode("/[last()]");
	}
	public static Object getGrandSonNode(){
		return (Element) currentNode.selectSingleNode("/[last()]").selectSingleNode("/[last()]");
	}
	
	
	public static void setGrandFatherAsCur(){
		currentNode = (Element) getGrandFatherNode();
	}

	public static void setFatherAsCur(){
		currentNode = (Element) getFatherNode();
	}

	public static void setSonAsCur(){
		currentNode = (Element) getSonNode();
	}

	public static void setGrandSonAsCur(){
		currentNode = (Element) getGrandSonNode();
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
