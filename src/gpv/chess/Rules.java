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

import java.util.*;
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
		
		public Rules() {
			this.mapOfRules = new HashMap<ChessPieceDescriptor, LinkedList<Rule>>();
			
			LinkedList<Rule> pawnRules = new LinkedList<Rule>();
			Collections.addAll(pawnRules, moveForward1Space, moveForward2Space, moveToAttackPawn);
			
			mapOfRules.put(ChessPieceDescriptor.WHITEPAWN, pawnRules);
			mapOfRules.put(ChessPieceDescriptor.BLACKPAWN, pawnRules);
			
		}
	
		public HashMap<ChessPieceDescriptor, LinkedList<Rule>> getRules(){
			return this.mapOfRules;
		}
	
		private final HashMap<ChessPieceDescriptor, LinkedList<Rule>> mapOfRules;
		
		/**
		 * UNIVERSAL RULES
		 */
		
		
		/**
		 * PAWN SPECIFIC
		 */
		static Rule moveForward1Space = (from, to, b, cp) ->  from.changeInX(to, cp) == 1 && from.changeInY(to, cp) == 0; 
		static Rule moveForward2Space = (from, to, b, cp) -> from.changeInX(to, cp) == 2 && from.changeInY(to, cp) == 0 && !cp.hasMoved();
		static Rule moveToAttackPawn = (from, to, b, cp) -> singleSquareDiagonalPawn(to, from, cp)
																&& b.getPieceAt(to) != null && isEnemyPiece(cp, to, b) ;
		
		
		/**
		 * KNIGHT SPECIFIC  
		 */
		static Rule validLShape = (from, to, b, cp) -> (b.getPieceAt(to) != null || isEnemyPiece(cp, to, b)) 
																				&& ((from.changeInX(to, cp) == 2 && from.changeInY(to, cp) == 1)
																						|| (from.changeInX(to, cp) == 1 && from.changeInY(to, cp) == 2));
		
	
		// Lambda helper methods
		private static boolean singleSquareDiagonalPawn(Coordinate to, Coordinate from, ChessPiece cp) {
			return from.changeInX(to, cp) == 1 && from.changeInY(to, cp) == 1;
		}	
		private static boolean isEnemyPiece(ChessPiece cp, Coordinate to, Board b) {
			ChessPiece pieceAtDest = (ChessPiece) b.getPieceAt(to);
			return pieceAtDest.getColor() != cp.getColor();
		}
		private static boolean isPieceInMyWay(Coordinate to, Coordinate from, Board b) {
			return false;
		}
		

}
