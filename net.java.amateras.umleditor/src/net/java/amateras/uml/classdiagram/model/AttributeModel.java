package net.java.amateras.uml.classdiagram.model;

import net.java.amateras.uml.UMLPlugin;
import net.java.amateras.uml.classdiagram.property.EnumPropertyDescriptor;
import net.java.amateras.uml.model.AbstractUMLModel;
import net.java.amateras.uml.properties.BooleanPropertyDescriptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.views.properties.ComboBoxPropertyDescriptor;
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
	private String cons="";
	private String kind="";
	public static final String VAR_INPUT="输入变量";
	public static final String VAR_MID="中间变量";
	public static final String VAR_OUTPUT="输出变量";
	public static final String []KIND_ARR = new String[]{VAR_INPUT, VAR_MID, VAR_OUTPUT};
	public static final String VAR_TYPE_INT="Int";
	public static final String VAR_TYPE_DOUBLE="Double";
	public static final String VAR_TYPE_CHAR="Char";
	public static final String []TYPE_ARR = new String[]{VAR_TYPE_INT, VAR_TYPE_DOUBLE, VAR_TYPE_CHAR};
	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
		firePropertyChange(P_KIND,null,kind);
		if (getParent() != null) {
			getParent().forceUpdate();
		}
	}

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
	public static final String P_KIND = "_kind";
	
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
				//new TextPropertyDescriptor(P_TYPE,
					//	UMLPlugin.getDefault().getResourceString("property.type")),
				new ComboBoxPropertyDescriptor(P_TYPE, "变量类型", TYPE_ARR),
				new TextPropertyDescriptor(P_CONS,"constraint"
						/*UMLPlugin.getDefault().getResourceString("property.static")*/),
				new ComboBoxPropertyDescriptor(P_KIND, "变量分类", KIND_ARR)
		};
	}

	public Object getPropertyValue(Object id) {
		if(id.equals(P_NAME)){
			return getName();
		} else if(id.equals(P_TYPE)){
			if(getType().equals(VAR_TYPE_INT))
				return 0;
			else if(getType().equals(VAR_TYPE_DOUBLE))
				return 1;
			else if(getType().equals(VAR_TYPE_CHAR))
				return 2;
			else
				return 3;
		} else if(id.equals(P_CONS)){
			return getCons();
		}else if(id.equals(P_KIND)){
		
			if(getKind().equals(VAR_INPUT))
				return 0;
			else if(getKind().equals(VAR_MID))
				return 1;
			else if(getKind().equals(VAR_OUTPUT))
				return 2;
			else
				return 3;
			
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
		}else if(id.equals(P_KIND)){
			return true;
		}
		return false;
	}

	public void setPropertyValue(Object id, Object value) {
		if(id.equals(P_NAME)){
			if(!validateInput((String)value))
			{
				
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
										"变量名格式错误",
										"变量名不合法！\n变量名不能为空且是以字母或者下划线开头的并且只包含数字、字母、下划线的字符串！"
										);
				return;
			}
			setName((String)value);
		} else if(id.equals(P_TYPE)){
			if((Integer)value == 0)
				setType(VAR_TYPE_INT);
			else if((Integer)value == 1)
				setType(VAR_TYPE_DOUBLE);
			else if((Integer)value == 2)
				setType(VAR_TYPE_CHAR);
			else 
				setType("错误");
			if(!validateCons(this.getType(), this.getCons()))
				this.setCons("");
		}else if(id.equals(P_CONS)){
			if(!validateCons(this.getType(),(String)value)){
				MessageDialog.openError(Display.getCurrent().getActiveShell(),
						"约束条件有误", "约束条件语法不合法！");
				return ;
			}
			setCons((String)value);
		}else if(id.equals(P_KIND)){
		
			if((Integer)value == 0)
				setKind(VAR_INPUT);
			else if((Integer)value == 1)
					setKind(VAR_MID);
			else if((Integer)value == 2)
					setKind(VAR_OUTPUT);
			else 
				setKind("错误");
		}
	}

	
	public String toString(){
		StringBuffer sb = new StringBuffer();
//		sb.append(visibility.getSign());
		sb.append(getName());
		sb.append(": ");
		sb.append(getType());
		sb.append(": ");
		sb.append(getKind());
		sb.append("(");
		sb.append(getCons());
		sb.append(")");
		return sb.toString();
	}

	public Object clone() {
		AttributeModel newModel = new AttributeModel();
		newModel.setName(getName());
		newModel.setType(getType());
		
		newModel.setCons(getCons());
		return newModel;
	}
	public boolean validateInput(String str)
	{
		if(str.length() == 0)
		{
			return false;
		}
		else
		{
			Pattern pattern = Pattern.compile("[a-zA-Z_][a-zA-Z_0-9]*");
			Matcher matcher = pattern.matcher(str);
			boolean b= matcher.matches();
			if(!b)
			{
				return false;
			}
		}
		return true;
	}
	public boolean validInt(String str)
	{
		if(str.length() == 0)
		{
			return true;
		}
		else
		{
			Pattern pattern = Pattern.compile("[\\[\\(] *-?[0-9]+ *, *-?[0-9]+ *[\\]\\)]");
			Matcher matcher = pattern.matcher(str);
			boolean b= matcher.matches();
			if(!b)
			{
				return false;
			}
		}
		return true;
	}
	public boolean validDouble(String str)
	{
		if(str.length() == 0)
		{
			return true;
		}
		else
		{
			Pattern pattern = Pattern.compile("[\\[\\(] *-?[0-9]+(.[0-9]+)? *, *-?[0-9]+(.[0-9]+)? *[\\]\\)]");
			Matcher matcher = pattern.matcher(str);
			boolean b= matcher.matches();
			if(!b)
			{
				return false;
			}
		}
		return true;
	}
	public boolean validChar(String str)
	{
		return true;
	}
	
	public boolean validateCons(String type, String cons)
	{
		boolean b = false;
		if(type.equals("Int"))
			b = validInt(cons);
		else if(type.equals("Double"))
			b =  validDouble(cons);
		else if(type.equals("Char"))
			b = validChar(cons);
		
		return b;
	}
	
}
