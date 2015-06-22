package edu.ncsu.csc.dlf.errorview.editors;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.ui.part.FileEditorInput;

public class ErrorEditorInput extends FileEditorInput {
  public IMarker marker;

  public ErrorEditorInput(IMarker marker) {
    super((IFile)marker.getResource());
    this.marker = marker;
  }
}
