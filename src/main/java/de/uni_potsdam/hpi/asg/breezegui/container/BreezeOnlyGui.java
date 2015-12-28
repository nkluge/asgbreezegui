package de.uni_potsdam.hpi.asg.breezegui.container;

/*
 * Copyright (C) 2012 - 2015 Norman Kluge
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

import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import de.uni_potsdam.hpi.asg.breezegui.breezegraph.ExportAction;
import de.uni_potsdam.hpi.asg.common.breeze.model.AbstractBreezeNetlist;

public class BreezeOnlyGui extends WindowAdapter {
	private BreezeOnlyFrame frame;
	private boolean closed;
	
	public BreezeOnlyGui(AbstractBreezeNetlist list, int rootchan) {
		frame = new BreezeOnlyFrame(list, this, rootchan);
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		closed = true;
	}
	
	public void show() {
		closed = false;
		frame.pack();
		frame.setVisible(true);
	}
	
	public boolean exportSvg(File file) {
		mxGraph graph = frame.getGraph();
		ExportAction a = new ExportAction();
		return a.exportSvg(graph, file);
	}
	public boolean exportPng(File file) {
		mxGraph graph = frame.getGraph();
		ExportAction a = new ExportAction();
		return a.exportPng(file, graph, new mxGraphComponent(graph));
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
