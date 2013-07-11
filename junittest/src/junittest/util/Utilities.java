package junittest.util;

import org.eclipse.core.resources.IResource;

public class Utilities {

	public static String getFullClassName(IResource res){
		return res.getProjectRelativePath().removeFirstSegments(1).removeFileExtension().toString().replaceAll("/", ".");
	}
}
