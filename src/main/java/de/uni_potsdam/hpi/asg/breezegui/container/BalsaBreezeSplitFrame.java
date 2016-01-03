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

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;

import de.uni_potsdam.hpi.asg.breezegui.breezegraph.BreezePanel;
import de.uni_potsdam.hpi.asg.breezegui.io.BreezeGuiInvoker;
import de.uni_potsdam.hpi.asg.common.breeze.model.AbstractBreezeNetlist;
import de.uni_potsdam.hpi.asg.common.breeze.model.BreezeProject;
import de.uni_potsdam.hpi.asg.common.io.WorkingdirGenerator;

public class BalsaBreezeSplitFrame extends JFrame {

	private static final long serialVersionUID = 4788036033920633439L;
	
	public BalsaBreezeSplitFrame(File codefile, WindowAdapter adapt) {
		super("Balsa Editor");
		
		this.addWindowListener(adapt);
		
		// Breeze
		BreezePanel breezepanel = new BreezePanel();
		try {
			BreezeGuiInvoker.getInstance().invokeBalsaC(codefile.getCanonicalPath());
		} catch(Exception e) {
			e.printStackTrace();
		}
		File breezefile = new File(WorkingdirGenerator.getInstance().getWorkingdir() + codefile.getName().replace("balsa", "breeze"));
		BreezeProject proj = BreezeProject.create(breezefile, null, false, false);
		AbstractBreezeNetlist netlist = null;
		for(AbstractBreezeNetlist n : proj.getSortedNetlists()) {
			netlist = n;
		}
		breezepanel.setNetlist(netlist, 1);
		
		// Balsa
		JPanel cp = new JPanel(new BorderLayout());
		RSyntaxTextArea textArea = new RSyntaxTextArea(20, 60);
		textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
		textArea.setCodeFoldingEnabled(true);
		
		try {
			FileReader reader = new FileReader(codefile.getCanonicalFile());
	        BufferedReader br = new BufferedReader(reader);
	        textArea.read( br, null );
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		RTextScrollPane sp = new RTextScrollPane(textArea);
		cp.add(sp);
		
		// SplitPane
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, cp, breezepanel);
		
		this.getContentPane().add(splitPane);
		this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	}
}
