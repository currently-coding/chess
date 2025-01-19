import java.util.ArrayList;
import java.util.List;

public enum PieceType {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING;

    @Override
    public String toString() {
        return switch (this) {
            case ROOK -> "R";
            case BISHOP -> "B";
            case KNIGHT -> "N";
            case KING -> "K";
            case QUEEN -> "Q";
            case PAWN -> "P";
            default -> throw new IllegalArgumentException("Unknown piece type: " + this);
        };
    }

    public boolean possible_move(Board board, Piece piece, Coordinate start, Coordinate end) {
        /**
         * Final say regarding whether a move is legal or not
         * checks:
         * - collisions
         * - checks
         * - in bounds check
         * - correctly removes pieces from en-passant pawn takes
         */

        boolean illegal = false;

        // check for check
        Side color = piece.getColor();
        ArrayList<ArrayList<Coordinate>> checked = color.is_checked(board);
        if (!checked.isEmpty()) {
            // System.out.println(color + ", you're currently in a check!");
        }

        ArrayList<Coordinate> path = valid_move(board, piece, start, end);
        if (path.size() == 1) { // contains just start positions -> illegal
            return false;
        } else if (path.isEmpty()) {
            throw new IllegalStateException("valid move returned an empty path.");
        }
        for (int i = 1; i < path.size() - 1; i++) {
            Coordinate coord = path.get(i);
            if (board.pieces.get(coord) != null) {
                return false;
            }
        }
        // check whether end pos is of same color -> illegal
        Piece end_piece = board.pieces.get(path.get(path.size() - 1));
        if (end_piece != null && end_piece.getColor() == piece.getColor()) {
            return false;
        }

        // complete en-passant movement
        // check conditions:
        if (piece.getType() == PieceType.PAWN) {
            int rowDiff = end.row - start.row;
            int colDiff = Math.abs(end.col - start.col);

            if (rowDiff == ((piece.getColor() == Side.WHITE) ? 1 : -1) && colDiff == 1
                    && board.pieces.get(end) == null) {
                // En passant detected: remove the opponent's pawn
                Coordinate enPassantPawn = new Coordinate(start.row, end.col);
                board.pieces.remove(enPassantPawn); // Remove captured pawn
            }
        }

        // temporarily try out move

        // perform the complete move
        board.pieces.put(end, piece);
        board.pieces.put(start, null);

        // if the move would put you in a check -> false
        if (!color.is_checked(board).isEmpty()) {
            // return only after moving back pieces
            illegal = true;
        }
        // revert the complete move and
        board.pieces.put(start, piece);
        board.pieces.put(end, end_piece);
        // return here
        if (illegal) {
            return !illegal;
        }

        // Boundaries check
        if (!in_bounds(end)) {
            return false;
        }
        return true;
    }

    private boolean in_bounds(Coordinate coord) {
        return coord.row < 9 && coord.row > 0 && coord.col < 9 && coord.col > 0;
    }

    public ArrayList<Coordinate> valid_move(Board board, Piece piece, Coordinate start, Coordinate end) {
        /**
         * Checks whether the actual move is allowed to happen by movement restrictions
         * e.g. knight patterns, blocked paths, etc
         * does not:
         * - check collisions -> see possible_move()
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
            default -> throw new IllegalArgumentException("Unknown piece: " + this);
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
        if (!(Math.abs(start.row - end.row) <= 1 && Math.abs(start.col - end.col) <= 1))
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

    private ArrayList<Coordinate> pawn_movement(Board board, Coordinate start, Coordinate end) {
        ArrayList<Coordinate> path = new ArrayList<>(List.of(start));

        // Get the piece and its color
        Piece piece = board.pieces.get(start);
        if (piece == null || piece.getType() != PieceType.PAWN) {
            return path; // Invalid if no pawn is present
        }
        Side color = piece.getColor();
        int direction = (color == Side.WHITE) ? 1 : -1; // Pawns move up or down depending on color

        // Calculate differences
        int rowDiff = end.row - start.row;
        int colDiff = Math.abs(end.col - start.col);

        // 1. Regular forward move (1 square ahead, column unchanged)
        if (rowDiff == direction && colDiff == 0 && board.pieces.get(end) == null) {
            path.add(end);
            return path;
        }

        // 2. Initial two-square move
        if (rowDiff == 2 * direction && colDiff == 0 && start.row == (color == Side.WHITE ? 2 : 7)
                && board.pieces.get(end) == null
                && board.pieces.get(new Coordinate(start.row + direction, start.col)) == null) {
            path.add(new Coordinate(start.row + direction, start.col)); // Intermediate square
            path.add(end);
            return path;
        }

        // 3. Capture (diagonal move to a square occupied by an opponent's piece)
        if (rowDiff == direction && colDiff == 1) {
            Piece targetPiece = board.pieces.get(end);
            if (targetPiece != null && targetPiece.getColor() != color) {
                path.add(end);
                return path;
            }
        }

        // 4. En passant
        if (rowDiff == direction && colDiff == 1) {
            // Determine the en-passant target square
            Coordinate enPassantTarget = new Coordinate(start.row, end.col); // Square beside the pawn
            Piece adjacentPawn = board.pieces.get(enPassantTarget);

            if (adjacentPawn != null
                    && adjacentPawn.getType() == PieceType.PAWN
                    && adjacentPawn.getColor() != color
                    && board.lastMoveWasDoublePawnMove(enPassantTarget)) {
                path.add(end);
                return path;
            }
        }

        // If no valid moves, return path with only the start position
        return new ArrayList<>(List.of(start));
    }

}
