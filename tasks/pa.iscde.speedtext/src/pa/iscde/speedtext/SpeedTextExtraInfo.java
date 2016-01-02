package pa.iscde.speedtext;


import java.io.File;
import java.util.Collection;
/**
 * Represents an extension point that adds information to the sugestion list
 */
public interface SpeedTextExtraInfo {
	
	/**
	 * Receives the sugestion list and adds information about each element
	 * @param list Collection of strings to add information
	 * @param file(class) with the methods/variables of the list
	 * @return list of new information for methods/variables, must have the same size and order as the param list
	 */
	public Collection<String> extraInfo(Collection<String> list, File file);
}
