package pt.iscde.classdiagram.internal;

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;

import pt.iscde.classdiagram.extensibility.ClassDiagramStyler;

class DefaultStyler implements ClassDiagramStyler {

	@Override
	public Color getBackgroundColor() {
		return new Color(null, 255, 255, 206);
	}

	@Override
	public Color getSelectedBackgroundColor() {
		return  new Color(null, 240, 240, 255);
	}

	@Override
	public Color getForegroundColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Color getSelectedForegroundColor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Font getTitleFont() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Font getAttributesFont() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Font getMethodsFont() {
		// TODO Auto-generated method stub
		return null;
	}

}
