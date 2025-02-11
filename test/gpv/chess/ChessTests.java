/*******************************************************************************
 * This files was developed for CS4233: Object-Oriented Analysis & Design. The course was
 * taken at Worcester Polytechnic Institute. All rights reserved. This program and the
 * accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Copyright ©2020 Gary F. Pollice
 *******************************************************************************/

package gpv.chess;

import static gpv.chess.ChessPieceDescriptor.*;
import static org.junit.Assert.*;
import java.util.stream.Stream;
import static gpv.util.Coordinate.makeCoordinate;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import gpv.util.*;

/**
 * Tests to ensure that pieces are created correctly and that all pieces get created.
 * 
 * @version Feb 23, 2020
 */
class ChessPieceTests {
	private static ChessPieceFactory factory = null;
	private Board board;

	@BeforeAll
	public static void setupBeforeTests() {
		factory = new ChessPieceFactory();
	}

	@BeforeEach
	public void setupTest() {
		board = new Board(8, 8);
	}

	@Test
	void makePiece() {
		ChessPiece pawn = factory.makePiece(WHITEPAWN);
		assertNotNull(pawn);
	}

	/**
	 * This type of test loops through each value in the Enum and one by one feeds it as
	 * an argument to the test method. It's worth looking at the different types of
	 * parameterized tests in JUnit:
	 * https://junit.org/junit5/docs/current/user-guide/#writing-tests-parameterized-tests
	 * 
	 * @param d
	 *            the Enum value
	 */
	@ParameterizedTest
	@EnumSource(ChessPieceDescriptor.class)
	void makeOneOfEach(ChessPieceDescriptor d) {
		ChessPiece p = factory.makePiece(d);
		assertNotNull(p);
		assertEquals(d.getColor(), p.getColor());
		assertEquals(d.getName(), p.getName());
	}

	@Test
	void placeOnePiece() {
		ChessPiece p = factory.makePiece(BLACKPAWN);
		board.putPieceAt(p, makeCoordinate(2, 2));
		assertEquals(p, board.getPieceAt(makeCoordinate(2, 2)));
	}

	@Test
	void placeTwoPieces() {
		ChessPiece bn = factory.makePiece(BLACKKNIGHT);
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		board.putPieceAt(bn, makeCoordinate(3, 5));
		board.putPieceAt(wb, makeCoordinate(2, 6));
		assertEquals(bn, board.getPieceAt(makeCoordinate(3, 5)));
		assertEquals(wb, board.getPieceAt(makeCoordinate(2, 6)));
	}

	@Test
	void checkForPieceHasMoved() {
		ChessPiece bq = factory.makePiece(BLACKQUEEN);
		assertFalse(bq.hasMoved());
		bq.setHasMoved();
		assertTrue(bq.hasMoved());
	}

	/** PAWN TESTS **/

	@ParameterizedTest
	@MethodSource("pawnPassProvider")
	void pawnTrueMove(
			Coordinate wfrom, Coordinate wto,
			Coordinate bfrom, Coordinate bto) {
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		assertTrue(wp.canMove(wfrom, wto, board));
		assertTrue(bp.canMove(bfrom, bto, board));
	}
	
	static Stream<Arguments> pawnPassProvider() {
		return Stream.of(
				Arguments.of(makeCoordinate(2,2), makeCoordinate(3,2),
						makeCoordinate(7,2), makeCoordinate(6,2)),
				Arguments.of(makeCoordinate(2,2), makeCoordinate(4,2),
						makeCoordinate(7,2), makeCoordinate(5,2))
				);
	}

	@ParameterizedTest
	@MethodSource("pawnFailProvider")
	void pawnFalseMove(
			Coordinate wfrom, Coordinate wto,
			Coordinate bfrom, Coordinate bto) {
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		wp.setHasMoved();
		bp.setHasMoved();
		assertFalse(wp.canMove(wfrom, wto, board));
		assertFalse(bp.canMove(bfrom, bto, board));
	}
	
	static Stream<Arguments> pawnFailProvider() {
		return Stream.of(
				Arguments.of(makeCoordinate(2,2), makeCoordinate(4,2),
						makeCoordinate(7,2), makeCoordinate(5,2)),
				Arguments.of(makeCoordinate(2,2), makeCoordinate(1,2),
						makeCoordinate(7,2), makeCoordinate(8,2)),
				Arguments.of(makeCoordinate(2,2), makeCoordinate(1,1),
						makeCoordinate(7,2), makeCoordinate(8,1)),
				Arguments.of(makeCoordinate(2,2), makeCoordinate(3,3),
						makeCoordinate(7,2), makeCoordinate(6,3))
				);	
	}
	
	@ParameterizedTest
	@MethodSource("pawnAttackProvider")
	void pawnTrueSingleMove(
			Coordinate wfrom, Coordinate wto,
			Coordinate bfrom, Coordinate bto) {
		ChessPiece wp = factory.makePiece(WHITEPAWN);
		ChessPiece bp = factory.makePiece(BLACKPAWN);
		board.putPieceAt(factory.makePiece(BLACKPAWN), wto);
		board.putPieceAt(factory.makePiece(WHITEPAWN), bto);
		assertTrue(wp.canMove(wfrom, wto, board));
		assertTrue(bp.canMove(bfrom, bto, board));
	}
	
	static Stream<Arguments> pawnAttackProvider() {
		return Stream.of(
				Arguments.of(makeCoordinate(2,2), makeCoordinate(3,3),
						makeCoordinate(7,2), makeCoordinate(6,3)),
				Arguments.of(makeCoordinate(2,2), makeCoordinate(3,1),
						makeCoordinate(7,2), makeCoordinate(6,1))
				);
	}
	
	
	/** Knight Tests **/
	@ParameterizedTest
	@MethodSource("knightPassProvider")
	void knightTrueMove(
			Coordinate wfrom, Coordinate wto,
			Coordinate bfrom, Coordinate bto) {
		ChessPiece wk = factory.makePiece(WHITEKNIGHT);
		ChessPiece bk = factory.makePiece(BLACKKNIGHT);
		board.putPieceAt(factory.makePiece(BLACKKNIGHT), wto);
		board.putPieceAt(factory.makePiece(WHITEKNIGHT), bto);
		assertTrue(wk.canMove(wfrom, wto, board));
		assertTrue(bk.canMove(bfrom, bto, board));
	}
	
	static Stream<Arguments> knightPassProvider() {
		return Stream.of(
				Arguments.of(makeCoordinate(1,2), makeCoordinate(3,3),
						makeCoordinate(8,2), makeCoordinate(6,1)),
				Arguments.of(makeCoordinate(4,3), makeCoordinate(2,4),
						makeCoordinate(4,6), makeCoordinate(6,5)),
				Arguments.of(makeCoordinate(1,2), makeCoordinate(2,4),
						makeCoordinate(8,2), makeCoordinate(7,4))
				);
	}
	
	@ParameterizedTest
	@MethodSource("knightFailProvider")
	void knightFalseMove(
			Coordinate wfrom, Coordinate wto,
			Coordinate bfrom, Coordinate bto) {
		ChessPiece wk = factory.makePiece(WHITEKNIGHT);
		ChessPiece bk = factory.makePiece(BLACKKNIGHT);
		board.putPieceAt(factory.makePiece(BLACKKNIGHT), bto);
		board.putPieceAt(factory.makePiece(WHITEKNIGHT), wto);
		assertFalse(wk.canMove(wfrom, wto, board));
		assertFalse(bk.canMove(bfrom, bto, board));
	}
	
	static Stream<Arguments> knightFailProvider() {
		return Stream.of(
				Arguments.of(makeCoordinate(1,2), makeCoordinate(2,3),
						makeCoordinate(8,2), makeCoordinate(7,3)),
				Arguments.of(makeCoordinate(1,2), makeCoordinate(2,4),
						makeCoordinate(8,2), makeCoordinate(7,4))
				);
	}
	
	/** Bishop Tests **/
	@ParameterizedTest
	@MethodSource("bishopPassProvider")
	void bishopTrueMove(
			Coordinate wfrom, Coordinate wto,
			Coordinate bfrom, Coordinate bto) {
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		ChessPiece bb = factory.makePiece(BLACKBISHOP);
		board.putPieceAt(factory.makePiece(BLACKBISHOP), wto);
		board.putPieceAt(factory.makePiece(WHITEBISHOP), bto);
		assertTrue(wb.canMove(wfrom, wto, board));
		assertTrue(bb.canMove(bfrom, bto, board));
	}
	
	static Stream<Arguments> bishopPassProvider() {
		return Stream.of(
				Arguments.of(makeCoordinate(1,3), makeCoordinate(4,6),
						makeCoordinate(8,3), makeCoordinate(5,6)),
				Arguments.of(makeCoordinate(1,6), makeCoordinate(4,3),
						makeCoordinate(8,6), makeCoordinate(5,3))
				);
	}
	
	@ParameterizedTest
	@MethodSource("bishopFailProvider")
	void bishopFalseMove(
			Coordinate wfrom, Coordinate wto,
			Coordinate bfrom, Coordinate bto) {
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		ChessPiece bb = factory.makePiece(BLACKBISHOP);
		board.putPieceAt(factory.makePiece(BLACKBISHOP), bto);
		board.putPieceAt(factory.makePiece(WHITEBISHOP), wto);
		assertFalse(wb.canMove(wfrom, wto, board));
		assertFalse(bb.canMove(bfrom, bto, board));
	}
	
	static Stream<Arguments> bishopFailProvider() {
		return Stream.of(
				Arguments.of(makeCoordinate(1,3), makeCoordinate(4,6),
						makeCoordinate(8,3), makeCoordinate(5,6)),
				Arguments.of(makeCoordinate(1,6), makeCoordinate(4,3),
						makeCoordinate(8,6), makeCoordinate(5,3))
				);
	}
	
	@Test
	void invalidDiagonal() {
		ChessPiece wb = factory.makePiece(WHITEBISHOP);
		assertFalse(wb.canMove(makeCoordinate(1,3),makeCoordinate(3,6), board));
	}
	
	/** Rook Tests **/
	@ParameterizedTest
	@MethodSource("rookPassProvider")
	void rookTrueMove(
			Coordinate wfrom, Coordinate wto,
			Coordinate bfrom, Coordinate bto) {
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece br = factory.makePiece(BLACKROOK);
		board.putPieceAt(factory.makePiece(BLACKROOK), wto);
		board.putPieceAt(factory.makePiece(WHITEROOK), bto);
		assertTrue(wr.canMove(wfrom, wto, board));
		assertTrue(br.canMove(bfrom, bto, board));
	}
	
	static Stream<Arguments> rookPassProvider() {
		return Stream.of(
				Arguments.of(makeCoordinate(1,8), makeCoordinate(8,8),
						makeCoordinate(1,8), makeCoordinate(1,1)),
				Arguments.of(makeCoordinate(8,5), makeCoordinate(3,5),
						makeCoordinate(1,1), makeCoordinate(1,7))
				);
	}
	
	@ParameterizedTest
	@MethodSource("rookFailProvider")
	void rookFalseMove(
			Coordinate wfrom, Coordinate wto,
			Coordinate bfrom, Coordinate bto) {
		ChessPiece wr = factory.makePiece(WHITEROOK);
		ChessPiece br = factory.makePiece(BLACKROOK);
		board.putPieceAt(factory.makePiece(BLACKROOK), bto);
		board.putPieceAt(factory.makePiece(WHITEROOK), wto);
		assertFalse(wr.canMove(wfrom, wto, board));
		assertFalse(br.canMove(bfrom, bto, board));
	}
	
	static Stream<Arguments> rookFailProvider() {
		return Stream.of(
				Arguments.of(makeCoordinate(1,8), makeCoordinate(8,8),
						makeCoordinate(1,8), makeCoordinate(1,1)),
				Arguments.of(makeCoordinate(8,5), makeCoordinate(3,5),
						makeCoordinate(1,1), makeCoordinate(1,7))
				);
	}
	
	
	/** King Tests **/
	@ParameterizedTest
	@MethodSource("kingPassProvider")
	void kingTrueMove(
			Coordinate wfrom, Coordinate wto,
			Coordinate bfrom, Coordinate bto) {
		ChessPiece wk = factory.makePiece(WHITEQUEEN);
		ChessPiece bk = factory.makePiece(BLACKQUEEN);
		board.putPieceAt(factory.makePiece(BLACKROOK), wto);
		board.putPieceAt(factory.makePiece(WHITEROOK), bto);
		assertTrue(wk.canMove(wfrom, wto, board));
		assertTrue(bk.canMove(bfrom, bto, board));
	}
	
	static Stream<Arguments> kingPassProvider() {
		return Stream.of(
				Arguments.of(makeCoordinate(2,2), makeCoordinate(3,2),
						makeCoordinate(6,2), makeCoordinate(6,1))
				);
	}

	@ParameterizedTest
	@MethodSource("kingFailProvider")
	void kingFalseMove(
			Coordinate wfrom, Coordinate wto,
			Coordinate bfrom, Coordinate bto) {
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece bk = factory.makePiece(BLACKKING);
		board.putPieceAt(factory.makePiece(BLACKROOK), bto);
		board.putPieceAt(factory.makePiece(WHITEROOK), wto);
		assertFalse(wk.canMove(wfrom, wto, board));
		assertFalse(bk.canMove(bfrom, bto, board));
	}
	
	static Stream<Arguments> kingFailProvider() {
		return Stream.of(
				Arguments.of(makeCoordinate(1,2), makeCoordinate(4,2),
						makeCoordinate(8,2), makeCoordinate(4,4))
				);
	}

	
	/** Queen Tests **/
	@ParameterizedTest
	@MethodSource("queenPassProvider")
	void queenTrueMove(
			Coordinate wfrom, Coordinate wto,
			Coordinate bfrom, Coordinate bto) {
		ChessPiece wq = factory.makePiece(WHITEQUEEN);
		ChessPiece bq = factory.makePiece(BLACKQUEEN);
		board.putPieceAt(factory.makePiece(BLACKROOK), wto);
		board.putPieceAt(factory.makePiece(WHITEROOK), bto);
		assertTrue(wq.canMove(wfrom, wto, board));
		assertTrue(bq.canMove(bfrom, bto, board));
	}
	
	static Stream<Arguments> queenPassProvider() {
		return Stream.of(
				Arguments.of(makeCoordinate(1,3), makeCoordinate(4,6),
						makeCoordinate(8,6), makeCoordinate(5,6)),
				Arguments.of(makeCoordinate(7,7), makeCoordinate(7,6),
						makeCoordinate(2,2), makeCoordinate(3,2)),
				Arguments.of(makeCoordinate(2,8), makeCoordinate(8,8),
						makeCoordinate(1,8), makeCoordinate(1,1))
				);
	}
	
	/** Castling Tests **/
	@Test
	void canCastleWhite() {
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(factory.makePiece(WHITEROOK), makeCoordinate(1,1));
		assertTrue(wk.canMove(makeCoordinate(1,5),makeCoordinate(1,3), board));
		wk = factory.makePiece(WHITEKING);
		board.putPieceAt(factory.makePiece(WHITEROOK), makeCoordinate(1,8));
		assertTrue(wk.canMove(makeCoordinate(1,5),makeCoordinate(1,7), board));
	}
	
	@Test
	void canNotCastleWhite() {
		ChessPiece wk = factory.makePiece(WHITEKING);
		ChessPiece wr = factory.makePiece(WHITEROOK);
		assertFalse(wk.canMove(makeCoordinate(1,5),makeCoordinate(1,3), board));
		wr.setHasMoved();
		board.putPieceAt(wr, makeCoordinate(1,1));
		assertFalse(wk.canMove(makeCoordinate(1,5),makeCoordinate(1,3), board));
		wk.setHasMoved();
		board.putPieceAt(factory.makePiece(WHITEROOK), makeCoordinate(1,8));
		assertFalse(wk.canMove(makeCoordinate(1,5),makeCoordinate(1,7), board));
	}
	
	@Test
	void canCastleBlack() {
		ChessPiece wk = factory.makePiece(BLACKKING);
		board.putPieceAt(factory.makePiece(BLACKROOK), makeCoordinate(8,1));
		assertTrue(wk.canMove(makeCoordinate(8,5),makeCoordinate(8,3), board));
		wk = factory.makePiece(BLACKKING);
		board.putPieceAt(factory.makePiece(BLACKROOK), makeCoordinate(8,8));
		assertTrue(wk.canMove(makeCoordinate(8,5),makeCoordinate(8,7), board));
	}
	
	@Test
	void canNotCastleBlack() {
		ChessPiece bk = factory.makePiece(BLACKKING);
		ChessPiece br = factory.makePiece(BLACKROOK);
		assertFalse(bk.canMove(makeCoordinate(1,5),makeCoordinate(8,3), board));
		br.setHasMoved();
		board.putPieceAt(br, makeCoordinate(8,1));
		assertFalse(bk.canMove(makeCoordinate(8,5),makeCoordinate(8,3), board));
		bk.setHasMoved();
		board.putPieceAt(factory.makePiece(BLACKROOK), makeCoordinate(8,8));
		assertFalse(bk.canMove(makeCoordinate(8,5),makeCoordinate(8,7), board));
	}
	
	
	@Test
	void thisShouldFailOnDelivery() {
		ChessPiece wk = factory.makePiece(WHITEKING);
		board.putPieceAt(wk, makeCoordinate(1, 5));
		assertTrue(wk.canMove(makeCoordinate(1, 5), makeCoordinate(2, 5), board));
	}

}
