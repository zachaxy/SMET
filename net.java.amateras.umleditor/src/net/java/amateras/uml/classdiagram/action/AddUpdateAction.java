package net.java.amateras.uml.classdiagram.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.classdiagram.editpart.GeneralizationEditPart;
import net.java.amateras.uml.classdiagram.model.AttributeModel;
import net.java.amateras.uml.classdiagram.model.GeneralizationModel;
import net.java.amateras.uml.editpart.AbstractUMLConnectionEditPart;
import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.AbstractUMLModel;

import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class AddUpdateAction extends AbstractTypeAction {
	
	public AddUpdateAction(CommandStack stack,  GraphicalViewer viewer){
		super("设置动作"/*UMLPlugin.getDefault().getResourceString("menu.addAttribute")*/, stack, viewer);
	}
	
	public void run(){
		stack.execute(new AddAttributeCommand(conn_target));
	}
	public void update(IStructuredSelection sel){
		Object obj = sel.getFirstElement();
		
		 if(obj!=null && obj instanceof GeneralizationEditPart){
			setEnabled(true);
			conn_target = (GeneralizationModel)((AbstractUMLConnectionEditPart)obj).getModel();
			target = null;
		
		}else {
			setEnabled(false);
			target = null;
		}
	}
	private static class AddAttributeCommand extends Command {
		
		private AttributeModel attr;
		private GeneralizationModel target;
		
		public AddAttributeCommand(GeneralizationModel target){
			this.target = target;
		}
		
		public void execute(){
			
			
			Display display = Display.getCurrent();
			Shell shell = display.getActiveShell();
			List<String> oldUpdates= new ArrayList<String>();
			oldUpdates.addAll(target.getUpdates());
			Collections.copy(oldUpdates, target.getUpdates());
			ConnUpdateDialog msg = new ConnUpdateDialog(shell, target);
			
			msg.open();
			if(msg.getStatus()==1)
			{
				
					
			}
			else
			{
				target.setUpdates(oldUpdates);
			}
			//attr.setName("var" + count);
			
		}
		
		public void undo() {
			
		}
		
		
	}
	
}
