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
import java.awt.Transparency;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.altervista.mbilotta.julia.Gradient;
import org.altervista.mbilotta.julia.IntermediateImage;
import org.altervista.mbilotta.julia.Palette;
import org.altervista.mbilotta.julia.impl.IntegerImage;
import org.altervista.mbilotta.julia.impl.PixelCalculator;


public class CarlsonPixelCalculator implements PixelCalculator {

	public static final int UNTRAPPED_OUTSIDE_POINT = -1;
	public static final int UNTRAPPED_INSIDE_POINT = -2;

	private final Palette[] palettes;
	private final int paletteOffset;
	private final Color untrappedOutsideColor;
	private final Color untrappedInsideColor;
	private final int transparency;

	public CarlsonPixelCalculator(int paletteSize, int paletteOffset,
			Color untrappedOutsidePoint,
			Color untrappedInsidePoint, Gradient... gradients) {
		this.palettes = Palette.createPalettes(paletteSize, gradients);
		this.paletteOffset = paletteOffset;
		this.untrappedOutsideColor = untrappedOutsidePoint;
		this.untrappedInsideColor = untrappedInsidePoint;
		this.transparency = computeTransparency();
	}

	public CarlsonPixelCalculator(int paletteSize, int paletteOffset,
			Color untrappedOutsidePoint,
			Color untrappedInsidePoint, CarlsonPixelCalculator recyclable) {
		this.palettes = recyclable.palettes;
		this.paletteOffset = paletteOffset;
		this.untrappedOutsideColor = untrappedOutsidePoint;
		this.untrappedInsideColor = untrappedInsidePoint;
		this.transparency = computeTransparency();
	}

	public int computePixel(int x, int y, IntermediateImage iimg) {
		int value = ((IntegerImage) iimg).getPoint(x, y);
		if (value == UNTRAPPED_OUTSIDE_POINT)
			return untrappedOutsideColor.getRGB();
		if (value == UNTRAPPED_INSIDE_POINT)
			return untrappedInsideColor.getRGB();

		return palettes[value % palettes.length].getColorAt(value / palettes.length, paletteOffset);
	}

	public int getTransparency() {
		return transparency;
	}

	public Palette getPalette(int i) {
		return palettes[i];
	}

	public int getNumOfPalettes() {
		return palettes.length;
	}

	public int getPaletteSize() {
		return palettes[0].getSize();
	}

	public int getPaletteOffset() {
		return paletteOffset;
	}

	public Color getUntrappedOutsideColor() {
		return untrappedInsideColor;
	}

	public Color getUntrappedInsideColor() {
		return untrappedInsideColor;
	}

	private int computeTransparency() {
		List<Transparency> elements = new ArrayList<>(palettes.length + 2);
		elements.addAll(Arrays.asList(palettes));
		elements.add(untrappedInsideColor);
		elements.add(untrappedOutsideColor);
		int rv = OPAQUE;
		for (Transparency element : elements) {
			switch (element.getTransparency()) {
			case OPAQUE: break;
			case BITMASK: rv = BITMASK; break;
			case TRANSLUCENT: return TRANSLUCENT;
			default: throw new AssertionError(element.getTransparency());
			}
		}
		return rv;
	}
}
