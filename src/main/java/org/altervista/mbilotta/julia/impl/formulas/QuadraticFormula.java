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
import org.altervista.mbilotta.julia.Formula;
import org.altervista.mbilotta.julia.NumberFactory;
import org.altervista.mbilotta.julia.math.Complex;
import org.altervista.mbilotta.julia.math.Real;


@Author(name = "Maurizio Bilotta", contact = "mailto:maurizeio@yahoo.it")
public class QuadraticFormula implements Formula {

	private Real bailout;

	private Real zero;
	private Real bailout2;

	private Real reZ;
	private Real imZ;
	private Real reZ2;
	private Real imZ2;

	private Real reC;
	private Real imC;

	public QuadraticFormula() {
		bailout = new Decimal(20);
	}

	public QuadraticFormula(Real bailout) {
		this.bailout = bailout;
	}

	@Override
	public Complex getZ() {
		return reZ.plus(imZ.i());
	}

	@Override
	public Complex getC() {
		return reC.plus(imC.i());
	}

	@Override
	public void setC(Complex c) {
		reC = c.re();
		imC = c.im();
	}

	@Override
	public void iterate() {
		Real reTemp = reZ2.minus(imZ2).plus(reC);
		imZ = reZ.times(imZ).times(2).plus(imC);
		reZ = reTemp;
		imZ2 = imZ.square();
		reZ2 = reZ.square();
	}

	@Override
	public boolean bailoutOccured() {
		return reZ2.plus(imZ2).gt(bailout2);
	}

	@Override
	public void initJuliaIteration(Complex z) {
		reZ = z.re();
		imZ = z.im();
		reZ2 = reZ.square();
		imZ2 = imZ.square();
	}

	@Override
	public void initMandelbrotIteration(Complex c) {
		reZ = imZ = reZ2 = imZ2 = zero;
		setC(c);
	}

	@Override
	public QuadraticFormula newInstance() {
		return new QuadraticFormula(bailout);
	}

	public Real getBailout() {
		return bailout;
	}

	public void setBailout(Real bailout) {
		this.bailout = bailout;
	}

	@Override
	public void cacheConstants(NumberFactory numberFactory) {
		zero = numberFactory.zero();
		bailout2 = bailout.square();
	}
}
