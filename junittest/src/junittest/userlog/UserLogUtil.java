package junittest.userlog;

import junittest.debug.JUnitRunner;

import org.dom4j.Element;

public class UserLogUtil {

	public static Object log(Object parent, String name, String value){
		if(parent instanceof String){
			return JUnitRunner.getInstance().getXMLLog().addElement((String)parent, name, value);
		}else if(parent instanceof Element){
			return JUnitRunner.getInstance().getXMLLog().addElement((Element)parent, name, value);
		}
		return parent;
	}
}
