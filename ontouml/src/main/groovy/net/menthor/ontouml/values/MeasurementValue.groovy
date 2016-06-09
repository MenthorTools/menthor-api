package net.menthor.ontouml.values

/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2016  Menthor Consulting in Information Technology
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies
 * or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

/**
 * @author John Guerson
 */
enum MeasurementValue {

	UNSET("Unset"),
	INTEGER("Integer"),
	REAL("Real"),
	DECIMAL("Decimal"),
	STRING("String");

	final String name;
	final String lowerName;
	static final Map map

    MeasurementValue(String name) {
		this.name = name;
		this.lowerName = name.toLowerCase()
	}

	@Override
	String toString() {
		return name;
	}

	static getEnum(name) {
		map[name.toLowerCase()]
	}

	static {
		map = [:] as TreeMap
		values().each{ e ->
			map.put(e.lowerName, e)
		}
	}
}