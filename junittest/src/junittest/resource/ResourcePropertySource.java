package junittest.resource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Properties;

import junittest.Activator;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.PropertyDescriptor;

public class ResourcePropertySource implements IPropertySource {

	private IResource res;
	private Properties props;
	private IPropertyDescriptor[] descs = new IPropertyDescriptor[0];
	public ResourcePropertySource(IResource res){
		this.res = res;
		IFile file = null;
		if(res.getType() == IResource.FOLDER){
			IFolder folder = (IFolder) Platform.getAdapterManager().getAdapter(res, IFolder.class);
			file = folder.getFile(res.getName() + Messages.ResourcePropertySource_0 + ResourceManager.SUFFIX_PROPERTIES);
		}else if(res.getType() == IResource.FILE){
			file = res.getParent().getFile(res.getFullPath().removeFileExtension().addFileExtension(ResourceManager.SUFFIX_PROPERTIES).makeRelativeTo(res.getParent().getFullPath()));
			
		}
		if(file != null && file.exists()){
			props = new Properties();
			try {
				InputStream is = file.getContents();
				props.load(is);
				is.close();
				buildDescs();
			} catch (IOException | CoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, e.getLocalizedMessage(), e));
			}
		}
	}
	
	public void buildDescs(){
		Object[] en = props.keySet().toArray();
//		Arrays.sort(en);
//		Set<Entry<Object,Object>> set = props.entrySet();
		descs = new IPropertyDescriptor[props.size()];
		for(int i = 0 ;i < descs.length;i ++){
			String name = (String) en[i];
			PropertyDescriptor txt = new PropertyDescriptor(name, name);
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
