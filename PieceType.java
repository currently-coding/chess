import java.util.ArrayList;
import java.util.List;

public enum PieceType {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING;

    public String print_short() {
        return switch (this) {
            case ROOK -> "R";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case KING -> "K";
            case QUEEN -> "Q";
            case PAWN -> "P";
            default -> throw new AssertionError("Unknown piece type: " + this);
        };
    }

    public boolean possible_move(Board board, Piece piece, Coordinate start, Coordinate end) {
        /**
         * Checks whether a move is blocked by a check
         */
        boolean illegal = false;
        // check for check
        Side color = piece.getColor();
        // Side opposite = color.equals(Side.WHITE) ? Side.BLACK : Side.WHITE;
        ArrayList<ArrayList<Coordinate>> checked = color.is_checked(board);
        if (!checked.isEmpty()) {
            // System.out.println(color + ", you're currently in a check!");
        }
        // temporarily try out move
        // store what's on the relevant fields now
        Piece end_piece = board.pieces.get(end);

        // perform the complete move
        board.pieces.put(end, piece);
        board.pieces.put(start, null);
        // System.out.print("SIMULATION: moved piece to end pos. call: ");
        // start.print();
        // end.print();
        // System.out.println();
        // board.display();

        // BUG: not sure about this but does not do what it's supposed to
        // does not work when the move(from king) is towards the checking piece
        // as the kings position is not updated correctly
        if (!color.is_checked(board).isEmpty()) {
            // System.out.println(color + ", you're moving into a check");
            illegal = true;
        }
        // revert the complete move
        board.pieces.put(start, piece);
        board.pieces.put(end, end_piece);
        // System.out.println("SIMULATION: moved everything back");
        // board.display();
        //
        if (illegal) {
            return !illegal;
        }

        // Boundaries check
        if (!(end.row < 9 && end.row > 0)) {
            System.err.println("PieceType: possible_move(): Move out of bounds");
            return false;
        } else if (!(end.col < 9 && end.col > 0)) {
            System.err.println("PieceType: possible_move(): Move out of bounds");
            return false;
        }
        ArrayList<Coordinate> path = valid_move(board, piece, start, end);
        return (path.size() > 1);
    }

    public ArrayList<Coordinate> valid_move(Board board, Piece piece, Coordinate start, Coordinate end) {
        /**
         * Checks whether the actual move is allowed to happen(movement restrictions)
         */

        switch (this) {
            case ROOK -> {
                return (this.horizontal_vertical_movement(board, start, end));
            }
            case KNIGHT -> {
                return (this.knight_movement(board, start, end));
            }
            case QUEEN -> {
                if (start.row != end.row && start.col != end.col) {
                    return this.diagonal_movement(board, start, end);
                }
                return (this.horizontal_vertical_movement(board, start, end));
            }
            case BISHOP -> {
                return this.diagonal_movement(board, start, end);
            }
            case PAWN -> {
                return pawn_movement(board, start, end);
            }
            case KING -> {
                return king_movement(start, end);
            }
            default -> throw new AssertionError();
        }
    }

    // rewrite all movement to return a path array they cover while moving.
    // If invalid -> return array with only start coordinate
    private ArrayList<Coordinate> diagonal_movement(Board board, Coordinate start, Coordinate end) {
        ArrayList<Coordinate> path = new ArrayList<>(List.of(start));
        // move equally much in each direction
        int diff_y = (start.col - end.col);
        int diff_x = (start.row - end.row);
        if (!(Math.abs(diff_y) == Math.abs(diff_x))) {
            return path;
        }
        // -> move is valid
        // find out direction
        int[] direction = new int[] { diff_y / Math.abs(diff_y), diff_x / Math.abs(diff_x) };

        // collect all coords of the way
        assert (diff_x == diff_y);
        for (int i = 1; i <= Math.abs(diff_x); i++) {
            path.add(new Coordinate(start.row + (direction[0] * i), start.col + (direction[1] * i)));
        }
        for (int i = 1; i < path.size() - 1; i++) {
            Coordinate pos = path.get(i);
            if (board.pieces.get(pos) != null) {
                System.out.println("MONSTERBLOCK ");
                return new ArrayList<>(List.of(start)); // Path is blocked, return just the start coordinate
            }
        }
        return path;

    }

    private ArrayList<Coordinate> king_movement(Coordinate start, Coordinate end) {
        ArrayList<Coordinate> result = new ArrayList<>(List.of(start));
        // may move 1 square max in any direction
        if (!(Math.abs(start.row - end.row) <= 1 && Math.abs(start.col - end.col) <= 3))
            return result;
        result.add(end);
        // king can always take and has no path
        return result;

    }

    private ArrayList<Coordinate> horizontal_vertical_movement(Board board, Coordinate start, Coordinate end) {
        ArrayList<Coordinate> path = new ArrayList<>();
        path.add(start);
        // Check if the movement is purely horizontal or vertical
        if (start.row != end.row && start.col != end.col) {
            return path; // Invalid movement for a rook
        }

        // Determine the direction of movement and step increment
        int rowStep = Integer.compare(end.row, start.row); // -1 for up, 1 for down, 0 for no row change
        int colStep = Integer.compare(end.col, start.col); // -1 for left, 1 for right, 0 for no col change

        // Traverse the path from start to end
        int currentRow = start.row + rowStep;
        int currentCol = start.col + colStep;
        while (currentRow != end.row || currentCol != end.col) {
            Coordinate currentCoord = new Coordinate(currentRow, currentCol);
            if (board.pieces.get(currentCoord) != null) {
                return new ArrayList<>(List.of(start)); // Path is blocked, return just the start coordinate
            }
            path.add(currentCoord);
            currentRow += rowStep;
            currentCol += colStep;
        }
        path.add(end); // Add the end coordinate to the path

        return path;
    }

    private ArrayList<Coordinate> knight_movement(Board board, Coordinate start, Coordinate end) {
        if (Math.abs(start.col - end.col) == 2 && Math.abs(start.row - end.row) == 1) {
            return new ArrayList<>(List.of(start, end));
        } else if (Math.abs(start.col - end.col) == 1 && Math.abs(start.row - end.row) == 2) {
            return new ArrayList<>(List.of(start, end));
        }
        // Knight has no collissions
        return new ArrayList<>(List.of(start));
    }

    // TODO: double check pawn movement -> not working right now
    // TODO: write en passant movement

    private ArrayList<Coordinate> pawn_movement(Board board, Coordinate start, Coordinate end) {
        // System.err.println("PieceType: pawn_movement(): Calling unsupported
        // method.");
        // throw new UnsupportedOperationException("Not supported yet.");
        return new ArrayList<>();
    }

}
