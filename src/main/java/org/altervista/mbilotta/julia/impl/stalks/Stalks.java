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

import java.awt.Color;

import org.altervista.mbilotta.julia.Author;
import org.altervista.mbilotta.julia.Decimal;
import org.altervista.mbilotta.julia.IntermediateImage;
import org.altervista.mbilotta.julia.NumberFactory;
import org.altervista.mbilotta.julia.Previewable;
import org.altervista.mbilotta.julia.Progress;
import org.altervista.mbilotta.julia.impl.AbstractSimpleRepresentation;
import org.altervista.mbilotta.julia.impl.FloatImage;
import org.altervista.mbilotta.julia.impl.PixelCalculator;
import org.altervista.mbilotta.julia.impl.PointCalculator;
import org.altervista.mbilotta.julia.impl.RasterImage;
import org.altervista.mbilotta.julia.math.CoordinateTransform;
import org.altervista.mbilotta.julia.math.Real;
import org.altervista.mbilotta.julia.program.parsers.IntParameter;
import org.altervista.mbilotta.julia.program.parsers.RealParameter;


@Author(name = "Maurizio Bilotta", contact = "mailto:maurizeio@gmail.com")
@Author(name = "J. P. Botelho", contact = "https://twitter.com/noob_studios")
public class Stalks extends AbstractSimpleRepresentation {
	
	private int maxIterations;
	private int minIterations;
	private Color trapColor;
	private Color background;

	private Real orbitPositionX;
	private Real orbitPositionY;
	private Real orbitPositionZ;
	private Real orbitPositionW;
	
	private Real orbitDividend;
	
	private boolean orbitInsideOnly;
	private boolean showOrbitX;
	private boolean showOrbitY;
	
	public Stalks() {
		super(5);
		minIterations = 1;
		maxIterations = 100;
		trapColor = new Color(0f, .25f, 1f);
		background = new Color(1f, 1f, 1f);
		
		orbitPositionX = Decimal.ZERO;
		orbitPositionY = Decimal.ZERO;
		orbitPositionZ = Decimal.ZERO;
		orbitPositionW = Decimal.ZERO;

		orbitDividend = new Decimal("0.05");
		
		orbitInsideOnly = false;
		showOrbitX = true;
		showOrbitY = true;
	}

	@Override
	protected RasterImage createIntermediateImage(int width, int height, Progress[] progress) {
		return new FloatImage(width, height, progress);
	}

	@Override
	protected PointCalculator createPointCalculator(IntermediateImage iImg, NumberFactory numberFactory,
			CoordinateTransform coordinateTransform, boolean isJuliaSet) {
		FloatImage floatImg = (FloatImage) iImg;
		return isJuliaSet ?
				new StalksCalculator.JuliaSet(floatImg, this, numberFactory) :
				new StalksCalculator.MandelbrotSet(floatImg, this, numberFactory);
	}

	@Override
	protected PixelCalculator createPixelCalculator() {
		return new StalksPixelCalculator(this);
	}

	public int getMaxIterations() {
		return maxIterations;
	}

	public int getMinIterations() {
		return minIterations;
	}

	public Color getTrapColor() {
		return trapColor;
	}

	public Color getBackground() {
		return background;
	}

	public Real getOrbitPositionX() {
		return orbitPositionX;
	}

	public Real getOrbitPositionY() {
		return orbitPositionY;
	}

	public Real getOrbitPositionZ() {
		return orbitPositionZ;
	}

	public Real getOrbitPositionW() {
		return orbitPositionW;
	}

	public Real getOrbitDividend() {
		return orbitDividend;
	}

	public boolean isOrbitInsideOnly() {
		return orbitInsideOnly;
	}

	public boolean isShowOrbitX() {
		return showOrbitX;
	}

	public boolean isShowOrbitY() {
		return showOrbitY;
	}

	@IntParameter.Min(1)
	public void setMaxIterations(int maxIterations) {
		this.maxIterations = maxIterations;
	}

	@IntParameter.Min(0)
	public void setMinIterations(int minIterations) {
		this.minIterations = minIterations;
	}

	@Previewable
	public void setTrapColor(Color trapColor) {
		this.trapColor = trapColor;
	}

	@Previewable
	public void setBackground(Color background) {
		this.background = background;
	}

	public void setOrbitPositionX(Real orbitPositionX) {
		this.orbitPositionX = orbitPositionX;
	}

	public void setOrbitPositionY(Real orbitPositionY) {
		this.orbitPositionY = orbitPositionY;
	}

	@RealParameter.Min("0")
	public void setOrbitPositionZ(Real orbitPositionZ) {
		this.orbitPositionZ = orbitPositionZ;
	}

	@RealParameter.Min("0")
	public void setOrbitPositionW(Real orbitPositionW) {
		this.orbitPositionW = orbitPositionW;
	}

	@RealParameter.Min(value = "0", inclusive = false)
	public void setOrbitDividend(Real orbitDividend) {
		this.orbitDividend = orbitDividend;
	}

	public void setOrbitInsideOnly(boolean orbitInsideOnly) {
		this.orbitInsideOnly = orbitInsideOnly;
	}

	public void setShowOrbitX(boolean showOrbitX) {
		this.showOrbitX = showOrbitX;
	}

	public void setShowOrbitY(boolean showOrbitY) {
		this.showOrbitY = showOrbitY;
	}
}
