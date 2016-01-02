package extensionpoints;

import org.eclipse.swt.graphics.Image;

/**
 * 
 * @author Rita Ramos
 *
 */
public interface Item {

	/**
	 * Defines the mandatory items to be shown on the preview when selecting
	 * (one-click) the Item´
	 */
	public void setItem(String name_item, String text_Preview, String text_Highlighted);

	/**
	 * Obtains the mandatory attribute item_name
	 * 
	 * @return name_item (non-null)
	 */
	public String getName();

	/**
	 * Obtains the mandatory attribute text_Preview to be shown on the preview
	 * when selecting (one-click) the Item
	 * 
	 * @return text_Preview (non-null)-> (ex:the content of a class)
	 */
	public String getPreviewText();

	/**
	 * Obtains the mandatory attribute text_Highlighted to be shown on the
	 * preview when selecting (one-click) the Item
	 * 
	 * @return text_Highlighted (non-null)->(ex: being the text_Preview the
	 *         content of class, the text_Highlighted can be the name of the
	 *         class)
	 */
	public String getHighlightText();

	/**
	 * Defines the image of the Item
	 * 
	 * @param Image
	 */
	public void setImg(Image image);

	/**
	 * Obtains the image of the Item (can be null if no Image was defined)
	 * 
	 * @return Image
	 */
	public Image getImg();

	/**
	 * Defines a special_data for the Item
	 * 
	 * @return Object -> a specialData defined of the Item that can be used when
	 *         double-clicking it (ex: File of a class)
	 */
	public void setSpecialData(Object special_data);

	/**
	 * Obtains the special_data of the Item (can be null if no Object was
	 * defined)
	 * 
	 * @param Object
	 *            -> a specialData defined of the Item that can be used when
	 *            double-clicking it (ex: File of a class)
	 */
	public Object getSpecialData();

}