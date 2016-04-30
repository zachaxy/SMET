package net.java.amateras.uml.java;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.java.amateras.uml.classdiagram.model.Argument;
import net.java.amateras.uml.classdiagram.model.AttributeModel;
import net.java.amateras.uml.classdiagram.model.ClassModel;
import net.java.amateras.uml.classdiagram.model.GeneralizationModel;
import net.java.amateras.uml.classdiagram.model.RealizationModel;
import net.java.amateras.uml.model.AbstractUMLConnectionModel;
import net.java.amateras.uml.model.AbstractUMLEntityModel;
import net.java.amateras.uml.model.AbstractUMLModel;
import net.java.amateras.uml.model.RootModel;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.wizard.Wizard;

public class JavaExportWizard extends Wizard {
	
	private JavaExportWizardPage folderPage;
	private IJavaProject project;
	private Map<String, AbstractUMLEntityModel> target = new HashMap<String, AbstractUMLEntityModel> ();
	
	public JavaExportWizard(IJavaProject project, RootModel root){
		this.project = project;
		
		setWindowTitle(UMLJavaPlugin.getDefault().getResourceString("generate.dialog.title"));
		//setNeedsProgressMonitor(true);
		
		List<AbstractUMLModel> children = root.getChildren();
		for(int i=0;i<children.size();i++){
			Object child = children.get(i);
			if(child instanceof ClassModel){
				this.target.put(((ClassModel)child).getName(), (AbstractUMLEntityModel) child);
			}
			
		}
	}

	public void addPages() {
		Iterator<String> ite = target.keySet().iterator();
		List<String> classNames = new ArrayList<String>();
		while(ite.hasNext()){
			classNames.add(ite.next());
		}
		
		this.folderPage = new JavaExportWizardPage(project, 
				classNames.toArray(new String[classNames.size()]));
		
		addPage(folderPage);
	}

//	public boolean performFinish() {
//		return false;
//	}
	
	public boolean performFinish() {
		String outputDir = folderPage.getOutputFolder();
		if(outputDir.startsWith("/")){
			outputDir = outputDir.substring(1);
		}
		if(outputDir.endsWith("/")){
			outputDir = outputDir.substring(0, outputDir.length()-1);
		}
		
		String[] selection = folderPage.getGenerateClasses();
		
		for(int i=0;i<selection.length;i++){
			Object obj = target.get(selection[i]);
			String fileName = getModelName((AbstractUMLEntityModel)obj).replace('.','/');
			if(!outputDir.equals("")){
				fileName = outputDir + "/" + fileName;
			}
			fileName = fileName.replaceAll("<.+?>", "");
			
			// create parent folders
			String[] folders = fileName.split("/");
			StringBuffer sb = new StringBuffer();
			
			for(int j=0;j<folders.length-1;j++){
				sb.append("/").append(folders[j]);
				IFolder folder = project.getProject().getFolder(new Path(sb.toString()));
				if(!folder.exists()){
					try {
						folder.create(true, true, new NullProgressMonitor());
					} catch(Exception ex){
						ex.printStackTrace();
					}
				}
			}
			
			IFile file = project.getProject().getFile(new Path(fileName + ".java"));
			String source = "";
			
			if(obj instanceof ClassModel){
				source = createClassSource((ClassModel)obj);
			} 
			
			// create a source file
			try {
				if(file.exists()){
					file.setContents(new ByteArrayInputStream(source.getBytes()),
						true, true, new NullProgressMonitor());
				} else {
					file.create(new ByteArrayInputStream(source.getBytes()),
						true, new NullProgressMonitor());
				}
			} catch(Exception ex){
				ex.printStackTrace();
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Creates source code of Java class.
	 */
	private String createClassSource(ClassModel model){
		String[] names = splitClassName(model.getName());
		StringBuffer sb = new StringBuffer();
		
		if(!names[0].equals("")){
			sb.append("package ").append(names[0]).append(";\n\n");
		}
		
		sb.append("public ");
		if(model.isAbstract()){
			sb.append("abstract ");
		}
		sb.append("class ").append(names[1]);
		sb.append(createParentRelation(model));
		sb.append(" {\n");
		List<AbstractUMLModel> children = model.getChildren();
		for(int i=0;i<children.size();i++){
			Object child = children.get(i);
			if(child instanceof AttributeModel){
				sb.append(createAttribute((AttributeModel)child)).append("\n");
			}
		}
		sb.append("\n");
		for(int i=0;i<children.size();i++){
			Object child = children.get(i);
		}
		sb.append("}\n");
		return sb.toString();
	}
	
	
	/**
	 * Creates a part of an attribute.
	 */
	private String createAttribute(AttributeModel attr){
		StringBuffer sb = new StringBuffer();
		sb.append("    ");
		String visibility = attr.getVisibility().toString();
		if(!visibility.equals("package")){
			sb.append(attr.getVisibility().toString());
			sb.append(" ");
		}
		if(attr.isStatic()){
			sb.append("static ");
		}
		sb.append(attr.getType());
		sb.append(" ");
		sb.append(attr.getName());
		
		
		
		sb.append(";");
		return sb.toString();
	}
	
	
	
	private String createParentRelation(AbstractUMLEntityModel model){
		StringBuffer sb = new StringBuffer();
		List<AbstractUMLConnectionModel> conns = model.getModelSourceConnections();
		int extendsCount = 0;
		for(AbstractUMLConnectionModel conn: conns){
			if(conn instanceof GeneralizationModel){
				if(extendsCount==0){
					sb.append(" extends ");
				} else {
					sb.append(", ");
				}
				AbstractUMLEntityModel target = ((GeneralizationModel) conn).getTarget();
				sb.append(getModelName(target));
				extendsCount++;
			}
		}
		int implementsCount = 0;
		for(AbstractUMLConnectionModel conn: conns){
			if(conn instanceof RealizationModel){
				if(implementsCount==0){
					sb.append(" implements ");
				} else {
					sb.append(", ");
				}
				AbstractUMLEntityModel target = ((RealizationModel) conn).getTarget();
				sb.append(getModelName(target));
				implementsCount++;
			}
		}
		return sb.toString();
	}
	
	/**
	 * 
	 * 
	 * @param full qualified class name
	 * @return 
	 * <ul>
	 *   <li>0 - package name</li>
	 *   <li>1 - class name</li>
	 * </ul>
	 */
	private String[] splitClassName(String className){
		int index = className.lastIndexOf('.');
		if(index < 0){
			return new String[]{"",className};
		}
		return new String[]{
				className.substring(0, index),
				className.substring(index+1)
		};
	}
	
	/**
	 * 
	 * 
	 * @param type
	 * @return
	 */
	private String getDefaultValue(String type){
		if(type.equals("int") || type.equals("long") || type.equals("double") || 
		   type.equals("short") || type.equals("char")){
			return "0";
		}
		if(type.equals("boolean")){
			return "false";
		}
		return "null";
	}
	
	private String getModelName(AbstractUMLEntityModel model){
		if(model instanceof ClassModel){
			return ((ClassModel)model).getName();
		}
		
		return null;
	}

}
