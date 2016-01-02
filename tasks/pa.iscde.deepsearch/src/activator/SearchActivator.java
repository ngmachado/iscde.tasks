package activator;

import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import extensionpoints.ISearchEvent;
import extensionpoints.ISearchEventListener;
import implementation.SearchEvent_Implementation;
import pa.iscde.mcgraph.service.McGraphServices;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class SearchActivator implements BundleActivator {

	private JavaEditorServices editor_service;
	private ProjectBrowserServices browser_service;

	private Set<ISearchEventListener> listeners;
	private ServiceRegistration<ISearchEvent> search_event_registry;

	private McGraphServices graph_services;

	private static SearchActivator activator;

	@Override
	public void start(BundleContext context) throws Exception {
		activator = this;
		listeners = new HashSet<ISearchEventListener>();

		ServiceReference<JavaEditorServices> ref_javaeditor = context.getServiceReference(JavaEditorServices.class);
		editor_service = context.getService(ref_javaeditor);
		ServiceReference<ProjectBrowserServices> ref_browser = context
				.getServiceReference(ProjectBrowserServices.class);
		browser_service = context.getService(ref_browser);
		
		//mc_graph
		ServiceReference<McGraphServices>ref_graph = context.getServiceReference(McGraphServices.class);
		graph_services = context.getService(ref_graph);

		search_event_registry = context.registerService(ISearchEvent.class, new SearchEvent_Implementation(), null);
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		activator = null;
		search_event_registry.unregister();
		listeners.clear();
	}

	public ProjectBrowserServices getBrowserService() {
		return browser_service;
	}

	public JavaEditorServices getEditorService() {
		return editor_service;
	}
	
	public McGraphServices getGraph_services() {
		return graph_services;
	}

	public static SearchActivator getActivatorInstance() {
		return activator;
	}

	public Set<ISearchEventListener> getListeners() {
		return listeners;
	}

	public void addListener(ISearchEventListener listener) {
		listeners.add(listener);
	}

	public void removeListener(ISearchEventListener listener) {
		listeners.remove(listener);
	}

	public Bundle getBundle() {
		return Platform.getBundle("pa.iscde.deepsearch");
	}
	
	public Image getImageFromURL(String parent) {
		URL fullPathString = FileLocator.find(getBundle(),
				new Path("images/" + parent.toLowerCase() + ".gif"), null);
		ImageDescriptor imageDesc = ImageDescriptor.createFromURL(fullPathString);
		Image image = imageDesc.createImage();
		return image;
	}
}
