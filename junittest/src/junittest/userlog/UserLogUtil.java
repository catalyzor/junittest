package junittest.userlog;

import junittest.debug.JUnitRunner;

import org.dom4j.Element;

public class UserLogUtil {

	private static Element currentNode;
	
	public static Object p_LevelLog(Object parent, String name, String value){
		if(parent == null){
			return JUnitRunner.getInstance().getXMLLog().addElement(currentNode, name, value);
		}else if(parent instanceof String){
			return JUnitRunner.getInstance().getXMLLog().addElement((String)parent, name, value);
		}else if(parent instanceof Element){
			return JUnitRunner.getInstance().getXMLLog().addElement((Element)parent, name, value);
		}
		return parent;
	}
	

	public static Object p_LevelLog(String name, String value){
		return p_LevelLog(null, name, value);
	}
	
	public static Object getCurrentNode(){
		return currentNode;
	}
	
	public static void setCurrentNode(Object node){
		currentNode = (Element) node;
	}
	
	public static Object addBrotherLog(String name, String value){
		Element el = currentNode.getParent().addElement(name).addText(value);
		return el;
	}
	
	public static Object addSonLog(String name, String value){
		Element el = currentNode.addElement(name).addText(value);
		return el;
	}
}
