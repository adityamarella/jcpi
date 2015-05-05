/*
 * Copyright 2007-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fluxchess.jcpi.internal.x88;

import com.fluxchess.jcpi.models.*;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class BoardTest {

  @Test
  public void testConstructor() {
    // Setup a new board
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    // Test pieces setup
    for (GenericPosition genericPosition : GenericPosition.values()) {
      GenericPiece genericPiece = genericBoard.getPiece(genericPosition);
      int piece = board.board[Position.valueOf(genericPosition)];
      if (genericPiece == null) {
        assertEquals(IntPiece.NOPIECE, piece);
      } else {
        assertEquals(genericPiece, IntPiece.toGenericPiece(piece));
      }
    }

    // Test castling
    for (GenericColor genericColor : GenericColor.values()) {
      for (GenericCastling genericCastling : GenericCastling.values()) {
        GenericFile genericFile = genericBoard.getCastling(genericColor, genericCastling);
        int file = board.castling[IntColor.valueOf(genericColor)][IntCastling.valueOf(genericCastling)];
        if (genericFile == null) {
          assertEquals(IntFile.NOFILE, file);
        } else {
          assertEquals(genericFile, IntFile.toGenericFile(file));
        }
      }
    }

    // Test en passant
    if (genericBoard.getEnPassant() == null) {
      assertEquals(Position.NOPOSITION, board.enPassant);
    } else {
      assertEquals(genericBoard.getEnPassant(), Position.toGenericPosition(board.enPassant));
    }

    // Test active color
    assertEquals(genericBoard.getActiveColor(), IntColor.toGenericColor(board.activeColor));

    // Test half move clock
    assertEquals(genericBoard.getHalfMoveClock(), board.halfMoveClock);

    // Test full move number
    assertEquals(genericBoard.getFullMoveNumber(), board.getFullMoveNumber());
  }

  @Test
  public void testToGenericBoard() {
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    assertEquals(genericBoard, board.toGenericBoard());
  }

  @Test
  public void testActiveColor() {
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    // Move white pawn
    int move = Move.valueOf(Move.Type.NORMAL, Position.a2, Position.a3, IntPiece.WHITEPAWN, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
    board.makeMove(move);
    assertEquals(IntColor.BLACK, board.activeColor);

    // Move black pawn
    move = Move.valueOf(Move.Type.NORMAL, Position.b7, Position.b6, IntPiece.BLACKPAWN, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
    board.makeMove(move);
    assertEquals(IntColor.WHITE, board.activeColor);
  }

  @Test
  public void testHalfMoveClock() {
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    // Move white pawn
    int move = Move.valueOf(Move.Type.NORMAL, Position.a2, Position.a3, IntPiece.WHITEPAWN, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
    board.makeMove(move);
    assertEquals(0, board.halfMoveClock);

    // Move black pawn
    move = Move.valueOf(Move.Type.NORMAL, Position.b7, Position.b6, IntPiece.BLACKPAWN, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
    board.makeMove(move);

    // Move white knight
    move = Move.valueOf(Move.Type.NORMAL, Position.b1, Position.c3, IntPiece.WHITEKNIGHT, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
    board.makeMove(move);
    assertEquals(1, board.halfMoveClock);
  }

  @Test
  public void testFullMoveNumber() {
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    // Move white pawn
    int move = Move.valueOf(Move.Type.NORMAL, Position.a2, Position.a3, IntPiece.WHITEPAWN, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
    board.makeMove(move);
    assertEquals(1, board.getFullMoveNumber());

    // Move black pawn
    move = Move.valueOf(Move.Type.NORMAL, Position.b7, Position.b6, IntPiece.BLACKPAWN, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
    board.makeMove(move);
    assertEquals(2, board.getFullMoveNumber());
  }

  @Test
  public void testNormalMove() {
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    int move = Move.valueOf(Move.Type.NORMAL, Position.a2, Position.a3, IntPiece.WHITEPAWN, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
    board.makeMove(move);
    board.undoMove(move);

    assertEquals(genericBoard, board.toGenericBoard());
  }

  @Test
  public void testPawnDoubleMove() {
    GenericBoard genericBoard = new GenericBoard(GenericBoard.STANDARDSETUP);
    Board board = new Board(genericBoard);

    int move = Move.valueOf(Move.Type.PAWNDOUBLE, Position.a2, Position.a4, IntPiece.WHITEPAWN, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
    board.makeMove(move);

    assertEquals(Position.a3, board.enPassant);

    board.undoMove(move);

    assertEquals(genericBoard, board.toGenericBoard());
  }

  @Test
  public void testPawnPromotionMove() throws IllegalNotationException {
    GenericBoard genericBoard = new GenericBoard("8/P5k1/8/8/2K5/8/8/8 w - - 0 1");
    Board board = new Board(genericBoard);

    int move = Move.valueOf(Move.Type.PAWNPROMOTION, Position.a7, Position.a8, IntPiece.WHITEPAWN, IntPiece.NOPIECE, IntChessman.QUEEN);
    board.makeMove(move);

    assertEquals(IntPiece.WHITEQUEEN, board.board[Position.a8]);

    board.undoMove(move);

    assertEquals(genericBoard, board.toGenericBoard());
  }

  @Test
  public void testEnPassantMove() throws IllegalNotationException {
    GenericBoard genericBoard = new GenericBoard("5k2/8/8/8/3Pp3/8/8/3K4 b - d3 0 1");
    Board board = new Board(genericBoard);

    // Make en passant move
    int move = Move.valueOf(Move.Type.ENPASSANT, Position.e4, Position.d3, IntPiece.BLACKPAWN, IntPiece.WHITEPAWN, IntChessman.NOCHESSMAN);
    board.makeMove(move);

    assertEquals(IntPiece.NOPIECE, board.board[Position.d4]);
    assertEquals(IntPiece.BLACKPAWN, board.board[Position.d3]);
    assertEquals(Position.NOPOSITION, board.enPassant);

    board.undoMove(move);

    assertEquals(genericBoard, board.toGenericBoard());
  }

  @Test
  public void testCastlingMove() throws IllegalNotationException {
    GenericBoard genericBoard = new GenericBoard("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
    Board board = new Board(genericBoard);

    int move = Move.valueOf(Move.Type.CASTLING, Position.e1, Position.c1, IntPiece.WHITEKING, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
    board.makeMove(move);

    assertEquals(IntFile.NOFILE, board.castling[IntColor.WHITE][IntCastling.QUEENSIDE]);

    board.undoMove(move);

    assertEquals(genericBoard, board.toGenericBoard());

    genericBoard = new GenericBoard("r3k2r/8/8/8/8/8/8/R3K2R w KQkq - 0 1");
    board = new Board(genericBoard);

    move = Move.valueOf(Move.Type.CASTLING, Position.e1, Position.g1, IntPiece.WHITEKING, IntPiece.NOPIECE, IntChessman.NOCHESSMAN);
    board.makeMove(move);

    assertEquals(IntFile.NOFILE, board.castling[IntColor.WHITE][IntCastling.KINGSIDE]);

    board.undoMove(move);

    assertEquals(genericBoard, board.toGenericBoard());
  }

}
