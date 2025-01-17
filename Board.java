
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
    protected Map<Coordinate, Piece> pieces;
    private final ArrayList<Move> moves;

    public Board() {

        pieces = new HashMap<>();
        moves = new ArrayList<>();
        setupPieces();

    }

    private void setupPieces() {
        // CHECKMATE setup
        pieces.put(new Coordinate(8, 1), new Piece(this, PieceType.ROOK, Side.WHITE));
        pieces.put(new Coordinate(6, 8), new Piece(this, PieceType.QUEEN, Side.WHITE));
        pieces.put(new Coordinate(1, 1), new Piece(this, PieceType.KING,
                Side.WHITE));
        pieces.put(new Coordinate(8, 8), new Piece(this, PieceType.KING,
                Side.BLACK));
        // BISHOP setup
        // pieces.put(new Coordinate(5, 6), new Piece(this, PieceType.BISHOP,
        // Side.WHITE));
        // pieces.put(new Coordinate(1, 1), new Piece(this, PieceType.KING,
        // Side.WHITE));
        // pieces.put(new Coordinate(8, 8), new Piece(this, PieceType.KING,
        // Side.BLACK));
        // NORMAL setup
        // pieces.put(new Coordinate(1, 1), new Piece(this, PieceType.ROOK,
        // Side.WHITE)); // White Rook at (1, 1)
        // pieces.put(new Coordinate(1, 2), new Piece(this, PieceType.KNIGHT,
        // Side.WHITE)); // White Knight at (2, 1)
        // pieces.put(new Coordinate(1, 3), new Piece(this, PieceType.BISHOP,
        // Side.WHITE)); // White Bishop at (3, 1)
        // pieces.put(new Coordinate(1, 4), new Piece(this, PieceType.QUEEN,
        // Side.WHITE)); // White Queen at (4, 1)
        // pieces.put(new Coordinate(1, 5), new Piece(this, PieceType.KING,
        // Side.WHITE)); // White King at (5, 1)
        // pieces.put(new Coordinate(1, 6), new Piece(this, PieceType.BISHOP,
        // Side.WHITE)); // White Bishop at (6, 1)
        // pieces.put(new Coordinate(1, 7), new Piece(this, PieceType.KNIGHT,
        // Side.WHITE)); // White Knight at (7, 1)
        // pieces.put(new Coordinate(1, 8), new Piece(this, PieceType.ROOK,
        // Side.WHITE)); // White Rook at (8, 1)

        // White pawns
        // for (int i = 1; i <= 8; i++) {
        // pieces.put(new Coordinate(2, i), new Piece(this, PieceType.PAWN,
        // Side.WHITE)); // White Pawns at (1, 2) to
        // // (8, 2)
        // }

        // Black pieces
        // pieces.put(new Coordinate(8, 1), new Piece(this, PieceType.ROOK,
        // Side.BLACK)); // Black Rook at (1, 8)
        // pieces.put(new Coordinate(8, 2), new Piece(this, PieceType.KNIGHT,
        // Side.BLACK)); // Black Knight at (2, 8)
        // pieces.put(new Coordinate(8, 3), new Piece(this, PieceType.BISHOP,
        // Side.BLACK)); // Black Bishop at (3, 8)
        // pieces.put(new Coordinate(8, 4), new Piece(this, PieceType.QUEEN,
        // Side.BLACK)); // Black Queen at (4, 8)
        // pieces.put(new Coordinate(8, 5), new Piece(this, PieceType.KING,
        // Side.BLACK)); // Black King at (5, 8)
        // pieces.put(new Coordinate(8, 6), new Piece(this, PieceType.BISHOP,
        // Side.BLACK)); // Black Bishop at (6, 8)
        // pieces.put(new Coordinate(8, 7), new Piece(this, PieceType.KNIGHT,
        // Side.BLACK)); // Black Knight at (7, 8)
        // pieces.put(new Coordinate(8, 8), new Piece(this, PieceType.ROOK,
        // Side.BLACK)); // Black Rook at (8, 8)

        // // Black pawns
        // for (int i = 1; i <= 8; i++) {
        // pieces.put(new Coordinate(7, i), new Piece(this, PieceType.PAWN,
        // Side.BLACK)); // Black Pawns at (1, 7) to
        // // (8, 7)
        // }
    }

    protected Coordinate king(Side side) {
        /**
         * really inefficent, should update to constantly just update the pos
         */
        switch (side) {
            case WHITE -> {
                Coordinate white_king = new Coordinate(-1, -1);
                for (Coordinate pos : this.pieces.keySet()) {
                    Piece piece = this.pieces.get(pos);
                    if (piece == null) {
                        continue;
                    }
                    if (piece.getColor().equals(Side.WHITE) && piece.getType().equals(PieceType.KING)) {
                        white_king = pos;
                    }

                }
                return white_king;
            }
            case BLACK -> {
                Coordinate black_king = new Coordinate(-1, -1);
                for (Coordinate pos : this.pieces.keySet()) {
                    Piece piece = this.pieces.get(pos);
                    if (piece == null) {
                        continue;
                    }
                    if (piece.getColor().equals(Side.BLACK) && piece.getType().equals(PieceType.KING)) {
                        black_king = pos;
                    }

                }
                return black_king;
            }
            default -> {
                System.out.println("YOU REALLY ARE STUPID. THERE IS JUST BLACK AND WHITE");
                return new Coordinate();
            }
        }
    }

    public void display() {
        // Display column letters

        // ---- logic
        // each displaying of the board calls all pieces to move to the opposing king
        final boolean white_checked = !Side.WHITE.is_checked(this).isEmpty();
        final boolean black_checked = !Side.BLACK.is_checked(this).isEmpty();
        final String CHECK = Colors.RED;
        final String RESET = Colors.RESET;
        final String BLACK = Colors.BLUE;
        final String WHITE = Colors.YELLOW;
        final String DARK_SQUARE = Colors.BG_BLACK;
        final String LIGHT_SQUARE = "";// Colors.BG_BRIGHT_BLACK;

        // ---- actual printing
        System.out.print("   ");
        for (char col = 'a'; col <= 'h'; col++) {
            System.out.print(col + "  ");
        }
        System.out.println();

        // Display the board
        for (int row = 8; row >= 1; row--) {
            System.out.print(row + " "); // Display row number
            for (int col = 1; col <= 8; col++) {
                // color background
                boolean isDarkSquare = (row + col) % 2 == 0;
                String squareColor = isDarkSquare ? DARK_SQUARE : LIGHT_SQUARE;
                System.out.print(squareColor); // Set square background color
                // get piece
                Coordinate coordinate = new Coordinate(row, col);
                Piece piece = pieces.get(coordinate);

                if (piece != null) {
                    boolean checked = false;

                    Side piece_color = piece.getColor();
                    String color = piece_color == Side.WHITE ? WHITE : BLACK;
                    boolean is_king = piece.getType() == PieceType.KING;
                    System.out.print(color);
                    System.out.print("|");
                    if (is_king && piece_color.equals(Side.WHITE) && white_checked) {
                        System.out.print(CHECK);
                        System.out.print(piece.get_string_type());
                        System.out.print(RESET);
                        System.out.print(squareColor); // Set square background color

                    } else if (is_king && piece_color.equals(Side.BLACK) && black_checked) { // black in check
                        System.out.print(CHECK);
                        System.out.print(piece.get_string_type());
                        System.out.print(RESET);
                        System.out.print(squareColor); // Set square background color
                    } else {
                        System.out.print(piece.get_string_type());
                    }
                    System.out.print(color);
                    System.out.print("|");
                    System.out.print(RESET);
                    System.out.print(squareColor); // Set square background color
                } else {
                    System.out.print("|_|");
                }
                System.out.print(Colors.RESET);

            }
            System.out.println(); // Move to the next row
        }

    }

    public boolean move(Coordinate start, Coordinate end) {

        Piece piece = pieces.get(start);
        if (piece == null) {
            return false;
        } else if (piece.move(start, end)) {
            if (pieces.get(end) != null) {
                // print them
            }

            pieces.put(end, piece); // overriding piece on end square
            pieces.remove(start); // removing piece from starting square
            moves.add(new Move(this, this.pieces.get(end), start, end));
            return true;
        } else {
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
