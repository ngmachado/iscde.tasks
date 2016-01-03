package pa.iscde.tasks.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.IAction;

import pa.iscde.tasks.extensibility.IActionForTask;
import pa.iscde.tasks.extensibility.ITask;
import pa.iscde.tasks.extensibility.ITaskProvider;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class TaskProvider implements ITaskProvider {

	
	private List<ITask> tasks = new ArrayList<ITask>();
	private String clientId;
	
	
	@Override
	public List<ITask> getTasks() {
		return tasks;
	}
	
	public void setTasks(List<ITask> lst)  {
		tasks = lst;
	}

	public String getClienteId() {
		return clientId;
	}

	public void setClienteId(String clientId) {
		this.clientId = clientId;
	}

	@Override
	public void performAction(JavaEditorServices jes, ITask task) {
		this.getAction().Action(jes, task);
		//act.Action(task);
		//jes.openFile(new File(task.getAbsolutePath()));
	}

	@Override
	public IActionForTask getAction() {
		return new IActionForTask() {
			@Override
			public void Action(JavaEditorServices jes, ITask task) {
				
			}
		};
	}
	 

	
	
}
