package junittest.resource;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;

public class TestResult implements ITestResult, IAdaptable {

	private IResource res;
	private TestResultEnum result;
	
	public TestResult(IResource res){
		this.res = res;
	}
	
	@Override
	public Object getAdapter(Class adapter) {
		// TODO Auto-generated method stub
		if(IResource.class.equals(adapter)){
			return res;
		}
		return null;
	}

	@Override
	public TestResultEnum getResult() {
		// TODO Auto-generated method stub
		if(res.getType() == IResource.FILE){
			return result;
		}else if(res.getType() == IResource.FOLDER){
//			res.
		}
		return result;
	}

	@Override
	public void setResult(TestResultEnum result) {
		// TODO Auto-generated method stub
		this.result = result;
	}

}
