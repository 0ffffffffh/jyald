package org.jyald;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.jyald.core.AdbValidator;

public class AdbValidatorDialog extends DialogExtender {

	protected Object result;
	protected Shell shell;
	private AdbValidator validator;
	
	public AdbValidatorDialog(Shell parent, int style, String execFile) {
		super(parent, style);
		setText("Adb Executable Validation");
		validator = new AdbValidator(execFile);
	}
	
	private void doValidate() {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				result = (Boolean)validator.validate();
				shell.close();
			}
			
		});
	}
	
	public Object open() {
		createContents();
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
		
		locateCenter();
		
		doValidate();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(239, 74);
		shell.setText(getText());
		
		Label lblValidatingPleaseWait = new Label(shell, SWT.NONE);
		lblValidatingPleaseWait.setBounds(10, 10, 201, 15);
		lblValidatingPleaseWait.setText("Validating. Please wait.");

	}

	@Override
	public Shell getShell() {
		return shell;
	}
}
