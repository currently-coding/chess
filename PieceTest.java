import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class PieceTest {


    @Test
    void getType() {
        // Setup
        var white_king = new Coordinate(1, 5);
        var black_king = new Coordinate(8, 5);
        var board = new Board(white_king, black_king);

        board.pieces.put(white_king, new Piece(board, PieceType.KING, Side.WHITE));
        board.pieces.put(new Coordinate(1, 1), new Piece(board, PieceType.ROOK, Side.WHITE));


        board.pieces.put(black_king, new Piece(board, PieceType.KING, Side.BLACK));
        // Test
        var piece = new Piece(board, PieceType.ROOK, Side.BLACK);
        assertEquals(PieceType.ROOK, piece.getType());
    }

    @Test
    void getColor() {
    }

    @Test
    void move() {
    }
}