package de.uni_potsdam.hpi.asg.breezegui.io;

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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_potsdam.hpi.asg.breezegui.BreezeGuiMain;
import de.uni_potsdam.hpi.asg.common.io.Invoker;
import de.uni_potsdam.hpi.asg.common.io.ProcessReturn;

public class BreezeGuiInvoker extends Invoker {
	private final static Logger logger = LogManager.getLogger();
	
	private static BreezeGuiInvoker instance;
	private BreezeGuiInvoker() {}
	
	public static BreezeGuiInvoker getInstance() {
		if(BreezeGuiInvoker.instance == null) {
			BreezeGuiInvoker.instance = new BreezeGuiInvoker();
			if(Invoker.instance == null) {
				Invoker.instance = BreezeGuiInvoker.instance;
			} else {
				logger.warn("Logger instance already set");
			}
		}
		return BreezeGuiInvoker.instance;
	}	
	
	public boolean invokeBalsaC(String infile) {
		String[] cmd = convertCmd(BreezeGuiMain.config.toolconfig.balsaccmd);
		if(cmd == null) {
			logger.error("Could not read balsac cmd String");
		}
		String[] params = {"-o", ".", infile};
		ProcessReturn ret = invoke(cmd, params);
		return errorHandling(ret);
	}
}
