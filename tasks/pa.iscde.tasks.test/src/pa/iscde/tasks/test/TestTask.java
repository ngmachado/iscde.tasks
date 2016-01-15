package pa.iscde.tasks.test;

import java.io.InputStream;

import pa.iscde.tasks.extensibility.ITask;
import pa.iscde.tasks.extensibility.ITaskType;

public class TestTask implements ITaskType {

	@Override
	public String getType() {
		return "TEST";
	}

	@Override
	public InputStream getIconStream() {
		//Get path to image
		String path = "/icon/icon.gif";
		InputStream istream = getClass().getResourceAsStream(path);
		return istream;
	}

	@Override
	public void performAction(ITask task) {
		System.out.println("Executing code from test type");
	}

}
