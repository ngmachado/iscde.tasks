package pt.iscde.classdiagram.extensibility.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.zest.core.viewers.GraphViewer;

public class RefreshAction extends Action {

	GraphViewer viewer;
	public RefreshAction(GraphViewer viewer) {
		this.viewer = viewer;
		setText("refresh");
	}

	@Override
	public void run() {
		viewer.refresh();
		viewer.applyLayout();
	}
}
