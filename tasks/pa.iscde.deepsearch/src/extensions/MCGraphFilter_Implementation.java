package extensions;

import org.eclipse.jdt.core.dom.MethodDeclaration;

import pa.iscde.mcgraph.service.McGraphFilter;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;

public class MCGraphFilter_Implementation implements McGraphFilter {

	private static MCGraphFilter_Implementation instance;
	private String searchClassElement = "";
	private String searchMethodElement = "";

	public MCGraphFilter_Implementation() {
		instance = this;
	}

	@Override
	public boolean accept(ClassElement c, MethodDeclaration md) {
		if (!searchClassElement.equals("")) {
			return c.getName().equals(searchClassElement);
		} else if (searchMethodElement.equals("")) {
			return md.getName().equals(searchMethodElement);
		}
		return true;
	}

	@Override
	public boolean acceptDependencies(ClassElement c, MethodDeclaration md) {
		return true;
	}

	public void addToSearchables(String searchedText, String type) {
		searchClassElement = "";
		searchMethodElement = "";
		if (type.equals("Class")) {
			searchClassElement = searchedText;
		} else {
			searchMethodElement = searchedText;
		}
	}

	public static MCGraphFilter_Implementation getInstance() {
		return instance;
	}

}
