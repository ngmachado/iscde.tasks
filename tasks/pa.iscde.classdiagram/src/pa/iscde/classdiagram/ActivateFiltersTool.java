package pa.iscde.classdiagram;

import pt.iscde.classdiagram.internal.ClassDiagramView;
import pt.iscte.pidesco.extensibility.PidescoTool;

public class ActivateFiltersTool implements PidescoTool {

	@Override
	public void run(boolean activate) {
		ClassDiagramView diagramView = ClassDiagramView.getInstance();
		diagramView.toggleFilters(activate);
	}

}
