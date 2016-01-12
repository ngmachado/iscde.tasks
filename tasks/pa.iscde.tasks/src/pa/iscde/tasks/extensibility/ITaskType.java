package pa.iscde.tasks.extensibility;

import java.io.InputStream;

/**
 * Represents a Task Type that can be used by Tasks.
 **
 */
public interface ITaskType {

	/**
	 * A display representation of the TaskType. This is the name that will be
	 * showed in the Type Cell.
	 * 
	 * @return String
	 */
	public String getType();
	
	/**
     * A Stream that contains a icon image for the Type.
     * 
     * @return InputStream
     */
	public InputStream getIconStream();
	
	

}
