/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package gpv.util;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import org.junit.jupiter.api.*;
import gpv.util.*;
import static gpv.util.Coordinate.makeCoordinate;

/**
 * Description
 * 
 * @version Mar 27, 2020
 */
class CoordinateTests {

	/**
	 * Description
	 * 
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	void setUp() throws Exception {
	}


	@Test
	void hasMovedHorizontalRight() {
		Coordinate from = makeCoordinate(1,1);
		Coordinate to = makeCoordinate(1,2);
		assertTrue(Arrays.equals(new int[] {0,1}, from.distanceToXY(to)));
	}
	
	@Test
	void hasMovedHorizontalLeft() {
		Coordinate from = makeCoordinate(1,8);
		Coordinate to = makeCoordinate(1,7);
		assertTrue(Arrays.equals(new int[] {0,-1}, from.distanceToXY(to)));
	}


	@Test
	void hasMovedVerticalWhite() {
		Coordinate from = makeCoordinate(1,1);
		Coordinate to = makeCoordinate(2,1);
		assertTrue(Arrays.equals(new int[] {1,0}, from.distanceToXY(to)));
	}
	
	@Test
	void hasMovedVerticalBlack() {
		Coordinate from = makeCoordinate(8,8);
		Coordinate to = makeCoordinate(7,8);
		assertTrue(Arrays.equals(new int[] {-1,0}, from.distanceToXY(to)));
	}

	@Test
	void hasMovedDiagonalNE() {
		Coordinate from = makeCoordinate(1,1);
		Coordinate to = makeCoordinate(2,2);
		assertTrue(Arrays.equals(new int[] {1,1}, from.distanceToXY(to)));
	}
	
	@Test
	void hasMovedDiagonalSW() {
		Coordinate from = makeCoordinate(8,8);
		Coordinate to = makeCoordinate(7,7);
		assertTrue(Arrays.equals(new int[] {-1,-1}, from.distanceToXY(to)));
	}
	
	@Test
	void hasMovedL() {
		Coordinate from = makeCoordinate(1,1);
		Coordinate to = makeCoordinate(3,2);
		assertTrue(Arrays.equals(new int[] {2,1}, from.distanceToXY(to)));
	}
}
