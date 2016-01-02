package extensionpoints;

import java.util.ArrayList;
import java.util.Collection;

/**
 * 
 * @author Rita Ramos
 *
 *         ExtensionPoint for populating results from the search and show them
 *         in preview for each selection/double-click
 */
public interface OutputPreview {

	/**
	 * Invoked when button is selected and gives the information that was used
	 * in the search
	 * 
	 * @param text_Search
	 *            (non-null) Text of the Search field
	 * 
	 * @param text_SearchInCombo
	 *            Text of the SearchInCombo field (if empty is represented by
	 *            "")
	 * @param specificText_SearchInCombo
	 *            specific class or package selected for SearchIn
	 * 
	 * @param text_SearchForCombo
	 *            (non-null) Text of the SearchForCombo field (if empty is
	 *            represented by "")
	 * 
	 * @param buttonsSelected_SearchForCombo
	 *            (can be empty, if no button selected) Represents the buttons
	 *            selected of the SearchForCombo ex:interface, abstract and enum
	 *            for text_SearchForCombo field with Class
	 * 
	 */

	public void search(String text_Search, String text_SearchInCombo, String specificText_SearchInCombo,
			String text_SearchForCombo, ArrayList<String> buttonsSelected_SearchForCombo);

	/**
	 * Obtains the parents for output hierarchy
	 * 
	 * @return Collection<Item> (non-null) parent Items (ex: Package)
	 */
	public Collection<Item> getParents();

	/**
	 * Obtains the children of the parent Item
	 * 
	 * @param parent
	 *            (non-null) parent Item (ex:Package)
	 * @return Collection<Item> (non-null) children of the parent Item (ex:
	 *         pt.iscte.pidesco.javaeditor)
	 */
	public Collection<Item> getChildren(String parent);

	/**
	 * Defines what is to be shown when double-clicking the Item
	 * 
	 * @return Item selected with double-click. The attributes of the Item (ex:
	 *         getImage) can be used to what should be shown when
	 *         double-clicking the Item
	 */
	public void doubleClick(Item item);

}
