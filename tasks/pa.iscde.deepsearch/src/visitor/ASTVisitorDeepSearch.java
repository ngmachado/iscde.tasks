package visitor;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import activator.SearchActivator;
import enums.SearchEnumType;
import extensionpoints.Item;
import implementation.OutputItem;

public class ASTVisitorDeepSearch extends ASTVisitor {

	private String searchText;
	private String myEnum;
	private String packageName = "";
	private String advancedSpecification;
	private String full_class;
	private File file;
	private LinkedList<Item> packageItems = new LinkedList<Item>();
	private LinkedList<Item> classItems = new LinkedList<Item>();
	private LinkedList<Item> interfaceItems = new LinkedList<Item>();
	private LinkedList<Item> enumItems = new LinkedList<Item>();
	private LinkedList<Item> importItems = new LinkedList<Item>();
	private LinkedList<Item> methodItems = new LinkedList<Item>();
	private LinkedList<Item> fieldItems = new LinkedList<Item>();

	public ASTVisitorDeepSearch(String searchText, SearchEnumType myEnm, String advancedSpecification) {
		this.searchText = searchText.toLowerCase();
		this.myEnum = myEnm.toString();
		this.advancedSpecification = advancedSpecification;
	}

	public void setFile(File file) {
		this.file = file;
	}

	@Override
	public boolean visit(PackageDeclaration node) {
		if (isToSearchFor_orIn_Pachage() && !packageName.equals("" + node.getName())) {
			String search_result = "" + node.getName();
			if (search_result.contains(searchText)) {
				packageName = "" + node.getName();
				packageItems.add(
						new OutputItem(packageName, SearchActivator.getActivatorInstance().getImageFromURL("package"),
								node.toString(), searchText, file));
			}
		}
		return true;
	}

	@Override
	public boolean visit(TypeDeclaration node) {
		full_class = node.toString();
		if (searcForClass_orInPachage_orClass()) {
			String search_result = node.modifiers().toString() + node.getName();
			if (node.getSuperclassType() != null)
				search_result += " extends " + node.getSuperclassType();
			if (node.superInterfaceTypes().size() > 0)
				search_result += " implements " + node.superInterfaceTypes().toString();

			String result = node.toString();
			if (!(node.getParent() instanceof TypeDeclaration)) {
				full_class = result;
			}
			if (search_result.toLowerCase().contains(searchText)) {
				String typeDeclarationName = node.getName() + "";
				if (isArgumentDefined(advancedSpecification)) {
					if ((search_result).contains(advancedSpecification)) {
						classItems.add(new OutputItem(typeDeclarationName,
								SearchActivator.getActivatorInstance().getImageFromURL("class"), node.toString(),
								searchText, file));
					} else if (advancedSpecification.equals("interface") && node.isInterface()) {
						interfaceItems.add(new OutputItem(typeDeclarationName,
								SearchActivator.getActivatorInstance().getImageFromURL("interface"), node.toString(),
								searchText, file));
					}
				} else {
					if (node.isInterface()) {
						interfaceItems.add(new OutputItem(typeDeclarationName,
								SearchActivator.getActivatorInstance().getImageFromURL("interface"), node.toString(),
								searchText, file));
					} else {
						classItems.add(new OutputItem(typeDeclarationName,
								SearchActivator.getActivatorInstance().getImageFromURL("class"), node.toString(),
								searchText, file));
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean visit(ImportDeclaration node) {
		if (isToSearchInPackage_orClass()) {
			String importName = node.toString();
			if (importName.contains(searchText)) {
				importItems.add(
						new OutputItem(importName, SearchActivator.getActivatorInstance().getImageFromURL("import"),
								importName, searchText, ""));
			}
		}
		return true;
	}


	@Override
	public boolean visit(EnumDeclaration node) {
		if (isToSearchInPackage_orClass() || isToSearchForClass_or_InClass_orPackage()) {
			String enumName = node.getName() + "";
			if (enumName.toLowerCase().contains(searchText)) {
				if (isArgumentDefined(advancedSpecification)) {
					if (advancedSpecification.equals("enum")) {
						enumItems.add(
								new OutputItem(enumName, SearchActivator.getActivatorInstance().getImageFromURL("enum"),
										node.toString(), searchText, file));
					}
				} else {
					enumItems.add(
							new OutputItem(enumName, SearchActivator.getActivatorInstance().getImageFromURL("enum"),
									node.toString(), searchText, file));
				}
			}
		}
		return true;
	}

	@Override
	public boolean visit(FieldDeclaration node) {
		if (isToSearchForField_orInPachage_orClass()) {
			String fielName = node.fragments().get(0).toString().split("=")[0] + "";
			if (fielName.toLowerCase().contains(searchText)) {
				if (isArgumentDefined(advancedSpecification)) {
					if (node.modifiers().toString().contains(advancedSpecification)) {
						fieldItems.add(new OutputItem(fielName,
								SearchActivator.getActivatorInstance().getImageFromURL("field"), full_class, searchText,
								file));
					}
				} else {
					fieldItems.add(
							new OutputItem(fielName, SearchActivator.getActivatorInstance().getImageFromURL("field"),
									full_class, searchText, file));
				}
			}
		}
		return true;
	}

	@Override
	public boolean visit(MethodDeclaration node) {
		if (isToSearchForMethod_or_InMethod_Class_orPackage()) {
			String methodName = node.getName() + "";
			if (methodName.toLowerCase().contains(searchText)) {
				if (isArgumentDefined(advancedSpecification)) {
					if (node.modifiers().toString().contains(advancedSpecification)) {
						methodItems.add(new OutputItem(methodName,
								SearchActivator.getActivatorInstance().getImageFromURL("method"), full_class,
								searchText, file));
					}
				} else {
					methodItems.add(
							new OutputItem(methodName, SearchActivator.getActivatorInstance().getImageFromURL("method"),
									full_class, searchText, file));
				}
			}
		}
		return true;
	}

	@Override
	public boolean visit(Block node) {
		if (isToSearchInMethod_Class_orPackage()) {
			String search_result = "";
			if (node.statements().size() > 0) {
				search_result += workStatement(node.statements());
			}
			if (search_result.contains(searchText)) {
				// ??? Fazer
			}
		}
		return true;
	}

	private String workStatement(List<?> stats) {
		for (int i = 0; i < stats.size(); i++) {
			if (stats.get(i) instanceof Block) {
				Block b = (Block) stats.get(i);
				if (b.statements().size() > 0) {
					workStatement(b.statements());
				} else {
					return b.toString();
				}
			} else {
				return stats.get(i).toString();
			}
		}
		return "";
	}

	public void setAdvancedSpecifications(String advancedSpecification) {
		this.advancedSpecification = advancedSpecification;
	}

	private boolean isToSearchForMethod_or_InMethod_Class_orPackage() {
		return myEnum.equals(SearchEnumType.SearchForMethod.toString()) || isToSearchInMethod_Class_orPackage();
	}

	private boolean isToSearchForClass_or_InClass_orPackage() {
		return myEnum.equals(SearchEnumType.SearchForClass.toString()) || isToSearchInPackage_orClass();
	}

	private boolean isToSearchInMethod_Class_orPackage() {
		return myEnum.equals(SearchEnumType.SearchInMethod.toString()) || isToSearchInPackage_orClass();
	}

	private boolean isToSearchInPackage_orClass() {
		return myEnum.equals(SearchEnumType.SearchInPackage.toString())
				|| myEnum.equals(SearchEnumType.SearchInClass.toString());
	}

	private boolean isArgumentDefined(String searchFor) {
		return !searchFor.equals("");
	}

	private boolean isToSearchFor_orIn_Pachage() {
		return (myEnum.equals(SearchEnumType.SearchForPackage.toString())
				|| myEnum.equals(SearchEnumType.SearchInPackage.toString()));
	}

	private boolean searcForClass_orInPachage_orClass() {
		return myEnum.equals(SearchEnumType.SearchForClass.toString()) || isToSearchInPackage_orClass();
	}

	private boolean isToSearchForField_orInPachage_orClass() {
		return myEnum.equals(SearchEnumType.SearchForField.toString()) || isToSearchInPackage_orClass();
	}

	public LinkedList<Item> getPackageItems() {
		return packageItems;
	}

	public LinkedList<Item> getFieldItems() {
		return fieldItems;
	}

	public LinkedList<Item> getMethodItems() {
		return methodItems;
	}

	public LinkedList<Item> getImportItems() {
		return importItems;
	}

	public LinkedList<Item> getInterfaceItems() {
		return interfaceItems;
	}

	public LinkedList<Item> getEnumItems() {
		return enumItems;
	}

	public LinkedList<Item> getClassItems() {
		return classItems;
	}
}
