package edu.ncsu.csc.dlf.errorview;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import java.util.Map;

public class Joiner implements IQuickFixProcessor{

  @Override
  public IJavaCompletionProposal[] getCorrections(IInvocationContext context,
      IProblemLocation[] locations) throws CoreException {

    return new IJavaCompletionProposal[]{new IJavaCompletionProposal() {

      @Override
      public String getDisplayString() {
        return "Show With Other Errors...";
      }


      @Override
      public void apply(IDocument document) {
        IWorkbenchPage page = PlatformUI.getWorkbench()
            .getActiveWorkbenchWindow().getActivePage();

        IEditorInput editorInput = page.getActiveEditor().getEditorInput();
        if(editorInput instanceof IFileEditorInput){
          IFileEditorInput fileEditorInput = (IFileEditorInput) editorInput;
          IFile file = fileEditorInput.getFile();
          try {
            // TODO really we don't want all errors in the file,
            // but instead all errors related to this error.
            IMarker[] markers = file.findMarkers(IMarker.PROBLEM, true,
                IResource.DEPTH_INFINITE);
            for (IMarker m : markers) {
              Map<String, Object> attributes =  m.getAttributes();
              for (String key : attributes.keySet()) {
                System.out.println(key + ": " + attributes.get(key));
              }
              // TODO (IFileEditorInput)m.getResource()
            }
          } catch (CoreException e) {
            e.printStackTrace();
            System.exit(1);
          }
          try {

            //open our editor, ignoring the fact that the "file" is already open
            page.openEditor(new FileEditorInput(file),
                    "edu.ncsu.csc.dlf.errorview.editor",
                    true,
                    IWorkbenchPage.MATCH_ID | IWorkbenchPage.MATCH_INPUT);
          } catch (PartInitException e) {
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
    }};
  }

  @Override
  public boolean hasCorrections(ICompilationUnit unit, int problemId) {
    return true;
  }

}
