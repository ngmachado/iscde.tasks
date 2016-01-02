package pa.iscde.metrix.internal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.HashMap;

/**
 * Class 
 * 
 */

class MetricAnalyzer {
	
	private HashMap<String, Integer> metrics = new HashMap<String, Integer>();
	private String[] inicialMetrics = new String[] {"Number of Lines", "Number of Methods", "Number of Constructors"
			, "Number of Fields", "Number of Comments", "Number of Characters" , "Number of Packages"};
	private MetrixView view;

	/**
	 * 
	 * @param file
	 */
	
	protected MetricAnalyzer(File file, MetrixView view) {
		this.view = view;
		metrics = view.getMetricsList();
		
		LineNumberReader lnr;
		try {
			lnr = new LineNumberReader(new FileReader(file));
			lnr.skip(Long.MAX_VALUE);
		
			metrics.remove(lnr.getLineNumber());
			metrics.put("Number of Lines", lnr.getLineNumber() + 1);
			 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public MetricAnalyzer() {}

	private HashMap<String, Integer> putInicialMetrics() {
		for (String m : inicialMetrics) {
			metrics.put(m, 0);
		}
		return metrics;
	}

	
	/**
	 * 
	 * @param string
	 * @return metrics
	 */
	protected int getNumbMetric(String string) {
		return metrics.get(string);
	}

	/**
	 * 
	 * @param m
	 */
	
	protected void incremetMetric(String m) {
		int v = metrics.get(m) + 1;
		
		metrics.remove(m);
		metrics.put(m, v);
		
	}
	
	/**
	 * 
	 * @return metrics
	 */
	
	protected HashMap<String, Integer> getMetrics() {
		return metrics;
	}
	
	/**
	 * 
	 * @return inicialMetrics
	 */
	
	protected String[] getInicialMetrics() {
		return inicialMetrics;
	}

	protected void addNewMetric(String name, int value) {
		view.addMetric(name,value);
		System.out.println("Colocou metrica " + name + " com value " + value);
	}

	protected HashMap<String, Integer> initializeMap() {
		return putInicialMetrics();
	}
	
	

}
