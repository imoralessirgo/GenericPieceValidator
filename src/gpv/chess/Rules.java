/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design.
 * The course was taken at Worcester Polytechnic Institute.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Copyright ©2016 Gary F. Pollice
 *******************************************************************************/

package gpv.chess;

import gpv.util.*;

@FunctionalInterface
interface Rule{
	boolean isValid(Coordinate from, Coordinate to, Board b, ChessPiece cp);
}

/**
 * Description
 * @version Apr 6, 2020
 */
public class Rules {
	
		/**
		 * WHITE PAWN SPECIFIC
		 */
		static Rule moveForward1SpaceWhite = (from, to, b, cp) ->  from.distanceToXY(to) == new int[] {1,0}; 
		static Rule moveForward2SpaceWhite = (from, to, b, cp) -> from.distanceToXY(to) == new int[] {2,0} && !cp.hasMoved();
		static Rule moveToAttackWhitePawn = (from, to, b, cp) -> singleSquareDiagonalWhitePawn(to, from) && b.getPieceAt(to) == null ;

		/**
		 * BLACK PAWN SPECIFIC
		 */
		static Rule moveForward1SpaceBlack = (from, to, b, cp) ->  from.distanceToXY(to) == new int[] {-1,0} && b.getPieceAt(to) == null; 
		static Rule moveForward2SpaceBlack = (from, to, b, cp) -> from.distanceToXY(to) == new int[] {-2,0} && !cp.hasMoved() && b.getPieceAt(to) == null;		
		static Rule moveToAttackBlackPawn = (from, to, b, cp) -> singleSquareDiagonalBlackPawn(to, from) && b.getPieceAt(to) == null ;

	
		// Lambda helper methods
		private static boolean singleSquareDiagonalWhitePawn(Coordinate to, Coordinate from) {
			int[] result = from.distanceToXY(to);
			if(result[0] == 1 && Math.abs(result[1]) == 1) {
				return true;
			}
			return false;
		}
		
		private static boolean singleSquareDiagonalBlackPawn(Coordinate to, Coordinate from) {
			int[] result = from.distanceToXY(to);
			if(result[0] == -1 && Math.abs(result[1]) == 1) {
				return true;
			}
			return false;
		}
		

}
