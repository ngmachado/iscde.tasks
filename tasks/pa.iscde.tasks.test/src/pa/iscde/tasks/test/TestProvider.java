package pa.iscde.tasks.test;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pa.iscde.tasks.extensibility.IActionForTask;
import pa.iscde.tasks.extensibility.ITask;
import pa.iscde.tasks.extensibility.ITaskProvider;
import pa.iscde.tasks.extensibility.ITaskType;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class TestProvider implements ITaskProvider {

	private class TaskTest implements ITask {

		@Override
		public String getPriority() {
			return "HIGH";
		}

		@Override
		public ITaskType getType() {
			return new ITaskType() {

				@Override
				public String getType() {
					return "NEWS";
				}

				@Override
				public InputStream getIconStream() {
					// TODO Auto-generated method stub
					return null;
				}
			};
		}

		@Override
		public String getMsg() {
			return "A task from a provider";
		}

		@Override
		public Integer getLineNo() {
			return 30;
		}

		@Override
		public String getFileName() {
			return "file";
		}

		@Override
		public String getAbsolutePath() {
			//return "D:\\projects\\iscte2015\\iscte.tasks\\pa.iscde.tasks.test\\plugin.xml";
			return "plugin.xml";
		}

	}

	@Override
	public List<ITask> getTasks() {
		final ArrayList<ITask> tasks = new ArrayList<>();
		tasks.add(new TaskTest());
		return tasks;
	}

	@Override
	public void performAction(pt.iscte.pidesco.javaeditor.service.JavaEditorServices jes, ITask task) {
		
		List<ITask> lst = this.getTasks();
		//get task to know information
		for (ITask tk : lst) {
			if(tk.equals(task)) {
				System.out.println(tk.getAbsolutePath());
				this.getAction().Action(jes, tk);
				//jes.openFile(new File(tk.getAbsolutePath()));
			}
		}
	}

	@Override
	public IActionForTask getAction() {
		return new IActionForTask() {
			
			@Override
			public void Action(JavaEditorServices jes, ITask task) {
				System.out.println(task.getAbsolutePath());
			}
		};
	}

}
