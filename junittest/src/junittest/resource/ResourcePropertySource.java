package junittest.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

public class ResourcePropertySource implements IPropertySource {

	private IResource res;
	private Properties props;
	private IPropertyDescriptor[] descs;
	public ResourcePropertySource(IResource res){
		this.res = res;
		IFile file = null;
		if(res.getType() == IResource.FOLDER){
			IFolder folder = (IFolder) Platform.getAdapterManager().getAdapter(res, IFolder.class);
			file = folder.getFile(res.getName() + "." + ResourceManager.SUFFIX_PROPERTIES);
		}else if(res.getType() == IResource.FILE){
			file = res.getParent().getFile(res.getFullPath().removeFileExtension().addFileExtension(ResourceManager.SUFFIX_PROPERTIES));
			
		}
		if(file.exists()){
			props = new Properties();
			try {
				InputStream is = file.getContents();
				props.load(is);
				is.close();
			} catch (IOException | CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void buildDescs(){
		Enumeration<Object> en = props.keys();
//		Set<Entry<Object,Object>> set = props.entrySet();
		descs = new IPropertyDescriptor[props.size()];
		for(int i = 0 ;i < descs.length;i ++){
			String name = (String) en.nextElement();
			TextPropertyDescriptor txt = new TextPropertyDescriptor(name, name);
			descs[i] = txt;
//			txt.set
		}
//		int i = 0;
//		for(Iterator<Entry<Object, Object>> itr = set.iterator(); itr.hasNext(); i ++){
//			descs[i] = new TextPropertyDescriptor(id, displayName)
//		}
	}
	@Override
	public Object getEditableValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IPropertyDescriptor[] getPropertyDescriptors() {
		// TODO Auto-generated method stub
		return descs;
	}

	@Override
	public Object getPropertyValue(Object id) {
		// TODO Auto-generated method stub
		if(props != null){
			return props.getProperty((String) id);
		}
		return null;
	}

	@Override
	public boolean isPropertySet(Object id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resetPropertyValue(Object id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setPropertyValue(Object id, Object value) {
		// TODO Auto-generated method stub

	}

}
