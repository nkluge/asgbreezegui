package de.uni_potsdam.hpi.asg.breezegui;

/*
 * Copyright (C) 2015 Norman Kluge
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

import java.io.File;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BreezeGuiMainTest {

	@BeforeClass
	public static void setup() {
		String oldbase = System.getProperty("basedir");
		if(oldbase == null) {
			//JUnit Testrunner
			oldbase = System.getProperty("user.dir");
			System.setProperty("user.dir", oldbase + "/target/test-runs");
		}
	}
	
	@Test
	public void testGui() {
		
		String[] args = {
			"-o", "3",
			"-mode", "gui",
			"../../src/test/resources/gcd.breeze"
		};
		
		Assert.assertEquals("BreezeGui failed", 0, BreezeGuiMain.main2(args));
	}
	
	@Test
	public void testPng() {
		
		String pngfile = System.getProperty("user.dir") + "/test.png";
		String[] args = {
			"-o", "0",
			"-mode", "png",
			"-out", pngfile,
			"../../src/test/resources/gcd.breeze"
		};
		
		Assert.assertEquals("BreezeGui failed", 0, BreezeGuiMain.main2(args));
		File f = new File(pngfile);
		Assert.assertEquals("Pngfile not present", true, f.exists());
	}
}
