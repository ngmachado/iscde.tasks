package extensionpoints;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

/**
 * 
 * @author Filipe Caçador
 *
 *         Service to be applied to get either results from after the Search
 *         Button is select (null if otherwise) or parameters used in the Searchs
 */
public interface ISearchEvent {

	/**
	 * Returns all the elements existing in SearchInCombo
	 * 
	 * @return String[] SearchInCombo Elements
	 */
	public String[] getElements_SearchInCombo();

	/**
	 * Returns all the elements existing in the SearchSpecificCombo if it is not
	 * disposed, else returns null
	 * 
	 * @return String[] SearchSpecificCombo Elements
	 */
	public String[] getSearchSpecificElements_SearchInCombo();

	/**
	 * Checks if SearchSpecificCombo is Disposed
	 * 
	 * @return boolean SearchSpecificCombo is Disposed
	 */
	public boolean isDisposedSearchSpecific_SearchInCombo();

	/**
	 * Checks if AdvancedButton is checked
	 * 
	 * @return boolean AdvancedButton checked
	 */
	public boolean isAdvancedButtonSelected();

	/**
	 * Returns all the elements existing is the SearchForCombo if AdvancedButton
	 * is checked, else returns null
	 * 
	 * @return String[] SearchForCombo Elements
	 */
	public String[] getElements_SearchForCombo();

	/**
	 * Returns all Selected Buttons of SearchForCombo if AdvancedButton is
	 * checked and selected buttons are not disposed, else returns null
	 * 
	 * @return String[] SelectedButtons of SearchForCombo
	 */
	public String[] getButtonsSelected_SearchForCombo();

	/**
	 * Checks if SearchForCombo Buttons are disposed
	 * 
	 * @return boolean SearchForCombo Buttons are disposed
	 */
	public boolean isDisposedButtons_SearchForCombo();

	/**
	 * Returns all Items per Parent
	 * 
	 * @return Map<String, LinkedList<Item>> Results per parent
	 */
	public Map<String, LinkedList<Item>> getResults();

	/**
	 * Returns only the result parents
	 * 
	 * @return Collection<String> Collection of parent results
	 */
	public Collection<String> getResultParents();

	/**
	 * Adds a new ISearchEventListener listener
	 * 
	 * @param listener
	 *            (non-null) Listener listener
	 */
	public void addListener(ISearchEventListener listener);

	/**
	 * Removes a ISearchEventListener listener
	 * 
	 * @param listener
	 *            (non-null) Listener listener
	 */
	public void removeListener(ISearchEventListener listener);

}
