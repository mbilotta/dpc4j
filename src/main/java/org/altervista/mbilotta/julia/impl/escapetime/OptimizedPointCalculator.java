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
import org.altervista.mbilotta.julia.impl.AbstractProgressiveRefinementPointCalculator;
import org.altervista.mbilotta.julia.impl.OptimizationDomain;
import org.altervista.mbilotta.julia.impl.ProgressiveRefinementPointCalculator;
import org.altervista.mbilotta.julia.impl.ProgressivelyRefinedImage;
import org.altervista.mbilotta.julia.math.CoordinateTransform;


public abstract class OptimizedPointCalculator extends AbstractProgressiveRefinementPointCalculator implements Cloneable {

	private final StandardPointCalculator delegate;
	private final OptimizationDomain optimizationDomain;

	public static class MandelbrotSet extends OptimizedPointCalculator {

		public MandelbrotSet(ProgressivelyRefinedImage iimg, EscapeTime representation) {
			super(iimg, representation, new StandardPointCalculator.MandelbrotSet(iimg, representation));
		}
	}

	public static class JuliaSet extends OptimizedPointCalculator {

		public JuliaSet(ProgressivelyRefinedImage iimg, EscapeTime representation) {
			super(iimg, representation, new StandardPointCalculator.JuliaSet(iimg, representation));
		}
	}

	public OptimizedPointCalculator(ProgressivelyRefinedImage iimg, EscapeTime representation, StandardPointCalculator delegate) {
		super(iimg, representation.getNumOfSteps());
		this.delegate = delegate;
		this.optimizationDomain = representation.getOptimizationDomain();
	}

	@Override
	public void computePoint(int x, int y, CoordinateTransform coordinateTansform, Formula formula) {
		int chunkSize = getChunkSize();
		if (chunkSize == initialChunkSize) {
			int i = delegate.computePointImpl(x, y, coordinateTansform, formula, 0);
			offerMinIterations(i);
		} else {
			int parentChunkSize = chunkSize << 1;
			int parentX = x % parentChunkSize == 0 ? x : x - chunkSize;
			int parentY = y % parentChunkSize == 0 ? y : y - chunkSize;
			if ( !(x == parentX && y == parentY) ) {
				int value = iimg.getPoint(parentX, parentY);
				if (optimizationDomain != OptimizationDomain.NOWHERE &&
						iimg.hasAllNeighborsEqual(parentX, parentY, value, parentChunkSize, optimizationDomain == OptimizationDomain.BORDERS_EXCLUDED)) {
					iimg.setPoint(x, y, value);
				} else {
					delegate.computePointImpl(x, y, coordinateTansform, formula, getMinIterations());
				}
			}
		}
	}

	@Override
	public ProgressiveRefinementPointCalculator newInstance() {
		try {
			return (ProgressiveRefinementPointCalculator) clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}
}