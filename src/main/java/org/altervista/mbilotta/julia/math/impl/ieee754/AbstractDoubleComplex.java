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

import java.util.Objects;

import org.altervista.mbilotta.julia.Decimal;
import org.altervista.mbilotta.julia.math.Complex;
import org.altervista.mbilotta.julia.math.Real;
import org.altervista.mbilotta.julia.math.SpecialValueException;


public abstract class AbstractDoubleComplex implements Complex {

	protected abstract double getReal();
	protected abstract double getImag();
	
	protected Real real(double re) {
		return new DoubleReal(re);
	}

	protected Complex complex(double re, double im) {
		return new DoubleComplex(re, im);
	}

	protected final double re(Complex c) {
		return ((AbstractDoubleComplex) c).getReal();
	}
	
	protected final double im(Complex c) {
		return ((AbstractDoubleComplex) c).getImag();
	}

	@Override
	public Complex plus(int i) {
		return plus((double) i);
	}

	@Override
	public Complex plus(Real r) {
		return plus(re(r));
	}
	
	private Complex plus(double v) {
		return complex(getReal() + v, getImag());
	}

	@Override
	public Complex plus(Complex c) {
		return complex(getReal() + re(c), getImag() + im(c));
	}

	@Override
	public Complex minus(int i) {
		return minus((double) i);
	}

	@Override
	public Complex minus(Real r) {
		return minus(re(r));
	}

	private Complex minus(double v) {
		return complex(getReal() - v, getImag());
	}

	@Override
	public Complex minus(Complex c) {
		return complex(getReal() - re(c), getImag() - im(c));
	}

	@Override
	public Complex times(int i) {
		return times((double) i);
	}

	@Override
	public Complex times(Real r) {
		return times(re(r));
	}

	private Complex times(double v) {
		return complex(getReal() * v, getImag() * v);
	}

	@Override
	public Complex times(Complex c) {
		double re1=getReal(), im1=getImag(), re2=re(c), im2=im(c);
		return complex(
				re1 * re2 - im1 * im2,
				re2 * im1 + re1 * im2);
	}

	@Override
	public Complex dividedBy(int i) {
		return dividedBy((double) i);
	}

	@Override
	public Complex dividedBy(Real r) {
		return dividedBy(re(r));
	}

	private Complex dividedBy(double v) {
		return complex(getReal() / v, getImag() / v);
	}

	@Override
	public Complex dividedBy(Complex c) {
		double re1=getReal(), im1=getImag(),
				re2=re(c), im2=im(c),
				norm2=(re2 * re2 + im2 * im2);
		return complex(
				(re1 * re2 + im1 * im2) / norm2,
				(re2 * im1 - re1 * im2) / norm2);
	}

	@Override
	public Complex toThe(int n) {
		if (n == 0) {
			return real(0);
		}

		int exponent = n;
		Complex base = this;

		boolean isMinValue = false;
		if (exponent < 0) {
			base = base.reciprocal();
			exponent = -exponent;
			if (exponent < 0) {
				exponent = Integer.MAX_VALUE;
				isMinValue = true;
			}
		}

		switch (exponent) {
		case 2: return base.square();
		case 3: return base.square().times(base);
		case 4: return base.square().square();
		}

		while ((exponent & 1) != 1) {
			base = base.square();
			exponent >>= 1;
		}
		
		if (exponent == 1) {
			return base;
		}

		Complex result = base;
		base = base.square();
		exponent >>= 1;

		while (true) {
			if ((exponent & 1) == 1)  {
				result = result.times(base);
				if (exponent == 1) break;
			}
			
			base = base.square();
			exponent >>= 1;
		}

		if (isMinValue)
			result = result.dividedBy(this);
		
		return result;
	}

	@Override
	public Complex reciprocal() {
		double re=getReal(), im=getImag(),
				norm=(re * re + im * im);
		return complex(
				re / norm,
				-im / norm);
	}

	@Override
	public Real abs() {
		return real(Math.hypot(getReal(), getImag()));
	}

	@Override
	public Real absSquared() {
		double re=getReal(), im=getImag();
		return real(re * re + im * im);
	}

	@Override
	public Real arg() {
		return real(Math.atan2(getImag(), getReal()));
	}

	@Override
	public Real re() {
		return real(getReal());
	}

	@Override
	public Real im() {
		return real(getImag());
	}

	public Complex i() {
		return complex(0.0, getReal());
	}

	public Real signum() {
		return real(Math.signum(getReal()));
	}

	@Override
	public Complex exp() {
		double im1=getImag(), abs=Math.exp(getReal());
		return complex(
				abs * Math.cos(im1),
				abs * Math.sin(im1));
	}

	@Override
	public Complex ln() {
		double re=getReal(), im=getImag();
		return complex(
				Math.log(Math.hypot(re, im)),
				Math.atan2(im, re));
	}

	public Real rLn() {
		return real(Math.log(getReal()));
	}

	@Override
	public Complex sin() {
		double re=getReal(), exp=Math.exp(getImag()), expm1=(1 / exp);
		return complex(
				Math.sin(re) * (exp + expm1) / 2,
				Math.cos(re) * (exp - expm1) / 2);
	}

	@Override
	public Complex cos() {
		double re=getReal(), exp=Math.exp(getImag()), expm1=(1 / exp);
		return complex(
				Math.cos(re) * (exp + expm1) / 2,
				-Math.sin(re) * (exp - expm1) / 2);
	}

	@Override
	public Complex tan() {
		double re=getReal(),
				sin=Math.sin(re),
				cos=Math.cos(re),
				exp=Math.exp(getImag()),
				expm1=(1 / exp),
				sinh=((exp - expm1) / 2),
				cosh=((exp + expm1) / 2);
		return complex(
				sin * cosh,
				cos * sinh).dividedBy(complex(
						cos * cosh,
						-sin * sinh));
	}

	@Override
	public Complex atan() {
		Complex i = complex(0, 1);
		return i.dividedBy(2).times(i.plus(this).dividedBy(i.minus(this)).ln());
	}

	@Override
	public Complex conj() {
		return complex(getReal(), -getImag());
	}

	@Override
	public Complex negate() {
		return complex(-getReal(), -getImag());
	}

	@Override
	public Complex square() {
		return times(this);
	}

	@Override
	public Complex sqrt() {
		double re1=getReal(), im1=getImag(),
				abs1=Math.hypot(re1, im1),
				im=Math.sqrt((abs1 - re1) / 2);
		return complex(
				Math.sqrt((abs1 + re1) / 2),
				Math.signum(im1) * im);
	}

	@Override
	public Complex nthRoot(int n) {
		if (n < 1)
			throw new IllegalArgumentException("" + n);
		if (n == 1)
			return this;
		if (n == 2)
			return sqrt();

		double re1=getReal(), im1=getImag(),
				abs=Math.pow(Math.hypot(re1, im1), 1.0 / n),
				arg=Math.atan2(im1, re1) / n;
		return complex(
				abs * Math.cos(arg),
				abs * Math.sin(arg));
	}

	@Override
	public Complex pow(Real x) {
		double re1=getReal(), im1=getImag(),
				re2=re(x),
				abs=Math.pow(Math.hypot(re1, im1), re2),
				arg=(re2 * Math.atan2(im1, re1));
		return complex(
				abs * Math.cos(arg),
				abs * Math.sin(arg));
	}

	@Override
	public Complex pow(Complex z) {
		double re1=getReal(), im1=getImag(),
				re2=re(z), im2=im(z),
				abs1=Math.hypot(re1, im1),
				arg1=Math.atan2(im1, re1),
				abs=(Math.pow(abs1, re2) * Math.exp(-im2 * arg1)),
				arg=(im2 * Math.log(abs1) + re2 * arg1);
		return complex(
				abs * Math.cos(arg),
				abs * Math.sin(arg));
	}

	public Real rSqrt() {
		return real(Math.sqrt(getReal()));
	}

	public Real rNthRoot(int n) {
		return real(Math.pow(getReal(), 1.0 / n));
	}

	public Real rPow(Real x) {
		return real(Math.pow(getReal(), re(x)));
	}

	public Real max(Real r) {
		double re=getReal();
		return Double.compare(Math.max(re, re(r)), re) == 0 ? (Real) this : r;
	}

	public Real min(Real r) {
		double re=getReal();
		return Double.compare(Math.min(re, re(r)), re) == 0 ? (Real) this : r;
	}

	public boolean lt(int i) {
		return getReal() < i;
	}

	public boolean lte(int i) {
		return getReal() <= i;
	}

	public boolean gt(int i) {
		return getReal() > i;
	}

	public boolean gte(int i) {
		return getReal() >= i;
	}

	public boolean eq(int i) {
		return getReal() == i;
	}

	public boolean lt(Real r) {
		return getReal() < re(r);
	}

	public boolean lte(Real r) {
		return getReal() <= re(r);
	}

	public boolean gt(Real r) {
		return getReal() > re(r);
	}

	public boolean gte(Real r) {
		return getReal() >= re(r);
	}

	public boolean eq(Real r) {
		return getReal() == re(r);
	}

	public int compareTo(Real r) {
		double re1=getReal(), re2=re(r);
		if (re1 < re2)
			return -1;
		if (re1 > re2)
			return 1;
		if (re1 == re2)
			return 0;

		throw new SpecialValueException(Double.isNaN(re1) ? re1 : re2);
	}

	@Override
	public boolean eq(Complex c) {
		return getReal() == re(c) && getImag() == im(c);
	}

	public Decimal decimalValue() {
		double re=getReal();
		try {
			return new Decimal(Double.toString(re));
		} catch (NumberFormatException e) {
			throw new SpecialValueException(re);
		}
	}

	public double doubleValue() {
		return getReal();
	}

	public float floatValue() {
		return (float) doubleValue();
	}

	public int intValue() {
		return (int) getReal();
	}

	@Override
	public int hashCode() {
		return Objects.hash(getReal(), getImag());
	}

	@Override
	public boolean equals(Object o) {
		if (o == this)
			return true;
		
		if (o instanceof AbstractDoubleComplex) {
			AbstractDoubleComplex c = (AbstractDoubleComplex) o;
			return Double.compare(getReal(), c.getReal()) == 0 &&
					Double.compare(getImag(), c.getImag()) == 0;
		}
		return false;
	}

	@Override
	public String toString() {
		return "(" + getReal() + ", " + getImag() + ")";
	}
}
