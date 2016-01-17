package pa.iscde.tasks.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import pa.iscde.tasks.extensibility.ITask;
import pa.iscde.tasks.extensibility.ITaskProvider;
import pa.iscde.tasks.extensibility.ITaskType;

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

				@Override
				public void performAction(ITask task) {
					System.out.println("Executing code from provider");
				}
			};
		}

		@Override
		public String getMsg() {
			return "A task from a provider";
		}

		@Override
		public Integer getLineNo() {
			return 0;
		}

		@Override
		public String getFileName() {
			return "file";
		}

		@Override
		public String getAbsolutePath() {
			return "./plugin.xml";
		}

	}

	@Override
	public List<ITask> getTasks() {
		final ArrayList<ITask> tasks = new ArrayList<>();
		tasks.add(new TaskTest());
		return tasks;
	}

	@Override
	public String getProviderName() {
		return "pa.provider.test";
	}

}
