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

import java.io.IOException;
import java.io.ObjectInputStream;

import org.altervista.mbilotta.julia.Author;
import org.altervista.mbilotta.julia.Decimal;
import org.altervista.mbilotta.julia.IntermediateImage;
import org.altervista.mbilotta.julia.NumberFactory;
import org.altervista.mbilotta.julia.Previewable;
import org.altervista.mbilotta.julia.impl.AbstractRasterRepresentation;
import org.altervista.mbilotta.julia.impl.OptimizationDomain;
import org.altervista.mbilotta.julia.impl.PixelCalculator;
import org.altervista.mbilotta.julia.impl.PointCalculator;
import org.altervista.mbilotta.julia.math.CoordinateTransform;
import org.altervista.mbilotta.julia.math.Real;


@Author(name = "Maurizio Bilotta", contact = "mailto:maurizeio@gmail.com")
public class MuEncyRepresentation extends AbstractRasterRepresentation {

	private int maxIterations = 500;
	private Real bailout = new Decimal(20);
	private boolean distanceEstimateComputed = true;
	private boolean continuousDwellComputed = true;
	private boolean binaryDecompositionComputed = true;
	private int dwellScaleLimit = 100000;
	private double finalangWeight = 0.15;
	private double finalradWeight = 0.05;
	private double angleWeight = 2.9;
	private OptimizationDomain optimizationDomain = OptimizationDomain.BORDERS_EXCLUDED;
	
	public MuEncyRepresentation() {
		setNumOfSteps(5);
	}

	@Override
	public IntermediateImage createIntermediateImage(int width, int height, int numOfProducers) {
		return new MuEncyImage(width, height, numOfProducers, this);
	}

	@Override
	protected PointCalculator createPointCalculator(IntermediateImage image,
			NumberFactory numberFactory,
			CoordinateTransform coordinateTransform, boolean isJuliaSet) {
		return isJuliaSet ?
				new MuEncyPointCalculator.JuliaSet((MuEncyImage) image, numberFactory, coordinateTransform, this) :
				new MuEncyPointCalculator.MandelbrotSet((MuEncyImage) image, numberFactory, coordinateTransform, this); 
	}

	@Override
	protected PixelCalculator createPixelCalculator() {
		return new MuEncyPixelCalculator(this);
	}

	@Override
	protected IntermediateImage readIntermediateImageImpl(int width, int height, int numOfProducers, ObjectInputStream in)
			throws ClassNotFoundException, IOException {
		return new MuEncyImage(width, height, numOfProducers, this, in);
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public Real getBailout() {
		return bailout;
	}

	public boolean isDistanceEstimateComputed() {
		return distanceEstimateComputed;
	}

	public boolean isContinuousDwellComputed() {
		return continuousDwellComputed;
	}

	public boolean isBinaryDecompositionComputed() {
		return binaryDecompositionComputed;
	}

	public int getDwellScaleLimit() {
		return dwellScaleLimit;
	}

	public double getFinalangWeight() {
		return finalangWeight;
	}

	public double getFinalradWeight() {
		return finalradWeight;
	}

	public double getAngleWeight() {
		return angleWeight;
	}

	public OptimizationDomain getOptimizationDomain() {
		return optimizationDomain;
	}

	@Override
	public int getNumOfSteps() {
		return super.getNumOfSteps();
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public void setBailout(Real bailout) {
		this.bailout = bailout;
	}

	public void setDistanceEstimateComputed(boolean distanceEstimateComputed) {
		this.distanceEstimateComputed = distanceEstimateComputed;
	}

	public void setContinuousDwellComputed(boolean continuousDwellComputed) {
		this.continuousDwellComputed = continuousDwellComputed;
	}

	public void setBinaryDecompositionComputed(boolean binaryDecompositionComputed) {
		this.binaryDecompositionComputed = binaryDecompositionComputed;
	}

	@Previewable
	public void setDwellScaleLimit(int dwellScaleLimit) {
		this.dwellScaleLimit = dwellScaleLimit;
	}

	@Previewable
	public void setFinalangWeight(double finalangWeight) {
		this.finalangWeight = finalangWeight;
	}

	@Previewable
	public void setFinalradWeight(double finalradWeight) {
		this.finalradWeight = finalradWeight;
	}

	@Previewable
	public void setAngleWeight(double angleWeight) {
		this.angleWeight = angleWeight;
	}

	public void setOptimizationDomain(OptimizationDomain optimizationDomain) {
		this.optimizationDomain = optimizationDomain;
	}

	@Override
	public void setNumOfSteps(int numOfSteps) {
		super.setNumOfSteps(numOfSteps);
	}
}
