/**
 * 
 */
package net.java.amateras.uml.dnd.java;

import net.java.amateras.uml.classdiagram.model.AttributeModel;
import net.java.amateras.uml.classdiagram.model.ClassModel;
import net.java.amateras.uml.java.UMLJavaUtils;
import net.java.amateras.uml.model.AbstractUMLEntityModel;

import org.eclipse.gef.requests.CreationFactory;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

/**
 * 
 * @author Takahiro Shida
 * @author Naoki Takezoe
 */
class ClassModelConverter implements CreationFactory {

	protected IType type;

	public ClassModelConverter(IType type) {
		this.type = type;
	}

	public Object getNewObject() {
		try {
			AbstractUMLEntityModel rv = null;
			if (type.isClass()) {
				rv = new ClassModel();
				((ClassModel) rv).setName(type.getFullyQualifiedName());
			}
			
			AttributeModel[] fields = UMLJavaUtils.getFields(type);
			for(int i=0;i<fields.length;i++){
				rv.addChild(fields[i]);
			}
			
			
			return rv;
		} catch (JavaModelException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Object getObjectType() {
		try {
			
				return ClassModel.class;
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return ClassModel.class;
	}

}