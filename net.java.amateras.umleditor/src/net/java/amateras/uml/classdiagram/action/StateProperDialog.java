package net.java.amateras.uml.classdiagram.action;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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


public class StateProperDialog extends org.eclipse.jface.dialogs.Dialog{

	private Shell s;
	private  AttributeModel var;
	Text textVar;
	Text textCons;
	Combo varType;
	Combo varKind;
	private int status;
	public int getStatus()
	{
		return status;
	}
	public StateProperDialog(Shell parentShell,  AttributeModel v) {
		super(parentShell);
		s = parentShell;
		var = v;
		// TODO Auto-generated constructor stub
	}

	private boolean validateInput()
	{
		if(textVar.getText().length() == 0)
		{
			MessageDialog.openError(s, "变量名错误", "变量名不能为空！");
			return false;
		}
		else
		{
			Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z_0-9]*");
			Matcher matcher = pattern.matcher(textVar.getText());
			boolean b= matcher.matches();
			if(!b)
			{
				MessageDialog.openError(s, "变量名错误", "变量名不合法！\n变量名是以字母或者下划线开头的并且只包含数字、字母、下划线的字符串！");
				return false;
			}
		}
		return true;
	}
	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		this.getShell().setText("添加变量");
		parent.setLayout(new GridLayout());
		Label label = new Label(parent, SWT.BORDER);
		label.setText("变量名:");
		textVar = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		textVar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		textVar.setText("");
		
		Label labelType = new Label(parent, SWT.BORDER);
		labelType.setText("变量类型:");
		varType = new Combo(parent, SWT.DROP_DOWN);
		varType.setItems(AttributeModel.TYPE_ARR);
		varType.select(0);
		
		Label labelKind = new Label(parent, SWT.BORDER);
		labelKind.setText("变量分类:");
		varKind = new Combo(parent, SWT.DROP_DOWN);
		varKind.setItems(AttributeModel.KIND_ARR);
		varKind.select(0);
		
		Label labelCons = new Label(parent, SWT.BORDER);
		labelCons.setText("变量约束:");
		textCons = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		textCons.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false));
		textCons.setText("");
		
		return super.createDialogArea(parent);
	}


	@Override
	protected void buttonPressed(int buttonId) {
		// TODO Auto-generated method stub
		
		if(buttonId==IDialogConstants.OK_ID)
		{
			if(!validateInput())
				return;
			var.setName(textVar.getText());
			var.setType(varType.getText());
			var.setCons(textCons.getText());
			var.setKind(varKind.getText());
			status = 1;
		}
		else
		{
			status = 0;
		}
		super.buttonPressed(buttonId);
	}

}
