package extensions;

import java.util.ArrayList;
import java.util.Set;

import pt.iscde.classdiagram.extensibility.ClassDiagramFilter;
import pt.iscde.classdiagram.model.types.EChildElementType;
import pt.iscde.classdiagram.model.types.EModifierType;
import pt.iscde.classdiagram.model.types.ETopElementType;

public class FilterDiagramClass_Implementation implements ClassDiagramFilter {
	private boolean active = false;
	private static FilterDiagramClass_Implementation instance;
	private ArrayList<String> searchForTop = new ArrayList<String>();
	private ArrayList<String> searchForChild = new ArrayList<String>();
	private String childElementToSearch;
	private static final String METHOD = "Method";
	private static final String ATTRIBUTE = "Attribute";
	private static final String FIELD = "Field";
	private static final String TYPEDECLARATION = "TypeDeclaration";
	private static final String SUPERCLASS_TYPE = "SUPERCLASS";
	private static final String CLASS_TYPE = "CLASS";

	public FilterDiagramClass_Implementation() {
		instance = this;
		System.out.println(instance);
	}

	@Override
	public String getFilterName() {
		return "DeepSearch_Filter";
	}

	@Override
	public String getFilterShortDescription() {
		return "DeepSearch";
	}

	@Override
	public boolean acceptTopElement(ETopElementType type, String name, EModifierType visibility,
			Set<EModifierType> modifiers) {
		if (searchForTop.size() > 0)
			return searchForTop.contains(type.toString());
		return true;

	}

	@Override
	public boolean acceptCildElement(EChildElementType type, String name, EModifierType visibility,
			Set<EModifierType> modifiers, String returnType) {
		// return EModifierType.STATIC.equals(visibility);
		if (childElementToSearch != null) {
			if (searchForChild.size() > 0) {
				if (visibility != null) {
					System.out.println(visibility.toString() + " " + visibility.toString() + " " + visibility);

					return childElementToSearch.equals(type.toString())
							&& searchForChild.contains(visibility.toString());

				} else
					return childElementToSearch.equals(type.toString());
			} else
				return childElementToSearch.equals(type.toString());
		} else
			return false;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void activate() {
		active = true;
	}

	@Override
	public void deactivate() {
		active = false;
	}

	public static FilterDiagramClass_Implementation getInstance() {
		return instance;
	}

	public void setTypesToSearch(String name_ItemSelected, ArrayList<String> buttonsSelected) {
		searchForChild.clear();
		System.out.println("tamaanooo" + searchForChild.size());
		searchForTop.clear();
		System.out.println(name_ItemSelected);
		System.out.println(buttonsSelected.size());
		if (name_ItemSelected.equals(TYPEDECLARATION)) {
			searchForTop.clear();
			if (buttonsSelected.size() > 0) {
				for (String type : buttonsSelected) {
					if (type.equals("abstract")) {
						searchForTop.add("SUPERCLASS");
						searchForTop.add("CLASS");
					} else {
						searchForTop.add(type.toUpperCase());
					}
				}
			} else {
				System.out.println("entrei asas");
				searchForTop.add(SUPERCLASS_TYPE);
				searchForTop.add(CLASS_TYPE);
			}
		} else if (name_ItemSelected.equals(METHOD)) {
			if (buttonsSelected.size() > 0) {
				for (String type : buttonsSelected) {
					System.out.println("dfdfd" + type);
					searchForChild.add(type.toUpperCase());
				}
			}
			childElementToSearch = METHOD;
		} else if (name_ItemSelected.equals(FIELD)) {
			if (buttonsSelected.size() > 0) {
				for (String type : buttonsSelected) {
					searchForChild.add(type.toUpperCase());
				}
			}
			childElementToSearch = ATTRIBUTE;
		}
		activate();
	}

}
