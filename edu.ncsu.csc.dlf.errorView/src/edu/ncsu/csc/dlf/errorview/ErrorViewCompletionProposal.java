package edu.ncsu.csc.dlf.errorview;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.MultiEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.IEditorDescriptor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.ncsu.csc.dlf.errorview.editors.ErrorEditorInput;

public class ErrorViewCompletionProposal implements IJavaCompletionProposal {
  private IInvocationContext context;
  private IProblemLocation[] locations;
  private ArrayList<Integer> problemIds;

  public ErrorViewCompletionProposal(IInvocationContext context,
      IProblemLocation[] locations) throws CoreException {
    this.context = context;
    this.locations = locations;

    System.out.println("selectionOffset: " + context.getSelectionOffset());
    System.out.println("selectionLength: " + context.getSelectionLength());

    this.problemIds = new ArrayList<Integer>(locations.length);

    for (int i = 0; i < locations.length; ++i) {
      System.out.println("problemId: " + locations[i].getProblemId());
      this.problemIds.add(locations[i].getProblemId());
    }
  }


  @Override
  public String getDisplayString() {
    return "Show With Other Errors...";
  }

  @Override
  public void apply(IDocument document) {
    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
        .getActivePage();
    IEditorInput editorInput = page.getActiveEditor().getEditorInput();

    if (editorInput instanceof IFileEditorInput) {
      IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
      IFile file = fileEditorInput.getFile();

      try {
        IMarker[] markers = this.findMarkers(file.getProject());
        System.out.println("errors to dislay: " + markers.length);
        IFileEditorInput[] inputs = new IFileEditorInput[markers.length];
        String[] editorIds = new String[markers.length];

        for (int i = 0; i < markers.length; ++i) {
          inputs[i] = new ErrorEditorInput(markers[i]);
          editorIds[i] = "edu.ncsu.csc.dlf.errorview.editor";
        }

        page.openEditor(
            new MultiEditorInput(editorIds, inputs),
            "edu.ncsu.csc.dlf.errorview.editor",
            true,
            IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
      } catch (Exception e) {
        // TODO handle error gracefully
        e.printStackTrace();
        System.exit(1);
      }

    }
  }

  @Override
  public IContextInformation getContextInformation() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public String getAdditionalProposalInfo() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int getRelevance() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public Point getSelection(IDocument document) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Image getImage() {
    // TODO Auto-generated method stub
    return null;
  }

  /**
   * Return the markers from `project` that are relevant to the problem
   * locations this completion proposal was created from.
   */
  protected IMarker[] findMarkers(IProject project) throws CoreException {
    IMarker[] markers = project.findMarkers(IMarker.PROBLEM, true,
        IResource.DEPTH_INFINITE);
    List<IMarker> markersList = new ArrayList<IMarker>(Arrays.asList(markers));

    for (Iterator<IMarker> it = markersList.iterator(); it.hasNext();) {
      Integer problemId = (Integer) it.next().getAttribute("id");
      if (!this.problemIds.contains(problemId)) {
        it.remove();
      }
    }

    return markersList.toArray(new IMarker[markersList.size()]);
  }
}
