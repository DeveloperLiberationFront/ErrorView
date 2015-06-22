package edu.ncsu.csc.dlf.errorview.editors;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;

@SuppressWarnings("restriction")
public class Editor extends EditorPart {

  private List<CompilationUnitEditor> editors;

  public Editor() {
    editors = new ArrayList<CompilationUnitEditor>();
  }

  @Override
  public void init(IEditorSite site, IEditorInput input)
      throws PartInitException {

    super.setInput(input);
    super.setSite(site);

    for (int i = 0; i < 3; i++) {
      CompilationUnitEditor editor = new CompilationUnitEditor();
      editor.init(site, input);
      editors.add(editor);
    }
  }

  @Override
  public void createPartControl(Composite parent) {
    parent.setLayout(new FillLayout(SWT.VERTICAL));

    for (CompilationUnitEditor editor : editors) {
      editor.createPartControl(parent);
    }
  }

  @Override
  public void doSave(IProgressMonitor monitor) {}

  @Override
  public boolean isDirty() {
    return false;
  }

  @Override
  public boolean isSaveAsAllowed() {
    return false;
  }

  @Override
  public void doSaveAs() {}

  @Override
  public void setFocus() {}
}
