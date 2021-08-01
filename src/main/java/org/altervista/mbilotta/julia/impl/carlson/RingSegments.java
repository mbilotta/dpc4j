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
import org.altervista.mbilotta.julia.program.parsers.IntParameter;
import org.altervista.mbilotta.julia.program.parsers.RealParameter;


@Author(name = "Maurizio Bilotta", contact = "mailto:maurizeio@gmail.com")
@Author(name = "Paul W. Carlson", contact = "mailto:carlpaulw@aol.com")
public class RingSegments extends AbstractSimpleRepresentation {

	private int maxIterations;
	private Real rm;
	private Real t;
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

	public RingSegments() {
		super(5);
		maxIterations = 500;
		rm = new Decimal("0.5");
		t = new Decimal("0.3");
		untrappedOutsidePoint = Color.BLACK;
		untrappedInsidePoint = Color.WHITE;
		paletteSize = 100;
		paletteOffset = 0;
		
		Gradient gold = new Gradient(255, 255, 0, 154, 64, 0);
		Gradient silver = new Gradient(255, 255, 255, 128, 128, 128);
		trappedPoint0 = gold;
		trappedPoint1 = silver;
		trappedPoint2 = gold;
		trappedPoint3 = silver;
		trappedPoint4 = gold;
		trappedPoint5 = silver;
		trappedPoint6 = gold;
		trappedPoint7 = silver;
	}

	@Override
	protected RasterImage createIntermediateImage(int width, int height, Progress[] progress) {
		return new IntegerImage(width, height, progress);
	}

	@Override
	protected PointCalculator createPointCalculator(IntermediateImage iimg,
			NumberFactory numberFactory,
			CoordinateTransform coordinateTransform, boolean isJuliaSet) {
		IntegerImage integerImage = (IntegerImage) iimg;
		return isJuliaSet ?
				new RingSegmentsCalculator.JuliaSet(integerImage, this, numberFactory) :
				new RingSegmentsCalculator.MandelbrotSet(integerImage, this, numberFactory);
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

	public Real getRm() {
		return rm;
	}

	public Real getT() {
		return t;
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

	@IntParameter.Min(1)
	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	@RealParameter.Min(value = "0", inclusive = false)
	public void setRm(Real rm) {
		this.rm = rm;
	}

	@RealParameter.Min(value = "0", inclusive = false)
	public void setT(Real t) {
		this.t = t;
	}

	@Previewable
	public void setUntrappedOutsidePoint(Color untrappedOutsidePoint) {
		this.untrappedOutsidePoint = untrappedOutsidePoint;
	}

	@Previewable
	public void setUntrappedInsidePoint(Color untrappedInsidePoint) {
		this.untrappedInsidePoint = untrappedInsidePoint;
	}

	@IntParameter.Min(1)
	@IntParameter.Max(268435455)
	public void setPaletteSize(int paletteSize) {
		this.paletteSize = paletteSize;
	}

	@Previewable
	@IntParameter.Min(0)
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
