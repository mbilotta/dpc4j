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
import org.altervista.mbilotta.julia.math.Real;


@Author(name = "Maurizio Bilotta", contact = "mailto:maurizeio@gmail.com")
public class MultibrotFormula extends QuadraticFormula {

	private int n;

	public MultibrotFormula() {
		n = 4;
	}

	public MultibrotFormula(int n, Real bailout) {
		super(bailout);
		this.n = n;
	}

	@Override
	public void iterate() {
		setZ(getZ().toThe(n).plus(getC()));
	}

	@Override
	public MultibrotFormula newInstance() {
		return new MultibrotFormula(n, getBailout());
	}

	public int getN() {
		return n;
	}

	public void setN(int n) {
		this.n = n;
	}
}
