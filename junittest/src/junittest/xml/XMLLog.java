package junittest.xml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Properties;

import junittest.resource.ResourceManager;
import junittest.resource.TestResultEnum;

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

//	public static final String NODE_ROOT = "Project";
//	public static final String NODE_SUITE = "Suite";
//	public static final String NODE_CASE = "Case";
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
		Element root = doc.addElement(res.getName()).addAttribute(NODE_ATTR_DATE, time);
		try {
			addElement(root, res.getFolder(ResourceManager.FOLDER_CASE).members());
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		saveToFile();
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
			while(itr.hasNext()){
				Entry<Object, Object> entry = itr.next();
				element.addAttribute((String)entry.getKey(), (String)entry.getValue());
			}
		}
	}
	public void saveToFile(){
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
			if(ress[i].getType() == IResource.FOLDER){
				element = parent.addElement(ress[i].getName());
				try {
					addElement(element, ((IFolder)ress[i].getAdapter(IFolder.class)).members());
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}else if(ress[i].getType() == IResource.FILE){
				String name = ress[i].getName();
				if(ress[i].getFileExtension() == null){
//					name = ress[i].getName();
					element = parent.addElement(name);
				}else if(ress[i].getFileExtension().toLowerCase().equals("class")){
					name = name.substring(0, ress[i].getName().length() - 6);
					element = parent.addElement(name);
				}
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
		if(result == null) return;
		String path = "//" + res.getName() + "/" + classname.replace('.', '/');
		System.out.println(path);
		Node node = doc.selectSingleNode(path);
		node.setText(result.name());
		
	}
	
	public void updateTestResult(Element element, TestResultEnum result){
		if(element == null) return;
		if(TestResultEnum.FAIL.equals(result)){
			element.setText(result.name());
		}else if(TestResultEnum.ERROR.equals(result)){
			if(!element.getText().equals(TestResultEnum.FAIL.name())) element.setText(result.name());
		}else if(TestResultEnum.OK.equals(result)){
			String str = element.getText();
			if(!str.equals(TestResultEnum.ERROR.name()) && !str.equals(TestResultEnum.FAIL.name())) element.setText(result.name());
		}
		updateTestResult(element.getParent(), result);
	}
	
	public static String getTestResult(String filename) throws DocumentException{
		SAXReader sax = new SAXReader();
		Document document = sax.read(new File(filename));
		return document.getRootElement().getText();
	}
}
