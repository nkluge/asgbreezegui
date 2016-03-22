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
		add, sub, equals, notEquals, lessThan, greaterThan, lessOrEquals, greaterOrEquals, and,	or, xor, invert, negate, unknown
	}
	
	private OpType op;
	private String constant;
	
	public BinaryOp(HSComponentType type) {
		String str = ((String)type.getParamValue(ParameterType.operator)).replace("\"", "");
		switch (str) {
			case "Add":				op = OpType.add; break;
			case "Subtract": 		op = OpType.sub; break;
			case "Equals":			op = OpType.equals; break;
			case "NotEquals":		op = OpType.notEquals; break;
			case "LessThan":		op = OpType.lessThan; break;
			case "GreaterThan":		op = OpType.greaterThan; break;
			case "LessOrEquals":	op = OpType.lessOrEquals; break;
			case "GreaterOrEquals":	op = OpType.greaterOrEquals; break;
			case "And":				op = OpType.and; break;
			case "Or":				op = OpType.or; break;
			case "Xor":				op = OpType.xor; break;
			case "Invert":			op = OpType.invert; break;
			case "Negate":			op = OpType.negate; break;
			default: op = OpType.unknown;
				logger.warn("Unknown OpType: " + str);
				break;
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
			case notEquals:
				return "!=" + constant;
			case lessThan:
				return "<" + constant;
			case greaterThan:
				return ">" + constant;
			case lessOrEquals:
				return "<=" + constant;
			case greaterOrEquals:
				return ">=" + constant;
			case and:
				return "and" + constant;
			case or:
				return "or" + constant;
			case xor:
				return "xor" + constant;
			case invert:
				return "!";
			case negate:
				return "-";
			case unknown:
			default:
				return "?";
		}
	}
}
