package junittest.util;

import java.util.Arrays;

import junittest.resource.ResourceManager;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;

public class Utilities {

	public static String getFullClassName(IResource res){
		return res.getProjectRelativePath().removeFirstSegments(1).removeFileExtension().toString().replaceAll("/", ".");
	}
	
	public static boolean isFilted(IResource res){
		if(res.getType() == IResource.FOLDER){
			if(res.getName().toUpperCase().equals("META-INF")){
				return true;
			}
			return false;
		}else if(res.getType() == IResource.FILE){
			if(res.getFileExtension() != null && res.getFileExtension().toLowerCase().equals("class")){
				return false;
			}
			return true;
		}
		return false;
	}

	public static IResource getLatestLogfile(IProject project){
		IResource res = null;
		if(project != null){
			IFolder folder = project.getFolder(ResourceManager.FOLDER_LOG);
			if(folder.exists()){
				String[] filenames = folder.getLocation().toFile().list();
				Arrays.sort(filenames);
				res = folder.getFile(filenames[filenames.length - 1]);
			}
		}
		return res;
	}
}
