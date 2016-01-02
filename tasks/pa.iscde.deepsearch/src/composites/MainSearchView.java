package composites;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;

import activator.SearchActivator;
import extensionpoints.ISearchEventListener;
import extensionpoints.Item;
import extensionpoints.OutputPreview;
import extensions.FilterDiagramClass_Implementation;
import extensions.MCGraphFilter_Implementation;
import implementation.OutputItem;
import pa.iscde.mcgraph.service.McGraphServices;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class MainSearchView implements PidescoView {

	private static MainSearchView MAIN_SEARCH_VIEW_INSTANCE;

	private JavaEditorServices editor_search;

	private McGraphServices graph_services;

	private SearchComposite search_composite;
	private PreviewComposite preview_composite;
	private AdvancedComposite advanced_composite;

	private String searched_data;
	private boolean advancedButtonIsSelected;

	private LinkedList<OutputPreview> extensionResult = new LinkedList<OutputPreview>();

	private LinkedList<String> parent_results = new LinkedList<String>();
	private Map<String, LinkedList<Item>> results = new HashMap<String, LinkedList<Item>>();

	public MainSearchView() {
		MAIN_SEARCH_VIEW_INSTANCE = this;
	}

	@Override
	public void createContents(final Composite viewArea, Map<String, Image> imageMap) {

		editor_search = SearchActivator.getActivatorInstance().getEditorService();

		graph_services = SearchActivator.getActivatorInstance().getGraph_services();

		viewArea.setLayout(new FillLayout(SWT.VERTICAL));

		search_composite = new SearchComposite(viewArea, SWT.BORDER);
		preview_composite = new PreviewComposite(viewArea, SWT.BORDER);

		checkExtensionsOutput();

		search_composite.getSearchButton().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				preview_composite.getPreview().setText("");
				preview_composite.getHierarquies().removeAll();
				searched_data = search_composite.getSearchField().getText();
				if (advancedButtonIsSelected) {
					if (FilterDiagramClass_Implementation.getInstance() != null)
						FilterDiagramClass_Implementation.getInstance().setTypesToSearch(
								advanced_composite.getComboSearchFor().getName_ItemSelected(),
								advanced_composite.getComboSearchFor().getButtonsSelected());
				}
				for (OutputPreview o : extensionResult) {
					if (advancedButtonIsSelected) {
						o.search(searched_data, search_composite.getSearchIn().getName_ItemSelected(),
								search_composite.getSearchIn().getText_ofSearchSpecific(),
								advanced_composite.getComboSearchFor().getName_ItemSelected(),
								advanced_composite.getComboSearchFor().getButtonsSelected());
						for (ISearchEventListener l : SearchActivator.getActivatorInstance().getListeners()) {
							l.widgetSelected(searched_data, search_composite.getSearchIn().getName_ItemSelected(),
									search_composite.getSearchIn().getText_ofSearchSpecific(),
									advanced_composite.getComboSearchFor().getName_ItemSelected(),
									advanced_composite.getComboSearchFor().getButtonsSelected());
						}
						advanced_composite.getComboSearchFor().clearSelected();
					} else {
						o.search(searched_data, search_composite.getSearchIn().getName_ItemSelected(),
								search_composite.getSearchIn().getText_ofSearchSpecific(), "", new ArrayList<String>());
						for (ISearchEventListener l : SearchActivator.getActivatorInstance().getListeners()) {
							l.widgetSelected(searched_data, search_composite.getSearchIn().getName_ItemSelected(),
									search_composite.getSearchIn().getText_ofSearchSpecific(), "",
									new ArrayList<String>());
						}
					}
					createTree(o);
				}
			}
		});
		search_composite.getAdvanced().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (search_composite.getAdvanced().getSelection()) {
					advancedButtonIsSelected = true;
					advanced_composite = new AdvancedComposite(viewArea, SWT.BORDER);
					advanced_composite.moveAbove(preview_composite);
					viewArea.layout();
				} else {
					advanced_composite.dispose();
					advancedButtonIsSelected = false;
					viewArea.layout();
				}
			}

		});
		preview_composite.getHierarquies().addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				for (@SuppressWarnings("unused")
				OutputPreview o : extensionResult) {
					TreeItem tree_item = (TreeItem) e.item;
					OutputItem item = new OutputItem(tree_item.getText(), tree_item.getImage(),
							tree_item.getData("previewText").toString(),
							tree_item.getData("highlightedText").toString(), tree_item.getData("SpecialData"));
					if (item.getPreviewText() != "") {
						preview_composite.styleText(item.getPreviewText(), item.getHighlightText(), searched_data);
					}
					if (MCGraphFilter_Implementation.getInstance() != null) {
						if (tree_item.getParentItem().getText().equals("Class")
								|| tree_item.getParentItem().getText().equals("Interface")
								|| tree_item.getParentItem().getText().equals("Enum")) {
							graph_services.deactivateFilter("MCFilter");
							MCGraphFilter_Implementation.getInstance().addToSearchables(tree_item.getText() + ".java",
									"Class");
							graph_services.activateFilter("MCFilter");
						} else if (tree_item.getParentItem().getText().equals("Method")) {
							graph_services.deactivateFilter("MCFilter");
							MCGraphFilter_Implementation.getInstance().addToSearchables(tree_item.getText(), "");
							graph_services.activateFilter("MCFilter");
						}
					}
				}
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				for (OutputPreview o : extensionResult) {
					TreeItem tree_item = (TreeItem) e.item;
					OutputItem item = new OutputItem(tree_item.getText(), tree_item.getImage(),
							tree_item.getData("previewText").toString(),
							tree_item.getData("highlightedText").toString(), tree_item.getData("SpecialData"));
					o.doubleClick(item);
				}
			}

		});
	}

	private void createTree(OutputPreview outputPreview) {
		for (Item parent : outputPreview.getParents()) {
			TreeItem newParent = new TreeItem(preview_composite.getHierarquies(), 0);
			newParent.setText(parent.getName());
			if (parent.getImg() != null) {
				newParent.setImage(parent.getImg());
			}
			newParent.setData("previewText", parent.getPreviewText());
			newParent.setData("highlightedText", parent.getHighlightText());
			for (Item child : outputPreview.getChildren(parent.getName())) {
				TreeItem newChild = new TreeItem(newParent, 0);
				newChild.setText(child.getName());
				newChild.setImage(child.getImg());
				if (child.getPreviewText() != null)
					newChild.setData("previewText", child.getPreviewText());
				else
					newChild.setData("previewText", "");
				if (child.getHighlightText() != null)
					newChild.setData("highlightedText", child.getHighlightText());
				else
					newChild.setData("highlightedText", "");
				newChild.setData("SpecialData", child.getSpecialData());
			}
		}
	}

	private void checkExtensionsOutput() {
		IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pa.iscde.deepsearch.output_preview");
		IExtension[] extensions = extensionPoint.getExtensions();
		for (IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for (IConfigurationElement c : confElements) {
				try {
					extensionResult.add((OutputPreview) c.createExecutableExtension("class"));
				} catch (CoreException e1) {
					e1.printStackTrace();
				}

			}
		}
	}

	public static MainSearchView getInstance() {
		return MAIN_SEARCH_VIEW_INSTANCE;
	}

	public JavaEditorServices getEditorSearch() {
		return editor_search;
	}

	public String getDataSearch() {
		return searched_data;
	}

	public Map<String, LinkedList<Item>> getResults() {
		return results;
	}

	public LinkedList<String> getParentResults() {
		return parent_results;
	}

}
