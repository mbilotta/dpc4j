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

import org.altervista.mbilotta.julia.Decimal;
import org.altervista.mbilotta.julia.math.Complex;
import org.altervista.mbilotta.julia.math.Real;


public class DoubleReal extends AbstractDoubleComplex implements Real {
	
	private final double v;

	public DoubleReal(double v) {
		this.v = v;
	}

	public DoubleReal(String s) {
		this(Double.parseDouble(s));
	}

	public DoubleReal(Decimal d) {
		this(d.toNormalizedString());
	}

	@Override
	protected final double getReal() {
		return v;
	}

	@Override
	protected final double getImag() {
		return 0.0;
	}

	@Override
	public Real plus(int i) {
		return plus((double) i);
	}

	@Override
	public Real plus(Real r) {
		return plus(re(r));
	}

	private Real plus(double v) {
		return real(getReal() + v);
	}

	@Override
	public Real minus(int i) {
		return minus((double) i);
	}

	@Override
	public Real minus(Real r) {
		return minus(re(r));
	}

	private Real minus(double v) {
		return real(getReal() - v);
	}

	@Override
	public Real times(int i) {
		return times((double) i);
	}

	@Override
	public Real times(Real r) {
		return times(re(r));
	}

	private Real times(double v) {
		return real(getReal() * v);
	}

	@Override
	public Real dividedBy(int i) {
		return dividedBy((double) i);
	}

	@Override
	public Real dividedBy(Real r) {
		return dividedBy(re(r));
	}

	private Real dividedBy(double v) {
		return real(getReal() / v);
	}

	@Override
	public Real abs() {
		return real(Math.abs(getReal()));
	}

	@Override
	public Real absSquared() {
		return square();
	}

	@Override
	public Real reciprocal() {
		return real(1 / getReal());
	}

	@Override
	public Real square() {
		return times(getReal());
	}

	@Override
	public Real toThe(int n) {
		return real(nthPow(getReal(), n));
	}

	@Override
	public Complex pow(Complex z) {
		double abs1=Math.abs(getReal()),
				abs=Math.pow(abs1, re(z)),
				arg=(im(z) * Math.log(abs1));
		return complex(
				abs * Math.cos(arg),
				abs * Math.sin(arg));
	}

	@Override
	public Real negate() {
		return real(-getReal());
	}

	@Override
	public Real conj() {
		return this;
	}

	@Override
	public Real exp() {
		return real(Math.exp(getReal()));
	}

	@Override
	public Real sin() {
		return real(Math.sin(getReal()));
	}

	@Override
	public Real cos() {
		return real(Math.cos(getReal()));
	}

	@Override
	public Real tan() {
		return real(Math.tan(getReal()));
	}

	@Override
	public Real atan() {
		return real(Math.atan(getReal()));
	}

	public static double nthPow(double x, int n) {
		if (n == 0) {
			return 1;
		}

		int exponent = n;
		double base = x;

		if (exponent < 0) {
			base = 1 / base;
			exponent = -exponent;
			if (exponent < 0) {
				return base > 1 || base < -1 ? Double.POSITIVE_INFINITY : 0;
			}
		}

		switch (exponent) {
		case 2: return base * base;
		case 3: return base * base * base;
		case 4: return base * base * base * base;
		}

		while ((exponent & 1) != 1) {
			base *= base;
			exponent >>= 1;
		}
		
		if (exponent == 1) {
			return base;
		}

		double result = base;
		base *= base;
		exponent >>= 1;

		while (true) {
			if ((exponent & 1) == 1)  {
				result *= base;
				if (exponent == 1) break;
			}
			
			base *= base;
			exponent >>= 1;
		}
		
		return result;
	}

	@Override
	public String toString() {
		return Double.toString(getReal());
	}
}
