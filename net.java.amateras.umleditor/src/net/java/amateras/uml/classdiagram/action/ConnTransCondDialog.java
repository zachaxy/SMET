package net.java.amateras.uml.classdiagram.action;

import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import net.java.amateras.uml.classdiagram.model.AttributeModel;
import net.java.amateras.uml.classdiagram.model.GeneralizationModel;


public class ConnTransCondDialog extends org.eclipse.jface.dialogs.Dialog{

	private Shell s;
	private  GeneralizationModel var;
	Text textVar;
	
	private int status;
	public int getStatus()
	{
		return status;
	}
	public ConnTransCondDialog(Shell parentShell,  GeneralizationModel v) {
		super(parentShell);
		s = parentShell;
		var = v;
		
		// TODO Auto-generated constructor stub
	}


	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		this.getShell().setText("设置转移条件");
		parent.setLayout(new GridLayout());
		Label label = new Label(parent, SWT.BORDER);
		label.setText("转移条件:");
		textVar = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		textVar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		textVar.setText(var.getTransCond());
		textVar.setSelection(0,textVar.getText().length());
		
		
		return super.createDialogArea(parent);
	}


	@Override
	protected void buttonPressed(int buttonId) {
		// TODO Auto-generated method stub
		
		if(buttonId==IDialogConstants.OK_ID)
		{
			var.setTransCond(textVar.getText());
			status = 1;
		}
		else
		{
			status = 0;
		}
		super.buttonPressed(buttonId);
	}

}
