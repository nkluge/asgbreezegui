package de.uni_potsdam.hpi.asg.breezegui.container;

/*
 * Copyright (C) 2015 Norman Kluge
 * 
 * This file is part of ASGBreezeGui.
 * 
 * ASGBreezeGui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASGBreezeGui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ASGBreezeGui.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JFrame;

public class BalsaBreezeSplitGui extends WindowAdapter {
	private BalsaBreezeSplitFrame frame;
	private boolean closed;
	
	public BalsaBreezeSplitGui(File codefile) {
		frame = new BalsaBreezeSplitFrame(codefile, this);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		closed = true;
	}
	
	public void show() {
		closed = false;
		frame.pack();
		frame.setVisible(true);
	}
	
	@Override
	public void windowClosed(WindowEvent e) {
		super.windowClosed(e);
		closed = true;
	}

	public boolean isClosed() {
		return closed;
	}
}
