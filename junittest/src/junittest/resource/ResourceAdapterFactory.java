package junittest.resource;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.ui.views.properties.IPropertySource;

public class ResourceAdapterFactory implements IAdapterFactory {

	@Override
	public Object getAdapter(Object adaptableObject, Class adapterType) {
		// TODO Auto-generated method stub
		if(adapterType.equals(IPropertySource.class)){
			return new ResourcePropertySource((IResource) adaptableObject);
		}
		return null;
	}

	@Override
	public Class[] getAdapterList() {
		// TODO Auto-generated method stub
		return new Class[]{IPropertySource.class};
	}

}
