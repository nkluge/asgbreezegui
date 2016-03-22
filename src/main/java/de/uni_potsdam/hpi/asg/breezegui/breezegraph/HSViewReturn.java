package de.uni_potsdam.hpi.asg.breezegui.breezegraph;

/*
 * Copyright (C) 2012 - 2015 Norman Kluge
 * 
 * This file is part of ASGbreezeGui.
 * 
 * ASGbreezeGui is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * ASGbreezeGui is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with ASGbreezeGui.  If not, see <http://www.gnu.org/licenses/>.
 */

import java.util.HashMap;
import java.util.Map;


import com.mxgraph.model.mxCell;

import de.uni_potsdam.hpi.asg.common.breeze.model.HSChannel;

public class HSViewReturn {
	private mxCell cell;
	private Map<HSChannel, mxCell> activeportmap;
	private Map<HSChannel, mxCell> passiveportmap;
	
	public HSViewReturn(mxCell cell) {
		this.cell = cell;
		this.activeportmap = new HashMap<>();
		this.passiveportmap = new HashMap<>();
	}
	public void addActivePort(HSChannel chan, mxCell port) {
		this.activeportmap.put(chan, port);
	}
	public void addPassivePort(HSChannel chan, mxCell port) {
		this.passiveportmap.put(chan, port);
	}
	
	public mxCell getCell() {
		return cell;
	}
	public Map<HSChannel, mxCell> getActiveportmap() {
		return activeportmap;
	}
	public Map<HSChannel, mxCell> getPassiveportmap() {
		return passiveportmap;
	}
}
