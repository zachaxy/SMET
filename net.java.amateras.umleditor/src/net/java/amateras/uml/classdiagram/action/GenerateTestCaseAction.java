package net.java.amateras.uml.classdiagram.action;

import java.util.List;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.action.AbstractUMLEditorAction;
import net.java.amateras.uml.classdiagram.ClassDiagramEditor;
import net.java.amateras.uml.editpart.AbstractUMLEntityEditPart;
import net.java.amateras.uml.editpart.RootEditPart;
import net.java.amateras.uml.model.AbstractUMLConnectionModel;
import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.RootModel;

import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.eclipse.draw2d.graph.EdgeList;
import org.eclipse.draw2d.graph.Node;
import org.eclipse.draw2d.graph.NodeList;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CompoundCommand;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.internal.Workbench;

/**
 * The action to layout all entities automatically.
 *
 * @author Naoki Takezoe
 * @see DirectedGraph
 * @see DirectedGraphLayout
 */
public class GenerateTestCaseAction extends AbstractUMLEditorAction {

	private ClassDiagramEditor editor;
	/**
	 * Constructor.
	 *
	 * @param viewer the graphical viewer
	 */
	public GenerateTestCaseAction(GraphicalViewer viewer,ClassDiagramEditor editor) {
		super("生成测试用例", viewer);
		this.editor = editor;
	}

	

	@SuppressWarnings({"rawtypes", "unchecked"})
	public void run() {
		System.out.println(editor.getEditorInput().getName());
	}	

	@Override
	public void update(IStructuredSelection sel) {
		// TODO Auto-generated method stub
		
	}


}
