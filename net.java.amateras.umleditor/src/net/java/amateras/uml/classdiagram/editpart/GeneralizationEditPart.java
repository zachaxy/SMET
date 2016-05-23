package net.java.amateras.uml.classdiagram.editpart;

import net.java.amateras.uml.classdiagram.figure.GeneralizationConnectionFigure;
import net.java.amateras.uml.editpart.AbstractUMLConnectionEditPart;

import org.eclipse.draw2d.IFigure;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;


public class GeneralizationEditPart extends AbstractUMLConnectionEditPart {

	
	
	protected IFigure createFigure() {
//		Display display = Display.getCurrent();
//		Shell shell = display.getActiveShell();
//		Label lb = new Label(shell, SWT.NONE);
//		lb.setText("ABCDEFG");
//		lb.setLocation(300, 300);
		return new GeneralizationConnectionFigure();
	}

}
