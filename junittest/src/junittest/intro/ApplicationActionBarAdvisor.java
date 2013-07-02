package junittest.intro;

import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;

public class ApplicationActionBarAdvisor extends ActionBarAdvisor {

//    private IWorkbenchAction introAction;
    
	public ApplicationActionBarAdvisor(IActionBarConfigurer configurer) {
		super(configurer);
	}

//	protected void makeActions(IWorkbenchWindow window) {
//		introAction = ActionFactory.INTRO.create(window);
//		register(introAction);
//	}
//
//	protected void fillMenuBar(IMenuManager menuBar) {
//		
//		MenuManager helpMenu = new MenuManager("&Help", IWorkbenchActionConstants.M_HELP);
//		menuBar.add(helpMenu);
//
//		// Help
//		helpMenu.add(introAction);
//	}

}
