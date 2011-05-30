package org.jyald;

import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jyald.core.LogcatShell;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Rectangle;

public class AdbShellDialog extends Dialog {

	private LogcatShell logcatShell;
	protected Object result;
	protected Shell shell;
	private StyledText txtShellOutput;
	private Text txtCommandInput;
	
	public AdbShellDialog(Shell parent, int style, String adb) {
		super(parent, style);
		setText("New Shell");
		logcatShell = new LogcatShell(adb,this);
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
		
		logcatShell.stopShell();
		
		return result;
	}
	
	protected void onTxtCommandInputKeyEnter() {
		String command = txtCommandInput.getText();
		
		txtCommandInput.setText("");
		
		logcatShell.sendShellInput(command);
	}
	
	protected void onResize() {
		if (txtShellOutput != null) {
			Rectangle rect = txtShellOutput.getBounds();
			rect.width = shell.getBounds().width - 20;
			rect.height = shell.getBounds().height - 120;
			
			txtShellOutput.setBounds(rect);
		}
	}
	
	private void createContents() {
		shell = new Shell(getParent(), SWT.CLOSE | SWT.MIN | SWT.RESIZE | SWT.TITLE);
		shell.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				onResize();
			}
		});
		shell.setSize(430, 306);
		shell.setText(getText());
		
		txtShellOutput = new StyledText(shell, SWT.BORDER);
		txtShellOutput.setBounds(10, 10, 397, 208);
		
		txtCommandInput = new Text(shell, SWT.BORDER);
		txtCommandInput.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.keyCode == 13) {
					onTxtCommandInputKeyEnter();
				}
			}
		});
		
		txtCommandInput.setBounds(10, 226, 397, 36);
		txtCommandInput.setFocus();
		logcatShell.startShell();
		
	}
	
	public void writeOutput(final String output) {
		Display.getDefault().asyncExec(new Runnable() {

			@Override
			public void run() {
				txtShellOutput.append(output + "\n");
				txtShellOutput.setSelection(txtShellOutput.getText().length());
			}
		});
	}
}
