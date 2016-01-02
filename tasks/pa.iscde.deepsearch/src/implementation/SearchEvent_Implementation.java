package implementation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.runtime.Assert;

import activator.SearchActivator;
import composites.AdvancedComposite;
import composites.MainSearchView;
import composites.SearchComposite;
import extensionpoints.ISearchEvent;
import extensionpoints.ISearchEventListener;
import extensionpoints.Item;

public class SearchEvent_Implementation implements ISearchEvent {

	@Override
	public void addListener(ISearchEventListener listener) {
		Assert.isNotNull(listener, "argument cannot be null");
		SearchActivator.getActivatorInstance().addListener(listener);
	}

	@Override
	public void removeListener(ISearchEventListener listener) {
		Assert.isNotNull(listener, "argument cannot be null");
		SearchActivator.getActivatorInstance().removeListener(listener);
	}

	@Override
	public String[] getElements_SearchInCombo() {
		return SearchComposite.getSearchCompositeInstance().getSearchIn().getComboBox_search().getItems();
	}

	@Override
	public String[] getSearchSpecificElements_SearchInCombo() {
		if (!isDisposedSearchSpecific_SearchInCombo()) {
			return SearchComposite.getSearchCompositeInstance().getSearchIn().getComboBox_searchSpecific().getItems();
		}
		return null;
	}

	@Override
	public boolean isAdvancedButtonSelected() {
		return SearchComposite.getSearchCompositeInstance().getAdvanced().getSelection();
	}

	@Override
	public boolean isDisposedSearchSpecific_SearchInCombo() {
		return SearchComposite.getSearchCompositeInstance().getSearchIn().getComboBox_searchSpecific().isDisposed();
	}

	@Override
	public String[] getElements_SearchForCombo() {
		if (isAdvancedButtonSelected()) {
			return AdvancedComposite.getAdvancedInstance().getComboSearchFor().getComboBox_search().getItems();
		}
		return null;
	}

	@Override
	public String[] getButtonsSelected_SearchForCombo() {
		if (isAdvancedButtonSelected() && !isDisposedButtons_SearchForCombo()) {
			return AdvancedComposite.getAdvancedInstance().getComboSearchFor().getMyButtons();
		}
		return null;
	}

	@Override
	public boolean isDisposedButtons_SearchForCombo() {
		return AdvancedComposite.getAdvancedInstance().getComboSearchFor().getIsDisposed();
	}

	@Override
	public Map<String, LinkedList<Item>> getResults() {
		return MainSearchView.getInstance().getResults();
	}

	@Override
	public Collection<String> getResultParents() {
		return MainSearchView.getInstance().getParentResults();
	}

}
