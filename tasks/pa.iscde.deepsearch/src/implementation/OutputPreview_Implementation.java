package implementation;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

import activator.SearchActivator;
import enums.SearchEnumType;
import extensionpoints.Item;
import extensionpoints.OutputPreview;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;
import visitor.DeepSearchVisitor;

public class OutputPreview_Implementation implements OutputPreview {

	private ProjectBrowserServices browser_search = SearchActivator.getActivatorInstance().getBrowserService();;
	private JavaEditorServices editor_search = SearchActivator.getActivatorInstance().getEditorService();
	private String text_Search;
	private String text_SearchInCombo = "";
	private String specificText_SearchInCombo;
	private DeepSearchVisitor visitor;
	private PackageElement root_package = (PackageElement) browser_search.getRootPackage();
	private String text_SearchForCombo = "";

	@Override
	public LinkedList<Item> getParents() {
		LinkedList<Item> parents = new LinkedList<Item>();
		parents.add(new OutputItem("Package", SearchActivator.getActivatorInstance().getImageFromURL("package"), "", "",
				null));
		parents.add(new OutputItem("Import", SearchActivator.getActivatorInstance().getImageFromURL("import"), "", "",
				null));
		parents.add(
				new OutputItem("Class", SearchActivator.getActivatorInstance().getImageFromURL("class"), "", "", null));
		parents.add(new OutputItem("Interface", SearchActivator.getActivatorInstance().getImageFromURL("interface"), "",
				"", null));
		parents.add(
				new OutputItem("Enum", SearchActivator.getActivatorInstance().getImageFromURL("enum"), "", "", null));
		parents.add(new OutputItem("Method", SearchActivator.getActivatorInstance().getImageFromURL("method"), "", "",
				null));
		parents.add(
				new OutputItem("Field", SearchActivator.getActivatorInstance().getImageFromURL("field"), "", "", null));
		return parents;
	}

	@Override
	public LinkedList<Item> getChildren(String parent) {
		switch (parent) {
		case "Package":
			return visitor.getASTVisitor_deepSearchMy().getPackageItems();
		case "Class":
			return visitor.getASTVisitor_deepSearchMy().getClassItems();
		case "Enum":
			return visitor.getASTVisitor_deepSearchMy().getEnumItems();
		case "Interface":
			return visitor.getASTVisitor_deepSearchMy().getInterfaceItems();
		case "Import":
			return visitor.getASTVisitor_deepSearchMy().getImportItems();
		case "Method":
			return visitor.getASTVisitor_deepSearchMy().getMethodItems();
		case "Field":
			return visitor.getASTVisitor_deepSearchMy().getFieldItems();
		default:
			return new LinkedList<Item>();
		}
	}

	@Override
	public void search(String text_Search, String text_SearchInCombo, String specificText_SearchInCombo,
			String text_SearchForCombo, ArrayList<String> buttonsSelected_SearchForCombo) {
		this.text_SearchInCombo = text_SearchInCombo;
		this.text_Search = text_Search;
		this.specificText_SearchInCombo = specificText_SearchInCombo;
		this.text_SearchForCombo = text_SearchForCombo;
		if (text_SearchForCombo != "") {
			searchFor(buttonsSelected_SearchForCombo);
		} else if (text_SearchInCombo != "") {
			searchIn();
		} else {
			visitor = new DeepSearchVisitor(text_Search, SearchEnumType.SearchInPackage, "");
			root_package.traverse(visitor);
		}
	}

	private void searchIn() {
		if (text_SearchInCombo.equals("Package")) {
			searchIn_orForPackage(SearchEnumType.SearchInPackage);
		} else if (text_SearchInCombo.equals("Class")) {
			visitor = new DeepSearchVisitor(text_Search, SearchEnumType.SearchInClass, "");
			searchInClass_orSearchFor();
		} else if (text_SearchInCombo.equals("Method")) {
			searchIn_orForMethod(SearchEnumType.SearchInMethod, "");
		}
	}

	private void searchFor(ArrayList<String> buttonsSelected) {
		if (text_SearchForCombo.equals("Package")) {
			searchIn_orForPackage(SearchEnumType.SearchForPackage);
		} else if (text_SearchForCombo.equals("TypeDeclaration")) {
			searchAdvanced(SearchEnumType.SearchForClass, buttonsSelected);
		} else if (text_SearchForCombo.equals("Method")) {
			searchAdvanced(SearchEnumType.SearchForMethod, buttonsSelected);
		} else if (text_SearchForCombo.equals("Field")) {
			searchAdvanced(SearchEnumType.SearchForField, buttonsSelected);
		}
	}

	private void searchAdvanced(SearchEnumType enumType, ArrayList<String> buttonsSelected) {
		if (buttonsSelected.size() > 0) {
			visitor = new DeepSearchVisitor(text_Search, enumType, buttonsSelected.get(0));
			for (String advancedSpecification : buttonsSelected) {
				visitor.getASTVisitor_deepSearchMy().setAdvancedSpecifications(advancedSpecification);
				searchInClass_orSearchFor();
			}
		} else {
			visitor = new DeepSearchVisitor(text_Search, enumType, "");
			searchInClass_orSearchFor();
		}
	}

	private void searchIn_orForMethod(SearchEnumType enumType, String advancedSpecifications) {
		visitor = new DeepSearchVisitor(text_Search, enumType, "");
		root_package.traverse(visitor);
	}

	private void searchIn_orForPackage(SearchEnumType enumType) {
		visitor = new DeepSearchVisitor(text_Search, enumType, "");
		if (isComboSearchFor_CoherentWith_ComboSearchIn()) {
			if (text_SearchInCombo != "" && !specificText_SearchInCombo.equals("")) {
				for (SourceElement sourcePackage : root_package.getChildren()) {
					String packageName = sourcePackage.getParent().getName() + "." + sourcePackage.getName();
					if (packageName.equals(specificText_SearchInCombo)) {
						((PackageElement) sourcePackage).traverse(visitor);
						break;
					}
				}
			} else {
				root_package.traverse(visitor);
			}
		}
	}

	private void searchInClass_orSearchFor() {
		if (isComboSearchFor_CoherentWith_ComboSearchIn()) {
			if (text_SearchInCombo.equals("Class") && !specificText_SearchInCombo.equals("")) {
				ClassElement classToVisit = visitor.getClass(specificText_SearchInCombo, root_package);
				visitor.visitClass(classToVisit);
			} else if (text_SearchInCombo.equals("Package") && !specificText_SearchInCombo.equals("")) {
				for (SourceElement sourcePackage : root_package.getChildren()) {
					String packageName = sourcePackage.getParent().getName() + "." + sourcePackage.getName();
					if (packageName.equals(specificText_SearchInCombo)) {
						((PackageElement) sourcePackage).traverse(visitor);
						break;
					}
				}
			} else {
				root_package.traverse(visitor);
			}
		}
	}

	private boolean isComboSearchFor_CoherentWith_ComboSearchIn() {
		if (text_SearchForCombo.equals("Package")) {
			if (text_SearchInCombo.equals("Package") || text_SearchInCombo.equals(""))
				return true;
			else
				return false;
		} else if (text_SearchForCombo.equals("TypeDeclaration")) {
			if (!text_SearchInCombo.equals("Method"))
				return true;
			else
				return false;
		} else if (text_SearchForCombo.equals("") || text_SearchForCombo.equals("Method")
				|| text_SearchForCombo.equals("Field"))
			return true;
		else
			return false;
	}

	@Override
	public void doubleClick(Item item) {
		if (item.getSpecialData() instanceof File) {
			editor_search.openFile((File) item.getSpecialData());
		}
	}

}
