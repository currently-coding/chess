

public enum Side {
    BLACK,
    WHITE,
    UNDEFINED;

    public boolean is_checked(Board board) {
        Coordinate king = board.king(this);
        for (Coordinate coordinate : board.pieces.keySet()) { // check for each piece
            Piece piece = board.pieces.get(coordinate);
            if (piece == null) {
                continue;
            }
            if (piece.getColor() != this) { // only opponent's pieces can check
                if (piece.getType().valid_move(
                        board, piece, coordinate, king)) { // check if current piece could move at king's position
                    return true;
                }
            }

        }
        return false;
    }

    public boolean checkmate(Board board) {
        System.err.println("Side: checkmate(): checking all possible moves");
        // TODO: Can evade checkmate by taking the aggressor
        int[][] offsets = new int[][] {
                { -1, -1 }, // Move up-left
                { -1, 0 }, // Move up
                { -1, 1 }, // Move up-right
                { 0, -1 }, // Move left
                { 0, 1 }, // Move right
                { 1, -1 }, // Move down-left
                { 1, 0 }, // Move down
                { 1, 1 } // Move down-right
        };
        for (int[] offset : offsets) {
            Coordinate king = board.king(this);
            Coordinate end = new Coordinate(king.col + offset[0], king.row + offset[1]);
            System.err.println("Side: checkmate(): checking king moves");
            if (board.move(king, end)) {
                return false;
            }
        }
        return true;
    }
}
