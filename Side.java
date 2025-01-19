import java.util.ArrayList;

public enum Side {
    BLACK,
    WHITE,
    DRAW,
    UNDEFINED;

    @Override
    public String toString() {
        switch (this) {
            case Side.BLACK:
                return "Black";
            case Side.WHITE:
                return "White";

            default:
                return "Error";
        }
    }

    public ArrayList<ArrayList<Coordinate>> is_checked(Board board) {
        /**
         * Returns all possible attacking paths the cause the check.
         * If size() of return is 0 => no check
         */
        // loop through all possible positions
        Coordinate king = board.king(this)
                .orElseThrow(() -> new IllegalStateException("There currently is no king on the board for this side."));
        ArrayList<ArrayList<Coordinate>> allPaths = new ArrayList<ArrayList<Coordinate>>();
        for (Coordinate square : board.pieces.keySet()) {
            Piece piece = board.pieces.get(square);
            if (piece == null) {
                // skip if there is no actual piece there
                continue;
            }
            if (piece.getColor() == this) {
                // skip if it's of the same color
                continue;
            }
            ArrayList<Coordinate> path = piece.getType().valid_move(board, piece, square, king);
            if (path.size() > 1) { // = the path moves the piece somewhere = is valid
                allPaths.add(path);
            }
        }
        return allPaths;

    }

    private boolean can_king_move(Board board, Coordinate king) {
        int[][] directions = new int[][] { { -1, -1 }, { -1, 1 }, { 1, 1 }, { 1, -1 }, { -1, 0 }, { 1, 0 }, { 0, -1 },
                { 0, 1 } };
        for (int[] dir : directions) {
            if (board.pieces.get(king).move(king, new Coordinate(king.row + dir[0], king.col + dir[1]))) {
                return true;
            }
        }
        return false;

    }

    private ArrayList<Coordinate> positions_to_block(Board board, Coordinate king) {
        ArrayList<ArrayList<Coordinate>> attack_path = is_checked(board);
        ArrayList<Coordinate> blockable = new ArrayList<Coordinate>();
        if (attack_path.isEmpty()) {
            // no attacks happening
            return blockable;
        }
        // remove the king's position from the attack paths as one cannot move to this
        // position - all other positions are empty and thus movable to
        for (int i = 0; i < attack_path.size(); i++) {
            attack_path.get(i).remove(king);
        }
        // modify to only keep positions appearing in every attack path as only those
        // are blockable
        // add initial list
        blockable.addAll(attack_path.get(0));
        // Iterate through the attack paths to find the intersection
        for (int i = 1; i < attack_path.size(); i++) {
            ArrayList<Coordinate> currentPath = attack_path.get(i);
            blockable.retainAll(currentPath); // Retain only the common elements
        }
        return blockable;
    }

    public boolean checkmate(Board board) {
        Coordinate king = board.king(this)
                .orElseThrow(() -> new IllegalStateException("There currently is no king on the board for this side."));
        boolean king_can_move = can_king_move(board, king);
        if (king_can_move) {
            return false;
        }
        boolean can_be_blocked = false;

        ArrayList<Coordinate> to_block = positions_to_block(board, king);
        // loop through all pieces
        // copy keySet to avoid concurrent modification error
        for (Coordinate square : new ArrayList<Coordinate>(board.pieces.keySet())) {
            Piece piece = board.pieces.get(square);
            if (piece == null) { // -> there is an actual piece
                continue;
            }
            if (piece.getColor() != this) { // only our pieces are relevant
                continue;
            }
            for (int i = 0; i < to_block.size(); i++) {
                Coordinate position = to_block.get(i);
                if (piece.move(square, position)) {
                    can_be_blocked = true; // piece can block all paths at once
                }
            }
        }
        // Case 1: blockable
        if (can_be_blocked) {
            return false;
            // Case 2: evadable
        } else if (!king_can_move && this.num_pieces_remaining(board) == 1)

        {
            return true; // stalemate
        } else {
            return false;
        }
        // Case 3: None of the above
        // Case 4: not evadable and nothing to blockable
    }

    private int num_pieces_remaining(Board board) {
        int sum = 0;
        for (Coordinate pos : board.pieces.keySet()) {
            Piece piece = board.pieces.get(pos);
            if (piece == null) {
                continue;
            }
            if (piece.getColor() == this) {
                sum += 1;
            }
        }
        return sum;
    }
}
