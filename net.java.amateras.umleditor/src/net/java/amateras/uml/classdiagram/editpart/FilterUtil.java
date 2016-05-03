/**
 *
 */
package net.java.amateras.uml.classdiagram.editpart;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.amateras.uml.classdiagram.action.ToggleAction;
import net.java.amateras.uml.classdiagram.model.AttributeModel;
import net.java.amateras.uml.classdiagram.model.Visibility;
import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.AbstractUMLModel;

/**
 * @author Takahiro Shida.
 *
 */
public class FilterUtil {

	public static List<AbstractUMLModel> getFilteredChildren(AbstractUMLEntityModel model) {
		List<AbstractUMLModel> rv = new ArrayList<AbstractUMLModel>();
		rv.addAll(getAttribute(model));
	
		return rv;
	}

	private static List<AttributeModel> getAttribute(AbstractUMLEntityModel model) {
		List<AttributeModel> rv = new ArrayList<AttributeModel>();
		List<AbstractUMLModel> children = model.getChildren();
		Map<String, Boolean> map = model.getFilterProperty();
		boolean v_public = isShow(ToggleAction.ATTRIBUTE + Visibility.PUBLIC, map);
		boolean v_protected = isShow(ToggleAction.ATTRIBUTE + Visibility.PROTECTED, map);
		boolean v_package = isShow(ToggleAction.ATTRIBUTE + Visibility.PACKAGE, map);
		boolean v_private = isShow(ToggleAction.ATTRIBUTE + Visibility.PRIVATE, map);
		for (Iterator<AbstractUMLModel> iter = children.iterator(); iter.hasNext();) {
			AbstractUMLModel element = (AbstractUMLModel) iter.next();
			if (element instanceof AttributeModel) {
				AttributeModel a = (AttributeModel) element;
				rv.add(a);
				
			}
		}
		return rv;
	}

	

	private static boolean isShow(String key, Map<String, Boolean> map) {
		if (map == null || !map.containsKey(key)) {
			return true;
		}
		Boolean bool = (Boolean) map.get(key);
		return !bool.booleanValue();
	}
}
