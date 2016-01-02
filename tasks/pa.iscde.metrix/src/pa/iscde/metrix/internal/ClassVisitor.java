package pa.iscde.metrix.internal;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BlockComment;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.PackageDeclaration;

/**
 * Class 
 * 
 */

class ClassVisitor extends ASTVisitor {
	
	private MetricAnalyzer view;
	
	/**
	 * 
	 */
	@Override
	public boolean visit(BlockComment node) {
		System.out.println("AAAA");
		int start = node.getStartPosition();
		int end = start + node.getLength();
		System.out.println(start + "  " + end);
		view.incremetMetric("Number of Comments");
		return true;
	}

	/**
	 * 
	 * @param view
	 */
	
	public ClassVisitor(MetricAnalyzer view) {
		this.view = view;
	}
	
	/**
	 * 
	 * @param node
	 */
	
	@Override
	public boolean visit(FieldDeclaration node) {
		view.incremetMetric("Number of Fields");
		return super.visit(node);
	}
	
	/**
	 * 
	 * @param node
	 * @return super.visit node
	 */

	@Override
	public boolean visit(MethodDeclaration node) {
		if (node.isConstructor()) {
			view.incremetMetric("Number of Constructors");
		}
		view.incremetMetric("Number of Methods");
		return super.visit(node);
	}

	/**
	 * 
	 * @param node
	 * @return super.visit node
	 */
	
	@Override
	public boolean visit(CharacterLiteral node) {
		view.incremetMetric("Number of Characters");
		return super.visit(node);
	}
	
	/**
	 * 
	 * @param node
	 * @return super.visit node
	 */
	
	@Override
	public boolean visit(PackageDeclaration node) {
		view.incremetMetric("Number of Packages");
		return super.visit(node);
	}


}
