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

package org.altervista.mbilotta.julia.impl.munafo;

import org.altervista.mbilotta.julia.Formula;
import org.altervista.mbilotta.julia.NumberFactory;
import org.altervista.mbilotta.julia.impl.AbstractProgressiveRefinementPointCalculator;
import org.altervista.mbilotta.julia.impl.OptimizationDomain;
import org.altervista.mbilotta.julia.impl.ProgressiveRefinementPointCalculator;
import org.altervista.mbilotta.julia.math.Complex;
import org.altervista.mbilotta.julia.math.CoordinateTransform;
import org.altervista.mbilotta.julia.math.Real;


public abstract class MuEncyPointCalculator extends
		AbstractProgressiveRefinementPointCalculator implements Cloneable {

	final MuEncyImage iimg;

	final int maxIterations;
	final Real bailout;
	final boolean distanceEstimateComputed;
	final boolean continuousDwellComputed;
	final boolean binaryDecompositionComputed;
	final boolean optimizeNowhere;
	final boolean optimizeOnlyAtInnerPoints;

	final double loglogBailout;
	final Real pixelSpacing;
	static final double log2 = Math.log(2);

	public static class MandelbrotSet extends MuEncyPointCalculator {

		private Complex derZ;
		private final Complex zero;

		public MandelbrotSet(MuEncyImage iimg,
				NumberFactory numberFactory,
				CoordinateTransform coordinateTransform,
				MuEncyRepresentation representation) {
			super(iimg, numberFactory, coordinateTransform, representation);
			zero = numberFactory.zero();
		}

		@Override
		protected void initIteration(Complex c, Formula formula) {
			formula.initMandelbrotIteration(c);
			derZ = zero;
		}

		@Override
		protected void iterateDerivative(Formula formula) {
			derZ = formula.getZ().times(2).times(derZ).plus(1);
		}

		@Override
		protected Complex getDerZ() {
			return derZ;
		}
	}

	public static class JuliaSet extends MuEncyPointCalculator {

		private Complex derZ;
		private final Complex one;

		public JuliaSet(MuEncyImage iimg,
				NumberFactory numberFactory,
				CoordinateTransform coordinateTransform,
				MuEncyRepresentation representation) {
			super(iimg, numberFactory, coordinateTransform, representation);
			one = numberFactory.one();
		}

		@Override
		protected void initIteration(Complex z, Formula formula) {
			formula.initJuliaIteration(z);
			derZ = one;
		}

		@Override
		protected void iterateDerivative(Formula formula) {
			derZ = formula.getZ().times(2).times(derZ);
		}

		@Override
		protected Complex getDerZ() {
			return derZ;
		}
	}

	public MuEncyPointCalculator(MuEncyImage iimg,
			NumberFactory numberFactory,
			CoordinateTransform coordinateTransform,
			MuEncyRepresentation repr) {
		super(iimg, repr.getNumOfSteps());

		this.iimg = iimg;

		maxIterations = repr.getMaxIterations();
		bailout = repr.getBailout();
		distanceEstimateComputed = repr.isDistanceEstimateComputed();
		continuousDwellComputed = repr.isContinuousDwellComputed();
		binaryDecompositionComputed = repr.isBinaryDecompositionComputed();
		optimizeNowhere = repr.getOptimizationDomain() == OptimizationDomain.NOWHERE;
		optimizeOnlyAtInnerPoints = repr.getOptimizationDomain() == OptimizationDomain.BORDERS_EXCLUDED;

		double doubleBailout = repr.getBailout().doubleValue();
		loglogBailout = Math.log(Math.log(doubleBailout));
		pixelSpacing = coordinateTransform.getScaleRe().abs().min(coordinateTransform.getScaleIm().abs());
	}

	protected abstract void initIteration(Complex point, Formula formula);
	protected abstract void iterateDerivative(Formula formula);
	protected abstract Complex getDerZ();

	private int computeDwell(int x, int y, CoordinateTransform coordinateTransform, Formula formula, int minIterations) {
		Complex point = coordinateTransform.toComplex(x, y);
		initIteration(point, formula);

		int dwell = 0;
		while (dwell < minIterations) {
			formula.iterate();
			dwell++;
		}

		while (!formula.bailoutOccured() && dwell < maxIterations) {
			formula.iterate();
			dwell++;
		}

		MuEncyImage.Point ipoint = iimg.new Point(x, y);
		if (dwell < maxIterations) {
			Complex z = formula.getZ();
			ipoint.setDwell(dwell);
			if (distanceEstimateComputed) {
				ipoint.setValue(1f);
			}
			if (continuousDwellComputed) {
				ipoint.setFinalrad(computeFinalrad(z));
			} else if (binaryDecompositionComputed) {
				ipoint.setFinalang(computeFinalang(z));
			}
		} else {
			ipoint.setDwell(Integer.MIN_VALUE);
		}
		return dwell;
	}

	private int computeDistanceEstimate(int x, int y, CoordinateTransform coordinateTransform, Formula formula, int minIterations) {
		Complex point = coordinateTransform.toComplex(x, y);
		initIteration(point, formula);

		int dwell = 0;
		while (dwell < minIterations) {
			iterateDerivative(formula);
			formula.iterate();
			dwell++;
		}

		while (!formula.bailoutOccured() && dwell < maxIterations) {
			iterateDerivative(formula);
			formula.iterate();
			dwell++;
		}

		MuEncyImage.Point ipoint = iimg.new Point(x, y);
		if (dwell < maxIterations) {
			Complex z = formula.getZ();
			Real absZ = z.abs();
			Real distanceEstimate = absZ.times(absZ.rLn()).times(2).dividedBy(getDerZ().abs());
			double dscale = Math.log(distanceEstimate.dividedBy(pixelSpacing).doubleValue()) / log2;
			float value;
			if (dscale > 0) {
				value = 1;
				ipoint.setDwell(dwell);
			} else if (dscale > -8) {
				value = (float) ((8 + dscale) / 8);
				ipoint.setDwell(-dwell);
			} else {
				value = 0;
				ipoint.setDwell(-dwell);
			}
			ipoint.setValue(value);
			
			if (continuousDwellComputed) {
				ipoint.setFinalrad(computeFinalrad(z));
			} else if (binaryDecompositionComputed) {
				ipoint.setFinalang(computeFinalang(z));
			}
		} else {
			ipoint.setDwell(Integer.MIN_VALUE);
		}
		return dwell;
	}

	private float computeFinalrad(Complex z) {
		double drv = 2 + (loglogBailout - Math.log(Math.log(z.absSquared().doubleValue()))) / log2;
		float frv = drv <= 0 ? 0f : (float) drv;
		if (binaryDecompositionComputed && z.im().lt(0)) {
			frv = Math.copySign(frv, -1f);
		}
		return frv;
	}

	private boolean computeFinalang(Complex z) {
		return z.im().lt(0);
	}

	public void computePoint(int x, int y, CoordinateTransform coordinateTransform, Formula formula) {
		int chunkSize = getChunkSize();
		if (chunkSize == initialChunkSize) {
			int i = distanceEstimateComputed ? 
					computeDistanceEstimate(x, y, coordinateTransform, formula, 0) :
					computeDwell(x, y, coordinateTransform, formula, 0);
			offerMinIterations(i);
		} else {
			int parentChunkSize = chunkSize << 1;
			int parentX = x % parentChunkSize == 0 ? x : x - chunkSize;
			int parentY = y % parentChunkSize == 0 ? y : y - chunkSize;
			if ( !(x == parentX && y == parentY) ) {
				if (optimizeNowhere) {
					if (distanceEstimateComputed) {
						computeDistanceEstimate(x, y, coordinateTransform, formula, getMinIterations());
					} else {
						computeDwell(x, y, coordinateTransform, formula, getMinIterations());
					}
				} else {
					int value = iimg.getPoint(parentX, parentY);
					if (iimg.hasAllNeighborsEqual(parentX, parentY, value, parentChunkSize, optimizeOnlyAtInnerPoints)) {
						if (value >= 0) {
							if (continuousDwellComputed || binaryDecompositionComputed) {
								computeDwell(x, y, coordinateTransform, formula, value);
							} else {
								MuEncyImage.Point ipoint = iimg.new Point(x, y);
								ipoint.setDwell(value);
								if (distanceEstimateComputed) {
									ipoint.setValue(1f);
								}
							}
						} else if (value == Integer.MIN_VALUE) {
							iimg.setPoint(x, y, value);
						} else {
							computeDistanceEstimate(x, y, coordinateTransform, formula, -value);
						}
					} else if (distanceEstimateComputed && iimg.hasNegativeNeighbors(parentX, parentY, parentChunkSize, optimizeOnlyAtInnerPoints)) {
						computeDistanceEstimate(x, y, coordinateTransform, formula, getMinIterations());
					} else {
						computeDwell(x, y, coordinateTransform, formula, getMinIterations());
					}
				}
			}
		}
	}

	public MuEncyImage getIntermediateImage() {
		return iimg;
	}

	public ProgressiveRefinementPointCalculator newInstance() {
		try {
			return (ProgressiveRefinementPointCalculator) clone();
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

}
