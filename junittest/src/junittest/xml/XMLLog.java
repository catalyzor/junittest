package junittest.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import junittest.resource.ResourceManager;
import junittest.resource.TestResultEnum;
import junittest.util.Utilities;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;

public class XMLLog {

	public static final String NODE_ROOT = "project";
	public static final String NODE_SUITE = "suite";
	public static final String NODE_CASE = "case";
	public static final String NODE_NAME = "name";
	public static final String NODE_PROPS = "properties";
	public static final String NODE_VERDICT = "verdict";
	public static final String NODE_ATTR_DATE = "date";
	public static final String NODE_ATTR_TIME = "time";
	
	private String time;
	private Document doc;
	private IProject res;
	private String fileName;
	
	public Document getDocument(){
		return doc;
	}
	public XMLLog(String time, IProject res){
		this.time = time;
		this.res = res;
		this.fileName = time + ".xml";
		doc = DocumentHelper.createDocument();
	}
	
	public void initStructure(){
		Element root = doc.addElement(NODE_ROOT).addAttribute(NODE_ATTR_DATE, SimpleDateFormat.getDateTimeInstance(SimpleDateFormat.MEDIUM, SimpleDateFormat.MEDIUM).format(new Date(Long.parseLong(time))));
		root.addElement(NODE_NAME).addText(res.getName());
		root.addElement(NODE_VERDICT);
		try {
			addElement(root, res.getFolder(ResourceManager.FOLDER_CASE).members());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		saveToFile();
	}
	
	public void addProp(Element element, IResource res) throws CoreException, IOException{
		IFile file = null;
		if(res.getType() == IResource.FOLDER){
			file = ((IFolder)res.getAdapter(IFolder.class)).getFile(res.getName() + "." + ResourceManager.SUFFIX_PROPERTIES);
		}else if(res.getType() == IResource.FILE){
			if(res.getFileExtension() != null && res.getFileExtension().equals(ResourceManager.SUFFIX_CLASS)){
				file = res.getParent().getFile(Path.fromOSString(res.getName().substring(0, res.getName().length() - ResourceManager.SUFFIX_CLASS.length() - 1) + ResourceManager.SUFFIX_PROPERTIES));
			}
		}
		if(file != null && file.exists()){
			Properties prop = new Properties();
			InputStream in = file.getContents();
			prop.load(in);
			in.close();
			Iterator<Entry<Object, Object>> itr = prop.entrySet().iterator();
			Element props = element.addElement(NODE_PROPS);
			while(itr.hasNext()){
				Entry<Object, Object> entry = itr.next();
				props.addElement((String)entry.getKey()).addText((String)entry.getValue());
			}
		}
	}
	public synchronized void saveToFile(){
		try {
			FileWriter writer = new FileWriter(res.getFolder(ResourceManager.FOLDER_LOG).getLocation().append(fileName).toFile());
//			doc.write(writer);
			XMLWriter xmlWriter = new XMLWriter(writer, OutputFormat.createPrettyPrint());
			xmlWriter.write(doc);
			xmlWriter.close();
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void addElement(Element parent, IResource[] ress){
		for (int i = 0; i < ress.length; i++) {
			Element element = null;
			if(ress[i].getType() == IResource.FOLDER && !Utilities.isFilted(ress[i])){
				element = parent.addElement(NODE_SUITE);
				element.addElement(NODE_NAME).addText(ress[i].getName());
				element.addElement(NODE_VERDICT);
				try {
					addElement(element, ((IFolder)ress[i].getAdapter(IFolder.class)).members());
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(ress[i].getType() == IResource.FILE && !Utilities.isFilted(ress[i])){
				String name = ress[i].getName();
//				if(ress[i].getFileExtension() == null){
////					name = ress[i].getName();
//					element = parent.addElement(name);
//				}else if(ress[i].getFileExtension().toLowerCase().equals("class")){
//					name = name.substring(0, ress[i].getName().length() - 6);
//					element = parent.addElement(name);
//				}
//				if(!Utilities.isFilted(ress[i])){
//					name = name.substring(0, ress[i].getName().length() - 6);
				if(ress[i].getFileExtension() != null && ress[i].getFileExtension().equals(ResourceManager.SUFFIX_CLASS)){
					name = name.substring(0, name.length() - ResourceManager.SUFFIX_CLASS.length() - 1);
				}
					element = parent.addElement(NODE_CASE);
					element.addElement(NODE_NAME).addText(name);
					element.addElement(NODE_VERDICT);
//				}
			}

			try {
				addProp(element, ress[i]);
			} catch (CoreException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public synchronized void updateTestResult(String classname, TestResultEnum result){
		updateTestResult(classname, result, false);
	}
	public synchronized void updateTestResult(String classname, TestResultEnum result, boolean suite){
		if(result == null) return;
//		String path = new StringBuffer("/").append(res.getName()).append("/").append(classname.replace('.', '/')).append('.').append(ResourceManager.SUFFIX_CLASS).toString();
		StringBuffer sb = new StringBuffer("//");
		String[] names = classname.split("\\.");
		StringBuffer psb = new StringBuffer();
		for(int i = 0;i < names.length;i ++){
			if(i != names.length - 1){
				sb.append((i == 0)?NODE_ROOT:NODE_SUITE).append("[").append(NODE_NAME).append("='").append(names[i]).append("']").append("/");
				psb.append(names[i]).append(".");
//				sb.append(NODE_SUITE);
			}else{
				sb.append(suite?((i == 0)?NODE_ROOT:NODE_SUITE):NODE_CASE).append("[").append(NODE_NAME).append("='").append(names[i]).append("']").append("/").append(NODE_VERDICT);
//				sb.append(NODE_CASE);
			}
//			sb.append("[").append(NODE_NAME).append("=").append(names[i]).append("]").append("/")
		}
		String path = sb.toString();
		System.out.println(path);
		Node node = doc.selectSingleNode(path);
//		node.setText(result.name());
		if(TestResultEnum.FAIL.equals(result)){
			node.setText(result.name());
		}else if(TestResultEnum.ERROR.equals(result)){
			if(!node.getText().equals(TestResultEnum.FAIL.name())) node.setText(result.name());
		}else if(TestResultEnum.OK.equals(result)){
			String str = node.getText();
			if(!str.equals(TestResultEnum.ERROR.name()) && !str.equals(TestResultEnum.FAIL.name())) node.setText(result.name());
		}
		if(psb.length() > 0){
			updateTestResult(psb.deleteCharAt(psb.length() - 1).toString(), result, true);
		}
	}
	
	public String getTestResult(String xpath){
		String[] names = xpath.split("/");
		StringBuffer sb = new StringBuffer("/");
		for(int i = 0;i < names.length;i ++){
			sb.append((i == 0)?NODE_ROOT:NODE_SUITE).append("[").append(NODE_NAME).append("='").append(names[i]).append("']").append("/");
		}
		sb.append(NODE_VERDICT);
		String result = "";
		if(doc != null){
			Node node = doc.selectSingleNode(sb.toString());
			if(node instanceof Element){
				result = ((Element)node).getTextTrim();
			}
		}
		return result;
	}
	
	public synchronized Element addElement(String classname, String name, String value){
		String path = new StringBuffer("/").append(res.getName()).append("/").append(classname.replace('.', '/')).append('.').append(ResourceManager.SUFFIX_CLASS).toString();
		Node node = doc.selectSingleNode(path);
		if(node instanceof Element){
			Element element = ((Element)node).addElement(name);
			element.setText(value);
			return element;
		}
		return null;
	}
	
	public synchronized Element addElement(Element element, String name, String value){
		if(element == null) return null;
		Element el = element.addElement(name);
		el.setText(value);
		return el;
	}
	
//	public void updateTestResult(Element element, TestResultEnum result){
//		if(element == null) return;
//		if(TestResultEnum.FAIL.equals(result)){
//			element.setText(result.name());
//		}else if(TestResultEnum.ERROR.equals(result)){
//			if(!element.getText().equals(TestResultEnum.FAIL.name())) element.setText(result.name());
//		}else if(TestResultEnum.OK.equals(result)){
//			String str = element.getText();
//			if(!str.equals(TestResultEnum.ERROR.name()) && !str.equals(TestResultEnum.FAIL.name())) element.setText(result.name());
//		}
//		updateTestResult(element.getParent(), result);
//	}
	
	public static String getLogTestResult(String filename) throws DocumentException{
		SAXReader sax = new SAXReader();
		Document document = sax.read(new File(filename));
		return document.selectSingleNode("/" + NODE_ROOT + "/" + NODE_VERDICT).getText();
	}
}
