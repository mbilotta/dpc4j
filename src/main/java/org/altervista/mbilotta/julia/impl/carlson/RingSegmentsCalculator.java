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
import org.altervista.mbilotta.julia.impl.carlson.CarlsonPixelCalculator;
import org.altervista.mbilotta.julia.math.Complex;
import org.altervista.mbilotta.julia.math.CoordinateTransform;
import org.altervista.mbilotta.julia.math.Real;


public abstract class RingSegmentsCalculator implements PointCalculator, Cloneable {
	
	protected final IntegerImage iimg;
	
	private final int maxIterations;
	private final Real rm;
	private final int paletteSize;

	private final Real halfT;
	private final Real px;
	private final Real py;
	private final Real dSqd;

	public static class MandelbrotSet extends RingSegmentsCalculator {

		public MandelbrotSet(IntegerImage iimg, RingSegments representation, NumberFactory numberFactory) {
			super(iimg, representation, numberFactory);
		}

		protected void initIteration(Complex c, Formula formula) {
			formula.initMandelbrotIteration(c);
		}
	}

	public static class JuliaSet extends RingSegmentsCalculator {

		public JuliaSet(IntegerImage iimg, RingSegments representation, NumberFactory numberFactory) {
			super(iimg, representation, numberFactory);
		}

		protected void initIteration(Complex z, Formula formula) {
			formula.initJuliaIteration(z);
		}
	}

	public RingSegmentsCalculator(IntegerImage iimg, RingSegments representation, NumberFactory numberFactory) {
		this.iimg = iimg;

		maxIterations = representation.getMaxIterations();
		rm = representation.getRm();
		paletteSize = representation.getPaletteSize();

		Real phi = numberFactory.pi().dividedBy(8);
		Real t = representation.getT();
		halfT = t.dividedBy(2);
		Real ro = rm.plus(halfT);
		px = rm.times(phi.cos());
		py = rm.times(phi.sin());
		dSqd = rm.square().plus(ro.square()).minus(ro.times(px).times(2));
	}

	@Override
	public void computePoint(int x, int y,
			CoordinateTransform coordinateTransform, Formula formula) {
		Complex point = coordinateTransform.toComplex(x, y);
		initIteration(point, formula);
		iimg.setPoint(x, y, computePointImpl(point, formula));
	}

	protected int computePointImpl(Complex point, Formula formula) {
		int i = 0;
		boolean trapped = false;
		Real zToCenterSqd = null;
		int segment = 0;
		boolean bailoutOccured = false;
		while (i < maxIterations && !trapped && !(bailoutOccured = formula.bailoutOccured())) {
			formula.iterate();
			
			Complex z = formula.getZ();
			if (i > 0 && z.abs().minus(rm).abs().lt(halfT)) {
				trapped = true;
				Real x = z.re();
				Real y = z.im();
				Real xAbs = x.abs();
				Real yAbs = y.abs();
				
				if (xAbs.gte(yAbs)) {
					zToCenterSqd = xAbs.minus(px).square().plus(yAbs.minus(py).square());
				} else {
					zToCenterSqd = xAbs.minus(py).square().plus(yAbs.minus(px).square());
				}
				
				if (x.gte(0) && y.gte(0)) {
					segment = xAbs.gte(yAbs) ? 0 : 1;
				} else if (x.lt(0)) {
					if (y.gte(0)) {
						segment = xAbs.lt(yAbs) ? 2 : 3;
					} else {
						segment = xAbs.gte(yAbs) ? 4 : 5;
					}
				} else {
					segment = xAbs.lt(yAbs) ? 6 : 7;
				}
			}
			
			i++;
		}
		
		int rv;
		if (trapped) {
			Real ratio = zToCenterSqd.dividedBy(dSqd).rSqrt();
			rv = ratio.times(paletteSize).intValue() * 8 + segment;
		} else if (bailoutOccured) {
			rv = CarlsonPixelCalculator.UNTRAPPED_OUTSIDE_POINT;
		} else {
			rv = CarlsonPixelCalculator.UNTRAPPED_INSIDE_POINT;
		}
		return rv;
	}

	protected abstract void initIteration(Complex point, Formula formula);

	@Override
	public IntermediateImage getIntermediateImage() {
		return iimg;
	}

	@Override
	public PointCalculator newInstance() {
		try {
			return (PointCalculator) clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}
