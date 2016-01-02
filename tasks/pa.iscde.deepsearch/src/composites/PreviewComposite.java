package composites;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;

public class PreviewComposite extends Composite {

	private Tree hierarquies;
	private StyledText preview;

	public PreviewComposite(Composite parent, int style) {
		super(parent, style);
		createContents();
	}

	private void createContents() {
		setLayout(new GridLayout(2, true));

		hierarquies = new Tree(this, SWT.IMAGE_GIF | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		hierarquies.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

		preview = new StyledText(this, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		preview.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));
		preview.setBackground(new Color(getDisplay(), 255, 255, 255));
		preview.setEditable(false);
	}

	public void styleText(String full, String result, String data_search) {
		getPreview().setText(full);
		if (!data_search.equals("")) {
			for (int i = result.indexOf(data_search.toLowerCase()); i >= 0; i = result
					.indexOf(data_search.toLowerCase(), i + 1)) {
				getPreview().setStyleRange(new StyleRange(full.toLowerCase().indexOf(result) + i, data_search.length(),
						null, getDisplay().getSystemColor(SWT.COLOR_GREEN)));
			}
		}
	}

	public Tree getHierarquies() {
		return hierarquies;
	}

	public StyledText getPreview() {
		return preview;
	}

}
