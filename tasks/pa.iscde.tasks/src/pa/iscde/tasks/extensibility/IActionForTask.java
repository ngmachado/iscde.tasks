package pa.iscde.tasks.extensibility;

import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public interface IActionForTask {
	
	public void Action(JavaEditorServices jes, ITask Task);
	
}
