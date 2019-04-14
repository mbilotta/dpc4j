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

package org.altervista.mbilotta.julia.impl.formulas;

import org.altervista.mbilotta.julia.Author;
import org.altervista.mbilotta.julia.Decimal;
import org.altervista.mbilotta.julia.NumberFactory;
import org.altervista.mbilotta.julia.impl.AbstractFormula;
import org.altervista.mbilotta.julia.math.Complex;
import org.altervista.mbilotta.julia.math.Real;


@Author(name = "Maurizio Bilotta", contact = "mailto:maurizeio@gmail.com")
public class QuadraticFormula extends AbstractFormula<QuadraticFormula> {

	private Real bailout;

	private Complex zero;
	private Real bailout2;

	public QuadraticFormula() {
		bailout = new Decimal(20);
	}

	public QuadraticFormula(Real bailout) {
		this.bailout = bailout;
	}

	public void iterate() {
		z = z.square().plus(c);
	}

	public boolean bailoutOccured() {
		return z.absSquared().gt(bailout2);
	}

	public void initJuliaIteration(Complex z) {
		setZ(z);
	}

	public void initMandelbrotIteration(Complex c) {
		setZ(zero);
		setC(c);
	}

	public QuadraticFormula newInstance() {
		return new QuadraticFormula(bailout);
	}

	public Real getBailout() {
		return bailout;
	}

	public void setBailout(Real bailout) {
		this.bailout = bailout;
	}

	public void cacheConstants(NumberFactory numberFactory) {
		zero = numberFactory.zero();
		bailout2 = bailout.square();
	}
}
