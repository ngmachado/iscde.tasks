package pa.iscde.metrix.extensibility;

import java.util.ArrayList;

/**
 * Representa uma nova metrica que pode ser adicionada � lista de metricas
 * Esta classe � accionada quando clicado no but�o New Metric
 * N�o tem construtor
 * 
 * @author Andre Carvalho
 *
 */
public interface NewMetric {
	
	/**
	 * Metodo para decidir o nome da nova metrica
	 * N�o deve devolver um null uma string em branco. 
	 * @return nome da metrica
	 */
	public String metricName();
	
	/**
	 * Escolher o tipo de c�lculo a ser usado entre metricas
	 * 
	 * @return Deve retornar um tipo de uma nova metrica
	 */
	public TypeNewMetric typeMetric();
	
	/**
	 * Metodo para escolher quais as m�tricas escolhidas para o c�lculo 
	 * da nova m�trica. 
	 * Dever� retornar uma array com pelo menos size > 1
	 * @return 
	 */
	public ArrayList<DefaultMetrics> targetMetrics(DefaultMetrics[] possibleMetrics);
	
	

}
