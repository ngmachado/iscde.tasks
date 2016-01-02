package pt.iscde.classdiagram.internal;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.zest.core.viewers.GraphViewer;
import org.eclipse.zest.layouts.LayoutAlgorithm;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.CompositeLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.HorizontalShift;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

import pt.iscde.classdiagram.extensibility.ClassDiagramAction;
import pt.iscde.classdiagram.extensibility.ClassDiagramFilter;
import pt.iscde.classdiagram.extensibility.ClassDiagramMenuHelper;
import pt.iscde.classdiagram.extensibility.ILayoutExtender;
import pt.iscde.classdiagram.extensibility.actions.ChangeLayoutAction;
import pt.iscde.classdiagram.extensibility.actions.RefreshAction;
import pt.iscde.classdiagram.model.zest.ClassDiagramContentProvider;
import pt.iscde.classdiagram.model.zest.ClassDiagramLabelProvider;
import pt.iscde.classdiagram.model.zest.NodeModelContentProvider;
import pt.iscde.classdiagram.model.zest.SourceElementVisitor;
import pt.iscde.classdiagram.service.ClassDiagramServices;
import pt.iscte.pidesco.extensibility.PidescoServices;
import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorListener;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;
import pt.iscte.pidesco.projectbrowser.model.PackageElement;
import pt.iscte.pidesco.projectbrowser.model.SourceElement;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserListener;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class ClassDiagramView implements PidescoView, ClassDiagramServices, ProjectBrowserListener, JavaEditorListener {
	private static final String VIEW_ID = "pt.iscte.pidesco.classdiagram";
	private static ClassDiagramView instance;
	private static PidescoServices pidescoServices;
	private static ProjectBrowserServices browserServices;
	private static JavaEditorServices javaEditorServices;

	private List<ClassDiagramAction> actions;
	private List<ClassDiagramFilter> filters;
	private List<ILayoutExtender> layouts;

	private GraphViewer viewer;
	private NodeModelContentProvider model;
	private Map<String, Image> imageMap;
	ClassDiagramMenuHelper menuHelper;

	public ClassDiagramView() {
		instance = this;
		pidescoServices = ClassDiagramActivator.getInstance().getPidescoServices();
		browserServices = ClassDiagramActivator.getInstance().getBrowserServices();
		javaEditorServices = ClassDiagramActivator.getInstance().getJavaEditorServices();
		browserServices.addListener(this);
		javaEditorServices.addListener(this);
		filters = new ArrayList<ClassDiagramFilter>();
		actions = new ArrayList<ClassDiagramAction>();
		layouts = new ArrayList<ILayoutExtender>();
	}

	public static ClassDiagramView getInstance() {
		if (instance == null)
			pidescoServices.openView(VIEW_ID);
		return instance;
	}

	@Override
	public void createContents(Composite viewArea, Map<String, Image> imageMap) {
		this.imageMap = imageMap;
		viewer = new GraphViewer(viewArea, SWT.BORDER);
		viewer.getGraphControl().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		viewer.setContentProvider(new ClassDiagramContentProvider());
		viewer.setLabelProvider(new ClassDiagramLabelProvider(new DefaultStyler()));
		model = new NodeModelContentProvider();
		viewer.setInput(model.getNodes());
		LayoutAlgorithm layout = getDefaultLayout();
		viewer.setLayoutAlgorithm(layout, true);
		viewer.applyLayout();
		
		menuHelper = new ClassDiagramMenuHelper(viewer);
		menuHelper.addDefaultAction(new RefreshAction(viewer));
		menuHelper.addDefaultAction(new ChangeLayoutAction(viewer, "Default Layout", getDefaultLayout()));
		viewer.getGraphControl().setMenu(menuHelper.getMenu());
		
		loadActions();
		loadLayouts();
		viewer.addSelectionChangedListener(new ClassDiagramGraphViewerSelectionChangedListener(viewer, actions));
	}

	private LayoutAlgorithm getDefaultLayout() {
		TreeLayoutAlgorithm springLayoutAlgorithm = new TreeLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		HorizontalShift horizontalShift = new HorizontalShift(LayoutStyles.NO_LAYOUT_NODE_RESIZING);
		return new CompositeLayoutAlgorithm(new LayoutAlgorithm[] { horizontalShift, springLayoutAlgorithm });
	}

	@Override
	public void update(SourceElement sourceElement) {
		actualizaDiagrama(sourceElement.getFile());
	}

	List<String> metodos = new ArrayList<String>();
	
	@Override
	public void doubleClick(SourceElement element) {
		if (element.isPackage()) {
			model = new NodeModelContentProvider();
			PackageElement packageElement = (PackageElement) element;
			SourceElementVisitor visitor = new SourceElementVisitor(imageMap);
			for (SourceElement sourceElement : packageElement.getChildren()) {
				if (sourceElement.isClass()) {
					javaEditorServices.parseFile(sourceElement.getFile(), visitor);
					model.getNodes().add(visitor.getTopLevelNode());
				}
			}
			model.addFilters(filters);
			viewer.setInput(model.getNodes());
		} else if (element.isClass()) {
			actualizaDiagrama(element.getFile());
		}
	}

	private void actualizaDiagrama(File file) {
		model = new NodeModelContentProvider();
		ClassDiagramElementVisitor elementVisitor = new ClassDiagramElementVisitor(imageMap, model);
		javaEditorServices.parseFile(file, elementVisitor);
		model.addFilters(filters);
		viewer.setInput(model.getNodes());
	}

	@Override
	public void selectionChanged(Collection<SourceElement> selection) {
		if (selection != null && !selection.isEmpty()) {
			for (SourceElement sourceElement : selection) {
				if (sourceElement.isClass()) {
					update(sourceElement);
					break;
				}
			}
		}

	}

	@Override
	public void fileOpened(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void fileSaved(File file) {
		actualizaDiagrama(file);

	}

	@Override
	public void fileClosed(File file) {
		// TODO Auto-generated method stub

	}

	@Override
	public void selectionChanged(File file, String text, int offset, int length) {
		// TODO Auto-generated method stub

	}

	@Override
	public IFigure getClassImage(SourceElement sourceElement) {
		if (sourceElement.isClass()) {
			ClassElement element = (ClassElement) sourceElement;
			SourceElementVisitor visitor = new SourceElementVisitor(imageMap);
			javaEditorServices.parseFile(element.getFile(), visitor);
			return visitor.getTopLevelNode().getFigure(new DefaultStyler());
		}
		return null;
	}

	public void toggleFilters(boolean activate) {
		filters = new ArrayList<>();
		if (activate) {
			IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
			IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pt.iscte.pidesco.classdiagram.Filter");
			IExtension[] extensions = extensionPoint.getExtensions();
			for (IExtension e : extensions) {
				IConfigurationElement[] confElements = e.getConfigurationElements();
				for (IConfigurationElement c : confElements) {
					try {
						Object o = c.createExecutableExtension("filter");
						if (o instanceof ClassDiagramFilter) {
							ClassDiagramFilter filter = (ClassDiagramFilter) o;
							filters.add(filter);
						}

					} catch (CoreException e1) {
						e1.printStackTrace();
					}
				}
			}

			model.addFilters(filters);
		} else {
			model.clearFilters();
		}
		menuHelper.setFilters(filters);
		viewer.refresh();
		viewer.setInput(model.getNodes());
	}
	
	public void loadActions() {

		IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pt.iscte.pidesco.classdiagram.PopupAction");
		IExtension[] extensions = extensionPoint.getExtensions();
		for (IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for (IConfigurationElement c : confElements) {
				try {
					Object o = c.createExecutableExtension("action");
					String actionText = (String) c.getAttribute("Action_Text");
					if (o instanceof ClassDiagramAction) {
						ClassDiagramAction action = (ClassDiagramAction) o;
						action.setText(actionText);
						actions.add(action);
					}

				} catch (CoreException e1) {
					e1.printStackTrace();
				}
			}
		}
	
		menuHelper.setActions(actions);
	}
	
	
	public void loadLayouts() {

		try{
		IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
		IExtensionPoint extensionPoint = extRegistry.getExtensionPoint("pt.iscte.pidesco.classdiagram.LayoutAlgorithm");
		IExtension[] extensions = extensionPoint.getExtensions();
		for (IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			for (IConfigurationElement c : confElements) {
				try {
					Object o = c.createExecutableExtension("Layout");
					String layoutText = (String) c.getAttribute("layoutName");
					if (o instanceof ILayoutExtender) {
						ILayoutExtender layout = (ILayoutExtender) o;
						layouts.add(layout);
					}

				} catch (CoreException e1) {
					e1.printStackTrace();
				}
			}
		}
	
		menuHelper.setLayouts(layouts);
		}
		catch(Exception ex){ex.printStackTrace();}
	}

}
