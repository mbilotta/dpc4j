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
import org.altervista.mbilotta.julia.math.Real;


public class DoubleComplex extends AbstractDoubleComplex {
	
	private final double re;
	private final double im;

	public DoubleComplex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	public DoubleComplex(Decimal re, Decimal im) {
		this.re = Double.parseDouble(re.toNormalizedString());
		this.im = Double.parseDouble(im.toNormalizedString());
	}

	public DoubleComplex(Real re, Real im) {
		this.re = re(re);
		this.im = re(im);
	}

	@Override
	protected final double getReal() {
		return re;
	}

	@Override
	protected final double getImag() {
		return im;
	}
}
