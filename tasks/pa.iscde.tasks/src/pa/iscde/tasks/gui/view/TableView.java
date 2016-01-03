package pa.iscde.tasks.gui.view;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Map;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellLabelProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.ViewerCell;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import pa.iscde.tasks.extensibility.ITask;
import pa.iscde.tasks.integration.TaskSearch;
import pa.iscde.tasks.control.TasksActivator;
import pa.iscde.tasks.model.ModelProvider;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;

public class TableView implements PidescoView {

	private static final String[] HEADER_ARRAY = { "", "Type", "File", "Priority", "Line No", "Msg", "Source" };
	private static final int[] BOUNDS_ARRAY = { 25, 100, 100, 100, 100, 300, 300 };

	private static TableView instance;

	private TableViewer taskViewer;
	
	
	@Override
 	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		instance = this;
		taskViewer = buildTaskTable(viewArea);
		taskViewer.setContentProvider(ArrayContentProvider.getInstance());
		taskViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				final IStructuredSelection selection = taskViewer.getStructuredSelection();
				final ITask taskOcc = (ITask) selection.getFirstElement();
				if (taskOcc != null)  {
					JavaEditorServices jes = TasksActivator.getJavaEditorServices();
					ModelProvider.INSTANCE.ActionPerformFromProvider(jes, taskOcc);
				}
				//TasksActivator.getJavaEditorServices().openFile(new File(taskOcc.getAbsolutePath()));
			}
		});	
		
		final Table table = taskViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		final Menu contextMenu = generateMenu(table);
		taskViewer.getTable().setMenu(contextMenu);

		refresh();
	}

	/**
	 * Refreshes the view with the information from the model
	 */
	public void refresh() {
		taskViewer.setInput(ModelProvider.INSTANCE.getTasksList());
	}

	public static TableView getInstance() {
		return instance;
	}

	private TableViewer buildTaskTable(final Composite viewArea) {
		final TableViewer taskView = new TableViewer(viewArea,
				SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);

		//Image based on Type
		createColumn(taskView, HEADER_ARRAY[0], BOUNDS_ARRAY[0], 0).setLabelProvider(new CellLabelProvider() {
			
			@Override
			public void update(ViewerCell cell) {
				//Define image based on type 
				Image img;
				//Get path to image
				//String path = "/images/" + ((ITask) cell.getElement()).getType().getType() + ".png";
				try {
					//InputStream istream = getClass().getResourceAsStream(path);
					//use a default image to client TaskType
					InputStream istream = ((ITask)cell.getElement()).getType().getIconStream();
					//Default Image...
					if(istream == null)  {
						istream = getClass().getResourceAsStream("/images/refresh.gif");
					}
					
					img = new Image(viewArea.getDisplay(), istream);
					cell.setImage(img);						
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}	
			}
		});
		
		// Task Type
		createColumn(taskView, HEADER_ARRAY[1], BOUNDS_ARRAY[1], 1).setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((ITask) element).getType().getType();
			}
		});

		// File
		createColumn(taskView, HEADER_ARRAY[2], BOUNDS_ARRAY[2], 2).setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((ITask) element).getFileName();
			}
		});

		// Priority
		createColumn(taskView, HEADER_ARRAY[3], BOUNDS_ARRAY[3], 3).setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((ITask) element).getPriority().toString();
			}
		});

		// Line
		createColumn(taskView, HEADER_ARRAY[4], BOUNDS_ARRAY[4], 4).setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((ITask) element).getLineNo().toString();
			}
		});

		// msg
		createColumn(taskView, HEADER_ARRAY[5], BOUNDS_ARRAY[5], 5).setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((ITask) element).getMsg();
			}
		});
		
		// source
		createColumn(taskView, HEADER_ARRAY[6], BOUNDS_ARRAY[6], 6).setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ModelProvider.INSTANCE.getProviderID((ITask) element);
			}
		});

		return taskView;
	}

	private TableViewerColumn createColumn(TableViewer view, String title, int bound, final int colNumber) {
		final TableViewerColumn viewerColumn = new TableViewerColumn(view, SWT.NONE);
		final TableColumn column = viewerColumn.getColumn();
		column.setText(title);
		column.setWidth(bound);
		column.setResizable(true);
		column.setMoveable(true);
		return viewerColumn;
	}

	private Menu generateMenu(Table table) {
		final Menu menu = new Menu(table);
		final MenuItem refreshMenu = new MenuItem(menu, SWT.CASCADE);
		refreshMenu.setText("Refresh");
		refreshMenu.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				refresh();
			}
		});

		final MenuItem priorityMenu = new MenuItem(menu, SWT.CASCADE);
		priorityMenu.setText("Priority");

		final Menu prioritySubMenu = new Menu(menu);
		priorityMenu.setMenu(prioritySubMenu);

		 

		return menu;
	}

	/*private void startListenToSearchEvent()  {
		ISearchEvent sevent = TasksActivator.getDeepSearchServices();
		sevent.addListener(new ISearchEventListener() {
			
			@Override
			public void widgetSelected(String text_Search, String text_SearchInCombo, String specificText_SearchInCombo,
					String text_SearchForCombo, ArrayList<String> buttonsSelected_SearchForCombo) {
				
				//Find if the search use type of tasks
				if(text_Search.equals("Bug"))
					System.out.println("Search event in TableView Key:" + text_Search);
				
			}
		});
		
	} */
}
