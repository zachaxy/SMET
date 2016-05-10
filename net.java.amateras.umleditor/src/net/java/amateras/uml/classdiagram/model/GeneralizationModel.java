package net.java.amateras.uml.classdiagram.model;

import org.eclipse.ui.views.properties.ColorPropertyDescriptor;
import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.model.AbstractUMLConnectionModel;

public class GeneralizationModel extends AbstractUMLConnectionModel {

	private String transCond="";
	public static final String P_TRANS_COND = "_transform_condition";
	public IPropertyDescriptor[] getPropertyDescriptors() {
		IPropertyDescriptor []propertyDescriptors = super.getPropertyDescriptors();
		int newLength = propertyDescriptors.length+1;
		IPropertyDescriptor[] newPropertyDescriptors = new IPropertyDescriptor[newLength];
		System.arraycopy(propertyDescriptors, 0, newPropertyDescriptors, 0, propertyDescriptors.length);

		newPropertyDescriptors[newLength - 1] =
			new TextPropertyDescriptor(P_TRANS_COND, "转移条件"/*UMLPlugin.getDefault().getResourceString("property.abstract")*/);

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
		}
		return super.getPropertyValue(id);
	}

	public boolean isPropertySet(Object id) {
		if (id.equals(P_TRANS_COND)) {
			return true;
		}
		return super.isPropertySet(id);
	}

	public void setPropertyValue(Object id, Object value) {
		if (id.equals(P_TRANS_COND)) {
			setTransCond((String)value);
		}
		super.setPropertyValue(id, value);
	}
}
