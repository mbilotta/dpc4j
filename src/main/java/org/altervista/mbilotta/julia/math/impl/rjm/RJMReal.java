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

package org.altervista.mbilotta.julia.math.impl.rjm;

import java.math.BigDecimal;
import java.math.MathContext;

import org.altervista.mbilotta.julia.Decimal;
import org.altervista.mbilotta.julia.math.Complex;
import org.altervista.mbilotta.julia.math.Real;


public class RJMReal extends AbstractRJMComplex implements Real {

	protected RJMReal(MathContext mc, BigDecimal v) {
		super(mc, v);
	}

	public RJMReal(MathContext mc, int i) {
		super(mc, i);
	}

	public RJMReal(MathContext mc, String s) {
		super(mc, s);
	}

	public RJMReal(MathContext mc, Decimal d) {
		super(mc, d);
	}

	@Override
	protected final BigDecimal getImag() {
		return super.getImag();
	}

	@Override
	public Real plus(int i) {
		return plus(re(i));
	}

	@Override
	public Real plus(Real r) {
		return plus(re(r));
	}

	private Real plus(BigDecimal v) {
		return real(add(getReal(), v));
	}

	@Override
	public Real minus(int i) {
		return minus(re(i));
	}

	@Override
	public Real minus(Real r) {
		return minus(re(r));
	}

	private Real minus(BigDecimal v) {
		return real(subtract(getReal(), v));
	}

	@Override
	public Real times(int i) {
		return times(re(i));
	}

	@Override
	public Real times(Real r) {
		return times(re(r));
	}

	private Real times(BigDecimal v) {
		return real(multiply(getReal(), v));
	}

	@Override
	public Real dividedBy(int i) {
		return dividedBy(re(i));
	}

	@Override
	public Real dividedBy(Real r) {
		return dividedBy(re(r));
	}

	private Real dividedBy(BigDecimal v) {
		return real(divide(getReal(), v));
	}

	@Override
	public Real abs() {
		return real(abs(getReal()));
	}

	@Override
	public Real absSquared() {
		return square();
	}

	@Override
	public Real arg() {
		return real(getReal().signum() >= 0 ? BigDecimal.ZERO : pi());
	}

	@Override
	public Real reciprocal() {
		return real(reciprocal(getReal()));
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
		BigDecimal abs1, abs, arg;
		abs=pow(abs1=abs(getReal()), re(z));
		arg=multiply(im(z), ln(abs1));
		return complex(
				multiply(abs, cos(arg)),
				multiply(abs, sin(arg)));
	}

	@Override
	public Real negate() {
		return real(negate(getReal()));
	}

	@Override
	public Real conj() {
		return this;
	}

	@Override
	public Real exp() {
		return real(exp(getReal()));
	}

	@Override
	public Real sin() {
		return real(sin(getReal()));
	}

	@Override
	public Real cos() {
		return real(cos(getReal()));
	}

	@Override
	public Real tan() {
		return real(tan(getReal()));
	}

	@Override
	public Real atan() {
		return real(atan(getReal()));
	}

	@Override
	public String toString() {
		return getReal().toString();
	}
}
