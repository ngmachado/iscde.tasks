package pt.iscde.classdiagram.extensibility;

import org.eclipse.jface.action.Action;

import pt.iscde.classdiagram.model.TopLevelElement;


public abstract class ClassDiagramAction extends Action {

	protected TopLevelElement selectedElement;
	
	public final void setSelectedElement(TopLevelElement selectedElement){
		this.selectedElement = selectedElement;
	}

}
