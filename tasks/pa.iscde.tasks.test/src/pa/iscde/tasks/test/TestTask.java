package pa.iscde.tasks.test;

import java.io.InputStream;

import pa.iscde.tasks.extensibility.ITaskType;

public class TestTask implements ITaskType {

	@Override
	public String getType() {
		return "TEST";
	}

	@Override
	public InputStream getIconStream() {
		//Get path to image
		String path = "/icon/icon.png";
		InputStream istream = getClass().getResourceAsStream(path);
		return istream;
	}

}
