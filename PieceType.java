import java.util.ArrayList;
import java.util.List;

public enum PieceType {
    PAWN,
    KNIGHT,
    BISHOP,
    ROOK,
    QUEEN,
    KING;

    public void print_short() {
        switch (this) {
            case ROOK -> System.out.print("R");
            case BISHOP -> System.out.print("B");
            case KNIGHT -> System.out.print("N");
            case KING -> System.out.print("K");
            case QUEEN -> System.out.print("Q");
            case PAWN -> System.out.print("P");
            default -> throw new AssertionError();
        }
    }

    public boolean possible_move(Board board, Piece piece, Coordinate start, Coordinate end) {
        // check for check
        Side color = piece.getColor();
        if (!color.is_checked(board).isEmpty()) {
            // temporarily try out move
            // store what's on the relevant fields now
            Piece end_piece = board.pieces.get(end);

            board.pieces.put(end, piece);
            board.pieces.put(start, null);
            if (!color.is_checked(board).isEmpty()) {
                return false;
            }
            board.pieces.put(start, piece);
            board.pieces.put(end, end_piece);
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
        return !path.isEmpty();
    }

    public ArrayList<Coordinate> valid_move(Board board, Piece piece, Coordinate start, Coordinate end) {
        System.err.println("PieceType: checking move");

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
                // called to check whether a pawn is checking a king
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
        ArrayList<Coordinate> result = new ArrayList<>(List.of(start));
        if (!(Math.abs(start.col - end.col) == 0 && Math.abs(start.row - end.col) == 0))
            return result;
        // TODO: check for collissions on path
        return result;
    }

    private ArrayList<Coordinate> king_movement(Coordinate start, Coordinate end) {
        ArrayList<Coordinate> result = new ArrayList<>(List.of(start));
        if (!(Math.abs(start.row - end.row) < 2 && Math.abs(start.col - end.col) < 2))
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
        System.err.println("PieceType: pawn_movement(): Calling unsupported method.");
//        throw new UnsupportedOperationException("Not supported yet.");
        return new ArrayList<>();
    }

}