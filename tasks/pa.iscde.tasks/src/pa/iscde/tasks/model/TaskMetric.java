package pa.iscde.tasks.model;

import pa.iscde.tasks.extensibility.ITaskType;
/*
 * Classe que representa uma metrica para task existentes.
 * */
public class TaskMetric {

	private  int count = 0;
	private final ITaskType type;
	
	public ITaskType getType() {
		return type;
	}

	public int getCount() {
		return count;
	}

	public void setCount() {
		this.count++;
	}
	
	public TaskMetric(ITaskType type)  {
		this.type = type;		
	}
	
}
