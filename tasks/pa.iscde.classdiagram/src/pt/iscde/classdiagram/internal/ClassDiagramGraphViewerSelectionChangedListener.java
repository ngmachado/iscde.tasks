package pt.iscde.classdiagram.internal;

import java.util.List;

import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.zest.core.viewers.GraphViewer;

import pt.iscde.classdiagram.extensibility.ClassDiagramAction;
import pt.iscde.classdiagram.model.TopLevelElement;

class ClassDiagramGraphViewerSelectionChangedListener implements ISelectionChangedListener {

	private GraphViewer viewer;
	private List<ClassDiagramAction> actions;

	public ClassDiagramGraphViewerSelectionChangedListener(GraphViewer viewer,  List<ClassDiagramAction> actions) {
		this.viewer = viewer;
		this.actions = actions;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		setActionSelectedElement(null);
		
		IStructuredSelection selection = (IStructuredSelection) event.getSelection();
		Object[] nodeElements = viewer.getNodeElements();
		
		for (Object nodeElement : nodeElements) {
			if (nodeElement instanceof TopLevelElement) {
				TopLevelElement element = (TopLevelElement) nodeElement;
				element.setSelected(false);
			}
		}
		
		if (selection.size() > 0) {
			Object selectedElement = selection.getFirstElement();
			if (selectedElement instanceof TopLevelElement) {
				TopLevelElement element = (TopLevelElement) selectedElement;
				element.setSelected(!element.isSelected());
				setActionSelectedElement(element);
			}
		}
		viewer.refresh(true);
	}
	
	
	private void setActionSelectedElement(TopLevelElement element){
		if(actions != null){
			for (ClassDiagramAction action : actions) {
				action.setSelectedElement(element);
			}
		}
	}
	
	
}