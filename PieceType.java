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
        // if (color.is_checked(board)) {
        // // temporarily try out move
        // // store whats on the relevant fields now
        // Piece end_piece = board.pieces.get(end);

        // board.pieces.put(end, piece);
        // board.pieces.put(start, null);
        // if (color.is_checked(board)) {
        // return false;
        // }
        // board.pieces.put(start, piece);
        // board.pieces.put(end, end_piece);
        // }

        // if not checked -> valid
        // else -> invalid
        // revert to previous state and evaluate

        // Boundaries check
        if (!(end.row < 9 && end.row > 0)) {
            System.err.println("PieceType: possible_move(): Move out of bounds");
            return false;
        } else if (!(end.col < 9 && end.col > 0)) {
            System.err.println("PieceType: possible_move(): Move out of bounds");
            return false;
        }
        return valid_move(board, piece, start, end);
    }

    public boolean valid_move(Board board, Piece piece, Coordinate start, Coordinate end) {
        System.err.println("PieceType: checking move");

        switch (this) {
            case ROOK -> {
                return (this.horizontal_vertical_movement(start, end));
            }
            case KNIGHT -> {
                return (this.knight_movement(start, end));
            }
            case QUEEN -> {
                return (this.horizontal_vertical_movement(start, end) || this.diagonal_movement(start, end));
            }
            case BISHOP -> {
                return this.diagonal_movement(start, end);
            }
            case PAWN -> {
                return pawn_movement(start, end, board);
            }
            case KING -> {
                return king_movement(start, end);
            }
            default -> throw new AssertionError();
        }
    }

    private boolean diagonal_movement(Coordinate start, Coordinate end) {
        return (Math.abs(start.col - end.col) == 0 && Math.abs(start.row - end.col) == 0);
    }

    private boolean king_movement(Coordinate start, Coordinate end) {
        return (Math.abs(start.row - end.row) < 2 && Math.abs(start.col - end.col) < 2);
    }

    private boolean horizontal_vertical_movement(Coordinate start, Coordinate end) {
        // horizontal movement
        if (start.col == end.col && start.row != end.row) {
            return true;
        }
        // vertical movement
        else if (start.col != end.col && start.row == end.row) {
            return true;
        }
        return false;
    }

    private boolean knight_movement(Coordinate start, Coordinate end) {
        if (Math.abs(start.col - end.col) == 2 && Math.abs(start.row - end.row) == 1) {
            return true;
        } else if (Math.abs(start.col - end.col) == 1 && Math.abs(start.row - end.row) == 2) {
            return true;
        }
        return false;
    }

    // TODO: double check pawn movement -> not working right now
    // TODO: write en passant movement
    private boolean pawn_movement(Coordinate start, Coordinate end, Board board) {
        // int rowDiff = end.row - start.row;
        // int colDiff = Math.abs(end.col - start.col);

        // if (piece.getColor() == Side.WHITE) {
        // // Standard move one step forward
        // if (rowDiff == 1 && colDiff == 0 && !board.is_piece(end)) {
        // return true;
        // }
        // // Initial move two steps forward
        // if (start.row == 2 && rowDiff == 2 && colDiff == 0 && !board.is_piece(end))
        // && !board.is_piece(end)) {
        // return true;
        // }
        // // Capture move
        // if (rowDiff == 1 && colDiff == 1 && board.is_piece(end) &&
        // board.getPiece(end).getColor() == Side.BLACK) {
        // return true;
        // }
        // // En passant (not fully implemented here, requires additional state)
        // // if (rowDiff == 1 && colDiff == 1 && !board.is_piece(end)) {
        // // return handleEnPassant(piece, start, end, board);
        // // }
        // } else if (piece.getColor() == Side.BLACK) {
        // // Standard move one step forward
        // if (rowDiff == -1 && colDiff == 0 && !board.is_piece(end)) {
        // return true;
        // }
        // // Initial move two steps forward
        // if (start.row == 7 && rowDiff == -2 && colDiff == 0 && !board.is_piece(end))
        // && !board.is_piece(end)) {
        // return true;
        // }
        // // Capture move
        // if (rowDiff == -1 && colDiff == 1 && board.is_piece(end) &&
        // board.getPiece(end).getColor() == Side.WHITE) {
        // return true;
        // }
        // // En passant (not fully implemented here, requires additional state)
        // // if (rowDiff == -1 && colDiff == 1 && !board.is_piece(end)) {
        // // return handleEnPassant(piece, start, end, board);
        // // }
        // }

        return false;
    }

    // Placeholder for en passant handling (not implemented)
    // private boolean handleEnPassant(Piece piece, Coordinate start, Coordinate
    // end, Board board) {
    // // Implement en passant logic here
    // return false;
    // }
}