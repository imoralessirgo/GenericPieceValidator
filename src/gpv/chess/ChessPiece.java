/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package gpv.chess;

import java.util.*;
import gpv.Piece;
import gpv.util.*;

/**
 * The chess piece is a piece with some special properties that are used for determining
 * whether a piece can move. It implements the Piece interface and adds properties and
 * methods that are necessary for the chess-specific behavior.
 * 
 * @version Feb 21, 2020
 */
public class ChessPiece implements Piece<ChessPieceDescriptor> {
	private final ChessPieceDescriptor descriptor;
	private boolean hasMoved; // true if this piece has moved

	/**
	 * The only constructor for a ChessPiece instance. Requires a descriptor.
	 * 
	 * @param descriptor
	 */
	public ChessPiece(ChessPieceDescriptor descriptor) {
		this.descriptor = descriptor;
		hasMoved = false;
	}

	/*
	 * @see gpv.Piece#getDescriptor()
	 */
	@Override
	public ChessPieceDescriptor getDescriptor() {
		return descriptor;
	}

	/**
	 * @return the color
	 */
	public PlayerColor getColor() {
		return descriptor.getColor();
	}

	/**
	 * @return the name
	 */
	public PieceName getName() {
		return descriptor.getName();
	}

	/*
	 * @see gpv.Piece#canMove(gpv.util.Coordinate, gpv.util.Coordinate, gpv.util.Board)
	 */
	@Override
	public boolean canMove(Coordinate from, Coordinate to, Board b) {
		// no movement not valid
		if(Arrays.equals(from.distanceToXY(to), new int[] {0,0})) {return false;} 
		
		Rules rules = new Rules();
		HashMap<ChessPieceDescriptor, LinkedList<Rule>> mapOfRules = rules.getRules();
		LinkedList<Rule> listOfRules = mapOfRules.get(this.getDescriptor());
		for(Rule r : listOfRules) {
			if (r.isValid(from, to, b, this)) {return true;}
		}
		return false;
	}

	/**
	 * @return the hasMoved
	 */
	public boolean hasMoved() {
		return hasMoved;
	}

	/**
	 * Once it moves, you can't change it.
	 * 
	 * @param hasMoved
	 *            the hasMoved to set
	 */
	public void setHasMoved() {
		hasMoved = true;
	}
}
