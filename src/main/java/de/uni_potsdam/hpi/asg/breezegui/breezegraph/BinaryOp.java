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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.uni_potsdam.hpi.asg.common.breeze.model.HSComponentType;
import de.uni_potsdam.hpi.asg.common.breeze.model.xml.Parameter.ParameterType;

public class BinaryOp {
	private static final Logger logger = LogManager.getLogger();
	
	/*
	(("Add") "+")
	(("Subtract") "-")
	(("Equals") "=")
	(("NotEquals") "/=")
	(("LessThan") "<")
	(("GreaterThan") ">")
	(("LessOrEquals") "<=")
	(("GreaterOrEquals") ">=")
	(("And") "and")
	(("Or") "or")
	(("Xor") "xor")
	*/
	
	private enum OpType {
		add, sub, equals, notequals, lessthan, greaterthan, lessorequals, greaterorequals, and,	or,	xor, invert, dontknow
	}
	
	private OpType op;
	private String constant;
	
	public BinaryOp(HSComponentType type) {
		String str = ((String)type.getParamValue(ParameterType.operator)).replace("\"", "");
		if(str.equals("Add")) {
			op = OpType.add;
		} else if(str.equals("Subtract")) {
			op = OpType.sub;
		} else if(str.equals("Equals")) {
			op = OpType.equals;
		} else if(str.equals("NotEquals")) {
			op = OpType.notequals;
		} else if(str.equals("LessThan")) {
			op = OpType.lessthan;
		} else if(str.equals("GreaterThan")) {
			op = OpType.greaterthan;
		} else if(str.equals("LessOrEquals")) {
			op = OpType.lessorequals;
		} else if(str.equals("GreaterOrEquals")) {
			op = OpType.greaterorequals;
		} else if(str.equals("And")) {
			op = OpType.and;
		} else if(str.equals("Or")) {
			op = OpType.or;
		} else if(str.equals("Xor")) {
			op = OpType.xor;
		} else if(str.equals("Invert")) {
			op = OpType.invert;
		} else {
			op = OpType.dontknow;
			logger.warn("Unknown OpType: " + str);
		}
		
		if(type.getParamValue(ParameterType.inputB_value) != null) {
			constant = " " + type.getParamValue(ParameterType.inputB_value);
		} else {
			constant = "";
		}
	}
	
	@Override
	public String toString() {
		switch(op) {
			case add:
				return "+" + constant;
			case sub:
				return "-" + constant;
			case equals:
				return "==" + constant;
			case notequals:
				return "!=" + constant;
			case lessthan:
				return "<" + constant;
			case greaterthan:
				return ">" + constant;
			case lessorequals:
				return "<=" + constant;
			case greaterorequals:
				return ">=" + constant;
			case and:
				return "and" + constant;
			case or:
				return "or" + constant;
			case xor:
				return "xor" + constant;
			case invert:
				return "!" + constant;
			case dontknow:
			default:
				return "?";
		}
	}
}
