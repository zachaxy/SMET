package net.java.amateras.uml.classdiagram.model;

import java.util.*;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import net.java.amateras.uml.model.AbstractUMLConnectionModel;
import parsii.eval.Expression;
import parsii.eval.Parser;
import parsii.eval.Scope;
import parsii.tokenizer.ParseException;

public class GeneralizationModel extends AbstractUMLConnectionModel {

	private String transCond="";
	private List<String> updates = new ArrayList<String>();
	private String update = "";
	public static final String P_TRANS_COND = "_transform_condition";
	public static final String P_UPDATE = "_UPDATE";
	public GeneralizationModel(){
		
	//	updates.add("abc");
	//	updates.add("def");
	}
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor []propertyDescriptors = super.getPropertyDescriptors();
		int newLength = propertyDescriptors.length+2;
		IPropertyDescriptor[] newPropertyDescriptors = new IPropertyDescriptor[newLength];
		System.arraycopy(propertyDescriptors, 0, newPropertyDescriptors, 0, propertyDescriptors.length);

		newPropertyDescriptors[newLength - 2] =
			new TextPropertyDescriptor(P_TRANS_COND, "转移条件"/*UMLPlugin.getDefault().getResourceString("property.abstract")*/);
		newPropertyDescriptors[newLength - 1] = 
				new TextPropertyDescriptor(P_UPDATE, "转移动作"/*UMLPlugin.getDefault().getResourceString("property.attributes")*/);
		propertyDescriptors = newPropertyDescriptors;
		return propertyDescriptors;
	}
	public String getTransCond() {
		return transCond;
	}
	public void setTransCond(String transCond) {
		this.transCond = transCond;
		firePropertyChange(P_TRANS_COND, null, this.transCond);
	}
	
	public Object getPropertyValue(Object id) {
		if (id.equals(P_TRANS_COND)) {
			return getTransCond();
			//return new Boolean(isAbstract());
		}else if(id.equals(P_UPDATE)){
			
			return buildUpdates();
		}
		return super.getPropertyValue(id);
	}

	public boolean isPropertySet(Object id) {
		if (id.equals(P_TRANS_COND)) {
			return true;
		}else if(id.equals(P_UPDATE)){
			return true;
		}

		return super.isPropertySet(id);
	}
	public void parseUpdate(String s)
	{
		
		String[] ups = s.split(";");
		updates = new ArrayList<String>();
		for(int i = 0; i < ups.length; i++){
			if(ups[i].trim().length()>0)
				if(!validAction(ups[i].trim()))
				{
					MessageDialog.openError(Display.getCurrent().getActiveShell(), "转移动作有误", "转移动作语法不合法！");
					return;
				}
				updates.add(ups[i].trim());
			
		}
	}
	public List<String> getUpdates(){
		return updates;
	}
	public void setUpdates(List<String> u){
		this.updates = u;
	}
	public String buildUpdates(){
		String s = "";
		Iterator<String> iter = updates.iterator();
		while(iter.hasNext())
			s = s+iter.next()+"; ";
		//return s;
		update= s;
		return update;
	}
	
	public String getUpdate(){
		StringBuilder sb = new StringBuilder();
		for(String s:updates){
			sb.append(s);
			sb.append(";");
		}
		System.out.println("update--->"+sb.toString());
		return sb.toString();
	}
	
	public void setUpdate(String s){
		update = s;
	}
	
	public void setPropertyValue(Object id, Object value) {
		if (id.equals(P_TRANS_COND)) {
			if(!validTrans((String)value))
			{
				MessageDialog.openError(Display.getCurrent().getActiveShell(), "转移条件有误", "转移条件语法不合法！");
				return;
			}
			setTransCond((String)value);
		}else if(id.equals(P_UPDATE)){
			//TODO:这里只需返回一个字符串即可
			update = (String)value;
			System.out.println("-------------------->"+update);
			parseUpdate((String)value);
		}
	
		super.setPropertyValue(id, value);
	}
	
	public boolean validAction(String str)
	{
		Scope scope = Scope.create();
		try {
			Expression parsiiExpr = Parser.parse(str);
			System.out.println("OK, trans ok:"+str);
			if(!(str.indexOf('=')>0))
				return false;
			return true;
		} catch (ParseException e) {
			//e.printStackTrace();
		}
		return false;
	}
	
	public boolean validTrans(String str)
	{
		Scope scope = Scope.create();
		try {
			Expression parsiiExpr = Parser.parse(str);
			System.out.println("OK, trans ok:"+str);
		
			return true;
		} catch (ParseException e) {
			//e.printStackTrace();
		}
		return false;
	}
}
