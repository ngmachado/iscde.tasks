package pa.iscde.metrix.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;

class TableTree {
	
	private Tree tree;
	private String[] metrics = new String[] {"Number of Lines", "Number of Methods", "Number of Constructors"
			, "Number of Fields", "Number of Comments", "Number of Characters" , "Number of Packages"};
	private ArrayList<TreeItem> listItems = new ArrayList<TreeItem>();
	private ArrayList<TreeItem> listPackages = new ArrayList<TreeItem>();
	private ArrayList<String> packages = new ArrayList<String>();


	protected TableTree(Composite viewArea) {
		 tree = new Tree(viewArea, SWT.BORDER | SWT.FILL | SWT.FILL);
		 tree.setHeaderVisible(true);
	}
	
	protected void init(String metricColumn, String valueColumn) {
		addColumn(metricColumn);
		addColumn(valueColumn);
		addItems();
	}
	
	private void addColumn(String name) {
		TreeColumn column1 = new TreeColumn(tree, SWT.LEFT);
	    column1.setText(name);
	    column1.setWidth(300);
	}
	
	private void addItems() {
		for (int i = 0; i < metrics.length ; i++) {
		      TreeItem item = new TreeItem(tree, SWT.NONE);
		      item.setText(new String[] { metrics[i], "" + 0 });
		      listItems.add(item);
		    }
	}
	
	protected void updateTable(MetricAnalyzer metric) {
		tree.clearAll(true);
		HashMap< String, Integer> map = metric.getMetrics();
		int i = 0;
		for ( Map.Entry<String, Integer> entry : map.entrySet()) {
			(listItems.get(i)).setText(new String[] { entry.getKey(), entry.getValue() + "", });
			i++;
			System.out.println(entry.getKey() + " CCCCCC " + entry.getValue() );
		}
//		for (int i = 0; i < map.size(); i++) {
//			(listItems.get(i)).setText(new String[] { metric.getInicialMetrics()[i], map.get(metric.getInicialMetrics()[i]) + "", });
//		}
		
	}

	protected void addSubtring(String path, String root) {
		if (root.equals("")) {
			System.out.println("*" + root + "*");
			packages.add(path);
			for (TreeItem ti : listItems) {
				TreeItem item = new TreeItem(ti, SWT.NONE);
				item.setText(new String[] {path, "0"});
				listPackages.add(item);
			}
		}
		if(packages.contains(root)) {
			for (TreeItem ti : listItems) {
				for(TreeItem t : listPackages) {
					if (t.getText(0).equals(root)) {
						TreeItem item = new TreeItem(ti, SWT.NONE);
						item.setText(new String[] {path, "0"});
						listPackages.add(item);
					}
				}
			}
			System.out.println("SIM: " + path);
		}
	}

	protected void updatePackages() {
		tree.clearAll(true);
		for (int i = 0; i < metrics.length; i++) {
			(listItems.get(i)).setText(new String[] { metrics[i], "" });
			for (TreeItem s : listPackages) {
				s.setText(new String[] {s.getText(0), ""});
			}
		}
	}

	protected void addNewMetric(String name, int value) {
		 TreeItem item = new TreeItem(tree, SWT.NONE);
	      item.setText(new String[] {name, "" + value });
	      listItems.add(item);
	}

	

	

	

}
