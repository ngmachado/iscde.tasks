package pt.iscde.classdiagram.extensibility.actions;

import org.eclipse.zest.core.viewers.GraphViewer;

import pt.iscde.classdiagram.extensibility.ClassDiagramFilter;

public class FilterAction extends RefreshAction{
	
	private ClassDiagramFilter filter;
	
	public FilterAction(ClassDiagramFilter classDiagramFilter, GraphViewer viewer) {
		super(viewer);
		this.filter = classDiagramFilter;
		setText(classDiagramFilter.getFilterName());
		setToolTipText(classDiagramFilter.getFilterShortDescription());
		setChecked(classDiagramFilter.isActive());
	}

	@Override
	public void run() {
		if(filter.isActive()){
			filter.deactivate();
		}else{
			filter.activate();
		}
		
		setChecked(filter.isActive());
		super.run();
	}
	
	
}
