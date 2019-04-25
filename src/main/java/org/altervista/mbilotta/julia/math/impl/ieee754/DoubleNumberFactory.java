/*
 * Copyright (C) 2015 Maurizio Bilotta.
 * 
 * This file is part of the Default Plugin Collection for Julia ("DPC4J").
 * See <http://mbilotta.altervista.org/>.
 * 
 * DPC4J is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * DPC4J is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with DPC4J. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.altervista.mbilotta.julia.math.impl.ieee754;

import org.altervista.mbilotta.julia.Author;
import org.altervista.mbilotta.julia.Decimal;
import org.altervista.mbilotta.julia.NumberFactory;
import org.altervista.mbilotta.julia.math.Real;


@Author(name = "Maurizio Bilotta", contact = "mailto:maurizeio@yahoo.it")
public class DoubleNumberFactory implements NumberFactory {

	public static final DoubleReal ZERO = new DoubleReal(0);
	public static final DoubleReal ONE = new DoubleReal(1);
	public static final DoubleReal PI = new DoubleReal(Math.PI);
	public static final DoubleReal E = new DoubleReal(Math.E);
	public static final DoubleComplex I = new DoubleComplex(0, 1);

	public DoubleNumberFactory() {
	}

	@Override
	public DoubleReal zero() {
		return ZERO;
	}

	@Override
	public DoubleReal one() {
		return ONE;
	}

	@Override
	public DoubleReal pi() {
		return PI;
	}

	@Override
	public DoubleReal e() {
		return E;
	}

	@Override
	public DoubleComplex i() {
		return I;
	}

	@Override
	public DoubleReal valueOf(int i) {
		return new DoubleReal(i);
	}

	@Override
	public DoubleReal valueOf(String s) {
		return new DoubleReal(s);
	}

	@Override
	public DoubleReal valueOf(Decimal d) {
		return new DoubleReal(d);
	}

	@Override
	public DoubleComplex valueOf(int real, int imag) {
		return new DoubleComplex(real, imag);
	}

	@Override
	public DoubleComplex valueOf(Decimal real, Decimal imag) {
		return new DoubleComplex(real, imag);
	}

	@Override
	public DoubleComplex valueOf(Real real, Real imag) {
		return new DoubleComplex(real, imag);
	}

	public String toString() {
		return getClass().getName() + "[]";
	}
}
