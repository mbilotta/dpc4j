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
import org.altervista.mbilotta.julia.math.Real;
import org.altervista.mbilotta.julia.math.impl.AbstractBigDecimalComplex;
import org.altervista.mbilotta.rjm.BigDecimalMath;


public abstract class AbstractRJMComplex extends AbstractBigDecimalComplex {
	
	protected AbstractRJMComplex(MathContext mc, BigDecimal v) {
		super(mc, v);
	}

	public AbstractRJMComplex(MathContext mc, int i) {
		super(mc, i);
	}

	public AbstractRJMComplex(MathContext mc, String s) {
		super(mc, s);
	}

	public AbstractRJMComplex(MathContext mc, Decimal d) {
		super(mc, d);
	}

	public AbstractRJMComplex(MathContext mc, Real r) {
		super(mc, r);
	}

	@Override
	protected final Real real(BigDecimal v) {
		return new RJMReal(getMathContext(), v);
	}

	@Override
	protected final AbstractRJMComplex complex(BigDecimal re, BigDecimal im) {
		return new RJMComplex(getMathContext(), re, im);
	}

	@Override
	protected BigDecimal hypot(BigDecimal x, BigDecimal y) {
		if (x.signum() == 0)
			return y.abs();
		if (y.signum() == 0)
			return x.abs();

		return BigDecimalMath.hypot(scalePrec(x), scalePrec(y));
	}

	@Override
	protected BigDecimal ln(BigDecimal x) {
		return BigDecimalMath.log(scalePrec(x));
	}

	@Override
	protected BigDecimal exp(BigDecimal x) {
		return BigDecimalMath.exp(scalePrec(x));
	}

	@Override
	protected BigDecimal sin(BigDecimal x) {
		return BigDecimalMath.sin(scalePrec(x));
	}

	@Override
	protected BigDecimal cos(BigDecimal x) {
		return BigDecimalMath.cos(scalePrec(x));
	}

	@Override
	protected BigDecimal tan(BigDecimal x) {
		return BigDecimalMath.tan(scalePrec(x));
	}

	@Override
	protected BigDecimal atan(BigDecimal x) {
		return BigDecimalMath.atan(scalePrec(x));
	}

	@Override
	protected BigDecimal atan2(BigDecimal y, BigDecimal x) {
		MathContext mc = getMathContext();
		int xSign = x.signum();
		if (xSign > 0) {
			return atan(y.divide(x, mc));
		} else if (xSign < 0) {
			int ySign = y.signum();
			if (ySign >= 0) {
				return atan(y.divide(x, mc)).add(pi(), mc);
			} else {
				return atan(y.divide(x, mc)).subtract(pi(), mc);
			}
		} else {
			int ySign = y.signum();
			if (ySign > 0) {
				return pi().divide(BigDecimal.valueOf(2), mc);
			} else if (ySign < 0) {
				return pi().divide(BigDecimal.valueOf(2), mc).negate();
			} else {
				return BigDecimal.ZERO;
			}
		}
	}

	@Override
	protected BigDecimal sqrt(BigDecimal x) {
		return BigDecimalMath.sqrt(x, getMathContext()).stripTrailingZeros();
	}

	@Override
	protected BigDecimal nthRoot(BigDecimal x, int n) {
		return BigDecimalMath.root(n, scalePrec(x)).stripTrailingZeros();
	}

	@Override
	protected BigDecimal nthPow(BigDecimal x, int n) {
		return BigDecimalMath.powRound(scalePrec(x), n).stripTrailingZeros();
	}

	@Override
	protected BigDecimal pow(BigDecimal x, BigDecimal r) {
		return BigDecimalMath.pow(scalePrec(x), scalePrec(r)).stripTrailingZeros();
	}

	protected BigDecimal pi() {
		return BigDecimalMath.pi(getMathContext());
	}

	private BigDecimal scalePrec(BigDecimal x) {
		return BigDecimalMath.scalePrec(x, getMathContext());
	}
}
