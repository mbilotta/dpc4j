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


public class RJMComplex extends AbstractRJMComplex {

	private final BigDecimal im;

	protected RJMComplex(MathContext mc, BigDecimal re, BigDecimal im) {
		super(mc, re);
		this.im = im;
	}

	public RJMComplex(MathContext mc, int re, int im) {
		super(mc, re);
		this.im = BigDecimal.valueOf(im).round(mc);
	}

	public RJMComplex(MathContext mc, Decimal re, Decimal im) {
		super(mc, re);
		this.im = new BigDecimal(im.toNormalizedString(), mc);
	}

	public RJMComplex(MathContext mc, Real re, Real im) {
		super(mc, re);
		this.im = re(im).round(mc);
	}

	@Override
	protected final BigDecimal getImag() {
		return im;
	}
}
