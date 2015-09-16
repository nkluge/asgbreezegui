package de.uni_potsdam.hpi.asg.breezegui.breezegraph;

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

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.KeyStroke;

import com.mxgraph.model.mxCell;
import com.mxgraph.swing.mxGraphComponent;
import com.mxgraph.view.mxGraph;

import de.uni_potsdam.hpi.asg.common.breeze.model.AbstractBreezeNetlist;
import de.uni_potsdam.hpi.asg.common.breeze.model.ComponentInst;
import de.uni_potsdam.hpi.asg.common.breeze.model.HSChannel;

public class MainFrame extends JFrame implements MouseWheelListener, MouseListener {
	private static final long serialVersionUID = -7423653526804246359L;
	
	private AbstractBreezeNetlist list;
	private mxGraph graph;
	private mxGraphComponent graphComponent;
	private Object defaultParent;
	private Map<ComponentInst, HSView> viewMap;
	private Map<HSChannel, List<HSView>> chanMap;
	private Map<Object, HSView> cellMap;
    
	public MainFrame(AbstractBreezeNetlist list, WindowAdapter adapt, int rootchan) {
		super("HS Viewer");
		this.list = list;
		viewMap = new HashMap<ComponentInst, HSView>();
		chanMap = new HashMap<HSChannel, List<HSView>>();
		cellMap = new HashMap<>();
		
		graph = initMxGraph();
		graphComponent = new mxGraphComponent(graph);
		graphComponent.addMouseWheelListener(this);
		graphComponent.getGraphControl().addMouseListener(this);
		graphComponent.getInputMap().put(KeyStroke.getKeyStroke('s'), "save");
		graphComponent.getActionMap().put("save", new ExportAction());
		defaultParent = graph.getDefaultParent();
		graphComponent.getGraphHandler().setLivePreview(true);
		this.addWindowListener(adapt);
		init(rootchan);
	}
	
	private mxGraph initMxGraph() {
		return new mxGraph() {
			@Override
			public boolean isCellMovable(Object cell) {
				if(cell instanceof mxCell) {
					mxCell cell2 = (mxCell)cell;
					if(cell2.isEdge()) {
						return false;
					} else if(cell2.getParent() != getDefaultParent()) {
						return false;
					} else {
						return true;
					}
				}
				return false;
			}
		};
	}
	
	private void init(int rootchan) {
	    Map<HSChannel, mxCell> activeportmap = new HashMap<HSChannel, mxCell>();
	    Map<HSChannel, mxCell> passiveportmap = new HashMap<HSChannel, mxCell>();
	    
	    HSView root = null;
	    
	    List<ComponentInst> list2 = new ArrayList<ComponentInst>();
	    list2.addAll(list.getAllHSInstances());
	    list2.addAll(list.getAllPorts());
	    list2.addAll(list.getSubBreezeInst());
	    for(ComponentInst inst : list2) {
	    	HSView view = new HSView(inst, graph);
    		viewMap.put(inst, view);
    		HSViewReturn ret = view.getView();
    		graph.addCell(ret.getCell(), defaultParent);
    		cellMap.put(ret.getCell(), view);
    		activeportmap.putAll(ret.getActiveportmap());
    		passiveportmap.putAll(ret.getPassiveportmap());
    		
    		Set<HSChannel> chans = new HashSet<HSChannel>(); 
    		chans.addAll(ret.getActiveportmap().keySet());
    		chans.addAll(ret.getPassiveportmap().keySet());
    		for(HSChannel chan : chans) {
    			if(chanMap.containsKey(chan)) {
    				chanMap.get(chan).add(view);
    			} else {
    				List<HSView> temp = new ArrayList<HSView>();
    				temp.add(view);
    				chanMap.put(chan, temp);
    			}	    			
    		}
    		
    		if(root == null) {
	    		for(HSChannel chan : ret.getActiveportmap().keySet()) {
	    			if(chan.getId() == rootchan) {
	    				root = view;
	    			}
	    		}
    		}
	    }
	    
	    for(Entry<HSChannel, mxCell> entry : passiveportmap.entrySet()) {
	    	HSChannel chan = entry.getKey();
	    	
	    	mxCell passivecell = entry.getValue();
	    	mxCell activecell = activeportmap.get(chan);
	    	
	    	String label = "" + chan.getId();
	    	String style = "strokeColor=black;fontColor=black;fontSize=16;";
	    	if(chan.getDatawidth()==0) {
	    		style += "startArrow=none;endArrow=none";
	    	} else {
	    		label += " (" + chan.getDatawidth() + ")";
	    		style += "startArrow=none;endArrow=classic;endSize=11";
	    		if(entry.getKey().getDatatype() == HSChannel.DataType.pull) {
	    			mxCell temp = activecell;
	    			activecell = passivecell;
	    			passivecell = temp;
	    		}
	    	}
	    	graph.insertEdge(defaultParent, null, label, activecell, passivecell, style); //mxCell edge = (mxCell)
	    }
		
	    if(root != null) {
	    	root.computeSpace(chanMap);
	    	root.placeBoxAndChilds(0, 0);
	    }
	    
	    graph.getModel().endUpdate();
	    
	    getContentPane().add(graphComponent);
	    	    
	    graph.setCellsEditable(false);
	    graph.setCellsMovable(true);
	    graph.setCellsResizable(false);
	    graphComponent.setConnectable(false);
	}
	
	public mxGraph getGraph() {
		return graph;
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		double scale = graph.getView().getScale();
		int scale_int = (int)(scale * 100);
		
		if(e.getWheelRotation() < 0) {
			if(scale_int <= 95) {
				scale_int += 5;
			}
		} else {
			if(scale_int >= 10) {
				scale_int -= 5;
			}
		}
		scale = scale_int/100.0;
		
		graph.getView().scaleAndTranslate(scale, graph.getView().getGraphBounds().getX() / 2, graph.getView().getGraphBounds().getY() / 2);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getClickCount() == 2) {
			Object cell = graphComponent.getCellAt(e.getX(), e.getY());
			if(cell != null) {
				if(cellMap.containsKey(cell)) {
					cellMap.get(cell).openSubBreezeWindow();
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
			
	}
}
