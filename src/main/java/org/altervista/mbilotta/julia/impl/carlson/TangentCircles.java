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

import java.awt.Color;

import org.altervista.mbilotta.julia.Author;
import org.altervista.mbilotta.julia.Decimal;
import org.altervista.mbilotta.julia.Gradient;
import org.altervista.mbilotta.julia.IntermediateImage;
import org.altervista.mbilotta.julia.NumberFactory;
import org.altervista.mbilotta.julia.Previewable;
import org.altervista.mbilotta.julia.Progress;
import org.altervista.mbilotta.julia.impl.AbstractSimpleRepresentation;
import org.altervista.mbilotta.julia.impl.IntegerImage;
import org.altervista.mbilotta.julia.impl.PixelCalculator;
import org.altervista.mbilotta.julia.impl.PointCalculator;
import org.altervista.mbilotta.julia.impl.RasterImage;
import org.altervista.mbilotta.julia.math.CoordinateTransform;
import org.altervista.mbilotta.julia.math.Real;


@Author(name = "Maurizio Bilotta", contact = "mailto:maurizeio@yahoo.it")
@Author(name = "Paul W. Carlson", contact = "mailto:carlpaulw@aol.com")
public class TangentCircles extends AbstractSimpleRepresentation {

	private int maxIterations;
	private Real rc;
	private Color untrappedOutsidePoint;
	private Color untrappedInsidePoint;
	private int paletteSize;
	private int paletteOffset;
	private Gradient trappedPoint0;
	private Gradient trappedPoint1;
	private Gradient trappedPoint2;
	private Gradient trappedPoint3;
	private Gradient trappedPoint4;
	private Gradient trappedPoint5;
	private Gradient trappedPoint6;
	private Gradient trappedPoint7;

	public TangentCircles() {
		super(5);
		maxIterations = 500;
		rc = new Decimal("0.2");
		untrappedOutsidePoint = new Color(0, 0, 0);
		untrappedInsidePoint = new Color(255, 255, 255);
		paletteSize = 100;
		paletteOffset = 0;
		trappedPoint0 = new Gradient(255,   0, 174,  96,   0,  32);
		trappedPoint1 = new Gradient(255,  32,  32,  96,   0,   0);
		trappedPoint2 = new Gradient(255, 130,   0, 162,  16,   0);
		trappedPoint3 = new Gradient(255, 255,   0, 154,  64,   0);
		trappedPoint4 = new Gradient(  0, 255, 108,   0,  48,  16);
		trappedPoint5 = new Gradient(  0, 255, 255,   0,  48,  48);
		trappedPoint6 = new Gradient( 64,  64, 255,   0,   0,  96);
		trappedPoint7 = new Gradient(174,  96, 255,  72,   0,  80);
	}

	@Override
	protected RasterImage createIntermediateImage(int width, int height, Progress[] progress) {
		return new IntegerImage(width, height, progress);
	}

	@Override
	protected PointCalculator createPointCalculator(IntermediateImage iimg, NumberFactory numberFactory, CoordinateTransform coordinateTransform, boolean isJuliaSet) {
		IntegerImage integerImage = (IntegerImage) iimg;
		return isJuliaSet ?
				new TangentCirclesCalculator.JuliaSet(integerImage, this, numberFactory) :
				new TangentCirclesCalculator.MandelbrotSet(integerImage, this, numberFactory);
	}

	@Override
	protected PixelCalculator createPixelCalculator() {
		return new CarlsonPixelCalculator(paletteSize, paletteOffset, untrappedOutsidePoint, untrappedInsidePoint,
				trappedPoint0, trappedPoint1, trappedPoint2, trappedPoint3, trappedPoint4, trappedPoint5, trappedPoint6, trappedPoint7);
	}

	@Override
	protected PixelCalculator recyclePixelCalculator(PixelCalculator pixelCalculator) {
		if (pixelCalculator instanceof CarlsonPixelCalculator) {
			CarlsonPixelCalculator recyclable = (CarlsonPixelCalculator) pixelCalculator;
			if (recyclable.getNumOfPalettes() == 8 && recyclable.getPaletteSize() == paletteSize &&
					recyclable.getPalette(0).getGradient().equals(trappedPoint0) &&
					recyclable.getPalette(1).getGradient().equals(trappedPoint1) &&
					recyclable.getPalette(2).getGradient().equals(trappedPoint2) &&
					recyclable.getPalette(3).getGradient().equals(trappedPoint3) &&
					recyclable.getPalette(4).getGradient().equals(trappedPoint4) &&
					recyclable.getPalette(5).getGradient().equals(trappedPoint5) &&
					recyclable.getPalette(6).getGradient().equals(trappedPoint6) &&
					recyclable.getPalette(7).getGradient().equals(trappedPoint7))
				return new CarlsonPixelCalculator(paletteSize, paletteOffset, untrappedOutsidePoint, untrappedInsidePoint, recyclable);
		}
		
		return super.recyclePixelCalculator(pixelCalculator);
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public Real getRc() {
		return rc;
	}

	public Color getUntrappedOutsidePoint() {
		return untrappedOutsidePoint;
	}

	public Color getUntrappedInsidePoint() {
		return untrappedInsidePoint;
	}

	public int getPaletteSize() {
		return paletteSize;
	}

	public int getPaletteOffset() {
		return paletteOffset;
	}

	public Gradient getTrappedPoint0() {
		return trappedPoint0;
	}

	public Gradient getTrappedPoint1() {
		return trappedPoint1;
	}

	public Gradient getTrappedPoint2() {
		return trappedPoint2;
	}

	public Gradient getTrappedPoint3() {
		return trappedPoint3;
	}

	public Gradient getTrappedPoint4() {
		return trappedPoint4;
	}

	public Gradient getTrappedPoint5() {
		return trappedPoint5;
	}

	public Gradient getTrappedPoint6() {
		return trappedPoint6;
	}

	public Gradient getTrappedPoint7() {
		return trappedPoint7;
	}

	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	public void setRc(Real rc) {
		this.rc = rc;
	}

	@Previewable
	public void setUntrappedOutsidePoint(Color untrappedOutsidePoint) {
		this.untrappedOutsidePoint = untrappedOutsidePoint;
	}

	@Previewable
	public void setUntrappedInsidePoint(Color untrappedInsidePoint) {
		this.untrappedInsidePoint = untrappedInsidePoint;
	}

	public void setPaletteSize(int paletteSize) {
		this.paletteSize = paletteSize;
	}

	@Previewable
	public void setPaletteOffset(int paletteOffset) {
		this.paletteOffset = paletteOffset;
	}

	@Previewable
	public void setTrappedPoint0(Gradient trappedPoint0) {
		this.trappedPoint0 = trappedPoint0;
	}

	@Previewable
	public void setTrappedPoint1(Gradient trappedPoint1) {
		this.trappedPoint1 = trappedPoint1;
	}

	@Previewable
	public void setTrappedPoint2(Gradient trappedPoint2) {
		this.trappedPoint2 = trappedPoint2;
	}

	@Previewable
	public void setTrappedPoint3(Gradient trappedPoint3) {
		this.trappedPoint3 = trappedPoint3;
	}

	@Previewable
	public void setTrappedPoint4(Gradient trappedPoint4) {
		this.trappedPoint4 = trappedPoint4;
	}

	@Previewable
	public void setTrappedPoint5(Gradient trappedPoint5) {
		this.trappedPoint5 = trappedPoint5;
	}

	@Previewable
	public void setTrappedPoint6(Gradient trappedPoint6) {
		this.trappedPoint6 = trappedPoint6;
	}

	@Previewable
	public void setTrappedPoint7(Gradient trappedPoint7) {
		this.trappedPoint7 = trappedPoint7;
	}
}
