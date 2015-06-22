package edu.ncsu.csc.dlf.errorview;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.ui.text.java.IInvocationContext;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jdt.ui.text.java.IProblemLocation;
import org.eclipse.jdt.ui.text.java.IQuickFixProcessor;

public class Joiner implements IQuickFixProcessor {

  @Override
  public IJavaCompletionProposal[] getCorrections(IInvocationContext context,
      IProblemLocation[] locations) throws CoreException {
    System.out.println("selectionOffset: " + context.getSelectionOffset());
    System.out.println("selectionLength: " + context.getSelectionLength());

    for (int i = 0; i < locations.length; ++i) {
      System.out.println("problemId: " + locations[i].getProblemId());
    }

    return new IJavaCompletionProposal[] {new ErrorViewCompletionProposal()};
  }

  @Override
  public boolean hasCorrections(ICompilationUnit unit, int problemId) {
    return true;
  }
}
