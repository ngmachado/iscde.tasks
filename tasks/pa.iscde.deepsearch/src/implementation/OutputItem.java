package implementation;

import java.io.File;

import org.eclipse.swt.graphics.Image;

import extensionpoints.Item;

public class OutputItem implements Item {
	private String name;
	private Image img;
	private String text_ToShowOnPreview;
	private String text_ToHightlightOnPreview;
	private Object f;

	public OutputItem(String name_item, Image image, String text_Preview, String text_Highlighted, Object F) {
		this.name = name_item;
		this.img = image;
		this.text_ToShowOnPreview = text_Preview;
		this.text_ToHightlightOnPreview = text_Highlighted;
		this.f = F;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Image getImg() {
		return img;
	}

	@Override
	public String getPreviewText() {
		return text_ToShowOnPreview;
	}

	@Override
	public String getHighlightText() {
		return text_ToHightlightOnPreview;
	}

	@Override
	public Object getSpecialData() {
		return f;
	}

	@Override
	public void setSpecialData(Object obj) {
		this.f = (File) obj;
	}

	@Override
	public void setItem(String name_item, String text_Preview, String text_Highlighted) {
		this.name = name_item;
		this.text_ToShowOnPreview = text_Preview;
		this.text_ToHightlightOnPreview = text_Highlighted;
	}

	@Override
	public void setImg(Image img) {
		this.img = img;
	}

}
