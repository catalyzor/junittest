package junittest.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import junittest.util.ZipUtils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;

public class ResourceManager {

	public static final String FOLDER_JAR = "jar";
	public static final String FOLDER_LOG = "log";
	public static final String FOLDER_REPORT = "report";
	public static final String FOLDER_CASE = "case";
	IResource workspace;
	private static ResourceManager instance;
	private ResourceManager(String path){
//		ResourcesPlugin.getWorkspace().getRoot()
	};
	public static ResourceManager getInstance(){
		if(instance == null)
			instance = new ResourceManager(null);
		return instance;
	}
	
	public boolean createProject(String name, String jarPath, IProgressMonitor monitor) throws Exception{
		boolean bool = false;
		File file = new File(jarPath);
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
		if(!project.exists() && file.exists()){
			//create project structure.
			IProjectDescription desc = ResourcesPlugin.getWorkspace().newProjectDescription(name);
			project.create(desc,new SubProgressMonitor(monitor, 1));
			project.open(new SubProgressMonitor(monitor, 1));
			project.getFolder(FOLDER_JAR).create(true, true, new SubProgressMonitor(monitor, 1));
			project.getFolder(FOLDER_LOG).create(true, true, new SubProgressMonitor(monitor, 1));
			project.getFolder(FOLDER_REPORT).create(true, true, new SubProgressMonitor(monitor, 1));
			InputStream is = new FileInputStream(file);
			project.getFolder(FOLDER_JAR).getFile(file.getName()).create(is, true, new SubProgressMonitor(monitor, 1));
			is.close();
			project.getFolder(FOLDER_CASE).create(true, true, new SubProgressMonitor(monitor, 1));
//			JarFile jar = new JarFile(project.getFolder(FOLDER_JAR).members()[0].getLocation().toOSString());
//			Enumeration<JarEntry> entries = jar.entries();
//			while(entries.hasMoreElements()){
//				JarEntry entry = entries.nextElement();
//				if(entry.isDirectory()){
//					project.getFolder(entry.getName()).create(true, true, new SubProgressMonitor(monitor, 1));
//				}else {
//					InputStream input = jar.getInputStream(entry);
//					project.getFile(entry.getName()).create(input, true, new SubProgressMonitor(monitor, 1));
//					input.close();
//				}
//			}
//			ZipFile zip = new ZipFile(project.getFolder(FOLDER_JAR).getFile(file.getName()).getLocation().toOSString());
			ZipUtils.unZip(project.getFolder(FOLDER_JAR).members()[0].getLocation().toOSString(), project.getFolder(FOLDER_CASE).getLocation().toOSString());
			project.refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(monitor, 1));
		}
		return bool;
	}
}
