package composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

public abstract class AutoCompleteCombo {
	protected Combo comboBox_search;
	protected int itemSelected;
	protected boolean hasAlreadySelected;
	private Composite parent;
	protected String[] comboItems;
	private Label search_label;
	private String comboName;

	public AutoCompleteCombo(Composite parent, String comboName, String[] comboItems) {
		this.comboName = comboName;
		this.parent = parent;
		this.comboItems = comboItems;
		hasAlreadySelected = false;
		createContents();
	}

	private void createContents() {
		search_label = new Label(parent, SWT.NONE);
		search_label.setText(comboName);
		comboBox_search = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		comboBox_search.setItems(comboItems);
		comboBox_search.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		comboBox_search.addModifyListener(new ModifyListener() {

			@Override
			public void modifyText(ModifyEvent e) {
				if (!hasAlreadySelected) {
					autoCompleteText(comboBox_search.getText());
				} else {
					if (hasSelectedItemWithMouse()) {
						itemSelected = comboBox_search.getSelectionIndex();
					} else {
						int length_ofTextInserted = comboBox_search.getText().length();
						if (length_ofTextInserted < comboItems[itemSelected].length()) {
							if (length_ofTextInserted == 1)
								autoCompleteText(comboBox_search.getText());
							else {
								itemSelected = 0;
							}
						}
					}
				}
				comboBox_search.select(itemSelected);
				if (itemSelected == 0) {
					hasAlreadySelected = false;
				}
				showRelatedSpecifications();
			}
			
		});
	}

	abstract protected void showRelatedSpecifications();

	private void autoCompleteText(String textInserted) {
		if (hasSelectedItemWithMouse()) {
			itemSelected = comboBox_search.getSelectionIndex();
			if (itemSelected > 0) {
				hasAlreadySelected = true;
			}
		} else {
			if (textInserted.length() > 0) {
				for (int i = 1; i < comboItems.length; i++) {
					if (textInserted.equalsIgnoreCase(comboItems[i].substring(0, textInserted.length()))) {
						itemSelected = i;
						hasAlreadySelected = true;
						break;
					}
				}
				if (!hasAlreadySelected) {
					itemSelected = 0;
				}
			}
		}
	}

	private boolean hasSelectedItemWithMouse() {
		return comboBox_search.getSelectionIndex() != itemSelected && comboBox_search.getSelectionIndex() >= 0;
	}

	public void setVisible(boolean visibility) {
		if (!visibility) {
			comboBox_search.dispose();
			search_label.dispose();
		}
	}

	public Combo getComboBox_search() {
		return comboBox_search;
	}

	public void dispose() {
		comboBox_search.removeAll();
	}

}
