package pt.iscde.classdiagram.extensibility;

import org.eclipse.zest.layouts.algorithms.AbstractLayoutAlgorithm;

public interface ILayoutExtender {

	public AbstractLayoutAlgorithm getLayout();

	public String getLayoutName();
	
}
