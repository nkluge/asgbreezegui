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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mxgraph.model.mxCell;
import com.mxgraph.model.mxGeometry;
import com.mxgraph.util.mxPoint;
import com.mxgraph.view.mxGraph;

import de.uni_potsdam.hpi.asg.common.breeze.model.BreezeNetlistInst;
import de.uni_potsdam.hpi.asg.common.breeze.model.ComponentInst;
import de.uni_potsdam.hpi.asg.common.breeze.model.HSChannel;
import de.uni_potsdam.hpi.asg.common.breeze.model.HSComponentInst;
import de.uni_potsdam.hpi.asg.common.breeze.model.PortComponent;
import de.uni_potsdam.hpi.asg.common.breeze.model.xml.Parameter.ParameterType;

public class HSView {
	
	protected static final int	PORT_DIAMETER	= 12;
	protected static final int	PORT_RADIUS		= PORT_DIAMETER / 2;
	protected static final int	COMP_SIZE		= 70;
	protected static final int	PORT_DIST		= 25;
	private static final int	COMP_VDIST		= 100;
	private static final int	COMP_HDIST		= 100;

	private ComponentInst inst;

	protected Map<HSChannel, Integer>	dataInChanMap;
	protected Map<HSChannel, Integer>	dataOutChanMap;
	protected Map<HSChannel, Integer>	controlInChanMap;
	protected Map<HSChannel, Integer>	controlOutChanMap;

	private Map<Integer, HSView>	dataInViewMap;
	private Map<Integer, HSView>	dataOutViewMap;
	private Map<Integer, HSView>	controlInViewMap;
	private Map<Integer, HSView>	controlOutViewMap;

	private HSView		parent;
	protected mxCell	cell;
	private mxGraph		graph;
	private Box			overallBox;

	private double	xControlOut	= 0;
	private double	yControlOut	= 0;
	private double	xDataIn		= 0;
	private double	yDataIn		= 0;
	private double	xDataOut	= 0;
	private double	yDataOut	= 0;

	private LayoutType layout;

	private enum LayoutType {
		SILCSO, //small InData, large Control, small OutData
		MIMCSO, //medium InData, medium Control, small OutData
		SIMCMO, //small InData, medium Control, medium OutData
		MISCMO, //medium InData, small Control, medium OutData
		Default //everything large
	}

	public HSView(ComponentInst inst, mxGraph graph) {
		dataInViewMap = new HashMap<Integer, HSView>();
		dataOutViewMap = new HashMap<Integer, HSView>();
		controlInViewMap = new HashMap<Integer, HSView>();
		controlOutViewMap = new HashMap<Integer, HSView>();

		dataInChanMap = new HashMap<HSChannel, Integer>();
		dataOutChanMap = new HashMap<HSChannel, Integer>();
		controlInChanMap = new HashMap<HSChannel, Integer>();
		controlOutChanMap = new HashMap<HSChannel, Integer>();

		parent = null;
		this.graph = graph;
		this.inst = inst;
	}

	protected mxCell getStandardCell(String str) {
		mxGeometry geo = new mxGeometry(0, 0, COMP_SIZE, COMP_SIZE);
		mxCell cell = new mxCell(str, geo, "shape=ellipse;foldable=0;fillColor=white;strokeColor=black;fontSize=18;fontColor=black");
		cell.setVertex(true);
		cell.setConnectable(false);
		return cell;
	}

	private mxCell getPortCell(String str) {
		mxGeometry geo = new mxGeometry(0, 0, COMP_SIZE, COMP_SIZE);
		mxCell cell = new mxCell(str, geo, "shape=ellipse;foldable=0;fillColor=white;strokeColor=black;fontSize=18;fontColor=black;strokeWidth=4");
		cell.setVertex(true);
		cell.setConnectable(false);
		return cell;
	}
	
	private mxCell getSubBreezeCell(String str) {
		mxGeometry geo = new mxGeometry(0, 0, COMP_SIZE, COMP_SIZE);
		mxCell cell = new mxCell(str, geo, "shape=ellipse;foldable=0;fillColor=white;strokeColor=black;fontSize=18;fontColor=black;strokeWidth=2;dashed=1");
		cell.setVertex(true);
		cell.setConnectable(false);
		return cell;
	}

	protected mxCell getPort(mxCell cell, double posx, double posy, boolean active, int pos) {
		return getPort(cell, posx, posy, active, false, pos);
	}

	protected mxCell getPortArrayed(mxCell cell, double posx, double posy, boolean active, int pos) {
		return getPort(cell, posx, posy, active, true, pos);
	}

	// left:0, above:1, right:2, beneath:3
	private mxCell getPort(mxCell cell, double posx, double posy, boolean active, boolean arrayed, int pos) {
		mxGeometry geo = new mxGeometry(posx, posy, PORT_DIAMETER, PORT_DIAMETER);
		if(!arrayed) {
			switch(pos) {
				case 0:
					geo.setOffset(new mxPoint(-2 * PORT_RADIUS, -PORT_RADIUS));
					break;
				case 1:
					geo.setOffset(new mxPoint(-PORT_RADIUS, -2 * PORT_RADIUS));
					break;
				case 2:
					geo.setOffset(new mxPoint(0, -PORT_RADIUS));
					break;
				case 3:
					geo.setOffset(new mxPoint(-PORT_RADIUS, 0));
					break;
			}

			geo.setRelative(true);
		}
		String portparam = "shape=ellipse;perimter=ellipsePerimeter;strokeColor=black;";
		if(active) {
			portparam += "fillColor=black";
		} else {
			portparam += "fillColor=white";
		}
		mxCell port = new mxCell(null, geo, portparam);
		port.setVertex(true);
		cell.insert(port);
		return port;
	}

	protected double getPos(int num) {
		double offset = (num - 1.0) / 2.0;
		double pos = (COMP_SIZE / 2.0) - (offset * PORT_DIST) - PORT_RADIUS;
		return pos;
	}

	public HSViewReturn getView() {
		if(inst instanceof PortComponent) {
			PortComponent inst2 = (PortComponent)inst;
			cell = getPortCell("\n" + inst2.getName() + "\n" + inst2.getDirection());
		} else if(inst instanceof BreezeNetlistInst) {
			BreezeNetlistInst inst2 = (BreezeNetlistInst)inst;
			cell = getSubBreezeCell(inst2.getInstantiatedNetlist().getName());
		} else {
			cell = getStandardCell("\n" + getSymbol() + "\n" + inst.getId());
		}
		HSViewReturn retVal = new HSViewReturn(cell);

		int i = 0;
		double posy = getPos(inst.getDataIn().size());
		for(HSChannel chan : inst.getDataIn()) {
			if(chan.getActive() == inst) {
				retVal.addActivePort(chan, getPortArrayed(cell, 0 - PORT_DIAMETER, posy, true, 0));
				dataInChanMap.put(chan, i++);
			} else {
				retVal.addPassivePort(chan, getPortArrayed(cell, 0 - PORT_DIAMETER, posy, false, 0));
			}
			posy += PORT_DIST;
		}
		i = 0;
		posy = getPos(inst.getDataOut().size());
		for(HSChannel chan : inst.getDataOut()) {
			if(chan.getActive() == inst) {
				retVal.addActivePort(chan, getPortArrayed(cell, COMP_SIZE, posy, true, 2));
				dataOutChanMap.put(chan, i++);
			} else {
				retVal.addPassivePort(chan, getPortArrayed(cell, COMP_SIZE, posy, false, 2));
			}
			posy += PORT_DIST;
		}
		i = 0;
		double posx = getPos(inst.getControlIn().size());
		for(HSChannel chan : inst.getControlIn()) {
			if(chan.getActive() == inst) {
				retVal.addActivePort(chan, getPortArrayed(cell, posx, 0 - PORT_DIAMETER, true, 1));
				controlInChanMap.put(chan, i++);
			} else {
				retVal.addPassivePort(chan, getPortArrayed(cell, posx, 0 - PORT_DIAMETER, false, 1));
			}
			posx += PORT_DIST;
		}
		i = 0;
		posx = getPos(inst.getControlOut().size());
		for(HSChannel chan : inst.getControlOut()) {
			if(chan.getActive() == inst) {
				retVal.addActivePort(chan, getPortArrayed(cell, posx, COMP_SIZE, true, 3));
				controlOutChanMap.put(chan, i++);
			} else {
				retVal.addPassivePort(chan, getPortArrayed(cell, posx, COMP_SIZE, false, 3));
			}
			posx += PORT_DIST;
		}

		return retVal;
	}

	private String getSymbol() {
		String symbol = null;
		if(inst instanceof HSComponentInst) {
			HSComponentInst inst2 = (HSComponentInst)inst;
			if(inst2.getComp() != null) {
				if(inst2.getComp().getSymbol() != null) {
					symbol = inst2.getComp().getSymbol();
				} else if((symbol = (String)inst2.getType().getParamValue(ParameterType.name)) != null) {
					symbol = symbol.replace("\"", "");
				} else if(inst2.getType().getParamValue(ParameterType.operator) != null) {
					BinaryOp op = new BinaryOp(inst2.getType());
					symbol = op.toString();
				} else if(inst2.getType().getParamValue(ParameterType.value) != null) {
					symbol = "= " + inst2.getType().getParamValue(ParameterType.value);
				} else {
					symbol = "##########";
				}
			}
		}
		return symbol;
	}

	public void place(double x, double y) {
		mxGeometry geo1 = graph.getModel().getGeometry(cell);
		mxGeometry geo = (mxGeometry)geo1.clone();
		geo.setX(x);
		geo.setY(y);
		graph.getModel().setGeometry(cell, geo);
	}

	public Box computeSpace(Map<HSChannel, List<HSView>> chanMap) {
		fillMaps(chanMap);

		for(HSView view : controlOutViewMap.values()) {
			if(view.getParent() == null) {
				view.setParent(this);
			}
		}
		for(HSView view : dataInViewMap.values()) {
			if(view.getParent() == null) {
				view.setParent(this);
			}
		}
		for(HSView view : dataOutViewMap.values()) {
			if(view.getParent() == null) {
				view.setParent(this);
			}
		}

		for(HSView view : controlOutViewMap.values()) {
			if(view.getParent() == this) {
				Box box = view.computeSpace(chanMap);
				xControlOut += box.getWidth();
				if(box.getHeight() > yControlOut) {
					yControlOut = box.getHeight();
				}
			}
		}

		for(HSView view : dataInViewMap.values()) {
			if(view.getParent() == this) {
				Box box = view.computeSpace(chanMap);
				yDataIn += box.getHeight();
				if(box.getWidth() > xDataIn) {
					xDataIn = box.getWidth();
				}
			}
		}

		for(HSView view : dataOutViewMap.values()) {
			if(view.getParent() == this) {
				Box box = view.computeSpace(chanMap);
				yDataOut += box.getHeight();
				if(box.getWidth() > xDataOut) {
					xDataOut = box.getWidth();
				}
			}
		}

		if((xControlOut > yDataIn) && (xControlOut > yDataOut)) {
			layout = LayoutType.SILCSO;
		} else if((xControlOut == yDataIn) && (xControlOut > yDataOut)) {
			layout = LayoutType.MIMCSO;
		} else if((xControlOut > yDataIn) && (xControlOut == yDataOut)) {
			layout = LayoutType.SIMCMO;
		} else if((xControlOut < xDataIn) && (xControlOut < xDataOut)) {
			layout = LayoutType.MISCMO;
		} else {
			layout = LayoutType.Default;
		}

		double x = 0;
		double y = 0;
		switch(layout) {
			case SILCSO:
				x = Math.max(xControlOut, xDataIn + COMP_SIZE + COMP_HDIST + xDataOut);
				y = Math.max(Math.max(yDataIn, yDataOut), COMP_SIZE + COMP_VDIST) + yControlOut;
				break;
			case MIMCSO:
				x = xDataIn + Math.max(xControlOut, COMP_SIZE + COMP_HDIST + xDataOut);
				y = Math.max(yDataIn, Math.max(yDataOut, COMP_SIZE + COMP_VDIST) + yControlOut);
				break;
			case SIMCMO:
				x = xDataOut + Math.max(xControlOut, COMP_SIZE + COMP_HDIST + xDataIn);
				y = Math.max(yDataOut, Math.max(yDataIn, COMP_SIZE + COMP_VDIST) + yControlOut);
				break;
			case MISCMO:
				x = xDataIn + Math.max(xControlOut, COMP_SIZE + COMP_HDIST) + xDataOut;
				y = Math.max(Math.max(yDataIn, yDataOut), COMP_SIZE + COMP_VDIST + yControlOut);
				break;
			case Default:
				x = xDataIn + Math.max(xControlOut, COMP_SIZE + COMP_HDIST) + xDataOut;
				y = Math.max(Math.max(yDataIn, yDataOut), COMP_SIZE + COMP_VDIST) + yControlOut;
				break;
		}
		overallBox = new Box(x, y);
		return overallBox;
	}

	public void placeBoxAndChilds(double startx, double starty) {
		double xDataInStart = 0;
		double yDataInStart = 0;
		double xControlOutStart = 0;
		double yControlOutStart = 0;
		double xDataOutStart = 0;
		double yDataOutStart = 0;

		switch(layout) {
			case SILCSO:
				double xtop = xDataIn + COMP_SIZE + COMP_HDIST + xDataOut;
				xDataInStart = startx + ((xtop > xControlOut) ? 0 : (xControlOut - xtop) / 2);
				yDataInStart = starty + ((yDataIn > yDataOut) ? 0 : (yDataOut - yDataIn) / 2.0);
				xControlOutStart = startx;
				yControlOutStart = starty + Math.max(Math.max(yDataIn, yDataOut), COMP_SIZE + COMP_VDIST);
				xDataOutStart = startx + ((xtop > xControlOut) ? 0 : (xControlOut - xtop) / 2 + xDataIn + COMP_SIZE + COMP_HDIST);
				yDataOutStart = starty + ((yDataOut > yDataIn) ? 0 : (yDataIn - yDataOut) / 2.0);
				break;
			case MIMCSO:
				xDataInStart = startx;
				yDataInStart = starty;
				xControlOutStart = startx + xDataIn;
				yControlOutStart = starty + Math.max(yDataOut, COMP_SIZE + COMP_VDIST);
				xDataOutStart = startx + xDataIn + COMP_SIZE + COMP_HDIST;
				yDataOutStart = starty;
				break;
			case SIMCMO:
				xDataInStart = startx;
				yDataInStart = starty;
				xControlOutStart = startx;
				yControlOutStart = starty + Math.max(yDataIn, COMP_SIZE + COMP_VDIST);
				xDataOutStart = startx + Math.max(xDataIn + COMP_SIZE + COMP_HDIST, xControlOut);
				yDataOutStart = starty;
				break;
			case MISCMO:
				xDataInStart = startx;
				yDataInStart = starty + ((yDataIn > yDataOut) ? 0 : (yDataOut - yDataIn) / 2.0);
				xControlOutStart = startx + xDataIn;
				yControlOutStart = starty + Math.max(Math.max(yDataIn, yDataOut), COMP_SIZE + COMP_VDIST);
				xDataOutStart = startx + xDataIn + Math.max(xControlOut, COMP_SIZE + COMP_HDIST);
				yDataOutStart = starty + ((yDataOut > yDataIn) ? 0 : (yDataIn - yDataOut) / 2.0);
				break;
			case Default:
				xDataInStart = startx;
				yDataInStart = starty + ((yDataIn > yDataOut) ? 0 : (yDataOut - yDataIn) / 2.0);
				xControlOutStart = startx + xDataIn;
				yControlOutStart = starty + Math.max(Math.max(yDataIn, yDataOut), COMP_SIZE + COMP_VDIST);
				xDataOutStart = startx + xDataIn + Math.max(xControlOut, COMP_SIZE + COMP_HDIST);
				yDataOutStart = starty + ((yDataOut > yDataIn) ? 0 : (yDataIn - yDataOut) / 2.0);
				break;
		}

		for(HSView view : dataInViewMap.values()) {
			if(view.getParent() == this) {
				Box box = view.getOverallBox();
				view.placeBoxAndChilds(xDataInStart + (xDataIn - box.getWidth()), yDataInStart);
				yDataInStart += box.getHeight();
			}
		}

		for(HSView view : dataOutViewMap.values()) {
			if(view.getParent() == this) {
				Box box = view.getOverallBox();
				view.placeBoxAndChilds(xDataOutStart, yDataOutStart);
				yDataOutStart += box.getHeight();
			}
		}

		for(HSView view : controlOutViewMap.values()) {
			if(view.getParent() == this) {
				Box box = view.getOverallBox();
				view.placeBoxAndChilds(xControlOutStart, yControlOutStart);
				xControlOutStart += box.getWidth();
			}
		}

		double thisx = startx + xDataIn + Math.max((xControlOut / 2.0) - (COMP_SIZE / 2), COMP_HDIST);// - (COMP_SIZE/2);
		double thisy = starty + Math.max((Math.max(yDataIn, yDataOut) / 2.0) - (COMP_SIZE / 2), COMP_VDIST);//-(COMP_SIZE/2);

		this.place(thisx, thisy);
	}

	private void fillMaps(Map<HSChannel, List<HSView>> chanMap) {
		for(Entry<HSChannel, Integer> entry : dataInChanMap.entrySet()) {
			List<HSView> views = chanMap.get(entry.getKey());
			for(HSView view : views) {
				if(!view.equals(this)) {
					dataInViewMap.put(entry.getValue(), view);
					break;
				}
			}

		}
		for(Entry<HSChannel, Integer> entry : dataOutChanMap.entrySet()) {
			List<HSView> views = chanMap.get(entry.getKey());
			for(HSView view : views) {
				if(!view.equals(this)) {
					dataOutViewMap.put(entry.getValue(), view);
					break;
				}
			}

		}
		for(Entry<HSChannel, Integer> entry : controlInChanMap.entrySet()) {
			List<HSView> views = chanMap.get(entry.getKey());
			for(HSView view : views) {
				if(!view.equals(this)) {
					controlInViewMap.put(entry.getValue(), view);
					break;
				}
			}

		}
		for(Entry<HSChannel, Integer> entry : controlOutChanMap.entrySet()) {
			List<HSView> views = chanMap.get(entry.getKey());
			for(HSView view : views) {
				if(!view.equals(this)) {
					controlOutViewMap.put(entry.getValue(), view);
					break;
				}
			}
		}
	}

	public HSView getParent() {
		return parent;
	}

	public void setParent(HSView parent) {
		this.parent = parent;
	}

	public Box getOverallBox() {
		return overallBox;
	}

	public void setColor(String backcolor, String fontcolor) {
		String style = cell.getStyle();
		StringBuilder newStyle = new StringBuilder();
		for(String str : style.split(";")) {
			if(str.startsWith("fillColor")) {
				newStyle.append("fillColor=").append(backcolor).append(";");
			} else if(str.startsWith("fontColor")) {
				newStyle.append("fontColor=").append(fontcolor).append(";");
			} else {
				newStyle.append(str).append(";");
			}
		}
		String newStyleStr = newStyle.substring(0, newStyle.length() - 1);
		graph.getModel().setStyle(cell, newStyleStr);
	}
	
	public void openSubBreezeWindow() {
		if(inst instanceof BreezeNetlistInst) {
			BreezeNetlistInst inst2 = (BreezeNetlistInst)inst;
			GuiMain gmain = new GuiMain(inst2.getInstantiatedNetlist(), 1);
			gmain.show();
		}
	}
}
