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
import java.io.ObjectOutputStream;

import org.altervista.mbilotta.julia.impl.IntegerImage;
import org.altervista.mbilotta.julia.impl.ProgressiveRefinement;
import org.altervista.mbilotta.julia.impl.ProgressivelyRefinedImage;
import org.altervista.mbilotta.julia.impl.RasterScan;


public class MuEncyImage extends ProgressivelyRefinedImage {
	
	public class Point extends IntegerImage.Point {

		public Point(int x, int y) {
			super(x, y);
		}

		public void setDwell(int dwell) {
			set(dwell);
		}

		public int getDwell() {
			return get();
		}

		public void setValue(float value) {
			values[index] = value;
		}

		public float getValue() {
			return values != null ? values[index] : 1f;
		}

		public void setFinalrad(float finalrad) {
			finalrads[index] = finalrad;
		}

		public float getFinalrad() {
			return finalrads != null ? finalrads[index] : 0f;
		}

		public void setFinalang(boolean finalang) {
			finalangs[index] = finalang;
		}

		public boolean getFinalang() {
			return finalangs != null ? finalangs[index] :
				(finalrads != null ? signBit(finalrads[index]) : false);
		}
	}

	private static boolean signBit(float f) {
		return Float.floatToRawIntBits(f) < 0;
	}

	private float[] values;
	private float[] finalrads;
	private boolean[] finalangs;

	public MuEncyImage(int width, int height, int numOfProducers, MuEncyRepresentation repr) {
		super(width, height, repr.getNumOfSteps() > 1 ?
				ProgressiveRefinement.createInitialProgress(width, height, numOfProducers, repr.getNumOfSteps()) :
				RasterScan.createInitialProgress(width, height, numOfProducers));

		if (repr.isDistanceEstimateComputed()) {
			values = new float[width * height];
		}

		if (repr.isContinuousDwellComputed()) {
			finalrads = new float[width * height];
		} else if (repr.isBinaryDecompositionComputed()) {
			finalangs = new boolean[width * height];
		}
	}

	public MuEncyImage(int width, int height, int numOfProducers, MuEncyRepresentation repr, ObjectInputStream in)
			throws ClassNotFoundException, IOException {
		super(width, height, repr.getNumOfSteps() > 1 ?
				ProgressiveRefinement.readProgress(width, height, numOfProducers, repr.getNumOfSteps(), in) :
				RasterScan.readProgress(width, height, numOfProducers, in));

		if (repr.isDistanceEstimateComputed()) {
			values = new float[width * height];
		}

		if (repr.isContinuousDwellComputed()) {
			finalrads = new float[width * height];
		} else if (repr.isBinaryDecompositionComputed()) {
			finalangs = new boolean[width * height];
		}

		if (repr.getNumOfSteps() > 1) {
			offerMinIterations(in.readInt());
			ProgressiveRefinement.readPoints(in, this);
		} else {
			RasterScan.readPoints(in, this);
		}
	}

	public boolean hasNegativeNeighbors(int x, int y, int chunkSize, boolean trueAtBorderPoints) {
		int leftX = x - chunkSize;
		int upY = y - chunkSize;
		int rightX = x + chunkSize;
		int downY = y + chunkSize;

		boolean borderPoint = false;
		if (leftX < 0) {
			leftX = x;
			borderPoint = true;
		}
		if (rightX >= width) {
			rightX = x;
			borderPoint = true;
		}
		if (upY < 0) {
			upY = y;
			borderPoint = true;
		}
		if (downY >= height) {
			downY = y;
			borderPoint = true;
		}

		return (borderPoint && trueAtBorderPoints)
				|| getPoint(leftX, upY) < 0
				|| getPoint(x, upY) < 0
				|| getPoint(rightX, upY) < 0
				|| getPoint(leftX, y) < 0
				|| getPoint(rightX, y) < 0
				|| getPoint(leftX, downY) < 0
				|| getPoint(x, downY) < 0
				|| getPoint(rightX, downY) < 0;
	}

	@Override
	public void readPoint(int x, int y, ObjectInputStream in)
			throws IOException {
		Point ipoint = new Point(x, y);
		ipoint.setDwell(in.readInt());
		if (values != null) {
			ipoint.setValue(in.readFloat());
		}
		if (finalrads != null) {
			ipoint.setFinalrad(in.readFloat());
		} else if (finalangs != null) {
			ipoint.setFinalang(in.readBoolean());
		}
	}

	@Override
	public void writePoint(int x, int y, ObjectOutputStream out)
			throws IOException {
		Point ipoint = new Point(x, y);
		out.writeInt(ipoint.getDwell());
		if (values != null) {
			out.writeFloat(ipoint.getValue());
		}
		if (finalrads != null) {
			out.writeFloat(ipoint.getFinalrad());
		} else if (finalangs != null) {
			out.writeBoolean(ipoint.getFinalang());
		}
	}
}
