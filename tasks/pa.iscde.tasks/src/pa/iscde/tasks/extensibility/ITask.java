package pa.iscde.tasks.extensibility;

/**
 * Abstracts the Task concept
 * 
 */
public interface ITask {
	
	
	/**
	 * Get the Priority for one task
	 * 
	 * @return String
	 */
	public String getPriority();

	/**
	 * Define the type to one task.
	 * 
	 * @return ITaskType
	 */
	public ITaskType getType();
	

	/**
	 * Get message that will be part of the task
	 * 
	 * @return String
	 */
	public String getMsg();

	/**
	 * Get the number line where is located the task
	 * 
	 * @return Integer
	 */
	public Integer getLineNo();

	/**
	 * Get the file where is located the task
	 * 
	 * @return String
	 */
	public String getFileName();

	/**
	 * Get the absolute path to file where is located the task
	 * 
	 * @return String
	 */
	public String getAbsolutePath();

}
