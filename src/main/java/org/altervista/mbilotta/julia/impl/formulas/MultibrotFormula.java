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
import org.altervista.mbilotta.julia.program.parsers.IntParameter;
import org.altervista.mbilotta.julia.program.parsers.RealParameter;


@Author(name = "Maurizio Bilotta", contact = "mailto:maurizeio@yahoo.it")
public class MultibrotFormula extends AbstractFormula<MultibrotFormula> {

	private int n;
	private Real bailout;

	private Real zero;
	private Real bailout2;

	public MultibrotFormula() {
		this(4, new Decimal(20));
	}

	public MultibrotFormula(int n, Real bailout) {
		this.n = n;
		this.bailout = bailout;
	}

	@Override
	public void iterate() {
		z = z.toThe(n).plus(c);
	}

	@Override
	public boolean bailoutOccured() {
		return z.absSquared().gt(bailout2);
	}

	@Override
	public void initJuliaIteration(Complex z) {
		setZ(z);
	}

	@Override
	public void initMandelbrotIteration(Complex c) {
		setZ(zero);
		setC(c);
	}

	@Override
	public MultibrotFormula newInstance() {
		return new MultibrotFormula(n, this.bailout);
	}

	public int getN() {
		return n;
	}

	@IntParameter.Min(2)
	public void setN(int n) {
		this.n = n;
	}

	public Real getBailout() {
		return bailout;
	}

	@RealParameter.Min(value = "0", inclusive = false)
	public void setBailout(Real bailout) {
		this.bailout = bailout;
	}

	@Override
	public void cacheConstants(NumberFactory numberFactory) {
		zero = numberFactory.zero();
		bailout2 = bailout.square();
	}
}
