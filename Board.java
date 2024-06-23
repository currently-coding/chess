
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
    protected Map<Coordinate, Piece> pieces;
    private final ArrayList<Move> moves; // even == Black, odd == White
    private Coordinate white_king;
    private Coordinate black_king;

    public Board() {
        System.err.println("Board: Creating new Board");
        pieces = new HashMap<>();
        moves = new ArrayList<>();
        setupPieces();
    }

    private void setupPieces() {
        // WHITE
        System.err.println("Setting up Pieces");
//        pieces.put(new Coordinate(1, 1), new Piece(this, PieceType.ROOK, Side.WHITE));
//        pieces.put(new Coordinate(1, 2), new Piece(this, PieceType.KNIGHT, Side.WHITE));
//        pieces.put(new Coordinate(1, 3), new Piece(this, PieceType.BISHOP, Side.WHITE));
//        pieces.put(new Coordinate(1, 4), new Piece(this, PieceType.QUEEN, Side.WHITE));
        white_king = new Coordinate(1, 5);
        pieces.put(white_king, new Piece(this, PieceType.KING, Side.WHITE));
        pieces.put(new Coordinate(1, 1), new Piece(this, PieceType.ROOK, Side.WHITE));
////        pieces.put(new Coordinate(1, 6), new Piece(this, PieceType.BISHOP, Side.WHITE));
////        pieces.put(new Coordinate(1, 7), new Piece(this, PieceType.KNIGHT, Side.WHITE));
////        pieces.put(new Coordinate(1, 8), new Piece(this, PieceType.ROOK, Side.WHITE));
////        // WHITE PAWNs
////        for (int i = 1; i < 9; i++) {
////            pieces.put(new Coordinate(2, i), new Piece(this, PieceType.PAWN, Side.WHITE));
////        }
//        // BLACK
//        pieces.put(new Coordinate(8, 1), new Piece(this, PieceType.ROOK, Side.BLACK));
//        pieces.put(new Coordinate(8, 2), new Piece(this, PieceType.KNIGHT, Side.BLACK));
//        pieces.put(new Coordinate(8, 3), new Piece(this, PieceType.BISHOP, Side.BLACK));
//        pieces.put(new Coordinate(8, 4), new Piece(this, PieceType.QUEEN, Side.BLACK));
        black_king = new Coordinate(8, 5);
        pieces.put(black_king, new Piece(this, PieceType.KING, Side.BLACK));
//        pieces.put(new Coordinate(8, 6), new Piece(this, PieceType.BISHOP, Side.BLACK));
//        pieces.put(new Coordinate(8, 7), new Piece(this, PieceType.KNIGHT, Side.BLACK));
//        pieces.put(new Coordinate(8, 8), new Piece(this, PieceType.ROOK, Side.BLACK));
//        // BLACK PAWNs
//        for (int i = 1; i < 9; i++) {
//            pieces.put(new Coordinate(7, i), new Piece(this, PieceType.PAWN, Side.BLACK));
//        }
    }

    protected Coordinate king(Side side) {
        switch (side) {
            case Side.WHITE -> {
                return white_king;
            }
            case Side.BLACK -> {
                return black_king;
            }
            default -> {
                System.err.println("Board: king(): Invalid Color");
                return new Coordinate();
            }
        }
    }

    public void display() {
        System.err.println("Displaying board");
        // Display column letters
        System.out.print("   ");
        for (char col = 'a'; col <= 'h'; col++) {
            System.out.print(col + "   ");
        }
        System.out.println();

        // Display the board
        for (int row = 8; row >= 1; row--) {
            System.out.print(row + " "); // Display row number
            for (int col = 1; col <= 8; col++) {
                Coordinate coordinate = new Coordinate(row, col);
                Piece piece = pieces.get(coordinate);
                if (piece != null) {
                    System.out.print("|"); // Assuming Piece has a getSymbol() method
                    piece.print_type();
                    System.out.print("| ");
                } else {
                    System.out.print("|_| "); // Empty square
                }
            }
            System.out.println(); // Move to the next row
        }
    }

    public boolean move(Coordinate start, Coordinate end) {
        System.err.println("Board: moving");
        Piece piece = pieces.get(start);
        if (piece == null) {
            System.err.println("Board: move(): Trying to access piece leading to NullPtr `piece == null`");
            return false;
        }
        else if (piece.move(start, end)) {
            if (pieces.get(end) != null) {
                System.out.print("Taking ");
                pieces.get(end).print();
                System.out.print(" on ");
                end.print();
                System.out.println();
            }
            System.err.print("Board: move(): Valid Move!");
            pieces.put(end, piece); // overriding piece on end square
            pieces.remove(start); // removing piece from starting square
            moves.add(new Move(this, this.pieces.get(end), start, end));
            return true;
        } else {
            System.err.println("Board: move(): Invalid Move!: Piece != Null + Move Invalid(piece.move())");
            return false;
        }
    }

    public boolean is_piece(Coordinate end) {
        return (pieces.get(end) != null);
    }

    public void print_moves() {
        for (Move m : moves) {
            m.print();
        }
    }

    public boolean game_over() {
        return Side.BLACK.checkmate(this) || Side.WHITE.checkmate(this);
    }

}