package pa.iscde.tasks.extensibility;

import java.util.List;

import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public interface ITaskProvider {
	
	public List<ITask> getTasks(); 
	
	public void performAction(JavaEditorServices jes, ITask task);
	
}
