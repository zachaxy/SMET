package net.java.amateras.uml.classdiagram.action;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.eclipse.jface.dialogs.*;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

//import net.java.amateras.uml.classdiagram.action.SampleListViewer.Language;
import net.java.amateras.uml.classdiagram.model.AttributeModel;
import net.java.amateras.uml.classdiagram.model.GeneralizationModel;

public class ConnUpdateDialog extends org.eclipse.jface.dialogs.Dialog {

	private Shell s;
	private Composite comp;
	private GeneralizationModel var;
	Text textVar;
	ListViewer listViewer;
	private int status;

	public int getStatus() {
		return status;
	}

	public ConnUpdateDialog(Shell parentShell, GeneralizationModel v) {
		super(parentShell);
		s = parentShell;
		var = v;

		// TODO Auto-generated constructor stub
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		// TODO Auto-generated method stub
		this.getShell().setText("设置动作");

		// parent.setLayout(new RowLayout());
		comp = new Composite(parent, SWT.NONE);
		// comp.setSize(600, 800);
		// GridLayout gl = new GridLayout();
		// gl.numColumns = 2;

		// comp.setLayout(gl);

		/*
		 * Label label = new Label(parent, SWT.BORDER); label.setText("更新动作:");
		 * textVar = new Text(parent, SWT.SINGLE | SWT.LEAD | SWT.BORDER);
		 * textVar.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
		 * false)); textVar.setText(var.buildUpdates());
		 * textVar.setSelection(0,textVar.getText().length());
		 * 
		 */
		// parent.setSize(520, 650);
		init();

		addButtons();
		// this.getShell().pack();

		Control c = super.createDialogArea(parent);

		return c;
	}

	@Override
	protected void buttonPressed(int buttonId) {
		// TODO Auto-generated method stub

		if (buttonId == IDialogConstants.OK_ID) {
			// var.parseUpdate(textVar.getText());
			status = 1;
		} else {
			status = 0;
		}
		super.buttonPressed(buttonId);
	}

	/**
	 * Represents programming languages.
	 *
	 */
	/*
	 * public static class Language { public String genre; public boolean
	 * isObjectOriented;
	 * 
	 * public Language() { } public Language(String genre, boolean
	 * isObjectOriented) { this.genre = genre; this.isObjectOriented =
	 * isObjectOriented; }
	 * 
	 * public String toString() { return "Lang: " + genre + " [" +
	 * (isObjectOriented ? "Object-oriented" : "Procedural") + "]"; } }
	 * 
	 * Vector languages = new Vector();
	 */
	private void init() {
		/*
		 * languages.add(new Language("Java", true)); languages.add(new
		 * Language("C", false)); languages.add(new Language("C++", true));
		 * languages.add(new Language("SmallTalk", true));
		 */
		listViewer = new ListViewer(comp);
		listViewer.getControl().setLocation(20, 10);
		listViewer.getControl().setSize(300, 250);

		listViewer.setContentProvider(new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				List<String> v = ((GeneralizationModel) inputElement).getUpdates();
				return v.toArray();
			}

			public void dispose() {
				System.out.println("Disposing ...");
			}

			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				System.out.println("Input changed: old=" + oldInput + ", new=" + newInput);
			}
		});

		// listViewer.setContentProvider(new ArrayContentProvider());

		listViewer.setInput(var);

		listViewer.setLabelProvider(new LabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				return ((String) element);
			}
		});

		listViewer.addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				IStructuredSelection selection = (IStructuredSelection) event.getSelection();
				StringBuffer sb = new StringBuffer("Selection - ");
				sb.append("tatal " + selection.size() + " items selected: ");
				for (Iterator iterator = selection.iterator(); iterator.hasNext();) {
					sb.append(iterator.next() + ", ");
				}
				System.out.println(sb);
			}
		});

		// listViewer.addFilter(new ViewerFilter() {
		// public boolean select(
		// Viewer viewer,
		// Object parentElement,
		// Object element) {
		//
		// if(((Language)element).isObjectOriented)
		// return true;
		// else
		// return false;
		// }
		// });

		// listViewer.setSorter(new ViewerSorter(){
		// public int compare(Viewer viewer, Object e1, Object e2) {
		// return ((Language)e1).genre.compareTo(((Language)e2).genre);
		// }
		//
		// });

		// Object[] toBeSelectedItems = new Object[2];
		// toBeSelectedItems[0] = languages.elementAt(languages.size()-1);
		// toBeSelectedItems[1] = languages.elementAt(languages.size()-2);
		// IStructuredSelection selection = new
		// StructuredSelection(toBeSelectedItems);
		//
		// listViewer.setSelection(selection);

		//
		// Vector v = new Vector();
		// v.addElement(new Language("VB", true));
		//
		// listViewer.setInput(v);
		//
		// listViewer.add(new Language("C#", true));
	}

	Button buttonAdd;
	Button buttonRemove;
	Button buttonModify;
	Button up;
	Button down;

	private void addButtons() {
		Composite composite = comp;// new Composite(this.getShell(), SWT.NULL);
		// FillLayout fillLayout = new FillLayout(SWT.VERTICAL);
		// fillLayout.spacing = 2;

		// composite.setLayout(fillLayout);

		buttonAdd = new Button(composite, SWT.PUSH);
		buttonAdd.setText("添加");
		buttonAdd.setLocation(340, 10);
		buttonAdd.setSize(60, 40);

		buttonModify = new Button(composite, SWT.PUSH);
		buttonModify.setText("修改");
		buttonModify.setLocation(340, 60);
		buttonModify.setSize(60, 40);

		buttonRemove = new Button(composite, SWT.PUSH);
		buttonRemove.setText("删除");
		buttonRemove.setLocation(340, 110);
		buttonRemove.setSize(60, 40);

		up = new Button(composite, SWT.PUSH);
		up.setText("上移");
		up.setLocation(340, 160);
		up.setSize(60, 40);

		down = new Button(composite, SWT.PUSH);
		down.setText("下移");
		down.setLocation(340, 210);
		down.setSize(60, 40);

		buttonAdd.addSelectionListener(new SelectionAdapter() {

			public void widgetSelected(SelectionEvent e) {
				String value;
				InputDialog inputDialog = new InputDialog(comp.getShell(), "添加动作", "新动作", "", null);
				if (inputDialog.open() == InputDialog.OK) {
					value = inputDialog.getValue();
					var.getUpdates().add(value);
				}

				listViewer.refresh(false);

			}
		});

		buttonModify.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();
				String language = (String) selection.getFirstElement();

				if (language == null) {
					MessageDialog.openInformation(comp.getShell(), "提示", "请选择一项再重试。");
					return;
				}
				String value;
				InputDialog inputDialog = new InputDialog(comp.getShell(), "修改", "动作:", language, null);
				if (inputDialog.open() == InputDialog.OK) {
					value = inputDialog.getValue();
					int i = var.getUpdates().indexOf(language);
					var.getUpdates().set(i, value);
				}
				System.out.println("language=" + language);
				// listViewer.update(language, null);
				listViewer.refresh(false);
			}
		});

		buttonRemove.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();
				String language = (String) selection.getFirstElement();
				if (language == null) {
					System.out.println("Please select a language first.");
					return;
				}

				var.getUpdates().remove(language);
				System.out.println("Removed: " + language);

				listViewer.refresh(false);
			}
		});

		up.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();
				String language = (String) selection.getFirstElement();
				int i = var.getUpdates().indexOf(language);
				if (language == null) {
					MessageDialog.openInformation(comp.getShell(), "提示", "请选择一项再重试。");
					return;
				}
				if (language != null && i > 0) {
					String t = var.getUpdates().get(i - 1);
					var.getUpdates().set(i - 1, var.getUpdates().get(i));
					var.getUpdates().set(i, t);
				}
				System.out.println("language=" + language);
				// listViewer.update(language, null);
				listViewer.refresh(false);
			}
		});

		down.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) listViewer.getSelection();
				String language = (String) selection.getFirstElement();
				int i = var.getUpdates().indexOf(language);
				if (language == null) {
					MessageDialog.openInformation(comp.getShell(), "提示", "请选择一项再重试。");
					return;
				}
				if (language != null && i < var.getUpdates().size() - 1) {
					String t = var.getUpdates().get(i + 1);
					var.getUpdates().set(i + 1, var.getUpdates().get(i));
					var.getUpdates().set(i, t);
				}

				// listViewer.update(language, null);
				listViewer.refresh(false);
			}
		});
	}
}
