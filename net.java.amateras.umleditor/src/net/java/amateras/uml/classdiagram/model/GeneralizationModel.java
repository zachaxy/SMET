package net.java.amateras.uml.classdiagram.model;

import java.util.*;

import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.PropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.model.AbstractUMLConnectionModel;

public class GeneralizationModel extends AbstractUMLConnectionModel {

	private String transCond="";
	private List<String> updates = new ArrayList<String>();
	public static final String P_TRANS_COND = "_transform_condition";
	public static final String P_UPDATE = "_UPDATE";
	public GeneralizationModel(){
		
		updates.add("abc");
		updates.add("def");
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
			String s = "";
			Iterator<String> iter = updates.iterator();
			while(iter.hasNext())
				s = s+iter.next()+"; ";
			return s;
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

	public void setPropertyValue(Object id, Object value) {
		if (id.equals(P_TRANS_COND)) {
			setTransCond((String)value);
		}else if(id.equals(P_UPDATE)){
			String s = (String)value;
			String[] ups = s.split(";");
			updates = new ArrayList();
			for(int i = 0; i < ups.length; i++){
				if(ups[i].trim().length()>0)
					updates.add(ups[i].trim());
				
			}
				
			
		}
	
		super.setPropertyValue(id, value);
	}
}
