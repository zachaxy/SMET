package net.java.amateras.uml.classdiagram.model;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.classdiagram.property.EnumPropertyDescriptor;
import net.java.amateras.uml.model.AbstractUMLModel;
import net.java.amateras.uml.properties.BooleanPropertyDescriptor;

import org.eclipse.ui.views.properties.IPropertyDescriptor;
import org.eclipse.ui.views.properties.TextPropertyDescriptor;

/**
 * 属性を表すモデルオブジェクト。
 * 
 * @author Naoki Takezoe
 */
public class AttributeModel extends AbstractUMLModel implements Cloneable {
	
	private String name = "";
	private String type = "int";
	private String cons;
	public String getCons() {
		return cons;
	}

	public void setCons(String cons) {
		this.cons = cons;
		firePropertyChange(P_CONS,null,cons);
		if (getParent() != null) {
			getParent().forceUpdate();
		}
	}

	private boolean isStatic;
	

	public static final String P_NAME = "_name";
	public static final String P_TYPE = "_type";
	public static final String P_CONS = "_cons";
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
		firePropertyChange(P_NAME,null,name);
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
		firePropertyChange(P_TYPE,null,type);
	}
	
	
	
	public boolean isStatic() {
		return isStatic;
	}



	public IPropertyDescriptor[] getPropertyDescriptors() {
		return new IPropertyDescriptor[]{
				new TextPropertyDescriptor(P_NAME, 
						UMLPlugin.getDefault().getResourceString("property.name")),
				new TextPropertyDescriptor(P_TYPE,
						UMLPlugin.getDefault().getResourceString("property.type")),
				
				new TextPropertyDescriptor(P_CONS,"constraint"
						/*UMLPlugin.getDefault().getResourceString("property.static")*/)
		};
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(P_NAME)){
			return getName();
		} else if(id.equals(P_TYPE)){
			return getType();
		} else if(id.equals(P_CONS)){
			return getCons();
		}
		return null;
	}

	public boolean isPropertySet(Object id) {
		if(id.equals(P_NAME)){
			return true;
		} else if(id.equals(P_TYPE)){
			return true;
		} else if(id.equals(P_CONS)){
			return true;
		}
		return false;
	}

	public void setPropertyValue(Object id, Object value) {
		if(id.equals(P_NAME)){
			setName((String)value);
		} else if(id.equals(P_TYPE)){
			setType((String)value);
		}else if(id.equals(P_CONS)){
			setCons((String)value);
		}
	}

	
	public String toString(){
		StringBuffer sb = new StringBuffer();
//		sb.append(visibility.getSign());
		sb.append(getName());
		sb.append(": ");
		sb.append(getType());
		return sb.toString();
	}

	public Object clone() {
		AttributeModel newModel = new AttributeModel();
		newModel.setName(getName());
		newModel.setType(getType());
		
		newModel.setCons(getCons());
		return newModel;
	}
	
}
