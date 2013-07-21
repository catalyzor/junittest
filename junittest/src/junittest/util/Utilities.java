package junittest.util;

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
}
