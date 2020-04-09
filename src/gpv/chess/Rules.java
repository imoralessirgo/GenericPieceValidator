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
import static gpv.util.Coordinate.makeCoordinate;

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
			LinkedList<Rule> knightRules = new LinkedList<Rule>();
			Collections.addAll(knightRules, validLShape);
			LinkedList<Rule> bishopRules = new LinkedList<Rule>();
			Collections.addAll(bishopRules, validDiagonalLine);
			LinkedList<Rule> rookRules = new LinkedList<Rule>();
			Collections.addAll(rookRules, validStraightLine);
			LinkedList<Rule> queenRules = new LinkedList<Rule>();
			Collections.addAll(queenRules, validStraightLine, validDiagonalLine);
			LinkedList<Rule> kingRules = new LinkedList<Rule>();
			Collections.addAll(kingRules, validStraightLine, validDiagonalLine);
			
			
			
			mapOfRules.put(ChessPieceDescriptor.WHITEPAWN, pawnRules);
			mapOfRules.put(ChessPieceDescriptor.BLACKPAWN, pawnRules);
			mapOfRules.put(ChessPieceDescriptor.WHITEKNIGHT, knightRules);
			mapOfRules.put(ChessPieceDescriptor.BLACKKNIGHT, knightRules);
			mapOfRules.put(ChessPieceDescriptor.WHITEBISHOP, bishopRules);
			mapOfRules.put(ChessPieceDescriptor.BLACKBISHOP, bishopRules);
			mapOfRules.put(ChessPieceDescriptor.WHITEROOK, rookRules);
			mapOfRules.put(ChessPieceDescriptor.BLACKROOK, rookRules);
			mapOfRules.put(ChessPieceDescriptor.WHITEQUEEN, queenRules);
			mapOfRules.put(ChessPieceDescriptor.BLACKQUEEN, queenRules);
			mapOfRules.put(ChessPieceDescriptor.WHITEKING, kingRules);
			mapOfRules.put(ChessPieceDescriptor.BLACKKING, kingRules);
		}
	
		public HashMap<ChessPieceDescriptor, LinkedList<Rule>> getRules(){
			return this.mapOfRules;
		}
	
		private final HashMap<ChessPieceDescriptor, LinkedList<Rule>> mapOfRules;
		
		
		/**
		 * PAWN SPECIFIC
		 */
		static Rule moveForward1Space = (from, to, b, cp) ->  from.changeInX(to, cp) == 1 && from.changeInY(to, cp) == 0; 
		static Rule moveForward2Space = (from, to, b, cp) -> from.changeInX(to, cp) == 2 && from.changeInY(to, cp) == 0 
																								&& !cp.hasMoved() && !isPieceInMyWayStraight(to, from, b);
		static Rule moveToAttackPawn = (from, to, b, cp) -> singleSquareDiagonalPawn(to, from, cp)
																&& b.getPieceAt(to) != null && isEnemyPiece(cp, to, b) ;
		
		/**
		 * KNIGHT SPECIFIC  
		 */
		static Rule validLShape = (from, to, b, cp) -> ((Math.abs(from.changeInX(to, cp)) == 2 && from.changeInY(to, cp) == 1)
																						|| (Math.abs(from.changeInX(to, cp)) == 1 && from.changeInY(to, cp) == 2) 
																							&& (b.getPieceAt(to) == null || isEnemyPiece(cp, to, b))  );
		
		
		/**
		 * MULTIPURPOSE RULES
		 */
		static Rule validDiagonalLine = (from, to, b, cp) -> (b.getPieceAt(to) == null || isEnemyPiece(cp, to, b)) && !isPieceInMyWayDiagonal(to,from,b);
		static Rule validStraightLine = (from, to, b, cp) -> (b.getPieceAt(to) == null || isEnemyPiece(cp, to, b)) && !isPieceInMyWayStraight(to,from,b);
		
		
		
		
	
		// Lambda helper methods
		private static boolean singleSquareDiagonalPawn(Coordinate to, Coordinate from, ChessPiece cp) {
			return from.changeInX(to, cp) == 1 && from.changeInY(to, cp) == 1;
		}
		
		private static boolean isEnemyPiece(ChessPiece cp, Coordinate to, Board b) {
			ChessPiece pieceAtDest = (ChessPiece) b.getPieceAt(to);
			return pieceAtDest.getColor() != cp.getColor();
		}
		
		private static boolean isPieceInMyWayStraight(Coordinate to, Coordinate from, Board b) {
			int[] distance = from.distanceToXY(to);
			if(distance[0] == 0 || distance[1] == 0) { // straigt line
				
				if(distance[0] > 0){return checkNorth(from, to, b);} // North
				else if(distance[0] < 0){return checkSouth(from, to, b);} // South
				else if(distance[1] > 0){return checkEast(from, to, b);} // East
				else if(distance[1] < 0){return checkWest(from, to, b);} // West
			}
			return true; // not a straight line
		}
		
		
		private static boolean isPieceInMyWayDiagonal(Coordinate to, Coordinate from, Board b) {
			int[] distance = from.distanceToXY(to);
			if(Math.abs(distance[0]) == Math.abs(distance[1])) { // valid diagonal
				if(distance[0] > 0 && distance[1] > 0){return checkNorthEast(from, to, b);} // NorthEast
				else if(distance[0] > 0 && distance[1] < 0){return checkNorthWest(from, to, b);} // NorthWest
				else if(distance[0] < 0 && distance[1] > 0){return checkSouthEast(from, to, b);} // SouthEast
				else if(distance[0] < 0 && distance[1] < 0){return checkSouthWest(from, to, b);} // SouthWest
			}
			return true; // found invalid diagonal
		}
		
		private static boolean checkNorth(Coordinate from, Coordinate to, Board b) {
			for(int i = from.getRow() + 1; i < to.getRow() - 1; i++) {
				if(b.getPieceAt(makeCoordinate(i,from.getColumn())) != null) {return true;}
			}
			return false;
		}
		
		private static boolean checkSouth(Coordinate from, Coordinate to, Board b) {
			System.out.println("here");
			for(int i = from.getRow() - 1; i > to.getRow() + 1; i--) {
				if(b.getPieceAt(makeCoordinate(i,from.getColumn())) != null) {return true;}
			}
			return false;
		}		
		
		private static boolean checkEast(Coordinate from, Coordinate to, Board b) {
			for(int i = from.getColumn() + 1; i < to.getColumn() - 1; i++) {
				if(b.getPieceAt(makeCoordinate(from.getRow(),i)) != null) {return true;}
			}
			return false;
		}
		
		private static boolean checkWest(Coordinate from, Coordinate to, Board b) {
			for(int i = from.getColumn() - 1; i > to.getColumn() + 1; i--) {
				if(b.getPieceAt(makeCoordinate(from.getRow(),i)) != null) {return true;}
			}
			return false;
		}
		
		private static boolean checkNorthEast(Coordinate from, Coordinate to, Board b) {
			for(int i = from.getRow() + 1, j = from.getColumn() + 1;
					i < to.getRow() - 1 && j < to.getColumn() - 1; i++, j++) {
				if(b.getPieceAt(makeCoordinate(i,j)) != null) {return true;}
			}
			return false;
		}
		private static boolean checkNorthWest(Coordinate from, Coordinate to, Board b) {
			for(int i = from.getRow() + 1, j = from.getColumn() - 1;
					i < to.getRow() - 1 && j > to.getColumn() + 1; i++, j--) {
				if(b.getPieceAt(makeCoordinate(i,j)) != null) {return true;}
			}
			return false;
		}
		private static boolean checkSouthEast(Coordinate from, Coordinate to, Board b) {
			for(int i = from.getRow() - 1, j = from.getColumn() + 1;
					i > to.getRow() + 1 && j < to.getColumn() - 1; i--, j++) {
				if(b.getPieceAt(makeCoordinate(i,j)) != null) {return true;}
			}
			return false;
		}
		private static boolean checkSouthWest(Coordinate from, Coordinate to, Board b) {
			for(int i = from.getRow() - 1, j = from.getColumn() - 1;
					i > to.getRow() + 1 && j > to.getColumn() + 1; i--, j--) {
				if(b.getPieceAt(makeCoordinate(i,j)) != null) {return true;}
			}
			return false;
		}
}
