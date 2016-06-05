package util;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.part.FileEditorInput;


public class ShowFile {
	static public void open(String fName) {
		if (fName != null) {
			IWorkbenchPage wbPage = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			IFile file = getFile(fName);
			try {
				if (file != null) {
					IDE.openEditor(wbPage, file);
				}
			} catch (PartInitException e) {
				e.printStackTrace();
			}
			return;
		}
	}
	static public void open(IFile file) {
		
			IWorkbenchPage wbPage = PlatformUI.getWorkbench()
					.getActiveWorkbenchWindow().getActivePage();
			try {
				if (file != null) {
					IDE.openEditor(wbPage, file);
				}
			} catch (PartInitException e) {
				e.printStackTrace();
			}
			return;
		
	}
	/**
	 * 根据文件名并指导其相对路径，取得该文件的IFile对象
	 * @author lifeng
	 * @param fileName
	 * @return
	 * IFile
	 * @datetime 2012-9-5 上午07:27:47
	 */
	static private IFile getFile(String fileName) {
		IProject prj = getActiveProject();
		
		if (prj == null) {
			return null;
		}
		
		try {
			prj.refreshLocal(IResource.DEPTH_INFINITE, null);
		} catch (CoreException e) {
			System.out.println("无法刷新");
			e.printStackTrace();
			
		}
		return prj.getFile(fileName);
	}
	/**
	 * 获取当前文件相关的项目对象
	 * @author leven
	 * @return
	 * IProject
	 */
	static private IProject getActiveProject() {
		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		if (editor.getEditorInput() instanceof FileEditorInput) {
			FileEditorInput fei = (FileEditorInput) editor.getEditorInput();
			IFile f = fei.getFile();
			return f.getProject();
		}
		return null;
	}
	
}
