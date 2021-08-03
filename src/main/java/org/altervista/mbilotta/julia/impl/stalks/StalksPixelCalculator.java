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

import org.altervista.mbilotta.julia.IntermediateImage;
import org.altervista.mbilotta.julia.impl.FloatImage;
import org.altervista.mbilotta.julia.impl.PixelCalculator;


public class StalksPixelCalculator implements PixelCalculator {
	
	private final boolean orbitInsideOnly;
	private final Color trapColor;
	private final float[] trapColorRgba;
	private final Color background;
	
	private final float[] rgb;

	public StalksPixelCalculator(Stalks representation) {
		orbitInsideOnly = representation.isOrbitInsideOnly();
		trapColor = representation.getTrapColor();
		background = representation.getBackground();

		trapColorRgba = representation.getTrapColor().getRGBComponents(null);
		rgb = new float[3];
	}
	
	private static boolean isTranslucent(Color c) {
		return c.getAlpha() > 0 && c.getAlpha() < 255;
	}

	private Color multiply(float value) {
		rgb[0] = trapColorRgba[0] * value;
		rgb[1] = trapColorRgba[1] * value;
		rgb[2] = trapColorRgba[2] * value;

		return new Color(Math.min(rgb[0], 1), Math.min(rgb[1], 1), Math.min(rgb[2], 1), trapColorRgba[3]);
	}

	@Override
	public int getTransparency() {
		if (isTranslucent(trapColor) || (orbitInsideOnly && isTranslucent(background))) {
			return TRANSLUCENT;
		}
		if (trapColor.getAlpha() == 255 && (!orbitInsideOnly || background.getAlpha() == 255)) {
			return OPAQUE;
		}
		return BITMASK;
	}

	@Override
	public int computePixel(int x, int y, IntermediateImage iimg) {
		float value = ((FloatImage) iimg).getPoint(x, y);
		if (value == -1) {
			return background.getRGB();
		}
		return multiply(value).getRGB();
	}
}
