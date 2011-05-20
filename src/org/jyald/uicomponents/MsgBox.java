package org.jyald.uicomponents;

import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

public class MsgBox {
	public static int show(Shell owner,String title, String msg, int type) {
		MessageBox msgBox = new MessageBox(owner,type);
		msgBox.setText(title);
		msgBox.setMessage(msg);
		return msgBox.open();
	}
}
