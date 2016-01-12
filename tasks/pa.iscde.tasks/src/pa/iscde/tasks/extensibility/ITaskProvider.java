package pa.iscde.tasks.extensibility;

import java.util.List;

public interface ITaskProvider {
	
	/**
	 * Return the Provider Name
	 * 
	 * @return String
	 */
	public String getProviderName();
	
	/**
	 * Get a list of task from the Provider
	 * 
	 * @return List<ITask>
	 */
	public List<ITask> getTasks(); 
	
	/**
	 * Define action to be perform in ITask task
	 * 
	 * @param task ITask that action will be perform
	 */
	public void performAction(ITask task);
	
}
