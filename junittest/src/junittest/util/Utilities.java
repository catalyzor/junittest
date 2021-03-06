package junittest.util;

import java.util.Arrays;

import junit.framework.TestCase;
import junittest.Activator;
import junittest.resource.ResourceManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

public class Utilities {

	public static String getFullClassName(IResource res){
		return res.getProjectRelativePath().removeFirstSegments(1).removeFileExtension().toString().replaceAll(Messages.Utilities_0, Messages.Utilities_1);
	}
	
	public static boolean isFilted(IResource res){
		if(res.getType() == IResource.FOLDER){
			if(res.getName().toUpperCase().equals(Messages.Utilities_2)){
				return true;
			}
			return false;
		}else if(res.getType() == IResource.FILE){
			if(res.getFileExtension() != null && res.getFileExtension().toLowerCase().equals(Messages.Utilities_3)){
				try {
					Class clazz = ResourceManager.getInstance().urlClassLoad.loadClass(getFullClassName(res));
//					if(clazz.isAssignableFrom(TestCase.class)){
//						return false;
//					}
					return !checkExtendsRelation(clazz, TestCase.class);
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
				}
				return false;
			}
			return true;
		}
		return false;
	}

	public static boolean checkExtendsRelation(Class subclass ,Class superclass){
		boolean bool = false;
		if(subclass.getSuperclass() != null){
			if(subclass.getSuperclass().equals(superclass)){
				bool = true;
			}else{
				bool = checkExtendsRelation(subclass.getSuperclass(), superclass);
			}
		}
		return bool;
	}
	public static IResource getLatestLogfile(IProject project){
		IResource res = null;
		if(project != null){
			IFolder folder = project.getFolder(ResourceManager.FOLDER_LOG);
			if(folder.exists()){
				String[] filenames = folder.getLocation().toFile().list();
				Arrays.sort(filenames);
				for(int i = filenames.length; i > 0;i --){
					IFolder f = folder.getFolder(filenames[i - 1]);
					if(f != null && f.exists()){
						IFile file = f.getFile(filenames[i - 1] + "." + ResourceManager.SUFFIX_LOG);
						if(file != null && file.exists()){
							res = file;
							break;
						}
					}
//					if(filenames[i - 1].endsWith(ResourceManager.SUFFIX_LOG)){
//						res = folder.getFile(filenames[i - 1]);
//						break;
//					}
				}
			}
		}
		return res;
	}
}
