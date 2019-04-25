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

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;

import org.altervista.mbilotta.julia.*;
import org.altervista.mbilotta.julia.impl.AbstractRasterRepresentation;
import org.altervista.mbilotta.julia.impl.IntegerImage;
import org.altervista.mbilotta.julia.impl.OptimizationDomain;
import org.altervista.mbilotta.julia.impl.PixelCalculator;
import org.altervista.mbilotta.julia.impl.PointCalculator;
import org.altervista.mbilotta.julia.impl.ProgressiveRefinement;
import org.altervista.mbilotta.julia.impl.ProgressivelyRefinedImage;
import org.altervista.mbilotta.julia.impl.RasterScan;
import org.altervista.mbilotta.julia.math.CoordinateTransform;


@Author(name = "Maurizio Bilotta", contact = "mailto:maurizeio@yahoo.it")
public class EscapeTime extends AbstractRasterRepresentation {

	private int maxIterations;
	private Gradient gradient;
	private int paletteSize;
	private int paletteOffset;
	private Color insideColor;
	private OptimizationDomain optimizationDomain;

	public EscapeTime() {
		maxIterations = 500;
		gradient = new Gradient(
				new Gradient.Stop(   0f, 255,   0,   0),
				new Gradient.Stop(0.07f, 255, 255,   0),
				new Gradient.Stop(0.49f,   0, 255,   0),
				new Gradient.Stop(0.57f,   0,   0, 255),
				new Gradient.Stop(   1f, 255,   0,   0));
		paletteSize = 500;
		paletteOffset = 0;
		insideColor = new Color(0, 0, 0);
		setNumOfSteps(5);
		optimizationDomain = OptimizationDomain.BORDERS_EXCLUDED;
	}

	public EscapeTime(int maxIterations, Gradient gradient, int paletteSize, int paletteOffset, Color insideColor,
			int numOfSteps,
			OptimizationDomain optimizationDomain) {
		super(numOfSteps);
		this.maxIterations = maxIterations;
		this.gradient = gradient;
		this.paletteSize = paletteSize;
		this.paletteOffset = paletteOffset;
		this.insideColor = insideColor;

		this.optimizationDomain = optimizationDomain;
	}

	public IntermediateImage createIntermediateImage(int width, int height, int numOfProducers) {
		return getNumOfSteps() > 1 ?
				new ProgressivelyRefinedImage(width, height, numOfProducers, getNumOfSteps()) :
				new IntegerImage(width,	height, RasterScan.createInitialProgress(width, height, numOfProducers));
	}

	@Override
	protected IntermediateImage readIntermediateImageImpl(int width, int height, int numOfProducers, ObjectInputStream in)
			throws ClassNotFoundException, IOException {
		if (getNumOfSteps() > 1) {
			ProgressivelyRefinedImage rv = new ProgressivelyRefinedImage(width, height, ProgressiveRefinement.readProgress(width, height, numOfProducers, getNumOfSteps(), in));
			rv.offerMinIterations(in.readInt());
			ProgressiveRefinement.readPoints(in, rv);
			return rv;
		}

		IntegerImage rv = new IntegerImage(width, height, RasterScan.readProgress(width, height, numOfProducers, in));
		RasterScan.readPoints(in, rv);
		return rv;
	}

	@Override
	protected PointCalculator createPointCalculator(IntermediateImage iimg, NumberFactory numberFactory, CoordinateTransform coordinateTransform, boolean isJuliaSet) {
		return isJuliaSet ?
				(getNumOfSteps() > 1 ?
					new OptimizedPointCalculator.JuliaSet((ProgressivelyRefinedImage) iimg, this) :
					new StandardPointCalculator.JuliaSet((IntegerImage) iimg, this)) :
				(getNumOfSteps() > 1 ?
					new OptimizedPointCalculator.MandelbrotSet((ProgressivelyRefinedImage) iimg, this) :
					new StandardPointCalculator.MandelbrotSet((IntegerImage) iimg, this));
	}

	@Override
	protected PixelCalculator createPixelCalculator() {
		return new EscapeTimePixelCalculator(gradient, paletteSize, paletteOffset, insideColor);
	}

	@Override
	protected PixelCalculator recyclePixelCalculator(PixelCalculator recyclable) {
		if (recyclable instanceof EscapeTimePixelCalculator) {
			return new EscapeTimePixelCalculator(gradient, paletteSize, paletteOffset, insideColor, (EscapeTimePixelCalculator) recyclable);
		}
		return super.recyclePixelCalculator(recyclable);
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public Gradient getGradient() {
		return gradient;
	}

	public int getPaletteSize() {
		return paletteSize;
	}

	public int getPaletteOffset() {
		return paletteOffset;
	}

	public Color getInsideColor() {
		return insideColor;
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

	@Previewable
	public void setGradient(Gradient gradient) {
		this.gradient = gradient;
	}

	@Previewable
	public void setPaletteSize(int paletteSize) {
		this.paletteSize = paletteSize;
	}

	@Previewable
	public void setPaletteOffset(int paletteOffset) {
		this.paletteOffset = paletteOffset;
	}

	@Previewable
	public void setInsideColor(Color insideColor) {
		this.insideColor = insideColor;
	}

	public void setOptimizationDomain(OptimizationDomain optimizationDomain) {
		this.optimizationDomain = optimizationDomain;
	}

	@Override
	public void setNumOfSteps(int numOfSteps) {
		super.setNumOfSteps(numOfSteps);
	}

	public String toString() {
		return getClass().getCanonicalName() +
			"[maxIterations=" + maxIterations +
			", gradient=" + gradient +
			", paletteSize=" + paletteSize +
			", paletteOffset=" + paletteOffset +
			", insideColor=" + insideColor +
			", numOfSteps=" + getNumOfSteps() +
			", optimizationPolicy=" + optimizationDomain +
			']';
	}
}
