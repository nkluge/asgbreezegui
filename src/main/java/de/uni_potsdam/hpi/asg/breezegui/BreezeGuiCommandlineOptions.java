package de.uni_potsdam.hpi.asg.breezegui;

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

import java.io.File;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.Option;

import de.uni_potsdam.hpi.asg.common.io.CommandlineOptions;

public class BreezeGuiCommandlineOptions extends CommandlineOptions {
	
	public boolean parseCmdLine(String[] args) {
		String version = BreezeGuiMain.class.getPackage().getImplementationVersion();
		String versionstr = "ASGBreezeGui " + (version==null ? "Testmode" : "v" + version);
		return super.parseCmdLine(args, versionstr + "\nUsage: ASGBreezeGui [options] <breezefile>\nOptions:");
	}
	
	@Option(name="-o", metaVar="<level>", usage="Outputlevel: 0:nothing\n1:errors\n[2:+warnings]\n3:+info")
	private int outputlevel = 2;
	@Option(name="-log", metaVar="<logfile>", usage="Define output Logfile, default is breezegui.log")
	private File logfile = new File(System.getProperty("user.dir") + File.separator + "breezegui.log");
	@Option(name="-debug")
	private boolean debug = false;
	
	@Option(name="-mode", metaVar="<mode>", usage="Operationmode: [gui]:Show GUI\npng:Export png\nsvg:Export svg")
	private String mode = "gui"; 
	@Option(name="-out", metaVar="<file>", usage="Outfile for png or svg operation mode. Default is out")
	private File outfile = new File(System.getProperty("user.dir") + File.separator + "out");
	
	@Argument(metaVar="Breezefile", required=true)
	private File breezefile;
	
	public int getOutputlevel() {
		return outputlevel;
	}
	public File getLogfile() {
		return logfile;
	}
	public boolean isDebug() {
		return debug;
	}
	public File getBreezefile() {
		return breezefile;
	}
	public String getMode() {
		return mode;
	}
	public File getOutfile() {
		return outfile;
	}
}
