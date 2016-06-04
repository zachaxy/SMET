package net.java.amateras.uml.classdiagram.action;

import java.util.List;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.classdiagram.editpart.AttributeEditPart;
import net.java.amateras.uml.classdiagram.editpart.GeneralizationEditPart;
import net.java.amateras.uml.classdiagram.model.AttributeModel;
import net.java.amateras.uml.classdiagram.model.GeneralizationModel;
import net.java.amateras.uml.editpart.AbstractUMLConnectionEditPart;
import net.java.amateras.uml.editpart.AbstractUMLEntityEditPart;
import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.AbstractUMLModel;
import util.ShowFile;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class AddAttributeAction extends AbstractTypeAction {
	
	public AddAttributeAction(CommandStack stack,  GraphicalViewer viewer){
		super("添加变量"/*UMLPlugin.getDefault().getResourceString("menu.addAttribute")*/, stack, viewer);
	}
	
	public void run(){
		stack.execute(new AddAttributeCommand(target));
	}
	
	private static class AddAttributeCommand extends Command {
		
		private AttributeModel attr;
		private AbstractUMLEntityModel target;
		
		public AddAttributeCommand(AbstractUMLEntityModel target){
			this.target = target;
		}
		
		public void execute(){

			attr = new AttributeModel();
			int count = 1;
			List<AbstractUMLModel> list = target.getChildren();
			for(int i=0;i<list.size();i++){
				if(list.get(i) instanceof AttributeModel){
					count++;
				}
			}
			
			Display display = Display.getCurrent();
			Shell shell = display.getActiveShell();
			
			StateProperDialog msg = new StateProperDialog(shell, attr);
			
			msg.open();
			if(msg.getStatus()==1)
			{
				//System.out.println(var);
				
			
				target.copyPresentation(attr);
				target.addChild(attr);
				
			}
			//attr.setName("var" + count);
			
		}
		
		public void undo() {
			target.removeChild(attr);
		}
	}
	
}
