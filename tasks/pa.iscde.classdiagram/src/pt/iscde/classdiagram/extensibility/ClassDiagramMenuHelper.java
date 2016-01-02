package pt.iscde.classdiagram.extensibility;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalShift;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import pt.iscde.classdiagram.extensibility.actions.ChangeLayoutAction;
import pt.iscde.classdiagram.extensibility.actions.FilterAction;

public class ClassDiagramMenuHelper {

	private List<Action> defaultActions;
	private List<ClassDiagramFilter> filters;
	private List<ClassDiagramAction> actions;
	private GraphViewer viewer;
	private MenuManager mm;
	private List<ILayoutExtender> layouts;

	public ClassDiagramMenuHelper(GraphViewer viewer) {
		super();
		defaultActions = new ArrayList<Action>();
		filters = new ArrayList<ClassDiagramFilter>();
		actions = new ArrayList<ClassDiagramAction>();

		this.viewer = viewer;
		this.mm = new MenuManager();
	}

	public Menu getMenu() {
		mm.createContextMenu(viewer.getGraphControl());
		updateManager();
		return mm.getMenu();
	}
	
	private void updateManager() {
		mm.removeAll();
		addDefaultActionsToMenu();
		addFiltersToMenu();
		addActionsToMenu();
		addLayoutsToMenu();
	}

	private void addDefaultActionsToMenu() {
		if (defaultActions != null && defaultActions.size() > 0) {
			for (Action action : defaultActions) {
				mm.add(action);
			}
		}
	}

	private void addFiltersToMenu() {
		if (filters != null && filters.size() > 0) {
			mm.add(new Separator());
			for (ClassDiagramFilter classDiagramFilter : filters) {
				Action filterAction = new FilterAction(classDiagramFilter, viewer);
				filterAction.setId("Filter_" + classDiagramFilter.hashCode());
				mm.add(filterAction);
			}
		}
	}

	private void addActionsToMenu() {
		if (actions != null && actions.size() > 0) {
			mm.add(new Separator());
			for (ClassDiagramAction action : actions) {
				mm.add(action);
			}
		}

	}
	
	private void addLayoutsToMenu() {
		if (layouts != null && layouts.size() > 0) {
			mm.add(new Separator());
			for (ILayoutExtender layout : layouts) {
				mm.add(new ChangeLayoutAction(viewer, layout.getLayoutName(), getLayout(layout)));
			}
		}

	}
	
	private CompositeLayoutAlgorithm getLayout(ILayoutExtender layout) {
		HorizontalShift horizontalShift = new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		return new CompositeLayoutAlgorithm(new LayoutAlgorithm[] { horizontalShift,  layout.getLayout() });
	}


	public void addDefaultAction(Action refreshAction) {
		defaultActions.add(refreshAction);
		updateManager();
	}

	public void setFilters(List<ClassDiagramFilter> filters) {
		this.filters = filters;
		updateManager();
	}

	public void setActions(List<ClassDiagramAction> actions) {
		this.actions = actions;
		updateManager();
	}

	public void setLayouts(List<ILayoutExtender> layouts) {
		this.layouts = layouts;
		updateManager();
	}
}
