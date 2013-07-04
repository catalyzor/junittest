package junittest.wizard;

import java.lang.reflect.InvocationTargetException;

import junittest.resource.ResourceManager;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.Wizard;

public class LoadWizard extends Wizard {

	LoadWizardPage1 page1;
	private String projectName;
	public LoadWizard() {
		setWindowTitle("New Wizard");
		setNeedsProgressMonitor(true);
	}

	@Override
	public void addPages() {
		page1 = new LoadWizardPage1();
		addPage(page1);
	}

	public IProject getProject(){
		return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
	}
	@Override
	public boolean performFinish() {
		try {
			getContainer().run(false, false, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException,
						InterruptedException {
					// TODO Auto-generated method stub
					
//					try {
						try {
							projectName = page1.getProjectName();
							ResourceManager.getInstance().createProject(projectName, page1.getJarPath(), monitor);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							throw new InvocationTargetException(e);
						}
//					} catch (CoreException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						throw new InvocationTargetException(e);
//					} catch (IOException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//						throw new InvocationTargetException(e);
//					}
				}
			});
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
