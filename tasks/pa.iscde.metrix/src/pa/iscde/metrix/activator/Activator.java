package pa.iscde.metrix.activator;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class Activator implements BundleActivator {
	
	private static JavaEditorServices editorService;
	private static ProjectBrowserServices projectService;

	@Override
	public void start(BundleContext context) throws Exception {
		editorService = context.getService(context.getServiceReference(JavaEditorServices.class));
		projectService = context.getService(context.getServiceReference(ProjectBrowserServices.class));
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		
	}
	
	public static JavaEditorServices getEditorService() {
		return editorService;
	}
	
	public static ProjectBrowserServices getProjectService() {
		return projectService;
	}

}
