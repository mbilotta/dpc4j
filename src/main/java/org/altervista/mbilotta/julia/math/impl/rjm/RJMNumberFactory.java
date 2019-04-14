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

import org.altervista.mbilotta.julia.Author;
import org.altervista.mbilotta.julia.Decimal;
import org.altervista.mbilotta.julia.NumberFactory;
import org.altervista.mbilotta.julia.math.Real;
import org.altervista.mbilotta.rjm.BigDecimalMath;


@Author(name = "Maurizio Bilotta", contact = "mailto:maurizeio@gmail.com")
public class RJMNumberFactory implements NumberFactory {
	
	private MathContext mc;
	private RJMReal zero;
	private RJMReal one;
	private RJMReal pi;
	private RJMReal e;
	private RJMComplex i;

	public RJMNumberFactory() {
		mc = new MathContext(16);
	}

	public final void setPrecision(int precision) {
		mc = new MathContext(precision);
	}

	public final int getPrecision() {
		return mc.getPrecision();
	}

	@Override
	public RJMReal zero() {
		if (zero == null) {
			zero = new RJMReal(mc, BigDecimal.ZERO);
		}
		return zero;
	}

	@Override
	public RJMReal one() {
		if (one == null) {
			one = new RJMReal(mc, BigDecimal.ONE);
		}
		return one;
	}

	@Override
	public RJMReal pi() {
		if (pi == null) {
			pi = new RJMReal(mc, BigDecimalMath.pi(mc));
		}
		return pi;
	}

	@Override
	public RJMReal e() {
		if (e == null) {
			e = new RJMReal(mc, BigDecimalMath.exp(mc));
		}
		return e;
	}

	@Override
	public RJMComplex i() {
		if (i == null) {
			i = new RJMComplex(mc, BigDecimal.ZERO, BigDecimal.ONE);
		}
		return i;
	}

	@Override
	public RJMReal valueOf(int i) {
		return new RJMReal(mc, i);
	}

	@Override
	public RJMReal valueOf(String s) {
		return new RJMReal(mc, s);
	}

	@Override
	public RJMReal valueOf(Decimal d) {
		return new RJMReal(mc, d);
	}

	@Override
	public RJMComplex valueOf(int real, int imag) {
		return new RJMComplex(mc, real, imag);
	}

	@Override
	public RJMComplex valueOf(Decimal real, Decimal imag) {
		return new RJMComplex(mc, real, imag);
	}

	@Override
	public RJMComplex valueOf(Real real, Real imag) {
		return new RJMComplex(mc, real, imag);
	}

	@Override
	public String toString() {
		return getClass().getName() + "[precision=" + getPrecision() + ']';
	}
}
