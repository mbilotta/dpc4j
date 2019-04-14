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

package org.altervista.mbilotta.julia.impl.escapetime;

import org.altervista.mbilotta.julia.Formula;
import org.altervista.mbilotta.julia.IntermediateImage;
import org.altervista.mbilotta.julia.impl.IntegerImage;
import org.altervista.mbilotta.julia.impl.PointCalculator;
import org.altervista.mbilotta.julia.math.Complex;
import org.altervista.mbilotta.julia.math.CoordinateTransform;


public abstract class StandardPointCalculator implements PointCalculator {

	final IntegerImage iimg;
	final int maxIterations;

	public static class MandelbrotSet extends StandardPointCalculator {

		public MandelbrotSet(IntegerImage iimg, EscapeTime representation) {
			super(iimg, representation);
		}

		@Override
		protected void initIteration(Complex c, Formula formula) {
			formula.initMandelbrotIteration(c);
		}
	}

	public static class JuliaSet extends StandardPointCalculator {

		public JuliaSet(IntegerImage iimg, EscapeTime representation) {
			super(iimg, representation);
		}

		@Override
		protected void initIteration(Complex z, Formula formula) {
			formula.initJuliaIteration(z);
		}
	}

	public StandardPointCalculator(IntegerImage iimg, EscapeTime representation) {
		this.iimg = iimg;
		this.maxIterations = representation.getMaxIterations();
	}

	protected int computePointImpl(int x, int y, CoordinateTransform coordinateTansform, Formula formula, int minIterations) {
		Complex point = coordinateTansform.toComplex(x, y);
		initIteration(point, formula);

		int i = 0;
		while (i < minIterations) {
			formula.iterate();
			i++;
		}

		while (!formula.bailoutOccured() && i < maxIterations) {
			formula.iterate();
			i++;
		}

		iimg.setPoint(x, y, i < maxIterations ? i : -1);
		return i;
	}

	protected abstract void initIteration(Complex point, Formula formula);

	@Override
	public void computePoint(int x, int y, CoordinateTransform coordinateTansform, Formula formula) {
		computePointImpl(x, y, coordinateTansform, formula, 0);
	}

	@Override
	public IntermediateImage getIntermediateImage() {
		return iimg;
	}

	@Override
	public PointCalculator newInstance() {
		return this;
	}
}