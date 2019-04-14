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

import java.awt.Color;
import java.awt.Transparency;

import org.altervista.mbilotta.julia.IntermediateImage;
import org.altervista.mbilotta.julia.impl.PixelCalculator;


public class MuEncyPixelCalculator implements PixelCalculator {

	final int dwellScaleLimit;
	final float finalradWeight;
	final float finalangWeight;
	final float angleWeight;

	public MuEncyPixelCalculator(MuEncyRepresentation repr) {
		dwellScaleLimit = repr.getDwellScaleLimit();
		finalradWeight = (float) repr.getFinalradWeight();
		finalangWeight = (float) repr.getFinalangWeight();
		angleWeight = (float) repr.getAngleWeight();
	}

	@Override
	public int getTransparency() {
		return Transparency.OPAQUE;
	}

	@Override
	public int computePixel(int x, int y, IntermediateImage iimg) {
		MuEncyImage image2 = (MuEncyImage) iimg;
		MuEncyImage.Point ipoint = image2.new Point(x, y);
		int dwell = Math.abs(ipoint.getDwell());
		if (dwell == Integer.MIN_VALUE) {
			return Color.WHITE.getRGB();
		}

		float value = ipoint.getValue(), angle, radius, hue, saturation;
		float p = (float) (Math.log(dwell) / Math.log(dwellScaleLimit));
		if (p < 0.5f) {
			p = 1 - 1.5f * p;
			angle = 1 - p;
		} else {
			p = 1.5f * p - 0.5f;
			angle = p;
		}
		radius = (float) Math.sqrt(p);

		if (dwell % 2 != 0) {
			value = 0.85f * value;
			radius = 0.667f * radius;
		}

		angle = angle * angleWeight + finalradWeight * Math.abs(ipoint.getFinalrad());
		if (ipoint.getFinalang()) {
			angle = angle + finalangWeight;
		}

		hue = angle - ((float) Math.floor(angle));
		saturation = radius - ((float) Math.floor(radius));
		int rgb = Color.HSBtoRGB(hue, saturation, value);
		return rgb;
	}

}
