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

import org.altervista.mbilotta.julia.Formula;
import org.altervista.mbilotta.julia.IntermediateImage;
import org.altervista.mbilotta.julia.NumberFactory;
import org.altervista.mbilotta.julia.impl.IntegerImage;
import org.altervista.mbilotta.julia.impl.PointCalculator;
import org.altervista.mbilotta.julia.math.Complex;
import org.altervista.mbilotta.julia.math.CoordinateTransform;
import org.altervista.mbilotta.julia.math.Real;


public abstract class TangentCirclesCalculator implements PointCalculator, Cloneable {

	protected final IntegerImage image;

	private final int maxIterations;
	private final Real rc;
	private final int paletteSize;

	private final Real rm;
	private final Real rcSqd;	
	private final Real px;
	private final Real py;

	public static class MandelbrotSet extends TangentCirclesCalculator {

		public MandelbrotSet(IntegerImage image, TangentCircles representation, NumberFactory numberFactory) {
			super(image, representation, numberFactory);
		}

		protected void initIteration(Complex c, Formula formula) {
			formula.initMandelbrotIteration(c);
		}
	}

	public static class JuliaSet extends TangentCirclesCalculator {

		public JuliaSet(IntegerImage image, TangentCircles representation, NumberFactory numberFactory) {
			super(image, representation, numberFactory);
		}

		protected void initIteration(Complex z, Formula formula) {
			formula.initJuliaIteration(z);
		}
	}

	public TangentCirclesCalculator(IntegerImage image, TangentCircles representation, NumberFactory numberFactory) {
		this.image = image;

		maxIterations = representation.getMaxIterations();
		rc = representation.getRc();
		paletteSize = representation.getPaletteSize();

		Real phi = numberFactory.pi().dividedBy(8);
		rcSqd = rc.square();
		rm = rc.dividedBy(phi.sin());
		px = rm.times(phi.times(2).cos());
		py = rm.times(phi.times(2).sin());
	}

	public IntermediateImage getIntermediateImage() {
		return image;
	}

	public PointCalculator newInstance() {
		try {
			return (PointCalculator) clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	protected int computePointImpl(Complex point, Formula formula) {
		int i = 0;
		boolean trapped = false;
		Real zToCenterSqd = null;
		int circle = 0;
		boolean bailoutOccured = false;
		while (i < maxIterations && !trapped && !(bailoutOccured = formula.bailoutOccured())) {
			formula.iterate();

			Complex z = formula.getZ();
			if (i > 0 && z.abs().minus(rm).abs().lt(rc)) {
				Real xAbs = z.re().abs();
				Real yAbs = z.im().abs();

				Real dSqd0 = xAbs.square().plus(yAbs.minus(rm).square());
				Real dSqd1 = xAbs.minus(px).square().plus(yAbs.minus(py).square());
				Real dSqd2 = xAbs.minus(rm).square().plus(yAbs.square());

				if (dSqd0.lt(rcSqd)) {
					trapped = true;
					zToCenterSqd = dSqd0;
					circle = z.im().gt(0) ? 0 : 4;
				} else if (dSqd1.lt(rcSqd)) {
					trapped = true;
					zToCenterSqd = dSqd1;
					if (z.re().gt(0)) {
						circle = z.im().gt(0) ? 1 : 3;
					} else {
						circle = z.im().gt(0) ? 7 : 5;
					}
				} else if (dSqd2.lt(rcSqd)) {
					trapped = true;
					zToCenterSqd = dSqd2;
					circle = z.re().gt(0) ? 2 : 6;
				}
			}
			
			i++;
		}

		int rv;
		if (trapped) {
			Real ratio = zToCenterSqd.dividedBy(rcSqd).rSqrt();
			rv = ratio.times(paletteSize).intValue() * 8 + circle;
		} else if (bailoutOccured) {
			rv = CarlsonPixelCalculator.UNTRAPPED_OUTSIDE_POINT;
		} else {
			rv = CarlsonPixelCalculator.UNTRAPPED_INSIDE_POINT;
		}
		return rv;
	}

	protected abstract void initIteration(Complex point, Formula formula);

	public void computePoint(int x, int y, CoordinateTransform coordinateTransform, Formula formula) {
		Complex point = coordinateTransform.toComplex(x, y);
		initIteration(point, formula);
		image.setPoint(x, y, computePointImpl(point, formula));
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public Real getRc() {
		return rc;
	}

	public int getPaletteSize() {
		return paletteSize;
	}
}
