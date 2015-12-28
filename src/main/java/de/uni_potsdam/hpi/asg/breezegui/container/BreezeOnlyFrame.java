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

import javax.swing.JFrame;

import com.mxgraph.view.mxGraph;

import de.uni_potsdam.hpi.asg.breezegui.breezegraph.BreezePanel;
import de.uni_potsdam.hpi.asg.common.breeze.model.AbstractBreezeNetlist;

public class BreezeOnlyFrame extends JFrame{

	private static final long serialVersionUID = -5235595070135474441L;
	
	private BreezePanel breezepanel;
	
	public BreezeOnlyFrame(AbstractBreezeNetlist list, WindowAdapter adapt, int rootchan) {
		super("HS Viewer");
		
		this.addWindowListener(adapt);
		this.breezepanel = new BreezePanel(list, rootchan);
		this.getContentPane().add(breezepanel.getGraphComponent());
	}
	
	public mxGraph getGraph() {
		return breezepanel.getGraph();
	}
}
