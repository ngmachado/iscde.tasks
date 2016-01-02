package pt.iscde.classdiagram.model;

import java.util.List;

import org.eclipse.draw2d.IFigure;

import pt.iscde.classdiagram.extensibility.ClassDiagramFilter;
import pt.iscde.classdiagram.extensibility.ClassDiagramStyler;
import pt.iscde.classdiagram.model.types.EModifierType;
import pt.iscde.classdiagram.model.types.ETopElementType;

/**
 * This interface should be implemented to contain the class diagram's top
 * elements (e.g. classes, interfaces,...)
 * 
 * @author joaocarias
 *
 */
public interface TopLevelElement {
	
	/**
	 * Gets the element unique ID
	 * 
	 * @return a {@link String} with this element ID
	 */
	public String getId();

	/**
	 * Gets this element name. For example, the Class name.
	 * @return a {@link String} with this element Name
	 */
	public String getName();

	
	/**
	 * Gets this element Type (Class, Abstract Class, Interface or Enum)
	 * @return this element's {@link ETopElementType} 
	 */
	public ETopElementType getClassType();

	
	/**
	 * Gets this element Type (Class, Abstract Class, Interface or Enum)
	 * @return
	 */
	public List<TopLevelElement> getConnectedTo();
	
	public void addAttribute(ChildElementTemplate childElement);
	
	public void addMethod(ChildElementTemplate childElement);
	
	public IFigure getFigure(ClassDiagramStyler styler);
	
	public void addMmodifier(EModifierType modifierType);

	/**
	 * Set the selected state
	 * @param value <code>true</code> to set <b>selected</b>, <code>false</code> to set <b>unselected</b>
	 */
	public void setSelected(boolean value);
	
	/**
	 * Gets the selected state
	 * @return <code>true</code> if the element is selected, <code>false</code> otherwise 
	 */
	public boolean isSelected();

	public void setFilters(List<ClassDiagramFilter> filters);

}

