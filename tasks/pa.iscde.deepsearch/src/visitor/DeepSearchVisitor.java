package visitor;

import java.io.File;

import activator.SearchActivator;
import enums.SearchEnumType;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.PackageElement.Visitor;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;

public class DeepSearchVisitor extends Visitor.Adapter {

	private JavaEditorServices editor_search = SearchActivator.getActivatorInstance().getEditorService();
	private SearchEnumType myEnumType;
	private ASTVisitorDeepSearch astVisitor_deepSearch;

	public DeepSearchVisitor(String searchText, SearchEnumType enum_type, String advancedSpecifications) {
		this.myEnumType = enum_type;
		astVisitor_deepSearch = new ASTVisitorDeepSearch(searchText, myEnumType, advancedSpecifications);
	}

	@Override
	public void visitClass(ClassElement c) {
		if (c != null) {
			File f = c.getFile();
			astVisitor_deepSearch.setFile(f);
			editor_search.parseFile(f, astVisitor_deepSearch);
		}
	}

	public ASTVisitorDeepSearch getASTVisitor_deepSearchMy() {
		return astVisitor_deepSearch;
	}

	public ClassElement getClass(String className, PackageElement rootPackage) {
		for (SourceElement source_package : rootPackage) {
			ClassElement c = getClassOfSearchIn(className, (PackageElement) source_package);
			if (c != null) {
				return c;
			}
		}
		return null;
	}

	private ClassElement getClassOfSearchIn(String className, PackageElement source_package) {
		for (SourceElement e : ((PackageElement) source_package).getChildren()) {
			if (e.isClass()) {
				if (className.equals(e.getParent().getName() + "." + e.getName())) {
					return (ClassElement) e;
				}
			} else {
				ClassElement c = getClassOfSearchIn(className, (PackageElement) e);
				if (c != null)
					return c;
			}
		}
		return null;
	}
}
