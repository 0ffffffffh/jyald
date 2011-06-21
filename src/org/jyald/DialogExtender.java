/*
 * JYald
 * 
 * Copyright (C) 2011 Oguz Kartal
 * 
 * This file is part of JYald
 * 
 * JYald is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * JYald is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with JYald.  If not, see <http://www.gnu.org/licenses/>.
 */


package org.jyald;

import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;

public abstract class DialogExtender extends Dialog {
	
	public DialogExtender(Shell parent) {
		super(parent);
	}
	
	public DialogExtender(Shell parent, int style) {
		super(parent, style);
	}

	public Monitor getMonitor() {
		return getParent().getDisplay().getPrimaryMonitor();
	}
	
	public void locateCenter() {
		Rectangle bounds = getMonitor().getBounds();
		
		Rectangle shellRect = getShell().getBounds();
		
		getShell().setLocation(bounds.x + (bounds.width - shellRect.width) / 2,
				bounds.y + (bounds.height - shellRect.height) / 2);
		
	}
	
	public abstract Shell getShell();
	
}
