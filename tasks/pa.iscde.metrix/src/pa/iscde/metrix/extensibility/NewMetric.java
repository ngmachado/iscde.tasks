package pa.iscde.metrix.extensibility;

import java.util.ArrayList;

/**
 * Representa uma nova metrica que pode ser adicionada á lista de metricas
 * Esta classe é accionada quando clicado no butão New Metric
 * Não tem construtor
 * 
 * @author Andre Carvalho
 *
 */
public interface NewMetric {
	
	/**
	 * Metodo para decidir o nome da nova metrica
	 * Não deve devolver um null uma string em branco. 
	 * @return nome da metrica
	 */
	public String metricName();
	
	/**
	 * Escolher o tipo de cálculo a ser usado entre metricas
	 * 
	 * @return Deve retornar um tipo de uma nova metrica
	 */
	public TypeNewMetric typeMetric();
	
	/**
	 * Metodo para escolher quais as métricas escolhidas para o cálculo 
	 * da nova métrica. 
	 * Deverá retornar uma array com pelo menos size > 1
	 * @return 
	 */
	public ArrayList<DefaultMetrics> targetMetrics(DefaultMetrics[] possibleMetrics);
	
	

}
