package edu.ncsu.csc.dlf.errorview.editors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.internal.ui.javaeditor.CompilationUnitEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.part.MultiEditorInput;

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

    IEditorInput[] inputs = ((MultiEditorInput)input).getInput();

    for (int i = 0; i < inputs.length; ++i) {
      CompilationUnitEditor editor = new CompilationUnitEditor();
      editor.init(site, inputs[i]);
      editors.add(editor);
    }
  }

  @Override
  public void createPartControl(Composite parent) {
    parent.setLayout(new FillLayout(SWT.VERTICAL));

    IEditorInput[] inputs = ((MultiEditorInput)this.getEditorInput())
        .getInput();

    for (int i = 0; i < editors.size(); ++i) {
      CompilationUnitEditor editor = editors.get(i);
      editor.createPartControl(parent);

      try {
        IMarker marker = ((ErrorEditorInput)inputs[i]).marker;
        Map<String, Object> attributes =  marker.getAttributes();

        for (String key : attributes.keySet()) {
          System.out.println(key + ": " + attributes.get(key));
        }

        Integer start = (Integer)marker.getAttribute("charStart");
        Integer end = (Integer)marker.getAttribute("charEnd");
        Integer len = end - start;
        System.out.println(end + " - " + start + " = " + len);

        editor.selectAndReveal(start, len);
      } catch (CoreException e) {
        // TODO handle error gracefully
        e.printStackTrace();
        System.exit(1);
      }

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
