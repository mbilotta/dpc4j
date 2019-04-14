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

import org.altervista.mbilotta.julia.Gradient;
import org.altervista.mbilotta.julia.IntermediateImage;
import org.altervista.mbilotta.julia.Palette;
import org.altervista.mbilotta.julia.impl.IntegerImage;
import org.altervista.mbilotta.julia.impl.PixelCalculator;


public class EscapeTimePixelCalculator implements PixelCalculator {

	private final Palette palette;
	private final int paletteOffset;
	private final Color insideColor;
	private final int transparency;

	public EscapeTimePixelCalculator(Gradient gradient, int paletteSize, int paletteOffset, Color insideColor) {
		this.palette = new Palette(paletteSize, gradient);
		this.paletteOffset = paletteOffset;
		this.insideColor = insideColor;
		this.transparency = computeTransparency();
	}

	public EscapeTimePixelCalculator(Gradient gradient, int paletteSize, int paletteOffset, Color insideColor,
			EscapeTimePixelCalculator recyclable) {
		this.palette = recyclable.getGradient().equals(gradient) && recyclable.getPaletteSize() == paletteSize ?
				recyclable.palette :
				new Palette(paletteSize, gradient);
		this.paletteOffset = paletteOffset;
		this.insideColor = insideColor;
		this.transparency = computeTransparency();
	}

	public int computePixel(int x, int y, IntermediateImage iimg) {
		int index = ((IntegerImage) iimg).getPoint(x, y);
		if (index < 0)
			return insideColor.getRGB();

		return palette.getColorAt(index, paletteOffset);
	}

	public Gradient getGradient() {
		return palette.getGradient();
	}

	public int getPaletteSize() {
		return palette.getSize();
	}

	public int getPaletteOffset() {
		return paletteOffset;
	}

	public Color getInsideColor() {
		return insideColor;
	}

	public int getTransparency() {
		return transparency;
	}

	private int computeTransparency() {
		switch (getGradient().getTransparency()) {
		case OPAQUE: switch (insideColor.getAlpha()) {
			case 0: return BITMASK;
			case 255: return OPAQUE;
			default: return TRANSLUCENT;
		}
		case BITMASK: switch (insideColor.getAlpha()) {
			case 0:
			case 255: return BITMASK;
			default: return TRANSLUCENT;
		}
		case TRANSLUCENT: return TRANSLUCENT;
		default: throw new AssertionError(getGradient().getTransparency());
		}
	}
}
