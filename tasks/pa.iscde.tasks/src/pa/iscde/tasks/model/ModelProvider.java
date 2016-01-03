package pa.iscde.tasks.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;

import pa.iscde.tasks.control.TasksActivator;
import pa.iscde.tasks.extensibility.ITask;
import pa.iscde.tasks.extensibility.ITaskProvider;
import pa.iscde.tasks.model.parser.CommentExtractor;
import pa.iscde.tasks.model.parser.FileToString;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;

public enum ModelProvider {

	INSTANCE;
	
	private final List<ITaskProvider> taskProviders;
	private String filterQuery;
	
	private ModelProvider() {
		taskProviders = new ArrayList<ITaskProvider>();
	}

	public void setFilter(String filter) {
		filterQuery = filter;
	}
	
	public List<ITask> getTasksList() {
		taskProviders.clear();
		getTasksFromClientProviders();
		try {
			 populateInternalTask(TasksActivator.getBrowserServices().getRootPackage().getChildren());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		List<ITask> returnListTasks = new ArrayList<>();
		for (ITaskProvider tp : taskProviders) {
			returnListTasks.addAll(tp.getTasks());
		}
		
		//Remove not query results
		if(filterQuery != null)  {
			List<ITask> filterResult = new ArrayList<>();
			for (ITask iTask : returnListTasks) {
				if(iTask.getType().getType().equals(filterQuery)) {
					filterResult.add(iTask);
				}
				
			}
			//If the result is empty then is better to show all
			if(filterResult.isEmpty())  {
				return returnListTasks;
			} else {
				return filterResult;
			}
			
			
		}
		
		return returnListTasks;
	}

	//Get tasks from plugin clients.
	public void getTasksFromClientProviders() {
		final IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		final IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pa.iscde.tasks.provider");

		for (IExtension e : extensionPoint.getExtensions()) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for (IConfigurationElement c : confElements) {
				try {
					ITaskProvider tp = (ITaskProvider) c.createExecutableExtension("class");
					String idProvider = c.getAttribute("class");
					
					if (tp != null) {
						TaskProvider provider = new TaskProvider();
						provider.setClienteId(idProvider);
						provider.setTasks(tp.getTasks());
						taskProviders.add(provider);
					}
				} catch (CoreException e1) {
					e1.printStackTrace();
				}
			}
		}

	}
	
	//Get Provider ID for a given task 
	public String getProviderID(ITask task) 
	{
		for (ITaskProvider tp : taskProviders) 
		{
			for (ITask t : tp.getTasks()) {
				if(t.equals(task))
				{
					return ((TaskProvider)tp).getClienteId();
				}
			}
		}
		//If don't found Provider user a generic one...
		return "pa.iscte.generic"; 
	}
	
	private void handleSources(SortedSet<SourceElement> sources, List<ITask> lst) throws IOException {
		// TODO - Convert to Visitor?
		for (SourceElement e : sources) {
			if (e.isPackage())
				handleSources(((PackageElement) e).getChildren(),lst);
			else 
			{
				lst.addAll(new CommentExtractor(new FileToString(e.getFile()).parse(), e.getName(),
						e.getFile().getAbsolutePath()).getCommentDetails());	
			}
				

		}
	}

	private void populateInternalTask(SortedSet<SourceElement> sources)  throws IOException
	{
		List<ITask> lst = new ArrayList<ITask>();
		handleSources(sources,lst);
		String id = this.getClass().getPackage().getName();
		TaskProvider prv = new TaskProvider();
		prv.setClienteId(id);
		prv.setTasks(lst);
		taskProviders.add(prv);
	}
	
	private ITaskProvider findProvider(ITask task)  {
		for (ITaskProvider tp : taskProviders) {
			for (ITask tk : tp.getTasks()) {
				if(tk.equals(task)) {
					return tp;
				}
			}
		}
		return null;
	}
	
	public void ActionPerformFromProvider(JavaEditorServices jes, ITask task) {
		//Find PerformAction of Task and executed in jes editor context
		//find based on providerID
		ITaskProvider tp = findProvider(task);
		if(tp != null)  {
			tp.performAction(jes,task);
		}
		//If not found don't do nothing...
		return;
	}
}
