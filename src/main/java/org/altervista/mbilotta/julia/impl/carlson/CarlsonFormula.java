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

package org.altervista.mbilotta.julia.impl.carlson;

import org.altervista.mbilotta.julia.Author;
import org.altervista.mbilotta.julia.Decimal;
import org.altervista.mbilotta.julia.impl.AbstractFormula;
import org.altervista.mbilotta.julia.math.Complex;
import org.altervista.mbilotta.julia.math.Real;


@Author(name = "Maurizio Bilotta", contact = "mailto:maurizeio@gmail.com")
public final class CarlsonFormula extends AbstractFormula<CarlsonFormula> {

	private Real bailout;

	private Complex errCache;

	public CarlsonFormula() {
		bailout = new Decimal("1e-12");
	}

	public CarlsonFormula(Real bailout) {
		this.bailout = bailout;
	}

	public void iterate() {
		Complex z = getZ();
		Complex z2 = z.square();
		Complex c = getC();
		errCache = z2.times(z2.plus(c).minus(1)).minus(c).dividedBy(
				z.times(z2.times(4).plus(c.minus(1).times(2))));
		setZ(z.minus(errCache));
	}

	public boolean bailoutOccured() {
		return errCache != null && errCache.absSquared().lt(bailout);
	}

	public void initJuliaIteration(Complex z) {
		setZ(z);
		errCache = null;
	}

	public void initMandelbrotIteration(Complex c) {
		setC(c);
		setZ(c.negate().plus(1).dividedBy(6).sqrt());
		errCache = null;
	}

	public CarlsonFormula newInstance() {
		return new CarlsonFormula(bailout);
	}

	public Real getBailout() {
		return bailout;
	}

	public void setBailout(Real bailout) {
		this.bailout = bailout;
	}

	public boolean equals(Object obj) {
		if (obj == this)
			return true;
		
		if (obj instanceof CarlsonFormula) {
			return bailout.equals(((CarlsonFormula) obj).bailout);
		}

		return false;
	}

	public String toString() {
		return getClass().getCanonicalName() + "[bailout=" + bailout + ']';
	}
}
