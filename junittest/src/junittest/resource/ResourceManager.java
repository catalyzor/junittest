package junittest.resource;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import junittest.Activator;
import junittest.debug.JUnitTestRunnerJob;
import junittest.device.DeviceManager;
import junittest.util.ZipUtils;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.core.runtime.jobs.Job;

import sun.misc.ClassLoaderUtil;

import com.broadthinking.btt.device.ExtDeviceException;

public class ResourceManager {

	public static final String FOLDER_JAR = Messages.ResourceManager_0;
	public static final String FOLDER_LOG = Messages.ResourceManager_1;
	public static final String FOLDER_REPORT = Messages.ResourceManager_2;
	public static final String FOLDER_CASE = Messages.ResourceManager_3;
	
	public static final String FILE_CONFIG = Messages.ResourceManager_4;
	public static final String SUFFIX_PROPERTIES = Messages.ResourceManager_5;
	public static final String SUFFIX_CLASS = Messages.ResourceManager_6;
	public static final String SUFFIX_ADDITIONAL_LOG = Messages.ResourceManager_7;
	public static final String SUFFIX_LOG = Messages.ResourceManager_15;
	
	private Map<String, TestResultEnum> mapResult;
	private IProject project;
	public URLClassLoader urlClassLoad;
	public static IFolder logFolder;
	public static String caseName;
	private DeviceManager deviceManager;
	private static ResourceManager instance;
	private ResourceManager(String path){
//		ResourcesPlugin.getWorkspace().getRoot()
	};
	public static ResourceManager getInstance(){
		if(instance == null)
			instance = new ResourceManager(null);
		return instance;
	}
	
	public DeviceManager getDeviceManager() {
		return deviceManager;
	}
	public void setDeviceManager(DeviceManager deviceManager) {
		if(this.deviceManager != null){
			try {
				this.deviceManager.disconnectAllDevices();
			} catch (ExtDeviceException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
//		DeviceManager.sIExtDevices = null;
		this.deviceManager = deviceManager;
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
			IFile configFile = project.getFile(FILE_CONFIG);
			configFile.getLocation().toFile().createNewFile();
			project.refreshLocal(IResource.DEPTH_INFINITE, new SubProgressMonitor(monitor, 1));
			setProject(project);
		}
		return bool;
	}
	
	public void deleteProject(final IProject[] projects){
		Job job = new Job(Messages.ResourceManager_8) {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				// TODO Auto-generated method stub
				try {
					for(IProject prj: projects){
						//close current opened project.
						if(prj == project){
							ResourceManager.getInstance().setProject(null);
						}
					}
					ResourcesPlugin.getWorkspace().delete(projects, true, monitor);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
					return new Status(IStatus.ERROR, Activator.PLUGIN_ID, Messages.ResourceManager_9, e);
				}
				return Status.OK_STATUS;
			}
		};
		job.setPriority(Job.LONG);
		job.setUser(true);
		job.schedule();
	}

	public IProject getProject(){
		return project;
	}
	public void setProject(IProject project){
		getMapResult().clear();
		Job.getJobManager().cancel(JUnitTestRunnerJob.FAMILINAME);
		if(this.project != null){
			unloadTestJarClassLoader();
		}
		this.project = project;
		setDeviceManager(null);
		if(this.project != null){
			try {
				setDeviceManager(new DeviceManager(Messages.ResourceManager_10, getProject()));
				String str = this.project.getFolder(FOLDER_JAR).members()[0].getLocation().toOSString();
				loadTestJar(str);
			} catch (ExtDeviceException | ClassNotFoundException | IOException | InstantiationException | IllegalAccessException | CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
			}
		}
	}
	public void loadTestJar(String jarPath) throws IOException, ClassNotFoundException{
		urlClassLoad = URLClassLoader.newInstance(new URL[]{new URL(Messages.ResourceManager_11 + jarPath)}, getClass().getClassLoader());
		JarFile jar = new JarFile(jarPath);
		Enumeration<JarEntry> e = jar.entries();
		while(e.hasMoreElements()){
			JarEntry entry = e.nextElement();
			String name = entry.getName();
			if(!entry.isDirectory() && name.endsWith(Messages.ResourceManager_12 + SUFFIX_CLASS)){
				name = name.substring(0, name.length() - 6).replaceAll(Messages.ResourceManager_13, Messages.ResourceManager_14);
				urlClassLoad.loadClass(name);
			}
		}
		jar.close();
	}
	
	public void unloadTestJarClassLoader(){
		if(urlClassLoad != null){
			ClassLoaderUtil.releaseLoader(urlClassLoad);
		}
	}
	
	public void setMapResult(Map<String, TestResultEnum> map){
		this.mapResult = map;
	}
	
	public Map<String, TestResultEnum> getMapResult(){
		if(mapResult == null) mapResult = new HashMap<>();
		return mapResult;
	}
}
