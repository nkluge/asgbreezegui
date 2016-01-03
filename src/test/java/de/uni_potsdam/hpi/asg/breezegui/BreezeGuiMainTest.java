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
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class BreezeGuiMainTest {

	private static String newbase;
	
	@BeforeClass
	public static void setup() {
		String oldbase = System.getProperty("basedir");
		if(oldbase == null) {
			//JUnit Testrunner
			oldbase = System.getProperty("user.dir");
			System.setProperty("user.dir", oldbase + "/target/test-runs");
		}
		
		String oldpath = System.getenv("PATH");
		newbase = oldbase + "/target/asgbreezegui-1.0.0-SNAPSHOT-unix/asgbreezegui-1.0.0-SNAPSHOT";
		System.setProperty("basedir", newbase);
		Map<String, String> env = new HashMap<String, String>(System.getenv());
		env.put("PATH", oldpath + ":" + newbase + "/tools/balsa/bin");
		env.put("BALSAHOME", newbase + "/tools/balsa");
		setEnv(env);
	}
	
	@Test
	public void testGui() {
		
		String[] args = {
			"-cfg", newbase + "/config/breezeguiconfig.xml",
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
	
	//Source: http://stackoverflow.com/questions/318239/how-do-i-set-environment-variables-from-java
	@SuppressWarnings("all")
	protected static void setEnv(Map<String, String> newenv) {
		try {
			Class<?> processEnvironmentClass = Class.forName("java.lang.ProcessEnvironment");
			Field theEnvironmentField = processEnvironmentClass.getDeclaredField("theEnvironment");
			theEnvironmentField.setAccessible(true);
			Map<String, String> env = (Map<String, String>) theEnvironmentField.get(null);
			env.putAll(newenv);
			Field theCaseInsensitiveEnvironmentField = processEnvironmentClass.getDeclaredField("theCaseInsensitiveEnvironment");
			theCaseInsensitiveEnvironmentField.setAccessible(true);
			Map<String, String> cienv = (Map<String, String>) theCaseInsensitiveEnvironmentField.get(null);
			cienv.putAll(newenv);
		} catch (NoSuchFieldException e) {
			try {
				Class[] classes = Collections.class.getDeclaredClasses();
				Map<String, String> env = System.getenv();
				for (Class cl : classes) {
					if ("java.util.Collections$UnmodifiableMap".equals(cl.getName())) {
						Field field = cl.getDeclaredField("m");
						field.setAccessible(true);
						Object obj = field.get(env);
						Map<String, String> map = (Map<String, String>) obj;
						map.clear();
						map.putAll(newenv);
					}
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}
}
