/*
 * Copyright (C) 2019 Maurizio Bilotta.
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

package org.altervista.mbilotta.julia.impl.stalks;

import org.altervista.mbilotta.julia.Formula;
import org.altervista.mbilotta.julia.IntermediateImage;
import org.altervista.mbilotta.julia.NumberFactory;
import org.altervista.mbilotta.julia.impl.FloatImage;
import org.altervista.mbilotta.julia.impl.PointCalculator;
import org.altervista.mbilotta.julia.math.Complex;
import org.altervista.mbilotta.julia.math.CoordinateTransform;
import org.altervista.mbilotta.julia.math.Real;


public abstract class StalksCalculator implements PointCalculator, Cloneable {
	
	protected final FloatImage image;

	private final int maxIterations;
	private final int minIterations;

	private final Real orbitPositionX;
	private final Real orbitPositionY;
	private final Real orbitPositionZ;
	private final Real orbitPositionW;
	
	private final Real orbitDividend;

	private final boolean orbitInsideOnly;
	private final boolean showOrbitX;
	private final boolean showOrbitY;
	
	private final Real initialTrapDistance;

	public static class MandelbrotSet extends StalksCalculator {

		public MandelbrotSet(FloatImage image, Stalks representation, NumberFactory numberFactory) {
			super(image, representation, numberFactory);
		}

		@Override
		protected void initIteration(Complex c, Formula formula) {
			formula.initMandelbrotIteration(c);
		}
	}

	public static class JuliaSet extends StalksCalculator {

		public JuliaSet(FloatImage image, Stalks representation, NumberFactory numberFactory) {
			super(image, representation, numberFactory);
		}

		@Override
		protected void initIteration(Complex z, Formula formula) {
			formula.initJuliaIteration(z);
		}
	}

	public StalksCalculator(FloatImage image, Stalks representation, NumberFactory numberFactory) {
		this.image = image;

		maxIterations = representation.getMaxIterations();
		minIterations = representation.getMinIterations();

		orbitPositionX = representation.getOrbitPositionX();
		orbitPositionY = representation.getOrbitPositionY();
		orbitPositionZ = representation.getOrbitPositionZ();
		orbitPositionW = representation.getOrbitPositionW();
		
		orbitDividend = representation.getOrbitDividend();
		
		orbitInsideOnly = representation.isOrbitInsideOnly();
		showOrbitX = representation.isShowOrbitX();
		showOrbitY = representation.isShowOrbitY();
		
		initialTrapDistance = numberFactory.valueOf(10000000);
	}

	protected float computePointImpl(Complex point, Formula formula) {
		int i = 0;
		while (i < minIterations) {
			formula.iterate();
			i++;
		}
		
		Real trapDistance = initialTrapDistance;
		boolean bailoutOccured = false;
		while (i < maxIterations && !(bailoutOccured = formula.bailoutOccured())) {
			formula.iterate();
			
			Real distanceToX = showOrbitX ? formula.getZ().re().plus(orbitPositionX).abs().plus(orbitPositionZ) : initialTrapDistance;
			Real distanceToY = showOrbitY ? formula.getZ().im().plus(orbitPositionY).abs().plus(orbitPositionW) : initialTrapDistance;
			
			Real distanceToTrap = distanceToX.min(distanceToY);

			trapDistance = trapDistance.min(distanceToTrap);
			
			i++;
		}
		
		if (orbitInsideOnly) {
			if (i >= maxIterations && !bailoutOccured) {
				return trapDistance.dividedBy(orbitDividend).floatValue();
			} else {
				return -1;
			}
		}
		
		return trapDistance.dividedBy(orbitDividend).floatValue();
	}

	protected abstract void initIteration(Complex point, Formula formula);

	@Override
	public void computePoint(int x, int y, CoordinateTransform coordinateTransform, Formula formula) {
		Complex point = coordinateTransform.toComplex(x, y);
		initIteration(point, formula);
		image.setPoint(x, y, computePointImpl(point, formula));
	}

	@Override
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
}
