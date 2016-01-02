package pt.iscde.classdiagram.extensibility.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutAlgorithm;

public class ChangeLayoutAction extends Action {
	
	private GraphViewer viewer;
	private LayoutAlgorithm layout;
	
	public ChangeLayoutAction(GraphViewer viewer, String layoutName, LayoutAlgorithm layoutAlgorithm) {
		this.viewer = viewer;
		this.layout = layoutAlgorithm;
		setText(layoutName);
	}

	@Override
	public void run() {
		viewer.setLayoutAlgorithm(layout);
		viewer.refresh();
		viewer.applyLayout();
	}
}
