import java.util.ArrayList;

public enum Side {
    BLACK,
    WHITE,
    DRAW,
    UNDEFINED;

    public void print(ArrayList<Coordinate> path) {
        for (Coordinate coord : path) {
            coord.print();
        }
    }

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
        Coordinate king = board.king(this);
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

    public boolean checkmate(Board board) {
        Coordinate king = board.king(this);
        // TODO: first check if king could evade. Needs to happen first
        // then check if could be blocked
        // TODO: add draw in unwinnable positions like king vs king etc:
        // implement remaining(board) method
        ArrayList<ArrayList<Coordinate>> attack_path = is_checked(board);
        if (attack_path.isEmpty()) {
            // no attacks happening
            return false;
        }
        // remove the king's position from the attack paths as one cannot move to this
        // position - all other positions are empty and thus movable to
        for (int i = 0; i < attack_path.size(); i++) {
            attack_path.get(i).remove(king);
        }
        // modify to only keep positions appearing in every attack path as only those
        // are blockable
        ArrayList<Coordinate> blockable = new ArrayList<Coordinate>();
        // add initial list
        blockable.addAll(attack_path.get(0));
        // Iterate through the attack paths to find the intersection
        for (int i = 1; i < attack_path.size(); i++) {
            ArrayList<Coordinate> currentPath = attack_path.get(i);
            blockable.retainAll(currentPath); // Retain only the common elements
        }
        if (blockable.isEmpty()) {
            // no common elements -> no one piece can block all -> checkmate
            return true;
        }
        for (int i = 0; i < attack_path.size(); i++) {
            attack_path.get(i).remove(king);
        }
        // loop through all pieces
        for (Coordinate square : board.pieces.keySet()) {
            Piece piece = board.pieces.get(square);
            if (piece == null) { // -> there is an actual piece
                continue;
            }
            if (piece.getColor() != this) {
                continue;
            }
            for (Coordinate position : blockable) {
                if (piece.move(square, position)) {
                    return false; // piece can block all paths at once
                }
            }

        }
        // no piece can block all attacks at once
        return true;
    }
}
