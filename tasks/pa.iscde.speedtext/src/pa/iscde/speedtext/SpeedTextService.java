package pa.iscde.speedtext;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import pt.iscte.pidesco.extensibility.PidescoView;
import pt.iscte.pidesco.javaeditor.service.JavaEditorServices;
import pt.iscte.pidesco.projectbrowser.model.ClassElement;
import pt.iscte.pidesco.projectbrowser.model.PackageElement.Visitor;
import pt.iscte.pidesco.projectbrowser.service.ProjectBrowserServices;

public class SpeedTextService implements PidescoView {

	private JavaEditorServices jeServices;
	private ProjectBrowserServices pbservices;
	private File file, tempfile;
	private boolean findpoint;
	private String filter="";
	private List sugestionList;

	IExtensionRegistry extRegistry = Platform.getExtensionRegistry();
	IExtensionPoint extensionPointSortList = extRegistry.getExtensionPoint("pa.iscde.speedtext.sortlist");
	private LinkedList<SpeedTextSortList> extensionResultSortList = new LinkedList<SpeedTextSortList>();
	IExtensionPoint extensionPointExtraInfo = extRegistry.getExtensionPoint("pa.iscde.speedtext.extrainfo");
	private LinkedList<SpeedTextExtraInfo> extensionResultExtraInfo = new LinkedList<SpeedTextExtraInfo>();


	@Override
	public void createContents(final Composite viewArea, Map<String, Image> imageMap) {

		IExtension[] extensions = concat(extensionPointSortList.getExtensions(),extensionPointExtraInfo.getExtensions());

		System.out.println("Extensoes detectadas - " + extensions.length);
		for (IExtension e : extensions) {
			IConfigurationElement[] confElements = e.getConfigurationElements();
			System.out.println("Sorts apanhados - " + confElements.length);
			for (IConfigurationElement c : confElements) {
				try {
					if(c.getName().equals("sortlist"))
						extensionResultSortList.add((SpeedTextSortList) c.createExecutableExtension("class"));
					else if(c.getName().equals("extrainfo"))
						extensionResultExtraInfo.add((SpeedTextExtraInfo) c.createExecutableExtension("class"));
					System.out.println("Nome: " + c.getName());
				} catch (CoreException e1) {
					e1.printStackTrace();
				}

			}
		}

		jeServices = Activator.getActivator().getJavaEditorservice(); 
		pbservices = Activator.getActivator().getProjectBrowserServices();

		// Janela
		file = jeServices.getOpenedFile();
		viewArea.setLayout(new GridLayout(2, false));
		final Button button = new Button(viewArea, SWT.PUSH);
		button.setText("Suggest");
		sugestionList = new List(viewArea, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL | SWT.WRAP);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		gridData.horizontalAlignment = SWT.FILL;
		gridData.grabExcessHorizontalSpace = true;
		gridData.verticalAlignment = SWT.FILL;
		gridData.grabExcessVerticalSpace = true;
		sugestionList.setLayoutData(gridData);

		button.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				sugestionList.removeAll();
				filter="";
				final String temp= findVarible();

				// Encontrar a Class da Variavel			
				jeServices.parseFile(file, new ASTVisitor() {
					@Override
					//Fields
					public boolean visit(final FieldDeclaration node){
						String[] simples= node.toString().replaceAll("[;\\&]", "").split("=")[0].split(" ");
						if (!findpoint && simples[simples.length-1].contains(filter))
							sugestionList.add(simples[simples.length-1]);
						else if(temp.equals(simples[simples.length-1]) &&findpoint){

							pbservices.getRootPackage().traverse(new Visitor(){
								@Override
								public boolean visitPackage(
										pt.iscte.pidesco.projectbrowser.model.PackageElement packageElement) {
									return true;
								}
								@Override
								public void visitClass(ClassElement classElement) {
									if(classElement.getName().equals(node.getType().toString()+".java")){
										File tempfile=classElement.getFile();
										jeServices.parseFile(tempfile, new ASTVisitor() {
											public boolean visit(MethodDeclaration node) {
												if(!node.isConstructor() && (node.modifiers().get(0).toString()).equals("public") && node.getName().toString().substring(0,filter.length()).equals(filter)){

													if(node.parameters().size()!=0){
														String parameters="";
														int i=0;
														for(Object p:node.parameters()){
															if(i>0 && i<node.parameters().size())
																parameters += ", "+p.toString();
															else
																parameters += p.toString();
															i++;
														}
														sugestionList.add(node.getName().toString()+"("+parameters+")");
													}else
														sugestionList.add(node.getName().toString()+"()");
												}
												return true;
											}
										});
									}
								}
							});
						}
						return true;
					}

					//--- codigo restaurado, melhorar isto
					public boolean visit(final VariableDeclarationStatement node) {

						//Sugere metodos
						if (temp.equals(node.fragments().get(0).toString().split("=")[0]) && findpoint) {
							pbservices.getRootPackage().traverse(new Visitor(){
								@Override
								public boolean visitPackage(
										pt.iscte.pidesco.projectbrowser.model.PackageElement packageElement) {
									return true;
								}
								@Override
								public void visitClass(ClassElement classElement) {
									if(classElement.getName().equals(node.getType().toString()+".java")){
										tempfile=classElement.getFile();
										jeServices.parseFile(tempfile, new ASTVisitor() {
											public boolean visit(MethodDeclaration node) {
												if(!node.isConstructor() && (node.modifiers().get(0).toString()).equals("public") && node.getName().toString().substring(0,filter.length()).equals(filter)){

													if(node.parameters().size()!=0){
														String parameters="";
														int i=0;
														for(Object p:node.parameters()){
															if(i>0 && i<node.parameters().size())
																parameters += ", "+p.toString();
															else
																parameters += p.toString();
															i++;
														}
														sugestionList.add(node.getName().toString()+"("+parameters+")");
													}else
														sugestionList.add(node.getName().toString()+"()");

												}
												return true;
											}
										});
									}
								}
							});

							//Sugere variaveis
						}
						else if((node.fragments().get(0).toString().split("=")[0]).contains(temp)){
							sugestionList.add((node.fragments().get(0).toString().split("=")[0]));
						}
						return true;

					}
				});

				sortlist();
				extraInfo();
			}
		});

		//Selecciona op�ao da gridList
		sugestionList.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {

				final int cursorpoint = jeServices.getCursorPosition();
				jeServices.insertText(file, sugestionList.getItem(sugestionList.getSelectionIndex()).split("-")[0], cursorpoint-filter.length(), filter.length());
				sugestionList.removeAll();
				filter="";
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
			}
		});
	}

	//Encontra a variavel antes do '.'
	private String findVarible() {
		final int cursorpoint = jeServices.getCursorPosition();
		jeServices.selectText(file, 0, cursorpoint);
		String s = jeServices.getTextSelected(file).getText();
		char[] charSa = s.toCharArray();
		findpoint = false;


		//Selecciona s� a linha
		if(charSa.length>0){
			for (int i = charSa.length - 1; i>0 ; i--) {
				if(charSa[i] == '\n'){
					jeServices.selectText(file, i + 1, cursorpoint-i-1);
					s = jeServices.getTextSelected(file).getText();
					if(s.contains("."))
						findpoint=true;
					charSa = s.toCharArray();
					break;
				}
			}

			for (int finChar = charSa.length - 1; finChar > 0; finChar--) {

				//Selecciona a Variavel antes do ponto
				if (findpoint){
					if (charSa[finChar] == '.') {
						int inichar = finChar;
						while (charSa[inichar] != ' ' && charSa[inichar] != ';' && charSa[inichar] != '{'
								&& charSa[inichar] != ',' && charSa[inichar] != '\t' && charSa[inichar] != '\n'){
							inichar--;
						}
						jeServices.selectText(file, cursorpoint-charSa.length+inichar+1 , finChar - inichar - 1); 
						s = jeServices.getTextSelected(file).getText();
						jeServices.selectText(file, cursorpoint, 0); //deixa cursor a seguir ao '.'
						if(!filter.isEmpty())
							filter = new StringBuffer(filter).reverse().toString();
						break;
					}else{
						filter += charSa[finChar];						
					}

					//Selecciona linha sem o ponto
				}else{
					int inichar = finChar;
					while (charSa[inichar] != ' ' && charSa[inichar] != ';' && charSa[inichar] != '{'
							&& charSa[inichar] != ',' && charSa[inichar] != '\t' && charSa[inichar] != '\n'){
						inichar--;
					}
					jeServices.selectText(file, cursorpoint-finChar+inichar , finChar - inichar );
					s = jeServices.getTextSelected(file).getText();
					filter=s;
					jeServices.selectText(file, cursorpoint, 0); //desseleciona
					break;
				}
			}
		}
		return s;
	}

	//Extension point: Jorge
	private void extraInfo(){		
		if (!extensionResultExtraInfo.isEmpty()){
			ArrayList<String> auxinfo = toArrayList(sugestionList);	
			ArrayList<String> aux = toArrayList(sugestionList);	
			if (findpoint)
				auxinfo = (ArrayList<String>)extensionResultExtraInfo.get(0).extraInfo(auxinfo,tempfile);
			else
				auxinfo = (ArrayList<String>)extensionResultExtraInfo.get(0).extraInfo(auxinfo,file);
			if(auxinfo.size()==aux.size()){
				for(int i=0;i<auxinfo.size();i++){
					aux.set(i, aux.get(i)+" - "+auxinfo.get(i));
				}
				arrayListToSugestionList(aux);
			}
		}
	}

	//Extension point: Pedras
	private void sortlist() {
		//Vers�o antiga
		//		if (!extensionResultSortList.isEmpty()){
		//			ArrayList<String> aux = toArrayList(sugestionList);			
		//			aux = (ArrayList<String>)extensionResultSortList.get(0).sortList(aux);
		//			arrayListToSugestionList(aux);
		//		}

		//Vers�o nova
		ArrayList<String> result = toArrayList(sugestionList);
		SpeedTextSortList extension = extensionResultSortList.get(0);
		for (int x=0; x<sugestionList.getItemCount(); x++){
			for (int y=x; y<sugestionList.getItemCount(); y++){
				int result_hack = extension.compare(result.get(x), result.get(y));
				boolean compareResult = false;
				if(result_hack > 1) {
					compareResult = true;
				}
				
				//boolean compareResult = extension.compare(result.get(x), result.get(y));
				if (compareResult==false){
					String aux = result.get(x);
					result.set(x, result.get(y));
					result.set(y, aux);
				}
			}
		}
		arrayListToSugestionList(result);
	}

	private ArrayList<String> toArrayList(List l){
		ArrayList<String> aux = new ArrayList<String>();
		for (String s : sugestionList.getItems()){
			aux.add(s);
		}
		return aux;
	}

	private void arrayListToSugestionList(ArrayList<String> a){
		sugestionList.removeAll();
		for (String s : a){
			sugestionList.add(s);
		}
	}

	private <T> T[] concat(T[] first, T[] second) {
		T[] result = Arrays.copyOf(first, first.length + second.length);
		System.arraycopy(second, 0, result, first.length, second.length);
		return result;
	}

}
