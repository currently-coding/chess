import java.util.ArrayList;
import java.util.Collections;

public enum PieceType {
    EMPTY,
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
        // check check
        Side color = piece.getColor();
        if (!color.is_checked(board).isEmpty()) {
            // temporarily try out move
            // store whats on the relevant fields now
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
        if (path.isEmpty()) {
            return false;
        }
        return true;
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
                return pawn_movement(board, start, end);
            }
            case KING -> {
                return king_movement(start, end);
            }
            default -> throw new AssertionError();
        }
    }

    // rewrite all movement to return a path array they cover while moving. If
    // invalid -> return empty array
    private ArrayList<Coordinate> diagonal_movement(Board board, Coordinate start, Coordinate end) {
        ArrayList<Coordinate> result = new ArrayList<>();
        if (!(Math.abs(start.col - end.col) == 0 && Math.abs(start.row - end.col) == 0))
            return result;
        // check for collissions on path
        return result;
    }

    private ArrayList<Coordinate> king_movement(Coordinate start, Coordinate end) {
        ArrayList<Coordinate> result = new ArrayList<>();
        if (!(Math.abs(start.row - end.row) < 2 && Math.abs(start.col - end.col) < 2))
            return result;
        result.add(end);
        // king can always take and has no path
        return result;

    }

    private ArrayList<Coordinate> horizontal_vertical_movement(Board board, Coordinate start, Coordinate end) {
        ArrayList<Coordinate> result = new ArrayList<>();
        // horizontal movement
        if (start.col == end.col && start.row != end.row) {
            int row = start.row;
            int direction = start.row < end.row ? 1 : -1;
            while (row != end.row) {
                if (board.pieces.get(new Coordinate(start.col, row)) != null) {
                    return new ArrayList<>();
                }
                result.add(new Coordinate(start.col, row));
                row += (1 * direction);
            }
            return result;
        }
        // vertical movement
        else if (start.col != end.col && start.row == end.row) {
            int col = start.col;
            int direction = start.col < end.col ? 1 : -1;
            while (col != end.col) {

                if (board.pieces.get(new Coordinate(col, start.row)) != null) {
                    return new ArrayList<>();
                }
                result.add(new Coordinate(start.row, col));
                col += (1 * direction);
            }
            return result;
        }

        return result;
    }

    private ArrayList<Coordinate> knight_movement(Board board, Coordinate start, Coordinate end) {
        if (Math.abs(start.col - end.col) == 2 && Math.abs(start.row - end.row) == 1) {
            return new ArrayList<>(Collections.singletonList(end));
        } else if (Math.abs(start.col - end.col) == 1 && Math.abs(start.row - end.row) == 2) {
            return new ArrayList<>(Collections.singletonList(end));
        }
        // Knight has no collissions
        return new ArrayList<>();
    }

    // TODO: double check pawn movement -> not working right now
    // TODO: write en passant movement

    private ArrayList<Coordinate> pawn_movement(Board board, Coordinate start, Coordinate end) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}