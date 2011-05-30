package org.jyald;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jyald.core.LogcatShell;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.SWT;

public class AdbShellDialog extends Dialog {

	private LogcatShell logcatShell;
	protected Object result;
	protected Shell shell;
	private StyledText txtShellOutput;
	
	
	public AdbShellDialog(Shell parent, int style) {
		super(parent, style);
		setText("New Shell");
		
		logcatShell = new LogcatShell();
	}

	
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(450, 300);
		shell.setText(getText());
		
		txtShellOutput = new StyledText(shell, SWT.BORDER);
		txtShellOutput.setBounds(21, 10, 397, 208);

		logcatShell.startShell();
		
	}
}
